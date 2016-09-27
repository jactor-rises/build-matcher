package com.github.jactorrises.matcher;

/**
 * Match several matches with a {@link MatchBuilder}. This interface is provided in order to use lambda expressions on {@link TypeSafeBuildMatcher} with an instance of
 * {@link LambdaBuildMatcher}
 *
 * @param <T> is the type to test
 */
public interface BuildMatcher<T> {
    MatchBuilder matches(T typeToTest, MatchBuilder matchBuilder);
}
