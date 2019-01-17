package com.jack.app.time;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.jack.app.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by liuyang on 2017/11/17.
 */

public class TestTimerActivity extends Activity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_timer_test);
        initTimer();
    }

    Timer timer ;
    boolean hasTimerStopped = true;

    private void initTimer() {
        timer = new Timer();
        final TimerTask timeTask = new TimerTask() {
            @Override
            public void run() {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");//System.currentTimeMillis();
                Date date = new Date(System.currentTimeMillis());
                String time = dateFormat.format(date);
                Log.d("text info :" , "time :" + time);
            }
        };
        findViewById(R.id.tv_display_content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasTimerStopped) {
                    timer.schedule(timeTask, 0, 1000);
                    hasTimerStopped = false;
                } else {
//                    timer.cancel();
                    timer.purge();
                    hasTimerStopped = true;
                }
            }
        });


    }

    public void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }
}
