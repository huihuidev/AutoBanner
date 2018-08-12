package com.hh.banner;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class AutoBanner extends FrameLayout implements ViewPager.OnPageChangeListener {
    private ViewPager mViewPager;
    private LinearLayout pointLayout;
    private Context mContext;
    private ViewPager.OnPageChangeListener onPageChangeListener;
    private BannerHandler mHandler;
    private MyPagerAdapter mAdapter;
    private boolean isAutoPlay = true;
    private int currentPosition;
    private List<String> mDataList;
    private int viewCount;
    private SparseArray<View> pointViews;
    private int lastPosition;

    public AutoBanner(@NonNull Context context) {
        super(context);
        initView(context);
        initData();
    }

    public AutoBanner(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        initData();
    }

    private void initView(Context context) {
        mContext = context;
        View contentView = LayoutInflater.from(context)
                .inflate(R.layout.view_auto_banner, this, true);
        mViewPager = contentView.findViewById(R.id.view_pager);
        pointLayout = contentView.findViewById(R.id.point_layout);
        mViewPager.addOnPageChangeListener(this);
    }

    private void initData() {
        mDataList = new ArrayList<>();
        pointViews = new SparseArray<>();
        mAdapter = new MyPagerAdapter();
        mHandler = new BannerHandler(this);
        mViewPager.setAdapter(mAdapter);
    }

    public void setData(List<String> list) {
        currentPosition = 1;
        lastPosition = 1;
        mDataList.clear();
        if (list == null || list.size() == 0) {
            viewCount = 0;
            return;
        }
        mDataList.addAll(list);
        int count = list.size();
        initPointLayout(count);
        viewCount = count > 1 ? count + 2 : count;
        if (mAdapter == null) {
            mAdapter = new MyPagerAdapter();
        }
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(currentPosition);
        if (isAutoPlay) {
            startPlay();
        }
    }

    private void initPointLayout(int count) {
        for (int i = 0; i < count; i++) {
            TextView view = new TextView(mContext);
            view.setEnabled(false);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(20, 20);
            params.leftMargin = 10;
            params.rightMargin = 10;
            view.setLayoutParams(params);
            view.setBackgroundResource(R.drawable.point_selector_bg);
            pointViews.put(i, view);
            pointLayout.addView(view);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            stopPlay();
        } else {
            startPlay();
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (onPageChangeListener != null) {
            onPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
    }

    @Override
    public void onPageSelected(int position) {
        if (onPageChangeListener != null) {
            onPageChangeListener.onPageSelected(position);
        }
        currentPosition = position;
        View oldView = pointViews.get(getRealPosition(lastPosition));
        View newView = pointViews.get(getRealPosition(position));
        oldView.setEnabled(false);
        newView.setEnabled(true);
        lastPosition = position;
    }

    private int getRealPosition(int position) {
        if (position == 0) {
            return mDataList.size() - 1;
        } else if (position == viewCount - 1) {
            return 0;
        } else {
            return position - 1;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (onPageChangeListener != null) {
            onPageChangeListener.onPageScrollStateChanged(state);
        }
        switch (state) {
            case ViewPager.SCROLL_STATE_DRAGGING:
                break;
            case ViewPager.SCROLL_STATE_SETTLING:
                break;
            case ViewPager.SCROLL_STATE_IDLE:
                if (currentPosition == 0) {
                    mViewPager.setCurrentItem(viewCount - 2, false);
                } else if (currentPosition == (viewCount - 1)) {
                    mViewPager.setCurrentItem(1, false);
                }
                break;
        }
    }


    /**
     * 设置viewpager 监听器
     *
     * @param onPageChangeListener
     */
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
    }

    public void startPlay() {
        mHandler.removeMessages(BannerHandler.AUTO_PLAY);
        mHandler.sendEmptyMessageDelayed(BannerHandler.AUTO_PLAY, 1500);
    }

    public void stopPlay() {
        mHandler.removeMessages(BannerHandler.AUTO_PLAY);
    }

    /**
     * handler用了处理消息进行自动播放
     */
    private static class BannerHandler extends Handler {
        private static final int AUTO_PLAY = 0x0010;
        private WeakReference<AutoBanner> mBanner;

        BannerHandler(AutoBanner autoBanner) {
            mBanner = new WeakReference<AutoBanner>(autoBanner);
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == AUTO_PLAY) {
                if (mBanner != null && mBanner.get() != null) {
                    final AutoBanner autoBanner = mBanner.get();
                    int currentItem = autoBanner.currentPosition;
                    autoBanner.mViewPager.setCurrentItem(currentItem + 1, true);
                    autoBanner.startPlay();
                }
            }
        }
    }

    /**
     * 适配器
     */
    private class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return viewCount;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object obj) {
            return view == obj;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            ImageView imageView = new ImageView(mContext);
            String url = "";
            if (position == 0) {
                url = mDataList.get(viewCount - 3);
            } else if (position == getCount() - 1) {
                url = mDataList.get(0);
            } else {
                url = mDataList.get(position - 1);
            }
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(mContext).load(url).into(imageView);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }
}
