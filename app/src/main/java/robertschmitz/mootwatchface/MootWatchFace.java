/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package robertschmitz.mootwatchface;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.wearable.watchface.CanvasWatchFaceService;
import android.support.wearable.watchface.WatchFaceStyle;
import android.text.format.Time;
import android.view.Gravity;
import android.view.SurfaceHolder;

import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Analog watch face with a ticking second hand. In ambient mode, the second hand isn't shown. On
 * devices with low-bit ambient mode, the hands are drawn without anti-aliasing in ambient mode.
 */
public class MootWatchFace extends AbstractHandbasedWatchFace {

    private int mMarkColor = Color.rgb(144, 144, 144);
    private Paint mMarkBoldPaint;
    private Paint mMarkThinPaint;

    private int mSolidColor = Color.rgb(255, 255, 255);
    private int mSolidAmbientColor = Color.rgb(200, 200, 200);

    private Paint mBackgroundPaint;

    private Paint mSolidPaint;

    private Paint mHandHourPaint;
    private Paint mHandHourAmbientPaint;

    private Paint mHandMinPaint;
    private Paint mHandMinAmbientPaint;

    private Paint mHandSecPaint;

    private Paint mCenterPaint;

    private int mCenterAmbientColor = Color.rgb(200, 200, 200);
    private Paint mCenterAmbientPaint;

    @Override
    public void onCreate(Resources resources) {
        super.onCreate(resources);

        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(Color.BLACK);

        mSolidPaint = new Paint();
        mSolidPaint.setColor(mSolidColor);
        mSolidPaint.setAntiAlias(true);
        mSolidPaint.setStrokeCap(Paint.Cap.SQUARE);

        mHandHourPaint = new Paint(mSolidPaint);
        mHandHourPaint.setStrokeWidth(7);

        mHandHourAmbientPaint = new Paint(mHandHourPaint);
        mHandHourAmbientPaint.setColor(mSolidAmbientColor);

        mHandMinPaint = new Paint(mSolidPaint);
        mHandMinPaint.setStrokeWidth(5);

        mHandMinAmbientPaint = new Paint(mHandMinPaint);
        mHandMinAmbientPaint.setColor(mSolidAmbientColor);

        mHandSecPaint = new Paint();
        mHandSecPaint.setAntiAlias(true);
        mHandSecPaint.setColor(Color.RED);
        mHandSecPaint.setStrokeCap(Paint.Cap.SQUARE);
        mHandSecPaint.setStrokeWidth(2);

        mCenterPaint = new Paint();
        mCenterPaint.setColor(mSolidColor);
        mCenterPaint.setStyle(Paint.Style.FILL);
        mCenterPaint.setAntiAlias(true);

        mCenterAmbientPaint = new Paint();
        mCenterAmbientPaint.setColor(mCenterAmbientColor);
        mCenterAmbientPaint.setStyle(Paint.Style.STROKE);
        mCenterAmbientPaint.setStrokeWidth(1);
        mCenterAmbientPaint.setAntiAlias(true);

        mMarkBoldPaint = new Paint();
        mMarkBoldPaint.setColor(mMarkColor);
        mMarkBoldPaint.setAntiAlias(true);
        mMarkBoldPaint.setStrokeCap(Paint.Cap.SQUARE);
        mMarkBoldPaint.setStrokeWidth(4);

        mMarkThinPaint = new Paint(mMarkBoldPaint);
        mMarkThinPaint.setStrokeWidth(2);
    }

    @Override
    protected void drawMarks(Canvas canvas, Rect bounds) {

        for(int i=0; i<12; i++) {
            float deg = (i/12f) * TWO_PI;
            float vx = (float) Math.sin(deg);
            float vy = (float) -Math.cos(deg);

            float stop = 160;
            float start = 120;

            if (i % 3 == 0) {
                canvas.drawLine(vx * start, vy * start, vx * stop, vy * stop, mMarkBoldPaint);
            } else {
                canvas.drawLine(vx * start, vy * start, vx * stop, vy * stop, mMarkThinPaint);
            }

        }

    }

    @Override
    protected void drawBackground(Canvas canvas, Rect bounds) {
        canvas.drawRect(bounds, mBackgroundPaint);
    }

    @Override
    protected void drawHandHour(Canvas canvas) {
        Paint paint = (mAmbientMode ? mHandHourAmbientPaint : mHandHourPaint);
        canvas.drawLine(0, -13.5f, 0, -96.5f, paint);
    }

    @Override
    protected void drawHandMin(Canvas canvas) {
        Paint paint = (mAmbientMode ? mHandMinAmbientPaint : mHandMinPaint);
        canvas.drawLine(0, -12.5f, 0, -117.5f, paint);
    }

    @Override
    protected void drawHandSec(Canvas canvas) {
        canvas.drawLine(0, -11, 0, -119, mHandSecPaint);
    }

    @Override
    protected void drawCenter(Canvas canvas) {
        if (!mAmbientMode) {
            Paint paint = (mAmbientMode ? mCenterAmbientPaint : mCenterPaint);
            canvas.drawCircle(0, 0, 5, paint);
        }
    }

    @Override
    protected int getStatusBarGravity() {
        return Gravity.TOP | Gravity.CENTER_HORIZONTAL;
    }
}
