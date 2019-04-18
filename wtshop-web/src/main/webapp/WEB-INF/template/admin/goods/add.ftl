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
	
	.productTable td {
		border: 1px solid #dde9f5;
	}
</style>
<script type="text/javascript">
$().ready(function() {

	var $inputForm = $("#inputForm");
	var $isDefault = $("#isDefault");
	var $productCategoryId = $("#productCategoryId");
	var $type = $("#type");
	var $price = $("#price");
	var $cost = $("#cost");
	var $commissionRate = $("#commissionRate");
	var $marketPrice = $("#marketPrice");
	var $filePicker = $("#filePicker");
	var $rewardPoint = $("#rewardPoint");
	var $exchangePoint = $("#exchangePoint");
	var $stock = $("#stock");
	var $promotionIds = $("select[name='type']");
	var $introduction = $("#introduction");
	var $attribute_value1 = $("#attribute_value1");
	var $productImageTable = $("#productImageTable");
	var $addProductImage = $("#addProductImage");
	var $parameterTable = $("#parameterTable");
	var $addParameter = $("#addParameter");
	var $resetParameter = $("#resetParameter");
	var $attributeTable = $("#attributeTable");
	var $specificationTable = $("#specificationTable");
	var $resetSpecification = $("#resetSpecification");
	var $productTable = $("#productTable");
    var $isMarketable = $("#isMarketable");
    var $isVip = $("#isVip");
    var $attribute_value0 = $("#attribute_value0");


	var productImageIndex = 0;
	var parameterIndex = 0;
	var specificationItemEntryId = 0;
	var hasSpecification = false;
    var $areaId = $("#areaId");
	[@flash_message /]
	
	var previousProductCategoryId = getCookie("previousProductCategoryId");
	previousProductCategoryId != null ? $productCategoryId.val(previousProductCategoryId) : previousProductCategoryId = $productCategoryId.val();


    $areaId.lSelect({
        url: "${base}/common/area.jhtml"
    });
	
	$filePicker.uploader();
	
	$introduction.editor();
    $attribute_value1.editor();

	// 商品分类
	$productCategoryId.change(function() {
		if ($attributeTable.find("select[value!='']").size() > 0) {
			$.dialog({
				type: "warn",
				content: "${message("admin.goods.productCategoryChangeConfirm")}",
				width: 450,
				onOk: function() {
					if ($parameterTable.find("input.parameterEntryValue[value!='']").size() == 0) {
						loadParameter();
					}
					loadAttribute();
					if ($productTable.find("input:text[value!='']").size() == 0) {
					}
					previousProductCategoryId = $productCategoryId.val();
				},
				onCancel: function() {
					$productCategoryId.val(previousProductCategoryId);
				}
			});
		} else {
			if ($parameterTable.find("input.parameterEntryValue[value!='']").size() == 0) {

			}
			loadAttribute();
			if ($productTable.find("input:text[value!='']").size() == 0) {

			}
			previousProductCategoryId = $productCategoryId.val();
		}
	});
	
	// 类型
	$attribute_value0.change(function() {


  //  alert("到了");
		changeView();
	});
	
	// 修改视图
	function changeView() {

    console.info("dddd");
    console.info($attribute_value0.val());
			switch ($attribute_value0.val()) {
				case "2":
                    console.info("2222");
                    $('.leixing').show();
                    $('.biaoqian').hide();
					break;
				case "1":
                    console.info("11111");
                    $('.leixing').hide();
                    $('.biaoqian').show();
					break;


		}
	}
	
	// 增加商品图片
	$addProductImage.click(function() {
		$productImageTable.append(
			[@compress single_line = true]
				'<tr>
					<td>
						<input type="file" name="productImages[' + productImageIndex + '].file" class="productImageFile" \/>
					<\/td>
					<td>
						<input type="text" name="productImages[' + productImageIndex + '].title" class="text" maxlength="200" \/>
					<\/td>
					<td>
						<input type="text" name="productImages[' + productImageIndex + '].orders" class="text productImageOrder" maxlength="9" style="width: 50px;" \/>
					<\/td>
					<td>
						<a href="javascript:;" class="remove">[${message("admin.common.remove")}]<\/a>
					<\/td>
				<\/tr>'
			[/@compress]
		);
		productImageIndex ++;
        //重新排序商品图片id,后台是读取的顺序id
        var index=0;
        $('#productImageTable tr').each(function (i,v) {
            if ($(this).find('input').length>0){
                $('#productImageTable tr').eq(i).find ('input').eq(0).attr('name','productImages['+index+'].file');
                $('#productImageTable tr').eq(i).find ('input').eq(1).attr('name','productImages['+index+'].title');
                $('#productImageTable tr').eq(i).find ('input').eq(2).attr('name','productImages['+index+'].orders');
                index++;
            }
        });
	});

	


	




	// 加载参数
	function loadParameter() {
		$.ajax({
			url: "parameters.jhtml",
			type: "GET",
			data: {productCategoryId: $productCategoryId.val()},
			dataType: "json",
			success: function(data) {
				parameterIndex = 0;
				$parameterTable.find("tr:gt(0)").remove();
				$.each(data, function(i, parameter) {
					var $parameterGroupTable = $(
						[@compress single_line = true]
							'<tr>
								<td colspan="2">
									<table>
										<tr>
											<th>
												${message("Parameter.group")}:
											<\/th>
											<td>
												<input type="text" name="parameterValues[' + parameterIndex + '].group" class="text parameterGroup" value="' + escapeHtml(parameter.group) + '" maxlength="200" \/>
											<\/td>
											<td>
												<a href="javascript:;" class="remove group">[${message("admin.common.remove")}]<\/a>
												<a href="javascript:;" class="add">[${message("admin.common.add")}]<\/a>
											<\/td>
										<\/tr>
									<\/table>
								<\/td>
							<\/tr>'
						[/@compress]
					).appendTo($parameterTable).find("table").data("parameterIndex", parameterIndex);

					var parameterEntryIndex = 0;
					$.each(parameter.names, function(i, name) {
						$parameterGroupTable.append(
							[@compress single_line = true]
								'<tr>
									<th>
										<input type="text" name="parameterValueEntrys[' + parameterIndex + '].entries[' + parameterEntryIndex + '].name" class="text parameterEntryName" value="' + escapeHtml(name) + '" maxlength="200" style="width: 50px;" \/>
									<\/th>
									<td>
										<input type="text" name="parameterValueEntrys[' + parameterIndex + '].entries[' + parameterEntryIndex + '].value" class="text parameterEntryValue" maxlength="200" \/>
									<\/td>
									<td>
										<a href="javascript:;" class="remove">[${message("admin.common.remove")}]<\/a>
									<\/td>
								<\/tr>'
							[/@compress]
						);
						parameterEntryIndex ++;
					});
					$parameterGroupTable.data("parameterEntryIndex", parameterEntryIndex);
					parameterIndex ++;
				});
			}
		});
	}



	
	// 是否默认
	$productTable.on("change", "input.isDefault", function() {
		var $this = $(this);
		if ($this.prop("checked")) {
			$productTable.find("input.isDefault").not($this).prop("checked", false);
		} else {
			$this.prop("checked", true);
		}
	});
	
	// 是否启用
	$productTable.on("change", "input.isEnabled", function() {
		var $this = $(this);
		if ($this.prop("checked")) {
			$this.closest("tr").find("input:not(.isEnabled)").prop("disabled", false);
		} else {
			$this.closest("tr").find("input:not(.isEnabled)").prop("disabled", true).end().find("input.isDefault").prop("checked", false);
		}
		if ($productTable.find("input.isDefault:not(:disabled):checked").size() == 0) {
			$productTable.find("input.isDefault:not(:disabled):first").prop("checked", true);
		}
	});
	
	// 生成商品表
	
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
			"product.price": {
				required: true,
				min: 0,
				decimal: {
					integer: 12,
					fraction: ${setting.priceScale}
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
	<div class="breadcrumb">
		<a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; ${message("admin.goods.add")}
	</div>
	<form id="inputForm" action="save.jhtml" method="post" enctype="multipart/form-data">

		<ul id="tab" class="tab">
			<li>
				<input type="button" value="${message("admin.goods.base")}" />
			</li>
			<li>
				<input type="button" value="试题" />
			</li>
            <li>
                <input type="button" value="答案" />
            </li>
		</ul>
		<table class="input tabContent">
            <tr>
                <th>
					模块分类
                </th>
                <td>
                    <select id="attribute_value0" name="goods.attribute_value0">
                            <option value="1">考点模式</option>
                            <option value="2">套卷模式</option>
                    </select>
                </td>
            </tr>
			<tr  class="leixing">
				<th>
					${message("Goods.type")}:
				</th>
				<td>
					<select id="type" name="goods.type">
						[#list types as type]
							<option value="${type}">${message("Goods.Type." + type)}${type}</option>
						[/#list]
					</select>
				</td>
			</tr>
            <tr  class="biaoqian">
                <th>
					标签
                </th>
                <td>
                    <select id="attribute_value10" name="goods.attribute_value10">
						[#list tags as tag]
                            <option value="${tag.id}">${tag.name}</option>
						[/#list]
                    </select>
                </td>
            </tr>
			<tr>
				<th>
					${message("Goods.sn")}:
				</th>
				<td>
					<input type="text" name="goods.sn" class="text" maxlength="100" title="${message("admin.goods.snTitle")}" />
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>${message("Goods.name")}:
				</th>
				<td>
					<input type="text" name="goods.name" class="text" maxlength="200" />
				</td>
			</tr>
            <tr class="leixing">
                <th>
                    <span class="requiredField">*</span>省份:
                </th>

                <td>
					[#--${area}--]
                    <select id="areaId" name="areaId">
						[#list area as area1]
                            <option value="${area1.id}">${area1.full_name}</option>
						[/#list]
                    </select>
					[#--<span class="fieldSet">

						<input type="hidden" id="areaId" name="areaId" value="${(goods.area.id)!}" treePath="${(goods.area.treePath)!}" />
					</span>--]
                </td>

			</tr>
			<tr>
				<th>
					${message("Goods.memo")}:
				</th>
				<td>
					<input type="text" name="goods.memo" title=${message("Goods.prepar")}  class="text" maxlength="200" />
				</td>
			</tr>
		</table>
		<table class="input tabContent">
			<tr>
				<td>
					<textarea id="introduction" name="goods.introduction" class="editor" style="width: 100%;"></textarea>
				</td>
			</tr>
		</table>
        <table class="input tabContent">
            <tr>
                <td>
                    <textarea id="attribute_value1" name="goods.attribute_value1" class="editor" style="width: 100%;"></textarea>
                </td>
            </tr>
        </table>
		<table id="productImageTable" class="item tabContent">
			<tr>
				<td colspan="4">
					<a href="javascript:;" id="addProductImage" title=${message("fudai.image.title")} class="button">${message("admin.goods.addProductImage")}</a>
				</td>
			</tr>
			<tr>
				<th>
					${message("ProductImage.file")}
				</th>
				<th>
					${message("ProductImage.title")}
				</th>
				<th>
					${message("admin.common.order")}
				</th>
				<th>
					${message("admin.common.action")}
				</th>
			</tr>
		</table>
		<table id="parameterTable" class="parameterTable input tabContent">
			<tr>
				<th>
					&nbsp;
				</th>
				<td>
					<a href="javascript:;" id="addParameter" class="button">${message("admin.goods.addParameter")}</a>
					<a href="javascript:;" id="resetParameter" class="button">${message("admin.goods.resetParameter")}</a>
				</td>
			</tr>
		</table>
		<table id="attributeTable" class="input tabContent"></table>
		<div class="tabContent">
			<table id="specificationTable" class="specificationTable input">
				<tr>
					<th>
						&nbsp;
					</th>
					<td>
						<a href="javascript:;" id="resetSpecification" class="button">${message("admin.goods.resetSpecification")}</a>
					</td>
				</tr>
			</table>
			<table id="productTable" class="productTable item"></table>
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