package com.eis.smslibrary;

/**
 * Object associating to each part of a multi-part SMS text message the corresponding Intent's
 * action to be received by SMSDeliveredBroadcastReceiver or SMSSentBroadcastReceiver;
 *
 * @author Giovanni Velludo
 */
class SMSPart implements Comparable<SMSPart> {

    private final String message;
    private final String intentAction;

    /**
     * Creates a new triple, with field `received` set to false by default.
     *
     * @param message      the SMS part for which sent or delivered confirmation is needed.
     * @param intentAction name of the corresponding Intent's action.
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


    @Override
    public int compareTo(SMSPart part) {
        return intentAction.compareTo(part.intentAction);
    }
}
