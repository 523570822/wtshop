[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>${message("admin.point.log")} - Powered By ${setting.siteAuthor}</title>
<meta name="author" content="${setting.siteAuthor}" />
<meta name="copyright" content="${setting.siteCopyright}" />
<link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/list.js"></script>
<script type="text/javascript">
$().ready(function() {
    var $filterMenu = $("#filterMenu");
    var $listForm = $("#listForm");
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
		<a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; ${message("admin.point.log")} <span>(${message("admin.page.total", page.totalRow)})</span>
	</div>
	<form id="listForm" action="integralStoreList.jhtml" method="post">
        <input type="hidden" name="typeName" id="typeName" value=""/>
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

                <div id="filterMenu" class="dropdownMenu">
                    <a href="javascript:;" class="button">
					${message("member.order.select")}<span class="arrow">&nbsp;</span>
                    </a>
                    <ul>
                        <li  val="4">${message("admin.point.five")}</li>
                        <li  val="0">${message("admin.point.first")}</li>
                        <li  val="1">${message("admin.point.secone")}</li>
                        <li  val="2">${message("admin.point.third")}</li>
                        <li  val="3">${message("admin.point.four")}</li>
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
                    <li[#if page.searchProperty == "name"] class="current"[/#if] val="name">综合查询</li>
                </ul>
            </div>
		</div>
		<table id="listTable" class="list">
			<tr>
				<th >
                    <a href="javascript:;" class="sort" name="nickname">门店</a>
					</th>

                <th>
                  <a href="javascript:;" class="sort" name="type">老板昵称</a>
                </th>
                <th>
                    <a href="javascript:;" class="sort" name="type">老板电话</a>
                </th>
                <th>
                    <a href="javascript:;" class="sort" name="nickname">${message("PointLog.member")}</a>
                </th>
                <th>
                    <a href="javascript:;" class="sort" name="nickname">会员手机号</a>
                </th>

              [#--  <th>
                    <a href="javascript:;" class="sort" name="type">${message("PointLog.type")}</a>
                </th>--]
				<th>
					<a href="javascript:;" class="sort" name="balance">${message("PointLog.balance")}</a>
				</th>

				<th>
					<a href="javascript:;" class="sort" name="create_date">${message("admin.common.createDate")}</a>
				</th>
			</tr>
			[#list page.list as pointLog]
				<tr>
                    <td>
						[#if pointLog.memberStore.store??]
							${pointLog.memberStore.store}
						[#else]
							-
						[/#if]
                    </td>
                    <td>
						[#if pointLog.memberStore.nickname??]
							${pointLog.memberStore.nickname}
						[#else]
							-
						[/#if]
                    </td>
                    <td>
						[#if pointLog.memberStore.phone??]
							${pointLog.memberStore.phone}
						[#else]
							-
						[/#if]
                    </td>
                    <td>
						[#if pointLog.nickname??]
							${pointLog.nickname}
						[#else]
							-
						[/#if]
                    </td>
                    <td>
						[#if pointLog.phone??]
							${pointLog.phone}
						[#else]
							-
						[/#if]
                    </td>
				[#--	<td>
						[#if pointLog.type == 2 || pointLog.type == 0 || pointLog.type ==1 ]
                            + ${currency(pointLog.credit, true)}
						[#elseif pointLog.type == 3 ]
                            - ${currency(pointLog.debit, true)}
						[/#if]
					</td>--]
					<td>
						${pointLog.balance}
					</td>

					<td>
						<span title="${pointLog.create_date?string("yyyy-MM-dd HH:mm:ss")}">${pointLog.create_date}</span>
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