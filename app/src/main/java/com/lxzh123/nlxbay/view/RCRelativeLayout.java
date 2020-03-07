package com.lxzh123.nlxbay.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.lxzh123.nlxbay.util.ViewUtils;

/**
 * description 圆角相对布局
 * author      Created by lxzh
 * date        2020-02-06
 */
public class RCRelativeLayout extends RelativeLayout {
    private float[] radii = new float[8];   // top-left, top-right, bottom-right, bottom-left
    private Path mClipPath;                 // 剪裁区域路径
    private Paint mPaint;                   // 画笔
    private boolean mClipBackground = false;// 是否剪裁背景
    private Region mAreaRegion;             // 内容区域
    private RectF mLayer;                   // 画布图层大小
    private int backgroundColor;            // 画布背景颜色

    public RCRelativeLayout(Context context) {
        this(context, null);
    }

    public RCRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RCRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        radii = new float[]{0, 0, 0, 0, 0, 0, 0, 0};

        mLayer = new RectF();
        mClipPath = new Path();
        mAreaRegion = new Region();
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mLayer.set(0, 0, w, h);
        float r = Math.min(w, h) / 2.0f;
        for (int i = 0; i < radii.length; i++) {
            radii[i] = r;
        }
        refreshRegion(this);
    }

    public void refreshRegion(View view) {
        int w = (int) mLayer.width();
        int h = (int) mLayer.height();
        RectF areas = new RectF();
        areas.left = view.getPaddingLeft();
        areas.top = view.getPaddingTop();
        areas.right = w - view.getPaddingRight();
        areas.bottom = h - view.getPaddingBottom();
        mClipPath.reset();

        mClipPath.addRoundRect(areas, radii, Path.Direction.CW);
        Region clip = new Region((int) areas.left, (int) areas.top,
                (int) areas.right, (int) areas.bottom);
        mAreaRegion.setPath(mClipPath, clip);
    }

    public void onClipDraw(Canvas canvas) {
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O_MR1) {
            mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
            canvas.drawPath(mClipPath, mPaint);
        } else {
            mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));

            final Path path = new Path();
            path.addRect(0, 0, (int) mLayer.width(), (int) mLayer.height(), Path.Direction.CW);
            path.op(mClipPath, Path.Op.DIFFERENCE);
            canvas.drawPath(path, mPaint);
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.saveLayer(mLayer, null, Canvas.ALL_SAVE_FLAG);
        super.dispatchDraw(canvas);
        onClipDraw(canvas);
        canvas.restore();
    }

    @Override
    public void setBackgroundColor(int color) {
        this.backgroundColor = color;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            int w = getMeasuredWidth();
            int h = getMeasuredHeight();
            int r = Math.min(w, h) / 2;
            GradientDrawable drawable = ViewUtils.getGradientDrawable(color, r);
            setBackground(drawable);
        } else {
            mClipBackground = true;
            super.setBackgroundColor(color);
        }
    }

    public void setBackgroundColor(int r, int color) {
        this.backgroundColor = color;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            GradientDrawable drawable = ViewUtils.getGradientDrawable(color, r);
            setBackground(drawable);
        } else {
            mClipBackground = true;
            super.setBackgroundColor(color);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if (mClipBackground) {
            canvas.save();
            canvas.clipPath(mClipPath);
            super.draw(canvas);
            canvas.restore();
        } else {
            super.draw(canvas);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        if (action == MotionEvent.ACTION_DOWN && !mAreaRegion.contains((int) ev.getX(), (int) ev.getY())) {
            return false;
        }
        if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_UP) {
            refreshDrawableState();
        } else if (action == MotionEvent.ACTION_CANCEL) {
            setPressed(false);
            refreshDrawableState();
        }
        return super.dispatchTouchEvent(ev);
    }

    //--- 公开接口 ----------------------------------------------------------------------------------

    public void setRadius(int radius) {
        for (int i = 0; i < radii.length; i++) {
            radii[i] = radius;
        }
        invalidate();
    }

    public void setTopLeftRadius(int topLeftRadius) {
        radii[0] = topLeftRadius;
        radii[1] = topLeftRadius;
        invalidate();
    }

    public void setTopRightRadius(int topRightRadius) {
        radii[2] = topRightRadius;
        radii[3] = topRightRadius;
        invalidate();
    }

    public void setBottomLeftRadius(int bottomLeftRadius) {
        radii[6] = bottomLeftRadius;
        radii[7] = bottomLeftRadius;
        invalidate();
    }

    public void setBottomRightRadius(int bottomRightRadius) {
        radii[4] = bottomRightRadius;
        radii[5] = bottomRightRadius;
        invalidate();
    }

    @Override
    public void invalidate() {
        refreshRegion(this);
        super.invalidate();
    }
}