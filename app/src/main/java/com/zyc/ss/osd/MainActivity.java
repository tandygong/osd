package com.zyc.ss.osd;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        OsdRelativeLayout osdRelativeLayout = findViewById(R.id.osdRelativeLayout);
        TextView textView = new TextView(this);
        textView.setText("Hello");
        osdRelativeLayout.addView(new OsdWarpView(this, textView));

        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.mipmap.ic_launcher_round);
        osdRelativeLayout.addView(new OsdWarpView(this, imageView));

    }
}
