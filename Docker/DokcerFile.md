### Dockerfile学习总结

* ##### Dcokerfile简介

  * ##### DockerFile使用

* ##### Dockerfile组成部分

  * ##### 基础镜像信息

  * ##### 维护者信息

  * ##### 镜像操作指令

  * ##### 容器启动时执行指令

* ##### Dcokerfile与Docker-compose



> ##### DockerFile简介

##### Dockerfile是一个文本文档，其中包含用户可以在命令行上调用以组装映像所有命令。

##### 那么为什么不直接docker pull下载镜像，docker run进行配置，不就可以启动一个docker容器了吗？为什么还需要Dockerfile?

##### 举个爪子：在使用Dokcer搭建Nginx的时候，如果需要配置一些静态资源进入Nginx的容器时，在从docker镜像拉取下来的时候，是没办法将本机资源复制到镜像中取得。那么如果静态资源无法复制进到Nginx容器中，那么也就没办法访问得到相关的资源了。所以为了能访问到Nginx容器中的资源，就得使用Dockerfile进行对Nginx镜像进行二次开发。



##### 对于Dockerfile的用法，可通过下述命令进行查询：

~~~
docker build --help
~~~

~~~
[root@iZwz9hcv43i3glmdkb46tiZ ~]# docker build --help

Usage:  docker build [OPTIONS] PATH | URL | -（使用这个参数，暂未知道会执行什么，只是一直阻塞）

Build an image from a Dockerfile

Options:
      --add-host list           Add a custom host-to-IP mapping (host:ip)（添加自定义的主机名到 IP 映射）
      --build-arg list          Set build-time variables（添加创建时的变量）
      --cache-from strings      Images to consider as cache sources（使用指定镜像作为缓存源）
      --cgroup-parent string    Optional parent cgroup for the container（继承的上层cgroup）
      --compress                Compress the build context using gzip（使用gizp来压缩创建上下文数据）
      --cpu-period int          Limit the CPU CFS (Completely Fair Scheduler) period（分配CFS调度器时长）
      --cpu-quota int           Limit the CPU CFS (Completely Fair Scheduler) quota（CFS调度器总份额）
  -c, --cpu-shares int          CPU shares (relative weight)（CPU权重）
      --cpuset-cpus string      CPUs in which to allow execution (0-3, 0,1)（多个CPU允许使用CPU）
      --cpuset-mems string      MEMs in which to allow execution (0-3, 0,1)（多个CPU允许使用的内存）
      --disable-content-trust   Skip image verification (default true)（不进行镜像校验，默认为true）
  -f, --file string             Name of the Dockerfile (Default is 'PATH/Dockerfile')（Dockerfile名称）
      --force-rm                Always remove intermediate containers（总是删除中间过程的容器）
      --iidfile string          Write the image ID to the file（将镜像ID写入文件）
      --isolation string        Container isolation technology（容器的隔离机制）
      --label list              Set metadata for an image（配置镜像的元数据）
  -m, --memory bytes            Memory limit（限制使用内存量）
      --memory-swap bytes       Swap limit equal to memory plus swap: '-1' to enable unlimited swap（限制内存和缓存的总量）
      --network string          Set the networking mode for the RUN instructions during build (default "default")（指定RUN命令时的网络模式）
      --no-cache                Do not use cache when building the image（创建镜像时不使用缓存）
      --pull                    Always attempt to pull a newer version of the image（总是尝试获取镜像的最新版本）
  -q, --quiet                   Suppress the build output and print image ID on success（不打印创建工程总日志信息）
      --rm                      Remove intermediate containers after a successful build (default true)（创建成功后自动删除中间过程容器，默认true）
      --security-opt strings    Security options（指定安全相关的选项）
      --shm-size bytes          Size of /dev/shm（/dev/shm的大小）
  -t, --tag list                Name and optionally a tag in the 'name:tag' format（指定镜像的标签列表）
      --target string           Set the target build stage to build.（指定创建的目标阶段）
      --ulimit ulimit           Ulimit options (default [])（指定ulimit的配置）
~~~

> ##### 镜像指令

##### 在Dockerfile中属性名约定大写，与参数空格间开。[后续补充：Dockerfile指令编译流程]

##### 配置指令：

| 指令        | 说明                               |
| ----------- | ---------------------------------- |
| ARG         | 定义创建镜像过程中使用的变量       |
| FROM        | 指定所创建镜像的基础镜像           |
| LABEL       | 为生成的镜像添加元数据标签信息     |
| EXPOSE      | 声明镜像内服务监听的端口           |
| ENV         | 指定环境变量                       |
| ENTRYPOINT  | 指定镜像的默认入口命令             |
| VOLUME      | 创建一个数据卷挂载点               |
| USER        | 指定运行容器时的用户名或UID        |
| WORKDIR     | 配置工作目录                       |
| ONBUILD     | 创建子镜像时指定自动执行的操作指令 |
| STOPSINGAL  | 指定退出的信号值                   |
| HEALTHCHECK | 配置所启动容器如何进行健康检查     |
| SHELL       | 指定默认shell类型                  |

##### 操作指令：

| 指令                   | 说明                       |
| ---------------------- | -------------------------- |
| RUN                    | 运行指定命令               |
| CMD                    | 启动容器时指定默认执行命令 |
| COPY（Docker官方推荐） | 复制内容到镜像             |
| ADD                    | 添加内容到镜像             |

> 命令误区

1、在官方文档中说明: 尽管ADD与COPY在功能上有相似之处，但一般来说还是COPY为最优选择。COPY命令比ADD命令更为透明。COPY仅支持将本地文件复制到容器中，而ADD具有一些功能（例如：仅本地tar提取和远程URL支持）并不立即显而易见。所以在将本地tar文件提取到容器中时ADD为更优选择。

> 资料参考：
>
> 《Docker技术入门与实践》 第三版
>
> [Docker](https://docs.docker.com/engine/reference/builder/#usage)