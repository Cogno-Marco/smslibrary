package com.eis.smslibrary;

import androidx.annotation.NonNull;

import com.eis.communication.Message;
import com.eis.smslibrary.exceptions.InvalidSMSMessageException;

import java.util.Objects;

/**
 * Representation of a single sms message
 * This class does NOT parse SMSMessages into sms-ready strings and back!
 *
 * @author Luca Crema, Marco Mariotto, Alberto Ursino, Giovanni Velludo
 */
public class SMSMessage implements Message<String, SMSPeer> {

    /**
     * Maximum number of concatenated messages in which an SMS can be split is 255, each containing
     * no more than 153 7-bit GSM characters: 153 * 255 = 39015.
     * See
     * https://portal.3gpp.org/desktopmodules/Specifications/SpecificationDetails.aspx?specificationId=747
     * https://en.wikipedia.org/wiki/Concatenated_SMS
     */
    static final int MAX_MSG_TEXT_LEN = 39015;
    static final int MAX_UCS2_MSG_TEXT_LEN = (MAX_MSG_TEXT_LEN * 7) / 16;
    // these regex can be used both on single characters and entire strings
    private static final String GSM_CHARACTERS_STRING_REGEX = "^[@£$¥èéùìòÇ\\nØø\\rÅåΔ_ΦΓΛΩΠΨΣΘΞÆæßÉ !\"#¤%&‘()*+,\\-./0-9:;<=>?¡A-ZÄÖÑÜ§¿a-zäöñüà]*$";
    private static final String GSM_CHARACTERS_EXTENSION_STRING_REGEX = "^[@£$¥èéùìòÇ\\nØø\\rÅåΔ_ΦΓΛΩΠΨΣΘΞÆæßÉ !\"#¤%&‘()*+,\\-./0-9:;<=>?¡A-ZÄÖÑÜ§¿a-zäöñüà\\f^{}\\\\\\[~\\]|€]*$";
    private static final String GSM_CHARACTERS_EXTENSION_REGEX = "[\\f^{}\\\\\\[~\\]|€]";
    private String text;
    private SMSPeer peer;

    /**
     * Constructor for a sms text message.
     *
     * @param peer        a valid peer
     * @param messageText the message content, can be empty but not null
     * @throws InvalidSMSMessageException if checkMessageText is different from MESSAGE_TEXT_VALID
     */
    public SMSMessage(@NonNull SMSPeer peer, @NonNull String messageText) throws InvalidSMSMessageException {
        //Checks on the message text
        ContentState contentState = checkMessageText(messageText);
        if (contentState != ContentState.MESSAGE_TEXT_VALID)
            throw new InvalidSMSMessageException("Message text length exceeds maximum allowed", contentState);
        this.text = messageText;
        this.peer = peer;
    }

    /**
     * Private constructor needed only in {@link SMSMessage#checkMessageText(String)}, to get a
     * String with the parsed message text.
     *
     * @param messageText the message content, can be empty but not null
     */
    private SMSMessage(@NonNull String messageText) {
        this.text = messageText;
    }

    /**
     * Checks if the message content is valid.
     *
     * @param messageText to be checked.
     * @return The state of the message after validity tests.
     */
    static ContentState checkMessageText(@NonNull String messageText) {
        /* Since applications using this library will probably try to avoid wasting characters,
         * they'll mostly stick to using the regular GSM character set, so we perform pattern
         * matching on the whole message first, to avoid multiple checks on single characters
         * when not necessary.
         */
        messageText = SMSMessageHandler.getInstance(null).parseMessage(new SMSMessage(messageText));
        if (messageText.matches(GSM_CHARACTERS_STRING_REGEX)) {
            // messageText contains only GSM characters
            if (messageText.length() <= MAX_MSG_TEXT_LEN) {
                return ContentState.MESSAGE_TEXT_VALID;
            }
        } else if (!messageText.matches(GSM_CHARACTERS_EXTENSION_STRING_REGEX)) {
            // messageText contains characters not present in the GSM charset nor its extension
            // table, it'll be encoded in UCS-2
            if (messageText.length() <= MAX_UCS2_MSG_TEXT_LEN) {
                return ContentState.MESSAGE_TEXT_VALID;
            }
        } else {
            // messageText contains only chars from the GSM charset and its extension table
            int charNum = 0;
            for (int i = 0; i < messageText.length(); i++) {
                String currentChar = messageText.substring(i, i + 1);
                if (currentChar.matches(GSM_CHARACTERS_EXTENSION_REGEX)) {
                    // characters from the GSM extension table are translated to two characters when
                    // the message is sent: an escape character + the character from the extension
                    // table
                    charNum += 2;
                } else {
                    charNum++;
                }
            }
            if (charNum <= MAX_MSG_TEXT_LEN) {
                return ContentState.MESSAGE_TEXT_VALID;
            }
        }
        return ContentState.MESSAGE_TEXT_TOO_LONG;
    }

    /**
     * Retrieves the message text.
     *
     * @return The text contained in this {@link SMSMessage}.
     */
    @Override
    public String getData() {
        return text;
    }

    /**
     * Retrieves the sender or the destination for the message
     *
     * @return Peer associated with this message
     */
    @Override
    public SMSPeer getPeer() {
        return peer;
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * @param o the reference object with which to compare.
     * @return {@code true} if this object is the same as the o argument; {@code false} otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SMSMessage that = (SMSMessage) o;
        return text.equals(that.text) &&
                peer.equals(that.peer);
    }

    /**
     * Returns a hash code value for the object. This method is supported for the benefit of hash
     * tables such as those provided by {@link java.util.HashMap}.
     *
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(text, peer);
    }

    /**
     * Possible states of a message after a check
     * There could be more in future so it has been used an enum
     * <p>
     * These are used in {@link #checkMessageText}
     */
    public enum ContentState {
        MESSAGE_TEXT_VALID,
        MESSAGE_TEXT_TOO_LONG
    }

    /**
     * Possible states of a message after it has been sent
     * These are given by Android library as an int, they have been put in an enum
     * so that one can see all the possible values without having to
     * look at the official Android documentation
     * <p>
     * These are used in {@link com.eis.smslibrary.listeners.SMSSentListener}
     */
    public enum SentState {
        MESSAGE_SENT,
        ERROR_GENERIC_FAILURE,
        ERROR_RADIO_OFF,
        ERROR_NULL_PDU,
        ERROR_NO_SERVICE,
        ERROR_LIMIT_EXCEEDED
    }

    /**
     * Possible states of a message after it has been delivered,
     * These are given by Android library as an int, they have been put in an enum so
     * that one can see al the possible values without having to look
     * at the official Android documentation
     * <p>
     * These are used in {@link com.eis.smslibrary.listeners.SMSDeliveredListener}
     */
    public enum DeliveredState {
        MESSAGE_DELIVERED,
        DELIVERY_ERROR,
        ERROR_GENERIC_FAILURE
    }
}



