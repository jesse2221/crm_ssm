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
			queryVisitByConditionForPage(1,5);

			$("#searchVisitBtn").click(function () {
				queryVisitByConditionForPage(1,$("#visit_pagination").bs_pagination('getOption', 'rowsPerPage'))
			});

			//定制字段
			$("#definedColumns > li").click(function(e) {
				//防止下拉菜单消失
				e.stopPropagation();
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
			$("#deleteAfterSaleBtn").click(function () {


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
					// alert(ids);
					$.ajax({
						url: "workbench/visit/deleteAfterSaleByIds.do",
						data: ids,
						type: "post",
						dataType: "json",
						success: function (data) {
							if (data.code == '1') {
								queryVisitByConditionForPage(1, $("#visit_pagination").bs_pagination('getOption','rowsPerPage'));
							} else {
								alert(data.message);
							}
						}
					});
				}
			});
});

function queryVisitByConditionForPage(pageNo,pageSize) {
			//收集前端查询需要的条件参数
			/*
            String owner,String theme,String endDate,String contactsName,
                                                       String returnState,String returnPriority,
             */
			var owner=$("#query-owner").val();
			var theme = $("#query-theme").val();
			var endDate = $("#query-endDate").val();
			var contactsName = $("#query-contactsName").val();
			var returnState = $("#query-returnState").val();
			var returnPriority = $("#query-returnPriority").val();
			// var pageNo =1;
			// var pageSize=10;
			var html="";
			$.ajax({
				url:"workbench/visit/queryVisitByConditionForPage.do",
				data: {
					owner:owner,
					theme:theme,
					endDate:endDate,
					contactsName:contactsName,
					returnState:returnState,
					returnPriority:returnPriority,
					pageNo:pageNo,
					pageSize:pageSize
				},
				type:"post",
				dataType: "json",
				success:function (data) {
					//显示总条数
					//$("#totalRowsB").text(data.totalRows);
					$.each(data.afterSalesList,function (index,n) {
						html+='<tr class="active">',
								html+='<td><input type="checkbox" name="xz" value="'+n.id+'"/></td>',
								html+='<td><a style="text-decoration: none; cursor: pointer;"onclick="window.location.href=\'workbench/visit/queryAfterSaleForDetail.do?id='+n.id+'\';">'+n.theme+'</a></td>',
								html+='<td>'+n.endDate+'</td>',
								html+='<td>'+n.contactsId+'</td>',
								html+='<td>'+n.returnState+'</td>',
								html+='<td>'+n.returnPriority+'</td>',
								html+='<td>'+n.owner+'</td>',
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
					$("#visit_pagination").bs_pagination({
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
							queryVisitByConditionForPage(pageObj.currentPage,pageObj.rowsPerPage);
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
			<h3>任务列表</h3>
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
						<select class="form-control" id="query-owner">
							<option></option>
							<c:forEach items="${userList}" var="u">
								<option value="${u.id}">${u.name}</option>
							</c:forEach>
						</select>
					</div>
				</div>

				<div class="form-group">
					<div class="input-group">
						<div class="input-group-addon">主题</div>
						<input class="form-control" type="text" id="query-theme">
					</div>
				</div>

				<div class="form-group">
					<div class="input-group">
						<div class="input-group-addon">到期日期</div>
						<input class="form-control" type="text" id="query-endDate">
					</div>
				</div>

				<div class="form-group">
					<div class="input-group">
						<div class="input-group-addon">联系人</div>
						<input class="form-control" type="text" id="query-contactsName">
					</div>
				</div>

				<br>

				<div class="form-group">
					<div class="input-group">
						<div class="input-group-addon">状态</div>
						<select class="form-control" id="query-returnState">
							<option></option>
							<c:forEach items="${returnStateList}" var="s">
								<option value="${s.id}">${s.value}</option>
							</c:forEach>
						</select>
					</div>
				</div>

				<div class="form-group">
					<div class="input-group">
						<div class="input-group-addon">优先级</div>
						<select class="form-control" id="query-returnPriority">
							<option></option>
							<c:forEach items="${returnPriorityList}" var="p">
								<option value="${p.id}">${p.value}</option>
							</c:forEach>
						</select>
					</div>
				</div>

				<button type="button" class="btn btn-default" id="searchVisitBtn">查询</button>

			</form>
		</div>
		<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
			<div class="btn-group" style="position: relative; top: 18%;">
				<button type="button" class="btn btn-primary" onclick="window.location.href='workbench/visvit/save.do';"><span class="glyphicon glyphicon-plus"></span> 任务</button>
				<button type="button" class="btn btn-default" onclick="alert('可以自行实现对通话的管理');"><span class="glyphicon glyphicon-plus"></span> 通话</button>
				<button type="button" class="btn btn-default" onclick="window.location.href='editTask.html';"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				<button type="button" class="btn btn-danger" id="deleteAfterSaleBtn"><span class="glyphicon glyphicon-minus"></span> 删除
				</button>
			</div>

		</div>
		<div style="position: relative;top: 10px;">
			<table class="table table-hover">
				<thead>
				<tr style="color: #B3B3B3;">
					<td><input type="checkbox" id="checkAll"/></td>
					<td>主题</td>
					<td>到期日期</td>
					<td>联系人</td>
					<td>状态</td>
					<td>优先级</td>
					<td>所有者</td>
				</tr>
				</thead>
				<tbody id="tBody">

				</tbody>
			</table>
		</div>
		<br>
		<div id="visit_pagination"></div>

	</div>

</div>
</body>
</html>