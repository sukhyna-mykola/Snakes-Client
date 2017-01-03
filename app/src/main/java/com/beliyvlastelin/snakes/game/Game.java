package com.beliyvlastelin.snakes.game;

/**
 * Created by mikola on 01.12.2016.
 */

public class Game {

    private GamePlace mGamePlace;

    public float getWidhtCell() {
        return widhtCell;
    }


    public float getHeihgtCell() {
        return heihgtCell;
    }


    public Game(float widht, float height) {
        mGamePlace = new GamePlace(sizePlace,sizePlace);

        this.widht = widht;
        this.height = height;

        widhtCell = widht/sizePlace;
        heihgtCell  = widht/sizePlace;
        //heihgtCell = height/sizePlace;

    }

    private float widht;
    private float height;

    private float widhtCell;
    private float heihgtCell;

    public static final int sizePlace = 21;


    public GamePlace getGamePlace() {
        return mGamePlace;
    }

    public void setGamePlace(GamePlace gamePlace) {
        mGamePlace = gamePlace;
    }

    public GamePlace getmGamePlace() {
        return mGamePlace;
    }

    public boolean update(){
       return mGamePlace.update();

    }
}
