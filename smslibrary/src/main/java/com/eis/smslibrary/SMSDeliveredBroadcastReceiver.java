package com.eis.smslibrary;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.eis.smslibrary.listeners.SMSDeliveredListener;
import com.eis.smslibrary.message.AbstractSMSMessage.DeliveredState;
import com.eis.smslibrary.message.SMSMessageToSend;

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
    private SMSMessageToSend message;

    /**
     * Constructor for the custom {@link BroadcastReceiver}.
     *
     * @param message  that will be sent.
     * @param listener to be called when the operation is completed.
     */
    SMSDeliveredBroadcastReceiver(@NonNull final SMSMessageToSend message, @NonNull final SMSDeliveredListener listener) {
        this.listener = listener;
        this.message = message;
    }

    /**
     * This method is subscribed to the intent of a message delivered, and will be called whenever a message is delivered using this library.
     * It interprets the state of the message delivering: {@link DeliveredState#MESSAGE_DELIVERED} if it has been correctly sent,
     * some other state otherwise; then calls the listener and unregisters itself.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        DeliveredState deliveredState = DeliveredState.ERROR_GENERIC_FAILURE;

        switch (getResultCode()) {
            case Activity.RESULT_OK:
                deliveredState = DeliveredState.MESSAGE_DELIVERED;
                break;
            case Activity.RESULT_CANCELED:
                deliveredState = DeliveredState.DELIVERY_ERROR;
                break;
        }

        if (listener != null) //extra check, even though listener should never be null
            listener.onSMSDelivered(message, deliveredState);

        context.unregisterReceiver(this);
    }
}
