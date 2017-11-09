package Lib.callback;

import java.lang.reflect.ParameterizedType;

/**
 * 创建时间：
 * 更新时间 2017/11/1 20:40
 * 版本：
 * 作者：Mr.jin
 * 描述：网络请求回调，支持泛型，如果成功，可以将访问到的数据转化为javaBean对象，
 *       失败则返回错误代码和信息
 */

public abstract class GoChatObjectCallBack<T> {
	private Class<T> clazz;

	@SuppressWarnings("unchecked")
	public GoChatObjectCallBack() {
		ParameterizedType type = (ParameterizedType) this.getClass()
				.getGenericSuperclass();
		this.clazz = (Class<T>) type.getActualTypeArguments()[0];
	}

	public abstract void onSuccess(T t);

	public abstract void onError(int error, String msg);

	public Class<T> getClazz() {
		return clazz;
	}

}
