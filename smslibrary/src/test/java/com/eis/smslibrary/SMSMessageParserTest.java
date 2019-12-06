package com.eis.smslibrary;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import com.eis.communication.MessageParseStrategy;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

/**
 * @author Luca Crema, Riccardo De Zen
 */
@RunWith(RobolectricTestRunner.class)
public class SMSMessageParserTest {

    private Context context;

    private SMSMessageParser defaultInstance;
    private static final SMSParseStrategy DEFAULT_SMS_MESSAGE_PARSE_STRATEGY = new DefaultSMSParseStrategy();
    private static final SMSPeer DEFAULT_PEER = new SMSPeer("+393465433432");
    private static final String DEFAULT_MESSAGE_CONTENT = "Test content";
    private static final SMSMessage DEFAULT_MESSAGE = new SMSMessage(DEFAULT_PEER,DEFAULT_MESSAGE_CONTENT);

    @Before
    public void init(){
        Context context = InstrumentationRegistry.getInstrumentation().getContext();
        defaultInstance = SMSMessageParser.getInstance(context);
        defaultInstance.setMessageParseStrategy(context, DEFAULT_SMS_MESSAGE_PARSE_STRATEGY);
    }

    @Test
    public void getInstance_isNotNull() {
        Assert.assertNotNull(SMSMessageParser.getInstance(context));
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

    @Test
    public void setStrategy_canSet(){
        defaultInstance.setMessageParseStrategy(context, new TestSMSParseStrategy());
        Assert.assertNotNull(defaultInstance.getMessageParseStrategy(context));
    }

    /**
     * A test to check whether, in tests the SharedPreferences won't persist (expected behaviour)
     */
    @Test
    public void setStrategy_wontPersistInOtherTests(){
        Assert.assertNull(defaultInstance.getMessageParseStrategy(context));
    }

    @Test
    public void parseMessage_anotherStrategy(){
        defaultInstance.setMessageParseStrategy(context, new TestSMSParseStrategy());
        Assert.assertEquals(DEFAULT_MESSAGE.getData(),defaultInstance.parseMessage(DEFAULT_PEER.getAddress(), defaultInstance.parseData(DEFAULT_MESSAGE)).getData());
    }
}