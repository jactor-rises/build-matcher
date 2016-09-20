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

    StringBuilder get() {
        return expectedDescription;
    }
}
