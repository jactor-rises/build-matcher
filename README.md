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
                not(containsString("other.unit.tests.UsageTest")))
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

### Acknowledgements
This code is build on top of [hamcrest](https://github.com/hamcrest/JavaHamcrest), specifically: **`org.hamcrest.TypeSafeMatcher`** and 
**`org.hamcrest.core.Is`**

### Side effects
* `EqualsMatcher` - tests the implementation of the equals method according to the java specification
* `HashCodeMatcher` - tests the implementation of the hashCode method according to the java specification
* `LabelMatcher` - extends `org.hamcrest.core.Is` and provides labeling of standard `org.hamcrest.Matcher` failure messages.
* `ToStringEditor` - provides customized toString messages of evaluated objects

### Future
When jdk1.9 is released, a new possibility of doing several asserts in one test will be added when using `org.junit`. How this
implementation is done will affect how this code will evolve.

### Releases

version | java version | description
---|---|---
v1.2.4 | 1.8 and greater | release v1.2.4: minor, method LambdaBuildMatcher.verify can also be used without LabelMatcher as long as "label" (String) is provided and removed dependency of apache.lang...
v1.2.3 | 1.8 and greater | release v1.2.3: minor, method LambdaBuildMatcher.build has been deprecated. Use LambdaBuildMatcher.verify
v1.2.2 | 1.8 and greater | release v1.2.2: minor, EqualsMatcher and HashCodeMatcher uses a LambdaBuildMatcher + bug fix
v1.2.1 | 1.8 and greater | the abstract method `matches` on `TypeSafeBuildMatcher` may throw any `Exception`
v1.2 | 1.8 and greater | introduced `LambdaBuildMatcher` in order to use `TypeSafeBuildMatcher` as lambda expression without having to initialize a new anonymous class
v1.1 | 1.8 and greater | converted `ToStringEditor` to a functional interface to be used with lambda expression
v1.0 | 1.5 and greater | first release containing `TypeSafeBuildMatcher`, `EqualsMatcher`, `HashCodeMatcher`, `LabelMatcher`, and `ToStringEditor`
