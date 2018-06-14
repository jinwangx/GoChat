package com.jw.business.business

import com.jw.business.db.AppDatabase
import com.jw.business.db.model.AccountInfo

/**
 * 作者 : jinwangx
 * 创建时间 : 2018/6/13
 * 描述 : 业务类(账号)
 */
object AccountInfoBusiness {

    /**
     * 得到所有账户
     * @return List<AccountInfo>
     */
    fun getAccountInfoAll() = AppDatabase.getInstance().accountDao().getAccountInfoAll()

    /**
     * 得到当前账户
     * @return AccountInfo
     */
    fun getCurrentAccountInfo() = AppDatabase.getInstance().accountDao().getCurrentAccountInfo()

    /**
     * 根据id查找账户
     * @param aaa String
     * @return AccountInfo
     */
    fun getAccountInfoByAccount(aaa: String) = AppDatabase.getInstance().accountDao().getAccountInfoByAccount(aaa)

    /**
     * 根据id更新账户头像本地存储地址
     * @param account String
     * @param iconPath String
     */
    fun updateIconByAccount(account: String, iconPath: String) = AppDatabase.getInstance().accountDao().updateIconByAccount(account, iconPath)

    /**
     * 新增账户
     * @param accountInfo AccountInfo
     */
    fun insert(accountInfo: AccountInfo) {
        AppDatabase.getInstance().accountDao().insert(accountInfo)
    }

    /**
     * 更新账户
     * @param accountInfo AccountInfo
     */
    fun update(accountInfo: AccountInfo) {
        AppDatabase.getInstance().accountDao().update(accountInfo)
    }

    /**
     * 删除账户
     * @param accountInfo AccountInfo
     */
    fun delete(accountInfo: AccountInfo) {
        AppDatabase.getInstance().accountDao().delete(accountInfo)
    }
}