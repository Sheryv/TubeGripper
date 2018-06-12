package com.sheryv.tubegripper.ui

import android.app.Activity
import android.app.Application
import android.util.Log
import android.view.View
import java.lang.ref.WeakReference


class Logger {

    private val listener: WeakReference<OnMsgReceived?>
    private val app: WeakReference<Application>
    private val startTime: Long = System.currentTimeMillis()
    public val minLogLevel: LogLevel

    constructor(application: Application, listener: OnMsgReceived? = null) {
        this.listener = WeakReference(listener)
        app = WeakReference(application)
        this.minLogLevel = LogLevel.DEBUG
    }



    fun d(s: String, sender: Any? = null, offset: Int = 0) {
        if (LogLevel.DEBUG.moreVerbose(minLogLevel)) {
            val id = sender(sender, offset)

            Log.d(String.format("> %.1f | %s== =>", getSecFromStart(), id), logging(s, null))
        }
    }



    fun i(s: String, sender: Any? = null, offset: Int = 0) {
        if (LogLevel.INFO.moreVerbose(minLogLevel)) {
            val id = sender(sender, offset)
            Log.i(String.format("> %.1f | %s** =>", getSecFromStart(), id), logging(s, null))
        }
    }

    fun w(s: String, sender: Any? = null, offset: Int = 0) {
        if (LogLevel.WARN.moreVerbose(minLogLevel)) {
            val id = sender(sender, offset)
            Log.w(String.format("> %.1f | %s@@ =>", getSecFromStart(), id), logging(s, null))
        }
    }


    fun e(s: String, t: Exception?, sender: Any?, offset: Int) {
        if (LogLevel.ERROR.moreVerbose(minLogLevel)) {
            var st: String? = null
            val id = sender(sender, offset)
            if (t == null) {
                Log.e(String.format("> %.1f | %s## =>", getSecFromStart(), id), logging(s, null))
            } else {
                Log.e(String.format("> %.1f | %s## =>", getSecFromStart(), id), logging(s, t), t)
                //  Log.e("> - | ########## =>",t.getMessage());
                st = t.message
                for (tr in t.stackTrace) {
                    st += "\n" + tr.toString()
                }
                Log.e("> ## =>", st)
            }
        }
    }

    private fun logging(s: String, t: Throwable?): String {

        return s
    }

    private fun sender(sender: Any?, offset: Int): String {
        if (sender != null){
            var s:String
            if (sender is Class<*>){
                s=sender.simpleName
            }else
            {
                s=sender::class.java.simpleName
            }
            return "$s[$offset]"
        }
        return ""
    }

    /**
     * Show snackbar for long time with msg
     *
     * @param m message
     * @param v view of caller
     */
    fun msgl(m: String, v: View) {
        listener.get()?.onMsgLongRec(m, v)
    }

    /**
     * Show snackbar for short time with msg
     *
     * @param m message
     * @param v view of caller
     */
    fun msg(m: String, v: View) {
        listener.get()?.onMsgShortRec(m, v)
    }

    /**
     * Show toast for short time with msg
     *
     * @param m message
     */
    fun t(m: String) {
        listener.get()?.onToastShortRec(m, app.get())
    }

    /**
     * Show toast for long time with msg
     *
     * @param m message
     */
    fun tl(m: String) {
        listener.get()?.onToastLongRec(m, app.get())
    }

    /**
     * Change status
     *
     * @param m        message
     * @param activity activity
     */
    fun st(m: String, activity: Activity) {
        listener.get()?.onStatusChange(m, activity)
    }

    private fun getSecFromStart(): Float {
        return (System.currentTimeMillis() - startTime) / 100 / 10.0f
    }

    interface OnMsgReceived {
        fun onTgLogReceived(msg: String, t: Throwable?, application: Application?)

        fun onMsgLongRec(msg: String, v: View)

        fun onMsgShortRec(msg: String, v: View)

        fun onToastShortRec(msg: String, application: Application?)

        fun onToastLongRec(msg: String, application: Application?)

        fun onStatusChange(msg: String, activity: Activity)

        /*
            static void msgl(String m)
            {
                Snackbar.make(null,m, Snackbar.LENGTH_LONG)
                        .setAction("MessageL", null).show();
            }
            static void msg(String m, View v)
            {
                Snackbar.make(v,m, Snackbar.LENGTH_SHORT)
                        .setAction("MessageS", null).show();
            }
            static void t(String m)
            {
                Toast.makeText(getApp(), m,
                        Toast.LENGTH_SHORT).show();
            }
        * */
    }


    enum class LogLevel(private val level: Int) {
        DEBUG(1),
        INFO(2),
        WARN(4),
        DISABLED(128),
        ERROR(8);

        fun moreVerbose(log: LogLevel): Boolean {
            return log.level <= level
        }
    }
}