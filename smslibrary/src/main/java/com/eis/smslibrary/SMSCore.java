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
 * @author Luca Crema, Marco Cognolato
 */
final class SMSCore {

    static SmsManager manager;

    /**
     * Sets up a given valid custom manager
     * @param manager The custom manager to set up to send messages
     *
     * @author Marco Cognolato
     */
    static void setManager(SmsManager manager){
        SMSCore.manager = manager;
    }

    /**
     * @return Returns the pre-set up manager if not null, else returns the default manager
     *
     * @author Marco Cognolato
     */
    //TODO: use PowerMock to mock static method SmsManager.getDefault() instead of using this method, then delete getManager() and setManager()
    static SmsManager getManager(){
        return manager == null ? SmsManager.getDefault() : manager;
    }

    /**
     * Calls the library method to send a single message
     *
     * @param message     to be sent
     * @param phoneNumber peer address
     * @param sentPI      {@link PendingIntent} for a broadcast on message sent, can be null
     * @param deliveredPI {@link PendingIntent} for a broadcast on message received, can be null
     */
    static void sendMessage(@NonNull final String message, @NonNull final String phoneNumber, @Nullable final PendingIntent sentPI, @Nullable final PendingIntent deliveredPI) {
        getManager().sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
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
        getManager().sendMultipartTextMessage(phoneNumber, null, messages, sentPIs, deliveredPIs);
    }

}
