package com.github.jactorrises.matcher;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.github.jactorrises.matcher.LambdaBuildMatcher.verify;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.rules.ExpectedException.none;

public class LambdaBuildMatcherTest {

    @Rule
    public ExpectedException expectedException = none();

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
                .matches(usageTest.song1, LabelMatcher.is(equalTo("Space Oddity"), "song one"), asString -> usageTest.song1)
                .matches(usageTest.song2, LabelMatcher.is(equalTo("Hey You"), "song two"), asString -> usageTest.song2)
        ));
    }

    @Test
    public void shouldAddDynamicExpectation() {
        expectedException.expect(AssertionError.class);
        expectedException.expectMessage(containsString("An error created by " + song1));

        assertThat(this, verify("An error", (test, buildMatcher) -> buildMatcher
                .append(" created by " + test.song1)
                .matches(test, nullValue(), "test") // will fail
        ));
    }
}
