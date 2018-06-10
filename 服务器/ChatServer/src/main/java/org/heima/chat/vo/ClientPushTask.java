package org.heima.chat.vo;

import java.io.Serializable;
import java.util.HashMap;

public class ClientPushTask	implements
							Serializable
{

	/**
	 * 
	 */
	private static final long		serialVersionUID	= 1656780159745914568L;

	private String					id;

	private String					action;

	private String					sender;

	private String					receiver;

	private HashMap<String, String>	params;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public HashMap<String, String> getParams() {
		return params;
	}

	public void setParams(HashMap<String, String> params) {
		this.params = params;
	}

}
