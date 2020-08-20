package com.ts_xiaoa.ts_base.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.Rect
import android.os.Build
import android.provider.Settings
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.*
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.annotation.IntRange
import com.ts_xiaoa.ts_base.R
import com.ts_xiaoa.ts_base.provider.ApplicationProvider
import java.util.*

/**
 * create by ts_xiaoA on 2020-01-17 17:23
 * email：443502578@qq.com
 * desc：状态栏工具类 5.0+ 5.0以下不考虑了
 */
@SuppressLint("PrivateApi")
object StatusBarUtil {

    //深色模式全屏
    fun darkFull(activity: Activity, immersionView: View? = null) {
        fullScreen(activity)
        AndroidBug5497Workaround.assistActivity(activity)
        setDarkMode(activity)
        setImmersionView(immersionView)
    }

    //高亮模式全屏
    fun lightFull(activity: Activity, immersionView: View? = null) {
        fullScreen(activity)
        AndroidBug5497Workaround.assistActivity(activity)
        setLightMode(activity)
        setImmersionView(immersionView)
    }

    /**
     * 设置全屏显示
     *
     * @param activity activity
     */
    fun fullScreen(activity: Activity) {
        val window = activity.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        //清除半透明
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        //全透明效果
        val decorView = window.decorView
        //View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN 布局扩展到状态栏后面
        val visibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        decorView.systemUiVisibility = visibility
        //设置状态栏颜色全透明
        window.statusBarColor = Color.TRANSPARENT
    }

    /**
     * 设置高亮模式（黑色字体）
     *
     * @param activity activity
     */
    fun setLightMode(activity: Activity) {
        //6.0+ 设置黑色字体
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val window = activity.window
            val decorView = window.decorView
            var systemUiVisibility = decorView.systemUiVisibility
            systemUiVisibility =
                systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            decorView.systemUiVisibility = systemUiVisibility
        } else {
            val manufacturer = Build.MANUFACTURER
            if (manufacturer == "Meizu") {
                setMeizuStatusBarDarkIcon(activity, true)
            } else if (manufacturer == "Xiaomi") {
                setMIUIStatusBarDarkIcon(activity, true)
            } else {
                //6.0以下不支持设置黑色字体 设置半透明避免状态栏不显示
                setStatusBarColor(activity, Color.BLACK)
            }
        }
    }

    /**
     * 设置深色模式（白色字体）
     *
     * @param activity activity
     */
    fun setDarkMode(activity: Activity) {
        //6.0+ 设置黑色字体
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val window = activity.window
            val decorView = window.decorView
            var systemUiVisibility = decorView.systemUiVisibility
            systemUiVisibility =
                systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            systemUiVisibility =
                systemUiVisibility xor View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            decorView.systemUiVisibility = systemUiVisibility
        } else {
            //6.0以下原生android不支持修改 魅族小米支持
            val manufacturer = Build.MANUFACTURER
            if (manufacturer == "Meizu") {
                setMeizuStatusBarDarkIcon(activity, false)
            } else if (manufacturer == "Xiaomi") {
                setMIUIStatusBarDarkIcon(activity, false)
            }
        }
    }

    /**
     * ...加个paddingTop
     * 设置状态沉浸的View
     *
     * @param activity      activityg
     * @param immersionView 状态栏背景与该view同步(加padding的View)
     */
    fun setImmersionView(
        immersionView: View?
    ) {
        immersionView?.let {
            if (immersionView.layoutParams.height != ViewGroup.LayoutParams.MATCH_PARENT
                && immersionView.layoutParams.height != ViewGroup.LayoutParams.WRAP_CONTENT
            ) {
                immersionView.layoutParams.height =
                    immersionView.layoutParams.height + getStatusBarHeight()
            }
            immersionView.setPadding(
                immersionView.paddingLeft,
                immersionView.paddingTop + getStatusBarHeight(),
                immersionView.paddingRight,
                immersionView.paddingBottom
            )
        }
    }

    fun changeImmersionView(oldView: View?, immersionView: View?) {
        oldView?.let {
            if (it.layoutParams.height != ViewGroup.LayoutParams.MATCH_PARENT
                && it.layoutParams.height != ViewGroup.LayoutParams.WRAP_CONTENT
            ) {
                it.layoutParams.height =
                    it.layoutParams.height - getStatusBarHeight()
            }
            it.setPadding(
                it.paddingLeft,
                it.paddingTop - getStatusBarHeight(),
                it.paddingRight,
                it.paddingBottom
            )
        }
        immersionView?.let {
            if (it.layoutParams.height != ViewGroup.LayoutParams.MATCH_PARENT
                && it.layoutParams.height != ViewGroup.LayoutParams.WRAP_CONTENT
            ) {
                it.layoutParams.height =
                    it.layoutParams.height + getStatusBarHeight()
            }
            it.setPadding(
                it.paddingLeft,
                it.paddingTop + getStatusBarHeight(),
                it.paddingRight,
                it.paddingBottom
            )
        }
    }


    /**
     * 修改 MIUI V6  以上状态栏颜色
     */
    private fun setMIUIStatusBarDarkIcon(
        activity: Activity,
        darkIcon: Boolean
    ) {
        val clazz: Class<out Window?> = activity.window.javaClass
        try {
            val layoutParams =
                Class.forName("android.view.MiuiWindowManager\$LayoutParams")
            val field =
                layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
            val darkModeFlag = field.getInt(layoutParams)
            val extraFlagField = clazz.getMethod(
                "setExtraFlags",
                Int::class.javaPrimitiveType,
                Int::class.javaPrimitiveType
            )
            extraFlagField.invoke(
                activity.window,
                if (darkIcon) darkModeFlag else 0,
                darkModeFlag
            )
        } catch (e: Exception) {
            //e.printStackTrace();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                activity.window.statusBarColor = Color.BLACK
            }
        }
    }

    /**
     * 修改魅族状态栏字体颜色 Flyme 4.0
     */
    private fun setMeizuStatusBarDarkIcon(
        activity: Activity,
        darkIcon: Boolean
    ) {
        try {
            val lp = activity.window.attributes
            val darkFlag =
                WindowManager.LayoutParams::class.java.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON")
            val meizuFlags =
                WindowManager.LayoutParams::class.java.getDeclaredField("meizuFlags")
            darkFlag.isAccessible = true
            meizuFlags.isAccessible = true
            val bit = darkFlag.getInt(null)
            var value = meizuFlags.getInt(lp)
            value = if (darkIcon) {
                value or bit
            } else {
                value and bit.inv()
            }
            meizuFlags.setInt(lp, value)
            activity.window.attributes = lp
        } catch (e: java.lang.Exception) {
            //e.printStackTrace();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                activity.window.statusBarColor = Color.BLACK
            }
        }
    }

    /**
     * 设置状态栏颜色及透明度
     *
     * @param activity activity
     * @param color    状态颜色
     * @param alpha    透明度
     */
    fun setStatusBarColor(
        activity: Activity, @ColorInt color: Int,
        @IntRange(from = 0, to = 255) alpha: Int = 100
    ) {
        val contentFrameLayout =
            activity.findViewById<ViewGroup>(Window.ID_ANDROID_CONTENT)
        var view: StatusBarView? = null
        for (i in 0 until contentFrameLayout.childCount) {
            if (contentFrameLayout.getChildAt(i) is StatusBarView) {
                view =
                    contentFrameLayout.getChildAt(i) as StatusBarView
                break
            }
        }
        if (view == null) {
            view = StatusBarView(activity)
            val layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                getStatusBarHeight()
            )
            view.layoutParams = layoutParams
            contentFrameLayout.addView(view)
        }
        view.setBackgroundColor(color)
        view.background.alpha = alpha
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    private fun getStatusBarHeight(): Int {
        var result = 0
        //获取状态栏高度的资源id
        val resourceId =
            ApplicationProvider.application.resources.getIdentifier(
                "status_bar_height",
                "dimen",
                "android"
            )
        if (resourceId > 0) {
            result = ApplicationProvider.application.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }


    /**
     * 用于设置状态栏颜色的占位View 其实就是一个View
     */
    private class StatusBarView : View {
        constructor(context: Context?) : super(context)
        constructor(context: Context?, attrs: AttributeSet?) : super(
            context,
            attrs
        )

        constructor(
            context: Context?,
            attrs: AttributeSet?,
            defStyleAttr: Int
        ) : super(context, attrs, defStyleAttr)
    }

    class AndroidBug5497Workaround//如果两次高度不一致
    //将计算的可视高度设置成视图的高度
    //请求重新布局
    private constructor(activity: Activity) {

//        private var mChildOfContent: View? = null
//        private var usableHeightPrevious = 0
//        private var frameLayoutParams: ViewGroup.LayoutParams? = null

        private var mChildOfContent: View? = null
        private var usableHeightPrevious = 0
        private var frameLayoutParams: FrameLayout.LayoutParams? = null
        private var contentHeight = 0
        private var isfirst = true
        private var statusBarHeight = 0

        companion object {
            fun assistActivity(activity: Activity) {
                AndroidBug5497Workaround(activity)
            }
        }

        init {
            //获取状态栏的高度
            val resourceId = activity.resources.getIdentifier("status_bar_height", "dimen", "android")
            statusBarHeight = activity.resources.getDimensionPixelSize(resourceId)
            val content = activity.findViewById<View>(R.id.content) as FrameLayout
            mChildOfContent = content.getChildAt(0)
            val attributes: WindowManager.LayoutParams = activity.window.attributes
            if (attributes.softInputMode and WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE == WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE) {
                mChildOfContent?.viewTreeObserver?.addOnGlobalLayoutListener {
                    if (isfirst) {
                        contentHeight = mChildOfContent!!.height //兼容华为等机型
                        isfirst = false
                    }
                    possiblyResizeChildOfContent()
                }
                frameLayoutParams = mChildOfContent?.layoutParams as FrameLayout.LayoutParams
//                frameLayoutParams = mChildOfContent?.layoutParams
            }
        }

        //重新调整跟布局的高度
        private fun possiblyResizeChildOfContent() {
            val usableHeightNow = computeUsableHeight()

            //当前可见高度和上一次可见高度不一致 布局变动
            if (usableHeightNow != usableHeightPrevious) {
                //int usableHeightSansKeyboard2 = mChildOfContent.getHeight();//兼容华为等机型
                val usableHeightSansKeyboard = mChildOfContent!!.rootView.height
                val heightDifference = usableHeightSansKeyboard - usableHeightNow
                if (heightDifference > usableHeightSansKeyboard / 4) {
                    // keyboard probably just became visible
                    frameLayoutParams!!.height = usableHeightSansKeyboard - heightDifference
//                    frameLayoutParams!!.height = usableHeightSansKeyboard - heightDifference + statusBarHeight
                } else {
                    frameLayoutParams!!.height = contentHeight
                }
                mChildOfContent!!.requestLayout()
                usableHeightPrevious = usableHeightNow
            }
        }


        private fun computeUsableHeight(): Int {
            //计算视图可视高度
            val r = Rect()
            mChildOfContent!!.getWindowVisibleDisplayFrame(r)
            return r.bottom
        }

        /**
         * 获取导航栏高度
         *
         * @param context 上下文对象
         * @return
         */
        private fun getNavigationBarHeight(context: Context): Int {
            //如果小米手机 开启了全面屏手势隐藏了导航栏则返回 0
            if (Settings.Global.getInt(context.contentResolver, "force_fsg_nav_bar", 0) != 0) {
                return 0
            }
            val realHeight: Int = getScreenSize(context)[1]
            val d =
                (Objects.requireNonNull(context.getSystemService(Context.WINDOW_SERVICE)) as WindowManager)
                    .defaultDisplay
            val displayMetrics = DisplayMetrics()
            d.getMetrics(displayMetrics)
            val displayHeight = displayMetrics.heightPixels
            return realHeight - displayHeight
        }

        private fun getScreenSize(context: Context): IntArray {
            val size = IntArray(2)
            val w =
                context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val d = Objects.requireNonNull(w).defaultDisplay
            val metrics = DisplayMetrics()
            d.getMetrics(metrics)
            // since SDK_INT = 1;
            var widthPixels = metrics.widthPixels
            var heightPixels = metrics.heightPixels
            try {
                val realSize = Point()
                Display::class.java.getMethod("getRealSize", Point::class.java)
                    .invoke(d, realSize)
                widthPixels = realSize.x
                heightPixels = realSize.y
            } catch (ignored: java.lang.Exception) {
            }
            size[0] = widthPixels
            size[1] = heightPixels
            return size
        }
    }

}