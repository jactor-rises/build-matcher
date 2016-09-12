package com.github.jactorrises.matcher;

import static com.github.jactorrises.matcher.EqualsMatcher.hasImplenetedEqualsMethodUsing;

import static org.junit.Assert.assertThat;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class EqualsMatcherTest {
    private static final EqualsBean BASE = new EqualsBean(true);

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void skalFeileNarManSammenlignerBonnerSomErUlikeForLikhet() {
        expectedException.expect(AssertionError.class);

        assertThat(BASE, hasImplenetedEqualsMethodUsing(new EqualsBean(false), new EqualsBean(false)));
    }

    @Test
    public void skalMatcheNarManSammenlignerBonnerSomErLikeForLikhet() {
        assertThat(BASE, hasImplenetedEqualsMethodUsing(new EqualsBean(true), new EqualsBean(false)));
    }

    @Test
    public void skalFeileNarManSammenlignerBonnerSomErLikeForUlikhet() {
        expectedException.expect(AssertionError.class);

        assertThat(BASE, hasImplenetedEqualsMethodUsing(new EqualsBean(true), new EqualsBean(true)));
    }

    @Test
    public void skalSjekkeAtLikeObjektHarUlikMinnereferanse() {
        expectedException.expect(AssertionError.class);
        expectedException.expectMessage(EqualsMatcher.NOT_SAME_INSTANCE);

        EqualsBean equalEqualsBean = new EqualsBean(true);

        assertThat(equalEqualsBean, hasImplenetedEqualsMethodUsing(equalEqualsBean, new EqualsBean(false)));
    }

    private final static class EqualsBean {
        private final boolean booleanProperty;

        private EqualsBean(boolean booleanProperty) {
            this.booleanProperty = booleanProperty;
        }

        @Override
        public boolean equals(Object o) {
            return this == o || (o instanceof EqualsBean && booleanProperty == ((EqualsBean) o).booleanProperty);
        }

        @Override
        public int hashCode() {
            return (booleanProperty ? 1 : 2);
        }
    }
}
