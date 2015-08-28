package nu.hjemme.test.matcher;

class ExpectedDescription {
    private final StringBuilder expectedDescription;

    ExpectedDescription() {
        this("to meet expectations");
    }

    ExpectedDescription(String expectedValueMessage) {
        expectedDescription = new StringBuilder("\nExpected \"")
                .append(expectedValueMessage != null ? expectedValueMessage : "to meet expectations")
                .append('\"');
    }

    public StringBuilder get() {
        return expectedDescription;
    }
}
