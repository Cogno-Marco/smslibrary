package com.eis.communication;

/**
 * Class used to switch from actual communication data to Message and back
 * Should use a <a href='https://en.wikipedia.org/wiki/Singleton_pattern'>Singleton pattern</a>
 *
 * @param <D> The data type used by the channel
 * @param <P> The peer type used by the channel
 * @param <M> The type of message to parse and output
 */
public interface MessageHandler<D,P,M extends Message> {

    /**
     * Interprets data arrived via the communication channel and parses it to a message
     *
     * @param peerData from the communication channel
     * @param data from the communication channel
     * @return the message if the data has been parsed correctly, null otherwise
     */
    M parseMessage(P peerData, D data);

    /**
     * Translates a message into data that can be sent via communication channel
     *
     * @param message message to be translated
     * @return the data to send
     */
    D parseData (M message);
}
