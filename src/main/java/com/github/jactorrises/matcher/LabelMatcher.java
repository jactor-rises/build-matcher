package com.github.jactorrises.matcher;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.hamcrest.core.Is;

/**
 * A {@link org.hamcrest.Matcher} extending {@link org.hamcrest.core.Is} and provides labeling of standard {@link org.hamcrest.Matcher} failure messages.
 *
 * @param <T> type to match
 */
public class LabelMatcher<T> extends Is<T> {

    private final String label;

    LabelMatcher(Matcher<T> matcher, String label) {
        super(matcher);
        this.label = label;
    }

    @Override public void describeTo(Description description) {
        description.appendText(this.label).appendText(" ");
        super.describeTo(description);
    }

    @Override public int hashCode() {
        return label.hashCode();
    }

    @Override public String toString() {
        StringDescription stringDescription = new StringDescription();
        describeTo(stringDescription);
        return stringDescription.toString();
    }

    /**
     * @param matcher to match the type
     * @param label for the match, typically the name of the variable to match
     * @param <T> type being matched
     * @return a label matcher
     */
    public static <T> LabelMatcher<T> is(Matcher<T> matcher, String label) {
        return new LabelMatcher<>(matcher, label);
    }

    String getLabel() {
        return label;
    }
}
