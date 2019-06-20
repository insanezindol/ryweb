<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<sec:authentication property="principal.saupcode" var="principalSaupcode" />
<sec:authentication property="principal.deptCode" var="principalDeptCode" />
<sec:authentication property="principal.username" var="principalUsername" />
<sec:authentication property="principal.superUser" var="principalSuperUser" />
<sec:authentication property="principal.contractUser" var="principalContractUser" />

<script type="text/javascript">

</script>
	
<div class="overlay"></div>
<nav class="navbar">
	<div class="container-fluid">
		<div class="navbar-header">
			<a href="javascript:;" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar-collapse" aria-expanded="false"></a>
			<a href="javascript:;" class="bars"></a> <a class="navbar-brand" href="/main.do">REYON Pharmaceutical</a>
		</div>
		<div class="collapse navbar-collapse" id="navbar-collapse">
               <ul class="nav navbar-nav navbar-right">
                   <li class="pull-right"><a href="javascript:void(0);" class="js-right-sidebar" data-close="true"><i class="material-icons">more_vert</i></a></li>
               </ul>
           </div>
	</div>
</nav>