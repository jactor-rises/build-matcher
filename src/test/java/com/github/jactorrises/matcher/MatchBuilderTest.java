package com.github.jactorrises.matcher;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.github.jactorrises.matcher.LabelMatcher.is;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MatchBuilderTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldMatchWhenHamcrestMatchersIsSatisfied() {
        Object obj = new Object();

        assertTrue(new MatchBuilder("An java.lang.Object is not equal to another instance of java.lang.Object")
                .matches(obj, is(not(equalTo(new Object())), "instances"))
                .isMatch()
        );
    }

    @Test
    public void shouldAddMatchLabelsToExceptionMessage() {
        expectedException.expectMessage("hashcode is");
        expectedException.expectMessage("equality is");

        Object obj = new Object();

        assertTrue(new MatchBuilder("Exception message")
                .matches(obj.hashCode(), is(equalTo(0), "hashcode"))
                .matches(obj, is(equalTo(new Object()), "equality"))
                .isMatch()
        );
    }

    @Test
    public void shouldNotFailWhenVerifyArgumentWithMockitoWithCorrectValues() {
        Argument argument = new Argument();
        argument.a = 1;
        argument.b = 2;

        MatchBuilderTest mockedMatchBuilderTest = mock(MatchBuilderTest.class);
        mockedMatchBuilderTest.verifyArgumentGiven(argument);

        verify(mockedMatchBuilderTest).verifyArgumentGiven(argThat(new TypeSafeMatcher<Argument>() {
            @Override
            protected boolean matchesSafely(Argument item) {
                return new MatchBuilder()
                        .matches(item.a, is(equalTo(1), "arg a"))
                        .matches(item.b, is(equalTo(2), "arg b"))
                        .isMatch();
            }

            @Override
            public void describeTo(Description description) {
            }
        }));
    }

    @Test
    public void shouldFailWithExpectedMessageWhenVerifyingWithMockitoAndArgumentIsNotExpected() {
        expectedException.expect(AssertionError.class);
        expectedException.expectMessage("- b is <2> | real: <1>");

        Argument argument = new Argument();
        argument.a = 1;
        argument.b = 1;

        MatchBuilderTest mockedMatchBuilderTest = mock(MatchBuilderTest.class);
        mockedMatchBuilderTest.verifyArgumentGiven(argument);

        verify(mockedMatchBuilderTest).verifyArgumentGiven(argThat(new TypeSafeMatcher<Argument>() {
            @Override
            protected boolean matchesSafely(Argument item) {
                return new MatchBuilder()
                        .matches(item.a, is(equalTo(1), "a"))
                        .matches(item.b, is(equalTo(2), "b"))
                        .isMatch();
            }

            @Override
            public void describeTo(Description description) {
            }
        }));
    }

    void verifyArgumentGiven(@SuppressWarnings("unused") Argument argument) {
        // mocked by mockito
    }

    @Test
    public void shouldDifferentiateNumberTypesVerified() {
        expectedException.expect(AssertionError.class);
        expectedException.expectMessage("<1>");
        expectedException.expectMessage("1L");

        assertThat(1L, new TypeSafeBuildMatcher<Long>("correct number markings") {
            @Override
            public MatchBuilder matches(Long typeToTest, MatchBuilder matchBuilder) {
                return matchBuilder.matches(typeToTest, is(equalTo(1), "integer vs long"));
            }
        });
    }

    @Test
    public void shouldNotAddQuotesOnMatches() {
        expectedException.expect(AssertionError.class);
        expectedException.expectMessage("1L");
        expectedException.expectMessage(not("\"is "));

        assertThat(1, new TypeSafeBuildMatcher<Integer>("No quotes on matches") {
            @Override
            public MatchBuilder matches(Integer typeToTest, MatchBuilder matchBuilder) {
                return matchBuilder.matches(typeToTest, is(equalTo(1L), "integer vs long"));
            }
        });
    }

    @Test
    public void shouldOfferCustomizedToStringDescription() {
        expectedException.expect(AssertionError.class);
        expectedException.expectMessage("value=");
        expectedException.expectMessage("Is ObjectWithoutToString");
        expectedException.expectMessage(not("ObjectWithoutToString@"));

        assertThat(new ObjectWithoutToString(), new TypeSafeBuildMatcher<ObjectWithoutToString>("customized toString on matched object") {
            @Override
            public MatchBuilder matches(ObjectWithoutToString typeToTest, MatchBuilder matchBuilder) {
                return matchBuilder.matches(typeToTest, is(equalTo(new ObjectWithoutToString()), "two different counted values"), arg -> "value=" + arg.value);
            }
        });
    }

    private class Argument {
        int a;
        int b;

        @Override
        public String toString() {
            return "a=" + a + " og b=" + b;
        }
    }

    private static class ObjectWithoutToString {
        static int counter;
        int value = ++counter;
    }
}
