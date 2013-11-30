package nu.hjemme.test;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/** @author Tor Egil Jacobsen */
public class RequirementsMatcherTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void willHaveAllFailedRequirementsInAssertionTextWhenFailedToMeetRequirements() {
        expectedException.expect(AssertionError.class);
        expectedException.expectMessage("- Skal være like");
        expectedException.expectMessage("- Skal ikke ha hashCode");

        assertThat(new Object(), new RequirementsMatcher<Object>("test RequirementsMatcher") {
            @Override
            protected void checkRequirementsFor(Object o) {
                checkIf("Skal være like", o, is(equalTo(new Object())));
                checkIf("Skal ikke ha hashCode", o.hashCode(), is(equalTo(0)));
            }
        });
    }

    @Test
    public void willHaveExpectedAndRealResultOfTheMatchingInAssertionErrorMessage() {
        expectedException.expect(AssertionError.class);
        expectedException.expectMessage(
                "- expected: is \"nu.hjemme.test.RequirementsMatcherTest\", got: \"java.lang.Object\""
        );

        assertThat(new Object(), new RequirementsMatcher<Object>("test RequirementsMatcher") {
            @Override
            protected void checkRequirementsFor(Object o) {
                checkIf("Skal være like meldinger", "svada", is(equalTo("lada")));
            }
        });
    }
}
