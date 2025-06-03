<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="MS932" %>
<%@ page import="org.apache.commons.lang.StringUtils"%>
<%@ page import="authentication.defines.*" %>
<%@ page import="authentication.bean.*" %>

<%
String path = request.getContextPath();
String logo = "../img/ATLAS_logo.png";
User lusr = (User)session.getAttribute(Consts.STR_SES_LOGINUSER);
if (lusr != null){
	if (StringUtils.isNotEmpty(lusr.getLogo_())){
		logo = lusr.getLogo_();
	}
}
%>


<%
if ((request.getParameter(Form.LOGIN_VIEW) == null) && (session.getAttribute(Consts.STR_SES_LOGINUSER) != null)){
	// ログオン画面表示時のパラメータ情報を取得
	String User = session.getAttribute("_"+Form.LOGIN_USER)==null ? "" : (String)session.getAttribute("_"+Form.LOGIN_USER);
	String Pass = session.getAttribute("_"+Form.LOGIN_PASS)==null ? "" : (String)session.getAttribute("_"+Form.LOGIN_PASS);
	String View = session.getAttribute("_"+Form.LOGIN_VIEW)==null ? "" : (String)session.getAttribute("_"+Form.LOGIN_VIEW);
	String Parameter = "";
	if (!"".equals(User)){
		// ログアウト時に初期化するパラメータ情報
		Parameter = "?"+Form.LOGIN_USER+"="+User+"&"+Form.LOGIN_PASS+"="+Pass+"&"+Form.LOGIN_VIEW+"="+View;
	}
	String menu = session.getAttribute("m")==null ? "" : (String)session.getAttribute("m");
%>

<div id="top_title">
<%
if ((request.getParameter(Form.LOGIN_VIEW) == null) && (session.getAttribute(Consts.STR_SES_LOGINUSER) != null)){
%>
<a href="<%=path%>/Servlet/Dashboard?m=<%=menu%>"><img src="<%=logo %>" alt="ATLAS"></a>
<%
} else {
%>
<img src="<%=logo %>" alt="ATLAS">
<%
}
%>
</div>

<div id="infomation">
	<div id="user_info">
		<a>ユーザー：</a><a><%= "　"+lusr.getId()+"　"+lusr.getName().replace("/", "　") %></a><br>
		<a>　　　　　　　</a>
		<a href="<%=path%>/Servlet/Dashboard">ダッシュボード</a>
		<a>&nbsp;</a>
		<a href="<%=path%>/Servlet/Dashboard?m=<%=menu%>">レポート</a>
		<a>&nbsp;</a>
		<a href="#" onClick="window.open('','_self').close();">ログアウト</a>
		<!--
		<a href="<%=path %>/Servlet/Login.do<%=Parameter %>">ログアウト</a>
		 -->
	</div>
</div>

<%
}
%>