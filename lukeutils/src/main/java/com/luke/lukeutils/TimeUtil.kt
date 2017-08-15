package com.luke.lukeutils


import android.app.Application
import android.content.Context
import android.content.pm.ApplicationInfo

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date


/**
 * 时间转换工具
 */
object TimeUtil {

    /**
     * 时间转化为显示字符串

     * @param timeStamp 单位为秒
     */
    fun getTimeStr(mContext: Context, timeStamp: Long): String {
        if (timeStamp  == 0L) return ""
        val inputTime = Calendar.getInstance()
        inputTime.timeInMillis = timeStamp
        val currenTimeZone = inputTime.time
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        if (calendar.before(inputTime)) {
            //今天23:59在输入时间之前，解决一些时间误差，把当天时间显示到这里
            val sdf = SimpleDateFormat("yyyy" + mContext.resources.getString(R.string.time_year)
                    + "MM"
                    + mContext.resources.getString(R.string.time_month)
                    + "dd"
                    + mContext.resources.getString(R.string.time_day))
            return sdf.format(currenTimeZone)
        }
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        if (calendar.before(inputTime)) {
            val sdf = SimpleDateFormat("HH:mm")
            return sdf.format(currenTimeZone)
        }
        calendar.add(Calendar.DAY_OF_MONTH, -1)
        if (calendar.before(inputTime)) {
            return mContext.resources.getString(R.string.time_yesterday)
        } else {
            calendar.set(Calendar.DAY_OF_MONTH, 1)
            calendar.set(Calendar.MONTH, Calendar.JANUARY)
            if (calendar.before(inputTime)) {
                val sdf = SimpleDateFormat("M" + mContext.resources.getString(R.string.time_month) + "d" + mContext.resources.getString(R.string.time_day))
                return sdf.format(currenTimeZone)
            } else {
                val sdf = SimpleDateFormat("yyyy" + mContext.resources.getString(R.string.time_year) + "MM" + mContext.resources.getString(R.string.time_month) + "dd" + mContext.resources.getString(R.string.time_day))
                return sdf.format(currenTimeZone)

            }

        }

    }

    /**
     * 时间转化为聊天界面显示字符串

     * @param timeStamp 单位为秒
     */
    fun getChatTimeStr(mContext: Context, timeStamp: Long): String {
        if (timeStamp == 0L) return ""
        val inputTime = Calendar.getInstance()
        inputTime.timeInMillis = timeStamp
        val currenTimeZone = inputTime.time
        val calendar = Calendar.getInstance()
        if (!calendar.after(inputTime)) {
            //当前时间在输入时间之前
            val sdf = SimpleDateFormat("yyyy" + mContext.resources.getString(R.string.time_year) + "MM" + mContext.resources.getString(R.string.time_month) + "dd" + mContext.resources.getString(R.string.time_day))
            return sdf.format(currenTimeZone)
        }
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        if (calendar.before(inputTime)) {
            val sdf = SimpleDateFormat("HH:mm")
            return sdf.format(currenTimeZone)
        }
        calendar.add(Calendar.DAY_OF_MONTH, -1)
        if (calendar.before(inputTime)) {
            val sdf = SimpleDateFormat("HH:mm")
            return mContext.resources.getString(R.string.time_yesterday) + " " + sdf.format(currenTimeZone)
        } else {
            calendar.set(Calendar.DAY_OF_MONTH, 1)
            calendar.set(Calendar.MONTH, Calendar.JANUARY)
            if (calendar.before(inputTime)) {
                val sdf = SimpleDateFormat("M" + mContext.resources.getString(R.string.time_month) + "d" + mContext.resources.getString(R.string.time_day) + " HH:mm")
                return sdf.format(currenTimeZone)
            } else {
                val sdf = SimpleDateFormat("yyyy" + mContext.resources.getString(R.string.time_year) + "MM" + mContext.resources.getString(R.string.time_month) + "dd" + mContext.resources.getString(R.string.time_day) + " HH:mm")
                return sdf.format(currenTimeZone)
            }

        }

    }
}
