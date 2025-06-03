<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="common.DefineReport"%>
<div id="promoWindow" class="easyui-dialog">
	<div class="easyui-layout" fit="true">
		<div data-options="region:'north'" border="false"
			style="height: 35px; padding: 5px; font-size: 105%;">
			<div class="cell">
				<label><%=DefineReport.Select.YY_KIJUN.getView()+DefineReport.Label.SELECT.getView()%></label>
				<select id="<%=DefineReport.Select.YY_KIJUN.getObj()%>_promo" style="width: 55px;"></select>
			</div>
			<div class="cell" style="float: right;">
				<input type="button"
					id="<%=DefineReport.Button.SEARCH_PROMO.getObj()%>" class="btn01"
					value="<%=DefineReport.Button.SEARCH_PROMO.getView()%>">
			</div>
		</div>
		<div region="center" border="false"
			style="padding: 0px; background: #fff; border: 0px solid #ccc;">
			<div id="tt" class="placeFace" style="width: auto; height: auto"></div>
		</div>
	</div>
</div>