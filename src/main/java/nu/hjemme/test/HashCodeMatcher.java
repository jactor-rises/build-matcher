package nu.hjemme.test;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import static nu.hjemme.test.DescriptionMatcher.is;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.sameInstance;

public final class HashCodeMatcher extends BaseMatcher<Object> {
    private static final String OBJECTS_CAN_NOT_BE_EQUAL = "The object said to be unequal can not be equal";
    private static final String OBJECTS_MUST_BE_EQUAL = "The two objects must be equal";
    private static final String OBJECTS_SHOULD_NOT_BE_THE_SAME_INSTANCE = "The objects being tested should not be the same instance";

    static final String EQUAL_FOR_CONSECUTIVE_CALLS = "The hash code should be equal for consecutive calls";
    static final String EQUAL_BEANS_UNEQUAL_HASH_CODE = "Two equal objects must have equal hash code";
    static final String UNEQUAL_OBJECTS_EQUAL_HASH_CODE = "Two unequal objects should not have the same hash code. Note: this is not an requirement, but a recommendation.";

    private final Object equalBean;
    private final Object unequalBean;

    public HashCodeMatcher(Object equalBean, Object unequalBean) {
        this.equalBean = equalBean;
        this.unequalBean = unequalBean;
    }

    public static HashCodeMatcher hasImplementedHashCodeAccordingTo(Object equalBean, Object unequalBean) {
        return new HashCodeMatcher(equalBean, unequalBean);
    }

    @Override
    public boolean matches(Object item) {

        Object hashCode = item != null ? item.hashCode() : 0;
        Object consecutiveHashCode = item != null ? item.hashCode() : 0;
        Object hashCodeEqual = equalBean != null ? equalBean.hashCode() : 0;
        Object hashCodeUnequal = unequalBean != null ? unequalBean.hashCode() : 0;

        BuildMatcher<Object> buildMatcher = new BuildMatcher<>(
                is(allOf(equalTo(hashCode), equalTo(consecutiveHashCode)), EQUAL_FOR_CONSECUTIVE_CALLS),
                is(equalTo(hashCodeEqual), EQUAL_BEANS_UNEQUAL_HASH_CODE),
                is(not(equalTo(hashCodeUnequal)), UNEQUAL_OBJECTS_EQUAL_HASH_CODE)
        );

        buildMatcher.appendMatchBuild(item, is(equalTo(equalBean)), OBJECTS_MUST_BE_EQUAL);
        buildMatcher.appendMatchBuild(item, is(not(sameInstance(equalBean))), OBJECTS_SHOULD_NOT_BE_THE_SAME_INSTANCE);
        buildMatcher.appendMatchBuild(item, is(not(unequalBean)), OBJECTS_CAN_NOT_BE_EQUAL);

        return buildMatcher.matches(hashCode);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("Implementation of hashCode() according to the java specification");
    }
}
