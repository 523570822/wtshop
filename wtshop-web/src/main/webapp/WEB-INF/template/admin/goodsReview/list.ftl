[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN""http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <title>商品审核- Powered By ${setting.siteAuthor}</title>
    <meta name="author" content="${setting.siteAuthor}"/>
    <meta name="copyright" content="${setting.siteCopyright}"/>
    <link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/list.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/datePicker/WdatePicker.js"></script>
    <script type="text/javascript">
        $().ready(function () {
            [@flash_message /]
            var $listForm = $("#listForm");
            var $filterMenu = $("#filterMenu");
            var $filterMenuItem = $("#filterMenu li");
            // 筛选菜单
            $filterMenu.hover(
                    function() {
                        $(this).children("ul").show();
                    }, function() {
                        $(this).children("ul").hide();
                    }
            );
            $filterMenuItem.click(function() {
                var $this = $(this);
                var $dest = $("#" + $this.attr("name"));
                if ($this.hasClass("checked")) {
                    $dest.val("");
                } else {
                    $dest.val($this.attr("val"));
                }
                $listForm.submit();
            });

        });
    </script>
</head>
<body>
<div class="breadcrumb">
    <a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; ${message("admin.goodsVerify")} <span>(${message("admin.page.total", page.totalRow)})</span>
</div>
<form id="listForm" action="list.jhtml" method="get">

[#-- 0/草稿, 2/待审核, 4/待产品主管审核, 6/待财务审核, 7/待财务主管审核, 8/上线 --]
    [#assign State_Draft=0 State_Review_ProductSpecialist=2 State_Review_ProductManager=4 State_Review_Financial=6 State_Review_FinanceDirector=7 State_Publish=8 ]


    <input type="hidden" id="isDelete" name="isDelete" value="${(isDelete?string("true", "false"))!}" />
    <input type="hidden" id="verifyResult" name="verifyResult" value="${verifyResult}"/>

    <div class="bar">
        <div class="buttonGroup">
            <a href="javascript:;" id="refreshButton" class="iconButton">
                <span class="refreshIcon">&nbsp;</span>${message("admin.common.refresh")}
            </a>
            <div id="filterMenu" class="dropdownMenu">
                <a href="javascript:;" class="button">
                    筛选<span class="arrow">&nbsp;</span>
                </a>
                <ul class="check">
                    <li name="isDelete"[#if isDelete?? && !isDelete] class="checked"[/#if] val="false">未删除</li>
                    <li name="isDelete"[#if isDelete?? && isDelete] class="checked"[/#if] val="true">已删除</li>
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
        [#--<div id="searchPropertyMenu" class="dropdownMenu">--]
            [#--<div class="search">--]
                [#--<span class="arrow">&nbsp;</span>--]
                [#--<input type="text" id="searchValue" name="pageable.searchValue" value="${pageable.searchValue}"  maxlength="200"/>--]
                [#--<button type="submit">&nbsp;</button>--]
            [#--</div>--]
        [#--</div>--]

    </div>
    <table id="listTable" class="list">
        <tr>

            <th>
                <a href="javascript:;" class="" name="">编号</a>
            </th>
            <th>
                <a href="javascript:;" class="" name="">名称</a>
            </th>
            <th>
                <a href="javascript:;" class="" name="">商品分类</a>
            </th>
            <th>
                <a href="javascript:;" class="" name="">销售价</a>
            </th>
            <th>
                <a href="javascript:;" class="" name="">申请日期</a>
            </th>
            <th>
                <a href="javascript:;" class="" name="">申请人</a>
            </th>
            <th>
                <a href="javascript:;" class="" name="">申请人账号</a>
            </th>
            <th>
                <a href="javascript:;" class="" name="">审核状态</a>
            </th>
            <th>
                <a href="javascript:;" class="sort" name="">${message("Footprint.action")}</a>
            </th>

        </tr>
        [#list page.list as goodsReview]
            <tr>
                <td>
                ${goodsReview.goods.sn}
                </td>
                <td>
                ${goodsReview.goods.name}
                </td>
                <td>
                ${goodsReview.goods.productCategory.name}
                </td>
                <td>
                ${currency(goodsReview.goods.price, true)}
                </td>
                <td>
                ${goodsReview.createDate?string("yyyy-MM-dd HH:mm:ss")}
                </td>
                <td>
                ${goodsReview.admin.name}
                </td>
                <td>
                ${goodsReview.admin.username}
                </td>
                <td>
                    [#if goodsReview.state == State_Draft]
                        <span class="red">待审核</span>
                    [#elseif goodsReview.state == State_Review_ProductSpecialist]
                        <span class="black">打回待修改</span>
                    [#elseif goodsReview.state == State_Review_ProductManager]
                        <span class="black">待产品主管审核</span>
                    [#elseif goodsReview.state == State_Review_Financial]
                        <span class="black">待财务审核</span>
                    [#elseif goodsReview.state == State_Review_FinanceDirector]
                        <span class="black">待财务主管审核</span>
                    [#elseif goodsReview.state == State_Publish]
                        <span class="green">已完成</span>
                    [#else]
                        <span class="silver">未知状态</span>
                    [/#if]
                </td>
                <td>

                [#--产品--]
                    [@shiro.hasAnyRoles name="R_ProductSpecialist"]
                        [#if goodsReview.state == State_Draft || goodsReview.state == State_Review_ProductSpecialist]
                            <a href="edit.jhtml?id=${goodsReview.id}">[查看详情]</a>
                            <a href="javascript:void(0);" class="review" data-state="${State_Review_ProductManager}" data-id="${goodsReview.id}">[提交产品主管审核]</a>
                        [/#if]
                        [#if goodsReview.state == State_Review_ProductSpecialist]
                            <a href="javascript:void(0);" class="comment" data-id="${goodsReview.id}">[查看打回意见]</a>
                        [/#if]
                    [/@shiro.hasAnyRoles]
                [#--产品主管--]
                    [@shiro.hasAnyRoles name="R_ProductManager"]
                        [#if goodsReview.state == State_Review_ProductManager]
                            <a href="edit.jhtml?id=${goodsReview.id}">[查看详情]</a>
                            <a href="javascript:void(0);" class="review" data-state="${State_Review_Financial}" data-id="${goodsReview.id}">[提交财务审核]</a>
                            <a href="javascript:void(0);" class="reject" data-state="${State_Review_ProductSpecialist}" data-id="${goodsReview.id}">[打回]</a>
                        [/#if]
                    [/@shiro.hasAnyRoles]
                [#--财务--]
                    [@shiro.hasAnyRoles name="R_Finance"]
                        [#if goodsReview.state == State_Review_Financial]
                            <a href="edit.jhtml?id=${goodsReview.id}">[查看详情]</a>
                            <a href="javascript:void(0);" class="review" data-state="${State_Review_FinanceDirector}" data-id="${goodsReview.id}">[提交财务主管审核]</a>
                            <a href="javascript:void(0);" class="reject" data-state="${State_Review_ProductSpecialist}" data-id="${goodsReview.id}">[打回]</a>
                        [/#if]
                    [/@shiro.hasAnyRoles]
                [#--财务主管--]
                    [@shiro.hasAnyRoles name="R_FinanceDirector"]
                        [#if goodsReview.state == State_Review_FinanceDirector]
                            <a href="edit.jhtml?id=${goodsReview.id}">[查看详情]</a>
                            <a href="javascript:void(0);" class="reject" data-state="${State_Review_ProductSpecialist}" data-id="${goodsReview.id}">[打回]</a>
                            [#--<a href="javascript:void(0);" class="reviewPass" data-id="${goodsReview.id}">[审核通过并同步]</a>--]
                        [/#if]
                    [/@shiro.hasAnyRoles]
                [#--产品--]
                    [@shiro.hasAnyRoles name="R_ProductSpecialist"]
                        <a href="javascript:void(0);" class="reviewClose" data-id="${goodsReview.id}">[删除]</a>
                    [/@shiro.hasAnyRoles]


                </td>
            </tr>
        [/#list]
    </table>
    [@pagination pageNumber = page.pageNumber totalPages = page.totalPage]
        [#include "admin/include/pagination.ftl"]
    [/@pagination]
</form>

<script type="text/javascript">
    //  提交审核
    $(".review").click(function () {
        state = $(this).attr('data-state');
        id = $(this).attr('data-id');
        $.dialog({
            type: "warn",
            content: '是否要提交审核?',
            ok: message("admin.dialog.ok"),
            cancel: message("admin.dialog.cancel"),
            onOk: function () {
                $.post('review.jhtml', {id: id, state: state}, function (data) {
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
        id = $(this).attr('data-id');
        $.dialog({
            title: "打回",
            width: 470,
            modal: true,
            content: '<table class="moreTable"> <tbody> <tr> <th>打回意见:</th> <td><textarea name="comment" id="comment" class="text" value="" rows="6" maxlength="200"></textarea></td> </tr> </tbody> </table>',
            ok: message("admin.dialog.ok"),
            cancel: message("admin.dialog.cancel"),
            onOk: function () {
                $.post('reject.jhtml', {id: id, state: state, comment: $("#comment").val()}, function (data) {
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
        id = $(this).attr('data-id');
        $.post('comment.jhtml', {id: id}, function (resp) {
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

    //  关闭审批
    $('.reviewClose').click(function () {
        id = $(this).attr('data-id');
        $.dialog({
            type: "warn",
            content: '是否删除当前申请?',
            ok: message("admin.dialog.ok"),
            cancel: message("admin.dialog.cancel"),
            onOk: function () {
                $.post('reviewClose.jhtml', {id: id}, function (data) {
                    if (data.code == 1) {
                        location.reload()
                    } else {
                        layer.tips(data.msg)
                    }
                })
            }
        });
    })

    //  通过审批
    $(".reviewPass").click(function () {
        id = $(this).attr('data-id');
        $.dialog({
            type: "warn",
            content: '是否通过申请并同步',
            ok: message("admin.dialog.ok"),
            cancel: message("admin.dialog.cancel"),
            onOk: function () {
                $.post('reviewPass.jhtml', {id: id}, function (data) {
                    if (data.code == 1) {
                        location.reload()
                    } else {
                        $.message("warn", data.msg);
                    }
                })
            }
        });
    });

</script>
</body>
</html>
[/#escape]