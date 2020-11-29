## Go

* ### Go 项目工作空间

  * ### src 目录包含go的源文件（每个目录对应一个包）

  * ### pkg 目录包含包对象

  * ### bin 目录包含可执行命令

* ### Go 基本数据类型

  * bool

  * string

  * int int8 int16 int32 int64

  * uint uint8 uint16 uint32 uint 64 uinttptr

  * byte // uint8 的别名

  * rune //int32 的别名  表示一个Unicode码点

  * float32 float64

  * complex64 complex128

  * PS

    ~~~
    int uint  uintptr 在32位系统上通常为32位宽，在64位系统上则为64位宽
    除非有特殊的理由使用固定大小或无符号的整数类型
    不然整数值应使用int类型
    ~~~

    ~~~
    在相关变量没有进行初始化时，相关未初始化变量会被赋零值
    数值类型为0
    布尔类型为false
    字符串为“”（空字符串）
    ~~~

    ~~~
    类型转换中，与C不同的是，GO不同类型项之间的赋值需要显示转换
    有如下两种转换方式
    1：
    var i = 42
    var x float = float(i)
    var y complex64 = complex64(i)
    2:
    i := 42
    x := float(i)
    y := complex64(i)
    ~~~

     ~~~
    在声明变量时，使用（var i = 表达式 | := ）对变量进行赋值时，变量的类型由右值进行推导
     ~~~

  * 常量

    ~~~
    常量的声明与变量类似，只是多了const
    常量可以使数值、布尔类型、字符、字符串
    常量不能用 :=来进行声明
    格式 const i
    
    const支持块结构
    const ()
    ~~~

* Go逻辑处理

  * for

    * for循环的range形式可遍历切片或映射

      * 当使用for煦暖遍历切片时，每次迭代都会返回两个值，第一个值为当前元素下表，第二个值为该下标所对应元素的一份**副本**

      * 可使用__忽略某些赋值

        ~~~go
        for i,_  := range pow
        for _,v := range pow
        //即两者都出现时，想忽略其中一个值时即可使用_
        //但如果只需索引，以下形式即可
        for i := range pow
        ~~~

        

* Go数组

  * 类型[n]T 表示拥有n 个 T类型的值的数组（数组长度是其类型的一部分，因此数组不能改变大小）

    ~~~
    var a [10]int
    将变量a声明为拥有10个int类型的数组
    ~~~

  * 切片

    * 在Go中数组的大小是固定的，而切片则为数据元素提供动态大小，官方说明，切片比起数组在实际应用中更为常用。但对于切片而言，其动态大小也还是基于相关的数组，所以其动态大小也绝不会超过原始数组的大小。在官方文档中，切片并不存储任何数据，切片仅描述了底层数组中的一段。更改切片元素会修改其底层数组中相应的元素。同样的，别的切片也能看到相关共享的底层数组的变化。

    * 类型 [] T 表示一个元素类型为T的切片

    * 切片通过两个下标来界定，即一个上界和一个下界（默认值为0），二者以冒号分隔（这是一个半开区间，包括第一个元素，排除最后一个元素）

      ~~~
      a[low : hight]
      ~~~

    * 切片的语法类似没有长度的数组文法
    
      ~~~go
      var a = [3]int{1,2,3}
      //此处为切片语法
      var b = []int{1,2,3}
      ~~~
    
    * 例子
    
      ~~~go
      package main
      
      import "fmt"
      
      func main() {
      	q := []int{2, 3, 5, 7, 11, 13}
      	fmt.Println(q)
      
      	r := []bool{true, false, true, true, false, true}
      	fmt.Println(r)
      
      	s := []struct {
      		i int
      		b bool
      	}{
      		{2, true},
      		{3, false},
      		{5, true},
      		{7, true},
      		{11, false},
      		{13, true},
      	}
      	fmt.Println(s)
      }
      ~~~
    
    * 切片默认行为
    
      切片下界默认为0，上界则是该切片的长度
    
      ~~~
      var a [10]int
      //等同于
      a[0:10]
      a[:10]
      a[0:]
      a[:]
      ~~~
    
      ~~~go
      package main
      
      import "fmt"
      
      func main() {
      	s := []int{2, 3, 5, 7, 11, 13}
      
      	s = s[1:4]
      	fmt.Println(s)
      
      	s = s[:2]
      	fmt.Println(s)
      
      	s = s[1:]
      	fmt.Println(s)
      }
      ~~~
    
    * 切片拥有长度和容量
    
      * 长度：切片所包含的元素个数
      * 容量：从切片的第一个元素开始数，到其底层数组元素末尾的个数
      * 切片的长度可通过**len(s)**和**cap(s)**来获取
    
    * 切片的零值是nil(nil切片的长度和容量为0且没有底层数组)
    
    * make创建切片
    
      * make函数会分配一个元素为零值的数组，并返回一个引用了数组的切片
    
      * 切片可以使用内建函数make来创建，这也是创建动态数组的方式
    
        ~~~
        a := make([]int,5) //len(a) = 5 制定了数组的长度
        
        ~~~
    
    * 切片可以包含任何类型，甚至包括它的切片
    
    * 切片扩展
    
      * append
    
        ~~~go
        func append(s []T , vs ...T) []T
        ~~~
    
        当切片底层的数组过小，不足以容纳给定的值时，会分配更大的底层数组，返回的切片会指向这个新分配的数组

* Go结构体

  * Name：语法仅支持列出部分字段（字段名顺序无关）

* Go指针

  * 指针
    * *T 是指向T类型的指针，器零值为nil
    * & 操作符会生成一个指向器操作数的指针
    * *操作符标识指针指向底层的指 （即使用 * + 内存地址指针  获取 内存地址指针指向的值）
    * 当有一个指针变量时（p）,可通过(*p).X去访问变量，但可以不需要做此操作，GO允许进行间接引用，p.X即可访问相应的变量

* Go 接口

  * 定义：接口类型由一组方法签名定义的集合
  * 接口值可用作函数的参数或返回值
  * 在内部，接口值可以看做包含值和具体类型的元组 （value type）
  * 空接口被用来处理未知类型的值，空接口可保存任何类型的值（因为每个类型都至少实现了零个方法）
  * 常用的一些接口
    * Stringer是一个可以用字符串描述自己的类型。ps：fmt包还有很多包都通过此接口来打印值
    * error：常用于判断是否为nil来判断是否有异常，error为nil时表示成功，非nil时表示有相关异常

* Go 方法章节

  * 方法是一类带特殊的接收者参数的函数

    Go 没有类，但可以为结构体类型定义方法

    ~~~go
    package main
    
    import (
    	"fmt"
    	"math"
    )
    
    type Vertex struct {
    	X, Y float64
    }
    
    func (v *Vertex) Abs()  float64{
    	return math.Sqrt(v.X * v.X + v.Y * v.Y)
    }
    
    func main() {
    	v := Vertex{3,4}
    	fmt.Print(v.Abs())
    }
    ~~~

    也可以声明非结构体的类型声明的方法

    ~~~
    package main
    
    import (
    	"fmt"
    	"math"
    )
    
    type Vertex struct {
    	X, Y float64
    }
    
    type Integer int
    
    func (i Integer) getInteger()  int{
    	return int(i) * 10
    }
    
    func (v *Vertex) Abs()  float64{
    	return math.Sqrt(v.X * v.X + v.Y * v.Y)
    }
    
    func main() {
    	v := Vertex{3,4}
    	fmt.Print(v.Abs())
    
    	//非结构体
    	i := Integer(1)
    	fmt.Print(i.getInteger())
    }
    ~~~

    

  * 函数

    在go中需要区分一下，方法是指相关带**接收者**参数的函数，而函数是不携带任何**接收者**参数的，除此之外，两者在功能上没有任何区别

    ~~~go
    package main
    
    import (
    	"fmt"
    )
    
    func test(x,y int)  int{
    	return x + y;
    }
    
    func main()  {
    	fmt.Print(test(10,20))
    }
    ~~~

* 需要深入

  * 切片与数组的关系
  * 切片修改后，是新的，还是旧的【底层原理去解析】{本质就是一个，只是重新扩容了}

* 不足

  * 对于指针部分还是比较薄弱
  * 变量定义中 type的使用
  * 接口类型的变量可以保存任何实现了这些方法的值
  * 切片相关处理还是不熟练【做好笔记与编码】

* 相关笔记

  * 对于方法中的接收者选择值还是指针
    * 参考如下方面：
      * 如果当前方法有相关的接收者值修改的问题，则可以选择指针作为接收者
      * 可以避免每次调用方法时复制相关的值，对于更为大型的struct结构时，代码更为简介与省事
  
* 资料

  * [Go - 切片](https://blog.go-zh.org/go-slices-usage-and-internals)