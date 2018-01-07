package com.adhamenaya.andfiori.ui.visualization.microchart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;

import com.adhamenaya.andfiori.ui.util.Box;

import static com.adhamenaya.andfiori.ui.visualization.microchart.ValueColor.ERROR;
import static com.adhamenaya.andfiori.ui.visualization.microchart.ValueColor.NEUTRAL;


/**
 * Created by Admin on 18/12/2016.
 */

public class DeltaChart extends Chart {

    private ValueColor color;
    private String deltaDisplayValue;
    private float value1;
    private float value2;
    private float deltaValue;
    private String valueTitle1;
    private String valueTitle2;

    Box box1;
    Box box2;
    Box delta;

    public DeltaChart(Context context) {
        super(context);
    }

    @Override
    protected void paintChart(Canvas canvas) {
        canvas.drawRect(box1.x1, box1.y1, box1.x2, box1.y2, getColoredRectPaint(Color.RED));
        canvas.drawRect(delta.x1, delta.y1, delta.x2, delta.y2, getColoredRectPaint(Color.CYAN));
        canvas.drawRect(box2.x1, box2.y1, box2.x2, box2.y2, getColoredRectPaint(Color.GRAY));
    }

    @Override
    protected void prepare() {

        // TODO: How to draw delta chart
        // Find the difference between two values, then draw the box of the difference.
        // The two values will be drawn with regards the difference box, where the
        // negative values will appear on the left, and the positive value will appear on the right

        float deltaValue;
        float maxWidth;
        float space;
        float currentY = 0;

        // 1) Calculate the height of the boxes
        // Calculate space between boxes vertically considering the spaces
        int boxHeight = (int) ((mHeight / 4) * 0.9);

        // Calculate the space between the bars, there is 2 spaces between 3 boxes
        space = (mHeight - (boxHeight * 4)) / 2;

        // 2) Calculate the width of the boxes
        deltaValue = Math.abs(value1 - value2);

        // Compute the width ratio
        maxWidth = Math.max(Math.max(Math.abs(value1), Math.abs(value2)), deltaValue);
        float widthRatio = mWidth / maxWidth;

        Log.d("max width", "" + maxWidth + ":" + widthRatio);

        int box1Width = (int) Math.floor(Math.abs(value1) * widthRatio);
        int box2Width = (int) Math.floor(Math.abs(value2) * widthRatio);
        int boxDeltaWidth = (int) Math.floor(deltaValue * widthRatio);

        int startBox1 = 0;
        int startBox2 = 0;
        int startDeltaBox = 0;

        // Determine the directions of boxes
        if (deltaValue == maxWidth) {
            if (value1 < 0) {
                startBox1 = (int) (mWidth - box1Width);
            } else {
                startBox1 = 0;
            }
            if (value2 < 0) {
                startBox2 = (int) (mWidth - box2Width);
            } else {
                startBox2 = 0;
            }
        } else if (Math.abs(value1) == maxWidth) {
            if (value1 - value2 < 0) {
                startDeltaBox = (int) (mWidth - boxDeltaWidth);
                startBox2 = 0;
            } else {
                startDeltaBox = 0;
                startBox2 = (mWidth - box2Width);
            }
        } else if (value2 == maxWidth) {

        }

        box1 = new Box(startBox1, currentY, startBox1 + box1Width, currentY + boxHeight, NEUTRAL);
        currentY += (space + boxHeight);
        delta = new Box(startDeltaBox, currentY, startDeltaBox + boxDeltaWidth, currentY + boxHeight * 2, ERROR);
        currentY += (space + boxHeight * 2);
        box2 = new Box(startBox2, currentY, startBox2 + box2Width, currentY + boxHeight, NEUTRAL);

      /*  if (value1 < 0 ^ value2 < 0) {
            // Start from left and right
            maxValueWidth = value1 + value2;
            // Box 1
            box1 = new Box(0, currentY, Math.abs(value1), currentY + boxHeight, NEUTRAL);
            currentY += space;
            delta = new Box(Math.abs(value1), currentY, deltaValue, currentY + boxHeight, NEUTRAL);
            currentY += space;
            box2 = new Box(Math.abs(value1) + deltaValue, currentY, Math.abs(value1), currentY + boxHeight, NEUTRAL);

        } else if (value1 < 0 && value2 < 0) {
            // Start from left
            maxValueWidth = Math.max(Math.abs(value1), Math.abs(value2));
        } else {
            // Start from right
            maxValueWidth = Math.max(value1, value2);
        }*/
    }

    @Override
    public void setData(ChartData data) {

    }

    public void setValues(float value1, float value2) {
        this.value1 = value1;
        this.value2 = value2;
        prepare();
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // Get measured height and width of the view
        setMeasuredDimension(getMeasurement(widthMeasureSpec, mWidth),
                getMeasurement(heightMeasureSpec, mHeight));

    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d("---", "onDraw");

        super.onDraw(canvas);
        paintChart(canvas);
    }
}
