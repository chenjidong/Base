package com.kuky.baselib.baseUtils

import android.util.Log
import com.kuky.baselib.BuildConfig

/**
 * @author Kuky
 */
object LogUtils {
    private var className: String? = null
    private var methodName: String? = null
    private var lineNumber: Int? = null

    private fun isDebuggable(): Boolean {
        return BuildConfig.DEBUG
    }

    private fun createLog(logMsg: String): String {
        return "$methodName($className:$lineNumber): $logMsg"
    }

    private fun getMethodName(throwable: Throwable) {
        className = throwable.stackTrace[1].fileName
        methodName = throwable.stackTrace[1].methodName
        lineNumber = throwable.stackTrace[1].lineNumber
    }

    fun e(msg: String) {
        if (!isDebuggable())
            return
        getMethodName(Throwable())
        Log.e(className, createLog(msg))
    }

    fun w(msg: String) {
        if (!isDebuggable())
            return
        getMethodName(Throwable())
        Log.w(className, createLog(msg))
    }

    fun i(msg: String) {
        if (!isDebuggable())
            return
        getMethodName(Throwable())
        Log.i(className, createLog(msg))
    }

    fun d(msg: String) {
        if (!isDebuggable())
            return
        getMethodName(Throwable())
        Log.d(className, createLog(msg))
    }

    fun v(msg: String) {
        if (!isDebuggable())
            return
        getMethodName(Throwable())
        Log.v(className, createLog(msg))
    }
}