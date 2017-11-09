package Lib;

public interface GoChatFuture {

	boolean isCancelled();

	boolean cancel(boolean mayInterruptIfRunning);

	boolean isFinished();
}
