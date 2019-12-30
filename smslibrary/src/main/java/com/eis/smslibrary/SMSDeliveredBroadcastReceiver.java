package com.eis.smslibrary;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.eis.smslibrary.listeners.SMSDeliveredListener;

import java.util.ArrayList;

/**
 * Broadcast receiver for delivered messages, called by Android Library.
 * Must be instantiated and set as receiver with context.registerReceiver(...).
 * There has to be one different DeliveredBroadcastReceiver per message sent,
 * so every IntentFilter name has to be different
 *
 * @author Marco Cognolato, Giovanni Velludo
 */
public class SMSDeliveredBroadcastReceiver extends BroadcastReceiver {

    private final SMSDeliveredListener listener;
    private final SMSMessage message;
    private short partsToDeliverCounter;

    /**
     * Constructor for the custom {@link BroadcastReceiver}.
     *
     * @param parts    the parts of the message to be delivered.
     * @param listener the listener to be called when the operation is completed
     * @param peer     the peer to whom the message will be delivered.
     */
    SMSDeliveredBroadcastReceiver(@NonNull final ArrayList<String> parts,
                                  @NonNull final SMSDeliveredListener listener,
                                  @NonNull final SMSPeer peer) {
        StringBuilder fullMessageText = new StringBuilder();
        for (String part : parts) {
            fullMessageText.append(part);
        }
        this.listener = listener;
        this.message = new SMSMessage(peer, fullMessageText.toString());
        this.partsToDeliverCounter = (short) parts.size(); // they can't be more than 255
    }

    /**
     * This method is subscribed to the intent of a message delivered, and will be called whenever a message is delivered using this library.
     * It interprets the state of the message delivering: {@link SMSMessage.DeliveredState#MESSAGE_DELIVERED} if it has been correctly delivered,
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

        if (deliveredState == SMSMessage.DeliveredState.MESSAGE_DELIVERED &&
                --partsToDeliverCounter > 0) {
            return;
        }
        listener.onSMSDelivered(message, deliveredState);
        context.unregisterReceiver(this);
    }
}
