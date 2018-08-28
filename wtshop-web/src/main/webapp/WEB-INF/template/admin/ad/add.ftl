[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>${message("admin.ad.add")} - Powered By ${setting.siteAuthor}</title>
<meta name="author" content="${setting.siteAuthor}" />
<meta name="copyright" content="${setting.siteCopyright}" />
<link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/webuploader.js"></script>
<script type="text/javascript" src="${base}/resources/admin/ueditor/ueditor.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/input.js"></script>
<script type="text/javascript" src="${base}/resources/admin/datePicker/WdatePicker.js"></script>
<script type="text/javascript">
$().ready(function() {

	var $inputForm = $("#inputForm");
	var $type = $("#type");
	var $content = $("#content");
	var $path = $("#path");
	var $filePicker = $("#filePicker");
	
	[@flash_message /]
    checkParam();//首次验证跳转
    sublevel();



	$filePicker.uploader();
	
	$content.editor();
	
	$type.change(function() {
		if ($(this).val() == "text") {
			$content.prop("disabled", false).closest("tr").show();
			$path.prop("disabled", true).closest("tr").hide();
		} else {
			$content.prop("disabled", true).closest("tr").hide();
			$path.prop("disabled", false).closest("tr").show();
		}
	});
	
	// 表单验证
	$inputForm.validate({
		rules: {
			"ad.title": "required",
			adPositionId: "required",
			"ad.path": {
				required: true,
				pattern: /^(http:\/\/|https:\/\/|\/).*$/i
			},
			"ad.url": {
				pattern: /^(http:\/\/|https:\/\/|ftp:\/\/|mailto:|\/|#).*$/i
			},
			"ad.orders": "digits"
		}
	});

    $("#subTitleId").hide();

});
function sublevel() {
    var subtext=$("#targetTitleId").find("option:selected").text().replace(/\s/g, "");
    var num = $("#targetTitleId").find("option:selected").attr("data");
    console.info(num);
    $("#param").val("")
    if(num==1||num==2){


        //清空下拉内容
        $.post("${base}/admin/goodsTheme/levelState.jhtml",{urlType:num},function (data) {
            if(data.length>0){
                $("#param").attr("readonly",false);

                $("#subTitleId").empty();
                //遍历添加 查到的二级内容
                for (var i=0;i<data.length;i++){
                    //用JS动态添加select的option
                    var op=document.createElement("option");      // 新建OPTION (op)
                    op.setAttribute("value",data[i].id);                 // 设置OPTION的 VALUE
                    // 设置扩展属性，用于验证
                    op.appendChild(document.createTextNode(data[i].title)); // 设置OPTION的 TEXT
                    document.getElementById("subTitleId").appendChild(op);
                }
                $("#subTitleId").show();
            }else {
                $("#subTitleId").hide();
            }
        })

    }else {
        $("#subTitleId").empty();
        $("#subTitleId").hide();
    }
    checkParam();
}
function checkParam() {
    var num = $("#targetTitleId").find("option:selected").attr("data");
    if(num!=3){
        $("#param").attr("readonly",true);
    }else {
        $("#param").attr("readonly",false);
    }
}
</script>
</head>
<body>
	<div class="breadcrumb">
		<a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; ${message("admin.ad.add")}
	</div>
	<form id="inputForm" action="save.jhtml" method="post">
		<table class="input">
			<tr>
				<th>
					<span class="requiredField">123*</span>${message("Ad.title")}:
				</th>
				<td>
					<input type="text" name="ad.title" class="text" maxlength="200" />
				</td>
			</tr>
			<tr>
				<th>
					${message("Ad.type")}:
				</th>
				<td>
					<select id="type" name="type">
						[#list types as type]
							<option value="${type}">${message("Ad.Type." + type)}</option>
						[/#list]
					</select>
				</td>
			</tr>

            <tr>
                <th>
				${message("Ad.toUrl")}:
                </th>
                <td>
                    <select id="targetTitleId" name="ad.target_id" onchange="sublevel()">
						[#list targetTitleTree as targetTitle]
							[#if targetTitle.level_state==1]
                                <option value="${targetTitle.id}" data="${targetTitle.urltype}" [#if targetTitle.id==1]selected[/#if]>
								${targetTitle.title}
                                </option>
							[/#if]
						[/#list]
                    </select>

                    <select id="subTitleId" name="targetId" >
                    </select>
                </td>
            </tr>

            <tr>
                <th>
                   </span>${message("Ad.param")}:
                </th>
                <td>
                    <input type="text" name="ad.param" class="text" title="${message("admin.ad.param")}" maxlength="200" id="param"/>
                </td>
            </tr>

			<tr>
				<th>
					${message("Ad.adPosition")}:
				</th>
				<td>
					<select name="adPositionId">
						[#list adPositions as adPosition]
							<option value="${adPosition.id}">${adPosition.name} [${adPosition.width} × ${adPosition.height}]</option>
						[/#list]
					</select>
				</td>
			</tr>
			<tr>
				<th>
					${message("Article.content")}:
				</th>
				<td>
					<textarea id="content" name="ad.content" class="editor"></textarea>
				</td>
			</tr>
			<tr class="hidden">
				<th>
					<span class="requiredField">*</span>${message("Ad.path")}:
				</th>
				<td>
					<span class="fieldSet">
						<input type="text" id="path" name="ad.path" class="text" maxlength="200" disabled="disabled" />
						<a href="javascript:;" id="filePicker" class="button">${message("admin.upload.filePicker")}</a>
					</span>
				</td>
			</tr>
			<tr>
				<th>
					${message("Ad.beginDate")}:
				</th>
				<td>
					<input type="text" id="beginDate" name="ad.begin_date" class="text Wdate" onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss', maxDate: '#F{$dp.$D(\'endDate\')}'});" />
				</td>
			</tr>
			<tr>
				<th>
					${message("Ad.endDate")}:
				</th>
				<td>
					<input type="text" id="endDate" name="ad.end_date" class="text Wdate" onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss', minDate: '#F{$dp.$D(\'beginDate\')}'});" />
				</td>
			</tr>
			<tr>
				<th>
					${message("admin.common.order")}:
				</th>
				<td>
					<input type="text" name="ad.orders" class="text" maxlength="9" />
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