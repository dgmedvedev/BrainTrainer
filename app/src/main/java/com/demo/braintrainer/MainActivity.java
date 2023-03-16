package com.demo.braintrainer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
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

    int countAnswer;
    int countQuestion;
    int randomA;
    int randomB;
    int result;
    int number = 50;

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

        // создание таймера с помощью абстрактного класса
        // 2 параметра: 1-сколько милисек будет отсчитывать таймер, 2-как часто будет тикать таймер
        CountDownTimer timer = new CountDownTimer(6000, 1000) {
            // onTick() принимает кол-во милисек оставшихся до завершения работы таймера
            @Override
            public void onTick(long millisUtilFinished) {
                int sec = (int) (millisUtilFinished / 1000);
                sec++;
                String seconds = Integer.toString(sec);
                textViewTimer.setText(seconds);
            }

            @Override
            public void onFinish() {
                Toast.makeText(MainActivity.this, "Таймер завершен", Toast.LENGTH_SHORT).show();
                textViewTimer.setText("0");
            }
        };
        timer.start();

        startTask();

        textViewOption1.setOnClickListener(view -> {
            int text = Integer.parseInt(textViewOption1.getText().toString());
            onClick(text);
        });
        textViewOption2.setOnClickListener(view -> {
            int text = Integer.parseInt(textViewOption2.getText().toString());
            onClick(text);
        });
        textViewOption3.setOnClickListener(view -> {
            int text = Integer.parseInt(textViewOption3.getText().toString());
            onClick(text);
        });
        textViewOption4.setOnClickListener(view -> {
            int text = Integer.parseInt(textViewOption4.getText().toString());
            onClick(text);
        });
    }

    private void startTask() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            randomA = (int) (Math.random() * number);
            randomB = (int) (Math.random() * number);
            handler.post(() -> {
                int random = (int) (Math.random() * 2);
                if (random == 0) {
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
        String score = String.format("%s / %s", countAnswer, countQuestion);
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
                int randomWrong = (int) (Math.random() * number);
                int random = (int) (Math.random() * 2);
                boolean isPositive = (random == 0);
                if (result < 0 && !isPositive) {
                    randomWrong = randomWrong * (-1);
                }
                while (randomWrong == result) {
                    randomWrong = (int) (Math.random() * number);
                    if (result < 0 && !isPositive) {
                        randomWrong = randomWrong * (-1);
                    }
                }
                String wrongAnswer = Integer.toString(randomWrong);
                textViewList.get(i).setText(wrongAnswer);
            }
        }
    }

    private void onClick(int text) {
        if (text == result) {
            countAnswer++;
        }
        countQuestion++;
        startTask();
    }
}