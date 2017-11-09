package org.heima.chat.control.nio;

import java.sql.Timestamp;
import java.util.Random;

import org.heima.chat.core.Action;
import org.heima.chat.nio.ChatRequest;
import org.heima.chat.nio.ChatResponse;
import org.heima.chat.nio.MessagePusher;
import org.heima.chat.nio.PushCallback;
import org.heima.chat.nio.PushMessage;
import org.heima.chat.nio.PushMessage.Type;
import org.heima.chat.nio.body.TextBody;
import org.heima.chat.pojo.Message;
import org.heima.chat.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// @Control(value = "text")
@Component(value = "nio:text")
public class TextControl implements
						Action
{
	private final String[]	CONTENTS	= new String[] { "叫地主", "我抢地主", "飞机", "不要走决战到天亮",
										"你是GG还是MM" };

	@Autowired
	MessagePusher			messagePusher;

	@Autowired
	MessageService			messageService;

	@Override
	public void doAction(ChatRequest request, ChatResponse response) {
		try {
			String account = (String) request.getSender();

			//		map.put("url", getTypeString(type));
			//		map.put("type", "request");
			//		map.put("receiver", receiver);
			//		map.put("content", getMessageBody(body));

			String receiver = (String) request.get("receiver");
			String content = (String) request.get("content");

			response.put("flag", true);
			response.writeResponse();

			if ("GoChat".equals(receiver)) {
				PushMessage msg = PushMessage.createPushMessage(PushMessage.Type.TEXT);
				msg.setReceiver(account);
				msg.setBody(new TextBody(CONTENTS[new Random().nextInt(CONTENTS.length)]));
				msg.setSender("GoChat");

				messagePusher.push(msg, new PushCallback() {
					@Override
					public void onSuccess() {
						System.out.println("push success");
					}

					@Override
					public void onProgress(int progress) {
						// TODO Auto-generated method stub
					}

					@Override
					public void onError(int error, String msg) {
						System.out.println("push error : " + msg);
					}
				});
			}

			//用于存储纪录
			final Message message = new Message();
			message.setContent(content);
			message.setCreateTime(new Timestamp(System.currentTimeMillis()));
			message.setSender(account);
			message.setReceiver(receiver);
			message.setType(Type.TEXT.ordinal());

			//判断接收者是否在线
			if (messagePusher.isOnline(receiver)) {
				//在线

				//存储到数据库中
				message.setState(1); //正在发送
				messageService.addMessage(message);

				//生成 msg
				PushMessage msg = PushMessage.createPushMessage(PushMessage.Type.TEXT);
				msg.setReceiver(receiver);
				msg.setBody(new TextBody(content));
				msg.setSender(account);

				messagePusher.push(msg, new PushCallback() {
					@Override
					public void onSuccess() {
						message.setState(2);//发送成功
						messageService.updateMessage(message);
					}

					@Override
					public void onProgress(int progress) {
						// TODO Auto-generated method stub
					}

					@Override
					public void onError(int error, String msg) {
						message.setState(3);//发送失败
						messageService.updateMessage(message);
					}
				});
			} else {
				//不在线
				//存储到数据库中
				message.setState(0); //等待发送
				messageService.addMessage(message);
			}
		} catch (Exception e) {

		}
	}

}