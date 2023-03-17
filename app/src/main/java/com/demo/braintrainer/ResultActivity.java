package com.demo.braintrainer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Button buttonStartOver = findViewById(R.id.buttonStartOver);
        TextView textViewResult = findViewById(R.id.textViewResult);

        Intent intent = getIntent();
        int countAnswer = intent.getIntExtra("countAnswer", 0);
        int record = intent.getIntExtra("record", 0);

        String result = String.format("Ваш результат: %s\nМаксимальный результат: %s",
                countAnswer, record);
        textViewResult.setText(result);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.edit().putInt("record", record).apply();

        buttonStartOver.setOnClickListener(view -> {
            Intent intentStartOver = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intentStartOver);
        });
    }
}