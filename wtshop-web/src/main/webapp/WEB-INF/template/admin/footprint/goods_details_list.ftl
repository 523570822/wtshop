[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>足迹- Powered By ${setting.siteAuthor}</title>
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
		<a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo;${message("foorprint.list")}  <span>(${message("admin.page.total", page.totalRow)})</span>
	</div>
	<form id="listForm" action="goodDetailsList.jhtml" method="post">
        <input  class="hidden" name="goodsId" value="${goodsId}"/>
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
                    <li[#if pageable.searchProperty == "admin.goods.name"] class="current"[/#if] val="nickname">${message("admin.wallet.nick")}</li>
                    <li[#if pageable.searchProperty == "admin.goods.name"] class="current"[/#if] val="phone">${message("admin.referrerGoods.mPhone")}</li>
                </ul>
			</div>
            <a href="javascript:history.back()" class="iconButton">
                <span class="moveDirIcon">&nbsp;</span>${message("admin.back")}
			</a>
		</div>
		<table id="listTable" class="list">
			<tr>

				<th>
					<a href="javascript:;" class="" name="">${message("admin.member.phone")}</a>
				</th>
				<th>
					<a href="javascript:;" class=""  name="">${message("admin.member.username")}</a>
				</th>
                <th>
                    <a href="javascript:;" class="sort" name="">${message("admin.member.nickname")}</a>
				</th>
                <th>
                    <a href="javascript:;" class="sort" name="caeate_date">${message("admin.wallet.time")}</a>
				</th>

			</tr>
			[#list page.list as d]
				<tr>
					<td>
					${d.phone}
					</td>
                    <td>
					${d.username}
                    </td>
                    <td>
					${d.nickname}
                    </td>
                    <td>
					${d.create_date}
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