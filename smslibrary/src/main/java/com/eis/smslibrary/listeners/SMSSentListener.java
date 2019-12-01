package com.eis.smslibrary.listeners;

import com.eis.smslibrary.SMSMessage;
import com.eis.smslibrary.SMSMessage.SentState;

/**
 * Listener for sent messages
 *
 * @author Alessandra Tonin
 */
public interface SMSSentListener {

    /**
     * Callback for message sent to the provider or in case of error
     * @param message that's been sent/not sent
     * @param sentState of the operation
     */
    void onSMSSent(SMSMessage message, SentState sentState);

}
