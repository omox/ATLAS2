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
	String reportNo	=	(String)request.getSession().getAttribute(Defines.ID_REQUEST_JSP_REPORT);
	// ユーザ・セッション取得
	String userId		=	(String)request.getSession().getAttribute(Defines.ID_REQUEST_USER_ID);
	// 制限値取得
	String ro				=	(String)request.getSession().getAttribute(Defines.ID_REQUEST_CUSTOM_REPORT);
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

<div id="AREA_FACTOR" style="width: 100%;_width:950px;">
	<div class="remarks" style="height: 100%;">
		<pre>店舗の売上・レジ件数を集計した画面。未登録件数は60日前までさかのぼり、登録があれば更新する。

</pre>
	</div>
	<div class="buttons">
		<div id="btn_form">
			<input type="button" id="<%=DefineReport.Button.SEARCH.getObj()%>"	class="btn01" value="<%=DefineReport.Button.SEARCH.getView()%>" disabled/>
			<input type="button" id="<%=DefineReport.Button.EXCEL.getObj()%>"		class="btn01" value="<%=DefineReport.Button.EXCEL.getView()%>"  disabled/>
			<label style="width: 190px;" id="<%=DefineReport.Label.CHECK.getObj() %>"></label>
		</div>
	</div>
	<br style="clear: both;"/>

	<div id="tb" style="padding:2px;height:auto;display:none;">
		<div id="main_form">
			<table style="font-size:102%;"><!-- 照会条件 -->
			<tr>
				<td>
					<fieldset>
						<legend><%=DefineReport.Label.LABELD02.getView()%></legend>
						<table class="vertical-align-em">
						<tr>
							<td><label><%=DefineReport.Label.LABELD03.getView()%></label></td>
							<td>
								<select name="<%=DefineReport.Select.YY_KIJUN.getObj() %>" id="<%=DefineReport.Select.YY_KIJUN.getObj() %>"></select>
								<a class="flat">年</a>
							</td>
						</tr>
						</table>
					</fieldset>
				</td>
			</tr>

			<tr>
				<td colspan="3">
					<fieldset>
						<legend><%=DefineReport.Label.LABELD04.getView()%></legend>
						<table class="vertical-align-em">
						<tr>
							<td><label class="labelwidth"><%=DefineReport.RadD13_COLUMN.NAME.getView()%></label></td>
							<td>
								<label for="<%=DefineReport.RadD13_COLUMN.ID1.getObj()%>" class="for">
									<input type="radio" name="<%=DefineReport.RadD13_COLUMN.NAME.getObj()%>" id="<%=DefineReport.RadD13_COLUMN.ID1.getObj()%>" value="<%=DefineReport.RadD13_COLUMN.ID1.getVal()%>" checked = checked/><%=DefineReport.RadD13_COLUMN.ID1.getView()%>
								</label>
								<label class="for">　</label>
							</td>
							<td>
								<label for="<%=DefineReport.RadD13_COLUMN.ID2.getObj()%>" class="for">
									<input type="radio" name="<%=DefineReport.RadD13_COLUMN.NAME.getObj()%>" id="<%=DefineReport.RadD13_COLUMN.ID2.getObj()%>" value="<%=DefineReport.RadD13_COLUMN.ID2.getVal()%>"/><%=DefineReport.RadD13_COLUMN.ID2.getView()%>
								</label>
								<label class="for">　</label>
							</td>
							<td>
								<label for="<%=DefineReport.RadD13_COLUMN.ID3.getObj()%>" class="for">
									<input type="radio" name="<%=DefineReport.RadD13_COLUMN.NAME.getObj()%>" id="<%=DefineReport.RadD13_COLUMN.ID3.getObj()%>" value="<%=DefineReport.RadD13_COLUMN.ID3.getVal()%>"/><%=DefineReport.RadD13_COLUMN.ID3.getView()%>
								</label>
								<label class="for">　</label>
							</td>
							<td>
								<label for="<%=DefineReport.RadD13_COLUMN.ID4.getObj()%>" class="for">
									<input type="radio" name="<%=DefineReport.RadD13_COLUMN.NAME.getObj()%>" id="<%=DefineReport.RadD13_COLUMN.ID4.getObj()%>" value="<%=DefineReport.RadD13_COLUMN.ID4.getVal()%>"/><%=DefineReport.RadD13_COLUMN.ID4.getView()%>
								</label>
								<label class="for">　</label>
								<label class="for">　</label>
								<label for="<%=DefineReport.RadD13_COLUMN.ID5.getObj()%>" class="for">
									<input type="radio" name="<%=DefineReport.RadD13_COLUMN.NAME.getObj()%>" id="<%=DefineReport.RadD13_COLUMN.ID5.getObj()%>" value="<%=DefineReport.RadD13_COLUMN.ID5.getVal()%>"/><%=DefineReport.RadD13_COLUMN.ID5.getView()%>
								</label>
							</td>
						</tr>
						<tr>
							<td></td>
							<td>
								<label for="<%=DefineReport.RadD13_COLUMN.ID6.getObj()%>" class="for">
									<input type="radio" name="<%=DefineReport.RadD13_COLUMN.NAME.getObj()%>" id="<%=DefineReport.RadD13_COLUMN.ID6.getObj()%>" value="<%=DefineReport.RadD13_COLUMN.ID6.getVal()%>"/><%=DefineReport.RadD13_COLUMN.ID6.getView()%>
								</label>
								<label class="for">　</label>
							</td>
							<td>
								<label for="<%=DefineReport.RadD13_COLUMN.ID7.getObj()%>" class="for">
									<input type="radio" name="<%=DefineReport.RadD13_COLUMN.NAME.getObj()%>" id="<%=DefineReport.RadD13_COLUMN.ID7.getObj()%>" value="<%=DefineReport.RadD13_COLUMN.ID7.getVal()%>"/><%=DefineReport.RadD13_COLUMN.ID7.getView()%>
								</label>
								<label class="for">　</label>
							</td>
							<td>
								<label for="<%=DefineReport.RadD13_COLUMN.ID8.getObj()%>" class="for">
									<input type="radio" name="<%=DefineReport.RadD13_COLUMN.NAME.getObj()%>" id="<%=DefineReport.RadD13_COLUMN.ID8.getObj()%>" value="<%=DefineReport.RadD13_COLUMN.ID8.getVal()%>"/><%=DefineReport.RadD13_COLUMN.ID8.getView()%>
								</label>
								<label class="for">　</label>
							</td>
							<td>
								<label for="<%=DefineReport.RadD13_COLUMN.ID9.getObj()%>" class="for">
									<input type="radio" name="<%=DefineReport.RadD13_COLUMN.NAME.getObj()%>" id="<%=DefineReport.RadD13_COLUMN.ID9.getObj()%>" value="<%=DefineReport.RadD13_COLUMN.ID9.getVal()%>"/><%=DefineReport.RadD13_COLUMN.ID9.getView()%>
								</label>
								<label class="for">　</label>
							</td>
							<td>
							</td>
						</tr>
						</table>
					</fieldset>
				</td>
			</tr>
			</table>
		</div>
	</div>
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
	<!-- 引き継ぎ情報(JSON文字列変換済 "'" で囲む) -->
	<input type="hidden" name="hiddenParam" id="hiddenParam" value='<%=sendParam %>' />
	<!-- ユーザID -->
	<input type="hidden" name="hiddenUser" id="hiddenUser" value="<%=user%>" />
	<!-- レポートID -->
	<input type="hidden" name="hiddenReport" id="hiddenReport" value="<%=report%>" />
</div>
</body>

<script type="text/javascript" src="../js/jquery.min.js"></script><!-- Load jQuery and JS files -->
<script type="text/javascript" src="../js/jquery.easyui.min.js"></script><!-- jquery -->
<script type="text/javascript" src="../js/easyui-lang-ja.js"></script><!-- EasyUI framework -->
<script type="text/javascript" src="../js/datagrid-scrollview.js?v=1"></script>	<!-- EasyUI framework -->

<script type="text/javascript" src="../js/json2.min.js"></script><!-- json plugin -->
<script type="text/javascript" src="../js/exdate.js"></script><!-- exdate plugin -->
<script type="text/javascript" src="../js/jshashtable-2.1.js"></script><!-- jshashset plugin -->
<script type="text/javascript" src="../js/jquery.numberformatter.min.js"></script><!-- numberformatter plugin -->

<!-- Report Option & Control & Event -->
<script type="text/javascript" src="../js/jquery.report/jquery.report.option.D31.js?t=<%System.currentTimeMillis();%>"></script>
<script type="text/javascript" src="../js/jquery.report/jquery.report.control.js?t=<%System.currentTimeMillis();%>"></script>
<script type="text/javascript" src="../js/jquery.report/jquery.report.events.js?t=<%System.currentTimeMillis();%>"></script>

</html>