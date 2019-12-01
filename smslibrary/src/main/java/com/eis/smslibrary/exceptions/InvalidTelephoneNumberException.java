package com.eis.smslibrary.exceptions;


import com.eis.smslibrary.SMSPeer;

/**
 * Exception usually thrown if a Peer has been instantiated without having the telephone number checked for validity
 * @author Luca Crema, suggested by Marco Cognolato
 */
public class InvalidTelephoneNumberException extends RuntimeException{

    /**
     * Invalid state of the telephone number
     */
    private SMSPeer.TelephoneNumberState telephoneNumberState;

    /**
     * @param state Invalid state of telephone number, cannot be {@link SMSPeer.TelephoneNumberState#TELEPHONE_NUMBER_VALID}
     */
    public InvalidTelephoneNumberException(SMSPeer.TelephoneNumberState state){
        if(state == SMSPeer.TelephoneNumberState.TELEPHONE_NUMBER_VALID)
            throw new IllegalArgumentException("TelephoneNumberState cannot be TELEPHONE_NUMBER_VALID if InvalidTelephoneNumberException is thrown!");
        this.telephoneNumberState = state;
    }

    /**
     * @param state Invalid state of telephone number, cannot be {@link SMSPeer.TelephoneNumberState#TELEPHONE_NUMBER_VALID}
     */
    public InvalidTelephoneNumberException(String message, SMSPeer.TelephoneNumberState state) {
        super(message);
        if(state == SMSPeer.TelephoneNumberState.TELEPHONE_NUMBER_VALID)
            throw new IllegalArgumentException("TelephoneNumberState cannot be TELEPHONE_NUMBER_VALID if InvalidTelephoneNumberException is thrown!");
        this.telephoneNumberState = state;
    }
}
