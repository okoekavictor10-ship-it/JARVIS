package com.jarvis.ai;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends Activity {

    private static final int VOICE_REQUEST = 100;
    private static final int AUDIO_PERMISSION = 101;

    private LinearLayout chatLayout;
    private EditText input;
    private TextView status;
    private TextToSpeech speech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        buildInterface();

        speech = new TextToSpeech(this, result -> {
            if (result == TextToSpeech.SUCCESS) {
                speech.setLanguage(Locale.US);
            }
        });
    }

    private void buildInterface() {

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(Color.rgb(5, 7, 10));

        // Header
        LinearLayout header = new LinearLayout(this);
        header.setOrientation(LinearLayout.VERTICAL);
        header.setGravity(Gravity.CENTER);
        header.setPadding(20, 35, 20, 20);

        TextView title = new TextView(this);
        title.setText("J A R V I S");
        title.setTextColor(Color.CYAN);
        title.setTextSize(27);
        title.setTypeface(Typeface.DEFAULT_BOLD);
        title.setGravity(Gravity.CENTER);

        status = new TextView(this);
        status.setText("● ONLINE");
        status.setTextColor(Color.GREEN);
        status.setTextSize(13);
        status.setGravity(Gravity.CENTER);
        status.setPadding(0, 8, 0, 0);

        header.addView(title);
        header.addView(status);

        root.addView(header);

        // Chat area
        ScrollView scrollView = new ScrollView(this);

        chatLayout = new LinearLayout(this);
        chatLayout.setOrientation(LinearLayout.VERTICAL);
        chatLayout.setPadding(20, 10, 20, 20);

        addMessage(
                "JARVIS",
                "Hello. I am JARVIS. How can I assist you?",
                true
        );

        scrollView.addView(chatLayout);

        LinearLayout.LayoutParams chatParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        0
                );

        chatParams.weight = 1;

        root.addView(scrollView, chatParams);

