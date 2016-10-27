package com.webtrends.qa.testng

import groovy.json.JsonSlurper
import org.testng.TestNG
import org.testng.annotations.Test

class DryRunListenerTests {
    /*
     * Verifies the DryRunListener creates a report that lines up with the format expected by a plugin
     */
    @Test
    void basicReport() {
        // Arrange
        // Shim File.withWriter to write to a StringBuilder instead of a file
        def sb = new StringBuilder()
        File.metaClass.constructor << { parent, fileName ->
            [withWriter: {
                it([writeLine: sb.&append])
            }]
        }

        // Act
        new TestNG(
            useDefaultListeners: false,
            verbose: 0,
            testClasses: [SystemUnderTest1.class, AnotherSuite.class],
            listenerClasses: [DryRunListener.class],
        ).run()

        // Assert
        def actual = new JsonSlurper().parseText(sb.toString())
        def expected = getExpected()
        assert actual == expected
    }

    private static Object getExpected() {
        [
            ClassName: null,
            MethodName: null,
            TestName: [FullName: null, Name: null],
            Tests: [
                [
                    Categories: [],
                    ClassName: '',
                    MethodName: 'com',
                    TestName: [FullName: 'com', Name: 'com'],
                    Tests: [
                        [
                            Categories: [],
                            ClassName: 'com',
                            MethodName: 'webtrends',
                            TestName: [FullName: 'com.webtrends', Name: 'webtrends'],
                            Tests: [
                                [
                                    Categories: [],
                                    ClassName: 'com.webtrends',
                                    MethodName: 'qa',
                                    TestName: [FullName: 'com.webtrends.qa', Name: 'qa'],
                                    Tests: [
                                        [
                                            Categories: [],
                                            ClassName: 'com.webtrends.qa',
                                            MethodName: 'testng',
                                            TestName: [FullName: 'com.webtrends.qa.testng', Name: 'testng'],
                                            Tests: [
                                                [
                                                    Categories: [],
                                                    ClassName: 'com.webtrends.qa.testng',
                                                    MethodName: 'AnotherSuite',
                                                    TestName: [FullName: 'com.webtrends.qa.testng.AnotherSuite', Name: 'AnotherSuite'],
                                                    Tests: [
                                                        [
                                                            Categories: [],
                                                            ClassName: 'com.webtrends.qa.testng.AnotherSuite',
                                                            MethodName: 'testPass',
                                                            TestName: [FullName: 'com.webtrends.qa.testng.AnotherSuite.testPass()', Name: 'testPass()'],
                                                            Tests: []
                                                        ]
                                                    ]
                                                ],
                                                [
                                                    Categories: [],
                                                    ClassName: 'com.webtrends.qa.testng',
                                                    MethodName: 'SystemUnderTest1',
                                                    TestName: [FullName: 'com.webtrends.qa.testng.SystemUnderTest1(string4)', Name: 'SystemUnderTest1(string4)'],
                                                    Tests: [
                                                        [
                                                            Categories: [],
                                                            ClassName: 'com.webtrends.qa.testng.SystemUnderTest1(string4)',
                                                            MethodName: 'testDataProvider',
                                                            TestName: [FullName: 'com.webtrends.qa.testng.SystemUnderTest1(string4).testDataProvider(string1)', Name: 'testDataProvider(string1)'],
                                                            Tests: []
                                                        ],
                                                        [
                                                            Categories: [],
                                                            ClassName: 'com.webtrends.qa.testng.SystemUnderTest1(string4)',
                                                            MethodName: 'testDataProvider',
                                                            TestName: [FullName: 'com.webtrends.qa.testng.SystemUnderTest1(string4).testDataProvider(string2)', Name: 'testDataProvider(string2)'],
                                                            Tests: []
                                                        ],
                                                        [
                                                            Categories: [],
                                                            ClassName: 'com.webtrends.qa.testng.SystemUnderTest1(string4)',
                                                            MethodName: 'testDataProviderWithClass',
                                                            TestName: [FullName: 'com.webtrends.qa.testng.SystemUnderTest1(string4).testDataProviderWithClass(string1)', Name: 'testDataProviderWithClass(string1)'],
                                                            Tests: []
                                                        ],
                                                        [
                                                            Categories: [],
                                                            ClassName: 'com.webtrends.qa.testng.SystemUnderTest1(string4)',
                                                            MethodName: 'testDataProviderWithClass',
                                                            TestName: [FullName: 'com.webtrends.qa.testng.SystemUnderTest1(string4).testDataProviderWithClass(string2)', Name: 'testDataProviderWithClass(string2)'],
                                                            Tests: []
                                                        ],
                                                        [
                                                            Categories: [],
                                                            ClassName: 'com.webtrends.qa.testng.SystemUnderTest1(string4)',
                                                            MethodName: 'testDataProviderWithExternalClass',
                                                            TestName: [FullName: 'com.webtrends.qa.testng.SystemUnderTest1(string4).testDataProviderWithExternalClass(string4)', Name: 'testDataProviderWithExternalClass(string4)'],
                                                            Tests: []
                                                        ],
                                                        [
                                                            Categories: [],
                                                            ClassName: 'com.webtrends.qa.testng.SystemUnderTest1(string4)',
                                                            MethodName: 'testDataProviderWithExternalClass',
                                                            TestName: [FullName: 'com.webtrends.qa.testng.SystemUnderTest1(string4).testDataProviderWithExternalClass(string5)', Name: 'testDataProviderWithExternalClass(string5)'],
                                                            Tests: []
                                                        ],
                                                        [
                                                            Categories: [],
                                                            ClassName: 'com.webtrends.qa.testng.SystemUnderTest1(string4)',
                                                            MethodName: 'testDependsOn',
                                                            TestName: [FullName: 'com.webtrends.qa.testng.SystemUnderTest1(string4).testDependsOn()', Name: 'testDependsOn()'],
                                                            Tests: []
                                                        ],
                                                        [
                                                            Categories: [],
                                                            ClassName: 'com.webtrends.qa.testng.SystemUnderTest1(string4)',
                                                            MethodName: 'testException',
                                                            TestName: [FullName: 'com.webtrends.qa.testng.SystemUnderTest1(string4).testException()', Name: 'testException()'],
                                                            Tests: []
                                                        ],
                                                        [
                                                            Categories: [],
                                                            ClassName: 'com.webtrends.qa.testng.SystemUnderTest1(string4)',
                                                            MethodName: 'testFailures',
                                                            TestName: [FullName: 'com.webtrends.qa.testng.SystemUnderTest1(string4).testFailures()', Name: 'testFailures()'],
                                                            Tests: []
                                                        ],
                                                        [
                                                            Categories: ['g1'],
                                                            ClassName: 'com.webtrends.qa.testng.SystemUnderTest1(string4)',
                                                            MethodName: 'testPass',
                                                            TestName: [FullName: 'com.webtrends.qa.testng.SystemUnderTest1(string4).testPass()', Name: 'testPass()'],
                                                            Tests: []
                                                        ]
                                                    ]
                                                ],
                                                [
                                                    Categories: [],
                                                    ClassName: 'com.webtrends.qa.testng',
                                                    MethodName: 'SystemUnderTest1',
                                                    TestName: [FullName: 'com.webtrends.qa.testng.SystemUnderTest1(string5)', Name: 'SystemUnderTest1(string5)'],
                                                    Tests: [
                                                        [
                                                            Categories: [],
                                                            ClassName: 'com.webtrends.qa.testng.SystemUnderTest1(string5)',
                                                            MethodName: 'testDataProvider',
                                                            TestName: [FullName: 'com.webtrends.qa.testng.SystemUnderTest1(string5).testDataProvider(string1)', Name: 'testDataProvider(string1)'],
                                                            Tests: []
                                                        ],
                                                        [
                                                            Categories: [],
                                                            ClassName: 'com.webtrends.qa.testng.SystemUnderTest1(string5)',
                                                            MethodName: 'testDataProvider',
                                                            TestName: [FullName: 'com.webtrends.qa.testng.SystemUnderTest1(string5).testDataProvider(string2)', Name: 'testDataProvider(string2)'],
                                                            Tests: []
                                                        ],
                                                        [
                                                            Categories: [],
                                                            ClassName: 'com.webtrends.qa.testng.SystemUnderTest1(string5)',
                                                            MethodName: 'testDataProviderWithClass',
                                                            TestName: [FullName: 'com.webtrends.qa.testng.SystemUnderTest1(string5).testDataProviderWithClass(string1)', Name: 'testDataProviderWithClass(string1)'],
                                                            Tests: []
                                                        ],
                                                        [
                                                            Categories: [],
                                                            ClassName: 'com.webtrends.qa.testng.SystemUnderTest1(string5)',
                                                            MethodName: 'testDataProviderWithClass',
                                                            TestName: [FullName: 'com.webtrends.qa.testng.SystemUnderTest1(string5).testDataProviderWithClass(string2)', Name: 'testDataProviderWithClass(string2)'],
                                                            Tests: []
                                                        ],
                                                        [
                                                            Categories: [],
                                                            ClassName: 'com.webtrends.qa.testng.SystemUnderTest1(string5)',
                                                            MethodName: 'testDataProviderWithExternalClass',
                                                            TestName: [FullName: 'com.webtrends.qa.testng.SystemUnderTest1(string5).testDataProviderWithExternalClass(string4)', Name: 'testDataProviderWithExternalClass(string4)'],
                                                            Tests: []
                                                        ],
                                                        [
                                                            Categories: [],
                                                            ClassName: 'com.webtrends.qa.testng.SystemUnderTest1(string5)',
                                                            MethodName: 'testDataProviderWithExternalClass',
                                                            TestName: [FullName: 'com.webtrends.qa.testng.SystemUnderTest1(string5).testDataProviderWithExternalClass(string5)', Name: 'testDataProviderWithExternalClass(string5)'],
                                                            Tests: []
                                                        ],
                                                        [
                                                            Categories: [],
                                                            ClassName: 'com.webtrends.qa.testng.SystemUnderTest1(string5)',
                                                            MethodName: 'testDependsOn',
                                                            TestName: [FullName: 'com.webtrends.qa.testng.SystemUnderTest1(string5).testDependsOn()', Name: 'testDependsOn()'],
                                                            Tests: []
                                                        ],
                                                        [
                                                            Categories: [],
                                                            ClassName: 'com.webtrends.qa.testng.SystemUnderTest1(string5)',
                                                            MethodName: 'testException',
                                                            TestName: [FullName: 'com.webtrends.qa.testng.SystemUnderTest1(string5).testException()', Name: 'testException()'],
                                                            Tests: []
                                                        ],
                                                        [
                                                            Categories: [],
                                                            ClassName: 'com.webtrends.qa.testng.SystemUnderTest1(string5)',
                                                            MethodName: 'testFailures',
                                                            TestName: [FullName: 'com.webtrends.qa.testng.SystemUnderTest1(string5).testFailures()', Name: 'testFailures()'],
                                                            Tests: []
                                                        ],
                                                        [
                                                            Categories: ['g1'],
                                                            ClassName: 'com.webtrends.qa.testng.SystemUnderTest1(string5)',
                                                            MethodName: 'testPass',
                                                            TestName: [FullName: 'com.webtrends.qa.testng.SystemUnderTest1(string5).testPass()', Name: 'testPass()'],
                                                            Tests: []
                                                        ]
                                                    ]
                                                ]
                                            ]
                                        ]
                                    ]
                                ]
                            ]
                        ]
                    ]
                ]
            ]
        ]
    }
}
