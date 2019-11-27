package com.eis.communication;

/**
 * This is a <a href="https://refactoring.guru/design-patterns/strategy">strategy pattern</a>:
 * every implementation of MessageHandler can use different strategies based on what the user wants to do
 *
 * @param <D> The data type used by the channel
 * @param <P> The peer type used by the channel
 * @param <M> The type of message to parse and output
 * @author Luca Crema (thanks to Marco Cognolato for design pattern reference)
 */
public interface MessageParseStrategy<D, P extends Peer, M extends Message> {

    /**
     * Parse channel data into a library message
     *
     * @param channelData read from the channel
     * @param channelPeer the sender of the channel message
     * @return library message
     */
    M parseMessage(D channelData, P channelPeer);

    /**
     * Parse library message into channel data
     *
     * @param message from library
     * @return channel data
     */
    D parseData(M message);

}
