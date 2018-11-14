package com.zyc.ss.osd;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by Administrator on 2017/12/10.
 */

public class OsdRelativeLayout extends RelativeLayout {
    private String tag = getClass().getSimpleName();

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        init();
    }

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


    private void init() {
        OsdWarpView osdPanel = new OsdWarpView(getContext());
        osdPanel.mTag = "OsdPanelView";

        DragEditText dragEditText = new DragEditText(getContext());
        dragEditText.setCursorVisible(true);
        dragEditText.setHint("你的号大");
        dragEditText.setBackgroundResource(R.drawable.bg_edittext);
        dragEditText.setPadding(0, 0, 0, 0);
        // textView.setText("Hello");


        OsdWarpView warpTextView = new OsdWarpView(getContext());
        warpTextView.mTag = "OsdTextWarpedView";
        warpTextView.addView(dragEditText);
        osdPanel.addView(warpTextView);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        ImageView imageView = new ImageView(getContext());
        imageView.setLayoutParams(new LayoutParams(200, 200));
        imageView.setImageResource(R.mipmap.ic_launcher_round);
        layoutParams.addRule(RelativeLayout.BELOW, warpTextView.getId());
        //    layoutParams1.addRule(RelativeLayout.CENTER_HORIZONTAL);
        OsdWarpView warpImageView = new OsdWarpView(getContext());
        //  warpImageView.setLayoutParams(layoutParams);
        warpImageView.mTag = "OsdImageWarpedView";
        warpImageView.addView(imageView);


        osdPanel.addView(warpImageView);
        osdPanel.setBackgroundResource(R.drawable.warp_view_panel_bg);

        addView(osdPanel, 700, 700);
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
