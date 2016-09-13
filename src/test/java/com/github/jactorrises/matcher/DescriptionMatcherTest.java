package com.github.jactorrises.matcher;

import static com.github.jactorrises.matcher.DescriptionMatcher.is;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.Assert.assertThat;
import static org.junit.rules.ExpectedException.none;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class DescriptionMatcherTest {

    @Rule
    public ExpectedException expectedException = none();

    @Test
    public void shouldHaveDescriptionStoredInToStringMethod() {
        assertThat(new DescriptionMatcher<>(equalTo(new Object()), "the object to match").toString(), containsString("the object to match is <java.lang.Object@"));
    }

    @Test
    public void shouldBeAbleToUseDescriptionOnRegularAssertThat() {
        expectedException.expect(AssertionError.class);
        expectedException.expectMessage(allOf(containsString("string match"), containsString("is \"guava\"")));

        assertThat("java", is(equalTo("guava"), "string match"));
    }
}
