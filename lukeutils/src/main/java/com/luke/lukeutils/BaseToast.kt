package com.luke.lukeutils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast


/**
 * Created by luke on 2017/6/26.
 */
class BaseToast private constructor(context: Context, text: CharSequence, duration: Int) {
    private  val mToast: Toast by lazy { Toast(context) }

    init {
        val v = LayoutInflater.from(context).inflate(R.layout.toast, null)
        val textView = v.findViewById<View>(R.id.textView1) as TextView
        textView.text = text
        mToast.duration = duration
        mToast.view = v
    }

    fun show() {
        mToast?.show()
    }

    fun setGravity(gravity: Int, xOffset: Int, yOffset: Int): BaseToast? {
        mToast?.setGravity(gravity, xOffset, yOffset)
        return toast
    }

    companion object {
        private var toast: BaseToast? = null

        fun makeText(context: Context, text: CharSequence, duration: Int): BaseToast? {
            toast = BaseToast(context, text, duration)
            return toast
        }
    }

}
