package com.eis.smslibrary.message;

import com.eis.smslibrary.SMSPeer;
import com.eis.smslibrary.SMSPeerTest;
import com.eis.smslibrary.exceptions.InvalidSMSMessageException;
import com.eis.smslibrary.header.SMSToSendHeader;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Alberto Ursino
 * @author Luca Crema
 * @author Mattia Fanan
 */
public class SMSToSendMessageTest {


    private SMSPeer validPeer;
    private SMSToSendHeader validHeader;
    private SMSMessageToSend smsMessageToSend;


    @Before
    public void init() {
        validPeer=new SMSPeer(SMSPeerTest.VALID_TELEPHONE_NUMBER);
        validHeader=new SMSToSendHeader(validPeer);
        smsMessageToSend = new SMSMessageToSend(validHeader, AbstractSMSMessageTest.VALID_TEXT_MESSAGE);
    }

    @Test(expected = InvalidSMSMessageException.class)
    public void smsMessage_constructor_notValidTextMessage() {
        SMSMessageToSend msg = new SMSMessageToSend(validHeader, AbstractSMSMessageTest.TOO_LONG_TEXT_MESSAGE);
        Assert.fail("Should have thrown an InvalidSMSMessageException");
    }


    @Test
    public void getData_smsData_isEquals() {
        Assert.assertEquals(AbstractSMSMessageTest.VALID_TEXT_MESSAGE, smsMessageToSend.getData());
    }

    @Test
    public void getHeader_validHeader_isEquals(){
        Assert.assertEquals(smsMessageToSend.getHeader(),validHeader);
    }
}
