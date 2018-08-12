package com.hh.banner;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

public class BannerHandler extends Handler {
    private static final int PLAY_CODE = 0x012;
    private int delayMillis = 1500;
    private WeakReference<AutoBanner> mAutoBanner;

    public BannerHandler(AutoBanner banner, int delayMillis) {
        mAutoBanner = new WeakReference<AutoBanner>(banner);
        this.delayMillis = delayMillis > 0 ? delayMillis : this.delayMillis;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case PLAY_CODE:
                if (mAutoBanner != null && mAutoBanner.get() != null) {
//                    AutoBanner banner = mAutoBanner.get();
//                    int currentItem = banner.getCurrentItem();
//                    banner.setCurrentItem(currentItem + 1);
//                    sendEmptyMessageDelayed(PLAY_CODE, delayMillis);
                }
                break;
        }
    }

    public void startPlay() {
        removeMessages(PLAY_CODE);
        sendEmptyMessageDelayed(PLAY_CODE, delayMillis);
    }

    public void stopPlay() {
        removeMessages(PLAY_CODE);
    }
}
