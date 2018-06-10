package com.jw.library.utils

import java.io.*
import java.text.DecimalFormat

/**
 * 作者：jw
 * 创建时间：2017/10/23 20:54
 * 描述:文件工具类
 */
object FileUtils {

    private const val SIZETYPE_B = 1//获取文件大小单位为B的double值
    private const val SIZETYPE_KB = 2//获取文件大小单位为KB的double值
    private const val SIZETYPE_MB = 3//获取文件大小单位为MB的double值
    private const val SIZETYPE_GB = 4//获取文件大小单位为GB的double值
    /**
     * Author: jw
     * Created on:  2017/8/12.
     * Description: 删除文件工具类(文件或者文件夹可以自动识别)
     */
    fun delete(fileName: String): Boolean {
        val file = File(fileName)
        return if (!file.exists()) {
            println("删除文件失败:" + fileName + "不存在！")
            false
        } else {
            if (file.isFile)
                deleteFile(fileName)
            else
                deleteDirectory(fileName)
        }
    }

    /**
     * 删除单个文件
     *
     * @param fileName
     * 要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    fun deleteFile(fileName: String): Boolean {
        val file = File(fileName)
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile) {
            if (file.delete()) {
                println("删除单个文件" + fileName + "成功！")
                return true
            } else {
                println("删除单个文件" + fileName + "失败！")
                return false
            }
        } else {
            println("删除单个文件失败：" + fileName + "不存在！")
            return false
        }
    }

    /**
     * 删除目录及目录下的文件
     *
     * @param dir
     * 要删除的目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    fun deleteDirectory(dir: String): Boolean {
        var dir = dir
        // 如果dir不以文件分隔符结尾，自动添加文件分隔符
        if (!dir.endsWith(File.separator))
            dir += File.separator
        val dirFile = File(dir)
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.exists() || !dirFile.isDirectory) {
            println("删除目录失败：" + dir + "不存在！")
            return false
        }
        var flag = true
        // 删除文件夹中的所有文件包括子目录
        val files = dirFile.listFiles()
        for (i in files.indices) {
            // 删除子文件
            if (files[i].isFile) {
                flag = deleteFile(files[i].absolutePath)
                if (!flag)
                    break
            } else if (files[i].isDirectory) {
                flag = deleteDirectory(files[i]
                        .absolutePath)
                if (!flag)
                    break
            }// 删除子目录
        }
        if (!flag) {
            println("删除目录失败！")
            return false
        }
        // 删除当前目录
        return if (dirFile.delete()) {
            println("删除目录" + dir + "成功！")
            true
        } else {
            false
        }
    }

    /**
     * 获取文件指定文件的指定单位的大小
     * @param filePath 文件路径
     * @param sizeType 获取大小的类型1为B、2为KB、3为MB、4为GB
     * @return double值的大小
     */
    fun getFileOrFilesSize(filePath: String, sizeType: Int): Double {
        val file = File(filePath)
        var blockSize: Long = 0
        try {
            blockSize = if (file.isDirectory) {
                getFileSizes(file)
            } else {
                getFileSize(file)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            println("获取文件大小获取失败!")
        }

        return FormetFileSize(blockSize, sizeType)
    }


    /**
     * 调用此方法自动计算指定文件或指定文件夹的大小
     *
     * @param filePath 文件路径
     * @return 计算好的带B、KB、MB、GB的字符串
     */
    fun getAutoFileOrFilesSize(filePath: String): String {
        val file = File(filePath)
        var blockSize: Long = 0
        try {
            blockSize = if (file.isDirectory) {
                getFileSizes(file)
            } else {
                getFileSize(file)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            println("获取文件大小获取失败!")
        }

        return FormetFileSize(blockSize)
    }

    /**
     * 获取指定文件大小
     *
     * @param file
     * @return
     * @throws Exception
     */
    @Throws(Exception::class)
    private fun getFileSize(file: File): Long {
        var size: Long = 0
        if (file.exists()) {
            var fis: FileInputStream? = null
            fis = FileInputStream(file)
            size = fis.available().toLong()
        } else {
            file.createNewFile()
            println("不存在!")
        }
        return size
    }

    /**
     * 获取指定文件夹
     *
     * @param f
     * @return
     * @throws Exception
     */
    @Throws(Exception::class)
    private fun getFileSizes(f: File): Long {
        var size: Long = 0
        val flist = f.listFiles()
        for (i in flist.indices) {
            size += if (flist[i].isDirectory) {
                getFileSizes(flist[i])
            } else {
                getFileSize(flist[i])
            }
        }
        return size
    }

    /**
     * 转换文件大小
     *
     * @param fileS
     * @return
     */
    private fun FormetFileSize(fileS: Long): String {
        val df = DecimalFormat("#.00")
        var fileSizeString = ""
        val wrongSize = "0B"
        if (fileS == 0L) {
            return wrongSize
        }
        fileSizeString = when {
            fileS < 1024 -> df.format(fileS.toDouble()) + "B"
            fileS < 1048576 -> df.format(fileS.toDouble() / 1024) + "KB"
            fileS < 1073741824 -> df.format(fileS.toDouble() / 1048576) + "MB"
            else -> df.format(fileS.toDouble() / 1073741824) + "GB"
        }
        return fileSizeString
    }

    /**
     * 转换文件大小,指定转换的类型
     *
     * @param fileS
     * @param sizeType
     * @return
     */
    private fun FormetFileSize(fileS: Long, sizeType: Int): Double {
        val df = DecimalFormat("#.00")
        var fileSizeLong = 0.0
        when (sizeType) {
            SIZETYPE_B -> fileSizeLong = java.lang.Double.valueOf(df.format(fileS.toDouble()))!!
            SIZETYPE_KB -> fileSizeLong = java.lang.Double.valueOf(df.format(fileS.toDouble() / 1024))!!
            SIZETYPE_MB -> fileSizeLong = java.lang.Double.valueOf(df.format(fileS.toDouble() / 1048576))!!
            SIZETYPE_GB -> fileSizeLong = java.lang.Double.valueOf(df.format(fileS.toDouble() / 1073741824))!!
            else -> {
            }
        }
        return fileSizeLong
    }

    /**
     * 文件复制
     * @param inPath 文件路径
     * @param outPath 要复制到的路径
     */
    fun copy(inPath: String, outPath: String) {
        var `in`: BufferedInputStream? = null
        var out: BufferedOutputStream? = null
        try {
            `in` = BufferedInputStream(FileInputStream(inPath))
            val file = File(outPath)
            if (!file.parentFile.exists())
                file.parentFile.mkdirs()
            if (file.exists())
                return
            out = BufferedOutputStream(FileOutputStream(outPath))
            var len = 0
            while (len != -1) {
                out.write(len)
                len = `in`.read()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                if (`in` != null)
                    `in`.close()
                if (out != null)
                    out.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }


    /**
     * 将对象序列化入一个文件
     * @param t
     * @param outPath 文件路径
     * @param <T>
     * @throws Exception
    </T> */
    @Throws(Exception::class)
    fun <T : Serializable> write(t: T, outPath: String) {
        var oos: ObjectOutputStream? = null
        try {
            val file = File(outPath)
            if (!file.parentFile.exists()) {
                file.parentFile.mkdirs()
            }

            oos = ObjectOutputStream(FileOutputStream(file))
            oos.writeObject(t)
        } finally {
            if (oos != null) {
                oos.close()
            }
        }
    }

    /**
     * 读出序列化对象
     * @param path 序列化文件路径
     * @return
     * @throws Exception
     */
    @Throws(Exception::class)
    fun read(path: String): Serializable? {
        var ois: ObjectInputStream? = null
        try {
            ois = ObjectInputStream(FileInputStream(path))
            val `object` = ois.readObject()

            if (`object` != null) {
                return `object` as Serializable
            }
        } finally {
            if (ois != null) {
                ois.close()
            }
        }
        return null
    }

}
