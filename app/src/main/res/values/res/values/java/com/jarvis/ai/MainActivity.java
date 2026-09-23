package com.jarvis.ai;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.Gravity;
import android.widget.*;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends Activity {

    private static final int SPEECH_REQUEST = 100;
    private static final int PERMISSION_REQUEST = 200;

    private TextView output;
    private TextToSpeech jarvisVoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createInterface();
        setupJarvisVoice();
        requestPhonePermissions();
    }

    private void createInterface() {

        LinearLayout main = new LinearLayout(this);
        main.setOrientation(LinearLayout.VERTICAL);
        main.setPadding(30, 60, 30, 30);
        main.setBackgroundColor(0xFF05070A);

        TextView title = new TextView(this);
        title.setText("J.A.R.V.I.S.");
        title.setTextColor(0xFF00BFFF);
        title.setTextSize(32);
        title.setGravity(Gravity.CENTER);

        main.addView(
                title,
                new LinearLayout.LayoutParams(-1, 100)
        );

        output = new TextView(this);
        output.setText(
                "JARVIS ONLINE\n\n" +
                "All primary systems initialized.\n\n" +
                "Tap the microphone and speak."
        );
        output.setTextColor(0xFFFFFFFF);
        output.setTextSize(18);
        output.setPadding(10, 30, 10, 30);

        main.addView(
                output,
                new LinearLayout.LayoutParams(
                        -1,
                        0,
                        1
                )
        );

        Button microphone = new Button(this);
        microphone.setText("🎙  SPEAK TO JARVIS");
        microphone.setTextSize(18);
        microphone.setOnClickListener(v -> listen());

        main.addView(
                microphone,
                new LinearLayout.LayoutParams(-1, 90)
        );

        Button permissions = new Button(this);
        permissions.setText("PHONE PERMISSIONS");
        permissions.setOnClickListener(
                v -> requestPhonePermissions()
        );

        main.addView(
                permissions,
                new LinearLayout.LayoutParams(-1, 70)
        );

        setContentView(main);
    }

    private void setupJarvisVoice() {

        jarvisVoice = new TextToSpeech(
                this,
                status -> {

                    if (status == TextToSpeech.SUCCESS) {

                        jarvisVoice.setLanguage(Locale.US);

                        // Calm, slower assistant-style delivery.
                        jarvisVoice.setPitch(0.72f);
                        jarvisVoice.setSpeechRate(0.88f);
                    }
                }
        );
    }

    private void speak(String message) {

        output.append("\n\nJARVIS: " + message);

        if (jarvisVoice != null) {

            jarvisVoice.speak(
                    message,
                    TextToSpeech.QUEUE_FLUSH,
                    null,
                    "JARVIS_RESPONSE"
            );
        }
    }

    private void listen() {

        Intent intent =
                new Intent(
                        RecognizerIntent.ACTION_RECOGNIZE_SPEECH
                );

        intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        );

        intent.putExtra(
                RecognizerIntent.EXTRA_PROMPT,
                "Speak to JARVIS"
        );

        startActivityForResult(
                intent,
                SPEECH_REQUEST
        );
    }

    @Override
    protected void onActivityResult(
            int requestCode,
            int resultCode,
            Intent data) {

        super.onActivityResult(
                requestCode,
                resultCode,
                data
        );

        if (
                requestCode == SPEECH_REQUEST &&
                resultCode == RESULT_OK &&
                data != null
        ) {

            ArrayList<String> results =
                    data.getStringArrayListExtra(
                            RecognizerIntent.EXTRA_RESULTS
                    );

            if (
                    results != null &&
                    !results.isEmpty()
            ) {

                processCommand(results.get(0));
            }
        }
    }

    private void processCommand(String command) {

        String c = command.toLowerCase(Locale.US);

        output.setText("You: " + command);

        if (c.contains("hello jarvis")) {

            speak(
                    "Good day. " +
                    "All primary systems are operational."
            );

            return;
        }

        if (c.contains("open settings")) {

            startActivity(
                    new Intent(Settings.ACTION_SETTINGS)
            );

            speak("Opening settings.");

            return;
        }

        if (
                c.contains("open wifi") ||
                c.contains("open wi-fi")
        ) {

            startActivity(
                    new Intent(Settings.ACTION_WIFI_SETTINGS)
            );

            speak("Opening Wi-Fi settings.");

            return;
        }

        if (c.contains("open bluetooth")) {

            startActivity(
                    new Intent(
                            Settings.ACTION_BLUETOOTH_SETTINGS
                    )
            );

            speak("Opening Bluetooth settings.");

            return;
        }

        if (c.contains("open camera")) {

            Intent camera =
                    new Intent(
                            "android.media.action.IMAGE_CAPTURE"
                    );

            startActivity(camera);

            speak("Opening the camera.");

            return;
        }

        if (c.startsWith("search for ")) {

            String search =
                    command.substring(11).trim();

            Intent browser =
                    new Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(
                                    "https://www.google.com/search?q="
                                            + Uri.encode(search)
                            )
                    );

            startActivity(browser);

            speak("Searching for " + search);

            return;
        }

        if (c.contains("what time")) {

            String time =
                    new SimpleDateFormat(
                            "h:mm a",
                            Locale.US
                    ).format(new Date());

            speak("The time is " + time);

            return;
        }

        speak(
                "I heard you say: " +
                command +
                "."
        );
    }

    private void requestPhonePermissions() {

        ArrayList<String> permissions =
                new ArrayList<>();

        String[] requested = {

                Manifest.permission.RECORD_AUDIO,

                Manifest.permission.READ_CONTACTS,

                Manifest.permission.CAMERA,

                Manifest.permission.ACCESS_FINE_LOCATION,

                Manifest.permission.ACCESS_COARSE_LOCATION,

                Manifest.permission.CALL_PHONE
        };

        for (String permission : requested) {

            if (
                    ContextCompat.checkSelfPermission(
                            this,
                            permission
                    )
                    != PackageManager.PERMISSION_GRANTED
            ) {

                permissions.add(permission);
            }
        }

        if (!permissions.isEmpty()) {

            ActivityCompat.requestPermissions(
                    this,
                    permissions.toArray(
                            new String[0]
                    ),
                    PERMISSION_REQUEST
            );
        }
    }

    @Override
    protected void onDestroy() {

        if (jarvisVoice != null) {

            jarvisVoice.stop();
            jarvisVoice.shutdown();
        }

        super.onDestroy();
    }
  }
