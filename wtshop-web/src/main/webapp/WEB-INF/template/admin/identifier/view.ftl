[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>${message("admin.order.view")} - Powered By ${setting.siteAuthor}</title>
<meta name="author" content="${setting.siteAuthor}" />
<meta name="copyright" content="${setting.siteCopyright}" />
<link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.tools.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.lSelect.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/input.js"></script>
<style type="text/css">
	.shipping, .returns {
		height: 380px;
		overflow-x: hidden;
		overflow-y: auto;
	}
	
	.shipping .item, .returns .item {
		margin: 6px 0px;
	}
	
	.transitSteps {
		height: 240px;
		line-height: 28px;
		padding: 0px 6px;
		overflow-x: hidden;
		overflow-y: auto;
	}
	
	.transitSteps th {
		width: 150px;
		color: #888888;
		font-weight: normal;
		text-align: left;
	}
</style>
<script type="text/javascript">
$().ready(function() {

	var $reviewForm = $("#reviewForm");
	var $passed = $("#passed");
	var $receiveForm = $("#receiveForm");
	var $completeForm = $("#completeForm");
	var $failForm = $("#failForm");
	var $reviewButton = $("#reviewButton");
	var $paymentButton = $("#paymentButton");
	var $refundsButton = $("#refundsButton");
	var $shippingButton = $("#shippingButton");
	var $returnsButton = $("#returnsButton");
	var $receiveButton = $("#receiveButton");
	var $completeButton = $("#completeButton");
	var $failButton = $("#failButton");
	var $transitStep = $("a.transitStep");
	var isLocked = false;
	
	[@flash_message /]
	
	// 检查锁定
	function checkLock() {
		if (!isLocked) {
			$.ajax({
				url: "checkLock.jhtml",
				type: "POST",
				data: {id: ${order.id}},
				dataType: "json",
				cache: false,
				success: function(message) {
					if (message.type != "success") {
						$.message(message);
						$reviewButton.add($paymentButton).add($refundsButton).add($shippingButton).add($returnsButton).add($receiveButton).add($completeButton).add($failButton).prop("disabled", true);
						isLocked = true;
					}
				}
			});
		}
	}

    // 检查锁定
    checkLock();
    setInterval(function() {
        checkLock();
    }, 50000);

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
                            物品:
            </th>
                    <td>
						${order.content}
                    </td>
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
					

						$("#shippingForm").submit();

					return false;
				}
			});
		});


	// 物流动态
	$transitStep.click(function() {
		var $this = $(this);
		$.ajax({
			url: "transitStep.jhtml?shippingId=" + $this.attr("shippingId"),
			type: "GET",
			dataType: "json",
			cache: true,
			beforeSend: function() {
				$this.hide().after('<span class="loadingIcon">&nbsp;<\/span>');
			},
			success: function(data) {
				if (data.message.type == "success") {
					if (data.transitSteps.length <= 0) {
						$.message("warn", "${message("admin.order.noResult")}");
						return false;
					}
					var transitStepsHtml = "";
					$.each(data.transitSteps, function(i, transitStep) {
						transitStepsHtml += 
							[@compress single_line = true]
								'<tr>
									<th>' + escapeHtml(transitStep.time) + '<\/th>
									<td>' + escapeHtml(transitStep.context) + '<\/td>
								<\/tr>'
							[/@compress]
						;
					});
					$.dialog({
						title: "${message("admin.order.transitStep")}",
						content: 
							[@compress single_line = true]
								'<div class="transitSteps">
									<table>' + transitStepsHtml + '<\/table>
								<\/div>'
							[/@compress]
						,
						width: 600,
						modal: true,
						ok: null,
						cancel: null
					});
				} else {
					$.message(data.message);
				}
			},
			complete: function() {
				$this.show().next("span.loadingIcon").remove();
			}
		});
		return false;
	});

});
</script>
</head>
<body>
	<form id="reviewForm" action="review.jhtml" method="post">
		<input type="hidden" name="id" value="${order.id}" />
		<input type="hidden" id="passed" name="passed" />
	</form>
	<form id="receiveForm" action="receive.jhtml" method="post">
		<input type="hidden" name="id" value="${order.id}" />
	</form>
	<form id="completeForm" action="complete.jhtml" method="post">
		<input type="hidden" name="id" value="${order.id}" />
	</form>
	<form id="failForm" action="fail.jhtml" method="post">
		<input type="hidden" name="id" value="${order.id}" />
	</form>
	<div class="breadcrumb">
		<a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; ${message("admin.order.view")}
	</div>
	<ul id="tab" class="tab">
		<li>
			<input type="button" value="${message("admin.order.orderInfo")}" />
		</li>

	</ul>
	<table class="input tabContent">
		<tr>
			<td>
				&nbsp;
			</td>
			<td colspan="3">
				[#--<input type="button" id="reviewButton" class="button" value="${message("admin.order.review")}"[#if order.hasExpired() || order.statusName != "pendingReview"] disabled="disabled"[/#if] />--]
				[#--<input type="button" id="paymentButton" class="button" value="${message("admin.order.payment")}" />--]
				[#--<input type="button" id="refundsButton" class="button" value="${message("admin.order.refunds")}"[#if order.refundableAmount <= 0] disabled="disabled"[/#if] />--]
				<input type="button" id="shippingButton" class="button" value="${message("admin.order.shipping")}"[#if order.status !=7] disabled="disabled"[/#if] />
				[#--<input type="button" id="returnsButton" class="button" value="${message("admin.order.returns")}"[#if order.returnableQuantity <= 0] disabled="disabled"[/#if] />--]
				[#--<input type="button" id="receiveButton" class="button" value="${message("admin.order.receive")}"[#if order.hasExpired() || order.statusName != "shipped"] disabled="disabled"[/#if] />--]
				[#--<input type="button" id="completeButton" class="button" value="${message("admin.order.complete")}"[#if order.hasExpired() || (order.statusName != "received" && order.statusName != "noReview" && order.statusName != "review")] disabled="disabled"[/#if] />--]
				[#--<input type="button" id="failButton" class="button" value="${message("admin.order.fail")}"[#if order.hasExpired() || (order.statusName != "pendingShipment" && order.statusName != "shipped" && order.statusName != "received")] disabled="disabled"[/#if] />--]
			</td>
		</tr>
        <tr>
            <th>
				姓名:
            </th>
            <td>
             ${order.member.nickname}
            </td>
            <th>
				物品:
            </th>
            <td>
				${order.content}
            </td>
        </tr>
        <tr>
            <th>
				${message("Order.member")}:
            </th>
            <td>
                <a href="../member/view.jhtml?id=${order.member.id}">${order.member.username}</a>
            </td>
            <th>
				${message("Member.memberRank")}:
            </th>
            <td>
				${order.member.memberRank.name}
            </td>
        </tr>
		<tr>

			<th>
				${message("admin.common.createDate")}:
			</th>
			<td>
				${order.createDate?string("yyyy-MM-dd HH:mm:ss")}
			</td>
            <th>
				完成时间:
            </th>
            <td>
			[#--	${order.completeDate!''}--]
			${(order.completeDate?string("yyyy-MM-dd HH:mm:ss"))!"未完成"}
            </td>
		</tr>
		<tr>
			<th>
				${message("Order.type")}:
			</th>
			<td>
				[#if brand.type==1||brand.type==null]

                    <span class="red">[门店]</span>



				[#elseif brand.type==2]

                        <span class="blue">[非门店]</span>


				[#elseif brand.type==3]

                        <span class="gray">线下</span>

				[/#if]
			</td>
			<th>
				${message("Order.status")}:
			</th>
			<td>
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
		</tr>

		<tr>
			<th>
                满金额：
			</th>
			<td>
				${currency(order.total_money, true)}
			</td>
            <th>
                反金额:
            </th>
            <td>
				${currency(order.money, true)}

            </td>
		</tr>
		<tr>
			<th>
                消费金额:
			</th>
			<td>
				<span class="red">${currency(order.price, true)}</span>
			</td>
            <th>
               运单号:
            </th>
            <td>
                <span class="green">${order.tracking_no}</span>
            </td>
		</tr>



		<tr>
			<th>
				${message("Order.consignee")}:
			</th>
			<td>
				${order.consignee}
			</td>
			<th>
				${message("Order.area")}:
			</th>
			<td>
				${order.areaName}
			</td>
		</tr>
		<tr>
			<th>
				${message("Order.address")}:
			</th>
			<td>
				${order.address}
			</td>
			<th>
				${message("Order.zipCode")}:
			</th>
			<td>
				${order.zipCode}
			</td>
		</tr>
		<tr>
			<th>
				${message("Order.phone")}:
			</th>
			<td>
				${order.phone}
			</td>
			<th>
				${message("Order.memo")}:
			</th>
			<td>
				${order.memo}
			</td>
		</tr>
	</table>

	<table class="input">
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