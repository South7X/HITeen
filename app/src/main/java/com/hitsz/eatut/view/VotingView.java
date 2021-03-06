package com.hitsz.eatut.view;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import com.hitsz.eatut.R;

public class VotingView extends View{
    /**
     * 左边的数量
     */
    private int leftNum;
    /**
     * 左边结束色
     */
    private int leftEndColor;
    /**
     * 左边开始色
     */
    private int leftStartColor;
    /**
     * 右边的数量
     */
    private int rightNum;
    /**
     * 右边开始色
     */
    private int rightStartColor;
    /**
     * 右边结束色
     */
    private int rightEndColor;
    /**
     * 白线倾斜度
     */
    private int mInclination;
    /**
     * 左边字体颜色
     */
    private int textColor;
    /**
     * 字体大小
     */
    private int textSize;
    /**
     * 左边边框距离
     */

    private Paint mPaint;
    private Path mPath;

    /**
     * 包含文字的框
     */
    private Rect mBound;

    /**
     * 状态
     */
    private int clickMode;

    public VotingView(Context context) {
        this(context, null);
    }

    public VotingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VotingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.VotingView, defStyleAttr, 0);
        int n = typedArray.getIndexCount();

        for (int i = 0; i < n; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.VotingView_leftNum:
                    leftNum = typedArray.getInteger(attr, 0);
                    break;
                case R.styleable.VotingView_leftStartColor:
                    leftStartColor = typedArray.getColor(attr, Color.parseColor("#FF7566"));
                    break;
                case R.styleable.VotingView_leftEndColor:
                    leftEndColor = typedArray.getColor(attr, Color.parseColor("#FFBD8D"));
                    break;
                case R.styleable.VotingView_rightNum:
                    rightNum = typedArray.getInteger(attr, 0);
                    break;
                case R.styleable.VotingView_rightStartColor:
                    rightStartColor = typedArray.getColor(attr, Color.parseColor("#13CCFF"));
                    break;
                case R.styleable.VotingView_rightEndColor:
                    rightEndColor = typedArray.getColor(attr, Color.parseColor("#0091FF"));
                    break;
                case R.styleable.VotingView_inclination:
                    mInclination = typedArray.getInteger(attr, 40);
                    break;
                case R.styleable.VotingView_textColor:
                    textColor = typedArray.getColor(attr, Color.WHITE);
                    break;
                case R.styleable.VotingView_textSize:
                    textSize = typedArray.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                default:
                    break;
            }
        }

        typedArray.recycle();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(5);
        mPath = new Path();
        mBound = new Rect();
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            width = getPaddingLeft() + getWidth() + getPaddingRight();
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = getPaddingTop() + getHeight() + getPaddingBottom();
        }

        setMeasuredDimension(width, height);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float leftLength;
        float rightLength;
        //一边为0人时的最短距路
        float minLength = 150 + getHeight();
        if (leftNum != 0 && rightNum != 0) {
            //都不为零看比例
            leftLength = minLength + ((float) leftNum / (leftNum + rightNum)) * (getWidth() - 2 * minLength);
        } else {
            if (leftNum == 0) {
                if (rightNum == 0) {
                    //左右都为0 各占一半
                    leftLength = getWidth() / 2;
                } else {
                    //左边为0右边不为0
                    leftLength = minLength;
                }
            } else {
                //左边不为0右边为0
                rightLength = minLength;
                leftLength = getWidth() - rightLength;
            }
        }

        RectF leftOval;
        switch (clickMode){     //绘制左边
            case 1:     //点击左边
                //画半圆
                leftOval = new RectF(0, 0, getHeight(), getHeight());
                mPath.moveTo(getHeight() / 2, getHeight() / 2);
                mPath.arcTo(leftOval, 90, 180);
                //画矩形
                mPath.moveTo(getHeight() / 2 - 5, 0);
                mPath.lineTo(leftLength + mInclination - 10, 0);
                mPath.lineTo(leftLength - mInclination - 10, getHeight());
                mPath.lineTo(getHeight() / 2 - 5, getHeight());

                break;
            case 2:     //点击右边
            case 3:
                //画半圆
                leftOval = new RectF(0, 10, getHeight() - 10, getHeight() - 10);
                mPath.moveTo(getHeight() / 2 - 5, getHeight() / 2);
                mPath.arcTo(leftOval, 90, 180);
                //画矩形
                mPath.moveTo(getHeight() / 2 - 5, 10);
                mPath.lineTo(leftLength + mInclination - 10, 10);
                mPath.lineTo(leftLength - mInclination - 10, getHeight() - 10);
                mPath.lineTo(getHeight() / 2 - 5, getHeight() - 10);
                break;
            case 0:     //未点击
            default:
                //画半圆
                leftOval = new RectF(0, 10, getHeight() - 10, getHeight() - 10);
                mPath.moveTo(getHeight() / 2 - 5, getHeight() / 2);
                mPath.arcTo(leftOval, 90, 180);
                //画矩形
                mPath.moveTo(getHeight() / 2 - 5, 10);
                mPath.lineTo(getWidth() / 2 - 10, 10);
                mPath.lineTo(getWidth() / 2 - 10, getHeight() - 10);
                mPath.lineTo(getHeight() / 2 - 5, getHeight() - 10);
                break;
        }

        //渐变色
        Shader leftShader = new LinearGradient(0, 0, leftLength + mInclination - 10, 0, leftStartColor, leftEndColor, Shader.TileMode.CLAMP);
        mPaint.setShader(leftShader);

        //左边布局绘制完毕
        canvas.drawPath(mPath, mPaint);
        mPaint.setShader(null);
        mPath.reset();

        if (clickMode == 0){
            //画中间白线
            mPath.moveTo(getWidth() / 2 - 10, 0);
            mPath.lineTo(getWidth() / 2 + 10, 0);
            mPath.lineTo(getWidth() / 2 + 10, getHeight());
            mPath.lineTo(getWidth() / 2 - 10, getHeight());
        }
        else {
            //画中间白线
            mPath.moveTo(leftLength + mInclination - 10, 0);
            mPath.lineTo(leftLength + mInclination + 10, 0);
            mPath.lineTo(leftLength - mInclination + 10, getHeight());
            mPath.lineTo(leftLength - mInclination - 10, getHeight());
        }
        mPath.close();
        mPaint.setColor(Color.WHITE);
        canvas.drawPath(mPath, mPaint);
        mPath.reset();

        //画右边半圆
        RectF rightOval;
        switch (clickMode){     //绘制左边
            case 2:     //点击右边
                //画右边半圆
                rightOval = new RectF(getWidth() - getHeight(), 0, getWidth(), getHeight());
                mPath.moveTo(getWidth() - getHeight() / 2, getHeight() / 2);
                mPath.arcTo(rightOval, -90, 180);
                //画右边矩形
                mPath.moveTo(leftLength + mInclination + 10, 0);
                mPath.lineTo(getWidth() - getHeight() / 2, 0);
                mPath.lineTo(getWidth() - getHeight() / 2, getHeight());
                mPath.lineTo(leftLength - mInclination + 10, getHeight());
            case 1:     //点击左边
            case 3:
                rightOval = new RectF(getWidth() - getHeight(), 10, getWidth() - 10, getHeight() - 10);
                mPath.moveTo(getWidth() - getHeight() / 2 - 5, getHeight() / 2);
                mPath.arcTo(rightOval, -90, 180);
                //画右边矩形
                mPath.moveTo(leftLength + mInclination + 10, 10);
                mPath.lineTo(getWidth() - getHeight() / 2 - 5, 10);
                mPath.lineTo(getWidth() - getHeight() / 2 - 5, getHeight() - 10);
                mPath.lineTo(leftLength - mInclination + 10, getHeight() - 10);
                break;
            case 0:     //未点击
            default:
                //画右边半圆
                rightOval = new RectF(getWidth() - getHeight(), 10, getWidth() - 10, getHeight() - 10);
                mPath.moveTo(getWidth() - getHeight() / 2 - 5, getHeight() / 2);
                mPath.arcTo(rightOval, -90, 180);
                //画右边矩形
                mPath.moveTo(getWidth() / 2 + 10, 10);
                mPath.lineTo(getWidth() - getHeight() / 2 - 5, 10);
                mPath.lineTo(getWidth() - getHeight() / 2 - 5, getHeight()-10);
                mPath.lineTo(getWidth() / 2+ 10, getHeight() - 10);
                break;
        }

        //渐变色
        Shader rightShader = new LinearGradient(leftLength + mInclination + 10,getHeight(), getWidth(), getHeight(), rightStartColor, rightEndColor, Shader.TileMode.CLAMP);
        mPaint.setShader(rightShader);

        //右边布局绘制完毕
        canvas.drawPath(mPath, mPaint);
        mPaint.setShader(null);

        String rightText = String.valueOf(rightNum);
        String leftText = String.valueOf(leftNum);
        mPaint.getTextBounds(leftText, 0, leftText.length(), mBound);
        mPaint.setColor(textColor);
        mPaint.setTextSize(textSize);
        mPaint.setTextAlign(Paint.Align.LEFT);

        switch (clickMode){
            case 1:
            case 2:
            case 3:
                //左边
                canvas.drawText(leftText, getHeight() / 2 + 6, getHeight() / 2 - 24 + mBound.height() / 2, mPaint);
                canvas.drawText("支持", getHeight() / 2 + 6, getHeight() / 2 + 20 + mBound.height() / 2, mPaint);
                break;
            case 0:
            default:
                canvas.drawText("支持", getHeight() / 2 + 6, getHeight() / 2 + 12, mPaint);
        }

        mPaint.setColor(textColor);
        mPaint.getTextBounds(rightText, 0, rightText.length(), mBound);
        mPaint.setTextAlign(Paint.Align.RIGHT);

        switch (clickMode){
            case 1:
            case 2:
            case 3:
                //右边文字
                canvas.drawText(rightText, getWidth() - getHeight() / 2 - 6, getHeight() / 2 - 30 + mBound.height() / 2, mPaint);
                canvas.drawText("不支持", getWidth() - getHeight() / 2 - 6, getHeight() / 2 + 14 + mBound.height() / 2, mPaint);
                break;
            case 0:
            default:
                canvas.drawText("不支持", getWidth() - getHeight() / 2 - 6, getHeight() / 2 + 12, mPaint);
                break;
        }

    }

    /**
     * 动态设置
     *
     * @param leftNum  左边
     * @param rightNum 右边
     */
    public void setNum(int leftNum, int rightNum) {
        this.leftNum = leftNum;
        this.rightNum = rightNum;
        postInvalidate();
    }
    public void setClickMode(int clickMode){
        this.clickMode = clickMode;
        mPath.reset();
        mPaint.reset();
    }
}
