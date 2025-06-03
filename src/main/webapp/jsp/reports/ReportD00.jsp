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
<link rel="stylesheet" type="text/css" href="../css/custom_d00.css">
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
		<pre>メニュー説明の文章を入れる</pre>
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
									<input type="radio" name="<%=DefineReport.RadD02_UNIT.NAME.getObj()%>" id="<%=DefineReport.RadD02_UNIT.ID1.getObj()%>" value="<%=DefineReport.RadD02_UNIT.ID1.getVal()%>" checked = checked/><%=DefineReport.RadD02_UNIT.ID1.getView()%>
								</label>
								<label class="for">　</label>
								<label for="<%=DefineReport.RadD02_UNIT.ID2.getObj()%>" class="for">
									<input type="radio" name="<%=DefineReport.RadD02_UNIT.NAME.getObj()%>" id="<%=DefineReport.RadD02_UNIT.ID2.getObj()%>" value="<%=DefineReport.RadD02_UNIT.ID2.getVal()%>"/><%=DefineReport.RadD02_UNIT.ID2.getView()%>
								</label>
								<label class="for">　</label>
								<label for="<%=DefineReport.RadD02_UNIT.ID3.getObj()%>" class="for">
									<input type="radio" name="<%=DefineReport.RadD02_UNIT.NAME.getObj()%>" id="<%=DefineReport.RadD02_UNIT.ID3.getObj()%>" value="<%=DefineReport.RadD02_UNIT.ID3.getVal()%>"/><%=DefineReport.RadD02_UNIT.ID3.getView()%>
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
								<select name="<%=DefineReport.Select.MM_KIJUN.getObj() %>" id="<%=DefineReport.Select.MM_KIJUN.getObj() %>" disabled></select>
								<select name="<%=DefineReport.Select.DD_KIJUN.getObj() %>" id="<%=DefineReport.Select.DD_KIJUN.getObj() %>" disabled></select>
							</td>
							<td>
								<label>～</label>
							</td>
							<td>
								<select name="<%=DefineReport.Select.YY_KIJUN_TO.getObj() %>" id="<%=DefineReport.Select.YY_KIJUN_TO.getObj() %>"></select>
								<select name="<%=DefineReport.Select.MM_KIJUN_TO.getObj() %>" id="<%=DefineReport.Select.MM_KIJUN_TO.getObj() %>" disabled></select>
								<select name="<%=DefineReport.Select.DD_KIJUN_TO.getObj() %>" id="<%=DefineReport.Select.DD_KIJUN_TO.getObj() %>" disabled></select>
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
							<td><label class="labelwidth"><%=DefineReport.RadD03_TENPO.NAME.getView()%></label></td>
							<td>
								<label for="<%=DefineReport.RadD03_TENPO.ID1.getObj()%>" class="for">
									<input type="radio" name="<%=DefineReport.RadD03_TENPO.NAME.getObj()%>" id="<%=DefineReport.RadD03_TENPO.ID1.getObj()%>" value="<%=DefineReport.RadD03_TENPO.ID1.getVal()%>" checked = checked/><%=DefineReport.RadD03_TENPO.ID1.getView()%>
								</label>
							</td>
							<td>
								<label for="<%=DefineReport.RadD03_TENPO.ID2.getObj()%>" class="for">
									<input type="radio" name="<%=DefineReport.RadD03_TENPO.NAME.getObj()%>" id="<%=DefineReport.RadD03_TENPO.ID2.getObj()%>" value="<%=DefineReport.RadD03_TENPO.ID2.getVal()%>"/><%=DefineReport.RadD03_TENPO.ID2.getView()%>
								</label>
							</td>
							<td>
								<input type="button" class="btn03" id="<%=DefineReport.Select.TENPO.getObj() %>" value="<%=DefineReport.Label.SELECT.getView()%>" disabled>
							</td>
						</tr>

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
							<td><label class="labelwidth"><%=DefineReport.RadD05_AGE.NAME.getView()%></label></td>
							<td>
								<label for="<%=DefineReport.RadD05_AGE.ID1.getObj()%>" class="for">
									<input type="radio" name="<%=DefineReport.RadD05_AGE.NAME.getObj()%>" id="<%=DefineReport.RadD05_AGE.ID1.getObj()%>" value="<%=DefineReport.RadD05_AGE.ID1.getVal()%>" checked = checked/><%=DefineReport.RadD05_AGE.ID1.getView()%>
								</label>
							</td>
							<td>
								<label for="<%=DefineReport.RadD05_AGE.ID2.getObj()%>" class="for">
									<input type="radio" name="<%=DefineReport.RadD05_AGE.NAME.getObj()%>" id="<%=DefineReport.RadD05_AGE.ID2.getObj()%>" value="<%=DefineReport.RadD05_AGE.ID2.getVal()%>"/><%=DefineReport.RadD05_AGE.ID2.getView()%>
								</label>
							</td>
							<td>
								<label for="<%=DefineReport.RadD05_AGE.ID3.getObj()%>" class="for">
									<input type="radio" name="<%=DefineReport.RadD05_AGE.NAME.getObj()%>" id="<%=DefineReport.RadD05_AGE.ID3.getObj()%>" value="<%=DefineReport.RadD05_AGE.ID3.getVal()%>"/><%=DefineReport.RadD05_AGE.ID3.getView()%>
								</label>
							</td>
						</tr>

						<tr>
							<td><label class="labelwidth"><%=DefineReport.RadD06_FKY.NAME.getView()%></label></td>
							<td>
								<label for="<%=DefineReport.RadD06_FKY.ID1.getObj()%>" class="for">
									<input type="radio" name="<%=DefineReport.RadD06_FKY.NAME.getObj()%>" id="<%=DefineReport.RadD06_FKY.ID1.getObj()%>" value="<%=DefineReport.RadD06_FKY.ID1.getVal()%>" checked = checked/><%=DefineReport.RadD06_FKY.ID1.getView()%>
								</label>
							</td>
							<td>
								<label for="<%=DefineReport.RadD06_FKY.ID2.getObj()%>" class="for">
									<input type="radio" name="<%=DefineReport.RadD06_FKY.NAME.getObj()%>" id="<%=DefineReport.RadD06_FKY.ID2.getObj()%>" value="<%=DefineReport.RadD06_FKY.ID2.getVal()%>"/><%=DefineReport.RadD06_FKY.ID2.getView()%>
								</label>
							</td>
							<td>
								<label for="<%=DefineReport.RadD06_FKY.ID3.getObj()%>" class="for">
									<input type="radio" name="<%=DefineReport.RadD06_FKY.NAME.getObj()%>" id="<%=DefineReport.RadD06_FKY.ID3.getObj()%>" value="<%=DefineReport.RadD06_FKY.ID3.getVal()%>"/><%=DefineReport.RadD06_FKY.ID3.getView()%>
								</label>
							</td>
						</tr>

						<tr>
							<td><label class="labelwidth"><%=DefineReport.RadD07_ZKG.NAME.getView()%></label></td>
							<td>
								<label for="<%=DefineReport.RadD07_ZKG.ID1.getObj()%>" class="for">
									<input type="radio" name="<%=DefineReport.RadD07_ZKG.NAME.getObj()%>" id="<%=DefineReport.RadD07_ZKG.ID1.getObj()%>" value="<%=DefineReport.RadD07_ZKG.ID1.getVal()%>" checked = checked/><%=DefineReport.RadD07_ZKG.ID1.getView()%>
								</label>
							</td>
						</tr>

						<tr>
							<td><label class="labelwidth"><%=DefineReport.RadD08_RVL.NAME.getView()%></label></td>
							<td>
								<label for="<%=DefineReport.RadD08_RVL.ID1.getObj()%>" class="for">
									<input type="radio" name="<%=DefineReport.RadD08_RVL.NAME.getObj()%>" id="<%=DefineReport.RadD08_RVL.ID1.getObj()%>" value="<%=DefineReport.RadD08_RVL.ID1.getVal()%>" checked = checked/><%=DefineReport.RadD08_RVL.ID1.getView()%>
								</label>
							</td>
							<td>
								<label for="<%=DefineReport.RadD08_RVL.ID2.getObj()%>" class="for">
									<input type="radio" name="<%=DefineReport.RadD08_RVL.NAME.getObj()%>" id="<%=DefineReport.RadD08_RVL.ID2.getObj()%>" value="<%=DefineReport.RadD08_RVL.ID2.getVal()%>"/><%=DefineReport.RadD08_RVL.ID2.getView()%>
								</label>
							</td>
						</tr>

						<tr>
							<td><label class="labelwidth"><%=DefineReport.RadD09_CLS.NAME.getView()%></label></td>
							<td>
								<label for="<%=DefineReport.RadD09_CLS.ID1.getObj()%>" class="for">
									<input type="radio" name="<%=DefineReport.RadD09_CLS.NAME.getObj()%>" id="<%=DefineReport.RadD09_CLS.ID1.getObj()%>" value="<%=DefineReport.RadD09_CLS.ID1.getVal()%>" checked = checked/><%=DefineReport.RadD09_CLS.ID1.getView()%>
								</label>
							</td>
							<td>
								<label for="<%=DefineReport.RadD09_CLS.ID2.getObj()%>" class="for">
									<input type="radio" name="<%=DefineReport.RadD09_CLS.NAME.getObj()%>" id="<%=DefineReport.RadD09_CLS.ID2.getObj()%>" value="<%=DefineReport.RadD09_CLS.ID2.getVal()%>"/><%=DefineReport.RadD09_CLS.ID2.getView()%>
								</label>
							</td>
							<td>
								<label for="<%=DefineReport.RadD09_CLS.ID3.getObj()%>" class="for">
									<input type="radio" name="<%=DefineReport.RadD09_CLS.NAME.getObj()%>" id="<%=DefineReport.RadD09_CLS.ID3.getObj()%>" value="<%=DefineReport.RadD09_CLS.ID3.getVal()%>"/><%=DefineReport.RadD09_CLS.ID3.getView()%>
								</label>
							</td>
							<td>
								<label for="<%=DefineReport.RadD09_CLS.ID4.getObj()%>" class="for">
									<input type="radio" name="<%=DefineReport.RadD09_CLS.NAME.getObj()%>" id="<%=DefineReport.RadD09_CLS.ID4.getObj()%>" value="<%=DefineReport.RadD09_CLS.ID4.getVal()%>"/><%=DefineReport.RadD09_CLS.ID4.getView()%>
								</label>
							</td>
						</tr>

						<tr>
							<td><label class="labelwidth"></label></td>
							<td>
								<label for="<%=DefineReport.CheckD10_CLS.ID1.getObj()%>" class="for">
									<input type="checkbox" name="<%=DefineReport.CheckD10_CLS.NAME.getObj()%>" id="<%=DefineReport.CheckD10_CLS.ID1.getObj()%>" value="<%=DefineReport.CheckD10_CLS.ID1.getVal()%>" checked = checked/><%=DefineReport.CheckD10_CLS.ID1.getView()%>
								</label>
							</td>
							<td>
								<label for="<%=DefineReport.CheckD10_CLS.ID2.getObj()%>" class="for">
									<input type="checkbox" name="<%=DefineReport.CheckD10_CLS.NAME.getObj()%>" id="<%=DefineReport.CheckD10_CLS.ID2.getObj()%>" value="<%=DefineReport.CheckD10_CLS.ID2.getVal()%>"/><%=DefineReport.CheckD10_CLS.ID2.getView()%>
								</label>
							</td>
							<td>
								<label for="<%=DefineReport.CheckD10_CLS.ID3.getObj()%>" class="for">
									<input type="checkbox" name="<%=DefineReport.CheckD10_CLS.NAME.getObj()%>" id="<%=DefineReport.CheckD10_CLS.ID3.getObj()%>" value="<%=DefineReport.CheckD10_CLS.ID3.getVal()%>"/><%=DefineReport.CheckD10_CLS.ID3.getView()%>
								</label>
							</td>
							<td>
								<label for="<%=DefineReport.CheckD10_CLS.ID4.getObj()%>" class="for">
									<input type="checkbox" name="<%=DefineReport.CheckD10_CLS.NAME.getObj()%>" id="<%=DefineReport.CheckD10_CLS.ID4.getObj()%>" value="<%=DefineReport.CheckD10_CLS.ID4.getVal()%>"/><%=DefineReport.CheckD10_CLS.ID4.getView()%>
								</label>
							</td>
							<td>
								<label for="<%=DefineReport.CheckD10_CLS.ID5.getObj()%>" class="for">
									<input type="checkbox" name="<%=DefineReport.CheckD10_CLS.NAME.getObj()%>" id="<%=DefineReport.CheckD10_CLS.ID5.getObj()%>" value="<%=DefineReport.CheckD10_CLS.ID5.getVal()%>"/><%=DefineReport.CheckD10_CLS.ID5.getView()%>
								</label>
							</td>
							<td>
								<label for="<%=DefineReport.CheckD10_CLS.ID6.getObj()%>" class="for">
									<input type="checkbox" name="<%=DefineReport.CheckD10_CLS.NAME.getObj()%>" id="<%=DefineReport.CheckD10_CLS.ID6.getObj()%>" value="<%=DefineReport.CheckD10_CLS.ID6.getVal()%>"/><%=DefineReport.CheckD10_CLS.ID6.getView()%>
								</label>
								<label class="for" style="width:35px;">　　　　</label>
								<label for="<%=DefineReport.CheckD10_CLS.ID7.getObj()%>" class="for">
									<input type="checkbox" name="<%=DefineReport.CheckD10_CLS.NAME.getObj()%>" id="<%=DefineReport.CheckD10_CLS.ID7.getObj()%>" value="<%=DefineReport.CheckD10_CLS.ID7.getVal()%>"/><%=DefineReport.CheckD10_CLS.ID7.getView()%>
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

<script type="text/javascript" src="../js/jquery.min.js"></script><!-- Load jQuery and JS files -->
<script type="text/javascript" src="../js/jquery.easyui.min.js"></script><!-- jquery -->
<script type="text/javascript" src="../js/easyui-lang-ja.js"></script><!-- EasyUI framework -->
<script type="text/javascript" src="../js/datagrid-scrollview.js?v=1"></script>	<!-- EasyUI framework -->

<script type="text/javascript" src="../js/json2.min.js"></script><!-- json plugin -->
<script type="text/javascript" src="../js/exdate.js"></script><!-- exdate plugin -->
<script type="text/javascript" src="../js/jshashtable-2.1.js"></script><!-- jshashset plugin -->
<script type="text/javascript" src="../js/jquery.numberformatter.min.js"></script><!-- numberformatter plugin -->

<!-- Report Option & Control & Event -->
<script type="text/javascript" src="../js/jquery.report/jquery.report.option.D00.js?v=1"></script>
<script type="text/javascript" src="../js/jquery.report/jquery.report.control.js"></script>
<script type="text/javascript" src="../js/jquery.report/jquery.report.events.js"></script>

</html>