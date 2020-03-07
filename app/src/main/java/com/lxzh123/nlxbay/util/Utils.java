package com.lxzh123.nlxbay.util;

/**
 * description $
 * author      Created by lxzh
 * date        2020-03-07
 */
public class Utils {
    public static void parseColor(int color) {
        int a = (color&0xFF000000)>>24;
        int r = (color&0x00FF0000)>>16;
        int g = (color&0x0000FF00)>>8;
        int b = (color&0x000000FF)>>0;
    }
}
