package com.example.morsecode;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

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
        String[] morseCode = {".-", "-...", "-.-.", "-..", ".", "..-.", "--.", "....", "..", ".---", "-.-", ".-..",
                "--", "-.", "---", ".--.", "--.-", ".-.", "...", "-", "..-", "...-", ".--", "-..-", "-.--", "--.." };
        String text = "";
        String[] morseArray = morse.split(" ");
        Map<String, Character> morseToText = new HashMap<>();

        for (int i = 0; i < 26; i++) {
            morseToText.put(morseCode[i], (char) ('a' + i));
        }

        for (int i = 0; i < morseArray.length; i++) {
            if(morseArray[i].compareTo("/") == 0){
                text += " ";
            }
            text += morseToText.get(morseArray[i]) + " ";
        }
        return text;
    }
}
