package com.eis.smslibrary;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import it.lucacrema.preferences.PreferencesManager;

/**
 * Broadcast receiver for received messages, called by Android.
 *
 * @author Luca Crema, Marco Mariotto, Giovanni Velludo
 * @since 29/11/2019
 */
public class SMSReceivedBroadcastReceiver extends BroadcastReceiver {

    public static final String INTENT_MESSAGE_TAG = "SMSMessage";
    public static final String SERVICE_CLASS_PREFERENCES_KEY = "ApplicationServiceClass";

    /**
     * Parses message and calls listener
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras == null) {
            return;
        }
        Object[] pdus = (Object[]) extras.get("pdus");
        if (pdus == null) return;
        String format = (String) extras.get("format");
        Map<String, String> peerMessageMap = new HashMap<>(pdus.length);

        /* There can be multiple SMSes from multiple senders. We'll concatenate multiple SMSes from
         * the same sender in one message.
         */
        for (Object pdu : pdus) {
            SmsMessage message = createMessageFromPdu(pdu, format);
            String phoneNumber = message.getOriginatingAddress();
            if (phoneNumber == null) phoneNumber = "";
            if (!peerMessageMap.containsKey(phoneNumber)) {
                peerMessageMap.put(phoneNumber, message.getMessageBody());
            } else {
                String previousParts = peerMessageMap.get(phoneNumber);
                String updatedMessage = previousParts + message.getMessageBody();
                peerMessageMap.put(phoneNumber, updatedMessage);
            }
        }
        for (String peer : peerMessageMap.keySet()) {
            SMSMessage parsedMessage =
                    SMSMessageHandler.getInstance().parseMessage(peer, peerMessageMap.get(peer));
            if (parsedMessage != null) callApplicationService(context, parsedMessage);
        }
    }

    /**
     * Calls the appropriate method to create a message from its pdus
     *
     * @param smsData message pdus
     * @param format  available only on build version >= 23
     * @return the created message
     */
    private SmsMessage createMessageFromPdu(Object smsData, String format) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //Requires android version >23
            return SmsMessage.createFromPdu((byte[]) smsData, format);
        }
        return SmsMessage.createFromPdu((byte[]) smsData);
    }

    /**
     * Calls the current subscribed app service
     *
     * @param context broadcast current context
     * @param message received message
     */
    private void callApplicationService(Context context, SMSMessage message) {
        Class<?> listener = null;
        try {
            listener = Class.forName(PreferencesManager.getString(context, SERVICE_CLASS_PREFERENCES_KEY));
        } catch (ClassNotFoundException e) {
            Log.e("SMSReceiver", "Service class to wake up could not be found");
        }
        if (listener == null)
            return;

        Intent serviceIntent = new Intent(context, listener);
        serviceIntent.putExtra(INTENT_MESSAGE_TAG, message);
        context.startService(serviceIntent);

    }
}
