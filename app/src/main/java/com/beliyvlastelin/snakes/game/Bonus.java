package com.beliyvlastelin.snakes.game;

/**
 * Created by mikola on 01.12.2016.
 */

public class Bonus {
    private  GameCell bonus;

    public GameCell getBonus() {
        return bonus;
    }

    public void setBonus(GameCell bonus) {
        this.bonus = bonus;
    }

    public Bonus(GameCell bonus) {

        this.bonus = bonus;
    }
}
