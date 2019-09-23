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
            "fullReduction.integral": {
                required:true,
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
		<input type="hidden" name="fullAnti.id" value="${fullAnti.id}" />
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
					<input type="text" name="fullAnti.name" class="text" value="${fullAnti.name}" maxlength="200" />
				</td>
			</tr>

			<tr>
				<th>
					<span class="requiredField">*</span>返给用户内容:
				</th>
				<td>
					<input type="text" name="fullAnti.title" class="text" value="${fullAnti.title}" maxlength="200" />
				</td>
			</tr>
            <tr>
                <th>
                    <span class="requiredField">*</span>请选择:${ fullAnti.type}
                </th>
                <td>
                    <select  name="fullAnti.type" class="text"  >
                        <option value="0"  [#if fullAnti.type==0]selected[/#if]>50元电话卡</option>
                        <option value="1" [#if fullAnti.type==1]selected[/#if]>100元电话卡</option>
                        <option value="2"[#if fullAnti.type==2]selected[/#if]>100元购物卡</option>
                        <option value="3"[#if fullAnti.type==3]selected[/#if]>200元购物卡</option>
                        <option value="4"[#if fullAnti.type==4]selected[/#if]>300元购物卡</option>
                    </select>
                </td>
            </tr>
            <tr>
                <th>
                    <span class="requiredField">*</span>${message("Promotion.total_money")}:
                </th>
                <td>
                    <input type="text" name="fullAnti.total_money" class="text" maxlength="200"  value="${fullAnti.total_money}" />
                </td>
            </tr>



            <tr>
                <th>
					${message("admin.common.order")}:
                </th>
                <td>
                    <input type="text" name="fullAnti.orders"value="${fullAnti.orders}"  class="text" maxlength="9" />
                </td>
            </tr>

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