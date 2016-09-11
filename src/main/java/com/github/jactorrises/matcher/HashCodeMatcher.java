package com.github.jactorrises.matcher;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import static com.github.jactorrises.matcher.DescriptionMatcher.is;
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
        return new TypeSafeBuildMatcher<Object>("Implementation of hashCode() according to the java specification") {
            @Override public MatchBuilder matches(Object typeToTest, MatchBuilder matchBuilder) {
                int hashCode = item != null ? item.hashCode() : 0;
                int consecutiveHashCode = item != null ? item.hashCode() : 0;
                int hashCodeEqual = equalBean != null ? equalBean.hashCode() : 0;
                int hashCodeUnequal = unequalBean != null ? unequalBean.hashCode() : 0;

                return matchBuilder
                        .matches(hashCode, is(equalTo(consecutiveHashCode), EQUAL_FOR_CONSECUTIVE_CALLS))
                        .matches(hashCode, is(equalTo(hashCodeEqual), EQUAL_BEANS_UNEQUAL_HASH_CODE))
                        .matches(hashCode, is(not(equalTo(hashCodeUnequal)), UNEQUAL_OBJECTS_EQUAL_HASH_CODE))
                        .matches(typeToTest, is(equalTo(equalBean), OBJECTS_MUST_BE_EQUAL))
                        .matches(typeToTest, is(not(sameInstance(equalBean)), OBJECTS_SHOULD_NOT_BE_THE_SAME_INSTANCE))
                        .matches(typeToTest, is(not(unequalBean), OBJECTS_CAN_NOT_BE_EQUAL));
            }
        }.matches(item);
    }

    @Override
    public void describeTo(Description description) { }
}
