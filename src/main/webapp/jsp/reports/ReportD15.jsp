<!-- 都道府県別実績 -->
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
		<pre>対象期間内に出荷のあった顧客を、都道府県別に集計した画面。
<font style="color:#CC0000;">※データ量が多いため、集計条件によっては表示にたいへん時間がかかります。</font>
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
						<tr>
							<td>
								<div>
									<select tabindex="-1" style="position:relative; left:-40px"></select>
								</div>
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
							<td><label class="labelwidth"><%=DefineReport.RadD02_UNIT.NAME.getView()%></label></td>
							<td>
								<label for="<%=DefineReport.RadD02_UNIT.ID1.getObj()%>" class="for">
									<input type="radio" name="<%=DefineReport.RadD02_UNIT.NAME.getObj()%>" id="<%=DefineReport.RadD02_UNIT.ID1.getObj()%>" value="<%=DefineReport.RadD02_UNIT.ID1.getVal()%>"/><%=DefineReport.RadD02_UNIT.ID1.getView()%>
								</label>
								<label class="for">　</label>
								<label for="<%=DefineReport.RadD02_UNIT.ID2.getObj()%>" class="for">
									<input type="radio" name="<%=DefineReport.RadD02_UNIT.NAME.getObj()%>" id="<%=DefineReport.RadD02_UNIT.ID2.getObj()%>" value="<%=DefineReport.RadD02_UNIT.ID2.getVal()%>"/><%=DefineReport.RadD02_UNIT.ID2.getView()%>
								</label>
								<label class="for">　</label>
								<label for="<%=DefineReport.RadD02_UNIT.ID3.getObj()%>" class="for">
									<input type="radio" name="<%=DefineReport.RadD02_UNIT.NAME.getObj()%>" id="<%=DefineReport.RadD02_UNIT.ID3.getObj()%>" value="<%=DefineReport.RadD02_UNIT.ID3.getVal()%>" checked = checked/><%=DefineReport.RadD02_UNIT.ID3.getView()%>
								</label>
								<label class="for">　</label>
								<label for="<%=DefineReport.RadD02_UNIT.ID4.getObj()%>" class="for">
									<input type="radio" name="<%=DefineReport.RadD02_UNIT.NAME.getObj()%>" id="<%=DefineReport.RadD02_UNIT.ID4.getObj()%>" value="<%=DefineReport.RadD02_UNIT.ID4.getVal()%>"/><%=DefineReport.RadD02_UNIT.ID4.getView()%>
								</label>
							</td>
						</tr>
						<tr>
							<td><label><%=DefineReport.Label.LABELD03.getView()%></label></td>
							<td>
								<select name="<%=DefineReport.Select.YY_KIJUN.getObj() %>" id="<%=DefineReport.Select.YY_KIJUN.getObj() %>"></select>
								<a class="flat">年</a>
								<select name="<%=DefineReport.Select.MM_KIJUN.getObj() %>" id="<%=DefineReport.Select.MM_KIJUN.getObj() %>" disabled></select>
								<a class="flat">月</a>
								　～
							</td>
							<td>
								<select name="<%=DefineReport.Select.YY_KIJUN_TO.getObj() %>" id="<%=DefineReport.Select.YY_KIJUN_TO.getObj() %>"></select>
								<a class="flat">年</a>
								<select name="<%=DefineReport.Select.MM_KIJUN_TO.getObj() %>" id="<%=DefineReport.Select.MM_KIJUN_TO.getObj() %>" disabled></select>
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
							<td><label class="labelwidth"><%=DefineReport.RadD04_AREA.NAME.getView()%></label></td>
							<td>
								<label for="<%=DefineReport.RadD04_AREA.ID1.getObj()%>" class="for">
									<input type="radio" name="<%=DefineReport.RadD04_AREA.NAME.getObj()%>" id="<%=DefineReport.RadD04_AREA.ID1.getObj()%>" value="<%=DefineReport.RadD04_AREA.ID1.getVal()%>" checked = checked/><%=DefineReport.RadD04_AREA.ID1.getView()%>
								</label>
							</td>
							<td>
								<label for="<%=DefineReport.RadD04_AREA.ID2.getObj()%>" class="for">
									<input type="radio" name="<%=DefineReport.RadD04_AREA.NAME.getObj()%>" id="<%=DefineReport.RadD04_AREA.ID2.getObj()%>" value="<%=DefineReport.RadD04_AREA.ID2.getVal()%>"/><%=DefineReport.RadD04_AREA.ID2.getView()%>
								</label>
							</td>
							<td>
								<input type="button" class="btn03" id="<%=DefineReport.Select.AREA.getObj() %>" value="<%=DefineReport.Label.SELECT.getView()%>" disabled>
							</td>
						</tr>

						<tr>
							<td><label class="labelwidth"><%=DefineReport.RadD09_CLS.NAME.getView()%></label></td>
							<td>
								<label for="<%=DefineReport.CheckD10_CLS.ID1.getObj()%>" class="for">
									<%=DefineReport.CheckD10_CLS.ID1.getView()%>
								</label>
							</td>
							<td>
								<label for="<%=DefineReport.CheckD10_CLS.ID2.getObj()%>" class="for">
									<%=DefineReport.CheckD10_CLS.ID2.getView()%>
								</label>
							</td>
							<td>
								<label for="<%=DefineReport.CheckD10_CLS.ID3.getObj()%>" class="for">
									<%=DefineReport.CheckD10_CLS.ID3.getView()%>
								</label>
							</td>
							<td>
								<label for="<%=DefineReport.CheckD10_CLS.ID4.getObj()%>" class="for">
									<%=DefineReport.CheckD10_CLS.ID4.getView()%>
								</label>
							</td>
							<td>
								<label for="<%=DefineReport.CheckD10_CLS.ID5.getObj()%>" class="for">
									<%=DefineReport.CheckD10_CLS.ID5.getView()%>
								</label>
							</td>
							<td>
								<label for="<%=DefineReport.CheckD10_CLS.ID6.getObj()%>" class="for">
									<%=DefineReport.CheckD10_CLS.ID6.getView()%>
								</label>
							</td>
						</tr>

						<tr>
							<td><label class="labelwidth"></label></td>
							<td>
								<select id="<%=DefineReport.Select.SHOS_1.getObj() %>" style= "width:100px;"></select>
							</td>
							<td>
								<select id="<%=DefineReport.Select.SHOS_2.getObj() %>" style= "width:100px;"></select>
							</td>
							<td>
								<select id="<%=DefineReport.Select.SHOS_3.getObj() %>" style= "width:100px;"></select>
							</td>
							<td>
								<select id="<%=DefineReport.Select.SHOS_4.getObj() %>" style= "width:100px;"></select>
							</td>
							<td>
								<select id="<%=DefineReport.Select.SHOS_5.getObj() %>" style= "width:150px;"></select>
							</td>
							<td>
								<select id="<%=DefineReport.Select.SHOS_6.getObj() %>" style= "width:150px;"></select>
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
<script type="text/javascript" src="../js/jquery.report/jquery.report.option.D15.js?t=<%System.currentTimeMillis();%>"></script>
<script type="text/javascript" src="../js/jquery.report/jquery.report.control.js?t=<%System.currentTimeMillis();%>"></script>
<script type="text/javascript" src="../js/jquery.report/jquery.report.events.js?t=<%System.currentTimeMillis();%>"></script>

</html>