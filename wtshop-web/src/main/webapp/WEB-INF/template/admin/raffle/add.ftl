[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <title>添加福袋 - Powered By ${setting.siteAuthor}</title>
    <meta name="author" content="${setting.siteAuthor}" />
    <meta name="copyright" content="${setting.siteCopyright}" />
    <link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
    <link href="${base}/statics/lib/layer/mobile/need/layer.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.tools.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.validate.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/webuploader.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/ueditor/ueditor.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/input.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/datePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="${base}/statics/lib/layer/layer.js"></script>

    <script type="text/javascript">

        $().ready(function() {

            var $inputForm = $("#inputForm");
            var $type = $("#type");
            var $content = $("#content");
            var $path = $("#path");
            var $filePicker = $("#filePicker");
            $("#addProduct").click(function() {
                layer.open({
                    title:"商品列表",
                    type: 2,
                    skin: 'layui-layer-rim', //加上边框
                    area: ['870px', '540px'], //宽高
                    content: "${base}/admin/reverseAuction/chooseGoods.jhtml?flag=2",
                    shadeClose:true,
                });
            });
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
                    "activity.title":"required",
                    "activity.number":"required",
                    "activity.phone": "required",
                    "product_name":"required",
                    "activity.beginDate":"required",
                    "activity.endDate":"required"

                }
            });

            var $introduction = $("#introduction");
            $introduction.editor();

            var $productImageTable = $("#productImageTable");
            var productImageIndex = ${(fuDai.productImagesConverter?size)!0};
            var $addProductImage = $("#addProductImage");





  
        });

function  inputForm() {


    var beginDate=$("#beginDate").val();
    var endDate=$("#endDate").val();
    var d1 = new Date(beginDate.replace(/\-/g, "\/"));
    var d2 = new Date(endDate.replace(/\-/g, "\/"));

    if(beginDate!=""&&endDate!=""&&d1 >=d2) {
        var d="开始时间不能大于结束时间！";
        layer.open({
            content: d,skin: 'msg'
            ,time: 1000//2秒后自动关闭
        });
    }else{
        $("#inputForm").submit();
    }





}
    </script>
</head>
<body>
<div class="breadcrumb">
    <a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo;${message("Fudai.add.llst")}
</div>
<form id="inputForm" action="save.jhtml" method="post"  enctype="multipart/form-data">
    <ul id="tab" class="tab">
        <li>
            <input type="button" value="${message("admin.coupon.base")}"class="current" />
        </li>
        <li>
            <input type="button" value="${message("Activity.rule")}" />
        </li>
       [#-- <li>
            <input type="button" value="${message("admin.goods.productImage")}" />
        </li>--]
        </ul>
    <input value="0" name="activity.status" type="hidden"/>
    <input value="0" name="fuDai.status" type="hidden"/>

    <table class="input tabContent">

        <tr>
            <th>
                <span class="requiredField">*</span>${message("Ad.title")}:
            </th>
            <td>

                <input type="text" value="${activity.opporName}" name="activity.opporName" class="text" maxlength="200"  />
            </td>
        </tr>

        <tr>
            <th>
                <span class="requiredField">*</span>${message("Activity.number")}:
            </th>
            <td>
                <input type="text"  value="${activity.number}"  name="activity.number" class="text" maxlength="200" 	min=0 title=${message("Activity.success.title")} />
            </td>
        </tr>
        <tr>
            <th>
                <span class="requiredField">*</span>${message("Activity.luckyNumber")}:
            </th>
            <td>
                <input type="text"  value="${activity.luckyNumber}"  name="activity.luckyNumber" class="text" maxlength="200" 	min=0 title=${message("Activity.luckyNumber.title")} />
            </td>
        </tr>
        <tr>
            <th>
                <span class="requiredField">*</span>${message("Activity.phone")}:
            </th>
            <td>
                <input type="text" value="${activity.phone}" name="activity.phone" class="text" maxlength="200" title=${message("Activity.success.phone")}  />
            </td>
        </tr>

        <tr>
            <th>
                ${message("Footprint.beginTime")}:
            </th>
            <td>
             <input  class="hidden"   id="stime" value="${stime?string('yyyy-MM-dd HH:mm:ss')}"/>
                <input  class="hidden" id="etime" value="${etime?string('yyyy-MM-dd HH:mm:ss')}"/>
                <input type="text" id="beginDate" name="activity.beginDate"
                       value="${beginDate?string('yyyy-MM-dd HH:mm:ss')}"
                       class="text Wdate begin_date" onclick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'stime\')}'});"  />
             <input type="text" id="endDate" name="activity.endDate"
                       value="${endDate?string('yyyy-MM-dd HH:mm:ss')}"
                       class="text Wdate end_date" onclick=" WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'beginDate\')}'}); " />


            </td>
        </tr>


        <tr>
            <th>
                <span class="requiredField"></span>${message("Activity.message")}:
            </th>
            <td>
                <textarea rows="" cols="" value="${activity.explain}" name="activity.explain" style="width: 300px;height:200px " maxlength="400" title= ${message("fudai.message.title")}></textarea>
            </td>
        </tr>



    </table>
    <table class="input tabContent">
        <tr><td><textarea id="introduction" value="${activity.rule}" name="activity.rule" class="editor" style="width: 100%;"></textarea></td></tr>
    </table>

[#--    <table id="productImageTable" class="item tabContent">
        <tr>
            <td colspan="4">
                <a href="javascript:;" id="addProductImage" title= ${message("fudai.image.title")} class="button">${message("admin.goods.addProductImage")}</a>
            </td>
        </tr>
        <tr>
            <th>
            ${message("ProductImage.file")}
            </th>
            <th>
            ${message("ProductImage.title")}
            </th>
            <th>
            ${message("admin.common.order")}
            </th>
            <th>
            ${message("admin.common.action")}
            </th>
        </tr>



    </table>--]
    <table class="input">
        <tr>
            <th>&nbsp;

            </th>
            <td>
            <input type="button" onclick="inputForm()" class="button" value="${message("admin.common.submit")}" />
            <input type="button" class="button" value="${message("admin.common.back")}" onclick="history.back(); return false;" />
        </td>
        </tr>
    </table>



</form>
</body>
</html>
[/#escape]