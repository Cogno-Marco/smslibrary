package com.eis.smslibrary.core;

import com.eis.communication.exception.InvalidHeaderException;
import com.eis.communication.exception.InvalidPayloadException;
import com.eis.communication.exception.InvalidPeerException;
import com.eis.smslibrary.SMSDataUnit;
import com.eis.smslibrary.SMSHeader;
import com.eis.smslibrary.SMSPayload;
import com.eis.smslibrary.SMSPeer;

/**
 * @author Mattia Fanan
 * adapter from APIMessage to SMSDataUnit
 */
public class SMSAdapter {

    /**
     * adapt SMSDataUnit to APIMessage
     * @param dataUnit SMSDataUnit to adapt
     * @return adapted APIMessage
     */
    public static APIMessage adaptToAPIMessage(SMSDataUnit dataUnit) {
        return new APIMessage(
                dataUnit.getHeader().getDestinationPeer().getAddress(),
                dataUnit.getHeader().getSourcePeer().getAddress(),
                dataUnit.getHeader().getStamp() + dataUnit.getPayload().getData()
        );
    }

    /**
     * adapt APIMessage to SMSDataUnit
     * @param apiMessage APIMessage to adapt
     * @return adapted SMSDataUnit
     * @throws InvalidHeaderException when the APIMessage stamp isn't recognized
     * @throws InvalidPayloadException when the APIMessage TextMessage can't become a well formed SMSPayload
     * @throws InvalidPeerException when APIMessage source address can't become a well formed SMSPeer
     */
    public static SMSDataUnit adaptToSMSDataUnit(APIMessage apiMessage)
            throws InvalidHeaderException, InvalidPayloadException, InvalidPeerException {

        SMSPeer source=null;
        SMSPeer destination=null;

        if(apiMessage.getSource()!=null)
            source=new SMSPeer(apiMessage.getSource());

        if(apiMessage.getDestination()!=null)
            destination=new SMSPeer(apiMessage.getDestination());

        SMSHeader header=new SMSHeader(destination,source);
        //extract sms stamp
        String stamp=apiMessage.getTextMessage().substring(0,SMSHeader.LENGTH);
        //compare the two stamps
        if(stamp.compareTo(header.getStamp())!=0)
            throw new InvalidPayloadException();
        //extract payload
        String smsText=apiMessage.getTextMessage().substring(SMSHeader.LENGTH);

        return new SMSDataUnit(
                header,
                new SMSPayload(smsText)
        );
    }


}
