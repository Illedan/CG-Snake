package com.codingame.game;

import java.awt.*;
import java.util.ArrayList;

public class Snake {
    private static int deathScore = -3;
    public int score;
    public int id;
    public ArrayList<SnakePart> snake = new ArrayList<>();
    public boolean isDead;
    public boolean isInitialized;
    public Snake(int id, Point spawn){
        this.id = id;
        snake.add(new SnakePart(spawn));
    }

    public void move(SnakeDirection direction, boolean eatsFood){
        if(eatsFood){
            score += Constants.FOOD_SCORE;
            snake.add(0, new SnakePart(direction.getNext(snake.get(0).point)));
            return;
        }

        Point next = direction.getNext(snake.get(0).point);
        if(snake.size()==2 && snake.get(1).point.equals(next)){
            kill();
            return;
        }

        for(int i = snake.size()-1; i > 0; i--){
            snake.get(i).point = snake.get(i-1).point;
        }

        snake.get(0).point = next;
    }

    public void kill(){
        isDead = true;
        score = deathScore++;
    }
}
