package com.eis.smslibrary.core;

/**
 * @author Mattia Fanan
 * combine of Android sms's data
 */
public class APIMessage {
    private String destination;
    private String textMessage;
    private String source;

    /**
     * build APIMessage
     * @param destination String destination's phone number
     * @param source String source's phone number
     * @param textMessage String messages text not null
     */
    protected APIMessage(String destination, String source,String textMessage){
        this.source=source;
        this.destination=destination;
        this.textMessage=textMessage;
    }

    /**
     * give destination's phone number
     * @return String destination's phone number maybe null if there isn't
     */
    public String getDestination(){
        return destination;
    }

    /**
     * give message's text
     * @return String message's text
     */
    public String getTextMessage(){
        return textMessage;
    }

    /**
     * give source's phone number
     * @return String source's phone number maybe null if there isn't
     */
    public String getSource(){
        return source;
    }
}
