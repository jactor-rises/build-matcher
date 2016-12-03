package com.github.jactorrises.matcher;

import org.hamcrest.Matcher;

import java.util.Arrays;
import java.util.Optional;

/**
 * A builder of strings regarding expected vs. real values...
 */
class ToStringBuilder<T> {
    private final Match<T> expectedMatch;
    private final MatchBuilder matchBuilder;
    private final ToStringEditor<T> toStringEditor;

    ToStringBuilder(Match<T> expectedMatch, MatchBuilder matchBuilder) {
        this(expectedMatch, matchBuilder, null);
    }

    ToStringBuilder(Match<T> expectedMatch, MatchBuilder matchBuilder, ToStringEditor<T> toStringEditor) {
        this.expectedMatch = expectedMatch;
        this.matchBuilder = matchBuilder;
        this.toStringEditor = toStringEditor;
    }

    MatchBuilder describeMismatch() {
        return matchBuilder.appendMismatchWith(provideExpectedVsRealValue());
    }

    private String provideExpectedVsRealValue() {
        return expectedMatch.fetchMatchPrefix() + provideQuotesOrNumberClass(expectedMatch) + " | real: " + provideQuotesOrNumberClass(expectedMatch.get()) + " | " + fetchStacktraceElementOfMatchFailure();
    }


    private String provideQuotesOrNumberClass(Object object) {
        if (object == null) {
            return null;
        }

        Optional<Class<?>> optionalClass = expectedMatch.fetchRealClass();
        String objectToString = toStringEditor == null ? object.toString() : toStringEditor.fetchStringFor(object, optionalClass.isPresent() ? optionalClass.get() : null);

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

    private String fetchStacktraceElementOfMatchFailure() {
        Optional<StackTraceElement> stack = fetchFrom(Thread.currentThread().getStackTrace());
        return stack.isPresent() ? stack.get().toString() : "unknown source";
    }

    private Optional<StackTraceElement> fetchFrom(StackTraceElement[] stackTrace) {
        return Arrays.stream(stackTrace)
                .filter(stackTraceElement -> !stackTraceElement.toString().contains("java.lang"))
                .filter(stackTraceElement -> !stackTraceElement.toString().contains("org.hamcrest"))
                .filter(stackTraceElement -> !stackTraceElement.toString().contains("org.junit"))
                .filter(stackTraceElement -> !stackTraceElement.toString().contains("junit.framework"))
                .filter(stackTraceElement -> !stackTraceElement.toString().contains(ToStringEditor.class.getPackage().getName()))
                .findFirst();
    }

    private StringBuilder surround(Number number) {
        return new StringBuilder("<").append(number).append(">");
    }

    private StringBuilder newStringBuilderWith(Number number) {
        return new StringBuilder(number.toString());
    }
}
