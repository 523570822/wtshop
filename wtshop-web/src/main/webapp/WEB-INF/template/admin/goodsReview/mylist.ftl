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
    <script type="text/javascript" src="${base}/resources/admin/layer/layer.js"></script>
    <script type="text/javascript">
        $().ready(function () {

            [@flash_message /]

            var $refreshButton = $("#refreshButton");
            $refreshButton.click(function () {
                $('#listForm').submit();
            });

//    $(document).on('change','#verifyResultSel',function () {
//        $('#listForm').submit();
//    })


            $('.verifycancel').click(function () {
                var goodsId = $(this).attr('goodsId');
                var flowId = $(this).attr('flowId');
                $.post('${base}/admin/goods/adminVerify.jhtml', {
                    flowId: flowId,
                    goodsId: goodsId,
                    verifyResult: 0
                }, function () {
                    location.reload();
                });

            });


            var $listForm = $("#listForm");
            var $filterMenu = $("#filterMenu");
            var $filterMenuItem = $("#filterMenu li");

            // 筛选菜单
            $filterMenu.hover(
                    function () {
                        $(this).children("ul").show();
                    }, function () {
                        $(this).children("ul").hide();
                    }
            );

            // 筛选
            $filterMenuItem.click(function () {
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
    <a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo;
    商品审核<span>(${message("admin.page.total", page.totalRow)})</span>
</div>
<form id="listForm" action="mytVerify.jhtml" method="get">
    <input type="hidden" id="verifyResult" name="verifyResult" value="${verifyResult}"/>

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
            <div id="searchPropertyMenu" class="dropdownMenu">
                <div class="search">
                    <span class="arrow">&nbsp;</span>
                    <input type="text" id="searchValue" name="pageable.searchValue" value="${pageable.searchValue}"
                           maxlength="200"/>
                    <button type="submit">&nbsp;</button>
                </div>
                <ul>
                    <li[#if pageable.searchProperty == "goodsId"] class="current"[/#if] val="goodsId">${message("admin.referrerGoods.goodsId")}</li>
                    <li[#if pageable.searchProperty == "submitName"] class="current"[/#if] val="submitName">${message("admin.member.no")}</li>
                </ul>
            </div>
            <div id="filterMenu" class="dropdownMenu" style="margin-left: 10px">
                <a href="javascript:;" class="button">${message("admin.goodsVerify.type")}<span class="arrow">&nbsp;</span>
                </a>
                <ul class="check">
                    <li name="verifyResult" [#if verifyResult=="1"]  class="checked"  [/#if]  val="1"> ${message("admin.goodsVerify.start")}</li>
                    <li name="verifyResult"  [#if verifyResult=="2"] class="checked"  [/#if] val="2">${message("ReturnsItem.Status.pass")}</li>
                    <li name="verifyResult"  [#if verifyResult=="3"] class="checked"  [/#if] val="3">${message("admin.goodsVerify.no")}</li>
                </ul>
            </div>

        </div>

    </div>
    <table id="listTable" class="list">
        <tr>

            <th>
                <a href="javascript:;" class="" name="">${message("admin.referrerGoods.goodsId")}</a>
            </th>
            <th>
                <a href="javascript:;" class="" name="">${message("admin.seo.goodsName")}</a>
            </th>
            <th>
                <a href="javascript:;" class="sort" name="">${message("admin.member.no")}</a>
            </th>
            <th>
                <a href="javascript:;" class="sort" name="">${message("admin.member.time")}</a>
            </th>
            <th>
                <a href="javascript:;" class="sort" name="">${message("admin.shenhe.status")}</a>
            </th>
            <th>
                <a href="javascript:;" class="sort" name="">${message("admin.shenhe.mmeber")}</a>
            </th>
            <th>
                <a href="javascript:;" class="sort" name="verifyTime">${message("admin.shenhe.time")}</a>
            </th>
            <th>
                <a href="javascript:;" class="" name="verifySuggest">${message("admin.deposit.memo")}</a>
            </th>

            <th>
                <a href="javascript:;" class="" name="optType">${message("admin.goodsVerify.type")}</a>
            </th>
            <th>
                <a href="javascript:;" class="sort" name="">${message("Footprint.action")}</a>
            </th>

        </tr>
        [#list page.list as d]
            <tr>
                <td>
                ${d.goodsId}
                </td>
                <td>
                    <span title="${d.goodsName}">${abbreviate(d.goodsName, 30, "...")}</span>
                </td>
                <td>
                ${d.submitName}
                </td>
                <td>
                ${d.create_date}
                </td>
                <td>
                    [#switch    d.verifyResult]
                     [#case  0 ]
                    ${message("admin.goodsVerify.start")}
                        [#break ]
                        [#case  1 ]
                        ${message("admin.status.doing")}
                            [#break ]
                        [#case  2 ]
                        ${message("ReturnsItem.Status.pass")}
                            [#break ]
                        [#case  3 ]
                        ${message("admin.goodsVerify.no")}
                            [#break ]
                    [/#switch]
                </td>

                <td>
                ${d.verifyName}
                </td>

                <td>
                ${d.verifyTime}
                </td>
                <td>
                ${d.verifySuggest}
                </td>
                <td>
                    [#if  d.optType==1] ${message("admin.shenhe.delete")} [#else]${message("admin.shenhe.edit")}  [/#if]

                </td>
                <td>
                    [#if  d.verifyResult==1]
                        <a class="verifycancel" flowId=${d.id}  goodsId="${d.goodsId}">${message("admin.shenhe.back")} </a>
                    [/#if]
                </td>


            </tr>
        [/#list]
    </table>
    [@pagination pageNumber = page.pageNumber totalPages = page.totalPage]
        [#include "/admin/include/pagination.ftl"]
    [/@pagination]
</form>
<div id="box" class="hidden" style="margin: 20px">
    <span>备注:</span>
    <textarea rows="" cols="" id="verifySuggestTxt" style="width: 320px;height: 150px;font-size: 14px"
              maxlength="500"></textarea>
</div>
</body>
</html>
[/#escape]