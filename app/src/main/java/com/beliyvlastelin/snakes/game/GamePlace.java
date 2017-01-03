package com.beliyvlastelin.snakes.game;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.beliyvlastelin.snakes.Constants;
import com.beliyvlastelin.snakes.ManagerRequests;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by mikola on 01.12.2016.
 */

public class GamePlace {
    private int width;
    private int height;
    private GameCell[][] mGameCells;
    private Random r;

    public static String res = new String();

    private ArrayList<Snake> mSnakes;
    private ArrayList<Bonus> mBonuses;

    private boolean dead;
    private boolean gameOver;


    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public GamePlace(int width, int height) {
        this.width = width;
        this.height = height;
        r = new Random();

        mBonuses = new ArrayList<>();
        mGameCells = new GameCell[width][height];


    }

    public GameCell[][] getGameCells() {
        return mGameCells;
    }

    public void setGameCells(GameCell[][] gameCells) {
        mGameCells = gameCells;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public boolean update() {
        mSnakes.clear();
        String responce = ManagerRequests.checkConnect(Constants.ip, Constants.port).update();

        String statusGame = ManagerRequests.getStatusGame(responce);

        if(statusGame.equals(Constants.GAME_OVER))
            setGameOver(true);

        if(statusGame.equals(Constants.DEAD))
            setDead(true);

        if(!isGameOver()) {
            mSnakes = (ArrayList<Snake>) ManagerRequests.getSnakesCoordinates(responce);

            mGameCells = new GameCell[width][height];


            for (Snake snake : mSnakes) {
                putCell(snake.getHead());
                for (GameCell cell : snake.getBody()) {
                    putCell(cell);
                }
            }

            for (Bonus bonus : mBonuses) {
                putCell(bonus.getBonus());
            }
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    if (mGameCells[i][j] == null) {
                        putCell(new GameCell(i, j, TypeCell.GAME_CELL));
                    }
                }
            }
            return true;
        }else {
            return false;
        }

    }

    public void putCell(GameCell cell) {
        mGameCells[cell.getX()][cell.getY()] = cell;
    }

}
