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
                    content: "/admin/reverseAuction/choose.jhtml?flag=2",
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
            var productImageIndex = ${(fuDai.productImagesConverter?size)!0};
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
    <a href="${base}/admin/common/index.jhtml"></a> &raquo; ${message("Fudai.edit.llst")}
</div>
<form id="inputForm" action="edit.jhtml" method="post"  enctype="multipart/form-data" >
    <input name="fuDai.id" value="${fuDai.id}" type="hidden"/>
    <input value="${fuDai.status}" name="fuDai.status" type="hidden"/>
    <ul id="tab" class="tab">
        <li>
            <input type="button" value="${message("admin.coupon.base")}"   class="current" />
        </li>
        <li>
            <input type="button" value="${message("Fudai.rule")}"  />
        </li>
        <li>
            <input type="button" value="${message("admin.goods.productImage")}" />
        </li>
    </ul>
    <table class="input tabContent">
        <tr>
            <th>
                <span class="requiredField">*</span>${message("Ad.title")}:
            </th>
            <td>
                <input type="text" name="fuDai.title" class="text" maxlength="200" value="${fuDai.title}" />
            </td>
        </tr>
        <tr>
            <th>
                <span class="requiredField">*</span>${message("Fudai.price")}:
            </th>
            <td>
                <input type="text" name="fuDai.price" class="text" maxlength="200" value="${fuDai.price}"  title= ${message("fudai.sale.title")}/>
            </td>
        </tr>
        <tr>
            <th>
                <span class="requiredField">*</span>${message("Fudai.primary.goods")}:
            </th>
            <td>
                <input type="hidden" name="productId" id="product_id" class="text" maxlength="200" value="${fuDai.fudaiProduct.product_id}"/>
                <input type="text" class="text" maxlength="200" id="product_name" title=${message("fudai.phone.title")}   value="${fuDai.fudaiProduct.product.goods.name}" />
                <input type="button" value="${message("fudai.select")}" class="button" id="addProduct"/>
            </td>
        </tr>
        <tr>
            <th><span class="requiredField">*</span>${message("fudai.goods")}</th>
            <td>
   <span class="fieldSet">
      <input type="text" name="fuDai.questionImage" class="text" value="${fuDai.questionImage}" maxlength="200" />
      <a  id="filePicker"  href="javascript:;"  title=${message("fudai.phone")} class="button filePicker">${message("admin.upload.filePicker")}</a>
      <a href="${fileServer}${fuDai.questionImage}" target="_blank">${message("admin.common.view")}</a>
   </span>
            </td>

        </tr>
        <tr>
            <th>
                <span class="requiredField">*</span>${message("Fudai.other.count")}:
            </th>
            <td>
                <input type="text" name="fuDai.num" class="text" maxlength="200" value="${fuDai.num}" title=${message("fudai.success.title")}/>
            </td>
        </tr>
        <tr>
            <th>
                <span class="requiredField"></span>${message("Fudai.message")}:
            </th>
            <td>
                <textarea rows="" cols="" name="fuDai.explain" style="width: 300px;height:200px " maxlength="400" title="${message("Fudai.message.list")}" >${fuDai.explain}</textarea>
            </td>
        </tr>
        <tr>
            <th>
            ${message("admin.common.order")}:
            </th>
            <td>
                <input type="text" name="fuDai.orders" class="text" maxlength="9" value="${fuDai.orders}" />
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

    <table class="input tabContent">
        <tr><td><textarea id="introduction" name="fuDai.rule" class="editor" style="width: 100%;"  >${fuDai.rule}</textarea></td></tr>
        <tr style="text-align: center">
            <td>
                <input type="submit" class="button" value="${message("admin.common.submit")}" />
                <input type="button" class="button" value="${message("admin.common.back")}" onclick="history.back(); return false;" />
            </td>
        </tr>
    </table>

    <table id="productImageTable" class="item tabContent">
        <tr>
            <td colspan="4">
                <a href="javascript:;" id="addProductImage" title=${message("fudai.image.title")} class="button">${message("admin.goods.addProductImage")}</a>
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

        [#list fuDai.productImagesConverter as productImage]
            <tr>
                <td>
                    <input type="hidden" name="productImages[${productImage_index}].source"
                           value="${productImage.source}"/>
                    <input type="hidden" name="productImages[${productImage_index}].large"
                           value="${productImage.large}"/>
                    <input type="hidden" name="productImages[${productImage_index}].medium"
                           value="${productImage.medium}"/>
                    <input type="hidden" name="productImages[${productImage_index}].thumbnail"
                           value="${productImage.thumbnail}"/>
                    <input type="file"  name="productImages[${productImage_index}].file" class="productImageFile p_img"/>
                    <a href="${fileServer}${productImage.large}" target="_blank">${message("admin.common.view")}</a>
                </td>
                <td>
                    <input type="text" name="productImages[${productImage_index}].title" class="text p_title"
                           value="${productImage.title}" maxlength="200"/>
                </td>
                <td>
                    <input type="text" name="productImages[${productImage_index}].orders" class="text productImageOrder p_order"
                           value="${productImage.orders}" maxlength="9" style="width: 50px;"/>
                </td>
                <td>
                    <a href="javascript:;" class="remove">[${message("admin.common.remove")}]</a>
                </td>
            </tr>
        [/#list]
    </table>
</form>
</body>
</html>
[/#escape]