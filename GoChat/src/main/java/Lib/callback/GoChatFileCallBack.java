package Lib.callback;

import java.io.File;

/**
 * 创建时间：
 * 更新时间 2017/11/1 20:46
 * 版本：
 * 作者：Mr.jin
 * 描述：文件下载回调接口，本项目虽用到头像下载，但图片的下载由Glide控制
 */

public abstract class GoChatFileCallBack {

	public abstract void onSuccess(File file);

	public abstract void onProgress(int progress);

	public abstract void onError(int error, String msg);

}
