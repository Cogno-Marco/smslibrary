package com.eis.communication;

import androidx.annotation.NonNull;

import java.io.Serializable;

/**
 * Represents a user in the network
 * @param <A> The type of address used by the network
 */
public interface Peer<A> extends Serializable {

    /**
     * Retrieves the peer's address
     * @return peer's address
     */
    A getAddress();

    /**
     * Sets or updates the peer's address
     * @param address new address for the peer
     */
    void setAddress(A address);

}
