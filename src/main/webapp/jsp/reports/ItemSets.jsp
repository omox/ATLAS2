<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>

<div id="win" class="easyui-dialog" title="商品入力" 
	iconCls="icon-edit" closed="true" modal="true" maximizable="true" resizable="true" toolbar="#dlg-toolbar"
	style="width:500px;height:400px;padding:5px;">
	<div class="easyui-layout" fit="true">
		<div id="dlg-toolbar">  
		    <table cellpadding="0" cellspacing="0" style="width:100%">  
		        <tr>  
		        	<td>
						<span>商品セット</span>
						<input id="itemCell" style="width:200px">
						<a href="javascript:void(0)" id="sb1" class="easyui-splitbutton" menu="#mm1" iconCls="icon-ok" ">表示</a>
		        	</td>
		            <td style="text-align:right">
						<a href="#" id="qt" class="easyui-linkbutton" iconCls="icon-search" plain="false">商品セットで検索</a>
					</td>
		        </tr>  
		    </table>  
		</div> 
		<div region="center" border="false" style="padding:0px;background:#fff;border:0px solid #ccc;">
			<div id="tt" style="width:auto;height:auto">
			</div>
		</div>
	</div>
</div>
<div id="mm1" style="width:100px;">
	<div id="mmExpand" iconCls="icon-ok">表示</div>
	<div id="mmDelete" iconCls="icon-cancel">削除</div>
</div>
