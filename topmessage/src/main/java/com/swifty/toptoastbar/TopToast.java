package com.swifty.toptoastbar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.swifty.topstatusbar.R;

import java.io.Serializable;

/**
 * Created by Swifty on 2016/2/6.
 */
public class TopToast extends FrameLayout {
    private static Intent intent;
    TextView message;
    public static final long DEFAULTTIME = 300;
    public static final long DEFAULTDERATION = 1000;
    long time;
    private Interpolator interpolator;

    public TopToast(Context context) {
        super(context);
    }

    public TopToast(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TopToast(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        message = (TextView) findViewById(R.id.message_text);
    }

    public void setText(String msg) {
        if (message != null) {
            message.setText(Html.fromHtml(msg));
        }
    }

    public void setTime(long time) {
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
                    TopToast.this.setY(-getHeight());
                }
            });
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    TopToast.this.animate().translationY(0).setDuration(DEFAULTDERATION).setInterpolator(interpolator == null ? new BounceInterpolator() : interpolator).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            super.onAnimationStart(animation);
                            TopToast.this.setVisibility(VISIBLE);
                            if (TopToast.this.getParent() instanceof View && "service_parent".equals(((View) TopToast.this.getParent()).getTag())) {
                                ((View) TopToast.this.getParent()).setVisibility(VISIBLE);
                            }
                        }
                    }).start();
                }
            }, delay);
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    TopToast.this.animate().translationY(-getHeight()).setDuration(DEFAULTDERATION).setInterpolator(interpolator == null ? new AccelerateInterpolator() : interpolator).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            TopToast.this.setVisibility(GONE);
                            if (TopToast.this.getParent() instanceof View && "service_parent".equals(((View) TopToast.this.getParent()).getTag())) {
                                ((View) TopToast.this.getParent()).setVisibility(GONE);
                            }
                        }
                    }).start();
                }
            }, time > 0 ? delay + time + DEFAULTDERATION : delay + DEFAULTTIME + DEFAULTDERATION);
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
    public static TopToast make(ViewGroup viewGroup, Context context, String message, long time) {
        if (viewGroup == null) {
            TopToast topToast = new TopToast(context);
            TopToast.intent = new Intent(context, FloatWindowService.class);
            intent.putExtra("message", message);
            intent.putExtra("time", time > 0 ? time : TopToast.DEFAULTTIME);
            return topToast;
        } else {
            TopToast topToast = (TopToast) LayoutInflater.from(context).inflate(R.layout.view_message, viewGroup, false);
            topToast.setText(message);
            topToast.setTime(time);
            viewGroup.addView(topToast);
            return topToast;
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
    public static TopToast make(Context context, String message, long time) {
        return make(null, context, message, time);
    }

    public TopToast setBackground(int color) {
        if (intent != null) {
            intent.putExtra("backgroundColor", color);
        } else {
            setBackgroundColor(color);
        }
        return this;
    }

    public TopToast setTextColor(int color) {
        if (intent != null) {
            intent.putExtra("textColor", color);
        } else {
            message.setTextColor(color);
        }
        return this;
    }

    public TopToast setAnimationInterpolator(Interpolator interpolator) {
        this.interpolator = interpolator;
        return this;
    }
}
