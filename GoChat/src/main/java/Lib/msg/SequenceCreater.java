package Lib.msg;

import java.util.UUID;

/**
 * 创建时间：
 * 更新时间 2017/11/1 23:15
 * 版本：
 * 作者：Mr.jin
 * 描述：uuid创建类
 */

public class SequenceCreater {

	public static String createSequence() {
		return UUID.randomUUID().toString();
	}
}
