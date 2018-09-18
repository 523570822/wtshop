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
</head>
<body>
<input type="hidden" id="reverseAuctionId" value="${reverseAuctionId}"/>
<input type="hidden" id="flag" value="${flag}">
<div class="breadcrumb">
    <a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; 商品列表 <span>(${message("admin.page.total", page.totalRow)})</span>
</div>
<form id="listForm" action="choose.jhtml" method="post">
    <input type="hidden" name="flag" value="${flag}">
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
    <div class="bar">
        <div class="buttonGroup">
            [#--<div id="filterMenu" class="dropdownMenu">--]
                [#--<a href="javascript:;" class="button">--]
                [#--${message("admin.goods.filter")}<span class="arrow">&nbsp;</span>--]
                [#--</a>--]
                [#--<ul class="check">--]
                    [#--<li name="isMarketable"[#if isMarketable?? && isMarketable] class="checked"[/#if] val="true">${message("admin.goods.isMarketable")}</li>--]
                    [#--<li name="isMarketable"[#if isMarketable?? && !isMarketable] class="checked"[/#if] val="false">${message("admin.goods.notMarketable")}</li>--]
                    [#--<li class="divider">&nbsp;</li>--]
                    [#--<li name="isList"[#if isList?? && isList] class="checked"[/#if] val="true">${message("admin.goods.isList")}</li>--]
                    [#--<li name="isList"[#if isList?? && !isList] class="checked"[/#if] val="false">${message("admin.goods.notList")}</li>--]
                    [#--<li class="divider">&nbsp;</li>--]
                    [#--<li name="isTop"[#if isTop?? && isTop] class="checked"[/#if] val="true">${message("admin.goods.isTop")}</li>--]
                    [#--<li name="isTop"[#if isTop?? && !isTop] class="checked"[/#if] val="false">${message("admin.goods.notTop")}</li>--]
                    [#--<li class="divider">&nbsp;</li>--]
                    [#--<li name="isOutOfStock"[#if isOutOfStock?? && !isOutOfStock] class="checked"[/#if] val="false">${message("admin.goods.isStack")}</li>--]
                    [#--<li name="isOutOfStock"[#if isOutOfStock?? && isOutOfStock] class="checked"[/#if] val="true">${message("admin.goods.isOutOfStack")}</li>--]
                    [#--<li class="divider">&nbsp;</li>--]
                    [#--<li name="isStockAlert"[#if isStockAlert?? && !isStockAlert] class="checked"[/#if] val="false">${message("admin.goods.normalStore")}</li>--]
                    [#--<li name="isStockAlert"[#if isStockAlert?? && isStockAlert] class="checked"[/#if] val="true">${message("admin.goods.isStockAlert")}</li>--]
                [#--</ul>--]
            [#--</div>--]

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
                <li[#if pageable.searchProperty == "name"] class="current"[/#if] val="name">${message("Goods.name")}</li>
            </ul>
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
                <a href="javascript:;" class="sort" name="goods_id">${message("Goods.name")}</a>
            </th>
            <th>
                <a href="javascript:;" class="sort" name="product_category_id">${message("Goods.productCategory")}</a>
            </th>
            <th>
                <a href="javascript:;" class="sort" name="price">${message("Goods.price")}</a>
            </th>
            <th>
                <a href="javascript:;" class="sort" name="is_marketable">${message("Goods.isMarketable")}</a>
            </th>
            <th>
                <a href="javascript:;" class="sort" name="create_date">${message("admin.common.createDate")}</a>
            </th>
            [#--<th>--]
                [#--<span>${message("admin.common.action")}</span>--]
            [#--</th>--]
        </tr>
        [#list page.list as product]
            <tr>
                <td>
                    <input type="checkbox" name="ids" value="${product.id}" />
                    <input type="hidden"  value="${product.goods.id}" />
                </td>
                <td>
						<span[#if product.goods.isOutOfStock] class="red"[#elseif product.goods.isStockAlert] class="blue"[/#if]>
                        ${product.goods.sn}
						</span>
                </td>
                <td>
						<span title="${product.goods.name}">
                        ${abbreviate(product.goods.name, 50, "...")}
						</span>
                    [#if product.goods.typeName != "general"]
                        <span class="red">*</span>
                    [/#if]
                    [#list product.goods.validPromotions as promotion]
                        <span class="promotion" title="${promotion.title}">${promotion.name}</span>
                    [/#list]
                </td>
                <td>
                ${product.goods.productCategory.name}
                </td>
                <td>
                ${currency(product.price, true)}
                </td>
                <td>
                    <span class="${product.goods.isMarketable?string("true", "false")}Icon">&nbsp;</span>
                </td>
                <td>
                    <span title="${product.goods.createDate?string("yyyy-MM-dd HH:mm:ss")}">${product.goods.createDate}</span>
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
<script type="text/javascript">
    var reverseAuctionId = parent.$("#reverseAuctionId").val();
    var goodsTheme = parent.$("#goodsThemeId").val();
    $().ready(function() {
        var $listForm = $("#listForm");
        var $filterMenu = $("#filterMenu");
        var $filterMenuItem = $("#filterMenu li");


        [@flash_message /]

        // 筛选菜单
        $filterMenu.hover(
                function() {
                    $(this).children("ul").show();
                }, function() {
                    $(this).children("ul").hide();
                }
        );

        // 筛选
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



        var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
        $('#listTable tr').click(function(){
            var productId = $(this).children('td').eq(0).children('input').val();
            var goodsName = $(this).children('td').eq(2).children('span').attr("title");
            var goodsId = $(this).children('td').eq(0).children('input').eq(1).val();
            var className = $(this).children('td').eq(3).html();
            var productImageIndex = parent.$('#detailNum').val();
            var num = parseInt(productImageIndex)+1;
            var flag = $("#flag").val();
            var productFlag = false;
            parent.$('#detailNum').val(num);
            if(flag != 0){
                parent.$(".productId").each(function () {console.log($(this).val())
                    if(productId==$(this).val()){
                        productFlag = true;
                        parent.layer.msg("已有相同规格产品");
                        parent.layer.close(index);
                        return;
                    }
                })
                if(productFlag){
                    return;
                }
            }

            if(flag==0){
            parent.$('#productImageTable').append(
                [@compress single_line = true]
                        '<tr>
                        <td>
                        '+goodsName+'
                        <input type="hidden" value="'+productId+'" name="reverseAuctionDetail['+productImageIndex+'].product_id" class="text productId" maxlength="200" />
                        <input type="hidden" value="'+reverseAuctionId+'" name="reverseAuctionDetail['+productImageIndex+'].reverse_auction_id" class="text" maxlength="200" />
                        </td>
                <td>
                    <input type="text" class="text auction_original_price" name="reverseAuctionDetail['+productImageIndex+'].auction_original_price" title="商品成本价格（显示用）" style="width:100px"\/>
                <\/td>
                <td>
                <input type="text" name="reverseAuctionDetail['+productImageIndex+'].auction_start_price" class="text auction_start_price" title="商品起拍价格" style="width:100px"\/>
                <\/td>
                <td>
                <input type="text" name="reverseAuctionDetail['+productImageIndex+'].auction_limit_up_price" class="text auction_limit_up_price" title="回收价格，当降价到此价格时，小概率回收" style="width:100px"\/>
                <\/td>
                <td>
                <input type="text" class="text auction_limit_down_price" name="reverseAuctionDetail['+productImageIndex+'].auction_limit_down_price" title="回收价格，当降价到此价格时，大概率回收" style="width:100px"\/>
                <\/td>
                <td>
                <input type="text" name="reverseAuctionDetail['+productImageIndex+'].total_num" class="text total_num"  value="1"  maxlength="100" style="width: 50px;" readonly \/>
                <\/td>
                <td>
                <a href="javascript:;" class="remove">[${message("admin.common.remove")}]<\/a>
                <\/td>
                <\/tr>'
                [/@compress]
            );
            }
            if(flag==1){//商品专题使用
            parent.$('#productImageTable').append(
                [@compress single_line = true]
                        '<tr>
                        <td>
                        '+goodsName+'
                        <input type="hidden" value="'+productId+'" name="themeProduct['+productImageIndex+'].product_id" class="text productId" maxlength="200" />
                        <input type="hidden" value="'+goodsTheme+'" name="themeProduct['+productImageIndex+'].goodsTheme_id" class="text" maxlength="200" />
                        </td>
                        <td>
                        <input type="number"class="text" style="width:80px" name="themeProduct['+productImageIndex+'].orders"/>
                        <\/td>
                        <td>
                        '+className+'
                        <\/td>
                        <td>
                        <a href="javascript:;" class="remove">[${message("admin.common.remove")}]<\/a>
                        <\/td>
                         <\/tr>'
                [/@compress]
            )
            }
            if(flag==2){
                parent.$("#product_id").val(productId);
                parent.$("#product_name").val(goodsName);
            }

            if(flag==5){

                parent.$("#product_id").val(productId);
                parent.$("#product_name").val(goodsName);

            }
            if(flag==6){

                parent.$("#goods_id").val(goodsId);
                parent.$("#goods_name").val(goodsName);

            }
            if(flag==3){//限时抢购使用
            parent.$('#productImageTable').append(
                [@compress single_line = true]
              '<tr>
                <td> '+goodsName+'
                    <input type="hidden" value="'+productId+'" name="flashsaleDetail['+productImageIndex+'].product_id" class="text productId" maxlength="200" />
                    <input type="hidden" value="'+goodsTheme+'" name="flashsaleDetail['+productImageIndex+'].flashsale_id" class="text" maxlength="200" />
                </td>
                <td> <input type="number"class="text" style="width:80px" name="flashsaleDetail['+productImageIndex+'].orders"/> <\/td>
                <td> '+className+' <\/td>
                <td> <input type="number"class="text num" style="width:80px" name="flashsaleDetail['+productImageIndex+'].num"/> <\/td>
                <td> <input type="number"class="text sale_num" style="width:80px" name="flashsaleDetail['+productImageIndex+'].sale_num"/> <\/td>
                <td> <a href="javascript:;" class="remove">[${message("admin.common.remove")}]<\/a> <\/td>
               <\/tr>'
                [/@compress]
            )
            }
            if(flag==4){//福袋产品使用
                parent.$('#productImageTable').append(
                [@compress single_line = true]
                        '<tr>
                        <td> '+goodsName+'
                        <input type="hidden" value="'+productId+'" name="fudaiProduct['+productImageIndex+'].product_id" class="text productId" maxlength="200" />
                        <input type="hidden" value="'+goodsTheme+'" name="fudaiProduct['+productImageIndex+'].fudai_id" class="text" maxlength="200" />
                        <input type="hidden" value="0" name="fudaiProduct['+productImageIndex+'].is_main" />
                        </td>
                        <td> '+className+' <\/td>
                        <td> <input type="number"class="text num" style="width:80px" title="商品抽取概率,大于0,小数点后两位,数字越大,概率越高"  name="fudaiProduct['+productImageIndex+'].probability" value="1"/> <\/td>
                        <td> <input type="number"class="text sale_num" style="width:80px" title="用户再一次抽取到该产品的最短时间,单位(秒)" name="fudaiProduct['+productImageIndex+'].repeatTime" value="0"/> <\/td>
                        <td>
                        <input type="number"      name="fudaiProduct['+productImageIndex+'].maxNum"  title="用户抽取一次抽到该商品最大数量"  class="text" maxlength="9" style="width: 80px;" value="0" \/>
                        <\/td>
                        <td>
                                    <span class= "isgrand">否</span>
                        <\/td>
                        <td>
                                <a href="javascript:;" class="remove">[${message("admin.common.remove")}]<\/a>
                                <a href="javascript:;" class="grandPrix">[设为大奖]<\/a>
                                <input type="hidden" name="fudaiProduct['+productImageIndex+'].grandPrix" value="0" class="grandPrixV"/>
                          <\/td>

                        <\/tr>'
                [/@compress]
            )
            }

                if(flag==5){//奖励产品使用
                parent.$('#productImageTable').append(
                [@compress single_line = true]
                        '<tr>
                        <td> '+goodsName+'
                        <input type="hidden" value="'+productId+'" name="activityProduct['+productImageIndex+'].product_id" class="text productId" maxlength="200" />
                        <input type="hidden" value="'+goodsTheme+'" name="activityProduct['+productImageIndex+'].activity_id" class="text" maxlength="200" />
                        <input type="hidden" value="0" name="activityProduct['+productImageIndex+'].is_main" />
                        </td>
                        <td> '+className+' <\/td>
                        <td> <input type="number"class="text num" style="width:80px" title="商品抽取概率,大于0,小数点后两位,数字越大,概率越高"  name="activityProduct['+productImageIndex+'].probability" value="1"/> <\/td>
                <td> <input  type="hidden"class="text sale_num" style="width:80px" title="用户再一次抽取到该产品的最短时间,单位(秒)" name="activityProduct['+productImageIndex+'].repeatTime" value="0"/>

                        <input type="hidden"      name="activityProduct['+productImageIndex+'].maxNum"  title="用户抽取一次抽到该商品最大数量"  class="text" maxlength="9" style="width: 80px;" value="0" \/>



                                <a href="javascript:;" class="remove">[${message("admin.common.remove")}]<\/a>

                                <input type="hidden" name="activityProduct['+productImageIndex+'].grandPrix" value="0" class="grandPrixV"/>
                          <\/td>

                        <\/tr>'
                [/@compress]
                    )
                    }

//                parent.$('#goodsId').text('我被改变了');
          //    parent.layer.msg('您将标记 [ ' +val + ' ] 成功传送给了父窗口');
//                productImageIndex++;
            parent.layer.close(index);
        });

        [@flash_message /]

    });
</script>
[/#escape]