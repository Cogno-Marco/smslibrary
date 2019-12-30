package com.eis.smslibrary.exceptions;

/**
 * Exception usually thrown if a Peer has been instantiated without having the telephone number checked for validity.
 *
 * @author Luca Crema, suggested by Marco Cognolato
 * @author Matteo Carnelos
 */
public class InvalidTelephoneNumberException extends RuntimeException {

    public enum Type {
        REGION_NEEDED,
        INVALID_NUMBER,
        NOT_PARSABLE_NUMBER
    }

    private Type type;

    /**
     * Constructor of a basic InvalidTelephoneNumberException.
     *
     * @param type The {@link Type} of the exception.
     * @author Matteo Carnelos
     */
    public InvalidTelephoneNumberException(Type type) {
        this.type = type;
    }

    /**
     * Constructor of a basic InvalidTelephoneNumberException with a message.
     *
     * @param type The {@link Type} of the exception.
     * @param message The message to be thrown with the exception.
     * @author Matteo Carnelos
     */
    public InvalidTelephoneNumberException(Type type, String message) {
        super(message);
        this.type = type;
    }

    /**
     * Returns the type of this exception.
     * @return The {@link Type} for this object.
     * @author Matteo Carnelos
     */
    public Type getType() {
        return this.type;
    }
}
