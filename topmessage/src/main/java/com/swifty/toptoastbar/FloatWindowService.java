package com.swifty.toptoastbar;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
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

    public static String SERVICE_WINDOWS_PARENT = "service_parent";

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
        final WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        if (windowManager == null) {
            return START_NOT_STICKY;
        }
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        }
        layoutParams.format = PixelFormat.RGBA_8888;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        layoutParams.gravity = position == Toast.Position.BOTTOM ? Gravity.BOTTOM : Gravity.TOP;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        Toast toast = (Toast) LayoutInflater.from(this).inflate(R.layout.view_message, null);
        final FrameLayout frameLayout = new FrameLayout(this);
        frameLayout.setTag(SERVICE_WINDOWS_PARENT);
        frameLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        toast.setText(message)
                .setPosition(position)
                .setTime(time)
                .addToastListener(new ToastListenerAdapter() {
                    @Override
                    public void afterToastDismiss(Toast toast) {
                        windowManager.removeView(frameLayout);
                    }
                });
        if (bgColor != 0) {
            toast.setBackground(bgColor);
        }
        if (textColor != 0) {
            toast.setTextColor(textColor);
        }
        toast.show(delay);

        frameLayout.addView(toast);
        windowManager.addView(frameLayout, layoutParams);
        stopSelf();
        return START_NOT_STICKY;
    }
}