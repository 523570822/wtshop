[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
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
    [@flash_message /]

});
</script>
</head>
<body>
	<div class="breadcrumb">
		<a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; ${message("admin.coupon.add")}
	</div>
	<form id="inputForm" action="update.jhtml" method="post">
        <input class="hidden" name ="ticketconfig.id"  value="${ticketconfig.id}"/>
        <ul id="tab" class="tab">
            <li>
                <input type="button" value="基本信息" class="current">
            </li>
        </ul>
        <table class="input tabContent">
            <tr>
                <th>
                    <span class="requiredField">*</span>:${message("ticketConfig.title")}
                </th>
                <td>
                    <input type="text" name="ticketconfig.title" class="text" value="${ticketconfig.title}" />
                </td>
            </tr>
            <tr>
                <th>
                    ${message("ticketConfig.content")}
                </th>
                <td>
                    <input type="text" name="ticketconfig.content" class="text"   value="${ticketconfig.content}"/>
                </td>
            </tr>
            <tr>
                <th>
                ${message("ticketConfig.cqkcqsl")}
                </th>
                <td>
                    <input type="text" name="ticketconfig.num" class="text"  value="${ticketconfig.num}" />
                </td>
            </tr>

            <tr>
                <th>
                ${message("ticketConfig.yxzdlqts")}
                </th>
                <td>
                    <input type="text" name="ticketconfig.maxReceiveDay" class="text"  value="${ticketconfig.maxReceiveDay}" />
                </td>
            </tr>

            <tr>
                <th>
                ${message("ticketConfig.fxcqxz")}
                </th>
                <td>
                    <input type="text" name="ticketconfig.shareLimit" class="text"  value="${ticketconfig.shareLimit}" />
                </td>
            </tr>


            <tr>
                <th>
                ${message("Promotion.beginDate")}:
                </th>
                <td>
                    <input type="text" id="begin_date" name="ticketconfig.beginTime" value="${ticketconfig.beginTime?string('yyyy-MM-dd HH:mm:ss')}" class="text Wdate begin_date" onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'});" />
                </td>
            </tr>
            <tr>
                <th>
                ${message("Promotion.endDate")}:
                </th>
                <td>
                    <input type="text" id="endDate" name="ticketconfig.endTime" value="${ticketconfig.endTime?string('yyyy-MM-dd HH:mm:ss')}" class="text Wdate end_date" onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'});" />
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
    {{# for(var i = 0, len = ticketconfig.length; i< len; i++){  var node= d[i];    }} <tr>
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