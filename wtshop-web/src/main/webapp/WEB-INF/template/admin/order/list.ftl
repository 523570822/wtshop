[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>${message("admin.order.list")} - Powered By ${setting.siteAuthor}</title>
<meta name="author" content="${setting.siteAuthor}" />
<meta name="copyright" content="${setting.siteCopyright}" />
<link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/datePicker/WdatePicker.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/list.js"></script>
<style type="text/css">
.moreTable th {
	width: 80px;
	line-height: 25px;
	padding: 5px 10px 5px 0px;
	text-align: right;
	font-weight: normal;
	color: #333333;
	background-color: #f8fbff;
}

.moreTable td {
	line-height: 25px;
	padding: 5px;
	color: #666666;
}
</style>
<script type="text/javascript">
$().ready(function() {

	var $listForm = $("#listForm");
	var $filterMenu = $("#filterMenu");
	var $filterMenuItem = $("#filterMenu li");
	var $moreButton = $("#moreButton");
	var $print = $("#listTable select[name='print']");
	
	[@flash_message /]
    $('#excelList').click(function () {
        var  beginDate=$('#beginDate').val();
        var  endDate=$('#endDate').val();
        var  type=$('#type').val();
        var  adminId=$('#adminId').val();
        var  status=$('#status').val();
        var  memberUsername=$('#memberUsername').val();
        var  hasExpired=$('#hasExpired').val();
        var  isPendingReceive=$('#isPendingReceive').val();
        var  isPendingRefunds=$('#isPendingRefunds').val();
        var  isAllocatedStock=$('#isAllocatedStock').val();
        location.href='getOrderExcel.jhtml?beginDate='+beginDate+"&endDate="+endDate+"&type="+type+"&adminId="+adminId+"&status="+status+"&memberUsername="+memberUsername+"&hasExpired="+hasExpired+"&isPendingReceive="+isPendingReceive+"&isPendingRefunds="+isPendingRefunds+"&isAllocatedStock="+isAllocatedStock;

    })
	// 筛选菜单
	$filterMenu.hover(
		function() {
			$(this).children("ul").show();
		}, function() {
			$(this).children("ul").hide();
		}
	);
	
	// 筛选
	$filterMenuItem.click(function() {
		var $this = $(this);
		var $dest = $("#" + $this.attr("name"));
		if ($this.hasClass("checked")) {
			$dest.val("");
		} else {
			$dest.val($this.attr("val"));
		}
		$listForm.submit();
	});
	
	// 更多选项
	$moreButton.click(function() {
		$.dialog({
			title: "${message("admin.order.moreOption")}",
			content: 
				[@compress single_line = true]
					'<table id="moreTable" class="moreTable">
						<tr>
							<th>
								${message("Order.type")}:
							<\/th>
							<td>
								<select id="type" name="type">
									<option value="">${message("admin.common.choose")}<\/option>
									[#list types as value]
										<option value="${value}"[#if value == type] selected="selected"[/#if]>${message("Order.Type." + value)}<\/option>
									[/#list]
								<\/select>
							<\/td>
						<\/tr>
						<tr>
							<th>
								${message("Order.status")}:
							<\/th>
							<td>
								<select name="status" id ="status">
									<option value="">${message("admin.common.choose")}<\/option>
									[#list statuses as value]
										<option value="${value}"[#if value == status] selected="selected"[/#if]>${message("Order.Status." + value)}<\/option>
									[/#list]
								<\/select>
							<\/td>
						<\/tr>
					<\/table>'
				[/@compress]
			,
			width: 470,
			modal: true,
			ok: "${message("admin.dialog.ok")}",
			cancel: "${message("admin.dialog.cancel")}",
			onOk: function() {
				$("#moreTable :input").each(function() {
					var $this = $(this);
					$("#" + $this.attr("name")).val($this.val());
				});
				$listForm.submit();
			}
		});
	});
	
	// 打印选择
	$print.change(function() {
		var $this = $(this);
		if ($this.val() != "") {
			window.open($this.val());
		}
	});

});
</script>
</head>
<body>
	<div class="breadcrumb">
		<a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; ${message("admin.order.list")} <span>(${message("admin.page.total", page.totalRow)})</span>
	</div>
	<form id="listForm" action="list.jhtml" method="post">
		<input type="hidden" id="type" name="type" value="${type}" />
		<input type="hidden" id="status" name="status" value="${status}" />
		<input type="hidden" id="memberUsername" name="memberUsername" value="${memberUsername}" />
		<input type="hidden" id="isPendingReceive" name="isPendingReceive" value="${(isPendingReceive?string("true", "false"))!}" />
		<input type="hidden" id="isPendingRefunds" name="isPendingRefunds" value="${(isPendingRefunds?string("true", "false"))!}" />
		<input type="hidden" id="isAllocatedStock" name="isAllocatedStock" value="${(isAllocatedStock?string("true", "false"))!}" />
		<input type="hidden" id="hasExpired" name="hasExpired" value="${(hasExpired?string("true", "false"))!}" />
		<div class="bar">
			<div class="buttonGroup">
				<a href="javascript:;" id="deleteButton" class="iconButton disabled">
					<span class="deleteIcon">&nbsp;</span>${message("admin.common.delete")}
				</a>
				<a href="javascript:;" id="refreshButton" class="iconButton">
					<span class="refreshIcon">&nbsp;</span>${message("admin.common.refresh")}
				</a>
				[#--<div id="filterMenu" class="dropdownMenu">--]
					[#--<a href="javascript:;" class="button">--]
						[#--${message("admin.order.filter")}<span class="arrow">&nbsp;</span>--]
					[#--</a>--]
					[#--<ul class="check">--]
						[#--<li name="isPendingReceive"[#if isPendingReceive?? && isPendingReceive] class="checked"[/#if] val="true">${message("admin.order.pendingReceive")}</li>--]
						[#--<li name="isPendingReceive"[#if isPendingReceive?? && !isPendingReceive] class="checked"[/#if] val="false">${message("admin.order.unPendingReceive")}</li>--]
						[#--<li class="divider">&nbsp;</li>--]
						[#--<li name="isPendingRefunds"[#if isPendingRefunds?? && isPendingRefunds] class="checked"[/#if] val="true">${message("admin.order.pendingRefunds")}</li>--]
						[#--<li name="isPendingRefunds"[#if isPendingRefunds?? && !isPendingRefunds] class="checked"[/#if] val="false">${message("admin.order.unPendingRefunds")}</li>--]
						[#--<li class="divider">&nbsp;</li>--]
						[#--<li name="isAllocatedStock"[#if isAllocatedStock?? && isAllocatedStock] class="checked"[/#if] val="true">${message("admin.order.allocatedStock")}</li>--]
						[#--<li name="isAllocatedStock"[#if isAllocatedStock?? && !isAllocatedStock] class="checked"[/#if] val="false">${message("admin.order.unAllocatedStock")}</li>--]
						[#--<li class="divider">&nbsp;</li>--]
						[#--<li name="hasExpired"[#if hasExpired?? && hasExpired] class="checked"[/#if] val="true">${message("admin.order.hasExpired")}</li>--]
						[#--<li name="hasExpired"[#if hasExpired?? && !hasExpired] class="checked"[/#if] val="false">${message("admin.order.unexpired")}</li>--]
					[#--</ul>--]
				[#--</div>--]
				<a href="javascript:;" id="moreButton" class="button">${message("admin.order.moreOption")}</a>
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
					<span class="arrow">&nbsp;</span>
					<input type="text" id="searchValue" name="pageable.searchValue" value="${pageable.searchValue}" maxlength="200" />
					<button type="submit">&nbsp;</button>
				</div>
				<ul>
					<li[#if pageable.searchProperty == "sn"] class="current"[/#if] val="sn">${message("Order.sn")}</li>
					<li[#if pageable.searchProperty == "consignee"] class="current"[/#if] val="consignee">${message("Order.consignee")}</li>
					<li[#if pageable.searchProperty == "area_name"] class="current"[/#if] val="area_name">${message("Order.area")}</li>
					<li[#if pageable.searchProperty == "address"] class="current"[/#if] val="address">${message("Order.address")}</li>
					<li[#if pageable.searchProperty == "zip_code"] class="current"[/#if] val="zip_code">${message("Order.zipCode")}</li>
					<li[#if pageable.searchProperty == "phone"] class="current"[/#if] val="phone">${message("Order.phone")}</li>
				</ul>
			</div>
            商铺:
			<select id="adminId" name="adminId">
                <option value="">
                    全部
                </option>
				<option value="0"
					  [#if 0 == adminId]
                                selected = "selected"[/#if]>
                    总店
				</option>
  [#list 1..50 as i]
			<option
			  [#if i == adminId]
                                selected = "selected"[/#if]
					value="${i}">
                店铺${i}
            </option>
  [/#list]
			</select>
			${message("admin.memberStatistic.beginDate")}:
            <input type="text" id="beginDate" name="beginDate" class="text Wdate" value="${beginDate?string("yyyy-MM-dd")}" style="width: 120px;" onfocus="WdatePicker({maxDate: '#F{$dp.$D(\'endDate\')}'});" />
			${message("admin.memberStatistic.endDate")}:
            <input type="text" id="endDate" name="endDate" class="text Wdate" value="${endDate?string("yyyy-MM-dd")}" style="width: 120px;" onfocus="WdatePicker({minDate: '#F{$dp.$D(\'beginDate\')}'});" />
            <input type="submit" class="button" value="${message("admin.common.submit")}" />
            <input type="button" value="${message("admin.caiwu.expect")}" class="button" id="excelList" />
        [#--    <a href="javascript:;" id="nohandle" class="iconButton">
                <span class="moveDirIcon">&nbsp;</span>${message("admin.common.nohandle")}
            </a>--]
		</div>


		<table id="listTable" class="list">
			<tr>
				<th class="checkin" >
					<input type="checkbox" id="selectAll" />${message("Order.selectAll")}
				</th>

				<th width="120px">
					<a href="javascript:;" name="amount">${message("Order.sn")}</a>
				</th>
                <th width="120px">
                    <a href="javascript:;" name="amount">商品名称</a>
                </th>
                <th>
                    <a href="javascript:;" name="amount">数  量</a>
                </th>
                <th>
                    <a href="javascript:;" name="amount">单  价</a>
                </th>
                <th>
                    <a href="javascript:;"  name="amount">商品总金额</a>
                </th>

                <th>
                    <a href="javascript:;" name="amount">喵币支付</a>
                </th>
                <th>
                    <a href="javascript:;" name="amount">邮费</a>
                </th>
				<th>
					<a href="javascript:;"  name="amount">${message("Order.amount")}</a>
				</th>
                <th>
                    <a href="javascript:;" name="amount">规格</a>
                </th>
                <th>
                    <a href="javascript:;" name="amount">拼团人数</a>
                </th>
                <th>
                    <a href="javascript:;" name="amount">订单备注</a>
                </th>

                <th>
                    <a href="javascript:;"  name="consignee">${message("Order.consignee")}</a>
                </th>
				<th  class="phone" width="86px"  >
					<a href="javascript:;"   name="member_id">${message("Order.phone")}</a>
				</th>



                <th>
                    <a href="javascript:;" class="sort" name="consignee">${message("Order.address")}</a>
                </th>
				<th>
					<a href="javascript:;" class="sort" name="payment_method_name">${message("Order.paymentMethod")}</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="payment_method_name">${message("Order.isInvoice")}</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="shipping_method_name">${message("Order.shippingMethod")}</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="status">${message("Order.status")}</a>
				</th>
                <th>
                    <a href="javascript:;" class="sort" name="type">${message("admin.type")}</a>
                </th>
				<th>
					<a href="javascript:;" class="sort" name="create_date">${message("admin.common.createDate")}</a>
				</th>
				[@shiro.hasPermission name = "admin:print"]
					<th>
						<span>${message("admin.order.print")}</span>
					</th>
				[/@shiro.hasPermission]
				<th>
					<span>${message("admin.common.action")}</span>
				</th>
			</tr>
			[#list page.list as order]
				<tr>

                    <td>
					<input type="checkbox" name="ids" value="${order.id}" /> 	${order.admin_id}	[#--${order_index+1}--]
					</td>
					<td>
						${order.sn}
					</td>
					<td>
						${order.name}
					</td>
                    <td>
						${order.quantity}
                    </td>
                    <td>
						${currency(order.good_price, true)}

                    </td>
                    <td>
						${currency(order.good_zprice, true)}

                    </td>

                    <td>
						${currency(order.miaobi_goodpaid, true)}

                    </td>
                    <td>
						${currency(order.fee, true)}
                    </td>
					<td>
						${currency(order.amount, true)}
					</td>
                    <td>
						${order.specifications}
                    </td>
                    <td>
						${order.count}/${order.groupnum}
                    </td>
                    <td>
						${order.memo}
                    </td>
					<td>
						${order.consignee}
					</td>

                    <td>
						${order.phone}
                    </td>

                    <td>
						${order.areaName}  ${order.address}
                    </td>
					<td>
						${order.paymentMethodName}
					</td>
                    <td>
						[#if order.isInvoice == "true"]

                            <span class="red">开发票</span>
						[#else ]

							  <span>否</span>
						[/#if]
                    </td>
					<td>
						${order.shippingMethodName}
					</td>
					<td>
						${message("Order.Status." + order.statusName)}
						[#if order.isDelete == true]
						[#else ]
							<span class="red">（删）</span>
						[/#if]
						[#if order.hasExpired()]
							<span class="silver">(${message("admin.order.hasExpired")})</span>
						[/#if]
					</td>

                    <td>
						[#if order.type == "0"]
                            <span>${message("Order.Type." + order.typeName)}</span>
						[#else ]
							<span class="blue">${message("Order.Type." + order.typeName)}</span>
						[/#if]
                    </td>
					<td>
						<span title="${order.createDate?string("yyyy-MM-dd HH:mm:ss")}">${order.createDate}</span>
					</td>
					[@shiro.hasPermission name = "admin:print"]
						<td>
							<select name="print">
								<option value="">${message("admin.common.choose")}</option>
								<option value="../print/order.jhtml?id=${order.id}">${message("admin.order.orderPrint")}</option>
								<option value="../print/product.jhtml?id=${order.id}">${message("admin.order.productPrint")}</option>
								<option value="../print/shipping.jhtml?id=${order.id}">${message("admin.order.shippingPrint")}</option>
								[#if order.isDelivery]
									<option value="../print/delivery.jhtml?orderId=${order.id}">${message("admin.order.deliveryPrint")}</option>
								[/#if]
							</select>
						</td>
					[/@shiro.hasPermission]
					<td>
						<a href="view.jhtml?id=${order.id}">[${message("admin.common.view")}]</a>
						[#if !order.hasExpired() && (order.statusName == "pendingPayment" || order.statusName == "pendingReview")]
							<a href="edit.jhtml?id=${order.id}">[${message("admin.common.edit")}]</a>
						[#else]
							<span title="${message("admin.order.editNotAllowed")}">[${message("admin.common.edit")}]</span>
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