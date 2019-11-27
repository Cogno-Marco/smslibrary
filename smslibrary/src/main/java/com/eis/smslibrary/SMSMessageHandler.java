package com.eis.smslibrary;

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
    private SMSMessageHandler(){
        parseStrategy = new DefaultSMSMessageParseStrategy();
    }

    /**
     * @return Singleton instance of this class
     */
    public static SMSMessageHandler getInstance(){
        if(instance == null)
            instance = new SMSMessageHandler();
        return instance;
    }

    /**
     * Update the parse strategy to a custom one
     * @param parseStrategy ?
     */
    public void setMessageParseStrategy(MessageParseStrategy<String,SMSPeer,SMSMessage> parseStrategy){
        this.parseStrategy = parseStrategy;
    }

    /**
     * Interprets a string arrived via the communication channel and parses it to a library {@link SMSMessage}
     *
     * @param peerData from the sms pdus
     * @param messageData from the sms pdus
     * @return the message if the string has been parsed correctly, null otherwise
     */
    public SMSMessage parseMessage(String peerData, String messageData){
        if(SMSPeer.checkPhoneNumber(peerData) != SMSPeer.TelephoneNumberState.TELEPHONE_NUMBER_VALID)
            return null;

        return parseStrategy.parseMessage(messageData,new SMSPeer(peerData));
    }

    /**
     * Translates a message into a string that can be sent via sms
     *
     * @param message message to be translated/parsed
     * @return the string to send
     */
    public String parseData(SMSMessage message){
        return parseStrategy.parseData(message);
    }

}

class DefaultSMSMessageParseStrategy implements MessageParseStrategy<String, SMSPeer, SMSMessage>{

    protected static final String HIDDEN_CHARACTER = (char) 0x02 + "";

    /**
     * Parses sms data into a SMSMessage if possible
     * @param channelData read from the channel
     * @return the parsed SMSMessage if the string was correct, null otherwise
     */
    @Override
    public SMSMessage parseMessage(String channelData, SMSPeer channelPeer) {
        //First character of the content must be the hidden char
        if (!channelData.startsWith(HIDDEN_CHARACTER))
            return null;

        return new SMSMessage(channelPeer, channelData);
    }

    /**
     * Parses SMSMessage into sms content data
     * @param message from library
     * @return the parsed sms content data ready to be sent
     */
    @Override
    public String parseData(SMSMessage message) {
        return HIDDEN_CHARACTER + message.getData();
    }
}
