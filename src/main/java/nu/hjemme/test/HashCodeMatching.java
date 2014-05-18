package nu.hjemme.test;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.sameInstance;

/** @author Tor Egil Jacobsen */
public class HashCodeMatching extends MatchBuilder {
    static final String EQUAL_FOR_CONSECUTIVE_CALLS = "The hash code should be equal for consecutive calls";
    static final String EQUAL_BEANS_UNEQUAL_HASH_CODE = "Two equal objects must have equal hash code";
    static final String UNEQUAL_OBJECTS_EQUAL_HASH_CODE = "Two unequal objects should not have the same " +
            "hash code. Note: this is not an requirement, but a recommendation.";

    private final Object base;

    public HashCodeMatching(Object base) {
        super("Implementation of hashCode() according to the java specification");
        this.base = base;

        matches(base.hashCode(), is(allOf(equalTo(base.hashCode()), equalTo(base.hashCode()))),
                EQUAL_FOR_CONSECUTIVE_CALLS
        );
    }

    public HashCodeMatching hasImplementionForEquality(Object equal) {
        matches(base, is(equalTo(equal)), "The two objects must be equal");
        matches(base, is(not(sameInstance(equal))), "The objects beeing tested should not be the same instance");
        matches(base.hashCode(), is(equalTo(equal.hashCode())), EQUAL_BEANS_UNEQUAL_HASH_CODE);

        return this;
    }

    public HashCodeMatching hasImplementationForUniqeness(Object another) {
        matches(base, is(not(another)), "The two objects can not be equal");
        matches(base.hashCode(), is(not(equalTo(another.hashCode()))), UNEQUAL_OBJECTS_EQUAL_HASH_CODE);

        return this;
    }
}
