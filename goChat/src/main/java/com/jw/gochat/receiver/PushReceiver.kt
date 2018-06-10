package com.jw.gochat.receiver

import android.content.BroadcastReceiver

abstract class PushReceiver : BroadcastReceiver() {
    companion object {
        val ACTION_TEXT = "action.text"
        val ACTION_INVATION = "action.invation"
        val ACTION_REINVATION = "action.reinvation"
        val ACTION_ICON_CHANGE = "action.iconchange"
        val ACTION_NAME_CHANGE = "action.namechange"
        val KEY_FROM = "from"
        val KEY_TO = "to"
        val KEY_TEXT_CONTENT = "text_content"
    }
}