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

	/**
	 * 判断服务是否运行
	 * 
	 * @param context
	 * @param clazz
	 *            要判断的服务的class
	 * @return
	 */
	public static boolean isServiceRunning(Context context,
			Class<? extends Service> clazz) {
		ActivityManager manager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);

		List<RunningServiceInfo> services = manager.getRunningServices(100);
		for (int i = 0; i < services.size(); i++) {
			String className = services.get(i).service.getClassName();
			if (className.equals(clazz.getName())) {
				return true;
			}
		}
		return false;
	}


	/**
	 * 释放Assets中的资源
	 * @param context
	 * @param name 资源名称
	 * @param path 释放到的路径
	 */
	public static void mkdirsAssets(Context context,String name,String path){
		BufferedInputStream in=null;
		BufferedOutputStream out=null;
		try {
			in = new BufferedInputStream(context.getAssets().open(name));
			File file = new File(path);
			//释放目录
			if(!file.getParentFile().exists())
				file.getParentFile().mkdirs();
			if(file.exists())
				return;
			out = new BufferedOutputStream(new FileOutputStream(path));
			int len=0;
			while((len=in.read())!=-1){
				out.write(len);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				if(in!=null)
					in.close();
				if(out!=null)
					out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
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

	public static String string2MD5(String inStr) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
			return "";
		}
		char[] charArray = inStr.toCharArray();
		byte[] byteArray = new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++)
			byteArray[i] = (byte) charArray[i];
		byte[] md5Bytes = md5.digest(byteArray);
		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16)
				hexValue.append("0");
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();
	}
}