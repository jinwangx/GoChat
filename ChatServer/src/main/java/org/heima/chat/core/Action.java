package org.heima.chat.core;

import org.heima.chat.nio.ChatRequest;
import org.heima.chat.nio.ChatResponse;

public interface Action {

	void doAction(ChatRequest request, ChatResponse response);
}
