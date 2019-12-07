package com.eis.communication.parser;

import com.eis.communication.Message;

/**
 * This is a <a href="https://refactoring.guru/design-patterns/strategy">strategy pattern</a>:
 * used as a strategy to switch from actual Message to communication data
 *
 * @param <D> the data type used by the channel
 * @param <P> the type of Message to parse
 * @author Luca Crema (thanks to Marco Cognolato for design pattern reference)
 * @author Mattia Fanan
 *
 * @see Message
 */
public interface DataParseStrategy<D,P extends Message> {

    /**
     * Parse library message into channel data
     *
     * @param message from library
     * @return channel data
     */
    D parseData(P message);
}
