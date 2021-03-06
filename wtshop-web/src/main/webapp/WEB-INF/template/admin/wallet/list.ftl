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
		<a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; ${message("admin.wallet.list")}<span>(${message("admin.page.total", page.totalRow)})</span>
	</div>
	<form id="listForm" action="list.jhtml" method="post">
		[#if member??]
			<input type="hidden" name="memberId" value="${member.id}" />
		[/#if]
		<div class="bar">
			<div class="buttonGroup">
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
                    <li[#if pageable.searchProperty == "nickname"] class="current"[/#if] val="nickname">${message("admin.wallet.nickname")}</li>
					<li[#if pageable.searchProperty == "phone"] class="current"[/#if] val="phone">${message("admin.wallet.userPhone")}</li>
					<li[#if pageable.searchProperty == "minMoney"] class="current"[/#if] val="minMoney">${message("admin.wallet.walletMin")}</li>
                    <li[#if pageable.searchProperty == "maxMoney"] class="current"[/#if] val="maxMoney">${message("admin.wallet.walletMax")}</li>
				</ul>
			</div>
		</div>
		<table id="listTable" class="list">
			<tr>
				<th>
					<a href="javascript:;" class="sort" name="id">${message("admin.wallet.id")}</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="phone">${message("admin.wallet.userPhone")}</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="nickname">${message("admin.wallet.nick")}</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="login_date">${message("admin.wallet.lastTime")}</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="balance">${message("admin.wallet.money")}</a>
				</th>
                <th>
                    <span>${message("admin.common.action")}</span>
                </th>
			</tr>
			[#list page.list as member]
				<tr>
					<td>
						${member.id}
					</td>
					<td>
						${member.phone}
					</td>
					<td>
						${member.nickname}
					</td>
					<td>
						[#if member.login_date??]
						<span title="${member.login_date?string("yyyy-MM-dd HH:mm:ss")}">${member.login_date}</span>
						[#else ]
						-
						[/#if ]
					</td>
                    <td>
                        ${member.balance}
                    </td>
                    <td>
                        <a href="log.jhtml?memberId=${member.id}">[${message("admin.common.view")}]</a>
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