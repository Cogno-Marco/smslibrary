package com.eis.smslibrary;

import androidx.annotation.NonNull;

import com.eis.communication.MessageHandler;
import com.eis.communication.MessageParseStrategy;

/**
 * Singleton class used to parse String to SMSMessage and back
 * Uses a strategy to parse messages, so that any user can update it to its preferred parser
 * By defaults it uses a default strategy, defined by the library
 *
 * @author Luca Crema, Alberto Ursino, Marco Mariotto
 */
public class SMSMessageHandler implements MessageHandler<String, String, SMSMessage> {

    private MessageParseStrategy<String, SMSPeer, SMSMessage> parseStrategy;
    private static SMSMessageHandler instance;

    /**
     * Private constructor
     */
    public SMSMessageHandler() {
        parseStrategy = new DefaultSMSMessageParseStrategy();
    }

    /**
     * Update the parse strategy to a custom one
     *
     * @param parseStrategy custom message parsing
     */
    public void setMessageParseStrategy(@NonNull final MessageParseStrategy<String, SMSPeer, SMSMessage> parseStrategy) {
        this.parseStrategy = parseStrategy;
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

    public class DefaultSMSMessageParseStrategy implements MessageParseStrategy<String, SMSPeer, SMSMessage> {

        protected static final String HIDDEN_CHARACTER = (char) 0x02 + "";

        /**
         * Parses sms data into a SMSMessage if possible
         *
         * @param channelPeer that sent the data
         * @param channelData read from the channel
         * @return the parsed SMSMessage if the string was correct, null otherwise
         */
        @Override
        public SMSMessage parseMessage(@NonNull final SMSPeer channelPeer, @NonNull final String channelData) {
            //First character of the content must be the hidden char
            if (!channelData.startsWith(HIDDEN_CHARACTER))
                return null;
            String messageData = channelData.substring(1);
            return new SMSMessage(channelPeer, messageData);
        }

        /**
         * Parses SMSMessage into sms content data
         *
         * @param message from library
         * @return the parsed sms content data ready to be sent
         */
        @Override
        public String parseData(@NonNull final SMSMessage message) {
            return HIDDEN_CHARACTER + message.getData();
        }
    }

}


