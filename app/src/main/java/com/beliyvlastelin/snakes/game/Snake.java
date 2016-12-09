package com.beliyvlastelin.snakes.game;

import java.util.ArrayList;

/**
 * Created by mikola on 01.12.2016.
 */

public class Snake {
   private GameCell head;
    private ArrayList<GameCell> body;

    public Snake(GameCell head, ArrayList<GameCell> body) {
        this.head = head;
        this.body = body;
    }

    public GameCell getHead() {

        return head;
    }

    public void setHead(GameCell head) {
        this.head = head;
    }

    public ArrayList<GameCell> getBody() {
        return body;
    }

    public void setBody(ArrayList<GameCell> body) {
        this.body = body;
    }

    private void addBodyCell(GameCell cell){
        body.add(cell);
    }
}
