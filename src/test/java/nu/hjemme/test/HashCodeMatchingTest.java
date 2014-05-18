package nu.hjemme.test;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Tor Egil Jacobsen
 */
public class HashCodeMatchingTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void before() {
        expectedException.handleAssertionErrors();
    }

    @Test
    public void skalSjekkeAtHashCodeGirSammeTallVedHvertKall() {
        expectedException.expectMessage(HashCodeMatching.EQUAL_FOR_CONSECUTIVE_CALLS);

        new HashCodeMatching(new Bean(HashCodeType.RANDOM)).isMatch();
    }

    @Test
    public void skalFeileNarHashCodeErForskjelligPaBonnerSomSkalVereLike() {
        expectedException.expectMessage(HashCodeMatching.EQUAL_BEANS_UNEQUAL_HASH_CODE);

        Bean base = new Bean(true);
        Bean notEqual = new Bean(false);

        assertFalse(new HashCodeMatching(base).hasImplementionForEquality(notEqual).isMatch());
    }

    @Test
    public void skalMatcheNarHashCodeErLikPaBonnerSomSkalVereLike() {
        Bean base = new Bean(true);
        Bean equal = new Bean(true);

        assertTrue(new HashCodeMatching(base).hasImplementionForEquality(equal).isMatch());
    }

    @Test
    public void skalFeileNarHashCodeErLikPaBonnerSomSkalVereUlike() {
        expectedException.expectMessage(HashCodeMatching.UNEQUAL_OBJECTS_EQUAL_HASH_CODE);

        Bean base = new Bean(true);
        Bean equal = new Bean(true);

        assertFalse(new HashCodeMatching(base).hasImplementationForUniqeness(equal).isMatch());
    }

    @Test
    public void skalMatcheNarHashCodeErUlikPaBonnerSomSkalVereUlike() {
        Bean base = new Bean(true);
        Bean notEqual = new Bean(false);

        assertTrue(new HashCodeMatching(base).hasImplementationForUniqeness(notEqual).isMatch());
    }

    private static class Bean {
        private final boolean booleanProperty;
        private final HashCodeType hashCodeType;

        Bean(boolean booleanProperty, HashCodeType hashCodeType) {
            this.booleanProperty = booleanProperty;
            this.hashCodeType = hashCodeType;
        }

        Bean(boolean booleanProperty) {
            this(booleanProperty, HashCodeType.NORMAL);
        }

        Bean(HashCodeType hashCodeType) {
            this(false, hashCodeType);
        }

        @Override
        public boolean equals(Object o) {
            return this == o || (o instanceof Bean && booleanProperty == ((Bean) o).booleanProperty);
        }

        @Override
        public int hashCode() {
            switch (hashCodeType) {
                case NORMAL:
                    return (booleanProperty ? 1 : 2);
                case RANDOM:
                    return (int) (Math.random() * 100000000);
                default:
                    throw new UnsupportedOperationException("type (" + hashCodeType + ") er ikke støttet!");
            }
        }
    }

    private static enum HashCodeType {
        RANDOM, NORMAL
    }
}
