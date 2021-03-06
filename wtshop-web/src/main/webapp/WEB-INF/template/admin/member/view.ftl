[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>${message("admin.member.view")} - Powered By ${setting.siteAuthor}</title>
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
		<a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; ${message("admin.member.view")}
	</div>
	<ul id="tab" class="tab">
		<li>
			<input type="button" value="${message("admin.member.base")}" />
		</li>
		[#if memberAttributes?has_content]
			<li>
				<input type="button" value="${message("admin.member.profile")}" />
			</li>
		[/#if]
	</ul>
	<table class="input tabContent">
		<tr>
			<th>
				${message("Member.username")}:
			</th>
			<td>
				${member.phone}

			</td>
		</tr>
		<tr>
			<th>
				${message("Member.email")}:
			</th>
			<td>
				${member.email}
			</td>
		</tr>
		<tr>
			<th>
				${message("Member.nickname")}:
			</th>
			<td>
				${member.nickname}
			</td>
		</tr>
		<tr>
			<th>
				${message("Member.memberRank")}:
			</th>
			<td>
				${member.memberRank.name}
			</td>
		</tr>
		<tr>
			<th>
				${message("admin.member.status")}:
			</th>
			<td>
				[#if !member.isEnabled]
					<span class="red">${message("admin.member.disabled")}</span>
				[#elseif member.isLocked]
					<span class="red"> ${message("admin.member.locked")} </span>
				[#else]
					<span class="green">${message("admin.member.normal")}</span>
				[/#if]
			</td>
		</tr>
		[#if member.isLocked]
			<tr>
				<th>
					${message("Member.lockedDate")}:
				</th>
				<td>
					${member.lockedDate?string("yyyy-MM-dd HH:mm:ss")}
				</td>
			</tr>
		[/#if]
		<tr>
			<th>
				${message("Member.point")}:
			</th>
			<td>
				${member.point}
			</td>
		</tr>
		<tr>
			<th>
				${message("Member.balance")}:
			</th>
			<td>
				${currency(member.balance, true)}
			</td>
		</tr>
		<tr>
			<th>
				${message("Member.amount")}:
			</th>
			<td>
				${currency(member.amount, true)}
			</td>
		</tr>
		<tr>
			<th>
				${message("admin.common.createDate")}:
			</th>
			<td>
				${member.createDate?string("yyyy-MM-dd HH:mm:ss")}
			</td>
		</tr>
		<tr>
			<th>
				${message("Member.loginDate")}:
			</th>
			<td>
				${(member.loginDate?string("yyyy-MM-dd HH:mm:ss"))!"-"}
			</td>
		</tr>
		<tr>
			<th>
				${message("Member.registerIp")}:
			</th>
			<td>
				${member.registerIp}
			</td>
		</tr>
		<tr>
			<th>
				${message("Member.loginIp")}:
			</th>
			<td>
				${member.loginIp!"-"}
			</td>
		</tr>
		<tr>
			<th>
				${message("admin.member.reviewCount")}:
			</th>
			<td>
				${member.reviews?size}
			</td>
		</tr>
		<tr>
			<th>
				${message("admin.member.consultationCount")}:
			</th>
			<td>
				${member.consultations?size}
			</td>
		</tr>
		<tr>
			<th>
				${message("admin.member.favoriteGoodsCount")}:
			</th>
			<td>
				${member.favoriteGoods?size}
			</td>
		</tr>
	</table>
	[#if memberAttributes?has_content]
		<table class="input tabContent">
			[#list memberAttributes as memberAttribute]
				<tr>
					<th>
						${memberAttribute.name}:
					</th>
					<td>
						[#if memberAttribute.typeName == "name"]
							${member.name}
						[#elseif memberAttribute.typeName == "gender"]
							[#if member.gender??]
								${message("Member.Gender." + member.genderName)}
							[/#if]
						[#elseif memberAttribute.typeName == "birth"]
							${member.birth}
						[#elseif memberAttribute.typeName == "area"]
							[#if member.area??]
								${member.area.fullName}
							[#else]
								${member.areaName}
							[/#if]
						[#elseif memberAttribute.typeName == "address"]
							${member.address}
						[#elseif memberAttribute.typeName == "zipCode"]
							${member.zipCode}
						[#elseif memberAttribute.typeName == "phone"]
							${member.phone}
						[#elseif memberAttribute.typeName == "mobile"]
							${member.mobile}
						[#elseif memberAttribute.typeName == "text"]
							${member.getAttributeValue(memberAttribute)}
						[#elseif memberAttribute.typeName == "select"]
							${member.getAttributeValue(memberAttribute)}
						[#elseif memberAttribute.typeName == "checkbox"]
							[#list member.getAttributeValue(memberAttribute) as option]
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