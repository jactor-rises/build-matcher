package nu.hjemme.test;

import org.hamcrest.core.SubstringMatcher;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Tor Egil Jacobsen
 */
public class EqualsMatchingTest {
    private EqualsMatching testEqualsMatching;

    @Before
    public void initForTesting() throws Exception {
        testEqualsMatching = new EqualsMatching(new Bean(true));
    }

    @Test(expected = AssertionError.class)
    public void skalFeileNarManSammenlignerBonnerSomErUlikeForLikhet() {
        assertFalse(testEqualsMatching.isEqualTo(new Bean(false)).isMatch());
    }

    @Test
    public void skalMatcheNarManSammenlignerBonnerSomErLikeForLikhet() {
        assertTrue(testEqualsMatching.isEqualTo(new Bean(true)).isMatch());
    }

    @Test(expected = AssertionError.class)
    public void skalFeileNarManSammenlignerBonnerSomErLikeForUlikhet() {
        assertFalse(testEqualsMatching.isNotEqualTo(new Bean(true)).isMatch());
    }

    @Test
    public void skalMatcheNarManSammenlignerBonnerSomErUlikeForUlikhet() {
        assertTrue(testEqualsMatching.isNotEqualTo(new Bean(false)).isMatch());
    }

    @Test
    public void skalSjekkeAtLikeObjektHarUlikMinnereferanse() {
        Bean equalBean = new Bean(true);

        try {
            new EqualsMatching(equalBean).isEqualTo(equalBean).isMatch();

            fail("forventet feil da bonner har lik minneadresse");
        } catch (AssertionError ae) {
            assertThat(ae.getMessage(), new SubstringMatcher(EqualsMatching.MINNEADRESSE) {
                @Override
                protected boolean evalSubstringOf(String string) {
                    return string.contains(EqualsMatching.MINNEADRESSE);
                }

                @Override
                protected String relationship() {
                    return "feilmelding til EqualMatching";
                }
            });
        }
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
