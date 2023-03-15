package com.demo.braintrainer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextView textViewTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewTimer = findViewById(R.id.textViewTimer);

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
    }
}