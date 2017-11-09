package Lib.msg;

/**
 * 创建时间：
 * 更新时间 2017/11/1 23:14
 * 版本：
 * 作者：Mr.jin
 * 描述：聊天消息体
 */

public class TextBody implements MessageBody {
	private String content;

	public TextBody(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}
}