[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>${message("admin.partner.view")} - Powered By ${setting.siteAuthor}</title>
<meta name="author" content="${setting.siteAuthor}" />
<meta name="copyright" content="${setting.siteCopyright}" />
<link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.tools.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/input.js"></script>
</head>
<body>
	<div class="breadcrumb">
		<a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; ${message("admin.partner.view")}
	</div>
	<ul id="tab" class="tab">
		<li>
			<input type="button" value="${message("admin.partner.base")}" />
		</li>
		[#if partnerAttributes?has_content]
			<li>
				<input type="button" value="${message("admin.partner.profile")}" />
			</li>
		[/#if]
	</ul>
	<table class="input tabContent">
		<tr>
			<th>
				${message("Member.username")}:
			</th>
			<td>
				${partner.phone}

			</td>
		</tr>
		<tr>
			<th>
				${message("Member.email")}:
			</th>
			<td>
				${partner.email}
			</td>
		</tr>
		<tr>
			<th>
				${message("Member.nickname")}:
			</th>
			<td>
				${partner.nickname}
			</td>
		</tr>
		<tr>
			<th>
				${message("admin.partner.partnerRank")}:
			</th>
			<td>
				${partner.levelName}
			</td>
		</tr>
		<tr>
			<th>
				${message("admin.partner.status")}:
			</th>
			<td>
				[#if partner.isEnabled==0]
					<span class="blue">${partner.enabledName}</span>
				[#elseif partner.isLocked==2]
					<span class="red"> ${partner.enabledName} </span>
				[#elseif partner.isLocked==1]
					<span class="green">${partner.enabledName}</span>
				[/#if]
			</td>
		</tr>
	[#--	[#if partner.isLocked]
			<tr>
				<th>
					${message("Member.lockedDate")}:
				</th>
				<td>
					${partner.lockedDate?string("yyyy-MM-dd HH:mm:ss")}
				</td>
			</tr>
		[/#if]--]


		[#--<tr>
			<th>
				${message("Member.balance")}:
			</th>
			<td>
				${currency(partner.balance, true)}
			</td>
		</tr>--]
		<tr>
			<th>
				${message("Member.partner.amount")}:
			</th>
			<td>
				${currency(partner.amount, true)}
			</td>
		</tr>
		<tr>
			<th>
				${message("admin.common.createDate")}:
			</th>
			<td>
				${partner.createDate?string("yyyy-MM-dd HH:mm:ss")}
			</td>
		</tr>





		<tr>
			<th>
				${message("admin.partner.locale")}:
			</th>
			<td>
				${partner.fullName}
			</td>
		</tr>
	</table>
	[#if partnerAttributes?has_content]
		<table class="input tabContent">
			[#list partnerAttributes as partnerAttribute]
				<tr>
					<th>
						${partnerAttribute.name}:
					</th>
					<td>
						[#if partnerAttribute.typeName == "name"]
							${partner.name}
						[#elseif partnerAttribute.typeName == "gender"]
							[#if partner.gender??]
								${message("Member.Gender." + partner.genderName)}
							[/#if]
						[#elseif partnerAttribute.typeName == "birth"]
							${partner.birth}
						[#elseif partnerAttribute.typeName == "area"]
							[#if partner.area??]
								${partner.area.fullName}
							[#else]
								${partner.areaName}
							[/#if]
						[#elseif partnerAttribute.typeName == "address"]
							${partner.address}
						[#elseif partnerAttribute.typeName == "zipCode"]
							${partner.zipCode}
						[#elseif partnerAttribute.typeName == "phone"]
							${partner.phone}
						[#elseif partnerAttribute.typeName == "mobile"]
							${partner.mobile}
						[#elseif partnerAttribute.typeName == "text"]
							${partner.getAttributeValue(partnerAttribute)}
						[#elseif partnerAttribute.typeName == "select"]
							${partner.getAttributeValue(partnerAttribute)}
						[#elseif partnerAttribute.typeName == "checkbox"]
							[#list partner.getAttributeValue(partnerAttribute) as option]
								${option}
							[/#list]
						[/#if]
					</td>
				</tr>
			[/#list]
		</table>
	[/#if]
	<table class="input">
		<tr>
			<th>
				&nbsp;
			</th>
			<td>
				<input type="button" class="button" value="${message("admin.common.back")}" onclick="history.back(); return false;" />
			</td>
		</tr>
	</table>
</body>
</html>
[/#escape]