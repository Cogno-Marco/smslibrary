package com.eis.smslibrary;

import com.eis.communication.Header;
import com.eis.communication.exception.InvalidHeaderException;
import com.eis.communication.exception.InvalidPeerException;

/**
 * @author Mattia Fanan
 * header of the SMSDataUnit
 */
public class SMSHeader extends Header<SMSPeer,String> {
    private static final String stamp=Character.toString((char)0x03A6);//capital phi
    public static final int LENGTH=1;

    /**
     * build the header
     * only one peer could be null
     * @param destination SMSPeer destination peer
     * @param source SMSPeer source peer
     * @throws InvalidHeaderException when all the peers are null
     * @throws InvalidPeerException when a not valid peer is found
     */
    public SMSHeader(SMSPeer destination,SMSPeer source)throws InvalidHeaderException,InvalidPeerException
    {
        canHaveNullPeer=true;

        //they can't be both null
        if(destination==null && source==null)
            throw new InvalidHeaderException();

        if(destination!=null)
            if(!destination.isValid())
                throw new InvalidPeerException();
            else
                destinationPeer=destination;

        if(source!=null)
            if(!source.isValid())
                throw new InvalidPeerException();
            else
                sourcePeer=source;
    }

    /**
     * get header's data to add to the payload
     * @return String header to add to the payload
     */
    @Override
    public String getStamp() {
        return stamp;
    }

    /**
     * methods that decides what is a valid header
     * @return true if is valid
     */
    @Override
    public boolean isValid() {
        return (sourcePeer!=null && sourcePeer.isValid()) || (destinationPeer!=null&&destinationPeer.isValid());
    }

}
