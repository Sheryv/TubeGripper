package com.sheryv.tubegripper.ui.add

import android.arch.lifecycle.MutableLiveData
import com.sheryv.tubegripper.Converter
import com.sheryv.tubegripper.R
import com.sheryv.tubegripper.model.Item
import com.sheryv.tubegripper.ui.BaseViewModel
import com.sheryv.tubegripper.util.Lc
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.onComplete
import java.io.BufferedInputStream

class AddViewModel : BaseViewModel() {


    override fun viewCreated() {
    }

    val item: MutableLiveData<Item> = MutableLiveData()

    fun loadVideoInfoAsync(link: String) {
        if (item.value != null)
            return
//        val r = (BufferedInputStream(Lc.app.resources.openRawResource(R.raw.data))).bufferedReader()
        doAsync {
            //            var line: String?
//            var s = ""
//            r.use {
//                line = r.readLine()
//                while (line != null) {
//                    s += line + "\n"
//                    line = r.readLine()
//                }
//            }
            val c = Converter()
            val i = c.convert(link)
            onComplete {
                item.value = i
            }
        }
    }


}