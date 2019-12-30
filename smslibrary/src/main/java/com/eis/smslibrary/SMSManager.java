package com.eis.smslibrary;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.eis.communication.CommunicationManager;
import com.eis.smslibrary.listeners.SMSDeliveredListener;
import com.eis.smslibrary.listeners.SMSReceivedServiceListener;
import com.eis.smslibrary.listeners.SMSSentListener;

import it.lucacrema.preferences.PreferencesManager;

/**
 * Communication handler for SMSs. It's a Singleton, you should
 * access it with {@link #getInstance}
 *
 * @author Luca Crema, Marco Mariotto, Alberto Ursino, Marco Tommasini, Marco Cognolato
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class SMSManager implements CommunicationManager<SMSMessage> {

    public static final String SENT_MESSAGE_INTENT_ACTION = "SMS_SENT";
    public static final String DELIVERED_MESSAGE_INTENT_ACTION = "SMS_DELIVERED";
    public static final int RANDOM_STARTING_COUNTER_VALUE_RANGE = 100000;

    /**
     * Singleton instance
     */
    private static SMSManager instance;

    /**
     * Received listener reference
     */
    private SMSReceivedServiceListener receivedListener;

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
    private SMSManager() {
        //Random because if we close and open the app the value probably differs
        messageCounter = (int) (Math.random() * RANDOM_STARTING_COUNTER_VALUE_RANGE);
    }

    /**
     * @return the current instance of this class
     */
    public static SMSManager getInstance() {
        if (instance == null)
            instance = new SMSManager();
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
     * @param context      The context of the application used to setup the listener
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
     * @param message           to be sent in the channel to a peer
     * @param deliveredListener called on message delivered or on error, can be null
     * @param context           The context of the application used to setup the listener
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
     * @param message           to be sent in the channel to a peer
     * @param sentListener      called on message sent or on error, can be null
     * @param deliveredListener called on message delivered or on error, can be null
     * @param context           The context of the application used to setup the listener
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
     * Creates a new {@link SMSSentBroadcastReceiver} and registers it to receive broadcasts
     * with action {@value SENT_MESSAGE_INTENT_ACTION}
     *
     * @param message  that will be sent
     * @param listener to call on broadcast received
     * @param context  The context of the application used to setup the listener
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
     * @param context  The context of the application used to setup the listener
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
     * Saves in memory the service class name to wake up. It doesn't need an
     * instance of the class, it just saves the name and instantiates it when needed.
     *
     * @param receivedListenerClassName the listener called on message received
     * @param <T>                       the class type that extends {@link SMSReceivedServiceListener} to be called
     * @param context                   the context used to set the listener
     */
    public <T extends SMSReceivedServiceListener> void setReceivedListener(Class<T> receivedListenerClassName, Context context) {
        PreferencesManager.setString(context, SMSReceivedBroadcastReceiver.SERVICE_CLASS_PREFERENCES_KEY, receivedListenerClassName.getName());
    }

    /**
     * Unsubscribe the current {@link SMSReceivedServiceListener} from being called on message arrival
     *
     * @param context The context used to remove the listener
     */
    public void removeReceivedListener(Context context) {
        PreferencesManager.removeValue(context, SMSReceivedBroadcastReceiver.SERVICE_CLASS_PREFERENCES_KEY);
    }

    /**
     * Helper function that gets the message content by using the pre-setup parser in {@link SMSMessageHandler}
     *
     * @param message to get the data from
     * @return the data parsed from the message
     */
    private String getSMSContent(SMSMessage message) {
        return SMSMessageHandler.getInstance().parseData(message);
    }
}
