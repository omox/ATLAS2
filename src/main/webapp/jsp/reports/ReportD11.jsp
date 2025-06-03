<!-- 通店プロモ商品速報 -->
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
	<div class="remarks">
		<pre>当月プロモーション対象商品の販売状況を確認できる画面。
　</pre>
	</div>
		<div id="btn_form">
			<input type="button" id="<%=DefineReport.Button.SEARCH.getObj()%>"	class="btn01" value="<%=DefineReport.Button.SEARCH.getView()%>"	disabled/>
			<input type="button" id="<%=DefineReport.Button.EXCEL.getObj()%>"		class="btn01" value="<%=DefineReport.Button.EXCEL.getView()%>"	disabled/>
			<br style="clear: both;"/>
			<input type="button" id="<%=DefineReport.Button.ITEM.getObj()%>"		class="btn01" value="<%=DefineReport.Button.ITEM.getView()%>"		disabled/>
			<label id="<%=DefineReport.Label.CHECK.getObj() %>" style="padding: 3px 2px;"></label>
		</div>

	<div id="tb" style="padding:2px;height:auto;display:none;">
		<div id="main_form">
			<table style="font-size:102%;"><!-- 照会条件 -->
			<tr>
				<td>
					<fieldset>
						<legend><%=DefineReport.Label.LABELD01.getView()%></legend>
						<table class="vertical-align-em">
						<tr>
							<td><label class="labelwidth"><%=DefineReport.RadD01_CHANEL.NAME.getView()%></label></td>
							<td>
								<label for="<%=DefineReport.RadD01_CHANEL.ID1.getObj()%>" class="for">
									<input type="radio" name="<%=DefineReport.RadD01_CHANEL.NAME.getObj()%>" id="<%=DefineReport.RadD01_CHANEL.ID1.getObj()%>" value="<%=DefineReport.RadD01_CHANEL.ID1.getVal()%>" checked = checked/><%=DefineReport.RadD01_CHANEL.ID1.getView()%>
								</label>
								<label class="for">　</label>
								<label for="<%=DefineReport.RadD01_CHANEL.ID2.getObj()%>" class="for">
									<input type="radio" name="<%=DefineReport.RadD01_CHANEL.NAME.getObj()%>" id="<%=DefineReport.RadD01_CHANEL.ID2.getObj()%>" value="<%=DefineReport.RadD01_CHANEL.ID2.getVal()%>"/><%=DefineReport.RadD01_CHANEL.ID2.getView()%>
								</label>
								<label class="for">　　</label>
								<label for="<%=DefineReport.RadD01_CHANEL.ID3.getObj()%>" class="for">
									<input type="radio" name="<%=DefineReport.RadD01_CHANEL.NAME.getObj()%>" id="<%=DefineReport.RadD01_CHANEL.ID3.getObj()%>" value="<%=DefineReport.RadD01_CHANEL.ID3.getVal()%>"/><%=DefineReport.RadD01_CHANEL.ID3.getView()%>
								</label>
							</td>
						</tr>
						</table>
					</fieldset>
				</td>
				<td>
					<label style= "width:10px;"></label>
				</td>
				<td>
					<fieldset>
						<legend><%=DefineReport.Label.LABELD02.getView()%></legend>
						<table class="vertical-align-em">
						<tr>
							<td><label><%=DefineReport.Label.LABELD03.getView()%></label></td>
							<td>
								<select name="<%=DefineReport.Select.YY_KIJUN.getObj() %>" id="<%=DefineReport.Select.YY_KIJUN.getObj() %>"></select>
								<a class="flat">年</a>
								<select name="<%=DefineReport.Select.MM_KIJUN.getObj() %>" id="<%=DefineReport.Select.MM_KIJUN.getObj() %>"></select>
								<a class="flat">月</a>
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
							<td><label class="labelwidth"><%=DefineReport.RadD14_CLS.NAME.getView()%></label></td>
							<td>
								<label for="<%=DefineReport.RadD14_CLS.ID1.getObj()%>" class="for">
									<input type="radio" name="<%=DefineReport.RadD14_CLS.NAME.getObj()%>" id="<%=DefineReport.RadD14_CLS.ID1.getObj()%>" value="<%=DefineReport.RadD14_CLS.ID1.getVal()%>"/><%=DefineReport.RadD14_CLS.ID1.getView()%>
								</label>
							</td>
							<td>
								<label for="<%=DefineReport.RadD14_CLS.ID2.getObj()%>" class="for">
									<input type="radio" name="<%=DefineReport.RadD14_CLS.NAME.getObj()%>" id="<%=DefineReport.RadD14_CLS.ID2.getObj()%>" value="<%=DefineReport.RadD14_CLS.ID2.getVal()%>"/><%=DefineReport.RadD14_CLS.ID2.getView()%>
								</label>
							</td>
							<td>
								<label for="<%=DefineReport.RadD14_CLS.ID3.getObj()%>" class="for">
									<input type="radio" name="<%=DefineReport.RadD14_CLS.NAME.getObj()%>" id="<%=DefineReport.RadD14_CLS.ID3.getObj()%>" value="<%=DefineReport.RadD14_CLS.ID3.getVal()%>"/><%=DefineReport.RadD14_CLS.ID3.getView()%>
								</label>
							</td>
							<td>
								<label for="<%=DefineReport.RadD14_CLS.ID4.getObj()%>" class="for">
									<input type="radio" name="<%=DefineReport.RadD14_CLS.NAME.getObj()%>" id="<%=DefineReport.RadD14_CLS.ID4.getObj()%>" value="<%=DefineReport.RadD14_CLS.ID4.getVal()%>"/><%=DefineReport.RadD14_CLS.ID4.getView()%>
								</label>
							</td>
							<td>
								<label for="<%=DefineReport.RadD14_CLS.ID5.getObj()%>" class="for">
									<input type="radio" name="<%=DefineReport.RadD14_CLS.NAME.getObj()%>" id="<%=DefineReport.RadD14_CLS.ID5.getObj()%>" value="<%=DefineReport.RadD14_CLS.ID5.getVal()%>" checked = checked/><%=DefineReport.RadD14_CLS.ID5.getView()%>
								</label>
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

<div style="display: none;">
	<jsp:include page="Report_Columnn_N3.jsp" flush="true"></jsp:include>
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
<script type="text/javascript" src="../js/jquery.report/jquery.report.option.D11.js?t=<%System.currentTimeMillis();%>"></script>
<script type="text/javascript" src="../js/jquery.report/jquery.report.control.js?t=<%System.currentTimeMillis();%>"></script>
<script type="text/javascript" src="../js/jquery.report/jquery.report.Columns.D11.js?t=<%System.currentTimeMillis();%>"></script>
<script type="text/javascript" src="../js/jquery.report/jquery.report.events.js?t=<%System.currentTimeMillis();%>"></script>

</html>