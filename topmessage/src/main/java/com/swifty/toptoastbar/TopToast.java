package com.swifty.toptoastbar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.swifty.topstatusbar.R;
import com.swifty.toptoastbar.UrlImage.URLImageParser;

/**
 * Created by Swifty on 2016/2/6.
 */
public class TopToast extends FrameLayout {
    private static Intent intent;
    TextView message;
    //show time
    public static final long DEFAULTTIME = 300;
    //dropdown duration
    public static final long DEFAULTDERATION = 1000;
    public static final int LENGTH_SHORT = 3000;
    public static final int LENGTH_LONG = 5000;
    //-1 means dont dismiss, only worked in the viewgroup
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
                    TopToast.this.setVisibility(VISIBLE);
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
            if (time != -1) {
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
            topToast.setVisibility(INVISIBLE);
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
        if (color == 0) return this;
        if (intent != null) {
            intent.putExtra("backgroundColor", color);
        } else {
            setBackgroundColor(color);
        }
        return this;
    }

    public TopToast setTextColor(int color) {
        if (color == 0) return this;
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
