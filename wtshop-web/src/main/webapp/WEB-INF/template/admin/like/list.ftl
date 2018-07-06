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

});
</script>
</head>
<body>
	<div class="breadcrumb">
		<a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; ${message("Goods.like")}
		<span>(${message("admin.page.total", page.totalRow)})</span>
	</div>
	<form id="listForm" action="list.jhtml" method="post">
        <div class="bar">
            <a href="add.jhtml" class="iconButton">
                <span class="addIcon">&nbsp;</span>${message("admin.common.add")}
            </a>
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
                    <li[#if pageable.searchProperty == "name"] class="current"[/#if] val="name">${message("Goods.name")}</li>
                    <li[#if pageable.searchProperty == "id"] class="current"[/#if] val="sn">${message("Goods.sn")}</li>
                </ul>
            </div>
        </div>
		<table id="listTable" class="list">
			<tr>
                <th class="check">
                    <input type="checkbox" id="selectAll" />
                </th>
				<th>
					<a href="javascript:;" class="sort" name="id">${message("Goods.sn")}</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="name">${message("Goods.name")}</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="product_category_id">${message("Goods.productCategory")}</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="price">${message("Goods.price")}</a>
				</th>
                <th>
                    <a href="javascript:;" class="sort" name="create_date">${message("admin.common.createDate")}</a>
                </th>

			</tr>
			[#list page.list as goods]
				<tr>
                    <td>
                        <input type="checkbox" name="ids" value="${goods.cid}" />
                    </td>
					<td>
						${goods.sn}
					</td>
					<td>
						${abbreviate(goods.name, 50, "...")}
					</td>
					<td>
						${goods.cname}
					</td>
					<td>
						${currency(goods.price, true)}
					</td>
                    <td>
                        <span title="${goods.createDate?string("yyyy-MM-dd HH:mm:ss")}">${goods.createDate}</span>
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