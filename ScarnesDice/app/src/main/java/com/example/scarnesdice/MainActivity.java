package com.example.scarnesdice;

import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private static int user_overall_score = 0;
    private static int comp_overall_score = 0;
    private static int user_turn_score = 0;
    private static int comp_turn_score = 0;

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


        diceImage = (ImageView) findViewById(R.id.dice_image);

        /*
            ROLL DICE CLICK LISTENER
         */
        rollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int dice_val = random.nextInt(6) + 1;
                changeDiceImage(dice_val);
                if(dice_val == 1) {
                    user_turn_score = 0;
                    computerTurn();
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
                user_turn_score = 0;

                // DISPLAY
                playerOverAllScoreText.setText(("" + user_overall_score));

                // COMPUTER CHANCE
                computerTurn();
            }
        });

        /*
            RESET CLICK LISTENER
         */
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("test", "I WAS CLICKED : RESET");

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
        turnScoreText.setText(("" + 0));
        turnLabel.setText("COMP TURN SCORE");
        rollButton.setEnabled(false);
        holdButton.setEnabled(false);

        int comp_dice_val = random.nextInt(6) + 1;

        while(comp_turn_score < 10 && comp_dice_val != 1){
            comp_turn_score += comp_dice_val;
            Log.d("test" , ("" + comp_turn_score + ", DICE: " + comp_dice_val));
            comp_dice_val = random.nextInt(6) + 1;
        }

        Log.d("text", ("OUT: " + comp_turn_score));

        if(comp_turn_score >= 10){
            // HOLD
            comp_overall_score += comp_turn_score;
        }

        computerResetValues();
    }
}
