echo "stop tomcat!"
 service tomcat stop
echo "删除文件及缓存"
sudo rm -rf /root/learngit/wtshop/wtshop-web/target/*
sudo rm -rf /var/lib/tomcat/webapps/teacher/*
cd /root/learngit/wtshop
echo "拉取git上代码"
git pull origin   --force
sleep 10
echo "通过maven自动编译代码"
mvn -X install -Dmaven.test.skip=true
sleep 10
#打包
#mvn install -Dmaven.test.skip=true -U -P dev
echo "拷贝到Tomcat下"
# 简化后 拷贝编译后的代码到指定tomcat服务器中sudo systemctl restart tomcat  启动Tomcat;
rsync -rtlvz --exclude 'wtshop.properties'  /root/learngit/wtshop/wtshop-web/target/wtshop-web/*    /var/lib/tomcat/webapps/teacher
rsync -rtlvz /root/teacher/wtshop.properties /var/lib/tomcat/webapps/teacher/WEB-INF/classes

echo "开启tomcat"
service tomcat start
#tail -f /mrmf/tomcat_shops/logs/catalina.out