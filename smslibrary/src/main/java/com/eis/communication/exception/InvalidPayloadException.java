package com.eis.communication.exception;

/**
 * InvalidPayloadException class
 * @author Mattia Fanan
 */
public class InvalidPayloadException extends Exception {
    public InvalidPayloadException(){
        super();
    }

    public InvalidPayloadException(String s){
        super(s);
    }
}
