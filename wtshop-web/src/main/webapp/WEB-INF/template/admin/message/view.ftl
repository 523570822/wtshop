[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>${message("admin.message.view")} - Powered By ${setting.siteAuthor}</title>
<meta name="author" content="${setting.siteAuthor}" />
<meta name="copyright" content="${setting.siteCopyright}" />
<link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.validate.js"></script>
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
		<a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; ${message("admin.message.view")}
	</div>
	<form id="inputForm" >
		<table class="input">
			<tr>
				<th>
					${message("Message.title")}:
				</th>
				<td colspan="2">
					${adminMessage.title}
				</td>
			</tr>
			<tr>
				<th>
				${message("admin.message")}:
				</th>
				<td colspan="2">
					${adminMessage.content}
				</td>
			</tr>
            <tr>
                <th>
				${message("admin.message.name")} :
                </th>
                <td colspan="2">
				${adminMessage.member.username}
                </td>
            </tr>
            <tr>
                <th>
				${message("admin.message.phone")}:
                </th>
                <td colspan="2">
				${adminMessage.member.phone}
                </td>
            </tr>
            <tr>
                <th>
				${message("admin.message.nickname")}:
                </th>
                <td colspan="2">
				${adminMessage.member.nick}
                </td>
            </tr>
            <tr>
                <th>
				${message("admin.message.read")}:
                </th>
                <td colspan="2">
				  ${adminMessage.status?string("是","否")}
                </td>
            </tr>

			<tr>
				<th>
					&nbsp;
				</th>
				<td colspan="2">
					<input type="button" class="button" value="${message("admin.common.back")}" onclick="history.back(); return false;" />
				</td>
			</tr>
		</table>
	</form>
</body>
</html>
[/#escape]