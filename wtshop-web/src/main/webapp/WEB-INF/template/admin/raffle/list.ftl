[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <title>${message("admin.promotion.list")} - Powered By ${setting.siteAuthor}</title>
    <meta name="author" content="${setting.siteAuthor}"/>
    <meta name="copyright" content="${setting.siteCopyright}"/>
    <link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/list.js"></script>
    <script type="text/javascript">

            function issue(id){

                                var sn =$("#sn").val();

                if (sn==null||sn==""){
                    alert("订单编好不能为空");
                }else {
                    window.location.href="publish.jhtml?sn="+sn+"&id="+id;
                }





            }

        $().ready(function (){

             [@flash_message /]



        });
    </script>
</head>
<body>
<div class="breadcrumb">
    <a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; ${message("Activity.manager")}
    <span>(${message("admin.page.total", page.totalRow)})</span>
</div>
<form id="listForm" action="list.jhtml" method="post">
    <div class="bar">
       [#-- <a href="add.jhtml" class="iconButton">
            <span class="addIcon">&nbsp;</span>${message("admin.common.add")}
        </a>
--]
        <div class="buttonGroup">
           [#-- <a href="javascript:;" id="deleteButton" class="iconButton disabled">
                <span class="deleteIcon">&nbsp;</span>${message("admin.common.delete")}
            </a>--]
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
                <input type="text" id="searchValue" name="pageable.searchValue" value="${pageable.searchValue}"
                       maxlength="200"/>
                <button type="submit">&nbsp;</button>
            </div>
            <ul>
            [#--<li[#if pageable.searchProperty == "name"] class="current"[/#if] val="name">${message("Promotion.name")}</li>--]
                <li[#if pageable.searchProperty == "title"] class="current"[/#if]
                                                            val="title">${message("Promotion.title")}</li>
            </ul>
        </div>
    [#--<a href="/admin/actIntroduce/details.jhtml?type=0" class=" iconButton">--]
    [#--<span class="moveDirIcon"></span>${message("Fudai.information")}--]
    [#--</a>--]
    </div>
    <table id="listTable" class="list">
        <tr>
            <th class="check">
                <input type="checkbox" id="selectAll"/>
            </th>
            <th>
                <span>${message("admin.wallet.nickname")}</span>
            </th>
            <th>
                <span>${message("admin.member.phone")}</span>
            </th>
            <th>
                <span>${message("Reffle.isReal")}</span>
            </th>
            <th>
                <span>${message("admin.point.point")}</span>
            </th>

            <th>
                <span>${message("Activity.opporName")}</span>
            </th>


            <th>
                <span>${message("NewGoods.time")}</span>
            </th>
            <th>
                <span>${message("admin.common.type")}</span>
            </th>
            <th>
                <span>${message("admin.common.action")}</span>
            </th>
        </tr>
        [#list page.list as raffle]
            <tr>
                <td>
                    <input type="checkbox" name="ids" value="${raffle.id}"/>
                </td>
                <td>
                    <span title="${raffle.member.nickname}">${abbreviate(raffle.member.nickname, 50, "...")}</span>
                </td>
                <td>
                    <span title="${raffle.member.phone}">${abbreviate(raffle.member.phone, 50, "...")}</span>
                </td>
                <td>
                        [#if raffle.isReal==1]
                           是
                        [#else ]
                            否
                        [/#if]
                </td>
                <td>
                    ${raffle.point}
                </td>
                <td>
                       ${raffle.activity.opporName}
                </td>

                <td>
                    [#if raffle.create_date??]
                        <span title="${raffle.create_date?string("yyyy-MM-dd HH:mm:ss")}">${raffle.create_date}</span>
                    [#else]
                        -
                    [/#if]
                </td>
                <td>
                    [#if raffle.issue==0]
                        <span class="red">[未发放]</span>
                    [#else]
                        <span class="red">[已发放]</span>
                    [/#if]


                </td>
                <td>

                    [#if raffle.issue==0]

                         <input name="sn" id="sn"  type="text" />

                        <a  onclick="issue(${raffle.id});" href="javascript:;" class="status" >[${message("LoginPlugin.isEnabled")}]</a>


                    [#else ]
                        <span >${raffle.sn}</span>

                    [/#if]
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