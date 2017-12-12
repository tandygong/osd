package com.zyc.ss.osd;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/12/10.
 */

public class OsdRelativeLayout extends RelativeLayout {
    private String tag = getClass().getSimpleName();
    public PopupWindow popupWindow;
    public int popupWindowMargin = 20;
    public static final int START_DRAG_MIN_DISTANCE = 60;

    private boolean hasMove;
    private int popHeight;
    private int popWidth;

    private float mTextView;
    private View touchedView;
    private DrawerLayout drawerLayout;

    public OsdRelativeLayout(Context context) {
        super(context);
        init();

    }

    private void init() {
        setWillNotDraw(false);
    }

    public OsdRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public OsdRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public OsdRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @Override
    public void addView(final View child) {
      /*  if (child instanceof OsdWarpView) {
            Log.e("OsdRelativeLayout", "is OsdWarpView");
            child.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    RelativeLayout.LayoutParams layoutParams = null;
                    float currX = event.getX();
                    float currY = event.getY();
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_DOWN:
                            closeDrawLayout();
                            touchedView = v;
                            Log.e("action", "ACTION_DOWN");
                            layoutParams = (LayoutParams) v.getLayoutParams();
                            int leftMargin = v.getLeft();
                            int topMargin = v.getTop();
                            removeView(v);
                            layoutParams.leftMargin = leftMargin;
                            layoutParams.topMargin = topMargin;
                            addView(v, layoutParams);
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
                                Log.e(tag, v.getLeft() + ":::" + v.getTop());
                                float dx = currX - startX;
                                float dy = currY - startY;
                                int left = v.getLeft() + (int) dx;
                                int top = v.getTop() + (int) dy;
                                int right = v.getRight() + (int) dx;
                                int bottom = v.getBottom() + (int) dy;
                                if (left < 0) {
                                    left = 0;
                                    right = v.getWidth();
                                } else if (right > getWidth()) {
                                    right = getWidth();
                                    left = getWidth() - v.getWidth();
                                }
                                if (top < 0) {
                                    top = 0;
                                    bottom = v.getHeight();
                                } else if (bottom > getHeight()) {
                                    bottom = getHeight();
                                    top = getHeight() - v.getHeight();
                                }
                                layoutParams = (LayoutParams) v.getLayoutParams();
                                layoutParams.setMargins(left, top, 0, 0);
                                v.setLayoutParams(layoutParams);
                                v.setAlpha(0.5f);
                                if (popupWindow != null && popupWindow.isShowing()) {
                                    popupWindow.update(v, (v.getWidth() - popWidth) / 2, getOffSetY(v), popupWindow.getWidth(), popupWindow.getHeight());
                                }
                            }

                            break;
                        case MotionEvent.ACTION_UP:
                            if (!hasMove) {
                                v.performClick();
                                showPopWindow(v);
                            }
                            v.setAlpha(1.0f);
                            hasMove = false;

                            Log.e("action", "ACTION_UP");
                            break;

                    }

                    return false;
                }
            });
        } else {
            Log.e("OsdRelativeLayout", "add view not OsdWarpView");
        }*/
        super.addView(child);
    }

    Paint paint = new Paint();
    Rect rect = new Rect();

    @Override
    protected void onDraw(Canvas canvas) {

        if (touchedView != null) {
            View childAt = getChildAt(1);
            if (childAt != null) {
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                paint.setColor(0x200000ff);
                int centerX = (childAt.getLeft() + childAt.getRight()) / 2;
                int centerY = (childAt.getTop() + childAt.getBottom()) / 2;
                Log.e("centerX", centerX + "");
                rect.set(centerX - 350, centerY - 350, centerX + 350, centerY + 350);
                canvas.drawRect(rect, paint);
            }
        }
        Log.e("draw", "draw");
        super.onDraw(canvas);
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
    protected void onFinishInflate() {
        drawerLayout = findViewById(R.id.draw_layout);
        drawerLayout.openDrawer(Gravity.LEFT);
        TextView addOsdText = drawerLayout.findViewById(R.id.osd_text);
        TextView addOsdImage = drawerLayout.findViewById(R.id.osd_image);
        addOsdText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(tag,"clickAddOsd");
                closeDrawLayout();
            }
        });
        addOsdImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(tag,"clickAddImage");
                closeDrawLayout();
            }
        });

        drawerLayout.setOnTouchListener(new OnTouchListener() {
           @Override
           public boolean onTouch(View v, MotionEvent event) {
               switch (event.getAction()&MotionEvent.ACTION_MASK) {
                   case MotionEvent.ACTION_DOWN:
                       dismissPopWindow();
                       break;
               }
               return false;
           }
       });
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                Log.e("aaa", "onDrawerOpened");
            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        super.onFinishInflate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                Log.e(tag, "ACTION_DOWN");
                dismissPopWindow();
                closeDrawLayout();
                break;
            case MotionEvent.ACTION_MOVE:
                hasMove = true;
                break;
            case MotionEvent.ACTION_UP:
                if (!hasMove) {
                    performClick();
                }
                invalidate();
                hasMove = false;
                break;
        }
        return super.onTouchEvent(event);
    }

    private void closeDrawLayout() {
        if (drawerLayout != null) {
            drawerLayout.closeDrawer(Gravity.LEFT);
        }
    }
}
