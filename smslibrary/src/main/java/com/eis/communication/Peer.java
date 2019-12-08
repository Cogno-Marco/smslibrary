package com.eis.communication;

import java.io.Serializable;

/**
 * Represents a user in the network
 *
 * @param <A> The type of address used by the network
 * @see Serializable
 */
public interface Peer<A> extends Serializable {

    /**
     * Retrieves the peer's address
     *
     * @return peer's address
     */
    A getAddress();

}
