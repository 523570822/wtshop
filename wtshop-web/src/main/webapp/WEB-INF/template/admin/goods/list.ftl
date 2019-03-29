[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>${message("admin.goods.list")} - Powered By ${setting.siteAuthor}</title>
<meta name="author" content="${setting.siteAuthor}" />
<meta name="copyright" content="${setting.siteCopyright}" />
<link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/list.js"></script>
<script type="text/javascript" src="${base}/statics/lib/layer/layer.js"></script>
<style type="text/css">

.stockAlert {
	color: orange;
}

.outOfStock {
	color: red;
	font-weight: bold;
}





</style>
<script type="text/javascript">
    function getId(item) {
		var val = $(item).parent().siblings("td:nth-child(1)").find("input").val();
		layer.alert("该商品标识符为  "+val);
    }

$().ready(function() {

	var $listForm = $("#listForm");
	var $filterMenu = $("#filterMenu");
	var $filterMenuItem = $("#filterMenu li");
	var $moreButton = $("#moreButton");
	
	[@flash_message /]
	
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
	
	// 更多选项
	$moreButton.click(function() {
		$.dialog({
			title: "${message("admin.goods.moreOption")}",
			[@compress single_line = true]
				content: '
				<table id="moreTable" class="moreTable">
					<tr>
						<th>
							${message("Goods.productCategory")}:
						<\/th>
						<td>
							<select name="productCategoryId">
								<option value="">${message("admin.common.choose")}<\/option>
								[#list productCategoryTree as productCategory]
									<option value="${productCategory.id}"[#if productCategory.id == productCategoryId] selected="selected"[/#if]>
										[#if productCategory.grade != 0]
											[#list 1..productCategory.grade as i]
												&nbsp;&nbsp;
											[/#list]
										[/#if]
										[#noescape]
											${productCategory.name?html?js_string}
										[/#noescape]
									<\/option>
								[/#list]
							<\/select>
						<\/td>
					<\/tr>
					<tr>
						<th>
							${message("Goods.type")}:
						<\/th>
						<td>
							<select name="type">
								<option value="">${message("admin.common.choose")}<\/option>
								[#list types as value]
									<option value="${value}"[#if value == type] selected="selected"[/#if]>${message("Goods.Type." + value)}<\/option>
								[/#list]
							<\/select>
						<\/td>
					<\/tr>
					<tr>
						<th>
							${message("Goods.brand")}:
						<\/th>
						<td>
							<select name="brandId">
								<option value="">${message("admin.common.choose")}<\/option>
								[#list brands as brand]
									<option value="${brand.id}"[#if brand.id == brandId] selected="selected"[/#if]>
										[#noescape]
											${brand.name?html?js_string}
										[/#noescape]
									<\/option>
								[/#list]
							<\/select>
						<\/td>
					<\/tr>
					<tr>
						<th>
							${message("Goods.tags")}:
						<\/th>
						<td>
							<select name="tagId">
								<option value="">${message("admin.common.choose")}<\/option>
								[#list tags as tag]
									<option value="${tag.id}"[#if tag.id == tagId] selected="selected"[/#if]>
										[#noescape]
											${tag.name?html?js_string}
										[/#noescape]
									<\/option>
								[/#list]
							<\/select>
						<\/td>
					<\/tr>
					<tr>
						<th>
							${message("Goods.promotions")}:
						<\/th>
						<td>
							<select name="promotionId">
								<option value="">${message("admin.common.choose")}<\/option>
								[#list promotions as promotion]
									<option value="${promotion.id}"[#if promotion.id == promotionId] selected="selected"[/#if]>
										[#noescape]
											${promotion.name?html?js_string}
										[/#noescape]
									<\/option>
								[/#list]
							<\/select>
						<\/td>
					<\/tr>
				<\/table>',
			[/@compress]
			width: 470,
			modal: true,
			ok: "${message("admin.dialog.ok")}",
			cancel: "${message("admin.dialog.cancel")}",
			onOk: function() {
				$("#moreTable :input").each(function() {
					var $this = $(this);
					$("#" + $this.attr("name")).val($this.val());
				});
				$listForm.submit();
			}
		});
	});

    $("#listForm").bind("submit",function () {
		var val = $("#searchValue").val();
		if(val.length > 100){
		   return false;
		}
    });

	$('.verifyState').click(function () {
        var nid=$(this).attr('nid');
		$.get('${base}/admin/msyApply/add.jhtml?goodsId='+nid,{},function () {
			location.reload();
        });
	});





});
</script>
</head>
<body>
	<div class="breadcrumb">
		<a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; ${message("admin.goods.list")} <span>(${message("admin.page.total", page.totalRow)})</span>
	</div>
	<form id="listForm" action="list.jhtml" method="get">
		<input type="hidden" id="type" name="type" value="${type}" />
		<input type="hidden" id="productCategoryId" name="productCategoryId" value="${productCategoryId}" />
		<input type="hidden" id="brandId" name="brandId" value="${brandId}" />
		<input type="hidden" id="tagId" name="tagId" value="${tagId}" />
		<input type="hidden" id="promotionId" name="promotionId" value="${promotionId}" />
		<input type="hidden" id="isMarketable" name="isMarketable" value="${(isMarketable?string("true", "false"))!}" />
		<input type="hidden" id="isList" name="isList" value="${(isList?string("true", "false"))!}" />
		<input type="hidden" id="isTop" name="isTop" value="${(isTop?string("true", "false"))!}" />
		<input type="hidden" id="isOutOfStock" name="isOutOfStock" value="${(isOutOfStock?string("true", "false"))!}" />
		<input type="hidden" id="isStockAlert" name="isStockAlert" value="${(isStockAlert?string("true", "false"))!}" />
        <input type="hidden" id="isVip" name="isVip" value="${(isVip?string("true", "false"))!}" />
		<div class="bar">

		[#-- 0/草稿, 2/待审核, 4/待产品主管审核, 6/待财务审核, 7/待财务主管审核, 8/上线 --]
			[#assign State_Draft=0 State_Review_ProductSpecialist=2 State_Review_ProductManager=4 State_Review_Financial=6 State_Review_FinanceDirector=7 State_Publish=8 ]

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


			</div>

		</div>
		<table id="listTable" class="list">
			<tr>
				<th class="check">
					<input type="checkbox" id="selectAll" />
				</th>
				<th>
					<a href="javascript:;" class="sort" name="sn">${message("Goods.sn")}</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="name">标题</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="product_category_id">分类</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="is_marketable">是否显示</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="create_date">${message("admin.common.createDate")}</a>
				</th>
                <th>
                    <span>${message("admin.seo.IP")}</span>
                </th>
				<th>
					<span>${message("admin.common.action")}</span>
				</th>
			</tr>
			[#list page.list as goods]
				<tr class="getId">
					<td>
						<input type="checkbox" name="ids" value="${goods.id}" />
					</td>
					<td>
						<span[#if goods.isOutOfStock] class="red"[#elseif goods.isStockAlert] class="blue"[/#if]>
							${goods.sn}
						</span>
					</td>
					<td>
						<span title="${goods.name}">
							${abbreviate(goods.name, 30, "...")}
						</span>
						[#if goods.typeName == "auction"]
							<span class="red">*倒拍商品</span>
						[/#if]
						[#list goods.validPromotions as promotion]
							<span class="promotion" title="${promotion.title}">${promotion.name}</span>
						[/#list]
					</td>
					<td>
						${goods.productCategory.name}
					</td>
					<td>
						<span class="${goods.isMarketable?string("true", "false")}Icon">&nbsp;</span>
					</td>
                    <td>
                        <span title="${goods.createDate?string("yyyy-MM-dd HH:mm:ss")}">${goods.createDate}</span>
                    </td>
					<td>
						${goods.operate_ip}
					</td>
					<td>

						[#if goods.check > State_Review_FinanceDirector ]
                            <a href="edit.jhtml?id=${goods.id}">[修改]</a>
                            <a onclick="getId(this);return false;"">[${message("admin.common.ids")}]</a>
						&nbsp;|&nbsp;
						[/#if]

						[#--产品--]
						[@shiro.hasAnyRoles name="R_ProductSpecialist"]
							[#if goods.check == State_Draft || goods.check == State_Review_ProductSpecialist]
                                <a href="edit.jhtml?id=${goods.id}${urlParam}">[${message("admin.common.edit")}]</a>
                                <a href="javascript:void(0);" class="review" data-state="${State_Review_ProductManager}" data-id="${goods.id}">[提交产品主管审核]</a>
							[/#if]
							[#if goods.check == State_Review_ProductSpecialist]
                                <a href="javascript:void(0);" class="comment" data-id="${goods.id}">[查看打回意见]</a>
							[/#if]
						[/@shiro.hasAnyRoles]
					[#--产品主管--]
						[@shiro.hasAnyRoles name="R_ProductManager"]
							[#if goods.check == State_Review_ProductManager]
                                <a href="edit.jhtml?id=${goods.id}${urlParam}">[${message("admin.common.edit")}]</a>
                                <a href="javascript:void(0);" class="review" data-state="${State_Review_Financial}" data-id="${goods.id}">[提交财务审核]</a>
                                <a href="javascript:void(0);" class="reject" data-state="${State_Review_ProductSpecialist}" data-id="${goods.id}">[打回]</a>
							[/#if]
						[/@shiro.hasAnyRoles]
					[#--财务--]
						[@shiro.hasAnyRoles name="R_Finance"]
							[#if goods.check == State_Review_Financial]
                                <a href="edit.jhtml?id=${goods.id}${urlParam}">[${message("admin.common.edit")}]</a>
                                <a href="javascript:void(0);" class="review" data-state="${State_Review_FinanceDirector}" data-id="${goods.id}">[提交财务主管审核]</a>
                                <a href="javascript:void(0);" class="reject" data-state="${State_Review_ProductSpecialist}" data-id="${goods.id}">[打回]</a>
							[/#if]
						[/@shiro.hasAnyRoles]
					[#--财务主管--]
						[@shiro.hasAnyRoles name="R_FinanceDirector"]
							[#if goods.check == State_Review_FinanceDirector]
                                <a href="edit.jhtml?id=${goods.id}${urlParam}">[${message("admin.common.edit")}]</a>
                                <a href="javascript:void(0);" class="reject" data-state="${State_Review_ProductSpecialist}" data-id="${goods.id}">[打回]</a>
                                <a href="javascript:void(0);" class="online" data-id="${goods.id}">[商品上架]</a>
							[/#if]
							[#if goods.check > State_Review_FinanceDirector ]
                                <a href="javascript:void(0);" class="offline" data-id="${goods.id}">[商品下架]</a>
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


    <script type="text/javascript">
        //  提交审核
        $(".review").click(function () {
            state = $(this).attr('data-state');
            goodsId = $(this).attr('data-id');
            $.dialog({
                type: "warn",
                content: '是否要提交审核?',
                ok: message("admin.dialog.ok"),
                cancel: message("admin.dialog.cancel"),
                onOk: function () {
                    $.post('review.jhtml', {id: goodsId, state: state}, function (data) {
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
            goodsId = $(this).attr('data-id');
            $.dialog({
                title: "打回",
                width: 470,
                modal: true,
                content: '<table class="moreTable"> <tbody> <tr> <th>打回意见:</th> <td><textarea name="comment" id="comment" class="text" value="" rows="6" maxlength="200"></textarea></td> </tr> </tbody> </table>',
                ok: message("admin.dialog.ok"),
                cancel: message("admin.dialog.cancel"),
                onOk: function () {
                    $.post('reject.jhtml', {id: goodsId, state: state, comment: $("#comment").val()}, function (data) {
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
            goodsId = $(this).attr('data-id');
            $.post('comment.jhtml', {id: goodsId}, function (resp) {
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

        //  商品下架
        $('.offline').click(function () {
            goodsId = $(this).attr('data-id');
            $.dialog({
                type: "warn",
                content: '是否下架商品?',
                ok: message("admin.dialog.ok"),
                cancel: message("admin.dialog.cancel"),
                onOk: function () {
                    $.post('offline.jhtml', {id: goodsId}, function (data) {
                        if (data.code == 1) {
                            location.reload()
                        } else {
                            layer.tips(data.msg)
                        }
                    })
                }
            });
        })

        //  商品上架
        $(".online").click(function () {
            goodsId = $(this).attr('data-id');
            $.dialog({
                type: "warn",
                content: '是否上架商品',
                ok: message("admin.dialog.ok"),
                cancel: message("admin.dialog.cancel"),
                onOk: function () {
                    $.post('online.jhtml', {id: goodsId}, function (data) {
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