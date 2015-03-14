package nu.hjemme.test;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static nu.hjemme.test.DescriptionMatcher.is;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MatchBuilderTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void skalMatcheDersomHamcrestMatchereGirEnMatch() {
        Object obj = new Object();

        assertTrue(new MatchBuilder("Object er ikke likt en annen instans av Object")
                        .matches(obj, is(not(equalTo(new Object())), "En instans av java.lang.Object skal ikke vare lik en annen instans"))
                        .isMatch()
        );
    }

    @Test
    public void skalKunneLeggeTilFeilmeldingerForHverEnkelSjekk() {
        expectedException.expectMessage("hashcode is");
        expectedException.expectMessage("likhet is");

        Object obj = new Object();

        assertTrue(new MatchBuilder("Alle feilmeldinger til konsoll")
                        .matches(obj.hashCode(), is(equalTo(0), "hashcode"))
                        .matches(obj, is(equalTo(new Object()), "likhet"))
                        .isMatch()
        );
    }

    @Test
    public void skalKunneSeToStringPaaObjektreferanserSomSjekkesForLikhet() {
        expectedException.expect(AssertionError.class);
        expectedException.expectMessage(" [expected: ");
        expectedException.expectMessage("| real: ");

        Object obj = new Object();

        assertTrue(new MatchBuilder("Skal kunne se toString nar likhet sjekkes")
                        .matches(obj, is(equalTo(new Object()), "real vs. expected"))
                        .isMatch()
        );
    }

    @Test
    public void skalKunneBrukeMatchersForAsserter() {
        expectedException.expect(AssertionError.class);
        expectedException.expectMessage("expected: uventet hashcode is (<101> or <0>) | real: ");

        Object obj = new Object();

        assertTrue(new MatchBuilder("Bruk av org.hamcrest.Matchers")
                        .matches(obj.hashCode(), is(anyOf(equalTo(101), equalTo(0)), "uventet hashcode"))
                        .isMatch()
        );
    }

    @Test
    public void skalIkkeFeileNarArgumentSjekkesMedMockito() {
        Argument argument = new Argument();
        argument.a = 1;
        argument.b = 2;

        MatchBuilderTest mockedMatchBuilderTest = mock(MatchBuilderTest.class);
        mockedMatchBuilderTest.sjekkArgumentverdierPa(argument);

        verify(mockedMatchBuilderTest).sjekkArgumentverdierPa(argThat(new TypeSafeMatcher<Argument>() {
            @Override
            protected boolean matchesSafely(Argument item) {
                return new MatchBuilder()
                        .matches(item.a, is(equalTo(1), "arg a"))
                        .matches(item.b, is(equalTo(2), "arg b"))
                        .isMatch();
            }

            @Override
            public void describeTo(Description description) {
            }
        }));
    }

    @Test
    public void skalKunneSjekkeArgumentverdierMedMockito() {
        expectedException.expect(AssertionError.class);
        expectedException.expectMessage("expected: b is <2> | real: \"1\"");

        Argument argument = new Argument();
        argument.a = 1;
        argument.b = 1;

        MatchBuilderTest mockedMatchBuilderTest = mock(MatchBuilderTest.class);
        mockedMatchBuilderTest.sjekkArgumentverdierPa(argument);

        verify(mockedMatchBuilderTest).sjekkArgumentverdierPa(argThat(new TypeSafeMatcher<Argument>() {
            @Override
            protected boolean matchesSafely(Argument item) {
                return new MatchBuilder()
                        .matches(item.a, is(equalTo(1), "a"))
                        .matches(item.b, is(equalTo(2), "b"))
                        .isMatch();
            }

            @Override
            public void describeTo(Description description) {
            }
        }));
    }

    protected void sjekkArgumentverdierPa(@SuppressWarnings("unused") Argument argument) {
        // mockes med mocito
    }

    @Test
    public void skalLeggeTilNummertypePaFeilmeldingerMedHeltallSomErAvLong() {
        expectedException.expect(AssertionError.class);
        expectedException.expectMessage("<1>");
        expectedException.expectMessage("1L");

        assertThat(1L, new TypeSafeBuildMatcher<Long>("nummerfeilmelding") {
            @Override
            public MatchBuilder matches(Long typeToTest, MatchBuilder matchBuilder) {
                return matchBuilder.matches(typeToTest, is(equalTo((Object) 1), "integer vs long"));
            }
        });
    }

    @Test
    public void skalIkkeHaAnforselstegnPaMatcher() {
        expectedException.expect(AssertionError.class);
        expectedException.expectMessage("1L");
        expectedException.expectMessage(not("\"is "));

        assertThat(1, new TypeSafeBuildMatcher<Integer>("ikke anforselsestegn pa Matcher") {
            @Override
            public MatchBuilder matches(Integer typeToTest, MatchBuilder matchBuilder) {
                return matchBuilder.matches(typeToTest, is(equalTo((Object) 1L), "integer vs long"));
            }
        });
    }

    @Test
    public void skalTilbyTilpassetToStringPaaReeltObjekt() {
        expectedException.expect(AssertionError.class);
        expectedException.expectMessage("value=");
        expectedException.expectMessage("Is ObjectWithoutToString");
        expectedException.expectMessage(not("ObjectWithoutToString@"));

        assertThat(new ObjectWithoutToString(), new TypeSafeBuildMatcher<ObjectWithoutToString>("forenklet toString pa matcher") {
            @Override
            public MatchBuilder matches(ObjectWithoutToString typeToTest, MatchBuilder matchBuilder) {
                ToStringEditor toStringEditor = new MyToStringEditor();
                return matchBuilder.matches(typeToTest, is(equalTo(new ObjectWithoutToString()), "to forskjellige counted values"), toStringEditor);
            }
        });
    }

    private static class Argument {
        int a;
        int b;

        @Override
        public String toString() {
            return "a=" + a + " og b=" + b;
        }
    }

    private static class ObjectWithoutToString {
        static int counter;
        int value = ++counter;
    }

    private static class MyToStringEditor extends ToStringEditor<ObjectWithoutToString> {

        public MyToStringEditor() {
            super(ObjectWithoutToString.class);
        }

        @Override
        public String toString(ObjectWithoutToString arg) {
            return "value=" + arg.value;
        }
    }
}
