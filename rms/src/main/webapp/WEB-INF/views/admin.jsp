<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>ADMIN TEST</title>
<!-- Bootstrap CSS -->
<link href="/css/bootstrap.min.css" rel="stylesheet">
<!-- bootstrap theme -->
<link href="/css/bootstrap-theme.css?ver=20190619" rel="stylesheet">
<!--external css-->
<!-- font icon -->
<link href="/css/elegant-icons-style.css" rel="stylesheet" />
<link href="/css/font-awesome.css" rel="stylesheet" />
<!-- Custom styles -->
<link href="/css/style.css?ver=20190619" rel="stylesheet">
<link href="/css/style-responsive.css" rel="stylesheet" />
<!-- jsgrid -->
<link rel="stylesheet" href="/css/jsgrid.min.css" />
<link rel="stylesheet" href="/css/jsgrid-theme.min.css" />
<!-- jquery script -->
<script type="text/javascript" src="/js/jquery-1.8.3.min.js"></script>
<!-- AES script -->
<script src="/js/AesUtil.js"></script>
<script src="/js/aes.js"></script>
<script src="/js/pbkdf2.js"></script>
<!-- jsgrid -->
<script type="text/javascript" src="/js/jsgrid.min.js"></script>
<!-- common function -->
<script type="text/javascript" src="/js/common.js?ver=20190619"></script>
<!--[if lt IE 9]>
<script src="/js/html5shiv.js"></script>
<script src="/js/respond.min.js"></script>
<![endif]-->
<script type="text/javascript">

$(function() {
	var grid = $("#jsGrid").jsGrid({
       width: "100%",
       height: "900px", 
       filtering: true,
       autoload : true,
       inserting: false,
       editing: false,
       sorting: true,
       paging: false,
       controller : {
           loadData : function(filter) {
               var def = $.Deferred();
               $.ajax({
                   url : '/common/getTotalUserListAjax.json?auth=reyon',
                   type : 'GET',
                   contentType : 'application/json; charset=utf-8',
                   dataType : 'json'
               }).done(function(item) {
					var output;
					output = $.grep(item.resultMsg, function(item) {
						if (filter.deptName != "") {
							return item.deptName === filter.deptName;
						} else if (filter.sabun != ""){
							return item.sabun === filter.sabun;
						} else if(filter.kname != ""){
							return item.kname === filter.kname;
						}
					});
					if(output.length == 0){
					 output = item.resultMsg
					}
            	   
                   def.resolve(output);
               });
               return def.promise();
           }
       },
       fields: [
			{ title: "부서", name: "deptName", type: "text", align: "center" },
			{ title: "사번", name: "sabun", type: "text", align: "center" },
			{ title: "이름", name: "kname", type: "text", align: "center" },
			{ 
				title: "로그인",
				align: "center",
                itemTemplate: function(_, item) {
                    return $("<input>").attr("type", "button").attr("value", "로그인").on("click", function () {
                    	goLogin($(this).parent().prev().prev().text());
                    });
              	}
            },
       ]
   });
});

function goLogin(userId) {
	var params = {
			userId : userId, 
			userPwd : "reyonadmin123!@#"
	}
	
	var request = $.ajax({
		url: '/loginAjax.json'
		, type : 'POST'
		, timeout: 30000
		, data : params
		, dataType : 'json'
		, error: function(xhr, textStatus, errorThrown) {
			alert("시스템 오류가 발생했습니다.");
		}
		, success : function(json) {
			if (json.resultCode == 0) {
				
			} else {
				alert(json.resultMsg);
			}
		}
	});
}

</script>
</head>
<body>
	<div id="jsGrid"></div>
</body>
</html>