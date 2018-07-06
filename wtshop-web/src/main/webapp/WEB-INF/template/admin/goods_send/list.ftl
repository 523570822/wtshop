[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>${message("admin.friendLink.list")} - Powered By ${setting.siteAuthor}</title>
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
		<a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; ${message("admin.goods_send.list")} <span>(${message("admin.page.total", page.totalRow)})</span>
	</div>
	<form id="listForm" action="list.jhtml" method="post">
		<div class="bar">
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
			<div id="searchPropertyMenu" class="dropdownMenu">
				<div class="search">
					<span class="arrow">&nbsp;</span>
					<input type="text" id="searchValue" name="pageable.searchValue" value="${pageable.searchValue}" maxlength="200" />
					<button type="submit">&nbsp;</button>
				</div>
				<ul>
					<li[#if pageable.searchProperty == "sn"] class="current"[/#if] val="sn">${message("Send.order")}</li>
                    <li[#if pageable.searchProperty == "nickname"] class="current"[/#if] val="nickname">${message("Send.member")}</li>
                    <li[#if pageable.searchProperty == "phone"] class="current"[/#if] val="phone">${message("Send.phone")}</li>

				</ul>
			</div>
		</div>
		<table id="listTable" class="list">
			<tr>
				<th class="check">
					<input type="checkbox" id="selectAll" />
				</th>
				<th>
					${message("Send.order")}
				</th>
				<th>
					${message("Send.time")}
				</th>
                <th>
                   ${message("Send.member")}
                </th>
                <th>
                    ${message("Send.phone")}
                </th>

				<th>
					${message("Send.order_time")}
				</th>
				<th>
					<span>${message("admin.common.action")}</span>
				</th>
			</tr>
			[#list page.list as goodsSend]
				<tr>
					<td>
						<input type="checkbox" name="ids" value="${goodsSend.id}" />
					</td>
					<td>
						${goodsSend.sn}
					</td>
					<td>
                        <span title="${goodsSend.create_date?string("yyyy-MM-dd HH:mm:ss")}">${goodsSend.create_date}</span>
					</td>
					<td>
						${goodsSend.nickname}
					</td>
					<td>
						${goodsSend.phone}
					</td>
					<td>
						<span title="${goodsSend.modify_date?string("yyyy-MM-dd HH:mm:ss")}">${goodsSend.modify_date}</span>
					</td>
					<td>
						<a href="${base}/admin/order/view.jhtml?id=${goodsSend.order_id}">[${message("Send.toSendGoods")}]</a>
                        [#--<a href="${base}/admin/message/sendMembers.jhtml?idList=${goodsSend.member_id}"">[${message("Send.toMessage")}]</a>--]
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