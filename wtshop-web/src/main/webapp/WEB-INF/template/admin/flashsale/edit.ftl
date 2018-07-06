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
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.validate.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/webuploader.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/ueditor/ueditor.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/input.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/datePicker/WdatePicker.js"></script>
    <script type="text/javascript">
        $().ready(function() {

            var $inputForm = $("#inputForm");
            var $type = $("#type");
            var $content = $("#content");
            var $path = $("#path");
            var $filePicker = $("#filePicker");

            [@flash_message /]

            $filePicker.uploader();

            $content.editor();

            $type.change(function() {
                if ($(this).val() == "text") {
                    $content.prop("disabled", false).closest("tr").show();
                    $path.prop("disabled", true).closest("tr").hide();
                } else {
                    $content.prop("disabled", true).closest("tr").hide();
                    $path.prop("disabled", false).closest("tr").show();
                }
            });

            // 表单验证
            $inputForm.validate({
                rules: {
                    "flashsale.title":"required",
                    "flashsale.end_date":"required",
                    "flashsale.begin_date":"required",
                    "ad.title": "required",
                    adPositionId: "required",
                    "ad.path": {
                        required: true,
                        pattern: /^(http:\/\/|https:\/\/|\/).*$/i
                    },
                    "ad.url": {
                        pattern: /^(http:\/\/|https:\/\/|ftp:\/\/|mailto:|\/|#).*$/i
                    },
                    "ad.orders": "digits"
                }
            });

        });
    </script>
</head>
<body>
<div class="breadcrumb">
    <a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; ${message("admin.flahsale.list")}
</div>
<form id="inputForm" action="update.jhtml" method="post">
    <input type="hidden" value="${flashsale.id}" name="flashsale.id"/>
    <table class="input">
        <tr>
            <th>
                <span class="requiredField">*</span>${message("Ad.title")}:
            </th>
            <td>
                <input type="text" name="flashsale.title" class="text" maxlength="200" value="${flashsale.title}" />
            </td>
        </tr>
        <tr>
            <th>
            ${message("Goods.productCategory")}:
            </th>
            <td>
                <select id="productCategoryId" name="flashsale.product_category_id">
                    [#list productCategoryTree as productCategory]
                        <option value="${productCategory.id}"[#if productCategory.id == flashsale.product_category_id] selected="selected"[/#if]>
                            [#if productCategory.grade != 0]
                                [#list 1..productCategory.grade as i]
                                    &nbsp;&nbsp;
                                [/#list]
                            [/#if]
                        ${productCategory.name}
                        </option>
                    [/#list]
                </select>
            </td>
        </tr>
        <tr>
            <th>
            ${message("Promotion.beginDate")}:
            </th>
            <td>
                <input value="${flashsale.begin_date?string('yyyy-MM-dd HH:mm:ss')}" type="text" id="beginDate" name="flashsale.begin_date" class="text Wdate" onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'});" />
            </td>
        </tr>
        <tr>
            <th>
            ${message("Promotion.endDate")}:
            </th>
            <td>
                <input value='${flashsale.end_date?string("yyyy-MM-dd HH:mm:ss")}' type="text" id="endDate" name="flashsale.end_date" class="text Wdate" onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'});" />
            </td>
        </tr>
        <tr>
            <th>
            ${message("admin.common.order")}:
            </th>
            <td>
                <input type="text" value="${flashsale.orders}" name="flashsale.orders" class="text" maxlength="9" />
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