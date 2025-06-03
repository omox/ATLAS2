<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="MS932" %>
<%@ page import="authentication.defines.*" %>
<%@ page import="authentication.validation.*" %>
<!DOCTYPE html>
<html>
<head>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<META http-equiv="Content-Script-Type" content="text/javascript">
<%
	// Web.xmlから時間を取得
	String fromData = application.getInitParameter(Consts.FROM_DATA);
	String toData = application.getInitParameter(Consts.TO_DATA);

	// Web.xmlから取得した時間をセッションの中へ(他画面でも使用するため
	session.setAttribute(Consts.FROM_DATA, fromData);
	session.setAttribute(Consts.TO_DATA, toData);

	// 時間外かどうかの判定を行う
	LoginValidation vld = new LoginValidation();
	String injp = vld.closeTime(fromData,toData);
	String includejsp=application.getInitParameter(injp);
%>

<link href="../frame/css/login.css?v=2" rel="stylesheet" type="text/css">
<title>ログイン</title>
</head>

<jsp:include page="<%=includejsp %>" flush="true" />

</html>