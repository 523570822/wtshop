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
            var $type = $("#type");
            var $content = $("#content");
            var $path = $("#path");
            var $filePicker = $("#filePicker");
            $("#addProduct").click(function() {
                layer.open({
                    title:"商品列表",
                    type: 2,
                    skin: 'layui-layer-rim', //加上边框
                    area: ['870px', '540px'], //宽高
                    content: "../reverseAuction/choose.jhtml?flag=2",
                    shadeClose:true,
                });
            });
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
                    "groupBuy.title":"required",
                    "groupBuy.price":"required",
                    "groupBuy.begin_date":"required",
                    "groupBuy.end_date":"required",
                    "groupBuy.uniprice":"required",
                    "groupBuy.num": "required",
                    "product_name":"required" ,
                    "freeUse.product_id":"required",
                    "freeUse.end_date":"required",
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
            var $introduction = $("#introduction");
            $introduction.editor();



            var $productImageTable = $("#productImageTable");
            var productImageIndex = ${(groupBuy.productImagesConverter?size)!0};
            var $addProductImage = $("#addProductImage");
            // 增加商品图片
            // 增加商品图片
            $addProductImage.click(function() {
            $productImageTable.append(
                [@compress single_line = true]
                        '<tr>
                        <td>
                        <input type="file" name="productImages[' + productImageIndex + '].file" class="productImageFile p_img" \/>
                    <\/td>
                <td>
                <input type="text" name="productImages[' + productImageIndex + '].title" class="text p_title" maxlength="200" \/>
                    <\/td>
                <td>
                <input type="text" name="productImages[' + productImageIndex + '].orders" class="text productImageOrder p_order" maxlength="9" style="width: 50px;" \/>
                    <\/td>
                <td>
                <a href="javascript:;" class="remove">[${message("admin.common.remove")}]<\/a>
                <\/td>
                <\/tr>'
                [/@compress]
                );
                productImageIndex ++;
                //重新排序商品图片id,后台是读取的顺序id
                var index=0;
                $('#productImageTable tr').each(function (i,v) {
                    if ($(this).find('input').length>0){
                        $('#productImageTable tr').eq(i).find ('.p_img').attr('name','productImages['+index+'].file');
                        $('#productImageTable tr').eq(i).find ('.p_title').attr('name','productImages['+index+'].title');
                        $('#productImageTable tr').eq(i).find ('.p_order').attr('name','productImages['+index+'].orders');
                        index++;
                    }
                });
            });

            // 删除商品图片
            $productImageTable.on("click", "a.remove", function() {
                $(this).closest("tr").remove();
            });

        });
    </script>
</head>
<body>
<div class="breadcrumb">
    <a href="${base}/admin/common/index.jhtml"></a> &raquo; ${message("groupBuy.edit.llst")}
</div>
<form id="inputForm" action="edit.jhtml" method="post"  enctype="multipart/form-data" >
    <input name="miaobiLssue.id" value="${miaobiLssue.id}" type="hidden"/>
    <input value="${miaobiLssue.status}" name="miaobiLssue.status" type="hidden"/>
    <ul id="tab" class="tab">
        <li>
            <input type="button" value="${message("admin.coupon.base")}"   class="current" />
        </li>
       [#-- <li>
            <input type="button" value="${message("groupBuy.rule")}"  />
        </li>--]
      [#--  <li>
            <input type="button" value="${message("admin.goods.productImage")}" />
        </li>--]
    </ul>
    <table class="input tabContent">
        <tr>
            <th>
                <span class="requiredField">*</span>${message("Ad.title")}:
            </th>
            <td>
                <input type="text" name="miaobiLssue.title" class="text" maxlength="200" value="${miaobiLssue.title}" />
            </td>
        </tr>





        <tr>
            <th>
                <span class="requiredField">*</span>喵币数量:
            </th>
            <td>
                <input type="text" name="miaobiLssue.number" class="text" value="${miaobiLssue.number}" maxlength="200" title=喵币数量 />
            </td>
        </tr>
        <tr>
            <th>
                ${message("Promotion.beginDate")}:
            </th>
            <td>
                <input type="text" id="beginDate" value="${miaobiLssue.begin_date}" name="miaobiLssue.begin_date" class="text Wdate" onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss', maxDate: '#F{$dp.$D(\'endDate\')}'});" />
            </td>
        </tr>
        <tr>
            <th>
                ${message("Promotion.endDate")}:
            </th>
            <td>
                <input type="text" id="endDate" name="miaobiLssue.end_date" value="${miaobiLssue.begin_date}" class="text Wdate" onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss', minDate: '#F{$dp.$D(\'beginDate\')}'});" />
            </td>
        </tr>
        <tr>
            <th>
                ${message("admin.common.setting")}:
            </th>
            <td>
                <label>
                    <input type="checkbox" name="status" value="true"
                       [#if miaobiLssue.status]  checked="checked"[/#if] />
              是否上架：
                    <input type="hidden" name="_status" value="false"/>
                </label>
            </td>
        </tr>
        <tr>
            <th>
                <span class="requiredField"></span>${message("groupBuy.message")}:
            </th>
            <td>
                <textarea rows="" cols="" name="miaobiLssue.explain" style="width: 300px;height:200px " maxlength="400" title="${message("groupBuy.message.list")}" ></textarea>
            </td>
        </tr>
       [#-- <tr>
            <th>
            ${message("admin.common.order")}:
            </th>
            <td>
                <input type="text" name="miaobiLssue.orders" class="text" maxlength="9" value="${groupBuy.orders}" />
            </td>
        </tr>--]
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