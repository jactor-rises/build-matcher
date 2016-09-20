package com.github.jactorrises.matcher;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.github.jactorrises.matcher.EqualsMatcher.hasImplenetedEqualsMethodUsing;
import static org.junit.Assert.assertThat;

public class EqualsMatcherTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldVerifyEqualsMethodWhenGivenOneObjectWhichIsEqualAndAnotherObjectWhicIsNotEqualToBeanGettingVerified() {
        assertThat(new EqualsBean(true), hasImplenetedEqualsMethodUsing(new EqualsBean(true), new EqualsBean(false)));
    }

    @Test
    public void shouldFailEqualsMatchingWhenNotVerifyingWithOneEqualBeanAndOneBeanWhichIsNotEqual() {
        expectedException.expect(AssertionError.class);

        assertThat(new EqualsBean(true), hasImplenetedEqualsMethodUsing(new EqualsBean(true), new EqualsBean(true)));
    }

    @Test
    public void shouldFailEqualMethodTestingWhenTheEqualBeansAreTheSameInstance() {
        expectedException.expect(AssertionError.class);
        expectedException.expectMessage(EqualsMatcher.NOT_SAME_INSTANCE);

        EqualsBean instanceOfEqualsBean = new EqualsBean(true);

        assertThat(instanceOfEqualsBean, hasImplenetedEqualsMethodUsing(instanceOfEqualsBean, new EqualsBean(false)));
    }

    private final static class EqualsBean {
        private final boolean booleanProperty;

        private EqualsBean(boolean booleanProperty) {
            this.booleanProperty = booleanProperty;
        }

        @Override
        public boolean equals(Object o) {
            return this == o || (o != null && o.getClass() == EqualsBean.class && booleanProperty == ((EqualsBean) o).booleanProperty);
        }

        @Override
        public int hashCode() {
            return (booleanProperty ? 1 : 2);
        }
    }
}
