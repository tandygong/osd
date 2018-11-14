package com.zyc.ss.osd;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

public class DragEditText extends android.support.v7.widget.AppCompatEditText {
    private int downX;
    private int downY;

    public DragEditText(Context context) {
        super(context);
    }

    public DragEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DragEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getX();
                downY = (int) event.getY();
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                int upX = (int) event.getX() - downX;
                int upY = (int) event.getY() - downY;
                Log.e("DragEditView", "Action_move");
                if (!((View) getParent()).isSelected()) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                if (Math.abs(upX) <= ViewConfiguration.get(getContext()).getScaledTouchSlop() && Math.abs(upY) <= ViewConfiguration.get(getContext()).getScaledTouchSlop()) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                } else {
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
        }
        return super.dispatchTouchEvent(event);
    }

}
