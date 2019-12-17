package com.eis.smslibrary;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.eis.communication.CommunicationHandler;
import com.eis.smslibrary.listeners.SMSDeliveredListener;
import com.eis.smslibrary.listeners.SMSReceivedListener;
import com.eis.smslibrary.listeners.SMSSentListener;

/**
 * Communication handler for SMSs. It's a Singleton, you should
 * access it with {@link #getInstance}
 *
 * @author Luca Crema, Marco Mariotto, Alberto Ursino, Marco Tommasini, Marco Cognolato
 */
public class SMSHandler implements CommunicationHandler<SMSMessage> {

    public static final String SENT_MESSAGE_INTENT_ACTION = "SMS_SENT";
    public static final String DELIVERED_MESSAGE_INTENT_ACTION = "SMS_DELIVERED";
    public static final int RANDOM_STARTING_COUNTER_VALUE_RANGE = 100000;

    /**
     * Singleton instance
     */
    private static SMSHandler instance;

    /**
     * Received listener reference
     */
    private SMSReceivedListener receivedListener;

    /**
     * This message counter is used so that we can have a different action name
     * for pending intent (that will call broadcastReceiver). If we were to use the
     * same action name for every message we would have a conflict and we wouldn't
     * know what message has been sent
     */
    private int messageCounter;

    /**
     * Private constructor for Singleton
     */
    private SMSHandler() {
        //Random because if we close and open the app the value probably differs
        messageCounter = (int) (Math.random() * RANDOM_STARTING_COUNTER_VALUE_RANGE);
    }

    /**
     * @return the current instance of this class
     */
    public static SMSHandler getInstance() {
        if (instance == null)
            instance = new SMSHandler();

        return instance;
    }

    /**
     * Sends a message to a destination peer via SMS.
     * Requires {@link android.Manifest.permission#SEND_SMS}
     *
     * @param message to be sent in the channel to a peer
     */
    @Override
    public void sendMessage(final @NonNull SMSMessage message) {
        sendMessage(message, null, null, null);
    }

    /**
     * Sends a message to a destination peer via SMS then
     * calls the listener.
     *
     * @param message      to be sent in the channel to a peer
     * @param sentListener called on message sent or on error, can be null
     * @param context The context of the application used to setup the listener
     */
    public void sendMessage(final @NonNull SMSMessage message,
                            final @Nullable SMSSentListener sentListener,
                            Context context) {
        sendMessage(message, sentListener, null, context);
    }

    /**
     * Sends a message to a destination peer via SMS then
     * calls the listener.
     *
     * @param message      to be sent in the channel to a peer
     * @param deliveredListener called on message delivered or on error, can be null
     * @param context The context of the application used to setup the listener
     */
    public void sendMessage(final @NonNull SMSMessage message,
                            final @Nullable SMSDeliveredListener deliveredListener,
                            Context context) {
        sendMessage(message, null, deliveredListener, context);
    }

    /**
     * Sends a message to a destination peer via SMS then
     * calls the listener.
     *
     * @param message      to be sent in the channel to a peer
     * @param sentListener called on message sent or on error, can be null
     * @param deliveredListener called on message delivered or on error, can be null
     * @param context The context of the application used to setup the listener
     */
    public void sendMessage(final @NonNull SMSMessage message,
                            final @Nullable SMSSentListener sentListener,
                            final @Nullable SMSDeliveredListener deliveredListener,
                            Context context) {
        PendingIntent sentPI = setupNewSentReceiver(message, sentListener, context);
        PendingIntent deliveredPI = setupNewDeliverReceiver(message, deliveredListener, context);
        SMSCore.sendMessage(getSMSContent(message), message.getPeer().getAddress(), sentPI, deliveredPI);
    }

    /**
     * @param receivedListener the listener called on message received
     */
    public void setReceivedListener(SMSReceivedListener receivedListener) {
        this.receivedListener = receivedListener;
    }

    /**
     * Method used by {@link SMSReceivedBroadcastReceiver} to call the listener
     * for messages received
     *
     * @param receivedMessage the message that has been received
     */
    protected void callReceivedListener(SMSMessage receivedMessage) {
        if (receivedListener != null)
            receivedListener.onMessageReceived(receivedMessage);
    }

    /**
     * Creates a new {@link SMSSentBroadcastReceiver} and registers it to receive broadcasts
     * with action {@value SENT_MESSAGE_INTENT_ACTION}
     *
     * @param message  that will be sent
     * @param listener to call on broadcast received
     * @param context The context of the application used to setup the listener
     * @return a {@link PendingIntent} to be passed to SMSCore
     */
    private PendingIntent setupNewSentReceiver(final @NonNull SMSMessage message,
                                               final @Nullable SMSSentListener listener,
                                               Context context) {
        if (listener == null || context == null)
            return null; //Doesn't make any sense to have a BroadcastReceiver if there is no listener or context

        SMSSentBroadcastReceiver onSentReceiver = new SMSSentBroadcastReceiver(message, listener);
        String actionName = SENT_MESSAGE_INTENT_ACTION + (messageCounter++);
        context.registerReceiver(onSentReceiver, new IntentFilter(actionName));
        return PendingIntent.getBroadcast(context, 0, new Intent(actionName), 0);
    }

    /**
     * Creates a new {@link SMSDeliveredBroadcastReceiver} and registers it to receive broadcasts
     * with action {@value DELIVERED_MESSAGE_INTENT_ACTION}
     *
     * @param message  that will be sent
     * @param listener to call on broadcast received
     * @param context The context of the application used to setup the listener
     * @return a {@link PendingIntent} to be passed to SMSCore
     */
    private PendingIntent setupNewDeliverReceiver(final @NonNull SMSMessage message,
                                                  final @Nullable SMSDeliveredListener listener,
                                                  Context context) {
        if (listener == null || context == null)
            return null; //Doesn't make any sense to have a BroadcastReceiver if there is no listener

        SMSDeliveredBroadcastReceiver onDeliveredReceiver = new SMSDeliveredBroadcastReceiver(message, listener);
        String actionName = DELIVERED_MESSAGE_INTENT_ACTION + (messageCounter++);
        context.registerReceiver(onDeliveredReceiver, new IntentFilter(actionName));
        return PendingIntent.getBroadcast(context, 0, new Intent(actionName), 0);
    }

    /**
     * Helper function that gets the message content by using the pre-setup parser in {@link SMSMessageHandler}
     * @param message to get the data from
     * @return the data parsed from the message
     */
    private String getSMSContent(SMSMessage message){
        return SMSMessageHandler.getInstance().parseData(message);
    }
}
