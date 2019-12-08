package com.eis.smslibrary.listeners;

import com.eis.smslibrary.message.AbstractSMSMessage.DeliveredState;
import com.eis.smslibrary.message.SMSMessageToSend;


/**
 * General interface for a listener for an SMS Delivered event
 *
 * @author Marco Cognolato
 */
public interface SMSDeliveredListener {

    /**
     * Callback for message sent to the provider or in case of error
     *
     * @param message        that's been delivered (or not)
     * @param deliveredState of the operation
     */
    void onSMSDelivered(SMSMessageToSend message, DeliveredState deliveredState);
}
