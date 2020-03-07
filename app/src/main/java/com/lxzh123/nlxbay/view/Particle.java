package com.lxzh123.nlxbay.view;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * description $
 * author      Created by lxzh
 * date        2020-03-07
 */
public class Particle {
    private final static float PI = (float) Math.PI;
    private float x;
    private float y;
    private float radius;
    private int color;
    private float vx;
    private float vy;
    private Paint paint;
    public static int X = 0;
    public static int Y = 0;
    public static int W = 400;
    public static int H = 400;

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public float getVx() {
        return vx;
    }

    public void setVx(float vx) {
        this.vx = vx;
    }

    public float getVy() {
        return vy;
    }

    public void setVy(float vy) {
        this.vy = vy;
    }

    public static void init(int x,int y,int w,int h) {
        X = x;
        Y = y;
        W = w;
        H = h;
    }

    public Particle(float x, float y, float radius, int color, float vx, float vy) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.color = color;
        this.vx = vx;
        this.vy = vy;
        paint = new Paint();
        paint.setStrokeWidth(radius);
        paint.setColor(color);
    }

    public void draw(Canvas canvas) {
        RectF rectF = new RectF(x - radius, y - radius, x + radius, y + radius);
        canvas.drawArc(rectF, 0f, 360, true, paint);
    }

    public void tick() {
        this.x += this.vx / 20;
        this.y += this.vy / 20;
        if (this.x - this.radius * 2 <= X || this.x + this.radius * 2 >= X + W) {
            this.vx = -this.vx;
        }
        if (this.y - this.radius * 2 <= Y || this.y + this.radius * 2 >= Y + H) {
            this.vy *= -1;
        }
    }
}
