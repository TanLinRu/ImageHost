### Mysql慢查询与Explain

* ##### 定位慢查询途径

* ##### Explain结果字段分析



> 定位慢查询途径

* ##### 通过慢日志定位执行效率较低的SQL语句，用--log0slow-queries[=file name]。选用此选项启动时，mysqld写一个包含所有执行时间超过long_query_time秒的SQL语句的日志文件。

* ##### 可使用show processlist命令查看当前Mysql在进行的线程，其包括线程的状态、是否锁表，可以实时查看SQL执行情况，同时对一些锁表操作进行优化。【ps:慢查询日志在查询结束后才记录，使用这个可在应用层面去分析问题】

> Explain结果字段分析

ID列：

- 表示执行SELECT语句的顺序
- ID相同时，执行顺序由上至下
- ID越大优先级越高，越优先被执行

SELECT_TYPE列：

- SIMPLE：不包含子查询或是UNION操作的查询
- PRIMARY：查询中包含任何子查询，那么最外层的查询则被标记为PRIMARY
- SUBQUERY：SELECT列表中的子查询
- DEPENDENT SUBQUERY：依赖外部结果的子查询
- UNION：UNION操作的第二个或是之后的查询的值为UNION
- DEPENDENT UNION：当UNION作为子查询时，第二或是第二个后的查询的SELECT_TYPE值
- UNION RESULT：UNION产生的结果集
- DERIVED：出现在FROM子句中的子查询

TABLE列：

- 输出数据行所在的表的名称
- <unionM,N>由ID为M,N查询union产生的结果集
- <derivedN>或<subqueryN>由ID为N的查询产生的结果
- 用途：查看数据来源

PARTITIONS列

- 对于分区表，显示查询的分区ID
- 对于非分区表，显示为NULL
- 用途：用于检查出低效率的跨分区扫描

TYPE列

- system：这是const联接类型的一个特例，当查询的表只有一行时使用
- const：表中有且只有一个匹配的行时使用，如对主键或是唯一索引的查询，效率最高的联接方式
- eq_ref： 唯一索引或主键查找，对于每个索引键，表中**只有一条**记录与之匹配
- ref：非唯一索引查找，返回匹配某个单独值的**所有行**。
- ref_or_null：类似于ref类型的查询，但是附加了对NULL值列的查询
- index_merge：该联接类型表示使用了索引合并优化方法。
- range：索引范围扫描，常见于between、>、<这样的查询条件
- index：全索引撒秒，同ALL的区别是，遍历的是索引数
- ALL：全表扫描，效率最差的连接方式

EXTRA列

- distinct：优化distinct操作，在找到第一匹配的元祖后即停止找同样值的动作
- not exists：使用Not Exists来优化查询
- using filesort：使用额外操作进行排序，通常会出现在order by或group by查询中
- using index：使用了覆盖索引进行查询
- using temporary：MySQL需要使用临时表来处理查询，常见于排序，子查询，和分组查询
- using where：需要在MySQL服务器层使用WHERE条件来过滤数据
- select tables optimized away：直接通过索引来获取数据，不用访问表（效率最高）

POSSIBLE_KEYS列

- 指出MySQL能使用哪些索引来优化查询
- 查询列所涉及到的列上的索引都会被列出，但不一定会被使用

KEY列

- 查询优化器优化查询实际所使用的索引
- 如果没有可用的索引，则显示为NULL
- 如查询使用了覆盖索引，则该索引仅出现在Key列中

KEY_LEN列

- 表示索引字段的最大可能长度
- 长度由字段定义计算而来，并非数据的实际长度

REF列

- 表示哪些列或常量被用于查找索引列上的值

ROWS列

- 表示MySQL通过索引统计信息，估算的所需读取的行数
- ROWS值的大小是个统计抽样结果，并不十分准确

FILTERED列

- 表示返回结果的行数占需读取行数的百分比
- FILTERED列的值越大越好
- 依赖于统计信息

> 资料来源：
>
> 《深入浅出Mysql》
>
> 《高性能Myqsl》
>
> https://www.jianshu.com/p/4644d7243185

> 个人思考：
>
> 查询慢，总的原因还是因为整个数据结构的问题，即索引问题。索引的使用与失效，严重影响了整个SQL语句的查询，也正慢查询中可以得出索引的有效利用极大的提升了查询的效率。其次，在看了许多文章，调优的方向为：硬件 -》 SQL语句 -》分区 -》 分步分表（TCC分布式事务）-》分布式。
>
> 反思: 通过对整体的慢查询的学习，发现自对于索引方面的知识还是比较薄弱，后续需要继续加强。