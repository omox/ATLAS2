<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="MS932" %>
<div id="itemWindow" class="easyui-dialog">
<div class="easyui-layout" fit="true">
	<div data-options="region:'center'" style="font-size: 105%;">
		<fieldset style="float:left;margin:0 5px;"><legend><b>【 純売上表示項目 】</b></legend>
			<table id="select_item_jun" class="tbl01">
			<tr>
				<td>
					<label for="chk_jun_1"><input type="checkbox" value="1" checked name="chk_jun_1" id="chk_jun_1" >件数</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_jun_19"><input type="checkbox" value="1" checked name="chk_jun_19" id="chk_jun_19" >送料</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_jun_20"><input type="checkbox" value="1" checked name="chk_jun_20" id="chk_jun_20" >返品送料</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_jun_2"><input type="checkbox" value="1" checked name="chk_jun_2" id="chk_jun_2" >上代売上</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_jun_3"><input type="checkbox" value="1" checked name="chk_jun_3" id="chk_jun_3" >返品</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_jun_4"><input type="checkbox" value="1" checked name="chk_jun_4" id="chk_jun_4" >上代売上合計</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_jun_5"><input type="checkbox" value="1" checked name="chk_jun_5" id="chk_jun_5" >割引</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_jun_6"><input type="checkbox" value="1" checked name="chk_jun_6" id="chk_jun_6" >割引戻し</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_jun_7"><input type="checkbox" value="1" checked name="chk_jun_7" id="chk_jun_7" >割引合計</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_jun_8"><input type="checkbox" value="1" checked name="chk_jun_8" id="chk_jun_8" >上代－割引</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_jun_9"><input type="checkbox" value="1" checked name="chk_jun_9" id="chk_jun_9" >返品－戻し</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_jun_10"><input type="checkbox" value="1" checked name="chk_jun_10" id="chk_jun_10" >純売上</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_jun_11"><input type="checkbox" value="1" checked name="chk_jun_11" id="chk_jun_11" >純売上累計</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_jun_12"><input type="checkbox" value="1" checked name="chk_jun_12" id="chk_jun_12" >単価</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_jun_13"><input type="checkbox" value="1" checked name="chk_jun_13" id="chk_jun_13" >前年/前月比</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_jun_14"><input type="checkbox" value="1" checked name="chk_jun_14" id="chk_jun_14" >達成率</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_jun_15"><input type="checkbox" value="1" checked name="chk_jun_15" id="chk_jun_15" >件数(前年/前月)</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_jun_16"><input type="checkbox" value="1" checked name="chk_jun_16" id="chk_jun_16" >純売上(前年/前月)</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_jun_17"><input type="checkbox" value="1" checked name="chk_jun_17" id="chk_jun_17" >純売上累計(前年/前月)</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_jun_18"><input type="checkbox" value="1" checked name="chk_jun_18" id="chk_jun_18" >単価(前年/前月)</label>
				</td>
			</tr>
			</table>
		</fieldset>
		<fieldset style="float: left;"><legend><b>【 出荷売上表示項目 】</b></legend>
			<table id="select_item_shu" class="tbl01">
			<tr>
				<td>
					<label for="chk_shu_1"><input type="checkbox" value="1" checked name="chk_shu_1" id="chk_shu_1" >件数</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_shu_19"><input type="checkbox" value="1" checked name="chk_shu_19" id="chk_shu_19" >送料</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_shu_20"><input type="checkbox" value="1" checked name="chk_shu_20" id="chk_shu_20" >返品送料</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_shu_2"><input type="checkbox" value="1" checked name="chk_shu_2" id="chk_shu_2" >上代総売上</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_shu_3"><input type="checkbox" value="1" checked name="chk_shu_3" id="chk_shu_3" >割引</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_shu_4"><input type="checkbox" value="1" checked name="chk_shu_4" id="chk_shu_4" >売価返品</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_shu_5"><input type="checkbox" value="1" checked name="chk_shu_5" id="chk_shu_5" >出荷売上</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_shu_6"><input type="checkbox" value="1" checked name="chk_shu_6" id="chk_shu_6" >出荷売上累計</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_shu_7"><input type="checkbox" value="1" checked name="chk_shu_7" id="chk_shu_7" >単価</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_shu_8"><input type="checkbox" value="1" checked name="chk_shu_8" id="chk_shu_8" >前年/前月比</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_shu_9"><input type="checkbox" value="1" checked name="chk_shu_9" id="chk_shu_9" >達成率</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_shu_10"><input type="checkbox" value="1" checked name="chk_shu_10" id="chk_shu_10" >件数(前年/前月)</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_shu_11"><input type="checkbox" value="1" checked name="chk_shu_11" id="chk_shu_11" >出荷売上(前年/前月)</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_shu_12"><input type="checkbox" value="1" checked name="chk_shu_12" id="chk_shu_12" >出荷売上累計(前年/前月)</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_shu_13"><input type="checkbox" value="1" checked name="chk_shu_13" id="chk_shu_13" >単価(前年/前月)</label>
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