[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>${message("admin.area.list")} - Powered By ${setting.siteAuthor}</title>
<meta name="author" content="${setting.siteAuthor}" />
<meta name="copyright" content="${setting.siteCopyright}" />
	<link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" href="${base}/resources/admin/css/style.min.css" rel="stylesheet">
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.min.js"></script>
    <script type="text/javascript" src="${base}/statics/lib/layer/layer.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/list.js"></script>
<script type="text/javascript">
$().ready(function() {
	var $delete = $("#listTable a.delete");
	var $jqList = $("#jqList");
	[@flash_message /]

    //切换
    $("#jqList").click(function () {
        var parentId = $(this).attr("data");
        var flag = $(this).attr("flag");
        if(flag=="0"){
            flag=1;
        }else {
            flag=0;
        }
        window.location.href="${base}/admin/area/list.jhtml?parentId="+parentId+"&flag="+flag;
    });
	// 删除
	$delete.click(function() {
		var $this = $(this);
		$.dialog({
			type: "warn",
			content: "${message("admin.dialog.deleteConfirm")}",
			onOk: function() {
				$.ajax({
					url: "delete.jhtml",
					type: "POST",
					data: {id: $this.attr("val")},
					dataType: "json",
					cache: false,
					success: function(message) {
						$.message(message);
						if (message.type == "success") {
							$this.parent().html("&nbsp;");
						}
					}
				});
			}
		});
		return false;
	});

});
</script>
</head>
<body>
	<div class="breadcrumb">
		<a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; ${message("admin.area.list")}
	</div>
	<div class="bar">
		<a href="add.jhtml?flag=${flag}[#if parent??]&parentId=${parent.id}[/#if]" id="add" class="iconButton">
			<span class="addIcon">&nbsp;</span>${message("admin.common.add")}
		</a>
		[#if parent??]
            [#if parent.parent??]
                <a href="list.jhtml?flag=${flag}&parentId=${parent.parent.id}" class="iconButton">
                    <span class="upIcon">&nbsp;</span>${message("admin.area.parent")}
                </a>
            [#else]
                <a href="list.jhtml?flag=${flag}" class="iconButton">
                    <span class="upIcon">&nbsp;</span>${message("admin.area.parent")}
                </a>
            [/#if]
		[/#if]
        <a href="javascript:;" class="iconButton" id="jqList" data="${parent.id}" flag="${flag}">
            <span class="moveIcon">&nbsp;</span>${message("admin.area.tableList")}
        </a>
	</div>
[#if flag==0]
	<table id="listTable" class="list">
		<tr>
			<th colspan="5" class="green" style="text-align: center;">
				[#if parent??]${message("admin.area.parent")} - ${parent.name}[#else]${message("admin.area.root")}[/#if]
			</th>
		</tr>
		[#list areas?chunk(5, "") as row]
			<tr>
				[#list row as area]
					[#if area?has_content]
						<td>
							<a href="list.jhtml?parentId=${area.id}&flag=0" title="${message("admin.common.view")}">${area.name}</a>
							<a href="edit.jhtml?id=${area.id}&flag=0">[${message("admin.common.edit")}]</a>
							<a href="javascript:;" class="delete" val="${area.id}">[${message("admin.common.delete")}]</a>
						</td>
					[#else]
						<td>
							&nbsp;
						</td>
					[/#if]
				[/#list]
			</tr>
		[/#list]
		[#if !areas?has_content]
			<tr>
				<td colspan="5" style="text-align: center; color: red;">
					${message("admin.area.emptyChildren")} <a href="add.jhtml?flag=0[#if parent??]&?parentId=${parent.id}[/#if]" class="silver">${message("admin.common.add")}</a>
				</td>
			</tr>
		[/#if]
	</table>
[/#if]
[#if flag==1]
<table id="listTable" class="list">
    <tr>

        <th>
            <a href="javascript:;" class="sort" name="id">${message("Area.name")} </a>
        </th>
        <th>
            <a href="javascript:;" class="sort" name="name">${message("Area.entrepot.name")} </a>
        </th>
        <th>
            <a href="javascript:;" class="sort" name="number">${message("Area.entrepot.message")} </a>
        </th>
        <th>
            <a href="javascript:;" class="sort" name="stock">${message("Area.receiving.beginTime")} </a>
        </th>
        <th>
            <a href="javascript:;" class="sort" name="stock">${message("Area.receiving.endTime")} </a>
        </th>
        <th>
            <span>${message("admin.common.action")}</span>
        </th>
    </tr>
    [#list areas as area]
        <tr>
            [#if area?has_content]
            <td>
                <a href="list.jhtml?parentId=${area.id}&flag=1" title="${message("admin.common.view")}">${area.name}</a>
            </td>
            <td>
            ${area.entrepot_name}
            </td>
            <td>
            ${area.entrepot_message}
            </td>
            <td>
            ${area.receiving_beginTime}
            </td>
            <td>
            ${area.receiving_endTime}
            </td>
            <td>
                <a href="edit.jhtml?id=${area.id}&flag=1">[${message("admin.common.edit")}]</a>
                <a href="javascript:;" class="delete" val="${area.id}">[${message("admin.common.delete")}]</a>
            </td>
            [/#if]
        </tr>
    [/#list]
</table>
[/#if]
</body>
</html>
[/#escape]