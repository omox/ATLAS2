<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="MS932" %>
<%@ page import="authentication.defines.*" %>
<%@ page import="authentication.util.*" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="ja">
<head>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<META http-equiv="Content-Script-Type" content="text/javascript">
<%
	session.removeAttribute(Consts.STR_SES_LOGINUSER);
	//web.xmlから時間情報取得
	String fromData = application.getInitParameter("FROM_DATA_MSG");
	String toData = application.getInitParameter("TO_DATA_MSG");
%>
<jsp:include page="common_head.jsp" flush="true" />
<LINK HREF="../frame/css/cmn.css" REL="stylesheet" TYPE="text/css">
<LINK HREF="../frame/css/menu.css" REL="stylesheet" TYPE="text/css">
<title>お知らせ画面</title>
</head>

<body>
<!-- ===  header === -->
<div id="header">
	<jsp:include page="header.jsp" flush="true" />
</div>
<!-- ===  /header=== -->

<!-- ***** container ***** -->
<div id="container">

<!-- ===  content === -->
<div id="content">
	<h2>ご利用時間のご案内</h2>
	<div id="WORK">
		<p>
		<!--
		<IMG src="../img/Work.gif" alt="" />
		<br/>
		 -->
		<br />
		ATLASシステムのご利用時間は <br>
		<span class="opentime"><%=fromData %> ～ <%=toData %></span> となっています。<br>
		ご利用時間までお待ち下さい。<br>
		</p>
	</div>
</div>
<!-- ===  /content === -->

</div>
<!-- ***** /container ***** -->
</body>
</html>