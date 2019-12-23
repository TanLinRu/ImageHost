## Thread学习

### Thread是什么

### Thread生命周期

### Thread方法解析

### Thread-线程安全问题剖析

### 并发解决思路

* ##### 低级解决

* ##### 高级解决

### 线程池

### 并发的性能与安全如何权衡

### Thread疑惑

* ##### 按照惯例，任何抛出InterruptedException清除中断状态而退出的方法都会这样做。但是，总是有可能通过另外一个线程调用方法再次设置中断状态interrupt.

### 学习中的解答

* ##### interrupt

  > 源码如下：
  
  ```
  public void interrupt() {
          if (this != Thread.currentThread())
              checkAccess(); //线程可以自身中断自身（始终被允许），但如果被别的线程中断，需运行次								checkAccess() [throws SecurityException]
  
          synchronized (blockerLock) {
              Interruptible b = blocker;
              if (b != null) {
                  interrupt0();           // Just to set the interrupt flag
                  b.interrupt(this);
                  return;
              }
          }
          interrupt0();
  }
  ```
  
  
  
  ##### 如果该线程阻塞的调用Object.Class(wait(),wait(long),wait(long,int)) 或 Threa.Class(join(), join(long) ,join(long, int) ,sleep(long),sleep(long, int) ()) ，该线程的中断标志位会被清除，即（true -> false）,并接受到相对应这些方法抛出的InterruptedException异常（这也是为什么线程被中断时，并调用相对应的方法时，会出现中断状态清除）。除此之外，线程中断只是相对应的标志位修改而已，且中断不存在的线程不需要任何的效果。
  
  ##### 以下解析暂未考虑
  
  > ##### 