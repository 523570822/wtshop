

var lottery={
    index:-1,    //当前转动到哪个位置，起点位置
    count:0,    //总共有多少个位置
    timer:0,    //setTimeout的ID，用clearTimeout清除
    speed:20,    //初始转动速度
    times:0,    //转动次数
    cycle:50,    //转动基本次数：即至少需要转动多少次再进入抽奖环节
    prize:-1,    //中奖位置
    init:function(id){
        if ($("#"+id).find(".lottery-unit").length>0) {
            $lottery = $("#"+id);
            $units = $lottery.find(".lottery-unit");
            this.obj = $lottery;
            this.count = $units.length;
            $lottery.find(".lottery-unit-"+this.index).addClass("active");
        };
    },
    roll:function(){
        var index = this.index;
        var count = this.count;
        var lottery = this.obj;
        $(lottery).find(".lottery-unit-"+index).removeClass("active");
        index += 1;
        if (index>count-1) {
            index = 0;
        };
        $(lottery).find(".lottery-unit-"+index).addClass("active");
        this.index=index;
        return false;
    },
    stop:function(index){
        this.prize=index;
        return false;
    }
};
var indexx=0;
function roll(){
    lottery.times += 1;
    lottery.roll();//转动过程调用的是lottery的roll方法，这里是第一次调用初始化
    if (lottery.times > lottery.cycle+10 && lottery.prize==lottery.index) {
        clearTimeout(lottery.timer);
        $(".winning-prizes").show();
        lottery.prize=-1;
        lottery.times=0;
        click=false;
    }else{
        if (lottery.times<lottery.cycle) {
            lottery.speed -= 10;
        }else if(lottery.times==lottery.cycle) {
         //   var index = Math.random()*(lottery.count)|0;//中奖物品通过一个随机数生成
            var index=indexx;
            lottery.prize = index;
        }else{
            if (lottery.times > lottery.cycle+10 && ((lottery.prize==0 && lottery.index==7) || lottery.prize==lottery.index+1)) {
                lottery.speed += 110;
            }else{
                lottery.speed += 20;
            }
        }
        if (lottery.speed<40) {
            lottery.speed=40;
        };
        lottery.timer = setTimeout(roll,lottery.speed);//循环调用

    }
    zhongJJL();
    return false;
}

var click=false;
function   zhongJJL() {
    var dataa={
        id:4
    }
    //中奖轮播
    $.ajax({
        url:"../api/activity/findRaffle.jhtml",
        dataType:'json',
        data:dataa,
        success: function(data){
            var arr=data.data.raffleList;
            var jilu="<ul >";
            console.info(data.data.raffleList)
            for (var index in arr){
                var phone=arr[index].phone;
                var phoneJ = phone.substr(0, 3) + '****' + phone.substr(9);
                var prizeName=arr[index].prizeName;
                if(arr[index].SerialNumber==3){
                    prizeName=   "二等奖"
                }else if(arr[index].SerialNumber==7){
                    prizeName=   "一等奖"
                }
                jilu=jilu+" <li><p><span>"+phoneJ+"</span>抽中了<span>"+prizeName+"</span></p><p>"+arr[index].date+"</p></li>";


            }
            jilu=jilu+"</ul>"
            $("#jilu").html(jilu)
        }
    });
}


window.onload=function(){

 zhongJJL();
 



















    lottery.init('lottery');
    $("#lottery button").click(function(){
        //click控制一次抽奖过程中不能重复点击抽奖按钮，后面的点击不响应
        if(click) {
            $(this).attr("disabled","true");
            alert("只能点击一次");
        } else {
            //转圈过程不响应click事件，会将click置为false
            lottery.speed = 100;

            var dataa={
                id:getQueryString("id")
            }
            $.ajax({
                url:"../api/activity/lottery.jhtml",
                dataType:'json',
                headers: {

                    token: getQueryString("token")
                },
                data:dataa,
                success: function(data){
                    console.info(data);
                   var mag="";

                    if(data.data.status==4){
                        mag="活动已经关闭";
                       $("#xx").html(mag);
                       $(".winning-prizes").show();

                    }else if(data.data.status==0){
                        mag="抽奖机会已经用完";

                        $("#xx").html(mag);
                        $("#xx").css('color','#FFFFFF');
                        $(".draw").css('background-image','url(images/bg_no.png)');
                        $(".draw").css('top','100px');
                        $(".draw").css('height','55%');

                        $(".winning-prizes").show();



                        return false;



                    }else if(data.data.status==2){

                       $("#xx").html(mag);
                       $(".winning-prizes").show();

                    }else if(data.data.status==3){
                        mag="活动未开始";
                        $("#xx").html(mag);
                        $(".winning-prizes").show();
                    }else{
                         indexx=data.data.Ranking;
                        console.info(data.data);

                           if(data.data.isz){
                               $("#xx").html("恭喜您获得"+PName);
						   }else{
                               $("#xx").html("恭喜您获得"+data.data.Point+"商城喵币");
						   }



                        roll();
                        //一次抽奖完成后，设置click为true，可继续抽奖

                        click = true;

                        return false;
                    }

                }
            });

        }
    });
};
function hide(){
    console.info("ddd");

}
function getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]); return null;
}
