package com.eis.smslibrary;

import com.eis.communication.MessageParseStrategy;

/**
 * Specific interface for SMS Message parsing.
 * @author Riccardo De Zen
 */
public interface SMSParseStrategy extends MessageParseStrategy<String, SMSPeer, SMSMessage> {
}
