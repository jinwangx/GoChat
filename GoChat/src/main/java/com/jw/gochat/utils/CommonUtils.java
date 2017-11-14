package com.jw.gochat.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Service;
import android.content.Context;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.List;

/**
 * 创建时间：
 * 更新时间 2017/11/2 12:58
 * 版本：
 * 作者：Mr.jin
 * 描述：公共工具类
 * 		1.某服务是否运行
 * 		2.释放Assets中的资源
 * 		3.将字符串MD5加密
 */

public class CommonUtils {

	public static String getPinYin(String inputString) {
		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		format.setToneType(HanyuPinyinToneType.WITH_TONE_MARK);
		format.setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE);

		char[] input = inputString.trim().toCharArray();
		StringBuffer output = new StringBuffer("");
		try {
			for (int i = 0; i < input.length; i++) {
				if (Character.toString(input[i]).matches("[//u4E00-//u9FA5]+")) {
					String[] temp = PinyinHelper.toHanyuPinyinStringArray(
							input[i], format);
					output.append(temp[0]);
					output.append(" ");
				} else
					output.append(Character.toString(input[i]));
			}
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			e.printStackTrace();
		}
		return output.toString();
	}

	public static String getFirstAlpha(String inputString) {
		// String pinYin = getPinYin(inputString);
		// if (pinYin != null && pinYin.length() > 0) {
		// return pinYin.substring(0, 1).toUpperCase();
		// }

		if (inputString != null) {

			String[] array = PinyinHelper.toHanyuPinyinStringArray(inputString
					.charAt(0));

			if (array == null) {
				return inputString.substring(0, 1).toUpperCase();
			} else {
				return array[0].toUpperCase();
			}
		}

		return "";
	}

}