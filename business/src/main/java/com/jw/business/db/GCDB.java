package com.jw.business.db;

public interface GCDB {
	String NAME = "gochat.db";
	int VERSION = 1;

	interface Account {
		//用户
		String TABLE_NAME = "account";
		//id,账号，姓名，性别，头像，   ，地区，   ，
		String COLUMN_ID = "_id";
		String COLUMN_ACCOUNT = "account";    //账号
		String COLUMN_NAME = "name";          //姓名
		String COLUMN_SEX = "sex";			  //性别
		String COLUMN_ICON = "icon";		  //头像
		String COLUMN_SIGN = "sign";		  //
		String COLUMN_AREA = "area";          //地区
		String COLUMN_TOKEN = "token";
		String COLUMN_CURRENT = "current";

		String SQL_CREATE_TABLE = "create table " + TABLE_NAME + " ("
				+ COLUMN_ID + " integer primary key autoincrement, "
				+ COLUMN_ACCOUNT + " text," + COLUMN_NAME + " text,"
				+ COLUMN_SEX + " integer," + COLUMN_ICON + " text,"
				+ COLUMN_SIGN + " text," + COLUMN_AREA + " text,"
				+ COLUMN_TOKEN + " text," + COLUMN_CURRENT + " integer" + ")";
	}

	interface Friend {
		//朋友
		String TABLE_NAME = "friend";
		//id,拥有者，账号，姓名，   ，地区，头像，性别，备注名，    ，
		String COLUMN_ID = "_id";
		String COLUMN_OWNER = "owner";            //本人
		String COLUMN_ACCOUNT = "account";        //该朋友账号
		String COLUMN_NAME = "name";			  //该朋友姓名
		String COLUMN_SIGN = "sign";
		String COLUMN_AREA = "area";			  //该朋友地区
		String COLUMN_ICON = "icon";			  //该朋友头像
		String COLUMN_SEX = "sex";				  //该朋友性别
		String COLUMN_NICKNAME = "nick_name";
		String COLUMN_ALPHA = "alpha";
		String COLUMN_SORT = "sort";

		String SQL_CREATE_TABLE = "create table " + TABLE_NAME + " ("
				+ COLUMN_ID + " integer primary key autoincrement, "
				+ COLUMN_OWNER + " text," + COLUMN_ACCOUNT + " text,"
				+ COLUMN_NAME + " text," + COLUMN_SIGN + " text," + COLUMN_AREA
				+ " text," + COLUMN_ICON + " text," + COLUMN_SEX + " integer,"
				+ COLUMN_NICKNAME + " text," + COLUMN_ALPHA + " text,"
				+ COLUMN_SORT + " integer" + ")";
	}

	interface Invitation {
		String TABLE_NAME = "invitation";
		String COLUMN_ID = "_id";
		String COLUMN_OWNER = "owner";                  //本人
		String COLUMN_INVITATOR_ACCOUNT = "invitator_account";// 邀请者的黑信号
		String COLUMN_INVITATOR_NAME = "invitator_name";// 邀请者的名字
		String COLUMN_INVITATOR_ICON = "invitator_icon";// 邀请者的图片
		String COLUMN_CONTENT = "content";// 邀请者的图片
		String COLUMN_AGREE = "agree";// 是否已经同意

		String SQL_CREATE_TABLE = "create table " + TABLE_NAME + " ("
				+ COLUMN_ID + " integer primary key autoincrement, "
				+ COLUMN_OWNER + " text," + COLUMN_INVITATOR_ACCOUNT + " text,"
				+ COLUMN_INVITATOR_ICON + " text," + COLUMN_CONTENT + " text,"
				+ COLUMN_INVITATOR_NAME + " text," + COLUMN_AGREE + " integer"
				+ ")";
	}

	interface Message {
		int TYPE_TEXT = 0;
		int TYPE_IMAGE = 1;

		String TABLE_NAME = "message";
		String COLUMN_ID = "_id";
		String COLUMN_OWNER = "owner";
		String COLUMN_ACCOUNT = "account";// 接收者或发送者
		String COLUMN_DIRECTION = "direct";// 0:发送 1:接收
		String COLUMN_TYPE = "type";
		String COLUMN_CONTENT = "content";
		String COLUMN_URL = "url";
		String COLUMN_STATE = "state";// 发送状态: 1.正在发送 2.已经成功发送 3.发送失败
		String COLUMN_READ = "read";// 0:未读 1:已读
		String COLUMN_CREATE_TIME = "create_time";

		String SQL_CREATE_TABLE = "create table " + TABLE_NAME + " ("
				+ COLUMN_ID + " integer primary key autoincrement, "
				+ COLUMN_OWNER + " text," + COLUMN_ACCOUNT + " text,"
				+ COLUMN_DIRECTION + " integer," + COLUMN_TYPE + " integer,"
				+ COLUMN_CONTENT + " text," + COLUMN_URL + " text,"
				+ COLUMN_STATE + " integer," + COLUMN_READ + " integer,"
				+ COLUMN_CREATE_TIME + " integer" + ")";
	}

	interface Conversation {
		String TABLE_NAME = "conversation";

		String COLUMN_ID = "_id";
		String COLUMN_OWNER = "owner";         //本人
		String COLUMN_ACCOUNT = "account";     //对话者账号
		String COLUMN_ICON = "icon";           //对话者头像
		String COLUMN_NAME = "name";           //对话者姓名
		String COLUMN_CONTENT = "content";     //对话内容
		String COLUMN_UNREAD = "unread_count"; //未读消息数量
		String COLUMN_UPDATE_TIME = "update_time";

		String SQL_CREATE_TABLE = "create table " + TABLE_NAME + " ("
				+ COLUMN_ID + " integer primary key autoincrement, "
				+ COLUMN_OWNER + " text," + COLUMN_ACCOUNT + " text,"
				+ COLUMN_ICON + " text," + COLUMN_NAME + " text,"
				+ COLUMN_CONTENT + " text," + COLUMN_UNREAD + " integer,"
				+ COLUMN_UPDATE_TIME + " integer" + ")";
	}

	interface BackTask {
		String TABLE_NAME = "back_task";

		String COLUMN_ID = "_id";
		String COLUMN_OWNER = "owner";
		String COLUMN_PATH = "path";
		String COLUMN_STATE = "state";// 0:未执行 1:正在执行 2:执行完成 

		String SQL_CREATE_TABLE = "create table " + TABLE_NAME + " ("
				+ COLUMN_ID + " integer primary key autoincrement, "
				+ COLUMN_OWNER + " text," + COLUMN_PATH + " text,"
				+ COLUMN_STATE + " integer" + ")";
	}
}
