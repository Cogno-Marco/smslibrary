package com.eis.smslibrary;

import com.eis.communication.Payload;
import com.eis.communication.exception.InvalidPayloadException;

/**
 * @author Mattia Fanan
 * sms implementation of payloadData
 */
public class SMSPayload extends Payload<String> {
    //TODO check if is 160 or 154 char
    public static final int MAX_PAYLOAD_LENGTH=160-SMSHeader.LENGTH;

    /**
     * build the smsPayload
     * @param data
     * @throws InvalidPayloadException if a non valid data is passed
     */
    public SMSPayload(String data) throws InvalidPayloadException {
        super(data);
    }

    /**
     * method that decides what is a valid data for the payload
     * @param data to validate
     * @return true if data is not null and shorther than MAX_PAYLOAD_LENGTH
     */
    @Override
    protected boolean isValidData(String data) {
        return data!=null && data.length()<=MAX_PAYLOAD_LENGTH;
    }

}
