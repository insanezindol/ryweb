<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<title>이연제약 연말정산 시스템</title>
	</head>
	
	<body>
		<form name="detail" method="post" action="/nts/getData.do" enctype="multipart/form-data">
			<input type="radio" name="ntsType" id="ntsType1" value="1" checked /><label for="ntsType1">공제자료 PDF</label>&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="radio" name="ntsType" id="ntsType2" value="2" /><label for="ntsType2">공제신고서 PDF</label>
			<input type="radio" name="ntsType" id="ntsType3" value="3" /><label for="ntsType3">지급명세서 PDF</label><br>
			비밀번호:<input type="text" name="p_pwd" size="10"><br>
			파일:<input type="file" name="upload" size="50">
			<input type="submit" value="전자문서 제출">
		</form>
		<a href="/nts/sampleDownload.do">PDF 다운로드</a>
	</body>
</html>