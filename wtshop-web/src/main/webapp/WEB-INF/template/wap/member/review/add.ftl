[#include "/wap/include/header1.ftl" /]
<script type="text/javascript" src="${base}/statics/js/wtshop.validate.js?v=2.6.0.161014"></script>
<link type="text/css" rel="stylesheet" href="${base}/statics/js/upload/uploader.css?v=2.6.0.161014" />
<link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/statics/js/upload/uploader.js?v=2.6.0.161014"></script>
<script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/listReview.js"></script>
<script type="text/javascript" src="${base}/statics/lib/layer/layer.js"></script>
<style type="text/css">
    img{ width: 100%; height: auto;max-width: 100%; display: block; }
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




    } );

    function luan(sn,image,name,sales,ids){
        //$("#imgshuadan").src("${fileServer}"+image);
        $("#imgshuadan").attr("src","${fileServer}"+image);
        $("#bianhao").html(sn);
        $("#name").html(name);
        $("#sales").html(sales);
        $("#ids").val(ids);

        console.info(ids);

    }
    function shuaShuLiang(id){
        var sss= "#xssl"+id;
        var num=$(sss).val();
        console.info($(sss).val());
        console.info(id);
        var dataa={
            id:id,
            num:num
        }

       $.ajax({ url: "${base}/wap/member/review/updataXL.jhtml",data:dataa, success: function(){
               mui.toast('提交成功',{ duration:'long', type:'div' })
                $(this).addClass("done");
            }});
    }
</script>






	<div class="mui-content">

       [#-- <div class="breadcrumb">
            <a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; ${message("admin.goods.list")} <span>(${message("admin.page.total", page.totalRow)})</span>
        </div>--]
        <form id="listForm" action="add.jhtml" method="get">
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


                <div class="buttonGroup">

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
                        <li[#if pageable.searchProperty == "sn"] class="current"[/#if] val="sn">${message("Goods.sn")}</li>
                        <li[#if pageable.searchProperty == "name"] class="current"[/#if] val="name">${message("Goods.name")}</li>
                        <li[#if pageable.searchProperty == "keyword"] class="current"[/#if] val="keyword">${message("admin.seo.keyword")}</li>
                    </ul>
                </div>
            </div>
            <table id="listTable" class="list "  style="width: unset">
                <tr>
                    <th >
                      [#--  <input type="checkbox" id="selectAll" />--]

                   [#-- <th>
                        <a href="javascript:;" class="sort" name="sn">${message("Goods.sn")}</a>
                    </th>--]

                        <a href="javascript:;" class="sort" name="name">${message("Goods.name")}</a>
                    </th>
                    <th>
                        <a href="javascript:;" class="sort" name="image">${message("Goods.image")}</a>
                    </th>
                    <th>
                        <span>${message("admin.common.action")}</span>
                    </th>
                    <th>
                        <a href="javascript:;" class="sort" name="price">${message("Goods.price")}</a>
                    </th>

                   [#-- <th>
                        <a href="javascript:;" class="sort" name="price">${message("Goods.sales")}</a>
                    </th>--]

                    <th>
                        <span>${message("admin.status.operator")}</span>
                    </th>

                </tr>
			[#list page.list as goods]
				<tr >
                    <td>
                        <input  onclick="luan('${goods.sn}','${goods.image}','${goods.name}','${goods.sales}','${goods.id}')"  type="radio" id="male" name="ids" value="${goods.id}" />

                    [#--   </td>
                   <td>
                         <span[#if goods.isOutOfStock] class="red"[#elseif goods.isStockAlert] class="blue"[/#if]>
                             ${goods.sn}
                         </span>
                     </td>
                    <td>--]

                            <label for="male">
							${abbreviate(goods.name, 30, "...")}
                                </label>

                    </td>
<td height="30px">
    <img style="height:30px;width: unset"   src="${fileServer}${goods.image}" style=" overflow:scroll;">
</td>
                    <td>


                        <input id="xssl${goods.id}" style="height: 100%;width: 35%" type="text" value="${goods.sales}" name="sales" placeholder="销售数量">
                        <button  style="width: 35%" onclick="shuaShuLiang('${goods.id}')" type="button" class="mui-btn mui-btn-primary" >确认</button>








                    </td>
                    <td>
						${currency(goods.price, true)}
                    </td>
                  [#--  <td>
                        ${goods.sales}
                    </td>--]

                    <td>
						[#if goods.check == State_Draft]
                            <span class="red">草稿</span>
						[#elseif goods.check == State_Review_ProductSpecialist]
                            <span class="black">打回待修改</span>
						[#elseif goods.check == State_Review_ProductManager]
                            <span class="black">待产品主管审核</span>
						[#elseif goods.check == State_Review_Financial]
                            <span class="black">待财务审核</span>
						[#elseif goods.check == State_Review_FinanceDirector]
                            <span class="black">待财务主管审核</span>
						[#elseif goods.check == State_Publish]
                            <span class="green">已上架</span>
						[#else]
                            <span class="silver">未知状态</span>
						[/#if]
                    </td>

                </tr>
			[/#list]

            </table>



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




                <div class="buttonGroup">


                <div >
                 	[@pagination pageNumber = page.pageNumber totalPages = page.totalPage]
                        [#include "/admin/include/pagination.ftl"]</div>&
                    [/@pagination]
                </div>
            </div>
            </div>



        </form>

	</div>

<form action="${base}/wap/member/review/save.jhtml" method="post">
    <ul class="mui-table-view layout-list-common comment-form margin-none">
        <li class="mui-table-view-cell">
        [#--<a href="${base}/wap/goods/detail.jhtml?id=${goods.id}" class="mui-navigate-right">--]
            <div class="mui-pull-left margin-right"><img id="imgshuadan" src="${base}${goods.image!setting.defaultThumbnailProductImage}">

            </div>
            <div class="title margin-small-bottom">
	    				<span>
						 编号：  <span  id="bianhao"></span>

                        </span>
            </div>
            <div class="title margin-small-bottom">
	    				<span>

						 名称：  <span  id="name"></span>

                        </span>
            </div>
            <div class="title margin-small-bottom">
	    				<span>

						 销量：  <span  id="sales"></span>
                        </span>
            </div>

            <!-- <span class="text-ellipsis text-gray">{$goods[_sku_spec]}</span> -->
            </a>
            <div class="margin-top padding-top border-top">

              <span class="mui-btn margin-small-right hd-btn-blue" data-score="5">五&nbsp;&nbsp;星</span>
                <span class="mui-btn margin-small-right hd-btn-gray" data-score="4">四&nbsp;&nbsp;星</span>
                <span class="mui-btn margin-small-right hd-btn-gray" data-score="3">三&nbsp;&nbsp;星</span>
         [#--    <span class="mui-btn margin-small-right hd-btn-gray" data-score="2">二&nbsp;&nbsp;星</span>
                <span class="mui-btn margin-small-right hd-btn-gray" data-score="1">一&nbsp;&nbsp;星</span>--]

        <span class="mui-btn margin-small-right hd-btn-gray" data-score="2">差&nbsp;&nbsp;&nbsp;评</span>
            </div>
            <input type="hidden" id="ids" name="ids"  />
            <input type="hidden" name="score" value="5">
        </li>
        <li>
            <input type="text" name="namee" class="mui-input-clear" placeholder="请输入昵称">
            <textarea class="border-none margin-none" name="content" placeholder="发表您的商品评价，与给多人一同分享"></textarea>
        </li>
    </ul>
    <div class="list-col-10 padding-lr">
        <div class="padding-tb border-bottom">
            <span class="icon-15 mui-pull-left margin-right"><img src="${base}/statics/images/ico_1.png" /></span>
            <span>上传图片完成晒单，最多可以上传5张照片</span>
        </div>
        <ul class="comment-upload-list padding-top-15 padding-small-bottom mui-clearfix">
            <li><div class="upload"> </div></li>
        </ul>
    </div>
    <div class="padding">
        <input type="hidden" name="id" value="${orderItem.id}">
        <input type="submit" class="mui-btn  mui-btn-primary" value="发表评论"/>
    </div>
</form>
	<footer class="footer posi">
		<div class="mui-text-center copy-text">
			<span></span>
		</div>
	</footer>
<script type="text/javascript">
	$('.mui-btn-primary').bind('click',function(){
		var ajax_return = $("form").Validform({
			showAllError:true,
			ajaxPost:true,
			callback:function(ret) {
				if(ret.status == 0) {
					$.tips({
						content:ret.message,
						callback:function() {
							return false;
						}
					});
				} else {
					$.tips({
						content:ret.message,
						callback:function() {
							window.location.href = "${base}/wap/member/review/list.jhtml";
						}
					});					
				}
			}
		})
	})
	window.onload = function(){
		
		var uploader = WebUploader.create({
	        auto:true,
	        fileNumLimit:5,
	        fileVal:'upfile',
	        // swf文件路径
	        swf: '${base}/statics/js/upload/uploader.swf',
	        // 文件接收服务端。
	        server: "${base}/wap/member/review/upload.jhtml",
	        // 选择文件的按钮。可选
	        formData:{
	            file : 'upfile',
	            //upload_init : ''
	        },
	        // 内部根据当前运行是创建，可能是input元素，也可能是flash.
	        pick: {
	            id: '.upload',
	            multiple:true
	        },
	        // 压缩图片大小
	        compress:false,
	        accept:{
	            title: '图片文件',
	            extensions: 'gif,jpg,jpeg,bmp,png',
	            mimeTypes: 'image/*'
	        },
	        chunked: false,
	        chunkSize:1000000,
	        resize: false
	    })
	
	    uploader.onUploadSuccess = function(file, response) {
	    	if(response.status == 1) {
	    		$('.upload').parents('.comment-upload-list').prepend('<li><img src="${fileServer}' + response.url + '" /><input type="hidden" name="images" value="' + response.url + '"/><span class="remove">×</span></li>');
	    	} else {
	    		$.tips({content: response.message})
	    		return false;
	    	}
	    }
	    $('.margin-small-right').bind('click',function(){
	    	$this = $(this);
	    	if($this.hasClass('hd-btn-gray')){
	    		$this.addClass('hd-btn-blue').removeClass('hd-btn-gray').siblings().removeClass('hd-btn-blue').addClass('hd-btn-gray');

	    			$('input[name=score]').val($this.attr('data-score'));
	    	}
	    });
	    
	    mui(".comment-upload-list").on('tap','.remove',function(){
	    	var li = $(this).parents("li");
	    	$.confirms("是否确认删除？",function(){
	    		li.fadeOut(300,function(){
		    		li.remove();
		    	});
	    	});
	    });
		
	}
    
</script>
