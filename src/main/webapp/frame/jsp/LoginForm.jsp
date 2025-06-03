<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="MS932" %>
<%@ page import="java.util.*" %>
<%@ page import="authentication.defines.*" %>
<%@ page import="authentication.bean.*" %>
<%@ page import="authentication.dbaccess.DBinfo" %>

<%
	// view 指定有りの場合は、セッションクリアしない
	if (request.getParameter(Form.LOGIN_VIEW) == null){
		session.removeAttribute(Consts.STR_SES_LOGINUSER);
	}
%>

<body onLoad="document.getElementById('User').focus()">
<!-- ***** container ***** -->
<div class="container">

	<!-- ===  content === -->
	<div class="head">
		<div style="display:none;">
		<label for="test">上余白（比率）</label>
		<input id="test" name="test" type="text" value="22" size="4" maxlength="2" style="height:26px;background-color: #eeeeee;">
		<label for="test">％</label>

		<label for="boxH">ログイン枠（高さ）</label>
		<input id="boxH" name="boxH" type="text" value="300" size="4" maxlength="3" style="height:26px;background-color: #eeeeee;">
		<label for="boxH">px</label>

		<label for="boxW">ログイン枠（幅）</label>
		<input id="boxW" name="boxW" type="text" value="400" size="4" maxlength="3" style="height:26px;background-color: #eeeeee;">
		<label for="boxW">px</label>

		<label for="fontSize">入力文字サイズ</label>
		<input id="fontSize" name="fontSize" type="text" value="0.9" size="4" maxlength="3" style="height:26px;background-color: #eeeeee;">
		<label for="fontSize">em</label>
		</div>
	</div>

	<div class="content">
		<div style="height:15%;"></div>
		<img class="logo" border="0" src="../img/ATLAS_logo.png" alt="ORBIS ATLAS">
		<div style="height:11%;"></div>
		<form id="login" style="width:100%;height:50%;" method="post" action="../Servlet/Login.do">

			<div class="loginform">
				<%-- 入力領域 --%>
				<img border="0" src="../img/labelA.png" alt="アカウント">
				<input class="text1a" type="text" id="User" name="User" style="width:100%;height:15%;">
				<div style="height:5%;"></div>
				<img border="0" src="../img/labelB.png" alt="パスワード">
				<input class="text1a" type="password" id="Pass" name="Pass" style="width:100%;height:15%;">
				<div style="height:19%;">
					<input type="hidden" id="KB" name="KB" value="1">
				</div>
				<div style="text-align:center;width:100%;height:17%;">
					<input type="image" src="../img/loginbtn.png" alt="ログイン">
				</div>
			</div>
		</form>
	</div>
	<!-- ===  /content === -->
				<%--  エラーメッセージ領域 --%>
				<%if(request.getAttribute(Form.COMMON_MSG) != null){%>
					<p class=<%= request.getAttribute(Form.COMMON_MSGTYPE).toString() %>>
						<%=request.getAttribute(Form.COMMON_MSG).toString()%>
					</p>
				<%}%>

</div>
<!-- ***** /container ***** -->
<script class="include" type="text/javascript" src="../dash/jquery.js"></script>
<script type="text/javascript">
$("#test").keypress( function ( e ) {
	if ( e.which == 13 ) {
		$(".head").css('height',$(this).val()+"%");
		return false;
	}
});
$("#test").on("change",function(){
	$(".head").css('height',$(this).val()+"%");
});
$("#boxH").keypress( function ( e ) {
	if ( e.which == 13 ) {
		$(".content").css('height',$(this).val()+"px");
		return false;
	}
});
$("#boxH").on("change",function(){
	$(".content").css('height',$(this).val()+"px");
});
$("#boxW").keypress( function ( e ) {
	if ( e.which == 13 ) {
		$(".content").css('width',$(this).val()+"px");
		return false;
	}
});
$("#boxW").on("change",function(){
	$(".content").css('width',$(this).val()+"px");
});
$("#fontSize").keypress( function ( e ) {
	if ( e.which == 13 ) {
		$(".text1a").css('font-size',$(this).val()+"em");
		return false;
	}
});
$("#fontSize").on("change",function(){
	$(".text1a").css('font-size',$(this).val()+"em");
});
</script>
</body>
