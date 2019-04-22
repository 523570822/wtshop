echo "stop tomcat!"
 service tomcat stop
echo "????"
sudo rm -rf /root/learngit/wtshop/wtshop-web/target/*
sudo rm -rf /var/lib/tomcat/webapps/teacher/*
cd /root/learngit/wtshop
echo "??git"
git pull origin   --force
sleep 10
echo "??maven??"
mvn -X install -Dmaven.test.skip=true
sleep 10
#??
#mvn install -Dmaven.test.skip=true -U -P dev
echo "???Tomcat?"
# ??? ???????????tomcat????sudo systemctl restart tomcat  ??Tomcat;
rsync -rtlvz --exclude 'wtshop.properties'  /root/learngit/wtshop/wtshop-web/target/wtshop-web/*    /var/lib/tomcat/webapps/teacher
rsync -rtlvz /root/teacher/wtshop.properties /var/lib/tomcat/webapps/teacher/WEB-INF/classes
chmod 777 /usr/share/tomcat/webapps/teacher/upload
echo "??tomcat"
service tomcat start

#tail -f /var/log/tomcat/catalina.out