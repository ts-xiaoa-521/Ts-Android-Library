package com.ts_xiaoa.ts_android_library

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.ts_xiaoa.ts_glide.loadCenterCropImage
import com.ts_xiaoa.ts_permission.requestPermissionsForResult
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //动态权限
        btn_permission.setOnClickListener {
            lifecycleScope.launch {
                val isGrant =
                    requestPermissionsForResult(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                Toast.makeText(this@MainActivity, "是否授权成功：${isGrant}", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        iv_cover.loadCenterCropImage("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3773660706,637234383&fm=26&gp=0.jpg")
    }
}