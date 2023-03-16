package com.demo.braintrainer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextView textViewScore;
    TextView textViewTimer;
    TextView textViewTask;
    TextView textViewOption1;
    TextView textViewOption2;
    TextView textViewOption3;
    TextView textViewOption4;

    int countQuestion;
    int countAnswer;
    int a;
    int b;
    boolean isPlus;

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

        createTask();
    }

    private void createTask() {
        if (isPlus) {
            sum(a, b);
        } else {
            diff(a, b);
        }
    }

    private void sum(int a, int b) {
        int c = a + b;
        Log.i("SUM_", Integer.toString(c));
    }

    private void diff(int a, int b) {
        int c = a - b;
        Log.i("DIFF_", Integer.toString(c));
    }
}