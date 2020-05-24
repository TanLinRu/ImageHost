### WebSocket学习

* ##### webSocket简介

* ##### Spring MVC中使用Websocket



> WebSocket简介

​	由于Http协议只能由客户端发起，所以为了解决实时通知客户端的问题，只能通过轮询的方法。而轮询这种方式，不仅对效率非常低且消耗资源极其大。

​	为了解决这种问题，所以WebSocket便诞生了。其特点是，服务器可以主动向客户端推送消息，客户端也可以向服务器发送信息，达到真正的双通通信。WebSocket属于服务器推送技术的一种。

![](https://github.com/TanLinRu/ImageHost/raw/master/WebSocket/images/websocket.png)

WebSocket的特点：

* ##### 建立在TCP协议之上，服务端实现比较容易

* ##### 与HTTP协议有着良好的兼容性。默认端口也是80和443，并且握手阶段采用HTTP协议，因此握手时不容易屏蔽，能通过各种HTTP代理服务器

* ##### 数据格式比较轻量，性能开销小，通信高效

* ##### 数据格式为：文本/二进制数据

* ##### 无同源限制，客户端可与任意服务器通信

* ##### 协议标识符：WS（加密下为：WSS），服务器网址即URL 如下

  ~~~shell
  ws://example.com:80/some/path
  ~~~

> Spring MVC中使用Websocket

需在pom.xml引入

~~~java
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-websocket</artifactId>
    <version>${spring.version}</version>
</dependency>
~~~

编写WebSocket拦截器，代码如下：

~~~
**
 * @Description: websocket拦截器
 * @Author: TanLinquan
 * @Date: 2020/5/20 21:36
 * @Version: V1.0
 **/
public class WebSocketHandshakeInterceptor extends BaseController implements HandshakeInterceptor {

    private final static org.apache.log4j.Logger LOGGER = Logger.getLogger(com.tlq.blog.interceptor.WebSocketHandshakeInterceptor.class);

	//握手前
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {

        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
            HttpSession session = servletRequest.getServletRequest().getSession();
            // 标记用户
            //SPRING_SECURITY_CONTEXT
            SecurityContext securityContext = (SecurityContext) session.getAttribute("SPRING_SECURITY_CONTEXT");
            User user = (User) securityContext.getAuthentication().getPrincipal();
            if (user != null) {
                attributes.put("user", user);
                LOGGER.info("标记用户：" + user.getEmail());
            } else {
                return false;
            }
        }
        return true;

    }

	//握手后
    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }

}
~~~

在拦截器中，通过debug进行查看，其主要作用为了将request请求中获取到相应的用户属性，并将其添加入webSeesion中

编写WebSoket处理器，处理器作用是在握手建立后，对信息的处理

在下方代码是基于在一个用户的文章被点赞或评论后，对用户进行实时通信的实现，具体的实现可以根据不同的函数进行相应的处理

~~~java
/**
 * @Description: Websocket消息处理
 * @Author: TanLinquan
 * @Date: 2020/5/20 14:09
 * @Version: V1.0
 **/
@Component
public class MsgScoketHandle extends BaseController implements WebSocketHandler {

    private final static Logger LOGGER = Logger.getLogger(com.tlq.blog.handler.MsgScoketHandle.class);

    @Autowired
    private UpvoteTmpService upvoteTmpService;

    //已经连接的用户
    private static final Map<String, WebSocketSession> users;

    static {
        //保存当前连接用户
        users = new HashMap<>();
    }

    //建立连接后
    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws Exception {

        LOGGER.debug("===连接成功===");
        //将用户信息添加到map中

        User user = (User) webSocketSession.getAttributes().get("user");
        if (user != null) {
            LOGGER.debug("当前连接用户：" + user.getEmail());
            users.put(user.getEmail(), webSocketSession);
            //检测用户是否有未读信息，有则进行推送
            if (upvoteTmpService.userUpvoteTmpMessage(user) != 0) {
                sendMessageToUser(user);
            }
        }
        LOGGER.debug("当前连接数量：" + users.size());

    }

    //消息处理
    @Override
    public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage) throws Exception {

        //服务端推送消息处理

    }

    //异常处理
    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {

        if (webSocketSession.isOpen()) {
            webSocketSession.close();
        }
        Iterator<Map.Entry<String, WebSocketSession>> it = users.entrySet().iterator();
        // 移除Socket会话
        while (it.hasNext()) {
            Map.Entry<String, WebSocketSession> entry = it.next();
            if (entry.getValue().getId().equals(webSocketSession.getId())) {
                users.remove(entry.getKey());
                LOGGER.warn("Socket会话已经移除:用户ID" + entry.getKey());
                break;
            }
        }

    }

    //连接断开
    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {

        LOGGER.info("WebSocket:" + webSocketSession.getId() + "断开连接");
        Iterator<Map.Entry<String, WebSocketSession>> it = users.entrySet().iterator();
        // 移除Socket会话
        while (it.hasNext()) {
            Map.Entry<String, WebSocketSession> entry = it.next();
            if (entry.getValue().getId().equals(webSocketSession.getId())) {
                users.remove(entry.getKey());
                LOGGER.info("Socket会话已经移除:用户ID" + entry.getKey());
                break;
            }
        }

    }

    @Override
    public boolean supportsPartialMessages() {

        return false;

    }

    /**
    * @Description: 发送消息给指定的用户
    * @Author: TanLinquan
    * @Date: 2020/5/20 16:23
    * @params: [user]
    * @return: void
    **/
    public void sendMessageToUser(User user) {

        //检测该用户是否在线
        WebSocketSession webSocketSession = users.get(user.getEmail());
        if (webSocketSession != null && webSocketSession.isOpen()) {
            TextMessage message = new TextMessage("您有新的消息");
            try {
                webSocketSession.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
~~~



最后就是将这拦截器，webSocket handler注册注册到相应的WebConfig中

~~~
@Configuration
@EnableWebSocket
public class WebScoketConfig extends WebMvcConfigurerAdapter implements WebSocketConfigurer {

    @Autowired
    private MsgScoketHandle msgScoketHandle;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {

        registry.addHandler(msgScoketHandle, "/ws").addInterceptors(new WebSocketHandshakeInterceptor()).setAllowedOrigins("*");
        //注册这个是为了防止浏览器不支持websocket
        registry.addHandler(msgScoketHandle, "/ws/sockjs").addInterceptors(new WebSocketHandshakeInterceptor()).withSockJS();

    }
}
~~~

在对webconfig配置中，还需要注意继承WebMvcConfigurerAdapter，否则会出现的异常（如下）

~~~javascript
WebSocket connection to 'ws://192.168.11.32:8080/' failed: Error during WebSocket handshake: Unexpected response code: 403
~~~

> 未完待续[还需与UDP/TCP相结合学习，以及对更多场景的分析等]

>资料来源：
>
>http://www.ruanyifeng.com/blog/2017/05/websocket.html
>
>https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#mvc-web-security