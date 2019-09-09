package com.webtrends.qa.testng

import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import groovy.util.logging.Log4j2
import org.testng.IReporter
import org.testng.ISuite
import org.testng.ITestResult
import org.testng.xml.XmlSuite

import java.time.Instant
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

/**
 * This reporter produces a json version of the results
 */
@Log4j2
class CommonFormatReporter implements IReporter {
    static final String KNOWN_BUG_FORMAT = "${Assert.KNOWN_BUG_MESSAGE} ([A-Z]+-[0-9]+)"
    static final String PERFORMANCE_FORMAT = "${Assert.PERF_MESSAGE} (.*)"
    static final String RESULT_FILE_NAME = 'testResults.json'

    // These get populated by the commandline option.  For example:
    // -reporter 'com.webtrends.qa.testng.CommonFormatReporter:name=foo,environment=prod,commandline=-testjar foo.jar'
    String name
    String environment
    String commandline

    /**
     * Generate a report for the given suites into the specified output directory.
     */
    @Override
    void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
        log.info 'generating a report'
        def date = null // is populated by the date the earliest test was run
        def time = null
        def tests = suites.collectMany { suite ->
            suite.results.values().collectMany { suiteResult ->
                def tc = suiteResult.testContext
                List<ITestResult> results =
                    [tc.passedTests,
                     tc.failedTests,
                     tc.skippedTests,
                     tc.failedButWithinSuccessPercentageTests]*.allResults.flatten()
                results = results.findAll().sort { buildName(it) } //.findAll() filters out nulls and []'s
                log.debug 'results = ' + results.collect { "$it.name ${buildState(it)}\n" }
                log.debug "results.size() = ${results.size()}"
                if (!date) {

                    def datetime = ZonedDateTime.ofInstant(
                            Instant.ofEpochMilli([results[0]?.startMillis][0]),
                            ZoneOffset.UTC
                    )
                    date = datetime.format(DateTimeFormatter.ofPattern('yyyy-MM-dd'))
                    time = datetime.format(DateTimeFormatter.ofPattern('hh:mm:ss'))
                }
                results.collect { result ->
                    [
                        name: buildName(result),
                        labels: buildLabels(result),
                        state: buildState(result),
                        performance: buildPerformance(result),
                        comment: buildComment(result),
                        defect: buildDefect(result)
                    ]
                }.unique(false)
            }
        }

        new File(outputDirectory, RESULT_FILE_NAME).withWriter {
            it.writeLine JsonOutput.toJson([
                name: name,
                environment: environment,
                date: date,
                time: time,
                commandline: commandline,
                tests: tests
            ])
        }

        log.debug "$RESULT_FILE_NAME written to $outputDirectory"
    }

    /*
     * appends the comma-joined parameters surrounded by parentheses to the test class name
     */
    private static String buildName(ITestResult result) {
        def testName = result.testClass.testName ?: result.testClass.name
        "$testName.${result.method.methodName}(${result.parameters*.toString().join(',')})"
    }

    /*
     * Gets the labels to add to the test cases
     */
    private static String[] buildLabels(ITestResult result) {
        result.method.groups
    }

    /*
     * Transforms an ITestResult to the enum values specified by the the common format
     */
    private static String buildState(result) {
        switch (result.status) {
            case ITestResult.SUCCESS:
            case ITestResult.SUCCESS_PERCENTAGE_FAILURE:
                return 'PASS'
            case ITestResult.FAILURE:
                return buildDefect(result) ? 'KNOWN_FAIL' : 'FAIL'
            case ITestResult.SKIP:
                return 'UNEXECUTED'
            default:
                throw new IllegalArgumentException('result.status')
        }
    }

    /*
     * Formats the exception message and stack trace into a string that can be used as a jira comment
     */
    private static String buildComment(result) {
        def exception = result.throwable
        if (exception == null) {
            return null
        }

        StringWriter sw = new StringWriter()
        PrintWriter pw = new PrintWriter(sw)
        exception.printStackTrace(pw)
        sw.buffer.toString()
    }

    /*
     * Searches for the KnownBug string and parses out the jira id
     */
    private static String buildDefect(result) {
        (result.throwable?.message =~ KNOWN_BUG_FORMAT)*.getAt(1)[0]
    }

    /*
     * Searches for the performance string and parses out the performance array
     */
    private static buildPerformance(result) {
        new JsonSlurper().parseText((result.throwable?.message =~ PERFORMANCE_FORMAT)*.getAt(1)[0] ?: 'null')
    }
}
