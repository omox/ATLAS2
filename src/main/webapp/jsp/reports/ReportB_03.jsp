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
	// セッションID
	String sid		=	request.getSession().getId();
%>
<body>
<div id="AREA_FACTOR" style="width: 100%;_width:950px;">
	<div class="remarks">
		<pre>チャネル別に受注件数を集計した画面。
<font style="color:#CC0000;">※オルビス新規は受注時の暫定判定のため、正式な件数（人数）は「7-1.初回商品別獲得数」でご確認ください。</font>
</pre>
	</div>
	<div id="btn_form">
		<input type="button" id="<%=DefineReport.Button.SEARCH.getObj()%>"
			class="btn01" value="<%=DefineReport.Button.SEARCH.getView()%>"/>
		<input type="button" id="<%=DefineReport.Button.EXCEL.getObj()%>"
			class="btn01" value="<%=DefineReport.Button.EXCEL.getView()%>"/><br>
		<input type="button" id="<%=DefineReport.Button.SEARCH2.getObj()%>"
			class="btn01" value="経路別照会"/>
		<label id="<%=DefineReport.Label.CHECK.getObj() %>"></label>
	</div>
<form name="ReportT" id="ReportT" method="POST" style="width:1080px;">
	<div id="main_form1" style="width:900px;">
		<fieldset style="border-style: none;">
			<legend></legend>
			<table>
			<tr>
			<td>
			<div class="cell" >
				<%=DefineReport.Select.YY_KIJUN.getView() %><br>
				<select id="<%=DefineReport.Select.YY_KIJUN.getObj() %>" style= "width:55px;"></select>
			</div>
			</td>
			<td>
			<div class="cell" >
				<%=DefineReport.Select.MM_KIJUN.getView() %><br>
				<select id="<%=DefineReport.Select.MM_KIJUN.getObj() %>" style= "width:40px;"></select>
			</div>
			</td>
			<td>
			<fieldset>
				<legend><%=DefineReport.Label.LABELB4j.getView() %></legend>
				<div class="line" style="height:1.8em;">
					<label class="for">　</label>
					<label for="<%=DefineReport.CheckB_3.ID1.getObj()%>" class="for"><input type="checkbox" name="<%=DefineReport.CheckB_3.NAME.getObj()%>" id="<%=DefineReport.CheckB_3.ID1.getObj()%>" checked="checked"><span id="<%=DefineReport.CheckB_3.ID1.getObj()%>S"><%=DefineReport.CheckB_3.ID1.getView()%></span></label>
					<label class="for">　</label>
					<label for="<%=DefineReport.CheckB_3.ID2.getObj()%>" class="for"><input type="checkbox" name="<%=DefineReport.CheckB_3.NAME.getObj()%>" id="<%=DefineReport.CheckB_3.ID2.getObj()%>" checked="checked"><span id="<%=DefineReport.CheckB_3.ID2.getObj()%>S"><%=DefineReport.CheckB_3.ID2.getView()%></span></label>
					<label class="for">　</label>
					<label for="<%=DefineReport.CheckB_3.ID3.getObj()%>" class="for"><input type="checkbox" name="<%=DefineReport.CheckB_3.NAME.getObj()%>" id="<%=DefineReport.CheckB_3.ID3.getObj()%>" checked="checked"><span id="<%=DefineReport.CheckB_3.ID3.getObj()%>S"><%=DefineReport.CheckB_3.ID3.getView()%></span></label>
					<label class="for">　</label>
					<label for="<%=DefineReport.CheckB_3.ID4.getObj()%>" class="for"><input type="checkbox" name="<%=DefineReport.CheckB_3.NAME.getObj()%>" id="<%=DefineReport.CheckB_3.ID4.getObj()%>" checked="checked"><span id="<%=DefineReport.CheckB_3.ID4.getObj()%>S"><%=DefineReport.CheckB_3.ID4.getView()%></span></label>
					<label class="for">　</label>
					<label for="<%=DefineReport.CheckB_3.ID5.getObj()%>" class="for"><input type="checkbox" name="<%=DefineReport.CheckB_3.NAME.getObj()%>" id="<%=DefineReport.CheckB_3.ID5.getObj()%>" checked="checked"><span id="<%=DefineReport.CheckB_3.ID5.getObj()%>S"><%=DefineReport.CheckB_3.ID5.getView()%></span></label>
					<label class="for">　</label>
					<label for="<%=DefineReport.CheckB_3.ID6.getObj()%>" class="for"><input type="checkbox" name="<%=DefineReport.CheckB_3.NAME.getObj()%>" id="<%=DefineReport.CheckB_3.ID6.getObj()%>" checked="checked"><span id="<%=DefineReport.CheckB_3.ID6.getObj()%>S"><%=DefineReport.CheckB_3.ID6.getView()%></span></label>
					<label class="for">　</label>
					<label for="<%=DefineReport.CheckB_3.ID7.getObj()%>" class="for"><input type="checkbox" name="<%=DefineReport.CheckB_3.NAME.getObj()%>" id="<%=DefineReport.CheckB_3.ID7.getObj()%>" checked="checked"><span id="<%=DefineReport.CheckB_3.ID7.getObj()%>S"><%=DefineReport.CheckB_3.ID7.getView()%></span></label>
					<label class="for">　</label>
					<label for="<%=DefineReport.CheckB_3.ID8.getObj()%>" class="for"><input type="checkbox" name="<%=DefineReport.CheckB_3.NAME.getObj()%>" id="<%=DefineReport.CheckB_3.ID8.getObj()%>" checked="checked"><span id="<%=DefineReport.CheckB_3.ID8.getObj()%>S"><%=DefineReport.CheckB_3.ID8.getView()%></span></label>
					<label class="for">　</label>
					<label for="<%=DefineReport.CheckB_3.ID9.getObj()%>" class="for"><input type="checkbox" name="<%=DefineReport.CheckB_3.NAME.getObj()%>" id="<%=DefineReport.CheckB_3.ID9.getObj()%>" checked="checked"><span id="<%=DefineReport.CheckB_3.ID9.getObj()%>S"><%=DefineReport.CheckB_3.ID9.getView()%></span></label>
					<select name="<%=DefineReport.Select.TENPO.getObj() %>" id="<%=DefineReport.Select.TENPO.getObj() %>" style= "width:150px;"></select>
					<input type="button" tabindex=-1 class="btn01" id="<%=DefineReport.Button.RESET.getObj()%>" value="<%=DefineReport.Button.RESET.getView()%>"  align="right">
				</div>
			</fieldset>
			</td>
			</tr>
			<tr>
			<td colspan="3">
				<div class="cell" style="float: left; width:400px;">
					<label id="<%=DefineReport.Label.TSU.getObj() %>"></label>
				</div>
				<div class="cell" style="width:200px;">
					<label id="<%=DefineReport.Label.TSU2.getView() %>"></label>
					<!--
					<input type="text" id="<%=DefineReport.Label.TSU2.getObj() %>" readonly="readonly" style="text-align:left;padding:0;height:17px;width:200px;" />
					 -->
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
<script type="text/javascript" src="../js/jquery.report/jquery.report.option.B_03.js?t=<%System.currentTimeMillis();%>"></script>
<script type="text/javascript" src="../js/jquery.report/jquery.report.control.js?t=<%System.currentTimeMillis();%>"></script>
<script type="text/javascript" src="../js/jquery.report/jquery.report.events.js?t=<%System.currentTimeMillis();%>"></script>

</html>