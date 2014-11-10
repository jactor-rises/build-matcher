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
     * NOTE! In case of a mismatch with discription(s), then this method will throw an {@link java.lang.AssertionError} with this descriptions.
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

    public MatchBuilder matches(boolean match) {
        if (!match) {
            setMismatchWith("boolean does not match");
        }

        return this;
    }

    public <T> MatchBuilder matches(T real, Matcher<T> expected) {
        if (!expected.matches(real)) {
            setMismatchWith("'" + real + "' does not match '" + expected + "'");
        }

        return this;
    }

    public void failWith(Exception exception) {
        Throwable rootCause = provideRootCauseOf(exception);
        appendErrorMessageWithLineNumberFrom(rootCause);

        fail(this);
    }

    private Throwable provideRootCauseOf(Exception exception) {
        Throwable cause = exception;

        while (cause.getCause() != null) {
            cause = cause.getCause();
        }

        return cause;
    }

    private void appendErrorMessageWithLineNumberFrom(Throwable rootCause) {
        doNewMismatchDescription();
        mismatchDescriptions.append(classNameOf(rootCause)).append(": ").append(rootCause.getMessage());
        doNewMismatchDescription();
        mismatchDescriptions.append(classNameOf(rootCause)).append(" occurred at line number ").append(rootCause.getStackTrace()[0].getLineNumber());
    }

    private String classNameOf(Throwable throwable) {
        return throwable.getClass().getName();
    }

    public static String provideExpectedVsRealValue(Object expected, Object real) {
        return " [expected: " + expected + " | real: " + real + "]";
    }

    String getExpectedValue() {
        return expectedValue;
    }
}
