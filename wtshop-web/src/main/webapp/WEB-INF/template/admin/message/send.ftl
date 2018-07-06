[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>${message("admin.message.send")} - Powered By ${setting.siteAuthor}</title>
<meta name="author" content="${setting.siteAuthor}" />
<meta name="copyright" content="${setting.siteCopyright}" />
<link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/input.js"></script>
<script type="text/javascript" src="${base}/statics/lib/layer/layer.js"></script>
<script type="text/javascript" src="${base}/resources/admin/ueditor/ueditor.js"></script>
<script type="text/javascript">
$().ready(function() {

	var $inputForm = $("#inputForm");
	var $isDraft = $("#isDraft");
	var $send = $("#send");

	// 立即发送
	$send.click(function() {
		$isDraft.val("false");
		$inputForm.submit();
	});


	// 表单验证
	$inputForm.validate({
		rules: {
			title: {
				required: true
			},
			content: {
				required: true,
				maxlength: 4000
			}
		}
	});

    $("#selectMember").click(function() {
        window.location.href = "/admin/message/memberSelect.jhtml";
    });

    $("#introduction").editor();

});

function aClick(obj) {
    var val = $(obj).parent().children('input').val();
    [#--$.ajax({--]
        [#--type: 'POST',--]
        [#--dataType: 'json',--]
        [#--url: '${base}/admin/message/handle.jhtml',--]
		[#--data: {id:val},--]
        [#--success: function (data) {--]
            [#--if(data.status == 1){--]
                [#--$(obj).parent().remove();--]
            [#--}--]
        [#--}--]
    [#--});--]
	window.location.href = "/admin/message/handle.jhtml?id="+val;
};

</script>
</head>
<body>
     <input type="hidden" id="detailNum" name="detailNum" value="${detailNum}"/>
	<div class="breadcrumb">
		<a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; ${message("admin.message.send")}
	</div>
	<form id="inputForm" action="sendSubmit.jhtml" method="post">
		<table class="input">
			<tr>
				<th>
					<span class="requiredField">*</span>${message("Message.receiver")}:
				</th>
				<td>
                    <input type="button" value="${message("admin.message.member")}" class="button" id="selectMember"/>
				</td>
			</tr>
			[#if members??]
                <tr>
					<th>
					${message("Review.memberNick")}	:
					</th>
					[#list members as member]
						<td style="display: inline-block;">
							<input type="hidden" name="memberIds" value="${member.id}" />${member.nickname}
							<a href="javascript:void(0)" onclick="aClick(this)" >[${message("admin.common.delete")}]</a>
						</td>
					[/#list]
                </tr>
			[#else ]

			[/#if]
			[#if totalRow??]
				<tr>
					<th>
					${message("admin.message.search")}:
					</th>
					<td>
						${searchValue}
					</td>
				</tr>
                <tr>
                    <th>
					${message("admin.message.count")}:
                    </th>
                    <td>
                        ${totalRow}
                    </td>
                </tr>
				[#list list as member]
                    <input type="hidden" name="memberIds" value="${member.id}" />
				[/#list]
			[/#if]

			<tr>
				<th>
					<span class="requiredField">*</span>${message("Message.title")}:
				</th>
				<td>
					<input type="text" name="title" class="text" value="${(draftMessage.title)!}" maxlength="200" />
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>${message("Message.secondTitle")}:
				</th>
				<td>
                    <input type="text" name="content" class="text" value="${(draftMessage.content)!}" style="width: 600px" />
				</td>
			</tr>

            <tr>
                <th>
                    <span class="requiredField">*</span>${message("Message.link")}:
                </th>
                <td>
                    <textarea id="introduction" name="introduction" class="editor" style="width: 900px" ></textarea>
                </td>
            </tr>
			<tr>
				<th>
					&nbsp;
				</th>
				<td>
					<input type="button" id="send" class="button" value="${message("admin.message.submit")}" />
				</td>
			</tr>
		</table>
	</form>
</body>
</html>
[/#escape]