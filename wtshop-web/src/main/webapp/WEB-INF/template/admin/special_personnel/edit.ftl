[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <title>${message("admin.brand.add")} - Powered By ${setting.siteAuthor}</title>
    <meta name="author" content="${setting.siteAuthor}" />
    <meta name="copyright" content="${setting.siteCopyright}" />
    <link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.validate.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/webuploader.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/ueditor/ueditor.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/input.js"></script>
    <script type="text/javascript">
        $().ready(function() {

            var $inputForm = $("#inputForm");
            var $type = $("#type");
            var $logo = $("#logo");
            var $filePicker = $("#filePicker");
            var $introduction = $("#introduction");

	[@flash_message /]

            $filePicker.uploader();

            $introduction.editor();

            $type.change(function() {
                if ($(this).val() == "text") {
                    $logo.prop("disabled", true).closest("tr").hide();
                } else {
                    $logo.prop("disabled", false).closest("tr").show();
                }
            });

            // 表单验证
            $inputForm.validate({
                rules: {
                    "brand.name": "required",
                    "brand.logo": {
                        required: true,
                        pattern: /^(http:\/\/|https:\/\/|\/).*$/i
                    },
                    "brand.url": {
                        pattern: /^(http:\/\/|https:\/\/|ftp:\/\/|mailto:|\/|#).*$/i
                    },
                    "brand.orders": "digits"
                }
            });

        });
    </script>
</head>
<body>
<div class="breadcrumb">
    <a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; ${message("admin.brand.add")}
</div>
<form id="inputForm" action="update.jhtml" method="post">
    <input type="hidden" name="specialPersonnel.id" value="${specialPersonnel.id}" />
    <table class="input">
        <tr>
            <th>
                <span class="requiredField">*</span>${message("Brand.name")}:
            </th>
            <td>
                <input type="text" name="specialPersonnel.title" value="${specialPersonnel.title}" class="text" maxlength="200" />
            </td>
        </tr>
        <tr>
            <th>
                手机号
            </th>
            <td>
                <input type="text" name="specialPersonnel.phone" value="${specialPersonnel.phone}"  class="text" maxlength="200" />
            </td>
        </tr>
        <tr>
            <th>
                &nbsp;
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