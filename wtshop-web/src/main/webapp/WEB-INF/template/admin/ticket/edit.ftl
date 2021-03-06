[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>${message("admin.coupon.edit")} - Powered By ${setting.siteAuthor}</title>
<meta name="author" content="${setting.siteAuthor}" />
<meta name="copyright" content="${setting.siteCopyright}" />
<link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.tools.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/admin/ueditor/ueditor.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/input.js"></script>
<script type="text/javascript" src="${base}/statics/lib/layer/layer.js"></script>
<script type="text/javascript" src="${base}/resources/admin/datePicker/WdatePicker.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/webuploader.js"></script>
<script type="text/javascript">
$().ready(function() {

	var $inputForm = $("#inputForm");
	var $isExchange = $("#isExchange");
	var $point = $("#point");
	var $introduction = $("#introduction");
    var $filePicker = $("#filePicker");
    var $addProductImage = $("#addProductImage");

    $filePicker.uploader();
	[@flash_message /]
	
	$introduction.editor();
	
	// 是否允许积分兑换
	$isExchange.click(function() {
		if ($(this).prop("checked")) {
			$point.prop("disabled", false);
		} else {
			$point.val("").prop("disabled", true);
		}
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
		"${message("admin.coupon.compare")}"
	);
	
	// 表单验证
	$inputForm.validate({
        rules: {
            "ticket.name": "required",
            "ticket.money": "required",
            "ticket.condition": "required",
        }
	});


//    $("#money").change(function () {
//        var money = $(this).val();
//        if(money != null && money!=""){
//            $("#modulus").attr("disabled","disabled");
//        }else {
//            $("#modulus").removeAttr("disabled");
//        }
//    });
//
//    $("#modulus").change(function () {
//        var modulus = $(this).val();
//        if(modulus != null && modulus!=""){
//            $("#money").attr("disabled","disabled");
//        }else {
//            $("#money").removeAttr("disabled");
//        }
//    });

});
</script>
</head>
<body>
	<div class="breadcrumb">
		<a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; ${message("admin.coupon.edit")}
	</div>
	<form id="inputForm" action="update.jhtml" method="post">
        <input class="hidden" name="ticket.id"  value="${coupon.id}"  />
        <input class="hidden" name="ticket.config_id"  value="${coupon.config_id}"  />
		<ul id="tab" class="tab">
			<li>
				<input type="button" value="${message("admin.coupon.base")}" />
			</li>
			<li>
				<input type="button" value="${message("Coupon.introduction")}" />
			</li>
		</ul>
		<div class="tabContent">
			<table class="input">
                <tr>
                    <th>
                        <span class="requiredField">*</span>${message("ticket.ticketName")}:
                    </th>
                    <td>
                        <input type="text" name="ticket.name" class="text" maxlength="200" value="${coupon.name}" />
                    </td>
                </tr>
                <tr>
                    <th>
                        <span class="requiredField">*</span>${message("mjje")}:
                    </th>
                    <td>
                        <input type="text" id="money" name="ticket.money" class="text"  value="${coupon.money}" maxlength="200" title="${message("ticket.tip1")}"/>
                    </td>
                </tr>
                <tr>
                    <th>
                        <span class="requiredField">*</span>${"ticket.coefficient"}:
                    </th>
                    <td>
                        <input type="text" id="modulus" name="ticket.modulus" class="text"  value="${coupon.modulus}" maxlength="200" title="${message("ticket.tip2")}"/>
                    </td>
                </tr>
                <tr>
                    <th>
                        <span class="requiredField">*</span>${message("ticket.sqzdxf")}:
                    </th>
                    <td>
                        <input type="text" name="ticket.condition" class="text"  value="${coupon.condition}" maxlength="200" title="${message}"/>
                    </td>
                </tr>
                <tr >
                    <th>
                        <span class="requiredField">*</span>${message("ticket.numLimit")}:
                    </th>
                    <td>
                        <input type="text" name="ticket.count" class="text" value="${coupon.count}" maxlength="200" title="${message("ticket.upperLimit=")}"/>
                    </td>
                </tr>
				<tr>
					<th>
						<span class="requiredField"></span>${message("Coupon.prefix")}:
					</th>
					<td>
						<input type="text" name="ticket.prefix" class="text" value="${coupon.prefix}" maxlength="200" />
					</td>
				</tr>
                <tr>
                    <th>
                        <span class="requiredField"></span>${message("ticket.preferentialClass")}:
                    </th>
                    <td>
                        <select id="productCategoryId" name="ticket.product_category_id">
                            <option value="" selected="selected">${message("ticket.preferentialClass")}</option>
							[#list productCategoryTree as productCategory]
                                <option value="${productCategory.id}"[#if productCategory == coupon.productCategory] selected="selected"[/#if]>
									[#if productCategory.grade != 0]
										[#list 1..productCategory.grade as i]
                                            &nbsp;&nbsp;
										[/#list]
									[/#if]
								${productCategory.name}
                                </option>
							[/#list]
                        </select>
                        <span class="requiredField">${message("ticket.beCareful")}</span>
                    </td>
                </tr>
                <tr>
                    <th>
                        <span class="requiredField"></span>${message("ticket.discountGoods")}:
                    </th>
                    <td colspan="4">
                        <input type="hidden" id ="product_id" name="ticket.product_id"/>
                        <input type="text" id="product_name" value="${ticket.product.goods.name}"  class="text" maxlength="200" />
                        <a href="javascript:;" id="addProductImage"  class="button">${message("ticket.addGoods")} </a>
                    </td>
                </tr>
                <tr>
                    <th>
                        ${message("ticket.discountPictures")}:
                    </th>
                    <td>
					<span class="fieldSet">
						<input type="text" name="ticket.image" class="text"  value="${coupon.image}"  maxlength="200" />
						<a href="javascript:;" id="filePicker" class="button">${message("admin.upload.filePicker")}</a>
					</span>
                    </td>
                </tr>
				<tr>
					<th>
                        <span class="requiredField">*</span>${message("Coupon.beginDate")}:
					</th>
					<td>
						<input type="text" id="beginDate" name="ticket.begin_date" class="text Wdate" value="[#if coupon.beginDate??]${coupon.beginDate?string("yyyy-MM-dd HH:mm:ss")}[/#if]" onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss', maxDate: '#F{$dp.$D(\'endDate\')}'});" />
					</td>
				</tr>
				<tr>
					<th>
                        <span class="requiredField">*</span>${message("Coupon.endDate")}:
					</th>
					<td>
						<input type="text" id="endDate" name="ticket.end_date" class="text Wdate" value="[#if coupon.endDate??]${coupon.endDate?string("yyyy-MM-dd HH:mm:ss")}[/#if]" onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss', minDate: '#F{$dp.$D(\'beginDate\')}'});" />
					</td>
				</tr>
                <tr>
                    <th>
                        <span class=""></span>${message("ticket.effectiveDays")}:
                    </th>
                    <td>
                        <input type="text" class="text" name="ticket.effectiveDay"  value="${coupon.effectiveDay}"/>
                        <span class="requiredField">${message("ticket.note")}</span>
                    </td>
                </tr>
				[#--<tr>--]
					[#--<th>--]
						[#--${message("Coupon.minimumPrice")}:--]
					[#--</th>--]
					[#--<td colspan="2">--]
						[#--<input type="text" id="minimumPrice" name="coupon.minimum_price" class="text" value="${coupon.minimumPrice}" maxlength="16" />--]
					[#--</td>--]
				[#--</tr>--]
				[#--<tr>--]
					[#--<th>--]
						[#--${message("Coupon.maximumPrice")}:--]
					[#--</th>--]
					[#--<td colspan="2">--]
						[#--<input type="text" name="coupon.maximum_price" class="text" value="${coupon.maximumPrice}" maxlength="16" />--]
					[#--</td>--]
				[#--</tr>--]
				[#--<tr>--]
					[#--<th>--]
						[#--${message("Coupon.minimumQuantity")}:--]
					[#--</th>--]
					[#--<td colspan="2">--]
						[#--<input type="text" id="minimumQuantity" name="coupon.minimum_quantity" class="text" value="${coupon.minimumQuantity}" maxlength="9" />--]
					[#--</td>--]
				[#--</tr>--]
				[#--<tr>--]
					[#--<th>--]
						[#--${message("Coupon.maximumQuantity")}:--]
					[#--</th>--]
					[#--<td colspan="2">--]
						[#--<input type="text" name="coupon.maximum_quantity" class="text" value="${coupon.maximumQuantity}" maxlength="9" />--]
					[#--</td>--]
				[#--</tr>--]
				[#--<tr>--]
					[#--<th>--]
						[#--${message("Coupon.priceExpression")}:--]
					[#--</th>--]
					[#--<td colspan="2">--]
						[#--<input type="text" name="coupon.price_expression" class="text" value="${coupon.priceExpression}" maxlength="255" title="${message("admin.coupon.priceExpressionTitle")}" />--]
					[#--</td>--]
				[#--</tr>--]
				<tr class="hidden">
					<th >
						${message("admin.common.setting")}:
					</th>
					<td  >
						<label>
							<input type="checkbox" name="isEnabled" value="true"[#if coupon.isEnabled] checked="checked"[/#if] />${message("Coupon.isEnabled")}
							<input type="hidden" name="_isEnabled" value="false" />
						</label>
						<label>
							<input type="checkbox" id="isExchange" name="isExchange" value="true"[#if coupon.isExchange] checked="checked"[/#if] />${message("Coupon.isExchange")}
							<input type="hidden" name="_isExchange" value="false" />
						</label>
					</td>
				</tr>
				<tr class="hidden">
					<th>
						<span class="requiredField">*</span>${message("Coupon.point")}:
					</th>
					<td>
						<input type="text" id="point" name="ticket.point" class="text" value="${coupon.point}" maxlength="9"[#if !coupon.isExchange] disabled="disabled"[/#if] />
					</td>
				</tr>
			</table>
		</div>
		<div class="tabContent">
			<table class="input">
				<tr>
					<td>
						<textarea id="introduction" name="ticket.introduction" class="editor" style="width: 100%;">${coupon.introduction}</textarea>
					</td>
				</tr>
			</table>
		</div>
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