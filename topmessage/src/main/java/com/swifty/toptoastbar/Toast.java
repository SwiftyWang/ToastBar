package com.swifty.toptoastbar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.collection.ArraySet;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.swifty.topstatusbar.R;
import com.swifty.toptoastbar.UrlImage.URLImageParser;
import com.swifty.toptoastbar.util.Utils;

import java.util.Set;

import static com.swifty.toptoastbar.FloatWindowService.SERVICE_WINDOWS_PARENT;

/**
 * Created by swifty on 22/11/2016.
 */
public class Toast extends FrameLayout {

    //dropdown duration
    public static final long DEFAULT_DURATION = 800;
    //show time
    public static final long DEFAULT_TIME = 300;
    public static final int LENGTH_LONG = 5000;
    public static final int LENGTH_SHORT = 3000;
    private Intent intent;
    TextView messageTextView;
    //-1 means dont dismiss, only worked in the viewgroup
    long time;
    Set<ToastListener> mToastListeners = new ArraySet<>();
    private Interpolator enterInterpolator;
    private Interpolator exitInterpolator;
    private ViewGroup mViewGroup;
    private Position position;

    public Toast(Context context) {
        super(context);
    }

    public Toast(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Toast(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * show the toast in the app views.
     *
     * @param viewGroup
     * @param message
     * @param time
     * @return
     */
    protected static Toast make(Position position, @NonNull ViewGroup viewGroup, String message, long time) {
        Toast toast = (Toast) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_message, viewGroup, false);
        toast.setText(message)
                .setPosition(position)
                .setTime(time)
                .setViewGroup(viewGroup)
                .setVisibility(INVISIBLE);
        return toast;
    }

    private void setIntent(Intent intent) {
        this.intent = intent;
    }

    private Toast setViewGroup(ViewGroup viewGroup) {
        this.mViewGroup = viewGroup;
        return this;
    }

    /**
     * show the toast in the android window
     *
     * @param context
     * @param message
     * @param time
     * @return
     */
    protected static Toast make(Position position, @NonNull Context context, String message, long time) {
        Toast toast = new Toast(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Utils.isMIUI8orLater()) {
            boolean hasPermission = checkWindowPermission(context);
            if (!hasPermission) {
                return toast;
            }
        }
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            boolean hasPermission = checkWindowPermission(context);
            if (!hasPermission) {
                return toast;
            }
        }
        Intent intent = new Intent(context, FloatWindowService.class);
        intent.putExtra("message", message);
        intent.putExtra("time", time > 0 ? time : DEFAULT_TIME);
        intent.putExtra("position", position);
        toast.setIntent(intent);
        return toast;
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private static boolean checkWindowPermission(Context context) {
        if (!Settings.canDrawOverlays(context)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + context.getPackageName()));
            context.startActivity(intent);
            return false;
        }
        return true;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        messageTextView = (TextView) findViewById(R.id.message_textview);
    }

    public Toast setAnimationInterpolator(Interpolator enterInterpolator, Interpolator exitInterpolator) {
        this.enterInterpolator = enterInterpolator;
        this.exitInterpolator = exitInterpolator;
        return this;
    }

    /**
     * @param enterInterpolator
     * @return
     * @deprecated use {@link Toast#setAnimationInterpolator(Interpolator, Interpolator)} instead of it.
     */
    public Toast setAnimationInterpolator(Interpolator enterInterpolator) {
        this.enterInterpolator = enterInterpolator;
        this.exitInterpolator = null;
        return this;
    }

    public Toast setBackground(int color) {
        if (color == 0) return this;
        if (intent != null) {
            intent.putExtra("backgroundColor", color);
        } else {
            setBackgroundColor(color);
        }
        return this;
    }

    /**
     * current only for internal use
     *
     * @param toastListener
     * @return this
     */
    Toast addToastListener(ToastListener toastListener) {
        if (toastListener != null) {
            mToastListeners.add(toastListener);
        }
        return this;
    }

    /**
     * current only for internal use
     *
     * @param toastListener
     * @return this
     */
    Toast removeToastListener(ToastListener toastListener) {
        if (toastListener != null) {
            mToastListeners.remove(toastListener);
        }
        return this;
    }

    public Toast setPosition(Position position) {
        this.position = position;

        if (position == Position.BOTTOM) {
            if (getLayoutParams() instanceof FrameLayout.LayoutParams) {
                ((LayoutParams) getLayoutParams()).gravity = Gravity.BOTTOM;
            } else if (getLayoutParams() instanceof RelativeLayout.LayoutParams) {
                ((RelativeLayout.LayoutParams) getLayoutParams()).addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            }
            setAnimationInterpolator(new LinearInterpolator(), null);
        } else {
            if (getLayoutParams() instanceof FrameLayout.LayoutParams) {
                ((LayoutParams) getLayoutParams()).gravity = Gravity.TOP;
            } else if (getLayoutParams() instanceof RelativeLayout.LayoutParams) {
                ((RelativeLayout.LayoutParams) getLayoutParams()).addRule(RelativeLayout.ALIGN_PARENT_TOP);
            }
            setAnimationInterpolator(new BounceInterpolator(), null);
        }
        return this;
    }

    public Toast setText(String msg) {
        if (messageTextView != null && !TextUtils.isEmpty(msg)) {
            URLImageParser p = new URLImageParser(messageTextView, getContext());
            Spanned htmlSpan = Html.fromHtml(msg, p, null);
            messageTextView.setText(htmlSpan);
        }
        return this;
    }

    public Toast setTextColor(int color) {
        if (color == 0) return this;
        if (intent != null) {
            intent.putExtra("textColor", color);
        } else {
            messageTextView.setTextColor(color);
        }
        return this;
    }

    public Toast setTime(long time) {
        if (time != 0) this.time = time;
        return this;
    }

    public void show() {
        show(0);
    }

    public void show(final long delay) {
        if (this.getParent() != null) {
            return;
        }

        if (mViewGroup != null) {
            mViewGroup.addView(this);
        } else if (intent != null) {
            intent.putExtra("delay", delay);
            getContext().startService(intent);
            return;
        }

        post(new Runnable() {
            @Override
            public void run() {
                Toast.this.setVisibility(VISIBLE);
                if (position == Position.BOTTOM) Toast.this.setY((getY() + getHeight()));
                else Toast.this.setY((getY() - getHeight()));
            }
        });
        postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.this.animate().translationY(0).setDuration(DEFAULT_DURATION).setInterpolator(enterInterpolator == null ? new BounceInterpolator() : enterInterpolator).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        for (ToastListener toastListener : mToastListeners) {
                            toastListener.beforeToastShow(Toast.this);
                        }
                        Toast.this.setVisibility(VISIBLE);
                        if (Toast.this.getParent() instanceof View && SERVICE_WINDOWS_PARENT.equals(((View) Toast.this.getParent()).getTag())) {
                            ((View) Toast.this.getParent()).setVisibility(VISIBLE);
                        }
                        for (ToastListener toastListener : mToastListeners) {
                            toastListener.afterToastShow(Toast.this);
                        }
                    }
                }).start();
            }
        }, delay);
        if (time != -1) {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toast.this.animate().translationY(position == Position.BOTTOM ? getHeight() : -getHeight()).setDuration(DEFAULT_DURATION).setInterpolator(exitInterpolator == null ? new AccelerateInterpolator() : exitInterpolator).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            for (ToastListener toastListener : mToastListeners) {
                                toastListener.beforeToastDismiss(Toast.this);
                            }
                            if (Toast.this.getParent() instanceof View
                                    && SERVICE_WINDOWS_PARENT.equals(((View) Toast.this.getParent()).getTag())) {
                                ((View) Toast.this.getParent()).setVisibility(GONE);
                            }
                            if (Toast.this.getParent() instanceof ViewGroup) {
                                ((ViewGroup) Toast.this.getParent()).removeView(Toast.this);
                            }
                            for (ToastListener toastListener : mToastListeners) {
                                toastListener.afterToastDismiss(Toast.this);
                            }
                        }
                    }).start();
                }
            }, time > 0 ? delay + time + DEFAULT_DURATION : delay + DEFAULT_TIME + DEFAULT_DURATION);
        }
    }

    public enum Position {
        TOP,
        BOTTOM,
    }
}
