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
            exceptionSafeMatching((T) item);
        } else {
            throw new AssertionError(matchBuilder.getExpectedValue() + ": Instance to test is null!");
        }

        return matchBuilder.isMatch();
    }

    private void exceptionSafeMatching(T item) {
        try {
            matches(item, matchBuilder);
        } catch (Exception e) {
            matchBuilder.failWith(e);
        }
    }

    @Override
    public final void describeTo(Description description) {
        description.appendText(matchBuilder.getExpectedValue());
    }

    public abstract MatchBuilder matches(T typeToTest, MatchBuilder matchBuilder);
}
