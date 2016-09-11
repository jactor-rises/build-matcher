package com.github.jactorrises.matcher;

/** All mismatch descriptions of a {@link MatchBuilder} and its expected value */
class MismatchDescriptions {
    private final ExpectedDescription expectedDescription;
    private final StringBuilder allMismatchDescriptions;

    MismatchDescriptions() {
        expectedDescription = new ExpectedDescription();
        allMismatchDescriptions = new StringBuilder();
    }

    MismatchDescriptions(String expectedValueMessage) {
        expectedDescription = new ExpectedDescription(expectedValueMessage);
        this.allMismatchDescriptions = new StringBuilder();
    }

    boolean hasMismatchDescriptions() {
        return !allMismatchDescriptions.toString().isEmpty();
    }

    void appendMismatchWith(String mismatchDescription) {
        allMismatchDescriptions.append("\n          ");
        allMismatchDescriptions.append(mismatchDescription);
    }

    void appendExceptionToFailureMessageUsing(Exception exception) {
        appendToFailureMessageUsing(exception);
        appendCauseOf(exception);
        fail();
    }

    private MatchBuilder fail() {
        throw new AssertionError(provideExpectedVsFailures());
    }

    private void appendToFailureMessageUsing(Exception exception) {
        StackTraceElement[] stackTrace = exception.getStackTrace();
        String exceptionClassName = exception.getClass().getName();
        boolean inTest = stackTrace[0].getClassName().endsWith("Test");

        appentToFailuremessageUsing(exception.getMessage(), stackTrace, exceptionClassName, inTest);
    }

    private void appentToFailuremessageUsing(String exceptionMessage, StackTraceElement[] stackTrace, String exceptionClassName, boolean inTest) {
        appendMismatchWith("An unexpeced exception occurred: " + exceptionClassName);
        appendMismatchWith(stackTrace[0] + ": " + exceptionMessage);

        if (!inTest) {
            appendMismatchWith(provideStackTraceFromnTestUsing(stackTrace));
        }
    }

    private String provideStackTraceFromnTestUsing(StackTraceElement[] stackTrace) {
        for (StackTraceElement stackTraceElement : stackTrace) {
            if (stackTraceElement.getClassName().endsWith("Test")) {
                return "occured in the test at " + stackTraceElement;
            }
        }

        return "occured in the test, but was unable to determine StackTraceElement";
    }

    private void appendCauseOf(Exception exception) {
        Throwable cause = exception;

        while (cause.getCause() != null) {
            cause = cause.getCause();
            appendCauseOfExceptionToFailureMessage(cause);
        }
    }

    private void appendCauseOfExceptionToFailureMessage(Throwable cause) {
        appendMismatchWith("Caused by " + cause.getClass().getName() + " at " + cause.getStackTrace()[0]);
    }

    String provideExpectedVsFailures() {
        return expectedDescription.get().append(allMismatchDescriptions.toString()).toString();
    }

    String getExpectedDescritpion() {
        return expectedDescription.get().toString();
    }
}
