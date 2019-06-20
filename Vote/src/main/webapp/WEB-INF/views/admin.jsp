<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>이연제약 투표 시스템</title>
<!-- bootstrap css -->
<link rel="stylesheet" href="css/bootstrap.min.css" >
<!-- jsgrid -->
<link rel="stylesheet" href="css/jsgrid.min.css" />
<link rel="stylesheet" href="css/jsgrid-theme.min.css" />
<style type="text/css">
@import url('https://fonts.googleapis.com/css?family=Nanum+Gothic');
body {
	font-family: 'Nanum Gothic', sans-serif;
}
</style>
<!-- jquery -->
<script type="text/javascript" src="js/jquery-3.3.1.min.js"></script>
<!-- popper -->
<script type="text/javascript" src="js/popper.js"></script>
<!-- bootstrap script -->
<script type="text/javascript" src="js/bootstrap.js"></script>
<!-- jsgrid -->
<script type="text/javascript" src="js/jsgrid.min.js"></script>
<!--[if lt IE 9]>
<script src="js/html5shiv.js"></script>
<script src="js/respond.min.js"></script>
<![endif]-->

<script type="text/javascript">

	$(function() {
	   $("#falseUserDiv").jsGrid({
	       width: "100%",
	       height: "400px",
	       autoload : true,
	       inserting: false,
	       editing: false,
	       sorting: true,
	       paging: false,
	       controller : {
	           loadData : function(filter) {
	               var def = $.Deferred();
	               $.ajax({
	                   url : './voteUserListAjax.json?auth=reyon&type=1',
	                   type : 'GET',
	                   contentType : 'application/json; charset=utf-8',
	                   dataType : 'json'
	               }).done(function(item) {
	            	   $("#falseUserHeader").text("미투표 인원 (" + item.list.length + "명)");
	                   def.resolve(item.list);
	               });
	               return def.promise();
	           }
	       },
	       fields: [
				{ title: "사번", name: "sabun", type: "text", align: "center" },
				{ title: "사업장", name: "saup", type: "text", align: "center" },
				{ title: "부서", name: "dept", type: "text", align: "center" },
				{ title: "직급", name: "pos", type: "text", align: "center" },
				{ title: "이름", name: "kname", type: "text", align: "center" },
	       ]
	   });
	   
	   
	   $("#trueUserDiv").jsGrid({
	       width: "100%",
	       height: "400px",
	       autoload : true,
	       inserting: false,
	       editing: false,
	       sorting: true,
	       paging: false,
	       controller : {
	           loadData : function(filter) {
	               var def = $.Deferred();
	               $.ajax({
	            	   url : './voteUserListAjax.json?auth=reyon&type=2',
	                   type : 'GET',
	                   contentType : 'application/json; charset=utf-8',
	                   dataType : 'json'
	               }).done(function(item) {
	            	   $("#trueUserHeader").text("투표 인원 (" + item.list.length + "명)");
	                   def.resolve(item.list);
	               });
	               return def.promise();
	           }
	       },
	       fields: [
				{ title: "사번", name: "sabun", type: "text", align: "center" },
				{ title: "사업장", name: "saup", type: "text", align: "center" },
				{ title: "부서", name: "dept", type: "text", align: "center" },
				{ title: "직급", name: "pos", type: "text", align: "center" },
				{ title: "이름", name: "kname", type: "text", align: "center" },
				{ title: "투표일시", name: "voteDate", type: "text", align: "center" },
	       ]
	   });
	   
	   $("#statisticsDiv").jsGrid({
	       width: "100%",
	       height: "400px",
	       autoload : true,
	       inserting: false,
	       editing: false,
	       sorting: true,
	       paging: false,
	       controller : {
	           loadData : function(filter) {
	               var def = $.Deferred();
	               $.ajax({
	            	   url : './voteUserListAjax.json?auth=reyon&type=3',
	                   type : 'GET',
	                   contentType : 'application/json; charset=utf-8',
	                   dataType : 'json'
	               }).done(function(item) {
	                   def.resolve(item.list);
	               });
	               return def.promise();
	           }
	       },
	       fields: [
				{ title: "등록번호", name: "voteSeq", type: "number", align: "center" },
	            {
               	 headerTemplate: function() {return "이미지"},
               	 itemTemplate: function(_, item) {
               		 	var imgFile = item.voteFilepath + item.voteFilename;
               		 	return $("<img>").attr("src", imgFile).css({ height: 50, width: 50 }).on("click", function() {
               		 		$("#imagePreview").attr("src", imgFile);
               		 		$("#myModal").modal();
               		 	});
               		 }, align: "center", sorting: false
				},
				{ title: "합계 점수", name: "voteSum", type: "number", align: "center" },
				{ title: "평균 점수", name: "voteAvg", type: "number", align: "center" },
				{ title: "5점", name: "cnt5", type: "number", align: "center" },
				{ title: "4점", name: "cnt4", type: "number", align: "center" },
				{ title: "3점", name: "cnt3", type: "number", align: "center" },
				{ title: "2점", name: "cnt2", type: "number", align: "center" },
				{ title: "1점", name: "cnt1", type: "number", align: "center" },
	       ]
	   });
	   
	});

</script>

</head>
<body>

	<div class="container">

		<div class="row">
			<div class="col-sm-12">
				<div style="margin-top: 20px; margin-bottom: 20px;"></div>
			</div>
		</div>

		<div class="row">
			<div class="col-sm-12">
				<h5 id="falseUserHeader">미투표 인원</h5>
				<div id="falseUserDiv"></div>
			</div>
		</div>

		<div class="row">
			<div class="col-sm-12">
				<div style="margin-top: 20px; margin-bottom: 20px;"></div>
			</div>
		</div>

		<div class="row">
			<div class="col-sm-12">
				<h5 id="trueUserHeader">투표 인원</h5>
				<div id="trueUserDiv"></div>
			</div>
		</div>
		
		<div class="row">
			<div class="col-sm-12">
				<div style="margin-top: 20px; margin-bottom: 20px;"></div>
			</div>
		</div>
		
		<div class="row">
			<div class="col-sm-12">
				<h5>투표 결과 통계</h5>
				<div id="statisticsDiv"></div>
			</div>
		</div>

		<div class="row">
			<div class="col-sm-12">
				<div class="text-center" style="margin-top: 20px; margin-bottom: 20px;">
					<a href="./logout.do">[로그아웃]</a>
				</div>
			</div>
		</div>

		<!-- The Modal -->
		<div class="modal" id="myModal">
			<div class="modal-dialog modal-lg">
				<div class="modal-content">
					<!-- Modal Header -->
					<div class="modal-header">
						<h4 class="modal-title">이미지 상세</h4>
						<button type="button" class="close" data-dismiss="modal">&times;</button>
					</div>
					<!-- Modal body -->
					<div class="modal-body">
						<img id="imagePreview" style="width: 100%;">
					</div>
				</div>
			</div>
		</div>

	</div>
	
</body>
</html>