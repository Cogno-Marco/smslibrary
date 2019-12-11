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
     * @return Peer associated with this message
     */
    P getPeer();

    /**
     * Returns the time in milliseconds at which this Message object was created.
     *
     * @return the difference, measured in milliseconds, between the time of the creation of this
     *         object and midnight, January 1, 1970 UTC
     */
    long getUnixTime();

    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * @param   o the reference object with which to compare.
     * @return  {@code true} if this object is the same as the o argument; {@code false} otherwise.
     */
    boolean equals(Object o);

    /**
     * Returns a hash code value for the object. This method is supported for the benefit of hash
     * tables such as those provided by {@link java.util.HashMap}.
     *
     * @return a hash code value for this object.
     */
    int hashCode();

}