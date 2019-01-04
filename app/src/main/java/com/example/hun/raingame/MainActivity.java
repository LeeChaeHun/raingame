package com.example.hun.raingame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    Bitmap umbrella;
    int umbrella_x, umbrella_y;
    int umbrellaWidth;
    int umbrellaHeight;
    Bitmap left, right;
    int left_x, left_y;
    int right_x, right_y;
    int Width, Height;
    int score;
    int button_width;


    Bitmap waterimg;
    int waterWidth;
    int waterHeight;

    AnswerWater answerWater;


    int count;

    ArrayList<Water> water;
    Bitmap screen;

    int number1, number2;
    int answer;
    int[] wrongNumber = new int[5];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MyView(this));

        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Width = display.getWidth();
        Height = display.getHeight();


        water = new ArrayList<Water>();

        umbrella = BitmapFactory.decodeResource(getResources(), R.drawable.umbreller);
        int x = Width / 4;
        int y = Height / 14;
        umbrella = Bitmap.createScaledBitmap(umbrella, x, y, true);

        umbrellaWidth = umbrella.getWidth();
        umbrellaHeight = umbrella.getHeight();

        umbrella_x = Width * 1 / 9;
        umbrella_y = Height * 6 / 9;


        left = BitmapFactory.decodeResource(getResources(), R.drawable.left);
        left_x = Width * 5 / 9;
        left_y = Height * 7 / 9;

        button_width = Width / 6;


        left = Bitmap.createScaledBitmap(left, button_width, button_width, true);

        right = BitmapFactory.decodeResource(getResources(), R.drawable.right);
        right_x = Width * 7 / 9;
        right_y = Height * 7 / 9;

        right = Bitmap.createScaledBitmap(right, button_width, button_width, true);


        waterimg = BitmapFactory.decodeResource(getResources(), R.drawable.water);
        waterimg = Bitmap.createScaledBitmap(waterimg, button_width, button_width + button_width / 4, true);

        waterWidth = waterimg.getWidth();
        waterHeight = waterimg.getHeight();


        screen = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        screen = Bitmap.createScaledBitmap(screen, Width, Height, true);

        Random r1 = new Random();
        int xx = r1.nextInt(Width);
        answerWater = new AnswerWater(x, 0, 5);


    }

    class MyView extends View {


        MyView(Context context) {

            super(context);    //상위클래스의 생성자를 호출해야 한다.
            setBackgroundColor(Color.BLUE);
            gHandler.sendEmptyMessageDelayed(0, 1000);
            makeQuestion();
            //  makeBalloon();
        }

        @Override
        synchronized public void onDraw(Canvas canvas) {


            if (water.size() < 4) {
                Random r1 = new Random();
                int x = r1.nextInt(Width - button_width);
                int y = r1.nextInt(Height / 4);
                water.add(new Water(x, -y, 5));

            }
            Paint p1 = new Paint();
            p1.setColor(Color.WHITE);
            p1.setTextSize(Width / 14);
            canvas.drawBitmap(screen, 0, 0, p1);


            canvas.drawText("점수 : " + Integer.toString(score), 0, Height * 1 / 12, p1);

            canvas.drawText("문제 : " + Integer.toString(number1) + "+" + Integer.toString(number2), 0, Height * 2 / 12, p1);

            canvas.drawBitmap(umbrella, umbrella_x, umbrella_y, p1);


            canvas.drawBitmap(left, left_x, left_y, p1);

            canvas.drawBitmap(right, right_x, right_y, p1);


            for (Water tmp : water)
                canvas.drawBitmap(waterimg, tmp.x, tmp.y, p1);

            for (int i = water.size() - 1; i >= 0; i--)
                canvas.drawText(Integer.toString(wrongNumber[i]), water.get(i).x + waterWidth / 6, water.get(i).y + waterWidth * 2 / 3, p1);


            canvas.drawBitmap(waterimg, answerWater.x, answerWater.y, p1);
            canvas.drawText(Integer.toString(answer), answerWater.x + waterWidth / 6, answerWater.y + waterWidth * 2 / 3, p1);


            if (answerWater.y > Height) answerWater.y = -50;

            moveBalloon();


            checkCollision();


            count++;


        }


        public void makeQuestion() {


            Random r1 = new Random();


            int x = r1.nextInt(99) + 1;
            number1 = x;

            x = r1.nextInt(99) + 1;
            number2 = x;

            answer = number1 + number2;


            int counter = 0;

            for (int i = 0; i < 5; i++) {
                x = r1.nextInt(197) + 1;
                while (x == answer) {
                    x = r1.nextInt(197) + 1;
                }
                wrongNumber[i] = x;

            }


        }


        public void moveBalloon() {
            //오답 풍선 움직이기
            for (int i = water.size() - 1; i >= 0; i--) {
                water.get(i).move();

            }

            //풍선이 화면아래로 사라지면 다시 위에서 나오도록 하기
            for (int i = water.size() - 1; i >= 0; i--) {
                if (water.get(i).y > Height) water.get(i).y = -100;
            }

            //정답 풍선 움직이기
            answerWater.move();
        }

        public void checkCollision() {
            //바구니와 오답풍선이 접촉했는지 체크
            for (int i = water.size() - 1; i >= 0; i--) {

                if (water.get(i).x + waterWidth / 2 > umbrella_x && water.get(i).x + waterWidth / 2 < umbrella_x + umbrellaWidth
                        && water.get(i).y + waterHeight > umbrella_y &&
                        water.get(i).y + waterHeight > umbrella_x + umbrellaWidth) {
                    water.remove(i);
                    score -= 10;
                }

            }

            //바구니와 정답풍선이 접촉했는지 체크
            if (answerWater.x + waterWidth / 2 > umbrella_x && answerWater.x + waterWidth / 2 < umbrella_x + umbrellaWidth
                    && answerWater.y + waterHeight > umbrella_y &&
                    answerWater.y + waterHeight > umbrella_x + umbrellaWidth) {
                score += 30;
                makeQuestion();
                Random r1 = new Random();

                int xx = r1.nextInt(Width - button_width);
                answerWater.x = xx;
                xx = r1.nextInt(300);
                answerWater.y = -xx;

            }


        }


        Handler gHandler = new Handler() {

            public void handleMessage(Message msg) {
                invalidate();

                gHandler.sendEmptyMessageDelayed(0, 30);  //1000 으로 하면 1초에 한번 실행된다.

            }


        };

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            int x = 0, y = 0;


            if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
                x = (int) event.getX();
                y = (int) event.getY();
            }

                if ((x > left_x) && (x < left_x + button_width) && (y > left_y) && (x < left_y + button_width))
                    umbrella_x -= 20;


                if ((x > right_x) && (x < right_x + button_width) && (y > right_y) && (x < right_y + button_width))
                    umbrella_x += 20;


                return true;
            }


        }

    }
