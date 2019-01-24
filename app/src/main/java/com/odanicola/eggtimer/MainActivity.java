package com.odanicola.eggtimer;

import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private TextView textView;
    private Button btnGo;
    private Button btnStop;
    private MediaPlayer mediaPlayer;

    int minutes;
    int seconds;

    long minutesToMil;
    long secondsToMil;
    boolean isCanceled;
    SeekBar seekBar;

    public void doPlay(View view){
        switch (view.getId()){
            case R.id.btnGo:
                isCanceled = false;
                btnStop.setVisibility(View.VISIBLE);
                btnGo.setVisibility(View.INVISIBLE);
                break;
            case R.id.btnStop:
                isCanceled = true;
                btnGo.setVisibility(View.VISIBLE);
                btnStop.setVisibility(View.INVISIBLE);
                break;
        }

        //Log.i("isCanceled: ",""+ isCanceled);
        CountDownTimer countDownTimer;
        long countDownInterval = 1000;

        countDownTimer = new CountDownTimer(minutesToMil, countDownInterval){
            public  void onTick(long millisUntilFinished){
                //CountDown is counting down (every second)
                long menitMilis = millisUntilFinished / 60000;
                long menit = TimeUnit.MINUTES.toMinutes(menitMilis);
                long detikMilis = (millisUntilFinished / 1000) % 60;

                String secondStrings = Long.toString(detikMilis);

                if (detikMilis == 0) {
                    secondStrings = "00";
                }else if(detikMilis < 10){
                    secondStrings = "0"+detikMilis;
                }

                if(isCanceled){
                    textView.setText("0:00");
                    seekBar.setProgress(0);
                    minutesToMil = 0;
                    cancel();
                }else{
                    seekBar.setProgress((int) millisUntilFinished / 1000);
                    textView.setText(Long.toString(menit) + ":" + secondStrings);
                }
            };

            public void onFinish(){
                //Counter is finished (after 10 second)
                Log.i("Timer is ", "finished");
                btnGo.setVisibility(View.VISIBLE);
                btnStop.setVisibility(View.INVISIBLE);
                seekBar.setProgress(0);
                minutesToMil = 0;
                mediaPlayer.start();
            }
        }.start();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textTimer);
        textView.setText("0:00");

        btnGo = (Button) findViewById(R.id.btnGo);
        btnStop = (Button) findViewById(R.id.btnStop);

        mediaPlayer = MediaPlayer.create(this, R.raw.air_horn);

        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setMax(600);
        seekBar.setProgress(0);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                minutes = i / 60;
                seconds = i - minutes * 60;

                secondsToMil = seconds * 1000;
                minutesToMil = minutes * 60000 + secondsToMil;

                String secondStrings = Integer.toString(seconds);

                if (seconds == 0) {
                    secondStrings = "00";
                }else if(seconds < 10){
                    secondStrings = "0"+seconds;
                }

                textView.setText(Integer.toString(minutes) + ":" + secondStrings);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //seekBar.setVisibility(View.GONE);
            }
        });
    }
}
