package nu.hjemme.test.matcher;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import static nu.hjemme.test.matcher.DescriptionMatcher.is;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.sameInstance;

public final class EqualsMatcher extends BaseMatcher<Object> {
    private static final String ALWAYS_TRUE = "This should always true; same instances are equal and different types are not equal";
    private static final String UNEQUAL_BEAN_EQUAL_TO_BASE_BEAN = "Unequal bean equal to base bean?";

    static final String NOT_SAME_INSTANCE = "The objects being tested should not be the same instance";
    static final String IS_EQUAL_WITH_HINT = "Not equal? Hint: base bean equal to the equal bean, but not vice versa: use 'getClass() ==' not 'instance of'";

    private final Object shouldBeEqual;
    private final Object shouldBeUnequal;

    private EqualsMatcher(Object shouldBeEqual, Object shouldBeUnequal) {
        this.shouldBeEqual = shouldBeEqual;
        this.shouldBeUnequal = shouldBeUnequal;
    }

    @Override
    public boolean matches(Object item) {
        return new TypeSafeBuildMatcher<Object>("Correct implementation of equals") {
            @Override public MatchBuilder matches(Object typeToTest, MatchBuilder matchBuilder) {
                return matchBuilder
                        .matches(typeToTest, is(allOf(equalTo(typeToTest), not(equalTo(new OtherType()))), ALWAYS_TRUE))
                        .matches(typeToTest, is(equalTo(shouldBeEqual), IS_EQUAL_WITH_HINT + "/vice "))
                        .matches(shouldBeEqual, is(equalTo(typeToTest), IS_EQUAL_WITH_HINT + "/versa"))
                        .matches(typeToTest, is(not(sameInstance(shouldBeEqual)), NOT_SAME_INSTANCE))
                        .matches(typeToTest, is(not(equalTo(shouldBeUnequal)), UNEQUAL_BEAN_EQUAL_TO_BASE_BEAN));
            }
        }.matches(item);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("Matching of equals method according to java specifications");
    }

    public static EqualsMatcher hasImplenetedEqualsMethodUsing(Object equalBean, Object unequalBean) {
        return new EqualsMatcher(equalBean, unequalBean);
    }

    private static class OtherType {

    }
}
