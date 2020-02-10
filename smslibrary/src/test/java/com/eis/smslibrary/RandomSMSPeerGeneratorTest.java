package com.eis.smslibrary;

import com.eis.random.RandomSMSPeerGenerator;
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

    private static final RandomSMSPeerGenerator GENERATOR = new RandomSMSPeerGenerator();

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
        SMSPeer peer = new SMSPeer(GENERATOR.generateValidAddress());
        System.out.println(name.getMethodName() + peer.getInvalidityReason());
        System.out.println(name.getMethodName() + peer.getInvalidityMessage());
        assertNull(peer.getInvalidityReason());
    }

    /**
     * Testing the ability to create an invalid address for the default region.
     * This test is run together with the parameterized tests to quickly perform multiple runs.
     */
    @Test
    public void canCreateInvalidAddress() {
        SMSPeer peer = new SMSPeer(GENERATOR.generateInvalidAddress());
        System.out.println(name.getMethodName() + peer.getInvalidityReason());
        System.out.println(name.getMethodName() + peer.getInvalidityMessage());
        assertNotNull(peer.getInvalidityReason());
    }

    //SPECIFIC REGION TESTS ------------------------------------------------------------------------

    /**
     * Testing the ability to create a valid address for the tested region.
     */
    @Test
    public void canCreateValidAddress() {
        SMSPeer peer = new SMSPeer(GENERATOR.generateValidAddress(testedRegion));
        System.out.println(name.getMethodName() + peer.getInvalidityReason());
        System.out.println(name.getMethodName() + peer.getInvalidityMessage());
        assertNull(peer.getInvalidityReason());
    }

    //PEER GENERATION TESTS ------------------------------------------------------------------------

    /**
     * The two following tests should be assumed passed only if the tests for SMSPeer passed.
     * Such tests depend on the result of the previous four.
     */
    @Test
    public void canCreateDefaultRegionValidPeer() {
        SMSPeer peer = GENERATOR.generateValidPeer();
        System.out.println(name.getMethodName() + peer.getInvalidityReason());
        System.out.println(name.getMethodName() + peer.getInvalidityMessage());
        assertNull(peer.getInvalidityReason());
    }

    @Test
    public void canCreateValidPeer() {
        SMSPeer peer = GENERATOR.generateValidPeer(testedRegion);
        System.out.println(name.getMethodName() + peer.getInvalidityReason());
        System.out.println(name.getMethodName() + peer.getInvalidityMessage());
        assertNull(peer.getInvalidityReason());
    }
}
