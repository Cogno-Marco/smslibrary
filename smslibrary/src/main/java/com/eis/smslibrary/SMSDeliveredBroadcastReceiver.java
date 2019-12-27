package com.eis.smslibrary;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.eis.smslibrary.listeners.SMSDeliveredListener;

import java.util.ArrayList;
import java.util.Collections;

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
    private final ArrayList<SMSPart> messageParts;
    private final SMSPeer peer;

    /**
     * Constructor for the custom {@link BroadcastReceiver}.
     *
     * @param parts    the parts of the message to be delivered.
     * @param listener the listener to be called when the operation is completed
     * @param peer     the peer to whom the message will be delivered.
     */
    SMSDeliveredBroadcastReceiver(@NonNull final ArrayList<SMSPart> parts,
                                  @NonNull final SMSDeliveredListener listener,
                                  @NonNull final SMSPeer peer) {
        this.listener = listener;
        this.messageParts = parts;
        this.peer = peer;
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

        // if delivery of a part failed, gives to the listener its deliveredState containing the
        // error
        if (deliveredState != SMSMessage.DeliveredState.MESSAGE_DELIVERED) {
            listener.onSMSDelivered(reconstructMessage(), deliveredState);
            context.unregisterReceiver(this);
            return;
        }
        // binary search on messageParts for the SMSPart associated to this intent's action
        int partIndex = Collections.binarySearch(messageParts, new SMSPart(null, intent.getAction()));
        if (partIndex >= 0) messageParts.get(partIndex).setReceived();
        for (SMSPart part : messageParts) {
            // if we're still waiting to receive intents for some parts, exit
            if (!part.wasReceived()) return;
        }
        listener.onSMSDelivered(reconstructMessage(), deliveredState);
        context.unregisterReceiver(this);
    }

    /**
     * Reconstructs SMSMessage to pass to listeners, using the messageParts in which it was split.
     */
    private SMSMessage reconstructMessage() {
        StringBuilder text = new StringBuilder();
        for (SMSPart part : messageParts) {
            text.append(part.getMessage());
        }
        return new SMSMessage(peer, text.toString());
    }
}
