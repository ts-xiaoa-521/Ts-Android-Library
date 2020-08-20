package com.ts_xiaoa.ts_glide

import com.bumptech.glide.load.engine.DiskCacheStrategy

/**
 * create by ts_xiaoA on 2020-08-20 09:29
 * email：443502578@qq.com
 * desc：
 */
object TsGlideConfig {
    //默认占位图
    var defaultImagePlaceHolder: Int = R.drawable.ts_default_img_place_holder

    //默认加载错误图片
    var defaultImageError: Int = R.drawable.ts_default_img_error

    //默认头像
    var defaultImageHead: Int = R.drawable.ts_default_head

    //默认缓存方式
    var defaultDiskCacheStrategy: DiskCacheStrategy = DiskCacheStrategy.RESOURCE
}