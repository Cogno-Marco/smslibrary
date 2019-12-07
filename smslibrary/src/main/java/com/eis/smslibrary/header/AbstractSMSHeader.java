package com.eis.smslibrary.header;

import androidx.annotation.NonNull;

import com.eis.communication.header.Header;
import com.eis.smslibrary.SMSPeer;

/**
 * AbstractSMSHeader is the base {@link Header} having all the common properties an {@link Header} in this layer must have
 * <ul>
 *     <li>a protocol identifier</li>
 *     <li>a {@link SMSPeer} peer</li>
 * </ul>
 * <i>Used as a base for building other more complex data-link headers without code replication</i>
 *
 * @author Mattia Fanan
 *
 * @see Header
 */
public class AbstractSMSHeader implements Header<String> {
    //protocol identifier
    public static final String ID = (char)0x02 + "";
    protected SMSPeer peer;

    /**
     * Constructor for AbstractSMSHeader
     *
     * @param peer the peer to be stored in this header
     */
    public AbstractSMSHeader(@NonNull SMSPeer peer){
        if(peer == null)
            throw new NullPointerException();

        this.peer = peer;
    }

    /**
     * Gets the ID associated with the protocol the message having this header is implementing
     *
     * @return protocol message's ID
     */
    @Override
    public String getID() {
        return ID;
    }
}
