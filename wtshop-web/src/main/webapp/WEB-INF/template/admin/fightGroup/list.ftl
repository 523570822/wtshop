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
    <link href="${base}/statics/lib/layer/mobile/need/layer.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/list.js"></script>
    <script type="text/javascript" src="${base}/statics/lib/layer/layer.js"></script>
    <script type="text/javascript">
         function chooseOrder(orid){

                 layer.open({
                     title:"团购订单列表",
                     type: 2,
                     skin: 'layui-layer-rim', //加上边框
                     area: ['870px', '540px'], //宽高
                     content: "../order/chooseOrder.jhtml?flag="+orid,
                     shadeClose:true,
                 });

         }
        $().ready(function () {

       /*     $("#addProduct").click(function() {
                layer.open({
                    title:"团购订单列表",
                    type: 2,
                    skin: 'layui-layer-rim', //加上边框
                    area: ['870px', '540px'], //宽高
                    content: "../order/chooseOrder.jhtml?flag=2",
                    shadeClose:true,
                });
            });*/
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
                <span>${message("fightGroup.id")}</span>
            </th>

            <th>
                <span>标题</span>
            </th>
            <th>
                <span>${message("groupBuy.price")}</span>
            </th>
            <th>
                <span>团购进度</span>
            </th>
            <th>
                <span>${message("fightGroup.createDate")}</span>
            </th>
            <th>
                <span>${message("admin.memberStatistic.endDate")}</span>
            </th>
            <th>
                <span>${message("fightGroup.status")}</span>
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
                    ${groupBuy.id}
                </td>
                <td>
                    <span title="${groupBuy.title}">${abbreviate(groupBuy.title, 50, "...")}</span>
                </td>

                <td>
                ${groupBuy.price}
                </td>
                <td>
                    <span class="red">${groupBuy.count}</span>   /${groupBuy.groupnum}
                </td>
                <td>
                    [#if groupBuy.create_date??]
                        <span title="${groupBuy.create_date?string("yyyy-MM-dd HH:mm:ss")}">${groupBuy.create_date}</span>
                    [#else]
                        -
                    [/#if]
                </td>
                <td>
                    [#if groupBuy.end_date??]
                        <span title="${groupBuy.end_date?string("yyyy-MM-dd HH:mm:ss")}">${groupBuy.end_date}</span>
                    [#else]
                        -
                    [/#if]
                </td>
                <td>
                    [#if groupBuy.status==1]
                        <span class="green">[拼团成功]</span>
                    [/#if]
                    [#if  groupBuy.status==0 ]
                        <span class="red">[拼图失败]</span>
                    [/#if]

                    [#if  groupBuy.status==2 ]
                        <span class="blue">[拼图中]</span>
                     [/#if]

                </td>
                <td>
                [#--    <input type="button" value="[${message("admin.common.view")}拼图订单]" class="button" />--]
                    <input type="button" onclick="chooseOrder('${groupBuy.id}');" value="[${message("admin.common.view")}拼图订单]" class="button" id="addProduct"/>
                    [#--    <a href="toEdit.jhtml?id=${groupBuy.id}">[${message("admin.common.view")}拼图订单]</a>--]




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