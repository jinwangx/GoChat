package org.heima.chat.nio;

import java.util.HashMap;
import java.util.Map;

import org.heima.chat.nio.body.InvitationBody;
import org.heima.chat.nio.body.ReInvitationBody;
import org.heima.chat.nio.body.TextBody;

public class PushMessage {
	private Map<String, Object>	map;
	private MessageBody			body;
	private String				receiver;
	private String				sender;
	private Type				type;

	private PushMessage() {}

	public enum Type {
		TEXT, IMAGE, INVITATION, REINVITATION
	}

	public static PushMessage createPushMessage(Type type) {
		PushMessage msg = new PushMessage();
		msg.type = type;
		return msg;
	}

	public Map<String, Object> getMap() {
		map = new HashMap<String, Object>();

		map.put("action", getTypeString(type));
		map.put("type", "request");
		map.put("sequence", SequenceCreater.createSequence());
		map.put("sender", sender);
		map.put("receiver", receiver);
		map.putAll(body.getMap());

		return map;
	}

	public void setBody(MessageBody body) {
		this.body = body;
	}

	public void setSender(String sender) {
		this.sender = sender;
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

	private String getTypeString(Type type) {
		switch (type)
		{
		case TEXT:
			return "text";
		case IMAGE:
			return "image";
		case INVITATION:
			return "invitation";
		case REINVITATION:
			return "reinvitation";
		default:
			break;
		}
		return null;
	}

	private Object getMessageBody(MessageBody body) {
		if (body instanceof TextBody) {
			return ((TextBody) body).getContent();
		} else if (body instanceof InvitationBody) {
			return ((InvitationBody) body).getContent();
		} else if (body instanceof ReInvitationBody) { return ((ReInvitationBody) body).getContent(); }

		return null;
	}

}
