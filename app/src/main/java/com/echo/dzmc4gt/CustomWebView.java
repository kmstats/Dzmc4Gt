package com.echo.dzmc4gt;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;

// CustomWebView.java
public class CustomWebView extends WebView {
    private GestureDetector gestureDetector;

    public CustomWebView(Context context) {
        super(context);
        init();
    }

    public CustomWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                // 判断右滑，条件是水平方向速度足够大且垂直方向速度较小
                if (e1.getX() - e2.getX() > SWIPE_THRESHOLD && Math.abs(e1.getY() - e2.getY()) < SWIPE_THRESHOLD_VERTICAL) {
                    onRightSwipe();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event);
    }

    // 定义一个接口，用于回调右滑事件
    public interface OnRightSwipeListener {
        void onRightSwipe();
    }

    // 声明一个接口实例变量
    private OnRightSwipeListener onRightSwipeListener;

    // 提供一个方法，允许外部设置接口实例
    public void setOnRightSwipeListener(OnRightSwipeListener listener) {
        this.onRightSwipeListener = listener;
    }

    // ...（之前的代码保持不变，包括onRightSwipe方法）

    // 在onRightSwipe方法中调用接口回调
    private void onRightSwipe() {
        if (onRightSwipeListener != null) {
            onRightSwipeListener.onRightSwipe();
        }
    }

    // 你可以根据需要调整这些阈值
    private static final int SWIPE_THRESHOLD = 100;
    private static final int SWIPE_THRESHOLD_VERTICAL = 50;
}
