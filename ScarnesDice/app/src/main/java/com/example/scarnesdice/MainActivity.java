package com.example.scarnesdice;

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

    private static boolean isPlayerChance = true;

    private Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button rollButton = (Button) findViewById(R.id.roll_btn);
        Button holdButton = (Button) findViewById(R.id.hold_btn);
        Button resetButton = (Button) findViewById(R.id.reset_btn);


        rollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView turnScore = (TextView) findViewById(R.id.turn_score);

                int dice_val = random.nextInt(6) + 1;
                if(dice_val == 1) {
                    isPlayerChance = !isPlayerChance;
                    user_turn_score = 0;

                    Log.d("test", "NOW COMPUTER CHANCE");
                }else{
                    user_turn_score+= dice_val;
                }
                turnScore.setText(("" +  user_turn_score));
                changeDiceImage(dice_val);
            }
        });

        holdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView userOverallScore = (TextView) findViewById(R.id.player_score);
                user_overall_score += user_turn_score;
                user_turn_score = 0;

                userOverallScore.setText(("" + user_overall_score));

            }
        });


        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("test", "I WAS CLICKED : RESET");

            }
        });
    }

    private void changeDiceImage(Integer dice_val){
        ImageView diceImage = (ImageView) findViewById(R.id.dice_image);
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

    private void computerTurn(){
        // TODO: COMPUTER TURN
    }
}
