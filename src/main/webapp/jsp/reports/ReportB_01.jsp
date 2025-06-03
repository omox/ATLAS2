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
<div id="AREA_FACTOR" style="width: 100%;_width:950px;">
	<div class="remarks">
		<pre>国内売上をチャネル別に確認できる画面。
※海外関係会社中国分は、システム変更のため、2011年7月以降更新していません。オルビス店舗の純売上は消化仕入調整額を反映しています。
</pre>
	</div>
	<div id="btn_form">
		<input type="button" id="<%=DefineReport.Button.SEARCH.getObj()%>" class="btn01" value="<%=DefineReport.Button.SEARCH1.getView()%>"/>
		<input type="button" id="<%=DefineReport.Button.EXCEL.getObj()%>" class="btn01" value="<%=DefineReport.Button.EXCEL.getView()%>"/><br>
		<input type="button" id="<%=DefineReport.Button.SEARCH2.getObj()%>" class="btn01" value="<%=DefineReport.Button.SEARCH2.getView()%>"/>
		<input type="button" id="<%=DefineReport.Button.ITEM.getObj()%>" class="btn01" value="<%=DefineReport.Button.ITEM.getView()%>"/>
		<label id="<%=DefineReport.Label.CHECK.getObj() %>" style="padding: 3px 2px;"></label>
	</div>

<form name="ReportT" id="ReportT" method="POST">
	<div id="main_form" style="width: 60em;">
		<fieldset style="border-style: none;">
			<legend></legend>
			<div  class="cell" style="width: 23em;">
				<div class="cell" style="width:15em;">
					<fieldset>
						<legend><%=DefineReport.RadB_1.NAME.getView()%></legend>
						<label for="<%=DefineReport.RadB_1.ID1.getObj()%>" class="for">
							<input type="radio" name="<%=DefineReport.RadB_1.NAME.getObj()%>"  id="<%=DefineReport.RadB_1.ID1.getObj()%>" value="<%=DefineReport.RadB_1.ID1.getVal()%>"
							/><%=DefineReport.RadB_1.ID1.getView()%></label>
						<label for="<%=DefineReport.RadB_1.ID2.getObj()%>" class="for">
							<input type="radio" name="<%=DefineReport.RadB_1.NAME.getObj()%>"  id="<%=DefineReport.RadB_1.ID2.getObj()%>" value="<%=DefineReport.RadB_1.ID2.getVal()%>"
							/><%=DefineReport.RadB_1.ID2.getView()%></label>
						<label for="<%=DefineReport.RadB_1.ID3.getObj()%>" class="for">
							<input type="radio" name="<%=DefineReport.RadB_1.NAME.getObj()%>"  id="<%=DefineReport.RadB_1.ID3.getObj()%>" value="<%=DefineReport.RadB_1.ID3.getVal()%>" checked
							/><%=DefineReport.RadB_1.ID3.getView()%></label>
					</fieldset>
				</div>
				<fieldset>
					<legend><%=DefineReport.RadB_2.NAME.getView()%></legend>
					<label for="<%=DefineReport.RadB_2.ID1.getObj()%>">
						<input type="radio" name="<%=DefineReport.RadB_2.NAME.getObj()%>"  id="<%=DefineReport.RadB_2.ID1.getObj()%>" value="<%=DefineReport.RadB_2.ID1.getVal()%>" checked
						/><%=DefineReport.RadB_2.ID1.getView()%></label>
					<label for="<%=DefineReport.RadB_2.ID2.getObj()%>">
						<input type="radio" name="<%=DefineReport.RadB_2.NAME.getObj()%>"  id="<%=DefineReport.RadB_2.ID2.getObj()%>" value="<%=DefineReport.RadB_2.ID2.getVal()%>"
						/><%=DefineReport.RadB_2.ID2.getView()%></label>
				</fieldset>
				<fieldset>
					<legend><%=DefineReport.Label.LABELB2.getView()%></legend>
					<select name="<%=DefineReport.Select.YY_KIJUN.getObj() %>" id="<%=DefineReport.Select.YY_KIJUN.getObj() %>"></select><%=DefineReport.Select.YY_KIJUN.getView()%>
					<select name="<%=DefineReport.Select.MM_KIJUN.getObj() %>" id="<%=DefineReport.Select.MM_KIJUN.getObj() %>"></select><%=DefineReport.Select.MM_KIJUN.getView()%>
					～
					<select name="<%=DefineReport.Select.YY_KIJUN_TO.getObj() %>" id="<%=DefineReport.Select.YY_KIJUN_TO.getObj() %>"></select><%=DefineReport.Select.YY_KIJUN.getView()%>
					<select name="<%=DefineReport.Select.MM_KIJUN_TO.getObj() %>" id="<%=DefineReport.Select.MM_KIJUN_TO.getObj() %>"></select><%=DefineReport.Select.MM_KIJUN.getView()%>
				</fieldset>
			</div>
			<fieldset id="<%=DefineReport.RadB_3.NAME.getObj()%>Area" class="outline" style="width:34em;">
				<legend><%=DefineReport.RadB_3.NAME.getView()%></legend>
				<div class="line">
					<label for="<%=DefineReport.RadB_3.ID1.getObj()%>" class="for">
						<input type="radio" name="<%=DefineReport.RadB_3.NAME.getObj()%>"  id="<%=DefineReport.RadB_3.ID1.getObj()%>" value="<%=DefineReport.RadB_3.ID1.getVal()%>" checked /><%=DefineReport.RadB_3.ID1.getView()%></label>
					<label for="<%=DefineReport.CheckB_1.ID1.getObj()%>" class="for">
						<input type="checkbox" name="<%=DefineReport.CheckB_1.NAME.getObj()%>" id="<%=DefineReport.CheckB_1.ID1.getObj()%>" value="<%=DefineReport.CheckB_1.ID1.getVal()%>" checked/><%=DefineReport.CheckB_1.ID1.getView()%></label>
					<label for="<%=DefineReport.CheckB_1.ID2.getObj()%>" class="for">
						<input type="checkbox" name="<%=DefineReport.CheckB_1.NAME.getObj()%>" id="<%=DefineReport.CheckB_1.ID2.getObj()%>" value="<%=DefineReport.CheckB_1.ID2.getVal()%>" checked/><%=DefineReport.CheckB_1.ID2.getView()%></label>
					<label for="<%=DefineReport.CheckB_1.ID3.getObj()%>" class="for">
						<input type="checkbox" name="<%=DefineReport.CheckB_1.NAME.getObj()%>" id="<%=DefineReport.CheckB_1.ID3.getObj()%>" value="<%=DefineReport.CheckB_1.ID3.getVal()%>" checked/><%=DefineReport.CheckB_1.ID3.getView()%></label>
					<label for="<%=DefineReport.CheckB_1.ID4.getObj()%>" class="for">
						<input type="checkbox" name="<%=DefineReport.CheckB_1.NAME.getObj()%>" id="<%=DefineReport.CheckB_1.ID4.getObj()%>" value="<%=DefineReport.CheckB_1.ID4.getVal()%>" checked/><%=DefineReport.CheckB_1.ID4.getView()%></label>
					<label for="<%=DefineReport.CheckB_1.ID5.getObj()%>" class="for">
						<input type="checkbox" name="<%=DefineReport.CheckB_1.NAME.getObj()%>" id="<%=DefineReport.CheckB_1.ID5.getObj()%>" value="<%=DefineReport.CheckB_1.ID5.getVal()%>" checked/><%=DefineReport.CheckB_1.ID5.getView()%></label>
					<label for="<%=DefineReport.CheckB_1.ID6.getObj()%>" class="for">
						<input type="checkbox" name="<%=DefineReport.CheckB_1.NAME.getObj()%>" id="<%=DefineReport.CheckB_1.ID6.getObj()%>" value="<%=DefineReport.CheckB_1.ID6.getVal()%>" checked/><%=DefineReport.CheckB_1.ID6.getView()%></label>
				</div>
				<div class="line">
					<div style="float: left;">
						<span>&nbsp;&nbsp;&nbsp;<%=DefineReport.Select.TENPO.getView() %></span>
						<select name="<%=DefineReport.Select.TENPO.getObj() %>" id="<%=DefineReport.Select.TENPO.getObj() %>" style= "width:150px;"></select>
						<span><%=DefineReport.Select.JUHAN.getView() %></span>
						<select name="<%=DefineReport.Select.JUHAN.getObj() %>" id="<%=DefineReport.Select.JUHAN.getObj() %>" style= "width:70px;"></select>
					</div>
					<div style="float: right;">
						<span><%=DefineReport.Select.YUSHU.getView()%></span>
						<input type="button" class="btn03" id="<%=DefineReport.Select.YUSHU.getObj() %>" value="<%=DefineReport.Label.SELECT.getView()%>">
					</div>
				</div>
				<div class="line">
					<div style="float: left;">
						<label for="<%=DefineReport.RadB_3.ID2.getObj()%>" class="for">
							<input type="radio" name="<%=DefineReport.RadB_3.NAME.getObj()%>"  id="<%=DefineReport.RadB_3.ID2.getObj()%>" value="<%=DefineReport.RadB_3.ID2.getVal()%>"
							/><%=DefineReport.RadB_3.ID2.getView()%></label>
						<input type="button" class="btn03" id="<%=DefineReport.Select.KANREN.getObj() %>" value="<%=DefineReport.Label.SELECT.getView()%>">
					</div>

					<div style="float: right;">
						<span><%=DefineReport.Select.ITAKU.getView() %></span>
						<input type="button" class="btn03" id="<%=DefineReport.Select.ITAKU.getObj() %>" value="<%=DefineReport.Label.SELECT.getView()%>">
						&nbsp;
						<span><%=DefineReport.Select.BRAND.getView() %></span>
						<input type="button" class="btn03" id="<%=DefineReport.Select.BRAND.getObj() %>" value="<%=DefineReport.Label.SELECT.getView()%>">
					</div>
				</div>
				<div class="line">
					<div style="float: left;">
						<label for="<%=DefineReport.RadB_3.ID3.getObj()%>" class="for">
							<input type="radio" name="<%=DefineReport.RadB_3.NAME.getObj()%>" id="<%=DefineReport.RadB_3.ID3.getObj()%>" value="<%=DefineReport.RadB_3.ID3.getVal()%>"
							/><%=DefineReport.RadB_3.ID3.getView()%></label>
					</div>
					<div style="float: right;">
						<input type="button" class="btn01" id="<%=DefineReport.Button.RESET.getObj()%>" value="<%=DefineReport.Button.RESET.getView()%>" >
					</div>
				</div>
			</fieldset>
		</fieldset>
	</div>
	<div class="line" style="height: 1.2em;">
		<label id="<%=DefineReport.Label.YUSHU.getObj()%>" class="for" style="display: none;"><span class="tit"><%=DefineReport.Label.YUSHU.getView() %></span><span></span></label>
		<label id="<%=DefineReport.Label.ITAKU.getObj()%>" class="for" style="display: none;"><span class="tit"><%=DefineReport.Label.ITAKU.getView() %></span><span></span></label>
		<label id="<%=DefineReport.Label.BRAND.getObj()%>" class="for" style="display: none;"><span class="tit"><%=DefineReport.Label.BRAND.getView() %></span><span></span></label>
		<label id="<%=DefineReport.Label.KANREN.getObj()%>" class="for" style="display: none;"><span class="tit"><%=DefineReport.Label.KANREN.getView() %></span><span></span></label>
	</div>
	<div class="line">
		<a style="color:black;">　　<%=DefineReport.Label.YOSAN.getView()%></a>
		<input type="text" id="<%=DefineReport.Label.YOSAN.getObj() %>" readonly="readonly" style="text-align:right;padding:0;width:17ex;" />
	</div>
<!--
<div class="line"  style="color:red">※他社売上の件数は商品数で表示しています。</div>
 -->
</form>
</div>
<div id="MAIN_REPORT">
	<!-- EasyUI グリッド -->
	<table id="gridholder" class="placeFace">
	</table>
</div>
<div style="display: none;">
	<jsp:include page="Report_Columnn_N.jsp" flush="true"></jsp:include>
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
<script type="text/javascript"	src="../js/jquery.report/jquery.report.option.B_01.js?t=<%System.currentTimeMillis();%>"></script>
<script type="text/javascript"	src="../js/jquery.report/jquery.report.control.js?t=<%System.currentTimeMillis();%>"></script>
<script type="text/javascript" src="../js/jquery.report/jquery.report.Columns.js?t=<%System.currentTimeMillis();%>"></script>
<script type="text/javascript"  src="../js/jquery.report/jquery.report.events.js?t=<%System.currentTimeMillis();%>"></script>

</html>