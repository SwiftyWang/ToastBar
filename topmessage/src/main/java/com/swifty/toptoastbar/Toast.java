package com.swifty.toptoastbar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
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

/**
 * Created by swifty on 22/11/2016.
 */
public class Toast extends FrameLayout {

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

    public void setPosition(Position position) {
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
    }

    public enum Position {
        TOP,
        BOTTOM,
    }

    private static Intent intent;
    TextView message;
    //show time
    public static final long DEFAULTTIME = 300;
    //dropdown duration
    public static final long DEFAULTDERATION = 800;
    public static final int LENGTH_SHORT = 3000;
    public static final int LENGTH_LONG = 5000;
    //-1 means dont dismiss, only worked in the viewgroup
    long time;
    private Interpolator enterInterpolator;
    private Interpolator exitInterpolator;

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        message = (TextView) findViewById(R.id.message_text);
    }

    public void setText(String msg) {
        if (message != null && !TextUtils.isEmpty(msg)) {
            URLImageParser p = new URLImageParser(message, getContext());
            Spanned htmlSpan = Html.fromHtml(msg, p, null);
            message.setText(htmlSpan);
        }
    }

    public void setTime(long time) {
        if (time != 0)
            this.time = time;
    }

    public void show() {
        show(0);
    }


    public void show(final long delay) {
        if (intent != null) {
            intent.putExtra("delay", delay);
            getContext().startService(intent);
            intent = null;
        } else {
            post(new Runnable() {
                @Override
                public void run() {
                    Toast.this.setVisibility(VISIBLE);
                    Toast.this.setY(position == Position.BOTTOM ? (getY() + getHeight()) : (getY() - getHeight()));
                }
            });
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toast.this.animate().translationY(0).setDuration(DEFAULTDERATION).setInterpolator(enterInterpolator == null ? new BounceInterpolator() : enterInterpolator).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            super.onAnimationStart(animation);
                            Toast.this.setVisibility(VISIBLE);
                            if (Toast.this.getParent() instanceof View && "service_parent".equals(((View) Toast.this.getParent()).getTag())) {
                                ((View) Toast.this.getParent()).setVisibility(VISIBLE);
                            }
                        }
                    }).start();
                }
            }, delay);
            if (time != -1) {
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.this.animate().translationY(position == Position.BOTTOM ? getHeight() : -getHeight()).setDuration(DEFAULTDERATION).setInterpolator(exitInterpolator == null ? new AccelerateInterpolator() : exitInterpolator).setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                if (Toast.this.getParent() instanceof View
                                        && "service_parent".equals(((View) Toast.this.getParent()).getTag())) {
                                    ((View) Toast.this.getParent()).setVisibility(GONE);
                                }
                                if (Toast.this.getParent() instanceof ViewGroup) {
                                    ((ViewGroup) Toast.this.getParent()).removeView(Toast.this);
                                }
                            }
                        }).start();
                    }
                }, time > 0 ? delay + time + DEFAULTDERATION : delay + DEFAULTTIME + DEFAULTDERATION);
            }
        }
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
    protected static Toast make(Position position, ViewGroup viewGroup, Context context, String message, long time) {
        if (viewGroup == null) {
            Toast toast = new Toast(context);
            toast.intent = new Intent(context, FloatWindowService.class);
            intent.putExtra("message", message);
            intent.putExtra("time", time > 0 ? time : toast.DEFAULTTIME);
            intent.putExtra("position", position);
            return toast;
        } else {
            Toast toast = (Toast) LayoutInflater.from(context).inflate(R.layout.view_message, viewGroup, false);
            toast.setText(message);
            toast.setPosition(position);
            toast.setTime(time);
            toast.setVisibility(INVISIBLE);
            viewGroup.addView(toast);
            return toast;
        }
    }

    /**
     * show the toast in the android window
     *
     * @param context
     * @param message
     * @param time
     * @return
     */
    protected static Toast make(Position position, Context context, String message, long time) {
        return make(position, null, context, message, time);
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

    public Toast setTextColor(int color) {
        if (color == 0) return this;
        if (intent != null) {
            intent.putExtra("textColor", color);
        } else {
            message.setTextColor(color);
        }
        return this;
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
}
