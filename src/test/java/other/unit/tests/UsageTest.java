package other.unit.tests;

import com.github.jactorrises.matcher.EqualsMatcher;
import com.github.jactorrises.matcher.MatchBuilder;
import com.github.jactorrises.matcher.TypeSafeBuildMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.github.jactorrises.matcher.HashCodeMatcher.hasImplementedHashCodeAccordingTo;
import static com.github.jactorrises.matcher.LabelMatcher.is;
import static com.github.jactorrises.matcher.LambdaBuildMatcher.verify;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.rules.ExpectedException.none;

public class UsageTest {

    @Rule
    public ExpectedException expectedException = none();

    @Test
    public void shouldUseTypeSafeBuildMatcherToMatchMultipleMatchesOnTypeWithSingleAssert() {
        expectedException.expect(AssertionError.class);
        expectedException.expectMessage(
                allOf(containsString("someone is \"bad\" | real: \"good\""), containsString("somewhat is \"different\" | real: \"boring\""))
        );

        assertThat(new TestSeveralMatches(), new TypeSafeBuildMatcher<TestSeveralMatches>("several matches in one assert") {
            @Override
            public MatchBuilder matches(TestSeveralMatches typeToTest, MatchBuilder matchBuilder) {
                return matchBuilder
                        .matches(typeToTest.someone, is(equalTo("bad"), "someone"))
                        .matches(typeToTest.somewhat, is(equalTo("different"), "somewhat"));
            }
        });
    }

    @Test
    public void shouldUseDescriptionMatcherToLabelTheFailingMatch() {
        expectedException.expect(AssertionError.class);
        expectedException.expectMessage(allOf(containsString("Expected: impossible is <2>"), containsString("but: was <1>")));

        assertThat(1, is(equalTo(2), "impossible"));
    }

    @Test
    public void shouldUseEqualsMatcherToVerifyImplementationOfTheEqualMethod() {
        expectedException.expect(AssertionError.class);
        expectedException.expectMessage(containsString("is not sameInstance"));

        String equalBean = "x";
        String unequalBean = "y";
        assertThat(equalBean, EqualsMatcher.hasImplenetedEqualsMethodUsing(equalBean, unequalBean));
    }

    private class TestSeveralMatches {
        String somewhat = "boring";
        String someone = "good";
    }

    @Test
    public void shouldUseHashCodeMatcherToVerifyImplementationOfTheHashCodeMethod() {
        expectedException.expect(AssertionError.class);
        expectedException.expectMessage(allOf(containsString("Two equal objects must have equal hash code"), containsString("The two objects must be equal")));

        TestHashCodeImplementation equalBean = new TestHashCodeImplementation();
        TestHashCodeImplementation unequalBean = new TestHashCodeImplementation();

        assertThat(new TestHashCodeImplementation(), hasImplementedHashCodeAccordingTo(equalBean, unequalBean));
    }

    private class TestHashCodeImplementation {
        @Override
        public int hashCode() {
            return super.hashCode();
        }
    }

    @Test
    public void shouldReceiveAssertionErrorWithFailureMessageWhenUsingMatchBuilderToBuildMatch() {
        expectedException.expect(AssertionError.class);
        expectedException.expectMessage(allOf(containsString("HashCode numerals"), containsString("hashCode is (<101> or <0>) | real: ")));

        Object obj = new Object();

        assertTrue(new MatchBuilder("HashCode numerals").matches(obj.hashCode(), is(anyOf(equalTo(101), equalTo(0)), "hashCode")).isMatch());
    }

    @Test
    public void shouldCreateCustomToStringMessageApplicableToVerification() {
        expectedException.expect(AssertionError.class);
        expectedException.expectMessage(allOf(containsString("- customize is \"something else\""), not(containsString("some.horrible.MemoryReference@123456789"))));

        assertThat("some.horrible.MemoryReference@123456789", new TypeSafeBuildMatcher<String>("custom toString") {
            @Override
            public MatchBuilder matches(String typeToTest, MatchBuilder matchBuilder) {
                return matchBuilder.matches(typeToTest, is(equalTo("something else"), "customize"), type ->
                        type.contains("some.horrible") ? "my customized string" : "not the correct customized string");
            }
        });
    }

    @Test
    public void shouldUseLambdaExpressionWithBuildMatcher() {
        expectedException.expect(AssertionError.class);
        expectedException.expectMessage(allOf(containsString("Quotes from song"), containsString("every step you take"), containsString("every move you make")));

        assertThat("I'll be watching you", verify("Quotes from song", (string, matchBuilder) -> matchBuilder
                .matches(string, containsString("every step you take"), "quote one")
                .matches(string, containsString("every move you make"), "quote two")
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
}
