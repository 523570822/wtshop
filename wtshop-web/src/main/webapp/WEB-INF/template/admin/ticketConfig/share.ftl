[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <title>${message("ticketShare.toptitle")}- Powered By ${setting.siteAuthor}</title>
    <meta name="author" content="${setting.siteAuthor}"/>
    <meta name="copyright" content="${setting.siteCopyright}"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <!-- Bootstrap framework -->
    <link rel="stylesheet" href="${base}/gebo3/bootstrap/css/bootstrap.min.css"/>
    <!-- theme color-->
    <link rel="stylesheet" href="${base}/gebo3/css/blue.css"/>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/layer_mobile/layer.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/layer/laytpl.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
    <script type="text/javascript">
        var SN=getQueryString('sn');
        $().ready(function () {
            $(document).on('click','#chouquan',function () {
                var phone=$('#phone').val();
                if (!phone){
                    alert('${message("ticketShare.qsrsjh")}');
                    return false;
                }

                if (!v.checkMobile(phone)){
                    alert('${message("ticketShare.qsrzqsjh")}');
                     return false;
                }
                var  ticketsnrecordId=$(this).attr('ticketsnrecordId')
               var index= dataload();
                $.get('extractTicket.jhtml',{phone:phone,sn:SN,},function (d) {
                    if(d.code!=1){
                        alert(d.msg)


                    }else {
                        alert('${message("ticketShare.lqcg")}');
                        laytpl($('#listTmpl').html()).render(d.data, function(html) {
                            $('#stime').text(d.data.ticketconfig.beginTime);
                            $('#etime').text(d.data.ticketconfig.endTime);
                            $('#listPack').html(html);
                        });
                    }


                    setTimeout(function(){
                        layer.closeAll()

                    },1000);
                })
            })

        });
    </script>
</head>
<body style="background: #f5f5f5">
<form role="form" style="margin-top: 10px">

    <div class="form-group"><label class="col-lg-2 control-label">${message("ticketShare.sqhdkssj")}:</label>
        <span class="col-lg-10" id="stime">${ticketconfig.beginTime}</span>
    </div>
    <div class="form-group"><label class="col-lg-2 control-label">${message("ticketShare.sqhdjssj")}:</label>
        <span class="col-lg-10" id="etime">${ticketconfig.endTime}</span>
    </div>
    <div class="form-group"><label class="col-lg-2 control-label">${message("ticketShare.dqhdqcqcs")}:</label>
        <span class="col-lg-10" id="num">${ticketsn.currentNum}</span>
    </div>
    <div class="form-group"><label class="col-lg-2 control-label">${message("ticketShare.dqhdsqcqzdxz")}:</label>
        <span class="col-lg-10" id="maxnum">${ticketsn.maxShareNum}</span>
    </div>
    <div class="form-group"><label class="col-lg-2 control-label" >${message("ticketShare.srsjh")}:</label>
        <input type="text"  class="col-lg-10" id="phone" maxlength="11"/>

        <span class="btn btn-primary"  id="chouquan" type="button">${message("ticketShare.cq")}</span>
    </div>

    <div class=" table  table-condensed" id="listPack"></div>



</form>




</body>
</html>


<script id="listTmpl" type="text/html">
    <table class="table" style="font-size: 14px">
        <tr class="bt">
            <td style="" >${message("ticketShare.qmc")}</td>
            <td style="">${message("ticketShare.yxksjs")}</td>
            <td style="">${message("ticketShare.yxjssj")}</td>
        </tr>
        {{# for(var i = 0, len = d.ticketList.length; i< len; i++){  var node= d.ticketList[i];    }} <tr>
        <td style="" class="name">{{node.name}}</td>
        <td style="">{{node.begin_date}}</td>
        <td style="">{{node.end_date}}</td>
    </tr>
        {{# } }}
    </table>
</script>

[/#escape]