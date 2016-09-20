package com.github.jactorrises.matcher;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

/**
 * A {@link org.hamcrest.Matcher} which will use {@link MatchBuilder} to do multiple assertions on a type. If any exception occurs during testing, it will be reported as an
 * {@link AssertionError}.
 *
 * @param <T> the type to match
 */
public abstract class TypeSafeBuildMatcher<T> extends TypeSafeMatcher<T> {
    private final MatchBuilder matchBuilder;

    protected TypeSafeBuildMatcher(String expects) {
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
