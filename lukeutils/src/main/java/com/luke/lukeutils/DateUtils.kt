package com.luke.lukeutils

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import android.text.format.Time

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

/**
 * Created by Administrator on 2016-06-08.
 */
@SuppressLint("SimpleDateFormat")
class DateUtils private constructor() {

    var date_Formater_1 = SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss")


    var date_Formater_2 = SimpleDateFormat("yyyy-MM-dd")
    var date_Formater_APMT = SimpleDateFormat("yyyy-MM")
    var date_Formater_3 = SimpleDateFormat("HH:mm:ss")
    var date_Formater_4 = SimpleDateFormat("HH:mm")

    fun getNow4(date: Date): String {
        val now4 = date_Formater_4.format(date)
        return now4
    }

    fun getDate(dateStr: String): Date? {
        var date = Date()
        if (TextUtils.isEmpty(dateStr)) {
            return date
        }
        try {
            date = date_Formater_1.parse(dateStr)
            return date
        } catch (e: ParseException) {
            e.printStackTrace()

        }

        return date

    }

    fun getDataString_1(date: Date?): String {
        var date = date
        if (date == null) {
            date = Date()
        }
        val str = date_Formater_1.format(date)
        return str

    }

    fun getDataString_2(date: Date?): String {
        var date = date
        if (date == null) {
            date = Date()
        }
        val str = date_Formater_2.format(date)
        return str

    }

    fun getDataString_APMT(date: Date?): String {
        var date = date
        if (date == null) {
            date = Date()
        }
        val str = date_Formater_APMT.format(date)
        return str

    }

    fun getDataString_3(date: Date?): String {
        var date = date
        if (date == null) {
            date = Date()
        }
        val str = date_Formater_3.format(date)
        return str

    }

    fun getDataString_4(date: Date?): String {
        var date = date
        if (date == null) {
            date = Date()
        }
        val str = date_Formater_4.format(date)
        return str

    }

    fun decodeTolong_1(string: String): Long {
        var date: Date? = null
        try {
            date = date_Formater_1.parse(string)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return date!!.time
    }


    /**
     * 将日期变成常见中文格式

     * @param date
     * *
     * @return
     */
    fun getRencentTime(date: String): String {
        val time = getDate(date) ?: return "一个月前"
        var ftime = ""
        val cal = Calendar.getInstance()

        val curDate = date_Formater_2.format(cal.time)
        val paramDate = date_Formater_2.format(time)
        if (curDate == paramDate) {
            val hour = ((cal.timeInMillis - time.time) / 3600000).toInt()
            if (hour == 0)
                ftime = Math.max(
                        (cal.timeInMillis - time.time) / 60000, 1).toString() + "分钟前"
            else
                ftime = hour.toString() + "小时前"
            return ftime
        }

        val lt = time.time / 86400000
        val ct = cal.timeInMillis / 86400000
        val days = (ct - lt).toInt()
        if (days == 0) {
            val hour = ((cal.timeInMillis - time.time) / 3600000).toInt()
            if (hour == 0)
                ftime = Math.max(
                        (cal.timeInMillis - time.time) / 60000, 1).toString() + "分钟前"
            else
                ftime = hour.toString() + "小时前"
        } else if (days == 1) {
            ftime = "昨天"
        } else if (days == 2) {
            ftime = "前天"
        } else if (days > 2 && days <= 10) {
            ftime = days.toString() + "天前"
        } else if (days > 10) {
            ftime = "一个月前"
        } else {
            ftime = date_Formater_2.format(time)
        }
        return ftime
    }

    /**
     * 日期时间格式转换

     * @param typeFrom 原格式
     * *
     * @param typeTo   转为格式
     * *
     * @param value    传入的要转换的参数
     * *
     * @return
     */
    fun stringDateToStringData(typeFrom: String, typeTo: String,
                               value: String): String {
        var re = value
        val sdfFrom = SimpleDateFormat(typeFrom)
        val sdfTo = SimpleDateFormat(typeTo)

        try {
            re = sdfTo.format(sdfFrom.parse(re))
        } catch (e: ParseException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }

        return re
    }

    /**
     * 得到这个月有多少天

     * @param year
     * *
     * @param month
     * *
     * @return
     */
    fun getMonthLastDay(year: Int, month: Int): Int {
        if (month == 0) {
            return 0
        }
        val a = Calendar.getInstance()
        a.set(Calendar.YEAR, year)
        a.set(Calendar.MONTH, month - 1)
        a.set(Calendar.DATE, 1)// 把日期设置为当月第一天
        a.roll(Calendar.DATE, -1)// 日期回滚一天，也就是最后一天
        val maxDate = a.get(Calendar.DATE)
        return maxDate
    }

    /**
     * 得到年份

     * @return
     */
    val currentYear: String
        get() {
            val c = Calendar.getInstance()
            return c.get(Calendar.YEAR).toString() + ""
        }

    /**
     * 得到月份

     * @return
     */
    val currentMonth: String
        get() {
            val c = Calendar.getInstance()
            return (c.get(Calendar.MONTH) + 1).toString() + ""
        }

    /**
     * 获得当天的日期

     * @return
     */
    val currDay: String
        get() {
            val c = Calendar.getInstance()
            return c.get(Calendar.DAY_OF_MONTH).toString() + ""
        }

    /**
     * 得到几天/周/月/年后的日期，整数往后推,负数往前移动

     * @param calendar
     * *
     * @param calendarType Calendar.DATE,Calendar.WEEK_OF_YEAR,Calendar.MONTH,Calendar.
     * *                     YEAR
     * *
     * @param next
     * *
     * @return
     */
    fun getDayByDate(calendar: Calendar, calendarType: Int, next: Int): String {

        calendar.add(calendarType, next)
        val date = calendar.time
        val dateString = date_Formater_1.format(date)
        return dateString

    }

    companion object {


        val YMDHMS = "yyyy-MM-dd HH:mm:ss"
        val YMD = "yyyy-MM-dd"
        val HMS = "HH:mm:ss"

        private var util: DateUtils? = null

        val instance: DateUtils?
            get() {

                if (util == null) {
                    util = DateUtils()

                }
                return util

            }


        /**
         * 日期类操作工具
         */
        fun formatDateTime(type: String): String {
            //格式化当前时间
            val isNow = SimpleDateFormat(type)
            val now = isNow.format(Date())
            return now
        }

        /**
         * 获取增加多少月的时间

         * @return addMonth - 增加多少月
         */
        fun getAddMonthDate(addMonth: Int): Date {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.MONTH, addMonth)
            return calendar.time
        }

        /**
         * 获取增加多少天的时间

         * @return addDay - 增加多少天
         */
        fun getAddDayDate(addDay: Int): Date {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_YEAR, addDay)
            return calendar.time
        }

        /**
         * 获取增加多少小时的时间

         * @return addDay - 增加多少消失
         */
        fun getAddHourDate(addHour: Int): Date {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.HOUR, addHour)
            return calendar.time
        }

        /**
         * 显示时间格式为 hh:mm

         * @param context
         * *
         * @param when
         * *
         * @return String
         */
        @SuppressLint("SimpleDateFormat")
        fun formatTimeHHmm(context: Context, `when`: Long): String {
            val formatStr = "HH:mm"
            val sdf = SimpleDateFormat(formatStr)
            var temp: String = sdf.format(`when`)
            if (temp != null && temp.length == 5 && temp.substring(0, 1) == "0") {
                temp = temp.substring(1)
            }
            return temp
        }


        /**
         * 显示时间格式为今天、昨天、yyyy/MM/dd hh:mm

         * @param context
         * *
         * @param when
         * *
         * @return String
         */
        fun formatTimeYTymdhm(context: Context, `when`: Long): String {
            val then = Time()
            then.set(`when`)
            val now = Time()
            now.setToNow()

            val formatStr: String
            if (then.year != now.year) {
                formatStr = "yyyy/MM/dd"
            } else if (then.yearDay != now.yearDay) {
                // If it is from a different day than today, show only the date.
                formatStr = "MM/dd"
            } else {
                // Otherwise, if the message is from today, show the time.
                formatStr = "HH:MM"
            }

            if (then.year == now.year && then.yearDay == now.yearDay) {
                return "今天"
            } else if (then.year == now.year && now.yearDay - then.yearDay == 1) {
                return "昨天"
            } else {
                val sdf = SimpleDateFormat(formatStr)
                var temp: String = sdf.format(`when`)
                if (temp != null && temp.length == 5 && temp.substring(0, 1) == "0") {
                    temp = temp.substring(1)
                }
                return temp
            }
        }

        /**
         * 是否同一天

         * @param date1
         * *
         * @param date2
         * *
         * @return
         */
        fun isSameDate(date1: Long, date2: Long): Boolean {
            val days1 = date1 / (1000 * 60 * 60 * 24)
            val days2 = date2 / (1000 * 60 * 60 * 24)
            return days1 == days2
        }

        /**
         * 获得几天之前或者几天之后的日期

         * @param diff 差值：正的往后推，负的往前推
         * *
         * @return
         */
        fun getOtherDay(diff: Int): String {
            val mCalendar = Calendar.getInstance()
            mCalendar.add(Calendar.DATE, diff)
            return getDateFormat(mCalendar.time)
        }

        /**
         * 将date转成yyyy-MM-dd字符串<br></br>

         * @param date Date对象
         * *
         * @return yyyy-MM-dd
         */
        fun getDateFormat(date: Date): String {
            return dateSimpleFormat(date, defaultDateFormat.get())
        }

        /**
         * yyyy-MM-dd HH:mm:ss格式
         */
        val defaultDateTimeFormat: ThreadLocal<SimpleDateFormat> = object : ThreadLocal<SimpleDateFormat>() {

            override fun initialValue(): SimpleDateFormat {
                return SimpleDateFormat(YMDHMS)
            }

        }

        /**
         * 将date转成字符串

         * @param date   Date
         * *
         * @param format SimpleDateFormat
         * *               <br></br>
         * *               注： SimpleDateFormat为空时，采用默认的yyyy-MM-dd HH:mm:ss格式
         * *
         * @return yyyy-MM-dd HH:mm:ss
         */
        fun dateSimpleFormat(date: Date?, format: SimpleDateFormat?): String {
            var format = format
            if (format == null)
                format = defaultDateTimeFormat.get()
            return if (date == null) "" else format!!.format(date)
        }

        /**
         * yyyy-MM-dd格式
         */
        val defaultDateFormat: ThreadLocal<SimpleDateFormat> = object : ThreadLocal<SimpleDateFormat>() {

            override fun initialValue(): SimpleDateFormat {
                return SimpleDateFormat(YMD)
            }

        }
    }

}
