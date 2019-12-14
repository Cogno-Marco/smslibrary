package com.eis.smslibrary;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;

import androidx.annotation.NonNull;

import com.eis.smslibrary.listeners.SMSSentListener;

import java.util.ArrayList;

/**
 * Broadcast receiver for sent messages, called by Android Library.
 * Must be instantiated and set as receiver with context.registerReceiver(...).
 * There has to be one different SentBroadcastReceiver per message sent,
 * so every IntentFilter name has to be different
 *
 * @author Luca Crema, Marco Mariotto, Giovanni Velludo
 */
public class SMSSentBroadcastReceiver extends BroadcastReceiver {

    private SMSSentListener listener;
    private SMSMessage message;
    private ArrayList<SMSPart> messageParts;
    private SMSPeer peer;

    /**
     * Constructor for the custom {@link BroadcastReceiver}, used when a single message is sent.
     *
     * @param message  the message that will be sent.
     * @param listener listener to be called when the operation is completed.
     */
    SMSSentBroadcastReceiver(@NonNull final SMSMessage message, @NonNull final SMSSentListener listener) {
        this.listener = listener;
        this.message = message;
    }

    /**
     * Constructor for the custom {@link BroadcastReceiver}, used when multiple messages are sent.
     *
     * @param parts     parts that will be sent.
     * @param listener  listener to be called when the operation is completed.
     */
    SMSSentBroadcastReceiver(@NonNull final ArrayList<SMSPart> parts,
                             @NonNull final SMSSentListener listener, @NonNull final SMSPeer peer) {
        this.listener = listener;
        this.messageParts = parts;
        this.peer = peer;
    }

    /**
     * This method is subscribed to the intent of a message sent, and will be called whenever a message is sent using this library.
     * It interprets the state of the message sending: {@link SMSMessage.SentState#MESSAGE_SENT} if it has been correctly sent,
     * some other state otherwise; then calls the listener and unregisters itself.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        SMSMessage.SentState sentState = SMSMessage.SentState.ERROR_GENERIC_FAILURE;

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
        }

        // if SMS sent had multiple parts
        if (this.message == null) {
            // if sending of a part failed, gives to the listener its sentState containing the error
            if (sentState != SMSMessage.SentState.MESSAGE_SENT) {
                reconstructMessage();
                listener.onSMSSent(message, sentState);
                context.unregisterReceiver(this);
                return;
            }
            // TODO: use HasMap with Intent.action as key instead of searching on an array?
            //  This would complicate building the SMSMessage in method reconstructMessage().
            for (SMSPart part : messageParts) {
                if (part.getIntentAction().equals(intent.getAction())) {
                    part.setReceived();
                }
            }
            for (SMSPart part : messageParts) {
                // if we're still waiting to receive intents for some parts, exit
                if (!part.wasReceived()) return;
            }
            reconstructMessage();
            listener.onSMSSent(message, sentState);
            context.unregisterReceiver(this);

        } else { // if SMS sent was a single message
            listener.onSMSSent(message, sentState);
            context.unregisterReceiver(this);
        }
    }

    /**
     * Reconstructs SMSMessage to pass to listeners, using the messageParts in which it was split.
     */
    private void reconstructMessage() {
        StringBuilder text = new StringBuilder();
        for (SMSPart part : messageParts) {
            text.append(part.getMessage());
        }
        message = new SMSMessage(peer, text.toString());
    }
}

