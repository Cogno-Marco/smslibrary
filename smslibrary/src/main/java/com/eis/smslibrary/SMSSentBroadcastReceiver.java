package com.eis.smslibrary;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;

import androidx.annotation.NonNull;

import com.eis.smslibrary.listeners.SMSSentListener;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Broadcast receiver for sent messages, called by Android Library.
 * Must be instantiated and set as receiver with context.registerReceiver(...).
 * There has to be one different SentBroadcastReceiver per message sent,
 * so every IntentFilter name has to be different
 *
 * @author Luca Crema, Marco Mariotto, Giovanni Velludo
 */
public class SMSSentBroadcastReceiver extends BroadcastReceiver {

    private final SMSSentListener listener;
    private final ArrayList<SMSPart> messageParts;
    private final SMSPeer peer;
    private short partsToSendCounter;

    /**
     * Constructor for the custom {@link BroadcastReceiver}.
     *
     * @param parts    the parts of the message that will be sent.
     * @param listener the listener to be called when the operation is completed.
     * @param peer     the peer to whom the message will be sent.
     */
    SMSSentBroadcastReceiver(@NonNull final ArrayList<SMSPart> parts,
                             @NonNull final SMSSentListener listener, @NonNull final SMSPeer peer) {
        this.listener = listener;
        this.messageParts = parts;
        this.peer = peer;
        this.partsToSendCounter = (short) parts.size(); // they can't be more than 255
    }

    /**
     * This method is subscribed to the intent of a message sent, and will be called whenever a message is sent using this library.
     * It interprets the state of the message sending: {@link SMSMessage.SentState#MESSAGE_SENT} if it has been correctly sent,
     * some other state otherwise; then calls the listener and unregisters itself.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        SMSMessage.SentState sentState;

        switch (getResultCode()) {
            case Activity.RESULT_OK:
                sentState = SMSMessage.SentState.MESSAGE_SENT;
                break;
            case SmsManager.RESULT_ERROR_RADIO_OFF:
                sentState = SMSMessage.SentState.ERROR_RADIO_OFF;
                break;
            case SmsManager.RESULT_ERROR_NULL_PDU:
                sentState = SMSMessage.SentState.ERROR_NULL_PDU;
                break;
            case SmsManager.RESULT_ERROR_NO_SERVICE:
                sentState = SMSMessage.SentState.ERROR_NO_SERVICE;
                break;
            case SmsManager.RESULT_ERROR_LIMIT_EXCEEDED:
                sentState = SMSMessage.SentState.ERROR_LIMIT_EXCEEDED;
                break;
            default:
                sentState = SMSMessage.SentState.ERROR_GENERIC_FAILURE;
                break;
        }

        if (sentState == SMSMessage.SentState.MESSAGE_SENT) {
            // binary search on messageParts for the SMSPart associated to this intent's action
            int partIndex = Collections.binarySearch(messageParts, new SMSPart(null, intent.getAction()));
            if (partIndex >= 0) {
                partsToSendCounter--;
            }
            if (partsToSendCounter > 0) return;
        }
        listener.onSMSSent(reconstructMessage(), sentState);
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

