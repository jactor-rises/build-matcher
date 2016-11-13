package com.github.jactorrises.matcher;

class ExpectedDescription {
    private final StringBuilder expectedDescription;

    ExpectedDescription() {
        this("no failures");
    }

    ExpectedDescription(String expectedValueMessage) {
        expectedDescription = new StringBuilder("\nExpected \"")
                .append(expectedValueMessage != null ? expectedValueMessage : "no failures")
                .append('"');
    }

    void append(Object dynamicExpectation) {
        expectedDescription.insert(expectedDescription.lastIndexOf("\""), dynamicExpectation);
    }

    StringBuilder get() {
        return expectedDescription;
    }
}
