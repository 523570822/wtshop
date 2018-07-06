[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <title>${message("admin.ad.add")} - Powered By ${setting.siteAuthor}</title>
    <meta name="author" content="${setting.siteAuthor}" />
    <meta name="copyright" content="${setting.siteCopyright}" />
    <link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
    <link href="${base}/statics/lib/layer/mobile/need/layer.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
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
            var $type = $("#type");
            var $content = $("#content");
            var $path = $("#path");
            var $filePicker = $("#filePicker");
            var $targetPathId = $("#targetPathId");
            [@flash_message /]


            // 表单验证
            $inputForm.validate({
                rules: {
                    "targetPath.title": "required"
                }
            });

            });

    </script>
</head>
<body>
<div class="breadcrumb">
    <a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; ${message("targetPath.edit.editPath")}
</div>
<form id="inputForm" action="update.jhtml" method="post">
    <input type="hidden" value="${targetPath.id}" name="targetPath.id"/>
    <table class="input">
        <tr>
            <th>
                <span class="requiredField">*</span>${message("Ad.title")}:
            </th>
            <td>
                <input type="text" name="targetPath.title" class="text" maxlength="200" value="${targetPath.title}"/>
            </td>
        </tr>
        <tr>
            <th>
                <span class="requiredField">*</span>${message("targetPath.edit.inType")}:
            </th>
            <td>
                <select id="targetPathId" name="targetId">
                    <option value="0">${message("tag.top")}</option>
                    [#list targetPathList as targetPath]
                        [#if targetPath.level_state == 1&&targetPath.id!=1]
                            <option value="${targetPath.id}" [#if urlType==targetPath.urltype]selected[/#if]>
                            ${targetPath.title}
                            </option>
                        [/#if]
                    [/#list]
                </select>
            </td>
        </tr>
        <tr>
            <th>
                <span class="requiredField">*</span>${message("Ad.path")}:
            </th>
            <td>
					<span class="fieldSet">
						<input type="text" id="path" name="targetPath.target_path" class="text" maxlength="200" value="${targetPath.target_path}" />
					</span>
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