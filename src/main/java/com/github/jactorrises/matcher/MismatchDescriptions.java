package com.github.jactorrises.matcher;

/** All mismatch descriptions of a {@link MatchBuilder} and its expected value */
class MismatchDescriptions {
    private final ExpectedDescription expectedDescription;
    private final StringBuilder allMismatchDescriptions;

    private MismatchDescriptions(ExpectedDescription expectedDescription, StringBuilder allMismatchDescriptions) {
        this.expectedDescription = expectedDescription;
        this.allMismatchDescriptions = allMismatchDescriptions;
    }

    MismatchDescriptions() {
        this(new ExpectedDescription(), new StringBuilder());
    }

    MismatchDescriptions(String expectedValueMessage) {
        this(new ExpectedDescription(expectedValueMessage), new StringBuilder());
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

        appentToFailuremessageUsing(exception.getMessage(), stackTrace, exceptionClassName);
    }

    private void appentToFailuremessageUsing(String exceptionMessage, StackTraceElement[] stackTrace, String exceptionClassName) {
        appendMismatchWith("An unexpeced exception occurred: " + exceptionClassName);
        appendMismatchWith(stackTrace[0] + ": " + exceptionMessage);

        if (!isTestStackFrom(stackTrace[0])) {
            appendMismatchWith(provideStackTraceFromTestUsing(stackTrace));
        }
    }

    private String provideStackTraceFromTestUsing(StackTraceElement[] stackTrace) {
        for (StackTraceElement stackTraceElement : stackTrace) {
            if (isTestStackFrom(stackTraceElement)) {
                return "was thrown in the test at " + stackTraceElement;
            }
        }

        return "was thrown in the test, but was unable to determine StackTraceElement";
    }

    private boolean isTestStackFrom(StackTraceElement stackTraceElement) {
        return stackTraceElement.getClassName().endsWith("Test");
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
