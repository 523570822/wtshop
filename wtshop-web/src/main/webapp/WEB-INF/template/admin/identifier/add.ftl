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
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.tools.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.validate.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.lSelect.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/webuploader.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/ueditor/ueditor.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/input.js"></script>
    <style type="text/css">
        .parameterTable table th {
            width: 146px;
        }

        .specificationTable span {
            padding: 10px;
        }

    </style>
    <script type="text/javascript">
        $().ready(function() {
            var $inputForm = $("#inputForm");
	[@flash_message /]



            $.validator.addClassRules({
                productImageFile: {
                    required: true,
                    extension: "${setting.uploadImageExtension}"
                },
                productImageOrder: {
                    digits: true
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
                    "areaId": "required",
                    "num": {
                        required: true,
                        min: 0,
                        decimal: {
                            integer: 12,
                            fraction:0
                        }
                    },
                    "product.cost": {
                        required: true,
                        min: 0,
                        decimal: {
                            integer: 12,
                            fraction: ${setting.priceScale}
                        }
                    },
                    "product.commissionRate": {
                        required: true,
                        min: 0,
                        decimal: {
                            integer: 12,
                            fraction: ${setting.priceScale}
                        }
                    },
                    "product.market_price": {
                        min: 0,
                        required: true,
                        decimal: {
                            integer: 12,
                            fraction: ${setting.priceScale}
                        }
                    },
                    "goods.image": {
                        pattern: /^(http:\/\/|https:\/\/|\/).*$/i
                    },
                    "goods.weight": {
                        digits: true,
                        required: true
                    },
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
                    $(form).find("input:submit").prop("disabled", true);
                    form.submit();
                }
            });

        });
    </script>
</head>
<body>
<div class="breadcrumb">
    <a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; ${message("admin.goods.add")}
</div>
<form id="inputForm" action="save.jhtml" method="post" >
    <input type="hidden" id="isDefault" name="product.is_default" value="true" />
    <ul id="tab" class="tab">
        <li>
            <input type="button" value="${message("admin.goods.base")}" />
        </li>

    </ul>
    <table class="input tabContent">
        <tr>
            <th>
                生成识别码数
            </th>
            <td>
                <input type="text" name="number" id="number" class="text"  maxlength="200" />
            </td>
        </tr>
    </table>
    <table class="input">
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
</body>
</html>
[/#escape]