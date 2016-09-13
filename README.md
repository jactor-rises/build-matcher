# build-matcher

Main goal of project:
How to do many matches using only one assertThat in a junit-test
* TypeSafeBuildMatcher

"Side effects":
* DescriptionMatcher - extends org.hamcrest.core.Is and provides labeling of standard org.hamcrest.Matcher failure messages.
* EqualsMatcher - tests the implementation of the equals method according to the java spec
* HashCodeMatcher - tests the implementation of the hashCode method according to the java spec
