package com.kresic.nikola.fingerspeedgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String KEY_REMAINING_TIME = "remaining time";
    private static final String KEY_STARTING_NUMBER = "clicked number";

    private TextView tvTimer;
    private TextView tvStartingNumber;
    private Button btnTapTap;
    private long initialCountdownTimeInMillis = 60000;
    private int timeInterval = 1000;
    private int remainingTime;
    private int startingNumber = 300;


    private CountDownTimer mCountDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvTimer = findViewById(R.id.tv_timer);
        tvStartingNumber = findViewById(R.id.tv_startingNumber);
        btnTapTap = findViewById(R.id.btn_tap_tap);
        tvStartingNumber.setText(startingNumber + "");

        if (savedInstanceState != null){
            remainingTime = savedInstanceState.getInt(KEY_REMAINING_TIME);
            startingNumber = savedInstanceState.getInt(KEY_STARTING_NUMBER);
            restoreTheGame();

        }


        btnTapTap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                winTheGame();

            }
        });



        if (savedInstanceState == null){
            startCountdown();
        }


    }

    private void winTheGame() {

        startingNumber--;
        tvStartingNumber.setText(startingNumber + "");

        if (remainingTime > 0 && startingNumber == 0){
            alertForEndingTheGame(getString(R.string.title_won), getString(R.string.message) );
        }
    }

    private void restoreTheGame() {
        int restoredRemainingTime = remainingTime;
        int restoredStartingNumber = startingNumber;
        tvTimer.setText(Integer.toString(restoredRemainingTime));
        tvStartingNumber.setText(Integer.toString(restoredStartingNumber));

        mCountDownTimer = new CountDownTimer((long)remainingTime * 1000, timeInterval) {
            @Override
            public void onTick(long millisUntilFinished) {

                remainingTime = (int)millisUntilFinished / 1000;
                tvTimer.setText(Integer.toString(remainingTime));

            }

            @Override
            public void onFinish() {
                alertForEndingTheGame(getString(R.string.tile_lost), getString(R.string.message) );

            }
        };
        mCountDownTimer.start();
    }


    public void resetTheGame(){

        if(mCountDownTimer != null){
            mCountDownTimer.cancel();
        }

        startingNumber = 300;
        tvStartingNumber.setText(Integer.toString(startingNumber));
        startCountdown();



    }

    public void startCountdown(){
        mCountDownTimer = new CountDownTimer(initialCountdownTimeInMillis, timeInterval) {
            @Override
            public void onTick(long millisUntilFinished) {
                remainingTime = (int) millisUntilFinished / 1000;
                tvTimer.setText(Integer.toString(remainingTime));

            }

            @Override
            public void onFinish() {

                alertForEndingTheGame(getString(R.string.tile_lost), getString(R.string.message));

            }
        };

        mCountDownTimer.start();

    }


    public void alertForEndingTheGame(String title, String message){

        AlertDialog builder = new AlertDialog.Builder(MainActivity.this)
                .setCancelable(false)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        resetTheGame();

                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .show();

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_REMAINING_TIME, remainingTime);
        outState.putInt(KEY_STARTING_NUMBER, startingNumber);
        mCountDownTimer.cancel();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.close_item){

            finish();

        }

        return true;
    }
}