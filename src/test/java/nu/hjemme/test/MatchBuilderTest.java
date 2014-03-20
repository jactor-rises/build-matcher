package nu.hjemme.test;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;

import java.util.List;

import static java.util.Collections.emptyList;
import static nu.hjemme.test.MatchBuilder.provideExpectedVsRealValue;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.AnyOf.anyOf;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MatchBuilderTest {

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
        try {
            Object obj = new Object();

            assertTrue(new MatchBuilder("Alle feilmeldinger til konsoll")
                    .matches(obj.hashCode(), is(equalTo(0)), "hashcode skal vare 0")
                    .matches(obj, is(equalTo(new Object())), "objekt skal vare likt et annet objekt")
                    .matches(obj.toString().equals("mitt objekt"), "toString() skal vare implementert")
                    .isMatch()
            );

            fail("Forventet feil i assert");
        } catch (AssertionError assertionError) {
            assertThat("Feilbeskrivelser i feilmelding", assertionError.getMessage(), allOf(
                    containsString("hashcode skal vare 0"),
                    containsString("objekt skal vare likt et annet objekt"),
                    containsString("toString() skal vare implementert")
            ));
        }
    }

    @Test
    public void skalKunneAvslutteMatchBuilderUtenException() {
        try {
            Object obj = new Object();

            assertTrue(new MatchBuilder("NullPointer exception skal ikke komme")
                    .failIfMatch(obj, obj, "null pointer skal ikke skje")
                    .matches(throwNullPointerException(), "skal ikke skje")
                    .isMatch()
            );

            fail("Forventet feil i assert");
        } catch (AssertionError assertionError) {
            assertThat("Feilbeskrivelser i feilmelding", assertionError.getMessage(), containsString("null pointer skal ikke skje"));
        }
    }

    private boolean throwNullPointerException() {
        throw new NullPointerException("testfeil");
    }

    @Test
    public void skalKunneSeToStringPaaObjektreferanserSomSjekkesForLikhet() {
        try {
            Object obj = new Object();

            assertTrue(new MatchBuilder("Skal kunne se toString nar likhet sjekkes")
                    .matches(obj, is(equalTo(new Object())), "real vs. expected")
                    .isMatch()
            );

            fail("Forventet feil i assert");
        } catch (AssertionError assertionError) {
            assertThat("Feilbeskrivelser i feilmelding", assertionError.getMessage(), allOf(
                    containsString(" [expected: "),
                    containsString("| real: ")
            ));
        }
    }

    @Test
    public void skalKunneBrukeMatchersForAsserter() {
        try {
            Object obj = new Object();

            assertTrue(new MatchBuilder("Bruk av org.hamcrest.Matchers")
                    .matches(obj.hashCode(), anyOf(equalTo(101), equalTo(0)), "uventet hashcode")
                    .isMatch()
            );

            fail("Forventet feil i assert");
        } catch (AssertionError assertionError) {
            assertThat("Feilbeskrivelse i feilmelding", assertionError.getMessage(), containsString("expected: (<101> or <0>)"));
        }
    }

    @Test(expected = AssertionError.class)
    public void skalFeileMedMatchOgBrukAvHamcrestMatchers() {
        Object object = new Object();
        new MatchBuilder().failIfMatch(object, is(equalTo(object)), "Objektene skal vare like (objektene er samme instans og feiler derfor)");
    }

    @Test
    public void skalIkkeFeileMedMatchOgBrukAvHamcrestMatchers() {
        new MatchBuilder().failIfMatch(new Object(), is(equalTo(new Object())), "Objektene skal ikke vare like (objektene er ikke like og feiler ikke)");
    }

    @Test
    public void skalIkkeFeileNarArgumentSjekkesMedMockitoUtenDescription() {
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

    @Test(expected = AssertionError.class)
    public void skalKunneSjekkeArgumentverdierMedMockitoUtenDescription() {
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

    @Test(expected = AssertionError.class)
    public void skalFeileVedMismatchNarDetErMismatchMellomVerdier() {
        new MatchBuilder().failIfMismatch(1, is(equalTo(2)), "Skal feile hvis verdiene er ulike. Det er de og derfor feiler det");
    }

    @Test
    public void skalKunneLageRealVsExpectedMismatchDescriptionNarEnkelMismatchMedBooleanBrukes() {
        try {
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

            fail("Forventet feil i assert");
        } catch (AssertionError assertionError) {
            assertThat("Feilbeskrivelse i feilmelding", assertionError.getMessage(), containsString("boolean test som feiler [expected: en tom liste | real: nada]"));
        }
    }

    @Test
    public void skalFeileVedMatchPaaBooleanVerdier() {
        try {
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

            fail("Forventet feil i assert");
        } catch (AssertionError ae) {
            assertThat("Feilbeskrivelse i feilmelding", ae.getMessage(), containsString("lista skal ikke vare tom"));
        }
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
