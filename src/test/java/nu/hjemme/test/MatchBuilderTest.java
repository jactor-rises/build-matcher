package nu.hjemme.test;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static nu.hjemme.test.MatchBuilder.provideExpectedVsRealValue;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.AnyOf.anyOf;
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
                        .matches(obj, is(not(equalTo(new Object()))), "En instans av java.lang.Object skal ikke vare lik en annen instans")
                        .isMatch()
        );
    }

    @Before
    public void handleAssertionErrors() {
        expectedException.handleAssertionErrors();
    }

    @Test
    public void skalKunneLeggeTilFeilmeldingerForHverEnkelSjekk() {
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
    public void skalKunneSeToStringPaaObjektreferanserSomSjekkesForLikhet() {
        expectedException.expect(AssertionError.class);
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
        expectedException.expectMessage("expected: (<101> or <0>)");

        Object obj = new Object();

        assertTrue(new MatchBuilder("Bruk av org.hamcrest.Matchers")
                        .matches(obj.hashCode(), anyOf(equalTo(101), equalTo(0)), "uventet hashcode")
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
                        .matches(item.a == 1)
                        .matches(item.b, is(equalTo(2)))
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
        expectedException.expectMessage("Matching produced failures:");
        expectedException.expectMessage("'1' does not match 'is <2>'");

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
        expectedException.expectMessage("Skal feile hvis verdiene er ulike. Det er de og derfor feiler det");

        new MatchBuilder().failIfMismatch(1, is(equalTo(2)), "Skal feile hvis verdiene er ulike. Det er de og derfor feiler det");
    }

    @Test
    public void skalKunneLageRealVsExpectedMismatchDescriptionNarEnkelMismatchMedBooleanBrukes() {
        expectedException.expect(AssertionError.class);
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

    private static class Argument {
        int a;
        int b;

        @Override
        public String toString() {
            return "a=" + a + " og b=" + b;
        }
    }
}
