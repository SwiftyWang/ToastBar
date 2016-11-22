package com.swifty.toptoastbar;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.swifty.topstatusbar.R;

/**
 * Created by Swifty on 2016/2/6.
 */
public class FloatWindowService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        long delay = intent.getLongExtra("delay", 0);
        long time = intent.getLongExtra("time", 0);
        int bgColor = intent.getIntExtra("backgroundColor", 0);
        int textColor = intent.getIntExtra("textColor", 0);
        Toast.Position position = (Toast.Position) intent.getSerializableExtra("position");
        String message = intent.getStringExtra("message");
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();

        layoutParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        layoutParams.format = PixelFormat.RGBA_8888;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        layoutParams.gravity = position == Toast.Position.BOTTOM ? Gravity.BOTTOM : Gravity.TOP;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        Toast toast = (Toast) LayoutInflater.from(this).inflate(R.layout.view_message, null);
        toast.setText(message);
        toast.setPosition(position);
        toast.setTime(time);
        toast.show(delay);
        if (bgColor != 0)
            toast.setBackground(bgColor);
        if (textColor != 0)
            toast.setTextColor(textColor);
        FrameLayout frameLayout = new FrameLayout(this);
        frameLayout.setTag("service_parent");
        frameLayout.addView(toast);
        frameLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        windowManager.addView(frameLayout, layoutParams);
        stopSelf();
        return START_NOT_STICKY;
    }
}