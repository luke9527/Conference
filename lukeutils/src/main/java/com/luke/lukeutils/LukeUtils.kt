package com.luke.lukeutils

import android.content.Context
import java.io.File

/**
 * Created by Administrator on 2017/8/13.
 */
class LukeUtils {
    companion object {
        var mcontext: Context? = null
        @JvmStatic
        fun init(context: Context) {
            mcontext = context
            FileUtil.init(mcontext)
        }

        @JvmStatic
        fun getContext(): Context {
            if (mcontext == null) {
                throw IllegalAccessException("Context is null")
            } else {
                return mcontext!!
            }
        }
    }

}