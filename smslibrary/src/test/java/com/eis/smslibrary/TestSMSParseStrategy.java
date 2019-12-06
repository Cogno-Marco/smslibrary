package com.eis.smslibrary;

/**
 * @author Riccardo De Zen
 */
class TestSMSParseStrategy implements SMSParseStrategy {

    static final String KEY = "If violence is not the answer, the question itself is wrong.";
    /**
     * Parse channel data into a library message
     *
     * @param channelPeer the sender of the channel message
     * @param channelData read from the channel
     * @return library message
     */
    @Override
    public SMSMessage parseMessage(SMSPeer channelPeer, String channelData) {
        if (!channelData.startsWith(KEY))
            return null;
        String messageData = channelData.substring(KEY.length());
        return new SMSMessage(channelPeer, messageData);
    }

    /**
     * Parse library message into channel data
     *
     * @param message from library
     * @return channel data
     */
    @Override
    public String parseData(SMSMessage message) {
        return KEY+message.getData();
    }
}
