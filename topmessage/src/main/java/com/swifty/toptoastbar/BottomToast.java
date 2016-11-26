package com.swifty.toptoastbar;

import android.content.Context;
import android.view.ViewGroup;

/**
 * Created by swifty on 22/11/2016.
 */

public class BottomToast extends Toast {

    private BottomToast(Context context) {
        super(context);
    }

    /**
     * show the toast in the app views.
     *
     * @param viewGroup
     * @param context
     * @param message
     * @param time
     * @return
     */
    public static Toast make(ViewGroup viewGroup, Context context, String message, long time) {
        return make(Position.BOTTOM, viewGroup, context, message, time);
    }

    /**
     * show the toast in the android window
     *
     * @param context
     * @param message
     * @param time
     * @return
     */
    public static Toast make(Context context, String message, long time) {
        return make(Position.BOTTOM, context, message, time);
    }
}
