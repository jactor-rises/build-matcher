package com.github.jactorrises.matcher;

import static com.github.jactorrises.matcher.DescriptionMatcher.is;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class DescriptionMatcherTest {

    @Test
    public void shouldHaveDescriptionStoredInToStringMethod() {
        assertThat(new DescriptionMatcher<>(equalTo(new Object()), "the object to match").toString(), containsString("the object to match is <java.lang.Object@"));
    }

    @Test
    public void shouldBeAbleToUseDescriptionOnRegularAssertThat() {
        try {
            assertThat("java", is(equalTo("guava"), "string match"));
        } catch (AssertionError assertionError) {
            assertThat(assertionError.getMessage(), allOf(containsString("string match"), containsString("is \"guava\"")));
        }

    }
}
