package com.sheryv.tubegripper

import android.app.Activity
import android.app.Application
import android.support.design.widget.Snackbar
import android.view.View
import com.sheryv.tubegripper.ui.Logger
import com.sheryv.tubegripper.util.Lc

class App : Application() {

    lateinit var log: Logger

    override fun onCreate() {
        super.onCreate()
        log= Logger(this,  LoggerListener())
        Lc.initialize(this)
    }

    inner class LoggerListener : Logger.OnMsgReceived{
        override fun onTgLogReceived(msg: String, t: Throwable?, application: Application?) {
        }

        override fun onMsgLongRec(msg: String, v: View) {
        }

        override fun onMsgShortRec(msg: String, v: View) {
        }

        override fun onToastShortRec(msg: String, application: Application?) {
        }

        override fun onToastLongRec(msg: String, application: Application?) {
        }

        override fun onStatusChange(msg: String, activity: Activity) {
            Snackbar.make(activity.window.decorView.rootView, msg, Snackbar.LENGTH_LONG)
        }
    }
}