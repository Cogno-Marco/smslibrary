package com.eis.smslibrary.core;

import com.eis.smslibrary.SMSPeerTest;
import com.eis.smslibrary.message.AbstractSMSMessageTest;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Mattia Fanan
 */
public class APIMessageTest {

    public static final APIMessage validAPIMessage = new APIMessage(SMSPeerTest.VALID_TELEPHONE_NUMBER, AbstractSMSMessageTest.VALID_TEXT_MESSAGE);

    @Test
    public void setUp() {
        try {

            new APIMessage(SMSPeerTest.VALID_TELEPHONE_NUMBER, AbstractSMSMessageTest.VALID_TEXT_MESSAGE);
        } catch (NullPointerException e) {
            Assert.fail("shouldn't throw NullPointerException");
        }
    }

    @Test(expected = NullPointerException.class)
    public void setUpNullText() {

        new APIMessage(SMSPeerTest.VALID_TELEPHONE_NUMBER, null);
        Assert.fail("shouldn't throw NullPointerException");
    }

    @Test(expected = NullPointerException.class)
    public void setUpNullPhoneNumber() {

        new APIMessage(null, AbstractSMSMessageTest.VALID_TEXT_MESSAGE);
        Assert.fail("shouldn't throw NullPointerException");
    }

    @Test
    public void getPhoneNumber_validNumber_isEquals() {
        Assert.assertEquals(validAPIMessage.getPhoneNumber(), SMSPeerTest.VALID_TELEPHONE_NUMBER);
    }

    @Test
    public void getTextMessage_validText_isEquals() {
        Assert.assertEquals(validAPIMessage.getTextMessage(), AbstractSMSMessageTest.VALID_TEXT_MESSAGE);
    }
}
