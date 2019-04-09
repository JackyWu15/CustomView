package com.hechuangwu.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * Created by cwh on 2019/4/8.
 * 功能:
 */
public class HorizontalView extends ViewGroup {
    private Scroller mScroller;
    private VelocityTracker mVelocityTracker;
    private int mLastInterceptX;
    private int mLastInterceptY;
    private int mLastX;
    private int mLastY;
    private int mChildIndex;
    private int mChildWidth;
    private int mChildSize;
    public HorizontalView(Context context) {
        super( context );
    }

    public HorizontalView(Context context, AttributeSet attrs) {
        this( context, attrs, 0 );

    }

    public HorizontalView(Context context, AttributeSet attrs, int defStyleAttr) {
        super( context, attrs, defStyleAttr );
        init();
    }

    private void init() {
        if (mScroller == null) {
            mScroller = new Scroller( getContext() );
            mVelocityTracker = VelocityTracker.obtain();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercept = false;
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                intercept = false;
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                    intercept = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = x - mLastInterceptX;
                int deltaY = y - mLastInterceptY;
                if (Math.abs( deltaX ) - Math.abs( deltaY ) > 0) {
                    intercept = true;
                } else {
                    intercept = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                intercept = false;
                break;
        }

        mLastInterceptX = x;
        mLastInterceptY = y;
        return intercept;

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mVelocityTracker.addMovement( event );
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = x - mLastX;
                int deltaY = y - mLastY;
                scrollBy( -deltaX, 0 );
                break;
            case MotionEvent.ACTION_UP:
                int scrollX = getScrollX();
                mVelocityTracker.computeCurrentVelocity( 1000 );
                float xVelocity = mVelocityTracker.getXVelocity();
                if (Math.abs( xVelocity ) > 50) {
                    mChildIndex = xVelocity > 0 ? mChildIndex - 1 : mChildIndex + 1;
                } else {
                    mChildIndex = (scrollX + mChildWidth / 2) / mChildWidth;
                }
                mChildIndex = Math.max( 0, Math.min( mChildIndex, mChildSize - 1 ) );
                int destX = mChildIndex * mChildWidth - scrollX;
                smoothScrollBy( destX, 0 );
                mVelocityTracker.clear();
                break;
        }
        mLastX = x;
        mLastY = y;
        return true;
    }

    private void smoothScrollBy(int destX, int destY) {
        mScroller.startScroll( getScrollX(), 0, destX, 0, 500 );
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure( widthMeasureSpec, heightMeasureSpec );
        int measureWidth = 0;
        int measureHeight = 0;
        int childCount = getChildCount();
        measureChildren( widthMeasureSpec, heightMeasureSpec );
        int mode = MeasureSpec.getMode( widthMeasureSpec );
        int size = MeasureSpec.getSize( widthMeasureSpec );
        int mode1 = MeasureSpec.getMode( heightMeasureSpec );
        int size1 = MeasureSpec.getSize( heightMeasureSpec );
        if (childCount == 0) {
            setMeasuredDimension( 0, 0 );
        } else if (mode == MeasureSpec.AT_MOST && mode1 == MeasureSpec.AT_MOST) {
            View childAt = getChildAt( 0 );
            measureWidth = childAt.getMeasuredWidth() * childCount;
            measureHeight = childAt.getMeasuredHeight();
            setMeasuredDimension( measureWidth, measureHeight );
        } else if (mode == MeasureSpec.AT_MOST) {
            View childAt = getChildAt( 0 );
            measureWidth = childAt.getMeasuredWidth() * childCount;
            setMeasuredDimension( measureWidth, size1 );
        } else if (mode1 == MeasureSpec.AT_MOST) {
            View childAt = getChildAt( 0 );
            measureHeight = childAt.getMeasuredHeight();
            setMeasuredDimension( size, measureHeight );
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        mChildSize = childCount;
        int childLeft = 0;
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt( i );
            if(childAt.getVisibility()!=View.GONE){
                int width = childAt.getMeasuredWidth();
                mChildWidth = width;
                childAt.layout( childLeft,0,childLeft+width,childAt.getMeasuredHeight() );
                childLeft +=width;
            }
        }
    }

    @Override
    public void computeScroll() {
        if(mScroller.computeScrollOffset()){
            scrollTo( mScroller.getCurrX(),mScroller.getCurrY() );
            postInvalidate();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        mVelocityTracker.recycle();
        super.onDetachedFromWindow();

    }
}
