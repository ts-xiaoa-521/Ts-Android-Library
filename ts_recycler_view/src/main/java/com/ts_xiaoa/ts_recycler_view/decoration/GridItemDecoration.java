package com.ts_xiaoa.ts_recycler_view.decoration;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * Created by ts_xiaoA on 2019/10/3 0003
 * E-Mail Address：443502578@qq.com
 * Desc: RecyclerView GridLayoutManager 分割线
 */
public class GridItemDecoration extends RecyclerView.ItemDecoration {

    //横行间距
    private int spaceHorizontal;
    //纵向间距
    private int spaceVertical;
    //分割线颜色（功能没写，哈哈哈哈哈）
    private int color;

    /**
     * 默认的，垂直方向 横纵1px 的分割线 颜色透明
     */
    public GridItemDecoration() {
        this(1);
    }

    /**
     * 自定义宽度的透明分割线
     *
     * @param spaceDP 指定宽度需要穿dp单位 自己转一下
     */
    public GridItemDecoration(int spaceDP) {
        this(spaceDP, Color.TRANSPARENT);
    }

    /**
     * 自定义宽度，并指定颜色的分割线
     *
     * @param space 间距
     * @param color 颜色
     */

    public GridItemDecoration(int space, int color) {
        this(space, space, color);
    }


    public GridItemDecoration(int spaceHorizontal, int spaceVertical, int color) {
        this.spaceHorizontal = spaceHorizontal;
        this.spaceVertical = spaceVertical;
        this.color = color;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        calcVerticalOffset(outRect, view, parent, state);
        calcHorizontalOffset(outRect, view, parent, state);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
    }

    private void calcHorizontalOffset(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        GridLayoutManager manager = (GridLayoutManager) parent.getLayoutManager();
        int spanCount = manager.getSpanCount();
        if (spanCount < 2) return;
        //水平总间距
        float totalHorizontalSpace = 1f * spaceHorizontal * (spanCount - 1);
        //每一个item水平间距
        int itemHorizontalSpace = (int) (totalHorizontalSpace / spanCount);
        //第一列 只需要加右边的间距
        if (parent.getChildLayoutPosition(view) % spanCount == 0) {
            outRect.left = 0;
            outRect.right = itemHorizontalSpace;
        } else if (parent.getChildLayoutPosition(view) % spanCount == spanCount - 1) {
            outRect.left = itemHorizontalSpace;
            outRect.right = 0;
        } else {
            outRect.left = itemHorizontalSpace / 2;
            outRect.right = itemHorizontalSpace / 2;
        }
    }


    private void calcVerticalOffset(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        GridLayoutManager manager = (GridLayoutManager) parent.getLayoutManager();
        int spanCount = manager.getSpanCount();
        int lines = (manager.getItemCount() + spanCount - 1) / spanCount;
        if (lines == 1) return;
        float totalVerticalSpace = 1f * (lines - 1) * spaceVertical;
        int itemVerticalSpace = (int) (totalVerticalSpace / lines);
        int lastLineFirstPosition = (lines - 1) * spanCount;
        if (parent.getChildLayoutPosition(view) < spanCount) {
            outRect.top = 0;
            outRect.bottom = itemVerticalSpace;
        } else if (parent.getChildLayoutPosition(view) > lastLineFirstPosition - 1) {
            outRect.top = itemVerticalSpace;
            outRect.bottom = 0;
        } else {
            outRect.top = itemVerticalSpace / 2;
            outRect.bottom = itemVerticalSpace / 2;
        }
    }
}
