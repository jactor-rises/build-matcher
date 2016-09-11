package com.github.jactorrises.matcher;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Matcher;

/**
 * To provide customized toString-implementations during evaluations, this class needs to be extended.
 */
public abstract class ToStringEditor<T> {
    private final Class<?> customizedStringClass;

    public ToStringEditor(Class<?> customizedStringClass) {
        this.customizedStringClass = customizedStringClass;
    }

    /**
     * @param type to get a string from
     * @return the string represented by type
     */
    protected abstract String toString(T type);

    String fetchStringFor(Object object) {
        if (object == null) {
            return null;
        }

        if (object instanceof Matcher) {
            return matcherToString((Matcher<?>) object);
        }

        if (customizedStringClass.equals(object.getClass())) {
            @SuppressWarnings("unchecked") T type = (T) object;
            return toString(type);
        }

        return object.toString();
    }

    private String matcherToString(Matcher<?> matcher) {
        if (matcher.toString().contains(customizedStringClass.getSimpleName() + "@")) {
            StringBuilder matcherStringBuilder = new StringBuilder();
            appendMatcherNamesTo(matcherStringBuilder, matcher.getClass());

            return matcherStringBuilder.append(customizedStringClass.getSimpleName()).toString();
        } else {
            return matcher.toString();
        }
    }

    private void appendMatcherNamesTo(StringBuilder matcherStringBuilder, Class<? extends Matcher> matcherClass) {
        Class<?> toStringClass = matcherClass;

        while (!BaseMatcher.class.equals(toStringClass)) {
            matcherStringBuilder.append(toStringClass.getSimpleName()).append(' ');
            toStringClass = toStringClass.getSuperclass();
        }
    }
}
