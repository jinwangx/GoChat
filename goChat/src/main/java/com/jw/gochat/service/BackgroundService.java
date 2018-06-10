package com.jw.gochat.service;

import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

import com.jw.gochat.ChatApplication;
import com.jw.business.bean.Account;
import com.jw.business.bean.NetTask;
import com.jw.business.db.dao.BackTaskDao;
import com.jw.business.db.GCDB;
import com.jw.gochatbase.BaseIntentService;
import com.jw.library.utils.FileUtils;
import com.jw.library.utils.HttpUtils;

import java.util.HashMap;
import java.util.Map;

import com.jw.chat.GoChatManager;
import com.jw.chat.GoChatURL;

/**
 * 创建时间：
 * 更新时间 2017/10/30 0:23
 * 版本：
 * 作者：Mr.jin
 * 描述：后台服务，可执行多次，每次任务执行完后自动销毁，用于执行后台任务
 */

public class BackgroundService extends BaseIntentService {
	private Account me=ChatApplication.getAccount();
	private Map<String, String> headers;

	public BackgroundService() {
		super("background");
	}
	@Override
	protected void onHandleIntent(Intent intent) {

		GoChatManager.getInstance(ChatApplication.getOkHttpClient()).initAccount(me.getAccount(),
				me.getToken());
		headers = new HashMap<>();
        Log.v("attt",me.getAccount()+"-"+me.getToken());
		headers.put("account",me.getAccount());
		headers.put("token",me.getToken());
		final BackTaskDao dao = new BackTaskDao(this);
		Map<Long, String> map = new HashMap<>();
		String owner = me.getAccount();
		Cursor cursor = dao.query(owner, 0);
		if (cursor != null) {
			while (cursor.moveToNext()) {
				final long id = cursor.getLong(cursor
						.getColumnIndex(GCDB.BackTask.COLUMN_ID));
				String filePath = cursor.getString(cursor
						.getColumnIndex(GCDB.BackTask.COLUMN_PATH));
				map.put(id, filePath);
			}
			cursor.close();
		}

		for (Map.Entry<Long, String> me : map.entrySet()) {
			try {
				final Long id = me.getKey();
				String filePath = me.getValue();
				NetTask task = (NetTask) FileUtils.read(filePath);
				int type = task.getType();
				if (type == NetTask.TYPE_NORMAL) {
					doNormalTask(dao, id, task);
				} else if (type == NetTask.TYPE_UPLOAD) {
					doUploadTask(dao, id, task);
				} else if (type == NetTask.TYPE_DOWNLOAD) {
					doDownloadTask(dao,id,task);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	private void doNormalTask(final BackTaskDao dao, final Long id, NetTask task) {
		boolean result = HttpUtils.getInstance().post(GoChatURL.BASE_HTTP+task.getPath(),headers,
				task.getParams());
		if (result) {
			Log.v("backGroundService",task.getType()+"任务后台执行成功");
			dao.updateState(id, 2);
		}
	}

	private void doUploadTask(final BackTaskDao dao, final Long id, NetTask task) {
		HashMap<String, String> files = task.getFiles();
		if (files != null) {
			HttpUtils.getInstance().upload(GoChatURL.BASE_HTTP+task.getPath(),headers,task.getParams(),
					task.getFiles());
		}
	}

	private void doDownloadTask(final BackTaskDao dao, final Long id, NetTask task) {
		HashMap<String, String> files = task.getFiles();
		if (files != null) {
			HttpUtils.getInstance().download(GoChatURL.BASE_HTTP+task.getPath(),headers,task.getParams(),
					task.getFiles());
		}
	}
}
