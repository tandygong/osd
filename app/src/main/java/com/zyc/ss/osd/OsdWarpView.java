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
    public String tag = "OsdWarpView@" + Integer.toHexString(this.hashCode());
    PopupWindow popupWindow;
    int popupWindowMargin = 20;
    private boolean hasMove;
    private float startX;
    private float startY;
    public static final int START_DRAG_MIN_DISTANCE = 60;
    private int popWidth;
    private int popHeight;
    public final static int MODE_FORBID_MOVE = 1;
    public final static int MODE_EDIT = 2;
    private int mode = 0;
    private boolean interceptChildEvent = false;
    private boolean hasBringToTop = false;


    public OsdWarpView(Context context) {
        super(context);
        setBackgroundResource(R.drawable.warp_view_bg);
        setWillNotDraw(false);
    }

    public void setInterceptChildEvent(boolean interceptChildEvent) {
        this.interceptChildEvent = interceptChildEvent;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        RelativeLayout.LayoutParams layoutParams = null;
        float currX = event.getX();
        float currY = event.getY();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                Log.e(tag, "ACTION_DOWN");
                setStartPoint(currX, currY);
                return true;
            case MotionEvent.ACTION_CANCEL:
                Log.e(tag, "ACTION_CANCEL");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e(tag, "ACTION_MOVE");
                if (mode == MODE_FORBID_MOVE) {
                    return false;
                }
                if (!hasMove) {
                    double distance = Math.sqrt(Math.pow(startX - currX, 2) + Math.pow(startY - currY, 2));
                    //  Log.e("distance", distance + "");
                    if (distance > START_DRAG_MIN_DISTANCE) {
                        hasMove = true;
                    }
                }
                if (hasMove) {
                    //  Log.e(tag, getLeft() + ":::" + getTop());
                    float dx = currX - startX;
                    float dy = currY - startY;
                    //     Log.e(tag, "dx" + dx);
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
                    //  Log.e(tag, "left " + left + " top" + top);
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
                interceptChildEvent = false;
                if (!hasMove) {
                    performClick();
                    showPopupWindow();
                }
                setAlpha(1.0f);
                hasMove = false;

                Log.e(tag, "ACTION_UP");
                break;

        }
        return super.onTouchEvent(event);
    }

    private void setStartPoint(float currX, float currY) {
        Log.e(tag, "setStartPoint");
        startX = currX;
        startY = currY;
    }


    private void bringSelfToTop() {
        LayoutParams layoutParams;
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
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                hasBringToTop = false;
                Log.e(tag, "onInterceptTouchEvent:" + "ACTION_DOWN");
                setStartPoint(ev.getX(), ev.getY());
                return false;

            case MotionEvent.ACTION_MOVE:   //表示父类需要
                Log.e(tag, "onInterceptTouchEvent:" + "ACTION_MOVE");
                return true;
            case MotionEvent.ACTION_UP:
                Log.e(tag, "onInterceptTouchEvent:" + "ACTION_UP");
                return false;
            default:
                break;
        }


        return false;    //如果设置拦截，除了down,其他都是父类处理
    }


    public void showPopupWindow() {
        requestCloseAllWindow();
        LinearLayout layout = (LinearLayout) (View.inflate(getContext(), R.layout.window_popup, null));
        layout.measure(0, 0);
        popupWindow = new PopupWindow(layout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popHeight = layout.getMeasuredHeight();
        popWidth = layout.getMeasuredWidth();
        Log.e(tag, "popHeight:" + popHeight + ":::popWidth" + popWidth);
        int offSetY = getOffSetY();
        popupWindow.showAsDropDown(this, (getWidth() - popWidth) / 2, offSetY);
        layout.findViewById(R.id.lock_self).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mode == MODE_FORBID_MOVE) {
                    mode = 0;
                } else {
                    mode = MODE_FORBID_MOVE;
                }
            }
        });
        setSelected(true);

    }

    @Override
    public void dispatchSetSelected(boolean selected) {
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
        setSelected(false);
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
                }else{
                    child.clearFocus();
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

    public void setMode(int mode) {
        this.mode = mode;
    }
}
