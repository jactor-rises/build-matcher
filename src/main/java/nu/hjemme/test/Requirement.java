package nu.hjemme.test;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;

import static org.hamcrest.CoreMatchers.equalTo;

/** @author Tor Egil Jacobsen */
class Requirement<T> {
    private final Matcher<T> matcher;
    private final T value;
    private final String description;

    Requirement(String description, T value, Matcher<T> matcher) {
        this.description = description;
        this.matcher = matcher;
        this.value = value;
    }

    boolean isMet() {
        return matcher.matches(value);
    }

    public String createDescriptionOfRequirement() {
        return "expects: " + obtainExpectedDescription() + ", real: " + obtainRealDescription();
    }

    private String obtainExpectedDescription() {
        StringDescription stringDescription = new StringDescription();
        matcher.describeTo(stringDescription);

        return stringDescription.toString();
    }

    private String obtainRealDescription() {
        StringDescription stringDescription = new StringDescription();
        equalTo(value).describeTo(stringDescription);

        return stringDescription.toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(matcher).append(value).append(description).toHashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Requirement<?> other = (Requirement<?>) o;

        return new EqualsBuilder()
                .append(matcher, other.matcher)
                .append(value, other.value)
                .append(description, other.description)
                .isEquals();
    }
}
