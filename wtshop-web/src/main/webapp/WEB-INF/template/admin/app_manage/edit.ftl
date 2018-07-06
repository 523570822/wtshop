[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <title>${message("appManage.edit.title")} - Powered By ${setting.siteAuthor}</title>
    <meta name="author" content="${setting.siteAuthor}" />
    <meta name="copyright" content="${setting.siteCopyright}" />
    <link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
    <link href="${base}/statics/lib/layer/mobile/need/layer.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.tools.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.validate.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/webuploader.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/ueditor/ueditor.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/input.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/datePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="${base}/statics/lib/layer/layer.js"></script>

    <script type="text/javascript">

        $().ready(function() {
            var $inputForm = $("#inputForm");
            [@flash_message /]

            // 表单验证
            $inputForm.validate({
                rules: {
                    "fuDai.title":"required",
                    "fuDai.price":"required",
                    "fuDai.num": "required",
                    "product_name":"required"

                }
            });


        });



    </script>
</head>
<body>
<div class="breadcrumb">
    <a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; ${message("appManage.edit.title")}
</div>
<form id="inputForm" action="update.jhtml" method="post"   >
    <ul id="tab" class="tab">
        <li>
            <input type="button" value="${message("appManage.add.base")}" class="current" />
        </li>
        </ul>
    <input type="hidden" value="${appManage.id}" name="appManage.id"/>
    <table class="input tabContent">
        <tr>
            <th>
                <span class="requiredField">*</span>${message("appManage.add.appName")}:
            </th>
            <td>
                <input type="text" name="appManage.appName" class="text" maxlength="200" value="${appManage.appName}" />
            </td>
        </tr>
        <tr>
            <th>
                <span class="requiredField">*</span>${message("appManage.add.appId")}:
            </th>
            <td>
                <input type="text" name="appManage.appId" class="text" maxlength="200"  readonly="readonly"   value="${appManage.appId}" />
            </td>
        </tr>

        <tr>
            <th>
                <span class="requiredField">*</span>${message("appManage.add.AppKey")}:
            </th>
            <td>
                <input type="text" name="appManage.appKey" class="text" maxlength="200"  readonly="readonly"  value="${appManage.appKey}" />
            </td>
        </tr>

        <tr>
            <th>
                <span class="requiredField"></span>${message("appManage.add.ip")}:
            </th>
            <td>
                <input type="text" name="appManage.ipWhiteList" class="text" maxlength="200"    value="${appManage.ipWhiteList}" />
                <span>${message("appManage.add.tip")}</span>
            </td>
        </tr>

    </table>

    <table class="input">
        <tr>
            <th>&nbsp;

            </th>
            <td>
            <input type="submit" class="button" value="${message("admin.common.submit")}" />
            <input type="button" class="button" value="${message("admin.common.back")}" onclick="history.back(); return false;" />
        </td>
        </tr>
    </table>



</form>
</body>
</html>
[/#escape]