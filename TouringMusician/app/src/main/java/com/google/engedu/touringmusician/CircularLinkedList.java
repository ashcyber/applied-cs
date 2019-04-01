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

package com.google.engedu.touringmusician;


import android.graphics.Point;
import android.util.Log;

import java.util.Iterator;

public class CircularLinkedList implements Iterable<Point> {

    Node head;

    private class Node {
        Point point;
        Node prev, next;

        Node(){
            this.prev = null;
            this.next = null;
        }

        Node(Point p){
            this.point = p;
            this.prev = null;
            this.next = null;
        }
    }

    // Helper function for creating node
    public Node makeNode(Point p){
        Node newNode = new Node(p);
        if(head == null){
            head = newNode;
            head.next = head;
            head.prev = head;
            head.next.next = head;

        }
        return newNode;
    }
    public void insertBeginning(Point p) {
        // Create a New Node
        Node newNode = makeNode(p);

        // Add Node at the beginning;
        newNode.next = head;
        newNode.prev = head.prev;
        newNode.prev.next = newNode;
        head.prev = newNode;
    }

    private float distanceBetween(Point from, Point to) {
        return (float) Math.sqrt(Math.pow(from.y-to.y, 2) + Math.pow(from.x-to.x, 2));
    }

    public float totalDistance() {
        float total = 0;

        CircularLinkedListIterator itr = new CircularLinkedListIterator();

        // When itr becomes head hasNext returns null because current is set to null by .next()
        while(itr.hasNext()){
            total += distanceBetween(itr.current.point,
                    itr.current.next.point);
            itr.next();
        }

        return total;
    }

    public void insertNearest(Point p) {
        Node newNode = makeNode(p);

        float near_dis = Float.MAX_VALUE;
        Node nearest_node = null;

        CircularLinkedListIterator itr = new CircularLinkedListIterator();

        while(itr.hasNext()){
            Point cur = itr.current.point;

            if(near_dis > distanceBetween(cur,p)){
                near_dis = distanceBetween(cur,p);
                nearest_node = itr.current;
            }
            itr.next();
        }

        newNode.next = nearest_node.next;
        newNode.prev = nearest_node;
        nearest_node.next = newNode;
    }

    public void insertSmallest(Point p) {
        Node newNode = makeNode(p);

        if(head.next == head){
            newNode.next = head;
            newNode.prev = head.prev;
            newNode.prev.next = newNode;
            head.prev = newNode;
        }else{
            // TODO : LOGIC FOR INSERT SMALLEST
        }




    }

    public void reset() {
        head = null;
    }

    private class CircularLinkedListIterator implements Iterator<Point> {

        Node current;

        public CircularLinkedListIterator() {
            current = head;
        }

        @Override
        public boolean hasNext() {
            return (current != null);
        }

        @Override
        public Point next() {
            Point toReturn = current.point;
            current = current.next;
            if (current == head) {
                current = null;
            }
            return toReturn;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public Iterator<Point> iterator() {
        return new CircularLinkedListIterator();
    }


}
