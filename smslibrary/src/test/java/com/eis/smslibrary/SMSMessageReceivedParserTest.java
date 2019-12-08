package com.eis.smslibrary;

import com.eis.communication.parser.DataParseStrategy;
import com.eis.communication.parser.MessageParseStrategy;
import com.eis.smslibrary.header.SMSToSendHeader;
import com.eis.smslibrary.message.AbstractSMSMessageTest;
import com.eis.smslibrary.message.SMSMessageReceived;
import com.eis.smslibrary.message.SMSMessageToSend;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Luca Crema
 * @author Mattia Fanan
 */
public class SMSMessageReceivedParserTest {

    private SMSMessageParser defaultInstance;
    private static final MessageParseStrategy<String, SMSMessageReceived> DEFAULT_SMS_MESSAGE_PARSE_STRATEGY = SMSMessageParser.getInstance().new DefaultSMSMessageParseStrategy();
    private static final DataParseStrategy<String, SMSMessageToSend> DEFAULT_DATA_PARSE_STRATEGY = SMSMessageParser.getInstance().new DefaultDataParseStrategy();

    private static final SMSPeer DEFAULT_PEER = new SMSPeer(SMSPeerTest.VALID_TELEPHONE_NUMBER);
    private static final String DEFAULT_MESSAGE_CONTENT = AbstractSMSMessageTest.VALID_TEXT_MESSAGE;

    private static final SMSMessageToSend DEFAULT_TO_SEND_MESSAGE = new SMSMessageToSend(new SMSToSendHeader(DEFAULT_PEER), DEFAULT_MESSAGE_CONTENT);


    @Before
    public void init() {
        defaultInstance = SMSMessageParser.getInstance();

        defaultInstance.setMessageParseStrategy(DEFAULT_SMS_MESSAGE_PARSE_STRATEGY);
        defaultInstance.setDataParseStrategy(DEFAULT_DATA_PARSE_STRATEGY);
    }

    @Test
    public void getInstance_isNotNull() {
        Assert.assertNotNull(SMSMessageParser.getInstance());
    }


    @Test
    public void parseMessage_message_send_to_receive() {
        String data = defaultInstance.parseData(DEFAULT_TO_SEND_MESSAGE);
        SMSMessageReceived parsedMessage = defaultInstance.parseMessage(data);

        if (!DEFAULT_TO_SEND_MESSAGE.getData().equals(parsedMessage.getData()))
            Assert.fail("data should be the same");

        if (!DEFAULT_TO_SEND_MESSAGE.getHeader().getDestination().equals(parsedMessage.getHeader().getSource()))
            Assert.fail("source and destination should be the same");
    }

    /**
     * If a normal sms is parsed (without hidden character) it should return null
     */
    @Test
    public void parseMessage_wrongSMS_isNull() {
        //the peer address works like a message without hidden character
        Assert.assertNull(defaultInstance.parseMessage(DEFAULT_PEER.getAddress()));
    }
}