package com.eis.communication;

/**
 * Handles communications in a channel
 *
 * @param <T> message class to be used for the channel
 */
public interface CommunicationHandler<T extends Message> {

    /**
     * Sends a single message in the channel, message content must be shorter than the maximum channel message size
     * @param message message to be sent in the channel to a peer
     */
    void sendMessage(T message);

}