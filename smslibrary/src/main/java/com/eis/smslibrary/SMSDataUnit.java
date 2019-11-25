package com.eis.smslibrary;

import com.eis.communication.DataUnit;
import com.eis.communication.exception.InvalidHeaderException;
import com.eis.communication.exception.InvalidPayloadException;

/**
 * @author Mattia Fanan
 * sms implementation of abstract message
 */
public class SMSDataUnit extends DataUnit<SMSHeader, SMSPayload> {


    /**
     * build the message
     *
     * @param header  header
     * @param payload payload
     * @throws InvalidHeaderException when invalid peer passed
     * @throws InvalidPayloadException   when invalid payload passed
     */
    public SMSDataUnit(SMSHeader header, SMSPayload payload) throws InvalidHeaderException, InvalidPayloadException {
        super(header, payload);
    }

    /**
     * get a string of the data unit's properties
     * @return String
     */
    @Override
    public String toString()
    {
        String stringRapp="Message:"+getPayload().getData();
        SMSPeer destination=getHeader().getDestinationPeer();
        SMSPeer source=getHeader().getSourcePeer();

        if(destination!=null)
            stringRapp=stringRapp+" ---Destination:"+destination.getAddress();
        if(source!=null)
            stringRapp=stringRapp+" ---Source:"+source.getAddress();

        return stringRapp;
    }

}