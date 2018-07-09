#!/bin/bash  
cd  /root/learngit/mr_feng
./root/learngit/mr_feng/server2Update.sh

# 匹配提示符  
CMD_PROMPT="\](\$|#)"  
  
# 要执行的脚本  
script="./fengServer2.sh"  
  
username="root"  
password="Renxingmao123456"  
host="10.24.203.237"  
port=22  
  
expect -c "  
   spawn ssh -p $port $username@$host  
    expect {  
            *yes/no* { send -- yes\r;exp_continue;}  
        *assword* { send -- $password\r;}  
    }  
    expect -re $CMD_PROMPT  
    send -- $script\r  
    expect -re $CMD_PROMPT  
    interact  
"  
echo "\r"  




  