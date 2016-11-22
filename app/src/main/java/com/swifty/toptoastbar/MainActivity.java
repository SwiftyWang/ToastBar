package com.swifty.toptoastbar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onBottomToast(View view) {
        BottomToast.make(MainActivity.this, "HELLO WORLD!!!!", 3000).show();
    }

    public void onTopToast(View view) {
        TopToast.make(MainActivity.this, "HELLO WORLD!!!!", 3000).show();
    }
}
