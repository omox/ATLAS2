<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="MS932" %>
<div id="itemWindow" class="easyui-dialog">
<div class="easyui-layout" fit="true">
	<div data-options="region:'center'" style="font-size: 105%;">
		<fieldset style="float:left;margin:0 5px;"><legend><b>【 出荷個数 】</b></legend>
			<table id="select_item_jun" class="tbl01">
			<tr>
				<td>
					<label for="chk_jun_1"><input type="checkbox" value="1" checked name="chk_jun_1" id="chk_jun_1" >当年実績</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_jun_2"><input type="checkbox" value="1" checked name="chk_jun_2" id="chk_jun_2" >前年実績</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_jun_3"><input type="checkbox" value="1" checked name="chk_jun_3" id="chk_jun_3" >計画値</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_jun_4"><input type="checkbox" value="1" checked name="chk_jun_4" id="chk_jun_4" >前年比</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_jun_5"><input type="checkbox" value="1" checked name="chk_jun_5" id="chk_jun_5" >計画比</label>
				</td>
			</tr>
			</table>
		</fieldset>
		<fieldset style="float:left;margin:0 5px;"><legend><b>【 出荷金額 】</b></legend>
			<table id="select_item_shu" class="tbl01">
			<tr>
				<td>
					<label for="chk_shu_1"><input type="checkbox" value="1" checked name="chk_shu_1" id="chk_shu_1" >当年実績</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_shu_2"><input type="checkbox" value="1" checked name="chk_shu_2" id="chk_shu_2" >前年実績</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_shu_3"><input type="checkbox" value="1" checked name="chk_shu_3" id="chk_shu_3" >計画値</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_shu_4"><input type="checkbox" value="1" checked name="chk_shu_4" id="chk_shu_4" >前年比</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_shu_5"><input type="checkbox" value="1" checked name="chk_shu_5" id="chk_shu_5" >計画比</label>
				</td>
			</tr>
			</table>
		</fieldset>
		<fieldset style="float: left;margin:0 5px;"><legend><b>【 うちWEB実績 】</b></legend>
			<table id="select_item_cho" class="tbl01">
			<tr>
				<td>
					<label for="chk_cho_1"><input type="checkbox" value="1" name="chk_cho_1" id="chk_cho_1" >PC個数</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_cho_2"><input type="checkbox" value="1" name="chk_cho_2" id="chk_cho_2" >PC個数率</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_cho_3"><input type="checkbox" value="1" name="chk_cho_3" id="chk_cho_3" >PC金額</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_cho_4"><input type="checkbox" value="1" name="chk_cho_4" id="chk_cho_4" >PC金額率</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_cho_5"><input type="checkbox" value="1" name="chk_cho_5" id="chk_cho_5" >スマホ個数</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_cho_6"><input type="checkbox" value="1" name="chk_cho_6" id="chk_cho_6" >スマホ個数率</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_cho_7"><input type="checkbox" value="1" name="chk_cho_7" id="chk_cho_7" >スマホ金額</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_cho_8"><input type="checkbox" value="1" name="chk_cho_8" id="chk_cho_8" >スマホ金額率</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_cho_9"><input type="checkbox" value="1" checked name="chk_cho_9" id="chk_cho_9" >WEB個数</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_cho_10"><input type="checkbox" value="1" checked name="chk_cho_10" id="chk_cho_10" >WEB個数率</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_cho_11"><input type="checkbox" value="1" checked name="chk_cho_11" id="chk_cho_11" >WEB金額</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_cho_12"><input type="checkbox" value="1" checked name="chk_cho_12" id="chk_cho_12" >WEB金額率</label>
				</td>
			</tr>
			</table>
		</fieldset>
	</div>
	<div data-options="region:'south'" style="height: 30px;font-size: 105%;">
		<input type="button" name="btn_AllCheck" id="btn_AllCheck" value="全選択" class="btn01" />
		<input type="button" name="btn_AllCancel" id="btn_AllCancel" value="全解除" class="btn01" />
		<input type="button" name="btn_AllCancel" id="btn_AllClose" value="確定" class="btn01" />
	</div>
</div>
</div>