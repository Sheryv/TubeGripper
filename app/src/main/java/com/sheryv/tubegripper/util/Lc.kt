package com.sheryv.tubegripper.util

import android.app.Activity
import android.view.View
import com.sheryv.tubegripper.App

object Lc {

    public lateinit var app: App
    @JvmStatic
    fun initialize(app: App) {
        this.app = app
    }

    fun d(s: String, sender: Any? = null, offset: Int = 0) {
        app.log.d(s, sender, offset)
    }

    fun i(s: String, sender: Any? = null, offset: Int = 0) {
        app.log.i(s, sender, offset)
    }

    fun w(s: String, sender: Any? = null, offset: Int = 0) {
        app.log.w(s, sender, offset)
    }

    fun e(s: String, t: Exception?, sender: Any? = null, offset: Int = 0) {
        app.log.e(s, t, sender, offset)
    }

    /**
     * Show snackbar for long time with msg
     *
     * @param m message
     * @param v view of caller
     */
    fun msgl(m: String, v: View) {
        app.log.msgl(m, v)

    }

    /**
     * Show snackbar for short time with msg
     *
     * @param m message
     * @param v view of caller
     */
    fun msg(m: String, v: View) {
        app.log.msg(m, v)

    }

    /**
     * Show toast for short time with msg
     *
     * @param m message
     */
    fun t(m: String) {
        app.log.t(m)

    }

    /**
     * Show toast for long time with msg
     *
     * @param m message
     */
    fun tl(m: String) {
        app.log.tl(m)

    }

    /**
     * Change status
     *
     * @param m        message
     * @param activity activity
     */
    fun st(m: String, activity: Activity) {
        app.log.st(m, activity)
    }


}