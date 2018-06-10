package com.jw.chat.future;

import com.jw.chat.GoChatFuture;
import okhttp3.Call;

/**
 * 创建时间：
 * 更新时间 2017/11/2 16:28
 * 版本：
 * 作者：Mr.jin
 * 描述：Http请求返回的future，可以操作网络请求，如取消请求等
 */

public class HttpFuture implements GoChatFuture {

	private Call call;

	public HttpFuture(Call call) {
		this.call = call;
	}


	@Override
	public boolean isCancelled() {
		return call == null || call.isCanceled();
	}

	//return call == null || call.cancel(mayInterruptIfRunning);
	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		return mayInterruptIfRunning;
	}

	@Override
	public boolean isFinished() {
		return call == null || call.isExecuted();
	}
}
