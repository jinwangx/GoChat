package Lib.callback;

/**
 * 创建时间：
 * 更新时间 2017/11/1 20:49
 * 版本：
 * 作者：Mr.jin
 * 描述：本项目通用回调
 */

public interface GoChatCallBack {

	void onSuccess();

	void onProgress(int progress);

	void onError(int error, String msg);

}
