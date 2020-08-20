package com.ts_xiaoa.ts_recycler_view

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * create by ts_xiaoA on 2020-07-24 15:34
 * email：443502578@qq.com
 * desc：
 */
fun RecyclerView.addItemDecorationOffsets(decoration: (outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) -> Unit) {
    this.addItemDecoration(object : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            decoration.invoke(outRect, view, parent, state)
        }
    })
}