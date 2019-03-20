echo "stop tomcat!"
 service tomcat stop
sleep 10
echo "删除文件及缓存"
sudo rm -rf /root/learngit/wtshop/wtshop-web/target/*
sudo rm -rf /mrmf/tomcat_shops/webapps/ROOT/*
cd /root/learngit/wtshop
echo "拉取git上代码"
git pull origin   --force
sleep 10
echo "通过maven自动编译代码"
mvn -X install -Dmaven.test.skip=true

#打包
#mvn install -Dmaven.test.skip=true -U -P dev
echo "拷贝到Tomcat下"
rsync -rtlvz --exclude 'wtshop.properties' /root/learngit/wtshop/wtshop-web/target/wtshop-web/*    /var/lib/tomcat/webapps  ;   简化后 拷贝编译后的代码到指定tomcat服务器中sudo systemctl restart tomcat  启动Tomcat;
rsync -rtlvz /mrmf/tomcat_shops/wtshop-web/WEB-INF/classes/wtshop.properties /var/lib/tomcat/webapps/WEB-INF/classes

echo "开启tomcat"
sudo /mrmf/tomcat_shops/bin/startup.sh
#tail -f /mrmf/tomcat_shops/logs/catalina.out