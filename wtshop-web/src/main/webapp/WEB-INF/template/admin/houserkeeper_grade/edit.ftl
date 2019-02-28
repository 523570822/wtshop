[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>${message("admin.memberRank.edit")} - Powered By ${setting.siteAuthor}</title>
<meta name="author" content="${setting.siteAuthor}" />
<meta name="copyright" content="${setting.siteCopyright}" />
<link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.tools.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/input.js"></script>
<script type="text/javascript">
$().ready(function() {

	var $inputForm = $("#inputForm");
	var $amount = $("#amount");
	var $isSpecial = $("#isSpecial");
	
	[@flash_message /]
	
	// 是否特殊
	$isSpecial.click(function() {
		if ($(this).prop("checked")) {
			$amount.val("").prop("disabled", true);
		} else {
			$amount.prop("disabled", false);
		}
	});
	
	// 表单验证
	$inputForm.validate({
		rules: {
			"HouserkeeperGrade.name": {
				required: true,
				remote: {
					url: "checkName.jhtml?previousName=${memberRank.name?url}",
					cache: false
				}
			},
			"HouserkeeperGrade.scale": {
				required: true,
				min: 0,
				decimal: {
					integer: 3,
					fraction: 3
				}
			},
			"HouserkeeperGrade.amount": {
				required: true,
				min: 0,
				decimal: {
					integer: 12,
					fraction: ${setting.priceScale}
				},
				remote: {
					url: "checkAmount.jhtml?previousAmount=${memberRank.amount}",
					cache: false
				}
			}
		},
		messages: {
			"HouserkeeperGrade.name": {
				remote: "${message("admin.validate.exist")}"
			},
			"HouserkeeperGrade.amount": {
				remote: "${message("admin.validate.exist")}"
			}
		}
	});

});
</script>
</head>
<body>
	<div class="breadcrumb">
		<a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; ${message("admin.memberRank.edit")}
	</div>
	<form id="inputForm" action="update.jhtml" method="post">
		<input type="hidden" name="houserkeeperGrade.id" value="${houserkeeperGrade.id}" />
		<table class="input">
			<tr>
				<th>
					<span class="requiredField">*</span>${message("MemberRank.name")}:
				</th>
				<td>
					<input type="text" name="houserkeeperGrade.name" class="text" value="${houserkeeperGrade.name}" maxlength="200" />
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>${message("MemberRank.scale")}:
				</th>
				<td>
					<input type="text" name="houserkeeperGrade.scale" class="text" value="${houserkeeperGrade.scale}" maxlength="7" />
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>${message("MemberRank.amount")}:
				</th>
				<td>
					<input type="text" id="amount" name="houserkeeperGrade.amount" class="text" value="${houserkeeperGrade.amount}" maxlength="16"[#if HouserkeeperGrade.isSpecial] disabled="disabled"[/#if] />
				</td>
			</tr>
			<tr>
				<th>
					${message("admin.common.setting")}:
				</th>
				<td>
					<label>
						<input type="checkbox" name="isDefault" value="true"[#if houserkeeperGrade.isDefault] checked="checked" disabled="disabled"[/#if] />${message("MemberRank.isDefault")}
						<input type="hidden" name="_isDefault" value="false" />
					</label>
					<label title="${message("admin.HouserkeeperGrade.isSpecialTitle")}">
						<input type="checkbox" id="isSpecial" name="isSpecial" value="true"[#if houserkeeperGrade.isSpecial] checked="checked"[/#if] />${message("MemberRank.isSpecial")}
						<input type="hidden" name="_isSpecial" value="false" />
					</label>
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