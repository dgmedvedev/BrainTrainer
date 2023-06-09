package com.demo.braintrainer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    TextView textViewScore;
    TextView textViewTimer;
    TextView textViewTask;
    TextView textViewOption1;
    TextView textViewOption2;
    TextView textViewOption3;
    TextView textViewOption4;
    List<TextView> textViewList;

    int countOfRightAnswers;
    int countOfQuestions;
    int randomA;
    int randomB;
    int record;
    int result;
    int min = 2;
    int max = 30;
    int seconds = 30;
    boolean isRunning = true;

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("countAnswer", countOfRightAnswers);
        outState.putInt("countQuestion", countOfQuestions);
        outState.putInt("seconds", seconds);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewScore = findViewById(R.id.textViewScore);
        textViewTimer = findViewById(R.id.textViewTimer);
        textViewTask = findViewById(R.id.textViewTask);
        textViewOption1 = findViewById(R.id.textViewOption1);
        textViewOption2 = findViewById(R.id.textViewOption2);
        textViewOption3 = findViewById(R.id.textViewOption3);
        textViewOption4 = findViewById(R.id.textViewOption4);
        textViewList = new ArrayList<>();
        textViewList.add(textViewOption1);
        textViewList.add(textViewOption2);
        textViewList.add(textViewOption3);
        textViewList.add(textViewOption4);
        countOfRightAnswers = 0;


        if (savedInstanceState != null) {
            savedInstanceState.getInt("countAnswer");
            savedInstanceState.getInt("countQuestion");
            savedInstanceState.getInt("seconds");
        }

        startTask();

        textViewOption1.setOnClickListener(view -> {
            onClick(textViewOption1.getText().toString());
        });
        textViewOption2.setOnClickListener(view -> {
            onClick(textViewOption2.getText().toString());
        });
        textViewOption3.setOnClickListener(view -> {
            onClick(textViewOption3.getText().toString());
        });
        textViewOption4.setOnClickListener(view -> {
            onClick(textViewOption4.getText().toString());
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        isRunning = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isRunning = true;
        timer();
    }

    private void startTask() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            randomA = (int) (Math.random() * (max - min + 1) + min);
            randomB = (int) (Math.random() * (max - min + 1) + min);
            handler.post(() -> {
                int mark = (int) (Math.random() * 2);
                if (mark == 0) {
                    result = randomA + randomB;
                    String task = String.format("%s + %s", randomA, randomB);
                    textViewTask.setText(task);
                    getContent();
                } else {
                    result = randomA - randomB;
                    String task = String.format("%s - %s", randomA, randomB);
                    textViewTask.setText(task);
                    getContent();
                }
            });
        });
    }

    private void getContent() {
        String score = String.format("%s / %s", countOfRightAnswers, countOfQuestions);
        textViewScore.setText(score);
        fillArray();
    }

    private void fillArray() {
        int randomRight = (int) (Math.random() * textViewList.size());
        for (int i = 0; i < textViewList.size(); i++) {
            if (i == randomRight) {
                String rightAnswer = Integer.toString(result);
                textViewList.get(i).setText(rightAnswer);
            } else {
                int randomWrong = (int) (Math.random() * (max - min + 1) + min);
                int mark = (int) (Math.random() * 2);
                boolean isPositive = (mark == 0);
                if (result < 0 && !isPositive) {
                    randomWrong = randomWrong * (-1);
                }
                while (randomWrong == result) {
                    randomWrong = (int) (Math.random() * (max - min + 1) + min);
                    if (result < 0 && !isPositive) {
                        randomWrong = randomWrong * (-1);
                    }
                }
                String wrongAnswer = Integer.toString(randomWrong);
                textViewList.get(i).setText(wrongAnswer);
            }
        }
    }

    private void onClick(String text) {
        int temp = Integer.parseInt(text);
        if (temp == result) {
            countOfRightAnswers++;
        }
        countOfQuestions++;
        startTask();
    }

    private void timer() {
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                int min = seconds / 60;
                int sec = seconds % 60;
                if (min == 0 && sec < 10) {
                    textViewTimer.setTextColor(getResources().getColor(R.color.red));
                }
                String time = String.format(Locale.getDefault(), "%02d:%02d", min, sec);
                textViewTimer.setText(time);
                if (seconds > 0) {
                    if (isRunning) {
                        seconds--;
                        handler.postDelayed(this, 1000);
                    }
                } else {
                    timerFinish();
                }
            }
        });
    }

    private void timerFinish() {
        if (seconds == 0) {
            Intent intent = new Intent(this, ResultActivity.class);
            textViewTimer.setText(R.string.time_up);

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            record = preferences.getInt("record", 0);

            if (countOfQuestions > 20 && ((double) countOfRightAnswers / countOfQuestions < 0.5)) {
                intent.putExtra("record", record);
                Toast.makeText(MainActivity.this, "Не торописька", Toast.LENGTH_LONG).show();
            } else {
                if (record < countOfRightAnswers) {
                    record = countOfRightAnswers;
                }
                intent.putExtra("countAnswer", countOfRightAnswers);
                intent.putExtra("record", record);

                Toast.makeText(MainActivity.this, "Время вышло", Toast.LENGTH_SHORT).show();
            }
            startActivity(intent);
            finish();
        }
    }
}