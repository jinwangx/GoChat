package org.heima.chat.dao.impl;

import java.util.List;

import org.heima.chat.dao.BackPushTaskDao;
import org.heima.chat.dao.BaseDaoSupport;
import org.heima.chat.pojo.BackPushTask;
import org.springframework.stereotype.Repository;

@Repository
public class BackPushTaskDaoImpl extends
								BaseDaoSupport<BackPushTask> implements
															BackPushTaskDao
{

	@Override
	public void addTask(BackPushTask task) {
		task.setId((String) save(task));
	}

	@Override
	public void updateTask(BackPushTask task) {
		super.update(task);
	}

	@Override
	public List<BackPushTask> queryPendingTaskByAccount(String account) {
		return findByHQL("from BackPushTask where receiver=? and (state=0 or state=3)", account);
	}

	@Override
	public List<BackPushTask> queryAllPendingTask() {
		return findByHQL("from BackPushTask where state=0 or state=3");
	}

}
