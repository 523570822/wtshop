[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>优惠券分享- Powered By ${setting.siteAuthor}</title>
<meta name="author" content="${setting.siteAuthor}" />
<meta name="copyright" content="${setting.siteCopyright}" />
<link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.tools.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/input.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/layer/layer.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/layer/laytpl.js"></script>
<script type="text/javascript" src="${base}/resources/admin/datePicker/WdatePicker.js"></script>
<script type="text/javascript">
$().ready(function() {

        $('#selCoupon').click(function () {
                $.get('getCouponList.jhtml',{},function (d) {
                    console.log(d);
                    laytpl($('#listTmpl').html()).render(d, function(html) {
                        $('#listPack').html(html);
                        customopen_nobuton('listPack','800px','500px');
                    });
                })
        })

        $(document).on('click','.sel',function () {
            $('#couponId').val( $(this).parent().attr("nodeid"));
            $('#couponTxt').text( $(this).parent().parent().find('.name').text());
            layer.closeAll();
        })
});
</script>
</head>
<body>
	<div class="breadcrumb">
		<a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; ${message("admin.coupon.add")}
	</div>
	<form id="inputForm" action="save.jhtml" method="post">
        <ul id="tab" class="tab">
            <li>
                <input type="button" value="基本信息" class="current">
            </li>
        </ul>
        <table class="input tabContent">
            <tr>
                <th>
                    <span class="requiredField">*</span>:标题
                </th>
                <td>
                    <input type="text" name="couponShare.title" class="text" value="" />
                </td>
            </tr>
            <tr>
                <th>
                    内容
                </th>
                <td>
                    <input type="text" name="couponShare.content" class="text"  />
                </td>
            </tr>
            <tr>
                <th>
                        选择优惠券
                </th>
                <td>
                    <input type="button" class="button" value="选择优惠券" id="selCoupon">
                    <input type="hidden"  id="couponId" name="couponShare.couponId" class="text"  />
                    <span id="couponTxt"></span>
                </td>
            </tr>
            <tr>
                <th>
                    最大领取数量
                </th>
                <td>
                    <input type="text" name="couponShare.numLimit" class="text"  />
                </td>
            </tr>
            <tr>
                <th>
                    优惠券分享页面跳转链接(一般默认即可)
                </th>
                <td>
                    <input type="text" name="couponShare.link" class="text"  value="/act/couoinShare.jhtml" />
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

        <div id="listPack" class="hidden"></div>

	</form>
</body>
</html>

<script id="listTmpl" type="text/html">
    <table class="input tabContent" style="font-size: 14px">
    <tr class="bt">
        <td style="width: 20%;" >名称</td>
        <td style="width: 20%;">前缀</td>
        <td style="width: 20%;">使用起始日期</td>
        <td style="width: 20%;">使用结束日期</td>
        <td style="width: 20%;">操作</td>
    </tr>
    {{# for(var i = 0, len = d.length; i< len; i++){  var node= d[i];    }} <tr>
        <td style="width: 20%;" class="name">{{node.name}}</td>
        <td style="width: 20%;">{{node.prefix}}</td>
        <td style="width: 20%;">{{node.begin_date}}</td>
        <td style="width: 20%;">{{node.end_date}}</td>
        <td nodeid={{node.id}} style="width: 20%;">
                <a class="sel">[选择]</a>
        </td>
    </tr>
    {{# } }}
    </table>
</script>
[/#escape]