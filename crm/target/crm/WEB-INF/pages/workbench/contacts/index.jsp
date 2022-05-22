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
		queryContactsByConditionForPage(1,10);

		//定制字段
		$("#definedColumns > li").click(function(e) {
			//防止下拉菜单消失
	        e.stopPropagation();
	    });

		//当容器加载完之后，对容器调用工具函数
		//$("input[name='mydate']").datetimepicker({
		$(".time").datetimepicker({
			minView: "month",//可以选择的最小视图
			language: 'zh-CN',
			format: 'yyyy-mm-dd',
			autoclose: true,//设置选择完日期或者时间之后，日否自动关闭日历
			todayBtn: true,//设置是否显示"今天"按钮,默认是false
			pickerPosition: "bottom-left",
			clearBtn: true//设置是否显示"清空"按钮，默认是false
		});

		$(".timetop").datetimepicker({
			minView: "month",//可以选择的最小视图
			language: 'zh-CN',
			format: 'yyyy-mm-dd',
			autoclose: true,//设置选择完日期或者时间之后，日否自动关闭日历
			todayBtn: true,//设置是否显示"今天"按钮,默认是false
			pickerPosition: "top-left",
			clearBtn: true//设置是否显示"清空"按钮，默认是false
		});

		$("#saveBtn").click(function () {
			var owner=$("#create-Owner").val();
			var source=$("#create-clueSource").val();
			var fullname=$.trim($("#create-surname").val());
			var appellation=$("#create-call").val();
			var job=$.trim($("#create-job").val());
			var mphone=$.trim($("#create-mphone").val());
			var email=$.trim($("#create-email").val());
			var customerId=$("#create-customerName").val();
			var description=$.trim($("#create-description").val());
			var contactSummary=$.trim($("#create-contactSummary1").val());
			var nextContactTime=$("#create-nextContactTime1").val();
			var address=$.trim($("#create-address1").val());
			var birthday=$.trim($("#create-birth").val());
			//校验
			if(fullname==null){
				alert("姓名不能为空！！");
				return;
			}

			$.ajax({
				url:"workbench/contacts/saveCreateContacts.do",
				data:{
					owner:owner,
					source:source,
					fullname:fullname,
					appellation:appellation,
					job:job,
					mphone:mphone,
					email:email,
					customerId:customerId,
					description:description,
					contactSummary:contactSummary,
					nextContactTime:nextContactTime,
					address:address,
					birthday:birthday
				},
				type:"post",
				dataType:"json",
				success:function (data) {
					if(data.code=='1'){
						queryContactsByConditionForPage(1,$("#contacts_pagination").bs_pagination('getOption', 'rowsPerPage'));
						$("#createContactsModal").modal("hide");
					}else {
						alert(data.message);
						$("#createContactsModal").modal("show");

					}
				}
			});
		});

	//条件查询
	$("#searchContactsBtn").click(function () {
		queryContactsByConditionForPage(1,$("#contacts_pagination").bs_pagination('getOption', 'rowsPerPage'));
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
	$("#deleteContactsBtn").click(function () {
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
				url: "workbench/contacts/deleteContactsByIds.do",
				data: ids,
				type: "post",
				dataType: "json",
				success: function (data) {
					if (data.code == '1') {
						queryContactsByConditionForPage(1, $("#contacts_pagination").bs_pagination('getOption','rowsPerPage'));
					} else {
						alert(data.message);
					}
				}
			});
		}
	});

	$("#editContactsBtn").click(function () {
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
			url:"workbench/contacts/queryContactsById.do",
			data:{
				id:id
			},
			type:"post",
			dataType:"json",
			success:function (data) {
				//把市场活动的信息显示在修改的模态窗口上
				$("#edit-id").val(data.id);
				$("#edit-clueOwner").val(data.owner);
				$("#edit-marketcontactsOwner").val(data.owner);
				$("#edit-clueSource1").val(data.source);
				$("#edit-surname").val(data.fullname);
				$("#edit-call").val(data.appellation);
				$("#edit-job").val(data.job);
				$("#edit-mphone").val(data.mphone);
				$("#edit-email").val(data.email);
				$("#edit-birth").val(data.birthday);
				$("#edit-customerId").val(data.customerId);
				$("#edit-customerName").val("西京学院");
				$("#edit-describe").val(data.description);
				$("#edit-contactSummary").val(data.contactSummary);
				$("#edit-nextContactTime").val(data.nextContactTime);
				$("#edit-address2").val(data.address);

				//弹出模态窗口
				$("#editContactsModal").modal("show");
			}
		});
	});
		
});

function queryContactsByConditionForPage(pageNo,pageSize) {
	//收集前端查询需要的条件参数
	var owner=$("#query-owner").val();
	var fullname = $("#query-fullname").val();
	var customerName = $("#query-customerName").val();
	var source= $("#query-source").val();
	var birthday= $("#query-birthday").val();
	var html="";
	$.ajax({
		url:"workbench/contacts/queryContactsByConditionForPage.do",
		data: {
			owner:owner,
			fullname:fullname,
			customerName:customerName,
			source:source,
			birthday:birthday,
			pageNo:pageNo,
			pageSize:pageSize
		},
		type:"post",
		dataType: "json",
		success:function (data) {
			//显示总条数
			//$("#totalRowsB").text(data.totalRows);
			$.each(data.contactsList,function (index,n) {
				html+='<tr class="active">',
						html+='<td><input type="checkbox" name="xz" value="'+n.id+'"/></td>',
						html+='<td><a style="text-decoration: none; cursor: pointer;"onclick="window.location.href=\'workbench/contacts/queryContactsForDetail.do?id='+n.id+'\';">'+n.fullname+'</a></td>',
						html+='<td>'+n.customerId+'</td>',
						html+='<td>'+n.owner+'</td>',
						html+='<td>'+n.source+'</td>',
						html+='<td>'+n.birthday+'</td>',
						html+='</tr>';
			})
			$("#tBody").html(html);

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
			$("#contacts_pagination").bs_pagination({
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
					queryContactsByConditionForPage(pageObj.currentPage,pageObj.rowsPerPage);
				}
			});

		},

	});
}
</script>
</head>
<body>

	
	<!-- 创建联系人的模态窗口 -->
	<div class="modal fade" id="createContactsModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" onclick="$('#createContactsModal').modal('hide');">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabelx">创建联系人</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" role="form">
					
						<div class="form-group">
							<label for="create-Owner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-Owner">
								 <c:forEach items="${userList}" var="u">
									 <option selected value="${u.id}" >${u.name}</option>
								 </c:forEach>
								</select>
							</div>
							<label for="create-clueSource" class="col-sm-2 control-label">来源</label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-clueSource">
								  <option></option>
								  <c:forEach items="${sourceList}" var="s">
									  <option value="${s.id}">${s.value}</option>
								  </c:forEach>
								</select>
							</div>
						</div>
						
						<div class="form-group">
							<label for="create-surname" class="col-sm-2 control-label">姓名<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-surname">
							</div>
							<label for="create-call" class="col-sm-2 control-label">称呼</label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-call">
								  <option></option>
								  <c:forEach items="${appellationList}" var="ap">
									  <option selected value="${ap.id}">${ap.value}</option>
								  </c:forEach>
								</select>
							</div>
							
						</div>
						
						<div class="form-group">
							<label for="create-job" class="col-sm-2 control-label">职位</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-job">
							</div>
							<label for="create-mphone" class="col-sm-2 control-label">手机</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-mphone">
							</div>
						</div>
						
						<div class="form-group" style="position: relative;">
							<label for="create-email" class="col-sm-2 control-label">邮箱</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-email">
							</div>
							<label for="create-birth" class="col-sm-2 time control-label">生日</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-birth" readonly>
							</div>
						</div>
						
						<div class="form-group" style="position: relative;">
							<label for="create-customerName" class="col-sm-2 control-label">客户名称</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="hidden" id="create-customerName" value="920ce994508e4a6ca8dc16f1d6c2a8ba">
								<input type="text" class="form-control" id="create-customerName1"
									   placeholder="支持自动补全，输入客户不存在则新建" value="阿里巴巴">
							</div>
						</div>
						
						<div class="form-group" style="position: relative;">
							<label for="create-description" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-description"></textarea>
							</div>
						</div>
						
						<div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative;"></div>
						
						<div style="position: relative;top: 15px;">
							<div class="form-group">
								<label for="create-contactSummary1" class="col-sm-2 control-label">联系纪要</label>
								<div class="col-sm-10" style="width: 81%;">
									<textarea class="form-control" rows="3" id="create-contactSummary1"></textarea>
								</div>
							</div>
							<div class="form-group">
								<label for="create-nextContactTime1" class="col-sm-2 control-label">下次联系时间</label>
								<div class="col-sm-10" style="width: 300px;">
									<input type="text" class="form-control timetop" id="create-nextContactTime1"
										   readonly>
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
					<button type="button" class="btn btn-primary" id="saveBtn">保存</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 修改联系人的模态窗口 -->
	<div class="modal fade" id="editContactsModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">修改联系人</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" role="form">
						<input type="hidden" id="edit-id">
						<div class="form-group">
							<label for="edit-marketcontactsOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-marketcontactsOwner">
									<c:forEach items="${userList}" var="u">
										<option value="${u.id}">${u.name}</option>
									</c:forEach>
								</select>
							</div>
							<label for="edit-clueSource1" class="col-sm-2 control-label">来源</label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-clueSource1">
								  <option></option>
									<c:forEach items="${sourceList}" var="s">
										<option value="${s.id}">${s.value}</option>
									</c:forEach>
								</select>
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-surname" class="col-sm-2 control-label">姓名<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-surname" value="李四">
							</div>
							<label for="edit-call" class="col-sm-2 control-label">称呼</label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-call">
								  <option></option>
									<c:forEach items="${appellationList}" var="ap">
										<option value="${ap.id}">${ap.value}</option>
									</c:forEach>
								</select>
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-job" class="col-sm-2 control-label">职位</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-job" value="CTO">
							</div>
							<label for="edit-mphone" class="col-sm-2 control-label">手机</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-mphone" value="12345678901">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-email" class="col-sm-2 control-label">邮箱</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-email" value="lisi@bjpowernode.com">
							</div>
							<label for="edit-birth" class="col-sm-2 control-label">生日</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-birth" readonly>
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-customerName" class="col-sm-2 control-label">客户名称</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="hidden" id="edit-customerId">
								<input type="text" class="form-control" id="edit-customerName" placeholder="支持自动补全，输入客户不存在则新建" value="动力节点">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-describe">这是一条线索的描述信息</textarea>
							</div>
						</div>
						
						<div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative;"></div>
						
						<div style="position: relative;top: 15px;">
							<div class="form-group">
								<label for="edit-contactSummary" class="col-sm-2 control-label">联系纪要</label>
								<div class="col-sm-10" style="width: 81%;">
									<textarea class="form-control" rows="3" id="edit-contactSummary"></textarea>
								</div>
							</div>
							<div class="form-group">
								<label for="edit-nextContactTime" class="col-sm-2 control-label">下次联系时间</label>
								<div class="col-sm-10" style="width: 300px;">
									<input type="text" class="form-control time" id="edit-nextContactTime" readonly>
								</div>
							</div>
						</div>
						
						<div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative; top : 10px;"></div>

                        <div style="position: relative;top: 20px;">
                            <div class="form-group">
                                <label for="edit-address2" class="col-sm-2 control-label">详细地址</label>
                                <div class="col-sm-10" style="width: 81%;">
                                    <textarea class="form-control" rows="1" id="edit-address2">北京大兴区大族企业湾</textarea>
                                </div>
                            </div>
                        </div>
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" data-dismiss="modal">更新</button>
				</div>
			</div>
		</div>
	</div>
	
	
	
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>联系人列表</h3>
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
				      <div class="input-group-addon">姓名</div>
				      <input class="form-control" type="text" id="query-fullname">
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
				      <div class="input-group-addon">来源</div>
				      <select class="form-control" id="query-source">
						  <option></option>
						  <c:forEach items="${sourceList}" var="s">
							  <option value="${s.id}">${s.value}</option>
						  </c:forEach>
						</select>
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">生日</div>
				      <input class="form-control time" type="text" readonly id="query-birthday">
				    </div>
				  </div>
				  
				  <button type="button" class="btn btn-default" id="searchContactsBtn">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 10px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#createContactsModal"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" data-toggle="modal" id="editContactsBtn"><span
						  class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteContactsBtn"><span class="glyphicon glyphicon-minus"></span> 删除
				  </button>
				</div>
				
				
			</div>
			<div style="position: relative;top: 20px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="checkAll"/></td>
							<td>姓名</td>
							<td>客户名称</td>
							<td>所有者</td>
							<td>来源</td>
							<td>生日</td>
						</tr>
					</thead>
					<tbody id="tBody">
						<%--<tr>
							<td><input type="checkbox" /></td>
							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">李四</a></td>
							<td>动力节点</td>
							<td>zhangsan</td>
							<td>广告</td>
							<td>2000-10-10</td>
						</tr>
                        <tr class="active">
                            <td><input type="checkbox" /></td>
                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">李四</a></td>
                            <td>动力节点</td>
                            <td>zhangsan</td>
                            <td>广告</td>
                            <td>2000-10-10</td>
                        </tr>--%>
					</tbody>
				</table>
			</div>
			<br>
			<div id="contacts_pagination"></div>
			<%--<div style="height: 50px; position: relative;top: 10px;">
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
			</div>
			
		</div>--%>
		</div>
	</div>
</body>
</html>