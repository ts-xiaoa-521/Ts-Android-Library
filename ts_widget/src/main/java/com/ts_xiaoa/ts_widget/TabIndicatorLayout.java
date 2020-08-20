package com.ts_xiaoa.ts_widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

/**
 * create by ts_xiaoa
 * create Time 2018/8/7
 * description: 自定义指示器indicator容器
 */
public class TabIndicatorLayout extends FrameLayout implements ViewPager.OnPageChangeListener {

    private View indicatorView;//指示器视图
    private int indicatorWidth;//指示器的宽度
    private int pageCount;//页面个数
    private int maxIndicatorOffset;//每次indicator的最大偏移量
    //记录Indicator的宽度是否为MATCH_PARENT
    private boolean isMatchParent;

    public TabIndicatorLayout(@NonNull Context context) {
        super(context);
    }

    public TabIndicatorLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TabIndicatorLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (getChildCount() > 1) {
            throw new IllegalArgumentException("只能有一个indicator布局");
        }
        //得到当前容器（TabIndicatorLayout）的宽度
        //该viewGroup的宽度
        int parentWidth = getMeasuredWidth();
        //得到子View（TabIndicatorLayout内的指示器布局如：三角形、图片等等）
        indicatorView = getChildAt(0);
        //indicator三角形的宽度
        if (indicatorView.getLayoutParams().width == LayoutParams.MATCH_PARENT
                && pageCount > 0) {
            indicatorWidth = getMeasuredWidth() / pageCount;
            ViewGroup.LayoutParams layoutParams = indicatorView.getLayoutParams();
            layoutParams.width = indicatorWidth;
            indicatorView.setLayoutParams(layoutParams);
        } else {
            indicatorWidth = indicatorView.getMeasuredWidth();
        }
        //得到指示器每次最大的偏移量（ViewPager翻页时，指示器滑动的距离）
        if (pageCount != 0) {
            maxIndicatorOffset = parentWidth / pageCount;
            //设置indicator的第一个位置（如：indicator的容器宽度是100，viewpager的数量是5，indicator的宽度是4 那么indicator最开始的X = 100/4/2 - 4/2）
            indicatorView.setX(parentWidth / pageCount / 2 - indicatorWidth / 2);
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

    /**
     * 绑定ViewPager
     *
     * @param viewPager
     */
    public void setUpWithViewPager(ViewPager viewPager) {
        //得到viewpager界面的个数
        pageCount = viewPager.getAdapter().getCount();
        //给ViewPager添加一个监听，监听界面滑动（实现onPageScrolled方法），移动indicator（如：三角形）
        viewPager.addOnPageChangeListener(this);
    }

    /**
     * viewpager滑动时的监听回调
     *
     * @param position             当前pager Item 的位置
     * @param positionOffset       偏移比例 0f - 1f 或 1f-0f
     * @param positionOffsetPixels 偏移的像素
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


        //重新设置indicator（如：三角形）的位置（原来的位置(0.5f + position) * maxIndicatorOffset - indicatorWidth / 2 加上偏移量maxIndicatorOffset * positionOffset）
        //如indicator的容器宽度是100，viewpager的数量是5
        //未偏移（indicator在中间）时 此时indicator的X为：maxIndicatorOffset * （position +0.5f) = maxIndicatorOffset * position+maxIndicatorOffset + maxIndicatorOffset * 0.5f
        //上一句没看懂 我再解释下  如viewpager5个pager 此时在第3个 那么position = 2 此时indicator的x为 maxIndicatorOffset * 2 + maxIndicatorOffset * 0.5
        //注：        你可能忘了  先前提到过 maxIndicatorOffset是每个indicator在viewpager滑动时最大的偏移量  其实也就是indicator的容器宽度除以pager个数 （自己画个图就懂了）
        indicatorView.setX((0.5f + position) * maxIndicatorOffset - indicatorWidth / 2 + maxIndicatorOffset * positionOffset);
        if (isMatchParent) {
            indicatorWidth = getMeasuredWidth() / pageCount;
            ViewGroup.LayoutParams layoutParams = indicatorView.getLayoutParams();
//            layoutParams.width = indicatorWidth / 2;
            layoutParams.width = indicatorWidth;
            indicatorView.setLayoutParams(layoutParams);
        }
    }


    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
