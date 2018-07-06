[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <title>消息中心 - Powered By ${setting.siteAuthor}</title>
    <meta name="author" content="${setting.siteAuthor}" />
    <meta name="copyright" content="${setting.siteCopyright}" />
    <link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.tools.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.lSelect.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/input.js"></script>
    <script type="text/javascript">
        $().ready(function() {
            [@flash_message /]
        });
    </script>
</head>
<body>
    <div class="breadcrumb">
        <a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; 消息中心
    </div>
    <ul id="tab" class="tab" >
        <li>
            <input type="button" value="我的消息" />
        </li>
        <li>
            <input type="button" value="技师消息" />
        </li>
        <li style="float:right ">
            <input type="button" value="设置" />
        </li>

    </ul>

    <form id="listForm" action="list.jhtml" method="post">
        <table class="item tabContent">
            <tr>
                <th>
                    <a href="javascript:;" class="sort" name="name">消息分类</a>
                </th>
                <th>
                    <a href="javascript:;" class="sort" name="create_date">推送时间</a>
                </th>
                <th>
                    <a href="javascript:;" class="sort" name="create_date">推送内容</a>
                </th>
                <th>
                    <span>${message("admin.common.action")}</span>
                </th>
            </tr>
            [#list page.list as interest]
                <tr>
                    <td>
                        <span title="${interest.name}">${abbreviate(interest.name, 50, "...")}</span>
                    </td>
                    <td>
                        [#if interest.create_date??]
                            <span title="${interest.create_date?string("yyyy-MM-dd HH:mm:ss")}">${interest.create_date}</span>
                        [#else]
                            -
                        [/#if]
                    </td>
                    <td>
                        <span title="${interest.name}">${abbreviate(interest.name, 50, "...")}</span>
                    </td>
                    <td>
                        <a href="view.jhtml?id=${interest.id}">[${message("admin.common.view")}]</a>
                    </td>
                </tr>
            [/#list]
        </table>
        [@pagination pageNumber = page.pageNumber totalPages = page.totalPage]
            [#include "/admin/include/pagination.ftl"]
        [/@pagination]
    </form>

    <form id="staff" action="staff.jhtml" method="post">
        <table class="item tabContent">
            <tr>
                <th>
                    <a href="javascript:;" class="sort" name="name">消息分类</a>
                </th>
                <th>
                    <a href="javascript:;" class="sort" name="create_date">推送时间</a>
                </th>
                <th>
                    <a href="javascript:;" class="sort" name="create_date">推送内容</a>
                </th>
                <th>
                    <span>${message("admin.common.action")}</span>
                </th>
            </tr>
            [#list page.list as interest]
                <tr>
                    <td>
                        <span title="${interest.name}">${abbreviate(interest.name, 50, "...")}</span>
                    </td>
                    <td>
                        [#if interest.create_date??]
                            <span title="${interest.create_date?string("yyyy-MM-dd HH:mm:ss")}">${interest.create_date}</span>
                        [#else]
                            -
                        [/#if]
                    </td>
                    <td>
                        <span title="${interest.name}">${abbreviate(interest.name, 50, "...")}</span>
                    </td>
                    <td>
                        <a href="view.jhtml?id=${interest.id}">[${message("admin.common.view")}]</a>
                    </td>
                </tr>
            [/#list]
        </table>
        [@pagination pageNumber = page.pageNumber totalPages = page.totalPage]
            [#include "/admin/include/pagination.ftl"]
        [/@pagination]
    </form>

    <form id="design" action="${base}/admin/setting/update.jhtml" method="post">
        <table class="input tabContent">
            <tr>
                <th>
                    我的消息:
                </th>
                <td>
                    <input type="checkbox" name="isMyMessage" value="true"[#if setting.isMyMessage] checked="checked"[/#if] />
                    <input type="hidden" name="_isMyMessage" value="false" />
                </td>
            </tr>
            <tr>
                <th>
                    技师消息:
                </th>
                <td>
                    <input type="checkbox" name="isStaffMessage" value="true"[#if setting.isStaffMessage] checked="checked"[/#if] />
                    <input type="hidden" name="_isStaffMessage" value="false" />
                </td>
            </tr>
            <tr>
                <th>
                    &nbsp;
                </th>
                <td>
                    <input type="submit" class="button" value="${message("admin.common.submit")}" />
                    <input type="button" class="button" value="${message("admin.common.back")}" onclick="history.back(); return false;" />
                </td>
            </tr>
        </table>
    </form>

</body>
</html>
[/#escape]