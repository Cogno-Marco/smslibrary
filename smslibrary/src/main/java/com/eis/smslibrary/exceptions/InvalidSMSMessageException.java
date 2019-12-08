package com.eis.smslibrary.exceptions;

import com.eis.smslibrary.message.AbstractSMSMessage;

/**
 * Thrown when user tries to work on a SMSMessageReceived with an invalid content
 *
 * @author Luca Crema
 */
public class InvalidSMSMessageException extends RuntimeException {

    /**
     * Invalid state of a message content
     */
    private AbstractSMSMessage.ContentState state;

    /**
     * @param message exception message content
     * @param state   invalid message content state
     */
    public InvalidSMSMessageException(String message, AbstractSMSMessage.ContentState state) {
        super(message);
        if (state == AbstractSMSMessage.ContentState.MESSAGE_TEXT_VALID)
            throw new IllegalArgumentException("Cannot throw exception on valid message content state");
        this.state = state;
    }

    /**
     * @param cause exception cause
     * @param state invalid message content state
     */
    public InvalidSMSMessageException(Throwable cause, AbstractSMSMessage.ContentState state) {
        super(cause);
        if (state == AbstractSMSMessage.ContentState.MESSAGE_TEXT_VALID)
            throw new IllegalArgumentException("Cannot throw exception on valid message content state");
        this.state = state;
    }

    /**
     * Retrieves the state of the message that is the reason of the exception
     *
     * @return
     */
    public AbstractSMSMessage.ContentState getState() {
        return this.state;
    }
}
