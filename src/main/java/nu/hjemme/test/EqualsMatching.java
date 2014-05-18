package nu.hjemme.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.sameInstance;

/** @author Tor Egil Jacobsen */
public class EqualsMatching extends MatchBuilder {
    private static final String ALWAYS_TRUE_COVERAGE = "Is always true, but implemented for test coverage";
    static final String NOT_SAME_INSTANCE = "The objects beeing tested should not be the same instance";
    static final String IS_EQUAL_WITH_HINT = "Is equal? Hint: if the base object is equal to the object " +
            "beeing tested against, but not vise versa: use 'getClass().equals(...)' not 'instance of";

    private final Object base;

    public EqualsMatching(Object base) {
        super("Implementation of equals(...) according to the java specification");
        this.base = base;
        matches(base, is(equalTo(base)), ALWAYS_TRUE_COVERAGE);
        matches(base, is(not(equalTo((Object) new OtherType()))), ALWAYS_TRUE_COVERAGE);
    }

    public EqualsMatching isEqualTo(Object equal) {
        matches(base, is(equalTo(equal)), IS_EQUAL_WITH_HINT);
        matches(equal, is(equalTo((base))), IS_EQUAL_WITH_HINT);
        matches(base, is(not(sameInstance(equal))), NOT_SAME_INSTANCE);

        return this;
    }

    public EqualsMatching isNotEqualTo(Object unequal) {
        matches(base, is(not(unequal)), "is unequal?");

        return this;
    }

    private static class OtherType {
    }
}
