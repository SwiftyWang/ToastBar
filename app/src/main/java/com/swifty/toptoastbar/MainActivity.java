package com.swifty.toptoastbar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

public class MainActivity extends AppCompatActivity {

    private Toast windowToast;
    private Toast toast;

    public void onBottomToast(View view) {
        BottomToast.make((ViewGroup) findViewById(R.id.parent), "HELLO WORLD!!!!", Toast.LENGTH_SHORT).show();
    }

    public void onBottomWindowToast(View view) {
        BottomToast.make(this, "HELLO WORLD!!!!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        windowToast = TopToast.make(this, "HELLO WORLD!!!!", Toast.LENGTH_SHORT);
        toast = TopToast.make((ViewGroup) findViewById(R.id.parent), "HELLO WORLD!!!!", Toast.LENGTH_SHORT);
    }

    public void onTopToast(View view) {
        toast.show();
    }

    public void onTopWindowToast(View view) {
        windowToast.show();
    }
}
