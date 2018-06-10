package com.jw.library.utils

import net.sourceforge.pinyin4j.PinyinHelper

/**
 * 创建时间：
 * 更新时间 2017/11/2 12:58
 * 版本：
 * 作者：Mr.jin
 * 描述：公共工具类
 * 1.某服务是否运行
 * 2.释放Assets中的资源
 * 3.将字符串MD5加密
 */

object CommonUtils {

    fun getFirstAlpha(inputString: String?): String {
        // String pinYin = getPinYin(inputString);
        // if (pinYin != null && pinYin.length() > 0) {
        // return pinYin.substring(0, 1).toUpperCase();
        // }

        if (inputString != null) {
            val array = PinyinHelper.toHanyuPinyinStringArray(inputString[0])
            return if (array == null) {
                inputString.substring(0, 1).toUpperCase()
            } else {
                array[0].toUpperCase()
            }
        }
        return ""
    }
}