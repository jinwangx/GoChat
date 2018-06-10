package com.jw.chat;

public interface GoChatFuture {

	boolean isCancelled();

	boolean cancel(boolean mayInterruptIfRunning);

	boolean isFinished();
}
