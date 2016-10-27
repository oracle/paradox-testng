package com.webtrends.qa.testng

import groovy.util.logging.Log4j
import org.testng.TestNG
import org.testng.annotations.AfterClass
import org.testng.annotations.AfterMethod
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

@Log4j
class TestNGReportAppenderTests {
    def final tempDir = 'temp'

    @BeforeMethod(alwaysRun = true)
    void setup() {
        new File(tempDir).mkdir()
    }

    @Test(enabled = false)
    void testOnlyLogStatementsAppear() {
        new TestNG(
                useDefaultListeners: true,
                verbose: 0,
                testClasses: [ SuiteThatLogs.class ],
                outputDirectory: tempDir
        ).run()

        def output = new File("$tempDir/index.html").text

        assert output.contains('Look at me')
        assert !output.contains('I should not appear')
    }

    @AfterClass(alwaysRun = true)
    void teardown() {
        //TestNG holds onto a reference to it's outputDirectory,
        //and does not provide an API to release it deterministically.
        System.gc()
        Thread.sleep(100)

        assert new File(tempDir).deleteDir()
    }
}
