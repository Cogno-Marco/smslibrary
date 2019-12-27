package com.eis.communication;

import java.io.Serializable;

/**
 * Encapsulates data to be sent (or received) to (from) a certain peer.
 *
 * @param <D> Data to be transmitted
 * @param <P> Peer type of users of the network
 */
public interface Message<D, P extends Peer> extends Serializable {

    /**
     * Retrieves the data received or to be sent in the network
     *
     * @return data contained in this message
     */
    D getData();

    /**
     * Retrieves the sender or the destination of this message
     *
     * @return Peer associated with this message
     */
    P getPeer();

}