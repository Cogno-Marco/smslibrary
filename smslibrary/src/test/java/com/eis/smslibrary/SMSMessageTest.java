package com.eis.smslibrary;

import com.eis.smslibrary.exceptions.InvalidSMSMessageException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Alberto Ursino, Luca Crema
 */
public class SMSMessageTest{

    private SMSMessage smsMessage;
    private static final int MAX_MSG_TEXT_LEN = SMSMessage.MAX_MSG_TEXT_LEN;
    private static final String MAIN_MESSAGE = "Test message";
    private static final String VALID_TEXT_MESSAGE = MAIN_MESSAGE;
    private static final String TOO_LONG_TEXT_MESSAGE = new String(new char[MAX_MSG_TEXT_LEN * 2]).replace('\0', ' ');
    private static final String MAX_LENGTH_TEXT_MESSAGE = new String(new char[MAX_MSG_TEXT_LEN]).replace('\0', ' ');
    private static final String MAX_LENGTH_TEXT_MESSAGE_P1 = new String(new char[MAX_MSG_TEXT_LEN + 1]).replace('\0', ' '); //P1 = Plus 1
    private static final String EMPTY_TEXT_MESSAGE = "";
    private static final String VALID_TELEPHONE_NUMBER = "+393433433433";


    @Before
    public void init() {
        smsMessage = new SMSMessage(new SMSPeer(VALID_TELEPHONE_NUMBER), VALID_TEXT_MESSAGE);
    }

    @Test(expected = InvalidSMSMessageException.class)
    public void smsMessage_constructor_notValidTextMessage() {
        SMSMessage msg = new SMSMessage(new SMSPeer(VALID_TELEPHONE_NUMBER), TOO_LONG_TEXT_MESSAGE);
        Assert.fail("Should have thrown an InvalidSMSMessageException");
    }

    @Test
    public void checkMessageText_smsText_isTooLong() {
        Assert.assertEquals(SMSMessage.ContentState.MESSAGE_TEXT_TOO_LONG, SMSMessage.checkMessageText(TOO_LONG_TEXT_MESSAGE));
    }

    @Test
    public void checkMessageText_smsText_isTooLongByOne() {
        Assert.assertEquals(SMSMessage.ContentState.MESSAGE_TEXT_TOO_LONG, SMSMessage.checkMessageText(MAX_LENGTH_TEXT_MESSAGE_P1));
    }

    @Test
    public void checkMessageText_smsText_isMaxLength() {
        Assert.assertEquals(SMSMessage.ContentState.MESSAGE_TEXT_VALID, SMSMessage.checkMessageText(MAX_LENGTH_TEXT_MESSAGE));
    }

    @Test
    public void checkMessageText_smsText_isValid() {
        Assert.assertEquals(SMSMessage.ContentState.MESSAGE_TEXT_VALID, SMSMessage.checkMessageText(VALID_TEXT_MESSAGE));
    }

    @Test
    public void checkMessageText_smsText_isEmpty() {
        Assert.assertEquals(SMSMessage.ContentState.MESSAGE_TEXT_VALID, SMSMessage.checkMessageText(EMPTY_TEXT_MESSAGE));
    }

    @Test
    public void getPeer_smsPeer_isEquals() {
        Assert.assertEquals(VALID_TELEPHONE_NUMBER, smsMessage.getPeer().toString());
    }

    @Test
    public void getData_smsData_isEquals() {
        Assert.assertEquals(VALID_TEXT_MESSAGE, smsMessage.getData());
    }
}
