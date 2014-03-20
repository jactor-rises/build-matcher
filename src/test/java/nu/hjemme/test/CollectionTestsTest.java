package nu.hjemme.test;

import org.junit.Test;

/** @author Tor Egil Jacobsen */
public class CollectionTestsTest {

    @Test
    public void willHaveHashCodeImplementedCorrect() {
        BeanWithHashCodeAndEquals base = new BeanWithHashCodeAndEquals();
        BeanWithHashCodeAndEquals equalToBase = new BeanWithHashCodeAndEquals();
        BeanWithHashCodeAndEquals notEqualToBase = new BeanWithHashCodeAndEquals();
        notEqualToBase.property = true;

        CollectionTests.assertThatHashCodeIsImplementedCorrect(base, equalToBase, notEqualToBase);
    }

    @Test(expected = AssertionError.class)
    public void skalIkkeHaHashCodeImplementertRiktigNarObjektSomSkalVareUliktErLikt() {
        BeanWithHashCodeAndEquals base = new BeanWithHashCodeAndEquals();
        BeanWithHashCodeAndEquals equalToBase = new BeanWithHashCodeAndEquals();
        BeanWithHashCodeAndEquals notEqualToBaseEqual = new BeanWithHashCodeAndEquals();

        CollectionTests.assertThatHashCodeIsImplementedCorrect(base, equalToBase, notEqualToBaseEqual);
    }

    @Test
    public void willHaveEqualsImplementedCorrect() {
        BeanWithHashCodeAndEquals base = new BeanWithHashCodeAndEquals();
        BeanWithHashCodeAndEquals equalToBase = new BeanWithHashCodeAndEquals();
        BeanWithHashCodeAndEquals notEqualToBase = new BeanWithHashCodeAndEquals();
        notEqualToBase.property = true;

        CollectionTests.assertThatEqualsIsImplementedCorrect(base, equalToBase, notEqualToBase);
    }

    @Test(expected = AssertionError.class)
    public void skalIkkeHaEqualsImplementertRiktigNarObjektSomSkalVaereUliktErLikt() {
        BeanWithHashCodeAndEquals base = new BeanWithHashCodeAndEquals();
        BeanWithHashCodeAndEquals equalToBase = new BeanWithHashCodeAndEquals();
        BeanWithHashCodeAndEquals notEqualToBaseEqual = new BeanWithHashCodeAndEquals();

        CollectionTests.assertThatEqualsIsImplementedCorrect(base, equalToBase, notEqualToBaseEqual);
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
