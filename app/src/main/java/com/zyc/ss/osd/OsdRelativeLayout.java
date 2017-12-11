package com.zyc.ss.osd;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

/**
 * Created by Administrator on 2017/12/10.
 */

public class OsdRelativeLayout extends RelativeLayout {
    private String tag = getClass().getSimpleName();
    public PopupWindow popupWindow;
    public int popupWindowMargin = 20;
    private float lastX;
    private float lastY;
    private boolean hasMove;
    private int popHeight;
    private int popWidth;

    public OsdRelativeLayout(Context context) {
        super(context);
    }

    public OsdRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OsdRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public OsdRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void addView(final View child) {
        if (child instanceof OsdWarpView) {
            Log.e("OsdRelativeLayout", "is OsdWarpView");
            child.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    float currX = event.getX();
                    float currY = event.getY();
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_DOWN:
                            Log.e("action", "ACTION_DOWN");
                            lastX = currX;
                            lastY = currY;
                            return true;
                        case MotionEvent.ACTION_CANCEL:
                            Log.e("action", "ACTION_CANCEL");
                            break;
                        case MotionEvent.ACTION_MOVE:
                            Log.e(tag, v.getLeft() + ":::" + v.getTop());
                            hasMove = true;
                            float dx = currX - lastX;
                            float dy = currY - lastY;
                            v.layout(v.getLeft() + (int) dx, v.getTop() + (int) dy, v.getRight() + (int) dx, v.getBottom() + (int) dy);
                            if (popupWindow != null && popupWindow.isShowing()) {
                                popupWindow.update(v, (v.getWidth() - popWidth) / 2, getOffSetY(v), popupWindow.getWidth(), popupWindow.getHeight());
                            }
                            break;
                        case MotionEvent.ACTION_UP:
                            if (!hasMove) {
                                v.performClick();
                                showPopWindow(v);
                            }
                            hasMove = false;
                            Log.e("action", "ACTION_UP");
                            break;

                    }

                    return false;
                }
            });
        } else {
            Log.e("OsdRelativeLayout", "add view not OsdWarpView");
        }
        super.addView(child);
    }

    private void showPopWindow(View v) {
        dismissPopWindow();
        LinearLayout layout = (LinearLayout) (View.inflate(getContext(), R.layout.window_popup, null));
        layout.measure(0, 0);
        popupWindow = new PopupWindow(layout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popHeight = layout.getMeasuredHeight();
        popWidth = layout.getMeasuredWidth();
        Log.e(tag, "popHeight:" + popHeight + ":::popWidth" + popWidth);
        int offSetY = getOffSetY(v);
        popupWindow.showAsDropDown(v, (v.getWidth() - popWidth) / 2, offSetY);
    }

    private int getOffSetY(View v) {
        int minYSpace = popHeight + popupWindowMargin;
        return v.getTop() >= minYSpace ? -(popHeight + v.getHeight() + popupWindowMargin) : popupWindowMargin;
    }

    private void dismissPopWindow() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                dismissPopWindow();
                break;
            case MotionEvent.ACTION_MOVE:
                hasMove = true;
                break;
            case MotionEvent.ACTION_UP:
                if (!hasMove) {
                    performClick();
                }
                hasMove = false;
                break;
        }
        return super.onTouchEvent(event);
    }
}
