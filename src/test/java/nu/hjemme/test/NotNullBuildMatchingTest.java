package nu.hjemme.test;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class NotNullBuildMatchingTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void skalKunneBrukesNarMatchingMedOrgJunitAssertAssertThatSamtMatchBuilderOgIkkeFeileNarRiktig() {
        assertThat(true, new NotNullBuildMatching<Boolean>("skal vere true") {
            @Override
            public MatchBuilder matches(Boolean item, MatchBuilder matchBuilder) {
                return matchBuilder.matches(item, is(equalTo(true)));
            }
        });
    }

    @Test
    public void skalKunneBrukesNarMatchingMedOrgJunitAssertAssertThatSamtMatchBuilderOgFeileVedFeil() {
        expectedException.expect(AssertionError.class);
        expectedException.handleAssertionErrors();
        expectedException.expectMessage("skal vere false");

        assertThat(true, new NotNullBuildMatching<Boolean>("skal vere false") {
            @Override
            public MatchBuilder matches(Boolean item, MatchBuilder matchBuilder) {
                return matchBuilder.matches(item, is(equalTo(false)), "prop skal vere false");
            }
        });
    }

    @Test
    public void skalIkkeMatcheNullInstans() {
        expectedException.expect(AssertionError.class);
        expectedException.handleAssertionErrors();
        expectedException.expectMessage("skal feile uten NullpointerException ved null");

        assertThat(null, new NotNullBuildMatching<Boolean>("skal feile uten NullpointerException ved null") {
            @Override
            public MatchBuilder matches(Boolean item, MatchBuilder matchBuilder) {
                throw new UnsupportedOperationException("skal aldri komme til denne metoden hvis instansen som testes er null");
            }
        });
    }
}
