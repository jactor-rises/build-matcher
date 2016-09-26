# build-matcher

### Main goal of project
How to do many matches using only one assertThat in a junit-test
* `TypeSafeBuildMatcher`

### Acknowledgements
This code is build on top of hamcrest matchers, specifically: **`org.hamcrest.TypeSafeMatcher`** and **`org.hamcrest.core.Is`**

### Side effects
* `EqualsMatcher` - tests the implementation of the equals method according to the java specification
* `HashCodeMatcher` - tests the implementation of the hashCode method according to the java specification
* `LabelMatcher` - extends `org.hamcrest.core.Is` and provides labeling of standard `org.hamcrest.Matcher` failure messages.
* `ToStringEditor` - provides customized toString messages of evaluated objects

### Releases
* v1.0 - can be used with jdk1.4 and greater
* v1.1 - can be used with jdk1.8 and greater