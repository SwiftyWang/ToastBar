package com.swifty.toptoastbar;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

/**
 * Created by Swifty on 2016/2/6.
 */
public class TopToast extends Toast {

    private TopToast(Context context) {
        super(context);
    }


    /**
     * show the toast in the app views.
     *
     * @param viewGroup
     * @param message
     * @param time
     * @return
     */
    public static Toast make(@NonNull ViewGroup viewGroup, String message, long time) {
        return make(Position.TOP, viewGroup, message, time);
    }

    /**
     * show the toast in the android window
     *
     * @param context
     * @param message
     * @param time
     * @return
     */
    public static Toast make(@NonNull Context context, String message, long time) {
        return make(Position.TOP, context, message, time);
    }
}
