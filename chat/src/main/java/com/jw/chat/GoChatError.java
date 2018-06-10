package com.jw.chat;

public interface GoChatError {

	int ERROR_SERVER = 1;
	int ERROR_CLIENT_NET = 2;

	interface Login {
		int PASSWORD_ERROR = 100;
		int ACCOUNT_MISS = 101;
	}

	interface Register {
		int ACCOUNT_EXIST = 150;
	}
}
