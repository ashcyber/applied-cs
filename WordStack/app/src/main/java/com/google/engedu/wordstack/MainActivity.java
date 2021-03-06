/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.wordstack;

import android.content.res.AssetManager;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    private static final int WORD_LENGTH = 5;
    public static final int LIGHT_BLUE = Color.rgb(176, 200, 255);
    public static final int LIGHT_GREEN = Color.rgb(200, 255, 200);
    private ArrayList<String> words = new ArrayList<>();
    private Random random = new Random();
    private StackedLayout stackedLayout;
    private String word1, word2;
    private LetterTile letterTile;

    private Stack<LetterTile> placedTiles = new Stack();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AssetManager assetManager = getAssets();
        try {
            InputStream inputStream = assetManager.open("words.txt");
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            while((line = in.readLine()) != null) {
                String word = line.trim();
                if(word.length() <= WORD_LENGTH){
                    words.add(word);
                }
            }
        } catch (IOException e) {
            Toast toast = Toast.makeText(this, "Could not load dictionary", Toast.LENGTH_LONG);
            toast.show();
        }
        LinearLayout verticalLayout = (LinearLayout) findViewById(R.id.vertical_layout);
        stackedLayout = new StackedLayout(this);
        verticalLayout.addView(stackedLayout, 3);


        View word1LinearLayout = findViewById(R.id.word1);
//        word1LinearLayout.setOnTouchListener(new TouchListener());
        word1LinearLayout.setOnDragListener(new DragListener());
        View word2LinearLayout = findViewById(R.id.word2);
//        word2LinearLayout.setOnTouchListener(new TouchListener());
        word2LinearLayout.setOnDragListener(new DragListener());
    }

    private class TouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN && !stackedLayout.empty()) {
                LetterTile tile = (LetterTile) stackedLayout.peek();
                tile.moveToViewGroup((ViewGroup) v);
                if (stackedLayout.empty()) {
                    TextView messageBox = (TextView) findViewById(R.id.message_box);
                    messageBox.setText(word1 + " " + word2);
                }
                placedTiles.push(tile);
                return true;
            }
            return false;
        }
    }

    /*
        ACTION_DRAG_STARED -> highlights the both linear layout where the word
        will be dragged

        ACTION_DRAG_ENTERED -> highlights green when the letter tile enter any 2 wordlinearLayout

        ACTION_DRAG_EXITED -> if user dragged by didn't drop on the text view then
        reset the blue color

        ACTION_DRAG_ENDED -> reset the color to white once dragged on the wordLinearLayout

        ACTION_DROP -> insert letter to placedTiles stack and change ViewGroup of the tile
     */
    private class DragListener implements View.OnDragListener {

        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    v.setBackgroundColor(LIGHT_BLUE);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DRAG_ENTERED:
                    v.setBackgroundColor(LIGHT_GREEN);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DRAG_EXITED:
                    v.setBackgroundColor(LIGHT_BLUE);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DRAG_ENDED:
                    v.setBackgroundColor(Color.WHITE);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DROP:
                    // Dropped, reassign Tile to the target Layout
                    LetterTile tile = (LetterTile) event.getLocalState();
                    tile.moveToViewGroup((ViewGroup) v);
                    if (stackedLayout.empty()) {
                        TextView messageBox = (TextView) findViewById(R.id.message_box);
                        ((Button) findViewById(R.id.button)).setEnabled(false);
                        messageBox.setText(word1 + " " + word2);
                    }
                    placedTiles.push(tile);
                    return true;
            }
            return false;
        }
    }
    // helper function for scrambling
    private String scrambleWords(){
        int i=0, j=0;
        String scram = "";

        while(i < word1.length() && j < word2.length()){
            switch(random.nextInt(2)){
                case 0:
                    scram += word1.charAt(i);
                    i++;
                    break;
                case 1:
                    scram += word2.charAt(j);
                    j++;
                    break;
            }
        }

        while(i < word1.length()){
            scram+= word1.charAt(i);
            i++;
        }

        while(j < word2.length()){
            scram+= word2.charAt(j);
            j++;
        }
        return  scram;
    }

    public boolean onStartGame(View view) {
        TextView messageBox = (TextView) findViewById(R.id.message_box);
        ((Button) findViewById(R.id.button)).setEnabled(true);

        // CLEAR VIEWS
        LinearLayout word1LinearLayout = findViewById(R.id.word1);
        LinearLayout word2LinearLayout = findViewById(R.id.word2);
        word1LinearLayout.removeAllViews();
        word2LinearLayout.removeAllViews();
        stackedLayout.removeAllViews();
        stackedLayout.clear();

        // Words pick
        int word2_index = random.nextInt(words.size());
        int word1_index = random.nextInt(words.size());

        while((word2_index = random.nextInt(words.size())) == word1_index);

        word1 = words.get(word1_index);
        word2 = words.get(word2_index);

        // Obtain Scramble Words
        String scramble_word = scrambleWords();

        Stack<Character> letters = new Stack<>();

        Log.d("test", word1 + " " + word2);
        for(int str_itr = 0; str_itr < scramble_word.length(); str_itr++)
            letters.push(scramble_word.charAt(str_itr));

        messageBox.setText("Game Started");

        while(!letters.isEmpty()){
            stackedLayout.push(new LetterTile(this, letters.pop()));
        }

        return true;

    }
    public boolean onUndo(View view) {
        if(!placedTiles.isEmpty()){
            LetterTile tile = placedTiles.pop();
            tile.moveToViewGroup(stackedLayout);
        }
        return true;
    }
}
