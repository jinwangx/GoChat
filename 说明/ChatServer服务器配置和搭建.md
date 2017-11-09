## ChatServer配置和搭建

### 项目导入
1. 将 ChatServer 导入到 eclipse 中
2. 导入后如果有报错的请看这里的处理方式 

	> 报错一般是因为你们的eclipse没有装插件。
	> 插件安装流程：
	> 1. eclipse 点击 Help --> Install New software
	> 2. 点击 add
	> 3. 在弹出的对话框中 name 输入框中输入插件名称(可随意取，我这里取的是Maven)
	> 4. 在Location输入框中输入:http://download.eclipse.org/technology/m2e/releases/
	> ![image](/image_notice/install_maven_1.png)
	> 4. 点击ok
	> 5. 选中选择框
	> 6. 点击next
	> ![image](/image_notice/install_maven_2.png)
	> 后面都是下一步下一步之类的
	>
	> maven安装完成后，eclipse重启后会从网上下载jar包，直到ChatServer配置完成(如果网速不好，时间会有点久)




### 配置说明
![image](/image_notice/chat_server_1.png)

1. src/main/resource下存放的都是项目资源文件dataSource.properties
	
	> 数据源文件，存储的是和数据库连接配置
	> 需要修改的内容：
	> ![image](/image_notice/chat_server_2.png)
	> 首先，你电脑本地得安装mysql，并且在mysql数据库中新建一个叫做gochat的数据库
	> 其次，‘chat.username=’后面的值设为你数据库的用户名
	> 最后，‘chat.password=’后面的值设为你数据库的密码


2. src/main/resource下存放的都是项目资源文件mina.properties

	> 存储的是 mina的端口配置：默认是9090，如有需要，可自行修改，但是手机客户端必须也修改为对应的端口访问

