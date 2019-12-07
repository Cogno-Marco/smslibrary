package com.eis.smslibrary;

import android.content.Context;

import androidx.annotation.NonNull;

import com.eis.communication.MessageHandler;

import java.lang.ref.WeakReference;

import it.lucacrema.preferences.PreferencesManager;

/**
 * Singleton class used to parse String to SMSMessage and back
 * Uses a strategy to parse messages, so that any user can update it to its preferred parser
 * All methods will operate only if the Application Context is still valid
 * By defaults it uses a default strategy: {@link DefaultSMSParseStrategy}
 *
 * @author Luca Crema, Alberto Ursino, Marco Mariotto, Riccardo De Zen
 */
public class SMSMessageParser implements MessageHandler<String, String, SMSMessage> {

    private static final String NULL_ERROR_MSG = "Parameter context cannot be null";
    private static final String PREFERENCE_DEFAULT_STRATEGY = "SMS_DEFAULT_STRATEGY";

    private SMSParseStrategy parseStrategy;
    private static SMSMessageParser instance;
    private static WeakReference<Context> context;

    /**
     * Private constructor
     */
    private SMSMessageParser() {
        SMSParseStrategy savedStrategy = getMessageParseStrategy();
        if(savedStrategy == null) parseStrategy = new DefaultSMSParseStrategy();
        else parseStrategy = savedStrategy;
    }

    /**
     * Method to get the only instance of this class
     * @param callingContext The Context asking for the Instance. Application Context will be used
     * @return {@link SMSMessageParser#instance} the only active instance of this class
     */
    public static SMSMessageParser getInstance(@NonNull Context callingContext){
        if(callingContext == null) throw new NullPointerException(NULL_ERROR_MSG);
        context = new WeakReference<>(callingContext.getApplicationContext());
        if(instance == null)
            instance = new SMSMessageParser();
        return instance;
    }

    /**
     * Method to update the parse strategy to a custom one
     *
     * @param parseStrategy custom message parsing
     * @return true if the Strategy was set. False if {@link SMSMessageParser#context} referenced
     * Context is no longer valid.
     */
    public boolean setMessageParseStrategy(@NonNull final SMSParseStrategy parseStrategy) {
        if(context.get() == null) return false;
        this.parseStrategy = parseStrategy;
        String strategyName = parseStrategy.getClass().getCanonicalName();
        PreferencesManager.setString(context.get(), PREFERENCE_DEFAULT_STRATEGY, strategyName);
        return true;
    }

    /**
     * Method to get the SMSParseStrategy from the class saved in the Preferences.
     *
     * @return The built SMSParseStrategy object, null if no valid class or constructor is found or
     * if {@link SMSMessageParser#context} referenced Context is no longer valid.
     */
    public SMSParseStrategy getMessageParseStrategy(){
        if(context.get() == null) return null;
        String savedStrategyName = PreferencesManager.getString(context.get(), PREFERENCE_DEFAULT_STRATEGY);
        if(savedStrategyName == null || savedStrategyName.isEmpty()) return null;
        try{
            Class savedStrategyClass = Class.forName(savedStrategyName);
            return (SMSParseStrategy) savedStrategyClass.newInstance();
        }
        catch (ClassNotFoundException e){
            e.printStackTrace();
            return null;
        }
        catch (InstantiationException e){
            e.printStackTrace();
            return null;
        }
        catch (IllegalAccessException e){
            e.printStackTrace();
            return null;
        }
        catch (ClassCastException e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Method to reset the parse strategy to the default value.
     * @return true if the strategy was reset successfully, false if {@link SMSMessageParser#context}
     * referenced Context is no longer valid.
     */
    public boolean resetMessageParseStrategy(){
        this.parseStrategy = new DefaultSMSParseStrategy();
        PreferencesManager.removeValue(context.get(), PREFERENCE_DEFAULT_STRATEGY);
        return true;
    }

    /**
     * Interprets a string arrived via the communication channel and parses it to a library {@link SMSMessage}
     *
     * @param peerData    from the sms pdus
     * @param messageData from the sms pdus
     * @return the message if the string has been parsed correctly, null otherwise
     */
    public SMSMessage parseMessage(@NonNull final String peerData, @NonNull final String messageData) {
        if (SMSPeer.checkPhoneNumber(peerData) != SMSPeer.TelephoneNumberState.TELEPHONE_NUMBER_VALID)
            return null;
        return parseStrategy.parseMessage(new SMSPeer(peerData), messageData);
    }

    /**
     * Translates a message into a string that can be sent via sms
     *
     * @param message to be translated/parsed
     * @return the string to send
     */
    public String parseData(@NonNull final SMSMessage message) {
        return parseStrategy.parseData(message);
    }
}


