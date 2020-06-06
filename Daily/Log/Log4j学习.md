### Log4j学习总结

* ##### log4j.properties配置解释

* ##### log4j小实战

* ##### 谈谈log4j、log4j2、logback



> log4j.properties配置解释

log4j有两种格式的配置文件（*.xml,*.properties）个人习惯所以就选了properties，其实没啥区别，只是格式上不同

Log4J配置文件的基本格式如下：

~~~java
#配置根Logger
log4j.rootLogger  =   [ level ]   ,  appenderName1 ,  appenderName2 ,  …

#配置日志信息输出目的地Appender
log4j.appender.appenderName  =  fully.qualified.name.of.appender.class 
log4j.appender.appenderName.option1  =  value1 
… 
log4j.appender.appenderName.optionN  =  valueN 

#配置日志信息的格式（布局）
log4j.appender.appenderName.layout  =  fully.qualified.name.of.layout.class 
log4j.appender.appenderName.layout.option1  =  value1 
… 
log4j.appender.appenderName.layout.optionN  =  valueN 
~~~

即：一个配置根Logger ，[(Appender+Layout)...]

其中 [level] 是日志输出级别，共有5级：

```
FATAL       0  记录影响系统正常运行，可能导致系统崩溃的事件
ERROR       3  记录影响业务流程正常进行，导致业务流程提前终止的事件
WARN        4  记录未预料到，可能导致业务流程无法进行的事件
INFO        6  记录系统启动/停止，模块加载/卸载之类事件
DEBUG       7  记录业务详细流程，用户鉴权/业务流程选择/数据存取事件
TRACE          记录系统进出消息，码流信息
```

Appender 为日志输出目的地，Log4j提供的appender有以下几种：

```
org.apache.log4j.ConsoleAppender（控制台），
org.apache.log4j.FileAppender（文件），
org.apache.log4j.DailyRollingFileAppender（每天产生一个日志文件），
org.apache.log4j.RollingFileAppender（文件大小到达指定尺寸的时候产生一个新的文件），
org.apache.log4j.WriterAppender（将日志信息以流格式发送到任意指定的地方）
```

Layout：日志输出格式，Log4j提供的layout有以下几种：

```
org.apache.log4j.HTMLLayout（以HTML表格形式布局），
org.apache.log4j.PatternLayout（可以灵活地指定布局模式），
org.apache.log4j.SimpleLayout（包含日志信息的级别和信息字符串），
org.apache.log4j.TTCCLayout（包含日志产生的时间、线程、类别等等信息）
```

然后格式的一个规范：

~~~
ConversionPattern 日志信息，符号所代表的含义：

 -X号: X信息输出时左对齐；
 %p: 输出日志信息优先级，即DEBUG，INFO，WARN，ERROR，FATAL,
 %d: 输出日志时间点的日期或时间，默认格式为ISO8601，也可以在其后指定格式，比如：%d{yyy MMM dd HH:mm:ss,SSS}，输出类似：2002年10月18日 22：10：28，921
 %r: 输出自应用启动到输出该log信息耗费的毫秒数
 %c: 输出日志信息所属的类目，通常就是所在类的全名
 %t: 输出产生该日志事件的线程名
 %l: 输出日志事件的发生位置，相当于%C.%M(%F:%L)的组合,包括类目名、发生的线程，以及在代码中的行数。举例：Testlog4.main (TestLog4.java:10)
 %x: 输出和当前线程相关联的NDC(嵌套诊断环境),尤其用到像java servlets这样的多客户多线程的应用中。
 %%: 输出一个"%"字符
 %F: 输出日志消息产生时所在的文件名称
 %L: 输出代码中的行号
 %m: 输出代码中指定的消息,产生的日志具体信息
 %n: 输出一个回车换行符，Windows平台为"\r\n"，Unix平台为"\n"输出日志信息换行
 可以在%与模式字符之间加上修饰符来控制其最小宽度、最大宽度、和文本的对齐方式。如：
 1)%20c：指定输出category的名称，最小的宽度是20，如果category的名称小于20的话，默认的情况下右对齐。
 2)%-20c:指定输出category的名称，最小的宽度是20，如果category的名称小于20的话，"-"号指定左对齐。
 3)%.30c:指定输出category的名称，最大的宽度是30，如果category的名称大于30的话，就会将左边多出的字符截掉，但小于30的话也不会有空格。
 4)%20.30c:如果category的名称小于20就补空格，并且右对齐，如果其名称长于30字符，就从左边较远输出的字符截掉。
~~~



> log4j小实战

##### 要求

* ##### 按照不同日志级别把相关日志输入到不同的日志文件

* ##### 日志文件滚动

~~~
log4j.rootLogger=DEBUG,Console,infoFile,warnFile,errorFile

#控制台日志
log4j.appender.Console=org.apache.log4j.ConsoleAppender
log4j.appender.Console.Target=System.out
log4j.appender.Console.layout=org.apache.log4j.PatternLayout
log4j.appender.Console.layout.ConversionPattern=%d %-5p --- [%16t] %C : %msg%n

#infoFile
log4j.appender.infoFile=org.apache.log4j.RollingFileAppender
log4j.appender.infoFile.Threshold=INFO
log4j.appender.infoFile.MaxFileSize=10MB
#infoFile输出配置
log4j.appender.infoFile.File=D:/project/redis/logs/info/info.log
log4j.appender.infoFile.layout=org.apache.log4j.PatternLayout
log4j.appender.infoFile.layout.ConversionPattern=%d %-5p --- [%16t] %C : %msg%n
#infoFile日志层级过滤
log4j.appender.infoFile.filter.F1=org.apache.log4j.varia.LevelRangeFilter
log4j.appender.infoFile.filter.F1.LevelMin=INFO
log4j.appender.infoFile.filter.F1.LevelMax=INFO

#warnFile
log4j.appender.warnFile=org.apache.log4j.RollingFileAppender
log4j.appender.warnFile.ImmediateFlush=true
log4j.appender.warnFile.MaxFileSize=10MB
#warnFile输出配置
log4j.appender.warnFile.File=D:/project/redis/logs/warn/warn.log
log4j.appender.warnFile.layout=org.apache.log4j.PatternLayout
log4j.appender.warnFile.layout.ConversionPattern=%d %-5p --- [%16t] %C : %msg%n
#warnFile日志层级过滤
log4j.appender.warnFile.filter.F1=org.apache.log4j.varia.LevelRangeFilter
log4j.appender.warnFile.filter.F1.LevelMin=WARN
log4j.appender.warnFile.filter.F1.LevelMax=WARN

#errorFile
log4j.appender.errorFile=org.apache.log4j.RollingFileAppender
log4j.appender.errorFile.ImmediateFlush=true
log4j.appender.errorFile.MaxFileSize=10MB
#errorFile输出配置
log4j.appender.errorFile.File=D:/project/redis/logs/error/error.log
log4j.appender.errorFile.layout=org.apache.log4j.PatternLayout
log4j.appender.errorFile.layout.ConversionPattern=%d %-5p --- [%16t] %C : %msg%n
#errorFile日志层级过滤
log4j.appender.errorFile.filter.F1=org.apache.log4j.varia.LevelRangeFilter
log4j.appender.errorFile.filter.F1.LevelMin=ERROR
log4j.appender.errorFile.filter.F1.LevelMax=ERROR
~~~

在上述的配置中定义了Console,infoFile,warnFile,errorFile不同的场景，DEBUG只是定义了输出的一个级别。在上述，除了Console，后面的三个都是输入到不同的日志文件中。为了能够区分不同级别的日志，所以使用了层级过滤器。当然网上也有很多版本，这是个人学的比较好的一个版本。

> 谈谈log4j、log4j2、logback

谈到日志框架 ，不少人看上面的，都只认识logback，毕竟spring boot默认的一个日志框架。也许有人会问，那slef又是啥？从网上找的一个资料，Slef属于一个日志框架的抽象，作为日志框架实现的规范，而自己不实现。

* log4j

这算是一个比较初期的一个Apache的一个日志框架了。支持xml/properties格式的配置文件，然后目前已经停止了维护，所以在官网上找的资料少之又少

* loaback

这是后续对logback的一个改良吧，个人也用的比较多。个人觉得在对于配置方面，还有可用性的方面的话，是比log4j与log4j2，更好点。当然这是个人的目前一个阅历。

* log4j2

在官网，log4j2的简介便是修复了logback一些缺陷。其次，在JDK1.7后提出的AIO（异步思想），log4j2也实现了异步的方法，使得性能更好。

三者的一个性能分析图：

![](https://github.com/TanLinRu/ImageHost/blob/raw/Daily/Images/log.png)

从上述的一个性能分析图可以看到，log4j2在同步及异步性能中会更出色。

~~~
个人见解：日志框架的选择，无疑也是整个系统的一个优化的地方，合理的一个优化，才能更好的构建一个性能完善的系统。
~~~

其次，个人在对于log4j2的配置中发现，其余log4j的配置区别不大，同样的一个案例log4j2配置如下：

~~~
<?xml version="1.0" encoding="UTF-8"?>
<!--Configuration后面的status，这个用于设置log4j2自身内部的信息输出，可以不设置，当设置成trace时，你会看到log4j2内部各种详细输出-->
<!--monitorInterval：Log4j能够自动检测修改配置 文件和重新配置本身，设置间隔秒数-->
<configuration monitorInterval="5">
    <!--日志级别以及优先级排序: OFF > FATAL > ERROR > WARN > INFO > DEBUG > TRACE > ALL -->

    <!--变量配置-->
    <Properties>
        <!-- 控制台输出格式 -->
        <property name="CONSOLE_PATTERN"
                  value="%style{%d}{bright,green} %highlight{%-5p} %-8T --- [%style{%16t}{bright,blue}]  %style{%C}{bright,yellow}: %msg%n%style{%throwable}{red}"/>
        <!--   日志文件输出格式     -->
        <property name="FILE_PATTERN" value="%date{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%ne"/>
        <!-- 定义日志存储的路径 -->
        <property name="FILE_PATH" value="D:/project/redis/logs"/>
    </Properties>

    <appenders>

        <console name="Console" target="SYSTEM_OUT">
            <!--输出日志的格式-->
            <PatternLayout
                    pattern="${CONSOLE_PATTERN}"
                    disableAnsi="false" noConsoleNoAnsi="false"/>
            <!--控制台只输出level及其以上级别的信息（onMatch），其他的直接拒绝（onMismatch）-->
            <ThresholdFilter level="debug" onMatch="ACCEPT" onMismatch="DENY"/>
        </console>

        <!-- 这个会打印出所有的info及以下级别的信息，每次大小超过size，则这size大小的日志会自动存入按年份-月份建立的文件夹下面并进行压缩，作为存档-->
        <RollingFile name="RollingFileInfo" fileName="${FILE_PATH}/info/info.log">
            <Filters>
                <!--将WARN及其以上级别的日志给DENY掉-->
                <ThresholdFilter level="WARN" onMatch="DENY" onMismatch="NEUTRAL"/>
                <!--控制台只输出level及以上级别的信息（onMatch），其他的直接拒绝（onMismatch）-->
                <ThresholdFilter level="info" onMatch="ACCEPT" onMismatch="DENY"/>
            </Filters>
            <PatternLayout pattern="${FILE_PATTERN}"/>
            <Policies>
                <!--interval属性用来指定多久滚动一次，默认是1 hour-->
                <TimeBasedTriggeringPolicy interval="1"/>
                <SizeBasedTriggeringPolicy size="10MB"/>
            </Policies>
            <!-- DefaultRolloverStrategy属性如不设置，则默认为最多同一文件夹下7个文件开始覆盖-->
            <DefaultRolloverStrategy max="15"/>
        </RollingFile>

        <!-- 这个会打印出所有的warn及以下级别的信息，每次大小超过size，则这size大小的日志会自动存入按年份-月份建立的文件夹下面并进行压缩，作为存档-->
        <RollingFile name="RollingFileWarn" fileName="${FILE_PATH}/warn/warn.log">
            <Filters>
                <!--将ERROR及其以上级别的日志给DENY掉-->
                <ThresholdFilter level="ERROR" onMatch="DENY" onMismatch="NEUTRAL"/>
                <!--控制台只输出level及以上级别的信息（onMatch），其他的直接拒绝（onMismatch）-->
                <ThresholdFilter level="WARN" onMatch="ACCEPT" onMismatch="DENY"/>
            </Filters>
            <PatternLayout pattern="${FILE_PATTERN}"/>
            <Policies>
                <!--interval属性用来指定多久滚动一次，默认是1 hour-->
                <TimeBasedTriggeringPolicy interval="1"/>
                <SizeBasedTriggeringPolicy size="10MB"/>
            </Policies>
            <!-- DefaultRolloverStrategy属性如不设置，则默认为最多同一文件夹下7个文件开始覆盖-->
            <DefaultRolloverStrategy max="15"/>
        </RollingFile>

        <!-- 这个会打印出所有的info及以下级别的信息，每次大小超过size，则这size大小的日志会自动存入按年份-月份建立的文件夹下面并进行压缩，作为存档-->
        <RollingFile name="RollingFileError" fileName="${FILE_PATH}/error/error.log">
            <!--控制台只输出level及以上级别的信息（onMatch），其他的直接拒绝（onMismatch）-->
            <ThresholdFilter level="ERROR" onMatch="ACCEPT" onMismatch="DENY"/>
            <PatternLayout pattern="${FILE_PATTERN}"/>
            <Policies>
                <!--interval属性用来指定多久滚动一次，默认是1 hour-->
                <TimeBasedTriggeringPolicy interval="1"/>
                <SizeBasedTriggeringPolicy size="10MB"/>
            </Policies>
            <!-- DefaultRolloverStrategy属性如不设置，则默认为最多同一文件夹下7个文件开始覆盖-->
            <DefaultRolloverStrategy max="15"/>
        </RollingFile>

    </appenders>

    <!--Logger节点用来单独指定日志的形式，比如要为指定包下的class指定不同的日志级别等。-->
    <!--然后定义loggers，只有定义了logger并引入的appender，appender才会生效-->
    <loggers>

        <root level="debug">
            <appender-ref ref="Console"/>
            <appender-ref ref="RollingFileInfo"/>
            <appender-ref ref="RollingFileWarn"/>
            <appender-ref ref="RollingFileError"/>
        </root>

    </loggers>

</configuration>
~~~

且log4j2在loggers的一个日志粒度会更细点。

其次，因为log4已经停止维护了，spring boot从1.3.X版本后支持了log4j2的配置，详情进spring.io进行查看。

> 资料来源：
>
> [log4j配置详解](https://my.oschina.net/xianggao/blog/515216)
>
> [log4j2官网](https://logging.apache.org/log4j/2.x/)