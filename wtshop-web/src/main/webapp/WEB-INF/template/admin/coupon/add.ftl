[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>${message("admin.coupon.add")} - Powered By ${setting.siteAuthor}</title>
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
<script type="text/javascript" src="${base}/resources/admin/datePicker/WdatePicker.js"></script>
<script type="text/javascript">
$().ready(function() {

	var $inputForm = $("#inputForm");
	var $isExchange = $("#isExchange");
	var $point = $("#point");
	var $introduction = $("#introduction");
    var $filePicker = $("#filePicker");
    var $addProductImage = $("#addProductImage");
	
	[@flash_message /]
    $filePicker.uploader();
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
			"coupon.name": "required",
			"coupon.prefix": "required",
            "coupon.money": "required",
            "coupon.condition": "required",
            "coupon.begin_date": "required",
            "coupon.end_date": "required",
            "coupon.count": "required",
            "coupon.modulus": {
                required: true,
                pattern: /^(\d(\.\d)?|10)$/i
            },
			"coupon.minimum_price": {
				min: 0,
				decimal: {
					integer: 12,
					fraction: ${setting.priceScale}
				}
			},
			"coupon.maximum_price": {
				min: 0,
				decimal: {
					integer: 12,
					fraction: ${setting.priceScale}
				},
				compare: "#minimumPrice"
			},
			"coupon.minimum_quantity": "digits",
			"coupon.maximum_quantity": {
				digits: true,
				compare: "#minimumQuantity"
			},
			"coupon.price_expression": {
				remote: {
					url: "checkPriceExpression.jhtml",
					cache: false
				}
			},
			"coupon.point": {
				required: true,
				digits: true
			}
		}
	});
	//默认选中分类
    $("#productCategory").attr("checked", "true");

	$("#productCategory").click(function () {
	    $("#product_name").attr("disabled","disabled");
        $("#product_name").attr("value","");
        $("#product_id").attr("value","");
        $addProductImage.attr("name","product");
        $("#productCategoryId").removeAttr("disabled");
    });

    $("#product").click(function () {
        $("#productCategoryId").attr("value","");
        $("#productCategoryId").attr("disabled","disabled");
        $addProductImage.removeAttr("name");
        $("#product_name").removeAttr("disabled");
    });




    //选择商品
    $addProductImage.click(function() {
        var name = $(this).attr("name");
        if(name == "product" ){
            return false;
		}
        layer.open({
            title:"商品列表",
            type: 2,
            skin: 'layui-layer-rim', //加上边框
            area: ['870px', '540px'], //宽高
            content: "/admin/reverseAuction/choose.jhtml?flag=5",
            shadeClose:true,
        });

    });


});
</script>
</head>
<body>
	<div class="breadcrumb">
		<a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; ${message("admin.coupon.add")}
	</div>
	<form id="inputForm" action="save.jhtml" method="post">
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
						<span class="requiredField">*</span>${message("Coupon.name")}:
					</th>
					<td>
						<input type="text" name="coupon.name" class="text" maxlength="200" />
					</td>
				</tr>
                <tr>
                    <th>
                        <span class="requiredField">*</span>满减金额:
                    </th>
                    <td>
                        <input type="text" id="money" name="coupon.money" class="text" maxlength="200" title="满减金额,如需打折券请输入0"/>
                    </td>
                </tr>
                <tr>
                    <th>
                        <span class="requiredField">*</span>优惠系数:
                    </th>
                    <td>
                        <input type="text" id="modulus" name="coupon.modulus" class="text" maxlength="200" title="打折券系数,请在0~10内输入,如需满减券请输入10"/>
                    </td>
                </tr>
                <tr>
                    <th>
                        <span class="requiredField">*</span>使用条件:
                    </th>
                    <td>
                        <input type="text" name="coupon.condition" class="text" maxlength="200" title="使用优惠券的条件金额"/>
                    </td>
                </tr>
                <tr>
                    <th>
                        <span class="requiredField">*</span>发放数量:
                    </th>
                    <td>
                        <input type="text" name="coupon.count" class="text" maxlength="200" title="优惠券发放上限"/>
                    </td>
                </tr>
                <tr>
                    <th>
                        <span class="requiredField">*</span>${message("Coupon.prefix")}:
                    </th>
                    <td>
                        <input type="text" name="coupon.prefix" class="text" maxlength="200" />
                    </td>
                </tr>
                <tr>
                    <th>
                        <span class="requiredField"></span>优惠分类:
                    </th>
                    <td>

						<select id="productCategoryId" name="coupon.product_category_id" >
							<option value="" selected="selected">请选择</option>
							[#list productCategoryTree as productCategory]
								<option value="${productCategory.id}">
									[#if productCategory.grade != 0]
										[#list 1..productCategory.grade as i]
											&nbsp;&nbsp;
										[/#list]
									[/#if]
								${productCategory.name}
								</option>
							[/#list]
						</select>
                        <input type="radio" id="productCategory" name="select" />
                    </td>
                </tr>
                <tr>
                    <th>
                        <span class="requiredField"></span>优惠商品:
                    </th>
                    <td colspan="4">
						<input type="hidden" id ="product_id"  name="coupon.product_id"/>
                        <input type="text" id="product_name" class="text" maxlength="200" disabled="disabled"/>
                        <a href="javascript:;" id="addProductImage" class="button" name="product" >选择商品 </a>
                        <input type="radio" id="product" name="select"/>
                    </td>
                </tr>
                <tr>
                    <th>
						优惠商品图片:
                    </th>
                    <td>
					<span class="fieldSet">
						<input type="text" name="coupon.image" class="text" maxlength="200" />
						<a href="javascript:;" id="filePicker" class="button">${message("admin.upload.filePicker")}</a>
					</span>
                    </td>
                </tr>
				<tr>
					<th>
                        <span class="requiredField">*</span>${message("Coupon.beginDate")}:
					</th>
					<td>
						<input type="text" id="beginDate" name="coupon.begin_date" class="text Wdate" onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss', maxDate: '#F{$dp.$D(\'endDate\')}'});" />
					</td>
				</tr>
				<tr>
					<th>
                        <span class="requiredField">*</span>${message("Coupon.endDate")}:
					</th>
					<td>
						<input type="text" id="endDate" name="coupon.end_date" class="text Wdate" onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss', minDate: '#F{$dp.$D(\'beginDate\')}'});" />
					</td>
				</tr>
				[#--<tr>--]
					[#--<th>--]
						[#--${message("Coupon.minimumPrice")}:--]
					[#--</th>--]
					[#--<td colspan="2">--]
						[#--<input type="text" id="minimumPrice" name="coupon.minimum_price" class="text" maxlength="16" />--]
					[#--</td>--]
				[#--</tr>--]
				[#--<tr>--]
					[#--<th>--]
						[#--${message("Coupon.maximumPrice")}:--]
					[#--</th>--]
					[#--<td colspan="2">--]
						[#--<input type="text" name="coupon.maximum_price" class="text" maxlength="16" />--]
					[#--</td>--]
				[#--</tr>--]
				[#--<tr>--]
					[#--<th>--]
						[#--${message("Coupon.minimumQuantity")}:--]
					[#--</th>--]
					[#--<td colspan="2">--]
						[#--<input type="text" id="minimumQuantity" name="coupon.minimum_quantity" class="text" maxlength="9" />--]
					[#--</td>--]
				[#--</tr>--]
				[#--<tr>--]
					[#--<th>--]
						[#--${message("Coupon.maximumQuantity")}:--]
					[#--</th>--]
					[#--<td colspan="2">--]
						[#--<input type="text" name="coupon.maximum_quantity" class="text" maxlength="9" />--]
					[#--</td>--]
				[#--</tr>--]
				[#--<tr>--]
					[#--<th>--]
						[#--${message("Coupon.priceExpression")}:--]
					[#--</th>--]
					[#--<td colspan="2">--]
						[#--<input type="text" name="coupon.price_expression" class="text" maxlength="255" title="${message("admin.coupon.priceExpressionTitle")}" />--]
					[#--</td>--]
				[#--</tr>--]
				<tr>
					<th>
						${message("admin.common.setting")}:
					</th>
					<td>
						<label>
							<input type="checkbox" name="isEnabled" value="true" checked="checked" />${message("Coupon.isEnabled")}
							<input type="hidden" name="_isEnabled" value="false" />
						</label>
						<label>
							<input type="checkbox" id="isExchange" name="isExchange" value="true" checked="checked" />${message("Coupon.isExchange")}
							<input type="hidden" name="_isExchange" value="false" />
						</label>
					</td>
				</tr>
				<tr>
					<th>
						<span class="requiredField">*</span>${message("Coupon.point")}:
					</th>
					<td>
						<input type="text" id="point" name="coupon.point" class="text" maxlength="9" />
					</td>
				</tr>
			</table>
		</div>
		<div class="tabContent">
			<table class="input">
				<tr>
					<td>
						<textarea id="introduction" name="coupon.introduction" class="editor" style="width: 100%;"></textarea>
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