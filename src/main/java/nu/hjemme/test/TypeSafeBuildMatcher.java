package nu.hjemme.test;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

/**
 * En {@link org.hamcrest.Matcher} som bruker {@link nu.hjemme.test.MatchBuilder} for lettleselig matching av forventede resultater.
 */
public abstract class TypeSafeBuildMatcher<T> extends TypeSafeMatcher<T> {
    private final MatchBuilder matchBuilder;

    public TypeSafeBuildMatcher(String expects) {
        matchBuilder = new MatchBuilder(expects);
    }

    @Override
    protected boolean matchesSafely(T item) {
        try {
            matches(item, matchBuilder);
        } catch (Exception e) {
            matchBuilder.failWith(e);
        }

        return matchBuilder.isMatch();
    }

    @Override
    public final void describeTo(Description description) {
        description.appendText(matchBuilder.getExpectedValueMessage());
    }

    public abstract MatchBuilder matches(T typeToTest, MatchBuilder matchBuilder);
}
