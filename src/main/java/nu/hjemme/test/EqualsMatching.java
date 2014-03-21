package nu.hjemme.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.sameInstance;

/** @author Tor Egil Jacobsen */
public class EqualsMatching extends MatchBuilder {
    static final String MINNEADRESSE = "base skal ha ulik minneadresse enn bonne som testes mot";

    private final Object base;

    public EqualsMatching(Object base) {
        this.base = base;
        matches(base, is(equalTo(base)), "base skal vere lik seg selv"); // dette slar aldri feil, men er med for coverage
    }

    public EqualsMatching isEqualTo(Object equal) {
        matches(base, is(equalTo(equal)), "base skal vere lik equal");
        matches(base, is(not(sameInstance(equal))), MINNEADRESSE);

        return this;
    }

    public EqualsMatching isNotEqualTo(Object notEqual) {
        matches(base, is(not(equalTo(notEqual))), "base skal ikke vaere lik notEqual");
        matches(base, is(not(sameInstance(notEqual))), MINNEADRESSE);
        matches(base, is(not(equalTo((Object) new OtherType()))), "base skal være ulik en annen type");

        return this;
    }

    private static class OtherType {
    }
}
