package com.sheryv.tubegripper.ui.main

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.sheryv.tubegripper.ui.BaseViewModel
import kotlinx.coroutines.experimental.handleCoroutineException
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.doAsyncResult
import org.jetbrains.anko.onComplete
import org.jetbrains.anko.uiThread

class MainViewModel : BaseViewModel() {



    init {
        println(this::class.java.simpleName + " constructor")
    }

    var data: MutableLiveData<String> = MutableLiveData()

    override fun viewCreated() {
        val s = doAsyncResult {
            Thread.sleep(4000)
//            uiThread {
//                data.value = "asdas"
//            }
            onComplete {
                data.value = "asdas"
            }
        }
    }

}
