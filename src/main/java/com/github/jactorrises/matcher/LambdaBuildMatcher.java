package com.github.jactorrises.matcher;

import org.hamcrest.Matcher;

/**
 * Usage of {@link TypeSafeBuildMatcher} in lambda expressions
 *
 * @param <T> represents the type being tested
 */
public class LambdaBuildMatcher<T> extends TypeSafeBuildMatcher<T> {

    private final BuildMatcher<T> buildMatcher;

    private LambdaBuildMatcher(String expects, BuildMatcher<T> buildMatcher) {
        super(expects);
        this.buildMatcher = buildMatcher;
    }

    @Override public MatchBuilder matches(T typeToTest, MatchBuilder matchBuilder) {
        return buildMatcher.matches(typeToTest, matchBuilder);
    }

    /**
     * @param buildMatcher is used to build matches of the type to match
     * @param expects states the expected behaviour
     * @return new {@link LambdaBuildMatcher} using {@link BuildMatcher}
     */
    public static <T> Matcher<T> verify(String expects, BuildMatcher<T> buildMatcher) {
        return new LambdaBuildMatcher<>(expects, buildMatcher);
    }
}
