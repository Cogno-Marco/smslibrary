package com.eis.communication;

import com.eis.communication.header.Header;

import java.io.Serializable;

/**
 * Encapsulation of a message in the network
 *
 * @param <H> {@link Header} containing all message's service information
 * @param <D> data to be transmitted
 * @see Serializable
 */
public interface Message<H extends Header<D>, D extends Serializable> extends Serializable {

    /**
     * Gets the data contained in this message
     *
     * @return data contained in this message
     */
    D getData();

    /**
     * Gets the header contained in this message
     *
     * @return the header contained in this message
     */
    H getHeader();

}