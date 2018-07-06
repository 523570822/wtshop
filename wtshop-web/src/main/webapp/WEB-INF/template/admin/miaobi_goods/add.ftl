[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <title>${message("admin.ad.add")} - Powered By ${setting.siteAuthor}</title>
    <meta name="author" content="${setting.siteAuthor}"/>
    <meta name="copyright" content="${setting.siteCopyright}"/>
    <link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css"/>
    <link href="${base}/statics/lib/layer/mobile/need/layer.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.validate.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/webuploader.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/ueditor/ueditor.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/input.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/datePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="${base}/statics/lib/layer/layer.js"></script>
    <script type="text/javascript">
        $().ready(function () {

            var $inputForm = $("#inputForm");
            var $type = $("#type");
            var $content = $("#content");
            var $path = $("#path");
            var $filePicker = $("#filePicker");
            $("#addProductImage").click(function () {
                layer.open({
                    title: "商品列表",
                    type: 2,
                    skin: 'layui-layer-rim', //加上边框
                    area: ['870px', '540px'], //宽高
                    content: "/admin/reverseAuction/chooseGoods.jhtml?flag=6",
                    shadeClose: true,
                });
            });
            [@flash_message /]

            $filePicker.uploader();

            $content.editor();

            $type.change(function () {
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
                    "miaobiGoods.price_miaobi": {
                        required: true,
                        number: true,
                        min: 0,
                    },
                    "miaobiGoods.price": {
                        required: true,
                        number: true,
                        min: 0,
                    },
                    "goods_name": "required"
                }
            });

        });
    </script>
</head>
<body>
<div class="breadcrumb">
    <a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; ${message("admin.goods.add")}
</div>
<form id="inputForm" action="save.jhtml" method="post">
    <table class="input">
        <tr>
            <th>
                <span class="requiredField">*</span>${message("admin.point.pointNum")}:
            </th>
            <td>
                <input type="text" name="miaobiGoods.price_miaobi" class="text" maxlength="200"/>
            </td>
        </tr>
        <tr>
            <th>
                <span class="requiredField">*</span>${message("admin.point.price")}:
            </th>
            <td>
                <input type="text" name="miaobiGoods.price" class="text" maxlength="200"/>
            </td>
        </tr>
        <tr>
            <th>
                <span class="requiredField">*</span>${message("admin.miaobiGoods.glGoods")}:
            </th>
            <td>
					<span class="fieldSet">
                        <input type="hidden" id="goods_id" name="miaobiGoods.goods_id"/>
						<input type="text" id="goods_name" name="goods_name" class="text" maxlength="200"/>
                        <a href="javascript:;" id="addProductImage" class="button">${message("ticket.addGoods")}</a>
					</span>
            </td>
        </tr>

        <tr>
            <th>
            ${message("admin.common.order")}:
            </th>
            <td>
                <input type="text" name="miaobiGoods.orders" class="text" maxlength="9"/>
            </td>
        </tr>
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
</html>
[/#escape]