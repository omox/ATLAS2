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
			<legend><b><%=titleName%></b></legend>
			<div class="line">
				<div class="cell">
					<label><%=DefineReport.Select.YM_KIJUN.getView() %></label>
					<select id="<%=DefineReport.Select.YM_KIJUN.getObj() %>"style= "width:90px;"></select>
				</div>
				<div class="cell">
					<label><%=DefineReport.Select.CHANEL.getView() %></label>
					<select id="<%=DefineReport.Select.CHANEL.getObj() %>">
						<option value="<%=DefineReport.OptionChanel.OPTION1.getVal() %>"><%=DefineReport.OptionChanel.OPTION1.getView() %></option>
						<option value="<%=DefineReport.OptionChanel.OPTION2.getVal() %>"><%=DefineReport.OptionChanel.OPTION2.getView() %></option>
					</select>
				</div>
				<div class="cell">
					<label><%=DefineReport.Select.PROMO_FROM.getView() %></label>
					<div>
						<label class="for" style="width:38px;"><%=DefineReport.Label.F.getView() %></label>
						<select id="<%=DefineReport.Select.PROMO_FROM.getObj() %>" style= "width:220px;"></select>
					</div>
					<div>
						<label class="for" style="width:55px;"><%=DefineReport.Label.T.getView() %></label>
						<select id="<%=DefineReport.Select.PROMO_TO.getObj() %>" style= "width:220px;"></select>
					</div>
				</div>
				<div class="cell">
					<label><%=DefineReport.Select.SEGNO.getView() %></label>
					<input type="text" id="<%=DefineReport.Select.SEGNO.getObj() %>" style="width: 270px;　height:20px"/>
					<label style="color:green;">【入力例】1,2,5-10,15-20,25</label>
				</div>
			</div>
			<div class="line">
				<div class="cell">
					<label for="<%=DefineReport.Check.CHK_SHO1.getObj()%>">
						<input type="checkbox" id="<%=DefineReport.Check.CHK_SHO1.getObj()%>" name="<%=DefineReport.Check.CHK_SHO1.getObj()%>" checked="checked"  disabled
						 /><%=DefineReport.Select.SHO_1.getView() %></label>
					<select id="<%=DefineReport.Select.SHO_1.getObj() %>" style= "width:100px;"></select>
				</div>
				<div class="cell">
					<label for="<%=DefineReport.Check.CHK_SHO2.getObj()%>">
						<input type="checkbox" id="<%=DefineReport.Check.CHK_SHO2.getObj()%>" name="<%=DefineReport.Check.CHK_SHO2.getObj()%>"
						/><%=DefineReport.Select.SHO_2.getView() %></label>
					<select id="<%=DefineReport.Select.SHO_2.getObj() %>" style= "width:100px;"></select>
				</div>
				<div class="cell">
					<label for="<%=DefineReport.Check.CHK_SHO3.getObj()%>">
						<input type="checkbox" id="<%=DefineReport.Check.CHK_SHO3.getObj()%>" name="<%=DefineReport.Check.CHK_SHO3.getObj()%>"
						/><%=DefineReport.Select.SHO_3.getView() %></label>
					<select id="<%=DefineReport.Select.SHO_3.getObj() %>" style= "width:100px;"></select>
				</div>
				<div class="cell">
					<label for="<%=DefineReport.Check.CHK_SHO4.getObj()%>">
						<input type="checkbox" id="<%=DefineReport.Check.CHK_SHO4.getObj()%>" name="<%=DefineReport.Check.CHK_SHO4.getObj()%>"
						/><%=DefineReport.Select.SHO_4.getView() %></label>
					<select id="<%=DefineReport.Select.SHO_4.getObj() %>" style= "width:100px;"></select>
				</div>
				<div class="cell">
					<label for="<%=DefineReport.Check.CHK_SHO5.getObj()%>">
						<input type="checkbox" id="<%=DefineReport.Check.CHK_SHO5.getObj()%>" name="<%=DefineReport.Check.CHK_SHO5.getObj()%>"
						/><%=DefineReport.Select.SHO_5.getView() %></label>
					<select id="<%=DefineReport.Select.SHO_5.getObj() %>" style= "width:100px;"></select>
				</div>
				<div class="cell">
					<label for="<%=DefineReport.Check.CHK_SHO6.getObj()%>">
						<input type="checkbox" id="<%=DefineReport.Check.CHK_SHO6.getObj()%>" name="<%=DefineReport.Check.CHK_SHO6.getObj()%>"
						/><%=DefineReport.Select.SHO_6.getView() %></label>
					<select id="<%=DefineReport.Select.SHO_6.getObj() %>" style= "width:100px;"></select>
				</div>
				<div class="cell">
					<label for="<%=DefineReport.Check.CHK_ITEM.getObj()%>">
						<input type="checkbox" id="<%=DefineReport.Check.CHK_ITEM.getObj()%>" name="<%=DefineReport.Check.CHK_ITEM.getObj()%>" checked="checked"
							/><%=DefineReport.Check.CHK_ITEM.getView() %></label>
					<label for="<%=DefineReport.RadioSho1.ID1.getObj()%>">
						<input type="radio" name="<%=DefineReport.RadioSho1.NAME.getObj() %>" id="<%=DefineReport.RadioSho1.ID1.getObj()%>"
							value="<%=DefineReport.RadioSho1.ID1.getVal()%>"
							/><%=DefineReport.RadioSho1.ID1.getView()%></label>
					<label for="<%=DefineReport.RadioSho1.ID2.getObj()%>">
						<input type="radio" name="<%=DefineReport.RadioSho1.NAME.getObj() %>" id="<%=DefineReport.RadioSho1.ID2.getObj()%>"
							value="<%=DefineReport.RadioSho1.ID2.getVal()%>" checked
							/><%=DefineReport.RadioSho1.ID2.getView()%></label>
				</div>
			</div>
		</fieldset>
	</div>
	<div id="btn_form">
		<input type="button" id="<%=DefineReport.Button.SEARCH.getObj()%>" class="btn01" value="<%=DefineReport.Button.SEARCH.getView()%>"/>
		<input type="button" id="<%=DefineReport.Button.EXCEL.getObj()%>" class="btn01" value="<%=DefineReport.Button.EXCEL.getView()%>"/><br>
		<label id="<%=DefineReport.Label.CHECK.getObj() %>"></label>
		<fieldset class="outline">
			<legend><%=DefineReport.RadioSho2.NAME.getView() %></legend>
			<div>
				<label for="<%=DefineReport.RadioSho2.ID1.getObj()%>" class="for">
					<input type="radio" name="<%=DefineReport.RadioSho2.NAME.getObj() %>" id="<%=DefineReport.RadioSho2.ID1.getObj()%>" value="<%=DefineReport.RadioSho2.ID1.getVal()%>"  checked="checked"
					/><%=DefineReport.RadioSho2.ID1.getView()%></label>
				<label for="<%=DefineReport.RadioSho2.ID2.getObj()%>" class="for">
					<input type="radio" name="<%=DefineReport.RadioSho2.NAME.getObj() %>" id="<%=DefineReport.RadioSho2.ID2.getObj()%>" value="<%=DefineReport.RadioSho2.ID2.getVal()%>"
					/><%=DefineReport.RadioSho2.ID2.getView()%></label>
			</div>
		</fieldset><br><br><br>
<div class="line"  style="color:red">※客数は分類毎の数値です</div>
<br>
<label style="font-size:80%;">
<a href="../doc/dataT07.html" onclick="window.open('../doc/dataT07.html', '', 'width=700,height=400'); return false;">データの説明</a>
</label>
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
	src="../js/jquery.report/jquery.report.option.T_07a.js"></script>
<script type="text/javascript"
	src="../js/jquery.report/jquery.report.control.js"></script>
<script type="text/javascript"
	src="../js/jquery.report/jquery.report.events.js"></script>

</html>