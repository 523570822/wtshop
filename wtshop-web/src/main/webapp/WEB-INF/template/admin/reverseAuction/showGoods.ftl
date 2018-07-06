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
    </script>
</head>
<body>
<div class="breadcrumb">
    <a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; ${message("Goods.Type.auction")}
</div>
    <table id="productImageTable" class="item tabContent">
        <tr>
            <th>
                ${message("OrderItem.name")}
            </th>
            <th>
                ${message("Payment.sn")}
            </th>
            <th>
                ${message("price.start")}
            </th>
            <th>
                ${message("price.init")}
            </th>
            <th>
                ${message("Cart.productQuantitye")}
            </th>
            <th>
                ${message("reverseGroup.goods.info")}
            </th>
        </tr>
    </table>
    <table class="input" id="goodsId">
        [#list reverseauction.reverseauctionDetails as detail]
            <tr>
                <td>
                    ${detail.product.goods.name}
                </td>
                <td>
                    ${detail.complete_num}
                </td>
                <td>
                    ${detail.auction_price}
                </td>
                <td>
                    ${detail.auction_cost}
                </td>
                <td>
                    ${detail.total_num}
                </td>
                <td>
                <a href="#">${message("reverseGroup.goods.page")}</a>
                </td>
            </tr>
        [/#list]
        <tr>
            <th>
                &nbsp;
            </th>
            <td>
                <input type="button" class="button" value="${message("admin.common.back")}" onclick="history.back(); return false;" />
            </td>
        </tr>
    </table>
</body>
</html>
[/#escape]