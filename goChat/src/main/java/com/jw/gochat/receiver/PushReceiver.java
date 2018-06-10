package com.jw.gochat.receiver;

import android.content.BroadcastReceiver;


public abstract class PushReceiver extends BroadcastReceiver {

	public final static String ACTION_TEXT = "action.text";
	public final static String ACTION_INVATION = "action.invation";
	public final static String ACTION_REINVATION = "action.reinvation";
	public final static String ACTION_ICON_CHANGE = "action.iconchange";
	public final static String ACTION_NAME_CHANGE = "action.namechange";

	public final static String KEY_FROM = "from";
	public final static String KEY_TO = "to";

	public final static String KEY_TEXT_CONTENT = "text_content";
}
