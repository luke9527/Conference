package com.luke.lukeutils


import android.app.Application
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import android.widget.Toast


import java.io.File
import java.io.FileOutputStream
import java.io.FilenameFilter
import java.io.PrintWriter
import java.io.StringWriter
import java.io.Writer
import java.lang.reflect.Field
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.HashMap


/**
 * Created by luke on 2016/12/16.
 */
class UnCeHandler(internal var application: Application,internal var a:()->Unit) : Thread.UncaughtExceptionHandler {

    private val mDefaultHandler: Thread.UncaughtExceptionHandler?
    // 用来存储设备信息和异常信息
    private val infos = HashMap<String, String>()

    // 用于格式化日期,作为日志文件名的一部分
    private val formatter = SimpleDateFormat("yyyy-MM-dd")

    init {
        // 获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        // 设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this)
        autoClear(5)
    }

    override fun uncaughtException(thread: Thread, ex: Throwable) {
        if (!handleException(ex,a) && mDefaultHandler != null) {
            //如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex)
        } else {
            try {
                Thread.sleep(2000)
            } catch (e: InterruptedException) {
                Log.e(TAG, "error : ", e)
            }

            /*  Intent intent = new Intent(application.getApplicationContext(), GuideActivity.class);
            PendingIntent restartIntent = PendingIntent.getActivity(application.getApplicationContext(), 0, intent, Intent.FLAG_ACTIVITY_NEW_TASK);
            //退出程序
            AlarmManager mgr = (AlarmManager)application.getSystemService(Context.ALARM_SERVICE);
            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000,
                    restartIntent); // 1秒钟后重启应用
            application.clear();*/
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.

     * @param ex
     * *
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private fun handleException(ex: Throwable?,crashDo:()->Unit): Boolean {
        if (ex == null)
            return false
        try {
            // 使用Toast来显示异常信息
            object : Thread() {
                override fun run() {
                    Looper.prepare()
                    /*  Toast.makeText(application,  getContext().getString(R.string.Oops_quit_for_abnormal_error),
                            Toast.LENGTH_LONG).show();
                            */
                    Looper.loop()
                }
            }.start()
            // 收集设备参数信息
            collectDeviceInfo(application)
            // 保存日志文件
            saveCrashInfoFile(ex)
            crashDo.invoke()
            // 重启应用（按需要添加是否重启应用）
            //            Intent intent = mContext.getPackageManager().getLaunchIntentForPackage(mContext.getPackageName());
            //            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //            mContext.startActivity(intent);
            //  TeachearApplication.exitApp();
        } catch (e: Exception) {
            e.printStackTrace()
            //TeachearApplication.exitApp();
        }

        return true
    }

    /**
     * 收集设备参数信息

     * @param ctx
     */
    fun collectDeviceInfo(ctx: Context) {
        try {
            val pm = ctx.packageManager
            val pi = pm.getPackageInfo(ctx.packageName,
                    PackageManager.GET_ACTIVITIES)
            if (pi != null) {
                val versionName = pi.versionName + ""
                val versionCode = pi.versionCode.toString() + ""
                infos.put("versionName", versionName)
                infos.put("versionCode", versionCode)
            }
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e(TAG, "an error occured when collect package info", e)
        }

        val fields = Build::class.java.declaredFields
        for (field in fields) {
            try {
                field.isAccessible = true
                infos.put(field.name, field.get(null).toString())
            } catch (e: Exception) {
                Log.e(TAG, "an error occured when collect crash info", e)
            }

        }
    }

    /**
     * 保存错误信息到文件中

     * @param ex
     * *
     * @return 返回文件名称, 便于将文件传送到服务器
     * *
     * @throws Exception
     */
    @Throws(Exception::class)
    private fun saveCrashInfoFile(ex: Throwable): String? {
        val sb = StringBuffer()
        try {
            val sDateFormat = SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss")
            val date = sDateFormat.format(Date())
            sb.append("\r\n" + date + "\n")
            for ((key, value) in infos) {
                sb.append(key + "=" + value + "\n")
            }

            val writer = StringWriter()
            val printWriter = PrintWriter(writer)
            ex.printStackTrace(printWriter)
            var cause: Throwable? = ex.cause
            while (cause != null) {
                cause.printStackTrace(printWriter)
                cause = cause.cause
            }
            printWriter.flush()
            printWriter.close()
            val result = writer.toString()
            sb.append(result)
            val fileName = writeFile(sb.toString())
            return fileName
        } catch (e: Exception) {
            Log.e(TAG, "an error occured while writing file...", e)
            sb.append("an error occured while writing file...\r\n")
            writeFile(sb.toString())
        }

        return null
    }

    @Throws(Exception::class)
    private fun writeFile(sb: String): String {
        val time = formatter.format(Date())
        val fileName = "crash-$time.log"
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            val path = globalpath
            val dir = File(path)
            if (!dir.exists())
                dir.mkdirs()

            val fos = FileOutputStream(path + fileName, true)
            fos.write(sb.toByteArray())
            fos.flush()
            fos.close()
        }
        return fileName
    }


    /**
     * 文件删除

     * @param
     */
    fun autoClear(autoClearDay: Int) {
        FileUtil.delete(globalpath, FilenameFilter { p0, p1 ->
            val s = FileUtil.getFileNameWithoutExtension(p1!!)
            val day = if (autoClearDay < 0) autoClearDay else -1 * autoClearDay
            val date = "crash-" + DateUtils.getOtherDay(day)
            date.compareTo(s) >= 0
        })
    }

    companion object {
        val TAG = "CatchExcep"
        val globalpath: String
            get() {
                return FileUtil.cacheDir.absolutePath+File.separator + "crash" + File.separator
            }
    }

}