package com.webtrends.qa.testng

import org.testng.annotations.Test

/**
 * Test cases for Assume testNG Class
 */
// Testing assertions
@SuppressWarnings([ 'ComparisonOfTwoConstants', 'ConstantAssertExpression' ])
class AssumeTests {
    /*
     * When no message is given in the inner assert, PowerAssert produces a multi-line expression where alignment is
     * important This tests that the alignment holds up.
     */
    @Test
    void testAssumeWithOutMessage() {
        def threwException = false
        try {
            Assume.that('aValidatingTest') { assert 1 == 2 }
        } catch (AssertionError e) {
            threwException = true
            assert e.message == '''
CANNOT COMPLETE TEST - Reason: assert 1 == 2 This is tested elsewhere in test: aValidatingTest
                                        |
                                        false. Expression: false'''
        }
        assert threwException
    }

    /*
     * When a message is given, a different format is used
     */
    @Test
    void testAssumeWithMessage() {
        def threwException = false
        try {
            Assume.that('aValidatingTest') { assert 1 == 2 : 'aMessage' }
        } catch (AssertionError e) {
            threwException = true
            assert e.message == '''
CANNOT COMPLETE TEST - Reason: aMessage. Expression: (1 == 2) This is tested elsewhere in test: aValidatingTest. \
Expression: false'''
        }

        assert threwException
    }

    /*
     * Assume should not generate an exception when the given assert returns true
     */
    @Test
    void testAssumeWithTruthyAssert() {
        Assume.that('aValidatingTest') { assert 1 == 1 : 'aMessage' }
    }

    /*
     * While generally discouraged, passing null as the validating test to Assume should not break things
     */
    @Test
    void testAssumeWithNullValidatingTest() {
        def threwException = false
        try {
            Assume.that(null) { assert 1 == 2 }
        } catch (AssertionError e) {
            threwException = true
            assert e.message == '''
CANNOT COMPLETE TEST - Reason: assert 1 == 2 This is tested elsewhere in test: null
                                        |
                                        false. Expression: false'''
        }

        assert threwException
    }

    /*
     * When an exception is thrown while evaluating the assert, that exception should bubble up.
     */
    @Test
    void testAssumeShouldOnlyCatchAssertionErrors() {
        def threwException = false
        def exception = new Exception('A General Exception')
        try {
            Assume.that('aValidatingTest') { throw exception }
        } catch (AssertionError e) {
            assert false : 'Expected an Exception, not an AssertionError'
        } catch (Exception e) {
            threwException = true
            assert e == exception
        }

        assert threwException
    }
}
