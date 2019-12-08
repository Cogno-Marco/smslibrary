package com.eis.smslibrary.core;


import androidx.annotation.NonNull;

/**
 * APIParser is raw parser from strings generated from {@link com.eis.smslibrary.SMSMessageParser} to {@link APIMessage} and back
 *
 * @author Mattia Fanan
 */
    public final class APIParser {

        public static final int PHONE_NUMBER_LENGTH = 16;
        public static final char PADDING = (char)0xfeff0126;//capital latin H

    /**
     * Parses data to APIMessage
     *
     * @param data the data to parse in the format --16 <code>char</code> long address + message text--
     * @return APIMessage if a valid one can be parsed from <code>data</code>, <code>null</code> otherwise
     */
    public static APIMessage parseToAPIMessage(@NonNull final String data) {

        try{

            String paddedPhoneNumber = data.substring(0, PHONE_NUMBER_LENGTH);

            return new APIMessage(
                    paddedPhoneNumber.substring(0, paddedPhoneNumber.indexOf(PADDING)),//delete padding
                    data.substring(PHONE_NUMBER_LENGTH)
            );
        }
        catch (IndexOutOfBoundsException e){return null;}

    }

    /**
     * Parses APIMessage to data
     *
     * @param apiMessage the APIMessage to parse
     * @return <code>String</code> data if <code>apiMessage</code> correctly parsed, <code>null</code> otherwise
     */
    public static String parseToUpperLayerData(@NonNull final APIMessage apiMessage){

        String phoneNumber = apiMessage.getPhoneNumber();

        //add padding to reach PHONE_NUMBER_LENGTH
        phoneNumber = addPadding(phoneNumber);

        if(phoneNumber != null)
            return phoneNumber + apiMessage.getTextMessage();

        return null;
    }

    /**
     * Adds padding in the passed <code>String</code> to reach the length of 16 <code>char</code>
     *
     * @param toAddPadding the <code>String</code> to add padding
     * @return <code>String</code> of length 16 or <code>null</code> if <code>toAddPadding</code> is already longer
     */
    public static String addPadding(@NonNull String toAddPadding){

        if(toAddPadding.length() > PHONE_NUMBER_LENGTH)
            return null;

        //add padding
        StringBuilder stringBuilder = new StringBuilder(toAddPadding);
        while(stringBuilder.length() < PHONE_NUMBER_LENGTH)
            stringBuilder.append(PADDING);

        return stringBuilder.toString();
    }

}
