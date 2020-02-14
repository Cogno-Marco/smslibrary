package com.eis.smslibrary.random;

import androidx.annotation.NonNull;

import com.eis.smslibrary.SMSPeer;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.util.Random;

/**
 * Utility class to generate random instances of {@code SMSPeer}. Useful in testing.
 *
 * @author Riccardo De Zen
 */
public class RandomSMSPeerGenerator implements RandomPeerGenerator<String, SMSPeer> {

    private static final String REGION_ERR = "This region's country code is not supported: ";
    private static final String INVALID_ADDRESS_HEADER = "#";

    private static final Random random = new Random();
    private static final PhoneNumberUtil utils = PhoneNumberUtil.getInstance();

    /**
     * @return a valid built {@code SMSPeer} for the default region.
     */
    @NonNull
    @Override
    public SMSPeer generateValidPeer() {
        return new SMSPeer(generateValidAddress());
    }

    /**
     * @param region the region for the {@code SMSPeer}.
     * @return a valid built SMSPeer for the given region.
     */
    @NonNull
    public SMSPeer generateValidPeer(String region) {
        return new SMSPeer(generateValidAddress(region));
    }

    /**
     * @return an example of valid address for the default region.
     */
    @NonNull
    @Override
    public String generateValidAddress() {
        return generateValidAddress(SMSPeer.getDefaultRegion());
    }

    /**
     * @param region the region for the {@code SMSPeer} address.
     * @return an example of valid address for the given region.
     * @throws IllegalArgumentException If the region is not supported.
     */
    @NonNull
    public String generateValidAddress(String region) throws IllegalArgumentException {
        if (!utils.getSupportedRegions().contains(region))
            throw new IllegalArgumentException(REGION_ERR + region);
        String address;
        SMSPeer peer;
        do {
            Phonenumber.PhoneNumber number = getRandomPhoneNumber(region);
            address = utils.format(number, PhoneNumberUtil.PhoneNumberFormat.E164);
            peer = new SMSPeer(address);
        } while (peer.getInvalidityReason() != null);
        return address;
    }

    /**
     * Method to generate an invalid address. As a simple invalidity criteria, a partially random
     * String is returned.
     *
     * @return an invalid address for an {@code SMSPeer}.
     * @see RandomSMSPeerGenerator#getInvalidRandomString() for details on the Strings returned.
     */
    @NonNull
    @Override
    public String generateInvalidAddress() {
        return getInvalidRandomString();
    }

    /**
     * Method to generate a random number. In order to leave the national number valid only the
     * last few digits are modified.
     *
     * @param region The region for which the national number should be generated. Must be a
     *               supported region.
     * @return a randomized phone number. Only keeps country code intact, may or may not be valid.
     */
    private Phonenumber.PhoneNumber getRandomPhoneNumber(String region) {
        Phonenumber.PhoneNumber number = utils.getExampleNumber(region);
        number.setNationalNumber(
                number.getNationalNumber() + Math.abs(random.nextLong() % 1000)
        );
        return number;
    }

    /**
     * @return a partially random String, made of between 1 and 16 characters. The String always
     * starts with {@link RandomSMSPeerGenerator#INVALID_ADDRESS_HEADER}.
     */
    private String getInvalidRandomString() {
        final int
                maxDigits = 15,
                minDigits = 2,
                maxCharValue = 255;
        int digits = Math.abs(random.nextInt(maxDigits - minDigits + 1)) + minDigits;
        StringBuilder result = new StringBuilder(INVALID_ADDRESS_HEADER);
        while (result.length() < digits)
            result.append((char) random.nextInt(maxCharValue));
        return result.toString();
    }
}
