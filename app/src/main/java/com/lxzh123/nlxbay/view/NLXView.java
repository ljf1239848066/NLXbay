package com.lxzh123.nlxbay.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.lxzh123.nlxbay.util.DensityUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * description $
 * author      Created by lxzh
 * date        2020-03-07
 */
public class NLXView extends View implements Runnable {
    private final static String TAG = "NLXView";

    private final static int DP_WIDTH = 300;
    private final static int DP_HEIGHT = 300;
    private final static float DP_RADIUS = 1.5f;
    private final static float DP_LINE_WIDTH = 1;
    private final static int PARTICLE_COUNT = 40;
    private final static int DP_MAX_LINE_DIST = 90;

    private final static int INTERVAL = 10;

    private volatile boolean mRunning;
    private volatile boolean mUpdating;
    private Context mContext;
    private float mRadius;
    private float mLineWidth;

    /**
     * x 坐标
     */
    private int mX;
    /**
     * y 坐标
     */
    private int mY;
    /**
     * 宽度
     */
    private int mW;
    /**
     * 高度
     */
    private int mH;
    /**
     * 最大速度
     */
    private int mMaxSpeed;
    /**
     * 粒子数
     */
    private int mCnt;
    /**
     * 线条最大距离
     */
    private int mDist;

    private List<Particle> particles;
    private Thread mThread;
    private Paint mPaint;
    private Paint mLinePaint;
    private int mBackColor;
    private int mPointColor;
    private ARGBColor mLineColor;

    public int getBackColor() {
        return mBackColor;
    }

    public void setBackColor(int backColor) {
        this.mBackColor = backColor;
    }

    public int getLineColor() {
        return mLineColor.get();
    }

    public void setLineColor(int lineColor) {
        this.mLineColor = new ARGBColor(lineColor);
    }

    public int getPointColor() {
        return mPointColor;
    }

    public void setPointColor(int pointColor) {
        this.mPointColor = pointColor;
    }

    public NLXView(Context context) {
        this(context, null, 0);
    }

    public NLXView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NLXView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        float scale = DensityUtils.getScale(mContext);
        mX = 0;
        mY = 0;
        mW = DensityUtils.dip2px(scale, DP_WIDTH);
        mH = DensityUtils.dip2px(scale, DP_HEIGHT);
        mRadius = DensityUtils.dip2px(scale, DP_RADIUS);
        mLineWidth = DensityUtils.dip2px(scale, DP_LINE_WIDTH);
        mDist = DensityUtils.dip2px(scale, DP_MAX_LINE_DIST);
        mMaxSpeed = 20;
        mCnt = PARTICLE_COUNT;
        mBackColor = 0xFF202632;
        mPointColor = 0xFFFFFFFF;
        mLineColor = new ARGBColor(0x969696);
        initView();
    }

    private synchronized void initView() {
        mUpdating = true;
        Random random = new Random();
        particles = new ArrayList<>();
        int upThreshold = mMaxSpeed * 2 + 1;
        Particle.init(mX, mY, mW, mH);
        for (int i = 0; i < mCnt; i++) {
            float x = random.nextInt(mW) + mX;
            float y = random.nextInt(mH) + mY;
            float vx = (float) (Math.floor(random.nextInt(upThreshold)) - mMaxSpeed);
            float vy = (float) (Math.floor(random.nextInt(upThreshold)) - mMaxSpeed);
            particles.add(new Particle(x, y, mRadius, mPointColor, vx, vy));
        }
        mPaint = new Paint();
        mPaint.setColor(mBackColor);
        mLinePaint = new Paint();
        mLinePaint.setStrokeWidth(mLineWidth);
        mUpdating = false;
    }

    public void setRect(int left, int top, int width, int height) {
        float scale = DensityUtils.getScale(mContext);
        mX = DensityUtils.dip2px(scale, left);
        mY = DensityUtils.dip2px(scale, top);
        mW = DensityUtils.dip2px(scale, width);
        mH = DensityUtils.dip2px(scale, height);
        initView();
    }

    public void setMaxSpeed(int maxSpeed) {
        int preSpeed = this.mMaxSpeed;
        float scale = maxSpeed * 1f / preSpeed;
        for (int i = 0; i < particles.size(); i++) {
            Particle p = particles.get(i);
            p.setVx(scale * p.getVx());
            p.setVy(scale * p.getVy());
        }
        this.mMaxSpeed = maxSpeed;
    }

    public void setCount(int count) {
        if (count <= 0) {
            count = PARTICLE_COUNT;
        }
        if(this.mCnt != count) {
            this.mCnt = count;
            initView();
        }
    }

    public void setMaxDist(int dist) {
        if (dist <= 0) {
            dist = DP_MAX_LINE_DIST;
        } else if (dist > mW || dist > mH) {
            dist = Math.min(mW, mH);
        }
        if (this.mDist != dist) {
            this.mDist = dist;
            initView();
        }
    }

    public void refresh() {
        initView();
        postInvalidate();
    }

    public void start() {
        mRunning = true;
//        if (mThread != null && mThread.isAlive()) {
//            mThread.interrupt();
//        }
        mThread = new Thread(this);
        mThread.start();
    }

    public void stop() {
        mRunning = false;
//        if (mThread != null && mThread.isAlive()) {
//            mThread.interrupt();
//        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    public void run() {
        try {
            Log.d(TAG, "start running");
            while (mRunning) {
                tick();
                postInvalidate();
                Thread.sleep(INTERVAL);
            }
            Log.d(TAG, "stop running");
        } catch (Exception ex) {
//            ex.printStackTrace();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        canvas.drawRect(new Rect(0, 0, mW, mH), mPaint);
        int cnt = particles.size();
//        Log.d(TAG, "onDraw");
        for (int i = 0; i < cnt; i++) {
            Particle p1 = particles.get(i);
            for (int j = i + 1; j < cnt; j++) {
                Particle p2 = particles.get(j);
                float lineAlpha = distance(p1.getX(), p1.getY(), p2.getX(), p2.getY()) / mDist;
                if (lineAlpha > 1) {
                    continue;
                }
//                Log.d(TAG, "onDraw i=" + i + ",j=" + j + ", lineAlpha=" + lineAlpha);
                mLinePaint.setColor(Color.argb((int) (255 * (1 - lineAlpha)), mLineColor.r, mLineColor.g, mLineColor.b));
                canvas.drawLine(p1.getX(), p1.getY(), p2.getX(), p2.getY(), mLinePaint);
            }
            p1.draw(canvas);
        }
    }

    private void tick() {
        for (int i = 0; i < particles.size() && !mUpdating; i++) {
            particles.get(i).tick();
        }
    }

    private float distance(float x1, float y1, float x2, float y2) {
        float dx = x2 - x1;
        float dy = y2 - y1;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }
}
