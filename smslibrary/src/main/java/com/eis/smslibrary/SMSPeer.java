package com.eis.smslibrary;

import androidx.annotation.NonNull;

import com.eis.communication.Peer;

/**
 * Implementation of {@link Peer} for SMS communication channel
 *
 * @author Luca Crema, Marco Mariotto
 */
public class SMSPeer implements Peer<String>, Comparable<SMSPeer> {

    public static final int MAX_TELEPHONE_NUMBER_LENGTH = 20;
    public static final int MIN_TELEPHONE_NUMBER_LENGTH = 3;

    private String telephoneNumber;

    /**
     * @param telephoneNumber a valid telephone number (checkTelephoneNumber state must be TELEPHONE_NUMBER_VALID)
     */
    public SMSPeer(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    /**
     * Checks if the given phone number is valid.
     *
     * @param telephoneNumber the phone number to check.
     * @return The {@link TelephoneNumberState} of the telephone number after the tests.
     */
    public static TelephoneNumberState checkPhoneNumber(String telephoneNumber) {
        //Check if the number is shorter than the MAX.
        if (telephoneNumber.length() > MAX_TELEPHONE_NUMBER_LENGTH) {
            return TelephoneNumberState.TELEPHONE_NUMBER_TOO_LONG;
        }
        //Check if the number is longer than the MIN.
        if (telephoneNumber.length() < MIN_TELEPHONE_NUMBER_LENGTH) {
            return TelephoneNumberState.TELEPHONE_NUMBER_TOO_SHORT;
        }
        //Check if it's actually a number and doesn't contain anything else
        //First we have to remove the "+"
        if (!telephoneNumber.substring(1, telephoneNumber.length() - 1).matches("[0-9]+")) {
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
     * Checks if the current phone number is valid.
     *
     * @return The {@link TelephoneNumberState} of the telephone number after the tests.
     */
    public TelephoneNumberState checkPhoneNumber() {
        return checkPhoneNumber(this.getAddress());
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
     * Retrieves the peer's address
     * @return Peer's address
     */
    @Override
    public String getAddress() {
        return telephoneNumber;
    }

    /**
     * Sets or updates the peer's address
     * @param telephoneNumber new address for the peer
     */
    @Override
    public void setAddress(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
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
        if (!(o instanceof SMSPeer))
            return false;
        SMSPeer peer = (SMSPeer) o;
        return telephoneNumber.equals(peer.telephoneNumber);
    }

    /**
     * @return A string representation of the peer
     */
    @NonNull
    public String toString(){
        return telephoneNumber;
    }

}
