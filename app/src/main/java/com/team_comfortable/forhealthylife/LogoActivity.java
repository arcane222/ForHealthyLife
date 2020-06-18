package com.team_comfortable.forhealthylife;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class LogoActivity extends AppCompatActivity
{
    private int timerCount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);
        final Timer timer = new Timer();
        TimerTask timerTask = new TimerTask()
        {
            @Override
            public void run() {
                timerCount++;
                if(timerCount >= 2)
                {
                    timer.cancel();
                    Intent intent = new Intent(getApplication(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
        timer.schedule(timerTask, 1000, 1000);
    }
}
