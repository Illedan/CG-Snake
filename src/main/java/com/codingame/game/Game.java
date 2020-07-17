package com.codingame.game;

import java.awt.*;
import java.util.ArrayList;

public class Game {
    private int currentPlayer;
    public ArrayList<Snake> snakes = new ArrayList<>();
    public ArrayList<Point> food = new ArrayList<>();
    private IReferee referee;

    public Game(int players, IReferee referee, long seed){
        this.referee = referee;
        for(int i = 0; i < players; i++){
            snakes.add(new Snake())
        }
    }

    public void onTurn(){

    }

    private int countAlivePlayers(){
        int count = 0;
        for(Snake snake : snakes){
            if(!snake.isDead) count++;
        }

        return count;
    }
}
