package org.heima.chat.nio;

import com.google.gson.Gson;

public class PushRequest {
	private PushCallback	callback;
	private PushMessage		message;
	private String			sequence;

	public PushRequest(PushCallback callback, PushMessage msg) {
		this.callback = callback;
		this.message = msg;

		sequence = (String) this.message.getMap().get("sequence");
	}

	public String getSequence() {
		return sequence;
	}

	public String getTransport() {
		return new Gson().toJson(this.message.getMap());
	}

	public PushCallback getCallBack() {
		return callback;
	}

	public String getReceiver() {
		return message.getMap().get("receiver").toString();
	}

}
