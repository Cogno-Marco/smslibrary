package com.eis.smslibrary.message;

import androidx.annotation.NonNull;

import com.eis.smslibrary.exceptions.InvalidSMSMessageException;
import com.eis.smslibrary.header.SMSReceivedHeader;

/**
 * SMSReceivedHeader is {@link AbstractSMSMessage} extension taking as <code>Header</code> {@link SMSReceivedHeader}
 *
 * @author Mattia Fanan
 * @since 08/12/2019 (gg/mm/aaaa)
 */
public final class SMSMessageReceived extends AbstractSMSMessage<SMSReceivedHeader> {

    /**
     * Constructor for SMSMessageReceived
     *
     * @param messageHeader the header of the this message
     * @param messageText   the content of this message, can be empty but not null
     * @throws InvalidSMSMessageException if {@link AbstractSMSMessage#checkMessageText} is different from MESSAGE_TEXT_VALID
     */
    public SMSMessageReceived(@NonNull final SMSReceivedHeader messageHeader, @NonNull final String messageText) throws InvalidSMSMessageException {
        super(messageHeader, messageText);
    }

}



