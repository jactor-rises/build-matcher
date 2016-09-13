package com.github.jactorrises.matcher;

import org.hamcrest.Matcher;

/** A builder of strings regarding expected vs. real values... */
class ToStringBuilder {
    private final Object expected;
    private final Object real;
    private final MatchBuilder matchBuilder;
    private final ToStringEditor<?> toStringEditor;

    ToStringBuilder(Object expected, Object real, MatchBuilder matchBuilder) {
        this(expected, real, matchBuilder, null);
    }

    ToStringBuilder(Object expected, Object real, MatchBuilder matchBuilder, ToStringEditor<?> toStringEditor) {
        this.expected = expected;
        this.real = real;
        this.matchBuilder = matchBuilder;
        this.toStringEditor = toStringEditor;
    }

    MatchBuilder describeMismastchWith(String mismatchDescription) {
        matchBuilder.appendMismatchWith(mismatchDescription + provideExpectedVsRealValue());
        return matchBuilder;
    }

    private String provideExpectedVsRealValue() {
        return " [expected: " + provideQuotesAndNumberClass(expected, toStringEditor) + " | real: " + provideQuotesAndNumberClass(real, toStringEditor) + "]";
    }

    private static String provideQuotesAndNumberClass(Object object, ToStringEditor<?> toStringEditor) {
        if (object == null) {
            return null;
        }

        String objectToString = toStringEditor == null ? object.toString() : toStringEditor.fetchStringFor(object);

        if (object instanceof Matcher || objectToString.indexOf(0) == '"') {
            return objectToString;
        } else if (object instanceof Number) {
            objectToString = provideNumberClass((Number) object);
        }

        return '"' + objectToString + '"';
    }

    private static String provideNumberClass(Number number) {
        StringBuilder numberAndType = new StringBuilder(number.toString());

        if (number instanceof Long) {
            numberAndType.append('L');
        } else if (!(number instanceof Integer) && !(number instanceof Double)) {
            numberAndType.append(" (").append(number.getClass().getSimpleName()).append(')');
        }

        return numberAndType.toString();
    }
}