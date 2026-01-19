<!DOCTYPE html>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="MS932" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="authentication.bean.*" %>
<%@ page import="authentication.defines.*" %>
<%@ page import="authentication.defines.Defines" %>
<%@ page import="authentication.dbaccess.DBinfo" %>
<%@ page import="authentication.io.FileReadWriter" %>
<%
	User lusr = (User)request.getSession().getAttribute(Consts.STR_SES_LOGINUSER);
	ArrayList menu = (ArrayList)request.getSession().getAttribute(Consts.STR_SES_REPSIDE);
	ArrayList rowdisp = (ArrayList)request.getSession().getAttribute("DISP");
	ArrayList mstr = (ArrayList)request.getSession().getAttribute(Consts.STR_SES_MSTSIDE);
	String repside = (String)request.getParameter(Form.REPORT_SIDE);
	String info = (String)request.getAttribute(Form.MENU_INFO_BTN);

	//パスワード有効期限警告メッセージ取得
	String pw_warning_msg = request.getAttribute(Defines.STR_SES_PASS_TERM_MSG) != null ? (String)request.getAttribute(Defines.STR_SES_PASS_TERM_MSG) : "";
	//初期化
	request.removeAttribute(Defines.STR_SES_PASS_TERM_MSG);
%>

<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Expires" content="Thu, 01 Dec 1994 16:00:00 GMT">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="Content-Script-Type" content="text/javascript">
<title>ATLAS</title>

<script type="text/javascript">
//パスワード有効期限警告メッセージ
	var msg_pw_warning="<%= pw_warning_msg %>";
	window.onload = function setFormat(){
		//パスワード警告メッセージ表示
		if (msg_pw_warning != ""){
			alert(msg_pw_warning);
		}
	}
function submit_forward(action,nm,val){
	var frm = document.forms[0];
 	var query = document.createElement("input");
	query.type = "hidden";
	query.name = nm;
	query.value = val;
	frm.appendChild(query);
	frm.action = action;

	try {
		frm.submit();
	} catch(e) {
		if (e.number == -2147024891) {
			alert("ファイル名をご確認下さい。");
		}
	}
}
</script>

<link rel="stylesheet" type="text/css" href="../frame/css/menu.css" />
</head>

<body>
<!-- ***** container ***** -->
<div id="container">

<!-- ===  header === -->
<div id="header">
	<jsp:include page="header.jsp" flush="true" />
</div>
<!-- ===  /header=== -->

<!-- ===  content === -->
<div id="content">
<form id="frm_repout" name="frm_repout" method="post">
	<h2>レポート画面</h2>
	<div style="font-size:80%;">
	　※2020年9月より店舗ショッパー分を含んだ個数・金額を表示しています<br>
	　※2021年3月より店舗ポイントカード保有者で個人情報登録のない人にも顧客番号が採番されるようになりました
	</div>

	<%--  エラーメッセージ領域 --%>
<%
if(request.getAttribute(Form.COMMON_MSG) != null){
%>
	<p class=<%= request.getAttribute(Form.COMMON_MSGTYPE).toString() %>>
		<%=request.getAttribute(Form.COMMON_MSG).toString()%>
	</p>
<%
}
%>
	<%-- /エラーメッセージ領域 --%>

	<div id="menu">
	<div id="menu_info" class="m_child_max">
		<h3>お知らせ</h3>
<%
	// ------------------------------
	// DB更新状況取得
	// ------------------------------
	DBinfo dbinfo = new DBinfo();

	// ATLAS用
	// お知らせ管理(1件)
	String szInfo = dbinfo.getInfoValue();

	if(lusr.getPos() == Consts.SYSTEM_MASTER){
		if(info!=null && info.equals(Form.MENU_INFO_EDIT)){
%>
		<input id="btn_info" type="hidden" value="保存" class="btn1" onclick="submit_forward('../Servlet/Menu.do','<%= Form.MENU_INFO_BTN %>','<%= Form.MENU_INFO_SAVE %>')">
<%
		}else{
%>
		<input id="btn_info" type="hidden" value="編集" class="btn1" onclick="submit_forward('../Servlet/Menu.do','<%= Form.MENU_INFO_BTN %>','<%= Form.MENU_INFO_EDIT %>')">
<%
		}
	}
	if(info!=null && info.equals(Form.MENU_INFO_EDIT)){
%>
		<textarea id="<%=Form.MENU_INFO_VALUE %>" name="<%=Form.MENU_INFO_VALUE %>"><%= szInfo %></textarea>
<%
	}else{
%>
		<p><%= szInfo %></p>
<%
	}
%>
	</div>

<%
long di =0;
String columnNo="";
String columnNoOld="";
String className="";

if (menu==null || menu.size()==0) {
%>
	<div class="m_child">
		<h3>本システムを利用することは出来ません</h3>
		<p style="height:300px;">各種ﾚﾎﾟｰﾄを閲覧する権限が与えられていません。</p>
	</div>
<%
} else {
%>
	<div id="menu_report" class="m_child_max">
		<h3>各種レポート</h3>
		<p>閲覧したいレポートを選択して下さい。</p>
<%
for(int i = 0;i<rowdisp.size();i++){
	// 列番号の取得
	columnNo=rowdisp.get(i).toString();
	// 現行の列番号と前回の列番号を比較して異なる場合、列用のメニューを書き込む
	if(!columnNoOld.equals(columnNo)){
%>
<%-- 大分類レポートの設定と行切り替え--%>
	</div>
<%
	if (i<rowdisp.size()-1){
%>
	<div id="menu_report" class="m_child">
<%
	}else{
%>
	<div id="menu_report" class="m_child_fix">
<%
	}

// メニューの区分名称を追加（固定）
	switch(columnNo){
	case "1":	className="売上"; break;
	case "2":	className="顧客分析"; break;
	case "3":	className="評価指標"; break;
	default:	className=""; break;
	}
	String m = (String)request.getParameter("m");
	if ("0".equals(m) && !"".equals(className)){
%>
		<div class="classBack"><div class="classFront"><pre class="classText"><%= className %></pre></div></div>
<%
	}
%>
	<%--// 大分類レポートの設定と行切り替え--%>
<%
	}
	int no_len = 0;
	ArrayList report = null;
	no_len = i;
	Side m = (Side)menu.get(i);
	report = m.getReport();

	for(int k = 0; k< report.size() ;k++){
		Report rep = (Report)report.get(k);
		if(rep.getReport_boolean()){
			//reportsideが同じものを出力
			if(i == no_len ){
%>
<ul>
	<li>
	<span><a class="btnIconBackground" href="../Servlet/Report.do?<%= Form.REPORT_SIDE %>=<%= m.getSide() %>
		&<%= Form.REPORT_SIDE_ARRAY %>=<%= i %> "><%= m.getSidename() %><!-- 列番号：<%=rowdisp.get(i)%>  -->
	</a></span>
	<ol>
<%
				no_len = no_len +1;
			}
		}//レポートに値があるか判定しているif文
	}//report.sizeのfor文
%>
		<jsp:include page="report_list.jsp">
			<jsp:param value="<%= Integer.toString(m.getSide()) %>" name="ReportSide" />
			<jsp:param value="FullName" name="ReportNameType" />
			<jsp:param value="<%=i %>" name="ReportSide_array" />
			<jsp:param value="1" name="HiddenReport" />
		</jsp:include>
	</ol>
	</li>
</ul>
<%
	// 現行の列番号を保持する
	columnNoOld = columnNo;
} // for 大外
%>
</div>
<%
}
if(columnNo.equals("3")){
%>
	<div id="menu_report" class="m_child_w">
	</div>
<%
}

if(mstr!=null && mstr.size()!=0){
%>
<div id="menu_system" class="m_child_max">
	<h3>システム管理用メニュー</h3>
	<p>当システムの情報を閲覧する事ができます。</p>
</div>
<div id="menu_report" class="m_child_fix">
<ul>
<li>
<%
int no_len = 0;
ArrayList report = null;
for(int i=0; i < mstr.size(); i++){
	no_len = i;
	Side m = (Side)mstr.get(i);
	report = m.getReportMst();
	for(int k = 0; k< report.size() ;k++){
		//reportsideが同じものを出力
		if(i == no_len){
%>
	<span><a class="btnIconBackground"><%= m.getSidename() %></a></span>
	<ol>
<%
			no_len = no_len +1;
		}
	}	//report.sizeのfor文
%>
	<jsp:include page="meintenance_report_list.jsp">
		<jsp:param value="<%= Integer.toString(m.getSide()) %>" name="ReportSide" />
		<jsp:param value="FullName" name="ReportNameType" />
	</jsp:include>
<%
}
%>
	</ol>
	</li>
</ul>
</div>
<%
} else {
%>
<div id="menu_report" class="m_child_w">
</div>
<%
}
%>
<div id="menu_report" class="m_child_w">
  <a href="../../ATLASO/Servlet/Login.do?User=<%= lusr.getDBFormatId() %>&KB=0&sc=1&m=0" target="_blank" style="text-align: right; margin-top: 15px; margin-right: 15px;">過去レポートはこちら</a>
</div>

</div>
</form>
</div>
<!-- ===  /content === -->

<!-- ===  footer === -->
<div id="footer">
</div>
<!-- === /footer === -->

</div>
<!-- ***** /container ***** -->
</body>
</html>
<script type="text/javascript" src="../js/jquery.min.js"></script>
<script type="text/javascript" src="../js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="../js/jquery.info.control.js"></script>