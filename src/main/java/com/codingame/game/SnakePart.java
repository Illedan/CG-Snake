package com.codingame.game;

import java.awt.*;

public class SnakePart {
    public Point point = new Point(0,0);
    public boolean hasFood;
    public SnakePart Next = null;
    public void move(SnakeDirection direction){
        point = new Point(point.x+direction.getDx(), point.y+direction.getDy());
    }

    
}
