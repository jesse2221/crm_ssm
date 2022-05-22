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
		queryCustomerByConditionForPage(1,10);

		//定制字段
		$("#definedColumns > li").click(function(e) {
			//防止下拉菜单消失
	        e.stopPropagation();
	    });

		$("#CreateCustomerBtn").click(function () {
			$("#createCustomerModal").modal("show");
		});

		//保存客户
		$("#saveCreateCustomerBtn").click(function () {
			//收集参数
			var owner = $("#create-customerOwner").val();
			var name =$("#create-customerName").val();
			var website=$("#create-website").val();
			var phone=$("#create-phone").val();
			var description = $("#create-description").val();
			var contactSummary=$("#create-contactSummary").val();
			var nextContactTime=$("#create-nextContactTime").val();
			var address=$("#create-address1").val();
			//表单验证
			$.ajax({
				url:"workbench/customer/saveCreateCustomer.do",
				data:{
					owner:owner,
					name:name,
					website:website,
					phone:phone,
					description:description,
					contactSummary:contactSummary,
					nextContactTime:nextContactTime,
					address:address
				},
				type:"post",
				dataType:"json",
				success:function (data) {
					if(data.code=="1"){
						//刷新页面
						queryCustomerByConditionForPage(1,10);
						//关闭模态窗口
						$("#createCustomerModal").modal("hide");
					}else {
						alert(data.message);
						$("#createCustomerModal").modal("show");
					}
				}
			});
		});

		//条件查询
		$("#searchCustomerBtn").click(function () {
			queryCustomerByConditionForPage(1,$("#customer_pagination").bs_pagination('getOption', 'rowsPerPage'))
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
		$("#deleteCustomerBtn").click(function () {
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
					url: "workbench/customer/deleteCustomerByIds.do",
					data: ids,
					type: "post",
					dataType: "json",
					success: function (data) {
						if (data.code == '1') {
							queryCustomerByConditionForPage(1, $("#customer_pagination").bs_pagination('getOption','rowsPerPage'));
						} else {
							alert(data.message);
						}
					}
				});
			}
		});

		$("#editCustomerBtn").click(function () {
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
			$.ajax({
				url:"workbench/customer/queryCustomerById.do",
				data:{
					id:id
				},
				type:"post",
				dataType:"json",
				success:function (data) {
					//把市场活动的信息显示在修改的模态窗口上
					$("#edit-id").val(data.id);
					$("#edit-marketCustomerOwner").val(data.owner);
					$("#edit-customerName").val(data.name);
					$("#edit-website").val(data.website);
					$("#edit-describe").val(data.description);
					$("#edit-phone").val(data.phone);
					$("#edit-contactSummary1").val(data.contactSummary);
					$("#edit-nextContactTime2").val(data.nextContactTime);
					$("#edit-address").val(data.address);
					//弹出模态窗口
					$("#editCustomerModal").modal("show");
				}
			});
		});

		//修改市场活动模块
		$("#saveEditCustomer").click(function () {
			//收集参数
			var id=$("#edit-id").val();
			var owner=$("#edit-marketCustomerOwner").val();
			var name=$("#edit-customerName").val();
			var website=$("#edit-website").val();
			var description=$("#edit-describe").val();
			var phone=$("#edit-phone").val();
			var contactSummary=$("#edit-contactSummary1").val();
			var nextContactTime=$("#edit-nextContactTime2").val();
			var address=$("#edit-address").val();
			$.ajax({
				url:"workbench/customer/SaveEditCustomer.do",
				data:{
					id:id,
					owner:owner,
					name:name,
					website:website,
					description:description,
					phone:phone,
					contactSummary:contactSummary,
					nextContactTime:nextContactTime,
					address:address,
				},
				type:"post",
				dataType:"json",
				success:function (data) {
					if(data.code=="1"){
						$("#editCustomerModal").modal("hide");
						queryCustomerByConditionForPage($("#customer_pagination").bs_pagination('getOption','currentPage'),$("#customer_pagination").bs_pagination('getOption','rowsPerPage'))
					}else {
						alert(data.message);
						$("#editCustomerModal").modal("show");
					}
				}
			});

		});
		
	});
	function queryCustomerByConditionForPage(pageNo,pageSize) {
		//收集前端查询需要的条件参数
		var name=$("#query-name").val();
		var owner = $("#query-owner").val();
		var phone = $("#query-phone").val();
		var website	 = $("#query-website").val();
		// var pageNo =1;
		// var pageSize=10;
		var html="";
		$.ajax({
			url:"workbench/customer/queryCustomerByConditionForPage.do",
			data: {
				name:name,
				owner:owner,
				phone:phone,
				website:website,
				pageNo:pageNo,
				pageSize:pageSize
			},
			type:"post",
			dataType: "json",
			success:function (data) {
				//显示总条数
				//$("#totalRowsB").text(data.totalRows);
				$.each(data.customerList,function (index,n) {
					html+='<tr class="active">',
							html+='<td><input type="checkbox" name="xz" value="'+n.id+'"/></td>',
							html+='<td><a style="text-decoration: none; cursor: pointer;"onclick="window.location.href=\'workbench/customer/queryCustomerForDetail.do?id='+n.id+'\';">'+n.name+'</a></td>',
							html+='<td>'+n.owner+'</td>',
							html+='<td>'+n.phone+'</td>',
							html+='<td>'+n.website+'</td>',
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
				$("#customer_pagination").bs_pagination({
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
						queryCustomerByConditionForPage(pageObj.currentPage,pageObj.rowsPerPage);
					}
				});

			},

		});
	}
</script>
</head>
<body>

	<!-- 创建客户的模态窗口 -->
	<div class="modal fade" id="createCustomerModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建客户</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" role="form">
					
						<div class="form-group">
							<label for="create-customerOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-customerOwner">
									<c:forEach items="${userList}" var="u">
										<option selected value="${u.id} ">${u.name}</option>
									</c:forEach>
								</select>
							</div>
							<label for="create-customerName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-customerName">
							</div>
						</div>
						
						<div class="form-group">
                            <label for="create-website" class="col-sm-2 control-label">公司网站</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-website">
                            </div>
							<label for="create-phone" class="col-sm-2 control-label">公司座机</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-phone">
							</div>
						</div>
						<div class="form-group">
							<label for="create-description" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-description"></textarea>
							</div>
						</div>
						<div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative;"></div>

                        <div style="position: relative;top: 15px;">
                            <div class="form-group">
                                <label for="create-contactSummary" class="col-sm-2 control-label">联系纪要</label>
                                <div class="col-sm-10" style="width: 81%;">
                                    <textarea class="form-control" rows="3" id="create-contactSummary"></textarea>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="create-nextContactTime" class="col-sm-2 control-label">下次联系时间</label>
                                <div class="col-sm-10" style="width: 300px;">
                                    <input type="text" class="form-control" id="create-nextContactTime">
                                </div>
                            </div>
                        </div>

                        <div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative; top : 10px;"></div>

                        <div style="position: relative;top: 20px;">
                            <div class="form-group">
                                <label for="create-address1" class="col-sm-2 control-label">详细地址</label>
                                <div class="col-sm-10" style="width: 81%;">
                                    <textarea class="form-control" rows="1" id="create-address1"></textarea>
                                </div>
                            </div>
                        </div>
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveCreateCustomerBtn">保存</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 修改客户的模态窗口 -->
	<div class="modal fade" id="editCustomerModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">修改客户</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" role="form">
						<input type="hidden" id="edit-id">
						<div class="form-group">

							<label for="edit-marketCustomerOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-marketCustomerOwner">
									<c:forEach items="${userList}" var="u">
										<option value="${u.id}">${u.name}</option>
									</c:forEach>
								</select>
							</div>
							<label for="edit-customerName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-customerName" value="动力节点">
							</div>
						</div>
						
						<div class="form-group">
                            <label for="edit-website" class="col-sm-2 control-label">公司网站</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-website" value="http://www.bjpowernode.com">
                            </div>
							<label for="edit-phone" class="col-sm-2 control-label">公司座机</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-phone" value="010-84846003">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-describe"></textarea>
							</div>
						</div>
						
						<div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative;"></div>

                        <div style="position: relative;top: 15px;">
                            <div class="form-group">
                                <label for="edit-contactSummary1" class="col-sm-2 control-label">联系纪要</label>
                                <div class="col-sm-10" style="width: 81%;">
                                    <textarea class="form-control" rows="3" id="edit-contactSummary1"></textarea>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="edit-nextContactTime2" class="col-sm-2 control-label">下次联系时间</label>
                                <div class="col-sm-10" style="width: 300px;">
                                    <input type="text" class="form-control" id="edit-nextContactTime2">
                                </div>
                            </div>
                        </div>

                        <div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative; top : 10px;"></div>

                        <div style="position: relative;top: 20px;">
                            <div class="form-group">
                                <label for="edit-address" class="col-sm-2 control-label">详细地址</label>
                                <div class="col-sm-10" style="width: 81%;">
                                    <textarea class="form-control" rows="1" id="edit-address">北京大兴大族企业湾</textarea>
                                </div>
                            </div>
                        </div>
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveEditCustomer">更新</button>
				</div>
			</div>
		</div>
	</div>
	
	
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>客户列表</h3>
			</div>
		</div>
	</div>
	
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
	
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="query-name">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="query-owner">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">公司座机</div>
				      <input class="form-control" type="text" id="query-phone">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">公司网站</div>
				      <input class="form-control" type="text" id="query-website">
				    </div>
				  </div>
				  
				  <button type="button" class="btn btn-default" id="searchCustomerBtn">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary"  id="CreateCustomerBtn"><span
						  class="glyphicon glyphicon-plus"></span> 创建
				  </button>
				  <button type="button" class="btn btn-default" data-toggle="modal" id="editCustomerBtn"><span
						  class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteCustomerBtn"><span class="glyphicon glyphicon-minus"></span> 删除
				  </button>
				</div>
				
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="checkAll"/></td>
							<td>名称</td>
							<td>所有者</td>
							<td>公司座机</td>
							<td>公司网站</td>
						</tr>
					</thead>
					<tbody id="tBody">

					</tbody>
				</table>
			</div>

			<div id="customer_pagination"></div>
			<%--<div style="height: 50px; position: relative;top: 30px;">
				<div>
					<button type="button" class="btn btn-default" style="cursor: default;">共<b>50</b>条记录</button>
				</div>
				<div class="btn-group" style="position: relative;top: -34px; left: 110px;">
					<button type="button" class="btn btn-default" style="cursor: default;">显示</button>
					<div class="btn-group">
						<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
							10
							<span class="caret"></span>
						</button>
						<ul class="dropdown-menu" role="menu">
							<li><a href="#">20</a></li>
							<li><a href="#">30</a></li>
						</ul>
					</div>
					<button type="button" class="btn btn-default" style="cursor: default;">条/页</button>
				</div>
				<div style="position: relative;top: -88px; left: 285px;">
					<nav>
						<ul class="pagination">
							<li class="disabled"><a href="#">首页</a></li>
							<li class="disabled"><a href="#">上一页</a></li>
							<li class="active"><a href="#">1</a></li>
							<li><a href="#">2</a></li>
							<li><a href="#">3</a></li>
							<li><a href="#">4</a></li>
							<li><a href="#">5</a></li>
							<li><a href="#">下一页</a></li>
							<li class="disabled"><a href="#">末页</a></li>
						</ul>
					</nav>
				</div>
			</div>--%>
			
		</div>
		
	</div>
</body>
</html>