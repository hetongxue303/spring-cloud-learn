## nacos

### 快速开始

- 安装并启动`nacos`

官网：[nacos](https://nacos.io/zh-cn/index.html)

- 配置`bootstrap.yml`

```yaml
spring:
  application:
    name: consumer-server
  profiles:
    active: dev
  cloud:
    nacos:
      username: nacos
      password: nacos
      discovery:
        server-addr: 127.0.0.1:8848
        namespace: b3cda453-fe2b-4cdb-a752-1320e66c2151
        group: spring-cloud-consumer
      config:
        server-addr: ${spring.cloud.nacos.discovery.server-addr}
        namespace: ${spring.cloud.nacos.discovery.namespace}
        group: ${spring.cloud.nacos.discovery.group}
        file-extension: yml
        refresh-enabled: true
```

### 相关配置

#### 1. 负载均衡

- 引入依赖

```xml
<!-- loadbalancer -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-loadbalancer</artifactId>
</dependency>
```

- 添加配置类

```java

@Configuration
public class BeanConfiguration {

    @Bean
    @LoadBalanced// 使用负载均衡
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
```

#### 2.临时实例 和 持久实例

```yaml
 # 临时实例：true(默认)  持久实例：false
 spring:
   cloud:
     nacos:
       discovery:
         ephemeral: true
```

**区别：**

- 临时实例

```text
临时实例则是每隔5秒上报心跳 如果15秒内未收到心跳则健康状态设置为false 超过30秒则删除实例：适合流量突增场景 服务弹性扩容
可自动注销
```

- 持久实例

```text
持久实例在服务挂掉的时候 服务还会存在 只是健康状态会变成false：运维可实时观察健康状态 便于后期扩容、警告（通过保护阈值）
```

#### 3. 保护阈值

*需要在nacos服务列表中进行设置*

保护阈值是0-1之间的浮点数，与集群中的健康实例占比相关，即当前服务健康实例数/当前服务总实例数，如果健康实例占比小于或等于设置的值，会触发保护阈值；

存在问题：如果有50个实例，其中有40个实例处于不健康状态，此时只能返回10个实例（触发了保护阈值情况下），如果请求流量较大，可能会直接击垮这10个服务实例，产生`雪崩效应`。

#### 4. 实例权重

权重取值范围在0--100之间，表示服务器的负载情况，在权重相同的情况下，
nacos默认使用`轮询算法`实现`负载均衡`，按照顺序依次轮询选择服务实例（即设置的值越大被访问的概率越大）；
在服务治理中，通过设置每个服务的权重，可以使请求更加合理，均衡的分配到每一个实例上；

*权重设置只是影响服务实例的访问概率，在高并发环境下可能会出现负载不均的情况*

- 配置权重

```yaml
# 1.在服务调用方（consumer）加入以下配置；
# 2.在nacos服务列表中设置相应的权重才能生效；
spring:
  cloud:
    loadbalancer:
      nacos:
        enabled: true
```

#### 5.命名空间 和 分组

*主要用于服务隔离*

命名空间用于区分不同的项目，默认为public；
分组用于区分某个项目中的某个服务，默认为DEFAULT_GROUP；

- 在nacos中创建按一个命名空间；
- 在模块中添加配置；

```yaml
spring:
  cloud:
    nacos:
      discovery:
        namespace: b3cda453-fe2b-4cdb-a752-1320e66c2151 # 注意：这里需要写ID
        group: spring-cloud-consumer
```

- 相同命名空间和相同组可访问；

#### 6. 集群部署

在使用nacos时，为了保证高可用，一般会进行集群部署，nacos规定在集群中节点要大于等于3个；
单机模式下数据保存下内嵌数据库中derby，不方便观察数据库存储情况；
如果集群中启用多个默认配置下的nacos节点，数据存储存在一致性问题，为了解决这个问题，
nacos采用了集中式存储方式来支持集群部署，目前只支持mysql；

**将存储更改为mysql：**

- 将nacos目录下的`conf/mysql-schema.sql`导入数据库；
- 修改nacos目录下的`conf/application.properties`文件下的如下配置：

```properties
spring.datasource.platform=mysql
spring.sql.init.platform=mysql
db.num=1
db.url.0=jdbc:mysql://127.0.0.1:3306/nacos_config?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true&useUnicode=true&useSSL=false&serverTimezone=UTC
db.user.0=root
db.password.0=root
```

- 重新启动nacos，日志显示`use external storage`即为成功

**集群部署：**

- 启用多个nacos

复制三份nacos服务，并修改每个nacos目录下的`conf/application.properties`中的port分别为8848，8858，8868；

- 在每个nacos/conf目录下的新增文件名为`cluster.conf`的文件，并添加如下配置：

*注意：如果在一个服务器上，端口间要存在距离，不能连续*

```text
192.168.8.4:8848
192.168.8.4:8858
192.168.8.4:8868
```

- 配置客户端（服务）

*server-addr设置集群即可，其余的不做改变，比如namespace*

```yaml
spring:
  cloud:
    nacos:
      discovery:
        server-addr: 127.0.0.1:8848,127.0.0.1:8858,127.0.0.1:8868
        # server-addr: 127.0.0.1:nginx地址 # 设置nginx时可以反向代理
```

- 启动

```shell
# 集群方式不需要-m standalone
# 在每个nacos/conf目录下执行
# 此时使用8848，8858，8868端口都能进入nacos
startup
```

#### 7. CAP模式

CAP是分布式系统中最基础的理论，即一个分布式系统最多只能同时满足一致性、可用性、分区容错性三项中的两种。

- 一致性

对于客户端灭磁读操作，要么督导最新数据，要么读取失败，`强调数据正确`，又可分为两种情况；
强数据一致性：每个节点数据保持完全一致；
最终数据一致性：在某个时间阈值内同步数据，最终保持一致性；

- 可用性

任何客户端都会返回响应数据，不会出现响应错误，一定会返回数据，不会返回错误，但不能保证数据最新，`强调服务不出错`；

- 分区容错性

分布式系统，网络通信不可靠，当任意消息丢失或延迟时，系统仍会提供服务，不会出现宕机，要求一直运行，`强调不宕机，不挂掉`；

*对于分布式系统而言，分区容错性是前提条件，此时只能在一致性 和 可用性中二选一*