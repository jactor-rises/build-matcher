package nu.hjemme.test;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class TypeSafeBuildMatcherTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void handleAssertionErrors() {
        expectedException.handleAssertionErrors();
    }

    @Test
    public void skalKunneBrukesNarMatchingMedOrgJunitAssertAssertThatSamtMatchBuilderOgIkkeFeileNarRiktig() {
        assertThat(true, new TypeSafeBuildMatcher<Boolean>("skal vere true") {
            @Override
            public MatchBuilder matches(Boolean item, MatchBuilder matchBuilder) {
                return matchBuilder.matches(item, equalTo(true));
            }
        });
    }

    @Test
    public void skalKunneBrukesNarMatchingMedOrgJunitAssertAssertThatSamtMatchBuilderOgFeileVedFeil() {
        expectedException.expect(AssertionError.class);
        expectedException.expectMessage("skal vere false");

        assertThat(true, new TypeSafeBuildMatcher<Boolean>("skal vere false") {
            @Override
            public MatchBuilder matches(Boolean item, MatchBuilder matchBuilder) {
                return matchBuilder.matches(item, is(equalTo(false)), "prop skal vere false");
            }
        });
    }

    @Test
    public void skalIkkeMatcheNullInstans() {
        expectedException.expect(AssertionError.class);
        expectedException.expectMessage("skal feile uten NullpointerException ved null");

        assertThat(null, new TypeSafeBuildMatcher<Boolean>("skal feile uten NullpointerException ved null") {
            @Override
            public MatchBuilder matches(Boolean item, MatchBuilder matchBuilder) {
                throw new UnsupportedOperationException("skal aldri komme til denne metoden hvis instansen som testes er null");
            }
        });
    }

    @Test
    public void skalKasteAssertionErrorPaBakgrunnAvExceptionUnderMatching() {
        expectedException.expect(AssertionError.class);
        expectedException.expectMessage("Et exception skal bare feile NotNullBuildMatching");

        assertThat(true, new TypeSafeBuildMatcher<Boolean>("Et exception skal bare feile NotNullBuildMatching") {

            @Override
            public MatchBuilder matches(Boolean typeToTest, MatchBuilder matchBuilder) {
                throw new UnsupportedOperationException("Matchinbg som skaper exception");
            }
        });
    }

    @Test
    public void skalHaAssertionErrorMedMeldingFraOpprinneligExceptionUnderMatching() {
        expectedException.expect(AssertionError.class);
        expectedException.expectMessage("Exception som oppstod under matching");

        assertThat(true, new TypeSafeBuildMatcher<Boolean>("Et exception skal bare feile NotNullBuildMatching") {

            @Override
            public MatchBuilder matches(Boolean typeToTest, MatchBuilder matchBuilder) {
                throw new UnsupportedOperationException("Exception som oppstod under matching");
            }
        });
    }

    @Test
    public void skalHaAssertionErrorMedNavnetTilOpprinneligExceptionSomOppstodUnderMatching() {
        expectedException.expect(AssertionError.class);
        expectedException.expectMessage(UnsupportedOperationException.class.getName());

        assertThat(true, new TypeSafeBuildMatcher<Boolean>("Et exception skal bare feile NotNullBuildMatching") {

            @Override
            public MatchBuilder matches(Boolean typeToTest, MatchBuilder matchBuilder) {
                throw new UnsupportedOperationException("Exception som oppstod under matching");
            }
        });
    }

    @Test
    public void skalHaAssertionErrorMedEventuelleEldreMismatchNarExceptionKommerUnderMatching() {
        expectedException.expect(AssertionError.class);
        expectedException.expectMessage("En feil som fremdeles skal vises");

        assertThat(true, new TypeSafeBuildMatcher<Boolean>("Et exception skal bare feile NotNullBuildMatching") {

            @Override
            public MatchBuilder matches(Boolean typeToTest, MatchBuilder matchBuilder) {
                matchBuilder.matches(false, "En feil som fremdeles skal vises");
                throw new UnsupportedOperationException("Exception som oppstod under matching");
            }
        });
    }

    @Test
    public void skalHaLinjenummerTilDerHvorExceptionOppstod() {
        expectedException.expect(AssertionError.class);
        expectedException.expectMessage(UnsupportedOperationException.class.getName() + " occurred at line number " + 124);

        assertThat(true, new TypeSafeBuildMatcher<Boolean>("Feil som forteller hvor exception oppstod") {

            @Override
            public MatchBuilder matches(Boolean typeToTest, MatchBuilder matchBuilder) {
                throw new UnsupportedOperationException();
            }
        });
    }
}
