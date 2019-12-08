package com.eis.smslibrary.header;

import androidx.annotation.NonNull;

import com.eis.communication.header.SourcePeerHeader;
import com.eis.smslibrary.SMSPeer;

/**
 * SMSReceivedHeader is {@link AbstractSMSHeader} extension implementing {@link SourcePeerHeader} in order to obtain a suitable header for {@link com.eis.smslibrary.message.SMSMessageReceived}
 *
 * @author Mattia Fanan
 * @since 08/12/2019 (gg/mm/aaaa)
 */
public final class SMSReceivedHeader extends AbstractSMSHeader implements SourcePeerHeader<SMSPeer, String> {

    /**
     * Constructor for SMSReceivedHeader
     *
     * @param sourcePeer the peer to store in this header
     */
    public SMSReceivedHeader(@NonNull SMSPeer sourcePeer) {
        super(sourcePeer);
    }

    /**
     * Gets the source peer stored in this header
     *
     * @return the source peer stored in this header
     */
    @Override
    public SMSPeer getSource() {
        return peer;
    }

    /**
     * Indicates whether some other object is "equal to" this one
     *
     * @param o The reference object with which to compare.
     * @return <code>true</code> if this object is the same as the obj argument; <code>false</code>otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof SMSReceivedHeader))
            return false;

        SMSReceivedHeader header = (SMSReceivedHeader) o;

        return peer.equals(header.getSource());
    }

    /**
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode() {
        return 47 * 11 * peer.hashCode();
    }
}
