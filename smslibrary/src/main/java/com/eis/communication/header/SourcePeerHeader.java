package com.eis.communication.header;

import com.eis.communication.Peer;

import java.io.Serializable;

/**
 * SourcePeerHeader extends {@link Header} with source {@link Peer} information inside
 *
 * @param <P> type of {@link Peer} which can be stored
 * @param <T> type of data in which the header's service information is saved
 *
 * @see Header
 *
 * @author Mattia Fanan
 */
public interface SourcePeerHeader<P extends Peer, T extends Serializable> extends Header<T> {
    /**
     * Gets the source peer stored in this header
     *
     * @return source peer stored in this header
     */
    P getSource();
}
