[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>${message("admin.parameter.list")} - Powered By ${setting.siteAuthor}</title>
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
        var  beginDate=$('#beginDate').val();
        var  endDate=$('#endDate').val();
        location.href='getReturnListExcel.jhtml?beginDate='+beginDate+"&endDate="+endDate;

    })

	$('#excel').click(function () {
		location.href="${base}/admin/caiwu/list.jhtml"
    })

});
</script>
	<style type="text/css">
        table.list td {
            border: 1px solid #b9e0f5;
        }

        table{
            width: 98%;
            margin-left: 1%;
        }
	</style>
</head>
<body>
	<div class="breadcrumb">
		<a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; ${message("admin.parameter.list")} <span>(${message("admin.page.total", page.totalRow)})</span>
	</div>
	<form id="listForm" action="returnList.jhtml" method="post">
		<div class="bar">
			[#--<a href="add.jhtml" class="iconButton">--]
				[#--<span class="addIcon">&nbsp;</span>${message("admin.common.add")}--]
			[#--</a>--]
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
						<li[#if page.pageSize == 10] class="current"[/#if] val="10">10</li>
						<li[#if page.pageSize == 20] class="current"[/#if] val="20">20</li>
						<li[#if page.pageSize == 50] class="current"[/#if] val="50">50</li>
						<li[#if page.pageSize == 100] class="current"[/#if] val="100">100</li>
					</ul>
				</div>
			</div>
		${message("admin.memberStatistic.beginDate")}:
            <input type="text" id="beginDate" name="beginDate" class="text Wdate" value="${beginDate?string("yyyy-MM-dd")}" style="width: 120px;" onfocus="WdatePicker({maxDate: '#F{$dp.$D(\'endDate\')}'});" />
		${message("admin.memberStatistic.endDate")}:
            <input type="text" id="endDate" name="endDate" class="text Wdate" value="${endDate?string("yyyy-MM-dd")}" style="width: 120px;" onfocus="WdatePicker({minDate: '#F{$dp.$D(\'beginDate\')}'});" />
            <input type="submit" class="button" value="${message("admin.common.submit")}" />
            <input type="button" value="${message("admin.caiwu.expect")}" class="button" id="excelList" />

			[#--<div id="searchPropertyMenu" class="dropdownMenu">--]
				[#--<div class="search">--]
					[#--<span class="arrow">&nbsp;</span>--]
					[#--<input type="text" id="searchValue" name="pageable.searchValue" value="${pageable.searchValue}" maxlength="200" />--]
					[#--<button type="submit">&nbsp;</button>--]
				[#--</div>--]
				[#--<ul>--]
					[#--<li[#if pageable.searchProperty == "parameter_group"] class="current"[/#if] val="parameter_group">${message("Parameter.group")}</li>--]
				[#--</ul>--]
			[#--</div>--]
            <input type="button" value="${message("CaiWu.view")}" class="button" id="excel" />
		</div>
		<table id="listTable" class="list">
			<tr>
                <th>
                    <a href="javascript:;" class="sort" name="type">${message("CaiWu.orderType")}</a>
                </th>
                <th>
                    <a href="javascript:;" class="sort" name="type">${message("CaiWu.orderSns")}</a>
                </th>
				<th>
					<a href="javascript:;" class="sort" name="sn">${message("Caiwu.returnSn")}</a>
				</th>
				<th>${message("Caiwu.memberNick")}</th>
                <th>
                    <a href="javascript:;" class="sort" name="status">${message("Caiwu.returnDate")}</a>
                </th>
				<th>
					<a href="javascript:;" class="sort" name="create_date">${message("Caiwu.returnCount")}</a>
				</th>

				<th>
					<span>${message("Caiwu.returnMoney")}</span>
				</th>
				<th>
					<span>${message("Caiwu.returnStatus")}</span>
				</th>
                <th>
                    <span>${message("CaiWu.goodsName")}</span>
                </th>

			</tr>
			[#list page.list as e]
				<tr>
                    <td >
						[#switch e.type]
						    [#case  0 ]
							${message("Order.Type.general")}
								[#break ]
							[#case  1 ]
							${message("Order.Type.exchange")}
								[#break ]
							[#case  2 ]
							${message("Order.Type.fudai")}
								[#break ]
							[#case  3 ]
							${message("Order.Type.daopai")}
								[#break ]
							[#case  4 ]
							${message("Order.Type.miaobi")}
								[#break ]
							[#case  5 ]
							${message("Order.Type.manjian")}
								[#break ]
							[#case  6 ]
							${message("Order.Type.vip")}
								[#break ]
						[/#switch]
                    </td>
                    <td>${e.sn}</td>
                    <td>${e.return_sn}</td>
                    <td>${e.nickname}</td>
                    <td>${e.modify_date}</td>
                    <td>${e.quantity}</td>
                    <td>${e.amount}</td>
                    <td>已完成</td>
                    <td>${e.goodsName}</td>

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
