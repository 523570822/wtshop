<!DOCTYPE html>
<html>

<head>
  <!-- Meta, title, CSS, favicons, etc. -->
  <meta charset="utf-8">
  <title>${message("admin.main.title")} - Powered By ${setting.siteAuthor}</title>
  <meta name="keywords" content="HTML5 Bootstrap 3 Admin Template UI Theme" />
  <meta name="description" content="AdminDesigns - A Responsive HTML5 Admin UI Framework">
  <meta name="author" content="AdminDesigns">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">

  <!-- Font CSS (Via CDN) -->
  <link rel='stylesheet' type='text/css' href='http://fonts.googleapis.com/css?family=Open+Sans:300,400,600'>

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

</head>

<body class="dashboard-page sb-l-o sb-r-c">

  <!-- Start: Main -->
  <div id="main">

    <!-- Start: Header -->
    <header class="navbar navbar-fixed-top bg-dark">
      <div class="navbar-branding dark bg-dark">
        <a class="navbar-brand" href="/" target="_blank">
          <b>邻檬圈</b>控制台
        </a>
        <span id="toggle_sidemenu_l" class="ad ad-lines"></span>
      </div>
    
      <ul class="nav navbar-nav navbar-right">
        <li>
          <a href="#" class=""> 
           <span class="fa fa-user pr5"></span> [@shiro.principal name="username" /] </a>
        </li>
        <li>
          <a href="${base}/admin/message/list.jhtml" target="iframe">
            <span class="fa fa-bell-o pr5"></span> 系统消息
            <span class="label label-warning">${unreadMessageCount}</span>
          </a>
        </li>
        <li>
           <a href="../profile/edit.jhtml" target="iframe">
           <span class="fa fa-gear pr5"></span>${message("admin.main.profile")}</a>
        </li>
        <li>
           <a href="/signout" target="_top"><span class="fa fa-power-off pr5"></span>${message("admin.main.logout")}</a>
        </li>
      </ul>

    </header>
    <!-- End: Header -->

    <!-- Start: Sidebar Left -->
    <aside id="sidebar_left" class="nano nano-primary affix has-scrollbar sidebar-default">

      <!-- Start: Sidebar Left Content -->
      <div class="sidebar-left-content nano-content">

        <!-- Start: Sidebar Header -->
        <header class="sidebar-header">

        </header>
        <!-- End: Sidebar Header -->

        <!-- Start: Sidebar Left Menu -->
        <ul class="nav sidebar-menu">
          <li class="sidebar-label pt15">系统菜单</li>
          
          <li>
            <a class="accordion-toggle" href="#">
              <span class="glyphicon glyphicon-fire"></span>
              <span class="sidebar-title">${message("admin.main.productGroup")}</span>
              <span class="caret"></span>
            </a>
            <ul class="nav sub-nav">
              	[@shiro.hasPermission name="admin:goods"]
					<li>
						<a href="../goods/list.jhtml" target="iframe">
						<span class="glyphicon glyphicon-book"></span>${message("admin.main.goods")}</a>
					<li>
				[/@shiro.hasPermission]
             	[@shiro.hasPermission name="admin:stock"]
					<li>
						<a href="../stock/log.jhtml" target="iframe">
						<span class="imoon imoon-quill"></span>${message("admin.main.stock")}</a>
					</li>
				[/@shiro.hasPermission]
				[@shiro.hasPermission name="admin:productCategory"]
					<li>
						<a href="../product_category/list.jhtml" target="iframe"><span class="glyphicon glyphicon-tags"></span>${message("admin.main.productCategory")}</a>
					</li>
				[/@shiro.hasPermission]
				[@shiro.hasPermission name="admin:parameter"]
					<li>
						<a href="../parameter/list.jhtml" target="iframe">${message("admin.main.parameter")}</a>
					</li>
				[/@shiro.hasPermission]
				[@shiro.hasPermission name="admin:attribute"]
					<li>
						<a href="../attribute/list.jhtml" target="iframe">${message("admin.main.attribute")}</a>
					</li>
				[/@shiro.hasPermission]
				[@shiro.hasPermission name="admin:specification"]
					<li>
						<a href="../specification/list.jhtml" target="iframe">${message("admin.main.specification")}</a>
					</li>
				[/@shiro.hasPermission]
				[@shiro.hasPermission name="admin:brand"]
					<li>
						<a href="../brand/list.jhtml" target="iframe">${message("admin.main.brand")}</a>
					</li>
				[/@shiro.hasPermission]
				[@shiro.hasPermission name="admin:productNotify"]
					<li>
						<a href="../product_notify/list.jhtml" target="iframe">${message("admin.main.productNotify")}</a>
					</li>
				[/@shiro.hasPermission]
            </ul>
          </li>
          <li>
            <a class="accordion-toggle" href="#">
	            <span class="fa fa-money"></span>
	            <span class="sidebar-title">${message("admin.main.orderGroup")}</span>
	            <span class="caret"></span>
            </a>
            <ul class="nav sub-nav">
              	[@shiro.hasPermission name="admin:order"]
					<li>
						<a href="../order/list.jhtml" target="iframe"><span class="fa fa-money"></span>${message("admin.main.order")}</a>
					</li>
				[/@shiro.hasPermission]
              	[@shiro.hasPermission name="admin:payment"]
					<li>
						<a href="../payment/list.jhtml" target="iframe">${message("admin.main.payment")}</a>
					</li>
				[/@shiro.hasPermission]
				[@shiro.hasPermission name="admin:refunds"]
					<li>
						<a href="../refunds/list.jhtml" target="iframe">${message("admin.main.refunds")}</a>
					</li>
				[/@shiro.hasPermission]
				[@shiro.hasPermission name="admin:shipping"]
					<li>
						<a href="../shipping/list.jhtml" target="iframe">${message("admin.main.shipping")}</a>
					</li>
				[/@shiro.hasPermission]
				[@shiro.hasPermission name="admin:returns"]
					<li>
						<a href="../returns/list.jhtml" target="iframe">${message("admin.main.returns")}</a>
					</li>
				[/@shiro.hasPermission]
				[@shiro.hasPermission name="admin:deliveryCenter"]
					<li>
						<a href="../delivery_center/list.jhtml" target="iframe">${message("admin.main.deliveryCenter")}</a>
					</li>
				[/@shiro.hasPermission]
				[@shiro.hasPermission name="admin:deliveryTemplate"]
					<li>
						<a href="../delivery_template/list.jhtml" target="iframe">${message("admin.main.deliveryTemplate")}</a>
					</li>
				[/@shiro.hasPermission]
            </ul>
          </li>
          <li>
          	<a class="accordion-toggle" href="#">
	            <span class="fa fa-users"></span>
	            <span class="sidebar-title">${message("admin.main.memberGroup")}</span>
	            <span class="caret"></span>
            </a>
            <ul class="nav sub-nav">
            	[@shiro.hasPermission name="admin:member"]
					<li>
						<a href="../member/list.jhtml" target="iframe">${message("admin.main.member")}</a>
					</li>
				[/@shiro.hasPermission]
				[@shiro.hasPermission name="admin:memberRank"]
					<li>
						<a href="../member_rank/list.jhtml" target="iframe">${message("admin.main.memberRank")}</a>
					</li>
				[/@shiro.hasPermission]
				[@shiro.hasPermission name="admin:memberAttribute"]
					<li>
						<a href="../member_attribute/list.jhtml" target="iframe">${message("admin.main.memberAttribute")}</a>
					</li>
				[/@shiro.hasPermission]
				[@shiro.hasPermission name="admin:point"]
					<li>
						<a href="../point/log.jhtml" target="iframe">${message("admin.main.point")}</a>
					</li>
				[/@shiro.hasPermission]
				[@shiro.hasPermission name="admin:deposit"]
					<li>
						<a href="../deposit/log.jhtml" target="iframe">${message("admin.main.deposit")}</a>
					</li>
				[/@shiro.hasPermission]
				[@shiro.hasPermission name="admin:review"]
					<li>
						<a href="../review/list.jhtml" target="iframe">${message("admin.main.review")}</a>
					</li>
				[/@shiro.hasPermission]
				[@shiro.hasPermission name="admin:consultation"]
					<li>
						<a href="../consultation/list.jhtml" target="iframe">${message("admin.main.consultation")}</a>
					</li>
				[/@shiro.hasPermission]
				[@shiro.hasPermission name="admin:messageConfig"]
					<li>
						<a href="../message_config/list.jhtml" target="iframe">${message("admin.main.messageConfig")}</a>
					</li>
				[/@shiro.hasPermission]
            </ul>
          </li>
          
          <li>
	          <a class="accordion-toggle" href="#">
		          <span class="fa fa-diamond"></span>
		          <span class="sidebar-title">${message("admin.main.contentGroup")}</span>
		          <span class="caret"></span>
	          </a>
	          <ul class="nav sub-nav">
	          	[@shiro.hasPermission name="admin:navigation"]
					<li>
						<a href="../navigation/list.jhtml" target="iframe">${message("admin.main.navigation")}</a>
					</li>
				[/@shiro.hasPermission]
				[@shiro.hasPermission name="admin:article"]
					<li>
						<a href="../article/list.jhtml" target="iframe">${message("admin.main.article")}</a>
					</li>
				[/@shiro.hasPermission]
				[@shiro.hasPermission name="admin:articleCategory"]
					<li>
						<a href="../article_category/list.jhtml" target="iframe">${message("admin.main.articleCategory")}</a>
					</li>
				[/@shiro.hasPermission]
				[@shiro.hasPermission name="admin:tag"]
					<li>
						<a href="../tag/list.jhtml" target="iframe">${message("admin.main.tag")}</a>
					</li>
				[/@shiro.hasPermission]
				[@shiro.hasPermission name="admin:friendLink"]
					<li>
						<a href="../friend_link/list.jhtml" target="iframe">${message("admin.main.friendLink")}</a>
					</li>
				[/@shiro.hasPermission]
				[@shiro.hasPermission name="admin:adPosition"]
					<li>
						<a href="../ad_position/list.jhtml" target="iframe">${message("admin.main.adPosition")}</a>
					</li>
				[/@shiro.hasPermission]
				[@shiro.hasPermission name="admin:ad"]
					<li>
						<a href="../ad/list.jhtml" target="iframe">${message("admin.main.ad")}</a>
					</li>
				[/@shiro.hasPermission]
				[@shiro.hasPermission name="admin:template"]
					<li>
						<a href="../template/list.jhtml" target="iframe">${message("admin.main.template")}</a>
					</li>
				[/@shiro.hasPermission]
				[@shiro.hasPermission name="admin:theme"]
					<li>
						<a href="../theme/setting.jhtml" target="iframe">${message("admin.main.theme")}</a>
					</li>
				[/@shiro.hasPermission]
				[@shiro.hasPermission name="admin:cache"]
					<li>
						<a href="../cache/clear.jhtml" target="iframe">${message("admin.main.cache")}</a>
					</li>
				[/@shiro.hasPermission]
				[@shiro.hasPermission name="admin:static"]
					<li>
						<a href="../static/generate.jhtml" target="iframe">${message("admin.main.static")}</a>
					</li>
				[/@shiro.hasPermission]
				[@shiro.hasPermission name="admin:index"]
					<li>
						<a href="../index/generate.jhtml" target="iframe">${message("admin.main.index")}</a>
					</li>
				[/@shiro.hasPermission]
	          </ul>
          </li>
          <li>
	          <a class="accordion-toggle" href="#">
		          <span class="glyphicon glyphicon-tower"></span>
		          <span class="sidebar-title">${message("admin.main.marketingGroup")}</span>
		          <span class="caret"></span>
	          </a>
	          <ul class="nav sub-nav">
		        [@shiro.hasPermission name="admin:promotion"]
					<li>
						<a href="../promotion/list.jhtml" target="iframe">${message("admin.main.promotion")}</a>
					</li>
				[/@shiro.hasPermission]
				[@shiro.hasPermission name="admin:coupon"]
					<li>
						<a href="../coupon/list.jhtml" target="iframe">${message("admin.main.coupon")}</a>
					</li>
				[/@shiro.hasPermission]
				[@shiro.hasPermission name="admin:seo"]
					<li>
						<a href="../seo/list.jhtml" target="iframe">${message("admin.main.seo")}</a>
					</li>
				[/@shiro.hasPermission]
				[@shiro.hasPermission name="admin:sitemap"]
					<li>
						<a href="../sitemap/generate.jhtml" target="iframe">${message("admin.main.sitemap")}</a>
					</li>
				[/@shiro.hasPermission]
	          </ul>
          </li>
          <li>
	          <a class="accordion-toggle" href="#">
		          <span class="glyphicon glyphicon-duplicate"></span>
		          <span class="sidebar-title">${message("admin.main.statisticGroup")}</span>
		          <span class="caret"></span>
	          </a>
          	<ul class="nav sub-nav">
          		[@shiro.hasPermission name="admin:statistics"]
					<li>
						<a href="../statistics/view.jhtml" target="iframe">${message("admin.main.statistics")}</a>
					</li>
				[/@shiro.hasPermission]
				[@shiro.hasPermission name="admin:statistics"]
					<li>
						<a href="../statistics/setting.jhtml" target="iframe">${message("admin.main.statisticsSetting")}</a>
					</li>
				[/@shiro.hasPermission]
				[@shiro.hasPermission name="admin:memberStatistic"]
					<li>
						<a href="../member_statistic/list.jhtml" target="iframe">${message("admin.main.memberStatistic")}</a>
					</li>
				[/@shiro.hasPermission]
				[@shiro.hasPermission name="admin:orderStatistic"]
					<li>
						<a href="../order_statistic/list.jhtml" target="iframe">${message("admin.main.orderStatistic")}</a>
					</li>
				[/@shiro.hasPermission]
				[@shiro.hasPermission name="admin:memberRanking"]
					<li>
						<a href="../member_ranking/list.jhtml" target="iframe">${message("admin.main.memberRanking")}</a>
					</li>
				[/@shiro.hasPermission]
				[@shiro.hasPermission name="admin:goodsRanking"]
					<li>
						<a href="../goods_ranking/list.jhtml" target="iframe">${message("admin.main.goodsRanking")}</a>
					</li>
				[/@shiro.hasPermission]
          	</ul>
          </li>
          <li>
	          <a class="accordion-toggle" href="#">
		          <span class="fa fa-gears"></span>
		          <span class="sidebar-title">${message("admin.main.systemGroup")}</span>
		          <span class="caret"></span>
	          </a>
	          <ul class="nav sub-nav">
	          	[@shiro.hasPermission name="admin:setting"]
					<li>
						<a href="../setting/edit.jhtml" target="iframe">${message("admin.main.setting")}</a>
					</li>
				[/@shiro.hasPermission]
				[@shiro.hasPermission name="admin:area"]
					<li>
						<a href="../area/list.jhtml" target="iframe">${message("admin.main.area")}</a>
					</li>
				[/@shiro.hasPermission]
				[@shiro.hasPermission name="admin:paymentMethod"]
					<li>
						<a href="../payment_method/list.jhtml" target="iframe">${message("admin.main.paymentMethod")}</a>
					</li>
				[/@shiro.hasPermission]
				[@shiro.hasPermission name="admin:shippingMethod"]
					<li>
						<a href="../shipping_method/list.jhtml" target="iframe">${message("admin.main.shippingMethod")}</a>
					</li>
				[/@shiro.hasPermission]
				[@shiro.hasPermission name="admin:deliveryCorp"]
					<li>
						<a href="../delivery_corp/list.jhtml" target="iframe">${message("admin.main.deliveryCorp")}</a>
					</li>
				[/@shiro.hasPermission]
				[@shiro.hasPermission name="admin:paymentPlugin"]
					<li>
						<a href="../payment_plugin/list.jhtml" target="iframe">${message("admin.main.paymentPlugin")}</a>
					</li>
				[/@shiro.hasPermission]
				[@shiro.hasPermission name="admin:storagePlugin"]
					<li>
						<a href="../storage_plugin/list.jhtml" target="iframe">${message("admin.main.storagePlugin")}</a>
					</li>
				[/@shiro.hasPermission]
				[@shiro.hasPermission name="admin:loginPlugin"]
					<li>
						<a href="../login_plugin/list.jhtml" target="iframe">${message("admin.main.loginPlugin")}</a>
					</li>
				[/@shiro.hasPermission]
				[@shiro.hasPermission name="admin:admin"]
					<li>
						<a href="../admin/list.jhtml" target="iframe">${message("admin.main.admin")}</a>
					</li>
				[/@shiro.hasPermission]
				[@shiro.hasPermission name="admin:permission"]
					<li>
						<a href="../permission/list.jhtml" target="iframe">${message("admin.main.permission")}</a>
					</li>
				[/@shiro.hasPermission]
				[@shiro.hasPermission name="admin:role"]
					<li>
						<a href="../role/list.jhtml" target="iframe">${message("admin.main.role")}</a>
					</li>
				[/@shiro.hasPermission]
				[@shiro.hasPermission name="admin:message"]
					<li>
						<a href="../message/send.jhtml" target="iframe">${message("admin.main.send")}</a>
					</li>
				[/@shiro.hasPermission]
				[@shiro.hasPermission name="admin:message"]
					<li>
						<a href="../message/list.jhtml" target="iframe">${message("admin.main.message")}</a>
					</li>
				[/@shiro.hasPermission]
				[@shiro.hasPermission name="admin:message"]
					<li>
						<a href="../message/draft.jhtml" target="iframe">${message("admin.main.draft")}</a>
					</li>
				[/@shiro.hasPermission]
				[@shiro.hasPermission name="admin:log"]
					<li>
						<a href="../log/list.jhtml" target="iframe">${message("admin.main.log")}</a>
					</li>
				[/@shiro.hasPermission]
	          </ul>
          </li>
        </ul>
      </div>
      <!-- End: Sidebar Left Content -->

    </aside>
    <!-- End: Sidebar Left -->

    <!-- Start: Content-Wrapper -->
    <section id="content_wrapper">
    	<iframe id="iframe" name="iframe" src="index.jhtml" frameborder="0" style="width:100%;" onLoad="iFrameHeight()"></iframe>
    </section>
    <!-- End: Content-Wrapper -->

    <!-- Start: Right Sidebar -->
  </div>
  <!-- End: Main -->

  <!-- jQuery -->
  <script src="${base}/vendor/jquery/jquery-1.11.1.min.js"></script>
  <script src="${base}/vendor/jquery/jquery_ui/jquery-ui.min.js"></script>

  <!-- Theme Javascript -->
  <script src="${base}/assets/js/utility/utility.js"></script>

  <script src="${base}/assets/js/main.js"></script>

  <script type="text/javascript">
  jQuery(document).ready(function() {

    // Init Theme Core      
    Core.init();
    
    // iframe高度自适应的方法
    /* function setIframeHeight(iframe) {
        if (iframe) {
            var iframeWin = iframe.contentWindow || iframe.contentDocument.parentWindow;
            if (iframeWin.document.body) {
                iframe.height = iframeWin.document.documentElement.scrollHeight || iframeWin.document.body.scrollHeight;
            }
        }
    };
    window.onload = function() {
        setIframeHeight(document.getElementById('iframe'));
    };  */
    
    
  });
  
  
	function iFrameHeight() {
		var ifm = document.getElementById("iframe");
		var subWeb = document.frames ? document.frames["iframe"].document : ifm.contentDocument;
		if (ifm != null && subWeb != null) {
			ifm.height = subWeb.body.scrollHeight;
			ifm.width = subWeb.body.scrollWidth;
		}
	}
	
	</script>

  <!-- END: PAGE SCRIPTS -->

</body>

</html>
