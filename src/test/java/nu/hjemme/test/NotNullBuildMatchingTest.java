package nu.hjemme.test;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class NotNullBuildMatchingTest {

    @Test
    public void skalKunneBrukesNarMatchingMedOrgJunitAssertAssertThatSamtMatchBuilderOgIkkeFeileNarRiktig() {
        assertThat(true, new NotNullBuildMatching<Boolean>("skal vere true") {
            @Override
            public MatchBuilder matches(Boolean item, MatchBuilder matchBuilder) {
                return matchBuilder.matches(item, is(equalTo(true)));
            }
        });
    }

    @Test(expected = AssertionError.class)
    public void skalKunneBrukesNarMatchingMedOrgJunitAssertAssertThatSamtMatchBuilderOgFeileVedFeil() {
        assertThat(true, new NotNullBuildMatching<Boolean>("skal vere false") {
            @Override
            public MatchBuilder matches(Boolean item, MatchBuilder matchBuilder) {
                return matchBuilder.matches(item, is(equalTo(false)), "prop skal vere false");
            }
        });
    }

    @Test(expected = AssertionError.class)
    public void skalIkkeMatcheNullInstans() {
        assertThat(null, new NotNullBuildMatching<Boolean>("skal ikke feile ved null") {
            @Override
            public MatchBuilder matches(Boolean item, MatchBuilder matchBuilder) {
                throw new UnsupportedOperationException("skal aldri komme til denne metoden hvis instansen som testes er null");
            }
        });
    }
}
