package com.eis.communication.parser;

import com.eis.communication.Message;

/**
 * Class used to switch from actual communication data to Message and back
 * use a <a href='https://en.wikipedia.org/wiki/Singleton_pattern'>Singleton pattern</a>
 *
 * @param <D> the data type used by the channel
 * @param <P> the type of Message to parse
 * @param <O> the type of Message to output
 * @see Message
 */
public interface MessageParser<D, P extends Message, O extends Message> {

    /**
     * Interprets data arrived via the communication channel and parses it to a message
     *
     * @param data from the communication channel
     * @return the message if the data has been parsed correctly, null otherwise
     */
    O parseMessage(D data);

    /**
     * Translates a message into data that can be sent via communication channel
     *
     * @param message to be translated
     * @return the data to send
     */
    D parseData(P message);
}
