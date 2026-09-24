package com.jarvis.ai;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText inputText;
    private Button sendButton;
    private TextView statusText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        inputText = findViewById(R.id.inputText);
        sendButton = findViewById(R.id.sendButton);
        statusText = findViewById(R.id.statusText);

        sendButton.setOnClickListener(v -> {
            String message = inputText.getText().toString().trim();

            if (message.isEmpty()) {
                statusText.setText("Please type something first.");
            } else {
                statusText.setText("JARVIS received: " + message);
            }
        });
    }
}
