package com.eis.smslibrary;

class TestSMSParseStrategy implements SMSParseStrategy {
    /**
     * Parse channel data into a library message
     *
     * @param channelPeer the sender of the channel message
     * @param channelData read from the channel
     * @return library message
     */
    @Override
    public SMSMessage parseMessage(SMSPeer channelPeer, String channelData) {
        return null;
    }

    /**
     * Parse library message into channel data
     *
     * @param message from library
     * @return channel data
     */
    @Override
    public String parseData(SMSMessage message) {
        return null;
    }
}
