package com.example.morsecode;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity {

    boolean hasCameraFlash = false;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText editText = findViewById(R.id.editText);
        Button button = findViewById(R.id.button);
        Button button1 = findViewById(R.id.button1);
        TextView textView = findViewById(R.id.textView);
        hasCameraFlash = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = editText.getText().toString();
                String morse = textToMorse(text);
                textView.setText(morse);
                for (int i = 0; i < morse.length(); i++) {
                    char ch = morse.charAt(i);
                    String character = Character.toString(ch);
                    if (ch == '.') {
                        if (hasCameraFlash) {
                            try {
                                flashLightOn();
                                sleep(250);
                                flashLightOff();
                                sleep(250);
                            } catch (CameraAccessException | InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    } else if (ch == '-') {
                        if (hasCameraFlash) {
                            try {
                                flashLightOn();
                                sleep(500);
                                flashLightOff();
                                sleep(250);
                            } catch (CameraAccessException | InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        ;
                    }
                }
            }
        });


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = editText.getText().toString();
                String morse = textToMorse(text);
                textView.setText(morse);
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS}, 1);
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("sms:" + "1 555-521-5556"));
                intent.putExtra("sms_body", morse);
                startActivity(intent);
            }
        });
    }


    private void flashLightOn() throws CameraAccessException {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
            String cameraId = cameraManager.getCameraIdList()[0];
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cameraManager.setTorchMode(cameraId, true);
            }
        }
    }

    private void flashLightOff() throws CameraAccessException {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
            String cameraId = cameraManager.getCameraIdList()[0];
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cameraManager.setTorchMode(cameraId, false);
            }
        }
    }

    public static String textToMorse(String text) {
        char[] alphabet = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
                's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
        String[] morseCode = {".-", "-...", "-.-.", "-..", ".", "..-.", "--.", "....", "..", ".---", "-.-", ".-..",
                "--", "-.", "---", ".--.", "--.-", ".-.", "...", "-", "..-", "...-", ".--", "-..-", "-.--", "--..",
                ".----", "..---","...--","....-",".....","-....","--...","---..","----.","-----"};
        String textToMorse = "";
        text = text.toLowerCase();
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == ' ') {
                textToMorse += "/" + " ";
            } else {
                for (int j = 0; j < alphabet.length; j++) {
                    if (text.charAt(i) == alphabet[j]) {
                        textToMorse += morseCode[j] + " ";
                    }
                }
            }
        }
        return textToMorse.trim();
    }

    public static String morseToText(String morse) {
        char[] alphabet = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
                's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
        String[] morseCode = {".-", "-...", "-.-.", "-..", ".", "..-.", "--.", "....", "..", ".---", "-.-", ".-..",
                "--", "-.", "---", ".--.", "--.-", ".-.", "...", "-", "..-", "...-", ".--", "-..-", "-.--", "--.."};
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


