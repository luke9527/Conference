package com.luke.lukeutils

import android.annotation.TargetApi
import android.content.Context
import android.content.CursorLoader
import android.content.res.AssetManager
import android.database.Cursor
import android.graphics.Typeface
import android.net.Uri
import android.provider.DocumentsContract
import android.provider.MediaStore

/**
 * Created by luke on 2016/9/22.
 */
object DisplayUtils {

    fun getYouyuan(context: Context): Typeface {
        val manager = context.assets
        //获得typeface
        val typeface = Typeface.createFromAsset(manager, "fonts/youyuan.ttf")
        return typeface
    }


    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变

     * @param pxValue
     * *
     * @param （DisplayMetrics类中属性density）
     * *
     * @return
     */

    fun px2dip(pxValue: Float): Int {

        val scale = LukeUtils.getContext().resources.displayMetrics.density

        return (pxValue / scale + 0.5f).toInt()
    }


    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变

     * @param dipValue
     * *
     * @param （DisplayMetrics类中属性density）
     * *
     * @return
     */

    fun dip2px(context: Context, dipValue: Float): Int {

        val scale = context.resources.displayMetrics.density

        return (dipValue * scale + 0.5f).toInt()

    }


    /**
     * 将px值转换为sp值，保证文字大小不变

     * @param pxValue
     * *
     * @param （DisplayMetrics类中属性scaledDensity）
     * *
     * @return
     */

    fun px2sp(context: Context, pxValue: Float): Int {

        val fontScale = context.resources.displayMetrics.scaledDensity

        return (pxValue / fontScale + 0.5f).toInt()

    }


    /**
     * 将sp值转换为px值，保证文字大小不变

     * @param spValue
     * *
     * @param （DisplayMetrics类中属性scaledDensity）
     * *
     * @return
     */

    fun sp2px(context: Context, spValue: Float): Int {

        val fontScale = context.resources.displayMetrics.scaledDensity
        LogUtils.d("***" + (spValue * fontScale + 0.5f).toInt())
        return (spValue * fontScale + 0.5f).toInt()

    }

    /**
     * 适配api19以上,根据uri获取图片的绝对路径
     */
    @TargetApi(19)
    fun getRealPathFromUri_AboveApi19(context: Context, uri: Uri): String {
        var filePath: String? = null
        val wholeID = DocumentsContract.getDocumentId(uri)

        // 使用':'分割
        val id = wholeID.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]

        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val selection = MediaStore.Images.Media._ID + "=?"
        val selectionArgs = arrayOf(id)

        val cursor = context.contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
                selection, selectionArgs, null)
        val columnIndex = cursor!!.getColumnIndex(projection[0])

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex)
        }
        cursor.close()
        return filePath!!
    }

    /**
     * 适配api11-api18,根据uri获取图片的绝对路径
     */
    fun getRealPathFromUri_Api11To18(context: Context, uri: Uri): String {
        var filePath: String? = null
        val projection = arrayOf(MediaStore.Images.Media.DATA)

        val loader = CursorLoader(context, uri, projection, null, null, null)
        val cursor = loader.loadInBackground()

        if (cursor != null) {
            cursor.moveToFirst()
            filePath = cursor.getString(cursor.getColumnIndex(projection[0]))
            cursor.close()
        }
        return filePath!!
    }

    /**
     * 适配api11以下(不包括api11),根据uri获取图片的绝对路径
     */
    fun getRealPathFromUri_BelowApi11(context: Context, uri: Uri): String {
        var filePath: String? = null
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(uri, projection, null, null, null)
        if (cursor != null) {
            cursor.moveToFirst()
            filePath = cursor.getString(cursor.getColumnIndex(projection[0]))
            cursor.close()
        }
        return filePath!!
    }


}
