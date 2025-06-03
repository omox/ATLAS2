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
			<legend><b><%=titleName%></b></legend>
			<div class="cell">
				<fieldset class="outline">
					<legend><%=DefineReport.Label.LABELT1.getView() %></legend>
					<div class="cell">
						<label><%=DefineReport.Label.LABELT2.getView() %></label>
						<select id="<%=DefineReport.Select.YM_KIJUN.getObj() %>" style= "width:100px;"></select>
					</div>

					<div class="cell">
						<label><%=DefineReport.Select.CHANEL.getView() %></label>
						<select id="<%=DefineReport.Select.CHANEL.getObj() %>"style= "width:80px;">
							<option value="<%=DefineReport.OptionChanel.OPTION1.getVal() %>"><%=DefineReport.OptionChanel.OPTION1.getView() %></option>
							<option value="<%=DefineReport.OptionChanel.OPTION2.getVal() %>"><%=DefineReport.OptionChanel.OPTION2.getView() %></option>
							<option value="<%=DefineReport.OptionChanel.OPTION3.getVal() %>"><%=DefineReport.OptionChanel.OPTION3.getView() %></option>
						</select>
					</div>
				</fieldset>
			</div>

			<div class="cell">
				<fieldset class="outline">
					<legend><%=DefineReport.RadT_03.NAME.getView() %></legend>
					<label for="<%=DefineReport.RadT_03.ID1.getObj() %>">
						<input type="radio" name="<%=DefineReport.RadT_03.NAME.getObj() %>" id="<%=DefineReport.RadT_03.ID1.getObj() %>" value="<%=DefineReport.RadT_03.ID1.getVal() %>" checked
						/><%=DefineReport.RadT_03.ID1.getView() %></label>
					<label for="<%=DefineReport.RadT_03.ID2.getObj() %>">
						<input type="radio" name="<%=DefineReport.RadT_03.NAME.getObj() %>" id="<%=DefineReport.RadT_03.ID2.getObj() %>" value="<%=DefineReport.RadT_03.ID2.getVal() %>"
						/><%=DefineReport.RadT_03.ID2.getView() %></label>
				</fieldset>
			</div>
			<div class="cell">
				<fieldset class="outline">
					<legend><%=DefineReport.RadT_05.NAME.getView() %></legend>
					<label for="<%=DefineReport.RadT_05.ID1.getObj() %>">
						<input type="radio" name="<%=DefineReport.RadT_05.NAME.getObj() %>" id="<%=DefineReport.RadT_05.ID1.getObj() %>" value="<%=DefineReport.RadT_05.ID1.getVal() %>" checked
						/><%=DefineReport.RadT_05.ID1.getView() %></label>
					<label for="<%=DefineReport.RadT_05.ID3.getObj() %>">
						<input type="radio" name="<%=DefineReport.RadT_05.NAME.getObj() %>" id="<%=DefineReport.RadT_05.ID3.getObj() %>" value="<%=DefineReport.RadT_05.ID3.getVal() %>"
						/><%=DefineReport.RadT_05.ID3.getView() %></label>
					<label for="<%=DefineReport.RadT_05.ID4.getObj() %>">
						<input type="radio" name="<%=DefineReport.RadT_05.NAME.getObj() %>" id="<%=DefineReport.RadT_05.ID4.getObj() %>" value="<%=DefineReport.RadT_05.ID4.getVal() %>"
						/><%=DefineReport.RadT_05.ID4.getView() %></label>
				</fieldset>
			</div>
		</fieldset>
	</div>
	<div id="btn_form">
		<input type="button" id="<%=DefineReport.Button.SEARCH.getObj()%>"
			class="btn01" value="<%=DefineReport.Button.SEARCH.getView()%>"/>
		<input type="button" id="<%=DefineReport.Button.EXCEL.getObj()%>"
			class="btn01" value="<%=DefineReport.Button.EXCEL.getView()%>"/>
		<label id="<%=DefineReport.Label.CHECK.getObj() %>"></label>
	</div>
</form>
<div class="line">【チャネル「通販」「すべて」の場合：ACT顧客 <= R12】　【「店舗」の場合：ACT顧客 <= R6】 </div>
</div>
<div id="MAIN_REPORT">
	<!-- EasyUI グリッド -->
	<table id="gridholder" class="placeFace">
	</table>
</div>

<div id="debug">
	<!-- report 情報 -->
	<input type="hidden" name="reportno" id="reportno" value="<%=reportNo %>" />
	<!-- レポート名 情報 -->
	<input type="hidden" name="reportname" id="reportname" value="<%=reportName %>" />
	<!-- ユーザー 情報 -->
	<input type="hidden" name="userid" id="userid" value="<%=userId %>" />
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
	src="../js/jquery.report/jquery.report.option.T_262.js"></script>
<script type="text/javascript"
	src="../js/jquery.report/jquery.report.control.js"></script>
<script type="text/javascript"
	src="../js/jquery.report/jquery.report.events.js"></script>

</html>