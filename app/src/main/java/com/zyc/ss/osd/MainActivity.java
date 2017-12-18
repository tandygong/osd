package com.zyc.ss.osd;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    String tag = "mainActivity";
    private DrawerLayout drawerLayout;

    private OsdRelativeLayout osdRelativeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        osdRelativeLayout = findViewById(R.id.osdRelativeLayout);


        drawerLayout = findViewById(R.id.draw_layout);
        drawerLayout.openDrawer(Gravity.LEFT);
        TextView addOsdText = drawerLayout.findViewById(R.id.osd_text);
        TextView addOsdImage = drawerLayout.findViewById(R.id.osd_image);
        addOsdText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final OsdWarpView osdVIew = new OsdWarpView(MainActivity.this);
                osdVIew.tag = "osdEditTextView";
                Log.e(tag, "clickAddOsd");
                final EditText editText = new EditText(MainActivity.this);
                editText.setHint("请输入...");
              /*  editText.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        osdVIew.showPopupWindow();
                        return false;
                    }
                });*/
                editText.setPadding(0, 0, 0, 0);
              //  editText.setFocusableInTouchMode(false);

                editText.setBackgroundResource(R.drawable.bg_edittext);


              //  osdVIew.setInterceptChildEvent(true);
               // TextView textView = new TextView(MainActivity.this);
               // textView.setText("你好");
                osdVIew.addView(editText);

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
