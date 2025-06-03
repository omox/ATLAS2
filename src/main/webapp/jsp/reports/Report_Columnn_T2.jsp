<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="MS932" %>
<style type="text/css">
input[type="radio"],input[type="checkbox"]{
  position: relative;
  top: 2px;
}
</style>
<div id="itemWindow" class="easyui-dialog">
<div class="easyui-layout" fit="true">
	<div data-options="region:'center'" style="font-size: 105%;">
		<fieldset style="float:left;margin:0 5px;"><legend>純売上</legend>
		<table id="select_item_jun" class="tbl01">
			<tr>
				<td>
					<label for="chk_jun_1"><input type="checkbox" value="1" checked name="chk_jun" id="chk_jun_1" >計画</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_jun_2"><input type="checkbox" value="1" checked name="chk_jun" id="chk_jun_2" >当年実績</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_jun_3"><input type="checkbox" value="1" checked name="chk_jun" id="chk_jun_3" >前年実績</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_jun_4"><input type="checkbox" value="1" checked name="chk_jun" id="chk_jun_4" >達成率</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_jun_5"><input type="checkbox" value="1" checked name="chk_jun" id="chk_jun_5" >前年比</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_jun_6"><input type="checkbox" value="1" checked name="chk_jun" id="chk_jun_6" >レジ件数-オルビス新規</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_jun_7"><input type="checkbox" value="1" checked name="chk_jun" id="chk_jun_7" >レジ件数-リピート</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_jun_8"><input type="checkbox" value="1" checked name="chk_jun" id="chk_jun_8" >レジ件数-カードなし</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_jun_9"><input type="checkbox" value="1" checked name="chk_jun" id="chk_jun_9" >レジ件数-未登録</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_jun_10"><input type="checkbox" value="1" checked name="chk_jun" id="chk_jun_10" >レジ件数-計</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_jun_11"><input type="checkbox" value="1" checked name="chk_jun" id="chk_jun_11" >レジ件数-前年実績</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_jun_12"><input type="checkbox" value="1" checked name="chk_jun" id="chk_jun_12" >レジ件数-前年比</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_jun_13"><input type="checkbox" value="1" checked name="chk_jun" id="chk_jun_13" >レジ件数-レジ単価</label>
				</td>
			</tr>
		</table>
		</fieldset>
		<fieldset style="float:left;margin-right:5px;"><legend>出荷売上</legend>
		<table id="select_item_shu" class="tbl01">
			<tr>
				<td>
					<label for="chk_shu_1"><input type="checkbox" value="1" checked name="chk_shu" id="chk_shu_1" style="display:none;">　</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_shu_2"><input type="checkbox" value="1" checked name="chk_shu" id="chk_shu_2" >当年実績</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_shu_3"><input type="checkbox" value="1" checked name="chk_shu" id="chk_shu_3" >前年実績</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_shu_4"><input type="checkbox" value="1" checked name="chk_shu" id="chk_shu_4" style="display:none;">　</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_shu_5"><input type="checkbox" value="1" checked name="chk_shu" id="chk_shu_5" >前年比</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_shu_6"><input type="checkbox" value="1" checked name="chk_shu" id="chk_shu_6" >レジ件数-オルビス新規</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_shu_7"><input type="checkbox" value="1" checked name="chk_shu" id="chk_shu_7" >レジ件数-リピート</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_shu_8"><input type="checkbox" value="1" checked name="chk_shu" id="chk_shu_8" >レジ件数-カードなし</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_shu_9"><input type="checkbox" value="1" checked name="chk_shu" id="chk_shu_9" >レジ件数-未登録</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_shu_10"><input type="checkbox" value="1" checked name="chk_shu" id="chk_shu_10" >レジ件数-計</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_shu_11"><input type="checkbox" value="1" checked name="chk_shu" id="chk_shu_11" >レジ件数-前年実績</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_shu_12"><input type="checkbox" value="1" checked name="chk_shu" id="chk_shu_12" >レジ件数-前年比</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_shu_13"><input type="checkbox" value="1" checked name="chk_shu" id="chk_shu_13" >レジ件数-レジ単価</label>
				</td>
			</tr>
		</table>
		</fieldset>
		<fieldset style="float:left;"><legend>調整前売上</legend>
		<table id="select_item_cho" class="tbl01">
			<tr>
				<td>
					<label for="chk_cho_1"><input type="checkbox" value="1" checked name="chk_cho" id="chk_cho_1" style="display:none;">　</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_cho_2"><input type="checkbox" value="1" checked name="chk_cho" id="chk_cho_2" >当年実績</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_cho_3"><input type="checkbox" value="1" checked name="chk_cho" id="chk_cho_3" >前年実績</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_cho_4"><input type="checkbox" value="1" checked name="chk_cho" id="chk_cho_4" style="display:none;">　</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_cho_5"><input type="checkbox" value="1" checked name="chk_cho" id="chk_cho_5" >前年比</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_cho_6"><input type="checkbox" value="1" checked name="chk_cho" id="chk_cho_6" >レジ件数-オルビス新規</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_cho_7"><input type="checkbox" value="1" checked name="chk_cho" id="chk_cho_7" >レジ件数-リピート</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_cho_8"><input type="checkbox" value="1" checked name="chk_cho" id="chk_cho_8" >レジ件数-カードなし</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_cho_9"><input type="checkbox" value="1" checked name="chk_cho" id="chk_cho_9" >レジ件数-未登録</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_cho_10"><input type="checkbox" value="1" checked name="chk_cho" id="chk_cho_10" >レジ件数-計</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_cho_11"><input type="checkbox" value="1" checked name="chk_cho" id="chk_cho_11" >レジ件数-前年実績</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_cho_12"><input type="checkbox" value="1" checked name="chk_cho" id="chk_cho_12" >レジ件数-前年比</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="chk_cho_13"><input type="checkbox" value="1" checked name="chk_cho" id="chk_cho_13" >レジ件数-レジ単価</label>
				</td>
			</tr>

		</table>
		</fieldset>
	</div>
	<div data-options="region:'south'" style="height: 30px;font-size: 105%;">
		<input type="button" name="btn_AllCheck"  id="btn_AllCheck"  value="全選択" class="btn01" />
		<input type="button" name="btn_AllCancel" id="btn_AllCancel" value="全解除" class="btn01" />
		<input type="button" name="btn_AllCancel" id="btn_AllClose"  value="確定"   class="btn01" />
	</div>
</div>
</div>