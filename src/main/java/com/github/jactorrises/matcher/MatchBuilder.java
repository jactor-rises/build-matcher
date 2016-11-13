package com.github.jactorrises.matcher;

import org.hamcrest.Matcher;

import static com.github.jactorrises.matcher.LabelMatcher.is;

/**
 * The {@link MatchBuilder} will be able to build several matches and keep any failures for a single evaluation of all matches with an exception message containing expected vs
 * real. The failure message is thrown with a {@link AssertionError}.
 */
public final class MatchBuilder {
    private final MismatchDescriptions mismatchDescriptions;
    private final Matches matches = new Matches();

    public MatchBuilder() {
        mismatchDescriptions = new MismatchDescriptions();
    }

    public MatchBuilder(String expectedValue) {
        mismatchDescriptions = new MismatchDescriptions(expectedValue);
    }

    /**
     * NOTE! In case of a mismatch with description(s), then this method will throw an {@link java.lang.AssertionError} with these descriptions.
     *
     * @return {@code true} if match, {@code false} if mismatch without any descriptions
     */
    public boolean isMatch() {
        if (matches.isMismatch() && mismatchDescriptions.hasMismatchDescriptions()) {
            throw new AssertionError(mismatchDescriptions.provideExpectedVsFailures());
        }

        return matches.isMatch();
    }

    /**
     * @param real match value
     * @param expected match value which will have a mismatch description if failure
     * @param <T> generic type of value
     * @return a {@link MatchBuilder} with this and any older matches
     */
    public <T> MatchBuilder matches(T real, LabelMatcher<T> expected) {
        return matches.append(new Match<>(real, expected)) ? this : new ToStringBuilder<>(matches.fetchLatest(), this).describeMismatch();
    }

    /**
     * @param real match value
     * @param expected match value
     * @param label will give an id to this match
     * @param <T> generic type of value
     * @return a {@link MatchBuilder} with this and any older matches
     */
    public <T> MatchBuilder matches(T real, Matcher<T> expected, String label) {
        return matches(real, is(expected, label));
    }

    /**
     * @param real match value
     * @param expected match value which will have a mismatch description if failure
     * @param toStringEditor edits an object to string into a readable mismatch
     * @param <T> generic type of value
     * @return a {@link MatchBuilder} with this and any older matches
     */
    public <T> MatchBuilder matches(T real, LabelMatcher<T> expected, ToStringEditor<T> toStringEditor) {
        return matches.append(new Match<>(real, expected)) ? this : new ToStringBuilder<>(matches.fetchLatest(), this, toStringEditor).describeMismatch();
    }

    /**
     * @param real match value
     * @param expected match value
     * @param label will give an id to this match
     * @param toStringEditor edits an object to string into a readable mismatch
     * @param <T> generic type of value
     * @return a {@link MatchBuilder} with this and any older matches
     */
    public <T> MatchBuilder matches(T real, Matcher<T> expected, String label, ToStringEditor<T> toStringEditor) {
        return matches(real, is(expected, label), toStringEditor);
    }

    MatchBuilder appendMismatchWith(String mismatchDescription) {
        mismatchDescriptions.appendMismatchWith(mismatchDescription);
        return this;
    }

    void failWith(Exception exception) {
        mismatchDescriptions.appendExceptionToFailureMessageUsing(exception);
    }

    /**
     * @param dynamicExpectation is appended to the static expectation
     * @return a {@link MatchBuilder} with any older matches
     */
    public MatchBuilder append(Object dynamicExpectation) {
        mismatchDescriptions.append(dynamicExpectation);
        return this;
    }

    String getExpectedValueMessage() {
        return mismatchDescriptions.getExpectedDescription();
    }
}
