package com.eis.communication.header;

import com.eis.communication.Peer;

import java.io.Serializable;

/**
 * DestinationPeerHeader extends {@link Header} with destination {@link Peer} information inside
 *
 * @param <P> type of {@link Peer} which can be stored
 * @param <T> type of data in which the header's service information is saved
 * @author Mattia Fanan
 * @see Header
 * @since 08/12/2019 (gg/mm/aaaa)
 */
public interface DestinationPeerHeader<P extends Peer, T extends Serializable> extends Header<T> {
    /**
     * Gets the destination peer stored in this header
     *
     * @return destination peer stored in this header
     */
    P getDestination();
}
