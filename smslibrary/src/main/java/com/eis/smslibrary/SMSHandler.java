package com.eis.smslibrary;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.eis.communication.CommunicationHandler;
import com.eis.smslibrary.listeners.SMSSentListener;

import java.lang.ref.WeakReference;

/**
 * Communication handler for SMSs. It's a Singleton, you should
 * access it with {@link #getInstance}, and before doing anything you
 * should call {@link #setup}.
 *
 * @author Luca Crema, Marco Mariotto, Alberto Ursino, Marco Tommasini
 */
public class SMSHandler implements CommunicationHandler<SMSMessage> {

    public static final String SENT_MESSAGE_INTENT_ACTION = "SMS_SENT";

    private static SMSHandler instance;

    /**
     * Weak reference doesn't prevent garbage collector to
     * de-allocate this class when it has reference to a
     * context that is still running. Prevents memory leaks.
     */
    private WeakReference<Context> context;

    /**
     * Private constructor for Singleton
     */
    private SMSHandler() {
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
     * Setup for the handler.
     *
     * @param context current context.
     */
    public void setup(Context context) {
        this.context = new WeakReference<>(context);
    }

    /**
     * Sends a message to a destination peer via SMS.
     *
     * @param message to be sent in the channel to a peer
     */
    @Override
    public void sendMessage(final @NonNull SMSMessage message) {
        sendMessage(message, null);
    }

    /**
     * Sends a message to a destination peer via SMS then
     * calls the listener.
     *
     * @param message      to be sent in the channel to a peer
     * @param sentListener called on message sent or on error, can be null
     */
    public void sendMessage(final @NonNull SMSMessage message, final @Nullable SMSSentListener sentListener) {
        String smsContent = SMSMessageHandler.getInstance().parseData(message);
        PendingIntent sentPI = setupNewSentReceiver(message, sentListener);

        SMSCore.sendMessage(smsContent, message.getPeer().getAddress(),sentPI,null);
    }

    /**
     * Creates a new {@link SMSSentBroadcastReceiver} and registers it to receive broadcasts
     * with action {@value SENT_MESSAGE_INTENT_ACTION}
     *
     * @param message that will be sent
     * @param listener to call on broadcast received
     * @return a {@link PendingIntent} to be passed to SMSCore
     */
    private PendingIntent setupNewSentReceiver(final @NonNull SMSMessage message, final @Nullable SMSSentListener listener) {
        if (listener == null)
            return null; //Doesn't make any sense to have a BroadcastReceiver if there is no listener

        SMSSentBroadcastReceiver onSentReceiver = new SMSSentBroadcastReceiver(message, listener);
        context.get().registerReceiver(onSentReceiver, new IntentFilter(SENT_MESSAGE_INTENT_ACTION));
        return PendingIntent.getBroadcast(context.get(), 0, new Intent(SENT_MESSAGE_INTENT_ACTION), 0);
    }
}
