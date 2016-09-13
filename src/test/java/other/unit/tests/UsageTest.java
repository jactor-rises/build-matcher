package other.unit.tests;

import com.github.jactorrises.matcher.MatchBuilder;
import com.github.jactorrises.matcher.TypeSafeBuildMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.github.jactorrises.matcher.DescriptionMatcher.is;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.rules.ExpectedException.none;

public class UsageTest {

    @Rule
    public ExpectedException expectedException = none();

    @Test
    public void shouldBeAbleToMatchMultipleMatchesOnTypeWithSingleAssert() {
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

    private class TestSeveralMatches {
        String somewhat = "boring";
        String someone = "good";
    }
}
