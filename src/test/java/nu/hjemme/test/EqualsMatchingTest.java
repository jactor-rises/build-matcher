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
public class EqualsMatchingTest {
    private EqualsMatching testEqualsMatching;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void before() {
        expectedException.handleAssertionErrors();
    }

    @Before
    public void initForTesting() throws Exception {
        testEqualsMatching = new EqualsMatching(new Bean(true));
    }

    @Test
    public void skalFeileNarManSammenlignerBonnerSomErUlikeForLikhet() {
        expectedException.expect(AssertionError.class);
        expectedException.handleAssertionErrors();

        assertFalse(testEqualsMatching.isEqualTo(new Bean(false)).isMatch());
    }

    @Test
    public void skalMatcheNarManSammenlignerBonnerSomErLikeForLikhet() {
        assertTrue(testEqualsMatching.isEqualTo(new Bean(true)).isMatch());
    }

    @Test
    public void skalFeileNarManSammenlignerBonnerSomErLikeForUlikhet() {
        expectedException.expect(AssertionError.class);
        expectedException.handleAssertionErrors();

        assertFalse(testEqualsMatching.isNotEqualTo(new Bean(true)).isMatch());
    }

    @Test
    public void skalMatcheNarManSammenlignerBonnerSomErUlikeForUlikhet() {
        assertTrue(testEqualsMatching.isNotEqualTo(new Bean(false)).isMatch());
    }

    @Test
    public void skalSjekkeAtLikeObjektHarUlikMinnereferanse() {
        expectedException.expectMessage(EqualsMatching.NOT_SAME_INSTANCE);
        Bean equalBean = new Bean(true);

        new EqualsMatching(equalBean).isEqualTo(equalBean).isMatch();
    }

    private static class Bean {
        private final boolean booleanProperty;

        private Bean(boolean booleanProperty) {
            this.booleanProperty = booleanProperty;
        }

        @Override
        public boolean equals(Object o) {
            return this == o || (o instanceof Bean && booleanProperty == ((Bean) o).booleanProperty);
        }

        @Override
        public int hashCode() {
            return (booleanProperty ? 1 : 2);
        }
    }
}
