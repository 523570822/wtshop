[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <title>${message("admin.brand.list")} - Powered By ${setting.siteAuthor}</title>
    <meta name="author" content="${setting.siteAuthor}" />
    <meta name="copyright" content="${setting.siteCopyright}" />
    <link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/datePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/list.js"></script>
    <script type="text/javascript">
        $().ready(function() {

	[@flash_message /]
            $('#excelList').click(function () {

                var  titleB=$('#titleB').val();
                var  titleE=$('#titleE').val();
                location.href='getExcel.jhtml?titleB='+titleB+'&titleE='+titleE;

            })



            [#if  brand.type==3&&brand.status!=null]
		// 发货
		$shippingButton.click(function() {
        $.dialog({
            title: "${message("admin.order.shipping")}",
        content:
					[@compress single_line = true]
						'<form id="shippingForm" action="shipping.jhtml" method="post">
                        <input type="hidden" name="token" value="${token}" \/>
                    <input type="hidden" name="orderId" value="${order.id}" \/>
                    <div class="shipping">
                            <table id="shippingLogistics" class="input">
                            <tr>
                            <th>
                        ${message("Order.sn")}:
										<\/th>
                    <td width="300">
                        ${order.sn}
                            <\/td>
                    <th>
                        ${message("admin.common.createDate")}:
										<\/th>
                    <td>
                        ${order.createDate?string("yyyy-MM-dd HH:mm:ss")}
                    <\/td>
                    <\/tr>
                    <tr>
                    <th>
                        ${message("Shipping.shippingMethod")}:
										<\/th>
                    <td>
                    <select name="shippingMethodId">
                            <option value="">${message("admin.common.choose")}<\/option>
                        [#list shippingMethods as shippingMethod]
                            [#noescape]
														<option value="${shippingMethod.id}"[#if shippingMethod == order.shippingMethod] selected="selected"[/#if]>${shippingMethod.name?html?js_string}<\/option>
                            [/#noescape]
                        [/#list]
											<\/select>
                    <\/td>
                    <th>
                        ${message("Shipping.deliveryCorp")}:
										<\/th>
                    <td>
                    <select name="deliveryCorpId">
                            <option value="">${message("admin.common.choose")}<\/option>
                        [#list deliveryCorps as deliveryCorp]
                            [#noescape]
														<option value="${deliveryCorp.id}"[#if order.shippingMethod?? && deliveryCorp == order.shippingMethod.defaultDeliveryCorp] selected="selected"[/#if]>${deliveryCorp.name?html?js_string}<\/option>
                            [/#noescape]
                        [/#list]
											<\/select>
                    <\/td>
                    <\/tr>
                    <tr>
                    <th>
                        ${message("Shipping.trackingNo")}:
										<\/th>
                    <td>
                    <input type="text" name="shipping.tracking_no" class="text" maxlength="200" \/>
                    <\/td>
                    <th>
                        ${message("Shipping.freight")}:
										<\/th>
                    <td>
                    <input type="text" name="shipping.freight" class="text" maxlength="16"  value="[#noescape]${order.fee?html?js_string}[/#noescape]" \/>
                    <\/td>
                    <\/tr>
                    <tr>
                    <th>
                        ${message("Shipping.consignee")}:
										<\/th>
                    <td>
                    <input type="text" name="shipping.consignee" class="text" value="[#noescape]${order.consignee?html?js_string}[/#noescape]" maxlength="200" \/>
                    <\/td>
                    <th>
                        ${message("Shipping.zipCode")}:
										<\/th>
                    <td>
                    <input type="text" name="shipping.zip_code" class="text" value="[#noescape]${order.zipCode?html?js_string}[/#noescape]" maxlength="200" \/>
                    <\/td>
                    <\/tr>
                    <tr>
                    <th>
                        ${message("Shipping.area")}:
										<\/th>
                    <td>
                    <span class="fieldSet">
                            <input type="hidden" id="areaId" name="areaId" value="[#noescape]${order.area.id?html?js_string}[/#noescape]" treePath="${(order.area.treePath)!}" \/>
                    <\/span>
                    <\/td>
                    <th>
                        ${message("Shipping.address")}:
										<\/th>
                    <td>
                    <input type="text" name="shipping.address" class="text" value="[#noescape]${order.address?html?js_string}[/#noescape]" maxlength="200" \/>
                    <\/td>
                    <\/tr>
                    <tr>
                    <th>
                        ${message("Shipping.phone")}:
										<\/th>
                    <td>
                    <input type="text" name="shipping.phone" class="text" value="[#noescape]${order.phone?html?js_string}[/#noescape]" maxlength="200" \/>
                    <\/td>
                    <th>
                        ${message("Shipping.memo")}:
										<\/th>
                    <td>
                    <input type="text" name="shipping.memo" class="text" maxlength="200" \/>
                    <\/td>
                    <\/tr>
                    <\/table>
                    <table class="item">
                            <tr>
                            <th>
                        ${message("ShippingItem.sn")}
                            <\/th>
                    <th>
                        ${message("ShippingItem.name")}
                    <\/th>
                    <th>
                        ${message("ShippingItem.isDelivery")}
                    <\/th>
                    <th>
                        ${message("admin.order.productStock")}
                    <\/th>
                    <th>
                        ${message("admin.order.productQuantity")}
                    <\/th>
                    <th>
                        ${message("admin.order.shippedQuantity")}
                    <\/th>
                    <th>
                        ${message("admin.order.shippingQuantity")}
                    <\/th>
                    <\/tr>
                        [#list order.orderItems as orderItem]
										<tr>
											<td>
												<input type="hidden" name="shippingItems[${orderItem_index}].sn" value="${orderItem.sn}" \/>
                            ${orderItem.sn}
                        <\/td>
                            [#noescape]
												<td width="300">
                                    <span title="${orderItem.name?html?js_string}">${abbreviate(orderItem.name, 50, "...")?html?js_string}<\/span>
                                [#if orderItem.specificationsConverter?has_content]
														<span class="silver">[${orderItem.specificationsConOrder.shippingMethodverter?join(", ")?html?js_string}]<\/span>
                                [/#if]
                                [#if orderItem.typeName != "general"]
														<span class="red">[${message("Goods.Type." + orderItem.typeName)}]<\/span>
                                [/#if]
												<\/td>
                            [/#noescape]
											<td>
                            ${message(orderItem.isDelivery?string('admin.common.true', 'admin.common.false'))}
											<\/td>
                        <td>
                            ${(orderItem.product.stock)!"-"}
                        <\/td>
                        <td>
                            ${orderItem.quantity}
                        <\/td>
                        <td>
                            ${orderItem.shippedQuantity}
                        <\/td>
                        <td>
												[#if orderItem.product?? && orderItem.product.stock < orderItem.shippableQuantity]
                                                    [#assign shippingQuantity = orderItem.product.stock /]
                                                [#else]
                                                    [#assign shippingQuantity = orderItem.shippableQuantity /]
                                                [/#if]
                        <input type="text" name="shippingItems[${orderItem_index}].quantity" class="text shippingItemsQuantity" value="${shippingQuantity}" style="width: 30px;"[#if shippingQuantity <= 0] disabled="disabled"[/#if]  data-is-delivery="${orderItem.isDelivery?string('true', 'false')}" \/>
                        <\/td>
                        <\/tr>
                        [/#list]
								<\/table>
                    <\/div>
                    <\/form>'
                    [/@compress]
                ,
                width: 900,
                    modal: true,
                    ok: "${message("admin.dialog.ok")}",
                    cancel: "${message("admin.dialog.cancel")}",
                    onShow: function() {
                var $shippingForm = $("#shippingForm");
                var $shippingLogistics = $("#shippingLogistics");
                var $shippingItemsQuantity = $("#shippingForm input.shippingItemsQuantity");

                $("#shippingForm input[name='areaId']").lSelect({
                    url: "${base}/common/area.jhtml"
                });

                function checkDelivery() {
                    var isDelivery = false;
                    $shippingItemsQuantity.each(function() {
                        var $this = $(this);
                        if ($this.data("isDelivery") && $this.val() > 0) {
                            isDelivery = true;
                            return false;
                        }
                    });
                    if (isDelivery) {
                        $shippingLogistics.find(":input:not([name='memo'])").prop("disabled", false);
                    } else {
                        $shippingLogistics.find(":input:not([name='memo'])").prop("disabled", true);
                    }
                }

                checkDelivery();

                $shippingItemsQuantity.on("input propertychange change", function(event) {
                    if (event.type != "propertychange" || event.originalEvent.propertyName == "value") {
                        checkDelivery()
                    }
                });

                $.validator.addClassRules({
                    shippingItemsQuantity: {
                        required: true,
                        digits: true
                    }
                });

                $shippingForm.validate({
                    rules: {
                        deliveryCorpId: "required",
                        freight: {
                            min: 0,
                            decimal: {
                                integer: 12,
                                fraction: ${setting.priceScale}
                            }
                        },
                        consignee: "required",
                        "shipping.tracking_no": "required",
                        zipCode: {
                            required: true,
                            pattern: /^\d{6}$/
                        },

                        areaId: "required",
                        address: "required",
                        phone: {
                            required: true,
                            pattern: /^\d{3,4}-?\d{7,9}$/
                        }
                    }
                });
            },
            onOk: function() {
                var total = 0;
                $("#shippingForm input.shippingItemsQuantity").each(function() {
                    var quantity = $(this).val();
                    if ($.isNumeric(quantity)) {
                        total += parseInt(quantity);
                    }
                });

                if (total <= 0) {
                    $.message("warn", "${message("admin.order.shippingQuantityPositive")}");
                } else {
                    $("#shippingForm").submit();
                }
                return false;
            }
        });
        });
            [/#if]

        });
        // 删除
        function disabled(id,status) {

            console.info(status);
            var data={
                id:id,
                status:status
            }
            $.dialog({
                type: "warn",
                content: "确定修改状态",
                ok: message("admin.dialog.ok"),
                cancel: message("admin.dialog.cancel"),
                onOk: function() {
                    $.ajax({
                        url: "disabled.jhtml",
                        type: "POST",
                        data:data ,
                        dataType: "json",
                        cache: false,
                        success: function(message) {

                            console.info("ssss");
                            console.info(message);
                            $.message(message);
                            if (message.type == "success"||message.code*1==1) {
                                //	$checkedIds.closest("tr").remove();

                                setTimeout(function() {
                                    location.reload(true);
                                }, 1000);

                            }
                            //    $deleteButton.addClass("disabled");
                            //   $selectAll.prop("checked", false);
                            //  $checkedIds.prop("checked", false);
                        }
                    });
                }
            });
            return false;
        };
    </script>
</head>
<body>
<div class="breadcrumb">
    <a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; ${message("admin.brand.list")} <span>(${message("admin.page.total", page.totalRow)})</span>
</div>
<form id="listForm" action="list.jhtml" method="post">
    <div class="bar">
        <a href="add.jhtml" class="iconButton">
            <span class="addIcon">&nbsp;</span>${message("admin.common.add")}
        </a>
        <div class="buttonGroup">
            <a href="javascript:;" id="deleteButton" class="iconButton disabled">
                <span class="deleteIcon">&nbsp;</span>${message("admin.common.delete")}
            </a>
            <a href="javascript:;" id="refreshButton" class="iconButton">
                <span class="refreshIcon">&nbsp;</span>${message("admin.common.refresh")}
            </a>
            <div id="pageSizeMenu" class="dropdownMenu">
                <a href="javascript:;" class="button">
					${message("admin.page.pageSize")}<span class="arrow">&nbsp;</span>
                </a>
                <ul>
                    <li[#if pageable.pageSize == 10] class="current"[/#if] val="10">10</li>
                    <li[#if pageable.pageSize == 20] class="current"[/#if] val="20">20</li>
                    <li[#if pageable.pageSize == 50] class="current"[/#if] val="50">50</li>
                    <li[#if pageable.pageSize == 100] class="current"[/#if] val="100">100</li>
                </ul>
            </div>
        </div>
        <div id="searchPropertyMenu" class="dropdownMenu">
            <div class="search">
			[#--<span class="arrow">&nbsp;</span>--]
                <input type="text" id="blurry" name="blurry" value="${blurry}" maxlength="200" />
                <button type="submit">&nbsp;</button>
            </div>
		[#--<ul>
            <li[#if pageable.searchProperty == "phone"] class="current"[/#if] val="code">识别码</li>
            <li[#if pageable.searchProperty == "title"] class="current"[/#if] val="title">批次</li>
            <li[#if pageable.searchProperty == "code"] class="current"[/#if] val="code">识别码</li>

        </ul>--]
        </div>

		${message("admin.memberStatistic.beginDate")}:
        <input type="text" id="beginDate" name="beginDate" class="text Wdate" value="${beginDate?string("yyyy-MM-dd")}" style="width: 120px;" onfocus="WdatePicker({maxDate: '#F{$dp.$D(\'endDate\')}'});" />
		${message("admin.memberStatistic.endDate")}:
        <input type="text" id="endDate" name="endDate" class="text Wdate" value="${endDate?string("yyyy-MM-dd")}" style="width: 120px;" onfocus="WdatePicker({minDate: '#F{$dp.$D(\'beginDate\')}'});" />
        <input type="submit" class="button" value="${message("admin.common.submit")}" />
        <input type="button" value="${message("admin.caiwu.expect")}" class="button" id="excelList" />
    </div>
    <table id="listTable" class="list">
        <tr>
            <th class="check">
                <input type="checkbox" id="selectAll" />
            </th>
		[#--	<th>
                <a href="javascript:;" class="sort" name="name">生产批次</a>
            </th>--]
            <th>
                <a href="javascript:;" class="sort" name="code">识别码</a>
            </th>
            <th>
                <a href="javascript:;" class="sort" name="i.share_code">邀请码</a>
            </th>
            <th>
                <a href="javascript:;" class="sort" name="m.nickname">姓名</a>
            </th>
            <th>
                <a href="javascript:;" class="sort" name="m.phone">电话</a>
            </th>
            <th>
                <a href="javascript:;" class="sort" name="type">所属</a>
            </th>
            <th>
                <a href="javascript:;" class="sort" name="status">状态</a>
            </th>
            <th>
                <a href="javascript:;" class="sort" name="i.total_money">满金额</a>
            </th>
            <th>
                <a href="javascript:;" class="sort" name="i.money">反金额</a>
            </th>
            <th>
                <a href="javascript:;" class="sort" name="i.price">消费金额</a>
            </th>
            <th>
                <a href="javascript:;" class="sort" name="i.start_date">开始时间</a>
            </th>

            <th>
                <a href="javascript:;" class="sort" name="i.end_date">截止时间</a>
            </th>
            <th>
                <a href="javascript:;" class="sort" name="i.complete_date">完成时间</a>
            </th>

            <th>
                <span>${message("admin.common.action")}</span>
            </th>
        </tr>
			[#list page.list as brand]
				<tr>
                    <td>
                        <input type="checkbox" name="ids" value="${brand.id}" />
                    </td>
				[#--<td>
                    ${brand.title}
                </td>--]
                    <td>
						${brand.code}
                    </td>
                    <td>
						${brand.share_code}
                    </td>
                    <td>
						${brand.member.nickname}
                    </td>
                    <td>
						${brand.member.phone}
                    </td>
                    <td>
					[#if brand.type==1||brand.type==null]

                        <span class="red">[门店]</span>



                    [#elseif brand.type==2]

                        <span class="blue">[非门店]</span>


                    [#elseif brand.type==3]

                        <span class="gray">线下</span>

                    [/#if]

                    </td>
                    <td>
                        ${brand.status}
					[#if brand.status==0||brand.status==null]

                        <span class="red">[未使用]</span>



					[#elseif brand.status==3]
                        [#if brand.type==3&&brand.status!=null]
                        <span class="blue">[未申请邮寄]</span>
                        [#else]
                        <span class="blue">[已完成]</span>
                        [/#if]
					[#elseif brand.status==6]

                        <span class="gray">[已邮寄]</span>

					[#elseif brand.status==5]

                        <span class="gray">[现场兑换]</span>

					[#elseif brand.status==1]
                        <span class="green">[已启用]</span>
                    [#elseif brand.status==7]
                        <span class="red">[待邮寄]</span>
					[#elseif brand.status==2]
                        <span class="green">[已失效]</span>
					[/#if]


                    </td>
                    <td>
						${brand.total_money}
                    </td>
                    <td>
						${brand.money}
                    </td>

                    <td>
						${brand.price}
                    </td>
                    <td>
						${brand.start_date}
                    </td>
                    <td>
						${brand.end_date}
                    </td> <td>
					${brand.complete_date}
                </td>

                    <td>
					[#if brand.status==2||brand.status==0||brand.status==1]
					[#-- <a href="javascript:;" class="status" onclick="disabled(${brand.id},3)"">[${message("admin.member.disabled")}]</a>--]
					[#--  <a href="publish.jhtml?id=${brand.id}" class="status" data="${brand.id}">[${message("LoginPlugin.isEnabled")}]</a>--]
					[#else ]

					[/#if]
                        <a href="view.jhtml?id=${brand.id}">[查看]</a>
						[#if brand.status==3&&brand.type==3]

						[#--		<a href="javascript:;" id="deleteButton" class="iconButton disabled">
                                    <span class="deleteIcon">&nbsp;</span>${message("admin.common.delete")}
                                </a>--]


					[#-- <a href="javascript:;" class="status"onclick="disabled(${brand.id},1)"">[${message("LoginPlugin.isEnabled")}]</a>--]


                            </a>
							<a href="javascript:;" class="status" onclick="disabled(${brand.id},5)"  class="iconButton disabled">
                                [现场兑换]
                            </a>

						[#--		 <a href="disabled.jhtml?id=${brand.id}&&status=4"  class="status" data="${brand.id}">[已邮寄]</a>
                                 <a href="disabled.jhtml?id=${brand.id}&&status=5" class="status" data="${brand.id}">[现场兑换]</a>--]

						[#else ]

						[/#if]
                    </td>
                </tr>
			[/#list]
    </table>
		[@pagination pageNumber = page.pageNumber totalPages = page.totalPage]
			[#include "/admin/include/pagination.ftl"]
		[/@pagination]
</form>
</body>
</html>
[/#escape]