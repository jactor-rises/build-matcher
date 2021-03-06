package com.github.jactorrises.matcher;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import static com.github.jactorrises.matcher.LabelMatcher.is;
import static com.github.jactorrises.matcher.LambdaBuildMatcher.verify;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.sameInstance;

/**
 * The {@link EqualsMatcher} will do a thorough testing av an objects equals method according to the java specifications.
 */
public final class EqualsMatcher extends BaseMatcher<Object> {

    private static final OtherType OTHER_TYPE = new OtherType();
    private static final String ALWAYS_TRUE = "This should always be true; same instances are equal and different types are not equal";
    private static final String UNEQUAL_BEAN_EQUAL_TO_BASE_BEAN = "Unequal bean equal to base bean?";
    private static final String IS_EQUAL_WITH_HINT = "Not equal? Hint: base bean equal to the equal bean, but not vice versa: use 'getClass() ==' not 'instance of'";

    static final String NOT_SAME_INSTANCE = "The objects being tested should not be the same instance";

    private final Object shouldBeEqual;
    private final Object shouldNotBeEqual;

    private EqualsMatcher(Object shouldBeEqual, Object shouldNotBeEqual) {
        this.shouldBeEqual = shouldBeEqual;
        this.shouldNotBeEqual = shouldNotBeEqual;
    }

    @Override
    public boolean matches(Object item) {
        return verify("Matching of equals method according to java specifications", (object, matchBuilder) -> matchBuilder
                .matches(object, is(allOf(equalTo(object), not(equalTo(OTHER_TYPE))), ALWAYS_TRUE))
                .matches(object, is(equalTo(shouldBeEqual), IS_EQUAL_WITH_HINT + "/object.equals(shouldBeEqual)"))
                .matches(shouldBeEqual, is(equalTo(object), IS_EQUAL_WITH_HINT + "/shouldBeEqual.equals(object)"))
                .matches(object, is(not(sameInstance(shouldBeEqual)), NOT_SAME_INSTANCE))
                .matches(object, is(not(equalTo(shouldNotBeEqual)), UNEQUAL_BEAN_EQUAL_TO_BASE_BEAN))
        ).matches(item);
    }

    @Override
    public void describeTo(Description description) {
    }

    public static EqualsMatcher hasImplenetedEqualsMethodUsing(Object equalBean, Object unequalBean) {
        return new EqualsMatcher(equalBean, unequalBean);
    }

    private static class OtherType {

    }
}
