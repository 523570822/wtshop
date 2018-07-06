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
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/list.js"></script>
    <script type="text/javascript">
        $().ready(function() {

            var $filterMenu = $("#filterMenu");
            var $listForm = $("#listForm");
            var $filterMenuItem = $("#filterMenu li");
            [@flash_message /]

            $("#memberId").click(function () {
                var spCodesTemp = "";
                $("input:checkbox[name='ids']:checked").each(function(i){
                    if(0==i){
                        spCodesTemp = $(this).val();
                    }else{
                        spCodesTemp += (","+$(this).val());
                    }
                });
                window.location.href= " ${base}/admin/message/memberList.jhtml?ids=" + spCodesTemp  ;

            });

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
        })
    </script>
</head>
<body>
<div class="breadcrumb">
    <a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; ${message("admin.member.list")} <span>(${message("admin.page.total", page.totalRow)})</span>
</div>
<form id="listForm" action="memberSelect.jhtml" method="post">
    <input type="hidden" name="typeName" id="typeName" value=""/>
    <div class="bar">
        <div class="buttonGroup">
            <a href="javascript:;" id="refreshButton" class="iconButton">
                <span class="refreshIcon">&nbsp;</span>${message("admin.common.refresh")}
            </a>

            <div id="filterMenu" class="dropdownMenu">
                <a href="javascript:;" class="button">
                ${message("member.rank.select")}<span class="arrow">&nbsp;</span>
                </a>
                <ul>
                    <li  val="1">${message("member.rank.first")}</li>
                    <li  val="2">${message("member.rank.second")}</li>
                    <li  val="3">${message("member.rank.third")}</li>
                    <li  val="4">${message("member.rank.four")}</li>
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
                <li[#if pageable.searchProperty == "username"] class="current"[/#if] val="username">${message("Member.username")}</li>
                <li[#if pageable.searchProperty == "email"] class="current"[/#if] val="email">${message("Member.email")}</li>
                <li[#if pageable.searchProperty == "nickname"] class="current"[/#if] val="nickname">${message("Member.nickname")}</li>
            </ul>
        </div>
    </div>
    <table id="listTable" class="list">
        <tr>
            <th class="check">
                <input type="checkbox" id="selectAll"  />
            </th>
            <th>
                <a href="javascript:;" class="sort" name="username">${message("Member.username")}</a>
            </th>
            <th>
                <a href="javascript:;" class="sort" name="member_rank_id">${message("Member.memberRank")}</a>
            </th>
            <th>
                <a href="javascript:;" class="sort" name="email">${message("Member.email")}</a>
            </th>
            <th>
                <a href="javascript:;" class="sort" name="nickname">${message("Member.nickname")}</a>
            </th>
            <th>
                <a href="javascript:;" class="sort" name="create_date">${message("admin.common.createDate")}</a>
            </th>
            <th>
                <span>${message("admin.member.status")}</span>
            </th>
        </tr>
        [#list page.list as member ]
            <tr>
                <td>
                    <input  type="checkbox" name="ids" id="ids" value="${member.id}" [#list idList as ids]  [#if member.id == ids] checked="checked"[/#if] [/#list] />
                    <input type = "hidden"  name = "number" id="number" value="${page.pageNumber}" />
                </td>
                <td>
                ${member.username}
                </td>
                <td>
                ${member.memberRank.name}
                </td>
                <td>
                ${member.email}
                </td>
                <td>
                ${member.nickname}
                </td>
                <td>
                    <span title="${member.createDate?string("yyyy-MM-dd HH:mm:ss")}">${member.createDate}</span>
                </td>
                <td>
                    [#if !member.isEnabled]
                        <span class="red">${message("admin.member.disabled")}</span>
                    [#elseif member.isLocked]
                        <span class="red"> ${message("admin.member.locked")} </span>
                    [#else]
                        <span class="green">${message("admin.member.normal")}</span>
                    [/#if]
                </td>
            </tr>
        [/#list]

    </table>
    [@pagination pageNumber = page.pageNumber totalPages = page.totalPage]
        [#include "/admin/include/pagination.ftl"]
    [/@pagination]
    <div>
        <tr>
            <th>
                &nbsp;
            </th>
            <td>
                <input type="button" id = "memberId" class="button" value="${message("admin.common.submit")}" />
                <input type="button" class="button" value="${message("admin.common.back")}" onclick="history.back(); return false;" />
            </td>
        </tr>
    </div>

</form>
</body>
</html>
[/#escape]