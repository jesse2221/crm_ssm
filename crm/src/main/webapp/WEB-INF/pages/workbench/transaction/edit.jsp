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
 $(function () {
	$("#closeEdit").click(function () {
		window.location.href="workbench/transaction/index.do";
	});
	var i='${tran.id}';
	 $("#create-transactionOwner").val("${tran2.owner}");
	 $("#create-amountOfMoney").val("${tran.money}");
	 $("#create-transactionName").val("${tran.name}");
	 $("#create-expectedClosingDate").val(${tran.expectedDate});
	 $("#edit-customerId").val("${tran2.customerId}");
	 $("#create-transactionStage").val("${tran2.stage}");
	 $("#create-transactionType").val("${tran2.type}");
	 $("#create-clueSource").val("${tran2.source}");
	 $("#create-activitySrc1").val("${tran2.activityId}");
	 $("#edit-contacatsId").val("${tran2.contactsId}");
	 $("#create-describe").val("${tran.description}");
	 $("#create-contactSummary").val("${tran.contactSummary}");
	 $("#create-nextContactTime").val("${tran.nextContactTime}");



	$("#saveEditBtn").click(function () {

		var id=i;
		var owner=$("#create-transactionOwner").val();
		var money=$("#create-amountOfMoney").val();
		var name=$("#create-transactionName").val();
		var expectedDate=$("#create-expectedClosingDate").val();
		var customerId=$("#edit-customerId").val();
		var stage=$("#create-transactionStage").val();
		var type=$("#create-transactionType").val();
		var source=$("#create-clueSource").val();
		var activityId=$("#create-activitySrc1").val();
		var contactsId=$("#edit-contacatsId").val();
		var description=$("#create-describe").val();
		var contactSummary=$("#create-contactSummary").val();
		var nextContactTime=$("#create-nextContactTime").val();
		$.ajax({
			url:"workbench/tran/SaveEditTran.do",
			data:{
				id:id,
				owner:owner,
				money:money,
				name:name,
				expectedDate:expectedDate,
				customerId:customerId,
				stage:stage,
				type:type,
				source:source,
				activityId:activityId,
				contactsId:contactsId,
				description:description,
				contactSummary:contactSummary,
				nextContactTime:nextContactTime
			},
			type:"post",
			dataType:"json",
			success:function (data) {
				if(data.code=="1"){
					alert("????????????");
					window.location.href="workbench/transaction/index.do";
				}else {
					alert(data.message);

				}
			}
		});
	});


 })

</script>
</head>
<body>




<div style="position:  relative; left: 30px;">
	<h3>????????????</h3>
	<div style="position: relative; top: -40px; left: 70%;">
		<button type="button" class="btn btn-primary" id="saveEditBtn">??????</button>
		<button type="button" class="btn btn-default" id="closeEdit">??????</button>
	</div>
	<hr style="position: relative; top: -40px;">
</div>
<form class="form-horizontal" role="form" style="position: relative; top: -30px;">
	<div class="form-group">
		<label for="create-transactionOwner" class="col-sm-2 control-label">?????????<span style="font-size: 15px; color: red;">*</span></label>
		<div class="col-sm-10" style="width: 300px;">
			<select class="form-control" id="create-transactionOwner">
				<c:forEach items="${userList}" var="u">
					<option value="${u.id}">${u.name}</option>
				</c:forEach>
			</select>
		</div>
		<label for="create-amountOfMoney" class="col-sm-2 control-label">??????</label>
		<div class="col-sm-10" style="width: 300px;">
			<input type="text" class="form-control" id="create-amountOfMoney">
		</div>
	</div>

	<div class="form-group">
		<label for="create-transactionName" class="col-sm-2 control-label">??????<span style="font-size: 15px; color: red;">*</span></label>
		<div class="col-sm-10" style="width: 300px;">
			<input type="text" class="form-control" id="create-transactionName" >
		</div>
		<label for="create-expectedClosingDate" class="col-sm-2 control-label">??????????????????<span style="font-size: 15px; color: red;">*</span></label>
		<div class="col-sm-10" style="width: 300px;">
			<input type="text" class="form-control" id="create-expectedClosingDate">
		</div>
	</div>

	<div class="form-group">
		<label for="create-accountName" class="col-sm-2 control-label">????????????<span style="font-size: 15px; color: red;">*</span></label>
		<div class="col-sm-10" style="width: 300px;">
			<input type="hidden" id="edit-customerId" value="920ce994508e4a6ca8dc16f1d6c2a8ba">
			<input type="text" class="form-control" id="create-accountName" placeholder="???????????????????????????????????????????????????">
		</div>
		<label for="create-transactionStage" class="col-sm-2 control-label">??????<span style="font-size: 15px; color: red;">*</span></label>
		<div class="col-sm-10" style="width: 300px;">
			<select class="form-control" id="create-transactionStage">

				<c:forEach items="${stageList}" var="s">
					<option value="${s.id}">${s.value}</option>
				</c:forEach>
			</select>
		</div>
	</div>

	<div class="form-group">
		<label for="create-transactionType" class="col-sm-2 control-label">??????</label>
		<div class="col-sm-10" style="width: 300px;">
			<select class="form-control" id="create-transactionType">
				<option></option>
				<c:forEach items="${transactionTypeList}" var="s">
					<option value="${s.id}">${s.value}</option>
				</c:forEach>
			</select>
		</div>
		<label for="create-possibility" class="col-sm-2 control-label">?????????</label>
		<div class="col-sm-10" style="width: 300px;">
			<input type="text" class="form-control" id="create-possibility" value="48">
		</div>
	</div>

	<div class="form-group">
		<label for="create-clueSource" class="col-sm-2 control-label">??????</label>
		<div class="col-sm-10" style="width: 300px;">
			<select class="form-control" id="create-clueSource" >
				<option></option>
				<c:forEach items="${sourceList}" var="s">
					<option value="${s.id}">${s.value}</option>
				</c:forEach>
			</select>
		</div>
		<label for="create-activitySrc" class="col-sm-2 control-label">???????????????&nbsp;&nbsp;<a href="javascript:void(0);" data-toggle="modal" data-target="#findMarketActivity"><span class="glyphicon glyphicon-search"></span></a></label>
		<div class="col-sm-10" style="width: 300px;">
			<input type="hidden" id="create-activitySrc1">
			<input type="text" class="form-control" id="create-activitySrc">
		</div>
	</div>

	<div class="form-group">
		<label for="create-contactsName" class="col-sm-2 control-label">???????????????&nbsp;&nbsp;<a href="javascript:void(0);" data-toggle="modal" data-target="#findContacts"><span class="glyphicon glyphicon-search"></span></a></label>
		<div class="col-sm-10" style="width: 300px;">
			<input type="text" class="form-control" id="create-contactsName">
		</div>
	</div>

	<div class="form-group">
		<label for="create-describe" class="col-sm-2 control-label">??????</label>
		<div class="col-sm-10" style="width: 70%;">
			<textarea class="form-control" rows="3" id="create-describe"></textarea>
		</div>
	</div>

	<div class="form-group">
		<label for="create-contactSummary" class="col-sm-2 control-label">????????????</label>
		<div class="col-sm-10" style="width: 70%;">
			<textarea class="form-control" rows="3" id="create-contactSummary"></textarea>
		</div>
	</div>

	<div class="form-group">
		<label for="create-nextContactTime" class="col-sm-2 control-label">??????????????????</label>
		<div class="col-sm-10" style="width: 300px;">
			<input type="text" class="form-control" id="create-nextContactTime">
		</div>
	</div>

</form>


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

<!-- ?????????????????? -->
<div class="modal fade" id="findMarketActivity" role="dialog">
	<div class="modal-dialog" role="document" style="width: 80%;">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">
					<span aria-hidden="true">??</span>
				</button>
				<h4 class="modal-title">??????????????????</h4>
			</div>
			<div class="modal-body">
				<div class="btn-group" style="position: relative; top: 18%; left: 8px;">
					<form class="form-inline" role="form">
						<div class="form-group has-feedback">
							<input type="text" class="form-control" style="width: 300px;" placeholder="????????????????????????????????????????????????">
							<span class="glyphicon glyphicon-search form-control-feedback"></span>
						</div>
					</form>
				</div>
				<table id="activityTable3" class="table table-hover" style="width: 900px; position: relative;top: 10px;">
					<thead>
					<tr style="color: #B3B3B3;">
						<td></td>
						<td>??????</td>
						<td>????????????</td>
						<td>????????????</td>
						<td>?????????</td>
					</tr>
					</thead>
					<tbody>
					<tr>
						<td><input type="radio" name="activity"/></td>
						<td>?????????</td>
						<td>2020-10-10</td>
						<td>2020-10-20</td>
						<td>zhangsan</td>
					</tr>
					<tr>
						<td><input type="radio" name="activity"/></td>
						<td>?????????</td>
						<td>2020-10-10</td>
						<td>2020-10-20</td>
						<td>zhangsan</td>
					</tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>

</body>
</html>