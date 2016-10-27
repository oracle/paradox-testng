package com.webtrends.qa.testng

import groovy.util.logging.Log4j
import org.testng.annotations.BeforeClass
import org.testng.annotations.BeforeGroups
import org.testng.annotations.BeforeMethod
import org.testng.annotations.BeforeSuite
import org.testng.annotations.BeforeTest
import org.testng.annotations.Factory
import org.testng.annotations.DataProvider
import org.testng.annotations.Test

import java.lang.reflect.Method

class SystemUnderTest1 {
    String classParameters = null

    @Factory(dataProvider = 'external', dataProviderClass = ExternalDataProvider.class)
    SystemUnderTest1(String ctorString) {
        classParameters = [ctorString].join(', ')
    }

    @BeforeClass
    void beforeClass() {
        println 'this runs before every class'
    }

    @BeforeTest
    void beforeTest() {
        println 'this runs before every test'
    }

    @BeforeGroups(groups = ['g1'])
    void beforeGroups() {
        println 'this runs before every Group'
    }

    @BeforeSuite
    void beforeSuite() {
        println 'this runs before every suite'
    }

    @BeforeMethod
    void beforeMethod(Method method, Object [] data) {
        println "this runs before every method $data, $method"
    }

    @Test
    void testFailures() {
        assert false && 'This test fails'
    }

    @Test(groups = ['g1'])
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
    void testException() {
        throw new Exception('This test throws an uncaught exception')
    }

    @DataProvider(name = 'aDataProvider')
    Object[][] dataProviderMethod(){ [
        ['string1'],
        ['string2'],
    ] }

    @Test(dataProvider = 'aDataProvider')
    void testDataProvider(String param) {
        assert param != null
    }

    @Test(dataProvider = 'aDataProvider', dataProviderClass = SystemUnderTest1.class)
    void testDataProviderWithClass(String param) {
        assert param != null
    }

    @Test(dataProvider = 'external', dataProviderClass = ExternalDataProvider.class)
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
    static Object[][] dataProviderMethod(){ [
        ['string4'],
        ['string5'],
    ] }
}

class AnotherSuite {
    @Test
    void testPass() {
        assert 'This test passes'
    }
}

@Log4j
class SuiteThatLogs {
    @Test
    void testLogMessage() {
        log.info 'Look at me'
    }

    @Test
    void testPrintLine() {
        println 'I should not appear'
    }
}
