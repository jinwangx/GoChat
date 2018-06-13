package com.jw.business.business

import com.jw.business.db.AppDatabase
import com.jw.business.db.GCDB
import com.jw.business.model.bean.Invitation

/**
 * Author : jinwangx
 * Created : Administrator on 2018/6/13.
 * Description : 描述
 */
object InvitationBusiness {

    /**
     * 得到拥有者所有的邀请
     * @param owner String
     * @return Cursor
     */
    fun getInvitationByOwner(owner: String) = AppDatabase.getInstance().invitationDao().getInvitationByOwner(owner)

    /**
     * 根据account得到邀请信息
     * @param owner String
     * @param account String
     * @return Invitation?
     */
    fun getInvitationByAccount(owner: String, account: String) = AppDatabase.getInstance().invitationDao().getInvitationByAccount(owner, account)

    /**
     * 新增邀请
     * @param invitation Invitation
     */
    fun addInvitation(invitation: Invitation) {
        AppDatabase.getInstance().invitationDao().addInvitation(invitation)
    }

    /**
     * 更新邀请
     * @param invitation Invitation
     */
    fun updateInvitation(invitation: Invitation) {
        AppDatabase.getInstance().invitationDao().updateInvitation(invitation)
    }

    /**
     * 是否有未同意邀请
     * @param owner String
     * @return Boolean
     */
    fun hasUnagree(owner: String): Boolean {
        val agreeCount = AppDatabase.getInstance().invitationDao().getUnAgreeCount(owner)
        return agreeCount!=0
    }
}