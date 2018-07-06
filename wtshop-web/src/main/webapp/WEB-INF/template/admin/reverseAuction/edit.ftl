[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <title>${message("admin.reverseAuction.edit")} - Powered By ${setting.siteAuthor}</title>
    <meta name="author" content="${setting.siteAuthor}"/>
    <meta name="copyright" content="${setting.siteCopyright}"/>
    <link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css"/>
    <link href="${base}/statics/lib/layer/skin/default/layer.css"/>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.tools.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.validate.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.autocomplete.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/webuploader.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/ueditor/ueditor.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/input.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/datePicker/WdatePicker.js"></script>
    <style type="text/css">
        .memberRank label, .coupon label {
            min-width: 120px;
            _width: 120px;
            display: block;
            float: left;
            padding-right: 4px;
            _white-space: nowrap;
        }
    </style>

</head>
<body>
<div class="breadcrumb">
    <a href="${base}/admin/common/index.jhtml">${message("Seo.Type.index")}</a> &raquo;
    <a href="${base}/admin/reverseAuction/list.jhtml">${message("admin.role.reverseGroup")}</a> &raquo; ${message("reverseGroup.edit")}
</div>
<form id="inputForm" action="update.jhtml" method="post">
    <input type="hidden" name="reverseAuction.id" value="${reverseAuction.id}"/>
    <ul id="tab" class="tab">
        <li>
            <input type="button" value="${message("admin.promotion.base")}"/>
        </li>
    </ul>
    <table class="input tabContent">
        <tr>
            <th>
                <span class="requiredField">*</span>${message("Promotion.title")}:
            </th>
            <td>
                <input type="text" name="reverseAuction.title" value="${reverseAuction.title}" class="text title"
                       maxlength="200" require/>
            </td>
        </tr>
        <tr>
            <th>
                <span class="requiredField">*</span>到期时间:
            </th>
            <td>
                <input type="text" id="expireDate" name="expireDate"
                       value="${reverseAuction.expireDate?string('yyyy-MM-dd HH:mm:ss')}"
                       class="text Wdate" onclick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss',minDate:'%y-%M-%d'})" require/>
            </td>
        </tr>
    </table>
    <table class="input">
        <tr>
            <th>
                &nbsp;
            </th>
            <td>
                <input type="submit" class="button" value="${message("admin.common.submit")}"/>
                <input type="button" class="button" value="${message("admin.common.back")}"
                       onclick="history.back(); return false;"/>
            </td>
        </tr>
    </table>
</form>
</body>
<script type="text/javascript">
    $().ready(function () {
        [@flash_message /]
        var $inputForm = $("#inputForm");
        var $filePicker = $("#filePicker");
        var $giftSelect = $("#giftSelect");
        var $giftTable = $("#giftTable");
        var $giftTitle = $("#giftTable tr:first");
        var $introduction = $("#introduction");

        $inputForm.validate({
            rules: {
                "reverseAuction.title": "required",
                "reverseAuction.expireDate": "required"
            },
            messages: {
                "goods.sn": {
                    pattern: "${message("admin.validate.illegal")}",
                    remote: "${message("admin.validate.exist")}"
                }
            },
            submitHandler: function (form) {
                form.submit();
            }
        });
    });
</script>
</html>
[/#escape]