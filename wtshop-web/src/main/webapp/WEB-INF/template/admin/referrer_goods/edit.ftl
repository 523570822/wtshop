[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <title>${message("admin.area.edit")} - Powered By ${setting.siteAuthor}</title>
    <meta name="author" content="${setting.siteAuthor}"/>
    <meta name="copyright" content="${setting.siteCopyright}"/>
    <link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.validate.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/input.js"></script>
    <script type="text/javascript">
        $().ready(function () {

            [@flash_message /]

            $("#goback").click(function () {
                window.location.href = "${base}/admin/referrer_goods/list.jhtml";
            });
            // 表单验证
            var $inputForm = $("#inputForm");
            $inputForm.validate({
                rules: {
                    "referrerConfig.referrer_time": "digits"
                }
            });
        });
    </script>
</head>
<body>
<div class="breadcrumb">
    <a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; ${message("admin.referrerGoods.edit")}
</div>
<form id="inputForm" action="edit.jhtml" method="post">
    <input type="hidden" name="referrerConfig.id" id="flag" value="${referrerConfig.id}"/>
    <table class="input">
        <tr>
            <th>
                <span class="requiredField">*</span>单个技师最大推荐频率
            </th>
            <td>
                <span>每</span>
                <input type="text" name="referrer_time" class="text" value="${referrer_time}" title="单位(秒)" maxlength="20" style="width: 50px;"/>
                <span>秒，可推荐</span>
                <input type="text" name="referrer_num" class="text" value="${referrer_num}" title="单位(次)" maxlength="20" style="width: 50px;"/>
                <span>次</span>
                &nbsp;&nbsp;&nbsp;&nbsp;
                <span>
                    如果填写0,0表示：单个技师，不限制推荐次数和时间间隔;
                    如果填写60,1表示：单个技师，一分钟(60秒)可以推荐1次;
                </span>
            </td>
        </tr>
        <tr>
            <th>
                &nbsp;
            </th>
            <td>
                <input type="submit" class="button" value="${message("admin.common.submit")}"/>
                <input type="button" class="button" value="${message("admin.common.back")}" id="goback"/>
            </td>
        </tr>
    </table>
</form>
</body>
</html>
[/#escape]