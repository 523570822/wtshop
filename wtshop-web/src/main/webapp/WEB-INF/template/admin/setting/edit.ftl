[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>${message("admin.setting.edit")} - Powered By ${setting.siteAuthor}</title>
<meta name="author" content="${setting.siteAuthor}" />
<meta name="copyright" content="${setting.siteCopyright}" />
<link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.tools.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/admin/ueditor/ueditor.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/input.js"></script>
<script type="text/javascript" src="${base}/statics/lib/layer/layer.js"></script>
<script type="text/javascript" src="${base}/resources/admin/datePicker/WdatePicker.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/webuploader.js"></script>
<script type="text/javascript">
$().ready(function() {

	var $inputForm = $("#inputForm");
	var $filePicker = $("a.filePicker");
	var $smtpHost = $("#smtpHost");
	var $smtpPort = $("#smtpPort");
	var $smtpUsername = $("#smtpUsername");
	var $smtpPassword = $("#smtpPassword");
	var $smtpSSLEnabled = $("#smtpSSLEnabled");
	var $smtpFromMail = $("#smtpFromMail");
	var $testSmtp = $("#testSmtp");
	var $toMail = $("#toMail");
	var $sendMail = $("#sendMail");
	var $testSmtpStatus = $("#testSmtpStatus");
	var $smsBalance = $("#smsBalance");
	
	[@flash_message /]
	
	$filePicker.uploader();
	
	// 邮件测试
	$testSmtp.click(function() {
		$testSmtp.closest("tr").hide();
		$toMail.closest("tr").show();
	});
	
	// 发送邮件
	$sendMail.click(function() {
		$toMail.removeClass("ignore");
		var validator = $inputForm.validate();
		var isValid = validator.element($smtpFromMail) & validator.element($smtpHost) & validator.element($smtpPort) & validator.element($smtpUsername) & validator.element($toMail);
		$toMail.addClass("ignore");
		$.ajax({
			url: "testSmtp.jhtml",
			type: "POST",
			data: {smtpHost: $smtpHost.val(), smtpPort: $smtpPort.val(), smtpUsername: $smtpUsername.val(), smtpPassword: $smtpPassword.val(), smtpSSLEnabled: $smtpSSLEnabled.prop("checked"), smtpFromMail: $smtpFromMail.val(), toMail: $toMail.val()},
			dataType: "json",
			cache: false,
			beforeSend: function() {
				if (!isValid) {
					return false;
				}
				$testSmtpStatus.html('<span class="loadingIcon">&nbsp;<\/span>${message("admin.setting.sendMailLoading")}');
				$sendMail.prop("disabled", true);
			},
			success: function(message) {
				$testSmtpStatus.empty();
				$sendMail.prop("disabled", false);
				$.message(message);
			}
		});
	});
	
	// 短信余额查询
	$smsBalance.click(function() {
		var $this = $(this);
		$.ajax({
			url: "smsBalance.jhtml",
			type: "GET",
			dataType: "json",
			cache: false,
			beforeSend: function() {
				$this.prop("disabled", true).after('<span class="loadingIcon">&nbsp;<\/span>');
			},
			success: function(message) {
				$this.prop("disabled", false).nextAll("span").remove();
				if (message.type == "success") {
					$.dialog({
						type: "warn",
						content: message.content,
						modal: true,
						ok: null,
						cancel: null
					});
				} else {
					$.message(message);
				}
			}
		});
		return false;
	});

	$.validator.addMethod("compareLength",
		function(value, element, param) {
			return this.optional(element) || $.trim(value) == "" || $.trim($(param).val()) == "" || parseFloat(value) >= parseFloat($(param).val());
		},
		"${message("admin.setting.compareLength")}"
	);

	$.validator.addMethod("requiredTo",
		function(value, element, param) {
			var parameterValue = $(param).val();
			if ($.trim(parameterValue) == "" || ($.trim(parameterValue) != "" && $.trim(value) != "")) {
				return true;
			} else {
				return false;
			}
		},
		"${message("admin.setting.requiredTo")}"
	);


    $("#isFreeMoney").change(function () {
        if ($('#isFreeMoney').attr('checked')) {
            $("#freeMoney").attr("disabled","disabled");
            $("#freeMoney").val("");
        }else {
            $("#freeMoney").removeAttr("disabled");
		}
    });

//    $("#isReturnInsurance").change(function () {
//        if ($('#isReturnInsurance').attr('checked')) {
//            $("#returnMoney").attr("disabled","disabled");
//            $("#returnMoney").val("");
//        }else {
//            $("#returnMoney").removeAttr("disabled");
//        }
//    });

    $("#isReturnInsurance").change(function () {
        var money = $("#returnMoney").val();
        if ($('#isReturnInsurance').attr('checked')) {
            if(money == null || money == ""){
                $("#idName1").remove();
                $("#returnMoney").after("<label id='idName1' style='color: #c09853'>${message("setting.edit.thhbxjebnwk")}</label>");
                return false;
            }else {
                $("#idName1").remove();
            }
        }else {
            $("#idName1").remove();
        }

    });

    $("#returnMoney").change(function () {
        var money = $(this).val();
        if ($('#isReturnInsurance').attr('checked')) {
            if(money == null || money == ""){
                $("#idName1").remove();
                $("#returnMoney").after("<label id='idName1' style='color: #c09853'>${message("setting.edit.thhbxjebnwk")}</label>");
                return false;
            }else {
                $("#idName1").remove();
            }
        }
    });

    $("#isSendMiaoBi").change(function () {
        var money = $("#sendMiaoBiLimit").val();
        if ($('#isSendMiaoBi').attr('checked')) {
            if(money == null || money == ""){
                $("#idName2").remove();
                $("#sendMiaoBiLimit").after("<label id='idName2' style='color: #c09853'>${message("setting.edit.mbzsblbnwk")}</label>");
                return false;
            }else {
                $("#idName2").remove();
            }
        }else {
            $("#idName2").remove();
        }

    });

    $("#sendMiaoBiLimit").change(function () {
        var money = $(this).val();
        if ($('#isSendMiaoBi').attr('checked')) {
            if(money == null || money == ""){
                $("#idName2").remove();
                $("#sendMiaoBiLimit").after("<label id='idName2' style='color: #c09853'>${message("setting.edit.mbzsblbnwk")}</label>");
                return false;
            }else {
                $("#idName2").remove();
            }
        }
    });

    $("#isUseMiaoBi").change(function () {
        var money = $("#miaoBiLimit").val();
        if ($('#isUseMiaoBi').attr('checked')) {
            if(money == null || money == ""){
                $("#idName3").remove();
                $("#miaoBiLimit").after("<label id='idName3' style='color: #c09853'>${message("setting.edit.mbdkedbnwk")}</label>");
                return false;
            }else {
                $("#idName3").remove();
            }
        }else {
            $("#idName3").remove();
        }

    });

    $("#miaoBiLimit").change(function () {
        var money = $(this).val();
        if ($('#isUseMiaoBi').attr('checked')) {
            if(money == null || money == ""){
                $("#idName3").remove();
                $("#miaoBiLimit").after("<label id='idName3' style='color: #c09853'>${message("setting.edit.mbdkedbnwk")}</label>");
                return false;
            }else {
                $("#idName3").remove();
            }
        }
    });



    // 表单验证
	$inputForm.validate({
		rules: {
			"setting.siteName": "required",
            "setting.siteUrl": {
				required: true,
				pattern: /^(http:\/\/|https:\/\/).*$/i
			},
            "setting.logo": {
				required: true,
				pattern: /^(http:\/\/|https:\/\/|\/).*$/i
			},
            "setting.email": "email",
            "setting.siteCloseMessage": "required",
            "setting.largeProductImageWidth": {
				required: true,
				integer: true,
				min: 1
			},
			"setting.largeProductImageHeight": {
					required: true,
					integer: true,
					min: 1
				},
			"setting.mediumProductImageWidth": {
					required: true,
					integer: true,
					min: 1
				},
			"setting.mediumProductImageHeight": {
					required: true,
					integer: true,
					min: 1
				},
			"setting.thumbnailProductImageWidth": {
					required: true,
					integer: true,
					min: 1
				},
			"setting.thumbnailProductImageHeight": {
					required: true,
					integer: true,
					min: 1
				},
			"setting.defaultLargeProductImage": {
					required: true,
					pattern: /^(http:\/\/|https:\/\/|\/).*$/i
				},
			"setting.defaultMediumProductImage": {
					required: true,
					pattern: /^(http:\/\/|https:\/\/|\/).*$/i
				},
			"setting.defaultThumbnailProductImage": {
					required: true,
					pattern: /^(http:\/\/|https:\/\/|\/).*$/i
				},
			"setting.watermarkAlpha": {
					required: true,
					digits: true,
					max: 100
				},
			"setting.watermarkImageFile": {
					extension: "${setting.uploadImageExtension}"
					},
			"setting.defaultMarketPriceScale": {
					required: true,
					min: 0,
					decimal: {
						integer: 3,
						fraction: 3
					}
				},
			"setting.usernameMinLength": {
					required: true,
					integer: true,
					min: 1,
					max: 117
				},
			"setting.usernameMaxLength": {
					required: true,
					integer: true,
					min: 1,
					max: 117,
					compareLength: "#usernameMinLength"
				},
			"setting.passwordMinLength": {
					required: true,
					integer: true,
					min: 1,
					max: 117
				},
			"setting.passwordMaxLength": {
					required: true,
					integer: true,
					min: 1,
					max: 117,
					compareLength: "#passwordMinLength"
				},
			"setting.registerPoint"	: "required",
			"setting.registerAgreement": "required",
			"setting.accountLockCount": {
					required: true,
					integer: true,
					min: 1
				},
			"setting.accountLockTime": {
					required: true,
					digits: true
				},
			"setting.safeKeyExpiryTime": {
					required: true,
					digits: true
				},
			"setting.uploadMaxSize": {
					required: true,
					digits: true
				},
			"setting.imageUploadPath": "required",
			"setting.mediaUploadPath": "required",
			"setting.fileUploadPath": "required",
			"setting.smtpFromMail": {
					required: true,
					email: true
				},
			"setting.smtpHost": "required",
				"setting.smtpPort": {
					required: true,
					digits: true
				},
			"setting.smtpUsername": "required",
			"setting.toMail": {
					required: true,
					email: true
				},
			"setting.currencySign": "required",
				"setting.currencyUnit": "required",
				"setting.stockAlertCount": {
					required: true,
					digits: true
				},
			"setting.defaultPointScale": {
					required: true,
					min: 0,
					decimal: {
						integer: 3,
						fraction: 3
					}
				},
			 "setting.taxRate": {
					required: true,
					min: 0,
					decimal: {
						integer: 3,
						fraction: 3
					}
			 },
			"setting.cookiePath": "required",
				"setting.smsKey": {
					requiredTo: "#smsSn"
				},
			"setting.certifiedCopyUrl": {
					url: true
				},
			"setting.shoppingCopyUrl": {
					url: true
				},
			"setting.returnCopyUrl": {
					url: true
				},
			"setting.freeMoney": {
                    required:true,
					digits: true,
					min: 0,
					max: 100000000

			},
			"setting.commomPayTime": {
				digits: true,
				min: 0,
				max: 100000000
			},
			"setting.shoppingCopy": {
				rangelength:[0,255]
			},
			"setting.certifiedCopy": {
				rangelength:[0,255]
			},
			"setting.returnMoney": {
				digits: true,
				min: 0,
				max: 100000000
			},
			"setting.miaoBiLimit": {
				number:true
			},
			"setting.scale": {
				digits: true,
				min: 0,
				max: 10000
			},
			"setting.sendMiaoBiLimit": {
				digits: true,
				min: 0,
				max: 10000
			}
        }

    });
})

</script>
</head>
<body>
	<div class="breadcrumb">
		<a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; ${message("admin.setting.edit")}
	</div>
	<form id="inputForm" action="update.jhtml" method="post" enctype="multipart/form-data">
		<ul id="tab" class="tab">
			<li>
				<input type="button" value="${message("admin.setting.base")}" />
			</li>
			<li>
				<input type="button" value="${message("admin.setting.show")}" />
			</li>
			<li>
				<input type="button" value="${message("admin.setting.registerSecurity")}" />
			</li>
			<li>
				<input type="button" value="${message("admin.setting.mail")}" />
			</li>
			<li>
				<input type="button" value="${message("admin.setting.other")}" />
			</li>
            <li>
                <input type="button" value="${message("setting.edit.appSet")}" />
            </li>
		</ul>
		<table class="input tabContent">
			<tr>
				<th>
					<span class="requiredField">*</span>${message("Setting.siteName")}:
				</th>
				<td>
					<input type="text" name="setting.siteName" class="text" value="${setting.siteName}" maxlength="200" />
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>${message("Setting.siteUrl")}:
				</th>
				<td>
					<input type="text" name="setting.siteUrl" class="text" value="${setting.siteUrl}" maxlength="200" />
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>${message("Setting.logo")}:
				</th>
				<td>
					<span class="fieldSet">
						<input type="text" name="setting.logo" class="text" value="${setting.logo}" maxlength="200" />
						<a href="javascript:;" class="button filePicker">${message("admin.upload.filePicker")}</a>
						<a href="${fileServer}${setting.logo}" target="_blank">${message("admin.common.view")}</a>
					</span>
				</td>
			</tr>
            <tr>
                <th>
                    <span class="requiredField">*</span>${message("setting.edit.defImg")}:
                </th>
                <td>
					<span class="fieldSet">
						<input type="text" name="setting.avatar" class="text" value="${setting.avatar}" maxlength="200" />
						<a href="javascript:;" class="button filePicker">${message("admin.upload.filePicker")}</a>
						<a href="${fileServer}${setting.avatar}" target="_blank">${message("admin.common.view")}</a>
					</span>
                </td>
            </tr>

            <tr>
                <th>
                    ${message("setting.edit.sendMiaoBiImg")}:
                </th>
                <td>
					<span class="fieldSet">
						<input type="text" name="setting.sendMiaoBiImg" class="text" value="${setting.sendMiaoBiImg}" maxlength="200" />
						<a href="javascript:;" class="button filePicker">${message("admin.upload.filePicker")}</a>
						<a href="${fileServer}${setting.avatar}" target="_blank">${message("admin.common.view")}</a>
					</span>
                </td>
            </tr>
			<tr>
				<th>
					${message("Setting.hotSearch")}:
				</th>
				<td>
					<input type="text" name="setting.hotSearch" class="text" value="${setting.hotSearch}" maxlength="200" title="${message("admin.setting.hotSearchTitle")}" />
				</td>
			</tr>
			<tr>
				<th>
					${message("Setting.address")}:
				</th>
				<td>
					<input type="text" name="setting.address" class="text" value="${setting.address}" maxlength="200" />
				</td>
			</tr>
			<tr>
				<th>
					${message("Setting.phone")}:
				</th>
				<td>
					<input type="text" name="setting.phone" class="text" value="${setting.phone}" maxlength="200" />
				</td>
			</tr>
			<tr>
				<th>
					${message("Setting.zipCode")}:
				</th>
				<td>
					<input type="text" name="setting.zipCode" class="text" value="${setting.zipCode}" maxlength="200" />
				</td>
			</tr>
			<tr>
				<th>
					${message("Setting.email")}:
				</th>
				<td>
					<input type="text" name="setting.email" class="text" value="${setting.email}" maxlength="200" />
				</td>
			</tr>
			<tr>
				<th>
					${message("Setting.certtext")}:
				</th>
				<td>
					<input type="text" name="setting.certtext" class="text" value="${setting.certtext}" maxlength="200" />
				</td>
			</tr>
			<tr>
				<th>
					${message("Setting.isSiteEnabled")}:
				</th>
				<td>
					<input type="checkbox" name="isSiteEnabled" value="true"[#if setting.isSiteEnabled] checked="checked"[/#if] />
					<input type="hidden" name="_isSiteEnabled" value="false" />
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>${message("Setting.siteCloseMessage")}:
				</th>
				<td>
					<textarea name="setting.siteCloseMessage" class="text">${setting.siteCloseMessage}</textarea>
				</td>
			</tr>
		</table>
		<table class="input tabContent">
			<tr>
				<th>
					${message("Setting.locale")}:
				</th>
				<td>
					<select name="locale">
						[#list locales as locale]
							<option value="${locale}"[#if locale == setting.locale] selected="selected"[/#if]>${message("Setting.Locale." + locale)}</option>
						[/#list]
					</select>
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>${message("admin.setting.largeProductImage")}:
				</th>
				<td>
					${message("admin.setting.width")}:
					<input type="text" name="setting.largeProductImageWidth" class="text" value="${setting.largeProductImageWidth}" maxlength="9" style="width: 50px;" title="${message("admin.setting.widthTitle")}" />
					${message("admin.setting.height")}:
					<input type="text" name="setting.largeProductImageHeight" class="text" value="${setting.largeProductImageHeight}" maxlength="9" style="width: 50px;" title="${message("admin.setting.heightTitle")}" />
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>${message("admin.setting.mediumProductImage")}:
				</th>
				<td>
					${message("admin.setting.width")}:
					<input type="text" name="setting.mediumProductImageWidth" class="text" value="${setting.mediumProductImageWidth}" maxlength="9" style="width: 50px;" title="${message("admin.setting.widthTitle")}" />
					${message("admin.setting.height")}:
					<input type="text" name="setting.mediumProductImageHeight" class="text" value="${setting.mediumProductImageHeight}" maxlength="9" style="width: 50px;" title="${message("admin.setting.heightTitle")}" />
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>${message("admin.setting.thumbnailProductImage")}:
				</th>
				<td>
					${message("admin.setting.width")}:
					<input type="text" name="setting.thumbnailProductImageWidth" class="text" value="${setting.thumbnailProductImageWidth}" maxlength="9" style="width: 50px;" title="${message("admin.setting.widthTitle")}" />
					${message("admin.setting.height")}:
					<input type="text" name="setting.thumbnailProductImageHeight" class="text" value="${setting.thumbnailProductImageHeight}" maxlength="9" style="width: 50px;" title="${message("admin.setting.heightTitle")}" />
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>${message("admin.setting.defaultLargeProductImage")}:
				</th>
				<td>
					<span class="fieldSet">
						<input type="text" name="setting.defaultLargeProductImage" class="text" value="${setting.defaultLargeProductImage}" maxlength="200" />
						<a href="javascript:;" class="button filePicker">${message("admin.upload.filePicker")}</a>
						<a href="${fileServer}${setting.defaultLargeProductImage}" target="_blank">${message("admin.common.view")}</a>
					</span>
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>${message("admin.setting.defaultMediumProductImage")}:
				</th>
				<td>
					<span class="fieldSet">
						<input type="text" name="setting.defaultMediumProductImage" class="text" value="${setting.defaultMediumProductImage}" maxlength="200" />
						<a href="javascript:;" class="button filePicker">${message("admin.upload.filePicker")}</a>
						<a href="${fileServer}${setting.defaultMediumProductImage}" target="_blank">${message("admin.common.view")}</a>
					</span>
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>${message("admin.setting.defaultThumbnailProductImage")}:
				</th>
				<td>
					<span class="fieldSet">
						<input type="text" name="setting.defaultThumbnailProductImage" class="text" value="${setting.defaultThumbnailProductImage}" maxlength="200" />
						<a href="javascript:;" class="button filePicker">${message("admin.upload.filePicker")}</a>
						<a href="${fileServer}${setting.defaultThumbnailProductImage}" target="_blank">${message("admin.common.view")}</a>
					</span>
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>${message("Setting.watermarkAlpha")}:
				</th>
				<td>
					<input type="text" name="setting.watermarkAlpha" class="text" value="${setting.watermarkAlpha}" maxlength="9" title="${message("admin.setting.watermarkAlphaTitle")}" />
				</td>
			</tr>
			<tr>
				<th>
					${message("Setting.watermarkImage")}:
				</th>
				<td>
					<span class="fieldSet">
						<input type="file" name="watermarkImageFile" />
						<a href="${base}${setting.watermarkImage}" target="_blank">${message("admin.common.view")}</a>
					</span>
				</td>
			</tr>
			<tr>
				<th>
					${message("Setting.watermarkPosition")}:
				</th>
				<td>
					<select name="watermarkPosition">
						[#list watermarkPositions as watermarkPosition]
							<option value="${watermarkPosition}"[#if watermarkPosition == setting.watermarkPosition] selected="selected"[/#if]>${message("Setting.WatermarkPosition." + watermarkPosition)}</option>
						[/#list]
					</select>
				</td>
			</tr>
			<tr>
				<th>
					${message("Setting.priceScale")}:
				</th>
				<td>
					<select name="setting.priceScale">
						<option value="0"[#if setting.priceScale == 0] selected="selected"[/#if]>${message("admin.setting.priceScale0")}</option>
						<option value="1"[#if setting.priceScale == 1] selected="selected"[/#if]>${message("admin.setting.priceScale1")}</option>
						<option value="2"[#if setting.priceScale == 2] selected="selected"[/#if]>${message("admin.setting.priceScale2")}</option>
						<option value="3"[#if setting.priceScale == 3] selected="selected"[/#if]>${message("admin.setting.priceScale3")}</option>
					</select>
				</td>
			</tr>
			<tr>
				<th>
					${message("Setting.priceRoundType")}:
				</th>
				<td>
					<select name="priceRoundType">
						[#list roundTypes as roundType]
							<option value="${roundType}"[#if roundType == setting.priceRoundType] selected="selected"[/#if]>${message("Setting.RoundType." + roundType)}</option>
						[/#list]
					</select>
				</td>
			</tr>
			<tr>
				<th>
					${message("Setting.isShowMarketPrice")}:
				</th>
				<td>
					<input type="checkbox" name="isShowMarketPrice" value="true"[#if setting.isShowMarketPrice] checked="checked"[/#if] />
					<input type="hidden" name="_isShowMarketPrice" value="false" />
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>${message("Setting.defaultMarketPriceScale")}:
				</th>
				<td>
					<input type="text" name="setting.defaultMarketPriceScale" class="text" value="${setting.defaultMarketPriceScale}" maxlength="7" title="${message("admin.setting.defaultMarketPriceScaleTitle")}" />
				</td>
			</tr>
		</table>
		<table class="input tabContent">
			<tr>
				<th>
					${message("Setting.isRegisterEnabled")}:
				</th>
				<td>
					<input type="checkbox" name="isRegisterEnabled" value="true"[#if setting.isRegisterEnabled] checked="checked"[/#if] />
					<input type="hidden" name="_isRegisterEnabled" value="false" />
				</td>
			</tr>
			<tr>
				<th>
					${message("Setting.isDuplicateEmail")}:
				</th>
				<td>
					<input type="checkbox" name="isDuplicateEmail" value="true"[#if setting.isDuplicateEmail] checked="checked"[/#if] />
					<input type="hidden" name="_isDuplicateEmail" value="false" />
				</td>
			</tr>
			<tr>
				<th>
					${message("Setting.disabledUsername")}:
				</th>
				<td>
					<input type="text" name="setting.disabledUsername" class="text" value="${setting.disabledUsername}" maxlength="200" title="${message("admin.setting.disabledUsernameTitle")}" />
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>${message("Setting.usernameMinLength")}:
				</th>
				<td>
					<input type="text" id="usernameMinLength" name="setting.usernameMinLength" class="text" value="${setting.usernameMinLength}" maxlength="3" />
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>${message("Setting.usernameMaxLength")}:
				</th>
				<td>
					<input type="text" name="setting.usernameMaxLength" class="text" value="${setting.usernameMaxLength}" maxlength="3" />
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>${message("Setting.passwordMinLength")}:
				</th>
				<td>
					<input type="text" id="passwordMinLength" name="setting.passwordMinLength" class="text" value="${setting.passwordMinLength}" maxlength="3" />
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>${message("Setting.passwordMaxLength")}:
				</th>
				<td>
					<input type="text" name="setting.passwordMaxLength" class="text" value="${setting.passwordMaxLength}" maxlength="3" />
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>${message("Setting.registerPoint")}:
				</th>
				<td>
					<input type="text" name="setting.registerPoint" class="text" value="${setting.registerPoint}" />
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>${message("Setting.registerAgreement")}:
				</th>
				<td>
					<textarea name="setting.registerAgreement" class="text">${setting.registerAgreement} </textarea>
				</td>
			</tr>
			<tr>
				<th>
					${message("Setting.isEmailLogin")}:
				</th>
				<td>
					<input type="checkbox" name="isEmailLogin" value="true"[#if setting.isEmailLogin] checked="checked"[/#if] />
					<input type="hidden" name="_isEmailLogin" value="false" />
				</td>
			</tr>
			<tr>
				<th>
					${message("Setting.captchaTypes")}:
				</th>
				<td>
					[#list captchaTypes as captchaType]
						<label>
							<input type="checkbox" name="captchaTypes" value="${captchaType}"[#if setting.captchaTypes?? && setting.captchaTypes?seq_contains(captchaType)] checked="checked"[/#if] />${message("Setting.CaptchaType." + captchaType)}
						</label>
					[/#list]
				</td>
			</tr>
			<tr>
				<th>
					${message("Setting.accountLockTypes")}:
				</th>
				<td>
					[#list accountLockTypes as accountLockType]
						<label>
							<input type="checkbox" name="accountLockTypes" value="${accountLockType}"[#if setting.accountLockTypes?? && setting.accountLockTypes?seq_contains(accountLockType)] checked="checked"[/#if] />${message("Setting.AccountLockType." + accountLockType)}
						</label>
					[/#list]
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>${message("Setting.accountLockCount")}:
				</th>
				<td>
					<input type="text" name="setting.accountLockCount" class="text" value="${setting.accountLockCount}" maxlength="9" title="${message("admin.setting.accountLockCountTitle")}" />
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>${message("Setting.accountLockTime")}:
				</th>
				<td>
					<input type="text" name="setting.accountLockTime" class="text" value="${setting.accountLockTime}" maxlength="9" title="${message("admin.setting.accountLockTimeTitle")}" />
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>${message("Setting.safeKeyExpiryTime")}:
				</th>
				<td>
					<input type="text" name="setting.safeKeyExpiryTime" class="text" value="${setting.safeKeyExpiryTime}" maxlength="9" title="${message("admin.setting.safeKeyExpiryTimeTitle")}" />
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>${message("Setting.uploadMaxSize")}:
				</th>
				<td>
					<input type="text" name="setting.uploadMaxSize" class="text" value="${setting.uploadMaxSize}" maxlength="9" title="${message("admin.setting.uploadMaxSizeTitle")}" />
				</td>
			</tr>
			<tr>
				<th>
					${message("Setting.uploadImageExtension")}:
				</th>
				<td>
					<input type="text" name="setting.uploadImageExtension" class="text" value="${setting.uploadImageExtension}" maxlength="200" title="${message("admin.setting.uploadImageExtensionTitle")}" />
				</td>
			</tr>
			<tr>
				<th>
					${message("Setting.uploadMediaExtension")}:
				</th>
				<td>
					<input type="text" name="setting.uploadMediaExtension" class="text" value="${setting.uploadMediaExtension}" maxlength="200" title="${message("admin.setting.uploadMediaExtensionTitle")}" />
				</td>
			</tr>
			<tr>
				<th>
					${message("Setting.uploadFileExtension")}:
				</th>
				<td>
					<input type="text" name="setting.uploadFileExtension" class="text" value="${setting.uploadFileExtension}" maxlength="200" title="${message("admin.setting.uploadFileExtensionTitle")}" />
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>${message("Setting.imageUploadPath")}:
				</th>
				<td>
					<input type="text" name="setting.imageUploadPath" class="text" value="${setting.imageUploadPath}" maxlength="200" />
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>${message("Setting.mediaUploadPath")}:
				</th>
				<td>
					<input type="text" name="setting.mediaUploadPath" class="text" value="${setting.mediaUploadPath}" maxlength="200" />
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>${message("Setting.fileUploadPath")}:
				</th>
				<td>
					<input type="text" name="setting.fileUploadPath" class="text" value="${setting.fileUploadPath}" maxlength="200" />
				</td>
			</tr>
		</table>
		<table class="input tabContent">
			<tr>
				<th>
					<span class="requiredField">*</span>${message("Setting.smtpHost")}:
				</th>
				<td>
					<input type="text" id="smtpHost" name="setting.smtpHost" class="text" value="${setting.smtpHost}" maxlength="200" />
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>${message("Setting.smtpPort")}:
				</th>
				<td>
					<input type="text" id="smtpPort" name="setting.smtpPort" class="text" value="${setting.smtpPort}" maxlength="9" title="${message("admin.setting.smtpPorteTitle")}" />
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>${message("Setting.smtpUsername")}:
				</th>
				<td>
					<input type="text" id="smtpUsername" name="setting.smtpUsername" class="text" value="${setting.smtpUsername}" maxlength="200" />
				</td>
			</tr>
			<tr>
				<th>
					${message("Setting.smtpPassword")}:
				</th>
				<td>
					<input type="password" id="smtpPassword" name="setting.smtpPassword" class="text" maxlength="200" autocomplete="off" title="${message("admin.setting.smtpPasswordTitle")}" />
				</td>
			</tr>
			<tr>
				<th>
					${message("Setting.smtpSSLEnabled")}:
				</th>
				<td>
					<input type="checkbox" id="smtpSSLEnabled" name="smtpSSLEnabled" value="true"[#if setting.smtpSSLEnabled] checked="checked"[/#if] />
					<input type="hidden" name="_smtpSSLEnabled" value="false" />
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>${message("Setting.smtpFromMail")}:
				</th>
				<td>
					<input type="text" id="smtpFromMail" name="setting.smtpFromMail" class="text" value="${setting.smtpFromMail}" maxlength="200" />
				</td>
			</tr>
			<tr>
				<th>
					&nbsp;
				</th>
				<td>
					<a href="javascript:;" id="testSmtp">[${message("admin.setting.testSmtp")}]</a>
				</td>
			</tr>
			<tr class="hidden">
				<th>
					${message("admin.setting.toMail")}:
				</th>
				<td>
					<span class="fieldSet">
						<input type="text" id="toMail" name="toMail" class="text ignore" maxlength="200" />
						<input type="button" id="sendMail" class="button" value="${message("admin.setting.sendMail")}" />
						<span id="testSmtpStatus">&nbsp;</span>
					</span>
				</td>
			</tr>
		</table>
		<table class="input tabContent">
			<tr>
				<th>
					<span class="requiredField">*</span>${message("Setting.currencySign")}:
				</th>
				<td>
					<input type="text" name="setting.currencySign" class="text" value="${setting.currencySign}" maxlength="200" />
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>${message("Setting.currencyUnit")}:
				</th>
				<td>
					<input type="text" name="setting.currencyUnit" class="text" value="${setting.currencyUnit}" maxlength="200" />
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>${message("Setting.stockAlertCount")}:
				</th>
				<td>
					<input type="text" name="setting.stockAlertCount" class="text" value="${setting.stockAlertCount}" maxlength="9" />
				</td>
			</tr>
			<tr>
				<th>
					${message("Setting.stockAllocationTime")}:
				</th>
				<td>
					<select name="stockAllocationTime">
						[#list stockAllocationTimes as stockAllocationTime]
							<option value="${stockAllocationTime}"[#if stockAllocationTime == setting.stockAllocationTime] selected="selected"[/#if]>${message("Setting.StockAllocationTime." + stockAllocationTime)}</option>
						[/#list]
					</select>
				</td>
			</tr>
			[#--<tr>--]
				[#--<th>--]
					[#--<span class="requiredField">*</span>${message("Setting.defaultPointScale")}:--]
				[#--</th>--]
				[#--<td>--]
					[#--<input type="text" name="setting.defaultPointScale" class="text" value="${setting.defaultPointScale}" maxlength="7" title="${message("admin.setting.defaultPointScaleTitle")}" />--]
				[#--</td>--]
			[#--</tr>--]
			<tr>
				<th>
					${message("Setting.isDevelopmentEnabled")}:
				</th>
				<td>
					<label title="${message("admin.setting.isDevelopmentEnabledTitle")}">
						<input type="checkbox" name="isDevelopmentEnabled" value="true"[#if setting.isDevelopmentEnabled] checked="checked"[/#if] />
						<input type="hidden" name="_isDevelopmentEnabled" value="false" />
					</label>
				</td>
			</tr>
			<tr>
				<th>
					${message("Setting.isReviewEnabled")}:
				</th>
				<td>
					<input type="checkbox" name="isReviewEnabled" value="true"[#if setting.isReviewEnabled] checked="checked"[/#if] />
					<input type="hidden" name="_isReviewEnabled" value="false" />
				</td>
			</tr>
			<tr>
				<th>
					${message("Setting.isReviewCheck")}:
				</th>
				<td>
					<input type="checkbox" name="isReviewCheck" value="true"[#if setting.isReviewCheck] checked="checked"[/#if] />
					<input type="hidden" name="_isReviewCheck" value="false" />
				</td>
			</tr>
			<tr>
				<th>
					${message("Setting.reviewAuthority")}:
				</th>
				<td>
					<select name="reviewAuthority">
						[#list reviewAuthorities as reviewAuthority]
							<option value="${reviewAuthority}"[#if reviewAuthority == setting.reviewAuthority] selected="selected"[/#if]>${message("Setting.ReviewAuthority." + reviewAuthority)}</option>
						[/#list]
					</select>
				</td>
			</tr>
			<tr>
				<th>
					${message("Setting.isConsultationEnabled")}:
				</th>
				<td>
					<input type="checkbox" name="isConsultationEnabled" value="true"[#if setting.isConsultationEnabled] checked="checked"[/#if] />
					<input type="hidden" name="_isConsultationEnabled" value="false" />
				</td>
			</tr>
			<tr>
				<th>
					${message("Setting.isConsultationCheck")}:
				</th>
				<td>
					<input type="checkbox" name="isConsultationCheck" value="true"[#if setting.isConsultationCheck] checked="checked"[/#if] />
					<input type="hidden" name="_isConsultationCheck" value="false" />
				</td>
			</tr>
			<tr>
				<th>
					${message("Setting.consultationAuthority")}:
				</th>
				<td>
					<select name="consultationAuthority">
						[#list consultationAuthorities as consultationAuthority]
							<option value="${consultationAuthority}"[#if consultationAuthority == setting.consultationAuthority] selected="selected"[/#if]>${message("Setting.ConsultationAuthority." + consultationAuthority)}</option>
						[/#list]
					</select>
				</td>
			</tr>
			<tr>
				<th>
					${message("Setting.isInvoiceEnabled")}:
				</th>
				<td>
					<input type="checkbox" name="isInvoiceEnabled" value="true"[#if setting.isInvoiceEnabled] checked="checked"[/#if] />
					<input type="hidden" name="_isInvoiceEnabled" value="false" />
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>${message("Setting.cookiePath")}:
				</th>
				<td>
					<input type="text" name="setting.cookiePath" class="text" value="${setting.cookiePath}" maxlength="200" />
				</td>
			</tr>
			<tr>
				<th>
					${message("Setting.cookieDomain")}:
				</th>
				<td>
					<input type="text" name="setting.cookieDomain" class="text" value="${setting.cookieDomain}" maxlength="200" />
				</td>
			</tr>
			<tr>
				<th>
					${message("Setting.kuaidi100Key")}:
				</th>
				<td>
					<input type="text" name="setting.kuaidi100Key" class="text" value="${setting.kuaidi100Key}" maxlength="200" />
				</td>
			</tr>
			<tr>
				<th>
					${message("Setting.smsSn")}:
				</th>
				<td>
					<input type="text" id="smsSn" name="setting.smsSn" class="text" value="${setting.smsSn}" maxlength="200" />
					[#if setting.smsSn?has_content && setting.smsKey?has_content]
						<a href="javascript:;" id="smsBalance">[${message("admin.setting.smsBalance")}]</a>
					[/#if]
				</td>
			</tr>
			<tr>
				<th>
					${message("Setting.smsKey")}:
				</th>
				<td>
					<input type="text" name="setting.smsKey" class="text" value="${setting.smsKey}" maxlength="200" />
				</td>
			</tr>
		</table>
        <table class="input tabContent">
			<tr>
                <th>
				${message("setting.set.freemoney")}:
                </th>
                <td>
                </td>
			</tr>
            <tr>
                <th>
				${message("setting.select.freemoney")}:
                </th>
                <td>
					[#if redisSetting.isFreeMoney??]
                        <input id = "isFreeMoney" type="checkbox" name="isFreeMoney" value="true" [#if redisSetting.isFreeMoney] checked="checked"[/#if] title="开启后系统将包邮" />
                        <input type="hidden" name="_isFreeMoney" value="false"  title="${message("setting.common.freemoney")}" />
					[#else ]
                        <input id = "isFreeMoney" type="checkbox" name="isFreeMoney" value="true" [#if setting.isFreeMoney] checked="checked"[/#if] title="开启后系统将包邮" />
                        <input type="hidden" name="_isFreeMoney" value="false"  title="${message("setting.common.freemoney")}" />
					[/#if]

                </td>
            </tr>
            <tr>
                <th>
                    <span class="requiredField">*</span>${message("setting.common.minmoney")}:
                </th>
                <td>
					[#if redisSetting.freeMoney??]
                    	<input id ="freeMoney" type="text" name="setting.freeMoney" class="text" value="${redisSetting.freeMoney}" maxlength="200" title="请输入免运费最小金额，计量单位为元"/>
					[#else ]
                        <input id ="freeMoney" type="text" name="setting.freeMoney" class="text" value="${setting.freeMoney}" maxlength="200" title="请输入免运费最小金额，计量单位为元"/>
					[/#if]
                </td>
            </tr>
            <tr>
                <th>
				${message("setting.common.order")}:
                </th>
                <td>
                </td>
            </tr>
            <tr>
                <th>
				${message("setting.commomPayTime")}:
                </th>
                <td>
					[#if redisSetting.commomPayTime??]
						<input type="text" name="setting.commomPayTime" class="text" value="${redisSetting.commomPayTime}" maxlength="200" title="${message("setting.commomPayTime.desc")}"/>
					[#else ]
						<input type="text" name="setting.commomPayTime" class="text" value="${setting.commomPayTime}" maxlength="200" title="${message("setting.commomPayTime.desc")}"/>
					[/#if]
                </td>
            </tr>

            <tr>
                <th>
                    ${message("Setting.tax")}:
                </th>
                <td>
                </td>
            </tr>

            <tr>
                <th>
				${message("Setting.isTaxPriceEnabled")}:
                </th>
                <td>
                [#if redisSetting.isTaxPriceEnabled??]
                    <input type="checkbox" name="isTaxPriceEnabled" value="true" title="${message("admin.setting.taxRateTitle")}"[#if redisSetting.isTaxPriceEnabled] checked="checked"[/#if] />
                    <input type="hidden" name="_isTaxPriceEnabled" value="false" />
				[#else ]
                    <input type="checkbox" name="isTaxPriceEnabled" value="true" title="${message("admin.setting.taxRateTitle")}"[#if setting.isTaxPriceEnabled] checked="checked"[/#if] />
                    <input type="hidden" name="_isTaxPriceEnabled" value="false" />
				[/#if]
                </td>
            </tr>
            <tr>
                <th>
                    <span class="requiredField">*</span>${message("Setting.taxRate")}:
                </th>
                <td>
					[#if redisSetting.taxRate??]
						<input type="text" name="setting.taxRate" class="text" value="${redisSetting.taxRate}"  title="${message("Setting.taxRate.title")}"  maxlength="7" />
					[#else ]
                        <input type="text" name="setting.taxRate" class="text" value="${setting.taxRate}"  title="${message("Setting.taxRate.title")}"  maxlength="7" />
					[/#if]
                </td>
            </tr>
            <tr>
                <th>
				${message("Setting.taxExplain")}:
                </th>
                <td>
					[#if redisSetting.taxExplain??]
						<textarea name="setting.taxExplain" title="${message("setting.copy.desc")}" class="text">${redisSetting.taxExplain}</textarea>
					[#else ]
                        <textarea name="setting.taxExplain" title="${message("setting.copy.desc")}" class="text">${setting.taxExplain}</textarea>
					[/#if]
                </td>
            </tr>
            <tr>
                <th>
				${message("Setting.taxExplainUrl")}:
                </th>
                <td>
					[#if redisSetting.taxExplainUrl??]
                    	<input type="text" name="setting.taxExplainUrl" title="${message("setting.copy.url")}" class="text" value="${redisSetting.taxExplainUrl}" maxlength="200" />
					[#else ]
                            <input type="text" name="setting.taxExplainUrl" title="${message("setting.copy.url")}" class="text" value="${setting.taxExplainUrl}" maxlength="200" />
					[/#if]
                </td>
            </tr>
            <tr>
                <th>
                    ${message("setting.really.set")}:
                </th>
                <td>
                </td>
            </tr>
            <tr>
                <th>
                    ${message("setting.really.word")}:
                </th>
                <td>
					[#if redisSetting.certifiedCopy??]
						<textarea name="setting.certifiedCopy" class="text" title="${message("setting.really.worddesc")}">${redisSetting.certifiedCopy} </textarea>
					[#else ]
                        <textarea name="setting.certifiedCopy" class="text" title="${message("setting.really.worddesc")}">${setting.certifiedCopy} </textarea>
					[/#if]
                </td>
            </tr>
            <tr>
                <th>
                    ${message("setting.really.url")}:
                </th>
                <td>
					[#if redisSetting.certifiedCopyUrl??]
						<input type="text" name="setting.certifiedCopyUrl" class="text" title="${message("setting.really.urlset")}" value="${redisSetting.certifiedCopyUrl}" maxlength="200" />
					[#else ]
                        <input type="text" name="setting.certifiedCopyUrl" class="text" title="${message("setting.really.urlset")}" value="${settinng.certifiedCopyUrl}" maxlength="200" />
					[/#if]
                </td>
            </tr>
            <tr>
                <th>
                    ${message("setting.regist.set")}:
                </th>
                <td>
                </td>
            </tr>
            <tr>
                <th>
                    ${message("setting.sendmiao.regist")}:
                </th>
                <td>
					[#if redisSetting.isRegisterSending??]
						<input id="isRegisterSending" type="checkbox" name="isRegisterSending" title="${message("setting.system.regist")}" value="true"[#if redisSetting.isRegisterSending] checked="checked"[/#if] />
						<input type="hidden" name="_isRegisterSending" value="false" title="${message("setting.system.regist")}" />
					[#else ]
                        <input id="isRegisterSending" type="checkbox" name="isRegisterSending" title="${message("setting.system.regist")}" value="true"[#if setting.isRegisterSending] checked="checked"[/#if] />
                        <input type="hidden" name="_isRegisterSending" value="false" title="${message("setting.system.regist")}" />
					[/#if]
                </td>
            </tr>



            <tr>
                <th>
                    ${message("setting.sendmiao.number")}:
                </th>
                <td>
					[#if redisSetting.registerSending??]
						<input id="registerSending" type="text" name="setting.registerSending" class="text" value="${redisSetting.registerSending}" title=" ${message("setting.sendmiao.set")}" maxlength="200" />
					[#else ]
						<input id="registerSending" type="text" name="setting.registerSending" class="text" value="${setting.registerSending}" title=" ${message("setting.sendmiao.set")}"  maxlength="200" />
					[/#if]
                </td>
            </tr>

            <tr>
                <th>
					邀请码赠送喵币：
                </th>
                <td>
					[#if redisSetting.registerSending??]
                        <input id="vipSending" type="text" name="setting.vipSending" class="text" value="${redisSetting.vipSending}" title=" ${message("setting.sendmiao.set")}" maxlength="200" />
					[#else ]
						<input id="vipSending" type="text" name="setting.vipSending" class="text" value="${setting.vipSending}" title=" ${message("setting.sendmiao.set")}"  maxlength="200" />
					[/#if]
                </td>
            </tr>
            <tr>
                <th>
				购买福袋赠送喵币：
                </th>
                <td>
					[#if redisSetting.registerSending??]
                        <input id="housekeeperSending" type="text" name="setting.housekeeperSending" class="text" value="${redisSetting.housekeeperSending}" title=" ${message("setting.sendmiao.set")}" maxlength="200" />
					[#else ]
						<input id="housekeeperSending" type="text" name="setting.housekeeperSending" class="text" value="${setting.housekeeperSending}" title=" ${message("setting.sendmiao.set")}"  maxlength="200" />
					[/#if]
                </td>
            </tr>
            <tr>
                <th>
				分享赠送喵币：
                </th>
                <td>
					[#if redisSetting.registerSending??]
                        <input id="shareSending" type="text" name="setting.shareSending" class="text" value="${redisSetting.shareSending}" title=" ${message("setting.sendmiao.set")}" maxlength="200" />
					[#else ]
						<input id="shareSending" type="text" name="setting.shareSending" class="text" value="${setting.shareSending}" title=" ${message("setting.sendmiao.set")}"  maxlength="200" />
					[/#if]
                </td>
            </tr>
            <tr>
			<tr>
                <th>
                    团购开始前多久提醒：
                </th>
                <td>
					[#if redisSetting.registerSending??]
                        <input id="shareSending" type="text" name="setting.hour" class="text" value="${redisSetting.hour}" title=" 团购开始前多久提醒（h） " maxlength="200" />
					[#else ]
						<input id="shareSending" type="text" name="setting.hour" class="text" value="${setting.hour}" title=" 团购开始前多久提醒（h）"  maxlength="200" />
					[/#if]
                </td>
            </tr>
            <tr>
                <th>
                    ${message("setting.buyGoods.set")}:
                </th>
                <td>
                </td>
            </tr>
            <tr>
                <th>
                    ${message("setting.buyGoods.word")}:
                </th>
                <td>
					[#if redisSetting.shoppingCopy??]
						<textarea name="setting.shoppingCopy" class="text" title="${message("setting.buyGoods.worddesc")} ">${redisSetting.shoppingCopy}</textarea>
					[#else ]
                        <textarea name="setting.shoppingCopy" class="text" title="${message("setting.buyGoods.worddesc")}">${setting.shoppingCopy}</textarea>
					[/#if]
                </td>
            </tr>
            <tr>
                <th>
                    ${message("setting.buyGoods.info")}:
                </th>
                <td>
					[#if redisSetting.shoppingCopyUrl??]
						<input type="text" name="setting.shoppingCopyUrl" class="text" value="${redisSetting.shoppingCopyUrl}" maxlength="200" title="${message("setting.buyGoods.desc")}" />
					[#else ]
                        <input type="text" name="setting.shoppingCopyUrl" class="text" value="${setting.shoppingCopyUrl}" maxlength="200" title="${message("setting.buyGoods.desc")}" />
					[/#if]
                </td>
            </tr>
            <tr>
                <th>
                    ${message("setting.returns.copyset")}:
                </th>
                <td>
                </td>
            </tr>
            <tr>
                <th>
                    ${message("setting.returns.start")}:
                </th>
                <td>
					[#if redisSetting.isReturnInsurance??]
						<input id="isReturnInsurance" type="checkbox" name="isReturnInsurance" title="${message("setting.returns.copy")}" value="true"[#if redisSetting.isReturnInsurance] checked="checked"[/#if] />
						<input type="hidden" name="_isReturnInsurance" value="false" title="${message("setting.returns.copy")}" />
					[#else ]
                        <input id="isReturnInsurance" type="checkbox" name="isReturnInsurance" title="${message("setting.returns.copy")}" value="true"[#if setting.isReturnInsurance] checked="checked"[/#if] />
                        <input type="hidden" name="_isReturnInsurance" value="false" title="${message("setting.returns.copy")}" />
					[/#if]
                </td>
            </tr>
            <tr>
                <th>
                    ${message("setting.returns.price")}:
                </th>
                <td>
					[#if redisSetting.returnMoney??]
                    	<input id="returnMoney" type="text" name="setting.returnMoney" class="text" value="${redisSetting.returnMoney}" title="${message("setting.returns.priceDesc")}" maxlength="200" />
					[#else ]
                        <input id="returnMoney" type="text" name="setting.returnMoney" class="text" value="${setting.returnMoney}" title="${message("setting.returns.priceDesc")}" maxlength="200" />
					[/#if]
                </td>
            </tr>
            <tr>
                <th>
                    ${message("setting.returns.word")}:
                </th>
                <td>
					[#if redisSetting.returnCopy??]
                    	<textarea name="setting.returnCopy" title="${message("setting.returns.desc")}" class="text">${redisSetting.returnCopy}</textarea>
					[#else ]
                        <textarea name="setting.returnCopy" title="${message("setting.returns.desc")}" class="text">${setting.returnCopy}</textarea>
					[/#if]
                </td>
            </tr>
            <tr>
                <th>
                    ${message("setting.returns.url")}:
                </th>
                <td>
					[#if redisSetting.returnCopyUrl??]
                   		 <input type="text" name="setting.returnCopyUrl" title="${message("setting.returns.urlset")}" class="text" value="${redisSetting.returnCopyUrl}" maxlength="200" />
					[#else ]
                        <input type="text" name="setting.returnCopyUrl" title="${message("setting.returns.urlset")}" class="text" value="${setting.returnCopyUrl}" maxlength="200" />
					[/#if]
                </td>
            </tr>
			<tr>
                <th>
                    ${message("setting.miaobi.set")}:
                </th>
                <td>
                </td>
			</tr>
            <tr>
                <th>
                    ${message("setting.miaobi.price")}:
                </th>
                <td>
					[#if redisSetting.isUseMiaoBi??]
						<input id="isUseMiaoBi" type="checkbox" name="isUseMiaoBi" title="${message("setting.miaobi.setting")}" value="true"[#if redisSetting.isUseMiaoBi] checked="checked"[/#if] />
						<input type="hidden" name="_isUseMiaoBi" value="false" title="${message("setting.miaobi.setting")}"/>
					[#else ]
                        <input id="isUseMiaoBi" type="checkbox" name="isUseMiaoBi" title="${message("setting.miaobi.setting")}" value="true"[#if setting.isUseMiaoBi] checked="checked"[/#if] />
                        <input type="hidden" name="_isUseMiaoBi" value="false" title="${message("setting.miaobi.setting")}"/>
					[/#if]
                </td>
            </tr>
            <tr>
                <th>
                    ${message("setting.sendmiaobi.number")}:
                </th>
                <td>
					[#if redisSetting.miaoBiLimit??]
						<input id="miaoBiLimit" type="text" name="setting.miaoBiLimit" class="text" value="${redisSetting.miaoBiLimit}" maxlength="200" title="${message("setting.sendmiaobi.info")}"/>
					[#else ]
                        <input id="miaoBiLimit" type="text" name="setting.miaoBiLimit" class="text" value="${setting.miaoBiLimit}" maxlength="200" title="${message("setting.sendmiaobi.info")}"/>
					[/#if]
                </td>
            </tr>
            <tr>
                <th>
                    ${message("setting.sendmiaobi.dikou")}:
                </th>
                <td>
					[#if redisSetting.miaoBiLimit??]
						<input type="text" name="setting.scale" class="text" value="${redisSetting.scale}" maxlength="200" title="${message("setting.sendmiaobi.desc")}"/>
					[#else ]
                        <input type="text" name="setting.scale" class="text" value="${setting.scale}" maxlength="200" title="${message("setting.sendmiaobi.desc")}"/>
					[/#if]
                </td>
            </tr>
            <tr>
                <th>
				${message("setting.sendmiaobi.set")}:
                </th>
				<td>
				</td>
            </tr>
            <tr>
                <th>
                    ${message("setting.miaobi.select")}:
                </th>
                <td>
					[#if redisSetting.isSendMiaoBi??]
						<input id="isSendMiaoBi" type="checkbox" name="isSendMiaoBi" title="${message("setting.miaobi.system")}" value="true" [#if redisSetting.isSendMiaoBi] checked="checked"[/#if] />
						<input type="hidden" name="_sendMiaoBi" value="false" title="${message("setting.miaobi.system")}" />
					[#else ]
                        <input id="isSendMiaoBi" type="checkbox" name="isSendMiaoBi" title="${message("setting.miaobi.system")}" value="true" [#if setting.isSendMiaoBi] checked="checked"[/#if] />
                        <input type="hidden" name="_sendMiaoBi" value="false" title="${message("setting.miaobi.system")}" />
					[/#if]
                </td>
            </tr>
            <tr>
                <th>
				${message("setting.miaobi.limit")}:
                </th>
                <td>
					[#if redisSetting.sendMiaoBiLimit??]
						<input id="sendMiaoBiLimit" type="text" name="setting.sendMiaoBiLimit" class="text" value="${redisSetting.sendMiaoBiLimit}" maxlength="200" title="${message("setting.sendmiaobi.limit")}"/>
					[#else ]
                        <input id="sendMiaoBiLimit" type="text" name="setting.sendMiaoBiLimit" class="text" value="${setting.sendMiaoBiLimit}" maxlength="200" title="${message("setting.sendmiaobi.limit")}"/>
					[/#if]
                </td>
            </tr>
            <tr>
                <th>
                    积分抵扣额度:
                </th>
                <td>
					[#if redisSetting.integralLimit??]
                        <input id="integralLimit" type="text" name="setting.integralLimit" class="text" value="${redisSetting.integralLimit}" maxlength="200" title="${message("setting.sendmiaobi.info")}"/>
					[#else ]
                        <input id="integralLimit" type="text" name="setting.integralLimit" class="text" value="${setting.integralLimit}" maxlength="200" title="${message("setting.sendmiaobi.info")}"/>
					[/#if]
                </td>
            </tr>
            <tr>
                <th>
                    积分抵扣比例:
                </th>
                <td>
					[#if redisSetting.integralScale??]
                        <input type="text" name="setting.integralScale" class="text" value="${redisSetting.integralScale}" maxlength="200" title="${message("setting.sendmiaobi.desc")}"/>
					[#else ]
                        <input type="text" name="setting.integralScale" class="text" value="${setting.integralScale}" maxlength="200" title="${message("setting.sendmiaobi.desc")}"/>
					[/#if]
                </td>
            </tr>



        </table>
		<table class="input">
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