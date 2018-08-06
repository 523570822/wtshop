[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <title>${message("admin.goods.add")} - Powered By ${setting.siteAuthor}</title>
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
    <script type="text/javascript" src="${base}/resources/admin/js/list.js"></script>
    <script type="text/javascript" src="${base}/statics/lib/layer/layer.js"></script>
    <style type="text/css">
        .parameterTable table th {
            width: 146px;
        }

        .specificationTable span {
            padding: 10px;
        }

        .productTable td {
            border: 1px solid #dde9f5;
        }
    </style>
    <script type="text/javascript">

        $().ready(function() {
            [@flash_message /]
            var flag = $("#flag").val();
            if(flag==1){alert(flag);
                $("#addProductImage").click();
            }
            var $inputForm = $("#inputForm");
            var $productCategoryId = $("#productCategoryId");
            var $filePicker = $("#filePicker");
            var $introduction = $("#introduction");
            var $productImageTable = $("#productImageTable");
            var $addProductImage = $("#addProductImage");
            var $parameterTable = $("#parameterTable");
            var productImageIndex = 0;
            var parameterIndex = 0;

            [@flash_message /]

            var previousProductCategoryId = getCookie("previousProductCategoryId");
            previousProductCategoryId != null ? $productCategoryId.val(previousProductCategoryId) : previousProductCategoryId = $productCategoryId.val();

            $filePicker.uploader();

            $introduction.editor();

            // 增加商品图片
            $addProductImage.click(function() {
                layer.open({
                    title:"商品列表",
                    type: 2,
                    skin: 'layui-layer-rim', //加上边框
                    area: ['870px', '540px'], //宽高
//                    ccontent:"/admin/goods/list.jhtml",
                    content: "/admin/reverseAuction/chooseGoods.jhtml?flag=4",
                    shadeClose:true,
                });
                $("#show").show();



                productImageIndex ++;
            });

            // 删除商品图片
            $productImageTable.on("click", "a.remove", function() {
                $(this).closest("tr").remove();
            });


            // 增加参数
            $parameterTable.on("click", "a.add", function() {
                var $table = $(this).closest("table");
                var parameterIndex = $table.data("parameterIndex");
                var parameterEntryIndex = $table.data("parameterEntryIndex");
            $table.append(
                [@compress single_line = true]
                        '<tr>
                        <th>
                        <input type="text" name="parameterValueEntrys[' + parameterIndex + '].entries[' + parameterEntryIndex + '].name" class="text parameterEntryName" maxlength="200" style="width: 50px;" \/>
                    <\/th>
                <td>
                <input type="text" name="parameterValueEntrys[' + parameterIndex + '].entries[' + parameterEntryIndex + '].value" class="text parameterEntryValue" maxlength="200" \/>
                    <\/td>
                <td>
                <a href="javascript:;" class="remove">[${message("admin.common.remove")}]<\/a>
                <\/td>
                <\/tr>'
                [/@compress]
                );
                $table.data("parameterEntryIndex", parameterEntryIndex + 1);
            });



            $.validator.addClassRules({
                sale_num: {
                    required: true,
                    number:true
                },
                num: {
                    required: true,
                    number:true,
                    min:0.01

                },
                maxNum:{
                    max:${fd.num}
                }

            });

            // 表单验证
            $inputForm.validate({
                rules: {}

            });

        });

        $(document).on('click','.grandPrix',function () {
           var  v=$(this).parent().find('.grandPrixV');

           if (v.val()==1){
               $(this).text('[设为大奖]')
               v.val(0)
               $(this).parent().parent().find('.isgrand').text('否');
           }else {
               $(this).text('[取消大奖]')
                v.val(1)
               $(this).parent().parent().find('.isgrand').text('是');
           }
            $(this).toggleClass('red').parent().parent().find('.isgrand').toggleClass('red');

        })
    </script>
</head>
<body>

<input type="hidden" id="detailNum" name="indexNum" value="${indexNum}"/>
<div class="breadcrumb">
    <a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; ${message("admin.goods.add")}
</div>
<form id="inputForm" action="saveGoogs.jhtml" method="post">
    <input type="hidden" id="goodsThemeId" name="fuDaiId" value="${fuDaiId}"/>
    <div style="margin: 10px;font-size: 16px;line-height: 20px">
        <p class="red">${message("fudai.tioajian.first")}</p>
        <p class="red">${message("fudai.tioajian.two")}</p>
        <p class="red">${message("fudai.tioajian.secone")}</p>
    </div>


    <table id="productImageTable" class="item tabContent" style="width: 70%">
        <tr>
            <td colspan="4">
                <a href="javascript:;" id="addProductImage" class="button">${message("admin.goods.add")}</a>
            </td>
        </tr>
        <tr>
            <th>
                ${message("Review.goodsName")}
            </th>
            <th>
                ${message("admin.navigation.productCategory")}
            </th>
            <th>
                ${message("fudai.tioajian.gailv")}
            </th>
            <th>
                ${message("fudai.tioajian.jiange")}
            </th>
            <th>
                ${message("fudai.tioajian.count")}
            </th>
            <th>
                ${message("fudai.tioajian.dajiang")}
            </th>
            <th>
            ${message("admin.common.action")}
            </th>
        </tr>
        [#list fuDaiProductList as fuDaiProduct]
            <tr>
                <td>
                ${fuDaiProduct.product.goods.name}
                    <input type="hidden" value="${fuDaiProduct.id}" name="fudaiProduct[${fuDaiProduct_index}].id"/>
                    <input type="hidden" class="productId" value="${fuDaiProduct.product_id}" name="fudaiProduct[${fuDaiProduct_index}].product_id"/>
                    <input type="hidden" value="${fuDaiProduct.fudai_id}" name="fudaiProduct[${fuDaiProduct_index}].fudai_id" />
                    <input type="hidden" value="0" name="fudaiProduct[${fuDaiProduct_index}].is_main" />
                </td>
                <td>
                ${fuDaiProduct.product.goods.productCategory.name}
                </td>
                <td>
                    <input type="number" value="${fuDaiProduct.probability}" title="${message("fudai.xianzi.gailv")}" name="fudaiProduct[${fuDaiProduct_index}].probability" class="text num"  style="width: 80px;" />
                    [#--<input type="number" value="${fuDaiProduct.probability?string('0.00')}" title=${message("fudai.xianzi.gailv")} name="fudaiProduct[${fuDaiProduct_index}].probability" class="text num"  style="width: 80px;" />--]
                </td>
                <td>
                    <input type="number" value="${fuDaiProduct.repeatTime/60}" name="fudaiProduct[${fuDaiProduct_index}].repeatTime" title="${message("fudai.xianzi.time")}"   class="text sale_num"  style="width: 80px;" />
                </td>
                <td>
                    <input type="number" value="${fuDaiProduct.maxNum}" name="fudaiProduct[${fuDaiProduct_index}].maxNum"  title="${message("fudai.xianzi.max")}"   class="text maxNum"  style="width: 80px;" />
                </td>
                <td>       [#if fuDaiProduct.grandPrix ==1]<span class="red isgrand">${message("fudai.tioajian.is")}</span>
                                [#else]<span class= "isgrand">${message("fudai.tioajian.no")}</span>
                                [/#if]
                </td>
                <td>
                    <a href="javascript:;" class="remove">[${message("admin.common.remove")}]</a>
                    [#if fuDaiProduct.grandPrix ==0]
                        <a class="grandPrix">[${message("fudai.tioajian.isDaJiang")}]</a>
                    [#else]
                        <a class="grandPrix red" >[${message("fudai.tioajian.noDaJiang")}]</a>

                    [/#if]
                    <input type="hidden" name="fudaiProduct[${fuDaiProduct_index}].grandPrix" value="${fuDaiProduct.grandPrix}" class="grandPrixV"/>

                </td>

            </tr>
        [/#list]

    </table>
    <table class="input" id="goodsId">
        <tr>
        [#--<th>--]
        [#--&nbsp;--]
        [#--</th>--]
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