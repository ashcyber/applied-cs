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

package com.google.engedu.bstguesser;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;

import org.w3c.dom.Node;

public class TreeNode {
    private static final int SIZE = 60;
    private static final int MARGIN = 20;
    private int value, height;
    protected TreeNode left, right;
    private boolean showValue;
    private int x, y;
    private int color = Color.rgb(150, 150, 250);

    public TreeNode(int value) {
        this.value = value;
        this.height = 0;
        showValue = false;
        left = null;
        right = null;
    }

    /* START HELPER FUNCTIONS  */
    private int height(TreeNode node){
        if(node == null){
            return  0;
        }
        return  node.height;
    }

    private int maxHeight(int left_h, int right_h){
        return left_h > right_h ? left_h : right_h;
    }

    private int getBalance(TreeNode node){
        if(node == null){
            return  0;
        }
            return (height(node.left) - height(node.right));
    }

    private void log(String msg){
        Log.d("AVLTREE", msg);
    }

    private TreeNode leftRotate(TreeNode y){

        /*
                  O Y
                    \
                     O X
                    /
                   O K

               TRANSFORMS TO

                    O X
                   /
                Y O
                   \
                    O K

         */

        TreeNode x = y.right;
        TreeNode k = x.left;

        // ROTATION
        x.left = y;
        y.right = k;

        // UPDATE HEIGHT of y first and then x value
        y.height = maxHeight(height(y.left), height(y.right)) + 1;
        x.height = maxHeight(height(x.left), height(x.right)) + 1;

        // return the new root node
        return x;
    }

    private TreeNode rightRotate(TreeNode y){
        /*

                    O Y
                   /
                X O
                   \
                    O K

               TRANSFORMS TO

                    O X
                     \
                      O Y
                     /
                    O K

         */

        TreeNode x = y.left;
        TreeNode k = x.right;

        // ROTATION
        x.right = y;
        y.left = k;

        // UPDATE HEIGHTS
        y.height = maxHeight(height(y.left), height(y.right)) + 1;
        x.height = maxHeight(height(x.left), height(x.right)) + 1;


        // return the new root node;
        return x;
    }

    /* END HELPER FUNCTIONS */

    public TreeNode insertRecur(TreeNode root, int key){
        if(root == null){
            return new TreeNode(key);
        }
        else if(key < root.value){
            root.left = insertRecur(root.left, key);
        }else if(key > root.value){
            root.right = insertRecur(root.right, key);
        }

        root.height = maxHeight(height(root.left), height(root.right)) + 1;

        int balance = getBalance(root);

        if(balance < -1 || balance > 1){
            // LEFT
            if(balance > 1){
                // LEFT CASE
                if(key < root.left.value){
                   // log("LEFT LEFT CASE");
                    return  rightRotate(root);
                }
                // RIGHT CASE
                else if(key > root.left.value){
                    // log("LEFT RIGHT CASE");
                    root.left = leftRotate(root.left);
                    return rightRotate(root);
                }
            }
            // RIGHT
            else{
                // LEFT CASE
                if(key < root.right.value){
                    // log("RIGHT LEFT CASE");
                    root.right = rightRotate(root.right);
                    return leftRotate(root);
                }
                // RIGHT CASE
                else{
                    // log("RIGHT RIGHT CASE");
                    return leftRotate(root);
                }
            }
        }
        return  root;
    }

    public int getValue() {
        return value;
    }

    public void positionSelf(int x0, int x1, int y) {
        this.y = y;
        x = (x0 + x1) / 2;

        if(left != null) {
            left.positionSelf(x0, right == null ? x1 - 2 * MARGIN : x, y + SIZE + MARGIN);
        }
        if (right != null) {
            right.positionSelf(left == null ? x0 + 2 * MARGIN : x, x1, y + SIZE + MARGIN);
        }
    }

    public void draw(Canvas c) {
        Paint linePaint = new Paint();
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(3);
        linePaint.setColor(Color.GRAY);
        if (left != null)
            c.drawLine(x, y + SIZE/2, left.x, left.y + SIZE/2, linePaint);
        if (right != null)
            c.drawLine(x, y + SIZE/2, right.x, right.y + SIZE/2, linePaint);

        Paint fillPaint = new Paint();
        fillPaint.setStyle(Paint.Style.FILL);
        fillPaint.setColor(color);
        c.drawRect(x-SIZE/2, y, x+SIZE/2, y+SIZE, fillPaint);

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(SIZE * 2/3);
        paint.setTextAlign(Paint.Align.CENTER);
        c.drawText(showValue  ? String.valueOf(value) : "?", x, y + SIZE * 3/4, paint);

        if (height > 0) {
            Paint heightPaint = new Paint();
            heightPaint.setColor(Color.MAGENTA);
            heightPaint.setTextSize(SIZE * 2 / 3);
            heightPaint.setTextAlign(Paint.Align.LEFT);
            c.drawText(String.valueOf(height), x + SIZE / 2 + 10, y + SIZE * 3 / 4, heightPaint);
        }

        if (left != null)
            left.draw(c);
        if (right != null)
            right.draw(c);
    }

    public int click(float clickX, float clickY, int target) {
        int hit = -1;
        if (Math.abs(x - clickX) <= (SIZE / 2) && y <= clickY && clickY <= y + SIZE) {
            if (!showValue) {
                if (target != value) {
                    color = Color.RED;
                } else {
                    color = Color.GREEN;
                }
            }
            showValue = true;
            hit = value;
        }
        if (left != null && hit == -1)
            hit = left.click(clickX, clickY, target);
        if (right != null && hit == -1)
            hit = right.click(clickX, clickY, target);
        return hit;
    }

    public void invalidate() {
        color = Color.CYAN;
        showValue = true;
    }
}
