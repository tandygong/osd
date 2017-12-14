package com.zyc.ss.osd;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

/**
 * Created by Administrator on 2017/12/10.
 */

public class OsdWarpView extends RelativeLayout {
    private String tag = "OsdWarpView";
    PopupWindow popupWindow;
    int popupWindowMargin = 20;
    private boolean hasMove;
    private float startX;
    private float startY;
    public static final int START_DRAG_MIN_DISTANCE = 60;
    private int popWidth;
    private int popHeight;


    public OsdWarpView(Context context) {
        super(context);
        setWillNotDraw(false);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        RelativeLayout.LayoutParams layoutParams = null;
        float currX = event.getX();
        float currY = event.getY();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                Log.e("action", "ACTION_DOWN");
                layoutParams = (LayoutParams) getLayoutParams();
                int leftMargin = getLeft();
                int topMargin = getTop();
                ViewParent parent = getParent();
                if (parent != null) {
                    ViewGroup viewGroup = (ViewGroup) parent;
                    viewGroup.removeView(this);
                    layoutParams.leftMargin = leftMargin;
                    layoutParams.topMargin = topMargin;
                    viewGroup.addView(this, layoutParams);
                }
                startX = currX;
                startY = currY;
                return true;
            case MotionEvent.ACTION_CANCEL:
                Log.e("action", "ACTION_CANCEL");
                break;
            case MotionEvent.ACTION_MOVE:
                if (!hasMove) {
                    double distance = Math.sqrt(Math.pow(startX - currX, 2) + Math.pow(startY - currY, 2));
                    Log.e("distance", distance + "");
                    if (distance > START_DRAG_MIN_DISTANCE) {
                        hasMove = true;
                    }
                }
                if (hasMove) {
                    Log.e(tag, getLeft() + ":::" + getTop());
                    float dx = currX - startX;
                    float dy = currY - startY;
                    Log.e(tag, "dx" + dx);
                    int left = getLeft() + (int) dx;
                    int top = getTop() + (int) dy;
                    int right = getRight() + (int) dx;
                    int bottom = getBottom() + (int) dy;
                    int parentWidth = ((View) getParent()).getWidth();
                    int parentHeight = ((View) getParent()).getHeight();
                    if (left < 0) {
                        left = 0;
                        right = getWidth();
                    } else if (right > parentWidth) {
                        right = getWidth();
                        left = parentWidth - getWidth();
                    }
                    if (top < 0) {
                        top = 0;
                        bottom = getHeight();
                    } else if (bottom > parentHeight) {
                        bottom = getHeight();
                        top = parentHeight - getHeight();
                    }
                    layoutParams = (LayoutParams) getLayoutParams();
                    layoutParams.setMargins(left, top, 0, 0);
                    //layout(left, top, right, bottom);
                    Log.e(tag, "left " + left + " top" + top);
                    setLayoutParams(layoutParams);
                    setAlpha(0.5f);
                    if (popupWindow != null && popupWindow.isShowing()) {
                        popupWindow.update(this, (getWidth() - popWidth) / 2, getOffSetY(), popupWindow.getWidth(), popupWindow.getHeight());
                    } else {
                        requestCloseAllWindow();
                    }

                }
                break;
            case MotionEvent.ACTION_UP:
                if (!hasMove) {
                    performClick();
                    showPopupWindow();
                }
                setAlpha(1.0f);
                hasMove = false;

                Log.e("action", "ACTION_UP");
                break;

        }
        return super.onTouchEvent(event);
    }

    private void showPopupWindow() {
        requestCloseAllWindow();
        LinearLayout layout = (LinearLayout) (View.inflate(getContext(), R.layout.window_popup, null));
        layout.measure(0, 0);
        popupWindow = new PopupWindow(layout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popHeight = layout.getMeasuredHeight();
        popWidth = layout.getMeasuredWidth();
        Log.e("OsdWarpView", "popHeight:" + popHeight + ":::popWidth" + popWidth);
        int offSetY = getOffSetY();
        popupWindow.showAsDropDown(this, (getWidth() - popWidth) / 2, offSetY);
    }

    private int getOffSetY() {
        int minYSpace = popHeight + popupWindowMargin;
        return getTop() >= minYSpace ? -(popHeight + getHeight() + popupWindowMargin) : popupWindowMargin;
    }

    private void closeSelfWindow() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            popupWindow = null;
        }
    }

    public void requestCloseAllWindow() {
        ViewParent parent = getRootParent();
        if (parent != null) {
            dispatchCloseAllWindow();
        } else {
            closeSelfAndChildWindow();
        }

    }

    public ViewParent getRootParent() {
        ViewParent parentView = getParent();
        if (parentView != null) {
            ViewParent root = parentView;
            while (parentView.getParent() != null && !(parentView instanceof OsdRelativeLayout)) {
                parentView = parentView.getParent();
                root = parentView;
            }
            return root;
        } else {
            return null;
        }
    }

    private void dispatchCloseAllWindow() {
        ViewParent parent = getRootParent();
        if (parent != null) {
            ViewGroup viewGroup = (ViewGroup) parent;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                if (child instanceof OsdWarpView) {
                    ((OsdWarpView) child).closeSelfAndChildWindow();
                }
            }
        }
    }

    private void closeSelfAndChildWindow() {
        closeSelfWindow();
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child instanceof OsdWarpView) {
                ((OsdWarpView) child).closeSelfAndChildWindow();
            }
        }
    }
}
