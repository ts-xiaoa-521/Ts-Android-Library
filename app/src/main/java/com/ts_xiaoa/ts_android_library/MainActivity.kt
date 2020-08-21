package com.ts_xiaoa.ts_android_library

import android.os.Bundle
import com.ts_xiaoa.ts_ui.helper.LayoutConfig

class MainActivity : BaseActivity() {
    override fun getLayoutConfig(): LayoutConfig {
        return LayoutConfig(R.layout.activity_main, hasAppbarLayout = true)
    }

    override fun onInit(savedInstanceState: Bundle?) {
        title = "主界面"
    }
}