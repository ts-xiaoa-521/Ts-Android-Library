package com.ts_xiaoa.ts_recycler_view.manager;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

/**
 * create by ts_xiaoA on 2020-05-15 11:18
 * email：443502578@qq.com
 * desc：可无限循环LinearLayoutManager
 */
public class CycleLinearLayoutManager extends RecyclerView.LayoutManager {

    //是否自动播放
    private boolean isAutoPlay = false;
    //是否自动居中
    private boolean isAutoCenter = true;
    //自动播放的handler
    private Handler autoPlayHandler;
    //滑动方向 垂直水平，和LinearLayout一致线性滑动
    private int orientation;
    //滑动偏移量
    private int offset = 0;
    //记录item总高度
    private int totalHeight = 0;
    //记录item总宽度
    private int totalWidth = 0;
    //记录第一个可见的item位置
    private int firstVisibleItemPosition = 0;
    //滑动时间 默认500ms
    private int animationDuration = 500;
    //轮播间隔时间 默认2000ms
    private long duration = 2000;
    private SnapHelper snapHelper;

    public CycleLinearLayoutManager() {
        this(RecyclerView.VERTICAL);
    }

    public CycleLinearLayoutManager(@RecyclerView.Orientation int orientation) {
        this(orientation, false);
    }

    public CycleLinearLayoutManager(@RecyclerView.Orientation int orientation, boolean isAutoPlay) {
        this.orientation = orientation;
        this.isAutoPlay = isAutoPlay;
    }

    @SuppressLint("HandlerLeak")
    @Override
    public void onAttachedToWindow(RecyclerView view) {
        super.onAttachedToWindow(view);
        if (isAutoPlay) {
            isAutoCenter = true;
            //创建handle实现轮播
            autoPlayHandler = new Handler() {
                @Override
                public void handleMessage(@NonNull Message msg) {
                    view.smoothScrollToPosition(msg.what);
                }
            };
        }
        if (isAutoCenter) {
            snapHelper = new LinearSnapHelper();
            snapHelper.attachToRecyclerView(view);
        }
    }

    @Override
    public void onDetachedFromWindow(RecyclerView view, RecyclerView.Recycler recycler) {
        if (autoPlayHandler != null) {
            autoPlayHandler.removeCallbacksAndMessages(null);
            autoPlayHandler = null;
        }
        super.onDetachedFromWindow(view, recycler);
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        totalHeight = getTotalHeight(recycler);
        totalWidth = getTotalWidth(recycler);
        layoutChildByPosition(0, recycler, state);
    }

    @Override
    public boolean canScrollHorizontally() {
        return orientation == RecyclerView.HORIZONTAL;
    }

    @Override
    public boolean canScrollVertically() {
        return orientation == RecyclerView.VERTICAL;
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (orientation == RecyclerView.HORIZONTAL) {
            layoutChildByPosition(-dx, recycler, state);
            return dx;
        } else {
            return 0;
        }
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (orientation == RecyclerView.VERTICAL) {
            layoutChildByPosition(-dy, recycler, state);
            return dy;
        } else {
            return 0;
        }
    }

    @Override
    public boolean isAutoMeasureEnabled() {
        return true;
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        View viewByPosition = findViewByPosition(position);
        if (viewByPosition != null) {
            if (orientation == RecyclerView.VERTICAL) {
                recyclerView.smoothScrollBy(0, viewByPosition.getMeasuredHeight(), new AccelerateDecelerateInterpolator(), animationDuration);
            } else {
                recyclerView.smoothScrollBy(viewByPosition.getMeasuredWidth(), 0, new AccelerateDecelerateInterpolator(), animationDuration);
            }
        }
    }

    //布局child view
    private void layoutChildByPosition(int distance, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getItemCount() <= 0 || state.isPreLayout()) {
            return;
        }
        // 先移除所有view
        detachAndScrapAttachedViews(recycler);
        //滑动时 取消自动滑动
        if (autoPlayHandler != null) {
            autoPlayHandler.removeCallbacksAndMessages(null);
        }
        //重置第一个可见的item
        firstVisibleItemPosition = -1;
        offset += distance;
        //垂直滑动
        if (orientation == RecyclerView.VERTICAL) {
            //判断当前偏移量，修正偏移量。实现无限轮播
            if (offset > 0) {
                offset -= totalHeight;
            }
            if (offset <= -totalHeight) {
                offset += totalHeight;
            }
            //计算第一个item的top
            int top = getPaddingTop() + offset;
            //循环添加需要显示的item
            for (int i = 0; i < getItemCount(); i++) {
                View child = recycler.getViewForPosition(i);
                measureChildWithMargins(child, 0, 0);
                //判断是否在显示区域，在则添加（addView）
                if (top > -child.getMeasuredHeight() && top < getHeight()) {
                    //记录第一个可见的item
                    if (firstVisibleItemPosition == -1) {
                        firstVisibleItemPosition = i;
                    }
                    addView(child);
                    layoutDecoratedWithMargins(child, getPaddingStart(), top, getPaddingStart() + child.getMeasuredWidth(), top + child.getMeasuredHeight());
                    top += child.getMeasuredHeight();
                    //判断最后item。是否填充满屏幕了，while循环从0开始添加，直到充满屏幕
                    if (i == getItemCount() - 1) {
                        int viewPosition = 0;
                        while (top < getHeight()) {
                            //判断下标如果超过总数量，则重新从0开始
                            if (viewPosition > getItemCount() - 1) {
                                viewPosition = 0;
                            }
                            View cycleView = recycler.getViewForPosition(viewPosition);
                            measureChildWithMargins(cycleView, 0, 0);
                            addView(cycleView);
                            layoutDecoratedWithMargins(cycleView, getPaddingStart(), top, getPaddingStart() + cycleView.getMeasuredWidth(), top + cycleView.getMeasuredHeight());
                            viewPosition++;
                            top += cycleView.getMeasuredHeight();
                        }
                    }
                } else {//不在显示区域，回收
                    top += child.getMeasuredHeight();
                    removeAndRecycleView(child, recycler);
                }
            }
        } else {//水平滑动
            //判断当前偏移量，修正偏移量。实现无限轮播
            if (offset > 0) {
                offset -= totalWidth;
            }
            if (offset <= -totalWidth) {
                offset += totalWidth;
            }
            //计算第一个item的top
            int left = getPaddingStart() + offset;
            for (int i = 0; i < getItemCount(); i++) {
                View child = recycler.getViewForPosition(i);
                measureChildWithMargins(child, 0, 0);
                //判断是否在显示区域，在则添加（addView）
                if (left > -child.getMeasuredWidth() && left < getWidth()) {
                    //记录第一个可见的item
                    if (firstVisibleItemPosition == -1) {
                        firstVisibleItemPosition = i;
                    }
                    addView(child);
                    layoutDecoratedWithMargins(child, left, getPaddingTop(), left + child.getMeasuredWidth(), getPaddingTop() + child.getMeasuredHeight());
                    left += child.getMeasuredWidth();
                    //判断最后item。是否填充满屏幕了，while循环从0开始添加，直到充满屏幕
                    if (i == getItemCount() - 1) {
                        int viewPosition = 0;
                        while (left < getWidth()) {
                            //判断下标如果超过总数量，则重新从0开始
                            if (viewPosition > getItemCount() - 1) {
                                viewPosition = 0;
                            }
                            View cycleView = recycler.getViewForPosition(viewPosition);
                            measureChildWithMargins(cycleView, 0, 0);
                            addView(cycleView);
                            layoutDecoratedWithMargins(cycleView, left, getPaddingTop(), left + cycleView.getMeasuredWidth(), getPaddingTop() + cycleView.getMeasuredHeight());
                            viewPosition++;
                            left += cycleView.getMeasuredWidth();
                        }
                    }
                } else {
                    left += child.getMeasuredWidth();
                    removeAndRecycleView(child, recycler);
                }
            }
        }
        if (isAutoPlay && autoPlayHandler != null) {
            autoPlayHandler.sendEmptyMessageDelayed(firstVisibleItemPosition, duration);
        }
    }

    public int findFirstVisibleItemPosition() {
        return firstVisibleItemPosition;
    }

    //获取总高度
    private int getTotalHeight(RecyclerView.Recycler recycler) {
        int height = getPaddingTop() + getPaddingBottom();
        for (int i = 0; i < getItemCount(); i++) {
            View child = recycler.getViewForPosition(i);
            measureChildWithMargins(child, 0, 0);
            height += child.getMeasuredHeight();
        }
        return height;
    }

    //获取总宽度
    private int getTotalWidth(RecyclerView.Recycler recycler) {
        int width = getPaddingStart() + getPaddingEnd();
        for (int i = 0; i < getItemCount(); i++) {
            View child = recycler.getViewForPosition(i);
            measureChildWithMargins(child, 0, 0);
            width += child.getMeasuredWidth();
            removeAndRecycleView(child, recycler);
        }
        return width;
    }


    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setAnimationDuration(int animationDuration) {
        this.animationDuration = animationDuration;
    }

    public void setAutoCenter(boolean autoCenter) {
        isAutoCenter = autoCenter;
    }

    public void setAutoPlay(boolean autoPlay) {
        isAutoPlay = autoPlay;
    }

    public void onResume() {
        if (isAutoPlay) {
            autoPlayHandler.sendEmptyMessageDelayed(firstVisibleItemPosition, duration);
        }
    }

    public void onPause() {
        if (autoPlayHandler != null) {
            autoPlayHandler.removeCallbacksAndMessages(null);
        }
    }

    public void play() {
        isAutoPlay = true;
    }

    public void stop() {
        isAutoPlay = false;
    }
}
