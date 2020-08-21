package com.ts_xiaoa.ts_ktx.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * create by ts_xiaoA on 2020-08-21 09:26
 * email：443502578@qq.com
 * desc：通用ViewModel
 */
class SimpleViewModel<T> : ViewModel() {

    val data = MutableLiveData<T>()
    val value: T?
        get() = data.value

    open class TsViewModelFactory<Data> : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            val viewModel = SimpleViewModel<Data>()
            return viewModel as T
        }
    }
}