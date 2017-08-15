package com.luke.lukeutils

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.util.Log

/**
 * Created by luke on 2016/9/23.
 */
class LogUtils {
    companion object {
        var LOGV = true
        private var TAG = "LUKE"
        private val LINE = "-行数-"
        private val METHOD = "-方法-"
        private val TEMP = "-内容-"
        val VERBOSE = 2
        val DEBUG = 3
        val INFO = 4
        val WARN = 5
        val ERROR = 6
        val ASSERT = 7

        /**
         * 初始化LogUtils     tag  需要在mainifes配置   istag  配置
         * @param context
         */
        fun initLog(context: Context) {
            var appInfo: ApplicationInfo? = null
            try {
                appInfo = context.packageManager.getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
                e("没有找到配置信息")
            }

            TAG = appInfo!!.metaData.getString("LOG_TAG")
            LOGV = appInfo.metaData.getBoolean("IS_LOG", true)
            Log.d("meta_data", "TAG = $TAG\nLOGV = $LOGV")

        }


        fun v(message: String) {
            if (LOGV) {
                val stackTrace = Throwable().stackTrace[1]
                val info = stackTrace.className+LINE +
                        stackTrace.lineNumber.toString()+
                        METHOD+
                        stackTrace.methodName+
                        TEMP
                printLoger(2, info + message)
            }
        }

        fun d(message: String) {
            val stackTrace = Throwable().stackTrace[1]
            val info = stackTrace.className+
                    LINE+
                    stackTrace.lineNumber+
                    METHOD+
                    stackTrace.methodName+
                    TEMP
            if (LOGV) {
                printLoger(3, info + message)
            }

        }

        fun i(message: String) {
            if (LOGV) {
                val stackTrace = Throwable().stackTrace[1]
                val info = stackTrace.className+
                        LINE+
                        stackTrace.lineNumber+
                        METHOD+
                        stackTrace.methodName+
                        TEMP
                printLoger(4, info + message)
            }

        }

        fun w(message: String) {
            if (LOGV) {
                val stackTrace = Throwable().stackTrace[1]
                val info = stackTrace.className+
                        LINE+
                        stackTrace.lineNumber+
                        METHOD+
                        stackTrace.methodName+
                        TEMP
                printLoger(5, info + message)
            }

        }

        fun e(message: String) {
            if (LOGV) {
                val stackTrace = Throwable().stackTrace[1]
                val info = stackTrace.className+
                        LINE+
                        stackTrace.lineNumber+
                        METHOD+
                        stackTrace.methodName+
                        TEMP
                printLoger(6, info + message)
            }

        }

        fun v(tag: String, message: String) {
            if (LOGV) {
                val stackTrace = Throwable().stackTrace[1]
                val info = stackTrace.className+
                        LINE+
                        stackTrace.lineNumber+
                        METHOD+
                        stackTrace.methodName+
                        TEMP
                printLoger(2, tag, info + message)
            }

        }

        fun d(tag: String, message: String) {
            if (LOGV) {
                val stackTrace = Throwable().stackTrace[1]
                val info = stackTrace.className+
                        LINE+
                        stackTrace.lineNumber+
                        METHOD+
                        stackTrace.methodName+
                        TEMP
                printLoger(3, tag, info + message)
            }

        }

        fun i(tag: String, message: String) {
            if (LOGV) {
                val stackTrace = Throwable().stackTrace[1]
                val info = stackTrace.className+
                        LINE+
                        stackTrace.lineNumber+
                        METHOD+
                        stackTrace.methodName+
                        TEMP
                printLoger(4, tag, info + message)
            }

        }

        fun w(tag: String, message: String) {
            if (LOGV) {
                val stackTrace = Throwable().stackTrace[1]
                val info = stackTrace.className+
                        LINE+
                        stackTrace.lineNumber+
                        METHOD+
                        stackTrace.methodName+
                        TEMP
                printLoger(5, tag, info + message)
            }

        }

        fun e(tag: String, message: String) {
            if (LOGV) {
                val stackTrace = Throwable().stackTrace[1]
                val info = stackTrace.className+
                        LINE+
                        stackTrace.lineNumber+
                        METHOD+
                        stackTrace.methodName+
                        TEMP
                printLoger(6, tag, info + message)
            }

        }


        private fun printLoger(priority: Int, tag: String, message: String) {
            when (priority) {

                2 ->

                    Log.v(tag, TEMP + message)
                3 -> Log.d(tag, TEMP + message)
                4 -> Log.i(tag, TEMP + message)
                5 -> Log.w(tag, TEMP + message)
                6 -> Log.e(tag, TEMP + message)
            }
        }


        private fun printLoger(priority: Int, message: String) {
            when (priority) {

                2 -> Log.v(TAG, message)
                3 -> Log.d(TAG, message)
                4 -> Log.i(TAG, message)
                5 -> Log.w(TAG, message)
                6 -> Log.e(TAG, message)
            }
        }
    }
}