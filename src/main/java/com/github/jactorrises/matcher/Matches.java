package com.github.jactorrises.matcher;

import java.util.ArrayList;
import java.util.List;

/**
 * A set of {@link Match}es
 */
class Matches {
    private final List<Match<?>> matches = new ArrayList<>();
    private boolean mismatch;

    boolean isMismatch() {
        return mismatch;
    }

    boolean isMatch() {
        return !mismatch;
    }

    /**
     * @param aMatch that is appended to current matches...
     * @return match evaluated: {@link Match#isMatch()}
     */
    boolean append(Match<?> aMatch) {
        matches.add(aMatch);

        if (!mismatch) {
            mismatch = !aMatch.isMatch();
        }

        return aMatch.isMatch();
    }

    @SuppressWarnings("unchecked") <T> Match<T> fetchLatest() {
        return (Match<T>) matches.get(matches.size() - 1);
    }
}
