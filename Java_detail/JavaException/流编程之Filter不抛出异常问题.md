> ###  流编程之Filter不抛出异常问题解析



##### 需求：表中设置了唯一索引，为避免添加的数据出现索引重复，使用了流式编程进行数据筛选。



##### 问题：即使数据重复，也不会抛出异常

##### 

```
progressNodeList.stream().filter(
                (ProgressNode node) -> {
                    if (progressNode.getProgressNodeName() == node.getProgressNodeName()) {
                        throw new CommonException("ccff.error.progress.node.progressnodename.repeat");
                    } else if (progressNode.getProgressNodeCode() == node.getProgressNodeCode()) {
                        throw new CommonException("ccff.error.progress.node.progressnodecode.repeat");
                    } else if (progressNode.getSeqNum() == node.getSeqNum()) {
                        throw new CommonException("ccff.error.progress.node.seq.num.repeat");
                    }
                    return true;
                }
        );
```

##### 原因分析：

##### 在计算机 中，有两种对数据处理的方式

* ##### 惰性求值

* ##### 直接求值

##### 在Java中也使用了这种方式，在stream中map，reduce,filter操作都调用均返回了StatelessOp，这里可以验证文档里说的Stream提供的接口操作分为*intermediate/terminal*两种操作，intermediate操作调用后并不会执行传入的操作。 以至于程序惰性求值，并不会对相对应的数据进行异常抛出。



##### 修改如下：

```
progressNodeList.stream().filter(
                (ProgressNode node) -> {
                    if (progressNode.getProgressNodeName() == node.getProgressNodeName()) {
                        throw new CommonException("ccff.error.progress.node.progressnodename.repeat");
                    } else if (progressNode.getProgressNodeCode() == node.getProgressNodeCode()) {
                        throw new CommonException("ccff.error.progress.node.progressnodecode.repeat");
                    } else if (progressNode.getSeqNum() == node.getSeqNum()) {
                        throw new CommonException("ccff.error.progress.node.seq.num.repeat");
                    }
                    return true;
                }
        ).collect(Collectors.toList());
```

