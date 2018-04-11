package com.swifty.toptoastbar;

public interface ToastListener {
    void beforeToastShow(Toast toast);

    void afterToastShow(Toast toast);

    void beforeToastDismiss(Toast toast);

    void afterToastDismiss(Toast toast);
}
