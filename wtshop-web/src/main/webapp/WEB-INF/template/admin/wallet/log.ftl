[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>${message("admin.deposit.log")} - Powered By ${setting.siteAuthor}</title>
<meta name="author" content="${setting.siteAuthor}" />
<meta name="copyright" content="${setting.siteCopyright}" />
<link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/list.js"></script>
<script type="text/javascript">
$().ready(function() {

	[@flash_message /]

});
</script>
</head>
<body>
	<div class="breadcrumb">
		<a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; ${message("admin.main.welletConsumption")} <span>(${message("admin.page.total", page.totalRow)})</span>
	</div>
	<form id="listForm" action="log.jhtml" method="post">
		[#if member??]
            <input type="hidden" name="memberId" value="${member.id}" />
		[/#if]
		<div class="bar">
			<div class="buttonGroup">
				[#if member??]
                    <a href="javascript:;" class="button" onclick="history.back(); return false;">${message("admin.common.back")}</a>
				[/#if]
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
            <div id="searchPropertyMenu" class="dropdownMenu">
                <div class="search">
                    <span class="arrow">&nbsp;</span>
                    <input type="text" id="searchValue" name="pageable.searchValue" value="${pageable.searchValue}" maxlength="200" />
                    <button type="submit">&nbsp;</button>
                </div>
                <ul>
                    <li[#if pageable.searchProperty == "userPhone"] class="current"[/#if] val="userPhone">${message("admin.wallet.userPhone")}</li>
                    <li[#if pageable.searchProperty == "nickname"] class="current"[/#if] val="nickname">${message("admin.wallet.nick")}</li>
                </ul>
            </div>
		</div>
		<table id="listTable" class="list">
			<tr>
                <th>
                    <a  name="id">${message("admin.wallet.id")}</a>
                </th>
                <th>
                    <a  name="member_phone">${message("admin.wallet.userPhone")}</a>
                </th>
                <th>
                    <a  name="nickname">${message("admin.wallet.nick")}</a>
                </th>
				<th>
					<a   name="type">${message("DepositLog.type")}</a>
				</th>
				<th>
					<a  name="change">${message("admin.wallet.change")}</a>
				</th>
                <th>
                    <a  name="change">${message("admin.wallet.balance")}</a>
                </th>
				<th>
					<a   name="create_date">${message("admin.common.createDate")}</a>
				</th>
			</tr>
			[#list page.list as depositLog]
				<tr>
                    <td>
					${depositLog.member.id}
                    </td>
                    <td>
					${depositLog.member.phone}
                    </td>
                    <td>
					[#if depositLog.member.nickname??]
					${depositLog.member.nickname}
					[#else ]
                        -
					[/#if ]
                    </td>
					<td>
						${message("DepositLog.Type." + depositLog.typeName)}
					</td>
					<td>
						[#if depositLog.type == 0 || depositLog.type == 3 || depositLog.type == 5 ]
							+ ${currency(depositLog.credit, true)}
						[#elseif depositLog.type == 1 || depositLog.type == 2 || depositLog.type == 4]
							- ${currency(depositLog.debit, true)}
						[/#if]
					</td>
                    <td>
					   ${depositLog.balance}
                    </td>
					<td>
						<span title="${depositLog.createDate?string("yyyy-MM-dd HH:mm:ss")}">${depositLog.createDate}</span>
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