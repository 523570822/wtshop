echo "stop tomcat!"
systemctl stop service
sleep 5
echo "删除文件及缓存"
sudo rm -rf /root/learngit/wtshop/wtshop-web/target/*
sudo rm -rf /mrmf/tomcat_shops/webapps/ROOT/*
cd /root/learngit/wtshop
echo "拉取git上代码"
git pull origin   --force
sleep 5
echo "通过maven自动编译代码"
mvn -X install -Dmaven.test.skip=true

#打包简化后 拷贝编译后的代码到指定tomcat服务器中sudo systemctl restart tomcat  启动Tomcat;
#mvn install -Dmaven.test.skip=true -U -P dev
echo "拷贝到Tomcat下"
rsync -rtlvz --exclude 'wtshop.properties' /root/learngit/wtshop/wtshop-web/target/wtshop-web/*  /mrmf/tomcat_shops/webapps/ROOT
rsync -rtlvz /mrmf/tomcat_shops/wtshop-web/WEB-INF/classes/wtshop.properties /mrmf/tomcat_shops/webapps/ROOT/WEB-INF/classes

echo "开启tomcat"
systemctl start service
#tail -f /mrmf/tomcat_shops/logs/catalina.out