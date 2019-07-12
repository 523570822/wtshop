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
	var $promotionIds = $("input[name='promotionIds']");
	var $introduction = $("#introduction");
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


	var productImageIndex = 0;
	var parameterIndex = 0;
	var specificationItemEntryId = 0;
	var hasSpecification = false;
    var $areaId = $("#areaId");
	[@flash_message /]
	
	var previousProductCategoryId = getCookie("previousProductCategoryId");
	previousProductCategoryId != null ? $productCategoryId.val(previousProductCategoryId) : previousProductCategoryId = $productCategoryId.val();
	
	loadParameter();
	loadAttribute();
	loadSpecification();

    $areaId.lSelect({
        url: "${base}/common/area.jhtml"
    });
	
	$filePicker.uploader();
	
	$introduction.editor();
	
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
						loadSpecification();
					}
					previousProductCategoryId = $productCategoryId.val();
				},
				onCancel: function() {
					$productCategoryId.val(previousProductCategoryId);
				}
			});
		} else {
			if ($parameterTable.find("input.parameterEntryValue[value!='']").size() == 0) {
				loadParameter();
			}
			loadAttribute();
			if ($productTable.find("input:text[value!='']").size() == 0) {
				loadSpecification();
			}
			previousProductCategoryId = $productCategoryId.val();
		}
	});
	
	// 类型
	$type.change(function() {


  //  alert("到了");
		changeView();
		buildProductTable();
	});
	
	// 修改视图
	function changeView() {
		if (hasSpecification) {
			$isDefault.prop("disabled", true);
			$price.add($cost).add($marketPrice).add($rewardPoint).add($exchangePoint).add($stock).prop("disabled", true).closest("tr").hide();
			switch ($type.val()) {
				case "general":
					$promotionIds.prop("disabled", false).closest("tr").show();
					break;
				case "exchange":
					$promotionIds.prop("disabled", true).closest("tr").hide();
					break;
				case "gift":
					$promotionIds.prop("disabled", true).closest("tr").hide();
					break;
                case "auction":
                    $promotionIds.prop("disabled", true).closest("tr").hide();
                    break;
			}
		} else {
			$isDefault.prop("disabled", false);
			$cost.add($marketPrice).add($stock).prop("disabled", false).closest("tr").show();
			switch ($type.val()) {
				case "general":
					$price.add($rewardPoint).add($isMarketable).add($isVip)
							.add($promotionIds).prop("disabled", false).closest("tr").show();
					$exchangePoint.prop("disabled", true).closest("tr").hide();
					break;
				case "exchange":
					$price.add($rewardPoint).add($promotionIds).prop("disabled", true).closest("tr").hide();
					$exchangePoint.prop("disabled", false).closest("tr").show();
					break;
				case "gift":
                    $price.add($rewardPoint).add($exchangePoint).add($promotionIds).prop("disabled", true).closest("tr").hide();
                    break;
                case "auction":
                    $rewardPoint.add($exchangePoint).add($isMarketable).add($isVip)
							.add($promotionIds).prop("disabled", true).closest("tr").hide();
                    break;
			}
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
	
	// 删除商品图片
	$productImageTable.on("click", "a.remove", function() {
		$(this).closest("tr").remove();
	});
	
	// 增加参数
	$addParameter.click(function() {
		$(
			[@compress single_line = true]
				'<tr>
					<td colspan="2">
						<table>
							<tr>
								<th>
									${message("Parameter.group")}:
								<\/th>
								<td>
									<input type="text" name="parameterValues[' + parameterIndex + '].group" class="text parameterGroup" maxlength="200" \/>
								<\/td>
								<td>
									<a href="javascript:;" class="remove group">[${message("admin.common.remove")}]<\/a>
									<a href="javascript:;" class="add">[${message("admin.common.add")}]<\/a>
								<\/td>
							<\/tr>
							<tr>
								<th>
									<input type="text" name="parameterValueEntrys[' + parameterIndex + '].entries[0].name" class="text parameterEntryName" maxlength="200" style="width: 50px;" \/>
								<\/th>
								<td>
									<input type="text" name="parameterValueEntrys[' + parameterIndex + '].entries[0].value" class="text parameterEntryValue" maxlength="200" \/>
								<\/td>
								<td>
									<a href="javascript:;" class="remove">[${message("admin.common.remove")}]<\/a>
								<\/td>
							<\/tr>
						<\/table>
					<\/td>
				<\/tr>'
			[/@compress]
		).appendTo($parameterTable).find("table").data("parameterIndex", parameterIndex).data("parameterEntryIndex", 1);
		parameterIndex ++;
	});
	
	// 重置参数
	$resetParameter.click(function() {
		$.dialog({
			type: "warn",
			content: "${message("admin.goods.resetParameterConfirm")}",
			width: 450,
			onOk: function() {
				loadParameter();
			}
		});
	});
	
	// 删除参数
	$parameterTable.on("click", "a.remove", function() {
		var $this = $(this);
		if ($this.hasClass("group")) {
			$this.closest("table").closest("tr").remove();
		} else {
			if ($this.closest("table").find("tr").size() <= 2) {
				$.message("warn", "${message("admin.common.deleteAllNotAllowed")}");
				return false;
			}
			$this.closest("tr").remove();
		}
	});
	
	// 增加参数
	$parameterTable.on("click", "a.add", function() {
		var $table = $(this).closest("table");
		var parameterIndex = $table.data("parameterIndex");
		var parameterEntryIndex = $table.data("parameterEntryIndex");
		$table.append(
			[@compress single_line = true]
				'<tr>
					<th>
						<input type="text" name="parameterValueEntrys[' + parameterIndex + '].entries[' + parameterEntryIndex + '].name" class="text parameterEntryName" maxlength="200" style="width: 50px;" \/>
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
		$table.data("parameterEntryIndex", parameterEntryIndex + 1);
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
	
	// 加载属性
	function loadAttribute() {
		$.ajax({
			url: "attributes.jhtml",
			type: "GET",
			data: {productCategoryId: $productCategoryId.val()},
			dataType: "json",
			success: function(data) {
				$attributeTable.empty();
				$.each(data, function(i, attribute) {
					var $select = $(
						[@compress single_line = true]
							'<tr>
								<th>' + escapeHtml(attribute.name) + ':<\/th>
								<td>
									<select name="attribute_' + attribute.id + '">
										<option value="">${message("admin.common.choose")}<\/option>
									<\/select>
								<\/td>
							<\/tr>'
						[/@compress]
					).appendTo($attributeTable).find("select");
					$.each(attribute.options, function(j, option) {
						$select.append('<option value="' + escapeHtml(option) + '">' + escapeHtml(option) + '<\/option>');
					});
				});
			}
		});
	}
	
	// 重置规格
	$resetSpecification.click(function() {
		$.dialog({
			type: "warn",
			content: "${message("admin.goods.resetSpecificationConfirm")}",
			width: 450,
			onOk: function() {
				hasSpecification = false;
				changeView();
				loadSpecification();
			}
		});
	});
	
	// 选择规格
	$specificationTable.on("change", "input:checkbox", function() {
		if ($specificationTable.find("input:checkbox:checked").size() > 0) {
			hasSpecification = true;
		} else {
			hasSpecification = false;
		}
		changeView();
		buildProductTable();
	});
	
	// 规格
	$specificationTable.on("change", "input:text", function() {
		var $this = $(this);
		var value = $.trim($this.val());
		if (value == "") {
			$this.val($this.data("value"));
			return false;
		}
		if ($this.hasClass("specificationItemEntryValue")) {
			var values = $this.closest("tr").find("input.specificationItemEntryValue").not($this).map(function() {
				return $.trim($(this).val());
			}).get();
			if ($.inArray(value, values) >= 0) {
				$.message("warn", "${message("admin.goods.specificationItemEntryValueRepeated")}");
				$this.val($this.data("value"));
				return false;
			}
		}
		$this.data("value", value);
		buildProductTable();
	});
	
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
	function buildProductTable() {
        console.info("生成商品表");
        console.info(!hasSpecification);

		var type = $type.val();
		var productValues = {};
		var specificationItems = [];
		if (!hasSpecification) {
			$productTable.empty()
			return false;
		}
		$specificationTable.find("tr:gt(0)").each(function() {
			var $this = $(this);
			var $checked = $this.find("input:checkbox:checked");
			if ($checked.size() > 0) {
				var specificationItem = {};
				specificationItem.name = $this.find("input.specificationItemName").val();
				specificationItem.entries = $checked.map(function() {
					return {
						id: $(this).siblings("input.specificationItemEntryId").val(),
						value: $(this).siblings("input.specificationItemEntryValue").val()
					};
				}).get();
				specificationItems.push(specificationItem);
			}
		});
		var products = cartesianProductOf($.map(specificationItems, function(specificationItem) {
			return [specificationItem.entries];
		}));
		$productTable.find("tr:gt(0)").each(function() {
			var $this = $(this);
			productValues[$this.data("ids")] = {
				price: $this.find("input.price").val(),
				cost: $this.find("input.cost").val(),
				market_price: $this.find("input.market_price").val(),
				reward_point: $this.find("input.reward_point").val(),
				exchange_point: $this.find("input.exchange_point").val(),
				stock: $this.find("input.stock").val(),
				is_default: $this.find("input.is_default").prop("checked"),
				isEnabled: $this.find("input.isEnabled").prop("checked")
			};
		});
		$titleTr = $('<tr><\/tr>').appendTo($productTable.empty());
		$.each(specificationItems, function(i, specificationItem) {
			$titleTr.append('<th>' + escapeHtml(specificationItem.name) + '<\/th>');
		});
		$titleTr.append(
			[@compress single_line = true]
				(type == "general" ? '<th>${message("Product.price")}<\/th>' : '') + '
				<th>
					${message("Product.cost")}
				<\/th>
				<th>
					${message("Product.marketPrice")}
				<\/th>
				' + (type == "general" ? '<th>${message("Product.rewardPoint")}<\/th>' : '') +
				(type == "exchange" ? '<th>${message("Product.exchangePoint")}<\/th>' : '') + '
				<th>
					${message("Product.stock")}
				<\/th>
				<th>
					${message("Product.isDefault")}
				<\/th>
				<th>
					${message("admin.goods.isEnabled")}
				<\/th>'
			[/@compress]
		);
		$.each(products, function(i, entries) {
			var ids = [];
			$productTr = $('<tr><\/tr>').appendTo($productTable);
			$.each(entries, function(j, entry) {
				$productTr.append(
					[@compress single_line = true]
						'<td>
							' + escapeHtml(entry.value) + '
							<input type="hidden" name="productLists[' + i + '].specificationValues[' + j + '].id" value="' + entry.id + '" \/>
							<input type="hidden" name="productLists[' + i + '].specificationValues[' + j + '].value" value="' + escapeHtml(entry.value) + '" \/>
						<\/td>'
					[/@compress]
				);
				ids.push(entry.id);
			});
			var productValue = productValues[ids.join(",")];
			var price = productValue != null && productValue.price != null ? productValue.price : "";
			var cost = productValue != null && productValue.cost != null ? productValue.cost : "";
			var market_price = productValue != null && productValue.market_price != null ? productValue.market_price : "";
			var reward_point = productValue != null && productValue.reward_point != null ? productValue.reward_point : "";
			var exchange_point = productValue != null && productValue.exchange_point != null ? productValue.exchange_point : "";
			var stock = productValue != null && productValue.stock != null ? productValue.stock : "";
			var is_default = productValue != null && productValue.is_default != null ? productValue.is_default : false;
			var isEnabled = productValue != null && productValue.isEnabled != null ? productValue.isEnabled : false;
			$productTr.append(
				[@compress single_line = true]
					(type == "general" ? '<td><input type="text" name="productList[' + i + '].price" class="text price" value="' + price + '" maxlength="16" style="width: 50px;" \/><\/td>' : '') + '
					<td>
						<input type="text" name="productList[' + i + '].cost" class="text cost" value="' + cost + '" maxlength="16" style="width: 50px;" \/>
					<\/td>
					<td>
						<input type="text" name="productList[' + i + '].market_price" class="text marketPrice" value="' + market_price + '" maxlength="16" style="width: 50px;" \/>
					<\/td>
					' + (type == "general" ? '<td><input type="text" name="productList[' + i + '].reward_point" class="text rewardPoint" value="' + reward_point + '" maxlength="9" style="width: 50px;" \/><\/td>' : '') +
					(type == "exchange" ? '<td><input type="text" name="productList[' + i + '].exchange_point" class="text exchangePoint" value="' + exchange_point + '" maxlength="9" style="width: 50px;" \/><\/td>' : '') + '
					<td>
						<input type="text" name="productList[' + i + '].stock" class="text stock" value="' + stock + '" maxlength="9" style="width: 50px;" \/>
					<\/td>
					<td>
						<input type="checkbox" name="productList[' + i + '].is_default" class="isDefault" value="true"' + (is_default ? ' checked="checked"' : '') + ' \/>
						<input type="hidden" name="_productList[' + i + '].is_default" value="false" \/>
					<\/td>
					<td>
						<input type="checkbox" name="isEnabled" class="isEnabled" value="true"' + (isEnabled ? ' checked="checked"' : '') + ' \/>
					<\/td>'
				[/@compress]
			).data("ids", ids.join(","));
			if (!isEnabled) {
				$productTr.find(":input:not(.isEnabled)").prop("disabled", true);
			}
		});
		if ($productTable.find("input.isDefault:not(:disabled):checked").size() == 0) {
			$productTable.find("input.isDefault:not(:disabled):first").prop("checked", true);
		}
	}
	
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
	
	// 加载规格	
	function loadSpecification() {
		$.ajax({
			url: "specifications.jhtml",
			type: "GET",
			data: {productCategoryId: $productCategoryId.val()},
			dataType: "json",
			success: function(data) {
				$specificationTable.find("tr:gt(0)").remove();
				$productTable.empty();
				$.each(data, function(i, specification) {
					var $td = $(
						[@compress single_line = true]
							'<tr>
								<th>
									<input type="text" name="specificationItems[' + i + '].name" class="text specificationItemName" value="' + escapeHtml(specification.name) + '" style="width: 50px;" \/>
								<\/th>
								<td><\/td>
							<\/tr>'
						[/@compress]
					).appendTo($specificationTable).find("input").data("value", specification.name).end().find("td");
					$.each(specification.options, function(j, option) {
						$(
							[@compress single_line = true]
								'<span>
									<input type="checkbox" name="specificationItemEntrys[' + i + '].entries[' + j + '].isSelected" value="true" \/>
									<input type="hidden" name="_specificationItemEntrys[' + i + '].entries[' + j + '].isSelected" value="false" \/>
									<input type="hidden" name="specificationItemEntrys[' + i + '].entries[' + j + '].id" class="text specificationItemEntryId" value="' + specificationItemEntryId + '" \/>
									<input type="text" name="specificationItemEntrys[' + i + '].entries[' + j + '].value" class="text specificationItemEntryValue" value="' + escapeHtml(option) + '" style="width: 50px;" \/>
								<\/span>'
							[/@compress]
						).appendTo($td).find("input.specificationItemEntryValue").data("value", option);
						specificationItemEntryId ++;
					});
				});
			}
		});
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
		<input type="hidden" id="isDefault" name="product.is_default" value="true" />
		<ul id="tab" class="tab">
			<li>
				<input type="button" value="${message("admin.goods.base")}" />
			</li>
			<li>
				<input type="button" value="${message("admin.goods.introduction")}" />
			</li>
			<li>
				<input type="button" value="${message("admin.goods.productImage")}" />
			</li>
			<li>
				<input type="button" value="${message("admin.goods.parameter")}" />
			</li>
			<li>
				<input type="button" value="${message("admin.goods.attribute")}" />
			</li>
			<li>
				<input type="button" value="${message("admin.goods.specification")}" />
			</li>
		</ul>
		<table class="input tabContent">
            <tr>
                <th>
                    所属店铺
                </th>
                <td>
                    <input type="radio" id="adminId0" checked name="adminId" value="${i}">
                    <label for="adminId0">总店</label>
                [#list 1..80 as i]
				[#if i%20==0]</br>[/#if]
                    <input type="radio" id="adminId${i}" name="adminId" value="${i}">
             		 <label for="adminId${i}">店铺${i}</label>
				[/#list]




                </td>
            </tr>
            <tr>
			<tr>
				<th>
					${message("Goods.productCategory")}:
				</th>
				<td>
					<select id="productCategoryId" name="productCategoryId">
						[#list productCategoryTree as productCategory]

							[#if productCategory.grade == 0]
                                <option value="${productCategory.id}" disabled="disabled" style="color: #888;">
								${productCategory.name}
                                </option>
							[#elseif productCategory.grade == 1]
                                <option value="${productCategory.id}" disabled="disabled" style="color: #888;">
									&nbsp;&nbsp;
								${productCategory.name}
                                </option>
							[#elseif productCategory.grade == 2]
                                <option value="${productCategory.id}"  style="color: #333;">
                                    &nbsp;&nbsp; &nbsp;&nbsp;
								${productCategory.name}
                                </option>
							[/#if]

						[/#list]
					</select>
				</td>
			</tr>
			<tr>
				<th>
					${message("Goods.type")}:
				</th>
				<td>
					<select id="type" name="type">
						[#list types as type]
							<option value="${type}">${message("Goods.Type." + type)}</option>
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
			<tr>
				<th>
					${message("Goods.caption")}:
				</th>
				<td>
					<input type="text" name="goods.caption" class="text" maxlength="200" />
				</td>
			</tr>
            <tr>
                <th>
				${message("Goods.keyword")}:
                </th>
                <td>
                    <input type="text" name="goods.keyword" class="text" maxlength="200" title="${message("admin.goods.keywordTitle")}" />
                </td>
            </tr>
			<tr>
				<th>
					<span class="requiredField">*</span>${message("Product.price")}:
				</th>
				<td>
					<input type="text" id="price" name="product.price" class="text" maxlength="16" />
				</td>
			</tr>
			<tr>
				<th>
                    <span class="requiredField">*</span>${message("Product.cost")}:
				</th>
				<td>
					<input type="text" id="cost" name="product.cost" class="text" maxlength="16" title="${message("admin.goods.costTitle")}" />
				</td>
			</tr>
			<tr>
				<th>
                    <span class="requiredField">*</span>${message("Product.marketPrice")}:
				</th>
				<td>
					<input type="text" id="marketPrice" name="product.market_price" class="text" maxlength="16" title="${message("admin.goods.marketPriceTitle")}" />
				</td>
			</tr>
            <tr>
                <th>
                    <span class="requiredField">*</span>佣金比例（%）
                </th>
                <td>
                    <input type="text" id="commissionRate" name="goods.commission_rate" class="text" maxlength="16" title="上级及本人优惠比例（如10%填写10）" />
                </td>
            </tr>
			<tr>
				<th>
					${message("Goods.image")}:
				</th>
				<td>
					<span class="fieldSet">
						<input type="text" name="goods.image" class="text" maxlength="200" title="${message("admin.goods.imageTitle")}" />
						<a href="javascript:;" id="filePicker" class="button">${message("admin.upload.filePicker")}</a>
					</span>
				</td>
			</tr>
			<tr>
				<th>
					${message("Goods.unit")}:
				</th>
				<td>
					<input type="text" name="goods.unit" class="text" maxlength="200" />
				</td>
			</tr>
			<tr>
				<th>
                    <span class="requiredField">*</span>${message("Goods.weight")}:
				</th>
				<td>
					<input type="text" name="goods.weight" class="text" maxlength="9" title="${message("admin.goods.weightTitle")}" />
				</td>
			</tr>
			[#--<tr>--]
				[#--<th>--]
					[#--${message("Product.rewardPoint")}:--]
				[#--</th>--]
				[#--<td>--]
					[#--<input type="text" id="rewardPoint" name="product.reward_point" class="text" maxlength="9" title="${message("admin.goods.rewardPointTitle")}" />--]
				[#--</td>--]
			[#--</tr>--]
			<tr class="hidden">
				<th>
					<span class="requiredField">*</span>${message("Product.exchangePoint")}:
				</th>
				<td>
					<input type="text" id="exchangePoint" name="product.exchange_point" class="text" maxlength="9" disabled="disabled" />
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>${message("Product.stock")}:
				</th>
				<td>
					<input type="text" id="stock" name="product.stock" class="text" value="999" maxlength="9" />
				</td>
			</tr>
            <tr>
                <th>
                    <span class="requiredField">*</span>产地:
                </th>
                <td>
					<span class="fieldSet">
						<input type="hidden" id="areaId" name="areaId" value="${(goods.area.id)!}" treePath="${(goods.area.treePath)!}" />
					</span>
                </td>

			</tr>
			<tr>
				<th>
					${message("Goods.brand")}:
				</th>
				<td>
					<select name="brandId">
						<option value="">${message("admin.common.choose")}</option>
						[#list brands as brand]
							<option value="${brand.id}">
								${brand.name}
							</option>
						[/#list]
					</select>
				</td>
			</tr>
			[#if promotions?has_content]
				<tr>
					<th>
						${message("Goods.promotions")}:
					</th>
					<td>
						[#list promotions as promotion]
							<label title="${promotion.title}">
								<input type="checkbox" name="promotionIds" value="${promotion.id}" />${promotion.name}
							</label>
						[/#list]
					</td>
				</tr>
			[/#if]

			<tr>
				<th>
				 vip:
				</th>
				<td>
                    <label>
                        <input type="checkbox" name="isVip" id="isVip" value="true" />${message("Goods.Type.vip")}
                        <input type="hidden" name="_isVip" value="false" />
                    </label>
				</td>
			</tr>

			[#if tags?has_content]
				<tr>
					<th>
						${message("Goods.tags")}:
					</th>
					<td>
						[#list tags as tag]
							<label>
								<input type="checkbox" name="tagIds" value="${tag.id}" />${tag.name}
							</label>
						[/#list]
					</td>
				</tr>
			[/#if]

			[#if effects?has_content]
                <tr>
                    <th>
					${message("Goods.effects")}:
                    </th>
                    <td>
						[#list effects as effect]
                            <label>
                                <input type="checkbox" name="effectIds" value="${effect.id}" />${effect.name}
                            </label>
						[/#list]
                    </td>
                </tr>
			[/#if]
			<tr>
				<th>
					${message("admin.common.setting")}:
				</th>
				<td>
					<label>
						<input type="checkbox" id="isMarketable" name="isMarketable" disabled />${message("Goods.isMarketable")}
						<input type="hidden" name="_isMarketable" value="false" />
					</label>
					<label>
						<input type="checkbox" name="isList" value="true" checked="checked" />${message("Goods.isList")}
						<input type="hidden" name="_isList" value="false" />
					</label>
					<label>
						<input type="checkbox" name="isTop" value="true" />${message("Goods.isTop")}
						<input type="hidden" name="_isTop" value="false" />
					</label>
					<label>
						<input type="checkbox" name="isDelivery" value="true" checked="checked" />${message("Goods.isDelivery")}
						<input type="hidden" name="_isDelivery" value="false" />
					</label>
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
			<tr>
				<th>
					${message("Goods.seoTitle")}:
				</th>
				<td>
					<input type="text" name="goods.seo_title" title=${message("Goods.prepar")} class="text" maxlength="200" />
				</td>
			</tr>
			<tr>
				<th>
					${message("Goods.seoKeywords")}:
				</th>
				<td>
					<input type="text" name="goods.seo_keywords" title=${message("Goods.prepar")} class="text" maxlength="200" />
				</td>
			</tr>
			<tr>
				<th>
					${message("Goods.seoDescription")}:
				</th>
				<td>
					<input type="text" name="goods.seo_description" title=${message("Goods.prepar")} class="text" maxlength="200" />
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