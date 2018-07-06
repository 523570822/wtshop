[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <title>${message("admin.promotion.list")} - Powered By ${setting.siteAuthor}</title>
    <meta name="author" content="${setting.siteAuthor}" />
    <meta name="copyright" content="${setting.siteCopyright}" />
    <link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/list.js"></script>
    <script type="text/javascript" src="${base}/statics/lib/layer/layer.js"></script>
    <script type="text/javascript">
        $().ready(function() {

            [@flash_message /]



        });
    </script>
</head>
<body>
<div class="breadcrumb">
    <a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; ${message("admin.role.reverseGroup")} <span>(${message("admin.page.total", page.totalRow)})</span>
</div>
<form id="listForm" action="list.jhtml" method="post">
    <input type="text" class="hidden" value="${orderid}" name="orderid"/>
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
        <div id="searchPropertyMenu" class="dropdownMenu">
            <div class="search">
                <span class="arrow">&nbsp;</span>
                <input type="text" id="searchValue" name="pageable.searchValue" value="${pageable.searchValue}" maxlength="200" />
                <button type="submit">&nbsp;</button>
            </div>
            <ul>
                [#--<li[#if pageable.searchProperty == "name"] class="current"[/#if] val="name">${message("Promotion.name")}</li>--]
                <li[#if pageable.searchProperty == "title"] class="current"[/#if] val="title">${message("Promotion.title")}</li>
            </ul>
        </div>

        <a onclick="history.go(-1)" class=" iconButton">
            <span class="moveDirIcon"></span>${message("admin.back")}
        </a>

    </div>
    <table id="listTable" class="list">
        <tr>
            <th>
                <a href="javascript:;" class="sort" name="type">${message("PointLog.type")}</a>
            </th>

            <th>
                <a href="javascript:;" class="sort" name="mname">${message("aCaiwu.memberNick")}</a>
            </th>

            <th>
                <a href="javascript:;" class="sort" name="gv">${message("ReturnsItem.name")}</a>
            </th>

            <th>
                <a href="javascript:;" class="sort" name="auction_cost">${message("Product.cost")}</a>
            </th>


            <th>
                <a href="javascript:;" class="sort" name="auction_price">${message("reverseGroup.system.oldprice")}</a>
            </th>


            <th>
                <a href="javascript:;" class="sort" name="current_price">${message("reverseGroup.system.price")}</a>
            </th>

            <th>
                <a href="javascript:;" class="sort" name="current_price">${message("NewGoods.time")}</a>
            </th>

        </tr>
        [#list page.list as e]
            <tr>
                <td>
                    [#if type==0 ]
                        ${message("reverseGroup.system.normal")}
                        [#else ]
                        ${message("reverseGroup.system.buy")}
                    [/#if]
                </td>

                <td>
                    ${e.mname}
                </td>


                <td>
                ${e.gv}
                </td>

                <td>
                ${e.auction_cost}
                </td>

                <td>
                ${e.auction_price}
                </td>

                <td>
                ${e.current_price}
                </td>



                <td>
            ${e.create_date}

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


<script type="text/javascript">

        $('.xiajia').click(function () {
            nid=$(this).attr('nid');
            $.dialog({
                type: "warn",
                content: message("确定要停止次活动吗?"),
                ok: message("admin.dialog.ok"),
                cancel: message("admin.dialog.cancel"),
                onOk: function() {
                    $.post('xiajia.jhtml',{actId:nid},function (data) {
                        if(data.code==1){
                            location.reload()
                        }else {
                            layer.tips(data.msg)
                        }
                    })
                }
            });

        })


</script>

