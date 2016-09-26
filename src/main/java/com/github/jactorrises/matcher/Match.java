package com.github.jactorrises.matcher;

import java.util.Optional;

/**
 * This represent one verification to be done. A match is a {@link LabelMatcher} with the real object being matched. This match will also contain a {@link ToStringEditor} which
 * will provide description of the object and the {@link LabelMatcher} which is used to verify the match.
 *
 * @param <T> is the type being matched
 */
class Match<T> extends LabelMatcher<T> {
    private final T match;
    private final String toString;

    Match(T match, LabelMatcher<T> matcher) {
        super(matcher, matcher.getLabel());
        this.match = match;
        toString = matcher.toString();
    }

    boolean isMatch() {
        return matches(match);
    }

    Optional<Class<?>> fetchRealClass() {
        return match != null ? Optional.of(match.getClass()) : Optional.empty();
    }

    Object get() {
        return match;
    }

    @Override public String toString() {
        return toString;
    }
}
