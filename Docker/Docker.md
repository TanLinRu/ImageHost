### Docker学习笔记 -- 入门篇

* ##### Docker简介

* ##### Docker简单入门

* ##### Docker使用场景


> Docker简介

在实际上Docker是一种虚拟化的容器技术。可能怎么说，会比较的难懂。但与VMare等虚拟机相比较结合的话，Docker实际上就是一种基于操作系统虚拟化的虚拟机容器。那么为什么Docker会慢慢取代了VMware这类硬件级别的虚拟机呢？

先来看看容器化的虚拟化与基于硬件级别的虚拟化两者的性能差异比较：

|   特性   |        容器        |    虚拟机    |
| :------: | :----------------: | :----------: |
| 启动速度 |        秒级        |    分钟级    |
|   性能   |      接近原生      |     较弱     |
| 内存代价 |        很小        |     较多     |
| 硬盘使用 |      一般为MB      |   一般为GB   |
| 运行密度 | 单机支持上千个容器 | 一般为几十个 |
|  隔离性  |      安全隔离      |   安全隔离   |
|  迁移性  |        优秀        |     一般     |

Docker容器化与虚拟机整体架构图：

![](https://github.com/TanLinRu/ImageHost/raw/master/Docker/images/docker虚拟化结构图.png)]()

可以从两者整体的架构图可以看出，在传统虚拟机中，整体虚拟系统的承载还需要相应的镜像支持，进而导致整体程序进程所需的资源消耗较大，且加载速度慢。在后期的环境转移与等等的运维问题上，与Docker相比差异较大。

在传统虚拟机中，ISO镜像成为虚拟机提供系统的唯一来源。而在Docker中，镜像成为了代替ISO镜像。其次，在传统的ISO镜像是单一的，不可重用的。而在Docker中镜像可谓是一生万物。这个思想比较像Java中类与对象（实例）的思想。

![](https://raw.githubusercontent.com/TanLinRu/ImageHost/raw/Docker/images/Docker架构图.png)

在Docker中三件客便是：镜像、容器、仓库。

怎么去理解呢？个人理解是这样的，镜像是作为容器生成的来源（即类与实例的关系）。那么仓库便是整个镜像的存储地方了。

> Docker简单实战

学习Docker必然是知行合一，才可真正掌握。

环境：

* 阿里云 Centos 7.7 64位
* Docker version 19.03.8

模拟场景搭建Mysql：

mysql下载的话，可以去https://c.163.com/hub#/library/repository/info?repoId=2955这里搜索一下，此处不多讲。此处的意思是启动一个名为Mysql的容器，-d后台运行，-p映射端口到宿主机器，后面的命令就是初始化密码。

~~~shell
docker run --name mysql -d -p 3306:3306 -e MYSQL_ROOT_PASSWORD=my-secret-pw mysql:tag
~~~

因为已经下载好了，所以使用docker images查看本地仓库中的镜像

~~~shell
[root@iZwz9hcv43i3glmdkb46tiZ ~]# docker images
REPOSITORY                     TAG                 IMAGE ID            CREATED             SIZE
jpress                         latest              5ee5baf53815        5 days ago          364MB
hello-world                    latest              bf756fb1ae65        4 months ago        13.3kB
hub.c.163.com/library/tomcat   latest              72d2be374029        2 years ago         292MB
hub.c.163.com/library/nginx    latest              46102226f2fd        3 years ago         109MB
hub.c.163.com/library/mysql    latest              9e64176cd8a2        3 years ago         407MB
~~~

启动后可用docker ps 查看相关情况：

~~~shell
[root@iZwz9hcv43i3glmdkb46tiZ ~]# docker ps
CONTAINER ID        IMAGE                         COMMAND                  CREATED             STATUS              PORTS                    NAMES
c414e6f41ae3        hub.c.163.com/library/mysql   "docker-entrypoint.s…"   5 days ago          Up 5 days           0.0.0.0:3306->3306/tcp   mysql
e6bf9d022461        jpress                        "catalina.sh run"        5 days ago          Up 5 days           0.0.0.0:8888->8080/tcp   interesting_kare
~~~

这边是大概的实战，至于参数等可以查询docker官网。其次的话，说一下个人在学习的过程中遇到的问题吧。在对mysql进行相关配置文件时，不知道如何挂载相关文件到哪里，这是可以看回刚刚的镜像仓库去找到相应的目录及文件进行挂载即可。

>  Docker使用场景

在往前的时候，虚拟机成为了每个程序猿部署测试环境与代码测试的必备工具。虽然我还是个小白，但也曾经历过这种时刻，代码在win系统运行的可以呀，部署上去了，哎问题出来了。其次，学生党嘛，电脑内存资源还是稀少，特别的Eclipse时代。这令我想起了，在当初写Swing时，开着虚拟机跟编译器，一天崩个十几次，也是常见的。

那么在这些场景下，Docker的优势便体现了出来。方便的代码部署，统一的测试环境。占去少量的资源便可以启动一个虚拟机。其次，ISO再也不是占用内存的唯一障碍。在Docker中，镜像是一种层次结构的组件，重复的层，不需重复下载，这也省了好多内存资源。

其次，Docker的好处还有其隔离性。Docker的船舱分离的思想，使得即使服务器中某个docker容器内存泄漏或是崩溃额不会影响到整个服务器的运行，而这一点虚拟机是远不可及的。（PS：在Spring Cloud中的Hystrix熔断器也使用了这个思想（记得有，还需再考证））。

除此之外，Docker+K8S这个成熟的运维方案也成为了目前云原生时代的云容器的技术方案。

> 资料来源：
>
> 《Docker技术入门与实践》第三版
>
> [docker中文社区](https://www.docker.org.cn/)
>
> [镜像仓库](https://c.163.com/hub#/library/repository/info?repoId=2955)

> 个人规划： dockerFile > docker compose(dockerFile升级版一次性启动多个容器)。除了对单机docker的学习，还需要上升到集群或是分布式的层面会更好点。