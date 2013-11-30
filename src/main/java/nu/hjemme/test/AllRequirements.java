package nu.hjemme.test;

import org.hamcrest.Description;

import java.util.HashSet;
import java.util.Set;

/** @author Tor Egil Jacobsen */
class AllRequirements {

    private final Set<Requirement<?>> requirements = new HashSet<Requirement<?>>();
    private final String summary;

    private Description mismatchDescription;
    private Boolean allRequirementsMet;

    AllRequirements(String summary) {
        this.summary = summary;
    }

    void throwAssertionErrorIfNoRequirementsAreAdded() {
        if (requirements.isEmpty()) {
            throw new AssertionError("No requirements are added");
        }
    }

    boolean areRequirementsMet() {
        return allRequirementsMet;
    }

    void check(Requirement requirement) {
        requirements.add(requirement);
        checkIfMetAndDescribeWhenFailures(requirement);
    }

    private boolean checkIfMetAndDescribeWhenFailures(Requirement requirement) {
        boolean requirementMet = requirement.isMet();

        if (!requirementMet) {
            mismatchDescription.appendText("- " + requirement.createDescriptionOfRequirement());
            allRequirementsMet = false;
        } else {
            allRequirementsMet = allRequirementsMet == null || allRequirementsMet;
        }

        return requirementMet;
    }

    public void use(Description mismatchDescription) {
        this.mismatchDescription = mismatchDescription;
    }

    public String getSummary() {
        return summary;
    }
}
