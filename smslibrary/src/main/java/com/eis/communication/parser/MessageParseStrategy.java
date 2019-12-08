package com.eis.communication.parser;

import com.eis.communication.Message;

/**
 * This is a <a href="https://refactoring.guru/design-patterns/strategy">strategy pattern</a>:
 * used as a strategy to switch from actual communication data to Message
 *
 * @param <D> the data type used by the channel
 * @param <O> the type of Message to output
 * @author Luca Crema (thanks to Marco Cognolato for design pattern reference)
 * @author Mattia Fanan
 * @see Message
 */
public interface MessageParseStrategy<D, O extends Message> {

    /**
     * Parse channel data into a library message
     *
     * @param channelData read from the channel
     * @return library message
     */
    O parseMessage(D channelData);

}
