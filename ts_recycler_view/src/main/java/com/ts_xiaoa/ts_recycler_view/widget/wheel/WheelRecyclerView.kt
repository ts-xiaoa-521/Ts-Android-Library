package com.ts_xiaoa.ts_recycler_view.widget.wheel

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper

/**
 * create by ts_xiaoA on 2020-07-30 10:05
 * email：443502578@qq.com
 * desc：
 */
class WheelRecyclerView : RecyclerView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private var snapHelper: SnapHelper = LinearSnapHelper()
    private var layoutManager: LinearLayoutManager

    var adapter: BaseWheelAdapter<*>? = null
        set(value) {
            field = value
            setAdapter(value)
        }
    var selectPosition = -1
        get() {
            if (adapter == null) return field
            else
                adapter!!.let {
                    val lastPosition: Int = field - it.showItemCount / 2
                    return when {
                        lastPosition < 0 -> 0
                        lastPosition >= it.getData().size -> it.getData().size - 1
                        else -> lastPosition
                    }
                }
        }

    var onSelectedChangedListener: ((position: Int) -> Unit)? = null

    init {
        snapHelper.attachToRecyclerView(this)
        layoutManager = object : LinearLayoutManager(context) {
            override fun onLayoutChildren(recycler: Recycler?, state: State?) {
                try {
                    super.onLayoutChildren(recycler, state)
                } catch (e: IndexOutOfBoundsException) {
                    e.printStackTrace()
                }
            }
        }
        //RecyclerView还未初始化完成不能直接调用其方法否则会报空指针，类构造方法中只能进行初始化成员变量
        post {
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    if (newState == SCROLL_STATE_IDLE) {
                        //暂停
                        onSelectedChangedListener?.invoke(selectPosition)
                    }
                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    //获取当前LayoutManager最接近对齐位置的那个view
                    val snapView = snapHelper.findSnapView(layoutManager) ?: return
                    val centerPosition: Int = layoutManager.getPosition(snapView)
                    if (selectPosition == centerPosition) {
                        return
                    }
                    selectPosition = centerPosition
                    adapter?.selectedPosition = centerPosition
                    for (i in 0 until childCount) {
                        val itemView = getChildAt(i)
                        if (itemView == snapView) {
                            adapter?.configSelectedView(itemView)
                        } else {
                            adapter?.configUnSelectedView(itemView)
                        }
                    }
                }
            })
            setLayoutManager(layoutManager)
            //注：去掉item动画(反正也用不到)避免出现Tmp detached view should be removed from RecyclerView before it can be recycled:ViewHolder
            itemAnimator = null
        }
    }
}