[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <title>添加团购 - Powered By ${setting.siteAuthor}</title>
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
                    title:"商品列表11",
                    type: 2,
                    skin: 'layui-layer-rim', //加上边框
                    area: ['870px', '540px'], //宽高
                    content: "../reverseAuction/chooseGoods.jhtml?flag=2",
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
                    "groupBuy.num": "required",
                    "product_name":"required"

                }
            });

            var $introduction = $("#introduction");
            $introduction.editor();

            var $productImageTable = $("#productImageTable");
            var productImageIndex = ${(groupBuy.productImagesConverter?size)!0};
            var $addProductImage = $("#addProductImage");
            var $filePicker = $("#filePicker");
            $filePicker.uploader();
            // 增加商品图片
            $addProductImage.click(function() {
            $productImageTable.append(
                [@compress single_line = true]
                        '<tr>
                        <td>
                        <input type="file" name="productImages[' + productImageIndex + '].file" class="productImageFile" \/>
                    <\/td>
                <td>
                <input type="text" name="productImages[' + productImageIndex + '].title" class="text" maxlength="200" \/>
                    <\/td>
                <td>
                <input type="text" name="productImages[' + productImageIndex + '].orders" class="text productImageOrder" maxlength="9" style="width: 50px;" \/>
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
                        $('#productImageTable tr').eq(i).find ('input').eq(0).attr('name','productImages['+index+'].file');
                        $('#productImageTable tr').eq(i).find ('input').eq(1).attr('name','productImages['+index+'].title');
                        $('#productImageTable tr').eq(i).find ('input').eq(2).attr('name','productImages['+index+'].orders');
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
    <a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo;${message("groupBuy.add.llst")}
</div>
<form id="inputForm" action="save.jhtml" method="post"  enctype="multipart/form-data" >
    <ul id="tab" class="tab">
        <li>
            <input type="button" value="${message("admin.coupon.base")}"class="current" />
        </li>
     [#--   <li>
            <input type="button" value="${message("groupBuy.rule")}" />
        </li>
        <li>
            <input type="button" value="${message("admin.goods.productImage")}" />
        </li>--]
        </ul>
    <input value="0" name="groupBuy.status" type="hidden"/>
    <table class="input tabContent">
        <tr>
            <th>
                <span class="requiredField">*</span>${message("Ad.title")}:
            </th>
            <td>
                <input type="text" name="groupBuy.title" class="text" maxlength="200"  />
            </td>
        </tr>
        <tr>
            <th>
                <span class="requiredField">*</span>${message("groupBuy.price")}:
            </th>
            <td>
                <input type="text" name="groupBuy.price" class="text" maxlength="200" title= ${message("groupBuy.sale.title")} />
            </td>
        </tr>
        <tr>
            <th>
                <span class="requiredField">*</span>${message("groupBuy.uniprice")}:
            </th>
            <td>
                <input type="text" name="groupBuy.uniprice" class="text" maxlength="200" title= ${message("groupBuy.uniprice.Explanation")} />
            </td>
        </tr>
        <tr>
            <th>
                <span class="requiredField">*</span>${message("groupBuy.primary.goods")}:
            </th>
            </th>
            <td>
                <input type="hidden" name="productId" id="product_id" class="text" maxlength="200" />
                <input type="text" class="text" maxlength="200" id="product_name" name="product_name" title=${message("groupBuy.phone.title")}  />
                <input type="button" value="选择产品" class="button" id="addProduct"/>
            </td>
        </tr>
     [#--   <tr>
            <th><span class="requiredField">*</span>${message("groupBuy.other.image")} </th>
            <td>
   <span class="fieldSet">
      <input type="text" name="groupBuy.questionImage" class="text" value="${groupBuyQuestionImage}" maxlength="200" />
      <a  id="filePicker"  href="javascript:;" title="手机端用于展示副产品的图片" class="button filePicker">${message("admin.upload.filePicker")} </a>
      <a href="${fileServer}${groupBuyQuestionImage}" target="_blank">${message("admin.common.view")}</a>
   </span>
            </td>

        </tr>--]
      <tr>
            <th>
                <span class="requiredField">*</span>${message("groupBuy.count")}:
            </th>
            <td>
                <input type="text" name="groupBuy.count" class="text" maxlength="200" title=${message("groupBuy.success.title")} />
            </td>
        </tr>
        <tr>
            <th>
                <span class="requiredField">*</span>${message("groupBuy.num")}:
            </th>
            <td>
                <input type="text" name="groupBuy.num" class="text" maxlength="200" title=${message("groupBuy.num.title")} />
            </td>
        </tr>


        <tr>
            <th>
                <span class="requiredField">*</span>${message("groupBuy.sales")}:
            </th>
            <td>
                <input type="text" name="groupBuy.num" class="text" maxlength="200"  />
            </td>
        </tr>
        <tr>
            <th>
                <span class="requiredField">*</span>${message("groupBuy.teamnum")}:
            </th>
            <td>
                <input type="text" name="groupBuy.teamnum" class="text" maxlength="200" />
            </td>
        </tr>
        <tr>
            <th>
                <span class="requiredField">*</span>${message("groupBuy.dispatchprice")}:
            </th>
            <td>
                <input type="text" name="groupBuy.dispatchprice" class="text" maxlength="200" />
            </td>
        </tr>
       <tr>
            <th>
                <span class="requiredField">*</span>${message("groupBuy.groupnum")}:
            </th>
            <td>
                <input type="text" name="groupBuy.groupnum" class="text" maxlength="200"  />
            </td>
        </tr><tr>
            <th>
                <span class="requiredField">*</span>${message("groupBuy.endtime")}:
            </th>
            <td>
                <input type="text" name="groupBuy.endtime" class="text" maxlength="200" title=${message("groupBuy.num.title")} />
            </td>
        </tr>


        <tr>
            <th>
                ${message("admin.common.setting")}:
            </th>
            <td>
                <label>
                    <input type="checkbox" id="isMarketable" name="isMarketable"  />${message("Goods.isMarketable")}
                    <input type="hidden" name="_isMarketable" value="false" />
                </label>
                <label>
                    <input type="checkbox" name="isList" value="true" checked="checked" />${message("Goods.isList")}
                    <input type="hidden" name="_isList" value="false" />
                </label>
                <label>
                    <input type="checkbox" name="isTop" value="true" />${message("Goods.isTop")}
                    <input type="hidden" name="_isTop" value="false" />
                </label>
                <label>
                    <input type="checkbox" name="isSinglepurchase" value="true" checked="checked" />${message("groupBuy.isSinglePurchase")}
                    <input type="hidden" name="_isSinglepurchase" value="false" />
                </label>
            </td>
        </tr>

        <tr>
            <th>
                <span class="requiredField"></span>${message("groupBuy.message")}:
            </th>
            <td>
                <textarea rows="" cols="" name="groupBuy.explain" style="width: 300px;height:200px " maxlength="400" title= ${message("groupBuy.message.title")}></textarea>
            </td>
        </tr>
        <tr>
            <th>
            ${message("admin.common.order")}:
            </th>
            <td>
                <input type="text" name="groupBuy.orders" class="text" maxlength="9" />
            </td>
        </tr>
<tr>
    <td>
        <input type="submit" class="button" value="${message("admin.common.submit")}" />
        <input type="button" class="button" value="${message("admin.common.back")}" onclick="history.back(); return false;" />
    </td>
</tr>

    </table>
    <table class="input tabContent">
        <tr><td><textarea id="introduction" name="groupBuy.rule" class="editor" style="width: 100%;"></textarea></td></tr>
    </table>

    <table id="productImageTable" class="item tabContent">
        <tr>
            <td colspan="4">
                <a href="javascript:;" id="addProductImage" title= ${message("groupBuy.image.title")} class="button">${message("admin.goods.addProductImage")}</a>
            </td>
        </tr>
        <tr>
            <th>
            ${message("ProductImage.file")}
            </th>
            <th>
            ${message("ProductImage.title")}
            </th>
            <th>
            ${message("admin.common.order")}
            </th>
            <th>
            ${message("admin.common.action")}
            </th>
        </tr>



    </table>
    <table class="input">
        <tr>
            <th>&nbsp;

            </th>

        </tr>
    </table>



</form>
</body>
</html>
[/#escape]