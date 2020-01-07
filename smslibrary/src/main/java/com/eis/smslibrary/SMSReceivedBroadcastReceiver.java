/*
 * Copyright (C) 2015 The Android Open Source Project
 * Modifications Copyright (C) 2019 Giovanni Velludo
 * Not a Contribution, Apache license notifications and license are retained
 * for attribution purposes only
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.eis.smslibrary;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.telephony.SmsMessage;
import android.util.Log;

import androidx.core.app.JobIntentService;

import it.lucacrema.preferences.PreferencesManager;

/**
 * Broadcast receiver for received messages, called by Android.
 *
 * @author Luca Crema, Marco Mariotto, Giovanni Velludo
 * @since 29/11/2019
 */
public class SMSReceivedBroadcastReceiver extends BroadcastReceiver {

    public static final String TAG = "SMSReceivedReceiver";
    public static final String INTENT_MESSAGE_TAG = "SMSMessage";
    public static final String SERVICE_CLASS_PREFERENCES_KEY = "ApplicationServiceClass";
    private static final int SCHEDULING_JOB_ID = 420;

    /**
     * Parses message and calls listener
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        SmsMessage[] pdus = getMessagesFromIntent(intent);
        if (pdus == null) return;
        String phoneNumber = pdus[0].getDisplayOriginatingAddress();
        String messageBody = buildMessageBodyFromPdus(pdus);
        // parseData() uses a MessageParsingStrategy to check if the message contains a special
        // String at the beginning of the message. If it does, then this string is removed;
        // otherwise it returns null.
        SMSMessage parsedMessage =
                SMSMessageHandler.getInstance(null).parseData(phoneNumber, messageBody);
        if (parsedMessage != null) callApplicationService(context, parsedMessage);
    }

    /**
     * Reads the PDUs out of an {@link android.provider.Telephony.Sms.Intents#SMS_RECEIVED_ACTION}
     * or a {@link android.provider.Telephony.Sms.Intents#DATA_SMS_RECEIVED_ACTION} intent.
     *
     * @param intent the intent to read from
     * @return an array of SmsMessages containing the PDUs
     */
    private static SmsMessage[] getMessagesFromIntent(Intent intent) {
        Object[] messages;
        try {
            messages = (Object[]) intent.getSerializableExtra("pdus");
        } catch (ClassCastException e) {
            Log.e(TAG, "getMessagesFromIntent: " + e);
            return null;
        }
        if (messages == null) {
            Log.e(TAG, "getMessagesFromIntent: pdus does not exist in the intent");
            return null;
        }
        String format = intent.getStringExtra("format");
        int pduCount = messages.length;
        SmsMessage[] msgs = new SmsMessage[pduCount];
        for (int i = 0; i < pduCount; i++) {
            byte[] pdu = (byte[]) messages[i];
            msgs[i] = createMessageFromPdu(pdu, format);
        }
        return msgs;
    }

    /**
     * Parses the SMS text from message PDUs.
     *
     * @param msgs the array of PDUs to parse
     * @return an String containing the parsed message text
     */
    private static String buildMessageBodyFromPdus(final SmsMessage[] msgs) {
        if (msgs.length == 1) {
            // There is only one part, so we grab the body directly
            return replaceFormFeeds(msgs[0].getDisplayMessageBody());
        } else {
            // Build up the body from the parts
            final StringBuilder body = new StringBuilder();
            for (final SmsMessage msg : msgs) {
                try {
                    // can throw NullPointerException if mWrappedMessage inside is null
                    body.append(msg.getDisplayMessageBody());
                } catch (final NullPointerException e) {
                    Log.e(TAG, "buildMessageFromPdus:" + e);
                }
            }
            return replaceFormFeeds(body.toString());
        }
    }

    /**
     * Some providers send formfeeds in their messages. Converts those formfeeds to newlines.
     *
     * @param s the String to parse
     * @return a String where formfeeds were replaced by newlines
     */
    private static String replaceFormFeeds(final String s) {
        return s == null ? "" : s.replace('\f', '\n');
    }

    /**
     * Calls the appropriate method to create a message from its pdus
     *
     * @param smsData message pdus
     * @param format  available only on build version >= 23
     * @return the created message
     */
    private static SmsMessage createMessageFromPdu(Object smsData, String format) {
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
        String preferencesClassName = PreferencesManager.getString(context, SERVICE_CLASS_PREFERENCES_KEY);
        try {
            listener = Class.forName(preferencesClassName);
        } catch (ClassNotFoundException e) {
            Log.e("SMSReceiver", "Service class to wake up could not be found: " + preferencesClassName);
        }
        if (listener == null)
            return;

        Intent serviceIntent = new Intent();
        serviceIntent.putExtra(INTENT_MESSAGE_TAG, message);
        JobIntentService.enqueueWork(context, listener, SCHEDULING_JOB_ID, serviceIntent);
    }
}
