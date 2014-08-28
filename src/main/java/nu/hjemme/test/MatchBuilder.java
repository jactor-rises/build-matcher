package nu.hjemme.test;

import org.hamcrest.Matcher;

/**
 * Hjelpeklasse for å evaluere flere feil på en assert som inneholder en feilmelding med expected vs real. Feilmeldingen brukes på en {@link AssertionError}.
 */
public class MatchBuilder {
    private final String expectedValue;
    private final StringBuilder mismatchDescriptions;

    public MatchBuilder() {
        mismatchDescriptions = new StringBuilder("Matching produced failures:");
        expectedValue = null;
    }

    public MatchBuilder(String expectedValue) {
        this.expectedValue = expectedValue;
        mismatchDescriptions = new StringBuilder("\nExpected \"" + expectedValue + "\", but there was failures:");
    }

    /**
     * NOTE! In case of a mismatch with discription, then this method will throw an {@link java.lang.AssertionError} with this descriptions.
     * @return <code>true</code> if match
     */
    public boolean isMatch() {
        if (hasFailures()) {
            throw new AssertionError(mismatchDescriptions.toString());
        }

        return hasNoFailures();
    }

    private boolean hasFailures() {
        return mismatchDescriptions.length() != mismatchDescriptions.indexOf(":") + 1;
    }

    private boolean hasNoFailures() {
        return !hasFailures();
    }

    public MatchBuilder matches(boolean match, String mismatchDescription) {
        if (!match) {
            return setMismatchWith(mismatchDescription);
        }

        return this;
    }

    private MatchBuilder setMismatchWith(String mismatchDescription) {
        doNewMismatchDescription();
        mismatchDescriptions.append(mismatchDescription);

        return this;
    }

    private void doNewMismatchDescription() {
        if (mismatchDescriptions.length() != 0) {
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
            setMismatchWith("boolean does not match");
        }

        return this;
    }

    public <T> MatchBuilder matches(T real, Matcher<T> expected) {
        if (!expected.matches(real)) {
            setMismatchWith("'" + real + "' does not match '" + expected +"'");
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
