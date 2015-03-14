package nu.hjemme.test;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.hamcrest.core.Is;

/**
 * An {@link org.hamcrest.core.Is} {@link org.hamcrest.Matcher}
 * @param <T> type to match
 * @author Tor Egil Jacobsen
 */
public class DescriptionMatcher<T> extends Is<T> {

    private final String description;

    public DescriptionMatcher(Matcher<T> matcher, String description) {
        super(matcher);
        this.description = description;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(this.description).appendText(" ");
        super.describeTo(description);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(description).toHashCode();
    }

    @Override
    public String toString() {
        StringDescription stringDescription = new StringDescription();
        describeTo(stringDescription);
        return stringDescription.toString();
    }

    /**
     * @param matcher to match the type
     * @param mismatchDescription for the match, typically the name of the variable to match
     * @param <T> type being matched
     * @return a mismatch description matcher
     */
    public static <T> DescriptionMatcher<T> is(Matcher<T> matcher, String mismatchDescription) {
        return new DescriptionMatcher<T>(matcher, mismatchDescription);
    }

    public String getDescrption() {
        return description;
    }
}
