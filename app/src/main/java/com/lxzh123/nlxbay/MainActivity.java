package com.lxzh123.nlxbay;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.lxzh123.nlxbay.util.DensityUtils;
import com.lxzh123.nlxbay.view.NLXView;
import com.lxzh123.nlxbay.view.RCRelativeLayout;

public class MainActivity extends AppCompatActivity {

    private Context mContext;
    private RCRelativeLayout layout;
    private NLXView view;
    private SeekBar seekSpeed;
    private SeekBar seekCount;
    private SeekBar seekRadius;
    private Button btnStart;
    private Button btnStop;
    private Button btnRefresh;
    private TextView tvSpeed;
    private TextView tvCount;
    private TextView tvRadius;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        initView();
        initData();
        initAction();
    }

    private void initView() {
        layout = findViewById(R.id.layout_nlx);
        view = findViewById(R.id.nlx_view);

        seekSpeed = findViewById(R.id.seek_speed);
        seekCount = findViewById(R.id.seek_count);
        seekRadius = findViewById(R.id.seek_radius);
        tvSpeed = findViewById(R.id.tv_speed);
        tvCount = findViewById(R.id.tv_count);
        tvRadius = findViewById(R.id.tv_radius);

        btnStart = findViewById(R.id.btn_start);
        btnStop = findViewById(R.id.btn_stop);
        btnRefresh = findViewById(R.id.btn_refresh);
    }

    private void initAction() {
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.start();
                layout.setRadius(DensityUtils.dip2px(mContext, seekRadius.getProgress()));
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.stop();
            }
        });
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.refresh();
            }
        });
        seekSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                view.setMaxSpeed(progress);
                tvSpeed.setText("速度:" + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekCount.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                view.setCount(progress);
                tvCount.setText("粒子数:" + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekRadius.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                layout.setRadius(DensityUtils.dip2px(mContext, seekRadius.getProgress()));
                tvRadius.setText("圆角:" + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void initData() {
        view.setRect(0, 0, 330, 330);
        view.setMaxSpeed(30);
        view.setPointColor(0xFFBBD1FF);
        view.setLineColor(0xBBD1FF);
        view.refresh();
        seekSpeed.setProgress(30);
        layout.setRadius(DensityUtils.dip2px(this, 18));
        tvSpeed.setText("速度:" + seekSpeed.getProgress());
        tvCount.setText("粒子数:" + seekCount.getProgress());
        tvRadius.setText("圆角:" + seekRadius.getProgress());
    }
}
