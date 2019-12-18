package com.eis.smslibrary.listeners;

import com.eis.smslibrary.SMSMessage;

public class TestSMSReceivedServiceListener extends SMSReceivedServiceListener {

    ListenerToMock mockListener;
    /**
     * Callback for when a sms message sent from this library is received
     *
     * @param message the received message
     */
    @Override
    public void onMessageReceived(SMSMessage message) {
        if (mockListener != null) mockListener.onMessageReceived(message);
    }

    public void addListener(ListenerToMock mockListener) {
        this.mockListener = mockListener;
    }
}
