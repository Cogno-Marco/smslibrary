package com.eis.smslibrary;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.eis.communication.Peer;
import com.eis.smslibrary.exceptions.InvalidTelephoneNumberException;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.util.Arrays;
import java.util.Locale;

/**
 * Implementation of {@link Peer} for SMS communication channel.
 *
 * @author Luca Crema, Marco Mariotto, Giovanni Velludo. Reviewed by Marco Cognolato and Mattia
 * Fanan
 * @author Matteo Carnelos
 */
public class SMSPeer implements Peer<String>, Comparable<SMSPeer>, java.io.Serializable {

    private static final String REGION_NEEDED_EXC_MSG = "The given number does not explicitly " +
            "contain the prefix and the default region is not obtainable. Please use the method " +
            "SMSPeer.setDefaultRegion() in order to set a valid one.";
    private static final String INVALID_NUMBER_EXC_MSG = "Invalid telephone number: '%s'. Note " +
            "that " +
            "a telephone number pointing outside the currently set default region (%s) should " +
            "be passed with also the prefix or it might be considered invalid.";
    private static final String NOT_PARSABLE_EXC_MSG = "Not parsable telephone number: '%s'. " +
            "Please " +
            "retry with a different String.";
    private static final String INVALID_ISO_EXC_MSG = "The given string is not a valid ISO " +
            "country code.";

    // The following regex checks if the given string is one of the following types:
    // +1#555521555*     [* = any decimal number]
    // 555521555*        [# = either a space or '-']
    // +1#555*
    // 555*
    // It's used to detect emulator phone numbers
    private static final String EMU_DETECTION_REGEX = "^(\\+1)?[ -]?(555521)?555\\d$";

    private static String defaultRegion = Locale.getDefault().getCountry();
    private String telephoneNumber;
    private InvalidTelephoneNumberException.Type invalidityReason = null;
    private String invalidityMessage = null;

    /**
     * Set the default country code used when a telephone number without prefix is given.
     *
     * @param defaultRegion The default 2 digit ISO country code.
     * @throws IllegalArgumentException If the given code is not a valid ISO country code.
     * @author Matteo Carnelos
     */
    public static void setDefaultRegion(@NonNull String defaultRegion) {
        if (!Arrays.asList(Locale.getISOCountries()).contains(defaultRegion))
            throw new IllegalArgumentException(INVALID_ISO_EXC_MSG);
        SMSPeer.defaultRegion = defaultRegion;
    }

    /**
     * Get the default region used when setting the country code.
     *
     * @return {@link SMSPeer#defaultRegion}.
     */
    public static String getDefaultRegion() {
        return defaultRegion;
    }

    /**
     * Create a new SMSPeer object. If the given number does not explicitly contains the prefix
     * will be added the default one.
     * Note: It is not always possible to obtain the correct default country code, especially if the
     * phone is moving between countries. To set a custom one use the method
     * {@link #setDefaultRegion(String)}.
     *
     * @param telephoneNumber Address for the peer.
     * @author Matteo Carnelos
     */
    public SMSPeer(@NonNull String telephoneNumber) {
        // If the given number is an emulator number, skip the validity checks
        if (adjustForEmulator(telephoneNumber)) return;
        try {
            Phonenumber.PhoneNumber validTelephoneNumber = checkValidity(telephoneNumber);
            this.telephoneNumber = formatE164(validTelephoneNumber);
        } catch (InvalidTelephoneNumberException e) {
            this.telephoneNumber = telephoneNumber;
            this.invalidityReason = e.getType();
            this.invalidityMessage = e.getMessage();
        }
    }

    /**
     * Checks the validity of a phone number and returns a parsed PhoneNumber object if valid.
     *
     * @param telephoneNumber The phone number for which to check validity.
     * @return A parsed PhoneNumber object, if telephoneNumber is valid.
     * @throws InvalidTelephoneNumberException if telePhone number is not valid.
     */
    private Phonenumber.PhoneNumber checkValidity(@NonNull String telephoneNumber)
            throws InvalidTelephoneNumberException {
        if (defaultRegion.isEmpty() && !telephoneNumber.startsWith("+"))
            throw new InvalidTelephoneNumberException(
                    InvalidTelephoneNumberException.Type.REGION_NEEDED,
                    REGION_NEEDED_EXC_MSG);

        Phonenumber.PhoneNumber phoneNumber = parseNumber(telephoneNumber);
        if (!PhoneNumberUtil.getInstance().isValidNumber(phoneNumber))
            throw new InvalidTelephoneNumberException(
                    InvalidTelephoneNumberException.Type.INVALID_NUMBER,
                    String.format(INVALID_NUMBER_EXC_MSG, telephoneNumber, defaultRegion));
        return phoneNumber;
    }

    /**
     * Return a parsed PhoneNumber object.
     *
     * @param number The destination string, eventually with the prefix.
     * @return The PhoneNumber object containing the phone number parsed.
     * @throws InvalidTelephoneNumberException If the given destination is not parsable.
     * @author Matteo Carnelos
     */
    private Phonenumber.PhoneNumber parseNumber(String number)
            throws InvalidTelephoneNumberException {
        Phonenumber.PhoneNumber phoneNumber;
        try {
            phoneNumber = PhoneNumberUtil.getInstance().parse(number, defaultRegion);
        } catch (NumberParseException e) {
            throw new InvalidTelephoneNumberException(
                    InvalidTelephoneNumberException.Type.NOT_PARSABLE_NUMBER,
                    String.format(NOT_PARSABLE_EXC_MSG, number));
        }
        return phoneNumber;
    }

    /**
     * Format the given phone number into the E164 format. The E164 format is basically the string
     * address without dashes, spaces and brackets. Example: "+393401234567".
     *
     * @param phoneNumber The {@link Phonenumber.PhoneNumber} object.
     * @return A string representing the formatted number.
     * @author Matteo Carnelos
     */
    private String formatE164(Phonenumber.PhoneNumber phoneNumber) {
        return PhoneNumberUtil.getInstance().format(phoneNumber,
                PhoneNumberUtil.PhoneNumberFormat.E164);
    }

    /**
     * Adjust the address string in case it is the phone number of an emulator.
     * The emulator phone numbers could be either "+1555521555*", "555521555*" or "555*". All
     * these numbers are invalid in the real world, so they need to avoid validation checks.
     *
     * @param address The address to eventually adjust.
     * @return True if the number given is an emulator number, false otherwise.
     * @author Matteo Carnelos
     */
    private boolean adjustForEmulator(String address) {
        if (address.matches(EMU_DETECTION_REGEX)) {
            this.telephoneNumber = "+1555521555" + address.charAt(address.length() - 1);
            return true;
        }
        return false;
    }

    /**
     * Compares this object with the specified object for order.
     *
     * @param o The object to be compared.
     * @return A negative integer, zero, or a positive integer as this object is less than, equal
     * to, or greater than the specified object.
     */
    @Override
    public int compareTo(SMSPeer o) {
        return telephoneNumber.compareTo(o.telephoneNumber);
    }

    /**
     * Retrieves the peer's address.
     *
     * @return Peer's address.
     */
    @Override
    public String getAddress() {
        return telephoneNumber;
    }

    /**
     * Retrieves the reason why this peer's telephoneNumber is invalid.
     *
     * @return An InvalidTelephoneNumberException.Type containing the reason of the invalidity if
     * telephoneNumber is invalid, null if telephoneNumber is valid.
     */
    @Nullable
    public InvalidTelephoneNumberException.Type getInvalidityReason() {
        return invalidityReason;
    }

    /**
     * Retrieves the message explaining why this peer's telephoneNumber is invalid.
     *
     * @return A String containing the reason of the invalidity if telephoneNumber is invalid, null
     * if telephoneNumber is valid.
     */
    @Nullable
    public String getInvalidityMessage() {
        return invalidityMessage;
    }

    /**
     * Indicates whether some other object is "equal to" this one
     *
     * @param o The reference object with which to compare.
     * @return True if this object is the same as the obj argument; false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof SMSPeer))
            return false;
        SMSPeer peer = (SMSPeer) o;
        return telephoneNumber.equals(peer.telephoneNumber);
    }

    /**
     * Return the hash code for the object.
     *
     * @return An integer representing the hash code.
     * @author Matteo Carnelos
     */
    @Override
    public int hashCode() {
        return telephoneNumber.hashCode();
    }

    /**
     * @return A string representation of the peer
     */
    @NonNull
    public String toString() {
        return telephoneNumber;
    }
}
