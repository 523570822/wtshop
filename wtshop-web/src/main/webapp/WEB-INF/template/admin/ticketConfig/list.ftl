[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>${message("admin.coupon.list")} - Powered By ${setting.siteAuthor}</title>
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
		<a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; ${message("admin.coupon.list")} <span>(${message("admin.page.total", page.totalRow)})</span>
	</div>
	<form id="listForm" action="list.jhtml" method="post">
		<div class="bar">
			<a href="add.jhtml" class="iconButton">
				<span class="addIcon">&nbsp;</span>${message("admin.common.add")}
			</a>
			<div class="buttonGroup">
				<a href="javascript:;" id="deleteButton" class="iconButton disabled" >
					<span class="deleteIcon">&nbsp;</span>${message("ticketConfig.list.disable")}
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

			</div>
		</div>
		<table id="listTable" class="list">
			<tr>
                <th class="check">
                    <input type="checkbox" id="selectAll" />
                </th>
				<th>
					<a href="javascript:;" class="" name="">${message("ticketConfig.list.type")}</a>
				</th>
				<th>
					<a href="javascript:;" class=""  name="">${message("ticketConfig.list.fxhdkssj")}</a>
				</th>
                <th>
                    <a href="javascript:;" class="sort" name="">${message("ticketConfig.list.fxhdjssj")}</a>
                </th>
                <th>
                    <a href="javascript:;" class="sort" name="">${message("ticketConfig.list.sjcqsl")}</a>
                </th>
                <th>
                    <a href="javascript:;" class="sort" name="">${message("ticketConfig.list.title")}</a>
                </th>
                <th>
                    <a href="javascript:;" class="sort" name="">${message("ticketConfig.list.content")}</a>
                </th>
                <th>
                    <a href="javascript:;" class="sort" name="">${message("ticketConfig.list.lqzdts")}</a>
                </th>
                <th>
                    <a href="javascript:;" class="sort" name="">${message("ticketConfig.list.cqcsxz")}</a>
                </th>
                <th>
                    <a href="javascript:;" class="sort" name="">${message("ticketConfig.list.state")}</a>
                </th>
                <th>
                    <a href="javascript:;" class="sort" name="">${message("ticketConfig.list.opt")}</a>
                </th>

			</tr>
			[#list page.list as d]
				<tr>
                    <td>
                        <input type="checkbox" name="ids" value="${d.id}" />
                    </td>
					<td>
						[#if d.type==0 ]
						    ${message("ticketConfig.list.ddfx")}
						[/#if]
					</td>
					<td>
					${d.beginTime?string("yyyy-MM-dd HH:mm:ss")}
					</td>
                    <td>
					${d.endTime?string("yyyy-MM-dd HH:mm:ss")}
                    </td>

                    <td>
					${d.num}
                    </td>

                    <td>
					${d.title}
                    </td>

                    <td>
					${d.content}
                    </td>
                    <td>
					${d.maxReceiveDay}
                    </td>
                    <td>
					${d.shareLimit}
                    </td>
                    <td>
						[#if d.state==0 ]
                            ${message("ticketConfig.list.normal")}
							[#elseif  d.state=1]
							${message("ticketConfig.list.prohibit")}
						[/#if]
                    </td>

                    <td>

						[#if d.state==0 ]
                            <a href="edit.jhtml?id=${d.id}">[${message("ticketConfig.list.edit")}]</a>
                            <a href="${base}/admin/ticketpool/list.jhtml?configId=${d.id}">[${message("ticketConfig.list.editTicket")}]</a>
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