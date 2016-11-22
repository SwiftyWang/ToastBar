package com.swifty.toptoastbar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onBottomToast(View view) {
        BottomToast.make((ViewGroup) findViewById(R.id.parent), MainActivity.this, "HELLO WORLD!!!!", 3000).show();
    }

    public void onTopToast(View view) {
        TopToast.make((ViewGroup) findViewById(R.id.parent), MainActivity.this, "HELLO WORLD!!!!", 3000).show();
    }
}
