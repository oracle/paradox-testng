package com.webtrends.qa.testng

/**
 * We have decided, that as a convention, asserts needed when setting up a test should differ from the asserts that make
 * up the purpose of the test. To reify this concept, we have this Assume wrapper which we can use to wrap assertions.
 * An assertion that is wrapped with assume is put into a special format that can be recognized by reporting mechanisms
 * downstream.
 */
@SuppressWarnings('ConstantAssertExpression')
class Assume {
    // The format is broken into pieces like this, because we want to know the length the the prefix in order to align
    // things properly.
    static final String INCONCLUSIVE_FORMAT_PREFIX = 'CANNOT COMPLETE TEST - Reason:'
    static final String INCONCLUSIVE_FORMAT_SUFFIX = 'This is tested elsewhere in test:'

    static that(String validatingTest, Closure assertion) {
        try {
            assertion.call()
        } catch (AssertionError e) {
            def lines = e.message.readLines()
            def space = ' ' * INCONCLUSIVE_FORMAT_PREFIX.length()
            def newMessage = "\n$INCONCLUSIVE_FORMAT_PREFIX ${lines[0]} $INCONCLUSIVE_FORMAT_SUFFIX $validatingTest"
            lines.drop(1).each { newMessage <<= "\n$space $it" }
            assert false : newMessage
        }
    }
}
