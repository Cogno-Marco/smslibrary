package com.eis.smslibrary;

import androidx.annotation.NonNull;

import com.eis.communication.Peer;
import com.eis.smslibrary.exceptions.InvalidTelephoneNumberException;

/**
 * Implementation of {@link Peer} for SMS communication channel.
 *
 * @author Luca Crema, Marco Mariotto. Reviewed by Marco Cognolato and Mattia Fanan
 */
public class SMSPeer implements Peer<String>, Comparable<SMSPeer> {

    public static final int MAX_TELEPHONE_NUMBER_LENGTH = 15;
    public static final int MIN_TELEPHONE_NUMBER_LENGTH = 3;

    private String telephoneNumber;

    /**
     * @param telephoneNumber Address for the peer.
     * @throws InvalidTelephoneNumberException If telephoneNumber check is not {@link TelephoneNumberState#TELEPHONE_NUMBER_VALID}.
     */
    public SMSPeer(String telephoneNumber) throws InvalidTelephoneNumberException {
        TelephoneNumberState telephoneNumberState = SMSPeer.checkPhoneNumber(telephoneNumber);
        if(telephoneNumberState != TelephoneNumberState.TELEPHONE_NUMBER_VALID)
            throw new InvalidTelephoneNumberException(telephoneNumberState);

        this.telephoneNumber = telephoneNumber;
    }

    /**
     * Checks if the given phone number is valid.
     *
     * @param telephoneNumber the phone number to check. Must not be null
     * @return The {@link TelephoneNumberState} of the telephone number after the tests.
     */
    public static TelephoneNumberState checkPhoneNumber(String telephoneNumber) {
        //Check if the number is shorter than the MAX.
        if (telephoneNumber.length() > MAX_TELEPHONE_NUMBER_LENGTH + 1) {
            return TelephoneNumberState.TELEPHONE_NUMBER_TOO_LONG;
        }
        //Check if the number is longer than the MIN.
        if (telephoneNumber.length() < MIN_TELEPHONE_NUMBER_LENGTH + 1) {
            return TelephoneNumberState.TELEPHONE_NUMBER_TOO_SHORT;
        }
        //Check if it's actually a number with a plus and doesn't contain anything else
        if (!telephoneNumber.matches("\\+?\\d{"+MIN_TELEPHONE_NUMBER_LENGTH+","+MAX_TELEPHONE_NUMBER_LENGTH+"}")) {
            return TelephoneNumberState.TELEPHONE_NUMBER_NOT_A_NUMBER;
        }
        //Check if there is a country code.
        if (telephoneNumber.charAt(0) != '+') {
            return TelephoneNumberState.TELEPHONE_NUMBER_NO_COUNTRY_CODE;
        }
        //If it passed all the tests we are sure the number is valid.
        return TelephoneNumberState.TELEPHONE_NUMBER_VALID;
    }

    /**
     * Compares this object with the specified object for order.
     * @param o The object to be compared.
     * @return A negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
     */
    @Override
    public int compareTo(SMSPeer o) {
        return telephoneNumber.compareTo(o.telephoneNumber);
    }

    /**
     * Retrieves the peer's address.
     * @return Peer's address.
     */
    @Override
    public String getAddress() {
        return telephoneNumber;
    }

    /**
     * Possible states of a telephone number after a validity check
     */
    public enum TelephoneNumberState {
        TELEPHONE_NUMBER_VALID,
        TELEPHONE_NUMBER_TOO_SHORT,
        TELEPHONE_NUMBER_TOO_LONG,
        TELEPHONE_NUMBER_NO_COUNTRY_CODE,
        TELEPHONE_NUMBER_NOT_A_NUMBER
    }

    /**
     * Indicates whether some other object is "equal to" this one
     * @param o The reference object with which to compare.
     * @return True if this object is the same as the obj argument; false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if(o == null)
            return false;
        if(this == o)
            return true;
        if (!(o instanceof SMSPeer))
            return false;
        SMSPeer peer = (SMSPeer) o;
        return telephoneNumber.equals(peer.telephoneNumber);
    }

    /**
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode(){
        return 31 * telephoneNumber.hashCode();
    }

    /**
     * @return A string representation of the peer
     */
    @NonNull
    public String toString(){
        return telephoneNumber;
    }
}
