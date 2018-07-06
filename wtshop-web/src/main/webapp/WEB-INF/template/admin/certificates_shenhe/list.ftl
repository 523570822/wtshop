[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <title>${message("admin.review.list")} - Powered By ${setting.siteAuthor}</title>
    <meta name="author" content="${setting.siteAuthor}" />
    <meta name="copyright" content="${setting.siteCopyright}" />
    <link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
    <link href="${base}/statics/lib/layer/mobile/need/layer.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/list.js"></script>
    <script type="text/javascript" src="${base}/statics/lib/layer/layer.js"></script>
    <script type="text/javascript">
        $().ready(function() {

            var $listForm = $("#listForm");
            var $type = $("#type");
            var $typeMenu = $("#typeMenu");
            var $typeMenuItem = $("#typeMenu li");
            var $ddd = $(".ddd");

            [@flash_message /]

            $typeMenu.hover(
                    function() {
                        $(this).children("ul").show();
                    }, function() {
                        $(this).children("ul").hide();
                    }
            );

            $typeMenuItem.click(function() {
                $type.val($(this).attr("val"));
                $listForm.submit();
            });
            $ddd.click(function () {
                var id = $(this).parent().parent().children('td').eq(0).children('input').val();
                layer.open({
                    title:"证件审核",
                    type: 2,
                    skin: 'layui-layer-rim', //加上边框
                    area: ['870px', '540px'], //宽高
                    content: "/admin/certificates_shenhe/toShenHe.jhtml?certificatesId=" + id,
                    shadeClose:true,
                });
            })
        });
    </script>
</head>
<body>
<div class="breadcrumb">
    <a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; ${message("shen.list")}  <span>(${message("admin.page.total", page.totalRow)})</span>
</div>
<form id="listForm" action="list.jhtml" method="post">
    <input type="hidden" id="type" name="type" value="" />
    <div class="bar">
        <div class="buttonGroup">
            <a href="javascript:;" id="deleteButton" class="iconButton disabled">
                <span class="deleteIcon">&nbsp;</span>${message("admin.common.delete")}
            </a>
            <a href="javascript:;" id="refreshButton" class="iconButton">
                <span class="refreshIcon">&nbsp;</span>${message("admin.common.refresh")}
            </a>
            <div id="typeMenu" class="dropdownMenu">
                <a href="javascript:;" class="button">
                ${message("admin.shenhe.status")}<span class="arrow">&nbsp;</span>
                </a>
                <ul>
                    <li  val="4">${message("admin.point.five")}</li>
                    <li  val="0">${message("admin.shenhe.reday")}</li>
                    <li  val="1">${message("admin.shenhe.pass")}</li>
                    <li  val="2">${message("admin.shenhe.nopass")}</li>
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
        <div id="searchPropertyMenu" class="dropdownMenu">
            <div class="search">
                <span class="arrow">&nbsp;</span>
                <input type="text" id="searchValue" name="pageable.searchValue" value="${pageable.searchValue}" maxlength="200" />
                <button type="submit">&nbsp;</button>
            </div>
            <ul>
                <li[#if pageable.searchProperty == "name"] class="current"[/#if] val="name">${message("Footprint.nickname")}</li>
                <li[#if pageable.searchProperty == "create_date"] class="current"[/#if] val="create_date">${message("shen.create_time")}</li>
            </ul>
        </div>
    </div>
    <table id="listTable" class="list">
        <tr>
            <th class="check">
                <input type="checkbox" id="selectAll" />
            </th>
            <th>
                <a href="javascript:;" class="sort" name="nick">${message("admin.referrerGoods.mName")}</a>
            </th>
            <th>
                <a href="javascript:;" class="sort" name="name">${message("Footprint.nickname")}</a>
            </th>
            <th>
                <a href="javascript:;" class="sort" name="phone">${message("Member.mobile")}</a>
            </th>
            <th>
                <a href="javascript:;" class="sort" name="id_card">${message("shen.idcard")}</a>
            </th>
            <th>
                <a href="javascript:;" class="sort" name="create_date">${message("shen.create_time")}</a>
            </th>
            <th>
                <a href="javascript:;" class="sort" name="state">${message("admin.shenhe.status")}</a>
            </th>
            <th>
                <span>${message("admin.common.action")}</span>
            </th>
        </tr>
        [#list page.list as certificates]
            <tr>
                <td>
                    <input type="checkbox" name="ids" value="${certificates.id}" />
                </td>
                <td>
                    ${abbreviate(certificates.member.nickname, 50, "...")}
                </td>
                <td>
                     ${abbreviate(certificates.name, 50, "...")}
                </td>
                <td>
                ${abbreviate(certificates.phone, 50, "...")}
                </td>
                <td>
                     ${abbreviate(certificates.id_card, 50, "...")}
                </td>
                <td>
                    <span title="${certificates.createDate?string("yyyy-MM-dd HH:mm:ss")}">${certificates.createDate}</span>
                </td>
                <td>
                    [#if certificates.state==0]
                        未审核
                    [#elseif certificates.state==1]
                        审核通过
                    [#elseif certificates.state==2]
                        审核未通过
                    [/#if]
                </td>
                <td>
                    <a class="ddd"  style="cursor: pointer">[${message("admin.common.view")}]</a>
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