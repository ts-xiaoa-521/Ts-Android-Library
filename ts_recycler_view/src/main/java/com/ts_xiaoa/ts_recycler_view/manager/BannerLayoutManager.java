package com.ts_xiaoa.ts_recycler_view.manager;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.ts_xiaoa.ts_recycler_view.listener.OnOffsetChangedListener;


/**
 * Created by ts_xiaoA on 2020/1/29 on 10:55
 * E-Mail Address：443502578@qq.com
 * Desc: banner LayoutManager recyclerView快速实现轮播图
 */
public class BannerLayoutManager extends RecyclerView.LayoutManager {

    //轮播时间
    private int duration = 2000;
    //动画时间 自动轮播时切换的速度
    private int animDuration = 300;
    //水平偏移量
    private int offsetX = 0;
    //轮播handle
    private Handler handler;
    //是否自动轮播
    private boolean autoPlay = true;
    //是否有切换动画
    private boolean hasAnim = false;
    //banner类型
    private int bannerType = TYPE_SINGLE;
    //显示一个item
    public static final int TYPE_SINGLE = 1;
    //显示三个item
    public static final int TYPE_MULTIPLE = 2;

    private float scaleValue = 0.8f;

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        layoutChildByPosition(0, recycler, state);
    }

    @Override
    public boolean canScrollHorizontally() {
        return bannerType == TYPE_MULTIPLE || (bannerType == TYPE_SINGLE && getItemCount() > 1);
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        layoutChildByPosition(-dx, recycler, state);
        return dx;
    }

    @SuppressLint("HandlerLeak")
    @Override
    public void onAttachedToWindow(RecyclerView view) {
        super.onAttachedToWindow(view);
        //通过LinearSnapHelper实现自动居中停靠
        LinearSnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(view);

        //创建handle实现轮播
        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                view.smoothScrollToPosition(msg.what);
            }
        };
    }

    @Override
    public void onDetachedFromWindow(RecyclerView view, RecyclerView.Recycler recycler) {
        handler.removeCallbacksAndMessages(null);
        handler = null;
        super.onDetachedFromWindow(view, recycler);
    }


    //布局child view
    private void layoutChildByPosition(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getItemCount() <= 0 || state.isPreLayout()) {
            return;
        }
        //滑动时 取消自动滑动
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        //显示的item个数
        int showCount;
        if (bannerType == TYPE_SINGLE) {
            showCount = 1;
        } else {
            showCount = 3;
        }
        //计算item显示的宽度
        int childMaxWidth = getWidth() / showCount;
        // 先移除所有view
        detachAndScrapAttachedViews(recycler);
        //滑动时改变偏移量
        offsetX += dx;
        //正常布局下的宽度
        int totalX = childMaxWidth * getItemCount();
        //判断当前偏移量，修正偏移量。实现无限轮播
        if (offsetX > 0) {
            offsetX -= totalX;
        }
        if (offsetX <= -totalX) {
            offsetX += totalX;
        }
        //计算当前显示的中间位置
        int centerPosition = -offsetX / childMaxWidth;
        //计算布局的left位置
        int left = offsetX + childMaxWidth * (showCount / 2);
        int widthUsed = getWidth() - childMaxWidth;
        int heightUsed = 0;
        //当前中间显示的是第一个时，将最后一个数据填充在第一个前面
        if (centerPosition == 0) {
            left -= childMaxWidth;
            View child = recycler.getViewForPosition(getItemCount() - 1);
            addView(child);
            measureChildWithMargins(child, widthUsed, heightUsed);
            layoutDecoratedWithMargins(child, left, getPaddingTop(), left + childMaxWidth, getPaddingTop() + child.getMeasuredHeight());
            left += childMaxWidth;
            if (hasAnim) {
                child.setScaleX(scaleValue);
                child.setScaleY(scaleValue);
            }
        }
        for (int i = 0; i < getItemCount(); i++) {
            //只需要显示中间及旁边的两个就行了
            if (Math.abs(centerPosition - i) <= showCount / 2 + 1) {
                View child = recycler.getViewForPosition(i);
                addView(child);
                measureChildWithMargins(child, widthUsed, heightUsed);
                layoutDecoratedWithMargins(child, left, getPaddingTop(), left + childMaxWidth, getPaddingTop() + child.getMeasuredHeight());
                //计算偏移量、设置动画效果(就是简单的缩放)
                int childOffset = Math.abs(offsetX) % childMaxWidth;
                if (hasAnim) {
                    if (i == centerPosition) {
                        float centerScale = (1.0f * childMaxWidth - childOffset) / childMaxWidth;
                        child.setScaleX(scaleValue + (1 - scaleValue) * centerScale);
                        child.setScaleY(scaleValue + (1 - scaleValue) * centerScale);
                    } else if (i == getCenterNextIndex(centerPosition)) {
                        float nextScale = 1f * (childOffset) / childMaxWidth;
                        child.setScaleX(scaleValue + (1 - scaleValue) * nextScale);
                        child.setScaleY(scaleValue + (1 - scaleValue) * nextScale);
                    } else {
                        child.setScaleX(scaleValue);
                        child.setScaleY(scaleValue);
                    }
                }
            }
            left += childMaxWidth;
        }
        //滑动到最后两个位置时在最后加上第一个和第二个数据
        if (centerPosition < getItemCount() && centerPosition > getItemCount() - 3) {
            View child = recycler.getViewForPosition(0);
            addView(child);
            measureChild(child, widthUsed, heightUsed);
//            measureChildWithMargins(child, getWidth() - childMaxWidth, getPaddingTop() + getPaddingBottom());
            layoutDecoratedWithMargins(child, left, getPaddingTop(), left + childMaxWidth, getPaddingTop() + child.getMeasuredHeight());
            left += childMaxWidth;
            if (hasAnim) {
                if (centerPosition == getItemCount() - 1) {
                    //计算偏移量、设置动画效果(就是简单的缩放)
                    int childOffset = Math.abs(offsetX) % childMaxWidth;
                    float nextScale = 1f * (childOffset) / childMaxWidth;
                    child.setScaleX(scaleValue + (1 - scaleValue) * nextScale);
                    child.setScaleY(scaleValue + (1 - scaleValue) * nextScale);
                } else {
                    child.setScaleX(scaleValue);
                    child.setScaleY(scaleValue);
                }
            }
            View child1 = recycler.getViewForPosition(getItemCount() > 1 ? 1 : 0);
            addView(child1);
            measureChildWithMargins(child1, widthUsed, heightUsed);
            layoutDecoratedWithMargins(child1, left, getPaddingTop(), left + childMaxWidth, getPaddingTop() + child1.getMeasuredHeight());
            if (hasAnim) {
                child1.setScaleX(scaleValue);
                child1.setScaleY(scaleValue);
            }
        }
        //计算偏移量比例
        int childOffset = Math.abs(offsetX) % childMaxWidth;
        float nextScale = 1f * (childOffset) / childMaxWidth;
        if (onOffsetChangedListener != null) {
            onOffsetChangedListener.onOffset(nextScale, centerPosition);
        }
        if (bannerType == TYPE_SINGLE) {
            if (autoPlay && getItemCount() > 1) {
                handler.sendEmptyMessageDelayed(centerPosition, duration);
            }
        } else {
            if (autoPlay) {
                handler.sendEmptyMessageDelayed(centerPosition, duration);
            }
        }
        if (autoPlay) {
            if ((bannerType == TYPE_SINGLE && getItemCount() > 1) || bannerType == TYPE_MULTIPLE) {
                handler.sendEmptyMessageDelayed(centerPosition, duration);
            }
        }
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        SmoothScroller smoothScroller = new SmoothScroller();
        smoothScroller.setTargetPosition(position);
        startSmoothScroll(smoothScroller);
    }

    private int getCenterNextIndex(int centerPosition) {
        if (centerPosition < getItemCount() - 1) {
            return centerPosition + 1;
        } else {
            return centerPosition - getItemCount() + 1;
        }
    }

    @Override
    public boolean isAutoMeasureEnabled() {
        return true;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setAnimDuration(int animDuration) {
        this.animDuration = animDuration;
    }

    public void setAutoPlay(boolean autoPlay) {
        this.autoPlay = autoPlay;
    }


    public void setHasAnim(boolean hasAnim) {
        this.hasAnim = hasAnim;
    }

    public void setBannerType(int bannerType) {
        this.bannerType = bannerType;
        this.hasAnim = true;
    }

    //处理recyclerView.smoothScrollToPosition事件
    class SmoothScroller extends RecyclerView.SmoothScroller {

        private Interpolator interpolator = new AccelerateDecelerateInterpolator();

        @Override
        protected void onStart() {

        }

        @Override
        protected void onStop() {

        }

        @Override
        protected void onSeekTargetStep(int dx, int dy, @NonNull RecyclerView.State state, @NonNull Action action) {

        }

        @Override
        protected void onTargetFound(@NonNull View targetView, @NonNull RecyclerView.State state, @NonNull Action action) {
//            int firstLeft = targetView.getRight() - (showCount / 2 + 1) * targetView.getMeasuredWidth();
            action.update(targetView.getMeasuredWidth(), 0, animDuration, interpolator);
        }
    }

    public void setScaleValue(float scaleValue) {
        this.scaleValue = scaleValue;
    }


    private OnOffsetChangedListener onOffsetChangedListener;

    public void setOnOffsetChangedListener(OnOffsetChangedListener onOffsetChangedListener) {
        this.onOffsetChangedListener = onOffsetChangedListener;
    }
}
