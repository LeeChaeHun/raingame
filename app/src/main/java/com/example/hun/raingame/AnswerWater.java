package com.example.hun.raingame;

public class AnswerWater {
    int x,y;
    int speed;

    AnswerWater(int x, int y, int speed){
        this.x = x; this.y =y ; this.speed= speed;

    }
    public void move(){
        y+=speed;
    }
}
