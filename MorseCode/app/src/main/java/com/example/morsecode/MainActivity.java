package com.example.morsecode;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    boolean hasCameraFlash = false;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText editText = findViewById(R.id.editText);
        Button button = findViewById(R.id.button);
        TextView textView = findViewById(R.id.textView);

        hasCameraFlash = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = editText.getText().toString();
                String morse = textToMorse(text);
                textView.setText(morse);

                if(hasCameraFlash){
                    try {
                        flashLightOn();
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }
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
            Toast.makeText(MainActivity.this, "Flashlight on", Toast.LENGTH_SHORT).show();
        }


    }

    public static String textToMorse(String text) {
        char[] alphabet = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
                's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
        String[] morseCode = {".-", "-...", "-.-.", "-..", ".", "..-.", "--.", "....", "..", ".---", "-.-", ".-..",
                "--", "-.", "---", ".--.", "--.-", ".-.", "...", "-", "..-", "...-", ".--", "-..-", "-.--", "--.."};
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

    public static String morseToText(String morse, char[] alphabet, String[] morseCode) {
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