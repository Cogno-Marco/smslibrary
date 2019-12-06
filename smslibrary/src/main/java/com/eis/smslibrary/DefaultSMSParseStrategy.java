package com.eis.smslibrary;

import androidx.annotation.NonNull;

public class DefaultSMSParseStrategy implements SMSParseStrategy {

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
