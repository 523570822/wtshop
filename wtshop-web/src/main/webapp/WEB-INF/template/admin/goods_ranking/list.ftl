[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <title>${message("admin.goodsRanking.list")} - Powered By ${setting.siteAuthor}</title>
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
            var $filterMenu = $("#rankingTypeMenu");
            var $filterMenuItem = $("#rankingTypeMenu li");

            // 筛选菜单
            $filterMenu.hover(
                    function () {
                        $(this).children("ul").show();
                    }, function () {
                        $(this).children("ul").hide();
                    }
            );
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

            $("#btn-excel").click(function () {
                var beginDate = $('#beginDate').val();
                var endDate = $('#endDate').val();
                location.href = 'download.jhtml?beginDate=' + beginDate + "&endDate=" + endDate;
            });

        });
    </script>
</head>
<body>
<div class="breadcrumb">
    <a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; ${message("admin.goodsRanking.list")}
</div>
<form id="listForm" action="list.jhtml" method="post">

    <input type="hidden" id="rankingType" name="rankingType" value="${rankingType}"/>

    <div class="bar">
        <div class="buttonGroup">
            <a href="javascript:;" id="refreshButton" class="iconButton">
                <span class="refreshIcon">&nbsp;</span>刷新
            </a>

            <div id="rankingTypeMenu" class="dropdownMenu">
                <a href="javascript:;" class="button">
                ${message("admin.goodsRanking.type")}<span class="arrow">&nbsp;</span>
                </a>
                <ul class="check">
                    <li name="rankingType" [#if rankingType == "sales"] class="checked"[/#if] val="sales">销量</li>
                </ul>
            </div>
        </div>
        <div class="buttonGroup">
        ${message("admin.memberStatistic.beginDate")}:
            <input type="text" id="beginDate" name="beginDate" class="text Wdate"
                   value="${beginDate?string("yyyy-MM-dd")}" style="width: 120px;"
                   onfocus="WdatePicker({maxDate: '#F{$dp.$D(\'endDate\')}'});"/>
        ${message("admin.memberStatistic.endDate")}:
            <input type="text" id="endDate" name="endDate" class="text Wdate" value="${endDate?string("yyyy-MM-dd")}"
                   style="width: 120px;" onfocus="WdatePicker({});"/>
        </div>
        <div class="buttonGroup">
            <input type="submit" class="button" value="${message("admin.common.submit")}"/>
        </div>
        <div class="buttonGroup">
            <input type="button" class="button" value="导 出" id="btn-excel"/>
        </div>
    </div>
    <div id="chart" class="chart"></div>
</form>
    [#if dataList??]
    <style type="text/css">
        .chart {
            min-height: 600px;
            padding: 0px 10px;
            border-top: 1px solid #d7e8f1;
            border-bottom: 1px solid #d7e8f1;
        }
    </style>
    <script type="text/javascript" src="${base}/resources/admin/js/echarts.js"></script>
    <script type="text/javascript">
        var chart = echarts.init(document.getElementById("chart"));

        chart.setOption({
            tooltip: {
                trigger: "axis",
//					formatter: function(params) {
//						return params[0][1].name + " [" + params[0][1].sn + "]<br \/>" + params[0][0] + ": " + params[0][2];
//					}
            },
            xAxis: [
                {
                    name: "${message("Goods.RankingType." + rankingType)}",
                    type: "value"
                }
            ],
            yAxis: [
                {
                    name: "商品",
                    type: "category",
                    data: [[#list dataList as data]
                        "${data.name}"[#if data_has_next],[/#if]
                    [/#list]],
                    axisLabel: {
                        rotate: 30,
                        formatter: function (value, index) {
                            return value;
                        }
                    }
                }
            ],
            series: [
                {
                    name: "${message("Goods.RankingType." + rankingType)}",
                    type: "bar",
                    data: [[#list dataList as data]
                        "${data.count}"[#if data_has_next],[/#if]
                    [/#list]]
                }
            ]
        });
    </script>
    [/#if]
</body>
</html>
[/#escape]