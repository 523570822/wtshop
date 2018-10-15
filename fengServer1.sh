echo "開始停止tomcat服务"
/mrmf/tomcat_shop/bin/shutdown.sh
echo "备份当前tomcat下商城代码 备份语句根据实际情况修改"
tar zcpf /mrmf/tomcat_shop/webapps/ROOT$(date +%Y%m%d%H).tar.gz /mrmf/tomcat_shop/webapps ;
sleep 10
echo "删除文件及缓存"
rm -rf /root/learngit/wtshop/wtshop-web/target/*
rm -rf /mrmf/tomcat_shop/webapps/ROOT/*
cd /root/learngit/wtshop
echo "拉取git上代码"
git pull origin   --force
echo "通过maven自动编译代码"
mvn -X install -Dmaven.test.skip=true
echo "拷贝到Tomcat下   简化后拷贝编译后的代码到指定tomcat服务器中sudo systemctl restart tomcat  打包"
rsync -rtlvz --exclude 'wtshop.properties' /root/learngit/wtshop/wtshop-web/target/wtshop-web/*  /mrmf/tomcat_shop/webapps/ROOT
rsync -rtlvz /root/learngit/mr_feng/wtshop.properties /mrmf/tomcat_shop/webapps/ROOT/WEB-INF/classes
echo "开启tomcat tail -f /mrmf/tomcat_shop/logs/catalina.out"
/mrmf/tomcat_shop/bin/startup.sh
