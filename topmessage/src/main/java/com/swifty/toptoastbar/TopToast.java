package com.swifty.toptoastbar;

import android.content.Context;
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
     * @param context
     * @param message
     * @param time
     * @return
     */
    protected static Toast make(ViewGroup viewGroup, Context context, String message, long time) {
        return make(Position.TOP, viewGroup, context, message, time);
    }

    /**
     * show the toast in the android window
     *
     * @param context
     * @param message
     * @param time
     * @return
     */
    protected static Toast make(Context context, String message, long time) {
        return make(Position.TOP, context, message, time);
    }
}
