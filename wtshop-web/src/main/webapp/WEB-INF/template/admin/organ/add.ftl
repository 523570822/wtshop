[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>${message("admin.parameter.add")} - Powered By ${setting.siteAuthor}</title>
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

	[@flash_message /]

	// 表单验证
	$inputForm.validate({
		rules: {
            "organ.name": "required",
            "organ.admin_name": {
                required: true,
                pattern: /^(1[3|4|5|7|8][0-9]{9}$).*$/i,
                minlength: ${setting.passwordMinLength}
            },
            "organ.admin_password": {
                required: true,
                pattern: /^[a-zA-Z0-9]{6,12}$/,
                minlength: ${setting.passwordMinLength}
            },
		}
	});
    $("#adminName").change(function () {
        $.ajax({
            url: "checkout.jhtml",
            type: "POST",
            data: {name:$("#adminName").val()},
            dataType: "json",
            cache: false,
            success: function(message) {
                if (message.type != "success") {
                    $("#idName").remove();
                    $("#adminName").after("<label id='idName' style='color: #c09853'>${message("NewGoods.no")}</label>");
					return false;
                }else {
                    $("#idName").remove();
				}
            }
        })
    });
})
</script>
</head>
<body>
	<div class="breadcrumb">
		<a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; ${message("NewGoods.addStory")}
	</div>
	<form id="inputForm" action="save.jhtml" method="post">
		<table class="input">
			<tr>
				<th>
                    <span class="requiredField">*</span>${message("NewGoods.name")}:
				</th>
				<td>
                    <input id="name" type="text" name="organ.name" class="text" maxlength="200" />
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>${message("NewGoods.admin")}:
				</th>
				<td>
					<input id="adminName" type="text" name="organ.admin_name" class="text" maxlength="200" placeholder="${message("admin.member.phone")}" />
				</td>
			</tr>
			<tr>
                <th>
                    <span class="requiredField">*</span>${message("NewGoods.password")}:
                </th>
                <td>
                    <input id="amdinPassword" type="password" name="organ.admin_password" class="text" maxlength="200" placeholder=${message("admin.member.limit")} autocomplete="off"/>
                </td>
			</tr>
			<tr>
				<th>
					${message("NewGoods.desc")}：
				</th>
				<td>
                    <input type="text" name="organ.desc" class="text" maxlength="200" />
				</td>
			</tr>
            <tr>
                <th>
                    ${message("NewGoods.rank")}：
                </th>
                <td>
                    <input type="text" name="organ.level" class="text" maxlength="200" />
                </td>
            </tr>
            <tr>
                <th>
                    ${message("NewGoods.status")}：
                </th>
                <td>
                    <select id="status" name="status">
						[#list status as state]
                            <option  value="${state}" [#if state == "using"] selected="selected"[/#if]>${message("Organ.Status." + state)}</option>
						[/#list]
                    </select>
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