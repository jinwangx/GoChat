package Lib.msg;

import java.util.HashMap;
import java.util.Map;

/**
 * 创建时间：
 * 更新时间 2017/11/1 23:05
 * 版本：
 * 作者：Mr.jin
 * 描述：聊天信息
 */

public class ChatMessage {
	private Map<String, Object> map;

	private MessageBody body;
	private String receiver;
	private String account;
	private String token;

	private Type type;

	public enum Type {
		TEXT, IMAGE, INVITATION
	}

	private ChatMessage() {
	}

	public static ChatMessage createMessage(Type type) {
		ChatMessage msg = new ChatMessage();
		msg.type = type;

		return msg;
	}

	public void setBody(MessageBody body) {
		this.body = body;
	}

	public MessageBody getBody() {
		return body;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Map<String, Object> getMap() {
		map = new HashMap<String, Object>();

		map.put("type", "request");
		map.put("sequence", SequenceCreater.createSequence());
		map.put("sender", account);
		map.put("token", token);
		map.put("action", getTypeString(type));
		map.put("receiver", receiver);
		map.put("content", getMessageBody(body));
		return map;
	}

	private String getTypeString(Type type) {
		switch (type) {
		case TEXT:
			return "text";
		case IMAGE:
			return "image";
		case INVITATION:
			return "invitation";
		default:
			break;
		}
		return null;
	}

	private String getMessageBody(MessageBody body) {
		if (body instanceof TextBody) {
			return ((TextBody) body).getContent();
		} else if (body instanceof InvitationBody) {
			return ((InvitationBody) body).getContent();
		}
		return null;
	}
}
