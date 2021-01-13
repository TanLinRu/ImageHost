## Mysql  - Lock wait timeout exceeded 异常处理

### 场景：在对一个数据进行逻辑删除的操作时，会返回相关的Lock wait timeout exceeded 异常

### 排查思路：

* 在第一时间上，先分析相关的表是不是有相关的锁操作了，借此排查（但实际上并不与其他业务操作有关）
* 根据异常语句定位相关位置，然后根据程序业务逻辑，发现是因为while() 循环删除，导致锁的占用（做好相关的业务逻辑处理即可解决相关问题）



## Lock wait timeout exceeded 

### 扩展点：

#### MySQL造成锁的情况比较多：

* 执行DML操作没有commit ，再执行删除操作就会锁表
* 在同一事务内先后对同一条数据进行插入和更新操作
* 表索引设计不当，导致数据库出现死锁
* 长事务，阻塞DDL，继而阻塞所有同表的后续操作

### 在mysql中有如下两种锁异常（目前个人了解到）

* Lock wait timeout exceeded ：后续提交的事务等待前面事务处理的事务释放锁，但是等待的时候超过了Mysql的锁等待时间，就会引发此异常
* Dead Lock：两个事务互相等待对方释放相同的资源锁，从而造成的死循环，就会引发这个异常

除此之外还需要注意一下两个变量

* innodb_lock_wait_timeout : innodb 的DML操作的行级锁等待的时间
* lock_wait_timeout: 数据结构DDL操作的锁的等待时间



### 从上述的问题与相关扩展点知识点来结合：

1. 首先查看 innnob_lock_wait_timeout 的具体指，从指来判断是不是当时设置小了导致（innodb_lock_wait_timeout 值得基本单位为s）[非问题根源]

2. 根据网上的操作使用查询目前的一个事务处理的情况

   ~~~mysql
   select * form infomation_schema.innodb_trx
   ~~~

   从上述的查询中只有一个数据，如图

   ![image-20210113112206577](https://raw.githubusercontent.com/TanLinRu/ImageHost/master/images/image-20210113112206577.png)

   从图可以看到，这个不是锁释放的问题，而是有个事务一直在跑，导致表资源被锁住，无法快速获取，导致Lock wait timeout 异常出现。

   ~~~
   网上相关文章会说明使用show processlit查看相关的一些执行信息，但在个人使用的时候，个人感觉对排查问题与解决问题来说，没有太多的一个定位的信息
   ~~~

   

   >补充

   information_shcema这张数据表保存了MySQL服务器所有的数据库信息。如数据库名、数据库表，表栏的数据类型与访问权限等。简而言之，在MySQL服务器上，有什么数据库，数据库中有什么表，每张表的字段类型是什么，各个数据库需要什么权限才能访问等等的信息都保存在infomation_schema库（表？待考证）中。

   对于本次的问题排查需要了解的三张表：

   * innodb_trx：当前运行的所有事务
   * innodb_locks：当前出现的锁
   * innodb_lock_waits: 锁等待的对应关系

3. 根据步骤二的相关排查流程，以及相关异常定位，最后排查程序的业务逻辑即可把问题处理完成

4. 最后根据步骤二的相关的信息，把相关的事务线程kill，即可完成所有的问题排查与结局

   

> 表字段说明

innodb_trx

![preview](https://raw.githubusercontent.com/TanLinRu/ImageHost/master/images/bVbcp4U)



innodb_locks

![clipboard.png](https://raw.githubusercontent.com/TanLinRu/ImageHost/master/images/bVbcp4H)



innodb_lock_waits

![clipboard.png](https://segmentfault.com/img/bVbcp4J?w=868&h=211)