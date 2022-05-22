<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<html>
<head>
<meta charset="UTF-8">
<base href="<%=basePath%>">
<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bs_pagination-master/css/jquery.bs_pagination.min.css" type="text/css" rel="stylesheet" >

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="jquery/bs_pagination-master/js/jquery.bs_pagination.min.js"></script>
<script type="text/javascript" src="jquery/bs_pagination-master/localization/en.js"></script>

<script type="text/javascript">

	$(function(){

		queryTranByConditionForPage(1,5);

		$("#createTranBtn").click(function () {
			window.location.href='workbench/transaction/toSave.do';
		});

		$("#searchBtn").click(function () {
			queryTranByConditionForPage(1,$("#tran_pagination").bs_pagination('getOption', 'rowsPerPage'));
		});

		//给"全选"按钮添加单击事件
		$("#checkAll").click(function () {
			$("#tBody input[type='checkbox']").prop("checked", this.checked);
		});

		$("#tBody").on("click", "input[type='checkbox']", function () {
			//如果列表中的所有checkbox都选中，则"全选"按钮也选中
			if ($("#tBody input[type='checkbox']").size() == $("#tBody input[type='checkbox']:checked").size()) {
				$("#checkAll").prop("checked", true);
			} else {//如果列表中的所有checkbox至少有一个没选中，则"全选"按钮也取消
				$("#checkAll").prop("checked", false);
			}
		});


		//给删除按钮添加单击事件
		$("#deleteTranBtn").click(function () {


			//收集参数
			//获取列表中所有被选中的checkbox
			var checkIds = $("#tBody input[type='checkbox']:checked");
			if (checkIds.size() == 0) {
				alert("请选择要删除的市场活动");
				return;
			}
			if (window.confirm("您确定删除吗？")) {
				//处理提交参数
				var ids = "";
				$.each(checkIds, function () {
					ids += "id=" + this.value + "&";
				});
				ids = ids.substr(0, ids.length - 1);
				alert(ids);
				$.ajax({
					url: "workbench/tran/deleteTranByIds.do",
					data: ids,
					type: "post",
					dataType: "json",
					success: function (data) {
						if (data.code == '1') {
							queryTranByConditionForPage(1,
									$("#tran_pagination").bs_pagination('getOption','rowsPerPage'));
						} else {
							alert(data.message);
						}
					}
				});
			}
		});

		$("#editTranBtn").click(function () {
			var checkId=$("#tBody input[type='checkbox']:checked");
			if(checkId.size()==0){
				alert("请选择一个需要修改的");
				return;
			}
			if(checkId.size()>1){
				alert("只能选择一个需要修改的");
				return;
			}

			var id=checkId.val();
			window.location.href="workbench/transaction/toEdit.do?id="+id;

		});
		
	});
	function queryTranByConditionForPage(pageNo,pageSize) {
		//收集前端查询需要的条件参数
		var owner = $("#query-owner").val();
		var name=$("#query-name").val();
		var customerName=$("#query-customerName").val();
		var stage = $("#query-stage").val();
		var type = $("#query-type").val();
		var source = $("#query-source").val();
		var contactsName = $("#query-contactsName").val();
		// var pageNo =1;
		// var pageSize=10;
		var html="";
		$.ajax({
			url:"workbench/tran/queryTranByConditionForPage.do",
			data: {
				owner:owner,
				name:name,
				customerName:customerName,
				stage:stage,
				type:type,
				source:source,
				contactsName:contactsName,
				pageNo:pageNo,
				pageSize:pageSize
			},
			type:"post",
			dataType: "json",
			success:function (data) {
				//显示总条数
				//$("#totalRowsB").text(data.totalRows);
				//id":"c3dcde6a2777422d9d2b3104967ee90a","owner":"李四","money":null,"name":"交易测试01","expectedDate":null,"customerId":null,"stage":"需求分析","type":"新业务","source":"内部研讨会","activityId":null,"contactsId":"马云",
				$.each(data.tranList,function (index,n) {
					html+='<tr class="active">',
							html+='<td><input type="checkbox" name="xz" value="'+n.id+'"/></td>',
							html+='<td><a style="text-decoration: none; cursor: pointer;"onclick="window.location.href=\'workbench/transaction/detailTran.do?id='+n.id+'\';">'+n.name+'</a></td>',
							html+='<td>'+n.customerId+'</td>',
							html+='<td>'+n.stage+'</td>',
							html+='<td>'+n.type+'</td>',
							html+='<td>'+n.owner+'</td>',
							html+='<td>'+n.source+'</td>',
							html+='<td>'+n.contactsId+'</td>',
							html+='</tr>';
				})
				$("#tBody").html(html)

				//分页后取消全选
				$("#checkAll").prop("checked",false);
				//计算总页数
				var totalPages=1;
				if(data.totalRows%pageSize==0){
					totalPages=data.totalRows/pageSize;
				}else{
					totalPages=parseInt(data.totalRows/pageSize)+1;
				}
				////对容器调用bs_pagination工具函数，显示翻页信息
				$("#tran_pagination").bs_pagination({
					currentPage:pageNo,//当前页号,相当于pageNo

					rowsPerPage:pageSize,//每页显示条数,相当于pageSize
					totalRows:data.totalRows,//总条数
					totalPages: totalPages,  //总页数,必填参数.

					visiblePageLinks:5,//最多可以显示的卡片数

					showGoToPage:true,//是否显示"跳转到"部分,默认true--显示
					showRowsPerPage:true,//是否显示"每页显示条数"部分。默认true--显示
					showRowsInfo:true,//是否显示记录的信息，默认true--显示

					//用户每次切换页号，都自动触发本函数;
					//每次返回切换页号之后的pageNo和pageSize
					onChangePage: function(event,pageObj) { // returns page_num and rows_per_page after a link has clicked
						//js代码
						//alert(pageObj.currentPage);
						//alert(pageObj.rowsPerPage);
						queryTranByConditionForPage(pageObj.currentPage,pageObj.rowsPerPage);
					}
				});

			},

		});
	}
</script>
</head>
<body>

	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>交易列表</h3>
			</div>
		</div>
	</div>
	
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
	
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="query-owner">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="query-name">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">客户名称</div>
				      <input class="form-control" type="text" id="query-customerName">
				    </div>
				  </div>
				  
				  <br>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">阶段</div>
					  <select class="form-control" id="query-stage">
					  	<option></option>
					  <c:forEach items="${stageList}" var="stage">
						  <option value="${stage.id}">${stage.value}</option>
					  </c:forEach>
					  </select>
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">类型</div>
					  <select class="form-control" id="query-type">
					  	<option></option>
						  <c:forEach items="${transactionTypeList}" var="type">
							  <option value="${type.id}">${type.value}</option>
						  </c:forEach>
					  </select>
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">来源</div>
				      <select class="form-control" id="query-source">
						  <option></option>
						  <c:forEach items="${sourceList}" var="source">
							  <option value="${source.id}">${source.value}</option>
						  </c:forEach>
						</select>
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">联系人名称</div>
				      <input class="form-control" type="text" id="query-contactsName">
				    </div>
				  </div>
				  
				  <button type="button" class="btn btn-default" id="searchBtn">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 10px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" id="createTranBtn"><span
						  class="glyphicon glyphicon-plus"></span> 创建
				  </button>
				  <button type="button" class="btn btn-default" id="editTranBtn"><span
						  class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteTranBtn"><span class="glyphicon glyphicon-minus"></span> 删除
				  </button>
				</div>
				
				
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="checkAll"/></td>
							<td>名称</td>
							<td>客户名称</td>
							<td>阶段</td>
							<td>类型</td>
							<td>所有者</td>
							<td>来源</td>
							<td>联系人名称</td>
						</tr>
					</thead>
					<tbody id="tBody">

					</tbody>
				</table>
				<div id="tran_pagination"></div>
			</div>
			

			
		</div>
		
	</div>
</body>
</html>