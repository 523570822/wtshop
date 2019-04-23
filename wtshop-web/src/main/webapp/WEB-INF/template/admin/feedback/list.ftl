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
    <link href="${base}/statics/lib/layer/mobile/need/layer.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="${base}/statics/lib/layer/layer.js"></script>
    <script type="text/javascript">
        $(function() {

            [@flash_message /]

            $("#listTable tr:gt(0) td:nth-child(5) span a ").click(function(){
                var $this = $(this);
                var test = $this.attr("value");
                layer.open({
                    title:"反馈内容",
                    type: 1,
                    skin: 'layui-layer-rim', //加上边框
                    area: ['400px', '250px'], //宽高
                    content: test
                });
            })

        });

    </script>
</head>
<style type="text/css">
    .imgbox img{
        width: 100px;
        height: 100px;
    }
</style>
<body>
<div class="breadcrumb">
    <a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; ${message("admin.main.feedback")} <span>(${message("admin.page.total", page.totalRow)})</span>
</div>
<form id="listForm" action="list.jhtml" method="post">
    <div class="bar">
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
                <input type="text" id="searchValue" name="memberName" value="${pageable.searchValue}" maxlength="200" />
                <button type="submit">&nbsp;</button>
            </div>
            <ul>
                <li[#if pageable.searchProperty == "memberName"] class="current"[/#if] val="memberName">${message("FeedBack.username")}</li>
            </ul>
        </div>
    </div>
    <table id="listTable" class="list">
        <tr>
            <th class="check">
                <input type="checkbox" id="selectAll" />
            </th>
            <th>
                <a href="javascript:;" class="sort" name="member_id">${message("FeedBack.username")}</a>
            </th>
            <th>
                <a href="javascript:;" class="sort" name="createTime">${message("FeedBack.time")}</a>
            </th>
         [#--   <th>
                <a href="javascript:;" class="sort" name="image">${message("FeedBack.image")}</a>
            </th>--]
            <th>
                <a href="javascript:;" class="sort" name="content">${message("FeedBack.content")}</a>
            </th>

        </tr>
        [#list page.list as feedback]
            <tr>
                <td>
                    <input type="checkbox" name="ids" value="${feedback.id}" />
                </td>
                <td>
                    <span title="${feedback.member_id}">${abbreviate(feedback.username, 10, "...")}</span>
                </td>
                <td>
                    [#if feedback.create_date??]
                        <span title="${feedback.create_date?string("yyyy-MM-dd HH:mm:ss")}">${feedback.create_date}</span>
                    [#else]
                        -
                    [/#if]
                </td>
                <td class="imgbox">
                    [#list feedback.imagesConverter as images]
                        [#if images??]
                            <img src="${fileServer}${images}"/>
                        [#else]
                            -
                        [/#if]

                    [/#list]
                </td>
                <td>
                    <span ><a href="javascript:;" value="${feedback.content}">[${message("admin.common.view")}]</a></span>
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