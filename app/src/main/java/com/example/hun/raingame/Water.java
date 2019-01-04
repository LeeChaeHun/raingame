package com.example.hun.raingame;

public class Water {
    int x , y;
    int speed;

    Water(int x ,int y, int speed){
        this.x = x; this.y = y ; this.speed = speed;
    }
    public void move(){
        y+=speed;
    }

}
