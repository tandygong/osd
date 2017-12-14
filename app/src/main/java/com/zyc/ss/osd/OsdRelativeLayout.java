package com.zyc.ss.osd;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/12/10.
 */

public class OsdRelativeLayout extends RelativeLayout {
    private String tag = getClass().getSimpleName();
    private DrawerLayout drawerLayout;
    public OsdRelativeLayout(Context context) {
        super(context);
        init();

    }
    private void init() {
        setWillNotDraw(false);
        OnTouchListener touchWarpViewListener = new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                closeDrawLayout();
                return false;
            }
        };

        OsdWarpView osdPanel = new OsdWarpView(getContext());

        TextView textView = new TextView(getContext());
        textView.setText("Hello");



        OsdWarpView warpTextView = new OsdWarpView(getContext());
        warpTextView.setOnTouchListener(touchWarpViewListener);
        warpTextView.addView(textView);
        osdPanel.addView(warpTextView);


        RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        ImageView imageView = new ImageView(getContext());
        imageView.setImageResource(R.mipmap.ic_launcher_round);
        layoutParams1.addRule(RelativeLayout.BELOW, warpTextView.getId());
        layoutParams1.addRule(RelativeLayout.CENTER_HORIZONTAL);
        OsdWarpView osdImageView = new OsdWarpView(getContext());
        osdImageView.addView(imageView);
        osdImageView.setOnTouchListener(touchWarpViewListener);

        osdPanel.addView(osdImageView);
        osdPanel.setBackgroundColor(0x600000ff);

        osdPanel.setOnTouchListener(touchWarpViewListener );
        addView(osdPanel, 700, 700);
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
    protected void onFinishInflate() {
        drawerLayout = findViewById(R.id.draw_layout);
        drawerLayout.openDrawer(Gravity.LEFT);
        TextView addOsdText = drawerLayout.findViewById(R.id.osd_text);
        TextView addOsdImage = drawerLayout.findViewById(R.id.osd_image);
        addOsdText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(tag, "clickAddOsd");
                closeDrawLayout();
            }
        });
        addOsdImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(tag, "clickAddImage");
                closeDrawLayout();
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
    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean b = super.dispatchTouchEvent(ev);
        if (!b) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    Log.e(tag, "ACTION_DOWN");
                    closeDrawLayout();
                    for (int i = 0; i < getChildCount(); i++) {
                        View childAt = getChildAt(i);
                        if (childAt instanceof OsdWarpView) {
                            ((OsdWarpView) childAt).requestCloseAllWindow();
                            break;
                        }
                    }
                    break;
            }
        }
        return b;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
      /*  switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                Log.e(tag, "ACTION_DOWN");
                closeDrawLayout();
                for (int i = 0; i < getChildCount(); i++) {
                    View childAt = getChildAt(i);
                    if (childAt instanceof OsdWarpView) {
                        ((OsdWarpView) childAt).requestCloseAllWindow();
                        break;
                    }
                }
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
        }*/
        return super.onTouchEvent(event);
    }


    private void closeDrawLayout() {
        if (drawerLayout != null) {
            drawerLayout.closeDrawer(Gravity.LEFT);
        }
    }
}
