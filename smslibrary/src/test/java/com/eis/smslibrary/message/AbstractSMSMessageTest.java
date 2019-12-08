package com.eis.smslibrary.message;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Alberto Ursino
 * @author Luca Crema
 * @author Mattia Fanan
 */
public class AbstractSMSMessageTest {

    private static final int MAX_MSG_TEXT_LEN = AbstractSMSMessage.MAX_MESSAGE_TEXT_LENGTH;
    private static final String MAIN_MESSAGE = "Test message";
    public static final String VALID_TEXT_MESSAGE = MAIN_MESSAGE;
    public static final String TOO_LONG_TEXT_MESSAGE = new String(new char[MAX_MSG_TEXT_LEN * 2]).replace('\0', ' ');
    public static final String MAX_LENGTH_TEXT_MESSAGE = new String(new char[MAX_MSG_TEXT_LEN]).replace('\0', ' ');
    public static final String MAX_LENGTH_TEXT_MESSAGE_P1 = new String(new char[MAX_MSG_TEXT_LEN + 1]).replace('\0', ' '); //P1 = Plus 1
    public static final String EMPTY_TEXT_MESSAGE = "";

    @Test
    public void checkMessageText_smsText_isTooLong() {
        Assert.assertEquals(AbstractSMSMessage.ContentState.MESSAGE_TEXT_TOO_LONG, AbstractSMSMessage.checkMessageText(TOO_LONG_TEXT_MESSAGE));
    }

    @Test
    public void checkMessageText_smsText_isTooLongByOne() {
        Assert.assertEquals(AbstractSMSMessage.ContentState.MESSAGE_TEXT_TOO_LONG, AbstractSMSMessage.checkMessageText(MAX_LENGTH_TEXT_MESSAGE_P1));
    }

    @Test
    public void checkMessageText_smsText_isMaxLength() {
        Assert.assertEquals(AbstractSMSMessage.ContentState.MESSAGE_TEXT_VALID, AbstractSMSMessage.checkMessageText(MAX_LENGTH_TEXT_MESSAGE));
    }

    @Test
    public void checkMessageText_smsText_isValid() {
        Assert.assertEquals(AbstractSMSMessage.ContentState.MESSAGE_TEXT_VALID, AbstractSMSMessage.checkMessageText(VALID_TEXT_MESSAGE));
    }

    @Test
    public void checkMessageText_smsText_isEmpty() {
        Assert.assertEquals(AbstractSMSMessage.ContentState.MESSAGE_TEXT_VALID, AbstractSMSMessage.checkMessageText(EMPTY_TEXT_MESSAGE));
    }
}
