package com.chronometer.presentation.view;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.chronometer.R;

import java.text.DecimalFormat;

public class CustomChronometerView extends ConstraintLayout {

    private final Handler handler = new Handler(Looper.getMainLooper());
    // TextViews for time display
    private AppCompatTextView tvHourTime;
    private AppCompatTextView tvMinuteTime;
    private AppCompatTextView tvSecondTime;
    private AppCompatTextView tvMillisecondsTime;
    private long mBase;
    private boolean mStarted;
    private boolean mRunning;
    private long timeElapsed;
    private final Runnable ticker = new Runnable() {
        @Override
        public void run() {
            if (mRunning) {
                updateText(SystemClock.elapsedRealtime());
                handler.postDelayed(this, 10); // Update every 10ms
            }
        }
    };
    private long pauseTime = 0; // Stores elapsed time when paused

    public CustomChronometerView(Context context) {
        this(context, null);
    }

    public CustomChronometerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomChronometerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        // Inflate the custom layout
        LayoutInflater.from(context).inflate(R.layout.custom_chronometer, this, true);

        // Initialize views
        tvHourTime = findViewById(R.id.tv_hour_time);
        tvMinuteTime = findViewById(R.id.tv_minute_time);
        tvSecondTime = findViewById(R.id.tv_second_time);
        tvMillisecondsTime = findViewById(R.id.tv_milliseconds_time);

        // Set initial time
        mBase = SystemClock.elapsedRealtime();
        updateText(mBase);
    }

    public void start() {
        mStarted = true;
        mBase = SystemClock.elapsedRealtime() - pauseTime; // Resume from saved time
        updateRunning();
    }

    public void stop() {
        mStarted = false;
        pauseTime = SystemClock.elapsedRealtime() - mBase; // Save elapsed time
        updateRunning();
    }

    public void reset() {
        stop();
        mBase = SystemClock.elapsedRealtime();
        pauseTime = 0;
        updateText(mBase);
    }

    private void updateRunning() {
        boolean running = mStarted;
        if (running != mRunning) {
            if (running) {
                handler.post(ticker);
            } else {
                handler.removeCallbacks(ticker);
            }
            mRunning = running;
        }
    }

    private void updateText(long now) {
        timeElapsed = now - mBase;

        DecimalFormat df = new DecimalFormat("00");
        DecimalFormat dfMilli = new DecimalFormat("0");

        // Calculate hours, minutes, seconds, and milliseconds
        int hours = (int) (timeElapsed / (3600 * 1000));
        int remaining = (int) (timeElapsed % (3600 * 1000));

        int minutes = remaining / (60 * 1000);
        remaining = remaining % (60 * 1000);

        int seconds = remaining / 1000;
        int milliseconds = (int) (timeElapsed % 1000) / 100; // Display 2 digits for milliseconds

        // Update the TextViews
        tvHourTime.setText(String.valueOf(hours));
        tvMinuteTime.setText(df.format(minutes));
        tvSecondTime.setText(df.format(seconds));
        tvMillisecondsTime.setText(dfMilli.format(milliseconds));
    }

    public long getTimeElapsed() {
        return timeElapsed;
    }
}