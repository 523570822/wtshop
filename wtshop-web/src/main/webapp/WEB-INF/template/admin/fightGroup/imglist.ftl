[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <title>福袋图片管理- Powered By ${setting.siteAuthor}</title>
    <meta name="author" content="${setting.siteAuthor}" />
    <meta name="copyright" content="${setting.siteCopyright}" />
    <link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/list.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/ajaxfileupload.js"></script>
    <script type="text/javascript">

        $().ready(function() {

            [@flash_message /]


            $(document).on('change','#imgUPloads',function(){
                $.ajaxFileUpload({
                    url: '${base}/admin/file/upload.jhtml', //用于文件上传的服务器端请求地址
                    secureuri: false, //是否需要安全协议，一般设置为false
                    fileElementId: 'imgUPloads', //文件上传域的ID
                    //fileElementId: $('#upImge').get(0).files,
                    data:{
                    },
                    dataType: 'json', //返回值类型 一般设置为json
                    success: function(data) //服务器成功响应处理函数
                    {
                        console.log(data);
                        if (data&&data.url){
                            $.post('${base}/admin/fuDai/saveImg.jhtml',{"fudaiImg.fudaiId":${fudaiId},"fudaiImg.path":data.url},function(){
                                location.reload();
                            })
                        }
                    },

                });
            })

            $(document).on('change','.orders',function () {
                $.post('saveImg.jhtml',{"fudaiImg.id":$(this).attr('nid'),"fudaiImg.orders":$(this).val()},function () {
                    location.reload();
                })
            })

            $(document).on('click','.del',function () {
                $.post('delImg.jhtml',{"id":$(this).attr('nid'),"fudaiImg.orders":$(this).val()},function () {
                    location.reload();
                })
            })
        });
    </script>
</head>
<body>
<div class="breadcrumb">
    <a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; ${message("fudai.phott.title")} <span>
</div>
<form id="listForm" action="list.jhtml" method="post">
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
        <a href="/admin/actIntroduce/details.jhtml?type=0" class=" iconButton"   onclick="history.back(); return false;">
            <span class="moveDirIcon"></span> ${message("admin.back")}
        </a>
    </div>

    <div class="zupLoad martop20 marleft20" style="position: relative;margin:10px">
        <span style="font-size:14px"> ${message("fudai.upload")}</span>
        <input type="file" id="imgUPloads" class="zimgUPload"  name="imgUPloads">
    </div>
    <table id="listTable" class="list">
        <tr>
            <th>${message("admin.goods.productImage")}</th>
            <th>${message("LoginPlugin.order")}</th>
            <th>${message("app_manage.list.timee")}</th>
            <th>${message("admin.common.action")}</th>
        </tr>
        [#list imgList as e]
            <tr>
                <td>
                    <img src="${fileServer}${e.path}" alt=""  style="width: 100px;height: 100px;" />
                </td>

                <td>
                    <input type="text" maxlength="2" class="text orders"   value=${e.orders}  nid=${e.id} />
                </td>


                <td>
                    <span>${e.create_date}</span>
                </td>

                <td>
                    <a class="del" nid=${e.id} >${message("shop.common.remove")}</a>
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