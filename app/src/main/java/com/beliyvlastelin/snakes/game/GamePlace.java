package com.beliyvlastelin.snakes.game;

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

    public static String res = "daishljdaskhdsajk";

    private ArrayList<Snake> mSnakes;
    private ArrayList<Bonus> mBonuses;

    public GamePlace(int width, int height) {
        this.width = width;
        this.height = height;
        r = new Random();

        mSnakes = new ArrayList<>();
        ArrayList<GameCell> body = new ArrayList<>();
        for (int i = 0; i < r.nextInt(Game.sizePlace); i++) {
            body.add(new GameCell(r.nextInt(Game.sizePlace), r.nextInt(Game.sizePlace), TypeCell.SNAKE_PART_CELL));
        }

        mSnakes.add(new Snake(new GameCell(r.nextInt(Game.sizePlace), r.nextInt(Game.sizePlace), TypeCell.SNAKE_HEAD_CELL), body));
        mBonuses = new ArrayList<>();
        mGameCells = new GameCell[width][height];


    }

    public GameCell[][] getGameCells() {
        return mGameCells;
    }

    public void setGameCells(GameCell[][] gameCells) {
        mGameCells = gameCells;
    }

    public void update() {
        mSnakes.clear();
        String responce = ManagerRequests.get(Constants.ip, Constants.port).getResponce();

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


    }

    public void putCell(GameCell cell) {
        mGameCells[cell.getX()][cell.getY()] = cell;
    }

}
