#wtshop

mvn idea:idea


**远程代码222服务器部署：**

第一次：
mkdir learngit //创建文件夹
cd learngit
pwd   // 查看文件夹路径
/root/learngit   //显示的文件夹路径
git clone https://github.com/523570822/wtshop.git  //克隆远程仓库 ，改成自己的地址连接


cd /wtshop //更新的目录 跟随git项目目录

git branch -a// 列出所有远程分支
  git remote -v   //查看远程主机名称及连接 origin

git checkout  -b rxmshop  remotes/origin/master//远程分支   //切换到指定分支  rxm_login(自己起的名字)


 在root目录执行
 cd rxm  //进入项目目录
 
 git pull origin   --force ;//强制更新本地仓库
 mvn -X install -Dmaven.test.skip=true ; //通过maven自动编译代码
 tar zcvf /opt/tomcat/webapps/wtshop$(date +%Y%m%d%H).tar.gz /opt/tomcat/webapps/wtshop ;//备份语句根据实际情况修改
 
 rsync -rtlvz  --exclude 'business-config.xml' --exclude 'datasource-config.xml'  --exclude '*.jsp'   --exclude '*.htm*' --exclude 'upload/*'  --exclude 'jsp/*'  --exclude 'thymeleaf/*'  --exclude "resources/*" --exclude 'log/*' /home/chengqiandai/chengqiandaiProjects/chengqiandaiweb/target/chengqiandaiweb/*  /home/production/chengqiandaiweb/ ;
rsync -rtlvz  /root/learngit/wtshop/target/wtshop-web/*  /opt/tomcat/webapps/wtshop  ;   简化后 拷贝编译后的代码到指定tomcat服务器中sudo systemctl restart tomcat  启动Tomcat
 
 

以后执行：
cd /root/learngit/rxm    //进入项目目录
git pull origin rxm_login  --force ;//强制更新本地仓库
rm -rf /root/learngit/wtshop/target/*     //部署前先删除
rm -rf /opt/tomcat/webapps/mrmf/*     //

mvn -X install -Dmaven.test.skip=true ; //通过maven自动编译代码
rsync -rtlvz  /root/learngit/wtshop/wtshop-web/target/wtshop-web/*  /opt/tomcat/webapps/wtshop ;   简化后 拷贝编译后的代码到指定tomcat服务器中


//sudo /opt/tomcat/bin/startup.sh --启动
//sudo /opt/tomcat/bin/shutdown.sh --停止

tail -f /opt/tomcat/logs/catalina.out  --查看日志

sudo systemctl restart tomcat

--去除远程仓文件




**远程代码测试服务器101.201.106.61部署：**

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

git checkout  -b rxmshop  remotes/origin/master//远程分支   //切换到指定分支  rxm_login(自己起的名字)


 在root目录执行
 cd wtshop   //进入项目目录
 
 git pull origin rxmshop  --force ;//强制更新本地仓库
 mvn -X install -Dmaven.test.skip=true ; //通过maven自动编译代码
 tar zcvf /mrmf/tomcat_shops/webapps/ROOT$(date +%Y%m%d%H).tar.gz /mrmf/tomcat_shops/webapps/ROOT ;//备份语句根据实际情况修改
 
 
rsync -rtlvz --exclude 'wtshop.properties'  /root/learngit/wtshop/target/wtshop-web/*  /mrmf/tomcat_shops/webapps/ROOT  ;   简化后 拷贝编译后的代码到指定tomcat服务器中sudo systemctl restart tomcat  启动Tomcat
 
 

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
  
  

 





