[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <title>${message("admin.member.list")} - Powered By ${setting.siteAuthor}</title>
    <meta name="author" content="${setting.siteAuthor}" />
    <meta name="copyright" content="${setting.siteCopyright}" />
    <link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
    <style type="text/css">
        .bar .inputv {
            width: 180px;
            height: 26px;
            position: relative;
            -webkit-box-shadow: 0px 1px 1px rgba(0, 0, 0, 0.075);
            -moz-box-shadow: 0px 1px 1px rgba(0, 0, 0, 0.075);
            box-shadow: 0px 1px 1px rgba(0, 0, 0, 0.075);
            -webkit-border-radius: 3px;
            -moz-border-radius: 3px;
            border-radius: 3px;
            border: 1px solid;
            border-color: #bfbfbf #d7d7d7 #d7d7d7 #bfbfbf;
            background: url(../images/list.gif) 0px 0px no-repeat;
        }
        .bar .inputv input {
            width: 120px;
            height: 18px;
            line-height: 18px;
            padding: 4px;
            margin: 0px;
            position: absolute;
            top: 0px;
            color: #666666;
            outline: none;
            border: 0px;
            background: none;
        }
    </style>
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
    <a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; ${message("admin.member.list")} <span>(${message("admin.page.total", page.totalRow)})</span>
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
        姓名：<div class="dropdownMenu">

            <div class="inputv">
          <input type="text" name="memname" />
            </div>
        </div>
        联系电话：<div class="dropdownMenu">

        <div class="inputv">
            <input type="text" name="memname" />
        </div>
    </div>
        <div class="dropdownMenu">
            级别：<select name="memberRankId">
						[#list houserkeeperList as houserkeeper]
                            <option value="${houserkeeper.id}"[#if houserkeeper == member.houserkeeperId] selected="selected"[/#if]>${houserkeeper.name}</option>
                        [/#list]
        </select>
        </div>
        <input type="submit" class="button" value="${message("admin.common.submit")}" />

    </div>
    <table id="listTable" class="list">
        <tr>
            <th class="check">
                <input type="checkbox" id="selectAll" />
            </th>
            <th>
                <a href="javascript:;" class="sort" name="username">${message("Member.mobile")}</a>
            </th>
            <th>
                <a href="javascript:;" class="sort" name="member_rank_id">${message("Member.memberRank")}</a>
            </th>
            <th>
                <a href="javascript:;" class="sort" name="nickname">${message("Member.nickname")}</a>
            </th>
            <th>
                <a href="javascript:;" class="sort" name="nickname">团队售出总金额</a>
            </th>
            <th>
                <a href="javascript:;" class="sort" name="nickname">团队总人数</a>
            </th>
            [#--<th>
                <a href="javascript:;" class="sort" name="create_date">${message("admin.common.createDate")}</a>
            </th>--]
            <th>
                <span>${message("admin.member.status")}</span>
            </th>
            <th>
                <span>${message("admin.common.action")}</span>
            </th>
        </tr>
			[#list page.list as member]
				<tr>
                    <td>
                        <input type="checkbox" name="ids" value="${member.id}" />
                    </td>
                    <td>
                        ${member.phone}
                    </td>
                    <td>
                        ${member.houserkeeper.name}
                    </td>
                    <td>
                        ${member.nickname}
                    </td>
                    <td>
                        ${member.zprice}
                    </td>
                    <td>
                        ${member.totalPeople}
                    </td>
                  [#--  <td>
                        <span title="${member.createDate?string("yyyy-MM-dd HH:mm:ss")}">${member.createDate}</span>
                    </td>--]
                    <td>
						[#if !member.isEnabled]
                            <span class="red">${message("admin.member.disabled")}</span>
                        [#elseif member.isLocked]
							<span class="red"> ${message("admin.member.locked")} </span>
                        [#else]
							<span class="green">${message("admin.member.normal")}</span>
                        [/#if]
                    </td>
                    <td>
                        <a href="view.jhtml?id=${member.id}">[${message("admin.common.view")}]</a>
                        <a href="edit.jhtml?id=${member.id}">[${message("admin.common.edit")}]</a>
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