package com.github.jactorrises.matcher;

/**
 * The {@link MatchBuilder} will be able to build several matches and keep any failures for a single evaluation of all matches with an exception message containing expected vs
 * real. The failure message is thrown with a {@link AssertionError}.
 */
public final class MatchBuilder {
    private final MismatchDescriptions mismatchDescriptions;
    private boolean mismatch;

    MatchBuilder() {
        mismatchDescriptions = new MismatchDescriptions();
    }

    MatchBuilder(String expectedValue) {
        mismatchDescriptions = new MismatchDescriptions(expectedValue);
    }

    /**
     * NOTE! In case of a mismatch with description(s), then this method will throw an {@link java.lang.AssertionError} with these descriptions.
     *
     * @return <code>true</code> if match
     */
    boolean isMatch() {
        if (mismatch && mismatchDescriptions.hasMismatchDescriptions()) {
            throw new AssertionError(mismatchDescriptions.provideExpectedVsFailures());
        }

        return !mismatch;
    }

    /**
     * @param real match value
     * @param expected match value which will have a mismatch description if failure
     * @param <T> generic type of value
     * @return a {@link MatchBuilder} with this and any older matches
     */
    public <T> MatchBuilder matches(T real, LabelMatcher<T> expected) {
        return expected.matches(real) ? this : new ToStringBuilder(expected, real, this).describeMismastchWith(expected.getLabel());
    }

    /**
     * @param real match value
     * @param expected match value which will have a mismatch description if failure
     * @param toStringEditor edits an object to string into a readable mismatch
     * @param <T> generic type of value
     * @return a {@link MatchBuilder} with this and any older matches
     */
    public <T> MatchBuilder matches(T real, LabelMatcher<T> expected, ToStringEditor<?> toStringEditor) {
        return expected.matches(real) ? this : new ToStringBuilder(expected, real, this, toStringEditor).describeMismastchWith(expected.getLabel());
    }

    MatchBuilder appendMismatchWith(String mismatchDescription) {
        mismatchDescriptions.appendMismatchWith(mismatchDescription);
        mismatch = true;

        return this;
    }

    void failWith(Exception exception) {
        mismatchDescriptions.appendExceptionToFailureMessageUsing(exception);
    }

    String getExpectedValueMessage() {
        return mismatchDescriptions.getExpectedDescritpion();
    }
}
