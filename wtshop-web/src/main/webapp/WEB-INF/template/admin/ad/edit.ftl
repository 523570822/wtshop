[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>${message("admin.ad.edit")} - Powered By ${setting.siteAuthor}</title>
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
    checkParam();//首次验证跳转
    sublevel();
	[@flash_message /]
	
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

    //判断显示不显示二级标题
    var subtext=$("#targetTitleId").find("option:selected").text().replace(/\s/g, "");

    if(subtext=="无跳转"){
        $("#subTitleId").hide();
    }

});
function sublevel() {
    var subtext=$("#targetTitleId").find("option:selected").text().replace(/\s/g, "");
    var num = $("#targetTitleId").find("option:selected").attr("data");
    if(num==1||num==2){
        //清空下拉内容
        $.post("${base}/admin/goodsTheme/levelState.jhtml",{urlType:num},function (data) {
            if(data.length>0){
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
    checkParam();//首次验证跳转
}
function checkParam() {
    var num = $("#targetTitleId").find("option:selected").attr("data");
    if(num!=3||num==10){
        $("#param").attr("readonly",true);
    }else {
        $("#param").attr("readonly",false);
    }
}
</script>
</head>
<body>
	<div class="breadcrumb">
		<a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; ${message("admin.ad.edit")}
	</div>
	<form id="inputForm" action="update.jhtml" method="post">
		<input type="hidden" name="ad.id" value="${ad.id}" />
		<table class="input">
			<tr>
				<th>
					<span class="requiredField">*</span>${message("Ad.title")}:
				</th>
				<td>
					<input type="text" name="ad.title" class="text" value="${ad.title}" maxlength="200" />
				</td>
			</tr>
			<tr>
				<th>
					${message("Ad.type")}:
				</th>
				<td>
					<select id="type" name="type">
						[#list types as typelist]
							<option value="${typelist}"[#if typelist == type] selected="selected"[/#if]>${message("Ad.Type." + typelist)}</option>
						[/#list]
					</select>
				</td>
			</tr>
			<tr>
				<th>
					${message("Ad.adPosition")}:
				</th>
				<td>
					<select name="adPositionId">
						[#list adPositions as adPosition]
							<option value="${adPosition.id}"[#if adPosition == ad.adPosition] selected="selected"[/#if]>${adPosition.name} [${adPosition.width} × ${adPosition.height}]</option>
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
						[#list ad.targetTitle as targetTitle]
							[#if targetTitle.level_state==1]
                                <option value="${targetTitle.id}" data="${targetTitle.urltype}"[#if targetTitle.urltype==ad.urlType.urltype]selected[/#if]>
								${targetTitle.title}
                                </option>
							[/#if]
						[/#list]
                    </select>

                    <select id="subTitleId" name="targetId"  >
						[#list ad.targetTitle as targetTitle]
							[#if targetTitle.level_state==2]
								[#if targetTitle.urltype==ad.urlType.urltype]
                                    <option value="${targetTitle.id}" data="${targetTitle.urltype}"[#if targetTitle.id==ad.urlType.id]selected[/#if]>
									${targetTitle.title}
                                    </option>
								[/#if]
							[/#if]
						[/#list]
                    </select>

                </td>
            </tr>

            <tr>
                <th>
                    </span>${message("Ad.param")}:
                </th>
                <td>
                    <input type="text" name="ad.param" class="text" title="跳转具体活动的参数" value="${ad.param}" maxlength="200" id="param"/>
                </td>
            </tr>

			<tr[#if ad.type != "text"] class="hidden"[/#if]>
				<th>
					${message("Article.content")}:
				</th>
				<td>
					<textarea id="content" name="ad.content" class="editor" style="width: 100%;">${ad.content}</textarea>
				</td>
			</tr>
			<tr[#if ad.type == "text"] class="hidden"[/#if]>
				<th>
					<span class="requiredField">*</span>${message("Ad.path")}:
				</th>
				<td>
					<span class="fieldSet">
						<input type="text" id="path" name="ad.path" class="text" value="${ad.path}" maxlength="200"[#if ad.typeName == "text"] disabled="disabled"[/#if] />
						<a href="javascript:;" id="filePicker" class="button">${message("admin.upload.filePicker")}</a>
					</span>
				</td>
			</tr>
			<tr>
				<th>
					${message("Ad.beginDate")}:
				</th>
				<td>
					<input type="text" id="beginDate" name="ad.begin_date" class="text Wdate" value="[#if ad.begin_date??]${ad.begin_date?string("yyyy-MM-dd HH:mm:ss")}[/#if]" onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss', maxDate: '#F{$dp.$D(\'endDate\')}'});" />
				</td>
			</tr>
			<tr>
				<th>
					${message("Ad.endDate")}:
				</th>
				<td>
					<input type="text" id="endDate" name="ad.end_date" class="text Wdate" value="[#if ad.end_date??]${ad.end_date?string("yyyy-MM-dd HH:mm:ss")}[/#if]" onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss', minDate: '#F{$dp.$D(\'beginDate\')}'});" />
				</td>
			</tr>
			<tr>
				<th>
					${message("admin.common.order")}:
				</th>
				<td>
					<input type="text" name="ad.orders" class="text" value="${ad.orders}" maxlength="9" />
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