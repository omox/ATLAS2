<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="MS932" %>
<div id="itemWindow" class="easyui-dialog">
<div class="easyui-layout" fit="true">
	<div data-options="region:'center'" style="font-size: 105%;">
		<fieldset style="float:left;margin:0 5px;"><legend><b>【 純売上表示項目 】</b></legend>
		<table id="select_item_jun" class="tbl01">
			<tr>
				<td>
					<label for="chk_jun_1"><input type="checkbox" value="1" checked name="chk_jun" id="chk_jun_1" >レジ客数</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_jun_2"><input type="checkbox" value="1" checked name="chk_jun" id="chk_jun_2" >レジ客数累計</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_jun_3"><input type="checkbox" value="1" checked name="chk_jun" id="chk_jun_3" >計画客数</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_jun_4"><input type="checkbox" value="1" checked name="chk_jun" id="chk_jun_4" >客数達成率</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_jun_5"><input type="checkbox" value="1" checked name="chk_jun" id="chk_jun_5" >単価</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_jun_6"><input type="checkbox" value="1" checked name="chk_jun" id="chk_jun_6" >純売上</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_jun_7"><input type="checkbox" value="1" checked name="chk_jun" id="chk_jun_7" >純売上累計</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_jun_8"><input type="checkbox" value="1" checked name="chk_jun" id="chk_jun_8" >前年比</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_jun_9"><input type="checkbox" value="1" checked name="chk_jun" id="chk_jun_9" >予算</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_jun_10"><input type="checkbox" value="1" checked name="chk_jun" id="chk_jun_10" >売上達成率</label>
				</td>
			</tr>
		</table>
		</fieldset>
		<fieldset style="float:left;margin-right:5px;"><legend><b>【 出荷売上表示項目 】</b></legend>
		<table id="select_item_shu" class="tbl01">
			<tr>
				<td>
					<label for="chk_shu_1"><input type="checkbox" value="1" checked name="chk_shu" id="chk_shu_1" >レジ客数</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_shu_2"><input type="checkbox" value="1" checked name="chk_shu" id="chk_shu_2" >レジ客数累計</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_shu_3"><input type="checkbox" value="1" checked name="chk_shu" id="chk_shu_3" >計画客数</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_shu_4"><input type="checkbox" value="1" checked name="chk_shu" id="chk_shu_4" >客数達成率</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_shu_5"><input type="checkbox" value="1" checked name="chk_shu" id="chk_shu_5" >単価</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_shu_6"><input type="checkbox" value="1" checked name="chk_shu" id="chk_shu_6" >出荷売上</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_shu_7"><input type="checkbox" value="1" checked name="chk_shu" id="chk_shu_7" >出荷売上累計</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_shu_8"><input type="checkbox" value="1" checked name="chk_shu" id="chk_shu_8" >前年比</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_shu_9"><input type="checkbox" value="1" checked name="chk_shu" id="chk_shu_9" >計画出荷売上</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_shu_10"><input type="checkbox" value="1" checked name="chk_shu" id="chk_shu_10" >売上達成率</label>
				</td>
			</tr>
		</table>
		</fieldset>
		<fieldset style="float:left;"><legend><b>【 調整額表示項目 】</b></legend>
		<table id="select_item_cho" class="tbl01">
			<tr>
				<td>
					<label for="chk_cho_1"><input type="checkbox" value="1" checked name="chk_cho" id="chk_cho_1" >レジ客数</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_cho_2"><input type="checkbox" value="1" checked name="chk_cho" id="chk_cho_2" >レジ客数累計</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_cho_3"><input type="checkbox" value="1" checked name="chk_cho" id="chk_cho_3" >純売上</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_cho_4"><input type="checkbox" value="1" checked name="chk_cho" id="chk_cho_4" >純売上累計</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_cho_5"><input type="checkbox" value="1" checked name="chk_cho" id="chk_cho_5" >出荷売上</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_cho_6"><input type="checkbox" value="1" checked name="chk_cho" id="chk_cho_6" >出荷売上累計</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_cho_7"><input type="checkbox" value="1" checked name="chk_cho" id="chk_cho_7" >調整前売上</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_cho_8"><input type="checkbox" value="1" checked name="chk_cho" id="chk_cho_8" >調整前累計</label>
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