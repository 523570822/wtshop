[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <title>优惠券分享- Powered By ${setting.siteAuthor}</title>
    <meta name="author" content="${setting.siteAuthor}"/>
    <meta name="copyright" content="${setting.siteCopyright}"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <!-- Bootstrap framework -->
    <link rel="stylesheet" href="${base}/gebo3/bootstrap/css/bootstrap.min.css"/>
    <!-- theme color-->
    <link rel="stylesheet" href="${base}/gebo3/css/blue.css"/>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/layer_mobile/layer.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
    <script type="text/javascript">
        $().ready(function () {
            $('#go').click(function () {
                var phone=$('#phone').val();
                if (!phone){
                    alert('请输入手机号');
                    return
                }

                if (!v.checkMobile(phone)){
                    alert('请输入正确手机号');
                    return
                }
               var index= dataload();
                $.post('receive.jhtml',{phone:phone,cShareId:${d.couponShare.id}},function (d) {
                    if(d.code!=1){
                        alert(d.msg)
                        layer.close(index);
                    }else {
                        alert('领取成功');
                        setTimeout(function(){
                            layer.close(index);
                            location.href="${base}/act/couponShare/info.jhtml?cShareId=${d.couponShare.id}&t="+Math.random();

                        },2000);
                    }
                })
            })

        });
    </script>
</head>
<body style="background: #f5f5f5">
<form role="form" style="margin-top: 10px">

    <div class="form-group">
        <label class="col-lg-2 control-label">优惠券名:</label>
        <span class="col-lg-10">${d.coupon.name}</span>
    </div>
    <div class="form-group"><label class="col-lg-2 control-label">开始时间:</label>
        <span class="col-lg-10">${d.coupon.begin_date}</span></div>
    <div class="form-group"><label class="col-lg-2 control-label">结束时间:</label>
        <span class="col-lg-10">${d.coupon.end_date}</span></div>
    <div class="form-group"><label class="col-lg-2 control-label">领取上限:</label>
        <span class="col-lg-10">${d.couponShare.numLimit}</span></div>
    <div class="form-group"><label class="col-lg-2 control-label">当前领取数量:</label>
        <span class="col-lg-10">${d.couponShare.currentNum}</span></div>
    <div class="form-group"><label class="col-lg-2 control-label" >输入手机号:</label>
        <input type="text"  class="col-lg-10" id="phone" maxlength="11"/></div>

    <div class="col-sm-offset-2 col-lg-10">
        <button type="button" class="btn btn-primary" id="go">领取</button>
    </div>

</form>


</body>
</html>
[/#escape]