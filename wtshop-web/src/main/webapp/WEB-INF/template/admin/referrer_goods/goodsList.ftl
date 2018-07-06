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

            [@flash_message /]

        });
    </script>
</head>
<body>
<div class="breadcrumb">
    <a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a>&raquo;
    <a href="${base}/admin/referrer_goods/list.jhtml">${message("admin.role.referrerGoods")}</a>&raquo;
${message("admin.main.referrerList")} <span>(${message("admin.page.total", page.totalRow)})</span>
</div>
<form id="listForm" action="queryPageByGoodsId.jhtml" method="post">
    <input type="hidden" name="goodsId" value="${goodsId}"/>
    <div class="bar">
        <div class="buttonGroup">
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
    </div>
    <table id="listTable" class="list">
        <tr>
            <th>
                <a href="javascript:;" class="sort" name="m.id">${message("admin.referrerGoods.mid")}</a>
            </th>
            <th>
                <a href="javascript:;" class="sort" name="g.name">${message("admin.referrerGoods.gName")}</a>
            </th>
            <th>
                <a href="javascript:;" class="sort" name="m.phone">${message("admin.referrerGoods.mPhone")}</a>
            </th>
            <th>
                <a href="javascript:;" class="sort" name="m.nickName">${message("admin.referrerGoods.mName")}</a>
            </th>
            <th>
                <a href="javascript:;" class="sort" name="sf.nickName">${message("admin.referrerGoods.staffName")}</a>
            </th>
            <th>
                <a href="javascript:;" class="sort" name="sf.phone">${message("admin.referrerGoods.staffPhone")}</a>
            </th>
            <th>
                <a href="javascript:;" class="sort" name="create_date">${message("admin.referrerGoods.createDate")}</a>
            </th>
        </tr>
        [#list page.list as member]
            <tr>
                <td>
                ${member.mid}
                </td>
                <td>
                ${member.goodsName}
                </td>
                <td>
                ${member.memberPhone}
                </td>
                <td>
                ${member.memberName}
                </td>
                <td>
                ${member.staffName}
                </td>
                <td>
                ${member.staffPhone}
                </td>
                <td>
                ${member.create_date}
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