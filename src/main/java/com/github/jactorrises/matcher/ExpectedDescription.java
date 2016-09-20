package com.github.jactorrises.matcher;

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

    StringBuilder get() {
        return expectedDescription;
    }
}
