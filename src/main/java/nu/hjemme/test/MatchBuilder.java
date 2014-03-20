package nu.hjemme.test;

import org.hamcrest.Matcher;

/**
 * Hjelpeklasse for å evaluere flere feil på en assert som inneholder en feilmelding med expected vs real. Kan brukes på en {@link AssertionError}.
 */
public class MatchBuilder {
    private boolean mismatch;
    private StringBuilder mismatchDescriptions;
    private String expectedValue;

    public MatchBuilder() {
    }

    public MatchBuilder(String expectedValue) {
        this.expectedValue = expectedValue;
        mismatchDescriptions = new StringBuilder("\nExpected \"" + expectedValue + "\", but there was failures:");
    }

    public boolean isMatch() {
        if (mismatch && mismatchDescriptions != null) {
            throw new AssertionError(mismatchDescriptions.toString());
        }

        return !mismatch;
    }

    public MatchBuilder matches(boolean match, String mismatchDescription) {
        if (!match) {
            setMismatchWith(mismatchDescription);
        }

        return this;
    }

    private MatchBuilder setMismatchWith(String mismatchDescription) {
        doNewMismatchDescription();
        mismatchDescriptions.append(mismatchDescription);
        mismatch = true;

        return this;
    }

    private void doNewMismatchDescription() {
        if (mismatchDescriptions == null) {
            mismatchDescriptions = new StringBuilder("");
        } else {
            mismatchDescriptions.append("\n          ");
        }
    }

    public <T> MatchBuilder matches(T real, Matcher<T> expected, String mismatchDescription) {
        return expected.matches(real) ? this : setMismatchWith(mismatchDescription + provideExpectedVsRealValue(expected, real));
    }

    private MatchBuilder fail(MatchBuilder matchBuilder) {
        throw new AssertionError(matchBuilder.mismatchDescriptions.toString());
    }

    public MatchBuilder failIfMatch(boolean match, String failureMessage) {
        return match ? fail(setMismatchWith(failureMessage)) : this;
    }

    public <T> MatchBuilder failIfMatch(T real, T expected, String mismatchDescription) {
        return isMatch(real, expected) ? fail(setMismatchWith(mismatchDescription + provideExpectedVsRealValue(expected, real))) : this;
    }

    public <T> MatchBuilder failIfMatch(T real, Matcher<T> expected, String mismatchDescription) {
        return expected.matches(real) ? fail(setMismatchWith(mismatchDescription)) : this;
    }

    @SuppressWarnings("unchecked")
    private <T> boolean isMatch(T real, T expected) {
        return !(expected instanceof Matcher) ? expected != null && expected.equals(real) || real == null && expected == null : ((Matcher<T>) expected).matches(real);
    }

    public MatchBuilder matches(boolean match) {
        if (!match) {
            mismatch = true;
        }

        return this;
    }

    public <T> MatchBuilder matches(T real, Matcher<T> expected) {
        if (!expected.matches(real)) {
            mismatch = true;
        }

        return this;
    }

    public <T> MatchBuilder failIfMismatch(T real, T expected, String mismatchDescription) {
        return !isMatch(real, expected) ? fail(setMismatchWith(mismatchDescription + provideExpectedVsRealValue(expected, real))) : this;
    }

    public <T> MatchBuilder failIfMismatch(T real, Matcher<T> expected, String mismatchDescription) {
        return !expected.matches(real) ? fail(setMismatchWith(mismatchDescription + provideExpectedVsRealValue(expected, real))) : this;
    }

    public static String provideExpectedVsRealValue(Object expected, Object real) {
        return " [expected: " + expected + " | real: " + real + "]";
    }

    protected String getExpectedValue() {
        return expectedValue;
    }
}
