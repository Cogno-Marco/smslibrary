package com.eis.smslibrary.listeners;

import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.core.app.JobIntentService;

import com.eis.smslibrary.SMSReceivedBroadcastReceiver;
import com.eis.smslibrary.message.SMSMessageReceived;

/**
 * Abstract class to implement in order to wake up the service when a message is received
 *
 * @author Luca Crema
 * @author Alberto Ursino
 * @author Marco Tommasini
 * @author Alessandra Tonin
 * @since 29/11/2019
 */
public abstract class SMSReceivedServiceListener extends JobIntentService {

    /**
     * Callback for when a sms message sent from this library is received
     *
     * @param message the received message
     */
    public abstract void onMessageReceived(SMSMessageReceived message);

    /**
     * Handles the service call, extracts the message from the intent extras and calls {@link #onMessageReceived(SMSMessageReceived)}
     *
     * @param intent intent passed from the broadcastReceiver
     */
    @Override
    protected void onHandleWork(@Nullable Intent intent) {
        SMSMessageReceived message = (SMSMessageReceived) intent.getSerializableExtra(SMSReceivedBroadcastReceiver.INTENT_MESSAGE_TAG);
        onMessageReceived(message);
    }


}
