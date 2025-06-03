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
<link rel="stylesheet" type="text/css" href="../css/common.css?t=<%System.currentTimeMillis();%>">
<link rel="stylesheet" type="text/css" href="../themes/easyui/default/easyui.css?t=<%System.currentTimeMillis();%>">
<link rel="stylesheet" type="text/css" href="../themes/easyui/icon.css?t=<%System.currentTimeMillis();%>">
<link rel="stylesheet" type="text/css" href="../themes/easyui/default/custom_easyui.css?t=<%System.currentTimeMillis();%>">
<link rel="stylesheet" type="text/css" href="../css/custom_d00.css?t=<%System.currentTimeMillis();%>">
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
	// セッションID
	String sid		=	request.getSession().getId();
%>
<body>
<div id="AREA_FACTOR" style="width: 100%;_width:950px;">
	<div class="remarks">
		<pre>当月末時点の顧客をRFMで集計した画面。RFM値の高い顧客は売上への貢献度が高いとされている。
※DM拒否者は集計対象外、R（最終受注日）はキャンセルを含む全受注を集計対象とし、F（累計出荷回数）/  M（累計出荷額）は出荷が完了した受注のみ集計対象とする（返品はマイナス）</pre>
	</div>
	<div id="btn_form">
		<input type="button" id="<%=DefineReport.Button.SEARCH.getObj()%>"
			class="btn01" value="<%=DefineReport.Button.SEARCH.getView()%>"/>
		<input type="button" id="<%=DefineReport.Button.EXCEL.getObj()%>"
			class="btn01" value="<%=DefineReport.Button.EXCEL.getView()%>"/>
		<label id="<%=DefineReport.Label.CHECK.getObj() %>"></label>
		<div class="line"  style="color:red"><label></label></div>
	</div>
<form name="ReportT" id="ReportT" method="POST">
	<div id="main_form1">
		<fieldset style="border-style: none;">
			<legend>
			</legend>
			<table class="tbl01">
			<tr>
			<td>
				<div class="cell">
					<label><%=DefineReport.Select.YM_KIJUN.getView() %></label>
					<select id="<%=DefineReport.Select.YY_KIJUN.getObj() %>" style= "width:70px;" ></select>
					<select id="<%=DefineReport.Select.MM_KIJUN.getObj() %>" style= "width:50px;" ></select>
				</div>
			</td>
			<td>
				<div class="cell">
					<label><%=DefineReport.Select.S_TYPE2.getView() %></label>
					<select id="<%=DefineReport.Select.S_TYPE2.getObj() %>" style= "width:150px;">
						<option value="<%=DefineReport.OptionTypeA.OPTIONT1.getVal() %>"><%=DefineReport.OptionTypeA.OPTIONT1.getView() %></option>
						<option value="<%=DefineReport.OptionTypeA.OPTIONT2.getVal() %>"><%=DefineReport.OptionTypeA.OPTIONT2.getView() %></option>
						<option value="<%=DefineReport.OptionTypeA.OPTIONT3.getVal() %>"><%=DefineReport.OptionTypeA.OPTIONT3.getView() %></option>
					</select>
				</div>
				<div class="cell">
					<label><%=DefineReport.Select.AGE.getView() %></label>
					<select id="<%=DefineReport.Select.AGE.getObj() %>" style= "width:100px;" ></select>
				</div>
				<div class="cell">
					<label><%=DefineReport.Select.AREA.getView() %></label>
					<!--
					<select id="<%=DefineReport.Select.AREA.getObj() %>" style= "width:70px;" ></select>
					 -->
					<input type="button" class="btn03" id="<%=DefineReport.Select.AREA.getObj() %>" value="<%=DefineReport.Label.SELECT.getView()%>">
				</div>
			</tr>
			<tr>
			<td>
			</td>
			<td>
				<div class="cell">
					<label><%=DefineReport.Select.CHANEL.getView() %></label>
					<select id="<%=DefineReport.Select.CHANEL.getObj() %>" style= "width:80px;" >
						<option value="<%=DefineReport.OptionChanel.OPTION3.getVal() %>"><%=DefineReport.OptionChanel.OPTION3.getView() %></option>
						<option value="<%=DefineReport.OptionChanel.OPTION1.getVal() %>"><%=DefineReport.OptionChanel.OPTION1.getView() %></option>
						<option value="<%=DefineReport.OptionChanel.OPTION2.getVal() %>"><%=DefineReport.OptionChanel.OPTION2.getView() %></option>
					</select>
				</div>
				<div class="cell">
					<label><%=DefineReport.Select.TENPO.getView() %></label>
					<select id="<%=DefineReport.Select.TENPO.getObj() %>" style= "width:250px;" disabled></select>
				</div>
				<div class="cell">
					<label>　</label>
					<select id="<%=DefineReport.Select.T_TYPE.getObj() %>"style= "width:150px;">
						<option value="<%=DefineReport.OptionType2.OPTIONT1.getVal() %>"><%=DefineReport.OptionType2.OPTIONT1.getView() %></option>
						<option value="<%=DefineReport.OptionType2.OPTIONT2.getVal() %>"><%=DefineReport.OptionType2.OPTIONT2.getView() %></option>
						<option value="<%=DefineReport.OptionType2.OPTIONT6.getVal() %>"><%=DefineReport.OptionType2.OPTIONT6.getView() %></option>
						<option value="<%=DefineReport.OptionType2.OPTIONT3.getVal() %>"><%=DefineReport.OptionType2.OPTIONT3.getView() %></option>
					</select>
				</div>
			</td>
			</tr>
			</table>
		</fieldset>
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
<script type="text/javascript" src="../js/datagrid-scrollview.js?v=1"></script>	<!-- EasyUI framework -->

<script type="text/javascript" src="../js/json2.min.js"></script>
<!-- json plugin -->
<script type="text/javascript" src="../js/exdate.js"></script>
<!-- exdate plugin -->

<script type="text/javascript" src="../js/jshashtable-2.1.js"></script>
<!-- jshashset plugin -->
<script type="text/javascript" src="../js/jquery.numberformatter.min.js"></script>
<!-- numberformatter plugin -->

<!-- Report Option & Control & Event  -->
<script type="text/javascript"	src="../js/jquery.report/jquery.report.option.T_01.js?t=<%System.currentTimeMillis();%>"></script>
<script type="text/javascript"	src="../js/jquery.report/jquery.report.control.js?t=<%System.currentTimeMillis();%>"></script>
<script type="text/javascript"	src="../js/jquery.report/jquery.report.events.js?t=<%System.currentTimeMillis();%>"></script>

</html>