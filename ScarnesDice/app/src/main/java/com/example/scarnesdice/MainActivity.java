package com.example.scarnesdice;

import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.nio.channels.Channel;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private static int user_overall_score = 0;
    private static int comp_overall_score = 0;
    private static int user_turn_score = 0;
    private static int comp_turn_score = 0;

    private static  boolean isWin = false;

    private Timer timer;

    private Random random = new Random();

    // Three Buttons
    private Button rollButton;
    private Button holdButton;
    private Button resetButton;

    // Text Views
    private TextView turnLabel;
    private TextView turnScoreText;
    private TextView playerOverAllScoreText;
    private TextView compOverallScoreText;
    private TextView winnerText;

    // Image View
    private ImageView diceImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rollButton = (Button) findViewById(R.id.roll_btn);
        holdButton = (Button) findViewById(R.id.hold_btn);
        resetButton = (Button) findViewById(R.id.reset_btn);

        turnScoreText = (TextView) findViewById(R.id.turn_score);
        turnLabel = (TextView) findViewById(R.id.turn_label_text);
        playerOverAllScoreText = (TextView) findViewById(R.id.player_score);
        compOverallScoreText = (TextView) findViewById(R.id.comp_score);

        winnerText = (TextView) findViewById(R.id.game_winner);

        diceImage = (ImageView) findViewById(R.id.dice_image);

        /*
            ROLL DICE CLICK LISTENER
         */
        rollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                winnerText.setText("");
                int dice_val = random.nextInt(6) + 1;
                changeDiceImage(dice_val);
                if(dice_val == 1) {
                    user_turn_score = 0;
                    Handler handle = new Handler();
                    handle.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            winnerText.setText("You rolled 1");

                            Handler handle2 = new Handler();
                            handle2.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    winnerText.setText("");
                                    if(isWin == false){
                                        computerTurn();
                                    }
                                }
                            }, 1000);

                        }
                    }, 500);
                }else{
                    user_turn_score+= dice_val;
                }
                turnScoreText.setText(("" +  user_turn_score));
            }
        });


        /*
            HOLD CLICK LISTENER
         */
        holdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // COMPUTE
                user_overall_score += user_turn_score;
                checkWinner();

                user_turn_score = 0;

                // DISPLAY
                playerOverAllScoreText.setText(("" + user_overall_score));
                turnScoreText.setText(("" + 0));
                changeDiceImage(1);

                // COMPUTER CHANCE
                if(isWin == false){
                    computerTurn();
                }
            }
        });

        /*
            RESET CLICK LISTENER
         */
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetAllValues();
            }
        });
    }

    private void changeDiceImage(Integer dice_val){
        String dice_id = "dice"+ dice_val;

        int resId = 0;

        switch (dice_val){
            case 1:
                resId = R.drawable.dice1;
                break;
            case 2:
                resId = R.drawable.dice2;
                break;
            case 3:
                resId = R.drawable.dice3;
                break;
            case 4:
                resId = R.drawable.dice4;
                break;
            case 5:
                resId = R.drawable.dice5;
                break;
            case 6:
                resId = R.drawable.dice6;
                break;
        }
        diceImage.setImageDrawable(getResources().getDrawable(resId));
    }

    private void computerResetValues() {
        compOverallScoreText.setText(("" + comp_overall_score));
        changeDiceImage(1);
        turnLabel.setText("PLAYER TURN SCORE");
        turnScoreText.setText(("" + 0));
        rollButton.setEnabled(true);
        holdButton.setEnabled(true);
        comp_turn_score = 0;
    }

    private void computerTurn() {
        turnLabel.setText("COMP TURN SCORE");
        rollButton.setEnabled(false);
        holdButton.setEnabled(false);

        final Handler handle = new Handler();

        handle.postDelayed(new Runnable() {
            @Override
            public void run() {
                int comp_dice_val = random.nextInt(6) + 1;

                if(comp_dice_val ==  1){
                    winnerText.setText(("COMP ROLLED 1, Your Turn" ));
                    computerResetValues();
                }
                else if(comp_turn_score >= 20){
                    comp_overall_score += comp_turn_score;
                    checkWinner();
                    computerResetValues();
                }else{
                    comp_turn_score += comp_dice_val;
                    turnScoreText.setText(("" + comp_turn_score));
                    changeDiceImage(comp_dice_val);
                    computerTurn();
                }
            }
        }, 1000);
    }
    private void resetAllValues(){
        user_overall_score = 0;
        comp_overall_score = 0;
        user_turn_score = 0;
        comp_turn_score =0;
        turnScoreText.setText(("" + 0));
        playerOverAllScoreText.setText(("" + 0));
        compOverallScoreText.setText(("" + 0));
        changeDiceImage(1);
        winnerText.setText("");
        isWin = false;
    }

    private void checkWinner(){
        if(user_overall_score >= 100){
            isWin = true;
            Handler handle = new Handler();
            handle.postDelayed(new Runnable() {
                @Override
                public void run() {
                    winnerText.setText(("PLAYER WINS"));

                    Handler handle1 = new Handler();
                    handle1.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            resetAllValues();
                        }
                    }, 1750);
                }
            }, 500);
            resetAllValues();
        }

        else if(comp_overall_score >= 100){
            isWin = true;
            Handler handle = new Handler();
            handle.postDelayed(new Runnable() {
                @Override
                public void run() {
                    winnerText.setText(("COMPUTER WINS"));

                    Handler handle1 = new Handler();
                    handle1.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            resetAllValues();
                        }
                    }, 1750);
                }
            }, 500);
        }
    }
}
