package nu.hjemme.test;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.List;

import static java.util.Collections.emptyList;
import static nu.hjemme.test.MatchBuilder.provideExpectedVsRealValue;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.AnyOf.anyOf;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.contains;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MatchBuilderTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void skalMatcheDersomHamcrestMatchereGirEnMatch() {
        Object obj = new Object();

        assertTrue(new MatchBuilder("Object er ikke likt en annen instans av Object")
                        .matches(obj, is(not(equalTo(new Object()))), "En instans av java.lang.Object skal ikke vare lik en annen instans")
                        .isMatch()
        );
    }

    @Test
    public void skalKunneLeggeTilFeilmeldingerForHverEnkelSjekk() {
        expectedException.expect(AssertionError.class);
        expectedException.handleAssertionErrors();
        expectedException.expectMessage("hashcode");
        expectedException.expectMessage("likhet");
        expectedException.expectMessage("toString()");

        Object obj = new Object();

        assertTrue(new MatchBuilder("Alle feilmeldinger til konsoll")
                        .matches(obj.hashCode(), is(equalTo(0)), "hashcode")
                        .matches(obj, is(equalTo(new Object())), "likhet")
                        .matches(obj.toString().equals("mitt objekt"), "toString()")
                        .isMatch()
        );
    }

    @Test
    public void skalKunneAvslutteMatchBuilderUtenException() {
        expectedException.expect(AssertionError.class);
        expectedException.handleAssertionErrors();
        expectedException.expectMessage("avoid NullpointerException");
        expectedException.expectMessage(not(contains("skal ikke skje")));

        Object obj = new Object();

        assertTrue(new MatchBuilder("NullPointer exception skal ikke komme")
                        .failIfMatch(obj, obj, "avoid NullpointerException")
                        .matches(throwNullPointerException(), "skal ikke skje")
                        .isMatch()
        );
    }

    private boolean throwNullPointerException() {
        throw new NullPointerException("testfeil");
    }

    @Test
    public void skalKunneSeToStringPaaObjektreferanserSomSjekkesForLikhet() {
        expectedException.expect(AssertionError.class);
        expectedException.handleAssertionErrors();
        expectedException.expectMessage(" [expected: ");
        expectedException.expectMessage("| real: ");

        Object obj = new Object();

        assertTrue(new MatchBuilder("Skal kunne se toString nar likhet sjekkes")
                        .matches(obj, is(equalTo(new Object())), "real vs. expected")
                        .isMatch()
        );
    }

    @Test
    public void skalKunneBrukeMatchersForAsserter() {
        expectedException.expect(AssertionError.class);
        expectedException.handleAssertionErrors();
        expectedException.expectMessage("expected: (<101> or <0>)");

        Object obj = new Object();

        assertTrue(new MatchBuilder("Bruk av org.hamcrest.Matchers")
                        .matches(obj.hashCode(), anyOf(equalTo(101), equalTo(0)), "uventet hashcode")
                        .isMatch()
        );
    }

    @Test
    public void skalFeileUmiddelbartMedMatchOgBrukAvHamcrestMatchers() {
        expectedException.expect(AssertionError.class);
        expectedException.handleAssertionErrors();
        expectedException.expectMessage("Objektene skal vare like (objektene er samme instans og feiler derfor)");

        Object object = new Object();
        new MatchBuilder().failIfMatch(object, is(equalTo(object)), "Objektene skal vare like (objektene er samme instans og feiler derfor)");
    }

    @Test
    public void skalIkkeFeileMedMatchOgBrukAvHamcrestMatchers() {
        new MatchBuilder().failIfMatch(new Object(), is(equalTo(new Object())), "Objektene skal ikke vare like (objektene er ikke like og feiler ikke)");
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
                        .matches(item.a == 1)
                        .matches(item.b, is(equalTo(2)))
                        .isMatch();
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("sjekker verdier pa argument");
            }
        }));
    }

    @Test
    public void skalKunneSjekkeArgumentverdierMedMockito() {
        expectedException.expect(AssertionError.class);
        expectedException.handleAssertionErrors();
        expectedException.expectMessage("matchBuilderTest.sjekkArgumentverdierPa(");
        expectedException.expectMessage("sjekker verdier pa argument");

        Argument argument = new Argument();
        argument.a = 1;
        argument.b = 1;

        MatchBuilderTest mockedMatchBuilderTest = mock(MatchBuilderTest.class);
        mockedMatchBuilderTest.sjekkArgumentverdierPa(argument);

        verify(mockedMatchBuilderTest).sjekkArgumentverdierPa(argThat(new TypeSafeMatcher<Argument>() {
            @Override
            protected boolean matchesSafely(Argument item) {
                return new MatchBuilder()
                        .matches(item.a == 1)
                        .matches(item.b, is(equalTo(2)))
                        .isMatch();
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("sjekker verdier pa argument");
            }
        }));
    }

    @SuppressWarnings("unused") // argumentet
    protected void sjekkArgumentverdierPa(Argument argument) {
        // mockes med mocito
    }

    @Test
    public void skalIkkeFeileVedMismatchNarDetErMatchMellomVerdier() {
        new MatchBuilder().failIfMismatch(1, 1, "Skal feile hvis verdiene er ulike. Det er de ikke og derfor feiler det ikke");
    }

    @Test
    public void skalFeileVedMismatchNarDetErMismatchMellomVerdier() {
        expectedException.expect(AssertionError.class);
        expectedException.handleAssertionErrors();
        expectedException.expectMessage("Skal feile hvis verdiene er ulike. Det er de og derfor feiler det");

        new MatchBuilder().failIfMismatch(1, is(equalTo(2)), "Skal feile hvis verdiene er ulike. Det er de og derfor feiler det");
    }

    @Test
    public void skalKunneLageRealVsExpectedMismatchDescriptionNarEnkelMismatchMedBooleanBrukes() {
        expectedException.expect(AssertionError.class);
        expectedException.handleAssertionErrors();
        expectedException.expectMessage("boolean test som feiler [expected: en tom liste | real: nada]");

        assertThat(new Object(), new TypeSafeDiagnosingMatcher<Object>() {
            @Override
            protected boolean matchesSafely(Object obj, Description mismatchDescription) {
                return new MatchBuilder()
                        .matches(false, "boolean test som feiler" + provideExpectedVsRealValue("en tom liste", "nada"))
                        .isMatch();
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("real vs expected");
            }
        });
    }

    @Test
    public void skalFeileVedMatchPaaBooleanVerdier() {
        expectedException.expect(AssertionError.class);
        expectedException.handleAssertionErrors();
        expectedException.expectMessage("lista skal ikke vare tom");

        assertThat(emptyList(), new TypeSafeDiagnosingMatcher<List<Object>>() {
            @Override
            public boolean matchesSafely(List<Object> item, Description mismatchDescription) {
                return new MatchBuilder().failIfMatch(item.isEmpty(), "lista skal ikke vare tom").isMatch();
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("feil grunnet tom liste");
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
}
