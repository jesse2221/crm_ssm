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
		$("#reminderTime").click(function(){
			if(this.checked){
				$("#reminderTimeDiv").show("200");
			}else{
				$("#reminderTimeDiv").hide("200");
			}
		});

		$("#saveVisitBtn").click(function () {
			var owner = $("#create-Owner").val();
			var theme = $("#create-theme").val();
			var endDate = $("#create-endDate").val();
			var contactsId = $("#create-contactsId").val();
			var returnState = $("#create-state").val();
			var returnPriority = $("#create-priority").val();
			var describe = $("#create-describe").val();
			$.ajax({
				url:"workbench/visit/insertAfterSale.do",
				data:{
					owner:owner,
					theme:theme,
					endDate:endDate,
					contactsId:contactsId,
					returnState:returnState,
					returnPriority:returnPriority,
					describe:describe,
				},
				type:"post",
				dataType:"json",
				success:function (data) {
					if(data.code=='1'){
						window.location.href="workbench/visit/index.do";
					}else {
						alert(data.message);
					}
				}
			});
		});
	});
</script>
</head>
<body>

	<!-- ??????????????? -->	
	<div class="modal fade" id="findContacts" role="dialog">
		<div class="modal-dialog" role="document" style="width: 80%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">??</span>
					</button>
					<h4 class="modal-title">???????????????</h4>
				</div>
				<div class="modal-body">
					<div class="btn-group" style="position: relative; top: 18%; left: 8px;">
						<form class="form-inline" role="form">
						  <div class="form-group has-feedback">
						    <input type="text" class="form-control" style="width: 300px;" placeholder="?????????????????????????????????????????????">
						    <span class="glyphicon glyphicon-search form-control-feedback"></span>
						  </div>
						</form>
					</div>
					<table id="activityTable" class="table table-hover" style="width: 900px; position: relative;top: 10px;">
						<thead>
							<tr style="color: #B3B3B3;">
								<td></td>
								<td>??????</td>
								<td>??????</td>
								<td>??????</td>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td><input type="radio" name="activity"/></td>
								<td>??????</td>
								<td>lisi@bjpowernode.com</td>
								<td>12345678901</td>
							</tr>
							<tr>
								<td><input type="radio" name="activity"/></td>
								<td>??????</td>
								<td>lisi@bjpowernode.com</td>
								<td>12345678901</td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
	
	<div style="position:  relative; left: 30px;">
		<h3>????????????</h3>
	  	<div style="position: relative; top: -40px; left: 70%;">
			<button type="button" class="btn btn-primary" id="saveVisitBtn">??????</button>
			<button type="button" class="btn btn-default">??????</button>
		</div>
		<hr style="position: relative; top: -40px;">
	</div>
	<form class="form-horizontal" role="form">
		<div class="form-group">
			<label for="create-Owner" class="col-sm-2 control-label">?????????<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<select class="form-control" id="create-Owner" >
				  <option></option>
				 <c:forEach items="${userList}" var="u">
					 <option value="${u.id}" selected>${u.name}</option>
				 </c:forEach>
				</select>
			</div>
			<label for="create-theme" class="col-sm-2 control-label">??????<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-theme">
			</div>
		</div>
		<div class="form-group">
			<label for="create-endDate" class="col-sm-2 control-label">????????????</label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-endDate">
			</div>
			<label for="create-contacts" class="col-sm-2 control-label">?????????&nbsp;&nbsp;<a href="javascript:void(0);" data-toggle="modal" data-target="#findContacts"><span class="glyphicon glyphicon-search"></span></a></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="hidden" value="5ab6aa49ab534f04bb98f85866a46626" id="create-contactsId">
				<input type="text" class="form-control" id="create-contacts" value="??????">
			</div>
		</div>
	
		<div class="form-group">
			<label for="create-state" class="col-sm-2 control-label">??????</label>
			<div class="col-sm-10" style="width: 300px;">
				<select class="form-control" id="create-state">
				  <option></option>
					<c:forEach items="${returnStateList}" var="s">
						<option value="${s.id}">${s.value}</option>
					</c:forEach>
				</select>
			</div>
			<label for="create-priority" class="col-sm-2 control-label">?????????</label>
			<div class="col-sm-10" style="width: 300px;">
				<select class="form-control" id="create-priority">
				  <option></option>
					<c:forEach items="${returnPriorityList}" var="p">
						<option value="${p.id}">${p.value}</option>
					</c:forEach>
				</select>
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-describe" class="col-sm-2 control-label">??????</label>
			<div class="col-sm-10" style="width: 70%;">
				<textarea class="form-control" rows="3" id="create-describe"></textarea>
			</div>
		</div>
		
		<div style="position: relative; left: 103px;">
			<span><b>????????????</b></span>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="checkbox" id="reminderTime">
		</div>
		
		<div id="reminderTimeDiv" style="width: 500px; height: 180px; background-color: #EEEEEE; position: relative; left: 185px; top: 20px; display: none;">
			<div class="form-group" style="position: relative; top: 10px;">
				<label for="create-startTime" class="col-sm-2 control-label">????????????</label>
				<div class="col-sm-10" style="width: 300px;">
					<input type="text" class="form-control" id="create-startTime">
				</div>
			</div>
			
			<div class="form-group" style="position: relative; top: 15px;">
				<label for="create-repeatType" class="col-sm-2 control-label">????????????</label>
				<div class="col-sm-10" style="width: 300px;">
					<select class="form-control" id="create-repeatType">
					  <option></option>
					  <option>??????</option>
					  <option>??????</option>
					  <option>??????</option>
					  <option>??????</option>
					</select>
				</div>
			</div>
			
			<div class="form-group" style="position: relative; top: 20px;">
				<label for="create-noticeType" class="col-sm-2 control-label">????????????</label>
				<div class="col-sm-10" style="width: 300px;">
					<select class="form-control" id="create-noticeType">
					  <option></option>
					  <option>??????</option>
					  <option>??????</option>
					</select>
				</div>
			</div>
		</div>
	</form>
	
	<div style="height: 200px;"></div>
</body>
</html>