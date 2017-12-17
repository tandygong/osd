package com.zyc.ss.osd;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
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

    public OsdRelativeLayout(Context context) {
        super(context);
        init();

    }

    private void init() {
        OsdWarpView osdPanel = new OsdWarpView(getContext());

        TextView textView = new TextView(getContext());
        textView.setCursorVisible(true);
        textView.setHint("你的号大");
       // textView.setText("Hello");


        OsdWarpView warpTextView = new OsdWarpView(getContext());
        warpTextView.addView(textView);
        osdPanel.addView(warpTextView);


        RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        ImageView imageView = new ImageView(getContext());
        imageView.setImageResource(R.mipmap.ic_launcher_round);
        layoutParams1.addRule(RelativeLayout.BELOW, warpTextView.getId());
        layoutParams1.addRule(RelativeLayout.CENTER_HORIZONTAL);
        OsdWarpView osdImageView = new OsdWarpView(getContext());
        osdImageView.addView(imageView);

        osdPanel.addView(osdImageView);
        osdPanel.setBackgroundResource(R.drawable.warp_view_panel_bg);

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
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                for (int i = 0; i < getChildCount(); i++) {
                    View childAt = getChildAt(i);
                    if (childAt instanceof OsdWarpView) {
                        ((OsdWarpView) childAt).requestCloseAllWindow();
                        break;
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }


    public OsdWarpView getOsdPanel() {
        return (OsdWarpView) getChildAt(0);
    }
}
