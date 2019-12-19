package com.eis.smslibrary.exceptions;

/**
 * Exception usually thrown if a Peer has been instantiated without having the telephone number checked for validity
 * @author Luca Crema, suggested by Marco Cognolato
 * @author Matteo Carnelos
 */
public class InvalidTelephoneNumberException extends RuntimeException{

    /**
     * Constructor of a basic InvalidTelephoneNumberException with a message.
     *
     * @param message The message to be thrown with the exception.
     */
    public InvalidTelephoneNumberException(String message) {
        super(message);
    }
}
