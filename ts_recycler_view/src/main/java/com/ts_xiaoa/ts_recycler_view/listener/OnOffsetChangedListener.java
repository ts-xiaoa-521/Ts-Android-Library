package com.ts_xiaoa.ts_recycler_view.listener;

/**
 * create by ts_xiaoA on 2020-07-22 15:27
 * email：443502578@qq.com
 * desc：
 */
public interface OnOffsetChangedListener {
    /**
     * 滑动回调
     *
     * @param offsetX        偏移比例
     * @param targetPosition 目标位置
     */
    void onOffset(float offsetX, int targetPosition);
}
