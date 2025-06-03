<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="common.Defines"%>
<%@ page import="common.DefineReport"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Expires" content="Thu, 01 Dec 1994 16:00:00 GMT">
<link rel="stylesheet" type="text/css" href="../css/common.css">
<link rel="stylesheet" type="text/css" href="../themes/easyui/default/easyui.css">
<link rel="stylesheet" type="text/css" href="../themes/easyui/icon.css">
<link rel="stylesheet" type="text/css" href="../themes/easyui/default/custom_easyui.css">
<title></title>
</head>
<%
	// レポート番号
	String reportNo		=	(String)request.getSession().getAttribute(Defines.ID_REQUEST_JSP_REPORT);
	// ユーザ・セッション取得
	String userId		=	(String)request.getSession().getAttribute(Defines.ID_REQUEST_USER_ID);
	// 制限値取得
	String ro			=	(String)request.getSession().getAttribute(Defines.ID_REQUEST_CUSTOM_REPORT);
	// レポート名取得
	String reportName	=	(String)request.getSession().getAttribute(Defines.ID_REQUEST_TITLE_REPORT);
	// ユーザID
	String user			=	(String)request.getSession().getAttribute(Defines.ID_REQUEST_CUSTOM_USER);
	// レポートID
	String report		=	(String)request.getSession().getAttribute(Defines.ID_REQUEST_REPORT_NO);

	// タイトル
	String titleName = "【 " + reportName + " 】";

	// 親画面からの引き継ぎ情報
	String sendParam	= "";
	if (request.getSession().getAttribute(Defines.ID_REQUEST_SEND_PARAM)!=null) {
		sendParam	=	(String)request.getSession().getAttribute(Defines.ID_REQUEST_SEND_PARAM);
	}
%>
<body>
<div id="AREA_FACTOR">
<form name="ReportT" id="ReportT" method="POST">
	<div id="main_form1">
		<fieldset>
			<legend>
				<b><%=titleName%></b>
			</legend>

			<div class="cell">
				<label><%=DefineReport.Select.YY_KIJUN.getView() %></label>
				<select name="<%=DefineReport.Select.YY_KIJUN.getObj() %>" id="<%=DefineReport.Select.YY_KIJUN.getObj() %>"  style= "width:55px;"></select>
			</div>
			<div class="cell">
				<label><%=DefineReport.Select.MM_KIJUN.getView() %></label>
				<select name="<%=DefineReport.Select.MM_KIJUN.getObj() %>" id="<%=DefineReport.Select.MM_KIJUN.getObj() %>"  style= "width:40px;"></select>
			</div>
			<div class="cell">
				<br/>
				<input type="button" id="<%=DefineReport.Select.TENPOG.getObj() %>" class="btn01" value="<%=DefineReport.Select.TENPOG.getView()%>">
			</div>
			<div class="cell">
				<label>軸選択</label>
				<select name="<%=DefineReport.Select.H_TYPE2.getObj() %>" id="<%=DefineReport.Select.H_TYPE2.getObj() %>"  style= "width:150px;" >
					<option value="<%=DefineReport.OptionType11.OPTION1.getVal() %>"><%=DefineReport.OptionType11.OPTION1.getView() %></option>
					<option value="<%=DefineReport.OptionType11.OPTION2.getVal() %>"><%=DefineReport.OptionType11.OPTION2.getView() %></option>
				</select>
			</div>
		</fieldset>
		<div class="line" style="font-size:98%; color:red;"><br/>※店舗の純売上は消化仕入調整額を反映しています。</div>
	</div>
	<div id="btn_form">
		<div class="line">
			<input type="button" id="<%=DefineReport.Button.SEARCH.getObj()%>" class="btn01" value="<%=DefineReport.Button.SEARCH1.getView()%>"/>
			<input type="button" id="<%=DefineReport.Button.EXCEL.getObj()%>" class="btn01" value="<%=DefineReport.Button.EXCEL.getView()%>"/>
		</div>
		<div class="line">
			<input type="button" id="<%=DefineReport.Button.SEARCH2.getObj()%>" class="btn01" value="<%=DefineReport.Button.SEARCH2.getView()%>"/>
			<input type="button" id="<%=DefineReport.Button.ITEM.getObj()%>" class="btn01" value="<%=DefineReport.Button.ITEM.getView()%>"/>
		</div>
		<input type="button" id="<%=DefineReport.Button.SEARCH3.getObj()%>" class="btn01" value="<%=DefineReport.Button.SEARCH3.getView()%>"/><br>
		<label id="<%=DefineReport.Label.CHECK.getObj() %>"></label>
	</div>
</form>
</div>
<div id="MAIN_REPORT">
	<!-- EasyUI グリッド -->
	<table id="gridholder" class="placeFace">
	</table>
</div>
<div style="display: none;">
	<jsp:include page="Report_Columnn_T.jsp" flush="true"></jsp:include>
</div>
<div id="debug">
	<!-- report 情報 -->
	<input type="hidden" name="reportno" id="reportno" value="<%=reportNo %>" />
	<!-- レポート名 情報 -->
	<input type="hidden" name="reportname" id="reportname" value="<%=reportName %>" />
	<!-- ユーザー 情報 -->
	<input type="hidden" name="userid" id="userid" value="<%=userId %>" />
	<!-- 引き継ぎ情報(JSON文字列変換済 "'" で囲む) -->
	<input type="hidden" name="hiddenParam" id="hiddenParam" value='<%=sendParam %>' />
	<!-- ユーザID -->
	<input type="hidden" name="hiddenUser" id="hiddenUser" value="<%=user%>" />
	<!-- レポートID -->
	<input type="hidden" name="hiddenReport" id="hiddenReport" value="<%=report%>" />
</div>
</body>

<!-- Load jQuery and JS files -->
<script type="text/javascript" src="../js/jquery.min.js"></script>
<!-- jquery -->
<script type="text/javascript" src="../js/jquery.easyui.min.js"></script>
<!-- EasyUI framework -->
<script type="text/javascript" src="../js/easyui-lang-ja.js"></script>

<script type="text/javascript" src="../js/json2.min.js"></script>
<!-- json plugin -->
<script type="text/javascript" src="../js/exdate.js"></script>
<!-- exdate plugin -->

<script type="text/javascript" src="../js/jshashtable-2.1.js"></script>
<!-- jshashset plugin -->
<script type="text/javascript" src="../js/jquery.numberformatter.min.js"></script>
<!-- numberformatter plugin -->

<!-- Report Option & Control & Event  -->
<script type="text/javascript"
	src="../js/jquery.report/jquery.report.option.B_02.js"></script>
<script type="text/javascript"
	src="../js/jquery.report/jquery.report.control.js"></script>
<script type="text/javascript" src="../js/jquery.report/jquery.report.Columns.js"></script>
<script type="text/javascript"
	src="../js/jquery.report/jquery.report.events.js"></script>

</html>