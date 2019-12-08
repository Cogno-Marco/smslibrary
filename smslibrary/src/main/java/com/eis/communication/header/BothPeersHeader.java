package com.eis.communication.header;

import com.eis.communication.Peer;

import java.io.Serializable;

/**
 * BothPeersHeader extends {@link Header} with both source and destination {@link Peer} inside
 *
 * @param <P> type of {@link Peer} which can be stored
 * @param <T> type of data in which the header's service information is saved
 * @author Mattia Fanan
 * @see Header
 * @see SourcePeerHeader
 * @see DestinationPeerHeader
 * @since 08/12/2019 (gg/mm/aaaa)
 */
public interface BothPeersHeader<P extends Peer, T extends Serializable> extends SourcePeerHeader<P, T>, DestinationPeerHeader<P, T> {
}
