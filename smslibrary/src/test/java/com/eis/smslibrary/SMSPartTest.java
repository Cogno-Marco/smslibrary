package com.eis.smslibrary;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Giovanni Velludo
 */
public class SMSPartTest {

    private SMSPart part;
    private static final String message = "Test message";
    private static final String intentAction = "SMS_SENT0000000000316463531";
    private static final String lowerIntentAction = "SMS_SENT0000000000316441531";
    private static final String lowerWithLessDigitsIntentAction = "SMS_SENT0000000000006461531";
    private static final String higherIntentAction = "SMS_SENT0000000000716441531";
    private static final String higherWithMoreDigitsIntentAction = "SMS_SENT0000000056716461531";

    @Before
    public void init() {
        part = new SMSPart(message, intentAction);
    }

    @Test
    public void getMessage() {
        assertEquals(message, part.getMessage());
    }

    @Test
    public void compareTo_itself() {
        assertEquals(0, part.compareTo(part));
    }

    @Test
    public void compareTo_lower() {
        SMSPart lowerPart = new SMSPart(message, lowerIntentAction);
        assertTrue(part.compareTo(lowerPart) > 0);
    }

    @Test
    public void compareTo_lowerWithLessDigits() {
        SMSPart lowerWithLessDigitsPart = new SMSPart(message, lowerWithLessDigitsIntentAction);
        assertTrue(part.compareTo(lowerWithLessDigitsPart) > 0);
    }

    @Test
    public void compareTo_higher() {
        SMSPart higherPart = new SMSPart(message, higherIntentAction);
        assertTrue(part.compareTo(higherPart) < 0);
    }

    @Test
    public void compareTo_higherWithMoreDigits() {
        SMSPart higherWithMoreDigitsPart = new SMSPart(message, higherWithMoreDigitsIntentAction);
        assertTrue(part.compareTo(higherWithMoreDigitsPart) < 0);
    }
}