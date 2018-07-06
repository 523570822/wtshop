[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <title>${message("admin.order.list")} - Powered By ${setting.siteAuthor}</title>
    <meta name="author" content="${setting.siteAuthor}" />
    <meta name="copyright" content="${setting.siteCopyright}" />
    [#--<link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />--]
    <link rel="stylesheet" href="${base}/statics/css/treeCss/demo.css" type="text/css">
    <link rel="stylesheet" href="${base}/statics/css/treeCss/zTreeStyle/zTreeStyle.css" type="text/css">
    <link rel="stylesheet" href="${base}/resources/admin/css/bootstrap.min.css?v=3.3.6" rel="stylesheet">
    <link rel="stylesheet" href="${base}/resources/admin/css/style.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${base}/resources/admin/css/ui.jqgrid.css?v=5.0.0" rel="stylesheet">
    <style>
        .maxHeight {
            height: 480px;
            border: 1px solid #ddd;
            overflow: auto;
            /*background: #F5F5F6;*/
        }

    </style>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.min.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.jsonsubmit.js"></script>
    <script type="text/javascript" src="${base}/statics/js/treeJs/jquery.ztree.core.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/grid.locale-cn.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.jqGrid.min.js?v=5.0.1"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/jqgridCommon.js"></script>

</head>
<body class="gray-bg">
    <div class="breadcrumb" style="padding: 4px 10px">
    <a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; ${message("organization.manage")}</span>
    </div>
    <div class="wrapper wrapper-content  animated fadeInRight">
        <div class="row">
            <div class="col-sm-12">
                <div class="ibox float-e-margins">
                    <div class="ibox-content">
                        <div class="row">
                            <div class="col-sm-3 maxHeight">
                                <ul id="treeDemo" class="ztree"></ul>
                            </div>
                            <div class="col-sm-9">
                                <form id="searchForm" method="get" class="form-horizontal">
                                    <input id="parentId" name="parentId" type="hidden"
                                           value="1">
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label">${message("organization.nickname")}</label>
                                        <div class="col-sm-3">
                                            <input id="regex:name" name="name" type="text"
                                                   class="form-control">
                                        </div>
                                        <div class="col-sm-2">
                                            <button id="search" class="btn btn-primary" type="button">${message("organization.select")}</button>
                                        </div>
                                        <div class="col-sm-2">
                                            <button id="newFunction" class="btn btn-outline btn-danger"
                                                    type="button">${message("organization.adds")}</button>
                                        </div>
                                    </div>
                                </form>
                                <div class="jqGrid_wrapper">
                                    <table id="functionTable"></table>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>

<script type="text/javascript">
    $(document).ready(function () {
        // zTree 的参数配置，深入使用请参考 API 文档（setting 配置详解）
        var setting = {
            data: {
                simpleData: {
                    enable: true,
                    idKey: "id",
                    pIdKey: "parentId",
                    rootPId: 0
                }
            },
            callback:{
                onClick: zTreeOnClick
            }
        };
        function zTreeOnClick(e, treeId, node){
            $("#parentId", $("#searchForm")).val(node.id);
            $("#functionTable").reloadGrid({
                postData : $("#searchForm").serialize()
            });
        }
        $("#newFunction")
                .click(
                        function() {
                            document.location.href = "/admin/organization/toAdd.jhtml?parentId="
                                    + $("#parentId").val();
                        });
        $("#search").click(function() {
            $("#functionTable").reloadGrid({
                postData : $("#searchForm").serialize()
            });
        });
        $.post("${base}/admin/organization/queryAll.jhtml",function (e) {
           $.fn.zTree.init($("#treeDemo"), setting, e);
        });
        $("#functionTable").grid({
            url :"/admin/organization/query.jhtml",
            postData : $("#searchForm").serialize(),
            colNames : [ "名称", "标识代码",
                "功能地址", "操作" ],
            colModel : [{name : "name", index : "name"},
                {name : "code", index : "code"},
                {name : "action", index : "action"},
                {name : "id", index : "id", align : "center",formatter : function(
                        cellvalue,
                        options,
                        rowObject) {
                    var v = "<a href='${base}/admin/organization/toUpdate.jhtml?functionId="
                            + cellvalue
                            + "'>详情</a>&nbsp;&nbsp;";
                    return v;
                }}
            ]
        });
        [@flash_message /]
    });


</script>
[/#escape]