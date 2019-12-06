package com.eis.smslibrary;

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
        String body = KEY + channelData;
        return new SMSMessage(channelPeer, body);
    }

    /**
     * Parse library message into channel data
     *
     * @param message from library
     * @return channel data
     */
    @Override
    public String parseData(SMSMessage message) {
        String body = message.getData();
        if(body.startsWith(KEY)) return body.substring(KEY.length());
        return null;
    }
}
