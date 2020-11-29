## Mysql嵌套连接算法

* ### Nested-Loop join Algorithms

  ~~~
  for each row in t1 matching range {
  	for each row in t2 matching reference key {
  		for each row in t3 {
  			if row satisties join conditions ,send to client
  		}
  	}
  }
  ~~~

  

* ### Block Nested-Loop join Algorithm

  ~~~
  for each row in t1 matching range {
  	for each row in t2 matching reference key {
  		store used columns form t1,t2 in join buffer 
  		if buffer is full {
  			for each row in t3 {
  				for each t1,t2 combination in join buffer {
  					if row satisfies join conditions ,send to client
  				}
  			}
  			empty join buffer
  		}
  	}
  }
  
  if buffer is not empty {
  	for each row in t3 {
  		for each t1,t2 combination in join buffer {
  			if row satifies join conditions ,send to client
  		}
  	}
  }
  ~~~

  t3扫描次数

  ~~~
  （S * C）/ join_buffer_size + 1
  s: join buffer 中每个t1,t2组合存储的大小
  c: join buffer中combination的数目
  ~~~

  