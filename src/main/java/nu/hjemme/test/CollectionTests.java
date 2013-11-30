package nu.hjemme.test;

import org.hamcrest.Matcher;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

/** Tests for beans being used in a {@link java.util.Collection} */
public final class CollectionTests {
    private CollectionTests() {
    }

    public static <T> void assertThatEqualsIsImplementedCorrect(T base, T equalToBase, T notEqualToBase) {

        assertThat(base, hasImplementedTheEqualsMethodAccordingToTheJavaSpecificationWhenUsing(
                equalToBase,
                notEqualToBase
        ));
    }

    private static Matcher<Object> hasImplementedTheEqualsMethodAccordingToTheJavaSpecificationWhenUsing(
            final Object equalToBase, final Object notEqualToBase
    ) {
        return new EqualsRequirements(equalToBase, notEqualToBase);
    }

    public static <T> void assertThatHashCodeIsImplementedCorrect(T base, T equalToBase, T notEqualToBase) {

        assertThat(base, hasImplementedTheHashCodeMethodAccordingToTheJavaSpecificationWhenUsing(
                equalToBase,
                notEqualToBase
        ));
    }

    private static Matcher<Object> hasImplementedTheHashCodeMethodAccordingToTheJavaSpecificationWhenUsing(
            final Object equalToBase,
            final Object notEqualToBase
    ) {
        return new HashCodeRequirements(equalToBase, notEqualToBase);
    }

    private static final class EqualsRequirements extends RequirementsMatcher<Object> {
        private final Object equalToBase;
        private final Object notEqualToBase;

        public EqualsRequirements(Object equalToBase, Object notEqualToBase) {
            super("");
            this.equalToBase = equalToBase;
            this.notEqualToBase = notEqualToBase;
        }

        @Override
        protected void checkRequirementsFor(Object base) {
            checkIf("Should be equal: base and base", base, is(equalTo(base)));
            checkIf("Should be equal: base and equalToBase", base, is(equalTo(equalToBase)));

            checkIf("Should not have the same memory address: base and equalToBase", base,
                    is(not(sameInstance(equalToBase))));

            checkIf("Should not have the same memory address: base and notEqualToBase", base,
                    is(not(sameInstance(notEqualToBase))));

            checkIf("Should not be equal to a different type: base", base,
                    is(not(equalTo((Object) new CollectionTests()))));

            checkIf("Should not be equal: base and notEqualToBase", base, is(not(equalTo(notEqualToBase))));
        }
    }

    private static final class HashCodeRequirements extends RequirementsMatcher<Object> {
        private final Object equalToBase;
        private final Object notEqualToBase;

        private HashCodeRequirements(Object equalToBase, Object notEqualToBase) {
            super("");
            this.equalToBase = equalToBase;
            this.notEqualToBase = notEqualToBase;
        }

        @SuppressWarnings(value = "unchecked")
        @Override
        protected void checkRequirementsFor(Object base) {
            checkIf("Should be equal: base and equalToBase", base, is(equalTo(equalToBase)));
            checkIf("Should not be equal: base and notEqualToBase", base, is(not(equalTo(notEqualToBase))));

            int baseHash = base.hashCode();
            int equalHash = equalToBase.hashCode();
            int notEqualHash = notEqualToBase.hashCode();

            checkIf("Should have a hash code greater than 0: base", baseHash, is(not(equalTo(0))));
            checkIf("Should have a hash code greater than 0: notEqualToBase", notEqualHash,
                    is(not(equalTo(0))));

            assertThat("Should be equal: base.hashCode() and equalToBase.hashCode()", baseHash, is(equalTo(equalHash)));
            assertThat("Should not be equal: base.hashCode() and notEqualToBase.hashCode()", baseHash,
                    is(not(notEqualHash)));
        }
    }
}
