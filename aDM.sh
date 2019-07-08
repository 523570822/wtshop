echo "開始停止tomcat服务"
/mrmf/apache-tomcat-8.5.42/bin/shutdown.sh
sleep 10
echo "删除文件及缓存"
rm -rf /root/learngit/wtshop/wtshop-web/target/*
rm -rf /mrmf/apache-tomcat-8.5.42/webapps/ROOT/*
cd /root/learngit/wtshop
echo "拉取git上代码"

echo "通过maven自动编译代码"
mvn -X install -Dmaven.test.skip=true
echo "拷贝到Tomcat下   简化后拷贝编译后的代码到指定tomcat服务器中sudo systemctl restart tomcat  打包"
rsync -rtlvz --exclude 'wtshop.properties' /root/learngit/wtshop/wtshop-web/target/wtshop-web/*  /mrmf/apache-tomcat-8.5.42/webapps/wtshop/
rsync -rtlvz /root/wtshop/wtshop.properties /mrmf/apache-tomcat-8.5.42/webapps/ROOT/WEB-INF/classes
chmod 777 /usr/share/tomcat/webapps/wtshop/upload
chmod 777 /var/lib/tomcat/webapps/wtshop/WEB-INF/classes/*
echo "开启tomcat tail -f /mrmf/apache-tomcat-8.5.42/logs/catalina.out"
/mrmf/apache-tomcat-8.5.42/bin/startup.sh


