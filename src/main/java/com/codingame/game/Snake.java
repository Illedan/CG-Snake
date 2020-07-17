package com.codingame.game;

import java.awt.*;

public class Snake {
    private static int deathScore = -4;
    public int score;
    public SnakePart head;
    public boolean isDead;
    public Snake(Point spawn){
        head = new SnakePart(spawn);
    }

    public void move(SnakeDirection direction, boolean eatsFood){
        head.moveHead(direction, eatsFood);
        if(eatsFood) score += Constants.FOOD_SCORE;
    }

    public void kill(){
        isDead = true;
        score = deathScore++;
    }
}
