package nu.hjemme.test;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

/**
 * En {@link org.hamcrest.Matcher} som bruker {@link MatchBuilder} for lettleselig matching av forventede resultater.
 */
public abstract class NotNullBuildMatching<T> extends BaseMatcher<T> {
    private final MatchBuilder matchBuilder;

    public NotNullBuildMatching(String expects) {
        matchBuilder = new MatchBuilder(expects);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean matches(Object item) {
        if (item != null) {
            matches((T) item, matchBuilder);
        } else {
            throw new AssertionError(matchBuilder.getExpectedValue() + ": Instance to test is null!");
        }

        return matchBuilder.isMatch();
    }

    @Override
    public final void describeTo(Description description) {
        description.appendText(matchBuilder.getExpectedValue());
    }

    public abstract MatchBuilder matches(T typeToTest, MatchBuilder matchBuilder);
}
