[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <title>${message("admin.returns.view")} - Powered By ${setting.siteAuthor}</title>
    <meta name="author" content="${setting.siteAuthor}"/>
    <meta name="copyright" content="${setting.siteCopyright}"/>
    <link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.tools.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.lSelect.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/input.js"></script>
    <script type="text/javascript">
        $().ready(function () {
            var $areaId = $("#areaId");
       //     var $inputForm = $("#returns");
            [@flash_message /]

            $areaId.lSelect({
                url: "${base}/common/area.jhtml"
            });
            // 表单验证
      /*      $inputForm.validate({
                rules: {
                    consignee: "required",
                    areaId: "required",
                    address: "required",
                    zipCode: {
                        required: true,
                        pattern: /^\d{6}$/
                    },
                    phone: {
                        required: true,
                        pattern: /^\d{3,4}-?\d{7,9}$/
                    }
                }
            });*/

        });
    </script>
</head>
<body>
<div class="breadcrumb">
    <a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a>
    &raquo; ${message("admin.returns.view")}
</div>
<ul id="tab" class="tab">
    <li>
        <input type="button" value="退换货项"/>
    </li>
    [#if returns.status == 2  ]
        <li>
            <input type="button" value="${message("admin.returns.base")}"/>
        </li>
    [/#if]
</ul>
<table class="item tabContent">
    <tr>
        <th>
            ${message("Member.username")}
        </th>
        <th>
            退换货类型
        </th>
        <th>
            订单状态
        </th>
        <th>
            ${message("ReturnsItem.status")}
        </th>
        <th>
            ${message("ReturnsItem.sn")}
        </th>
        <th>
            ${message("ReturnsItem.name")}
        </th>
        <th>
            ${message("ReturnsItem.quantity")}
        </th>
        <th>
            ${message("ReturnsItem.cause")}
        </th>
        <th>
            ${message("admin.common.createDate")}:
        </th>

    </tr>
    [#list returns.returnsItems as returnsItem]
        <tr>
            <td>
                ${returnsItem.member.username}
            </td>
            <td>
                [#if returns.category == 1 ]
                    <span class="green">换货</span>
                [#elseif returns.category == 2]
                    <span class="red">退货</span>
                [/#if]
            </td>
            <td>
                [#if returns.status == 0 ]
                    <span class="green">未发货</span>
                [#elseif returns.status == 1]
                    <span class="red">已发货(未签收)</span>
                [#elseif returns.status == 2]
                    <span class="red">已发货(已签收)</span>
                [/#if]
            </td>
            <td>
                ${message("ReturnsItem.Status." + returns.typeName)}
            </td>
            <td>
                ${returnsItem.sn}
            </td>
            <td>
                <span title="${returnsItem.name}">${abbreviate(returnsItem.name, 50, "...")}</span>
            </td>
            <td>
                ${returnsItem.quantity}
            </td>
            <td>
                ${returnsItem.cause} ${returnsItem.desc}
            </td>
            <td>
                ${returnsItem.createDate?string("yyyy-MM-dd HH:mm:ss")}
            </td>
        </tr>
    [/#list]

    <tr>
        [#if returns.category == 2]
            <td>
                &nbsp;
            </td>
            <td>
                [#if returns.type == 0 ]
                    <input type="button" class="button" value="审核通过(同意退货)"
                           onclick="location.href='${base}/admin/returns/review.jhtml?id=${returns.id}'"/>
                [#elseif returns.type == 2 && returns.status != 0]
                    <input type="button" class="button" value="已收到寄回商品"
                           onclick="location.href='${base}/admin/returns/returns.jhtml?id=${returns.id}'"/>
                [#elseif (returns.type == 2 || returns.type == 4) && returns.category == 2 ]
                    <input type="button" class="button" value="退款"
                           onclick="location.href='${base}/admin/returns/refund.jhtml?id=${returns.id}'"/>
                [#elseif returns.type == 4 && returns.category == 1]
                    <input type="button" class="button" value="${message("ReturnsItem.Status.handle")}"
                           onclick="location.href='${base}/admin/returns/handle.jhtml?id=${returns.id}'"/>
                [#elseif returns.type == 7 || returns.type  == 5 || returns.type  == 1]
                    <input type="button" class="button" value="${message("ReturnsItem.Status.complete")}"
                           onclick="location.href='${base}/admin/returns/complete.jhtml?id=${returns.id}'"/>
                [#else]
                    <input type="button" class="button" disabled="disabled" value="退货退款完成"/>
                [/#if]
            </td>
            <td>
                [#--<input type="button" class="button" disabled="disabled" value="${message("ReturnsItem.return.review")}"--]
                       [#--onclick="location.href='${base}/admin/returns/review.jhtml?id=${returns.id}'"/>--]
            </td>
        [/#if]

        [#if returns.category == 1]
            <td>

            </td>
            <td>
                [#--<input type="button" class="button" disabled="disabled"--]
                       [#--value="${message("ReturnsItem.returnMoney.review")}"--]
                       [#--onclick="location.href='${base}/admin/returns/review.jhtml?id=${returns.id}'"/>--]
            </td>
            <td>
                [#if returns.type == 0 ]
                    <input type="button" class="button" value="通过申请"
                           onclick="location.href='${base}/admin/returns/review.jhtml?id=${returns.id}'"/>
                [#elseif returns.type == 2 && returns.status != 0]
                    <input type="button" class="button" value="换货商品已收到"
                           onclick="location.href='${base}/admin/returns/returns.jhtml?id=${returns.id}'"/>
                [#elseif (returns.type == 2 || returns.type == 4) && returns.category == 2 ]
                    <input type="button" class="button" value="${message("ReturnsItem.Status.refund")}"
                           onclick="location.href='${base}/admin/returns/refund.jhtml?id=${returns.id}'"/>
                [#elseif returns.type == 4 && returns.category == 1]
                    <input type="button" class="button" value="${message("ReturnsItem.Status.handle")}"
                           onclick="location.href='${base}/admin/returns/handle.jhtml?id=${returns.id}'"/>
                [#elseif returns.type == 7 || returns.type  == 5 || returns.type  == 1]
                    <input type="button" class="button" value="${message("ReturnsItem.Status.complete")}"
                           onclick="location.href='${base}/admin/returns/complete.jhtml?id=${returns.id}'"/>
                [#else]
                    <input type="button" class="button" disabled="disabled" value="换货完成"/>
                [/#if]
            </td>

        [/#if]

        <td>
            <input type="button" class="button" value="${message("admin.common.back")}"
                   onclick="location.href='${base}/admin/returns/list.jhtml'"/>
        </td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
    </tr>
    <tr>
    </tr>
</table>

<form id="returns" action="edit.jhtml" method="post">
    <table class="input tabContent">
        <tr>
            <th>
                ${message("Returns.sn")}:
            </th>
            <td>
                <input type="text" name="sn" class="text" value="${returns.sn}"/>
            </td>
            <th>
                ${message("admin.common.createDate")}:
            </th>
            <td>
                ${returns.createDate?string("yyyy-MM-dd HH:mm:ss")}
            </td>
        </tr>
        <tr>
            <th>
                ${message("Returns.shippingMethod")}:
            </th>
            <td>
                <select name="shippingMethodId">
                    [#list shippingMethods as shippingMethod]
                        <option value="${shippingMethod.id}" [#if returns.shippingMethod == shippingMethod.name]
                                selected="selected"[/#if]>
                            ${shippingMethod.name}
                        </option>
                    [/#list]
                </select>
            </td>

            <th>
                ${message("Returns.deliveryCorp")}:
            </th>
            <td>
                <select name="deliveryCorpId">
                    [#list delivers as deliver]
                        <option value="${deliver.id}" [#if returns.deliveryCorp == deliver.name]
                                selected="selected"[/#if]>
                            ${deliver.name}
                        </option>
                    [/#list]
                </select>
            </td>
        </tr>
        <tr>
            <th>
                ${message("Returns.trackingNo")}:
            </th>
            <td>
                <input type="text" name="trackingNo" class="text" value="${returns.trackingNo}"/>
            </td>
            <th>
                ${message("Returns.freight")}:
            </th>
            <td>
                <input type="text" name="freight" class="text" value="${returns.freight}"/>
            </td>
        </tr>
        <tr>
            <th>
                ${message("Returns.shipper")}:
            </th>
            <td>
                <input type="text" name="shipper" class="text" value="${returns.shipper}"/>
            </td>
            <th>
                ${message("Returns.phone")}:
            </th>
            <td>
                <input type="text" name="phone" class="text" value="${returns.phone}"/>
            </td>
        </tr>
        <tr>
            <th>
                ${message("Returns.area")}:
            </th>

            <td>
					<span class="fieldSet">
						<input type="hidden" id="areaId" name="areaId" value="${(returns.area.id)!}" treePath="${(returns.area.treePath)!}"/>
					</span>
            </td>

            <th>
                ${message("Returns.address")}:
            </th>
            <td>
                <input type="text" name="address" class="text" value="${returns.address}"/>
            </td>
        </tr>


        <tr>
            <th>
                ${message("Returns.zipCode")}:
            </th>
            <td>
                <input type="text" name="zipCode" class="text" value="${returns.zipCode}"/>
            </td>
            <th>
                ${message("Returns.order")}:
            </th>
            <td>
                ${returns.order.sn}
            </td>
        </tr>
        <tr>
            <th>
                ${message("Returns.operator")}:
            </th>
            <td>
                ${returns.operator!"-"}
            </td>
            <th>
                ${message("Returns.memo")}:
            </th>
            <td>
                <input type="text" name="memo" class="text" value="${returns.memo}"/>
            </td>
        </tr>
        <tr>
            <th>
                &nbsp;
            </th>
            <td>
                <input type="submit" class="button" value="${message("admin.common.submit")}"/>
                <input type="button" class="button" value="${message("admin.common.back")}" onclick="history.back(); return false;"/>
            </td>
        </tr>
    </table>
</form>


</body>
</html>
[/#escape]