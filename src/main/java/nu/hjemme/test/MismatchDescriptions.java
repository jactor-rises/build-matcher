package nu.hjemme.test;

/**
 * All mismatch descriptions of a {@link MatchBuilder} and its expected value
 *
 * @author Tor Egil Jacobsen - Accenture
 */
class MismatchDescriptions {
    private final String expectedValueMessage;
    private final StringBuilder allMismatchDescriptions;

    MismatchDescriptions() {
        expectedValueMessage = null;
        allMismatchDescriptions = new StringBuilder();
    }

    MismatchDescriptions(String expectedValueMessage) {
        this.expectedValueMessage = expectedValueMessage;
        this.allMismatchDescriptions = new StringBuilder("\nExpected \"")
                .append(expectedValueMessage)
                .append("\", but there was failures:");
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
        throw new AssertionError(getAll());
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

    String getAll() {
        return allMismatchDescriptions.toString();
    }

    String getExpectedValueMessage() {
        return expectedValueMessage;
    }
}
