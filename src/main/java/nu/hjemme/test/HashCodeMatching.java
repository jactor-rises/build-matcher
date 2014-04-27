package nu.hjemme.test;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

/** @author Tor Egil Jacobsen */
public class HashCodeMatching extends MatchBuilder {
    static final String HASH_CODE_SKAL_IKKE_VAERE_TALLET_NULL = "hash code skal ikke være 0";
    static final String HASH_CODE_HASH_CODE_SKAL_VAERE_LIK_FOR_HVERT_KALL = "hash code skal vøre lik for hvert kall";

    private final Object base;

    public HashCodeMatching(Object base) {
        super("Inplementasjon av hashCode() ihht. java spesifikasjon");
        this.base = base;
        matches(base.hashCode(), is(not(equalTo(0))), HASH_CODE_SKAL_IKKE_VAERE_TALLET_NULL);
        matches(base.hashCode(), is(allOf(equalTo(base.hashCode()), equalTo(base.hashCode()))), HASH_CODE_HASH_CODE_SKAL_VAERE_LIK_FOR_HVERT_KALL);
    }

    public HashCodeMatching isImplementedForEquality(Object equal) {
        matches(base, is(equalTo(equal)), "base og equal ma vere like");
        matches(base.hashCode(), is(equalTo(equal.hashCode())), "base skal ha hashcode lik equal");

        return this;
    }

    public HashCodeMatching isUniqueImplementation(Object notEqual) {
        matches(base, is(not(equalTo(notEqual))), "base og notEqual kan ikke vere like");
        matches(base.hashCode(), is(not(equalTo(notEqual.hashCode()))), "base skal ikke ha hashcode lik notEqual");

        return this;
    }
}
