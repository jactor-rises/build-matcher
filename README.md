# build-matcher

### Main goal of project
How to do many matches using only one assertThat in a junit-test
* `TypeSafeBuildMatcher`
* `LambdaBuildMatcher`

### Example of usage

    @Test
    public void shouldUseLambdaExpressionWithBuildMatcher() {
        expectedException.expect(AssertionError.class);
        expectedException.expectMessage(allOf(containsString("Quotes from song"), containsString("every step you take"), containsString("every move you make")));

        assertThat("I'll be watching you", build("Quotes from song", (string, buildMatcher) -> buildMatcher
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

        assertThat(new UsageTest(), build("Song titles", (usageTest, buildMatcher) -> buildMatcher
                .matches(usageTest.song1, is(equalTo("Space Oddity"), "song one"), asString -> usageTest.song1)
                .matches(usageTest.song2, is(equalTo("Hey You"), "song two"), asString -> usageTest.song2)
        ));
    }


### Acknowledgements
This code is build on top of hamcrest matchers, specifically: **`org.hamcrest.TypeSafeMatcher`** and **`org.hamcrest.core.Is`**
When jdk1.9 is released, the new framwork from `org.unit`

### Side effects
* `EqualsMatcher` - tests the implementation of the equals method according to the java specification
* `HashCodeMatcher` - tests the implementation of the hashCode method according to the java specification
* `LabelMatcher` - extends `org.hamcrest.core.Is` and provides labeling of standard `org.hamcrest.Matcher` failure messages.
* `ToStringEditor` - provides customized toString messages of evaluated objects

### Releases

version | java version | description
---|---|---
v1.2 | 1.8 and greater | introduced `LambdaBuildMatcher` in order to use `TypeSafeBuildMatcher` as lambda expression without having to initialize a new anonymous class
v1.1 | 1.8 and greater | converted `ToStringEditor` to a functional interface to be used with lambda expression
v1.0 | 1.5 and greater | first release containing `TypeSafeBuildMatcher`, `EqualsMatcher`, `HashCodeMatcher`, `LabelMatcher`, and `ToStringEditor`
