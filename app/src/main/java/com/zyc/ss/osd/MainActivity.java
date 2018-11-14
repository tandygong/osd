package com.zyc.ss.osd;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    String tag = "mainActivity";
    private DrawerLayout drawerLayout;

    private OsdRelativeLayout osdRelativeLayout;
    private ImageView ivBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        osdRelativeLayout = findViewById(R.id.osdRelativeLayout);
        ivBitmap = findViewById(R.id.iv_);


        drawerLayout = findViewById(R.id.draw_layout);
        drawerLayout.openDrawer(Gravity.LEFT);
        TextView addOsdText = drawerLayout.findViewById(R.id.osd_text);
        TextView addOsdImage = drawerLayout.findViewById(R.id.osd_image);
        TextView tvApply = drawerLayout.findViewById(R.id.osd_apply);


        tvApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OsdWarpView osdPanel = osdRelativeLayout.getOsdPanel();

                ArrayList<OsdWarpView> emptyTextViewWarps = new ArrayList<>();
                for (int i = 0; i < osdPanel.getChildCount(); i++) {
                    View childAt = osdPanel.getChildAt(i);
                    if (childAt instanceof OsdWarpView) {
                        View warpedView = ((OsdWarpView) childAt).getChildAt(0);
                        if (warpedView instanceof TextView) {
                            if (((TextView) warpedView).getText().toString().equals("")) {
                                emptyTextViewWarps.add((OsdWarpView) childAt);
                            }
                        }
                    }
                }

                for (OsdWarpView warpView : emptyTextViewWarps) {
                    osdPanel.removeView(warpView);
                }


                osdPanel.setDrawingCacheEnabled(true);
                osdPanel.setBackgroundColor(Color.TRANSPARENT);
                osdPanel.setDrawingCacheBackgroundColor(Color.TRANSPARENT);
                osdPanel.buildDrawingCache();
                Bitmap bitmap = Bitmap.createBitmap(osdPanel.getDrawingCache());
                osdPanel.setDrawingCacheEnabled(false);

                if (bitmap == null) {
                    Log.e("bitmap", "null---->");
                }
                osdPanel.setBackgroundResource(R.drawable.warp_view_panel_bg);
                ivBitmap.setImageBitmap(bitmap);

            }
        });
        addOsdText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final OsdWarpView osdVIew = new OsdWarpView(MainActivity.this);
                osdVIew.mTag = "osdEditTextView";
                Log.e(tag, "clickAddOsd");
                final DragEditText dragEditText = new DragEditText(MainActivity.this);
                dragEditText.setHint("请输入...");
                dragEditText.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return false;
                    }
                });
                dragEditText.setHintTextColor(Color.argb(40, 0, 0, 0));
              /*  editText.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        osdVIew.showPopupWindow();
                        return false;
                    }
                });*/
                dragEditText.setPadding(0, 0, 0, 0);
                //  editText.setFocusableInTouchMode(false);

                dragEditText.setBackgroundResource(R.drawable.bg_edittext);


                //  osdVIew.setInterceptChildEvent(true);
                // TextView textView = new TextView(MainActivity.this);
                // textView.setText("你好");
                osdVIew.addView(dragEditText);
                osdVIew.setSelected(true);
                osdRelativeLayout.getOsdPanel().addView(osdVIew);
                closeDrawLayout();
            }
        });
        addOsdImage.setOnClickListener(new View.OnClickListener() {
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

    }

    private void closeDrawLayout() {
        if (drawerLayout != null) {
            drawerLayout.closeDrawer(Gravity.LEFT);
        }
    }
}
