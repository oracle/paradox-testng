# testng
This project provides a functionality to write testng tests better.

## Assert

Assert.knownBug should be called in favor of `assert false` in any test case
that has a known bug associated with it.  It should be called at the top of the
test.  The method will contact the JIRA server and lookup the bug with ID of
bugID eg. "ENG-999".  Any failure by Assert.KnownBug will prepend
"__KnownFail__" to the message. If this message is seen on a test
case result we're about to put into database we remove "__KnownFail__" from
the message and override result from "fail" to "known fail".

## Assume

Assume is used when it is necessary to ascertain whether the test is in the
appropriate state or not.  If setting up the test fails, Assume should be used
instead of assert.

## CommonFormatReporter

Produces a json result from a test suite that is
compatible with how we expect to import test results into our results database.
It has top-level properties for name, environment, date, time, and the
commandline arguments used to invoke the testcase.  For each testcase, the name,
and state are reported.  If the test failed, the stacktrace is put into a
comment field.  If there is a known bug associated to the test case, its id is
put into a defect field.  If the testcase is assigned groups, those groups are
put into an array and given in the labels field.

## DryRunListener

Creates a file (tests.json) that lists all the tests that would be run given the
commandline options.  Does not actually run the tests.

## TestNGReportAppender 

Writes log4j messages to a testng reporter to show up in the results.
