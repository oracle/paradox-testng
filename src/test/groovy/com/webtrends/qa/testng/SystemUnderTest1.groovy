package com.webtrends.qa.testng

import groovy.util.logging.Log4j2
import org.testng.annotations.BeforeClass
import org.testng.annotations.BeforeGroups
import org.testng.annotations.BeforeMethod
import org.testng.annotations.BeforeSuite
import org.testng.annotations.BeforeTest
import org.testng.annotations.Factory
import org.testng.annotations.DataProvider
import org.testng.annotations.Test

import java.lang.reflect.Method

/**
 * Test cases for WTTestng plugin
 */
@Log4j2
class SystemUnderTest1 {
    String classParameters = null

    @Factory(dataProvider = 'external', dataProviderClass = ExternalDataProvider)
    SystemUnderTest1(String ctorString) {
        classParameters = [ctorString].join(', ')
    }

    @BeforeClass
    void beforeClass() {
        log.debug 'this runs before every class'
    }

    @BeforeTest
    void beforeTest() {
        log.debug 'this runs before every test'
    }

    @BeforeGroups(groups = ['g1'])
    void beforeGroups() {
        log.debug 'this runs before every Group'
    }

    @BeforeSuite
    void beforeSuite() {
        log.debug 'this runs before every suite'
    }

    @BeforeMethod
    void beforeMethod(Method method, Object [] data) {
        log.debug "this runs before every method $data, $method"
    }

    @Test
    @SuppressWarnings('ConstantAssertExpression') // Used to validate assertion failing
    void testFailures() {
        assert false && 'This test fails'
    }

    @Test(groups = ['g1'])
    @SuppressWarnings('ConstantAssertExpression') // Used to validate assertion passing
    void testPass() {
        assert 'This test passes'
    }

    @Test(enabled = false)
    void testDisabled() {
        assert false && 'This test should be disabled'
    }

    @Test(dependsOnMethods = 'testPass')
    void testDependsOn() {
        assert false && 'This test depends on testPass'
    }

    @Test
    @SuppressWarnings('ThrowException') // Used to validate exceptions thrown
    void testException() {
        throw new Exception('This test throws an uncaught exception')
    }

    @DataProvider(name = 'aDataProvider')
    static Object[][] dataProviderMethod() { [
        ['string1'],
        ['string2'],
    ] }

    @Test(dataProvider = 'aDataProvider')
    void testDataProvider(String param) {
        assert param != null
    }

    @Test(dataProvider = 'aDataProvider', dataProviderClass = SystemUnderTest1)
    void testDataProviderWithClass(String param) {
        assert param != null
    }

    @Test(dataProvider = 'external', dataProviderClass = ExternalDataProvider)
    void testDataProviderWithExternalClass(String param) {
        assert param != null
    }

    @Test
    void testKnownBug() {
        Assert.knownBug('ABC-123')
    }

    @Test
    void testPerformance() {
        int i = 0
        Assert.performance(5) {
            i++
        }
    }
}

class ExternalDataProvider {
    @DataProvider(name = 'external')
    static Object[][] dataProviderMethod() { [
        ['string4'],
        ['string5'],
    ] }
}

class AnotherSuite {
    @Test
    @SuppressWarnings('ConstantAssertExpression') // Used to validate assertion psasing
    void testPass() {
        assert 'This test passes'
    }
}

@Log4j2
class SuiteThatLogs {
    @Test
    void testLogMessage() {
        log.info 'Look at me'
    }

    @Test
    void testPrintLine() {
        log.debug 'I should not appear'
    }
}
