package com.github.jactorrises.matcher;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.github.jactorrises.matcher.DescriptionMatcher.is;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class TypeSafeBuildMatcherTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldVerifyWithTypeSafeBuildMatcher() {
        assertThat(true, new TypeSafeBuildMatcher<Boolean>("should be true") {
            @Override
            public MatchBuilder matches(Boolean item, MatchBuilder matchBuilder) {
                return matchBuilder.matches(item, is(equalTo(true), "boolean match"));
            }
        });
    }

    @Test
    public void shouldNotVerifyWithTypeSafeBuildMatcher() {
        expectedException.expect(AssertionError.class);
        expectedException.expectMessage("should be false");

        assertThat(true, new TypeSafeBuildMatcher<Boolean>("should be false") {
            @Override
            public MatchBuilder matches(Boolean item, MatchBuilder matchBuilder) {
                return matchBuilder.matches(item, is(equalTo(false), "boolean match"));
            }
        });
    }

    @Test
    public void shouldThrowAnAssertionErrorWhenExceptionOccursWithinTheMatcher() {
        expectedException.expect(AssertionError.class);
        expectedException.expectMessage("An exception thrown inside the matcher should only fail the matching");

        assertThat(true, new TypeSafeBuildMatcher<Boolean>("An exception thrown inside the matcher should only fail the matching") {

            @Override
            public MatchBuilder matches(Boolean typeToTest, MatchBuilder matchBuilder) {
                throw new UnsupportedOperationException("Matchinbg som skaper exception");
            }
        });
    }

    @Test
    public void shouldPreserveOriginalExceptionMessageOnTheAssertionErrorWhenExceptionIsThrownWithinMather() {
        expectedException.expect(AssertionError.class);
        expectedException.expectMessage("Exception is thrown during matchin");

        assertThat(true, new TypeSafeBuildMatcher<Boolean>("An exception thrown inside the matcher should only fail the matching") {

            @Override
            public MatchBuilder matches(Boolean typeToTest, MatchBuilder matchBuilder) {
                throw new UnsupportedOperationException("Exception is thrown during matchin");
            }
        });
    }

    @Test
    public void shouldPreserveExceptionClassNameOnTheAssertionErrorWhenExceptionIsThrownWithinMather() {
        expectedException.expect(AssertionError.class);
        expectedException.expectMessage(UnsupportedOperationException.class.getName());

        assertThat(true, new TypeSafeBuildMatcher<Boolean>("An exception thrown inside the matcher should only fail the matching") {

            @Override
            public MatchBuilder matches(Boolean typeToTest, MatchBuilder matchBuilder) {
                throw new UnsupportedOperationException("Exception is thrown during matchin");
            }
        });
    }

    @Test
    public void shouldPreserveAnyEvaluatedMatchesBeforeAnyExceptionOccurs() {
        expectedException.expect(AssertionError.class);
        expectedException.expectMessage("A match failure to show");

        assertThat(true, new TypeSafeBuildMatcher<Boolean>("An exception thrown inside the matcher should only fail the matching") {

            @Override
            public MatchBuilder matches(Boolean typeToTest, MatchBuilder matchBuilder) {
                matchBuilder.matches(true, is(equalTo(false), "A match failure to show"));
                throw new UnsupportedOperationException("An exception arised during matching");
            }
        });
    }
}
