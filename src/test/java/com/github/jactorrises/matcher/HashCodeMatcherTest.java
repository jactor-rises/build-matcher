package com.github.jactorrises.matcher;

import static com.github.jactorrises.matcher.HashCodeMatcher.hasImplementedHashCodeAccordingTo;

import static org.junit.Assert.assertThat;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class HashCodeMatcherTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void skalFeileNarHashCodeGirUlikeTallVedHvertKall() {
        expectedException.expect(AssertionError.class);
        expectedException.expectMessage(HashCodeMatcher.EQUAL_FOR_CONSECUTIVE_CALLS);

        assertThat(new HashCodeBean(HashCodeType.RANDOM), hasImplementedHashCodeAccordingTo(new HashCodeBean(HashCodeType.RANDOM), new HashCodeBean(HashCodeType.RANDOM)));
    }

    @Test
    public void skalFeileNarHashCodeErForskjelligPaBonnerSomSkalVereLike() {
        expectedException.expect(AssertionError.class);
        expectedException.expectMessage(HashCodeMatcher.EQUAL_BEANS_UNEQUAL_HASH_CODE);

        assertThat(new HashCodeBean(true), hasImplementedHashCodeAccordingTo(new HashCodeBean(false), new HashCodeBean(false)));
    }

    @Test
    public void skalMatcheNarHashCodeErLikPaBonnerSomErLike() {
        assertThat(new HashCodeBean(true), hasImplementedHashCodeAccordingTo(new HashCodeBean(true), new HashCodeBean(false)));
    }

    @Test
    public void skalFeileNarHashCodeErLikPaBonnerSomErUlike() {
        expectedException.expect(AssertionError.class);
        expectedException.expectMessage(HashCodeMatcher.UNEQUAL_OBJECTS_EQUAL_HASH_CODE);

        assertThat(new HashCodeBean(true), hasImplementedHashCodeAccordingTo(new HashCodeBean(true), new HashCodeBean(true)));
    }

    private class HashCodeBean {
        private final boolean booleanProperty;
        private final HashCodeType hashCodeType;

        HashCodeBean(boolean booleanProperty, HashCodeType hashCodeType) {
            this.booleanProperty = booleanProperty;
            this.hashCodeType = hashCodeType;
        }

        HashCodeBean(boolean booleanProperty) {
            this(booleanProperty, HashCodeType.NORMAL);
        }

        HashCodeBean(HashCodeType hashCodeType) {
            this(false, hashCodeType);
        }

        @Override
        public boolean equals(Object o) {
            return this == o || (o instanceof HashCodeBean && booleanProperty == ((HashCodeBean) o).booleanProperty);
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

    private enum HashCodeType {
        RANDOM,
        NORMAL
    }
}
