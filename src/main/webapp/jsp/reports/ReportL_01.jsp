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
<style type="text/css">
input[type="radio"],input[type="checkbox"]{
  position: relative;
  top: 2px;
}
</style>
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
			<fieldset style="border-style: none;">
				<div class="cell">
					<label><%=DefineReport.Select.YM_KIJUN.getView() %></label>
					<select id="<%=DefineReport.Select.YY_KIJUN.getObj() %>" style= "width:70px;" ></select>
					<select id="<%=DefineReport.Select.MM_KIJUN.getObj() %>" style= "width:50px;" ></select>
					<select id="<%=DefineReport.Select.DD_KIJUN.getObj() %>" style= "width:50px;" ></select>
				</div>
					<fieldset class="outline">
					<legend>期間種別選択</legend>
						<div class="cell">
						<label for="rad_1_1">
							<input type="radio" name="radio_category_1" id="rad_1_1" value="1" >
							時間表示
						</label>
						</div>
						<div class="cell">
						<label for="rad_1_2">
							<input type="radio" name="radio_category_1" id="rad_1_2" value="2" checked="checked" >
							日別
						</label>
						</div>
						<div class="cell">
						<label for="rad_1_3"><input type="radio" name="radio_category_1" id="rad_1_3" value="3"  >月別日別一覧</label>
						</div>
						<div class="cell">
						<label for="rad_1_4"><input type="radio" name="radio_category_1" id="rad_1_4" value="4"  >月別</label>
						</div>
				</fieldset>
					<fieldset class="outline">
					<legend>条件選択</legend>
						<div class="cell">
						<label for=rad_2_1>
							<input type="radio" name="radio_category_2" id="rad_2_1" value="1"  >
							メニュー別
						</label>
						</div>
						<div class="cell">
						<label for="rad_2_2">
							<input type="radio" name="radio_category_2" id="rad_2_2" value="2" >
							ユーザー別
						</label>
						</div>
						<div class="cell">
						<label for="rad_2_3">
							<input type="radio" name="radio_category_2" id="rad_2_3" value="3" checked="checked" >
							ユーザー・メニュー別
						</label>
						</div>
				</fieldset>
			</fieldset>
		</div>
		<div id="btn_form">
			<input type="button" id="<%=DefineReport.Button.SEARCH.getObj()%>"
				class="btn01" value="<%=DefineReport.Button.SEARCH.getView()%>"/>
			<input type="button" id="<%=DefineReport.Button.EXCEL.getObj()%>"
				class="btn01" value="<%=DefineReport.Button.EXCEL.getView()%>"/>
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
<script type="text/javascript"	src="../js/jquery.report/jquery.report.option.L_01.js?t=<%System.currentTimeMillis();%>"></script>
<script type="text/javascript"	src="../js/jquery.report/jquery.report.control.js?t=<%System.currentTimeMillis();%>"></script>
<script type="text/javascript"	src="../js/jquery.report/jquery.report.events.js?t=<%System.currentTimeMillis();%>"></script>

</html>