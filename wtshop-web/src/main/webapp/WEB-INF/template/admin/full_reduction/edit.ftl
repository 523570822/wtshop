[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>${message("admin.promotion.edit")} - Powered By ${setting.siteAuthor}</title>
<meta name="author" content="${setting.siteAuthor}" />
<meta name="copyright" content="${setting.siteCopyright}" />
<link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.tools.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.autocomplete.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/webuploader.js"></script>
<script type="text/javascript" src="${base}/resources/admin/ueditor/ueditor.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/input.js"></script>
<script type="text/javascript" src="${base}/resources/admin/datePicker/WdatePicker.js"></script>
<style type="text/css">
.memberRank label, .coupon label {
	min-width: 120px;
	_width: 120px;
	display: block;
	float: left;
	padding-right: 4px;
	_white-space: nowrap;
}
</style>
<script type="text/javascript">
$().ready(function() {

	var $inputForm = $("#inputForm");
	var $filePicker = $("#filePicker");
	var $giftSelect = $("#giftSelect");
	var $giftTable = $("#giftTable");
	var $giftTitle = $("#giftTable tr:first");
	var $introduction = $("#introduction");
	
	[@flash_message /]
	
	$filePicker.uploader();
	
	$introduction.editor();
	
	// 赠品选择
	$giftSelect.autocomplete("giftSelect.jhtml", {
		dataType: "json",
		extraParams: {
			excludeIds: function() {
				return $giftTable.find("input:hidden").map(function() {
					return $(this).val();
				}).get();
			}
		},
		cacheLength: 0,
		max: 20,
		width: 218,
		scrollHeight: 300,
		parse: function(data) {
			return $.map(data, function(item) {
				return {
					data: item,
					value: item.name
				}
			});
		},
		formatItem: function(item) {
			return '<span title="' + escapeHtml(item.name) + '">' + escapeHtml(abbreviate(item.name, 50, "...")) + '<\/span>' + (item.specifications.length > 0 ? ' <span class="silver">[' + escapeHtml(item.specifications.join(", ")) + ']<\/span>' : '');
		}
	}).result(function(event, item) {
		var $giftTr = (
			[@compress single_line = true]
				'<tr>
					<td>
						<input type="hidden" name="giftIds" value="' + item.id + '" \/>
						' + item.sn + '
					<\/td>
					<td>
						<span title="' + escapeHtml(item.name) + '">' + escapeHtml(abbreviate(item.name, 50, "...")) + '<\/span>' + (item.specifications.length > 0 ? ' <span class="silver">[' + escapeHtml(item.specifications.join(", ")) + ']<\/span>' : '') + '
					<\/td>
					<td>
						<a href="' + escapeHtml(item.url) + '" target="_blank">[${message("admin.common.view")}]<\/a>
						<a href="javascript:;" class="remove">[${message("admin.common.remove")}]<\/a>
					<\/td>
				<\/tr>'
			[/@compress]
		);
		$giftTitle.show();
		$giftTable.append($giftTr);
	});
	
	// 删除赠品
	$giftTable.on("click", "a.remove", function() {
		var $this = $(this);
		$.dialog({
			type: "warn",
			content: "${message("admin.dialog.deleteConfirm")}",
			onOk: function() {
				$this.closest("tr").remove();
				if ($giftTable.find("tr").size() <= 1) {
					$giftTitle.hide();
				}
				$giftSelect.val("");
			}
		});
	});
	
	$.validator.addMethod("compare", 
		function(value, element, param) {
			var parameterValue = $(param).val();
			if ($.trim(parameterValue) == "" || $.trim(value) == "") {
				return true;
			}
			try {
				return parseFloat(parameterValue) <= parseFloat(value);
			} catch(e) {
				return false;
			}
		},
		"${message("admin.promotion.compare")}"
	);
	
	// 表单验证
	$inputForm.validate({
		rules: {
			"fullReduction.name": "required",
			"fullReduction.title": "required",
			"fullReduction.image": {
				pattern: /^(http:\/\/|https:\/\/|\/).*$/i
			},
			"fullReduction.total_money": {
				min: 0,
				decimal: {
					integer: 12,
					fraction: ${setting.priceScale}
				}
			},
			"fullReduction.money": {
				min: 0,
				decimal: {
					integer: 12,
					fraction: ${setting.priceScale}
				},
				compare: "#minimumPrice"
			},
			"fullReduction.minimum_quantity": "digits",
			"fullReduction.maximum_quantity": {
				digits: true,
				compare: "#minimumQuantity"
			},
			"fullReduction.price_expression": {
				remote: {
					url: "checkPriceExpression.jhtml",
					cache: false
				}
			},
			"fullReduction.point_expression": {
				remote: {
					url: "checkPointExpression.jhtml",
					cache: false
				}
			},
			"fullReduction.orders": "digits"
		}
	});

});
</script>
</head>
<body>
	<div class="breadcrumb">
		<a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; ${message("admin.promotion.edit")}
	</div>
	<form id="inputForm" action="update.jhtml" method="post">
		<input type="hidden" name="fullReduction.id" value="${fullReduction.id}" />
		<ul id="tab" class="tab">
			<li>
				<input type="button" value="${message("admin.promotion.base")}" />
			</li>
		[#--	<li>
				<input type="button" value="${message("Promotion.introduction")}" />
			</li>--]
		</ul>
		<table class="input tabContent">
			<tr>
				<th>
					<span class="requiredField">*</span>${message("Promotion.name")}:
				</th>
				<td>
					<input type="text" name="fullReduction.name" class="text" value="${fullReduction.name}" maxlength="200" />
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>${message("Promotion.title")}:
				</th>
				<td>
					<input type="text" name="fullReduction.title" class="text" value="${fullReduction.title}" maxlength="200" />
				</td>
			</tr>
            <tr>
                <th>
                    <span class="requiredField">*</span>${message("Promotion.total_money")}:
                </th>
                <td>
                    <input type="text" name="fullReduction.total_money" class="text" maxlength="200"  value="${fullReduction.total_money}" />
                </td>
            </tr>

            <tr>
                <th>
                    <span class="requiredField">*</span>${message("Promotion.money")}:
                </th>
                <td>
                    <input type="text" name="fullReduction.money" class="text" maxlength="200" value="${fullReduction.money}"/>
                </td>
            </tr>
			[#--<tr>--]
				[#--<th>--]
					[#--${message("Promotion.minimumPrice")}:--]
				[#--</th>--]
				[#--<td>--]
					[#--<input type="text" id="minimumPrice" name="promotion.minimum_price" class="text" value="${promotion.minimumPrice}" maxlength="16" />--]
				[#--</td>--]
			[#--</tr>--]
			[#--<tr>--]
				[#--<th>--]
					[#--${message("Promotion.maximumPrice")}:--]
				[#--</th>--]
				[#--<td>--]
					[#--<input type="text" name="promotion.maximum_price" class="text" value="${promotion.maximumPrice}" maxlength="16" />--]
				[#--</td>--]
			[#--</tr>--]
			[#--<tr>--]
				[#--<th>--]
					[#--${message("Promotion.minimumQuantity")}:--]
				[#--</th>--]
				[#--<td>--]
					[#--<input type="text" id="minimumQuantity" name="promotion.minimum_quantity" class="text" value="${promotion.minimumQuantity}" maxlength="9" />--]
				[#--</td>--]
			[#--</tr>--]
			[#--<tr>--]
				[#--<th>--]
					[#--${message("Promotion.maximumQuantity")}:--]
				[#--</th>--]
				[#--<td>--]
					[#--<input type="text" name="promotion.maximum_quantity" class="text" value="${promotion.maximumQuantity}" maxlength="9" />--]
				[#--</td>--]
			[#--</tr>--]
			[#--<tr>--]
				[#--<th>--]
					[#--${message("Promotion.priceExpression")}:--]
				[#--</th>--]
				[#--<td>--]
					[#--<input type="text" name="promotion.price_expression" class="text" value="${promotion.priceExpression}" maxlength="255" title="${message("admin.promotion.priceExpressionTitle")}" />--]
				[#--</td>--]
			[#--</tr>--]
			[#--<tr>--]
				[#--<th>--]
					[#--${message("Promotion.pointExpression")}:--]
				[#--</th>--]
				[#--<td>--]
					[#--<input type="text" name="promotion.point_expression" class="text" value="${promotion.pointExpression}" maxlength="255" title="${message("admin.promotion.pointExpressionTitle")}" />--]
				[#--</td>--]
			[#--</tr>--]
			[#--<tr>--]
				[#--<th>--]
					[#--${message("admin.common.order")}:--]
				[#--</th>--]
				[#--<td>--]
					[#--<input type="text" name="promotion.orders" class="text" value="${promotion.orders}" maxlength="9" />--]
				[#--</td>--]
			[#--</tr>--]
			[#--<tr class="memberRank">--]
				[#--<th>--]
					[#--${message("Promotion.memberRanks")}--]
				[#--</th>--]
				[#--<td>--]
					[#--[#list memberRanks as memberRank]--]
						[#--<label>--]
							[#--<input type="checkbox" name="memberRankIds" value="${memberRank.id}"[#if promotion.memberRanks?seq_contains(memberRank)] checked="checked"[/#if] />${memberRank.name}--]
						[#--</label>--]
					[#--[/#list]--]
				[#--</td>--]
			[#--</tr>--]
			[#--<tr class="coupon">--]
				[#--<th>--]
					[#--${message("Promotion.coupons")}--]
				[#--</th>--]
				[#--<td>--]
					[#--[#list coupons as coupon]--]
						[#--<label>--]
							[#--<input type="checkbox" name="couponIds" value="${coupon.id}"[#if promotion.coupons?seq_contains(coupon)] checked="checked"[/#if] />${coupon.name}--]
						[#--</label>--]
					[#--[/#list]--]
				[#--</td>--]
			[#--</tr>--]
			[#--<tr>--]
				[#--<th>--]
					[#--${message("admin.common.setting")}:--]
				[#--</th>--]
				[#--<td>--]
					[#--<label>--]
						[#--<input type="checkbox" name="isFreeShipping" value="true"[#if promotion.isFreeShipping] checked="checked"[/#if] />${message("Promotion.isFreeShipping")}--]
						[#--<input type="hidden" name="_isFreeShipping" value="false" />--]
					[#--</label>--]
					[#--<label>--]
						[#--<input type="checkbox" name="isCouponAllowed" value="true"[#if promotion.isCouponAllowed] checked="checked"[/#if] />${message("Promotion.isCouponAllowed")}--]
						[#--<input type="hidden" name="_isCouponAllowed" value="false" />--]
					[#--</label>--]
				[#--</td>--]
			[#--</tr>--]
			[#--<tr>--]
				[#--<th>--]
					[#--${message("Promotion.gifts")}:--]
				[#--</th>--]
				[#--<td>--]
					[#--<input type="text" id="giftSelect" name="giftSelect" class="text" maxlength="200" title="${message("admin.promotion.giftSelectTitle")}" />--]
				[#--</td>--]
			[#--</tr>--]
			[#--<tr>--]
				[#--<th>--]
					[#--&nbsp;--]
				[#--</th>--]
				[#--<td>--]
					[#--<table id="giftTable" class="item">--]
						[#--<tr[#if !promotion.gifts?has_content] class="hidden"[/#if]>--]
							[#--<th>--]
								[#--${message("Product.sn")}--]
							[#--</th>--]
							[#--<th>--]
								[#--${message("Product.name")}--]
							[#--</th>--]
							[#--<th>--]
								[#--${message("admin.common.action")}--]
							[#--</th>--]
						[#--</tr>--]
						[#--[#list promotion.gifts as gift]--]
							[#--<tr>--]
								[#--<td>--]
									[#--<input type="hidden" name="giftIds" value="${gift.id}" />--]
									[#--${gift.sn}--]
								[#--</td>--]
								[#--<td>--]
									[#--<span title="${gift.name}">${abbreviate(gift.name, 50, "...")}</span>--]
									[#--[#if gift.specifications?has_content]--]
										[#--<span class="silver">[${gift.specifications?join(", ")}]</span>--]
									[#--[/#if]--]
								[#--</td>--]
								[#--<td>--]
									[#--<a href="${gift.url}" target="_blank">[${message("admin.common.view")}]</a>--]
									[#--<a href="javascript:;" class="remove">[${message("admin.common.remove")}]</a>--]
								[#--</td>--]
							[#--</tr>--]
						[#--[/#list]--]
					[#--</table>--]
				[#--</td>--]
			[#--</tr>--]
		[#--</table>--]
		[#--<table class="input tabContent">--]
			[#--<tr>--]
				[#--<td>--]
					[#--<textarea id="introduction" name="introduction" class="editor" style="width: 100%;">${promotion.introduction}</textarea>--]
				[#--</td>--]
			[#--</tr>--]
		[#--</table>--]
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
</html>
[/#escape]