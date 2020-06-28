### Collection 

* ##### collection简介

* ##### collection相关实现

  * ##### 非线程安全（java.util）

  * ##### 线程安全（java.util.concurrent）

> ##### collection简介

##### 在java官网中，collection是用于表示和操作集合的统一体系结构。

##### 在所有的集合框架都包含如下的内容：

* ##### 接口

* ##### 实现

* ##### 算法

  ##### 在Java中，算法是对实现集合接口对象执行有用的方法（搜索，排序）。且这些算法是多态的。（即不同的集合实现，其算法实现也是不同的）



##### 在实际中，collection的实现类有set,list,queue,deque,而我们平常使用的map并不算是真正的collection。而区分是否是真正collection得看是否是单列数据还是映射（即key-value这样的数据结构）

##### (ps: 因为collection集合和Map都是涉及java对象的操作，所以此处对于相关源码的阅读还需要泛型的支持。在collection集合或者Map集合是不涉及基础数据类型的存储的)

##### java中集合的操作有三种遍历的方式

* ##### stream流式编程

~~~java
list.stream().forEach(
    i -> {
       System.out.println(i);
    }
);
~~~



* ##### for-each

~~~java
for (Integer i : list) {
    System.out.println(i);
}
~~~



* ##### Iterator(使用这个，相关collection类需实现`Iterator`接口)

~~~java
List<Integer> list = new ArrayList<>();
list.add(1);
list.add(2);
list.add(3);
list.add(4);

for (Iterator iter = list.iterator(); iter.hasNext();) {
	System.out.println(iter.next());
}
~~~

> ##### colletion相关实现

##### 在java.util包中，collection接口定义了其子类中相关的通用操作，属于集合实现类的根类。（个人思考：此处设计更多像抽象工厂模式，通过规定相关的接口函数规范，进而更好的进行实现类的多态性。）

##### java.util下有这些集合类：

- [**Set**](https://docs.oracle.com/javase/8/docs/api/java/util/Set.html) - 熟悉的集合抽象。不允许重复的元素。
- [**List**](https://docs.oracle.com/javase/8/docs/api/java/util/List.html) - 排序的集合，也称为*序列*。通常允许重复。
- [**Queue**](https://docs.oracle.com/javase/8/docs/api/java/util/Queue.html) - 设计用于在处理之前保存元素的集合。除了基本的“`收集”`操作外，队列还提供其他插入，提取和检查操作。
- [**Deque**](https://docs.oracle.com/javase/8/docs/api/java/util/Deque.html) - *双端队列*，在两端支撑元件插入和移除。扩展`队列`接口。
- [**Map**](https://docs.oracle.com/javase/8/docs/api/java/util/Map.html) - 从键到值的映射。每个键可以映射到一个值。即常说的键值对（key-value）
- [**SortedSet**](https://docs.oracle.com/javase/8/docs/api/java/util/SortedSet.html) - 一个集合，其元素按照其*自然顺序*（请参阅[` Comparable`](https://docs.oracle.com/javase/8/docs/api/java/lang/Comparable.html) 接口）或创建` SortedSet`实例 时提供的[` Comparator`](https://docs.oracle.com/javase/8/docs/api/java/util/Comparator.html)对象自动排序 。
- [**SortedMap**](https://docs.oracle.com/javase/8/docs/api/java/util/SortedMap.html) - 一种映射，其映射使用键的*自然顺序*或创建` SortedMap`实例时提供的比较器自动按键对映射进行排序。
- [**NavigableSet**](https://docs.oracle.com/javase/8/docs/api/java/util/NavigableSet.html) -一个` SortedSet的`扩展了导航方法报告对于给定的搜索目标最接近的匹配。` NavigableSet` 可以被访问并以升序或降序顺序遍历。
- [**NavigableMap**](https://docs.oracle.com/javase/8/docs/api/java/util/NavigableMap.html) - 一个` SortedMap的`扩展了导航方法返回给定搜索目标最接近的匹配。 `NavigableMap的`可被访问并且以升序或降序键顺序遍历。
- 具体可通过IDEA生成类继承图以及API进行学习查找

##### 在JDK1.5后，java提出了java.util.concurrent并发包，很好的解决了JDK1.2collection遗漏下来的并发操作的问题。在此package下，都是java对于一些并发情况下处理的工具类，线程、同步器等等的一些。本次只对并发集合类进行说明。

##### 除了队列，这个包提供的集合实现在多线程环境中设计用于： [`ConcurrentHashMap`](http://www.matools.com/file/manual/jdk_api_1.8_google/java/util/concurrent/ConcurrentHashMap.html) ， [`ConcurrentSkipListMap`](http://www.matools.com/file/manual/jdk_api_1.8_google/java/util/concurrent/ConcurrentSkipListMap.html) ， [`ConcurrentSkipListSet`](http://www.matools.com/file/manual/jdk_api_1.8_google/java/util/concurrent/ConcurrentSkipListSet.html) ， [`CopyOnWriteArrayList`](http://www.matools.com/file/manual/jdk_api_1.8_google/java/util/concurrent/CopyOnWriteArrayList.html)和[`CopyOnWriteArraySet`](http://www.matools.com/file/manual/jdk_api_1.8_google/java/util/concurrent/CopyOnWriteArraySet.html) 。 当预期许多线程访问给定的集合时， `ConcurrentHashMap`通常优于同步的`HashMap` ，并且`ConcurrentSkipListMap`通常优于同步的`TreeMap` 。 甲`CopyOnWriteArrayList`优选同步`ArrayList`时的预期数量的读取和遍历的数量大大超过更新的数量的列表。

##### 与此包中某些类使用的“并发”前缀是一个简写，表示与类似“同步”类的几个差异。 例如`java.util.Hashtable`和`Collections.synchronizedMap(new HashMap())`被同步。 但[`ConcurrentHashMap`](http://www.matools.com/file/manual/jdk_api_1.8_google/java/util/concurrent/ConcurrentHashMap.html)是“并发”。 并发集合是线程安全的，但不受单个排除锁的约束。 在ConcurrentHashMap的特定情况下，它可以安全地允许任意数量的并发读取以及可调整数量的并发写入。 当您需要通过单个锁来阻止对集合的所有访问时，“同步”类可能会有用，而不损害可扩展性。 在预期多个线程访问公共集合的其他情况下，“并发”版本通常是可取的。 并且当两个集合都是非共享的时候，不同步的集合是可取的，或者只有在持有其他锁时才可访问。

##### 大多数并发收集实现（包括大多数队列）也与通常的`java.util`约定不同，因为它们的[Iterators](http://www.matools.com/file/manual/jdk_api_1.8_google/java/util/Iterator.html)和[Spliterators](http://www.matools.com/file/manual/jdk_api_1.8_google/java/util/Spliterator.html)提供*弱一致*而不是快速失败遍历：

- ##### 可能会同时进行其他行动

- ##### 永远不会throw ConcurrentModificationException`](http://www.matools.com/file/manual/jdk_api_1.8_google/java/util/ConcurrentModificationException.html)

- ##### 它们被保证能够在建筑物上存在一次元素，并且可能（但不能保证）反映施工后的任何修改。

(ps:通过对相关的concurrent相关的集合类进行源码阅读：1、synchronized 2、无锁算法 并发安全大概可用上述方案进行)

> 资料来源：
>
> java官网



> 个人思考：
>
> 在juc包下的相关的并发类中，对于很对属性的修饰，比起java.util下的collection，juc包中更多都是私有化。这种设计思想，较为符合逃逸分析中设计线程安全问题的理论。其次，在对于使用同步块、锁机制、无锁算法去实现并发时，觉得并发能力的提升还得根据场景，大概率减少上下文的切换才可行（协程）。



