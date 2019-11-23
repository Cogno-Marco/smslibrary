package com.eis.communication;

import androidx.annotation.NonNull;

import java.io.Serializable;

/**
 * Represents a user in the network
 * @param <A> The type of address used by the network
 */
public interface Peer<A> extends Serializable {

    A getAddress();

    void setAddress(A address);

    @NonNull
    @Override
    String toString();

}
