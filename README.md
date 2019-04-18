



#wtshop
1.安装jdk（https://blog.csdn.net/github_38336924/article/details/82221258）配置yum安装的环境变量 https://www.cnblogs.com/benjamin77/p/8460030.html
1.查看安装列表yum search java | grep -i --color jdk
2.#或者如下命令，安装jdk1.8.0的所有文件 yum install -y java-1.8.0-openjdk* .
安装tomcat（http://www.cnblogs.com/liaolongjun/p/5638740.html）
安装git
1.git --version
2.yum install -y git
安装maven
sudo wget http://repos.fedorapeople.org/repos/dchen/apache-maven/epel-apache-maven.repo -O /etc/yum.repos.d/epel-apache-maven.repo
sudo sed -i s/\$releasever/6/g /etc/yum.repos.d/epel-apache-maven.repo
sudo yum install -y maven
mvn -version

3.安装redis
https://www.cnblogs.com/fanlinglong/p/6635828.html

mvn idea:idea

**远程代码测试服务器101.201.106.61部署：**

通过yum安装的tomcat
yum install tomcat
 yum install tomcat-admin-webapps

  修改80端口  需要 更改防火墙iptables -t nat -A PREROUTING -p tcp --dport 80 -j REDIRECT --to-port 8080
  通过  service tomcat start 启动
第一次：
mkdir learngit //创建文件夹
cd learngit
pwd   // 查看文件夹路径
/root/learngit   //显示的文件夹路径
git clone https://github.com/523570822/wtshop.git  //克隆远程仓库 ，改成自己的地址连接


cd wtshop //更新的目录 跟随git项目目录

git branch -a// 列出所有远程分支
  git remote -v   //查看远程主机名称及连接 origin
  //要关联一个远程库，使用命令git remote add gitHub https://github.com/523570822/wtshop.git； git remote add origin http://192.168.1.222:3000/root/wtshop.git

git checkout  -b rxmteacher  remotes/origin/teacher_rx  //远程分支   //切换到指定分支  rxmteacher(自己起的名字)


 在root目录执行
 cd wtshop   //进入项目目录
 
 git pull origin    --force ;//强制更新本地仓库
 
 
 mvn -X install -Dmaven.test.skip=true ; //通过maven自动编译代码
rsync -rtlvz   /root/learngit/wtshop/wtshop-web/target/wtshop-web/*  /var/lib/tomcat/webapps/news  ;   简化后 拷贝编译后的代码到指定tomcat服务器中sudo systemctl restart tomcat  启动Tomcat
 
 

以后执行：
cd /root/learngit/wtshop //进入项目目录
git pull origin   --force ;//强制更新本地仓库
rm -rf /root/learngit/wtshop/wtshop-web/target/*     //部署前先删除
rm -rf /mrmf/tomcat_shops/webapps/ROOT/*     //


mvn -X install -Dmaven.test.skip=true ; //通过maven自动编译代码


ps -ef |grep tomcat_shops |awk '{print $2}'|xargs kill -9
tar zcvf /mrmf/tomcat_shops/webapps/ROOT$(date +%Y%m%d%H).tar.gz /mrmf/tomcat_shops/webapps/ROOT ;//备份语句根据实际情况修改
 
rsync -rtlvz --exclude 'wtshop.properties' /root/learngit/wtshop/wtshop-web/target/wtshop-web/*  /mrmf/tomcat_shops/webapps/ROOT  ;   简化后 拷贝编译后的代码到指定tomcat服务器中sudo systemctl restart tomcat  启动Tomcat; 




 sudo /mrmf/tomcat_shops/bin/startup.sh  --启动
 sudo /mrmf/tomcat_shops/bin/shutdown.sh  --停止
 
 
tail -f /mrmf/tomcat_shops/logs/catalina.out  --查看日志

sudo systemctl restart tomcat

--去除远程仓文件

git rm --cached -r  "target"


 git commit -m "delete target"
 git pull gitHub master
 
  git pull  origin master
  
  
  server2 部署
  单独部署server2 需要在server1中执行 ./serverBS2_mr_feng.sh
  scp -r   root@10.25.92.80:/root/learngit/mr_feng/fengServer2.sh   /root/learngit/mr_feng    --拷贝.sh 文件
  chmod 755 /root/learngit/mr_feng/fengServer2.sh   --赋予执行权限
  
  scp -r   root@10.25.92.80:/root/learngit/mr_feng/wtshop.properties /root
  i@8fmhY7!m3FkwJ*
  
  




   
  测试服务器启动redis
  cd  /root/work/redis/src
  进入src目录下，执行./redis-server & (带上&是在后台启动)
                      ./redis-server ../redis_shop.conf
                      
   redis-server /root/word/redis/redis_shop.conf

  2 redis关闭
  
  进入src目录下，执行./redis-cli shutdown
  
  3 远程连接redis服务器
  
  ./redis-cli -h redis服务器IP -p 6379 (默认端口)
   
   
   textserver 启动mongodb
   cd /root/work/mongodb/bin
   
   ./mongod -f mongod.conf
 ./mongod

    
  //linux 常用命令
   ps -ef |grep tomcat  查看tomcat线程
   
   //查看tcp 端口使用情况
    netstat -ntpl
   
    ps -ef |grep java
   杀死java线程  pkill -9  java
   杀死线程kill -9 23119
   
   
   查找大于100M的文件
  find / -size +100M -exec ls -lh {} \;
  
  
  如果重启服务器server1需要启动
  cd /mrmf/apache-tomcat-8.0.30/bin 下的打
  ./startup.sh
  

 





