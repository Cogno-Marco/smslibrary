package com.example.smslibproject;

import android.util.Log;

import com.eis.smslibrary.SMSMessage;
import com.eis.smslibrary.listeners.SMSReceivedServiceListener;

public class ReceivedMessageListener extends SMSReceivedServiceListener {
    @Override
    public void onMessageReceived(SMSMessage message) {
        Log.e("RML", "Received a message: " + message.getData());
    }
}
