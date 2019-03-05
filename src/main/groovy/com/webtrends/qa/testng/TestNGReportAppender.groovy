package com.webtrends.qa.testng

import org.apache.log4j.AppenderSkeleton
import org.apache.log4j.spi.LoggingEvent
import org.apache.log4j.Layout
import org.testng.Reporter

/**
 * Writes messages to the testng reporter
 */
class TestNGReportAppender extends AppenderSkeleton implements Closeable {
    @Override
    protected void append(LoggingEvent event) {
        Reporter.log layout.format(event)

        if (layout.ignoresThrowable()) {
            def message = event.throwableStrRep
            if (message) {
                Reporter.log message.join(Layout.LINE_SEP)
            }
        }
    }

    @Override
    void close() { }

    @Override
    boolean requiresLayout() { true }
}
