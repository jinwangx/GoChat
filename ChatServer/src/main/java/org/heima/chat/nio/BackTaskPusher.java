package org.heima.chat.nio;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.heima.chat.core.ContextUtil;
import org.heima.chat.core.SerializableUtil;
import org.heima.chat.nio.PushMessage.Type;
import org.heima.chat.nio.body.NormalBody;
import org.heima.chat.pojo.BackPushTask;
import org.heima.chat.service.BackPushTaskService;
import org.heima.chat.vo.ClientPushTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BackTaskPusher {
	private ExecutorService	pool;

	//	@Autowired
	//	SocketDispatcher		socketDispatcher;

	@Autowired
	BackPushTaskService		taskService;

	@Autowired
	ContextUtil				contextUtil;

	public BackTaskPusher() {
		pool = Executors.newFixedThreadPool(1);
	}

	public void doPush() {
		pool.submit(new Runnable() {

			@Override
			public void run() {
				List<BackPushTask> list = taskService.queryAllPendingTask();
				push(list);
			}
		});
	}

	public void doPush(final String account) {
		pool.submit(new Runnable() {

			@Override
			public void run() {
				List<BackPushTask> list = taskService.queryPendingTaskByAccount(account);
				push(list);
			}
		});
	}

	private void push(List<BackPushTask> list) {
		if (list != null) {
			SocketDispatcher socketDispatcher =
												ContextUtil.getApplicationContext()
															.getBean(SocketDispatcher.class);

			for (final BackPushTask task : list) {
				try {
					ClientPushTask pushTask =
												(ClientPushTask) SerializableUtil.read(task.getPath());

					String receiver = pushTask.getReceiver();
					String sender = pushTask.getSender();

					PushMessage msg = PushMessage.createPushMessage(Type.TEXT);
					msg.setSender(sender);
					msg.setReceiver(receiver);
					msg.setBody(new NormalBody(pushTask.getAction(),
												(Map) pushTask.getParams()));

					System.out.println("receiver : " + receiver);
					//接收者在线
					if (socketDispatcher.isOnline(receiver)) {
						System.out.println("online : " + receiver);

						//设置为运行中
						task.setState(1);
						taskService.updateTask(task);

						socketDispatcher.addPush(new PushRequest(new PushCallback() {
							@Override
							public void onSuccess() {
								task.setState(2);
								taskService.updateTask(task);

								//删除文件
								File file = new File(task.getPath());
								file.delete();
							}

							@Override
							public void onProgress(int progress) {}

							@Override
							public void onError(int error, String msg) {
								task.setState(3);
								taskService.updateTask(task);
							}
						}, msg));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

}
