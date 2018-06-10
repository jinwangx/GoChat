package com.jw.business.db.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.jw.business.bean.Invitation
import com.jw.business.db.GCDB
import com.jw.business.db.GCDBOpenHelper

class InvitationDao(context: Context) {
    private val helper: GCDBOpenHelper = GCDBOpenHelper.getInstance(context)

    fun queryCursor(owner: String): Cursor {
        val db = helper.readableDatabase
        val sql = ("select * from " + GCDB.Invitation.TABLE_NAME + " where "
                + GCDB.Invitation.COLUMN_OWNER + "=?")
        return db.rawQuery(sql, arrayOf(owner))
    }

    fun addInvitation(invitation: Invitation) {
        val db = helper.writableDatabase
        val values = ContentValues()
        values.put(GCDB.Invitation.COLUMN_OWNER, invitation.owner)
        values.put(GCDB.Invitation.COLUMN_INVITATOR_ACCOUNT, invitation.account)
        values.put(GCDB.Invitation.COLUMN_INVITATOR_NAME, invitation.name)
        values.put(GCDB.Invitation.COLUMN_INVITATOR_ICON, invitation.icon)
        values.put(GCDB.Invitation.COLUMN_CONTENT, invitation.content)
        values.put(GCDB.Invitation.COLUMN_AGREE, if (invitation.isAgree) 1 else 0)
        db.insert(GCDB.Invitation.TABLE_NAME, null, values)
    }

    fun updateInvitation(invitation: Invitation) {
        val db = helper.writableDatabase
        val values = ContentValues()
        values.put(GCDB.Invitation.COLUMN_INVITATOR_NAME, invitation.name)
        values.put(GCDB.Invitation.COLUMN_INVITATOR_ICON, invitation.icon)
        values.put(GCDB.Invitation.COLUMN_CONTENT, invitation.content)
        values.put(GCDB.Invitation.COLUMN_AGREE, if (invitation.isAgree) 1 else 0)
        val whereClause = (GCDB.Invitation.COLUMN_OWNER + "=? and "
                + GCDB.Invitation.COLUMN_INVITATOR_ACCOUNT + "=?")
        val whereArgs = arrayOf<String>(invitation.owner!!, invitation.account!!)
        db.update(GCDB.Invitation.TABLE_NAME, values, whereClause, whereArgs)
    }

    fun queryInvitation(owner: String, account: String): Invitation? {
        val db = helper.writableDatabase
        val sql = ("select * from " + GCDB.Invitation.TABLE_NAME + " where "
                + GCDB.Invitation.COLUMN_OWNER + "=? and "
                + GCDB.Invitation.COLUMN_INVITATOR_ACCOUNT + "=?")
        val cursor = db.rawQuery(sql, arrayOf(owner, account))
        var invitation: Invitation? = null
        if (cursor != null) {
            if (cursor.moveToNext()) {
                // String account = cursor
                // .getString(cursor
                // .getColumnIndex(GCDB.Invitation.COLUMN_INVITATOR_ACCOUNT));
                val name = cursor.getString(cursor
                        .getColumnIndex(GCDB.Invitation.COLUMN_INVITATOR_NAME))
                val icon = cursor.getString(cursor
                        .getColumnIndex(GCDB.Invitation.COLUMN_INVITATOR_ICON))
                val agree = cursor.getInt(cursor
                        .getColumnIndex(GCDB.Invitation.COLUMN_AGREE)) == 1
                val content = cursor.getString(cursor
                        .getColumnIndex(GCDB.Invitation.COLUMN_CONTENT))
                // String owner = cursor.getString(cursor
                // .getColumnIndex(GCDB.Invitation.COLUMN_OWNER));
                val id = cursor.getLong(cursor
                        .getColumnIndex(GCDB.Invitation.COLUMN_ID))
                invitation = Invitation()
                invitation.account = account
                invitation.isAgree = agree
                invitation.content = content
                invitation.icon = icon
                invitation.name = name
                invitation.owner = owner
                invitation.id = id
            }
            cursor.close()
        }
        return invitation
    }

    fun hasUnagree(owner: String): Boolean {
        val db = helper.writableDatabase
        val sql = ("select count(" + GCDB.Invitation.COLUMN_ID + ") from "
                + GCDB.Invitation.TABLE_NAME + " where "
                + GCDB.Invitation.COLUMN_AGREE + "=0")
        val cursor = db.rawQuery(sql, null)
        var count = 0
        if (cursor != null) {
            if (cursor.moveToNext()) {
                count = cursor.getInt(0)
            }
            cursor.close()
        }
        return count != 0
    }
}