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
.panel-tool{
	position:absolute;
	right:4px;
	top:10px;
	height: 20px;
	_left :875;	/* IE6 リサイズ時対応 */
}
.lbl_shob{
	float:left;
	width:55px;
}
.sel_shos { width:210px; }
.sel_shob { width: 80px; }
.sel_shobs{ width:210px; }
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
		<pre>各商品の出荷個数・金額をチャネル別に確認できる画面。数量の合計にサンプルは含まず。
※月別の年月指定は最大36ヶ月です。集計条件によっては表示に時間のかかることがあります。
</pre>
	</div>
	<div id="btn_form">
		<input type="button" id="<%=DefineReport.Button.SEARCH.getObj()%>" class="btn01" value="<%=DefineReport.Button.SEARCH.getView()%>"/>
		<input type="button" id="<%=DefineReport.Button.EXCEL.getObj()%>" class="btn01" value="<%=DefineReport.Button.EXCEL.getView()%>"/><br>
		<input type="button" id="collapse" class="btn01" value="結果表示"/><input type="button" id="expand" class="btn01" style="display:none;" value="条件表示"/>
		<label style="width: 190px;" id="<%=DefineReport.Label.CHECK.getObj() %>"></label>
	</div>

<form name="ReportT" id="ReportT" method="POST">
<div style="float:right;width: 310px;">
</div><br style="clear: both;"/>
<div id="tb" style="padding:2px;height:auto;display:none;">
<div id="main_form">
<table  style="font-size:102%;">
<tr>
<td valign="top">
	<!-- Left -->
	<div class="cell" style="width: 340px;">
		<!-- 照会条件 -->
		<fieldset style="border-style: none;">
			<legend></legend>
			<fieldset><legend><%=DefineReport.Label.LABELB42.getView()%></legend>
				<label for="<%=DefineReport.RadB_1.ID4.getObj()%>" class="for">
					<input type="radio" name="<%=DefineReport.RadB_1.NAME.getObj()%>" id="<%=DefineReport.RadB_1.ID4.getObj()%>" value="<%=DefineReport.RadB_1.ID4.getVal()%>" checked = checked
					/><%=DefineReport.RadB_1.ID4.getView()%></label>
				<label for="<%=DefineReport.RadB_1.ID1.getObj()%>" class="for">
					<input type="radio" name="<%=DefineReport.RadB_1.NAME.getObj()%>" id="<%=DefineReport.RadB_1.ID1.getObj()%>" value="<%=DefineReport.RadB_1.ID1.getVal()%>"
					/><%=DefineReport.RadB_1.ID1.getView()%></label>
				<label for="<%=DefineReport.RadB_1.ID2.getObj()%>" class="for">
					<input type="radio" name="<%=DefineReport.RadB_1.NAME.getObj()%>" id="<%=DefineReport.RadB_1.ID2.getObj()%>" value="<%=DefineReport.RadB_1.ID2.getVal()%>"
					/><%=DefineReport.RadB_1.ID2.getView()%></label>
				<label for="<%=DefineReport.RadB_1.ID3.getObj()%>" class="for">
					<input type="radio" name="<%=DefineReport.RadB_1.NAME.getObj()%>" id="<%=DefineReport.RadB_1.ID3.getObj()%>" value="<%=DefineReport.RadB_1.ID3.getVal()%>"
					/><%=DefineReport.RadB_1.ID3.getView()%></label>
			</fieldset>
			<fieldset><legend><%=DefineReport.Label.LABELB2.getView()%></legend>
				<select name="<%=DefineReport.Select.YY_KIJUN.getObj() %>" id="<%=DefineReport.Select.YY_KIJUN.getObj() %>"></select>
				<select name="<%=DefineReport.Select.MM_KIJUN.getObj() %>" id="<%=DefineReport.Select.MM_KIJUN.getObj() %>"></select>
				<select name="<%=DefineReport.Select.DD_KIJUN.getObj() %>" id="<%=DefineReport.Select.DD_KIJUN.getObj() %>" disabled></select>
				～
				<select name="<%=DefineReport.Select.YY_KIJUN_TO.getObj() %>" id="<%=DefineReport.Select.YY_KIJUN_TO.getObj() %>" disabled></select>
				<select name="<%=DefineReport.Select.MM_KIJUN_TO.getObj() %>" id="<%=DefineReport.Select.MM_KIJUN_TO.getObj() %>" disabled></select>
				<select name="<%=DefineReport.Select.DD_KIJUN_TO.getObj() %>" id="<%=DefineReport.Select.DD_KIJUN_TO.getObj() %>" disabled></select>
			</fieldset>
			<fieldset><legend><%=DefineReport.Label.LABELB43.getView()%></legend>
				<div class="line">
					<select name="<%=DefineReport.Select.H_TYPE2.getObj() %>" id="<%=DefineReport.Select.H_TYPE2.getObj() %>" style= "width:100px;">
						<option value="1">数量</option>
						<option value="2">金額（税抜）</option>
					</select>
					　<%=DefineReport.Label.LABELB4d.getView()%>
					<select name="<%=DefineReport.Select.H_TYPE3.getObj() %>" id="<%=DefineReport.Select.H_TYPE3.getObj() %>" style= "width:80px;">
						<option value="1">なし</option>
						<option value="2">前年比</option>
						<option value="3">前月比</option>
					</select>
				</div>
				<label class="for"><%=DefineReport.Label.LABELB44.getView()%></label>
				<label for="<%=DefineReport.RadB_4.ID1.getObj()%>" class="for">
					<input type="radio" name="<%=DefineReport.RadB_4.NAME.getObj()%>" id="<%=DefineReport.RadB_4.ID1.getObj()%>" value="<%=DefineReport.RadB_4.ID1.getVal()%>" checked = checked disabled
					/><%=DefineReport.RadB_4.ID1.getView()%></label>
				<label for="<%=DefineReport.RadB_4.ID2.getObj()%>" class="for">
					<input type="radio" name="<%=DefineReport.RadB_4.NAME.getObj()%>" id="<%=DefineReport.RadB_4.ID2.getObj()%>" value="<%=DefineReport.RadB_4.ID2.getVal()%>" disabled
					/><%=DefineReport.RadB_4.ID2.getView()%></label>
			</fieldset>
			<fieldset><legend><%=DefineReport.Label.LABELB45.getView()%></legend>
				<label for="<%=DefineReport.RadB_5.ID1.getObj()%>" class="for">
					<input type="radio" name="<%=DefineReport.RadB_5.NAME.getObj()%>" id="<%=DefineReport.RadB_5.ID1.getObj()%>" value="<%=DefineReport.RadB_5.ID1.getVal()%>"checked= checked
					/><%=DefineReport.RadB_5.ID1.getView()%></label>
				<label for="<%=DefineReport.RadB_5.ID2.getObj()%>" class="for">
					<input type="radio" name="<%=DefineReport.RadB_5.NAME.getObj()%>" id="<%=DefineReport.RadB_5.ID2.getObj()%>" value="<%=DefineReport.RadB_5.ID2.getVal()%>"
					/><%=DefineReport.RadB_5.ID2.getView()%></label>
				<select name="<%=DefineReport.Select.YM_NR.getObj() %>" id="<%=DefineReport.Select.YM_NR.getObj() %>" style= "width:80px;" disabled ></select>
				～
				<select name="<%=DefineReport.Select.YM_NR_TO.getObj() %>" id="<%=DefineReport.Select.YM_NR_TO.getObj() %>" style= "width:80px;" disabled ></select>
			</fieldset>
		</fieldset>
		<!-- /照会条件 -->
		<!-- チャネル -->
		<fieldset style="border-style: none;">
			<legend></legend>
			<fieldset><legend><%=DefineReport.Label.LABELB48.getView()%></legend>
				<table class="tbl01">
				<tr>
					<td>
						<label for="<%=DefineReport.RadB_7a.ID1.getObj()%>" class="for">
							<input type="radio" name="<%=DefineReport.RadB_7a.NAME.getObj()%>" id="<%=DefineReport.RadB_7a.ID1.getObj()%>" value="<%=DefineReport.RadB_7a.ID1.getVal()%>"checked= checked
							/><%=DefineReport.RadB_7a.ID1.getView()%></label>
					</td>
					<td colspan="2">
						<label for="<%=DefineReport.RadB_7a.ID2.getObj()%>" class="for">
							<input type="radio" name="<%=DefineReport.RadB_7a.NAME.getObj()%>" id="<%=DefineReport.RadB_7a.ID2.getObj()%>" value="<%=DefineReport.RadB_7a.ID2.getVal()%>"
							/><%=DefineReport.RadB_7a.ID2.getView()%></label>
					</td>
				</tr>
				<tr>
					<td>
						<label for="<%=DefineReport.RadB_7a.ID3.getObj()%>" class="for">
							<input type="radio" name="<%=DefineReport.RadB_7a.NAME.getObj()%>" id="<%=DefineReport.RadB_7a.ID3.getObj()%>" value="<%=DefineReport.RadB_7a.ID3.getVal()%>"
							/><%=DefineReport.RadB_7a.ID3.getView()%></label>
					</td>
					<td>
						<label for="<%=DefineReport.RadB_7a.ID4.getObj()%>" class="for">
							<input type="radio" name="<%=DefineReport.RadB_7a.NAME.getObj()%>" id="<%=DefineReport.RadB_7a.ID4.getObj()%>" value="<%=DefineReport.RadB_7a.ID4.getVal()%>"
							/><%=DefineReport.RadB_7a.ID4.getView()%></label>
					</td>
					<td>
						<label for="<%=DefineReport.RadB_7a.ID5.getObj()%>" class="for">
							<input type="radio" name="<%=DefineReport.RadB_7a.NAME.getObj()%>" id="<%=DefineReport.RadB_7a.ID5.getObj()%>" value="<%=DefineReport.RadB_7a.ID5.getVal()%>"
							/><%=DefineReport.RadB_7a.ID5.getView()%></label>
					</td>
				</tr>
				</table>
			</fieldset>
			<fieldset class="outline" style="margin-right: 4px;"><legend>チャネル分類指定</legend>
				<table class="tbl01">
				<tr>
					<td><%=DefineReport.Select.CHA_1.getView()%></td>
					<td>
						<select name="<%=DefineReport.Select.CHA_NM.getObj()%>" id="<%=DefineReport.Select.CHA_1.getObj()%>" style= "width:150px;"></select>
					</td>
				</tr>
				<tr>
					<td><%=DefineReport.Select.CHA_2.getView()%></td>
					<td>
						<select name="<%=DefineReport.Select.CHA_NM.getObj()%>" id="<%=DefineReport.Select.CHA_2.getObj()%>" style= "width:150px;"></select>
					</td>
				</tr>
				<tr>
					<td><%=DefineReport.Select.CHA_3.getView()%></td>
					<td>
						<select name="<%=DefineReport.Select.CHA_NM.getObj()%>" id="<%=DefineReport.Select.CHA_3.getObj()%>" style= "width:150px;"></select>
					</td>
				</tr>
				<tr>
					<td><%=DefineReport.Select.CHA_4.getView()%></td>
					<td>
						<select name="<%=DefineReport.Select.CHA_NM.getObj()%>" id="<%=DefineReport.Select.CHA_4.getObj()%>" style= "width:150px;"></select>
					</td>
				</tr>
				</table>
			</fieldset>
			<fieldset class="outline sumChk"><legend><%=DefineReport.Label.LABELB4b.getView()%></legend>
				<label for="<%=DefineReport.CheckB4_1.ID1.getObj()%>">
					<input type="checkbox" name="<%=DefineReport.CheckB4_1.NAME.getObj()%>" id="<%=DefineReport.CheckB4_1.ID1.getObj()%>" checked="checked" disabled="disabled"
					/><%=DefineReport.CheckB4_1.ID1.getView()%></label>
				<label for="<%=DefineReport.CheckB4_1.ID2.getObj()%>">
					<input type="checkbox" name="<%=DefineReport.CheckB4_1.NAME.getObj()%>" id="<%=DefineReport.CheckB4_1.ID2.getObj()%>"
					/><%=DefineReport.CheckB4_1.ID2.getView()%></label>
				<label for="<%=DefineReport.CheckB4_1.ID3.getObj()%>">
					<input type="checkbox" name="<%=DefineReport.CheckB4_1.NAME.getObj()%>" id="<%=DefineReport.CheckB4_1.ID3.getObj()%>"
					/><%=DefineReport.CheckB4_1.ID3.getView()%></label>
				<label for="<%=DefineReport.CheckB4_1.ID4.getObj()%>">
					<input type="checkbox" name="<%=DefineReport.CheckB4_1.NAME.getObj()%>" id="<%=DefineReport.CheckB4_1.ID4.getObj()%>"
					/><%=DefineReport.CheckB4_1.ID4.getView()%></label>
			</fieldset>
		</fieldset>
		<!-- /チャネル -->
	</div>
	<!-- /Left -->
</td>
<td valign="top">
	<!-- Right -->
	<div class="cell" style="width: 390px;">
		<!-- 商品 -->
		<fieldset style="border-style: none;">
			<legend></legend>
	<div><input type="button" id="<%=DefineReport.Button.RESET.getObj()%>" class="btn04" value="<%=DefineReport.Button.RESET.getView()%>" style="width:5em;margin-left: 300px;"/>
	</div>
			<fieldset class="outline" style="margin-right: 4px;"><legend><%=DefineReport.Label.LABELB4a.getView()%></legend>
				<table class="tbl01">
				<tr>
					<td><%=DefineReport.Select.SHOS_1.getView()%></td>
					<td>
						<select name="<%=DefineReport.Select.SHOS_NM.getObj()%>" id="<%=DefineReport.Select.SHOS_1.getObj()%>" class="sel_shos"></select>
					</td>
				</tr>
				<tr>
					<td><%=DefineReport.Select.SHOS_2.getView()%></td>
					<td>
						<select name="<%=DefineReport.Select.SHOS_NM.getObj()%>" id="<%=DefineReport.Select.SHOS_2.getObj()%>" class="sel_shos"></select>
					</td>
				</tr>
				<tr>
					<td><%=DefineReport.Select.SHOS_3.getView()%></td>
					<td>
						<select name="<%=DefineReport.Select.SHOS_NM.getObj()%>" id="<%=DefineReport.Select.SHOS_3.getObj()%>" class="sel_shos"></select>
					</td>
				</tr>
				<tr>
					<td><%=DefineReport.Select.SHOS_4.getView()%></td>
					<td>
						<select name="<%=DefineReport.Select.SHOS_NM.getObj()%>" id="<%=DefineReport.Select.SHOS_4.getObj()%>" class="sel_shos"></select>
					</td>
				</tr>
				<tr>
					<td><%=DefineReport.Select.SHOS_5.getView()%></td>
					<td>
						<select name="<%=DefineReport.Select.SHOS_NM.getObj()%>" id="<%=DefineReport.Select.SHOS_5.getObj()%>" class="sel_shos"></select>
					</td>
				</tr>
				<tr>
					<td><%=DefineReport.Select.SHOS_6.getView()%></td>
					<td>
						<select name="<%=DefineReport.Select.SHOS_NM.getObj()%>" id="<%=DefineReport.Select.SHOS_6.getObj()%>" class="sel_shos" ></select>
					</td>
				</tr>
				</table>
			</fieldset>
			<fieldset class="outline sumChk"><legend><%=DefineReport.Label.LABELB4b.getView()%></legend>
				<label for="<%=DefineReport.CheckB4_2.ID1.getObj()%>">
					<input type="checkbox" name="<%=DefineReport.CheckB4_2.NAME.getObj()%>" id="<%=DefineReport.CheckB4_2.ID1.getObj()%>" checked="checked" disabled="disabled"
					/><%=DefineReport.CheckB4_2.ID1.getView()%></label>
				<label for="<%=DefineReport.CheckB4_2.ID2.getObj()%>">
					<input type="checkbox" name="<%=DefineReport.CheckB4_2.NAME.getObj()%>" id="<%=DefineReport.CheckB4_2.ID2.getObj()%>"
					/><%=DefineReport.CheckB4_2.ID2.getView()%></label>
				<label for="<%=DefineReport.CheckB4_2.ID3.getObj()%>">
					<input type="checkbox" name="<%=DefineReport.CheckB4_2.NAME.getObj()%>" id="<%=DefineReport.CheckB4_2.ID3.getObj()%>"
					/><%=DefineReport.CheckB4_2.ID3.getView()%></label>
				<label for="<%=DefineReport.CheckB4_2.ID4.getObj()%>">
					<input type="checkbox" name="<%=DefineReport.CheckB4_2.NAME.getObj()%>" id="<%=DefineReport.CheckB4_2.ID4.getObj()%>"
					/><%=DefineReport.CheckB4_2.ID4.getView()%></label>
				<label for="<%=DefineReport.CheckB4_2.ID5.getObj()%>">
					<input type="checkbox" name="<%=DefineReport.CheckB4_2.NAME.getObj()%>" id="<%=DefineReport.CheckB4_2.ID5.getObj()%>"
					/><%=DefineReport.CheckB4_2.ID5.getView()%></label>
				<label for="<%=DefineReport.CheckB4_2.ID6.getObj()%>">
					<input type="checkbox" name="<%=DefineReport.CheckB4_2.NAME.getObj()%>" id="<%=DefineReport.CheckB4_2.ID6.getObj()%>"
					/><%=DefineReport.CheckB4_2.ID6.getView()%></label>
				<label for="<%=DefineReport.CheckB4_2.ID7.getObj()%>">
					<input type="checkbox" name="<%=DefineReport.CheckB4_2.NAME.getObj()%>" id="<%=DefineReport.CheckB4_2.ID7.getObj()%>"
					/><%=DefineReport.CheckB4_2.ID7.getView()%></label>
			</fieldset>
			<!-- 商品 分類条件-->
			<fieldset class="outline"><legend><%=DefineReport.Label.LABELB4n.getView()%></legend>
				<table class="tbl01">
				<tr>
					<td><label class="lbl_shob"><%=DefineReport.Select.SHOB_1.getView()%></label>
						<select name="<%=DefineReport.Select.SHOB_NM.getObj()%>" id="<%=DefineReport.Select.SHOB_1.getObj()%>" class="sel_shob"></select>
						<select name="<%=DefineReport.Select.SHOBS_NM.getObj()%>" id="<%=DefineReport.Select.SHOBS_1.getObj()%>" class="sel_shobs"></select>
					</td>
				</tr>
				<tr>
					<td><label class="lbl_shob"><%=DefineReport.Select.SHOB_2.getView()%></label>
						<select name="<%=DefineReport.Select.SHOB_NM.getObj()%>" id="<%=DefineReport.Select.SHOB_2.getObj()%>" class="sel_shob"></select>
						<select name="<%=DefineReport.Select.SHOBS_NM.getObj()%>" id="<%=DefineReport.Select.SHOBS_2.getObj()%>" class="sel_shobs"></select>
					</td>
				</tr>
				<tr>
					<td><label class="lbl_shob"><%=DefineReport.Select.SHOB_3.getView()%></label>
						<select name="<%=DefineReport.Select.SHOB_NM.getObj()%>" id="<%=DefineReport.Select.SHOB_3.getObj()%>" class="sel_shob"></select>
						<select name="<%=DefineReport.Select.SHOBS_NM.getObj()%>" id="<%=DefineReport.Select.SHOBS_3.getObj()%>" class="sel_shobs"></select>
					</td>
				</tr>
				<tr>
					<td><label class="lbl_shob"><%=DefineReport.Select.SHOB_4.getView()%></label>
						<select name="<%=DefineReport.Select.SHOB_NM.getObj()%>" id="<%=DefineReport.Select.SHOB_4.getObj()%>" class="sel_shob"></select>
						<select name="<%=DefineReport.Select.SHOBS_NM.getObj()%>" id="<%=DefineReport.Select.SHOBS_4.getObj()%>" class="sel_shobs"></select>
					</td>
				</tr>
				<tr>
					<td><label class="lbl_shob"><%=DefineReport.Select.SHOB_5.getView()%></label>
						<select name="<%=DefineReport.Select.SHOB_NM.getObj()%>" id="<%=DefineReport.Select.SHOB_5.getObj()%>" class="sel_shob"></select>
						<select name="<%=DefineReport.Select.SHOBS_NM.getObj()%>" id="<%=DefineReport.Select.SHOBS_5.getObj()%>" class="sel_shobs"></select>
					</td>
				</tr>
				<tr>
					<td><label class="lbl_shob"><%=DefineReport.Select.SHOB_6.getView()%></label>
						<select name="<%=DefineReport.Select.SHOB_NM.getObj()%>" id="<%=DefineReport.Select.SHOB_6.getObj()%>" class="sel_shob"></select>
						<select name="<%=DefineReport.Select.SHOBS_NM.getObj()%>" id="<%=DefineReport.Select.SHOBS_6.getObj()%>" class="sel_shobs"></select>
					</td>
				</tr>
				<tr>
					<td><label class="lbl_shob"><%=DefineReport.Select.SHOB_7.getView()%></label>
						<select name="<%=DefineReport.Select.SHOB_NM.getObj()%>" id="<%=DefineReport.Select.SHOB_7.getObj()%>" class="sel_shob"></select>
						<select name="<%=DefineReport.Select.SHOBS_NM.getObj()%>" id="<%=DefineReport.Select.SHOBS_7.getObj()%>" class="sel_shobs"></select>
					</td>
				</tr>
				<tr>
					<td><label class="lbl_shob"><%=DefineReport.Select.SHOB_8.getView()%></label>
						<select name="<%=DefineReport.Select.SHOB_NM.getObj()%>" id="<%=DefineReport.Select.SHOB_8.getObj()%>" class="sel_shob"></select>
						<select name="<%=DefineReport.Select.SHOBS_NM.getObj()%>" id="<%=DefineReport.Select.SHOBS_8.getObj()%>" class="sel_shobs"></select>
					</td>
				</tr>
				<tr>
					<td><label class="lbl_shob"><%=DefineReport.Select.SHOB_9.getView()%></label>
						<select name="<%=DefineReport.Select.SHOB_NM.getObj()%>" id="<%=DefineReport.Select.SHOB_9.getObj()%>" class="sel_shob"></select>
						<select name="<%=DefineReport.Select.SHOBS_NM.getObj()%>" id="<%=DefineReport.Select.SHOBS_9.getObj()%>" class="sel_shobs"></select>
					</td>
				</tr>
				<tr>
					<td><label class="lbl_shob"><%=DefineReport.Select.SHOB_10.getView()%></label>
						<select name="<%=DefineReport.Select.SHOB_NM.getObj()%>" id="<%=DefineReport.Select.SHOB_10.getObj()%>" class="sel_shob"></select>
						<select name="<%=DefineReport.Select.SHOBS_NM.getObj()%>" id="<%=DefineReport.Select.SHOBS_10.getObj()%>" class="sel_shobs"></select>
					</td>
				</tr>
				</table>
			</fieldset>
			<!-- /商品 分類条件-->
		</fieldset>
		<!-- /商品 -->
	</div>
	<!-- /Right -->
</td>
<td valign="top">
	<div class="cell" style="width: 210px;">
		<fieldset><legend><%=DefineReport.Label.LABELT7.getView()%></legend>
			<label for="<%=DefineReport.RadioSho1.ID1.getObj()%>" class="for">
				<input type="radio" name="<%=DefineReport.RadioSho1.NAME.getObj() %>" id="<%=DefineReport.RadioSho1.ID1.getObj()%>"
					value="<%=DefineReport.RadioSho1.ID1.getVal()%>" checked
					/><%=DefineReport.RadioSho1.ID1.getView()%></label>
			<label for="<%=DefineReport.RadioSho1.ID2.getObj()%>" class="for">
				<input type="radio" name="<%=DefineReport.RadioSho1.NAME.getObj() %>" id="<%=DefineReport.RadioSho1.ID2.getObj()%>"
					value="<%=DefineReport.RadioSho1.ID2.getVal()%>"
					/><%=DefineReport.RadioSho1.ID2.getView()%></label>
		</fieldset>
	</div>
</td>
</tr>
</table>
</div>

</div>
</form>
</div>



<div id="MAIN_REPORT" style="display: none;">
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

<!-- Report Option & Control & Event -->
<script type="text/javascript"	src="../js/jquery.report/jquery.report.option.B_04.js?t=<%System.currentTimeMillis();%>"></script>
<script type="text/javascript"	src="../js/jquery.report/jquery.report.control.js?t=<%System.currentTimeMillis();%>"></script>
<script type="text/javascript"	src="../js/jquery.report/jquery.report.eventsB04.js?t=<%System.currentTimeMillis();%>"></script>

</html>