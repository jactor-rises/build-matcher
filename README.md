# build-matcher

### Main goal of project
How to do many matches using only one assertThat in a junit-test
* `TypeSafeBuildMatcher`
* `LambdaBuildMatcher`

#### How it works
When asserting with `TypeSafeBuildMatcher` or `LambdaBuildMatcher`, you make matches on one single type (one logical concept) and build
your matches on this type only. This should help you to keep your unit tests small and easy to read/maintain.

One key point with building matches, is usage of `LabelMatcher` to differentiate between matches being done since all failing tests will
be shown when a failure occurs in a test. Therefore `LabelMatcher.is(<Matcher>, <label>)` must be given when building a match.

Since there is a possibility that the test code will produce an exception, the exception will be caught and an `AssertionError`
containing failure messages of any failed tests will be thrown. 

### Example of usage

    @Test
    public void shouldUseLambdaExpressionWithBuildMatcher() {
        expectedException.expect(AssertionError.class);
        expectedException.expectMessage(allOf(containsString("Quotes from song"), containsString("every step you take"), containsString("every move you make")));

        assertThat("I'll be watching you", verify("Quotes from song", (string, matchBuilder) -> matchBuilder
                .matches(string, is(equalTo("every step you take"), "quote one"))
                .matches(string, is(equalTo("every move you make"), "quote two"))
        ));
    }

    private final String song1 = "Human Behaviour";
    private final String song2 = "Possibly Maybe";

    @Test
    public void shouldUseLambdaExpressionWithBuildMatcherAndCustomizedToString() {
        expectedException.expect(AssertionError.class);
        expectedException.expectMessage(allOf(
                containsString("song one is \"Space Oddity\" | real: \"Human Behaviour\""),
                containsString("song two is \"Hey You\" | real: \"Possibly Maybe\""),
                not(containsString(this.getClass().getSimpleName())))
        );

        assertThat(this, verify("Song titles", (usageTest, matchBuilder) -> matchBuilder
                .matches(usageTest.song1, is(equalTo("Space Oddity"), "song one"), asString -> usageTest.song1)
                .matches(usageTest.song2, is(equalTo("Hey You"), "song two"), asString -> usageTest.song2)
        ));
    }

    @Test
    public void shouldUseLambdaExpressionWithBuildMatcherAndCustomizedToStringWithoutLabeledMatcher() {
        expectedException.expect(AssertionError.class);
        expectedException.expectMessage(allOf(
                containsString("song one is \"Space Oddity\" | real: \"Human Behaviour\""),
                containsString("song two is \"Hey You\" | real: \"Possibly Maybe\""),
                not(containsString("other.unit.tests.UsageTest")))
        );

        assertThat(this, verify("Song titles", (usageTest, matchBuilder) -> matchBuilder
                .matches(usageTest.song1, equalTo("Space Oddity"), "song one", asString -> usageTest.song1)
                .matches(usageTest.song2, equalTo("Hey You"), "song two", asString -> usageTest.song2)
        ));
    }

    @Test
    public void shouldDisplayStackTraceElementForEachMatch() {
        expectedException.expect(AssertionError.class);
        expectedException.expectMessage(allOf(
                containsString("UsageTest.java:183"),
                containsString("UsageTest.java:184")
        ));

        assertThat(this, verify("Showing stack trace element", (test, buildMatcher) -> buildMatcher
                .matches(test, nullValue(), "test failure one")
                .matches(test, nullValue(), "test failure two")
                .matches(test, notNullValue(), "match will not fail")
        ));
    }

### Acknowledgements
This code is build on top of [hamcrest](https://github.com/hamcrest/JavaHamcrest), specifically: **`org.hamcrest.TypeSafeMatcher`** and **`org.hamcrest.core.Is`**

### License
      Apache License, Version 2.0
      http://www.apache.org/licenses/LICENSE-2.0.txt


### Side effects
* `EqualsMatcher` - tests the implementation of the equals method according to the java specification
* `HashCodeMatcher` - tests the implementation of the hashCode method according to the java specification
* `LabelMatcher` - extends `org.hamcrest.core.Is` and provides labeling of standard `org.hamcrest.Matcher` failure messages.
* `ToStringEditor` - provides customized toString messages of evaluated objects

### No longer supported

As of junit 5, the main purpose of this project is not needed anymore:


    @Test
    void groupedAssertions() {
        // In a grouped assertion all assertions are executed, and any
        // failures will be reported together.
        assertAll("address",
            () -> assertEquals("John", address.getFirstName()),
            () -> assertEquals("User", address.getLastName())
        );
    }


### Releases

version | java version | description
---|---|---
v2.2 | 1.8 | stack trace element of match failure is displayed in error message
v2.1.0 | 1.8 | matching with the MatchBuilder contains a match numbering
v2.0.1 | 1.8 | removed use of deprecated code
v2.0 | 1.8 | the project has an apache v 2.0 license to satisfy open source projects deployed on maven central 
v1.2.4 | 1.8 | method LambdaBuildMatcher.verify can also be used without LabelMatcher as long as "label" (String) is provided and removed dependency of apache.lang...
v1.2.3 | 1.8 | method LambdaBuildMatcher.build has been deprecated. Use LambdaBuildMatcher.verify
v1.2.2 | 1.8 | EqualsMatcher and HashCodeMatcher uses a LambdaBuildMatcher + bug fix
v1.2.1 | 1.8 | the abstract method `matches` on `TypeSafeBuildMatcher` may throw any `Exception`
v1.2 | 1.8 | introduced `LambdaBuildMatcher` in order to use `TypeSafeBuildMatcher` as lambda expression without having to initialize a new anonymous class
v1.1 | 1.8 | converted `ToStringEditor` to a functional interface to be used with lambda expression
v1.0 | 1.5 and greater | first release containing `TypeSafeBuildMatcher`, `EqualsMatcher`, `HashCodeMatcher`, `LabelMatcher`, and `ToStringEditor`
