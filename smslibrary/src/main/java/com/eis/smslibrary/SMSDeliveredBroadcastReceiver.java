package com.eis.smslibrary;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.eis.smslibrary.listeners.SMSDeliveredListener;

/**
 * Broadcast receiver for delivered messages, called by Android Library.
 * Must be instantiated and set as receiver with context.registerReceiver(...).
 * There has to be one different DeliveredBroadcastReceiver per message sent,
 * so every IntentFilter name has to be different
 *
 * @author Marco Cognolato
 */
public class SMSDeliveredBroadcastReceiver extends BroadcastReceiver {
    private SMSDeliveredListener listener;
    private SMSMessage message;

    /**
     * Constructor for the custom {@link BroadcastReceiver}.
     *
     * @param message  that will be sent.
     * @param listener to be called when the operation is completed.
     */
    SMSDeliveredBroadcastReceiver(@NonNull final SMSMessage message, @NonNull final SMSDeliveredListener listener) {
        this.listener = listener;
        this.message = message;
    }

    /**
     * This method is subscribed to the intent of a message delivered, and will be called whenever a message is delivered using this library.
     * It interprets the state of the message delivering: {@link SMSMessage.DeliveredState#MESSAGE_DELIVERED} if it has been correctly sent,
     * some other state otherwise; then calls the listener and unregisters itself.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        SMSMessage.DeliveredState deliveredState;

        switch (getResultCode()) {
            case Activity.RESULT_OK:
                deliveredState = SMSMessage.DeliveredState.MESSAGE_DELIVERED;
                break;
            case Activity.RESULT_CANCELED:
                deliveredState = SMSMessage.DeliveredState.DELIVERY_ERROR;
                break;
            default:
                deliveredState = SMSMessage.DeliveredState.ERROR_GENERIC_FAILURE;
                break;
        }

        if (listener != null) //extra check, even though listener should never be null
            listener.onSMSDelivered(message, deliveredState);

        context.unregisterReceiver(this);
    }
}
