package com.eis.communication.header;

import com.eis.communication.Peer;

import java.io.Serializable;

/**
 * SourcePeerHeader extends {@link Header} with source {@link Peer} information inside
 *
 * @param <P> type of {@link Peer} which can be stored
 * @param <T> type of data in which the header's service information is saved
 * @author Mattia Fanan
 * @see Header
 * @since 08/12/2019 (gg/mm/aaaa)
 */
public interface SourcePeerHeader<P extends Peer, T extends Serializable> extends Header<T> {
    /**
     * Gets the source peer stored in this header
     *
     * @return source peer stored in this header
     */
    P getSource();
}
