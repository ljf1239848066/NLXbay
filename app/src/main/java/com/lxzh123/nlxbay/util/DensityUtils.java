package com.lxzh123.nlxbay.util;

import android.content.Context;

/**
 * 像素转换工具类
 *
 * @author geetest 谷闹年
 * @date 2019/3/13
 */
public class DensityUtils {

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static float getScale(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    public static float getFontScale(Context context) {
        return context.getResources().getDisplayMetrics().scaledDensity;
    }

    public static int dip2px(float scale, float dpValue) {
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dip(float scale, float pxValue) {
        return (int) (pxValue / scale + 0.5f);
    }

    public static int px2sp(float fontScale, float pxValue) {
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static int sp2px(float fontScale, float spValue) {
        return (int) (spValue * fontScale + 0.5f);
    }
}
