package com.eis.communication;

import java.io.Serializable;

/**
 * Encapsulation of a message in the network
 * @param <D> Data to be transmitted
 * @param <P> Peer type of users of the network
 */
public interface  Message<D, P extends Peer> extends Serializable {

    /**
     * Retrieves the data sent or to be sent in the network
     * @return data contained in this message
     */
    D getData();

    /**
     * Retrieves the sender or the destination
     * @return the peer this message is for or is from
     */
    P getPeer();

}