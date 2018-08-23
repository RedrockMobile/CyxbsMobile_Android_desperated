package com.mredrock.cyxbs.freshman.utils.kt

import android.util.Log

/**
 * Created by Hosigus on 2018/8/8.
 * 日志输出等级
 */

enum class Level {
    ALL, VERBOSE, DEBUG, INFO, WARN, ERROR, NOTHING
}

/**
 * 用KT写这个文件的主要原因就是重载运算符，
 * 这让我可以用'>='来比较日志等级。
 * 但是其他地方就正常使用就好了，
 * 实在不懂可以看LogUtils是怎么处理的，那就相当于一个例子
 * @see LogUtils
 */
class LogBuilder(private val tag: String, var level: Level) {

    fun v(msg: String) {
        if (level <= Level.VERBOSE) {
            Log.v(tag, msg)
        }
    }

    fun d(msg: String) {
        if (level <= Level.DEBUG) {
            Log.d(tag, msg)
        }
    }

    fun i(msg: String) {
        if (level <= Level.INFO) {
            Log.i(tag, msg)
        }
    }

    fun w(msg: String) {
        if (level <= Level.WARN) {
            Log.w(tag, msg)
        }
    }

    fun e(msg: String) {
        if (level <= Level.ERROR) {
            Log.e(tag, msg)
        }
    }

}