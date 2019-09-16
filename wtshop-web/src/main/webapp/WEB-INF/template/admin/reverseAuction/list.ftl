[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <title>${message("admin.reverseAuction.list")} - Powered By ${setting.siteAuthor}</title>
    <meta name="author" content="${setting.siteAuthor}"/>
    <meta name="copyright" content="${setting.siteCopyright}"/>
    <link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/list.js"></script>
    <script type="text/javascript" src="${base}/statics/lib/layer/layer.js"></script>
    <script type="text/javascript">
        $().ready(function () {
            [@flash_message /]
        });
    </script>
</head>
<body>
<div class="breadcrumb">
    <a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; ${message("admin.role.reverseGroup")}
    <span>(${message("admin.page.total", page.totalRow)})</span>
</div>
<form id="listForm" action="list.jhtml" method="post">
    <div class="bar">

        [#--0/停止, 1/草稿, 2/待审核, 4/待产品主管审核, 6/待财务审核, 7/待财务主管审核, 8/上线, 10/已完成--]
        [#assign State_Stop=0 State_Draft=1 State_Review_ProductSpecialist=2 State_Review_ProductManager=4 State_Review_Financial=6 State_Review_FinanceDirector=7 State_Publish=8 State_Current=9 State_Finished=10]

        [@shiro.hasAnyRoles name="R_ProductSpecialist,R_ProductManager"]
            <a href="add.jhtml" class="iconButton">
                <span class="addIcon">&nbsp;</span>${message("admin.common.add")}
            </a>
        [/@shiro.hasAnyRoles]

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
                <input type="text" id="searchValue" name="pageable.searchValue" value="${pageable.searchValue}" maxlength="200"/>
                <button type="submit">&nbsp;</button>
            </div>
            <ul>
                <li[#if pageable.searchProperty == "title"] class="current"[/#if] val="title">${message("Promotion.title")}</li>
            </ul>
        </div>

        <a href="config.jhtml" id="configDappai" class="iconButton">
            <span class="editIcon">&nbsp;</span>${message("reverseGroup.config")}
        </a>
    </div>
    <table id="listTable" class="list">
        <tr>
            <th>
                <a href="javascript:;" class="sort" name="title">${message("Promotion.title")}</a>
            </th>
            <th>
                <a href="javascript:;" >${message("reverseGroup.goods.count")}</a>
            </th>
            <th>
                <a href="javascript:;" class="sort" name="end_date">${message("Order.status")}</a>
            </th>
            <th>
                <a href="javascript:;" class="sort" name="orders">${message("admin.common.order")}</a>
            </th>
            <th>
                <span>${message("admin.common.action")}</span>
            </th>
        </tr>
        [#list page.list as reverseAuction]
            <tr>
                <td>
                    <span title="${reverseAuction.title}">${abbreviate(reverseAuction.title, 50, "...")}</span>
                </td>
                <td>
                    <span title="">共 ${reverseAuction.reverseAuctionDetails?size} 件商品</span>
                </td>
                <td>
                    [#if reverseAuction.state == State_Stop]
                        <span class="red">已结束</span>
                    [#elseif reverseAuction.state == State_Draft]
                        <span class="black">草稿</span>
                    [#elseif reverseAuction.state == State_Review_ProductSpecialist]
                        <span class="black">打回待修改</span>
                    [#elseif reverseAuction.state == State_Review_ProductManager]
                        <span class="black">待产品主管审核</span>
                    [#elseif reverseAuction.state == State_Review_Financial]
                        <span class="black">待财务审核</span>
                    [#elseif reverseAuction.state == State_Review_FinanceDirector]
                        <span class="black">待财务主管审核</span>
                    [#elseif reverseAuction.state == State_Publish]
                        <span class="green">已发布</span>
                    [#elseif reverseAuction.state == State_Current]
                        <span class="blue">当前期</span>
                    [#elseif reverseAuction.state == State_Finished]
                        <span class="silver">已完成</span>
                    [#else]
                        <span class="silver">未知状态</span>
                    [/#if]
                </td>

                <td>
                    ${reverseAuction.orders}
                </td>
                <td>

                    [#--产品--]
                    [@shiro.hasAnyRoles name="R_ProductSpecialist"]
                        [#if reverseAuction.state == State_Draft || reverseAuction.state == State_Review_ProductSpecialist]
                            <a href="edit.jhtml?id=${reverseAuction.id}">[${message("admin.common.edit")}]</a>
                            <a href="editGoods.jhtml?id=${reverseAuction.id}">[${message("admin.goods.edit")}]</a>
                            <a href="javascript:void(0);"  class="delete" data-id="${reverseAuction.id}">[删除]</a>
                            <a href="javascript:void(0);" class="review" data-state="${State_Review_ProductManager}" data-id="${reverseAuction.id}">[提交产品主管审核]</a>
                        [/#if]
                        [#if reverseAuction.state == State_Review_ProductSpecialist]
                            <a href="javascript:void(0);" class="comment" data-id="${reverseAuction.id}">[查看打回意见]</a>
                        [/#if]
                    [/@shiro.hasAnyRoles]
                    [#--产品主管--]
                    [@shiro.hasAnyRoles name="R_ProductManager"]
                        [#if reverseAuction.state == State_Review_ProductManager]
                            <a href="edit.jhtml?id=${reverseAuction.id}">[${message("admin.common.edit")}]</a>
                            <a href="editGoods.jhtml?id=${reverseAuction.id}">[${message("admin.goods.edit")}]</a>
                            <a href="javascript:void(0);" class="review" data-state="${State_Review_Financial}" data-id="${reverseAuction.id}">[提交财务审核]</a>
                            <a href="javascript:void(0);" class="reject" data-state="${State_Review_ProductSpecialist}" data-id="${reverseAuction.id}">[打回]</a>
                        [/#if]
                    [/@shiro.hasAnyRoles]
                    [#--财务--]
                    [@shiro.hasAnyRoles name="R_Finance"]
                        [#if reverseAuction.state == State_Review_Financial]
                            <a href="edit.jhtml?id=${reverseAuction.id}">[${message("admin.common.edit")}]</a>
                            <a href="editGoods.jhtml?id=${reverseAuction.id}">[${message("admin.goods.edit")}]</a>
                            <a href="javascript:void(0);" class="review" data-state="${State_Review_FinanceDirector}" data-id="${reverseAuction.id}">[提交财务主管审核]</a>
                            <a href="javascript:void(0);" class="reject" data-state="${State_Review_ProductSpecialist}" data-id="${reverseAuction.id}">[打回]</a>
                        [/#if]
                    [/@shiro.hasAnyRoles]
                    [#--财务主管--]
                    [@shiro.hasAnyRoles name="R_FinanceDirector"]
                        [#if reverseAuction.state == State_Review_FinanceDirector]
                            <a href="edit.jhtml?id=${reverseAuction.id}">[${message("admin.common.edit")}]</a>
                            <a href="editGoods.jhtml?id=${reverseAuction.id}">[${message("admin.goods.edit")}]</a>
                            <a href="javascript:void(0);" class="reject" data-state="${State_Review_ProductSpecialist}" data-id="${reverseAuction.id}">[打回]</a>
                            <a href="javascript:void(0);" class="online" data-id="${reverseAuction.id}">[活动上线]</a>
                        [/#if]
                        [#if reverseAuction.state > State_Review_FinanceDirector && reverseAuction.state < State_Finished]
                            <a href="javascript:void(0);" class="offline" data-id="${reverseAuction.id}">[活动下线]</a>
                        [/#if]
                    [/@shiro.hasAnyRoles]

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

    //  活动删除
    $('.delete').click(function () {
        auctionId = $(this).attr('data-id');
        $.dialog({
            type: "warn",
            content: '是否删除活动?',
            ok: message("admin.dialog.ok"),
            cancel: message("admin.dialog.cancel"),
            onOk: function () {
                $.post('delete.jhtml', {ids: auctionId}, function (data) {
                    if (data.code == 1) {
                        location.reload()
                    } else {
                        layer.tips(data.msg)
                    }
                })
            }
        });
    })

    //  活动下线
    $('.offline').click(function () {
        auctionId = $(this).attr('data-id');
        $.dialog({
            type: "warn",
            content: message("${message("reverseGroup.goods.desc")}"),
            ok: message("admin.dialog.ok"),
            cancel: message("admin.dialog.cancel"),
            onOk: function () {
                $.post('offline.jhtml', {auctionId: auctionId}, function (data) {
                    if (data.code == 1) {
                        location.reload()
                    } else {
                        layer.tips(data.msg)
                    }
                })
            }
        });
    })

    //  活动上线
    $(".online").click(function () {
        auctionId = $(this).attr('data-id');
        $.dialog({
            type: "warn",
            content: message("${message("reverseGroup.goods.ture")}"),
            ok: message("admin.dialog.ok"),
            cancel: message("admin.dialog.cancel"),
            onOk: function () {
                $.post('online.jhtml', {auctionId: auctionId}, function (data) {
                    if (data.code == 1) {
                        location.reload()
                    } else {
                        $.message("warn", data.msg);
                    }
                })
            }
        });
    });

    //  提交审核
    $(".review").click(function () {
        state = $(this).attr('data-state');
        auctionId = $(this).attr('data-id');
        $.dialog({
            type: "warn",
            content: '是否要提交审核?',
            ok: message("admin.dialog.ok"),
            cancel: message("admin.dialog.cancel"),
            onOk: function () {
                $.post('review.jhtml', {auctionId: auctionId, state: state}, function (data) {
                    if (data.code == 1) {
                        location.reload()
                    } else {
                        $.message("warn", data.msg);
                    }
                })
            }
        });
    });

    //  打回审核
    $(".reject").click(function () {
        state = $(this).attr('data-state');
        auctionId = $(this).attr('data-id');
        $.dialog({
            title: "打回",
            width: 470,
            modal: true,
            content: '<table class="moreTable"> <tbody> <tr> <th>打回意见:</th> <td><textarea name="comment" id="comment" class="text" value="" rows="6" maxlength="200"></textarea></td> </tr> </tbody> </table>',
            ok: message("admin.dialog.ok"),
            cancel: message("admin.dialog.cancel"),
            onOk: function () {
                $.post('reject.jhtml', {auctionId: auctionId, state: state, comment: $("#comment").val()}, function (data) {
                    if (data.code == 1) {
                        location.reload()
                    } else {
                        $.message("warn", data.msg);
                    }
                })
            }
        });
    });

    //  查看审核
    $(".comment").click(function () {
        auctionId = $(this).attr('data-id');
        $.post('comment.jhtml', {auctionId: auctionId}, function (resp) {
            var tds = '';
            for(var i = 0; i < resp.data.length; i ++){
                var item = resp.data[i];
                tds += ('<tr><td>' + item.admin_name + '</td>' + '<td>' + item.create_time + '</td>' + '<td>' + item.comment + '</td></tr>');
            }
            var tables = '<div style="height: 300px;overflow-y: auto;"><table class="list"> <tbody> <tr> <th style="width: 80px;">审核人</th> <th style="width: 80px;">审核时间</th> <th>审核意见</th> </tr> ' + tds + ' </tbody> </table></div>';
            $.dialog({
                title: "打回意见",
                width: "700",
                modal: true,
                content: tables,
                ok: message("admin.dialog.ok"),
                cancel: message("admin.dialog.cancel")
            });
        })
    });


</script>

