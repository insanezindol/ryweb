
/**************************************************************************
 * LNB 영역 메뉴를 현재 페이지에 맞게 선택
 * 
 * @param String menuId       : LNB의 메뉴 ID, lnb.jsp의 id값 참조
***************************************************************************/
function cfLNBMenuSelect(menuId) {
	$("#"+menuId).addClass('active');
	$("#"+menuId+" ul").show();
	$("#"+menuId+" a").addClass('toggled');
}
function cfLNBChildMenuSelect(menuId) {
	$("#"+menuId).addClass('active');
	$("#"+menuId+" a").addClass('toggled');
}

/**************************************************************************
 * 페이징 처리 HTML을 가져온다.
 * 
 * @param String totalCnt       : 전체 레코드 수
 * @param String startRowNo     : 시작 페이지 번호
 * @param String pageSize       : 한번에 보여줄 레코드 개수
 * @param String getListFncName : 다른 페이지로 이동할때 호출될 메소드명
 * @returns String htmlStr      : 페이징 처리 영역에 들어갈 html
***************************************************************************/
function cfGetPagingHtml(totalCnt, pageNo, pageSize, getListFncName) {
	var htmlStr = '';       //반환할 페이징 처리 html
	htmlStr = '<div class="btn-group" role="group">';
	
	var iTotalCnt = parseInt(totalCnt + "");
	var iPageNo = parseInt(pageNo + "");
	var iPageSize = parseInt(pageSize + "");
	var displayPageCnt = 10; //화면에 보여줄 페이지 번호 개수(<<1 2 3 4 5 6 7 8 9 10 >> 일때 10개)
	var displayPageBlock = 0; //현재 페이지 블럭의 첫번째 페이지 번호 1~10=1, 11~20=11, 21~30=21
	var totalPageCnt = 0;   //전체 페이지 수
				
	//전체 페이지 수 계산
	totalPageCnt = Math.floor(iTotalCnt / iPageSize);
	if((iTotalCnt % iPageSize) > 0) {
		totalPageCnt++;
	}	
	// 첫번째 블럭 계산
	displayPageBlock = Math.floor(((iPageNo-1)/displayPageCnt)) * displayPageCnt + 1;
	
	if(displayPageBlock > 1) {
		//처음 페이지
		htmlStr += '<button class="btn btn-default waves-effect" type="button" onclick=\"javascript:'+getListFncName+'(1);\">처음</button>';
		//htmlStr += "<span id=\"pageSpan\" class=\"page_btn1\" onclick=\""+getListFncName+"(1)\">|&lt;</span>\n";
		//이전 10개
		htmlStr += '<button class="btn btn-default waves-effect" type="button" onclick=\"javascript:'+getListFncName+'('+ (displayPageBlock - 1)  +');\">이전</button>';
		//htmlStr += '<button class="btn btn-default" type="button" onclick=\"javascript:'+getListFncName+'('+ (displayPageBlock - displayPageCnt)  +');\">이전</button>';
		//htmlStr += "<span id=\"pageSpan\" class=\"page_btn1\" onclick=\""+getListFncName+"("+ (displayPageBlock - displayPageCnt)  +")\">&lt;</span>\n";
	}
	
	var i = 1;
	while(i <= displayPageCnt && displayPageBlock <= totalPageCnt) {
		if(displayPageBlock == iPageNo) {
			// 현재 페이지
			htmlStr += '<button class="btn bg-green waves-effect waves-light" type="button">'+displayPageBlock+'</button>';
			//htmlStr += "<span id=\"currentPageSpan\" class=\"page_btn2\">"+displayPageBlock+"</span>\n";	 
		}else{
			// 이전/다음 페이지
			htmlStr += '<button class="btn btn-default waves-effect" type="button" onclick=\"javascript:'+getListFncName+'('+displayPageBlock+');\">'+displayPageBlock+'</button>';
			//htmlStr += "<span id=\"pageSpan\" class=\"page_btn1\" onclick=\""+getListFncName+"("+displayPageBlock+")\">"+displayPageBlock+"</span>\n";
		}
		i++;
		displayPageBlock++;
	}	
	
	if(displayPageBlock <= totalPageCnt) {
		//다음 10개
		htmlStr += '<button class="btn btn-default waves-effect" type="button" onclick=\"javascript:'+getListFncName+'('+ displayPageBlock +');\">다음</button>';
		//htmlStr += "<span id=\"pageSpan\" class=\"page_btn1\" onclick=\""+getListFncName+"("+ displayPageBlock +")\">&gt;</span>\n";
		//마지막 페이지
		htmlStr += '<button class="btn btn-default waves-effect" type="button" onclick=\"javascript:'+getListFncName+'('+ totalPageCnt +');\">마지막</button>';
		//htmlStr += "<span id=\"pageSpan\" class=\"page_btn1\" onclick=\""+getListFncName+"("+ totalPageCnt +")\">&gt;|</span>\n";
	}
	
	htmlStr += '</div>';
	return htmlStr;
}

/**************************************************************************
 * LNB 영역 메뉴를 현재 페이지에 맞게 선택
 * 
 * @param String param       : 체크해야 할 파라메터 JSON 객체
 * @param String checkTypeList       : 체크해야 할 파라메터 문자열, '||' 구분
 * 
 * @returns String boolean      : 파라메터 체크 결과
***************************************************************************/
function cfCheckSearchCondition(param, checkTypeList) {
	var items = checkTypeList.split('||');
	
	for (var i in items) {
		if(items[i] == "searchType") {
			if (param.searchType == '' || param.searchType == null) {
				alert('검색 조건을 선택해주세요.');
				$("#searchType").focus();
				return false;
			}
		} else if(items[i] == "searchText") {
			if (param.searchText == '' || param.searchText == null) {
				alert('검색어를 입력해주세요.');
				$("#searchText").focus();
				return false;
			}
		} else if(items[i] == "startDate") {
			if (param.startDate != "" && param.endDate == "") {
				alert('종료일을 선택해주세요.');
				return false;
			}
		} else if(items[i] == "endDate") {
			if (param.endDate != "" && param.startDate == "") {
				alert('시작일을 선택해주세요.');
				return false;
			} else if (param.startDate != "" && param.endDate != "") {
				if (param.startDate > param.endDate) {
					alert('시작일이 종료일보다 클 수 없습니다.');
					return false;
				}
			}
		} else if(items[i] == "searchDept") {
			if (param.searchText == '' || param.searchText == null) {
				alert('부서를 선택해주세요.');
				$("#searchDept").focus();
				return false;
			}
		} else if(items[i] == "searchStatus") {
			if (param.searchText == '' || param.searchText == null) {
				alert('상태를 선택해주세요.');
				$("#searchStatus").focus();
				return false;
			}
		}
	}
	return true;
}

/**************************************************************************
 * 상단 메인 이미지 클릭 이벤트
***************************************************************************/
function cfGoMain(){
	location.href="/main.do";
}

/**************************************************************************
 * 상단 메인 이미지 클릭 이벤트
***************************************************************************/
function cfLogin(){
	location.href="/login.do";
}


/***************************************************************
 * 전화번호 형식 표시
 * 
 * @param 전화번호
 * @return 변형된 전화번호
****************************************************************/
function cfPhoneFormat(num){
	return num.replace(/(^02.{0}|^01.{1}|[0-9]{3})([0-9]+)([0-9]{4})/,"$1-$2-$3");
}

/***************************************************************
 * 문자열 길이 제한
 * 
 * @param String textid : 체크해야 할 필드 ID
 * @param String limit : 제한할 길이
 * @return boolean
 ****************************************************************/
function cfLimitChar(textid, limit) {
	var text = $('#' + textid).val();
	if (text.length > limit) {
		alert("입력단어는 "+limit+"자 이하로 입력해 주세요.");
		$('#' + textid).val(text.substr(0, limit));
		$('#' + textid).focus();
		return false;
	} else {
		return true;
	}
}

/***************************************************************
 * 비밀번호 정규식 체크
 * 체크 규칙 : 영문, 숫자, 특수문자를 혼합하여 8~15자리 이내
 * 
 * @param 비밀번호
 * @return 정규식 체크 결과 (true|false)
****************************************************************/
function cfPasswordCheck(pwd) {
	// []안에 들어가는 규칙과 관련한 [, ], -, _ 4개는 escape 대상, javascript 기본 escape \ escape 대상
	// 시작(/^) 종료($/)를 따로 썼으니 ', "는 escape 대상 아님
	//escape 전 정규식 문자열 : (?=.*[a-zA-Z])(?=.*[0-9])(?=.*[~`!@#$%^&*()-_+={}[]\|;:'"<>,./?]).{1,15}
	var pwdRegex = /^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[~`!@#$%^&*()\-\_+={}\[\]|";:<>,.\\\/?]).{8,15}$/;

	if (!pwdRegex.test(pwd)){
		//alert("패스워드 체크 에러");
		return false;
	}/*    else { //for test
		alert("패스워드 체크 성공");
		return false; 
	} */
	
	return true;
}

/***************************************************************
 * 영문숫자 정규식 체크
 * 체크 규칙 : 영문, 숫자로만 구성된 문자열인지 체크
 * 
 * @param 문자열
 * @return 정규식 체크 결과 (true|false)
****************************************************************/
function cfAlphaNumericCheck(str) {
	var strRegex = /^[A-Za-z0-9]*$/;
	
	if (!strRegex.test(str)){
		//alert("영문숫자 체크 에러");
		return false;
	}/*    else { //for test
		alert("영문숫자 체크 성공");
		return false; 
	}*/
	
	return true;		
}

/***************************************************************
 * ID 정규식 체크
 * 체크 규칙 : ID 형식에 맞는 문자열인지 체크
 * 
 * @param 문자열
 * @return ID 정규식 체크 결과 (true|false)
****************************************************************/
function cfIDCheck(str) {
	// 영문, 숫자 + 특수문자 4종(@ . - _) 허용
	var strRegex = /^[A-Za-z0-9.\-\_@]*$/;
	
	if (!strRegex.test(str)){
		//alert("영문숫자일부특수문자 체크 에러");
		return false;
	}/*    else { //for test
		alert("영문숫자일부특수문자 체크 성공");
		return false; 
	}*/
	
	return true;		
}

/***************************************************************
 * 이메일 정규식 체크
 * 체크 규칙 : 이메일 형식에 맞는 문자열인지 체크
 * 
 * @param 문자열
 * @return 이메일 정규식 체크 결과 (true|false)
****************************************************************/
function cfEmailCheck(str) {
	var emailRegex = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/;
	
	if (!emailRegex.test(str)){
		//alert("이메일 체크 에러");
		return false;
	}/*    else { //for test
		alert("이메일 체크 성공");
		return false; 
	}*/
	
	return true;	
	
}

/***************************************************************
 * 전화번호 형식 체크
 * 
 * @param 전화번호
 * @return 전화번호 정규식 체크 결과 (true|false)
****************************************************************/
function cfPhoneNumberCheck(phoneNumber) {
	var phoneNumberRegex = /^(01[016789]{1}|02|0[3-9]{1}[0-9]{1})[0-9]{3,4}?[0-9]{4}$/;

	if (!phoneNumberRegex.test(phoneNumber)){
		//alert("전화번호 체크 에러");
		return false;
	}/* else { //for test
		alert("전화번호 체크 성공");
		return false; 
	}*/
	
	return true;
}

/***************************************************************
 * IP 정규식 체크
 * 체크 규칙 : IP 형식(IP version 4)에 맞는 문자열인지 체크
 * 
 * @param 문자열
 * @return IP 정규식 체크 결과 (true|false)
****************************************************************/
function cfIPCheck(str) {
	var strRegex = /^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$/;
	
	if (!strRegex.test(str)){
		//alert("IP 체크 에러");
		return false;
	}/*  else { //for test
		alert("IP 체크 성공");
		return false; 
	} */
	
	return true;		
}


/***************************************************************
 * 숫자 인지 확인
 * 체크 규칙 : isNaN을 사용하여 숫자를 사용하는지 확인
 * 
 * @param 문자열
 * @return 숫자인지 체크 결과 (true|false)
****************************************************************/
function cfIsNumber(str) {
	str += ''; // 문자열로 변환
	str = str.replace(/^\s*|\s*$/g, ''); // 좌우 공백 제거
	if (str == '' || isNaN(str))
		return false;
	return true;
}


/***************************************************************
 * DATE 패턴 숫자 인지 확인
 * 체크 규칙 : YYYY-MM-DD 형식에 맞는 문자열인지 체크
 * 
 * @param 문자열
 * @return DATE 패턴 숫자인지 체크 결과 (true|false)
****************************************************************/
function cfIsDatePattern(str) {
	var strRegex = /[0-9]{4}-[0-9]{2}-[0-9]{2}/;
	
	if (!strRegex.test(str)){
		return false;
	}
	
	return true;	
}


/***************************************************************
 * 존재하는 날짜인지 체크
 * 체크 규칙 : 존재하는 날짜 인지 체크
 * 
 * @param 문자열
 * @return DATE 패턴 숫자인지 체크 결과 (true|false)
****************************************************************/
function cfIsExistDate(str) {
	// 존재하는 날자(유효한 날자) 인지 체크
	var bDateCheck = true;
	var arrDate = str.split("-");
	var nYear = Number(arrDate[0]);
	var nMonth = Number(arrDate[1]);
	var nDay = Number(arrDate[2]);

	if (nYear < 1900 || nYear > 3000) {
		bDateCheck = false;
	}

	if (nMonth < 1 || nMonth > 12) {
		bDateCheck = false;
	}

	// 해당달의 마지막 일자 구하기
	var nMaxDay = new Date(new Date(nYear, nMonth, 1) - 86400000).getDate();
	if (nDay < 1 || nDay > nMaxDay) {
		bDateCheck = false;
	}

	if (bDateCheck == false) {
		return false;
	} else {
		return true;
	}
}


/*******************************************************************************
 * 숫자 타입에서 쓸 수 있도록 format() 함수 추가
 * 
 * @param 숫자
 * 
 * @return 천단위 , 찍기
 ******************************************************************************/
Number.prototype.format = function(){
    if(this==0) return 0;
 
    var reg = /(^[+-]?\d+)(\d{3})/;
    var n = (this + '');
 
    while (reg.test(n)) n = n.replace(reg, '$1' + ',' + '$2');
 
    return n;
};


/***************************************************************
 * 문자열 타입에서 쓸 수 있도록 format() 함수 추가
 * 
 * @param 문자열
 * 
 * @return 천단위 , 찍기
****************************************************************/
String.prototype.format = function(){
    var num = parseFloat(this);
    if( isNaN(num) ) return "0";
 
    return num.format();
};


/***************************************************************
 * 쿠키 설정
 * 
 * @param String cookieName     : 설정할 쿠키 이름
 * @param String value          : 설정할 쿠키 값
 * @param String exdays         : 유효기간(day수)
 * 
 * @returns void
****************************************************************/
function setCookie(cookieName, value, exdays) {
	var exdate = new Date();
	exdate.setDate(exdate.getDate() + exdays);
	var cookieValue = escape(value) + ((exdays == null) ? "" : "; expires=" + exdate.toGMTString());
	document.cookie = cookieName + "=" + cookieValue;
}


/***************************************************************
 * 쿠키 삭제
 * 
 * @param String cookieName     : 삭제할 쿠키 이름
 * 
 * @returns void
****************************************************************/
function deleteCookie(cookieName) {
	var expireDate = new Date();
	expireDate.setDate(expireDate.getDate() - 1);
	document.cookie = cookieName + "= " + "; expires=" + expireDate.toGMTString();
}


/***************************************************************
 * 쿠키 값 가져오기
 * 
 * @param String cookieName     : 가지고 올 쿠키 이름
 * 
 * @returns String              : 쿠키 값
****************************************************************/
function getCookie(cookieName) {
	cookieName = cookieName + '=';
	var cookieData = document.cookie;
	var start = cookieData.indexOf(cookieName);
	var cookieValue = '';
	if (start != -1) {
		start += cookieName.length;
		var end = cookieData.indexOf(';', start);
		if (end == -1)
			end = cookieData.length;
		cookieValue = cookieData.substring(start, end);
	}
	return unescape(cookieValue);
}

/***************************************************************
 * 로딩바 팝업 열기
 * 
 * @param
 * 
 * @returns void
****************************************************************/
function cfOpenMagnificPopup() {
	$.magnificPopup.open({
		items: {
			src: '#loadingPopup',
		},
		type: 'inline',
		removalDelay: 200,
		mainClass: 'my-mfp-slide-bottom',
		showCloseBtn: false,
		closeOnBgClick: false,
		enableEscapeKey: false,
	});
}

/***************************************************************
 * 로딩바 팝업 닫기
 * 
 * @param
 * 
 * @returns void
****************************************************************/
function cfCloseMagnificPopup() {
	$('#loadingPopup').magnificPopup('close');
}

/***************************************************************
 * 바이트 체크
 * 
 * @param String obj          : 계산할 오브젝트
 * @param String maxByte      : 최대 바이트 값
 * @param String spanObj      : 계산 값이 나올 span ID
 * 
 * @returns void
****************************************************************/
function cfChkByte(obj, maxByte, spanObj){
	var str = obj.value;
	var str_len = str.length;

	var rbyte = 0;
	var rlen = 0;
	var one_char = "";
	var str2 = "";

	for(var i=0; i<str_len; i++){
		one_char = str.charAt(i);
		if (escape(one_char).length > 4) {
			rbyte += 2; // 한글2Byte
		} else {
			rbyte++; // 영문 등 나머지 1Byte
		}

		if(rbyte <= maxByte){
			rlen = i+1; // return할 문자열 갯수
		}
	}

	if (rbyte > maxByte) {
	    alert("한글 "+(maxByte/2)+"자 / 영문 "+maxByte+"자를 초과 입력할 수 없습니다.");
	    str2 = str.substr(0,rlen);
	    obj.value = str2;
	    fnChkByte(obj, maxByte);
	} else {
		$("#"+spanObj).text(rbyte);
	}
}

/***************************************************************
 * 화폐 단위 3자리마다 콤마 찍기
 * 
 * @param
 * 
 * @returns void
****************************************************************/
function cfNumberWithCommas(x) {
	return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}