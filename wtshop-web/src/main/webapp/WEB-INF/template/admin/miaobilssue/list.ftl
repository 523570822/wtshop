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
    <a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; ${message("groupBuy.manager")}
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
    [#--<span class="moveDirIcon"></span>${message("groupBuy.information")}--]
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
                <span>产品名称</span>
            </th>
            <th>
                <span>${message("groupBuy.price")}</span>
            </th>
            <th>
                <span>${message("groupBuy.uniprice")}</span>
            </th>
            <th>
                <span>${message("groupBuy.count")}</span>
            </th>
            <th>
                <span>${message("groupBuy.num")}</span>
            </th>
            <th>
                <span> ${message("groupBuy.sales")}</span>
            </th>
            <th>
                <span>${message("groupBuy.teamnum")}</span>
            </th>
            <th>
                <span>${message("groupBuy.dispatchprice")}</span>
            </th>
            <th>
                <span>${message("groupBuy.groupnum")}</span>
            </th>
            <th>
                <span> 开始时间</span>
            </th>
            <th>
                <span>结束时间</span>
            </th>

            <th>
                <a href="javascript:;" class="sort" name="orders">${message("shop.common.order")}</a>
            </th>
            <th>
                <span>${message("app_manage.list.time")}</span>
            </th>
            <th>
                <span>${message("admin.common.type")}</span>
            </th>
            <th>
                <span>${message("admin.common.action")}</span>
            </th>
        </tr>
        [#list page.list as groupBuy]
            <tr>
                <td>
                    <input type="checkbox" name="ids" value="${groupBuy.id}"/>
                </td>
                <td>
                    <span title="${groupBuy.title}">${abbreviate(groupBuy.title, 50, "...")}</span>
                </td>
                <td>
                ${groupBuy.product.name}
                </td>
                <td>
                ${groupBuy.price}
                </td>
                <td>
                ${groupBuy.uniprice}
                </td>
                <td>
                    ${groupBuy.count}
                </td>
                <td>
                ${groupBuy.num}
                </td>
                <td>
                ${groupBuy.sales}
                </td>
                <td>
                    ${groupBuy.teamnum}
                </td>
                <td>
                    ${groupBuy.dispatchprice}
                </td>
                <td>
                    ${groupBuy.groupnum}
                </td>
                <td>
                    ${groupBuy.begin_date}
                </td>
                <td>
                    ${groupBuy.end_date}
                </td>
                <td>
                ${groupBuy.orders}
                </td>
                <td>
                    [#if groupBuy.create_date??]
                        <span title="${groupBuy.create_date?string("yyyy-MM-dd HH:mm:ss")}">${groupBuy.create_date}</span>
                    [#else]
                        -
                    [/#if]
                </td>
                <td>
                    [#if groupBuy.status]

                       <span class="green">[已上架]</span>
                    [#else]
                         <span class="red">[已下架]</span>
                    [/#if]

                    [#if groupBuy.isMarketable]

                    [#else]

                    [/#if]

[#--
                    [#if groupBuy.isTop]
                        <span class="green">[已置顶]</span>
                        [#else]
                        <span class="red">[未置顶]</span>
                        [/#if]--]

                    [#if groupBuy.is_singlepurchase]
                        <span class="green">[可以单独购买]</span>
                        [#else]
                        <span class="red">[不可以单独购买]</span>
                        [/#if]
                </td>
                <td>
                    [#if groupBuy.status]
                        <a href="toEdit.jhtml?id=${groupBuy.id}">[${message("admin.common.edit")}]</a>


                        <a href="disabled.jhtml?id=${groupBuy.id}" class="status"
                           data="${groupBuy.id}">][${message("admin.member.disabled")}]</a>
                    [#else ]
                        <a href="toEdit.jhtml?id=${groupBuy.id}">[${message("admin.common.edit")}]</a>

                        <a href="publish.jhtml?id=${groupBuy.id}" class="status"
                           data="${groupBuy.id}">[${message("LoginPlugin.isEnabled")}]</a>
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