[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <title>${message("admin.review.edit")} - Powered By ${setting.siteAuthor}</title>
    <meta name="author" content="${setting.siteAuthor}" />
    <meta name="copyright" content="${setting.siteCopyright}" />
    <link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/input.js"></script>
    <script type="text/javascript" src="${base}/statics/js/jquery.imgbox.pack.js"></script>
    <script type="text/javascript" src="${base}/statics/lib/layer/layer.js"></script>
    <script type="text/javascript">


        $().ready(function() {
            var id = $("#id").val();
            var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
            var $inputForm = $("#inputForm");
            var $fail = $("#fail");
            var $success = $("#success");
            var $back = $("#back");
            [@flash_message /]
            $success.click(function () {
                $.post("/admin/certificates_shenhe/shenhe.jhtml",{"type":1,"certificatesId":id},function (data) {
                    if(data.result){
                        parent.layer.msg(data.msg,{shade: 0,time:1000,icon:1});
                    }else {
                        parent.layer.msg(data.msg,{shade: 0,time:1000,icon:2});
                    }
                    window.parent.location.reload();
                    parent.layer.close(index);
                })
            });
            $fail.click(function () {
                $.post("/admin/certificates_shenhe/shenhe.jhtml",{"type":2,"certificatesId":id},function (data) {
                    if(data.result){
                        parent.layer.msg(data.msg,{shade: 0,time:1000,icon:1});
                    }else {
                        parent.layer.msg(data.msg,{shade: 0,time:1000,icon:2});
                    }
                    window.parent.location.reload();
                    parent.layer.close(index);
                })
            });
            $back.click(function () {
                window.parent.location.reload();
                parent.layer.close(index);
            });

            layer.photos({
                photos: '#oppositeImg',
                shadeClose:false,
                closeBtn:2,
                anim: 0,
                area: ['500px', '300px']
            });

            layer.photos({
                photos: '#positiveImg',
                shadeClose:false,
                closeBtn:2,
                anim: 0,
                area: ['500px', '300px']
            });


        });
    </script>
</head>
<body>
<div class="breadcrumb">
</div>
<form id="inputForm" action="update.jhtml" method="post">
    <input type="hidden" name="id" id="id" value="${certificates.id}" />
    <table class="input">
        <tr>
            <th>
            ${message("admin.referrerGoods.mName")}:
            </th>
            <td>
               ${certificates.member.nickname}
            </td>
        </tr>
        <tr>
            <th>
            ${message("Footprint.nickname")}:
            </th>
            <td>
            ${certificates.name}
            </td>
        </tr>
        <tr>
            <th>
            ${message("Member.mobile")}:
            </th>
            <td>
            ${certificates.phone}
            </td>
        </tr>
        <tr>
            <th>
            ${message("shen.idcard")}:
            </th>
            <td>
            ${certificates.id_card}
            </td>
        </tr>
        <tr>
            <th>
            ${message("shen.create_time")}:
            </th>
            <td>
            ${certificates.create_date}
            </td>
        </tr>
        <tr>
            <th>
            ${message("admin.shenhe.zheng")}:
            </th>
            <td id="positiveImg">
                <img src="${fileServer}${certificates.positiveImg}" layer-src= "${fileServer}${certificates.positiveImg}" height="100"  width="150">
            </td>
        </tr>
        <tr>
            <th>
            ${message("admin.shenhe.fan")}::
            </th>
            <td id="oppositeImg">
                <img src="${fileServer}${certificates.oppositeImg}" layer-src= "${fileServer}${certificates.oppositeImg}" height="100" width="150">
            </td>
        </tr>
        <tr>
            <th>
                &nbsp;
            </th>
            <td>
                <input type="button" class="button" id="success" value="通过" />
                <input type="button" class="button" id="fail" value="不通过" />
                <input type="button" class="button" id="back" value="${message("admin.common.back")}"/>
            </td>
        </tr>
    </table>
</form>
</body>
</html>
[/#escape]