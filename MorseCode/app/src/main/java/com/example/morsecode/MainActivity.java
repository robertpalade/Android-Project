package com.example.morsecode;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static java.lang.Thread.sleep;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MainActivity extends AppCompatActivity {

    boolean hasCameraFlash = false;
    static EditText editText;
    static TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.editText);
        Button button = findViewById(R.id.button);
        Button button1 = findViewById(R.id.button1);
        textView = findViewById(R.id.textView);
        hasCameraFlash = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = editText.getText().toString();
                String morse = textToMorse(text);
//                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, 2);

                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        for (int i = 0; i < text.length(); i++) {
                            try {
                                Thread.sleep(1000);
                                int finalI = i;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        char ch = text.charAt(finalI);
                                        String character = Character.toString(ch);
                                        textView.setText(textToMorse(character));

                                        String morseCode = textToMorse(character);
                                        for (int j = 0; j < morseCode.length(); j++) {
                                            char morseCharacter = morseCode.charAt(j);
                                            if (morseCharacter == '.') {
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
                                            } else if (morseCharacter == '-') {
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
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                };
                thread.start();
            }
        });


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = editText.getText().toString();
                String morse = textToMorse(text);
                textView.setText(morse);
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS}, 1);
//                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("sms:" + "1 555-521-5556"));
//                intent.putExtra("sms_body", morse);
//                startActivity(intent);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        String text = editText.getText().toString();
//        String morse = textToMorse(text);
//        textView.setText(morse);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    String text = editText.getText().toString();
                    String morse = textToMorse(text);
                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                    intent.setData(Uri.parse("sms:" + "1 555-521-5556"));
                    intent.putExtra("sms_body", morse);
                    textView.setText(morse);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
        }

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
        String textToMorse = "";
        text = text.toLowerCase();
        String[] morseCode = {".-", "-...", "-.-.", "-..", ".", "..-.", "--.", "....", "..", ".---", "-.-", ".-..",
                "--", "-.", "---", ".--.", "--.-", ".-.", "...", "-", "..-", "...-", ".--", "-..-", "-.--", "--.."};
        for (int i = 0; i < text.length(); i++) {
            textToMorse += morseCode[text.charAt(i) - 'a'] + " ";
        }
        return textToMorse;
    }

    
}


