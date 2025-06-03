/**
 * jquery report container
 * ﾌﾟﾛﾓｰｼｮﾝ一覧
 */

;(function($) {

$.extend({
	  PromoList: {
		name:'Report_PromoList',
		initializes: false,		// 初期化フラグ
		lastIndex: -1,			// 編集位置（行）
		init: function() {
			$($.id.button_close).click(function () {$('#promoWindow').window('close');});
			// ﾌﾟﾛﾓｰｼｮﾝ一覧
			$($.id.button_promo).on("click", this.Open);
			// ﾌﾟﾛﾓｰｼｮﾝ一覧
			$($.id.button_search_promo).on("click", this.Search);

			this.setDialog("#promoWindow");

			// 基準年月
			this.setYy(reportno, $.id.select_yy);
			// dataGrid 初期化
			this.setGrid('#tt');
		},
		Open: function() {
			// ﾌﾟﾛﾓｰｼｮﾝ一覧 window 表示
			$('#promoWindow').window('open');
			$($.id.button_search_promo).click();
		},
		setYy: function(reportno, id){	// 基準年月
			var id2 = id + "_promo";
			$.post(
					$.reg.easy ,
					{
						"page"		:	'' ,
						"obj"		:	id,
						"sel"		:	(new Date()).getTime(),
						"target"	:	id,
						"action"	:	$.id.action_init,
						"rowsOnly"	:	true
					},
					function(data){
						var item = new Array();
						for(var i=0; i < data.length; i++){
							item.push('<option value="'+ data[i]['VALUE'] + '">' + data[i]['TEXT'] + '</option>');
						}
						$('#'+id2 ).append(item.join(''));
						// IE6 selected プロパティ エラー回避
						$('#'+id2 ).width();
					},
					"json"
			);
		},
		Search: function(){
			// 「検索」処理
			// ﾌﾟﾛﾓｰｼｮﾝの情報取得
			var szSelYy = $("#"+$.id.select_yy + "_promo").val();
			var id = $.id.object_search_promo;
			$.post(
					$.reg.easy ,
					{
						"page"		:	'' ,
						"obj"		:	id,
						"sel"		:	szSelYy,
						"target"	:	id,
						"action"	:	$.id.action_init,
						"rowsOnly"	:	true
					},
					function(data){
						$('#tt').datagrid('loadData',data);
					},
					"json"
			);
		},
		setGrid: function (id){	// グリッドの構築
			$(id).datagrid({
				fit:true,
				nowrap: true,
				border: false,
				striped: true,
				collapsible:false,
				remoteSort: true,
				idField:'SORT',
				fitColumns:false,	// 指定カラム幅を適用する場合、false 指定。
				columns:[[
					{field:'SORT',title:'SORT',hidden:true},
					{field:'HANTEI_YM',title:'判定月'    ,width:70 ,resizable:true,align:'left'},
					{field:'PROMO_CD' ,title:'コード'    ,width:80 ,resizable:true,align:'left'},
					{field:'HAKKOBI'  ,title:'発送開始日',width:80 ,resizable:true,align:'left'},
					{field:'PROMO_MEI',title:'コメント'  ,width:200,resizable:true,align:'left'},
					{field:'BYTAI2_CD',title:'媒体コード',width:80 ,resizable:true,align:'left'}
				]],
				pagination:false,
				rownumbers:false
			});
			if (	(!jQuery.support.opacity)
				&&	(!jQuery.support.style)
				&&	(typeof document.documentElement.style.maxHeight == "undefined")
				) {
				// ページリストに select を利用している。IE6  のバグで z-index が適用されない。
				// modalダイアログを利用する場合は、表示なしにする必要あり。
				$.fn.pagination.defaults.showPageList = false;
			}
		},
		setDialog: function (id){	// ダイアログの構築
			$(id).dialog({
				title: '月別用ﾌﾟﾛﾓｰｼｮﾝ一覧',
				width: 650,
				height:350,
				top:50,
				closed: true,
				modal: true,
				maximizable: false,
				resizable: true,
				draggable: true,
				onBeforeOpen:function(){
					// IE6SELECTBox透過対応
					if ($._ua.ltIE6){
						var item = '<iframe id="dummy" scrolling="no" frameborder="0"'
								 + ' style="z-index:100;position:absolute;width:100%;height:300;top:0;filter:alpha(opacity=1);"></iframe>';
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