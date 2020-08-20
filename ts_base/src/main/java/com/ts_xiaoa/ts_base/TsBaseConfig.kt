package com.ts_xiaoa.ts_base

/**
 * create by ts_xiaoA on 2020-08-20 14:38
 * email：443502578@qq.com
 * desc：ts_base库中相关配参数置
 */
class TsBaseConfig {

    //默认屏幕适配宽度375dp
    var densityWidthDp = 375

    //默认SharedPreferences数据库名称
    var shareDbName = "myShare.db"

    companion object {
        //单例
        val instance: TsBaseConfig by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { TsBaseConfig() }
    }
}