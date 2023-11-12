**项目相关**

> spring-cloud：[版本说明](https://github.com/alibaba/spring-cloud-alibaba/wiki/%E7%89%88%E6%9C%AC%E8%AF%B4%E6%98%8E)
>
> jdk：21
>
> maven：3.9.5
>
> spring boot：3.1.5
>
> spring cloud：2022.0.4
>
> spring cloud alibaba：2022.0.0.0
>
> redisson：3.23.4
>
> mysql：8.1.0
>
> mybatis plus：3.5.3.2
>
> dynamic datasource：4.1.3
>
> nacos：2.2.1

# 个人学习笔记

## 一、docker

*注意：为避免一些问题，关闭防火墙；或者放行对应的端口；*

```shell
### 直接关闭防火墙 ###
systemctl stop firewalld.service
systemctl disable firewalld.service
systemctl status firewalld.service

### 或者 放行对应端口 ###
# 查看当前已放行的端口
sudo firewall-cmd --zone=public --list-ports 
# 这里以3306为例，替换成你要放行的端口即可
sudo firewall-cmd --zone=public --add-port=3306/tcp --permanent 
# 关闭某个端口的放行(关闭时使用)
sudo firewall-cmd --zone=public --remove-port=3306/tcp --permanent
# 无论开启或关闭 最后都需要重新加载
sudo firewall-cmd --reload

### ifconfig命令不存在 ###
sudo yum install net-tools
```

---

### docker

安装：[官方安装](https://docs.docker.com/engine/install/centos/)、[基于阿里云安装](https://developer.aliyun.com/mirror/docker-ce?spm=a2c6h.13651102.0.0.57e31b112lRXie)

```shell
# step1：卸载历史版本
sudo yum remove docker \
                  docker-client \
                  docker-client-latest \
                  docker-common \
                  docker-latest \
                  docker-latest-logrotate \
                  docker-logrotate \
                  docker-engine
                  
# step2：安装必要工具
sudo yum install -y yum-utils device-mapper-persistent-data lvm2

# step3：设置镜像源
# 官方镜像源(有点慢)
sudo yum-config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo
# 清华大学
# sudo yum-config-manager --add-repo https://mirrors.tuna.tsinghua.edu.cn/docker-ce/linux/centos/docker-ce.repo
# 阿里云
# sudo yum-config-manager --add-repo https://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo

# step4：安装docker-ce
# 将 Docker 的官方源更改为阿里云源
# sudo sed -i 's+download.docker.com+mirrors.aliyun.com/docker-ce+' /etc/yum.repos.d/docker-ce.repo
# 重建yum缓存
sudo yum makecache fast
# 安装docker-ce
sudo yum -y install docker-ce

# step5: 开启docker服务
sudo systemctl start docker

# step6：设置开机自启
sudo systemctl enable docker
```

详情访问：[Docker：镜像加速器](https://developer.aliyun.com/article/886423)

```shell
sudo mkdir -p /etc/docker

sudo tee /etc/docker/daemon.json <<-'EOF'
{
  "registry-mirrors": ["https://registry.docker-cn.com","https://euvza1kr.mirror.aliyuncs.com"]
}
EOF

sudo systemctl daemon-reload

# 重启docker
sudo systemctl restart docker

# docker安装验证
sudo docker --version

# 查看版本
sudo docker version
```

---

### dockr-compose

github地址：[docker/compose](https://github.com/docker/compose/releases)

```shell
# step1：下载docker-compose(下载慢的话可以直接去github下载后再拉进来)
curl -L "https://github.com/docker/compose/releases/download/1.28.4/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose

# step2：授予权限(这里的docker-compose命名是你自己的命名)
chmod +x /usr/local/bin/docker-compose
```

---

### docker可视化

#### portainer

版本查看：[portainer](https://github.com/portainer/portainer/releases/tag/2.19.1)

```shell
docker run -d \
-p 8000:8000 \
-p 9000:9000 \
    --restart=always \
    --name portainer \
    -v /usr/local/docker/portainer/docker.sock:/var/run/docker.sock \
    -v /usr/local/docker/portainer/data:/data \
    portainer/portainer-ce:2.19.1
  
# 查看日志
docker logs -f portainer
```

访问地址：``http://host:9000``



---

#### panel

官网：[1Panel](https://1panel.cn/)

```shell
# 下载安装
curl -sSL https://resource.fit2cloud.com/1panel/package/quick_start.sh -o quick_start.sh && sh quick_start.sh
```

---

### docker容器

#### java

- 安装

```shell
# 1.可通过dnf 或 yum 安装
sudo yum install java-17-openjdk-devel

#############################

# 2.可通过手动下载和安装二进制文件
# 2.1 访问Oracle官方网站下载对应的二进制文件  解压缩并放到指定位置
tar -xvf jdk-17.tar.gz -C /usr/local/java/jdk-17
# 2.2 配置环境变化
vim /etc/profile

export JAVA_HOME=/usr/local/jdk-17
export CLASSPATH=$:CLASSPATH:$JAVA_HOME/lib/
export PATH=$PATH:$JAVA_HOME/bin
# 2.3 刷新配置
source /etc/profile
# 2.4 验证安装
java -version
```

---

#### mysql

```shell
docker run -d \
	-p 3306:3306 \	
	--name mysql \
	--restart always \
	-e MYSQL_ROOT_PASSWORD=root \
	-v /usr/local/docker/mysql/log:/var/log \
	-v /usr/local/docker/mysql/data:/var/data \
	-v /usr/local/docker/mysql/conf:/etc/conf \
	-v /etc/localtime:/etc/localtime:ro \
	mysql \
	--character-set-server=utf8mb4 \
	--collation-server=utf8mb4_0900_ai_ci

### 参数说明 ###
MYSQL_ROOT_PASSWORD：密码
character-set-server:字符集
collation-server：排序规则

### 扩展命令 ###
# 查看日志
docker logs -f mysql
# 备份sql(全部)
docker exec mysql sh -c 'exec mysqldump --all-databases -uroot "$MYSQL_ROOT_PASSWORD"' > /root/all-databases.sql
# 备份sql(指定)
docker exec mysql sh -c 'exec mysqldump --databases 库表 -uroot "$MYSQL_ROOT_PASSWORD"' > /root/all-databases.sql
# 备份sql(不要数据)
docker exec mysql sh -c 'exec mysqldump --no-data --databases 库表 -uroot "$MYSQL_ROOT_PASSWORD"' > /root/all-databases.sql
# 执行sql
docker exec -i mysql sh -c 'exec mysql -uroot -p"$MYSQL_ROOT_PASSWORD"' < /root/xxx.sql
```

---

#### redis

```shell
docker run -d \
	-p 6379:6379 \
	--restart always \
	--name redis \
	-v /usr/local/docker/redis/conf:/etc/redis/conf \
	-v /usr/local/docke/redis/data:/data \
	redis \
	--requirepass root \
	--appendonly yes
	
### 参数说明 ###
requirepass：密码
appendonly：持久化
```

---

#### nacos

- 单机非持久化

```shell
docker run -d \
--name nacos \
-p 8848:8848 \
-e MODE=standalone \
-e NACOS_AUTH_TOKEN=SecretKey012345678901234567890123456789012345678901234567890123456789 \
-e NACOS_AUTH_CACHE_ENABLE=enable \
-e NACOS_AUTH_IDENTITY_KEY=nacos \
-e NACOS_AUTH_IDENTITY_VALUE=nacos \
--privileged=true \
--restart=always \
nacos/nacos-server:v2.2.1
```

- 单机持久化

*前提：安装mysql并初始化nacos数据库*

```shell
docker run -d \
--name nacos \
-p 8848:8848 \
-p 9848:9848 \
-p 9849:9849 \
--privileged=true \
-e JVM_XMS=256m \
-e JVM_XMX=256m \
-e MODE=standalone \
-e NACOS_SERVERS=58.87.87.4 \
-e NACOS_SERVER_IP=58.87.87.4 \
-e SPRING_DATASOURCE_PLATFORM=mysql \
-e MYSQL_SERVICE_HOST=58.87.87.4 \
-e MYSQL_SERVICE_DB_NAME=nacos_config \
-e MYSQL_SERVICE_USER=root \
-e MYSQL_SERVICE_PASSWORD=root-hetongxue \
-e MYSQL_SERVICE_PORT=3306 \
-e MYSQL_DATABASE_NUM=1 \
-e NACOS_AUTH_CACHE_ENABLE=enable \
-e NACOS_AUTH_TOKEN=SecretKey012345678901234567890123456789012345678901234567890123456789 \
-e NACOS_AUTH_IDENTITY_KEY=nacos \
-e NACOS_AUTH_IDENTITY_VALUE=nacos \
--restart=always \
nacos/nacos-server:v2.2.1
```

- windows启动

*注：如果使用的2.2.0及之后的版本需要对其配置文件中的一下配置进行修改，否则无法启动；*

- 需要更改的配置

```properties
nacos.core.auth.enabled=true
nacos.core.auth.server.identity.key=xxx
nacos.core.auth.server.identity.value=xxx
nacos.core.auth.plugin.nacos.token.secret.key=jhg87hgfdt6rhgr56t4gshjhfu7fhu8fe7=
```

- 命令启动

```shell
# 来到nacos的bin目录下（非集群启动）
startup.cmd -m standalone
# 或者修改bin目录下的 startup.cmd 文件中的set MODE="cluster" 为 set MODE="standalone"
```

- 访问地址

[http://127.0.0.1:8848/nacos](http://127.0.0.1:8848/nacos)



---

### 存储相关

#### MinIO*[更新中...]*

官网：[MinIO | 高性能分布式存储](https://www.minio.org.cn/)

文档：[中文](https://docs.minio.org.cn/docs/)、[英文](https://docs.minio.io/docs/)

- 单机部署

```shell
docker run -d \
	-p 9000:9000 \
	-p 9001:9001 \
	--restart always \
	--name minio \
	--net host \
	--privileged=true \
	-e MINIO_ACCESS_KEY=admin \
	-e MINIO_SECRET_KEY=admin123 \
	-v /usr/local/docker/minio/data:/data \
	-v /usr/local/docker/minio/config:/root/.minio \
    minio/minio \
	server /data --console-address ":9001"
```

- compose部署

```shell
sudo mkdir /usr/local/docker/minio
sudo vi /usr/local/docker/minio/docker-compose.yml
```

```yacas
version: '3.7'
services:
  minio:
    container_name: minio
    restart: always
    privileged: true
    image: minio/minio
    ports:
      - '9000:9000'
      - '9001:9001'
    volumes:
      - ./data1:/data1
      - ./data2:/data2
      - ./data3:/data3
      - ./data4:/data4
    environment:
      - MINIO_ROOT_USER = admin
      - MINIO_ROOT_PASSWORD = admin
    command: server --console-address ':9001' http://minio/data{1...4}
    healthcheck:
      test: [ 'cmd','curl','-f','http://localhost:9000/minio/health/live' ]
      interval: 30s
      timeout: 20s
      retries: 3
```

---

#### GitLab*[更新中...]*

- 创建gitlab目录

```shell
# 创建gitlab目录
sudo mkdir /usr/local/docker/gitlab

# 编辑配置文件
sudo vi /usr/local/docker/gitlab/docker-compose.yml

# 拉取镜像
docker pull gitlab/gitlab-ce
```

---

### 自动化

#### jenkins

##### gitlab

- 安装gitlab

```shell
# 安装相关依赖
yum -y install policycoreutils openssh-server openssh-client postfix

# 启动ssh服务&&设置开机自启
sudo systemctl enable sshd && sudo systemctl start sshd && sudo systemctl status sshd

# 启动postfix服务&&设置开机自启
sudo systemctl enable postfix && systemctl start postfix && sudo systemctl status postfix

# 开发ssh&&http服务&&重启防火墙
firewall-cmd --add-service=ssh --permanent
firewall-cmd --add-service=http --permanent
firewall-cmd --reload

# 下载gitlab包&&安装
# 本地下载：https://tuna.moe/
wget https://mirrors.tuna.tsinghua.edu.cn/gitlab-ce/yum/el7/gitlab-ce-16.3.6-ce.0.el7.x86_64.rpm
rpm -ivh gitlab-ce-16.3.6-ce.0.el7.x86_64.rpm

# 修改gitlab配置
vi /etc/gitlab/gitlab.rb

external_url 'http://192.168.164.10:82'
nginx['listen_port'] = 82
gitlab_rails['initial_root_password'] = 'Gitlab@hy.com'

# 重新配置&&启动gitlab
gitlab-ctl reconfigure
gitlab-ctl restart

# 添加端口到防火墙
firewall-cmd --zone=public --add-port=82/tcp --permanent
firewall-cmd --reload

# 访问地址
http://192.168.164.10:82/

sudo gitlab-rails console -e production
irb> user = User.where(id: 1).first
irb> user.password = 'Gitlab@hy.com'
irb> user.password_confirmation = 'Gitlab@hy.com'
irb> user.save!
```



