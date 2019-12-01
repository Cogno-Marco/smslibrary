package com.eis.smslibrary;

import com.eis.communication.MessageParseStrategy;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Luca Crema
 */
public class SMSMessageHandlerTest {

    private SMSMessageHandler defaultInstance;
    private static final MessageParseStrategy<String,SMSPeer,SMSMessage> DEFAULT_SMS_MESSAGE_PARSE_STRATEGY = SMSMessageHandler.getInstance().new DefaultSMSMessageParseStrategy();
    private static final SMSPeer DEFAULT_PEER = new SMSPeer("+393465433432");
    private static final String DEFAULT_MESSAGE_CONTENT = "Test content";
    private static final SMSMessage DEFAULT_MESSAGE = new SMSMessage(DEFAULT_PEER,DEFAULT_MESSAGE_CONTENT);

    @Before
    public void init(){
        defaultInstance = SMSMessageHandler.getInstance();

        defaultInstance.setMessageParseStrategy(DEFAULT_SMS_MESSAGE_PARSE_STRATEGY);
    }

    @Test
    public void getInstance_isNotNull() {
        Assert.assertNotNull(SMSMessageHandler.getInstance());
    }


    @Test
    public void parseMessage_message_isCorrect() {
        Assert.assertEquals(DEFAULT_MESSAGE.getData(),defaultInstance.parseMessage(DEFAULT_PEER.getAddress(), defaultInstance.parseData(DEFAULT_MESSAGE)).getData());
    }

    /**
     * If a normal sms is parsed (without hidden character) it should return null
     */
    @Test
    public void parseMessage_wrongSMS_isNull(){
        Assert.assertNull(defaultInstance.parseMessage(DEFAULT_PEER.getAddress(),DEFAULT_MESSAGE_CONTENT));
    }
}