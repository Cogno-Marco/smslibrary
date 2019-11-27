package com.eis.smslibrary;

import android.app.PendingIntent;
import android.telephony.SmsManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * Wrapper for the {@link android.telephony} library.
 * This class is only used to interface with the core sms library of Android.
 *
 * @author Luca Crema
 */
class SMSCore {

    /**
     * Calls the library method to send a single message
     *
     * @param message     to be sent
     * @param phoneNumber peer address
     * @param sentPI      {@link PendingIntent} for a broadcast on message sent, can be null
     * @param deliveredPI {@link PendingIntent} for a broadcast on message received, can be null
     */
    static void sendMessage(@NonNull final String message, @NonNull final String phoneNumber, @Nullable final PendingIntent sentPI, @Nullable final PendingIntent deliveredPI) {
        SmsManager.getDefault().sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
    }

    /**
     * Calls the library method to send multiple messages
     *
     * @param messages     {@link ArrayList} of messages to be sent
     * @param phoneNumber  peer phone address
     * @param sentPIs      {@link ArrayList} of pending intents for a broadcast on message sent, can be null
     * @param deliveredPIs {@link ArrayList}  of pending intents for a broadcast on message delivered, can be null
     */
    static void sendMessages(@NonNull final ArrayList<String> messages, @NonNull final String phoneNumber, @Nullable final ArrayList<PendingIntent> sentPIs, @Nullable final ArrayList<PendingIntent> deliveredPIs) {
        SmsManager.getDefault().sendMultipartTextMessage(phoneNumber, null, messages, sentPIs, deliveredPIs);
    }

}
