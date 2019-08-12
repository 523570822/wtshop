[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>${message("admin.brand.list")} - Powered By ${setting.siteAuthor}</title>
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

        var  titleB=$('#titleB').val();
        var  titleE=$('#titleE').val();
        location.href='getExcel.jhtml?titleB='+titleB+'&titleE='+titleE;

    })

});
</script>
</head>
<body>
	<div class="breadcrumb">
		<a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; ${message("admin.brand.list")} <span>(${message("admin.page.total", page.totalRow)})</span>
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
					[#--<span class="arrow">&nbsp;</span>--]
					<input type="text" id="blurry" name="blurry" value="${blurry}" maxlength="200" />
					<button type="submit">&nbsp;</button>
				</div>
				[#--<ul>
                    <li[#if pageable.searchProperty == "phone"] class="current"[/#if] val="code">识别码</li>
					<li[#if pageable.searchProperty == "title"] class="current"[/#if] val="title">批次</li>
					<li[#if pageable.searchProperty == "code"] class="current"[/#if] val="code">识别码</li>

				</ul>--]
			</div>

			${message("admin.memberStatistic.beginDate")}:
            <input type="text" id="beginDate" name="beginDate" class="text Wdate" value="${beginDate?string("yyyy-MM-dd")}" style="width: 120px;" onfocus="WdatePicker({maxDate: '#F{$dp.$D(\'endDate\')}'});" />
			${message("admin.memberStatistic.endDate")}:
            <input type="text" id="endDate" name="endDate" class="text Wdate" value="${endDate?string("yyyy-MM-dd")}" style="width: 120px;" onfocus="WdatePicker({minDate: '#F{$dp.$D(\'beginDate\')}'});" />
            <input type="submit" class="button" value="${message("admin.common.submit")}" />
            <input type="button" value="${message("admin.caiwu.expect")}" class="button" id="excelList" />
		</div>
		<table id="listTable" class="list">
			<tr>
				<th class="check">
					<input type="checkbox" id="selectAll" />
				</th>
			[#--	<th>
					<a href="javascript:;" class="sort" name="name">生产批次</a>
				</th>--]
				<th>
					<a href="javascript:;" class="sort" name="code">识别码</a>
				</th>
                <th>
                    <a href="javascript:;" class="sort" name="i.share_code">邀请码</a>
                </th>
                <th>
                    <a href="javascript:;" class="sort" name="m.nickname">姓名</a>
                </th>
                <th>
                    <a href="javascript:;" class="sort" name="m.phone">电话</a>
                </th>
                <th>
                    <a href="javascript:;" class="sort" name="status">状态</a>
                </th>
                <th>
                    <a href="javascript:;" class="sort" name="i.total_money">满金额</a>
                </th>
                <th>
                    <a href="javascript:;" class="sort" name="i.money">反金额</a>
                </th>
                <th>
                    <a href="javascript:;" class="sort" name="i.price">消费金额</a>
                </th>
                <th>
                    <a href="javascript:;" class="sort" name="i.start_date">开始时间</a>
                </th>

                <th>
                    <a href="javascript:;" class="sort" name="i.end_date">截止时间</a>
                </th>
                <th>
                    <a href="javascript:;" class="sort" name="i.complete_date">完成时间</a>
                </th>

				<th>
					<span>${message("admin.common.action")}</span>
				</th>
			</tr>
			[#list page.list as brand]
				<tr>
					<td>
						<input type="checkbox" name="ids" value="${brand.id}" />
					</td>
					[#--<td>
						${brand.title}
					</td>--]
					<td>
						${brand.code}
					</td>
                    <td>
						${brand.share_code}
                    </td>
                    <td>
						${brand.member.nickname}
                    </td>
                    <td>
						${brand.member.phone}
                    </td>
                    <td>
					[#if brand.status==0||brand.status==null]

                        <span class="red">[未使用]</span>



					[#elseif brand.status==3]

                        <span class="blue">[未邮寄]</span>

					[#elseif brand.status==4]

                        <span class="gray">[已邮寄]</span>

					[#elseif brand.status==5]

                        <span class="gray">[现场兑换]</span>

					[#elseif brand.status==1]
                        <span class="green">[已启用]</span>
					[#elseif brand.status==2]
                        <span class="green">[已失效]</span>
					[/#if]

                    </td>
                    <td>
						${brand.total_money}
                    </td>
                    <td>
						${brand.money}
                    </td>

                    <td>
						${brand.price}
                    </td>
                    <td>
						${brand.start_date}
                    </td>
                    <td>
						${brand.end_date}
                    </td> <td>
						${brand.complete_date}
                    </td>

					<td>
					[#if brand.status==2||brand.status==0||brand.status==1]
                    <a href="disabled.jhtml?id=${brand.id}" class="status" data="${brand.id}">[${message("admin.member.disabled")}]</a>
[#--  <a href="publish.jhtml?id=${brand.id}" class="status" data="${brand.id}">[${message("LoginPlugin.isEnabled")}]</a>--]
						 [#else ]

						 [/#if]
						[#if brand.status==3]
					 <a href="publish.jhtml?id=${brand.id}" class="status" data="${brand.id}">[${message("LoginPlugin.isEnabled")}]</a>
							 <a href="disabled.jhtml?id=${brand.id}&&status=4" class="status" data="${brand.id}">[已邮寄]</a>
							 <a href="disabled.jhtml?id=${brand.id}&&status=5" class="status" data="${brand.id}">[现场兑换]</a>

						[#else ]

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