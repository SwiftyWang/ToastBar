package com.swifty.toptoastbar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

public class MainActivity extends AppCompatActivity {

    public void onBottomToast(View view) {
        BottomToast.make((ViewGroup) findViewById(R.id.parent), "HELLO WORLD!!!!", 3000).show();
    }

    public void onBottomWindowsToast(View view) {
        BottomToast.make(this, "HELLO WORLD!!!!", 3000).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onTopToast(View view) {
        TopToast.make((ViewGroup) findViewById(R.id.parent), "HELLO WORLD!!!!", 3000).show();
    }

    public void onTopWindowsToast(View view) {
        TopToast.make(this, "HELLO WORLD!!!!", 3000).show();
    }
}
