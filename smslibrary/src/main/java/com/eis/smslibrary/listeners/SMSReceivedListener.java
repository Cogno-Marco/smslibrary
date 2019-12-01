package com.eis.smslibrary.listeners;

import com.eis.smslibrary.SMSMessage;

/**
 * Listener for received messages
 *
 * @author Alberto Ursino
 */
public interface SMSReceivedListener {

    /**
     * Callback for when a sms message sent from this library is received
     *
     * @param message the received message
     */
     void onMessageReceived(SMSMessage message);
}
