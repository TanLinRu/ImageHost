### redis-Sentinl搭建笔记

* ##### 搭建前提要求：

  * ##### 对redis的命令有一定的熟悉

  * ##### 对docker 网络的一个模式

* ##### 搭建环境:

  * ##### Centos 7.7 64位

  * ##### docker 19.03.8

  * ##### redis : hub.c.163.com/library/redis:latest

* ##### 断电配置

  | 服务名               | 端口  |
  | -------------------- | ----- |
  | redis-6380           | 6380  |
  | redis-6381           | 6381  |
  | redis-6382           | 6382  |
  | redis-sentinel-26380 | 26380 |
  | redis-sentinel-26381 | 26381 |
  | redis-sentinel-26382 | 26382 |

  

1、docker pull redis 镜像

~~~
docker pull hub.c.163.com/library/redis
~~~

2、docker 容器

~~~~
docker run --name redis-6380 --net=host -d hub.c.163.com/library/redis redis-server --port （6380）【此处更换上述的redis端口】
~~~~

3、搭建主从（6380端口为主机，其余为从机）

~~~
docker exec -it redis-6381[服务名] redis-cli -p 端口号
~~~

进入容器内部，在执行：

~~~
SLAVEOF ip地址 6380
~~~

然后再执行：

~~~
info
~~~

查看下列信息：

~~~
# Replication
role:slaves
connected_slaves:2
slave0:ip=112.74.100.192,port=6382,state=online,offset=36562971,lag=0
slave1:ip=112.74.100.192,port=6380,state=online,offset=36562971,lag=0
master_replid:f00f1d394fdbe3b0a76a855712d0e2d7f4235c75
master_replid2:388e14850c9e4f49991c2168a21956f3e5346ab3
master_repl_offset:36563117
second_repl_offset:58911
repl_backlog_active:1
repl_backlog_size:1048576
repl_backlog_first_byte_offset:35514542
repl_backlog_histlen:1048576
~~~

这里的role显示目前这个redis是否是master

然后重复上述步骤，把其余的从机进行配置

4、搭建redis-sentienl

现在/data/redis/sentinel,里面结构如下：

~~~
[root@iZwz9hcv43i3glmdkb46tiZ sentinel]# ls
redis-6380  redis-6381  redis-6382
[root@iZwz9hcv43i3glmdkb46tiZ sentinel]# pwd
/data/redis/sentinel
~~~

然后再这些文件夹下创建sentinel.conf

~~~
sentinel monitor mymaster（集群名） ip地址 6380 1（一个投票）
~~~

然后按上述的端口号及服务器，依次启动redis-sentinel

~~~
docker run --name redis-sentinel-26380 -v /data/redis/sentinel/redis-6380/sentinel.conf:/usr/local/etc/redis/sentinel.conf --net=host -d hub.c.163.com/library/redis redis-sentinel /usr/local/etc/redis/sentinel.conf --port （26380）【此处更换上述的redis-sentinel端口】
~~~

重复上述步骤，即可完成redis-sentinel搭建

通过命令：docker logs redis-sentinel-【端口号】

~~~
[root@iZwz9hcv43i3glmdkb46tiZ redis-6380]# docker logs redis-sentinel-26380
1:X 04 Jun 14:49:58.218 # oO0OoO0OoO0Oo Redis is starting oO0OoO0OoO0Oo
1:X 04 Jun 14:49:58.218 # Redis version=4.0.1, bits=64, commit=00000000, modified=0, pid=1, just started
1:X 04 Jun 14:49:58.218 # Configuration loaded
1:X 04 Jun 14:49:58.221 * Running mode=sentinel, port=26380.
1:X 04 Jun 14:49:58.221 # WARNING: The TCP backlog setting of 511 cannot be enforced because /proc/sys/net/core/somaxconn is set to the lower value of 128.
1:X 04 Jun 14:49:58.225 # Sentinel ID is b37cc4c0c0f6c34f110ccdfe3e8c00c744f53905
1:X 04 Jun 14:49:58.225 # +monitor master redis-master 112.74.100.192 6380 quorum 1
1:X 04 Jun 14:49:58.226 * +slave slave 112.74.100.192:6381 112.74.100.192 6381 @ redis-master 112.74.100.192 6380
1:X 04 Jun 14:49:58.231 * +slave slave 112.74.100.192:6382 112.74.100.192 6382 @ redis-master 112.74.100.192 6380
1:X 04 Jun 14:50:34.897 * +sentinel sentinel b5c753cf9383bc5f028b6c4e8459b2e23c835155 172.18.243.24 26381 @ redis-master 112.74.100.192 6380
1:X 04 Jun 14:50:48.677 * +sentinel sentinel 89dc9d355d00627c40412f3187febd6e73189578 172.18.243.24 26382 @ redis-master 112.74.100.192 6380
1:X 04 Jun 14:55:16.469 # +new-epoch 1
1:X 04 Jun 14:55:16.472 # +vote-for-leader 89dc9d355d00627c40412f3187febd6e73189578 1
1:X 04 Jun 14:55:16.474 # +sdown master redis-master 112.74.100.192 6380
1:X 04 Jun 14:55:16.474 # +odown master redis-master 112.74.100.192 6380 #quorum 1/1
1:X 04 Jun 14:55:16.474 # Next failover delay: I will not start a failover before Thu Jun  4 15:01:17 2020
1:X 04 Jun 14:55:17.442 # +config-update-from sentinel 89dc9d355d00627c40412f3187febd6e73189578 172.18.243.24 26382 @ redis-master 112.74.100.192 6380
1:X 04 Jun 14:55:17.442 # +switch-master redis-master 112.74.100.192 6380 112.74.100.192 6381
1:X 04 Jun 14:55:17.443 * +slave slave 112.74.100.192:6382 112.74.100.192 6382 @ redis-master 112.74.100.192 6381
1:X 04 Jun 14:55:17.443 * +slave slave 112.74.100.192:6380 112.74.100.192 6380 @ redis-master 112.74.100.192 6381
1:X 04 Jun 14:55:47.500 # +sdown slave 112.74.100.192:6380 112.74.100.192 6380 @ redis-master 112.74.100.192 6381
1:X 04 Jun 14:56:13.428 # -sdown slave 112.74.100.192:6380 112.74.100.192 6380 @ redis-master 112.74.100.192 6381
~~~

通过上述可以查看到整个集群的搭建的情况

可通过docker stop redis-【端口号】来模拟主机宕机，redis-sentinel自动切换

* spring boot如何使用哨兵即可使用

~~~
spring:
  redis:
    sentinel:
      master: redis-master
      nodes: 112.74.100.192:26380,112.74.100.192:26381,112.74.100.192:26382
~~~

> 个人思考：
>
> 1、哨兵机制的搭建还是属于比较麻烦的，如果放到实际上，会不会是K8S或是集群会好点？
>
> 2、docker搭建缓慢，后期换位docker-compose

