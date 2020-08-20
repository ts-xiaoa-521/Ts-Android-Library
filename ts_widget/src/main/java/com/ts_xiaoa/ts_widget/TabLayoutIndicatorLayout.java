package com.ts_xiaoa.ts_widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

/**
 * create by ts_xiaoA on 2019-12-10 15:06
 * email：443502578@qq.com
 * desc：自定义指示器indicator容器
 */
public class TabLayoutIndicatorLayout extends FrameLayout implements ViewTreeObserver.OnScrollChangedListener,
        ViewPager.OnPageChangeListener {
    //需要显示的指示器
    private View indicatorView;
    //绑定的viewpager和tabLayout
    private TabLayout tabLayout;
    private ViewPager viewPager;
    //tabItem的父容器
    private ViewGroup tabParentLayout;
    //记录Indicator的宽度是否为MATCH_PARENT
    private boolean isMatchParent;
    private boolean isPagerScroll;

    public TabLayoutIndicatorLayout(@NonNull Context context) {
        super(context);
    }

    public TabLayoutIndicatorLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TabLayoutIndicatorLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (getChildCount() > 1) {
            throw new IllegalArgumentException("只能有一个indicator布局");
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //得到指示器indicator的视图
        indicatorView = getChildAt(0);
        //判断是否需要Indicator宽度全充满
        switch (indicatorView.getLayoutParams().width) {
            case LayoutParams.MATCH_PARENT:
                isMatchParent = true;//记录是否需要全充满
                break;
            default:
                isMatchParent = false;//记录是否需要全充满
                break;
        }
    }

    public void setupWithViewPager(TabLayout tabLayout, ViewPager viewPager) {
        this.tabLayout = tabLayout;
        this.viewPager = viewPager;
        this.tabLayout.getViewTreeObserver().addOnScrollChangedListener(this);
        this.viewPager.addOnPageChangeListener(this);
        //得到TabItem的父布局
        this.tabParentLayout = (ViewGroup) tabLayout.getChildAt(0);
    }

    /**
     * 监听tabLayout的滑动
     */
    @Override
    public void onScrollChanged() {
        //如果用户只拖动TabLayout,让Indicator的位置跟着TabItem一起动
        if (!isPagerScroll) {
            int tabRelationX = (int) tabParentLayout.getChildAt(viewPager.getCurrentItem()).getX() - tabLayout.getScrollX();
            int tabWidth = tabParentLayout.getChildAt(viewPager.getCurrentItem()).getMeasuredWidth();
            indicatorView.setX(tabRelationX + tabWidth / 2 - indicatorView.getMeasuredWidth() / 2);
        }
    }

    /**
     * 监听viewPager的滑动
     *
     * @param position
     * @param positionOffset
     * @param positionOffsetPixels
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        isPagerScroll = true;
        //判断当切换完成到最后一个pager时
        if (position == tabParentLayout.getChildCount() - 1) {
            //return,下面107行位置tabParentLayout.getChildAt(position + 1).getMeasuredWidth()会报NullPoint
            return;
        }
        //如果没有tab return
        if (tabParentLayout.getChildCount() == 0) return;
        /* *******************************indicator移动部分*********************************** */
        //得到当前TabItem的X坐标
        int currTabItemX = (int) tabParentLayout.getChildAt(position).getX();
        //得到当前TabItem的宽度
        int currTabItemWidth = tabParentLayout.getChildAt(position).getMeasuredWidth();
        //计算出当前Indicator的X 注：没有滑动时indicator的位置X
        int indicatorX = currTabItemX + currTabItemWidth / 2 - indicatorView.getMeasuredWidth() / 2;
        //得到indicator移动到下一个pager的总距离：(当前TabItem的宽度+下一个TabItem的宽度)/2
        int distance = (tabParentLayout.getChildAt(position + 1).getMeasuredWidth() + currTabItemWidth) / 2;
        //计算滑动viewpager时的偏移量：偏移比例 * 滑动到下一个pager的总距离
        int offset = (int) (positionOffset * (distance));
        //得到TabLayout滑动的X
        int tabScrollX = tabLayout.getScrollX();
        //计算得出滑动时indicator的X：原来的X+滑动的偏移量 - TabLayout滑动的X
        indicatorX += offset - tabScrollX;
        //重新设置当前indicator的X
        indicatorView.setX(indicatorX);
        /* *******************************indicator移动部分*********************************** */

        /* *******************************indicator宽度变化部分*********************************** */
        //如果indicator的宽度设置为MATCH_PARENT，每个TabItem的宽度不一样
        if (isMatchParent) {
            int leftWidth = tabParentLayout.getChildAt(position).getMeasuredWidth();
            int rightWidth = tabParentLayout.getChildAt(position + 1).getMeasuredWidth();
            int indicatorWidth = (int) (leftWidth + (rightWidth - leftWidth) * positionOffset);
            ViewGroup.LayoutParams layoutParams = indicatorView.getLayoutParams();
            layoutParams.width = indicatorWidth;
            indicatorView.setLayoutParams(layoutParams);
        }
        /* *******************************indicator宽度变化部分*********************************** */
    }

    @Override
    public void onPageSelected(int i) {
        isPagerScroll = false;
    }

    @Override
    public void onPageScrollStateChanged(int i) {
        /**
         * ViewPager.SCROLL_STATE_IDLE;值为0 表示空闲
         * ViewPager.SCROLL_STATE_DRAGGING;值为1 表示拖动viewPager
         * ViewPager.SCROLL_STATE_SETTLING;值为2 表示停止
         */

        //记录viewpager是否正在滑动
        if (i != ViewPager.SCROLL_STATE_DRAGGING) {
            isPagerScroll = false;
        }
    }
}
