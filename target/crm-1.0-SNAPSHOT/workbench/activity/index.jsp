<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>

<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bs_pagination/jquery.bs_pagination.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
<script type="text/javascript" src="jquery/bs_pagination/en.js"></script>

<script type="text/javascript">

	$(function(){

		//展现市场活动信息
		getActivity(1,2);

		//为创建模态窗口按钮 (addBtn)绑定点击事件
		$("#addBtn").click(function (){

			//模态窗口中日期插件
			$(".time").datetimepicker({
				minView:"month",
				language:'zh-CN',
				format:'yyyy-mm-dd',
				autoclose:true,
				todayBtn:true,
				pickerPosition:"bottom-left"
			});

			//点击创建按钮，获得数据库中tbl_user的用户信息
			$.ajax({
				url:"workbench/activity/getUserList.do",
				type:"get",
				dataType:"json",
				success:function (data){

					let opt = "<option></option>";

					$.each(data,function (index,element){
					//拿到所有用户的名字信息
					opt += ("<option value='"+element.id+"'>"+element.name+"</option>");
					})

					$("#create-marketActivityOwner").html(opt);

					//取得当前用户id
					//在js中使用el表达式
					let id = "${user.id}";
					//给<option>的value赋值
					$("#create-marketActivityOwner").val(id);

					//需要操作的模态窗口的jquery对象，调用modal方法，为该方法传递参数 show/hide
					$("#createActivityModal").modal("show");

				}
			})
		});

		//模态窗口，进行保存创建信息操作
		$("#save").click(function (){

			$.ajax({
				url:"workbench/activity/save.do",
				data:{
					"owner":$.trim($("#create-marketActivityOwner").val()),
					"name":$.trim($("#create-marketActivityName").val()),
					"startDate":$.trim($("#create-startDate").val()),
					"endDate":$.trim($("#create-endDate").val()),
					"cost":$.trim($("#create-cost").val()),
					"description":$.trim($("#create-describe").val())
				},
				type:"post",
				dataType:"json",
				success:function (data){
					if(data.success){
						//添加成功后，局部刷新市场活动信息列表
						getActivity(1,$("#activityPage").bs_pagination('getOption','rowsPerPage'));
						//清空模态窗口填写的信息
						$("#create-form")[0].reset();
						//关闭模态窗口
						$("#createActivityModal").modal("hide");

					}else{
						alert("添加市场活动失败");
					}
				}
			})
		})

		//为查询绑定一个事件
		$("#search").click(function (){

			//点击查询，将查询条件存在hidden中
			$("#hidden-name").val($.trim($("#search-name").val()));
			$("#hidden-owner").val($.trim($("#search-owner").val()));
			$("#hidden-startDate").val($.trim($("#search-startDate").val()));
			$("#hidden-endDate").val($.trim($("#search-endDate").val()));

			getActivity(1,2);
		})

        //为全选的复选框绑定事件，触发全选操作
        $("#checkAll").click(function (){
            $("input[name='checkOne']").prop("checked",this.checked);
        });

		//子框与总框发生关联
		$("#activity-tbody").on("click",$("input[name='checkOne']"),function (){
			let flg = true;
			//如果有子框未被选中，则设置总框checked的属性为false
			 if(($("input[name='checkOne']").not(":checked").size() > 0)){
				 flg = false;
			 }
			$("#checkAll").prop("checked",flg);

		})

		let ids = new Array();
		//为删除按钮绑定点击事件
		$("#delete").click(function (){

			let check = $("input[name='checkOne']:checked");

			if(check.length == 0){
				alert("请选择需要删除的记录");
			}else {
				if(confirm("确认删除所选中的记录吗")){
					//如果获取勾选值
					$.each(check, function () {
						ids.push($(this).val());
					});

					//拿到勾选值，发送ajax请求，进行数据库对应数据删除操作
					$.ajax({
						url:"workbench/activity/delete.do",
						type:"post",
						dataType:"json",
						traditional:true,
						data:{"ids":ids},
						success:function (data){
							if(data.success){
								alert("删除成功");
								getActivity(1,$("#activityPage").bs_pagination('getOption','rowsPerPage'));
								ids.length = 0;
							}else {
								alert("删除失败");
							}
						}
					});
				}


			}
		})

		//为修改按钮绑定点击事件
		$("#edit").click(function (){
			//模态窗口中日期插件
			$(".time").datetimepicker({
				minView:"month",
				language:'zh-CN',
				format:'yyyy-mm-dd',
				autoclose:true,
				todayBtn:true,
				pickerPosition:"bottom-left"
			});

			let check = $("input[name='checkOne']:checked");

			if(check.length == 0){
				alert("请选择需要修改的记录");
			}else if(check.length > 1){
				alert("只能选择一条记录进行修改");
			}else{
				//获取选择框的id值
				let activityId = check.val();

				//拿到所有者，并填充
				//获得数据库中tbl_user的用户信息
				$.ajax({
					url:"workbench/activity/getUserList.do",
					type:"get",
					dataType:"json",
					success:function (data){

						let opt = "<option></option>";

						$.each(data,function (index,element){
							//拿到所有用户的名字信息
							opt += "<option value='"+element.id+"'>"+element.name+"</option>";
						})

						$("#edit-marketActivityOwner").html(opt);

						//根据选择框checkId 拿到activity详情
						$.ajax({
							url:"workbench/activity/edit.do",
							data:{"activityId":activityId},
							type:"post",
							dataType:"json",
							success:function (element){
								//将市场活动详情填充到表格中
								$("#edit-marketActivityOwner").val(element.owner);
								$("#edit-marketActivityName").val(element.name);
								$("#edit-startTime").val(element.startDate);
								$("#edit-endTime").val(element.endDate);
								$("#edit-cost").val(element.cost);
								$("#edit-describe").val(element.description);
							}
						})

					}
				})

				//显示窗口
				$("#editActivityModal").modal("show");

			}
		})


		//为修改内容--更新按钮绑定点击事件
		$("#update").click(function (){
			//获取选择框的id值
			let activityId = $("input[name='checkOne']").val();
			//获取表单内容
			let owner = $("#edit-marketActivityOwner").val();
			let name = $("#edit-marketActivityName").val();
			let startDate = $("#edit-startTime").val();
			let endDate = $("#edit-endTime").val();
			let cost = $("#edit-cost").val();
			let description = $("#edit-describe").val();
			//获取修改人
			let editBy = "${user.name}";

			//发送ajax请求
			$.ajax({
				url:"workbench/activity/update.do",
				data:{
					"id":activityId,
					"owner":owner,
					"name":name,
					"startDate":startDate,
					"endDate" :endDate,
					"cost":cost,
					"description":description,
					"editBy":editBy
				},
				type:"post",
				dataType:"json",
				success:function (data){
					if(data.success){
						$("#editActivityModal").modal("hide");
						getActivity($("#activityPage").bs_pagination('getOption','currentPage'),
								$("#activityPage").bs_pagination('getOption','rowsPerPage'));
					}
				}
			})


		})


	});

	//获取市场活动列表信息
	function getActivity(pageNum,pageSize){

		//每次刷新市场活动列表，将总选择框取消选中
		$("#checkAll").prop("checked",false);


		$("#search-name").val($("#hidden-name").val());
		$("#search-owner").val($("#hidden-owner").val());
		$("#search-startDate").val($("#hidden-startDate").val());
		$("#search-endDate").val($("#hidden-endDate").val());

		$.ajax({
			url:"workbench/activity/getActivity.do",
			data:{
				"pageNum":pageNum,
				"pageSize":pageSize,
				"name":$.trim($("#search-name").val()),
				"owner":$.trim($("#search-owner").val()),
				"startDate":$.trim($("#search-startDate").val()),
				"endDate":$.trim($("#search-endDate").val())
			},
			type:"get",
			dataType:"json",
			success : function (data){

				/*<tr class="active">
					<td><input type="checkbox" /></td>
					<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='workbench/activity/detail.jsp';">发传单</a></td>
					<td>zhangsan</td>
					<td>2020-10-10</td>
					<td>2020-10-20</td>
				</tr>*/

				let activityTableStr = '';
				$.each(data.dataList,function (index,element){
					activityTableStr += '<tr class="active">';
					activityTableStr += '<td><input type="checkbox" value="'+element.id+'" name="checkOne"/></td>';
					activityTableStr += '<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/activity/getDetail.do?id='+element.id+'\';">'+element.name+'</a></td>';
					activityTableStr += '<td>'+element.owner+'</td>';
					activityTableStr += '<td>'+element.startDate+'</td>';
					activityTableStr += '<td>'+element.endDate+'</td>';
					activityTableStr += '</tr>';

				})
					$("#activity-tbody").html(activityTableStr);

				//计算总页数
				let totalPages = data.total%pageSize == 0 ? data.total/pageSize : parseInt(data.total/pageSize)+1

				//数据处理完毕后，结合分页插件，对前端展现分页信息
				$("#activityPage").bs_pagination({
					currentPage:pageNum,//选定当前页的页码
					rowsPerPage:pageSize,//每页显示的记录条数
					maxRowsPerPage:20,//每页最多显示的记录数
					totalPages:totalPages,//总页数
					totalRows:data.total,//总记录数
					visiblePageLinks:3,//显示几个页码卡片，没显示出来的就隐藏..
					showGoToPage: true,
					showRowsPerPage: true,
					showRowsInfo: true,
					showRowsDefaultInfo: true,

					//该回调函数时，点击分页组件的时候触发的
					onChangePage:function (event,data){
						getActivity(data.currentPage,data.rowsPerPage);
					}
				})

			}
		})
	}
	
</script>
</head>
<body>

	<input type="hidden" id="hidden-name">
	<input type="hidden" id="hidden-owner">
	<input type="hidden" id="hidden-startDate">
	<input type="hidden" id="hidden-endDate">

	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form" id="create-form">
					
						<div class="form-group">
							<label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-marketActivityOwner">
								</select>
							</div>
                            <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-marketActivityName">
                            </div>
						</div>
						
						<div class="form-group">
							<label for="create-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-startDate" readonly>
							</div>
							<label for="create-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-endDate" readonly>
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-describe"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="save">保存</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 修改市场活动的模态窗口 -->
	<div class="modal fade" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form">
					
						<div class="form-group">
							<label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-marketActivityOwner">
								</select>
							</div>
                            <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-marketActivityName">
                            </div>
						</div>

						<div class="form-group">
							<label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-startTime">
							</div>
							<label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-endTime">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-describe"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="update">更新</button>
				</div>
			</div>
		</div>
	</div>
	
	
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表</h3>
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
				      <input class="form-control" type="text" id="search-name">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="search-owner">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control" type="text" id="search-startTime" />
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control" type="text" id="search-endTime">
				    </div>
				  </div>
				  
				  <button type="button" class="btn btn-default" id="search">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" id="addBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="edit"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="delete"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="checkAll"/></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="activity-tbody">
					</tbody>
				</table>
			</div>
			
			<div style="height: 50px; position: relative;top: 30px;">
				<div id="activityPage"></div>

			</div>
			
		</div>
		
	</div>
</body>
</html>