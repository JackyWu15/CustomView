package com.hechuangwu.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by cwh on 2019/4/8.
 * 功能:
 */
public class CircleView extends View {
    private int mColor = Color.GREEN;
    private Paint mPaint = new Paint( Paint.ANTI_ALIAS_FLAG );
    public CircleView(Context context) {
        super( context );
        init();
    }

    public CircleView(Context context,  AttributeSet attrs) {
        this( context, attrs ,0);
    }

    public CircleView(Context context,  AttributeSet attrs, int defStyleAttr) {
        super( context, attrs, defStyleAttr );
        TypedArray typedArray = context.obtainStyledAttributes( attrs, R.styleable.CircleView );
        mColor = typedArray.getColor( R.styleable.CircleView_circle_color, Color.BLUE );
        typedArray.recycle();
        init();
    }
    private void init() {
        mPaint.setColor( mColor );
    }

    //处理wrap_content
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure( widthMeasureSpec, heightMeasureSpec );
        int mode = MeasureSpec.getMode( widthMeasureSpec );
        int size = MeasureSpec.getSize( widthMeasureSpec );
        int mode1 = MeasureSpec.getMode( heightMeasureSpec );
        int size1 = MeasureSpec.getSize( heightMeasureSpec );

        if(mode==MeasureSpec.AT_MOST&&mode1==MeasureSpec.AT_MOST){
            setMeasuredDimension( 100,100 );
        }else if(mode==MeasureSpec.AT_MOST){
            setMeasuredDimension( 100,size );
        }else if(mode1==MeasureSpec.AT_MOST) {
            setMeasuredDimension( size1,100 );
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw( canvas );
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();
        int width = getWidth()-paddingLeft-paddingRight;
        int height = getHeight()-paddingTop-paddingBottom;
        int radius = Math.min( width, height )/2;
        canvas.drawCircle( width/2+paddingLeft,height/2+paddingTop,radius,mPaint );
    }
}
