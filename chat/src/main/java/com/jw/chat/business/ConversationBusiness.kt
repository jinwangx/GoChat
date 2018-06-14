package com.jw.chat.business

import com.jw.chat.db.ChatDataBase
import com.jw.chat.db.bean.Conversation
import com.jw.chat.db.bean.Message

/**
 * 作者 : jinwangx
 * 创建时间 : 2018/6/13
 * 描述 : 业务类(会话信息)
 */
object ConversationBusiness {

    /**
     * 得到当前账号会话列表Cursor
     * @param owner String
     * @return Cursor?
     */
    fun query(owner: String) = ChatDataBase.getInstance().conversationDao().query(owner)

    /**
     * 得到当前账号某一联系人会话
     * @param owner String
     * @param account String
     * @return Conversation?
     */
    fun query(owner: String, account: String) = ChatDataBase.getInstance().conversationDao().query(owner, account)

    /**
     * 新增会话
     * @param conversation Conversation
     * @return Long 返回1，插入成功
     */
    fun insert(conversation: Conversation) = ChatDataBase.getInstance().conversationDao().insert(conversation)

    /**
     * 更新会话信息(最后一条消息、最后一条消息时间)
     * @param conversation Conversation
     * @return Int
     */
    fun update(conversation: Conversation) = ChatDataBase.getInstance().conversationDao().update(conversation)

    fun insert(message: Message) {
        val conversation = query(message.owner!!, message.account!!)
        if (conversation != null) {
            conversation.account = message.account
            conversation.content = if (message.type == 0) message.content else "[图片]"
            conversation.owner = message.owner
            conversation.unread_count = MessageBusiness.getUnreadCountByAccount(message.owner!!, message.account!!)
            conversation.update_time = System.currentTimeMillis()
            update(conversation)
        } else {
            val conversation = Conversation()
            conversation.account = message.account
            conversation.content = if (message.type == 0) message.content else "[图片]"
            conversation.owner = message.owner
            conversation.unread_count = if (message.read) 0 else 1
            conversation.update_time = System.currentTimeMillis()
            insert(conversation)
        }
    }

    /**
     * 将对应account未读消息设为已读
     * @param owner String
     * @param account String
     */
    fun clearUnreadByAccount(owner: String, account: String) {
        ChatDataBase.getInstance().messageDao().update(owner, account, true)
        ChatDataBase.getInstance().conversationDao().update(owner, account, 0)
    }

    /**
     * 将当前账号所有未读消息设为已读，并且更新会话列表，设所有会话已读
     * @param owner String
     */
    fun clearUnreadByOwner(owner: String) {
        ChatDataBase.getInstance().messageDao().update(owner, true)
        ChatDataBase.getInstance().conversationDao().update(owner, 0)
    }

    /**
     * 得到当前账号某一联系人会话未读消息条数
     * @param owner String
     * @param account String
     * @return Int
     */
    fun getUnreadByAccountOfOwner(owner: String, account: String) = ChatDataBase.getInstance().conversationDao().getUnreadByAccount(owner, account)

    /**
     * 得到当前账号所有会话未读消息条数总和
     * @param owner String
     * @return Int
     */
    fun getAllUnread(owner: String) = ChatDataBase.getInstance().conversationDao().getUnreadByOwner(owner)
}