package com.jw.chat.business

import com.jw.chat.db.ChatDataBase
import com.jw.chat.db.bean.Message

/**
 * Author : jinwangx
 * Created : Administrator on 2018/6/13.
 * Description : 描述
 */
object MessageBusiness {
    /**
     * 新增消息
     * @param message Message
     * @return Int 1为成功
     */
    fun insert(message: Message) = ChatDataBase.getInstance().messageDao().insert(message)

    /**
     * 更新消息
     * @param message Message
     * @return Int 1为成功
     */
    fun update(message: Message) = ChatDataBase.getInstance().messageDao().update(message)

    /**
     * 更新对应account所有消息的阅读状态
     * @param owner String
     * @param account String
     * @param read Boolean 0为未读，1为已读
     * @return Int 1为成功
     */
    fun update(owner: String, account: String, read: Boolean) = ChatDataBase.getInstance().messageDao().update(owner, account, read)

    /**
     * 得到对应account所有消息
     * @param owner String
     * @param account String
     * @return Cursor
     */
    fun query(owner: String, account: String) = ChatDataBase.getInstance().messageDao().query(owner, account)

    /**
     * 得到对应account未读消息的条数
     * @param owner String
     * @param account String
     * @return Int 未读消息的条数
     */
    fun getUnreadCountByAccount(owner: String, account: String) = ChatDataBase.getInstance().messageDao().getUnreadCountByAccount(owner, account)

    /**
     * 得到对应owner所有未读消息的条数
     * @param owner String
     * @return Int 未读消息的条数
     */
    fun getUnreadCountByOwner(owner: String) = ChatDataBase.getInstance().messageDao().getUnreadCountByOwner(owner)
}