package nu.hjemme.test;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

/**
 * A {@link Matcher} which matches all requirements when {@linkplain * TypeSafeDiagnosingMatcher#matchesSafely(Object)} is executed.
 * @author Tor Egil Jacobsen
 */
public abstract class RequirementsMatcher<T> extends TypeSafeDiagnosingMatcher<T> {
    private final AllRequirements allRequirements;

    protected RequirementsMatcher(String summaryOfRequirements) {
        allRequirements = new AllRequirements(summaryOfRequirements);
    }

    /**
     * Adds all requirements for a type
     * @param t is the type to list requirements for
     */
    protected abstract void checkRequirementsFor(T t);

    @Override
    protected final boolean matchesSafely(T t, Description mismatchDescription) {
        allRequirements.use(mismatchDescription);
        checkRequirementsFor(t);
        allRequirements.throwAssertionErrorIfNoRequirementsAreAdded();
        return allRequirements.areRequirementsMet();
    }

    protected <X> void checkIf(String description, X value, Matcher<X> requirement) {
        allRequirements.check(new Requirement<X>(description, value, requirement));
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(allRequirements.getSummary());
    }
}
