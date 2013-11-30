package nu.hjemme.test;

import org.junit.Test;

/** @author Tor Egil Jacobsen */
public class CollectionTestsTest {

    @Test
    public void willHaveEqualAndHashCodeImplementedCorrect() {
        BeanWithHashCodeAndEquals base = new BeanWithHashCodeAndEquals();
        BeanWithHashCodeAndEquals equalToBase = new BeanWithHashCodeAndEquals();
        BeanWithHashCodeAndEquals notEqualToBase = new BeanWithHashCodeAndEquals();
        notEqualToBase.property = true;

        CollectionTests.assertThatEqualsIsImplementedCorrect(base, equalToBase, notEqualToBase);
        CollectionTests.assertThatHashCodeIsImplementedCorrect(base, equalToBase, notEqualToBase);
    }

    private class BeanWithHashCodeAndEquals {
        boolean property = false;

        @Override
        public boolean equals(Object o) {
            return this == o ||
                    (o instanceof BeanWithHashCodeAndEquals && property == ((BeanWithHashCodeAndEquals) o).property);

        }

        @Override
        public int hashCode() {
            return (property ? 1 : 2);
        }
    }
}
