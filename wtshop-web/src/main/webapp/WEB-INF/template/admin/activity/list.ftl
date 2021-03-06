[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <title>${message("admin.promotion.list")} - Powered By ${setting.siteAuthor}</title>
    <meta name="author" content="${setting.siteAuthor}"/>
    <meta name="copyright" content="${setting.siteCopyright}"/>
    <link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/list.js"></script>
    <script type="text/javascript">
        $().ready(function () {
            [@flash_message /]
        });
    </script>
</head>
<body>
<div class="breadcrumb">
    <a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; ${message("Fudai.manager")}
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
                <input type="text" id="searchValue" name="pageable.searchValue" value="${pageable.searchValue}"
                       maxlength="200"/>
                <button type="submit">&nbsp;</button>
            </div>
            <ul>
            [#--<li[#if pageable.searchProperty == "name"] class="current"[/#if] val="name">${message("Promotion.name")}</li>--]
                <li[#if pageable.searchProperty == "title"] class="current"[/#if]
                                                            val="title">${message("Promotion.title")}</li>
            </ul>
        </div>
    [#--<a href="/admin/actIntroduce/details.jhtml?type=0" class=" iconButton">--]
    [#--<span class="moveDirIcon"></span>${message("Fudai.information")}--]
    [#--</a>--]
    </div>
    <table id="listTable" class="list">
        <tr>
            <th class="check">
                <input type="checkbox" id="selectAll"/>
            </th>
            <th>
                <span>${message("Promotion.title")}</span>
            </th>
            <th>
                <span>${message("Activity.ptNum")}</span>
            </th>
            <th>
                <span>${message("Activity.nowNumber")}</span>
            </th>
            <th>
                <span>${message("Activity.number")}</span>
            </th>
            <th>
                <span>${message("Activity.phone")}</span>
            </th>
            <th>
                <span>${message("Activity.luckyNumber")}</span>
            </th>

            <th>
                <span>${message("Footprint.beginTime")}</span>
            </th>
            <th>
                <span>${message("Footprint.endTime")}</span>
            </th>
            <th>
                <span>${message("NewGoods.time")}</span>
            </th>
            <th>
                <span>${message("admin.common.type")}</span>
            </th>
            <th>
                <span>${message("admin.common.action")}</span>
            </th>
        </tr>
        [#list page.list as activity]
            <tr>
                <td>
                    <input type="checkbox" name="ids" value="${activity.id}"/>
                </td>
                <td>
                    <span title="${activity.opporName}">${abbreviate(activity.opporName, 50, "...")}</span>
                </td>
                <td>
                ${activity.ptNum}
                </td>
                <td>
                ${activity.nowNumber}
                </td>
                <td>
                    ${activity.number}
                </td>
                <td>
                    ${activity.phone}
                </td>
                <td>
                    ${activity.luckyNumber}
                </td>
                <td>
                    [#if activity.beginDate??]
                        <span title="${activity.beginDate?string("yyyy-MM-dd HH:mm:ss")}">${activity.beginDate}</span>
                    [#else]
                        -
                    [/#if]
                </td>
                <td>
                    [#if activity.endDate??]
                        <span title="${activity.endDate?string("yyyy-MM-dd HH:mm:ss")}">${activity.endDate}</span>
                    [#else]
                        -
                    [/#if]
                </td>
                <td>
                    [#if activity.create_date??]
                        <span title="${activity.create_date?string("yyyy-MM-dd HH:mm:ss")}">${activity.create_date}</span>
                    [#else]
                        -
                    [/#if]
                </td>
                <td>
                    [#if activity.status==1]
                      [#--  <span class="green">[已启用]</span>--]
                         [#if activity.isTime==0]
                        <span class="green">[已开始]</span>
                         [#elseif  activity.isTime==1]
                        <span class="red">[已结束]</span>
                         [#elseif activity.isTime==1]
                            <span class="red">[未开始]</span>
                         [#else]
                       <span class="red">[有问题联系技术]</span>
                         [/#if]
                    [#else]
                        <span class="red">[已禁用]</span>
                    [/#if]


                </td>
                <td>
                    <a href="addGoods.jhtml?id=${activity.id}">[${message("Activity.goods")}]</a>
                    <a href="toEdit.jhtml?id=${activity.id}">[${message("admin.common.edit")}]</a>
                    [#if activity.status==1]
                        <a href="disabled.jhtml?id=${activity.id}" class="status" data="${activity.id}">][${message("admin.member.disabled")}]</a>
                    [#else ]

                        <a href="publish.jhtml?id=${activity.id}" class="status" data="${activity.id}">[${message("LoginPlugin.isEnabled")}]</a>
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