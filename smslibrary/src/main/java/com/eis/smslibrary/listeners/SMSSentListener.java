package com.eis.smslibrary.listeners;

import com.eis.smslibrary.message.AbstractSMSMessage.SentState;
import com.eis.smslibrary.message.SMSMessageToSend;

/**
 * General interface for a listener for an SMS Sent event
 *
 * @author Alessandra Tonin
 */
public interface SMSSentListener {

    /**
     * Callback for message sent to the provider or in case of error
     *
     * @param message   the message that's been sent/not sent
     * @param sentState the state of the operation
     */
    void onSMSSent(SMSMessageToSend message, SentState sentState);

}
