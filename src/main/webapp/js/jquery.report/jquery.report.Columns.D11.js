/**
 * jquery report Columns
 *
 */

;(function($) {

	$.extend({
	  Columns: {

		initializes: false,	// 初期化フラグ
		lastIndex: -1,			// 編集位置（行）

		init: function(junVals,shuVals,choVals) {
			$('#btn_AllCheck').click(function () {$('#itemWindow').find("input").prop('checked',true);});
			$('#btn_AllCancel').click(function () {$('#itemWindow').find("input").prop('checked',false);});
			$('#btn_AllClose').click(function () {$('#itemWindow').window('close');});

			this.setDialog("#itemWindow");

			// 表示項目-純売上
			if(junVals){
				var inp = $('#' + $.id.item_jun).find("input");
				if(junVals.length == inp.length){
					for(var i=0; i < inp.length; i++){
						$(inp[i]).attr('checked',(junVals[i]=="1"));
					}
				}
			}
			// 表示項目-出荷-売上
			if(shuVals){
				var inp = $('#' + $.id.item_shu).find("input");
				if(shuVals.length == inp.length){
					for(var i=0; i < inp.length; i++){
						$(inp[i]).attr('checked',(shuVals[i]=="1"));
					}
				}
			}
			// 表示項目-調整前-売上
			if(choVals){
				var inp = $('#' + $.id.item_cho).find("input");
				if(choVals.length == inp.length){
					for(var i=0; i < inp.length; i++){
						$(inp[i]).attr('checked',(choVals[i]=="1"));
					}
				}
			}
			// 商品入力
			$('#'+$.id.object_item).on("click", this.Open);
		},
		Open: function() {
			// 入力 window 表示
			$('#itemWindow').window('open');
		},
		setDialog: function (id){	// ダイアログの構築
			// 高さ・幅を設定
			var cols = $("#itemWindow fieldset");
			var w = cols.length * 101;
			var h = 300;
			for(var i=0; i < cols.length; i++){
				var hh = $(cols[i]).find(":checkbox").length * 20 + 55;
				if(h < hh && hh < 650){	// 最大高:650px
					h = hh;
				}
			}

			$(id).dialog({
				title: '表示項目選択',
				width: w,
				height:h,
				top:50,
				closed: true,
				modal: true,
				maximizable: false,
				resizable: true,
				draggable: true,
				dosize:false,
				onBeforeOpen:function(){
					// IE6SELECTBox透過対応
					if ($._ua.ltIE6){
						var item = '<iframe id="dummy" scrolling="no" frameborder="0"'
								 + ' style="z-index:100;position:absolute;width:100%;height:100%;top:0;filter:alpha(opacity=1);"></iframe>';
						$("div:has("+id+")").after(item);
					}
				},
				onBeforeClose:function(){
					if ($._ua.ltIE6){
						$('#dummy').remove();
					}
				}
			}).dialog("close");
		}
	  }
	});

})(jQuery);