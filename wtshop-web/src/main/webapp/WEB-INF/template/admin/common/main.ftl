<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <title>${message("admin.main.title")} - Powered By ${setting.siteAuthor}</title>

        <!-- Bootstrap framework -->
            <link rel="stylesheet" href="${base}/gebo3/bootstrap/css/bootstrap.min.css" />
        <!-- jQuery UI theme -->
            <link rel="stylesheet" href="${base}/gebo3/lib/jquery-ui/css/Aristo/Aristo.css" />
        <!-- breadcrumbs -->
            <link rel="stylesheet" href="${base}/gebo3/lib/jBreadcrumbs/css/BreadCrumb.css" />
        <!-- tooltips-->
            <link rel="stylesheet" href="${base}/gebo3/lib/qtip2/jquery.qtip.min.css" />
		<!-- colorbox -->
            <link rel="stylesheet" href="${base}/gebo3/lib/colorbox/colorbox.css" />
        <!-- code prettify -->
            <link rel="stylesheet" href="${base}/gebo3/lib/google-code-prettify/prettify.css" />
        <!-- sticky notifications -->
            <link rel="stylesheet" href="${base}/gebo3/lib/sticky/sticky.css" />
        <!-- aditional icons -->
            <link rel="stylesheet" href="${base}/gebo3/img/splashy/splashy.css" />
		<!-- flags -->
            <link rel="stylesheet" href="${base}/gebo3/img/flags/flags.css" />
        <!-- datatables -->
            <link rel="stylesheet" href="${base}/gebo3/lib/datatables/extras/TableTools/media/css/TableTools.css">


        <!-- main styles -->
            <link rel="stylesheet" href="${base}/gebo3/css/style.css" />
		<!-- theme color-->
            <link rel="stylesheet" href="${base}/gebo3/css/blue.css" id="link_theme" />

            <!-- <link href='../fonts.googleapis.com/css@family=PT+Sans' rel='stylesheet' type='text/css'> -->

        <!-- favicon -->
           <link rel="shortcut icon" href="${base}/gebo3/favicon.ico" />

        <!--[if lte IE 8]>
            <link rel="stylesheet" href="css/ie.css" />
        <![endif]-->

        <!--[if lt IE 9]>
			<script src="${base}/gebo3/js/ie/html5.js"></script>
			<script src="${base}/gebo3/js/ie/respond.min.js"></script>
			<script src="${base}/gebo3/lib/flot/excanvas.min.js"></script>
        <![endif]-->
         <script>
	     	// iframe高度自适应的方法
	        function setIframeHeight(iframe) {
	     		var iframe = document.getElementById("iframe");
	            if (iframe) {
	                var iframeWin = iframe.contentWindow || iframe.contentDocument.parentWindow;
	                if (iframeWin.document.body) {
	                	var header = document.getElementById("header");
						var h = header.offsetHeight;  //高度
	                    iframe.height = document.body.clientHeight-h-5;
	                }
	            }
	        };
        </script>
</head>

    <body class="full_width">
		<div id="maincontainer" class="clearfix">
            <header>
				<nav class="navbar navbar-default navbar-fixed-top" role="navigation">
					<div class="navbar-inner">
						<div class="container-fluid" id="header">
							<a class="brand pull-left" href="${base}/admin/common/main.jhtml">${message("admin.main.title")}</a>
							<ul class="nav navbar-nav user_menu pull-right">
								<li class="hidden-phone hidden-tablet">
									<div class="nb_boxes clearfix">
										<a href="${base}/admin/messageAll/tomain.jhtml" target="iframe" class="label bs_ttip"><i class="splashy-mail_light"></i></a>
									</div>
								</li>
								[#--<li class="divider-vertical hidden-sm hidden-xs"></li>--]
								[#--<li class="dropdown">--]
									[#--<a href="#" class="dropdown-toggle nav_condensed" data-toggle="dropdown"><i class="flag-cn"></i> <b class="caret"></b></a>--]
									[#--<ul class="dropdown-menu dropdown-menu-right">--]
										[#--<li><a href="javascript:void(0)"><i class="flag-cn"></i> 简体 </a></li>--]
										[#--<li><a href="javascript:void(0)"><i class="flag-tw"></i> 繁体 </a></li>--]
										[#--<li><a href="javascript:void(0)"><i class="flag-gb"></i> 英语 </a></li>--]
									[#--</ul>--]
								[#--</li>--]
								<li class="divider-vertical hidden-sm hidden-xs"></li>
								<li class="dropdown">
									<a href="#" class="dropdown-toggle" data-toggle="dropdown"><img src="${base}/gebo3/img/user_avatar.png" alt="" class="user_avatar">[@shiro.principal name="username" /] <b class="caret"></b></a>
									<ul class="dropdown-menu dropdown-menu-right">
										<li><a href="../profile/edit.jhtml" target="iframe">${message("admin.main.profile")}</a></li>
										<li class="divider"></li>
										<li><a href="${base}/signout">${message("admin.main.logout")}</a></li>
									</ul>
								</li>
							</ul>
						</div>
					</div>
				</nav>
			</header>
            <div id="contentwrapper">
                <div class="main_content" style="padding: 42px 0 0">
					<div class="row">
				        <div class="col-sm-12 col-md-12">
				            <!-- 显示内容 -->
							<iframe src="index.jhtml" frameborder="0" scrolling="auto" id="iframe" name="iframe"  onload="setIframeHeight(this)" height=100% width=100%></iframe>
				        </div>
	   				 </div>
	    		</div>
        	</div>
        </div>

    <a href="javascript:void(0)" class="sidebar_switch on_switch bs_ttip" data-placement="auto right" data-viewport="body" title="Hide Sidebar">
    	Sidebar switch
    </a>
    <div class="sidebar">

        <div class="sidebar_inner_scroll">
            <div class="sidebar_inner">
                <div id="side_accordion" class="panel-group">
                	[#list ["admin:goods", "admin:goodsReview","admin:myVerify" ] as permission]

						[@shiro.hasPermission name = permission]
						${@shiro.hasPermission}
							<div class="panel panel-default">
		                        <div class="panel-heading">
		                            <a href="#productGroup" data-parent="#side_accordion" data-toggle="collapse" class="accordion-toggle">
		                                <i class="glyphicon glyphicon-folder-close"></i> ${message("admin.main.productGroup")}
		                            </a>
		                        </div>
		                        <div class="accordion-body collapse in" id="productGroup">
		                            <div class="panel-body">
		                                <ul class="nav nav-pills nav-stacked">
		                                    [@shiro.hasPermission name="admin:goods"]
												<li><a href="../goods/list.jhtml" target="iframe">${message("admin.main.goods")}</a></li>
											[/@shiro.hasPermission]
											[@shiro.hasPermission name="admin:goodsReview"]
                                                <li><a href="../goodsReview/list.jhtml" target="iframe">${message("admin.goodsVerify")}</a></li>
											[/@shiro.hasPermission]
										</ul>
		                            </div>
		                        </div>
		                    </div>
							[#break /]
						[/@shiro.hasPermission]
					[/#list]

					[#list ["admin:cartMember" ,"admin:follow", "admin:cartGoods", "admin:referrerGoods", "admin:point", "admin:footprint", "admin:interestCategory"] as permission]
				[@shiro.hasPermission name = permission]
									<div class="panel panel-default">
										<div class="panel-heading">
											<a href="#marketing" data-parent="#side_accordion" data-toggle="collapse" class="accordion-toggle">
												<i class="glyphicon glyphicon-eye-open"></i> ${message("admin.marketing")}
											</a>
										</div>
										<div class="accordion-body collapse" id="marketing">
											<div class="panel-body">
                                                <ul class="nav nav-pills nav-stacked">

													[@shiro.hasPermission name="admin:follow"]
                                                        <li><a href="../userFollow/list.jhtml" target="iframe">${message("admin.role.follow")}</a></li>
													[/@shiro.hasPermission]

													[@shiro.hasPermission name="admin:cartMember"]
                                                        <li><a href="../cartMember/list.jhtml" target="iframe">${message("admin.role.cartMember")}</a></li>
													[/@shiro.hasPermission]

													[@shiro.hasPermission name="admin:cartGoods"]
                                                        <li><a href="../cartGoods/list.jhtml" target="iframe">${message("admin.role.cartGoods")}</a></li>
													[/@shiro.hasPermission]

													[@shiro.hasPermission name="admin:referrerGoods"]
                                                        <li><a href="../referrer_goods/list.jhtml" target="iframe">${message("admin.role.referrerGoods")}</a></li>
													[/@shiro.hasPermission]

													[@shiro.hasPermission name="admin:point"]
                                                        <li><a href="../point/log.jhtml" target="iframe">${message("admin.main.point")}</a></li>
													[/@shiro.hasPermission]

													[@shiro.hasPermission name="admin:footprint"]
                                                        <li><a href="../footprint/list.jhtml" target="iframe">${message("admin.footprint")}</a></li>
													[/@shiro.hasPermission]

													[@shiro.hasPermission name="admin:interestCategory"]
                                                        <li><a href="../interestCategory/list.jhtml" target="iframe">${message("admin.role.interestCategory")}</a></li>
													[/@shiro.hasPermission]
                                                </ul>
											</div>
										</div>
									</div>
					[#break /]
				[/@shiro.hasPermission]
			[/#list]

					[#list ["admin:skinType", "admin:stock", "admin:productCategory", "admin:parameter", "admin:attribute", "admin:specification", "admin:brand", "admin:tag", "admin:effect"     ] as permission]
						[@shiro.hasPermission name = permission]
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    <a href="#goodsAbout" data-parent="#side_accordion" data-toggle="collapse" class="accordion-toggle">
                                        <i class="glyphicon glyphicon-leaf"></i> ${message("admin.goodsAbout")}
                                    </a>
                                </div>
                                <div class="accordion-body collapse" id="goodsAbout">
                                    <div class="panel-body">
                                        <ul class="nav nav-pills nav-stacked">
											[@shiro.hasPermission name="admin:skinType"]
                                                <li><a href="../skinType/list.jhtml" target="iframe">${message("admin.role.skinType")}</a></li>
											[/@shiro.hasPermission]
											[@shiro.hasPermission name="admin:stock"]
                                                <li><a href="../stock/log.jhtml" target="iframe">${message("admin.main.stock")}</a></li>
											[/@shiro.hasPermission]

											[@shiro.hasPermission name="admin:productCategory"]
                                                <li><a href="../product_category/list.jhtml" target="iframe">${message("admin.main.productCategory")}</a></li>
											[/@shiro.hasPermission]

											[@shiro.hasPermission name="admin:parameter"]
                                                <li><a href="../parameter/list.jhtml" target="iframe">${message("admin.main.parameter")}</a></li>
											[/@shiro.hasPermission]

											[@shiro.hasPermission name="admin:attribute"]
                                                <li><a href="../attribute/list.jhtml" target="iframe">${message("admin.main.attribute")}</a></li>
											[/@shiro.hasPermission]

											[@shiro.hasPermission name="admin:specification"]
                                                <li><a href="../specification/list.jhtml" target="iframe">${message("admin.main.specification")}</a></li>
											[/@shiro.hasPermission]

											[@shiro.hasPermission name="admin:brand"]
                                                <li><a href="../brand/list.jhtml" target="iframe">${message("admin.main.brand")}</a></li>
											[/@shiro.hasPermission]

											[@shiro.hasPermission name="admin:tag"]
                                                <li><a href="../tag/list.jhtml" target="iframe">${message("admin.main.tag")}</a></li>
											[/@shiro.hasPermission]


											[@shiro.hasPermission name="admin:effect"]
                                                <li><a href="../effect/list.jhtml" target="iframe">${message("admin.role.effect")}</a></li>
											[/@shiro.hasPermission]
                                        </ul>
                                    </div>
                                </div>
                            </div>
							[#break /]
						[/@shiro.hasPermission]
					[/#list]
                    [#list ["admin:order", "admin:print", "admin:payment", "admin:refunds", "admin:shipping", "admin:returns", "admin:vipOrder" ] as permission]
						[@shiro.hasPermission name = permission]
							<div class="panel panel-default">
		                        <div class="panel-heading">
		                            <a href="#orderGroup" data-parent="#side_accordion" data-toggle="collapse" class="accordion-toggle">
		                                <i class="glyphicon glyphicon-th"></i> ${message("admin.main.orderGroup")}
		                            </a>
		                        </div>
		                        <div class="accordion-body collapse" id="orderGroup">
		                            <div class="panel-body">
		                                <ul class="nav nav-pills nav-stacked">
		                                    [@shiro.hasPermission name="admin:order"]
												<li><a href="../order/list.jhtml" target="iframe">${message("admin.main.order")}</a></li>
											[/@shiro.hasPermission]

											[@shiro.hasPermission name="admin:goodsSend"]
                                                <li><a href="../goodsSend/list.jhtml" target="iframe">${message("admin.role.goodsSend")}</a></li>
											[/@shiro.hasPermission]

											[@shiro.hasPermission name="admin:print"]
                                           		<li><a href="../print/list.jhtml" target="iframe">${message("admin.role.print")}</a></li>
											[/@shiro.hasPermission]

											[@shiro.hasPermission name="admin:payment"]
                                                <li><a href="../payment/list.jhtml" target="iframe">${message("admin.main.payment")}</a></li>
											[/@shiro.hasPermission]

											[@shiro.hasPermission name="admin:refunds"]
												<li><a href="../refunds/list.jhtml" target="iframe">${message("admin.main.refunds")}</a></li>
											[/@shiro.hasPermission]

											[@shiro.hasPermission name="admin:shipping"]
												<li><a href="../shipping/list.jhtml" target="iframe">${message("admin.main.shipping")}</a></li>
											[/@shiro.hasPermission]

											[@shiro.hasPermission name="admin:returns"]
												<li><a href="../returns/list.jhtml" target="iframe">${message("admin.main.returns")}</a></li>
											[/@shiro.hasPermission]

		                                </ul>
		                            </div>
		                        </div>
		                    </div>
							[#break /]
						[/@shiro.hasPermission]
					[/#list]
                    [#list ["admin:member", "admin:certificates_shenhe", "admin:review" ,"admin:consultation", "admin:messageConfig", "admin:feedback"] as permission]
						[@shiro.hasPermission name = permission]
							<div class="panel panel-default">
		                        <div class="panel-heading">
		                            <a href="#memberGroup" data-parent="#side_accordion" data-toggle="collapse" class="accordion-toggle">
		                                <i class="glyphicon glyphicon-user"></i> ${message("admin.main.memberGroup")}
		                            </a>
		                        </div>
		                        <div class="accordion-body collapse" id="memberGroup">
		                            <div class="panel-body">
		                                <ul class="nav nav-pills nav-stacked">
		                                    [@shiro.hasPermission name="admin:member"]
												<li><a href="../member/list.jhtml" target="iframe">${message("admin.main.member")}</a></li>
											[/@shiro.hasPermission]
											[@shiro.hasPermission name="admin:certificates_shenhe"]
                                                <li><a href="../certificates_shenhe/list.jhtml" target="iframe">${message("certificates.shenhe")}</a></li>
											[/@shiro.hasPermission]

											[@shiro.hasPermission name="admin:review"]
												<li><a href="../review/list.jhtml" target="iframe">${message("admin.main.review")}</a></li>
											[/@shiro.hasPermission]

											[@shiro.hasPermission name="admin:consultation"]
												<li><a href="../consultation/list.jhtml" target="iframe">${message("admin.main.consultation")}</a></li>
											[/@shiro.hasPermission]

											[@shiro.hasPermission name="admin:messageConfig"]
												<li><a href="../message_config/list.jhtml" target="iframe">${message("admin.main.messageConfig")}</a></li>
											[/@shiro.hasPermission]


											[@shiro.hasPermission name="admin:feedback"]
                                                <li><a href="../feedback/list.jhtml" target="iframe">${message("admin.main.feedback")}</a></li>
											[/@shiro.hasPermission]
		                                </ul>

		                            </div>
		                        </div>
		                    </div>
							[#break /]
						[/@shiro.hasPermission]
					[/#list]
                    [#list ["admin:adPosition", "admin:ad", "admin:newGoods", "admin:like","admin:character" ] as permission]
						[@shiro.hasPermission name = permission]
							<div class="panel panel-default">
		                        <div class="panel-heading">
		                            <a href="#contentGroup" data-parent="#side_accordion" data-toggle="collapse" class="accordion-toggle">
		                                <i class="glyphicon glyphicon-leaf"></i> ${message("admin.main.contentGroup")}
		                            </a>
		                        </div>
		                        <div class="accordion-body collapse" id="contentGroup">
		                            <div class="panel-body">
		                                 <ul class="nav nav-pills nav-stacked">


											[@shiro.hasPermission name="admin:adPosition"]
												<li><a href="../ad_position/list.jhtml" target="iframe">${message("admin.main.adPosition")}</a></li>
											[/@shiro.hasPermission]

											[@shiro.hasPermission name="admin:ad"]
												<li><a href="../ad/list.jhtml" target="iframe">${message("admin.main.ad")}</a></li>
											[/@shiro.hasPermission]

											 [@shiro.hasPermission name="admin:newGoods"]
                                                 <li><a href="../newGoods/list.jhtml" target="iframe">${message("admin.role.newGoods")}</a></li>
											 [/@shiro.hasPermission]

											 [@shiro.hasPermission name="admin:like"]
                                                 <li><a href="../like/list.jhtml" target="iframe">${message("admin.role.like")}</a></li>
											 [/@shiro.hasPermission]


											 [@shiro.hasPermission name="admin:character"]
                                                 <li><a href="../character/list.jhtml" target="iframe">${message("admin.role.character")}</a></li>
											 [/@shiro.hasPermission]

										 </ul>
		                            </div>
		                        </div>
		                    </div>
							[#break /]
						[/@shiro.hasPermission]
					[/#list]
                    [#list ["admin:miaobiGoods", "admin:promotion" ] as permission]
						[@shiro.hasPermission name = permission]
							<div class="panel panel-default">
		                        <div class="panel-heading">
		                            <a href="#marketingGroup" data-parent="#side_accordion" data-toggle="collapse" class="accordion-toggle">
		                               <i class="glyphicon glyphicon-phone-alt"></i> ${message("admin.main.activity")}
		                            </a>
		                        </div>
		                        <div class="accordion-body collapse" id="marketingGroup">
		                            <div class="panel-body">
		                                <ul class="nav nav-pills nav-stacked">


											[@shiro.hasPermission name="admin:miaobiGoods"]
                                                <li><a href="../miaobi_goods/list.jhtml" target="iframe">${message("admin.role.miaobiGoods")}</a></li>
											[/@shiro.hasPermission]

											[@shiro.hasPermission name="admin:promotion"]
												<li><a href="../promotion/list.jhtml" target="iframe">${message("admin.main.promotion")}</a></li>
											[/@shiro.hasPermission]
												[@shiro.hasPermission name="admin:activity"]
												<li><a href="../activity/list.jhtml" target="iframe">${message("admin.main.activity")}</a></li>
												[/@shiro.hasPermission]


										</ul>
		                            </div>
		                        </div>
		                    </div>
							[#break /]
						[/@shiro.hasPermission]
					[/#list]
					[#list ["admin:reverseAuctionPerm:list"  ] as permission]
						[@shiro.hasPermission name = permission]
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    <a href="#reverseAuction" data-parent="#side_accordion" data-toggle="collapse" class="accordion-toggle">
                                        <i class="glyphicon glyphicon-time"></i> ${message("admin.role.reverseAuction")}
                                    </a>
                                </div>
                                <div class="accordion-body collapse" id="reverseAuction">
                                    <div class="panel-body">
                                        <ul class="nav nav-pills nav-stacked">
											[@shiro.hasPermission name="admin:reverseAuctionPerm:list"]
												<li><a href="../reverseAuction/list.jhtml" target="iframe">${message("admin.role.reverseAuction")}</a></li>
											[/@shiro.hasPermission]
										</ul>
                                    </div>
                                </div>
                            </div>
							[#break /]
						[/@shiro.hasPermission]
					[/#list]
					[#list ["admin:fuDai"] as permission]
						[@shiro.hasPermission name = permission]
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    <a href="#fuDaiGroup" data-parent="#side_accordion" data-toggle="collapse" class="accordion-toggle">
                                        <i class="glyphicon glyphicon-gift"></i> ${message("admin.main.fuDai")}
                                    </a>
                                </div>
                                <div class="accordion-body collapse" id="fuDaiGroup">
                                    <div class="panel-body">
                                        <ul class="nav nav-pills nav-stacked">
											[@shiro.hasPermission name="admin:fuDai"]
                                                <li><a href="../fuDai/list.jhtml" target="iframe">${message("admin.main.fuDai")}</a></li>
											[/@shiro.hasPermission]
                                        </ul>
                                    </div>
                                </div>
                            </div>
							[#break /]
						[/@shiro.hasPermission]
					[/#list]
					[#list ["admin:myWallet" ,"admin:myWalletInfo"] as permission]
						[@shiro.hasPermission name = permission]
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    <a href="#walletGroup" data-parent="#side_accordion" data-toggle="collapse" class="accordion-toggle">
                                        <i class="glyphicon glyphicon-time"></i> ${message("admin.main.walletManage")}
                                    </a>
                                </div>
                                <div class="accordion-body collapse" id="walletGroup">
                                    <div class="panel-body">
                                        <ul class="nav nav-pills nav-stacked">
											[@shiro.hasPermission name="admin:myWallet"]
                                                <li><a href="../myWallet/list.jhtml" target="iframe">${message("admin.role.myWallet")}</a></li>
											[/@shiro.hasPermission]

											[@shiro.hasPermission name="admin:myWalletInfo"]
                                                <li><a href="../myWallet/log.jhtml" target="iframe">${message("admin.role.myWalletInfo")}</a></li>
											[/@shiro.hasPermission]
                                        </ul>
                                    </div>
                                </div>
                            </div>
							[#break /]
						[/@shiro.hasPermission]
					[/#list]
                    [#list [ "admin:vip_goods", "admin:orderStatistic", "admin:memberRanking", "admin:goodsRanking" ,"admin:caiwu"] as permission]
						[@shiro.hasPermission name = permission]
							 <div class="panel panel-default">
		                        <div class="panel-heading">
		                            <a href="#statisticGroup" data-parent="#side_accordion" data-toggle="collapse" class="accordion-toggle">
		                                <i class="glyphicon glyphicon-signal"></i> ${message("admin.main.statisticGroup")}
		                            </a>
		                        </div>
		                        <div class="accordion-body collapse" id="statisticGroup">
		                            <div class="panel-body">
		                                <ul class="nav nav-pills nav-stacked">
		                                    [#--[@shiro.hasPermission name="admin:statistics"]--]
												[#--<li><a href="../statistics/view.jhtml" target="iframe">${message("admin.main.statistics")}</a></li>--]
											[#--[/@shiro.hasPermission]--]

											[#--[@shiro.hasPermission name="admin:statistics"]--]
												[#--<li><a href="../statistics/setting.jhtml" target="iframe">${message("admin.main.statisticsSetting")}</a></li>--]
											[#--[/@shiro.hasPermission]--]

											[@shiro.hasPermission name="admin:vip_goods"]
                                                <li><a href="../vipGoods/list.jhtml" target="iframe">${message("admin.role.vipGoods")}</a></li>
											[/@shiro.hasPermission]

											[@shiro.hasPermission name="admin:memberStatistic"]
												<li><a href="../member_statistic/list.jhtml?type=0" target="iframe">${message("admin.main.memberStatistic")}</a></li>
											[/@shiro.hasPermission]

											[@shiro.hasPermission name="admin:orderStatistic"]
												<li><a href="../order_statistic/list.jhtml?type=1" target="iframe">${message("admin.main.orderStatistic")}</a></li>
											[/@shiro.hasPermission]

											[@shiro.hasPermission name="admin:memberRanking"]
												<li><a href="../member_ranking/list.jhtml" target="iframe">${message("admin.main.memberRanking")}</a></li>
											[/@shiro.hasPermission]

											[@shiro.hasPermission name="admin:goodsRanking"]
                                                <li><a href="../goods_ranking/list.jhtml" target="iframe">${message("admin.main.goodsRanking")}</a></li>
											[/@shiro.hasPermission]


											[@shiro.hasPermission name="admin:caiwu"]
                                                <li><a href="../caiwu/list.jhtml" target="iframe">${message("admin.role.caiwu")}</a></li>
											[/@shiro.hasPermission]
											[@shiro.hasPermission name="admin:proxy"]
                                                <li><a href="../proxy/list.jhtml" target="iframe">${message("admin.role.proxy")}</a></li>
											[/@shiro.hasPermission]

		                                </ul>
		                            </div>
		                        </div>
		                    </div>
							[#break /]
						[/@shiro.hasPermission]
					[/#list]
                   [#list ["admin:setting", "admin:targetPath", "admin:area", "admin:shippingMethod", "admin:deliveryCorp", "admin:message"] as permission]
						[@shiro.hasPermission name = permission]
							<div class="panel panel-default">
		                        <div class="panel-heading">
		                            <a href="#systemGroup" data-parent="#side_accordion" data-toggle="collapse" class="accordion-toggle">
		                                <i class="glyphicon glyphicon-cog"></i> ${message("admin.main.systemGroup")}
		                            </a>
		                        </div>
		                        <div class="accordion-body collapse" id="systemGroup">
		                            <div class="panel-body">
		                                <ul class="nav nav-pills nav-stacked">
		                                    <!-- <li class="active"><a href="javascript:void(0)">Account Settings</a></li> -->
		                                    [@shiro.hasPermission name="admin:setting"]
													<li><a href="../setting/edit.jhtml" target="iframe">${message("admin.main.setting")}</a></li>
											[/@shiro.hasPermission]

											[@shiro.hasPermission name="admin:targetPath"]
                                                <li><a href="../targetPath/list.jhtml" target="iframe">${message("admin.main.targetPath")}</a></li>
											[/@shiro.hasPermission]

											[@shiro.hasPermission name="admin:area"]
													<li><a href="../area/list.jhtml?flag=0" target="iframe">${message("admin.main.area")}</a></li>
											[/@shiro.hasPermission]


											[@shiro.hasPermission name="admin:shippingMethod"]
													<li><a href="../shipping_method/list.jhtml" target="iframe">${message("admin.main.shippingMethod")}</a></li>
											[/@shiro.hasPermission]

											[@shiro.hasPermission name="admin:deliveryCorp"]
													<li><a href="../delivery_corp/list.jhtml" target="iframe">${message("admin.main.deliveryCorp")}</a></li>
											[/@shiro.hasPermission]


											[@shiro.hasPermission name="admin:message"]
													<li><a href="../message/list.jhtml" target="iframe">${message("admin.main.message")}</a></li>
											[/@shiro.hasPermission]

											[@shiro.hasPermission name="admin:log"]
													<li><a href="../log/list.jhtml" target="iframe">${message("admin.main.log")}</a></li>
											[/@shiro.hasPermission]


		                                </ul>
		                            </div>
		                        </div>
		                    </div>
							[#break /]
						[/@shiro.hasPermission]
					[/#list]
					[#list ["admin:admin", "admin:permission", "admin:role"] as permission]
						[@shiro.hasPermission name = permission]
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    <a href="#permission" data-parent="#side_accordion" data-toggle="collapse" class="accordion-toggle">
                                        <i class="glyphicon glyphicon-gift"></i> ${message("admin.permission")}
                                    </a>
                                </div>
                                <div class="accordion-body collapse" id="permission">
                                    <div class="panel-body">
                                        <ul class="nav nav-pills nav-stacked">
											[@shiro.hasPermission name="admin:admin"]
                                                <li><a href="../admin/list.jhtml" target="iframe">${message("admin.main.admin")}</a></li>
											[/@shiro.hasPermission]

											[@shiro.hasPermission name="admin:permission"]
                                                <li><a href="../permission/list.jhtml" target="iframe">${message("admin.main.permission")}</a></li>
											[/@shiro.hasPermission]

											[@shiro.hasPermission name="admin:role"]
                                                <li><a href="../role/list.jhtml" target="iframe">${message("admin.main.role")}</a></li>
											[/@shiro.hasPermission]
                                        </ul>
                                    </div>
                                </div>
                            </div>
							[#break /]
						[/@shiro.hasPermission]
					[/#list]
					[#list ["admin:appManage"] as permission]
						[@shiro.hasPermission name = permission]
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    <a href="#third" data-parent="#side_accordion" data-toggle="collapse" class="accordion-toggle">
                                        <i class="glyphicon glyphicon-gift"></i>  ${message("admin.third")}
                                    </a>
                                </div>
                                <div class="accordion-body collapse" id="third">
                                    <div class="panel-body">
                                        <ul class="nav nav-pills nav-stacked">
											[@shiro.hasPermission name="admin:appManage"]
                                                <li><a href="../appManage/list.jhtml" target="iframe">${message("admin.appManage")}</a></li>
											[/@shiro.hasPermission]
                                        </ul>
                                    </div>
                                </div>
                            </div>
							[#break /]
						[/@shiro.hasPermission]
					[/#list]


			[#list ["admin:partner"] as permission]
		[@shiro.hasPermission name = permission]
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    <a href="#partner" data-parent="#side_accordion" data-toggle="collapse" class="accordion-toggle">
                                        <i class="glyphicon glyphicon-gift"></i>  ${message("admin.role.partner")}
                                    </a>
                                </div>
                                <div class="accordion-body collapse" id="partner">
                                    <div class="panel-body">
                                        <ul class="nav nav-pills nav-stacked">
											[@shiro.hasPermission name="admin:partner"]
                                                <li><a href="../partner/list.jhtml" target="iframe">${message("admin.role.partner")}</a></li>
											[/@shiro.hasPermission]
                                        </ul>
                                    </div>
                                </div>
                            </div>
			[#break /]
		[/@shiro.hasPermission]
	[/#list]

					[#list ["admin:deliveryCenter", "admin:deliveryTemplate", "admin:deposit", "admin:memberRank", "admin:memberAttribute", "admin:paymentMethod", "admin:paymentPlugin",
					"admin:storagePlugin", "admin:loginPlugin", "admin:coupon", "admin:template", "admin:static", "admin:cache", "admin:index", "admin:organization", "admin:theme" ,
					"admin:organ", "admin:seo", "admin:flashsale", "admin:freeApply", "admin:sitemap", "admin:goodsTheme", "admin:ticketShare", "admin:navigation", "admin:article", "admin:articleCategory", "admin:friendLink", "admin:productNotify" ,"admin:log"  ] as permission]
						[@shiro.hasPermission name = permission]
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    <a href="#parpering" data-parent="#side_accordion" data-toggle="collapse" class="accordion-toggle">
                                        <i class="glyphicon glyphicon-time"></i> ${message("admin.parpering")}
                                    </a>
                                </div>
                                <div class="accordion-body collapse" id="parpering">
                                    <div class="panel-body">
                                        <ul class="nav nav-pills nav-stacked">
											[@shiro.hasPermission name="admin:deliveryCenter"]
                                                <li><a href="../delivery_center/list.jhtml" target="iframe">${message("admin.main.deliveryCenter")}</a></li>
											[/@shiro.hasPermission]

											[@shiro.hasPermission name="admin:deliveryTemplate"]
                                                <li><a href="../delivery_template/list.jhtml" target="iframe">${message("admin.main.deliveryTemplate")}</a></li>
											[/@shiro.hasPermission]

											[@shiro.hasPermission name="admin:deposit"]
                                                <li><a href="../deposit/log.jhtml" target="iframe">${message("admin.main.deposit")}</a></li>
											[/@shiro.hasPermission]

											[@shiro.hasPermission name="admin:memberRank"]
                                                <li><a href="../member_rank/list.jhtml" target="iframe">${message("admin.main.memberRank")}</a></li>
											[/@shiro.hasPermission]

											[@shiro.hasPermission name="admin:memberAttribute"]
                                                <li><a href="../member_attribute/list.jhtml" target="iframe">${message("admin.main.memberAttribute")}</a></li>
											[/@shiro.hasPermission]

											[@shiro.hasPermission name="admin:paymentMethod"]
                                                <li><a href="../payment_method/list.jhtml" target="iframe">${message("admin.main.paymentMethod")}</a></li>
											[/@shiro.hasPermission]
											[@shiro.hasPermission name="admin:paymentPlugin"]
                                                <li><a href="../payment_plugin/list.jhtml" target="iframe">${message("admin.main.paymentPlugin")}</a></li>
											[/@shiro.hasPermission]

											[@shiro.hasPermission name="admin:storagePlugin"]
                                                <li><a href="../storage_plugin/list.jhtml" target="iframe">${message("admin.main.storagePlugin")}</a></li>
											[/@shiro.hasPermission]

											[@shiro.hasPermission name="admin:loginPlugin"]
                                                <li><a href="../login_plugin/list.jhtml" target="iframe">${message("admin.main.loginPlugin")}</a></li>
											[/@shiro.hasPermission]
											[@shiro.hasPermission name="admin:coupon"]
                                                <li><a href="../coupon/list.jhtml" target="iframe">${message("admin.main.coupon")}</a></li>
											[/@shiro.hasPermission]


											[@shiro.hasPermission name="admin:template"]
                                                <li><a href="../template/list.jhtml" target="iframe">${message("admin.main.template")}</a></li>
											[/@shiro.hasPermission]

											[@shiro.hasPermission name="admin:theme"]
                                                <li><a href="../theme/setting.jhtml" target="iframe">${message("admin.main.theme")}</a></li>
											[/@shiro.hasPermission]
											[@shiro.hasPermission name="admin:cache"]
                                                <li><a href="../cache/clear.jhtml" target="iframe">${message("admin.main.cache")}</a></li>
											[/@shiro.hasPermission]

											[@shiro.hasPermission name="admin:static"]
                                                <li><a href="../static/generate.jhtml" target="iframe">${message("admin.main.static")}</a></li>
											[/@shiro.hasPermission]

											[@shiro.hasPermission name="admin:index"]
                                                <li><a href="../index/generate.jhtml" target="iframe">${message("admin.main.index")}</a></li>
											[/@shiro.hasPermission]
											[@shiro.hasPermission name="admin:organ"]
                                                <li>
                                                    <a href="../organ/list.jhtml" target="iframe">${message("admin.main.organ")}</a>
                                                </li>
											[/@shiro.hasPermission]
											[@shiro.hasPermission name="admin:organization"]
                                                <li><a href="../organization/list.jhtml" target="iframe">${message("admin.main.organization")}</a></li>
											[/@shiro.hasPermission]

											[@shiro.hasPermission name="admin:seo"]
                                                <li><a href="../seo/list.jhtml" target="iframe">${message("admin.main.seo")}</a></li>
											[/@shiro.hasPermission]
											[@shiro.hasPermission name="admin:flashsale"]
                                                <li><a href="../flashsale/list.jhtml" target="iframe">${message("admin.role.flashsale")}</a></li>
											[/@shiro.hasPermission]
											[@shiro.hasPermission name="admin:freeApply"]
                                                <li><a href="../freeUser/list.jhtml" target="iframe">${message("admin.role.freeApply")}</a></li>
											[/@shiro.hasPermission]
											[@shiro.hasPermission name="admin:sitemap"]
                                                <li><a href="../sitemap/generate.jhtml" target="iframe">${message("admin.main.sitemap")}</a></li>
											[/@shiro.hasPermission]
											[@shiro.hasPermission name="admin:goodsTheme"]
                                                <li><a href="../goodsTheme/list.jhtml" target="iframe">${message("admin.goods.theme")}</a></li>
											[/@shiro.hasPermission]

											[@shiro.hasPermission name="admin:ticketShare"]
                                                <li><a href="../ticketConfig/list.jhtml" target="iframe">${message("admin.role.ticket_share")}</a></li>
											[/@shiro.hasPermission]

											[@shiro.hasPermission name="admin:navigation"]
                                                <li><a href="../navigation/list.jhtml" target="iframe">${message("admin.main.navigation")}</a></li>
											[/@shiro.hasPermission]

											[@shiro.hasPermission name="admin:article"]
                                                <li><a href="../article/list.jhtml" target="iframe">${message("admin.main.article")}</a></li>
											[/@shiro.hasPermission]

											[@shiro.hasPermission name="admin:articleCategory"]
                                                <li><a href="../article_category/list.jhtml" target="iframe">${message("admin.main.articleCategory")}</a></li>
											[/@shiro.hasPermission]

											[@shiro.hasPermission name="admin:friendLink"]
                                                <li><a href="../friend_link/list.jhtml" target="iframe">${message("admin.main.friendLink")}</a></li>
											[/@shiro.hasPermission]


											[@shiro.hasPermission name="admin:productNotify"]
                                                <li><a href="../product_notify/list.jhtml" target="iframe">${message("admin.main.productNotify")}</a></li>
											[/@shiro.hasPermission]

										</ul>
                                    </div>
                                </div>
                            </div>
							[#break /]
						[/@shiro.hasPermission]
					[/#list]
                </div>
                <div class="push"></div>
            </div>
        </div>
    </div>
    <script src="${base}/gebo3/js/jquery.min.js"></script>
    <script src="${base}/gebo3/js/jquery-migrate.min.js"></script>
    <script src="${base}/gebo3/lib/jquery-ui/jquery-ui-1.10.0.custom.min.js"></script>
    <!-- touch events for jquery ui-->
	<script src="${base}/gebo3/js/forms/jquery.ui.touch-punch.min.js"></script>
    <!-- easing plugin -->
	<script src="${base}/gebo3/js/jquery.easing.1.3.min.js"></script>
    <!-- smart resize event -->
	<script src="${base}/gebo3/js/jquery.debouncedresize.min.js"></script>
    <!-- js cookie plugin -->
	<script src="${base}/gebo3/js/jquery_cookie_min.js"></script>
    <!-- main bootstrap js -->
	<script src="${base}/gebo3/bootstrap/js/bootstrap.min.js"></script>
    <!-- bootstrap plugins -->
	<script src="${base}/gebo3/js/bootstrap.plugins.min.js"></script>
	<!-- typeahead -->
	<script src="${base}/gebo3/lib/typeahead/typeahead.min.js"></script>
    <!-- code prettifier -->
	<script src="${base}/gebo3/lib/google-code-prettify/prettify.min.js"></script>
    <!-- sticky messages -->
	<script src="${base}/gebo3/lib/sticky/sticky.min.js"></script>
    <!-- lightbox -->
	<script src="${base}/gebo3/lib/colorbox/jquery.colorbox.min.js"></script>
    <!-- jBreadcrumbs -->
	<script src="${base}/gebo3/lib/jBreadcrumbs/js/jquery.jBreadCrumb.1.1.min.js"></script>
	<!-- hidden elements width/height -->
	<script src="${base}/gebo3/js/jquery.actual.min.js"></script>
	<!-- custom scrollbar -->
	<script src="${base}/gebo3/lib/slimScroll/jquery.slimscroll.js"></script>
	<!-- fix for ios orientation change -->
	<script src="${base}/gebo3/js/ios-orientationchange-fix.js"></script>
	<!-- to top -->
	<script src="${base}/gebo3/lib/UItoTop/jquery.ui.totop.min.js"></script>
	<!-- mobile nav -->
	<script src="${base}/gebo3/js/selectNav.js"></script>
    <!-- moment.js date library -->
    <script src="${base}/gebo3/lib/moment/moment.min.js"></script>

	<!-- common functions -->
	<script src="${base}/gebo3/js/pages/gebo_common.js"></script>


    <script>
        $(document).ready(function() {
			//* jQuery.browser.mobile (http://detectmobilebrowser.com/)
			//* jQuery.browser.mobile will be true if the browser is a mobile device
			(function(a){jQuery.browser.mobile=/android.+mobile|avantgo|bada\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|midp|mmp|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\/|plucker|pocket|psp|symbian|treo|up\.(browser|link)|vodafone|wap|windows (ce|phone)|xda|xiino/i.test(a)||/1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\-(n|u)|c55\/|capi|ccwa|cdm\-|cell|chtm|cldc|cmd\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\-s|devi|dica|dmob|do(c|p)o|ds(12|\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\-|_)|g1 u|g560|gene|gf\-5|g\-mo|go(\.w|od)|gr(ad|un)|haie|hcit|hd\-(m|p|t)|hei\-|hi(pt|ta)|hp( i|ip)|hs\-c|ht(c(\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\-(20|go|ma)|i230|iac( |\-|\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\/)|klon|kpt |kwc\-|kyo(c|k)|le(no|xi)|lg( g|\/(k|l|u)|50|54|e\-|e\/|\-[a-w])|libw|lynx|m1\-w|m3ga|m50\/|ma(te|ui|xo)|mc(01|21|ca)|m\-cr|me(di|rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\-2|po(ck|rt|se)|prox|psio|pt\-g|qa\-a|qc(07|12|21|32|60|\-[2-7]|i\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\-|oo|p\-)|sdk\/|se(c(\-|0|1)|47|mc|nd|ri)|sgh\-|shar|sie(\-|m)|sk\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\-|v\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\-|tdg\-|tel(i|m)|tim\-|t\-mo|to(pl|sh)|ts(70|m\-|m3|m5)|tx\-9|up(\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|xda(\-|2|g)|yas\-|your|zeto|zte\-/i.test(a.substr(0,4))})(navigator.userAgent||navigator.vendor||window.opera);
			//replace themeforest iframe
			if(jQuery.browser.mobile) {
				if (top !== self) top.location.href = self.location.href;
			}
        });
    </script>

	<!--ipt type="text/javascript">

		var _gaq = _gaq || [];
		_gaq.push(['_setAccount', 'UA-32339645-1']);
		_gaq.push(['_trackPageview']);

		(function() {
		  var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
		  ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
		  var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
		})();

	  </scri-->

    </body>
</html>
