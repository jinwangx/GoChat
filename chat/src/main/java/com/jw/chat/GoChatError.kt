package com.jw.chat

interface GoChatError {

    interface Login {
        companion object {
            val PASSWORD_ERROR = 100
            val ACCOUNT_MISS = 101
        }
    }

    interface Register {
        companion object {
            val ACCOUNT_EXIST = 150
        }
    }

    companion object {
        val ERROR_SERVER = 1
        val ERROR_CLIENT_NET = 2
    }
}