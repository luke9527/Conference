package com.luke.lukeutils

import android.content.Context
import android.widget.Toast


/**
 * Created by luke on 2016/10/18.
 */
object ToastUtils {
    fun showToast(context: Context, info: String) {
        BaseToast.makeText(context, info, Toast.LENGTH_SHORT)!!.show()

    }

    fun showToast(context: Context, info: String, gravity: Int) {
        BaseToast.makeText(context, info, Toast.LENGTH_SHORT)!!.setGravity(gravity, 0, 0)

    }
}
