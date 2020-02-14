package com.eis.smslibrary.random;

import com.eis.smslibrary.SMSPeer;
import com.google.i18n.phonenumbers.PhoneNumberUtil;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Test class for {@link RandomSMSPeerGenerator}.
 *
 * @author Riccardo De Zen
 */
@RunWith(Parameterized.class)
public class RandomSMSPeerGeneratorTest {

    /**
     * Whether {@link RandomSMSPeerGeneratorTest#logValidity(SMSPeer)} should print out the
     * validity of the generated peers to help with test clarity or not.
     */
    private static final boolean LOGGING_ENABLED = true;
    private static final String LOG_MESSAGE_TEMPLATE = "The peer is %s\nMessage: %s";

    private static final RandomSMSPeerGenerator testedGenerator = new RandomSMSPeerGenerator();

    private final String testedRegion;

    /**
     * Rule for getting test name.
     */
    @Rule
    public TestName name = new TestName();

    /**
     * @return parameters for the test:
     * - Region to test.
     */
    @Parameterized.Parameters(name = "{index}: {0}")
    public static Collection<String> data() {
        String[] supportedCountries = PhoneNumberUtil
                .getInstance()
                .getSupportedRegions()
                .toArray(new String[0]);
        return Arrays.asList(supportedCountries);
    }

    /**
     * @param testedRegion the country code for which we want to run the tests.
     */
    public RandomSMSPeerGeneratorTest(String testedRegion) {
        this.testedRegion = testedRegion;
    }

    //DEFAULT REGION TESTS -------------------------------------------------------------------------

    /**
     * Testing the ability to create a valid address for the default region.
     * This test is run together with the parameterized tests to quickly perform multiple runs.
     */
    @Test
    public void canCreateDefaultRegionValidAddress() {
        SMSPeer peer = new SMSPeer(testedGenerator.generateValidAddress());
        logValidity(peer);
        assertNull(peer.getInvalidityReason());
    }

    /**
     * Testing the ability to create an invalid address for the default region.
     * This test is run together with the parameterized tests to quickly perform multiple runs.
     */
    @Test
    public void canCreateInvalidAddress() {
        SMSPeer peer = new SMSPeer(testedGenerator.generateInvalidAddress());
        logValidity(peer);
        assertNotNull(peer.getInvalidityReason());
    }

    //SPECIFIC REGION TESTS ------------------------------------------------------------------------

    /**
     * Testing the ability to create a valid address for the tested region.
     */
    @Test
    public void canCreateValidAddress() {
        SMSPeer peer = new SMSPeer(testedGenerator.generateValidAddress(testedRegion));
        logValidity(peer);
        assertNull(peer.getInvalidityReason());
    }

    //PEER GENERATION TESTS ------------------------------------------------------------------------

    /**
     * The two following tests should be assumed passed only if the tests for SMSPeer passed.
     * Such tests depend on the result of the previous four.
     */
    @Test
    public void canCreateDefaultRegionValidPeer() {
        SMSPeer peer = testedGenerator.generateValidPeer();
        logValidity(peer);
        assertNull(peer.getInvalidityReason());
    }

    @Test
    public void canCreateValidPeer() {
        SMSPeer peer = testedGenerator.generateValidPeer(testedRegion);
        logValidity(peer);
        assertNull(peer.getInvalidityReason());
    }

    /**
     * Method printing the validity of a {@link SMSPeer} if
     * {@link RandomSMSPeerGeneratorTest#LOGGING_ENABLED} is {@code true}.
     *
     * @param peer The {@link SMSPeer} whose validity we want to print.
     */
    private static void logValidity(SMSPeer peer) {
        if (!LOGGING_ENABLED) return;
        final String
                valid = "valid.",
                invalid = "invalid, with reason: %s";

        String validity = (peer.getInvalidityReason() == null) ?
                valid :
                String.format(invalid, peer.getInvalidityReason());

        String message = String.format(LOG_MESSAGE_TEMPLATE, validity, peer.getInvalidityMessage());
        System.out.println(message);
    }
}
