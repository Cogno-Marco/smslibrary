package com.eis.smslibrary.exceptions;

import com.eis.smslibrary.SMSMessage;

/**
 * Thrown when user tries to work on a SMSMessage with an invalid content
 *
 * @author Luca Crema
 */
public class InvalidSMSMessageException extends RuntimeException {

    /**
     * Invalid state of a message content
     */
    private SMSMessage.ContentState state;

    /**
     * @param message exception message content
     * @param state   invalid message content state
     */
    public InvalidSMSMessageException(String message, SMSMessage.ContentState state) {
        super(message);
        if (state == SMSMessage.ContentState.MESSAGE_TEXT_VALID)
            throw new IllegalArgumentException("Cannot throw exception on valid message content state");
        this.state = state;
    }

    /**
     * @param cause exception cause
     * @param state invalid message content state
     */
    public InvalidSMSMessageException(Throwable cause, SMSMessage.ContentState state) {
        super(cause);
        if (state == SMSMessage.ContentState.MESSAGE_TEXT_VALID)
            throw new IllegalArgumentException("Cannot throw exception on valid message content state");
        this.state = state;
    }

    /**
     * Retrieves the state of the message that is the reason of the exception.
     *
     * @return the state of the message that is the reason of the exception.
     */
    public SMSMessage.ContentState getState() {
        return this.state;
    }
}
