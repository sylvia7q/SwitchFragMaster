package com.kanban.switchfragmaster.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.kanban.switchfragmaster.R;
import com.kanban.switchfragmaster.utils.ImageUtil;


/**
 * Created by lq on 2017/9/29.
 */

/**
 * 主界面设置小圆点和数字
 */
public class MainRadioButton extends android.support.v7.widget.AppCompatRadioButton {
    private Paint mPaint;
    private boolean isShowDot;
    private boolean isShowNumberDot;

    private int width;
    private int height;

    /**
     * 顶部图片宽
     */
    private int IntrinsicWidth;

    private String numberText;
    /**
     * 圆点和未读消息的坐标
     * */
    private float pivotX;
    private float pivotY;
    /**
     * 圆点半径
     * */
    private final int circleDotRadius = ImageUtil.dp2px(getContext(),3);
    //圆点默认颜色
    private final int PAINT_COLOR_DEFAULT = getResources().getColor(R.color.colorAccent);

    public MainRadioButton(Context context) {
        this(context, null);
    }

    public MainRadioButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MainRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        Drawable drawable = getCompoundDrawables()[1];
        if (drawable != null) {
            IntrinsicWidth = drawable.getIntrinsicWidth();
        }
        /**
         * 给RadioButton增加一定的padding
         * */
        if (getPaddingBottom() == 0) {
            setPadding(ImageUtil.dp2px(getContext(),10),ImageUtil.dp2px(getContext(),10),ImageUtil.dp2px(getContext(),10),ImageUtil.dp2px(getContext(),10));
        }
    }

    @Override
    public void setCompoundDrawables(@Nullable Drawable left, @Nullable Drawable top, @Nullable Drawable right, @Nullable Drawable bottom) {
        super.setCompoundDrawables(left, top, right, bottom);
        Drawable drawable = getCompoundDrawables()[1];
        if (drawable != null) {
            IntrinsicWidth = drawable.getIntrinsicWidth();
        }
        /**
         * 给RadioButton增加一定的padding
         * */
        if (getPaddingBottom() == 0) {
            setPadding(ImageUtil.dp2px(getContext(),10),ImageUtil.dp2px(getContext(),10),ImageUtil.dp2px(getContext(),10),ImageUtil.dp2px(getContext(),10));
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(width / 2, height / 2);
        mPaint = new Paint();
        mPaint.setStrokeWidth(2);
        mPaint.setAntiAlias(true);
        mPaint.setColor(PAINT_COLOR_DEFAULT);
        mPaint.setTextSize(16f);
        pivotX = (float) (IntrinsicWidth / 2 > width / 2 ? width / 2 : IntrinsicWidth / 2) + circleDotRadius;
        pivotY = (float) (height / 2 * 0.8);
        if (isShowDot) {
            canvas.drawCircle(pivotX, -pivotY, circleDotRadius, mPaint);
        } else if (isShowNumberDot && !TextUtils.isEmpty(numberText)) {
            float textWidth = mPaint.measureText(numberText);
            Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
            float textHeight = Math.abs((fontMetrics.top + fontMetrics.bottom));
            /**
             * 数字左右增加一定的边距
             * */
            RectF rectF = new RectF(pivotX- ImageUtil.dp2px(getContext(),4), -pivotY-textHeight/2-ImageUtil.dp2px(getContext(),2), pivotX + textWidth+ImageUtil.dp2px(getContext(),4), -pivotY+textHeight);
            canvas.drawRoundRect(rectF,ImageUtil.dp2px(getContext(),6),ImageUtil.dp2px(getContext(),6), mPaint);
            mPaint.setColor(Color.parseColor("#ffffff"));
            canvas.drawText(numberText, pivotX, -pivotY + textHeight / 2, mPaint);
        }
    }
    /**
     * `
     * 设置是否显示小圆点
     */
    public void setShowSmallDot(boolean isShowDot) {
        this.isShowDot = isShowDot;
        invalidate();
    }

    /**
     * 设置是否显示数字
     */
    public void setNumberDot(boolean isShowNumberDot, @NonNull String text) {
        this.isShowNumberDot = isShowNumberDot;
        this.numberText = text;
        if (isShowNumberDot) {
            isShowDot = false;
        }
        invalidate();
    }
}
