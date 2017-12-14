package com.zyc.ss.osd;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      /*  OsdRelativeLayout osdRelativeLayout = findViewById(R.id.osdRelativeLayout);

        OsdWarpView osdPanel = new OsdWarpView(this);

        TextView textView = new TextView(this);
        textView.setText("Hello");


        OsdWarpView warpTextView = new OsdWarpView(this);
        warpTextView.addView(textView);
        osdPanel.addView(warpTextView);

        RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.mipmap.ic_launcher_round);
        layoutParams1.addRule(RelativeLayout.BELOW, warpTextView.getId());
        layoutParams1.addRule(RelativeLayout.CENTER_HORIZONTAL);
        OsdWarpView osdImageView = new OsdWarpView(this);
        osdImageView.addView(imageView);

        osdPanel.addView(osdImageView);
*/
    }
}
