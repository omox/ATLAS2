<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="MS932"%>
<%@ page import="common.Defines" %>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<html>
<head>
<title>資料参照</title>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=UTF-8">
<jsp:include page="/frame/jsp/common_head.jsp" flush="true" />
<LINK HREF="../frame/css/cmn.css" REL="stylesheet" TYPE="text/css">
<LINK HREF="../frame/css/common.css" REL="stylesheet" TYPE="text/css">
<script type="text/javascript" src="../frame/js/Common.js"></script>
</head>
<body>
<!-- ***** container ***** -->
<div id="container">

<!-- ===  header === -->
<div id="header">
	<jsp:include page="../frame/jsp/header.jsp" flush="true" />
</div>
<!-- ===  /header=== -->

<!-- ===  content === -->
<div id="content">
	<h2>資料参照</h2>
	<div id="AREA_FACTOR">
		<table>
		<tr>
			<td>
				<span>マニュアルへのリンク</span>
			</td>
			<td >　→　</td>
			<td>&nbsp</td>
			<td>
			<label><a href="../doc/atlas_gaiyo.xls" target="_blank">概要と変更履歴</a></label>
			&nbsp &nbsp
			<label><a href="../doc/atlas_kanri.xls" target="_blank">管理用マニュアル</a></label>
			&nbsp &nbsp
			<label><a href="../doc/atlas_data_N.xls" target="_blank">データ期間と注記</a></label>
			&nbsp &nbsp
			<label><a href="../doc/shohin_grp.xls" target="_blank">初回商品マスタ更新</a></label>
			</td>
		</tr>
		<tr>
			<td >
				<span>関連資料へのリンク</span>
			</td>
			<td >　→　</td>
			<td>&nbsp</td>
			<td>
			<label><a href="../doc/atlas_tenso2013.xls" target="_blank">メニュー別転送ファイル一覧</a></label>
			&nbsp &nbsp
			<label><a href="../doc/atlas_host.xls" target="_blank">ホスト側データレイアウト</a></label>
			&nbsp &nbsp
			</td>
		</tr>
		<tr>
			<td>
				<span>問合せ・トラブル報告</span>
			</td>
			<td >　→　</td>
			<td>&nbsp</td>
			<td>
			<label><a href="../doc/question2013.xls" target="_blank">問合せ記録</a></label>
			&nbsp &nbsp
			<label><a href="../doc/trouble2013.xls" target="_blank">トラブル報告</a></label>
			</td>
		</tr>
		<tr>
			<td>
				<span>本番リリース用</span>
			</td>
			<td >　→　</td>
			<td>&nbsp</td>
			<td>
			<label><a href="../doc/release_manual.xls" target="_blank">手順書</a></label>
			&nbsp &nbsp
			<label><a href="../doc/release_report.xls" target="_blank">報告書</a></label>
			</td>
		</tr>
		<tr>
			<td>
				<span>その他個別資料</span>
			</td>
			<td >　→　</td>
			<td>&nbsp</td>
			<td>
			<label><a href="../doc/T19_KEISAN.xls" target="_blank">アクティブ顧客数_詳細計算式</a></label>
			&nbsp &nbsp
			<label><a href="../doc/T21_NewRep.xls" target="_blank">新規・リピート顧客数コード表</a></label>
			</td>
		</tr>
		<tr>
			<td>&nbsp</td>
		</tr>
		<tr>
			<td colspan="4">アクティブ顧客数_詳細の計算式</td>
		</tr>
		<tr>
			<td>&nbsp</td>
			<td colspan="3">
<div class="line">■当月不明（アクティブ）＝   前月アクティブ計 ＋ アクティブ顧客（リスト外 ＋ スリープ ＋ ドロップ）</div>
<div class="line">　　　　　　　　　　　　　　－ スリープ顧客_アクティブ － アクティブ顧客_当月アクティブ計</div>
<div class="line">■当月不明（スリープ）　＝   前月スリープ計 ＋ スリープ顧客（アクティブ + 不明戻り)</div>
<div class="line">　　　　　　　　　　　　　　－ アクティブ顧客_スリープ － ドロップ顧客_スリープ － 当月スリープ計</div>
<div class="line">■当月不明（ドロップ）　＝   前月ドロップ計 ＋ ドロップ顧客（スリープ + 不明戻り）</div>
<div class="line">　　　　　　　　　　　　　　－ アクティブ顧客_ドロップ － 当月ドロップ計</div>
			</td>
		</tr>
		</table>
	</div>
</div>
<br><br>
<div id="content">
	<h2>過去の資料</h2>
	<div id="AREA_FACTOR"><br>
		<table>
		<tr>
			<td><span>過去の問合せ</span></td>
			<td>　→　</td>
			<td>&nbsp &nbsp<label><a href="../doc/question2012.xls" target="_blank">問合せ</a></label></td>
		</tr>
		<tr>
			<td><span>説明書</span></td>
			<td>　→　</td>
			<td>&nbsp &nbsp
			<label><a href="../doc/atlashelp_pdf.pdf" target="_blank">青地球儀説明書</a></label>
			</td>
		</tr>
		</table>
	</div>
</div>

<!-- ===  /content === -->
</div>
<!-- ***** /container ***** -->
</body>
</html>