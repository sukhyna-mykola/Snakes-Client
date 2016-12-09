package com.beliyvlastelin.snakes.game;

/**
 * Created by mikola on 01.12.2016.
 */

public class GameCell {
    private int x;
    private int y;
    private TypeCell mTypeCell;

    public GameCell(int x, int y, TypeCell typeCell) {
        this.x = x;
        this.y = y;
        mTypeCell = typeCell;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public TypeCell getTypeCell() {
        return mTypeCell;
    }

    public void setTypeCell(TypeCell typeCell) {
        mTypeCell = typeCell;
    }
}
