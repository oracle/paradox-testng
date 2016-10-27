package com.webtrends.qa.testng

import groovy.json.JsonOutput
import org.testng.IMethodInstance
import org.testng.IMethodInterceptor
import org.testng.ITestContext
import org.testng.annotations.DataProvider
import org.testng.annotations.Test

/**
 * This interceptor builds up a list of tests that would run without actually running them
 * The @BeforeMethod and @BeforeClass methods are NOT run, but the @BeforeSuite, @BeforeTest and constructors still run.
 * If data providers rely on the @BeforeClass methods getting called, they won't work.  Tests are sorted alphabetically.
 */
class DryRunListener implements IMethodInterceptor {
    static final String RESULT_FILE_NAME = 'tests.json'

    @Override
    List<IMethodInstance> intercept(List<IMethodInstance> methods, ITestContext context) {
        def tests = methods.collectMany { method ->
            // Get class name plus any parameters given by factories
            def className = getClassName(method)

            // Handling for data providers. If the method uses one, go invoke it
            def params = getParametersForMethod(method)
            params.collect {
                [
                    name:"${className}.$method.method.methodName(${it.join ", "})",
                    groups: method.method.groups,
                    id: method.method.constructorOrMethod.method.getAnnotation(Jira)?.value()
                ]
            }
        }.sort { a,b -> a.name <=> b.name }

        String outDir = new File(context.outputDirectory).parent
        new File(outDir, RESULT_FILE_NAME).withWriter { it.writeLine JsonOutput.toJson(treeify(tests)) }
        println JsonOutput.toJson(treeify(tests))
        println "$RESULT_FILE_NAME written to $outDir"
        return [] // tells testng - Don't run any methods!
    }

    /*
     * When using factories, there's no way to distinguish instances the factory has created.
     * TestNG has a mechanism whereby you inherit from ITest, and implement a getTestName, which is typically
     * set during a @BeforeMethod call, and is used by reporters. This requires @BeforeMethod actually be called,
     * which we don't want to do from this interceptor. Instead, we ask the test class to implement a property for
     * returning a serialized string called classParameters, which we will append to the canonicalName of the class
     */
    private static String getClassName(IMethodInstance method) {
        def realClass = method.method.realClass
        def canonicalName = realClass.canonicalName
        if (realClass.metaClass.hasProperty(method.instance, 'classParameters')) {
            canonicalName += "($method.instance.classParameters)"
        }

        canonicalName
    }

    /*
     * The interceptor receives the list of methods before they have been exploded for data providers
     */
    private static Object[][] getParametersForMethod(IMethodInstance method) {
        def testAnnotation = method.method.constructorOrMethod.method.getAnnotation(Test)
        Class dataProviderClass = testAnnotation.dataProviderClass()
        if (dataProviderClass == null || dataProviderClass == Object) {
            dataProviderClass = method.method.realClass
        }
        String dataProviderName = testAnnotation.dataProvider()
        def dataProviderMethod = dataProviderClass.methods.find {
            it.getAnnotation(DataProvider)?.name() == dataProviderName
        }
        dataProviderMethod?.invoke(method.instance) ?: [[]]
    }

    /*
     * Takes a list of strings that represent fully qualified method calls in the form:
     * "path.to.package.class(optional, list, of, args).method(optional, list, or, args)"
     * and combines them.
     *
     * treeify(["foo.bar.baz","foo.bar.qux", "foo.blah"]) results in a tree structure akin to
     *  foo
     *  +-bar
     *  | +-baz
     *  | +-qux
     *  +-blah
     */
    private Map<?,?> treeify(def tests) {
        def root = [
            Tests: [],
            TestName: [FullName: null, Name: null],
            ClassName: null,
            MethodName: null,
        ]

        def current
        for (test in tests) {
            current = root
            test.name.tokenize('.').each { namespace ->
                // append namespace to full name, unless full name is null, then just use namespace
                def newFullName = (current.TestName.FullName?.plus('.') ?: '') + namespace

                // look for an existing node with the same full name as what would be created
                def child = current.Tests.find { it.TestName.FullName == newFullName }

                if (!child) {
                    // if we don't find one, create one
                    def tokens = newFullName.tokenize('.')
                    child = [
                        Tests: [],
                        TestName: [FullName: newFullName, Name: tokens[-1]],
                        ClassName: tokens[0..<-1].join('.'),
                        MethodName: tokens[-1].tokenize('(')[0],
                        Categories: [],
                    ]

                    // add it to the list of children for the current node
                    current.Tests += child
                }

                current = child
            }

            current.Id = test.id
            current.Categories = test.groups
        }

        return root
    }
}
