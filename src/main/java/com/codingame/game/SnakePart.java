package com.codingame.game;

import java.awt.*;

public class SnakePart {
    public Point point = new Point(0,0);
    public SnakePart next = null;
    public boolean hasFood;
    public SnakeDirection direction = new SnakeDirection("N");

    public SnakePart(Point startingPosition){
        point = startingPosition;
    }

    private void move(SnakeDirection nextDirection, boolean ateFood){
        if(next != null){
            next.move(direction, hasFood);
        }else if(hasFood){
            next = new SnakePart(point);
        }
        point = new Point(point.x+direction.getDx(), point.y+direction.getDy());

        hasFood = ateFood;
        direction = nextDirection;
    }

    public void moveHead(SnakeDirection nextDirection, boolean ateFood){
        direction = nextDirection;
        move(nextDirection, ateFood);
    }
}
