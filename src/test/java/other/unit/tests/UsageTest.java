package other.unit.tests;

import com.github.jactorrises.matcher.EqualsMatcher;
import com.github.jactorrises.matcher.MatchBuilder;
import com.github.jactorrises.matcher.TypeSafeBuildMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.github.jactorrises.matcher.LabelMatcher.is;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
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
    public void shouldReceiveAssertionErrorAsFailureMessageWhenUsingMatchBuilderToBuildMatch() {
        expectedException.expect(AssertionError.class);
        expectedException.expectMessage("- hashcode is (<101> or <0>) | real: ");

        Object obj = new Object();

        assertTrue(new MatchBuilder().matches(obj.hashCode(), is(anyOf(equalTo(101), equalTo(0)), "hashcode")).isMatch());
    }
}
