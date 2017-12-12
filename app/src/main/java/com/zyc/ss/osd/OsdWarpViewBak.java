package com.zyc.ss.osd;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by Administrator on 2017/12/10.
 */

public class OsdWarpViewBak extends RelativeLayout {
    public OsdWarpViewBak(Context context, View warpedView) {
        super(context);
        addView(warpedView);
        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
