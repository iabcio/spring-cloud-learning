##Eureka 学习

#### 一、Eureka是什么？

1.1 概念

一个基于REST的服务注册与发现的AP系统

1.2 角色

Spring -> Spring Cloud -> Spring Cloud Netflix -> Eureka

`Spring Cloud Netflix OSS components`

```
Eureka : 服务发现, 
Hystrix : 断路器, 
Zuul : 服务路由, 
Ribbon : 负载均衡,
Archaius : 配置管理, 
etc
```

1.3 Eureka源码

```
1. 是纯正的 servlet 应用，需构建成war包部署 
2. 使用了 Jersey 框架实现自身的 RESTful HTTP接口 
3. peer之间的同步与服务的注册全部通过 HTTP 协议实现 
4. 定时任务(发送心跳、定时清理过期服务、节点同步等)通过 JDK 自带的 Timer 实现 
5. 内存缓存使用Google的guava包实现
```

**Netflix Eureka源码** : https://github.com/Netflix/eureka

**Spring Cloud Netflix适配Eureka源码** : https://github.com/spring-cloud/spring-cloud-netflix/tree/master

-------

#### 二、Eureka使用示例:


**官方示例工程Eureka Server** : https://github.com/spring-cloud-samples/eureka

**官方示例工程Eureka Clients** : https://github.com/spring-cloud-samples/customers-stores

**学习示例工程** : https://github.com/iabcdata/spring-cloud-learning

**`学习示例工程模块说明`**：

```
spring-parent:父POM

eureka-registry:eureka注册中心服务端单机版

eureka-registry-cloud:eureka注册中心服务端集群版

user-service:服务提供者,注册中心为集群

demo-service:服务提供者,注册中心为单机

demo-consumer:服务消费者,通过LoadBalancerClient获取服务地址后进行消费

demo-consumer-feign:服务消费者,通过feign客户端进行消费

demo-consumer-ribbon:服务消费者,通过RestTemplate进行轮询消费

```


##### 2.1  配置


2.1.1 单机配置application.yml

```
server:
  port: 1024
spring:
  application:
    name: eureka-registry
    
eureka:
  server:
    waitTimeInMsWhenSyncEmpty: 0
  client:
    serviceUrl:
      defaultZone: http://eureka.iabc.io:1024/eureka/
    register-with-eureka: false
    fetch-registry: true 
    healthcheck:
          enabled: true
  instance:
    hostname: eureka.iabc.io  
    
logging:
  level:
    com:
      netflix:
        eureka: off
        discovery: off 

```

***`单机版管理后台`*** : 

http://eureka.iabc.io:1024


2.1.2 单机配置application.properties

```
server.port=1024
spring.application.name=eureka-registry
    
eureka.server.waitTimeInMsWhenSyncEmpty=0
eureka.client.serviceUrl.defaultZone=http://eureka.iabc.io:1024/eureka/
eureka.client.register-with-eureka=false
eureka.client.fetch-registry=true 
eureka.client.healthcheck.enabled=true
eureka.instance.hostname=eureka.iabc.io  
    
logging.level.com.netflix.eureka=off
logging.level.com.netflix.discovery=off 

```


2.2.1 集群配置application.yml

```
spring:
  application:
    name: eureka-registry-cloud
    
logging:
  level:
    com:
      netflix:
        eureka: off
        discovery: off 
    
---
spring:
  profiles: eurekahost1
server:
  port: 1111
eureka:
  instance:
    hostname: eurekahost1  
  server:
    waitTimeInMsWhenSyncEmpty: 0
  client:
    serviceUrl:
      defaultZone: http://eurekahost2:1112/eureka/,http://eurekahost3:1113/eureka/
    register-with-eureka: false
    fetch-registry: true 
    healthcheck:
          enabled: true
       
---   
spring:
  profiles: eurekahost2
server:
  port: 1112
eureka:
  instance:
    hostname: eurekahost2  
  server:
    waitTimeInMsWhenSyncEmpty: 0
  client:
    serviceUrl:
      defaultZone: http://eurekahost1:1111/eureka/,http://eurekahost3:1113/eureka/
    register-with-eureka: false
    fetch-registry: true 
    healthcheck:
          enabled: true
      
---    
spring:
  profiles: eurekahost3
server:
  port: 1113
eureka:
  instance:
    hostname: eurekahost3  
  server:
    waitTimeInMsWhenSyncEmpty: 0
  client:
    serviceUrl:
      defaultZone: http://eurekahost1:1111/eureka/,http://eurekahost2:1112/eureka/
    register-with-eureka: false
    fetch-registry: true 
    healthcheck:
          enabled: true
   
```

2.2.2 host绑定

```
127.0.0.1 eurekahost1
127.0.0.1 eurekahost2
127.0.0.1 eurekahost3
```

2.2.3 启动脚本

启动实例1:

```
java -jar eureka-registry-cloud-1.0.0-SNAPSHOT.jar --spring.profiles.active=eurekahost1
```

启动实例2:

```
java -jar eureka-registry-cloud-1.0.0-SNAPSHOT.jar --spring.profiles.active=eurekahost2
```

启动实例3:

```
java -jar eureka-registry-cloud-1.0.0-SNAPSHOT.jar --spring.profiles.active=eurekahost3
```

***`集群版管理后台`*** : 

http://eurekahost1:1111

http://eurekahost2:1112

http://eurekahost3:1113

-------


#### 三、与同类产品比较

***Consul vs Zookeeper vs Etcd vs Eureka服务发现比较*** : 

https://luyiisme.github.io/2017/04/22/spring-cloud-service-discovery-products/

#### 四、一些注意问题

4.1 几处缓存

服务启动后最长可能需要2分钟时间才能被其它服务感知到
原因: 三处缓存 + 一处延迟
说明: Eureka对HTTP响应做了30s缓存
Eureka Client对已经获取的注册信息做了30s缓存
负载均衡组件Ribbon也有30s缓存
非Spring Cloud环境下应用启动后延迟30s发送心跳和注册信息


4.2 注册信息不会二次传播
Eureka Server集群配置时，只有配置为peer的节点才会同步注册信息.假设部署A、B、C三个ureka Server实例，A设置B为其Peer，B设置C为其Peer，那么注册到A上面的服务信息，在B上能被感知，但是B不会再将A上的服务信息向C传递。如果需要向C传递，那么需要在A上同时设置B、C为其Peer才行。


4.3 多网卡环境下的IP选择问题
如果服务部署的机器上安装了多块网卡，它们分别对应IP地址A, B, C，此时： 
Eureka会选择IP合法(标准ipv4地址)、索引值最小(eth0, eth1中eth0优先)且不在忽略列表中(可在application.properites中配置忽略哪些网卡)的网卡地址作为服务IP。 
这个坑的详细分析见：http://blog.csdn.net/neosmith/article/details/53126924


4.4 不剔除已关停的节点
当其注册表里服务因为网络或其他原因出现故障而关停时，Eureka不会剔除服务注册，而是等待其修复。

4.5 不可靠的健康检查
通过心跳来感知服务是否可用，需要依赖spring-boot-starter-actuator，自定义实现org.springframework.boot.actuate.health.HealthIndicator来完善。






