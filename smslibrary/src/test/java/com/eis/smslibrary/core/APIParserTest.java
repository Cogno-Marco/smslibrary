package com.eis.smslibrary.core;

import com.eis.smslibrary.SMSPeerTest;
import com.eis.smslibrary.message.AbstractSMSMessageTest;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Mattia Fanan
 */
public class APIParserTest {

    private static final int STRING_LENGTH = 16;
    private static final String LESS_THAN_16 = "qwerty";
    private static final String MORE_THAN_16 = "abcdefghijklmnopqrst";
    private static final String EQUAL_16 = "abcdefghijklmnop";

    private static final String EXPECTED_DATA = APIParser.addPadding(SMSPeerTest.VALID_TELEPHONE_NUMBER) + AbstractSMSMessageTest.VALID_TEXT_MESSAGE;
    private static final APIMessage EXPECTED_API_MESSAGE = new APIMessage(SMSPeerTest.VALID_TELEPHONE_NUMBER, AbstractSMSMessageTest.VALID_TEXT_MESSAGE);

    @Test
    public void addPaddingLess() {
        String paddingAdded = APIParser.addPadding(LESS_THAN_16);

        if (paddingAdded == null)
            Assert.fail("should't return null");

        if (paddingAdded.length() != STRING_LENGTH)
            Assert.fail("returned string length should be 16");

        if (paddingAdded.indexOf(APIParser.PADDING) != LESS_THAN_16.length())
            Assert.fail("padding should starts after string end");
    }

    @Test
    public void addPaddingMore() {
        String paddingAdded = APIParser.addPadding(MORE_THAN_16);

        if (paddingAdded != null)
            Assert.fail("should return null");
    }

    @Test
    public void addPaddingEqual() {
        String paddingAdded = APIParser.addPadding(EQUAL_16);

        if (paddingAdded == null)
            Assert.fail("should't return null");

        if (!paddingAdded.equals(EQUAL_16))
            Assert.fail("returned string should be the same as the passed one");
    }

    @Test(expected = NullPointerException.class)
    public void addPaddingNull() {
        APIParser.addPadding(null);
        Assert.fail("should throw NullPointerException");
    }

    @Test
    public void parseToUpperLayerData() {
        Assert.assertEquals(EXPECTED_DATA, APIParser.parseToUpperLayerData(EXPECTED_API_MESSAGE));
    }

    @Test(expected = NullPointerException.class)
    public void parseToUpperLayerDataNull() {
        APIParser.parseToUpperLayerData(null);
        Assert.fail("should throw NullPointerException");
    }

    @Test
    public void parseToAPIMessage() {
        APIMessage parsedAPIMessage = APIParser.parseToAPIMessage(EXPECTED_DATA);

        if (!parsedAPIMessage.getTextMessage().equals(EXPECTED_API_MESSAGE.getTextMessage()))
            Assert.fail("data should be the same");

        if (!parsedAPIMessage.getPhoneNumber().equals(EXPECTED_API_MESSAGE.getPhoneNumber()))
            Assert.fail("phone number should be the same");
    }

    @Test(expected = NullPointerException.class)
    public void parseToAPIMessageNull() {
        APIParser.parseToAPIMessage(null);
        Assert.fail("should throw NullPointerException");
    }
}
