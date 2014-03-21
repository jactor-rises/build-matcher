package nu.hjemme.test;

import org.hamcrest.core.SubstringMatcher;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Tor Egil Jacobsen
 */
public class HashCodeMatchingTest {

    @Test
    public void skalSjekkeAtHashCodeIkkeErTalletNull() {
        try {
            new HashCodeMatching(new Bean(HashCodeType.ZERO)).isMatch();

            fail("forventet feil da bonne skal gi tallet null som hashCode");
        } catch (AssertionError ae) {
            assertThat(ae.getMessage(), new SubstringMatcher(HashCodeMatching.HASH_CODE_ER_TALLET_NULL) {
                @Override
                protected boolean evalSubstringOf(String string) {
                    return string.contains(HashCodeMatching.HASH_CODE_ER_TALLET_NULL);
                }

                @Override
                protected String relationship() {
                    return "feilmelding til HashCodeMatching";
                }
            });
        }
    }

    @Test
    public void skalSjekkeAtHashCodeGirSammeTallVedHvertKall() {
        try {
            new HashCodeMatching(new Bean(HashCodeType.RANDOM)).isMatch();

            fail("forventet feil da bonne skal gi forskjellig tall ved kall til hashCode");
        } catch (AssertionError ae) {
            assertThat(ae.getMessage(), new SubstringMatcher(HashCodeMatching.HASH_CODE_RANDOM_NUMBER) {
                @Override
                protected boolean evalSubstringOf(String string) {
                    return string.contains(HashCodeMatching.HASH_CODE_ER_TALLET_NULL);
                }

                @Override
                protected String relationship() {
                    return "feilmelding til HashCodeMatching";
                }
            });
        }
    }

    @Test(expected = AssertionError.class)
    public void skalFeileNarHashCodeErForskjelligPaBonnerSomSkalVereLike() {
        Bean base = new Bean(true);
        Bean notEqual = new Bean(false);

        assertFalse(new HashCodeMatching(base).isImplementedForEquality(notEqual).isMatch());
    }

    @Test
    public void skalMatcheNarHashCodeErLikPaBonnerSomSkalVereLike() {
        Bean base = new Bean(true);
        Bean equal = new Bean(true);

        assertTrue(new HashCodeMatching(base).isImplementedForEquality(equal).isMatch());
    }

    @Test(expected = AssertionError.class)
    public void skalFeileNarHashCodeErLikPaBonnerSomSkalVereUlike() {
        Bean base = new Bean(true);
        Bean equal = new Bean(true);

        assertFalse(new HashCodeMatching(base).isUniqueImplementation(equal).isMatch());
    }

    @Test
    public void skalMatcheNarHashCodeErUlikPaBonnerSomSkalVereUlike() {
        Bean base = new Bean(true);
        Bean notEqual = new Bean(false);

        assertTrue(new HashCodeMatching(base).isUniqueImplementation(notEqual).isMatch());
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
                case ZERO:
                    return 0;
                case RANDOM:
                    return (int) Math.random();
                default:
                    throw new UnsupportedOperationException("type (" + hashCodeType + ") er ikke støttet!");
            }
        }
    }

    private static enum HashCodeType {
        ZERO, RANDOM, NORMAL
    }
}
