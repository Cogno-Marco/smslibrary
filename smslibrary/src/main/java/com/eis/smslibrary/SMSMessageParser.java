package com.eis.smslibrary;

import androidx.annotation.NonNull;

import com.eis.communication.parser.DataParseStrategy;
import com.eis.communication.parser.MessageParseStrategy;
import com.eis.communication.parser.MessageParser;
import com.eis.smslibrary.core.APIParser;
import com.eis.smslibrary.exceptions.InvalidTelephoneNumberException;
import com.eis.smslibrary.header.AbstractSMSHeader;
import com.eis.smslibrary.header.SMSReceivedHeader;
import com.eis.smslibrary.message.SMSMessageReceived;
import com.eis.smslibrary.message.SMSMessageToSend;

/**
 * <a href="https://en.wikipedia.org/wiki/Singleton_pattern">Singleton pattern</a> class used to parse <code>String</code> to {@link SMSMessageReceived}
 * and {@link SMSMessageToSend} to <code>Strings</code>
 * <p>
 * Uses a <a href="https://en.wikipedia.org/wiki/Strategy_pattern">Strategy pattern</a> to parse message and data, so that any user can update them to its preferred parser
 * <i>By defaults it uses strategies defined by the library</i>
 *
 * @author Luca Crema
 * @author Alberto Ursino
 * @author Marco Mariotto
 * @author Mattia Fanan
 * @see DataParseStrategy
 * @see MessageParseStrategy
 * @see MessageParser
 */
public class SMSMessageParser implements MessageParser<String, SMSMessageToSend, SMSMessageReceived> {

    private MessageParseStrategy<String, SMSMessageReceived> messageParseStrategy;
    private DataParseStrategy<String, SMSMessageToSend> dataParseStrategy;
    private static SMSMessageParser instance;

    /**
     * Private constructor
     */
    private SMSMessageParser() {
        messageParseStrategy = new DefaultSMSMessageParseStrategy();
        dataParseStrategy = new DefaultDataParseStrategy();
    }

    /**
     * Gets singleton instance for this class
     *
     * @return Singleton instance of this class
     */
    public static SMSMessageParser getInstance() {
        if (instance == null)
            instance = new SMSMessageParser();
        return instance;
    }

    /**
     * Updates the message parse strategy to a custom one
     *
     * @param messageParseStrategy the custom message parse strategy
     */
    public void setMessageParseStrategy(@NonNull final MessageParseStrategy<String, SMSMessageReceived> messageParseStrategy) {
        this.messageParseStrategy = messageParseStrategy;
    }

    /**
     * Updates the data parse strategy to a custom one
     *
     * @param dataParseStrategy the custom data parse strategy
     */
    public void setDataParseStrategy(@NonNull final DataParseStrategy<String, SMSMessageToSend> dataParseStrategy) {
        this.dataParseStrategy = dataParseStrategy;
    }

    /**
     * Interprets a <code>String</code> arrived via the communication channel and parses it to a library message
     *
     * @param data the data to parse from the lower layer protocol
     * @return the message if the string has been parsed correctly, <code>null</code> otherwise
     */
    public SMSMessageReceived parseMessage(@NonNull final String data) {
        return messageParseStrategy.parseMessage(data);
    }

    /**
     * Translates a message into a string that can be passed to the lower layer protocol
     *
     * @param message the message to be parsed
     * @return the channel data if the message has been parsed correctly, <code>null</code> otherwise
     */
    public String parseData(@NonNull final SMSMessageToSend message) {
        return dataParseStrategy.parseData(message);
    }

    /**
     * DefaultSMSMessageParseStrategy is the default {@link MessageParseStrategy} for this class
     */
    public class DefaultSMSMessageParseStrategy implements MessageParseStrategy<String, SMSMessageReceived> {


        /**
         * Parse channel data into a SMSMessageReceived
         *
         * @param channelData the data read from the channel
         * @return the message if it's possible to parse from the channel data, <code>null</code> otherwise
         */
        @Override
        public SMSMessageReceived parseMessage(String channelData) {
            try {
                String foundAddress = channelData.substring(0, APIParser.PHONE_NUMBER_LENGTH);
                String foundData = channelData.substring(APIParser.PHONE_NUMBER_LENGTH);

                //check protocol id
                if (!foundData.startsWith(AbstractSMSHeader.ID))
                    return null;

                //retrieve source address and delete padding
                foundAddress = foundAddress.substring(0, foundAddress.indexOf(APIParser.PADDING));

                SMSPeer sourcePeer;

                //try to parse the source address
                try {
                    sourcePeer = new SMSPeer(foundAddress);
                } catch (InvalidTelephoneNumberException e) {
                    return null;
                }

                //retrieve message data and delete protocol id
                String messageData = foundData.substring(1);

                return new SMSMessageReceived(new SMSReceivedHeader(sourcePeer), messageData);
            } catch (Exception e) {
                return null;
            }
        }
    }

    /**
     * DefaultDataParseStrategy is the default {@link DataParseStrategy} for this class
     */
    public class DefaultDataParseStrategy implements DataParseStrategy<String, SMSMessageToSend> {

        /**
         * Parse SMSMessageToSend into channel data
         *
         * @param message the message from the upper level protocol
         * @return the channel data if it's possible to parse from the message, <code>null</code> otherwise
         */
        @Override
        public String parseData(SMSMessageToSend message) {

            String address = message.getHeader().getDestination().getAddress();
            //adding padding to the address
            //addPadding never return null cause address is always shorter than 16
            address = APIParser.addPadding(address);

            return address + message.getHeader().getID() + message.getData();
        }
    }

}


