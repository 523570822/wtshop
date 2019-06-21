<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <title>Gebo Admin v3.1</title>

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

        <!-- upload -->
            <link rel="stylesheet" href="${base}/gebo3/lib/plupload/js/jquery.plupload.queue/css/plupload-gebo.css" />
		<!-- tag handler -->
            <link rel="stylesheet" href="${base}/gebo3/lib/tag_handler/css/jquery.taghandler.css" />

        <!-- main styles -->
            <link rel="stylesheet" href="${base}/gebo3/css/style.css" />
		<!-- theme color-->
            <link rel="stylesheet" href="${base}/gebo3/css/blue.css" id="link_theme" />
        <!-- layui -->
            <link rel="stylesheet" href="${base}/resources/admin/laypage/skin/laypage.css" />
            <link rel="stylesheet" href="${base}/resources/admin/layui/css/layui.css" />

            <!-- <link href='../fonts.googleapis.com/css@family=PT+Sans' rel='stylesheet' type='text/css'> -->

        <!-- favicon -->
            
            

        <!--[if lte IE 8]>
            <link rel="stylesheet" href="css/ie.css" />
        <![endif]-->

        <!--[if lt IE 9]>
			<script src="js/ie/html5.js"></script>
			<script src="js/ie/respond.min.js"></script>
			<script src="lib/flot/excanvas.min.js"></script>
        <![endif]-->    </head>
    <body class="full_width">
       <div id="maincontainer" class="clearfix">
            <div id="contentwrapper">
                <div class="main_content" style="margin-left: 0px; padding: 0px;">
                	<div id="jCrumbs" class="breadCrumb module" style="margin: 0px;">
					    <ul>
					        <li>
					            <a href="${base}/admin/common/index.jhtml"><i class="glyphicon glyphicon-home"></i></a>
					        </li>
					        <li>
					            <a href="#">${message("admin.breadcrumb.home")}</a>
					        </li>
					        <li>
					            ${message("admin.brand.list")}
					        </li>
					    </ul>
					</div>
					<!-- <div style="margin: 5px;">
						<a href="add.jhtml" class="btn btn-default btn-sm"><i class="splashy-add"></i> ${message("admin.common.add")} </a>
						<div class="btn-group">
							<a href="javascript:;" id="deleteButton" class="btn btn-default btn-sm" ><i class="splashy-remove"></i> ${message("admin.common.delete")} </a>
							<a href="javascript:;" id="refreshButton" class="btn btn-default btn-sm"><i class="splashy-refresh_backwards"></i> ${message("admin.common.refresh")} </a>
							<div class="btn-group">
								<a href="javascript:;" data-toggle="dropdown" class="btn dropdown-toggle btn-default btn-sm" aria-expanded="false">${message("admin.page.pageSize")} <span class="caret"></span></a>
								<ul class="dropdown-menu">
									<li val="10">10</a></li>
									<li val="20">20</a></li>
									<li val="50">50</a></li>
									<li val="100">100</a></li>
								</ul>
							</div>
						</div>
					</div> -->
					
					<div class="layui-btn-group" style="margin: 5px;">
					  <button class="layui-btn layui-btn-small">
					    <i class="layui-icon">&#xe654;</i>
					  </button>
					  <button class="layui-btn layui-btn-small">
					    <i class="layui-icon">&#xe640;</i>
					  </button>
					</div>
					
					<div class="row">
					    <div class="col-sm-12 col-md-12">
							<div class="mbox">
								<div class="tabbable">
									<div class="tab-content">
										<table id="dt_inbox" class="table table_vam mbox_table " data-msg_rowlink="a">
											<thead>
												<tr>
													<th class="table_checkbox"><input type="checkbox" name="select_msgs" class="select_msgs" data-tableid="dt_inbox"></th>
													<th>${message("Brand.name")}</th>
													<th>${message("Brand.logo")}</th>
													<th>${message("Brand.url")}</th>
													<th>${message("admin.common.order")}</th>
													<th>${message("admin.common.action")}</th>
												</tr>
											</thead>
											<tbody>
											[#list page.list as brand]
												<tr>
													<td class="nohref"><input type="checkbox" name="msg_sel" class="select_msg"></td>
													<!-- <td class="nohref"><input type="checkbox" name="ids" value="${brand.id}" /></td> -->
													<td>${brand.name}</td>
													<td>
														[#if brand.typeName == "image"]
															<a href="${brand.logo}" target="_blank">${message("admin.common.view")}</a>
														[#else]
															-
														[/#if]
													</td>
													<td>
														[#if brand.url??]
															<a href="${brand.url}" target="_blank">${brand.url}</a>
														[#else]
															-
														[/#if]
													</td>
													<td>${brand.orders}</td>
													<td>
														<a href="edit.jhtml?id=${brand.id}">[${message("admin.common.edit")}]</a>
														<a href="${base}${brand.path}" target="_blank">[${message("admin.common.view")}]</a>
													</td>
												</tr>
											[/#list]
											</tbody>
										</table>	
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="sepH_a" id="pagination"></div>   
					    
					<!-- hide elements-->
					<div class="hide">
					    <!-- actions for inbox -->
					    <div class="dt_inbox_actions">
							<div class="btn-group">
								<a href="javascript:void(0)" class="btn btn-default btn-sm" title="Answer"><i class="glyphicon glyphicon-pencil"></i></a>
								<a href="javascript:void(0)" class="btn btn-default btn-sm" title="Forward"><i class="glyphicon glyphicon-share-alt"></i></a>
								<a href="#" class="delete_msg btn btn-default btn-sm" title="Delete" data-tableid="dt_inbox"><i class="glyphicon glyphicon-trash"></i></a>
							</div>
					    </div>
						<!-- actions for outbox -->
					    <div class="dt_outbox_actions">
							<div class="btn-group">
								<a href="javascript:void(0)" class="btn btn-default btn-sm" title="Resend"><i class="glyphicon glyphicon-share-alt"></i></a>
								<a href="#" class="delete_msg btn btn-default btn-sm" title="Delete" data-tableid="dt_outbox"><i class="glyphicon glyphicon-trash"></i></a>
							</div>
					    </div>
					    <!-- actions for trash -->
					    <div class="dt_trash_actions">
							<div class="btn-group">
								<a href="#" class="delete_msg btn btn-default btn-sm" title="Delete permamently" data-tableid="dt_trash"><i class="glyphicon glyphicon-trash"></i></a>
							</div>
					    </div>
					    
					    <!-- confirmation box -->
					    <div id="confirm_dialog" class="cbox_content">
					        <div class="sepH_c tac"><strong>Are you sure you want to delete this message(s)?</strong></div>
					        <div class="tac">
					            <a href="#" class="btn confirm_yes btn-sm btn-default">Yes</a>
					            <a href="#" class="btn confirm_no btn-sm btn-link">No</a>
					        </div>
					    </div>
					</div>     
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

	<!-- datatable (inbox,outbox) -->
	<script src="${base}/gebo3/lib/datatables/jquery.dataTables.min.js"></script>
    <!-- additional sorting for datatables -->
    <script src="${base}/gebo3/lib/datatables/jquery.dataTables.sorting.js"></script>
    <!-- datatables bootstrap integration -->
    <script src="${base}/gebo3/lib/datatables/jquery.dataTables.bootstrap.min.js"></script>
    <!-- plupload and all it's runtimes and the jQuery queue widget (attachments) -->
    <script type="text/javascript" src="${base}/gebo3/lib/plupload/js/plupload.full.js"></script>
    <script type="text/javascript" src="${base}/gebo3/lib/plupload/js/jquery.plupload.queue/jquery.plupload.queue.full.js"></script>
    <!-- autosize textareas (new message) -->
	<script src="${base}/gebo3/js/forms/jquery.autosize.min.js"></script>
    <!-- tag handler (recipients) -->
	<script src="${base}/gebo3/lib/tag_handler/jquery.taghandler.min.js"></script>
	 <!-- layui -->
	<script src="${base}/resources/admin/layui/layui.js"></script>
	<script src="${base}/resources/admin/laypage/laypage.js"></script>
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
    
    <script>
	    //好像很实用的样子，后端的同学再也不用写分页逻辑了。
		laypage({
		  cont: 'pagination',
		  pages: ${page.totalPage}, //可以叫服务端把总页数放在某一个隐藏域，再获取。假设我们获取到的是18
		  skin: 'molv', //皮肤
		  skip: true, //是否开启跳页
		  curr: function(){ //通过url获取当前页，也可以同上（pages）方式获取
		    var page = ${page.pageNumber};
		    return ${page.pageNumber} || 1;
		  }(), 
		  jump: function(e, first){ //触发分页后的回调
		    if(!first){ //一定要加此判断，否则初始时会无限刷新
		      location.href = '?pageable.pageNumber=' + e.curr;
		    }
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
