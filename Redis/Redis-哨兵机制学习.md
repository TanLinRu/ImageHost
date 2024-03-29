### 哨兵机制

在传统的集群中，Redis更多是使用了主从复制的模式作为一个高可用方案的首选。主从复制不单单在Redis集群中可以看到，在Mysql、Mongo、或是MQ等一些为保证整体系统一个高可用方案首选。

但同时的，随着主从复制方案的演进，主从复制的模式也是有一定的缺点的：

* 在系统出现状况时，从节点的配置需要人工干预。
* 主从借点彼此不感知，所以在往后一些场景下，会导致数据的缺失。

于是这种情况下，Redis Sentinel（哨兵机制）便诞生了。



Redis Sentinel（哨兵机制）[最小配置为一主一从]主要功能：

* ##### 主节点存活检测

* ##### 主从运行情况检测

* ##### 自动故障转移

Redis Sentinel主要执行以下四个任务：

* ##### 监控：Sentinel会不断检查主服务器和从服务器是否正常运行

* ##### 通知：当被监控的某个Redis服务器出现问题，Sentinel会通过API脚本想管理员或者其他的应用程序发送通知

* ##### 自动故障转移：当主节点不能正常工作时，Sentinel会开始以此自动的故障转移操作，将从于失效主节点是主从关系的从节点中，基于投票选举的机制选出新的主节点，并将其余节点指向新的节点

* ##### 配置提供者：在Redis Sentinel模式下，客户端应用在初始化链接的是Sentienl节点集合，从中获取主节点的信息

在Redis Sentinel中是通过每秒一次的频率对Redis节点和其他的Sentinel节点发送PING命令【在官网的说明中还有其余的命令模式】进而根据节点回复判断节点是否在线。

在Sentinel中有两种下线的状态：

* ##### 主观下线：主观下线适用于所有主节点和从节点。如果down-after-milliseconds毫秒内，Sentinel没有收到目标节点的有效回复，则会判定该节点为主观下线。

* ##### 客观下线：客观下线只适用于主节点。如果主节点出现故障，Sentinel节点会通过Sentinel is-maser-down-by-addr命令，向其他Sentinel节点询问该节点的状态判断。如果超过<quorum>个数节点的判定主节点不可达，则该Sentinel节点判断主节点为客观下线。

`Sentinel` 与 `Redis` **主节点** 和 **从节点** 交互的命令，主要包括：

| 命令      | 作 用                                                        |
| --------- | ------------------------------------------------------------ |
| PING      | `Sentinel` 向 `Redis` 节点发送 `PING` 命令，检查节点的状态   |
| INFO      | `Sentinel` 向 `Redis` 节点发送 `INFO` 命令，获取它的 **从节点信息** |
| PUBLISH   | `Sentinel` 向其监控的 `Redis` 节点 `__sentinel__:hello` 这个 `channel` 发布 **自己的信息** 及 **主节点** 相关的配置 |
| SUBSCRIBE | `Sentinel` 通过订阅 `Redis` **主节点** 和 **从节点** 的 `__sentinel__:hello` 这个 `channnel`，获取正在监控相同服务的其他 `Sentinel` 节点 |

`Sentinel` 与 `Sentinel` 交互的命令，主要包括：

| 命令                            | 作 用                                                        |
| ------------------------------- | ------------------------------------------------------------ |
| PING                            | `Sentinel` 向其他 `Sentinel` 节点发送 `PING` 命令，检查节点的状态 |
| SENTINEL:is-master-down-by-addr | 和其他 `Sentinel` 协商 **主节点** 的状态，如果 **主节点** 处于 `SDOWN` 状态，则投票自动选出新的 **主节点** |

其实说白了，Sentinel机制的出现，通过了相关的命令，与选举机制，使得整个Redis主从复制架构变得更加可靠稳固。虽然在主从复制的架构下，redis的主从架构还是会受到一定的读性能压力，但后期还可以采取分布式的方案外加均衡负载的方案，使得整个Redis集群架构变得更为高可用、高并发。

> 资料来源：
>
> [redis](https://redis.io/documentation)
>
> https://www.cnblogs.com/bingshu/p/9776610.html

> 个人思考：
>
> 在整个选举机制，联想到了zookpper的选举机制。同时，根据分布式的链条，想起了CAP理论中的相关知识。在整个分布式的方案中，还得具体分析数据与性能的轻重，进而设计出最优的分布式架构。