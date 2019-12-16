package com.eis.smslibrary.listeners;

import com.eis.smslibrary.SMSMessage;

public class TestReceivedListener extends SMSReceivedServiceListener {
    /**
     * Callback for when a sms message sent from this library is received
     *
     * @param message the received message
     */
    @Override
    public void onMessageReceived(SMSMessage message) {
        //TODO: verify that message received is correct, remove this exception
        throw new Exception();
    }
}
