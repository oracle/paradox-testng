package com.webtrends.qa.testng

import groovy.json.JsonSlurper
import org.testng.TestNG
import org.testng.annotations.Test

import java.time.LocalDateTime

import static java.time.temporal.ChronoUnit.SECONDS

/**
 * Test cases for CommonFormatReporter
 */
class CommonFormatReporterTests {
    /*
     * Verifies the CommonFormatReporter creates a report that lines up with the Common Test Reporting Format
     */
    @Test
    void basicReport() {
        // Shim File.withWriter to write to a StringBuilder instead of a file
        def sb = new StringBuilder()
        File.metaClass.constructor = { parent, child  ->
            [
                withWriter: { it([writeLine: sb.&append]) }
            ]
        }

        def expectedDate = LocalDateTime.now()
        new TestNG(
            useDefaultListeners: false,
            verbose: 0,
            testClasses: [SystemUnderTest1],
            listenerClasses: [CommonFormatReporter],
        ).run()
        def actual = new JsonSlurper().parseText(sb.toString()) as Map
        actual.tests.each { it.comment = it.comment?.split(/[\r\n]/)?.getAt(0) }
        actual.tests = actual.tests.toSet()

        def expectedTests = new HashSet(
            [
                [
                    comment: null, defect: null, labels: [],
                    name: 'com.webtrends.qa.testng.SystemUnderTest1.testDataProvider(string1)',
                    state: 'PASS', performance: null
                ],
                [
                    comment: null, defect: null, labels: [],
                    name: 'com.webtrends.qa.testng.SystemUnderTest1.testDataProvider(string2)',
                    state: 'PASS', performance: null
                ],
                [
                    comment: null, defect: null, labels: [],
                    name: 'com.webtrends.qa.testng.SystemUnderTest1.testDataProviderWithClass(string1)',
                    state: 'PASS', performance: null
                ],
                [
                    comment: null, defect: null, labels: [],
                    name: 'com.webtrends.qa.testng.SystemUnderTest1.testDataProviderWithClass(string2)',
                    state: 'PASS', performance: null
                ],
                [
                    comment: null, defect: null, labels: ['g1'],
                    name: 'com.webtrends.qa.testng.SystemUnderTest1.testPass()',
                    state: 'PASS', performance: null
                ],
                [
                    comment: 'Assertion failed: ', defect: null, labels: [],
                    name: 'com.webtrends.qa.testng.SystemUnderTest1.testFailures()',
                    state: 'FAIL', performance: null
                ],
                [
                    comment: 'java.lang.Exception: This test throws an uncaught exception', defect: null, labels: [],
                    name: 'com.webtrends.qa.testng.SystemUnderTest1.testException()',
                    state: 'FAIL', performance: null
                ],
                [
                    comment: null, defect: null, labels: [],
                    name: 'com.webtrends.qa.testng.SystemUnderTest1.testDataProviderWithExternalClass(string4)',
                    state: 'PASS', performance: null
                ],
                [
                    comment: null, defect: null, labels: [],
                    name: 'com.webtrends.qa.testng.SystemUnderTest1.testDataProviderWithExternalClass(string5)',
                    state: 'PASS', performance: null
                ],
                [
                    comment: 'java.lang.Exception: This test throws an uncaught exception', defect: null, labels: [],
                    name: 'com.webtrends.qa.testng.SystemUnderTest1.testException()',
                    state: 'FAIL', performance: null
                ],
                [
                    comment: 'Assertion failed: ', defect: null, labels: [],
                    name: 'com.webtrends.qa.testng.SystemUnderTest1.testDependsOn()',
                    state: 'FAIL', performance: null
                ],
                [
                    comment: 'java.lang.AssertionError: __KnownBug__Jira ID - ABC-123', defect: 'ABC-123', labels: [],
                    name: 'com.webtrends.qa.testng.SystemUnderTest1.testKnownBug()',
                    state: 'KNOWN_FAIL', performance: null
                ],
                [
                    comment: 'java.lang.AssertionError: __PERF_TEST__: [0, 1, 2, 3, 4]', defect: null, labels: [],
                    name: 'com.webtrends.qa.testng.SystemUnderTest1.testPerformance()',
                    state: 'FAIL', performance: [0, 1, 2, 3, 4]
                ]
            ]
        )
        def actualDate = LocalDateTime.parse(actual.date + 'T' + actual.time)
        assert SECONDS.between(expectedDate, actualDate) < 5
        assert actual.containsKey('environment')
        assert actual.containsKey('name')
        assert actual.containsKey('commandline')
        assert actual.tests.size() == expectedTests.size()
        assert actual.tests == expectedTests
    }
}
