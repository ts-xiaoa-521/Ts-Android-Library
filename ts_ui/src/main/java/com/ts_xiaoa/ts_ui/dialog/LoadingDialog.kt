package com.ts_xiaoa.ts_ui.dialog
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.animation.LinearInterpolator
import com.ts_xiaoa.ts_ui.base.BaseDialogFragment
import com.ts_xiaoa.ts_ui.R
import com.ts_xiaoa.ts_ui.databinding.TsDialogLoadingBinding

/**
 * Created by ts_xiaoA on 2019/9/23 10:08
 * E-Mail Address：443502578@qq.com
 * Desc: 加载中的弹框
 */
class LoadingDialog : BaseDialogFragment() {

    private var rotation: ObjectAnimator? = null
    private val binding: TsDialogLoadingBinding by lazy {
        rootBinding as TsDialogLoadingBinding
    }
    override fun getLayoutId(): Int = R.layout.ts_dialog_loading

    override fun initView(savedInstanceState: Bundle?) {
        ObjectAnimator.ofFloat()
        rotation = ObjectAnimator.ofFloat(binding.ivLoading, "rotation", 0f, 360f)
        rotation!!.duration = 2000
        rotation!!.interpolator = LinearInterpolator()
        rotation!!.repeatCount = -1
        rotation!!.repeatMode = ValueAnimator.RESTART
    }

    override fun getDimAmount() = 0f

    override fun onStart() {
        super.onStart()
        rotation!!.start()
    }

    override fun onResume() {
        super.onResume()
        rotation!!.resume()
    }

    override fun onPause() {
        super.onPause()
        rotation!!.pause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        rotation!!.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onDetach() {
        super.onDetach()
    }
}