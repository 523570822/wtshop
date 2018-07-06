[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>${message("admin.area.add")} - Powered By ${setting.siteAuthor}</title>
<meta name="author" content="${setting.siteAuthor}" />
<meta name="copyright" content="${setting.siteCopyright}" />
<link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/input.js"></script>
<script type="text/javascript">
$().ready(function() {

	var $inputForm = $("#inputForm");
    $("#goback").click(function () {
        var id = $(this).attr("data");
        var flag = $(this).attr("flag");
        window.location.href="${base}/admin/area/list.jhtml?parentId="+id+"&flag="+flag;
    })
	[@flash_message /]
	
	// 表单验证
	$inputForm.validate({
		rules: {
			"area.name": "required",
			"area.orders": "digits",
            "areaDescribe.receiving_beginTime":"required",
            "areaDescribe.receiving_endtime":{
                required: true,
                digits:true,
                min:1

            }
		}
	});

});
</script>
</head>
<body>
	<div class="breadcrumb">
		<a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; ${message("admin.area.add")}
	</div>
	<form id="inputForm" action="save.jhtml" method="post">
        <input type="hidden" name="flag" id="flag" value="${flag}" />
		[#if parent??]
			<input type="hidden" name="parentId" id="parentId" value="${parent.id}" />
		[/#if]
		<table class="input">
			<tr>
				<th>
					${message("admin.area.parent")}:
				</th>
				<td>
					[#if parent??]${parent.name}[#else]${message("admin.area.root")}[/#if]
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>${message("Area.name")}:
				</th>
				<td>
					<input type="text" name="area.name" class="text" maxlength="200" />
				</td>
			</tr>
            <tr>
                <th>
                    <span class="requiredField"></span>${message("Area.entrepot.name")}:
                </th>
                <td>
                    <input type="text" name="areaDescribe.entrepot_name" class="text" maxlength="200" />
                </td>
            </tr>
            <tr>
                <th>
                    <span class="requiredField"></span>${message("Area.entrepot.message")}:
                </th>
                <td>
                    <input type="text" name="areaDescribe.entrepot_message" class="text" maxlength="200" />
                </td>
            </tr>
            <tr>
                <th>
                    <span class="requiredField"></span>${message("Area.receiving.beginTime")}:
                </th>
                <td>
                    <input type="text" name="areaDescribe.receiving_beginTime" title=${message("area.receiving_beginTime.title")}  class="text" maxlength="200" />
                </td>
            </tr>
            <tr>
                <th>
                    <span class="requiredField"></span>${message("Area.receiving.endTime")}:
                </th>
                <td>
                    <input type="text" name="areaDescribe.receiving_endtime" title=${message("area.receiving_endtime.title")} class="text" maxlength="200" />
                </td>
            </tr>
			<tr>
				<th>
					${message("admin.common.order")}:
				</th>
				<td>
					<input type="text" name="area.orders" class="text" maxlength="9" />
				</td>
			</tr>
			<tr>
				<th>
					&nbsp;
				</th>
				<td>
					<input type="submit" class="button" value="${message("admin.common.submit")}" />
					<input type="button" class="button" value="${message("admin.common.back")}" id="goback" data="${parent.parent_id}" flag="${flag}" />
				</td>
			</tr>
		</table>
	</form>
</body>
</html>
[/#escape]