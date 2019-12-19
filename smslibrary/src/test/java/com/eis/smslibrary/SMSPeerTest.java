package com.eis.smslibrary;

import com.eis.smslibrary.exceptions.InvalidTelephoneNumberException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Random;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Test class for SMSPeer.
 *
 * @author Matteo Carnelos
 */
public class SMSPeerTest {

    private static final int VALID_TEST_ITERATIONS = 10;

    private static final String VALID_ADDR = "3401234567";
    private static final String VALID_ADDR_WITH_PREFIX = "+39" + VALID_ADDR;
    private static final String VALID_ADDR_WITH_SPACES = "340 1234567";
    private static final String VALID_ADDR_WITH_PREFIX_AND_SPACES = "+39 " + VALID_ADDR_WITH_SPACES;
    private static final String VALID_ADDR_WITH_DASHES = "340-1234567";
    private static final String VALID_ADDR_WITH_PREFIX_AND_DASHES = "+39-" + VALID_ADDR_WITH_DASHES;

    // Test for simulator special types of phone numbers
    private static final String EMULATOR_ADDR = "5556";
    private static final String EMULATOR_ADDR_WITH_EXT = "555521" + EMULATOR_ADDR;
    private static final String EMULATOR_ADDR_WITH_PREFIX_AND_EXT = "+1" + EMULATOR_ADDR_WITH_EXT;

    private static final String EMPTY_ADDR = "";
    private static final String SHORT_ADDR = "340123";
    private static final String LONG_ADDR = "3401234567901234";

    private static final String HIGHER_VALID_ADDR = "3401234568";

    private static PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
    private static String[] randomValidAddresses = new String[VALID_TEST_ITERATIONS];
    private static String randomInvalidAddress;

    @BeforeClass
    public static void setup() {
        Set<String> availableCountries = phoneNumberUtil.getSupportedRegions();
        String[] countryCodes = availableCountries.toArray(new String[0]);
        int randomIndex = new Random().nextInt(countryCodes.length);

        randomInvalidAddress = phoneNumberUtil.format(
                phoneNumberUtil.getInvalidExampleNumber(countryCodes[randomIndex]),
                PhoneNumberUtil.PhoneNumberFormat.E164);

        for (int i = 0; i < VALID_TEST_ITERATIONS; i++) {
            randomIndex = new Random().nextInt(countryCodes.length);
            randomValidAddresses[i] = phoneNumberUtil.format(
                    phoneNumberUtil.getExampleNumber(countryCodes[randomIndex]),
                    PhoneNumberUtil.PhoneNumberFormat.E164);
        }
    }

    @Test
    public void validPeers_areCreated() {
        // Random cases
        for(String validAddress : randomValidAddresses)
            new SMSPeer(validAddress);
        // Special cases
        new SMSPeer(VALID_ADDR);
        new SMSPeer(VALID_ADDR_WITH_PREFIX);
        new SMSPeer(VALID_ADDR_WITH_PREFIX_AND_SPACES);
        new SMSPeer(VALID_ADDR_WITH_DASHES);
        new SMSPeer(VALID_ADDR_WITH_PREFIX_AND_DASHES);
        new SMSPeer(EMULATOR_ADDR);
        new SMSPeer(EMULATOR_ADDR_WITH_EXT);
        new SMSPeer(EMULATOR_ADDR_WITH_PREFIX_AND_EXT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyPeer_isNotCreated() {
        new SMSPeer(EMPTY_ADDR);
        fail();
    }

    @Test(expected = InvalidTelephoneNumberException.class)
    public void shortPeer_isNotCreated() {
        new SMSPeer(SHORT_ADDR);
        fail();
    }

    @Test(expected = InvalidTelephoneNumberException.class)
    public void longPeer_isNotCreated() {
        new SMSPeer(LONG_ADDR);
        fail();
    }

    @Test(expected = InvalidTelephoneNumberException.class)
    public void invalidPeer_isNotCreated() {
        new SMSPeer(randomInvalidAddress);
    }

    @Test
    public void validAddressed_areCompared() {
        SMSPeer lowerPeer = new SMSPeer(VALID_ADDR);
        SMSPeer higherPeer = new SMSPeer(HIGHER_VALID_ADDR);
        assertTrue(higherPeer.compareTo(lowerPeer) > 0);
    }

    @Test
    public void validAddresses_areGet() {
        for(String validAddress : randomValidAddresses)
            assertEquals(validAddress, new SMSPeer(validAddress).getAddress());
    }

    @Test
    public void validPeers_areEqual() {
        assertEquals(new SMSPeer(VALID_ADDR), new SMSPeer(VALID_ADDR_WITH_PREFIX));
        assertEquals(new SMSPeer(VALID_ADDR_WITH_SPACES), new SMSPeer(VALID_ADDR_WITH_PREFIX_AND_SPACES));
        assertEquals(new SMSPeer(VALID_ADDR_WITH_DASHES), new SMSPeer(VALID_ADDR_WITH_PREFIX_AND_DASHES));
        assertEquals(new SMSPeer(EMULATOR_ADDR), new SMSPeer(EMULATOR_ADDR_WITH_EXT));
        assertEquals(new SMSPeer(EMULATOR_ADDR), new SMSPeer(EMULATOR_ADDR_WITH_PREFIX_AND_EXT));
    }

    @Test
    public void validPeersHashes_areEqual() {
        assertEquals(new SMSPeer(VALID_ADDR).hashCode(), new SMSPeer(VALID_ADDR_WITH_PREFIX).hashCode());
        assertEquals(new SMSPeer(VALID_ADDR_WITH_SPACES).hashCode(), new SMSPeer(VALID_ADDR_WITH_PREFIX_AND_SPACES).hashCode());
        assertEquals(new SMSPeer(VALID_ADDR_WITH_DASHES).hashCode(), new SMSPeer(VALID_ADDR_WITH_PREFIX_AND_DASHES).hashCode());
        assertEquals(new SMSPeer(EMULATOR_ADDR).hashCode(), new SMSPeer(EMULATOR_ADDR_WITH_EXT).hashCode());
        assertEquals(new SMSPeer(EMULATOR_ADDR).hashCode(), new SMSPeer(EMULATOR_ADDR_WITH_PREFIX_AND_EXT).hashCode());
    }
}
