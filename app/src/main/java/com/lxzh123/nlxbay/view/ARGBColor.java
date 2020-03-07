package com.lxzh123.nlxbay.view;

/**
 * description $
 * author      Created by lxzh
 * date        2020-03-07
 */
public class ARGBColor {
    public int a;
    public int r;
    public int g;
    public int b;

    public ARGBColor(int a, int r, int g, int b) {
        this.a = a & 0xFF;
        this.r = r & 0xFF;
        this.g = g & 0xFF;
        this.b = b & 0xFF;
    }

    public ARGBColor(int color) {
        if (color < 0x01000000) {
            this.a = 0xFF;
        } else {
            this.a = (color & 0xFF000000) >> 24;
        }
        this.r = (color & 0x00FF0000) >> 16;
        this.g = (color & 0x0000FF00) >> 8;
        this.b = (color & 0x000000FF) >> 0;
    }

    public int get() {
        return ((a & 0xFF) << 24) |
               ((r & 0xFF) << 16) |
               ((g & 0xFF) << 8) |
                (b & 0xFF);
    }

    @Override
    public String toString() {
        return "ARGBColor{" + this.hashCode() +
                " argb=(" + a +
                "," + r +
                "," + g +
                "," + b +
                ')';
    }
}
