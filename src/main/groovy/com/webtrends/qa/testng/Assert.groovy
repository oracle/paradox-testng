package com.webtrends.qa.testng

/**
 * Adds Known bug functionality
 */
class Assert {
    static final String KNOWN_BUG_MESSAGE = '__KnownBug__Jira ID -'
    static final String PERF_MESSAGE = '__PERF_TEST__:'

    /**
     * Executes the closure as a performance test and exit with an exception
     * @param trials The number of times the closure should execute
     * @param c The closure to execute.  Should return a numeric value
     */
    static void performance(int trials, Closure c) {
        throw new AssertionError("$PERF_MESSAGE ${(0..<trials).collect(c)}")
    }

    /**
     * Assertion when there is a known bug associated with a failure.
     * @param bugID The jira issue id, e.g. ABC-123
     */
    static void knownBug(String bugId) {
        throw new AssertionError("$KNOWN_BUG_MESSAGE $bugId")
    }
}
