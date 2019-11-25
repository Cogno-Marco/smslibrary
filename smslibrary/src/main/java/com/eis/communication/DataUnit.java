package com.eis.communication;

import com.eis.communication.exception.InvalidHeaderException;
import com.eis.communication.exception.InvalidPayloadException;

/**
 * DataUnit abstract class
 * @author Mattia Fanan
 *
 * @param H Header data-type
 * @param M Payload data-type
 */
public abstract class DataUnit<H extends Header, M extends Payload> {
    protected H header;
    protected M payload;

    /**
     * Create a message
     * @param header Header of the DataUnit
     * @param payload Payload of the DataUnit
     * @throws InvalidHeaderException when an invalid header is passed
     * @throws InvalidPayloadException when an invalid payload is passed
     */
    public DataUnit(H header, M payload) throws InvalidHeaderException, InvalidPayloadException {
        if (header == null || !header.isValid())
            throw new InvalidHeaderException();

        if (payload == null || !payload.isValid())
            throw new InvalidPayloadException();

        this.payload = payload;
        this.header = header;
    }

    /**
     * Get header of the DataUnit
     * @return header of the DataUnit
     */
    public H getHeader() {
        return header;
    }

    /**
     * Get payload of the DataUnit
     * @return the payload of the DataUnit
     */
    public M getPayload() {
        return payload;
    }

    /**
     * Set header of the DataUnit
     * @param header Header to assign to the DataUnit
     * @return true if header is valid, false if header is not valid
     */
    public boolean setHeader(H header){
        if (header == null || !header.isValid())
            return false;

        this.header = header;
        return true;
    }

    /**
     * Set payload of the DataUnit
     * @param payload Payload to assign to the DataUnit
     * @return true if payload is valid, false if payload is not valid
     */
    public boolean setPayload(M payload) {
        if (payload==null || !payload.isValid())
            return false;

        this.payload = payload;
        return true;
    }

    /**
     * Check if DataUnit is valid
     * @return true if DataUnit is valid, false if DataUnit is not valid
     */
    public boolean isValid() {
        return header.isValid() && payload.isValid();
    }
}
