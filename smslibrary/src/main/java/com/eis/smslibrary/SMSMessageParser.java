package com.eis.smslibrary;

import android.content.Context;

import androidx.annotation.NonNull;

import com.eis.communication.MessageHandler;

import it.lucacrema.preferences.PreferencesManager;

/**
 * Singleton class used to parse String to SMSMessage and back
 * Uses a strategy to parse messages, so that any user can update it to its preferred parser
 * By defaults it uses a default strategy, defined by the library
 *
 * @author Luca Crema, Alberto Ursino, Marco Mariotto
 */
public class SMSMessageParser implements MessageHandler<String, String, SMSMessage> {

    private static final String PREFERENCE_DEFAULT_STRATEGY = "";

    private SMSParseStrategy parseStrategy;
    private static SMSMessageParser instance;

    /**
     * Private constructor
     * @param context The calling context
     */
    private SMSMessageParser(Context context) {
        SMSParseStrategy savedStrategy = getSavedMessageParseStrategy(context);
        if(savedStrategy == null) parseStrategy = new DefaultSMSParseStrategy();
        else parseStrategy = savedStrategy;
    }

    /**
     * Method to get the only instance of this class
     * @param context The calling context
     * @return {@link SMSMessageParser#instance} the only active instance of this class
     */
    public static SMSMessageParser getInstance(Context context){
        if(instance == null)
            return new SMSMessageParser(context);
        return instance;
    }

    /**
     * Method to update the parse strategy to a custom one. Needs to be called runtime from a valid Context.
     *
     * @param context the calling Context
     * @param parseStrategy custom message parsing
     */
    public void setMessageParseStrategy(Context context, @NonNull final SMSParseStrategy parseStrategy) {
        this.parseStrategy = parseStrategy;
        String strategyName = parseStrategy.getClass().getCanonicalName();
        PreferencesManager.setString(context, PREFERENCE_DEFAULT_STRATEGY, strategyName);
    }

    /**
     * Method to get the SMSParseStrategy from the class saved in the Preferences.
     *
     * @param context the calling Context.
     * @return The built SMSParseStrategy object, null if no valid class or constructor is found.
     */
    public SMSParseStrategy getSavedMessageParseStrategy(Context context){
        String savedStrategyName = PreferencesManager.getString(context, PREFERENCE_DEFAULT_STRATEGY);
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
     *
     * @param context the calling context
     */
    public void resetMessageParseStrategy(Context context){
        this.parseStrategy = new DefaultSMSParseStrategy();
        PreferencesManager.removeValue(context, PREFERENCE_DEFAULT_STRATEGY);
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


