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
            var $isDefault = $("#isDefault");
            var $productCategoryId = $("#productCategoryId");
            var $filePicker = $("#filePicker");
            var $introduction = $("#introduction");
            var $productImageTable = $("#productImageTable");
            var $addProductImage = $("#addProductImage");
            var $productTable = $("#productTable");
            var productImageIndex = 0;
            var hasSpecification = false;

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
                    content: "/admin/reverseAuction/choose.jhtml?flag=3",
                    shadeClose:true,
                });
                $("#show").show();
                productImageIndex ++;
            });
// 删除行
            $productImageTable.on("click", "a.remove", function() {
                $(this).closest("tr").remove();
            });

            // 笛卡尔积
            function cartesianProductOf(array) {
                function addTo(current, args) {
                    var i, copy;
                    var rest = args.slice(1);
                    var isLast = !rest.length;
                    var result = [];
                    for (i = 0; i < args[0].length; i++) {
                        copy = current.slice();
                        copy.push(args[0][i]);
                        if (isLast) {
                            result.push(copy);
                        } else {
                            result = result.concat(addTo(copy, rest));
                        }
                    }
                    return result;
                }
                return addTo([], array);
            }

            $.validator.addClassRules({
                sale_num: {
                    required: true,
                    number:true
                },
                num: {
                    required: true,
                    digits:true
                },
                parameterGroup: {
                    required: true
                },
                price: {
                    required: true,
                    min: 0,
                    decimal: {
                        integer: 12,
                        fraction: ${setting.priceScale}
                    }
                },
                cost: {
                    min: 0,
                    decimal: {
                        integer: 12,
                        fraction: ${setting.priceScale}
                    }
                },
                marketPrice: {
                    min: 0,
                    decimal: {
                        integer: 12,
                        fraction: ${setting.priceScale}
                    }
                },
                rewardPoint: {
                    digits: true
                },
                exchangePoint: {
                    required: true,
                    digits: true
                },
                stock: {
                    required: true,
                    digits: true
                }
            });

            // 表单验证
            $inputForm.validate({
                rules: {
                    productCategoryId: "required",
                    "goods.sn": {
                        pattern: /^[0-9a-zA-Z_-]+$/,
                        remote: {
                            url: "checkSn.jhtml",
                            cache: false
                        }
                    },
                    "goods.name": "required",
                    "product.price": {
                        required: true,
                        min: 0,
                        decimal: {
                            integer: 12,
                            fraction: ${setting.priceScale}
                        }
                    },
                    "product.cost": {
                        min: 0,
                        decimal: {
                            integer: 12,
                            fraction: ${setting.priceScale}
                        }
                    },
                    "product.market_price": {
                        min: 0,
                        decimal: {
                            integer: 12,
                            fraction: ${setting.priceScale}
                        }
                    },
                    "goods.image": {
                        pattern: /^(http:\/\/|https:\/\/|\/).*$/i
                    },
                    "goods.weight": "digits",
                    "product.reward_point": "digits",
                    "product.exchange_point": {
                        digits: true,
                        required: true
                    },
                    "product.stock": {
                        required: true,
                        digits: true
                    }
                },
                messages: {
                    "goods.sn": {
                        pattern: "${message("admin.validate.illegal")}",
                        remote: "${message("admin.validate.exist")}"
                    }
                },
                submitHandler: function(form) {
                    if (hasSpecification && $productTable.find("input.isEnabled:checked").size() == 0) {
                        $.message("warn", "${message("admin.goods.specificationProductRequired")}");
                        return false;
                    }
                    addCookie("previousProductCategoryId", $productCategoryId.val(), {expires: 24 * 60 * 60});
                    $(form).find("input:submit").prop("disabled", true);
                    form.submit();
                }
            });

        });
    </script>
</head>
<body>
<input type="hidden" id="detailNum" name="detailNum" value="${detailNum}"/>
<div class="breadcrumb">
    <a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; ${message("admin.goods.add")}
</div>
<form id="inputForm" action="saveGoogs.jhtml" method="post">
    <input type="hidden" id="goodsThemeId" name="flashsaleId" value="${flashsaleId}"/>
    <table id="productImageTable" class="item tabContent" style="width: 60%">
        <tr>
            <td colspan="4">
                <a href="javascript:;" id="addProductImage" class="button">添加商品</a>admin.goods.add
            </td>
        </tr>
        <tr>
            <th>
            ${message("Review.goodsName")}
            </th>
            <th>
            ${message("Review.no")}
            </th>
            <th>
            ${message("admin.navigation.productCategory")}
            </th>
            <th>
            ${message("Cart.productQuantity")}
            </th>
            <th>
            ${message("shop.cart.effectivePrice")}
            </th>
            <th>
            ${message("admin.common.action")}
            </th>
        </tr>
        [#list detailList as flashsaleDetail]
            <tr>
                <td>
                ${flashsaleDetail.product.goods.name}
                    <input type="hidden" value="${flashsaleDetail.id}" name="flashsaleDetail[${flashsaleDetail_index}].id"/>
                    <input type="hidden" class="productId" value="${flashsaleDetail.product_id}" name="flashsaleDetail[${flashsaleDetail_index}].product_id"/>
                    <input type="hidden" value="${flashsaleDetail.flashsale_id}" name="flashsaleDetail[${flashsaleDetail_index}].flashsale_id" />
                </td>
                <td>
                    <input type="number" value="${flashsaleDetail.orders}" name="flashsaleDetail[${flashsaleDetail_index}].orders" class="text productImageOrder" maxlength="9" style="width: 80px;" \/>
                </td>
                <td>
                ${flashsaleDetail.product.goods.productCategory.name}
                </td>
                <td>
                    <input type="number" value="${flashsaleDetail.num}" name="flashsaleDetail[${flashsaleDetail_index}].num" class="text num" maxlength="9" style="width: 80px;" \/>
                </td>
                <td>
                    <input type="number" value="${flashsaleDetail.sale_num}" name="flashsaleDetail[${flashsaleDetail_index}].sale_num" class="text sale_num" maxlength="9" style="width: 80px;" \/>
                </td>
                <td>
                    <a href="javascript:;" class="remove">[${message("admin.common.remove")}]</a>
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