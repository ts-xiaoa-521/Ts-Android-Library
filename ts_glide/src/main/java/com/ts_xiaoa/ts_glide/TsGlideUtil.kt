package com.ts_xiaoa.ts_glide

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

/**
 * create by ts_xiaoA on 2020-08-20 09:29
 * email：443502578@qq.com
 * desc：图片加载相关
 */
//加载图片CenterCrop
fun ImageView.loadCenterCropImage(
    url: Any?,
    radius: Int = 0,
    defaultImagePlaceHolder: Int = TsGlideConfig.instance.defaultImagePlaceHolder,
    defaultImageError: Int = TsGlideConfig.instance.defaultImageError,
    defaultDiskCacheStrategy: DiskCacheStrategy = TsGlideConfig.instance.defaultDiskCacheStrategy
) {
    val options = if (radius == 0) RequestOptions().centerCrop() else {
        RequestOptions.bitmapTransform(
            MultiTransformation(
                CenterCrop(),
                RoundedCorners(radius)
            )
        )
    }
    options.apply {
        this.placeholder(defaultImagePlaceHolder) //占位图
            .error(defaultImageError) //错误图
            .override(this@loadCenterCropImage.width, this@loadCenterCropImage.height)
            .diskCacheStrategy(defaultDiskCacheStrategy)
    }
    Glide.with(context!!).load(url).apply(options).into(this)
}

//加载图片FitCenter
fun ImageView.loadFitCenterImage(
    url: Any?,
    defaultImagePlaceHolder: Int = TsGlideConfig.instance.defaultImagePlaceHolder,
    defaultImageError: Int = TsGlideConfig.instance.defaultImageError,
    defaultDiskCacheStrategy: DiskCacheStrategy = TsGlideConfig.instance.defaultDiskCacheStrategy
) {
    val options = RequestOptions()
        .fitCenter()
        .placeholder(defaultImagePlaceHolder) //占位图
        .error(defaultImageError) //错误图
        .override(this@loadFitCenterImage.width, this@loadFitCenterImage.height)
        .dontAnimate()
        .diskCacheStrategy(defaultDiskCacheStrategy)
    Glide.with(context!!).load(url).apply(options).into(this)
}


//加载头像
fun ImageView.loadHeadImage(
    url: Any?,
    isCircleCrop: Boolean = true,
    defaultImageHead: Int = TsGlideConfig.instance.defaultImageHead,
    defaultDiskCacheStrategy: DiskCacheStrategy = TsGlideConfig.instance.defaultDiskCacheStrategy
) {
    val options = RequestOptions().apply {
        if (isCircleCrop) {
            this.centerCrop()
        }
    }.placeholder(defaultImageHead) //占位图
        .error(defaultImageHead) //错误图
        .override(this@loadHeadImage.width, this@loadHeadImage.height)
        .dontAnimate()
        .diskCacheStrategy(defaultDiskCacheStrategy)
    Glide.with(context!!).load(url).apply(options).into(this)
}


//加载指定视频帧数图片
fun ImageView.loadCenterCropVideoFrame(
    url: Any?,
    frameTime: Long = 1000,
    defaultImagePlaceHolder: Int = TsGlideConfig.instance.defaultImagePlaceHolder,
    defaultImageError: Int = TsGlideConfig.instance.defaultImageError,
    defaultDiskCacheStrategy: DiskCacheStrategy = TsGlideConfig.instance.defaultDiskCacheStrategy
) {
    val options = RequestOptions()
        .centerCrop()
        .placeholder(defaultImagePlaceHolder) //占位图
        .error(defaultImageError) //错误图
        .override(this@loadCenterCropVideoFrame.width, this@loadCenterCropVideoFrame.height)
        .dontAnimate()
        .diskCacheStrategy(defaultDiskCacheStrategy)
    Glide.with(context!!)
        .setDefaultRequestOptions(
            RequestOptions().frame(frameTime * 1000) //单位微秒
        )
        .load(url).apply(options).into(this)
}


//加载指定视频帧数图片
fun ImageView.loadFitCenterVideoFrame(
    url: Any?,
    frameTime: Long = 1000,
    defaultImagePlaceHolder: Int = TsGlideConfig.instance.defaultImagePlaceHolder,
    defaultImageError: Int = TsGlideConfig.instance.defaultImageError,
    defaultDiskCacheStrategy: DiskCacheStrategy = TsGlideConfig.instance.defaultDiskCacheStrategy
) {
    val options = RequestOptions()
        .fitCenter()
        .placeholder(defaultImagePlaceHolder) //占位图
        .error(defaultImageError) //错误图
        .override(this@loadFitCenterVideoFrame.width, this@loadFitCenterVideoFrame.height)
        .dontAnimate()
        .diskCacheStrategy(defaultDiskCacheStrategy)
    Glide.with(context!!)
        .setDefaultRequestOptions(
            RequestOptions().frame(frameTime * 1000) //单位微秒
        )
        .load(url).apply(options).into(this)
}