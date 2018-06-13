package com.jw.chat.business

import com.jw.chat.db.ChatDataBase
import com.jw.chat.db.bean.Conversation
import com.jw.chat.db.bean.Message

/**
 * Author : jinwangx
 * Created : Administrator on 2018/6/13.
 * Description : 描述
 */
object ConversationBusiness {

    fun query(owner:String)=ChatDataBase.getInstance().conversationDao().query(owner)

    fun insert(conversation:Conversation)=ChatDataBase.getInstance().conversationDao().insert(conversation)

    fun update(conversation:Conversation)=ChatDataBase.getInstance().conversationDao().update(conversation)

    fun insert(message: Message) {
        val cursor=MessageBusiness.query(message.owner!!, message.account!!)
        if (cursor.moveToNext()) {
            // 关闭cursor
            cursor.close()
            var unread = MessageBusiness.getUnreadCountByAccount(message.owner!!, message.account!!)
            val conversation= Conversation()
            conversation.account=message.account
            conversation.content=if(message.type==0) message.content else "图片"
            conversation.owner=message.owner
            conversation.unread_count=unread
            conversation.update_time=System.currentTimeMillis()
            update(conversation)
        } else {
            val conversation = Conversation()
            conversation.account = message.account
            conversation.content = if(message.type==0) message.content else "图片"
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
        ChatDataBase.getInstance().messageDao().update(owner,account,true)
        ChatDataBase.getInstance().conversationDao().update(owner,account,0)
    }

    /**
     * 将owner消息记录中所有未读消息设为已读
     * @param owner String
     */
    fun clearUnreadByOwner(owner: String) {
        ChatDataBase.getInstance().messageDao().update(owner,true)
        ChatDataBase.getInstance().conversationDao().update(owner,0)
    }

    /**
     * 得到对应account所有未读消息条数
     * @param owner String
     * @param account String
     * @return Int
     */
    fun getUnreadByAccountOfOwner(owner: String, account: String)=ChatDataBase.getInstance().conversationDao().getUnreadByAccount(owner,account)

    /**
     * 得到消息记录所有未读消息条数
     * @param owner String
     * @return Int
     */
    fun getAllUnread(owner: String)= ChatDataBase.getInstance().conversationDao().getUnreadByOwner(owner)
}