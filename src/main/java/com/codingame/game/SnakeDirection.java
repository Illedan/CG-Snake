package com.codingame.game;

import java.awt.*;

public class SnakeDirection {
    private final Direction dir;
    public SnakeDirection(String direction) throws IllegalArgumentException{
        try{
            dir = Direction.valueOf(direction);
        }
        catch (Exception e){
            throw new IllegalArgumentException("Can't parse direction: " + direction + ". Valid values are: N, W, S or E");
        }
    }

    public Point getNext(Point current){
        return new Point(current.x+getDx(), current.y+getDy());
    }

    public int getDy(){
        if(dir == Direction.N) return -1;
        if(dir == Direction.S) return 1;
        return 0;
    }

    public int getDx(){
        if(dir == Direction.W) return -1;
        if(dir == Direction.E) return 1;
        return 0;
    }

}
