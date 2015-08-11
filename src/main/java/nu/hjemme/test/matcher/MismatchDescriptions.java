package nu.hjemme.test.matcher;

/**
 * All mismatch descriptions of a {@link MatchBuilder} and its expected value
 *
 * @author Tor Egil Jacobsen - Accenture
 */
class MismatchDescriptions {
    private final ExpectedDescription expectedDescription;
    private final StringBuilder allMismatchDescriptions;

    MismatchDescriptions() {
        expectedDescription = new ExpectedDescription();
        allMismatchDescriptions = new StringBuilder();
    }

    MismatchDescriptions(String expectedValueMessage) {
        expectedDescription = new ExpectedDescription(expectedValueMessage);
        this.allMismatchDescriptions = new StringBuilder() ;
    }

    boolean hasMismatchDescriptions() {
        return !allMismatchDescriptions.toString().isEmpty();
    }

    void appendMismatchWith(String mismatchDescription) {
        appendNewMismatchDescription();
        allMismatchDescriptions.append(mismatchDescription);
    }

    private void appendNewMismatchDescription() {
        if (hasMismatchDescriptions()) {
            allMismatchDescriptions.append("\n          ");
        }
    }

    void appendToFailureMessagesUsing(Exception exception) {
        appendToFailureMessageWith(exception);
        appendCauseOf(exception);
        fail();
    }

    private MatchBuilder fail() {
        throw new AssertionError(provideExpectedVsFailures());
    }

    private void appendToFailureMessageWith(Exception exception) {
        appendMismatchWith("An unexpeced exception occurred: " + exception.getClass().getName());
        appendMismatchWith(exception.getStackTrace()[0] + ": " + exception.getMessage());
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
