package com.swifty.toptoastbar;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TopToast.make(MainActivity.this, "HELLO WORLD!!!!", 3000)
                        .setBackground(Color.DKGRAY)
                        .setTextColor(Color.GREEN)
                        .setAnimationInterpolator(new BounceInterpolator())
                        .show(500);
            }
        });

    }
}
