package com.eis.smslibrary.random;

import com.eis.communication.Peer;

/**
 * @param <P> the type of generated Peer.
 * @author Riccardo De Zen.
 */
public interface RandomPeerGenerator<A, P extends Peer<A>> {
    /**
     * @return a valid built Peer.
     */
    P generateValidPeer();

    /**
     * @return an example of valid address.
     */
    A generateValidAddress();

    /**
     * @return an example of invalid address.
     */
    A generateInvalidAddress();
}
