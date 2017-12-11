package com.zyc.ss.osd;

import android.content.Context;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by Administrator on 2017/12/10.
 */

public class OsdWarpView extends RelativeLayout {
    public OsdWarpView(Context context, View warpedView) {
        super(context);
        setBackgroundColor(Color.GREEN);
        addView(warpedView);

    }
}
