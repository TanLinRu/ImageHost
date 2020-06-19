### Spring-AOP基础学习

* ##### AOP相关概念

* ##### AOP业务场景

* ##### 代码实战

> ##### AOP相关概念

##### AOP在spring 中有两种选择，一是选择JDK默认实现的动态代理（基于接口实现，效率会差点，自由度没那么高），还有一种是GCLIB实现的动态代理（基于字节码实现）。

* ##### Aspect：涉及多个类别的关注点的模块化（参考业务场景：spring 事务）。在Spring AOP中，方面是通过使用常规类（基于模式的方法）或通过注释进行@Aspect注释的常规类 （@AspectJ样式）来实现的【切面类】

* ##### Join point：在程序执行过程中的一点，例如方法的执行或异常的处理。在Spring AOP中，连接点始终代表方法的执行。【对什么类或方法进作为切入点】

* ##### Advice：方面在特定的连接点处采取的操作。不同类型的建议包括“周围”，“之前”和“之后”建议。（建议类型将在后面讨论。）包括Spring在内的许多AOP框架都将建议建模为拦截器，并在连接点周围维护一系列拦截器。【根据切入点进行相应的处理】

  * #####  **@Before**（在连接点之前运行的建议，但是没办法阻止执行流程继续到连接点）

  * ##### **@AfterReturning**（在连接点正常完成后要运行的建议（例如，如果方法返回而没有引发异常））

  * ##### **@AfterThrowing**（如果方法因抛出异常而退出，则执行建议）

  * ##### **@After**（无论连接点退出的方式如何（正常或特殊返回），均应执行建议）

  * ##### **@Around**（围绕连接点的建议，例如方法调用。这是==最有力的建议==。周围建议可以在方法调用之前和之后执行自定义行为。它还负责选择是返回连接点还是通过返回其自身的返回值或引发异常来捷径建议的方法执行。）

* ##### Pointcut：与连接点匹配的谓词。建议与切入点表达式关联，并在与该切入点匹配的任何连接点处运行（例如，执行具有特定名称的方法）。切入点表达式匹配的连接点的概念是AOP的核心，默认情况下，Spring使用AspectJ切入点表达语言。【与连接点意思相关】

  * ##### @Pointcut

    Spring AOP支持以下在切入点表达式中使用的AspectJ切入点指示符（PCD）：

    - @execution：用于匹配方法执行的连接点。这是使用Spring AOP时要使用的主要切入点指示符。

    - @within：将匹配限制为某些类型内的连接点（使用Spring AOP时，在匹配类型内声明的方法的执行）。

    - @this：将匹配限制为连接点（使用Spring AOP时方法的执行），其中bean引用（Spring AOP代理）是给定类型的实例。

    - @target：在目标对象（代理的应用程序对象）是给定类型的实例的情况下，将匹配限制为连接点（使用Spring AOP时方法的执行）。

    - @args：在参数是给定类型的实例的情况下，将匹配限制为连接点（使用Spring AOP时方法的执行）。

    - @target：在执行对象的类具有给定类型的注释的情况下，将匹配限制为连接点（使用Spring AOP时方法的执行）。

    - @args：限制匹配的连接点（使用Spring AOP时方法的执行），其中传递的实际参数的运行时类型具有给定类型的注释。

    - @within：将匹配限制为具有给定注释的类型内的连接点（使用Spring AOP时，使用给定注释的类型中声明的方法的执行）。

    - @annotation：将匹配限制在连接点的主题（Spring AOP中正在执行的方法）具有给定注释的连接点上。

    - ==注意（下述的特性，只限定在aspect框架，现在Spring aop暂不支持）==：

      完整的AspectJ切入点语言支持未在Spring支持额外的切入点指示符：`call`，`get`，`set`，`preinitialization`， `staticinitialization`，`initialization`，`handler`，`adviceexecution`，`withincode`，`cflow`， `cflowbelow`，`if`，`@this`，和`@withincode`。在Spring AOP解释的切入点表达式中使用这些切入点指示符会导致`IllegalArgumentException`抛出异常。

    - [详细例子](https://docs.spring.io/spring-framework/docs/current/spring-framework-reference/core.html#aop-ataspectj)

    

> AOP业务场景

##### AOP日志，事务处理等业务场景。实际上，对于切面，可以怎么理解。在传统的MVC层面，可以使用AOP将架构层面切开三层，这便是AOP切面编程。

> ##### 代码实现

业务场景: 使用AOP进行对接口完成的日志记录

~~~
package com.tlq.redis.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * @Description: 接口响应日志AOP
 * @Author: TanLinquan
 * @Date: 2020/6/5 9:51
 * @Version: V1.0
 **/
@Slf4j
@Aspect
@Component
public class OptimizeLogAspect {

    @Pointcut("@annotation(com.tlq.redis.annotaion.OptimizeLog)")
    public void logPointCut() {

    }

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {

        //开始时间
        long start = System.currentTimeMillis();
        //执行方法
        Object result = point.proceed();
        long end = System.currentTimeMillis();
        MethodSignature signature = (MethodSignature) point.getSignature();

        String methodName = signature.getName();
        log.info("接口名："+methodName+",执行时间："+(end-start)+"ms");
        return result;

    }

}

~~~

> 参考资料：
>
> https://docs.spring.io/spring-framework/docs/current/spring-framework-reference/core.html#aop-introduction-proxies

