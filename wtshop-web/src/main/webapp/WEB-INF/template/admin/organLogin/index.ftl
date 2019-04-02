<!DOCTYPE html>
<html lang="en" class="login_page">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <title>${message("admin.login.title")} - Powered By ${setting.siteAuthor}.com</title>

        <!-- Bootstrap framework -->
            <link rel="stylesheet" href="${base}/gebo3/bootstrap/css/bootstrap.min.css" />
        <!-- theme color-->
            <link rel="stylesheet" href="${base}/gebo3/css/blue.css" />
        <!-- tooltip -->
			<link rel="stylesheet" href="${base}/gebo3/lib/qtip2/jquery.qtip.min.css" />
        <!-- main styles -->
            <link rel="stylesheet" href="${base}/gebo3/css/style.css" />

        <!-- favicon -->
            <link rel="shortcut icon" href="${base}/gebo3/favicon.ico" />

        <!-- <link href='../fonts.googleapis.com/css@family=PT+Sans' rel='stylesheet' type='text/css'> -->

        <!--[if lt IE 9]>
            <script src="js/ie/html5.js"></script>
			<script src="js/ie/respond.min.js"></script>
        <![endif]-->
		<script type="text/javascript">
			//防止页面嵌套在Iframe中
			if (window != top){
				top.location.href = location.href;
			}
		</script>
    </head>
    <body>
		<div class="login_box" style="width: 450px;">
			<form action="organLogin.jhtml" method="post" id="loginForm">
				<div class="top_b">登&nbsp;&nbsp;录</div>
				[#if failureMessage??]
					<div class="alert alert-info alert-login" id="alert1">
						${failureMessage.content}
					</div>
				[/#if]
				<div class="cnt_b" style="padding-bottom: 10px">
					<div class="form-group">
						<div class="input-group">
							<span class="input-group-addon"><i class="glyphicon glyphicon-user"></i></span>
							<input class="form-control" type="text" id="username" name="username" placeholder="${message("admin.login.username")}" />
						</div>
					</div>
					<div class="form-group">
						<div class="input-group">
							<span class="input-group-addon"><i class="glyphicon glyphicon-lock"></i></span>
							<input type="hidden" id="enPassword" name="enPassword" />
							<input class="form-control" type="password" id="password" autocomplete="off" placeholder="${message("admin.login.password")}"/>
						</div>
					</div>
					<div class="form-group">
						<div class="input-group">
							<span class="input-group-addon"><i class="glyphicon glyphicon-refresh"></i></span>
							<input class="form-control" type="text" id="captcha" name="captcha" maxlength="4" autocomplete="off" placeholder="${message("admin.captcha.name")}" style="width: 50%"/>&nbsp;&nbsp;<img id="captchaImage" class="captchaImage" src="${base}/admin/common/captcha.jhtml?width=100&height=34&fontsize=30" title="${message("admin.captcha.imageTitle")}" />
						</div>
					</div>
					<div class="form-group">
						<div class="checkbox"><label><input type="checkbox" name="remember" id="remember"/> ${message("admin.login.rememberUsername")}</label></div>
					</div>
				</div>
				<div class="btm_b clearfix">
					<button class="btn btn-primary pull-right" type="submit">${message("admin.login.login")}</button>
				</div>
			</form>

			<div class="links_b links_btm clearfix">
				<span class="linkform">&copy; 2019 All Rights Reserved. wangtiansoft.com</span>
			</div>

		</div>

        <script src="${base}/gebo3/js/jquery.min.js"></script>
        <script src="${base}/gebo3/js/jquery.actual.min.js"></script>
        <script src="${base}/gebo3/lib/validation/jquery.validate.js"></script>
		<script src="${base}/gebo3/bootstrap/js/bootstrap.min.js"></script>
		<script type="text/javascript" src="${base}/resources/admin/js/jsbn.js"></script>
	    <script type="text/javascript" src="${base}/resources/admin/js/prng4.js"></script>
	    <script type="text/javascript" src="${base}/resources/admin/js/rng.js"></script>
	    <script type="text/javascript" src="${base}/resources/admin/js/rsa.js"></script>
	    <script type="text/javascript" src="${base}/resources/admin/js/base64.js"></script>
	    <script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
        <script>
            $(document).ready(function(){

				//* boxes animation
				form_wrapper = $('.login_box');
				function boxHeight() {
					form_wrapper.animate({ marginTop : ( - ( form_wrapper.height() / 2) - 24) },400);
				};
				form_wrapper.css({ marginTop : ( - ( form_wrapper.height() / 2) - 24) });
                $('.linkform a,.link_reg a').on('click',function(e){
					var target	= $(this).attr('href'),
						target_height = $(target).actual('height');
					$(form_wrapper).css({
						'height'		: form_wrapper.height()
					});
					$(form_wrapper.find('form:visible')).fadeOut(400,function(){
						form_wrapper.stop().animate({
                            height	 : target_height,
							marginTop: ( - (target_height/2) - 24)
                        },500,function(){
                            $(target).fadeIn(400);
                            $('.links_btm .linkform').toggle();
							$(form_wrapper).css({
								'height'		: ''
							});
                        });
					});
					e.preventDefault();
				});

				//* validation
				$('#login_form').validate({
					onkeyup: false,
					errorClass: 'error',
					validClass: 'valid',
					rules: {
						username: { required: true, minlength: 3 },
						password: { required: true, minlength: 3 }
					},
					highlight: function(element) {
						$(element).closest('.form-group').addClass("f_error");
						setTimeout(function() {
							boxHeight()
						}, 200)
					},
					unhighlight: function(element) {
						$(element).closest('.form-group').removeClass("f_error");
						setTimeout(function() {
							boxHeight()
						}, 200)
					},
					errorPlacement: function(error, element) {
						$(element).closest('.form-group').append(error);
					}
				});
            });

            var $captchaImage = $("#captchaImage");

			$().ready( function() {
				// 更换验证码
	        	$captchaImage.click( function() {
	        		var d = new Date();
	        		$captchaImage.attr("src", "${base}/admin/common/captcha.jhtml?width=100&height=34&fontsize=30&time=" + d.toString(40));
	        	});
			});

            var $loginForm = $("#loginForm");
        	var $enPassword = $("#enPassword");
        	var $username = $("#username");
        	var $password = $("#password");

        	// 表单验证、记住用户名
        	$loginForm.submit( function() {
        		var rsaKey = new RSAKey();
        		rsaKey.setPublic(b64tohex("${modulus}"), b64tohex("${exponent}"));
        		var enPassword = hex2b64(rsaKey.encrypt($password.val()));
        		$enPassword.val(enPassword);
        	});

        </script>
    </body>
</html>
