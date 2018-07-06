[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>${message("admin.message.list")} - Powered By ${setting.siteAuthor}</title>
<meta name="author" content="${setting.siteAuthor}" />
<meta name="copyright" content="${setting.siteCopyright}" />
<link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/list.js"></script>
<script type="text/javascript">
$().ready(function() {

    var $listForm = $("#listForm");
    var $filterMenu = $("#filterMenu");
    var $filterMenuItem = $("#filterMenu li");
	[@flash_message /]

    //路径选择
    $filterMenu.hover(
            function() {
                $(this).children("ul").show();
            }, function() {
                $(this).children("ul").hide();
            }
    );
    //选择路径的类型
    $filterMenuItem.click( function() {
        $("#typeName").val($(this).attr("val"));
        $listForm.submit();
    });


});	
</script>
</head>
<body>
	<div class="breadcrumb">
		<a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; ${message("admin.message.list")} <span>(${message("admin.page.total", page.totalRow)})</span>
	</div>
	<form id="listForm" action="list.jhtml" method="post">
        <input type="hidden" name="typeName" id="typeName" value=""/>
		<div class="bar">
			<a href="send.jhtml" class="iconButton">
				<span class="addIcon">&nbsp;</span>${message("admin.message.send")}
			</a>
			<div class="buttonGroup">
				<a href="javascript:;" id="deleteButton" class="iconButton disabled">
					<span class="deleteIcon">&nbsp;</span>${message("admin.common.delete")}
				</a>
				<a href="javascript:;" id="refreshButton" class="iconButton">
					<span class="refreshIcon">&nbsp;</span>${message("admin.common.refresh")}
				</a>
                <div id="filterMenu" class="dropdownMenu">
                    <a href="javascript:;" class="button">
					${message("admin.message.limit")}<span class="arrow">&nbsp;</span>a
                    </a>
                    <ul>
                        <li  val="0">${message("admin.message.system")}</li>
                        <li  val="1">${message("admin.message.order")}</li>
                        <li  val="2">${message("admin.message.news")}</li>
                        <li  val="3">${message("admin.message.goods")}</li>
                        <li  val="4">${message("admin.message.staff")}</li>
                    </ul>
                </div>
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
					<li[#if pageable.searchProperty == "title"] class="current"[/#if] val="title">${message("Message.title")}</li>
                    <li[#if pageable.searchProperty == "name"] class="current"[/#if] val="name">${message("FeedBack.username")}</li>
				</ul>
			</div>
		</div>
		<table id="listTable" class="list">
			<tr>
				<th class="check">
					<input type="checkbox" id="selectAll" />
				</th>
				<th>
					<a href="javascript:;" class="sort" name="title">${message("Message.title")}</a>
				</th>
				<th>
                    <a href="javascript:;" class="sort" name="name">${message("FeedBack.username")}</a>
				</th>
				<th>
                    <a href="javascript:;" class="sort" name="type">${message("admin.message.type")}</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="create_date">${message("admin.common.createDate")}</a>
				</th>
				<th>
					<span>${message("admin.common.action")}</span>
				</th>
			</tr>
			[#list page.list as adminMessage]
				<tr>
					<td>
						<input type="checkbox" name="ids" value="${adminMessage.id}" />
					</td>
					<td>
						<span title="${adminMessage.title}">${abbreviate(adminMessage.title, 50, "...")}</span>
					</td>
					<td>
						${adminMessage.username}
					</td>
					<td>
						[#if adminMessage.type == 0]
						${message("admin.message.system")}
						[#elseif adminMessage.type == 1]
						${message("admin.message.order")}
						[#elseif adminMessage.type == 2]
						${message("admin.message.news")}
						[#elseif adminMessage.type == 3]
						${message("admin.message.goods")}
						[#elseif adminMessage.type == 4]
						${message("admin.message.staff")}
						[/#if]
					</td>
					<td>
						<span title="${adminMessage.create_date?string("yyyy-MM-dd HH:mm:ss")}">${adminMessage.create_date}</span>
					</td>
					<td>
						<a href="view.jhtml?id=${adminMessage.id}">[${message("admin.common.view")}]</a>
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