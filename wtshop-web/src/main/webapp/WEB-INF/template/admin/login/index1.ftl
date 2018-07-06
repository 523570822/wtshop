<!DOCTYPE html>
<html>

<head>
    <!-- Meta, title, CSS, favicons, etc. -->
    <meta charset="utf-8">
    <title>${message("admin.login.title")} - Powered By omengo.com</title>
    <meta name="keywords" content=""/>
    <meta name="description" content="">
    <meta name="author" content="">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <!-- Theme CSS -->
    <link rel="stylesheet" type="text/css" href="${base}/assets/skin/default_skin/css/theme.css">
    <!-- Admin Forms CSS -->
    <link rel="stylesheet" type="text/css" href="${base}/assets/admin-tools/admin-forms/css/admin-forms.css">
    <!-- Favicon -->
    <link rel="shortcut icon" href="${base}/assets/img/favicon.ico">

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
    <script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
    <![endif]-->
    <script>
        if (window.top !== window.self) {
            window.top.location = window.location;
        }
    </script>
</head>

<body class="external-page sb-l-c sb-r-c">

<!-- Start: Main -->
<div id="main" class="animated fadeIn">

    <!-- Start: Content-Wrapper -->
    <section id="content_wrapper">

        <!-- begin canvas animation bg -->
        <div id="canvas-wrapper">
            <canvas id="demo-canvas"></canvas>
        </div>

        <!-- Begin: Content -->
        <section id="content">
            <div class="admin-form theme-info mw400" id="login">
                <div class="panel mt30 mb25">
                    <form method="post" action="login.jhtml" id="loginForm">
                        <input type="hidden" id="enPassword" name="enPassword"/>

                        <div class="panel-body bg-light p25 pb15">
                            <div class="section-divider mv30"><span>登&nbsp;&nbsp;录</span></div>
                            <!-- Username Input -->
                            <div class="section">
                                <label for="username" class="field prepend-icon">
                                    <input type="text" name="username" id="username" class="gui-input"
                                           placeholder="${message("admin.login.username")}">
                                    <label for="username" class="field-icon"><i class="fa fa-user"></i></label>
                                </label>
                            </div>
                            <!-- Password Input -->
                            <div class="section">
                                <label for="password" class="field prepend-icon">
                                    <input type="password" id="password" name="password" class="gui-input" autocomplete="off"
                                           placeholder="${message("admin.login.password")}">
                                    <label for="password" class="field-icon"><i class="fa fa-lock"></i></label>
                                </label>
                            </div>

                            <div class="section">
                                <label for="captcha" class="field prepend-icon">
                                    <input type="text" id="captcha" name="captcha" class="gui-input" autocomplete="off"
                                           placeholder="${message("admin.captcha.name")}" style="width: 60%;" >
                                    &nbsp;&nbsp;<img id="captchaImage" class="captchaImage"  onclick='window.open("${base}/admin/common/captcha.jhtml?width=100&height=34&fontsize=30&time=" + d.toString(40)))' src="${base}/admin/common/captcha.jhtml?width=120&height=42&fontsize=30" title="${message("admin.captcha.imageTitle")}" />
                                    <label for="password" class="field-icon"><i class="fa fa-refresh"></i></label>
                                </label>
                            </div>

                        </div>

                        <div class="panel-footer clearfix">
                            <button type="submit"
                                    class="button btn-primary mr10 pull-right">${message("admin.login.login")}</button>
                            <label class="switch ib switch-primary mt10">
                                <input type="checkbox" name="remember" id="remember" checked>
                                <label for="remember" data-on="是" data-off="否"></label>
                                <span>${message("admin.login.rememberUsername")}</span>
                            </label>
                        </div>

                        <div class="alert alert-danger light alert-dismissable" id="alert1">
                            <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                            <i class="fa fa-warning pr10"></i>
                        [#if failureMessage??]
                            <strong>${failureMessage.content}</strong>
                        [/#if]
                        </div>

                    </form>
                </div>
                <div class="login-links">
                    <p> &copy; 2017 All Rights Reserved.</p>
                </div>
            </div>
        </section>
        <!-- End: Content -->
    </section>
    <!-- End: Content-Wrapper -->
</div>
<!-- End: Main -->

<!-- BEGIN: PAGE SCRIPTS -->
<style>
    #alert1 {
        display: none;
    }
</style>
<!-- jQuery -->
<script src="${base}/gebo3/js/jquery.min.js"></script>
<script src="${base}/gebo3/lib/jquery-ui/jquery-ui-1.10.0.custom.min.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jsbn.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/prng4.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/rng.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/rsa.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/base64.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>

<!-- jQuery Validate Plugin-->
<script src="${base}/assets/admin-tools/admin-forms/js/jquery.validate.min.js"></script>

<!-- Theme Javascript -->
<script src="${base}/assets/js/utility/utility.js"></script>

<!--  <script src="assets/js/demo/demo.js"></script> -->
<script src="${base}/assets/js/main.js"></script>

<!-- Page Javascript -->
<script type="text/javascript">
    jQuery(document).ready(function () {

        $(".captchaImage").click( function() {
            var d = new Date();
            $(".captchaImage").attr("src", "${base}/admin/common/captcha.jhtml?width=100&height=34&fontsize=30&time=" + d.toString(40));
        });


        "use strict";
        // Init Theme Core
        Core.init();

        $("#loginForm").validate({
            /* @validation states + elements
            ------------------------------------------- */
            errorClass: "state-error",
            validClass: "state-success",
            errorElement: "em",

            /* @validation rules
            ------------------------------------------ */
            rules: {
                username: {
                    required: true
                },
                password: {
                    required: true,
                    minlength: 4,
                    maxlength: 16
                }
            },

            /* @validation error messages
            ---------------------------------------------- */
            messages: {
                username: {
                    required: '${message("admin.login.usernameRequired")}'
                },
                password: {
                    required: '${message("admin.login.passwordRequired")}'
                }
            },

            /* @validation highlighting + error placement
            ---------------------------------------------------- */
            highlight: function (element, errorClass, validClass) {
                $(element).closest('.field').addClass(errorClass).removeClass(validClass);
            },
            unhighlight: function (element, errorClass, validClass) {
                $(element).closest('.field').removeClass(errorClass).addClass(validClass);
            },
            errorPlacement: function (error, element) {
                if (element.is(":radio") || element.is(":checkbox")) {
                    element.closest('.option-group').after(error);
                } else {
                    error.insertAfter(element.parent());
                }
            }
        });

        /* @login
        ---------------------------------------------------- */
        var $loginForm = $("#loginForm");
        var $enPassword = $("#enPassword");
        var $username = $("#username");
        var $password = $("#password");

    [#if failureMessage??]
        //$.message("${failureMessage.type}", "${failureMessage.content}");
        $('#alert1').fadeToggle();
    [/#if]

        // 表单验证、记住用户名
        $loginForm.submit(function () {
            var rsaKey = new RSAKey();
            rsaKey.setPublic(b64tohex("${modulus}"), b64tohex("${exponent}"));
            var enPassword = hex2b64(rsaKey.encrypt($password.val()));
            $enPassword.val(enPassword);
        });

    });
</script>

<!-- END: PAGE SCRIPTS -->

</body>

</html>
