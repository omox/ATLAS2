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
		<div id="main_form">
			<fieldset>
				<legend>
					<b><%=titleName%></b>
				</legend>
				<div class="cell"><br>
					<label><%=DefineReport.Select.YM_KIJUN.getView() %></label>
					<select id="<%=DefineReport.Select.YM_KIJUN.getObj() %>"style= "width:90px;"></select>
				</div>

				<div class="cell"><br>
					<label><%=DefineReport.Select.CHANEL.getView() %></label>
					<select id="<%=DefineReport.Select.CHANEL.getObj() %>">
						<option value="<%=DefineReport.OptionChanel.OPTION1.getVal() %>"
							><%=DefineReport.OptionChanel.OPTION1.getView() %></option>
						<option value="<%=DefineReport.OptionChanel.OPTION2.getVal() %>"
							><%=DefineReport.OptionChanel.OPTION2.getView() %></option>
					</select>
				</div>

				<div class="cell"><br>
					<label><%=DefineReport.Select.S_TYPE.getView() %></label>
					<select id="<%=DefineReport.Select.S_TYPE.getObj() %>">
						<option value="<%=DefineReport.OptionType.OPTIONT1.getVal() %>"
							><%=DefineReport.OptionType.OPTIONT1.getView() %></option>
						<option value="<%=DefineReport.OptionType.OPTIONT2.getVal() %>"
							><%=DefineReport.OptionType.OPTIONT2.getView() %></option>
						<option value="<%=DefineReport.OptionType.OPTIONT3.getVal() %>"
							><%=DefineReport.OptionType.OPTIONT3.getView() %></option>
						<option value="<%=DefineReport.OptionType.OPTIONT4.getVal() %>"
							><%=DefineReport.OptionType.OPTIONT4.getView() %></option>
						<option value="<%=DefineReport.OptionType.OPTIONT5.getVal() %>"
							><%=DefineReport.OptionType.OPTIONT5.getView() %></option>
						<option value="<%=DefineReport.OptionType.OPTIONT6.getVal() %>"
							><%=DefineReport.OptionType.OPTIONT6.getView() %></option>
					</select>
				</div>
				<div class="cell"><br>
					<label><%=DefineReport.Select.T_TYPE.getView() %></label>
					<select id="<%=DefineReport.Select.T_TYPE.getObj() %>">
						<option value="<%=DefineReport.OptionType2.OPTIONT1.getVal() %>"><%=DefineReport.OptionType2.OPTIONT1.getView() %></option>
						<option value="<%=DefineReport.OptionType2.OPTIONT2.getVal() %>"><%=DefineReport.OptionType2.OPTIONT2.getView() %></option>
						<option value="<%=DefineReport.OptionType2.OPTIONT6.getVal() %>"><%=DefineReport.OptionType2.OPTIONT6.getView() %></option>
						<option value="<%=DefineReport.OptionType2.OPTIONT3.getVal() %>"><%=DefineReport.OptionType2.OPTIONT3.getView() %></option>
					</select>
				</div><br><br><br><br>

				<div class="cell">
					<label><%=DefineReport.Select.PROMO.getView() %></label>
					<select id="<%=DefineReport.Select.PROMO_FROM.getObj() %>" style="width: 250px;"></select>
					 ～
					<select id="<%=DefineReport.Select.PROMO_TO.getObj() %>" style="width: 250px;"></select>
				</div>
				<div class="cell">
					<label><%=DefineReport.Select.SEGNO.getView() %></label>
					<select id="<%=DefineReport.Select.SEGNO_FROM.getObj() %>"></select>
				</div>

				<div class="cell">
					<label for="<%=DefineReport.RadioCategory.ID1.getObj()%>">
					<input type="radio"
						name="<%=DefineReport.RadioCategory.NAME.getObj() %>"
						id="<%=DefineReport.RadioCategory.ID1.getObj()%>"
						value="<%=DefineReport.RadioCategory.ID1.getVal()%>" checked
						><%=DefineReport.RadioCategory.ID1.getView()%>
					</label>
					<label for="<%=DefineReport.RadioCategory.ID2.getObj()%>">
					<input type="radio"
						name="<%=DefineReport.RadioCategory.NAME.getObj() %>"
						id="<%=DefineReport.RadioCategory.ID2.getObj()%>"
						value="<%=DefineReport.RadioCategory.ID2.getVal()%>"
						><%=DefineReport.RadioCategory.ID2.getView()%>
					</label>
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
	src="../js/jquery.report/jquery.report.option.T_04.js"></script>
<script type="text/javascript"
	src="../js/jquery.report/jquery.report.control.js"></script>
<script type="text/javascript"
	src="../js/jquery.report/jquery.report.events.js"></script>

</html>