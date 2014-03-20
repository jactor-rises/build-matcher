package nu.hjemme.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.sameInstance;

/** Tests for beans being used in a {@link java.util.Collection} */
public final class CollectionTests {
    private CollectionTests() {
    }

    public static <T> void assertThatEqualsIsImplementedCorrect(T base, T equalToBase, T notEqualToBase) {

        new MatchBuilder()
                .matches(base, is(equalTo(base)), "base skal vaere lik seg selv")
                .matches(base, is(equalTo(equalToBase)), "base skal vaere lik equalToBase")
                .matches(base, is(not(equalTo(notEqualToBase))), "base skal ikke vaere lik notEqualToBase")
                .matches(base, is(not(sameInstance(equalToBase))), "base skal ha ulik minneadresse enn equalToBase")
                .matches(base, is(not(sameInstance(notEqualToBase))), "base skal ha ulik minneadresse enn notEqualToBase")
                .matches(base, is(not(equalTo((Object) new CollectionTests()))), "base skal være ulik en annen type")
                .isMatch();
    }

    public static <T> void assertThatHashCodeIsImplementedCorrect(T base, T equalToBase, T notEqualToBase) {

        int baseHash = base.hashCode();
        int equalHash = equalToBase.hashCode();
        int notEqualHash = notEqualToBase.hashCode();

        new MatchBuilder()
                .matches(base, is(equalTo(equalToBase)), "base og equalToBase ma vaere like")
                .matches(base, is(not(equalTo(notEqualToBase))), "base og notEqualToBase kan ikke vaere like")
                .matches(baseHash, is(not(equalTo(0))), "hash code skal ikke vaere 0")
                .matches(baseHash, is(equalTo(equalHash)), "base skal ha hashcode lik equalToBase")
                .matches(baseHash, is(not(equalTo(notEqualHash))), "base skal ikke ha hashcode lik notEqualToBase")
                .isMatch();
    }
}
