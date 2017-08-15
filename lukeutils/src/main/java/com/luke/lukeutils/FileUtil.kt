package com.luke.lukeutils

import android.app.Activity
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log


import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.FilenameFilter
import java.io.IOException


/**
 * 文件工具类
 */
class FileUtil private constructor() {

    init {
        /* cannot be instantiated */
        throw UnsupportedOperationException("cannot be instantiated")
    }

    enum class FileType {
        IMG,
        AUDIO,
        VIDEO,
        FILE
    }

    companion object {

        private val TAG = "FileUtil"
        private val pathDiv = "/"
        @JvmField
        var cacheDir= File("")
        lateinit var mContext: Context
        @JvmStatic
        fun init(context: Context) {
            mContext = context
            cacheDir = if (!isExternalStorageWritable) mContext.filesDir else mContext.externalCacheDir

        }


        /**
         * 创建临时文件

         * @param type 文件类型
         */
        fun getTempFile(type: FileType): File? {

            try {
                val file = File.createTempFile(type.toString(), null, cacheDir)
                file.deleteOnExit()
                return file
            } catch (e: IOException) {
                return null
            }

        }


        /**
         * 获取缓存文件地址
         */
        fun getCacheFilePath(fileName: String): String {
            return cacheDir.absolutePath + pathDiv + fileName
        }


        /**
         * 判断缓存文件是否存在
         */
        fun isCacheFileExist(fileName: String): Boolean {
            val file = File(getCacheFilePath(fileName))
            return file.exists()
        }

        fun getFilePath(fileName: String): String {
            val path = cacheDir.toString() + File.separator + fileName
            return path
        }

        /**
         * 将图片存储为文件

         * @param bitmap 图片
         */
        fun createFile(bitmap: Bitmap?, filename: String, fileCallBack: FileCallBack) {
            if (bitmap == null) {
                LogUtils.d("bitmap:" + "bitmap= null")
                fileCallBack.getPath(null)
                return
            }
            val f = File(cacheDir, filename)
            if (f.exists()) {
                f.delete()
            }
            Thread(
                    Runnable {
                        try {
                            if (f.createNewFile()) {
                                val bos = ByteArrayOutputStream()
                                bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos)
                                val bitmapdata = bos.toByteArray()
                                val fos = FileOutputStream(f)
                                fos.write(bitmapdata)
                                fos.flush()
                                fos.close()
                            }
                        } catch (e: IOException) {
                            Log.e(TAG, "create bitmap file error" + e)
                        }

                        if (f.exists()) {
                            fileCallBack.getPath(f.absolutePath)
                        }
                    }
            ).start()


        }

        /**
         * 将图片存储为文件

         * @param bitmap 图片
         */
        fun createFile(bitmap: Bitmap?, filename: String): String? {
            if (bitmap == null) {
                LogUtils.d("bitmap:" + "bitmap= null")
                return null
            }
            val f = File(cacheDir, filename)
            if (f.exists()) {
                f.delete()
            }
            try {
                if (f.createNewFile()) {
                    val bos = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos)
                    val bitmapdata = bos.toByteArray()
                    val fos = FileOutputStream(f)
                    fos.write(bitmapdata)
                    fos.flush()
                    fos.close()
                }
            } catch (e: IOException) {
                Log.e(TAG, "create bitmap file error" + e)
            }

            if (f.exists()) {
                return f.absolutePath
            }
            return null
        }

        /**
         * 将图片存储为文件   这里是将图片存在根目录通知更新后可以在相册分类

         * @param bitmap 图片
         */
        fun saveBitmap(bitmap: Bitmap?, dirName: String, filename: String): String? {
            if (bitmap == null) {
                LogUtils.d("bitmap:" + "bitmap= null")
                return null
            }
            var dir: File? = null
            val sdCardExist = Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
            if (sdCardExist) {
                dir = File(Environment.getExternalStorageDirectory().absolutePath + File.separator + dirName)
            } else {
                dir = cacheDir
            }
            if (!dir.exists()) {
                dir.mkdirs()
            }
            val f = File(dir, filename)

            if (f.exists()) {
                f.delete()
            }
            try {
                if (f.createNewFile()) {
                    val bos = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos)
                    val bitmapdata = bos.toByteArray()
                    val fos = FileOutputStream(f)
                    fos.write(bitmapdata)
                    fos.flush()
                    fos.close()
                }
            } catch (e: IOException) {
                Log.e(TAG, "create bitmap file error" + e)
            }

            if (f.exists()) {
                return f.absolutePath
            }
            return null
        }

        /**
         * 将数据存储为文件

         * @param data 数据
         */
        fun createFile(data: ByteArray, filename: String) {
            val f = File(cacheDir, filename)
            try {
                if (f.createNewFile()) {
                    val fos = FileOutputStream(f)
                    fos.write(data)
                    fos.flush()
                    fos.close()
                }
            } catch (e: IOException) {
                Log.e(TAG, "create file error" + e)
            }

        }


        /**
         * 判断缓存文件是否存在
         */
        fun isFileExist(fileName: String, type: String): Boolean {
            if (isExternalStorageWritable) {
                val dir = mContext.getExternalFilesDir(type)
                LogUtils.d("dir : " + dir!!.absolutePath)
                if (dir != null) {
                    val f = File(dir, fileName)
                    return f.exists()
                }
            }
            return false
        }


        /**
         * 将数据存储为文件

         * @param data     数据
         * *
         * @param fileName 文件名
         * *
         * @param type     文件类型
         */
        fun createFile(data: ByteArray, fileName: String, type: String): File? {
            if (isExternalStorageWritable) {
                val dir = mContext.getExternalFilesDir(type)
                if (dir != null) {
                    val f = File(dir, fileName)
                    try {
                        if (f.createNewFile()) {
                            val fos = FileOutputStream(f)
                            fos.write(data)
                            fos.flush()
                            fos.close()
                            return f
                        }
                    } catch (e: IOException) {
                        Log.e(TAG, "create file error" + e)
                        return null
                    }

                }
            }
            return null
        }

        fun createFile1(data: ByteArray, fileName: String, type: String): File? {
            if (isExternalStorageWritable) {
                val dir = mContext.getExternalFilesDir(type)
                if (dir != null) {
                    val f = File(dir, fileName)
                    if (f.exists()) {
                        f.delete()
                    }
                    try {
                        if (f.createNewFile()) {
                            val fos = FileOutputStream(f)
                            fos.write(data)
                            fos.flush()
                            fos.close()
                            return f
                        }
                    } catch (e: IOException) {
                        Log.e(TAG, "create file error" + e)
                        return null
                    }

                }
            }
            return null
        }


        /**
         * 从URI获取图片文件地址

         * @param context 上下文
         * *
         * @param uri     文件uri
         */
        fun getImageFilePath(context: Context, uri: Uri?): String? {
            if (uri == null) {
                return null
            }
            var path: String? = null
            val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
            if (isKitKat) {
                if (!isMediaDocument(uri)) {
                    try {
                        val docId = DocumentsContract.getDocumentId(uri)
                        val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                        val contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        val selection = "_id=?"
                        val selectionArgs = arrayOf(split[1])
                        path = getDataColumn(context, contentUri, selection, selectionArgs)
                    } catch (e: IllegalArgumentException) {
                        path = null
                    }

                }
            }
            if (path == null) {
                val projection = arrayOf(MediaStore.Images.Media.DATA)
                val cursor = (context as Activity).managedQuery(uri, projection, null, null, null)
                if (cursor != null) {
                    val column_index = cursor
                            .getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                    cursor.moveToFirst()
                    return cursor.getString(column_index)
                }

                path = null
            }
            return path
        }


        /**
         * 从URI获取文件地址

         * @param context 上下文
         * *
         * @param uri     文件uri
         */
        fun getFilePath(context: Context, uri: Uri): String? {
            val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
            // DocumentProvider
            if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    val type = split[0]

                    if ("primary".equals(type, ignoreCase = true)) {
                        return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                    }

                    // TODO handle non-primary volumes
                } else if (isDownloadsDocument(uri)) {

                    val id = DocumentsContract.getDocumentId(uri)
                    val contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id)!!)

                    return getDataColumn(context, contentUri, null, null)
                } else if (isMediaDocument(uri)) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    val type = split[0]

                    var contentUri: Uri? = null
                    if ("image" == type) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    } else if ("video" == type) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    } else if ("audio" == type) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    }

                    val selection = "_id=?"
                    val selectionArgs = arrayOf(split[1])

                    return getDataColumn(context, contentUri, selection, selectionArgs)
                }// MediaProvider
                // DownloadsProvider
            } else if ("content".equals(uri.scheme, ignoreCase = true)) {
                return getDataColumn(context, uri, null, null)
            } else if ("file".equals(uri.scheme, ignoreCase = true)) {
                return uri.path
            }// File
            // MediaStore (and general)

            return null
        }


        /**
         * Get the value of the data column for this Uri. This is useful for
         * MediaStore Uris, and other file-based ContentProviders.

         * @param context       The context.
         * *
         * @param uri           The Uri to query.
         * *
         * @param selection     (Optional) Filter used in the query.
         * *
         * @param selectionArgs (Optional) Selection arguments used in the query.
         * *
         * @return The value of the _data column, which is typically a file path.
         */
        private fun getDataColumn(context: Context, uri: Uri?, selection: String?,
                                  selectionArgs: Array<String>?): String? {
            var cursor: Cursor? = null
            val column = "_data"
            val projection = arrayOf(column)
            try {
                cursor = context.contentResolver.query(uri, projection, selection, selectionArgs, null)
                if (cursor != null && cursor.moveToFirst()) {
                    val index = cursor.getColumnIndexOrThrow(column)
                    return cursor.getString(index)
                }
            } finally {
                if (cursor != null)
                    cursor.close()
            }
            return null
        }


        /**
         * 判断外部存储是否可用
         */
        val isExternalStorageWritable: Boolean
            get() {
                val state = Environment.getExternalStorageState()
                if (Environment.MEDIA_MOUNTED == state) {
                    return true
                }
                Log.e(TAG, "ExternalStorage not mounted")
                return false
            }

        fun isMediaDocument(uri: Uri): Boolean {
            return "com.android.providers.media.documents" == uri.authority
        }

        fun isExternalStorageDocument(uri: Uri): Boolean {
            return "com.android.externalstorage.documents" == uri.authority
        }

        /**
         * @param uri The Uri to check.
         * *
         * @return Whether the Uri authority is DownloadsProvider.
         */
        fun isDownloadsDocument(uri: Uri): Boolean {
            return "com.android.providers.downloads.documents" == uri.authority
        }


        /**
         * 生成缩略图
         * 缩略图是将原图等比压缩，压缩后宽、高中较小的一个等于198像素
         * 详细信息参见文档
         */
        fun getThumb(path: String?): Bitmap? {
            if (path == null) {
                return null
            }
            val file = File(path)
            if (!file.exists()) {
                return null
            }
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeFile(path, options)
            val reqWidth: Int
            val reqHeight: Int
            val width = options.outWidth
            val height = options.outHeight
            if (width > height) {
                reqWidth = 256
                reqHeight = reqWidth * height / width
            } else {
                reqHeight = 256
                reqWidth = width * reqHeight / height
            }
            var inSampleSize = 1
            if (height > reqHeight || width > reqWidth) {
                val halfHeight = height / 2
                val halfWidth = width / 2
                while (halfHeight / inSampleSize > reqHeight && halfWidth / inSampleSize > reqWidth) {
                    inSampleSize *= 2
                }
            }
            try {
                options.inSampleSize = inSampleSize
                options.inJustDecodeBounds = false
                val mat = Matrix()
                val bitmap = BitmapFactory.decodeFile(path, options) ?: return null
                val ei = ExifInterface(path)
                val orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
                when (orientation) {
                    ExifInterface.ORIENTATION_ROTATE_90 -> mat.postRotate(90f)
                    ExifInterface.ORIENTATION_ROTATE_180 -> mat.postRotate(180f)
                }

                return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, mat, true)
            } catch (e: IOException) {
                return null
            }

        }

        fun getPaths(ac: Context, uriStr: String): String? {
            val uri = Uri.parse(uriStr)
            if (uri.scheme.toString().compareTo("content") == 0) {
                val cr = ac.contentResolver
                val cursor = cr.query(uri, null, null, null, null)// 根据Uri从数据库中找
                if (cursor != null) {
                    cursor.moveToFirst()
                    val filePath = cursor.getString(cursor.getColumnIndex("_data"))// 获取图片路径
                    cursor.close()
                    if (filePath != null) {
                        return filePath
                    }
                }
            } else if (uri.scheme.toString().compareTo("file") == 0) {
                return uri.toString().replace("file://", "")
            }
            return null
        }


        /**
         * 删除指定目录中特定的文件

         * @param dir
         * *
         * @param filter
         */
        fun delete(dir: String, filter: FilenameFilter?) {
            if (TextUtils.isEmpty(dir))
                return
            val file = File(dir)
            if (!file.exists())
                return
            if (file.isFile)
                file.delete()
            if (!file.isDirectory)
                return

            var lists: Array<File>? = null
            if (filter != null)
                lists = file.listFiles(filter)
            else
                lists = file.listFiles()

            if (lists == null)
                return
            for (f in lists) {
                if (f.isFile) {
                    f.delete()
                }
            }
        }

        val FILE_EXTENSION_SEPARATOR = "."

        fun getFileNameWithoutExtension(filePath: String): String {
            if (TextUtils.isEmpty(filePath)) {
                return filePath
            }
            val extenPosi = filePath.lastIndexOf(FILE_EXTENSION_SEPARATOR)
            val filePosi = filePath.lastIndexOf(File.separator)
            if (filePosi == -1) {
                return if (extenPosi == -1)
                    filePath
                else
                    filePath.substring(0,
                            extenPosi)
            }
            if (extenPosi == -1) {
                return filePath.substring(filePosi + 1)
            }
            return if (filePosi < extenPosi)
                filePath.substring(filePosi + 1,
                        extenPosi)
            else
                filePath.substring(filePosi + 1)
        }
    }


}
