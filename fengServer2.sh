echo "開始停止tomcat服务!"
 /mrmf/tomcat_shops/bin/shutdown.sh
echo "备份当前tomcat下商城代码"
tar zcvf /mrmf/tomcat_shops/webapps/ROOT$(date +%Y%m%d%H).tar.gz /mrmf/tomcat_shops/webapps/ROOT/* ;//备份语句根据实际情况修改
sleep 10
echo "删除文件及缓存"
sudo rm -rf /root/learngit/wtshop/wtshop-web/target/*
sudo rm -rf /mrmf/tomcat_shops/webapps/ROOT/*
cd /root/learngit/wtshop
echo "复制service1代码"

expect -c "
    spawn scp -r root@10.25.92.80:/root/learngit/wtshop/wtshop-web/target/wtshop-web    /root/learngit/wtshop/wtshop-web/target
    expect {
        \"*assword\" {set timeout 300; send \"i@8fmhY7!m3FkwJ*\r\"; exp_continue;}
        \"yes/no\" {send \"yes\r\";}
    }
expect eof"

echo "拷贝到Tomcat下"
rsync -rtlvz --exclude 'wtshop.properties' /root/learngit/wtshop/wtshop-web/target/wtshop-web/*  /mrmf/tomcat_shops/webapps/ROOT  ;   简化后 拷贝编译后的代码到指定tomcat服务器中sudo systemctl restart tomcat  启动Tomcat;
rsync -rtlvz /root/learngit/mr_feng/wtshop.properties /mrmf/tomcat_shops    /webapps/ROOT/WEB-INF/classes
echo "开启启动tomcat"
sleep 2
sudo /mrmf/tomcat_shops/bin/startup.sh
#tail -f /mrmf/tomcat_shops/logs/catalina.out