/*
 * JavaScript - List
 * 
 */

$().ready( function() {

	var $listForm = $("#listForm");
	var $deleteButton = $("#deleteButton");
	var $refreshButton = $("#refreshButton");
    var $targetPath = $("#targetPath");
    var $targetPathItem = $("#targetPath li");
	var $pageSizeMenu = $("#pageSizeMenu");
	var $pageSizeMenuItem = $("#pageSizeMenu li");
	var $searchPropertyMenu = $("#searchPropertyMenu");
	var $searchPropertyMenuItem = $("#searchPropertyMenu li");
	var $searchValue = $("#searchValue");
	var $listTable = $("#listTable");
	var $selectAll = $("#selectAll");
	var $ids = $("#listTable input[name='ids']");
	var $contentRow = $("#listTable tr:gt(0)");
	var $sort = $("#listTable a.sort");
	var $pageSize = $("#pageSize");
	var $searchProperty = $("#searchProperty");
	var $orderProperty = $("#orderProperty");
	var $orderDirection = $("#orderDirection");
	var $pageNumber = $("#pageNumber");
   var  $nohandle=$("#nohandle");
	

	


	
	// 每页记录数菜单
	$pageSizeMenu.hover(
		function() {
			$(this).children("ul").show();
		}, function() {
			$(this).children("ul").hide();
		}
	);

	// 每页记录数
	$pageSizeMenuItem.click( function() {
		$pageSize.val($(this).attr("val"));
		$pageNumber.val("1");
		$listForm.submit();
	});
	//路径选择
    $targetPath.hover(
        function() {
            $(this).children("ul").show();
        }, function() {
            $(this).children("ul").hide();
        }
    );
    //选择路径的类型
    $targetPathItem.click( function() {
        $("#targetId").val($(this).attr("val"));
        $listForm.submit();
    });
	// 搜索项菜单
	$searchPropertyMenu.hover(
		function() {
			$(this).children("ul").show();
		}, function() {
			$(this).children("ul").hide();
		}
	);

	// 搜索项
	$searchPropertyMenuItem.click( function() {
		var $this = $(this);
		$searchProperty.val($this.attr("val"));
		$searchPropertyMenuItem.removeClass("current");
		$this.addClass("current");
	});

	// 全选
	$selectAll.click( function() {
		var $this = $(this);
		var $enabledIds = $("#listTable input[name='ids']:enabled");
		if ($this.prop("checked")) {
			$enabledIds.prop("checked", true);
			if ($enabledIds.filter(":checked").size() > 0) {
				$deleteButton.removeClass("disabled");
				$contentRow.addClass("selected");
			} else {
				$deleteButton.addClass("disabled");
			}
		} else {
			$enabledIds.prop("checked", false);
			$deleteButton.addClass("disabled");
			$contentRow.removeClass("selected");
		}
	});

	// 选择
	$ids.click( function() {


		var $this = $(this);


        $contentRow.removeClass("selected");

        $this.closest("tr").addClass("selected");

	});

	// 排序
	$sort.click( function() {
		var orderProperty = $(this).attr("name");
		if ($orderProperty.val() == orderProperty) {
			if ($orderDirection.val() == "asc") {
				$orderDirection.val("desc");
			} else {
				$orderDirection.val("asc");
			}
		} else {
			$orderProperty.val(orderProperty);
			$orderDirection.val("asc");
		}
		$pageNumber.val("1");
		$listForm.submit();
		return false;
	});

	// 排序图标
	if ($orderProperty.val() != "") {
		$sort = $("#listTable a[name='" + $orderProperty.val() + "']");
		if ($orderDirection.val() == "asc") {
			$sort.removeClass("desc").addClass("asc");
		} else {
			$sort.removeClass("asc").addClass("desc");
		}
	}

	// 页码
	$pageNumber.keypress(function(event) {
		return (event.which >= 48 && event.which <= 57) || event.which == 8 || (event.which == 13 && $(this).val().length > 0);
	});

	// 表单提交
	$listForm.submit(function() {
		if (!/^\d*[1-9]\d*$/.test($pageNumber.val())) {
			$pageNumber.val("1");
		}
		if ($searchValue.size() > 0 && $searchValue.val() != "" && $searchProperty.val() == "") {
			$searchProperty.val($searchPropertyMenuItem.first().attr("val"));
		}
	});
	
	// 页码跳转
	$.pageSkip = function(pageNumber) {


		$pageNumber.val(pageNumber);
		$listForm.submit();
		return false;
	};
	
	// 列表查询
	if (location.search != "") {
		addCookie("listQuery", location.search, {expires: 10 * 60, path: ""});
	} else {
		removeCookie("listQuery", {path: ""});
	}

});