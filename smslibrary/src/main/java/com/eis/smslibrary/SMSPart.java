package com.eis.smslibrary;

/**
 * Data structure associating to each part of a multi-part SMS text message:
 * - the corresponding Intent's action to be received by SMSDeliveredBroadcastReceiver or
 *   SMSSentBroadcastReceiver;
 * - whether the Intent was received;
 * - the result state of the message part, given by its corresponding Intent.
 * @author Giovanni Velludo
 */
class SMSPart {

    private String message;
    private String intentAction;
    private boolean received = false;
    private SMSMessage.SentState sentState = SMSMessage.SentState.ERROR_GENERIC_FAILURE;

    /**
     * Creates a new triple, with field `received` set to false by default.
     * @param message       the SMS part for which sent or delivered confirmation is needed.
     * @param intentAction  name of the corresponding Intent's action.
     */
    SMSPart(String message, String intentAction) {
        this.message = message;
        this.intentAction = intentAction;
    }

    /**
     * @return a part of a multi-part SMS text message.
     */
    String getMessage() {
        return message;
    }

    /**
     * @return the name of this part's corresponding Intent's action.
     */
    String getIntentAction() {
        return intentAction;
    }

    /**
     * @return whether the Intent associated to this part was received.
     */
    boolean wasReceived() {
        return received;
    }

    /**
     * Method to be called when the Intent associated to this part is received.
     * @param sentState result state of the message sending operation.
     */
    void setReceived(SMSMessage.SentState sentState) {
        received = true;
        this.sentState = sentState;
    }

    /**
     * @return result code of this part.
     */
    SMSMessage.SentState getState() {
        return sentState;
    }
}
