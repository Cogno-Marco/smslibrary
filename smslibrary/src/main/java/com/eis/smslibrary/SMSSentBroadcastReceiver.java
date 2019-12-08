package com.eis.smslibrary;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;

import androidx.annotation.NonNull;

import com.eis.smslibrary.listeners.SMSSentListener;
import com.eis.smslibrary.message.AbstractSMSMessage.SentState;
import com.eis.smslibrary.message.SMSMessageToSend;

/**
 * Broadcast receiver for sent messages, called by Android Library.
 * Must be instantiated and set as receiver with context.registerReceiver(...).
 * There has to be one different SentBroadcastReceiver per message sent,
 * so every IntentFilter name has to be different
 *
 * @author Luca Crema, Marco Mariotto
 */
public class SMSSentBroadcastReceiver extends BroadcastReceiver {

    private SMSSentListener listener;
    private SMSMessageToSend message;

    /**
     * Constructor for the custom {@link BroadcastReceiver}.
     *
     * @param message  that will be sent.
     * @param listener to be called when the operation is completed.
     */
    SMSSentBroadcastReceiver(@NonNull final SMSMessageToSend message, @NonNull final SMSSentListener listener) {
        this.listener = listener;
        this.message = message;
    }

    /**
     * This method is subscribed to the intent of a message sent, and will be called whenever a message is sent using this library.
     * It interprets the state of the message sending: {@link SentState#MESSAGE_SENT} if it has been correctly sent,
     * some other state otherwise; then calls the listener and unregisters itself.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        SentState sentState = SentState.ERROR_GENERIC_FAILURE;

        switch (getResultCode()) {
            case Activity.RESULT_OK:
                sentState = SentState.MESSAGE_SENT;
                break;
            case SmsManager.RESULT_ERROR_RADIO_OFF:
                sentState = SentState.ERROR_RADIO_OFF;
                break;
            case SmsManager.RESULT_ERROR_NULL_PDU:
                sentState = SentState.ERROR_NULL_PDU;
                break;
            case SmsManager.RESULT_ERROR_NO_SERVICE:
                sentState = SentState.ERROR_NO_SERVICE;
                break;
            case SmsManager.RESULT_ERROR_LIMIT_EXCEEDED:
                sentState = SentState.ERROR_LIMIT_EXCEEDED;
                break;
        }

        if (listener != null) //extra check, even though listener should never be null
            listener.onSMSSent(message, sentState);

        context.unregisterReceiver(this);
    }
}
