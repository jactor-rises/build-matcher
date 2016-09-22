package com.github.jactorrises.matcher;

import org.hamcrest.Matcher;

/** A builder of strings regarding expected vs. real values... */
class ToStringBuilder<T> {
    private final Matcher<T> expected;
    private final T real;
    private final MatchBuilder matchBuilder;
    private final ToStringEditor<T> toStringEditor;

    ToStringBuilder(Matcher<T> expected, T real, MatchBuilder matchBuilder) {
        this(expected, real, matchBuilder, null);
    }

    ToStringBuilder(Matcher<T> expected, T real, MatchBuilder matchBuilder, ToStringEditor<T> toStringEditor) {
        this.expected = expected;
        this.real = real;
        this.matchBuilder = matchBuilder;
        this.toStringEditor = toStringEditor;
    }

    MatchBuilder describeMismatch() {
        return matchBuilder.appendMismatchWith(provideExpectedVsRealValue());
    }

    private String provideExpectedVsRealValue() {
        return " - " + provideQuotesAndNumberClass(expected, toStringEditor) + " | real: " + provideQuotesAndNumberClass(real, toStringEditor);
    }

    private String provideQuotesAndNumberClass(Object object, ToStringEditor<T> toStringEditor) {
        if (object == null) {
            return null;
        }

        String objectToString = toStringEditor == null ? object.toString() : toStringEditor.fetchStringFor(object, real.getClass());

        if (object instanceof Matcher || objectToString.indexOf(0) == '"') {
            return objectToString;
        } else if (object instanceof Number) {
            return provideNumberTypeMarking((Number) object);
        }

        return '"' + objectToString + '"';
    }

    private String provideNumberTypeMarking(Number number) {
        StringBuilder numberAndType;

        if (number instanceof Integer) {
            numberAndType = surround(number); // surround with <>
        } else if (number instanceof Long) {
            numberAndType = newStringBuilderWith(number).append('L'); //  add type L
        } else if (!(number instanceof Double)) {
            numberAndType = newStringBuilderWith(number).append(" (").append(number.getClass().getSimpleName()).append(')'); // add simple class name
        } else {
            numberAndType = surround(number); // a double type is also surrounded with <>
        }

        return numberAndType.toString();
    }

    private StringBuilder surround(Number number) {
        return new StringBuilder("<").append(number).append(">");
    }

    private StringBuilder newStringBuilderWith(Number number) {
        return new StringBuilder(number.toString());
    }
}
