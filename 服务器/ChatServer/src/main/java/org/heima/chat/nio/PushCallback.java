package org.heima.chat.nio;

public interface PushCallback {

	void onSuccess();

	void onProgress(int progress);

	void onError(int error, String msg);
}
