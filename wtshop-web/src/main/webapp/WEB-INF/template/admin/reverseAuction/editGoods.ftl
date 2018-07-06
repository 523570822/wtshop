[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <title>${message("admin.goods.add")} - Powered By ${setting.siteAuthor}</title>
    <meta name="author" content="${setting.siteAuthor}"/>
    <meta name="copyright" content="${setting.siteCopyright}"/>
    <link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css"/>
    <link href="${base}/statics/lib/layer/mobile/need/layer.css" rel="stylesheet" type="text/css"/>
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
        $().ready(function () {
            [@flash_message /]
            var flag = $("#flag").val();
            if (flag == 1) {
                alert(flag);
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
            $addProductImage.click(function () {
                layer.open({
                    title: "商品列表",
                    type: 2,
                    skin: 'layui-layer-rim', //加上边框
                    area: ['870px', '540px'], //宽高
//                    ccontent:"/admin/goods/list.jhtml",
                    content: "/admin/reverseAuction/choose.jhtml?flag=0",
                    shadeClose: true,
                });
                $("#show").show();
                productImageIndex++;
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

            // 删除行
            $productImageTable.on("click", "a.remove", function () {
                $(this).closest("tr").remove();
            });

            $.validator.addClassRules({
                auction_start_price: {
                    required: true,
                    number: true,
                    min: 0,
                },
                auction_original_price: {
                    required: true,
                    number: true,
                    min: 0,
                },
                auction_limit_up_price: {
                    required: true,
                    number: true,
                    min: 0,
                },
                auction_limit_down_price: {
                    required: true,
                    number: true,
                    min: 0,
                },
                total_num: {
                    required: true,
                    number: true
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
                submitHandler: function (form) {
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
<form id="inputForm" action="saveReverseAuctionDetail.jhtml" method="post">
    <input type="hidden" id="reverseAuctionId" name="reverseAuctionId" value="${reverseAuction.id}"/>
    <table id="productImageTable" class="item tabContent">
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
            ${message("price.init")}
            </th>
            <th>
            ${message("price.start")}
            </th>
            <th>
            ${message("price.auction_limit_up_price")}
            </th>
            <th>
            ${message("price.auction_limit_down_price")}
            </th>
            <th>
            ${message("Cart.productQuantity")}
            </th>
            <th>
            ${message("admin.common.action")}
            </th>
        </tr>
        [#list reverseAuction.reverseAuctionDetails as detail]
            <tr>
                <td>
                ${detail.product.goods.name}
                    <input type="hidden" value="${detail.id}" name="reverseAuctionDetail[${detail_index}].id"/>
                    <input type="hidden" class="productId" value="${detail.product_id}"
                           name="reverseAuctionDetail[${detail_index}].product_id"/>
                    <input type="hidden" value="${detail.reverse_auction_id}"
                           name="reverseAuctionDetail[${detail_index}].reverse_auction_id"/>
                </td>
                <td>
                    <input type="text" value="${detail.auction_original_price}"
                           name="reverseAuctionDetail[${detail_index}].auction_original_price"
                           class="text auction_original_price" title="商品成本价格（显示用）"
                           style="width:100px" \/>
                </td>
                <td>
                    <input type="text" value="${detail.auction_start_price}"
                           name="reverseAuctionDetail[${detail_index}].auction_start_price"
                           class="text auction_start_price" title="商品起拍价格"
                           style="width:100px" \/>
                </td>
                <td>
                    <input type="text" value="${detail.auction_limit_up_price}"
                           name="reverseAuctionDetail[${detail_index}].auction_limit_up_price"
                           class="text auction_limit_up_price" title="回收价格，当降价到此价格时，小概率回收"
                           style="width:100px" \/>
                </td>
                <td>
                    <input type="text" value="${detail.auction_limit_down_price}"
                           name="reverseAuctionDetail[${detail_index}].auction_limit_down_price"
                           class="text auction_limit_down_price" title="回收价格，当降价到此价格时，大概率回收"
                           style="width:100px" \/>
                </td>
                <td>
                    <input type="text" value="${detail.total_num}"
                           name="reverseAuctionDetail[${detail_index}].total_num" value="1" readonly
                           class="text total_num" maxlength="100" style="width: 50px;" \/>
                </td>
                <td>
                    <a href="javascript:;" class="remove">[${message("admin.common.remove")}]</a>
                </td>

            </tr>
        [/#list]

    </table>
    <table class="input" id="goodsId">
        <tr>
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