package nu.hjemme.test;

/**
 * The verification types that is supported by {@link RequirementsMatcher}
 * ASSERT_THAT is used for verification using {@link org.junit.Assert#assertThat(Object, org.hamcrest.Matcher)} and
 * {@link org.junit.Assert#assertThat(String, Object, org.hamcrest.Matcher)}.
 * MOCKITO_VERIFY is used for verification using {@link org.hamcrest.Matcher} when verifying arguments when mocking with
 * {@link org.mockito.Mockito}.
 * @author Tor Egil Jacobsen
 */
public enum VerificationType {
    ASSERT_THAT, MOCKITO_VERIFY
}
