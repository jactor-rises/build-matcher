package nu.hjemme.test.matcher;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import static nu.hjemme.test.matcher.DescriptionMatcher.is;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.sameInstance;

public final class EqualsMatcher extends BaseMatcher<Object> {
    private static final String ALWAYS_TRUE_COVERAGE = "Is always true, but implemented for test coverage";
    private static final String UNEQUAL_BEAN_EQUAL_TO_BASE_BEAN = "Unequal bean equal to base bean?";
    private static final ThreadLocal<EqualsMatcher> THREAD_LOCAL = new ThreadLocal<>();

    static final String NOT_SAME_INSTANCE = "The objects being tested should not be the same instance";
    static final String IS_EQUAL_WITH_HINT = "Is equal? Hint: if the base object is equal to the object being tested against, but not vise versa: " +
            "use 'getClass().equals(...)' not 'instance of'";

    private final Object shouldBeEqual;
    private final Object shouldBeUnequal;

    private EqualsMatcher(Object shouldBeEqual, Object shouldBeUnequal) {
        this.shouldBeEqual = shouldBeEqual;
        this.shouldBeUnequal = shouldBeUnequal;
    }

    @Override
    public boolean matches(Object item) {
        BuildMatcher<Object> equalsMatching = new BuildMatcher<>(is(equalTo(item), ALWAYS_TRUE_COVERAGE), is(not(equalTo((Object) new OtherType())), ALWAYS_TRUE_COVERAGE));
        equalsMatching.appendMatchBuild(item, is(equalTo(shouldBeEqual)), IS_EQUAL_WITH_HINT);
        equalsMatching.appendMatchBuild(shouldBeEqual, is(equalTo((item))), IS_EQUAL_WITH_HINT);
        equalsMatching.appendMatchBuild(item, is(not(sameInstance(shouldBeEqual))), NOT_SAME_INSTANCE);
        equalsMatching.appendMatchBuild(item, is(not(equalTo(shouldBeUnequal))), UNEQUAL_BEAN_EQUAL_TO_BASE_BEAN);

        return equalsMatching.matches(item);
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
