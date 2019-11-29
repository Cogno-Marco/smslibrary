package com.eis.smslibrary.listeners;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.eis.smslibrary.SMSMessage;
import com.eis.smslibrary.SMSReceivedBroadcastReceiver;

/**
 * Abstract class to implement in order to wake up the service when a message is received
 *
 * @author Luca Crema, Alberto Ursino, Marco Tommasini, Alessandra Tonin
 * @since 29/11/2019
 */
public abstract class SMSReceivedListener extends IntentService {

    /**
     * Creates an IntentService. Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    protected SMSReceivedListener(@NonNull String name) {
        super(name);
    }

    /**
     * Callback for when a sms message sent from this library is received
     *
     * @param message the received message
     */
    public abstract void onMessageReceived(SMSMessage message);

    /**
     * Handles the service call, extracts the message from the intent extras and calls {@link #onMessageReceived(SMSMessage)}
     *
     * @param intent intent passed from the broadcastReceiver
     */
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        SMSMessage message = (SMSMessage) intent.getSerializableExtra(SMSReceivedBroadcastReceiver.INTENT_MESSAGE_TAG);
        onMessageReceived(message);
    }

}
