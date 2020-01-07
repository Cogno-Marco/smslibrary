package com.eis.smslibrary;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Luca Crema
 */
public class SMSMessageHandlerTest {

    private SMSMessageHandler defaultInstance;
    private static final SMSPeer DEFAULT_PEER = new SMSPeer("+393465433432");
    private static final String DEFAULT_MESSAGE_CONTENT = "Test content";
    private static final SMSMessage DEFAULT_MESSAGE = new SMSMessage(DEFAULT_PEER,DEFAULT_MESSAGE_CONTENT);

    @Before
    public void init(){
        defaultInstance = SMSMessageHandler.getInstance(null);
    }

    @Test
    public void getInstance_isNotNull() {
        Assert.assertNotNull(SMSMessageHandler.getInstance(null));
    }


    @Test
    public void parseMessage_message_isCorrect() {
        Assert.assertEquals(DEFAULT_MESSAGE.getData(),defaultInstance.parseData(DEFAULT_PEER.getAddress(), defaultInstance.parseMessage(DEFAULT_MESSAGE)).getData());
    }

    /**
     * If a normal sms is parsed (without hidden character) it should return null
     */
    @Test
    public void parseMessage_wrongSMS_isNull(){
        Assert.assertNull(defaultInstance.parseData(DEFAULT_PEER.getAddress(),DEFAULT_MESSAGE_CONTENT));
    }
}