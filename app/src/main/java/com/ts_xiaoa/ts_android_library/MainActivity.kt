package com.ts_xiaoa.ts_android_library

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.ts_xiaoa.ts_glide.loadCenterCropImage
import com.ts_xiaoa.ts_permission.requestPermissionsForResult
import com.ts_xiaoa.ts_retrofit.TsNetConfig
import com.ts_xiaoa.ts_retrofit.bean.INetResult
import com.ts_xiaoa.ts_retrofit.helper.awaitNet
import com.ts_xiaoa.ts_retrofit.interceptor.ResultCodeInterceptor
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //动态权限
        btn_permission.setOnClickListener {
            TsNetConfig.instance.addNetResultInterceptor(object : ResultCodeInterceptor {
                override fun getCode(): Int {
                    return 405
                }

                override fun interceptResult(result: INetResult<*>): Boolean {
                    Toast.makeText(this@MainActivity, result.getResultMessage(), Toast.LENGTH_SHORT)
                        .show()
                    return true
                }
            })
            lifecycleScope.launch {
                val result = async {
                    delay(500)
                    NetResult<String>().apply {
                        data = "这是数据结果"
                        code = 200
                        message = "code is $code "
                    }
                }
                result.awaitNet()?.data?.let {
                    Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT)
                        .show()
                }
                val isGrant =
                    requestPermissionsForResult(Manifest.permission.WRITE_EXTERNAL_STORAGE)

                if (isGrant) {
                    Log.e("msg", "授权成功")
                } else {
                    Log.e("msg", "授权失败")
                }

                Toast.makeText(this@MainActivity, "是否授权成功：${isGrant}", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        val imageUrl =
            "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3773660706,637234383&fm=26&gp=0.jpg"
        iv_cover.loadCenterCropImage(imageUrl)
    }
}