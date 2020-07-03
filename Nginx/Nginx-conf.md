### Nginx-conf 配置文件详解

* ##### Nginx配置四大部分

  * ##### main（全局配置，此部分设置的相关指令影响其他所有部分的设置）

  * ##### server（主机配置，主要用户指定虚拟主机域名、IP、端口号）

  * ##### upstream（上游服务器设置，主要为反向代理、负载均衡相关配置）

  * ##### location（URL匹配位置后的设置，用于匹配网页位置）

  * ##### 继承关系：server 、http继承 main，location 继承 server，server继承http

> ##### Nignx.conf配置文件

~~~
#定于Nginx运行的用户和用户组
#user  nobody;

#nginx进程数，通常设置成和CPU的数量相等
worker_processes  1;

#全局错误日志定义类型，[debug|info|notice|warn|error|crit|alert|emerg]
#error_log  logs/error.log;
#error_log  logs/error.log  notice;
#error_log  logs/error.log  info;

#进程ID存储文件
#pid        logs/nginx.pid;


events {

 	#设置网路连接序列化，防止惊群现象发生，默认为on
 	accept_mutex on;  
 	
 	#设置一个进程是否同时接受多个网络连接，默认为off
 	multi_accept on;  
 	
 	#事件驱动模型，select|poll|kqueue|epoll|resig|/dev/poll|eventport
 	#use epoll;  
 	
    #最大连接数，默认为512
    worker_connections  1024;
    
}


http {

	#文件扩展名与文件类型映射表
    include       mime.types;
    
    #默认文件类型，默认为text/plain
    default_type  application/octet-stream;

    #log_format  main  '$remote_addr - $remote_user [$time_local] "$request" '
    #                  '$status $body_bytes_sent "$http_referer" '
    #                  '"$http_user_agent" "$http_x_forwarded_for"';
	
	#服务日志格式 log_format main
    #access_log  logs/access.log  main;

	#允许sendfile方式传输文件，默认为off，可以在http块，server块，location块。
    sendfile        on;
    
    #启用或禁用TCP_NOPUSHFreeBSD上的TCP_CORKsocket选项或Linux 上的socket选项。仅在使用sendfile时启用这些选项
    #tcp_nopush     on;

	#保持活动的客户端连接将在服务器端保持打开状态
    #keepalive_timeout  0;
    keepalive_timeout  65;

    server {
    
    	#设置IP的地址和端口，或服务器将在其上接受请求的UNIX域套接字的路径
        listen       80;
        
        #设置虚拟服务器的名称
        server_name  localhost;

		
        #charset koi8-r;

	    #服务日志格式 log_format main
        #access_log  logs/host.access.log  main;

        location / {
            root   html;
            index  index.html index.htm;
        }

		#定义将针对指定错误显示的URI
        #error_page  404              /404.html;

        # redirect server error pages to the static page /50x.html
        #
        error_page   500 502 503 504  /50x.html;
        location = /50x.html {
            root   html;
        }

        # proxy the PHP scripts to Apache listening on 127.0.0.1:80
        #
        #location ~ \.php$ {
        #    proxy_pass   http://127.0.0.1;
        #}

        # pass the PHP scripts to FastCGI server listening on 127.0.0.1:9000
        #
        #location ~ \.php$ {
        #    root           html;
        #    fastcgi_pass   127.0.0.1:9000;
        #    fastcgi_index  index.php;
        #    fastcgi_param  SCRIPT_FILENAME  /scripts$fastcgi_script_name;
        #    include        fastcgi_params;
        #}

        # deny access to .htaccess files, if Apache's document root
        # concurs with nginx's one
        #
        #location ~ /\.ht {
        #    deny  all;
        #}
    }


    # another virtual host using mix of IP-, name-, and port-based configuration
    #
    #server {
    #    listen       8000;
    #    listen       somename:8080;
    #    server_name  somename  alias  another.alias;

    #    location / {
    #        root   html;
    #        index  index.html index.htm;
    #    }
    #}


    # HTTPS server
    #
    #server {
    #    listen       443 ssl;
    #    server_name  localhost;

    #    ssl_certificate      cert.pem;
    #    ssl_certificate_key  cert.key;

    #    ssl_session_cache    shared:SSL:1m;
    #    ssl_session_timeout  5m;

    #    ssl_ciphers  HIGH:!aNULL:!MD5;
    #    ssl_prefer_server_ciphers  on;

    #    location / {
    #        root   html;
    #        index  index.html index.htm;
    #    }
    #}

}

~~~

> PS:
>
> 上述有一些配置目前在官网文档中暂未找到相关的属性介绍，所以暂不建议使用。同时，虽然开启了相关的属性，但是并未发现报错。

> ##### 资料参考：
>
> [Nginx官网](https://nginx.org/en/docs/http/ngx_http_core_module.html?&_ga=2.191470508.789053351.1593659596-2077933294.1593659596#error_page)