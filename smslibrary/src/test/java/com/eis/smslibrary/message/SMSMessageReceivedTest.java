package com.eis.smslibrary.message;

import com.eis.smslibrary.SMSPeer;
import com.eis.smslibrary.SMSPeerTest;
import com.eis.smslibrary.exceptions.InvalidSMSMessageException;
import com.eis.smslibrary.header.SMSReceivedHeader;
import com.eis.smslibrary.message.SMSMessageReceived;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Alberto Ursino
 * @author Luca Crema
 * @author Mattia Fanan
 */
public class SMSMessageReceivedTest {

    private SMSPeer validPeer;
    private SMSReceivedHeader validHeader;
    private SMSMessageReceived smsMessageReceived;


    @Before
    public void init() {
        validPeer=new SMSPeer(SMSPeerTest.VALID_TELEPHONE_NUMBER);
        validHeader=new SMSReceivedHeader(validPeer);
        smsMessageReceived = new SMSMessageReceived(validHeader, AbstractSMSMessageTest.VALID_TEXT_MESSAGE);
    }

    @Test(expected = InvalidSMSMessageException.class)
    public void smsMessage_constructor_notValidTextMessage() {
        SMSMessageReceived msg = new SMSMessageReceived(validHeader,AbstractSMSMessageTest.TOO_LONG_TEXT_MESSAGE);
        Assert.fail("Should have thrown an InvalidSMSMessageException");
    }

    @Test
    public void getData_smsData_isEquals() {
        Assert.assertEquals(AbstractSMSMessageTest.VALID_TEXT_MESSAGE, smsMessageReceived.getData());
    }

    @Test
    public void getHeader_validHeader_isEquals(){
        Assert.assertEquals(smsMessageReceived.getHeader(),validHeader);
    }
}
