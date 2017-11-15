## 说明

### 应用设计思想及主要技术
1. 大体上遵循MVC设计模式
   应用管理类充当Controller，管理类中具体的功能实现方法充当Model，Activity以及fragment充当View。
2. 聊天引擎类采用Mina框架进行TCP通信，Controller给其io设置监听，当有消息从服务器推送过来时，
    Controller调用对应Model，Model执行完数据库更新后，发送广播，通知相关View执行更新操作
3. Http通信（如注册、登录、好友查询）,Controller将请求封装，并调用自己的OKHttp封装工具类：
    耦合性好，并都可设置请求头、参数，异步请求都可传入监听并返回可操作Future
    * get请求,同步请求；post提交，同步请求
    * 下载，同步请求；上传，同步请求
    * post异步请求；上传，异步请求
4. 数据库有五张表:
    * 用户信息
    * 后台服务
    * 好友信息
    * 邀请信息
    * 聊天信息

### 用到的自定义控件（在README.md的gif图中均有展示）
1. RefreshListView,继承自ListView,具有下拉刷新功能
2. HomeDrag,继承自FrameLayout，运用ViewDragHelper，实现了主界面侧拉效果
3. SlideDeleteDrag,和HomeDrag异曲同工，实现了消息列表item侧拉删除、置顶
4. NormalTopBar,继承自LinearLayout,类似于ToolBar,可设置各种监听，根据设置的监听加载不同布局
5. 多种类型自定义Dialog
6. Button、EditText各种选择器

### 用到的开源框架
  * Glide
  * Butterknife

### 集成的第三方开放平台
  * 科大讯飞开放平台（聊天信息语音输入）


