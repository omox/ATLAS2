<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="common.Defines"%>
<%@ page import="common.DefineReport"%>
<%@ page import="authentication.defines.Form" %>

<!DOCTYPE html>
<html>
<head>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Expires" content="Thu, 01 Dec 1994 16:00:00 GMT">
<link rel="stylesheet" type="text/css" href="../css/common.css">
<link rel="stylesheet" type="text/css" href="../themes/easyui/default/easyui.css">
<link rel="stylesheet" type="text/css" href="../themes/easyui/icon.css">
<link rel="stylesheet" type="text/css" href="../themes/easyui/default/custom_easyui.css">
<style type="text/css">
input[type="radio"],input[type="checkbox"]{
  position: relative;
  top: 2px;
}
.tbl01 td { height: 22px; }
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
	// 日付情報
	String dateList = (String)request.getSession().getAttribute(Form.MFU_SEL_YM);
%>
<body>
<div id="AREA_FACTOR" style="margin:10px;">
<form name="form_upload" id="form_upload" method="POST" enctype="multipart/form-data">
	<div id="main_form1">
		<table class="tbl01" style="margin:5px;" >
		<tr>
			<td>
				<input type="radio" name="<%=Form.MFU_RAD_UP %>" id="<%=Form.MFU_RAD_UP %>_1" value="0">売上目標
			</td>
			<td>
				<a class="flat">　</a>
				<select name="<%=DefineReport.Select.YY_KIJUN.getObj() %>" id="<%=DefineReport.Select.YY_KIJUN.getObj() %>"></select>
				<a class="flat">年</a>
				<select name="<%=DefineReport.Select.MM_KIJUN.getObj() %>" id="<%=DefineReport.Select.MM_KIJUN.getObj() %>"></select>
				<a class="flat">月</a>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<input type="radio" name="<%=Form.MFU_RAD_UP %>" id="<%=Form.MFU_RAD_UP %>_6" value="5">商品実績_カテゴリ
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<input type="radio" name="<%=Form.MFU_RAD_UP %>" id="<%=Form.MFU_RAD_UP %>_7" value="6">商品実績_重点商材
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<input type="radio" name="<%=Form.MFU_RAD_UP %>" id="<%=Form.MFU_RAD_UP %>_3" value="2">新規獲得_計画数
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<input type="radio" name="<%=Form.MFU_RAD_UP %>" id="<%=Form.MFU_RAD_UP %>_4" value="3">新規獲得_媒体別費用
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<input type="radio" name="<%=Form.MFU_RAD_UP %>" id="<%=Form.MFU_RAD_UP %>_8" value="7">新規獲得_商材指定
			</td>
		</tr>
		<tr>
			<td>
				<input type="radio" name="<%=Form.MFU_RAD_UP %>" id="<%=Form.MFU_RAD_UP %>_5" value="4">プロモ商品計画数
			</td>
			<td>
				<a class="flat">　</a>
				<select name="<%=DefineReport.Select.YY_KIJUN_TO.getObj() %>" id="<%=DefineReport.Select.YY_KIJUN_TO.getObj() %>"></select>
				<a class="flat">年</a>
				<select name="<%=DefineReport.Select.MM_KIJUN_TO.getObj() %>" id="<%=DefineReport.Select.MM_KIJUN_TO.getObj() %>"></select>
				<a class="flat">月</a>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<input type="radio" name="<%=Form.MFU_RAD_UP %>" id="<%=Form.MFU_RAD_UP %>_2" value="1" checked="checked">海外為替
			</td>
		</tr>
		</table>

		<table style="margin:5px;" >
		<tr><td>
		 <table border=3>
		 <tr><td colspan=5>海外為替用チャネルコード一覧</td></tr>
		 <tr><td>コード１</td><td>コード２</td><td>コード３</td><td>コード４</td><td>　国名称</td></tr>
		 <tr><td>　2</td><td>　20</td><td>　10</td><td>　0</td><td>韓国</td></tr>
		 <tr><td>　2</td><td>　20</td><td>　30</td><td>　0</td><td>台湾</td></tr>
		 <tr><td>　2</td><td>　20</td><td>　40</td><td>　0</td><td>中国</td></tr>
		 <tr><td colspan=5>為替は小数点以下４桁まで入力できます。<br>
		 率は消費税用ですが、未使用の為 0 を入れてください。
		 </td></tr>
		 </table>
		 </td>
		</table>


		<table style="margin-top:10px;margin-left:5px;">
		<tr>
			<td>ファイルパス：</td>
			<td><input type="file" name="fle_upload" id="fle_upload" size="100" style="width: 500px;height: 1.6em;" /></td>
		</tr>
		<tr>
			<td colspan="2"><span class="errcolor">※ファイルパスは必ず参照ボタンから指定してください。<br/>※1行目はヘッダーとして扱われます。</span></td>
		</tr>
		</table>

		<div id="btn_form" style="margin-left:5px;">
			<input type="button" id="<%=Form.BTN_UP_LOAD %>" value="アップロード" class="btn01" />
			<input type="button" id="<%=Form.BTN_DOW_LOAD %>" value="ダウンロード" class="btn01" />
		</div>
		<%--  エラーメッセージ領域 --%>
		<div class="line" style="margin-top:20px;">
			<label id="<%=Form.UP_DOWN_MSG%>"></label>
		</div>
		<%-- /エラーメッセージ領域 --%>
	</div>

</form>
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
	<!-- 引き継ぎ情報2(JSON文字列変換済 "'" で囲む) -->
	<input type="hidden" name="hiddenDateParam" id="hiddenDateParam" value='<%=dateList %>' />
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
<script type="text/javascript"	src="../js/jquery.report/jquery.report.option.Upload.js"></script>
<script type="text/javascript"	src="../js/jquery.report/jquery.report.control.js"></script>
<script type="text/javascript"	src="../js/jquery.report/jquery.report.events.js"></script>

</html>