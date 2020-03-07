package com.lxzh123.nlxbay.util;

import android.graphics.drawable.GradientDrawable;
import android.view.View.MeasureSpec;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.reflect.Field;

/**
 * description View 工具类
 * author      Created by lxzh
 * date        2020-02-11
 */
public class ViewUtils {
    public static GradientDrawable getGradientDrawable(int color, int radius) {
        // xml中定义的shape标签 对应此类
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);// 矩形
        shape.setCornerRadius(radius);// 圆角半径
        shape.setStroke(0, color);//描边
        shape.setColor(color);//填充颜色
        return shape;
    }

    public static String getMode(int measureSpec) {
        int mode = MeasureSpec.getMode(measureSpec);
        return (mode == MeasureSpec.AT_MOST) ? "AT_MOST" :
                mode == MeasureSpec.EXACTLY ? "EXACTLY" : "UNSPECIFIED";
    }

    /**
     * 设置 EditText 禁止显示复制粘贴菜单
     * @param editText
     */
    public static void setInsertionDisabled(EditText editText) {
        try {
            Field editorField = TextView.class.getDeclaredField("mEditor");
            editorField.setAccessible(true);
            Object editorObject = editorField.get(editText);

            // if this view supports insertion handles
            Class editorClass = Class.forName("android.widget.Editor");
            Field mInsertionControllerEnabledField = editorClass.getDeclaredField("mInsertionControllerEnabled");
            mInsertionControllerEnabledField.setAccessible(true);
            mInsertionControllerEnabledField.set(editorObject, false);

            // if this view supports selection handles
            Field mSelectionControllerEnabledField = editorClass.getDeclaredField("mSelectionControllerEnabled");
            mSelectionControllerEnabledField.setAccessible(true);
            mSelectionControllerEnabledField.set(editorObject, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
