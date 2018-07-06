[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>${message("admin.adPosition.list")} - Powered By ${setting.siteAuthor}</title>
<meta name="author" content="${setting.siteAuthor}" />
<meta name="copyright" content="${setting.siteCopyright}" />
<link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/list.js"></script>
<script type="text/javascript">
$().ready(function() {

	[@flash_message /]

    $("#send").click(function () {
        var memberList = $("#memberList").val();
        window.location.href= "${base}/admin/message/sendMember.jhtml?idList=" + memberList;
    })


});
</script>
</head>
<body>
	<div class="breadcrumb">
		<a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo;${message("Cart.list")}
		<span>(${message("admin.page.total", page.totalRow)})</span>
	</div>
	<form id="listForm"  method="post">
        <input type="hidden" id="memberList" value="${memberList}"/>
        <div class="bar">
            <div class="buttonGroup">

                <input type="button" class="button" value="${message("admin.common.back")}" onclick="history.back(); return false;" />
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
                <a id="send" class="button">
                    <input type="hidden" id="memberList" value="${memberList}"/>
				${message("admin.send.sendMessage")}
                </a>
            </div>
		<table id="listTable" class="list">
			<tr>

				<th>
					<a href="javascript:;" class="sort" name="id">${message("Footprint.id")}</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="phone">${message("Footprint.phone")}</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="name">${message("Footprint.nickname")}</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="number">${message("Footprint.createTime")}</a>
				</th>
			</tr>
			[#list page.list as cartGoods]
				<tr>
					<td>
						${cartGoods.id}
					</td>
					<td>
						${cartGoods.phone}
					</td>
					<td>
						${cartGoods.nickname}
					</td>
					<td>
						[#if cartGoods.create_date??]
                            <span title="${cartGoods.create_date?string("yyyy-MM-dd HH:mm:ss")}">${cartGoods.create_date}</span>
						[#else]
                            -
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