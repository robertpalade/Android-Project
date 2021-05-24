package com.example.morsecode;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class ReceiveMessage extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getExtras() != null){
            Object[] smsextras = (Object[]) intent.getExtras().get("pdus");
            for (int i = 0; i < smsextras.length; i++) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) smsextras[i]);
                String text = smsMessage.getMessageBody().toString();
//                String number = smsMessage.getOriginatingAddress();
                Toast.makeText(context, morseToText(text), Toast.LENGTH_SHORT).show();
            }
        }
    }


    public static String morseToText(String morse) {
        char[] alphabet = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
                's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
        String[] morseCode = {".-", "-...", "-.-.", "-..", ".", "..-.", "--.", "....", "..", ".---", "-.-", ".-..",
                "--", "-.", "---", ".--.", "--.-", ".-.", "...", "-", "..-", "...-", ".--", "-..-", "-.--", "--..",
                ".----", "..---","...--","....-",".....","-....","--...","---..","----.","-----"};
        String morseToText = "";
        String[] array = morse.split(" ");
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < morseCode.length; j++) {
                if (array[i].compareTo(morseCode[j]) == 0) {
                    morseToText += alphabet[j];
                } else {
                    if (array[i].compareTo("/") == 0) {
                        morseToText += " ";
                        break;
                    }
                }
            }
        }
        return morseToText;
    }
}
