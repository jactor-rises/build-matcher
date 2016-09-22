package com.github.jactorrises.matcher;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Matcher;

/**
 * To provide customized toString-implementations during evaluations
 *
 * @param <T> the type to create a custom string implementation for
 */
public interface ToStringEditor<T> {

    /**
     * @param type to get a string from
     * @return the string represented by this type
     */
    String toString(T type);

    /**
     * @param object to get a string from
     * @param customizedStringClass should be the class of the type to create
     * @return the string represented by the object
     */
    default String fetchStringFor(Object object, Class<?> customizedStringClass) {
        if (object == null) {
            return null;
        }

        if (object instanceof Matcher) {
            return matcherToString((Matcher<?>) object, customizedStringClass);
        }

        if (customizedStringClass.equals(object.getClass())) {
            @SuppressWarnings("unchecked") T type = (T) object;
            return toString(type);
        }

        return object.toString();
    }

    default String matcherToString(Matcher<?> matcher, Class<?> customizedStringClass) {
        if (matcher.toString().contains(customizedStringClass.getSimpleName() + "@")) {
            StringBuilder matcherStringBuilder = new StringBuilder();
            appendMatcherNamesTo(matcherStringBuilder, matcher.getClass());

            return matcherStringBuilder.append(customizedStringClass.getSimpleName()).toString();
        } else {
            return matcher.toString();
        }
    }

    default void appendMatcherNamesTo(StringBuilder matcherStringBuilder, Class<? extends Matcher> matcherClass) {
        Class<?> toStringClass = matcherClass;

        while (!BaseMatcher.class.equals(toStringClass)) {
            matcherStringBuilder.append(toStringClass.getSimpleName()).append(' ');
            toStringClass = toStringClass.getSuperclass();
        }
    }
}
