package com.jw.business.db.dao

import android.arch.persistence.room.*
import com.jw.business.model.bean.Account

@Dao
interface AccountDao {

    @Query("select * from account")
    fun getAll(): List<Account>

    @Query("select * from account where 'current'=1")
    fun getCurrentAccount(): Account

    @Query("select * from account where 'account'=:aaa")
    fun findAccount(aaa: String): Account

    @Insert
    fun insert(account: Account)

    @Update
    fun update(account: Account)

    @Query("update account set 'icon'=:iconPath where 'account'=:account")
    fun update(account: String, iconPath: String)

    @Delete
    fun delete(account: Account)

}