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
		<pre>各商品購入者の年齢分布を確認できる画面。
※商品間の顧客重複あり
</pre>
	</div>
	<div id="btn_form">
		<input type="button" id="<%=DefineReport.Button.SEARCH.getObj()%>"
			class="btn01" value="<%=DefineReport.Button.SEARCH.getView()%>"/>
		<input type="button" id="<%=DefineReport.Button.EXCEL.getObj()%>"
			class="btn01" value="<%=DefineReport.Button.EXCEL.getView()%>"/>
		<label id="<%=DefineReport.Label.CHECK.getObj() %>"></label>
	</div>
<form name="ReportT" id="ReportT" method="POST">
	<div id="main_form">
		<fieldset style="border-style: none;">
			<legend></legend>
			<div class="cell">
				<fieldset class="outline">
					<legend><%=DefineReport.Label.LABELT1.getView() %></legend>
					<div class="cell">
						<label for="<%=DefineReport.RadB_1.ID1.getObj()%>">
							<input type="radio" name="<%=DefineReport.RadB_1.NAME.getObj() %>" id="<%=DefineReport.RadB_1.ID1.getObj() %>" value="<%=DefineReport.RadB_1.ID1.getVal() %>"
							/><%=DefineReport.RadB_1.ID1.getView() %>
						</label>
						<label for="<%=DefineReport.RadB_1.ID2.getObj()%>">
							<input type="radio" name="<%=DefineReport.RadB_1.NAME.getObj() %>" id="<%=DefineReport.RadB_1.ID2.getObj() %>" value="<%=DefineReport.RadB_1.ID2.getVal() %>" checked
							/><%=DefineReport.RadB_1.ID2.getView() %>
						</label>
					</div>
					<div class="cell">
						<label><%=DefineReport.Label.LABELT2.getView() %></label>
						<select id="<%=DefineReport.Select.YY_KIJUN.getObj() %>" style= "width:70px;" ></select>
						<select id="<%=DefineReport.Select.MM_KIJUN.getObj() %>" style= "width:50px;" ></select>
					</div>

					<div class="cell">
						<label><%=DefineReport.Select.CHANEL.getView() %></label>
						<select id="<%=DefineReport.Select.CHANEL.getObj() %>"style= "width:80px;">
							<option value="<%=DefineReport.OptionChanel.OPTION3.getVal() %>"><%=DefineReport.OptionChanel.OPTION3.getView() %></option>
							<option value="<%=DefineReport.OptionChanel.OPTION1.getVal() %>"><%=DefineReport.OptionChanel.OPTION1.getView() %></option>
							<option value="<%=DefineReport.OptionChanel.OPTION2.getVal() %>"><%=DefineReport.OptionChanel.OPTION2.getView() %></option>
						</select>
					</div>
				</fieldset>
			</div>
			<div class="cell">
				<fieldset class="outline">
					<legend><%=DefineReport.RadT_03.NAME.getView() %></legend>
					<label for="<%=DefineReport.RadT_03.ID1.getObj() %>">
						<input type="radio" name="<%=DefineReport.RadT_03.NAME.getObj()%>" id="<%=DefineReport.RadT_03.ID1.getObj() %>" value="<%=DefineReport.RadT_03.ID1.getVal() %>" checked
						/><%=DefineReport.RadT_03.ID1.getView() %>
					</label>
					<label for="<%=DefineReport.RadT_03.ID2.getObj() %>">
						<input type="radio" name="<%=DefineReport.RadT_03.NAME.getObj()%>" id="<%=DefineReport.RadT_03.ID2.getObj() %>" value="<%=DefineReport.RadT_03.ID2.getVal() %>"
						/><%=DefineReport.RadT_03.ID2.getView() %>
					</label>
				</fieldset>
			</div>
			<div class="line">
				<div class="cell">
					<label><input type="checkbox" id="<%=DefineReport.Check.CHK_SHO1.getObj()%>" name="<%=DefineReport.Check.CHK_SHO1.getObj()%>" checked="checked" disabled
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
					<select id="<%=DefineReport.Select.SHO_5.getObj() %>" style= "width:150px;"></select>
				</div>
				<div class="cell">
					<label for="<%=DefineReport.Check.CHK_SHO6.getObj()%>">
						<input type="checkbox" id="<%=DefineReport.Check.CHK_SHO6.getObj()%>" name="<%=DefineReport.Check.CHK_SHO6.getObj()%>"
						/><%=DefineReport.Select.SHO_6.getView() %></label>
					<select id="<%=DefineReport.Select.SHO_6.getObj() %>" style= "width:150px;"></select>
				</div>
				<div class="cell">
					<label for="<%=DefineReport.Check.CHK_ITEM.getObj()%>">
						<input type="checkbox" id="<%=DefineReport.Check.CHK_ITEM.getObj()%>" name="<%=DefineReport.Check.CHK_ITEM.getObj()%>"
						/><%=DefineReport.Check.CHK_ITEM.getView() %></label>
				</div>
			</div>
		</fieldset>
	</div>
	<div id="btn_form">
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
<script type="text/javascript"	src="../js/jquery.report/jquery.report.option.T_252.js?t=<%System.currentTimeMillis();%>"></script>
<script type="text/javascript"	src="../js/jquery.report/jquery.report.control.js?t=<%System.currentTimeMillis();%>"></script>
<script type="text/javascript"	src="../js/jquery.report/jquery.report.events.js?t=<%System.currentTimeMillis();%>"></script>

</html>