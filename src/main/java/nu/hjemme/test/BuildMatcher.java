package nu.hjemme.test;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.internal.ReflectiveTypeFinder;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static nu.hjemme.test.DescriptionMatcher.is;

/**
 * A matcher which will perform with the {@link nu.hjemme.test.MatchBuilder}. So it is possible to do several matches in one assert without using
 * {@link nu.hjemme.test.TypeSafeBuildMatcher}. One drawbacks of using this, vs. {@link nu.hjemme.test.TypeSafeBuildMatcher} is that an exception will be thrown and
 * not be reported as an error. This will also cause other failure messages tho be hidden. Another drawback is that all asserting must be done on the same type as the one sent to
 * the <code>assertThat</code> and it is impossible to do invocations on the item being asserted.
 * @author Tor Egil Jacobsen
 */
public class BuildMatcher<T> extends TypeSafeMatcher<T> {
    private static final ReflectiveTypeFinder TYPE_FINDER = new ReflectiveTypeFinder("matchesSafely", 1, 0);

    private final MatchBuilder matchBuilder;
    private final List<Matcher<?>> matchers;

    public BuildMatcher(Matcher<?>... matchers) {
        this.matchers = new ArrayList<>(asList(matchers));
        matchBuilder = new MatchBuilder("Matches on " + TYPE_FINDER.findExpectedType(getClass()).getSimpleName() + "!");
    }

    @SuppressWarnings("unchecked")
    @Override
    protected boolean matchesSafely(T item) {
        for (Matcher matcher : matchers) {
            matchBuilder.matches(item, is(matcher, matcher.getClass().getSimpleName()));
        }

        return matchBuilder.isMatch();
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(matchBuilder.getExpectedValueMessage());
    }

    protected void appendMatchBuild(T type, Matcher<T> matcher, String mismatchDescription) {
        matchBuilder.matches(type, is(matcher, mismatchDescription));
    }

    public static <T> Matcher<T> matches(Matcher<T>... matcher) {
        return new BuildMatcher<>(matcher);
    }
}
