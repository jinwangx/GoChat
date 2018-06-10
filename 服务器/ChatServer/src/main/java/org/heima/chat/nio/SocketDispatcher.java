package org.heima.chat.nio;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.heima.chat.core.Action;
import org.heima.chat.core.ContextUtil;
import org.heima.chat.core.annotation.Control;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

public class SocketDispatcher	implements
								IoHandler
{
	private Logger									logger			=
																		LoggerFactory.getLogger(SocketDispatcher.class);

	private static Map<String, Action>				actionPool		=
																		new LinkedHashMap<String, Action>();
	private static Map<Long, IoSession>				sessionPool		=
																		new LinkedHashMap<Long, IoSession>();

	private static Map<String, Long>				accountPool		=
																		new LinkedHashMap<String, Long>();

	private static Map<String, PushRequest>			pushPool		=
																		new LinkedHashMap<String, PushRequest>();
	private static LinkedBlockingQueue<PushRequest>	pushQueue		=
																		new LinkedBlockingQueue<PushRequest>(
																												256);
	private static int								pushWorkerSize	= 16;

	private static ExecutorService					requestWorkerThreadPool;

	private String									classPath;

	public SocketDispatcher() {
		ApplicationContext context = ContextUtil.getApplicationContext();
		classPath = context.getClassLoader().getResource("/").getPath();
		logger.info("path : " + classPath);

		String[] names = context.getBeanDefinitionNames();
		for (String string : names) {
			logger.info("name : " + string);
		}

		//新建线程池
		requestWorkerThreadPool = Executors.newFixedThreadPool(128);

		//扫描类
		//		scan(new File(classPath));

		scanNioControl();

		//初始化推送的线程工作者
		for (int i = 0; i < pushWorkerSize; i++) {
			new Thread(new PushWorker()).start();
		}
	}

	private void scanNioControl() {
		ApplicationContext context = ContextUtil.getApplicationContext();
		String[] names = context.getBeanDefinitionNames();

		for (String name : names) {
			System.out.println("name : " + name);

			if (name.startsWith("nio:")) {
				String replace = name.replace("nio:", "");
				actionPool.put(replace, (Action) context.getBean(name));
			}
		}
	}

	private void scan(File file) {
		try {
			if (file.isFile()) {
				//找到一个文件。。。。。。。
				if (file.getName().endsWith(".class")) {
					//找到了一个类
					String classRealPath = file.getPath(); //c:\\tomcat\.......\BookAction.class

					//加载类，判断是否是action
					ChatClassLoader myloader =
												new ChatClassLoader(
																	SocketDispatcher.class.getClassLoader());
					Class clazz = myloader.load(classRealPath);

					Control control = (Control) clazz.getAnnotation(Control.class);

					if (control != null) {
						boolean flag = false;
						Class[] interfaces = clazz.getInterfaces();
						for (Class intClass : interfaces) {
							if (intClass.equals(Action.class)) {
								flag = true;
								break;
							}
						}

						if (flag) {
							//意味着找到了一个action
							Object action = clazz.newInstance();

							String uri = control.value();
							actionPool.put(uri, (Action) action);
						}
					}
				}
			} else {
				File files[] = file.listFiles();
				for (File child : files) {
					scan(child);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void addPush(PushRequest request) {
		String sequence = request.getSequence();
		try {
			pushPool.put(sequence, request);

			pushQueue.put(request);
		} catch (InterruptedException e) {
			e.printStackTrace();

			pushPool.remove(sequence);

			PushCallback callBack = request.getCallBack();
			if (callBack != null) {
				callBack.onError(1, "服务器问题");
			}
		}
	}

	public boolean isOnline(String account) {
		return accountPool.containsKey(account);
	}

	class PushWorker implements
					Runnable
	{

		@Override
		public void run() {
			while (true) {
				PushRequest request = null;
				try {
					request = pushQueue.take();

					String receiver = request.getReceiver();
					IoSession session = sessionPool.get(accountPool.get(receiver));
					if (session != null) {
						session.write(request.getTransport());
					} else {
						PushCallback callBack = request.getCallBack();
						if (callBack != null) {
							callBack.onError(1, "服务器问题");
						}
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
					PushCallback callBack = request.getCallBack();
					if (callBack != null) {
						callBack.onError(1, "服务器问题");
					}
				}
			}
		}
	}

	class ChatClassLoader	extends
							ClassLoader
	{

		public ChatClassLoader(ClassLoader parent) {
			super(parent);
		}

		public Class load(String path) {
			try {
				ByteArrayOutputStream bout = new ByteArrayOutputStream();
				FileInputStream in = new FileInputStream(path);
				int len = 0;
				byte buffer[] = new byte[1024];
				while ((len = in.read(buffer)) > 0) {
					bout.write(buffer, 0, len);
				}
				bout.close();
				byte code[] = bout.toByteArray();

				return super.defineClass(null, code, 0, code.length);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	public void sessionCreated(IoSession session) throws Exception {
		logger.info("sessionCreated");
	}

	public void sessionOpened(IoSession session) throws Exception {
		logger.info("sessionOpened");
	}

	public void sessionClosed(IoSession session) throws Exception {
		logger.info("sessionClosed");

		long id = session.getId();
		sessionPool.remove(id);
		accountPool.remove(session.getAttribute("account"));
	}

	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
		logger.info("sessionIdle");
	}

	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		logger.info("exceptionCaught");

		if (cause instanceof FormatExcetion) {
			long id = session.getId();
			sessionPool.remove(id);
			accountPool.remove(session.getAttribute("account"));
			session.closeOnFlush();
		}
	}

	@SuppressWarnings("serial")
	public void messageReceived(final IoSession session, Object message) throws Exception {
		logger.info("messageReceived : " + message);
		try {
			String json = message.toString();

			JsonObject root = new JsonParser().parse(json).getAsJsonObject();

			final Map<String, Object> clientMap =
													new Gson().fromJson(root,
																		new TypeToken<Map<String, Object>>()
																		{}.getType());
			String type = root.getAsJsonPrimitive("type").getAsString();
			String sequence = root.getAsJsonPrimitive("sequence").getAsString();

			if ("request".equalsIgnoreCase(type)) {
				//客户端发送端请求
				String act = root.getAsJsonPrimitive("action").getAsString();
				String account = root.getAsJsonPrimitive("sender").getAsString();
				final Action action = actionPool.get(act);

				clientMap.put("#account", account);

				//认证请求
				if ("auth".equalsIgnoreCase(act)) {
					//判断是否有其他设备已经登录同一账号
					Long tmpId = accountPool.get(account);
					if (tmpId != null) {
						//将已经在线的用户挤下去
						IoSession ioSession = sessionPool.get(tmpId);

						//踢出用户
						//ioSession.write("{url:'kick',sequence:'" + sequence + "',type:'request'}");
						ioSession.write("{flag:" + false + ",type:'response',sequence:'" + sequence
										+ "'}");
						ioSession.closeOnFlush();
					}

					//添加到管理中来
					long id = session.getId();
					sessionPool.put(id, session);
					accountPool.put(account, id);
					session.setAttribute("account", account);
				}

				if (action != null) {
					final ChatResponse response = new ChatResponse(session);
					response.put("type", "response");
					response.put("sequence", sequence);

					requestWorkerThreadPool.submit(new Runnable() {
						@Override
						public void run() {
							action.doAction(new ChatRequest(clientMap), response);
						}
					});
				}

			} else if ("response".equalsIgnoreCase(type)) {
				//客户端发送端响应
				//TODO:
				// 请求返回response
				JsonPrimitive flagJson = root.getAsJsonPrimitive("flag");
				boolean flag = flagJson.getAsBoolean();
				// 消息发送结果只有 成功或者 失败,不需要返回对象
				if (flag) {
					PushRequest request = pushPool.remove(sequence);
					if (request != null) {
						PushCallback callBack = request.getCallBack();
						if (callBack != null) {
							callBack.onSuccess();
						}
					}
				} else {
					PushRequest request = pushPool.remove(sequence);
					if (request != null) {
						PushCallback callBack = request.getCallBack();
						if (callBack != null) {
							JsonPrimitive errorCodeJson = root.getAsJsonPrimitive("errorCode");
							int errorCode = errorCodeJson.getAsInt();

							JsonPrimitive errorStringJson = root.getAsJsonPrimitive("errorString");
							String errorString = errorStringJson.getAsString();

							callBack.onError(errorCode, errorString);
						}
					}
				}
			}
		} catch (Exception e) {
//			throw new FormatExcetion();
		}
	}

	public void messageSent(IoSession session, Object message) throws Exception {
		logger.info("messageSent");
	}

	private final class FormatExcetion	extends
										Exception
	{

	}
}
