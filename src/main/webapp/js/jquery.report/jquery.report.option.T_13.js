/**
 * jquery report container
 * セグメント別実績
 */

;(function($) {

	$.extend({
		reportOption: {
		name:		'ReportT_13',				// （必須）レポートオプションの確認
		jsonString:	[],							// （必須）検索条件情報
		jsonHidden: [],							// （必須）親画面からの引き継ぎ情報
		caption: function(){					// （必須）タイトル
			return $('#reportname').val();
		},
		sortName: '',	// ソート項目名
		sortOrder: '',	// ソート順
		timeData : (new Date()).getTime(),
		defaultValues: [],
		initedObject: [],
		initedObjCount: 4,
		initializes : true,
		initialize: function (reportno){		// （必須）初期化
			var that = this;
			// ツールバー初期化
			$($.id.toolbar).panel({
				title: this.caption(),
				iconCls:'icon-cube1',
				border: false,
				onBeforeCollapse:function(){
					// Load処理回避
					$.tryChangeURL(null);
				},
				onCollapse:function(){
					that.setResize();
					// Load処理回避
					$.tryChangeURL($.reg.jqeasy);
				},
				onBeforeExpand:function(){
					// Load処理回避
					$.tryChangeURL(null);
				},
				onExpand:function(){
					that.setResize();
					// Load処理回避
					$.tryChangeURL($.reg.jqeasy);
				},
				collapsible:true
			});
			// 引き継ぎ情報
			this.jsonHidden = $.getTargetValue();

			// レポート更新チェック
			this.setCheck(reportno, $.id.lbl_check);
			// 可不可制御処理追加
			$('input[name="' + $.id.radio_b1 + '"]').change(function(){
				var val =$(this).val();
				$("#"+$.id.select_ymd).closest("div").toggle(val==3);
				$("#"+$.id.select_ymd2).closest("div").toggle(val!=3);
			});

			// 基準年月
			this.setYm2(reportno, $.id.select_ymd2);
			this.setYm(reportno, $.id.select_ymd);
			// プロモーション(FROM/TO)
			this.setPromo(reportno,$.id.select_promo_from, $.id.select_promo_to);
			// データ表示エリア初期化
			that.setGrid($.id.gridholder, reportno);

			// 初期化終了
			this.initializes=!this.initializes;
			// ログ出力
			$.log(that.timeData, 'init initialize:');
		},
		validation: function (){				// （必須）批准
			// 入力チェック
			var rt = !this.isFromTo($("#"+$.id.select_promo_from).val(), $("#"+$.id.select_promo_to).val());
			if (!rt) {
				// エラーメッセージ設定
				this.message = $.message.ID_MESSAGE_INVARID_FROM_TO_PROMO;
				alert(this.message);
			}
			return rt;
		},
		success: function(reportno,sortable){			// （必須）正処理
			var that = this;
			// 検索実行
			var szRadYm			=	$.getJSONObject(this.jsonString, $.id.radio_b1).value;			// 表示項目-1:年月日 2:年月
			var szSelYm			=	$.getJSONObject(this.jsonString, $.id.select_ymd).value;			// 年月
			var szSelYm2		=	$.getJSONObject(this.jsonString, $.id.select_ymd2).value;			// 年月
			var szSelPromoFrom	=	$.getJSONObject(this.jsonString, $.id.select_promo_from).value;	// プロモーションFROM
			var szSelPromoTo	=	$.getJSONObject(this.jsonString, $.id.select_promo_to).value;	// プロモーションTO
			var szRad			=	$.getJSONObject(this.jsonString, $.id.radio_t07).value;			// 表示項目-1:即時 2:継続

			// 処理時間計測用
			that.timeData = (new Date()).getTime();
			$($.id.gridholder).datagrid('loading');

			// grid.options 取得
			var options = $($.id.gridholder).datagrid('options');
			if (sortable) sortable=1; else sortable=0;
			that.sortName	= options.sortName;
			that.sortOrder	= options.sortOrder;

			$.getJSON(
				$.reg.jqgrid ,
				{
					report			:	that.name,					// レポート名
					RAD_YM			:	szRadYm,					// 年月日
					YYYY_MMDD		:	szSelYm,					// 年月
					YYYY_MM			:	szSelYm2,					// 年月
					PROMO_CD_FROM	:	szSelPromoFrom,				// プロモーションFROM
					PROMO_CD_TO		:	szSelPromoTo,				// プロモーションTO
					RAD				:	szRad,						// 表示項目-1:即時 2:継続
					t				:	(new Date()).getTime(),
					sortable		:	sortable,
					sortName		:	that.sortName,
					sortOrder		:	that.sortOrder,
					records			:	0						// 総レコード数（初期化）
				},
				function(json){
					// ログ出力
					$.log(that.timeData, 'query:');

					// 初期検索時に並び替え情報のリセット
					if (sortable===0){
						// Load処理回避
						$.tryChangeURL(null);

						var options = $($.id.gridholder).datagrid('options');
						options.sortName = null;
						options.sortOrder = null;

						// Colomns再設定(不要の場合は除去) ※DataGrid用
						if(json!=undefined && json.rows != undefined){
							var column=[];
							var filed = 5;
							// 数値型列作成
							var num = json.rows.length - filed + 1;
							for (var index=0; index<num; index++){
								var tit = json.rows[filed-1];
								if($.inArray(tit, ['継続率(F2-)'])!=-1){
									column.push({field:'F'+filed, title:tit, width: 90, resizable:true, align:'right',
										formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0.0');}});
								}else if($.inArray(tit, ['平均単価(回)','平均単価(人)'])!=-1){
										column.push({field:'F'+filed, title:tit, width: 90, resizable:true, align:'right',
											formatter:function(value,rowData,rowIndex){return '￥'+$.getFormat(value,'#,##0');}});
								}else{
									column.push({field:'F'+filed, title:tit, width:90, resizable:true, align:'right',
										formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0');}});
								}
								filed++;
							}
							var columns = [];
							columns.push(column);
							$($.id.gridholder).datagrid({ columns:columns });
						}
						// Load処理回避
						$.tryChangeURL($.reg.jqeasy);
					}

					// グリッド再描画
					$($.id.gridholder).datagrid('load', {} );
					// ログ出力
					$.log(that.timeData, 'loaded:');
				}
			);
		},
		getEasyUI: function(){					// （必須）情報の取得
			// 初期化
			this.jsonString = [];

			// レポート名
			this.jsonString.push({
				id:		"reportname",
				value:	this.caption(),
				text:	this.caption()
			});

			// 照会データ指定-3:日別 2:月別
			this.jsonString.push({
				id:		$.id.radio_b1,
				value:	$("input[name="+ $.id.radio_b1 + "]:checked").val(),
				text:	$("label[for='"+$("input:radio[name="+ $.id.radio_b1 + "]:checked").attr("id")+"']").text()
			});
			// 基準年月
			this.jsonString.push({
				id:		$.id.select_ymd2,
				value:	$('#'+$.id.select_ymd2).val(),
				text:	$('#'+$.id.select_ymd2+' option:selected').text()
			});
			this.jsonString.push({
				id:		$.id.select_ymd,
				value:	$('#'+$.id.select_ymd).val(),
				text:	$('#'+$.id.select_ymd+' option:selected').text()
			});
			// プロモーションTO
			this.jsonString.push({
				id:		$.id.select_promo_to,
				value:	$('#'+$.id.select_promo_to).val(),
				text:	$('#'+$.id.select_promo_to+' option:selected').text()
			});
			//プロモーションFROM
			this.jsonString.push({
				id:		$.id.select_promo_from,
				value:	$('#'+$.id.select_promo_from).val(),
				text:	$('#'+$.id.select_promo_from+' option:selected').text()
			});
			// 表示項目-1:即時 2:継続
			this.jsonString.push({
				id:		$.id.radio_t07,
				value:	$("input[name="+ $.id.radio_t07 + "]:checked").val(),
				text:	$("label[for='"+$("input:radio[name="+ $.id.radio_t07 + "]:checked").attr("id")+"']").text()
			});
			// レポート更新チェック
			this.jsonString.push({
				id:		$.id.lbl_check,
				value:	$('#'+$.id.lbl_check).val(),
				text:	$('#'+$.id.lbl_check).text()
			});
		},
		triggerInitEvent:function(){		// 初期表示時イベント実施
			var that = this;
			// Load処理回避
			$.tryChangeURL(null);
			// ツールバー表示
			$.tryShowToolbar($.id.toolbar, that);
			// 初期イベント
			$('input[name="' + $.id.radio_b1 + '"]').change();
			$($.id.button_search).attr("disabled",false);
			$($.id.button_excel).attr("disabled",false);
			// リサイズ
			that.setResize();
			// 初期表示処理完了
			that.initedObject.push("triggerInitEvent");
		},
		setCheck: function(reportno, id){
			// 情報設定
			$.post(
					$.reg.easy ,
					{
						"page"		:	reportno,
						"obj"		:	id,
						"sel"		:	(new Date()).getTime(),
						"target"	:	id,
						"action"	:	$.id.action_init,
						"rowsOnly"	:	"true"
					},
					function(data){
						if(data.length>0){
							$('#'+id).text(data[0]['MSG']);
							if(data[0]['UPDATE_FLG']=='1'){
								$($.id.button_search).attr("style","visibility: hidden;");
								$($.id.button_excel).attr("style","visibility: hidden;");
								$('#'+id).attr("style","color: #ff0000;");
							}
						}
					},
					"json"
			);
		},
		setYm: function(reportno, id){	// 基準年月
			var that = this;
			var obj1 = $('#'+id );
			$.post(
					$.reg.easy ,
					{
						"page"		:	reportno,
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
						obj1.append(item.join(''));
						// IE6 selected プロパティ エラー回避
						obj1.width();
						// 初期値設定
						obj1.val($.getFirstValue(that,id ,null));
						// 初期表示終了時にイベント実行
						if (that.initedObject.length == that.initedObjCount){
							that.triggerInitEvent();
						}
					},
					"json"
			);
		},
		setYm2: function(reportno, id){	// 基準年月
			var that = this;
			var obj1 = $('#'+id );
			$.post(
					$.reg.easy ,
					{
						"page"		:	reportno,
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
						obj1.append(item.join(''));
						// IE6 selected プロパティ エラー回避
						obj1.width();
						// 初期値設定
						obj1.val($.getFirstValue(that,id ,null));
						// 初期表示終了時にイベント実行
						if (that.initedObject.length == that.initedObjCount){
							that.triggerInitEvent();
						}
					},
					"json"
			);
		},
		setPromo: function(reportno,id,id2){
			var that = this;
			var obj1 = $('#'+id );
			var obj2 = $('#'+id2);
			$.post(
					$.reg.easy ,
					{
						"page"		:	reportno,
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
						obj1.append(item.join(''));
						obj2.append(item.join(''));
						// IE6 selected プロパティ エラー回避
						obj1.width();
						obj2.width();
						// 初期値設定
						obj1.val($.getFirstValue(that,id ,null));
						obj2.val($.getFirstValue(that,id2,null));
						// 初期表示終了時にイベント実行
						if (that.initedObject.length == that.initedObjCount){
							that.triggerInitEvent();
						}
					},
					"json"
			);
			obj1.change(function(){ obj2.val(obj1.val());});
		},
		setGrid: function (id, reportNumber){	// グリッドの構築
		 	var that = this;
			$(id).datagrid({
				nowrap: true,
				border: false,
				striped: true,
				collapsible:false,
				remoteSort: true,
				idField:'F1',
				pageSize:40,
				fitColumns:false,	// 指定カラム幅を適用する場合、false 指定。
				frozenColumns:[[
					{field:'F1', title:'SORT',width:10,hidden:true},
					{field:'F2', title:'掲載日',width:65},
					{field:'F3', title:'プロモーション',width:85, resizable:true},
					{field:'F4', title:'媒体名',width:150, resizable:true}
				]],
				columns:[[
					{field:'F5', title:' ',width:85, resizable:true, align:'right',
						formatter:function(value,rowData,rowIndex){ return $.getFormat(value,'#,##0');}},
					{field:'F6', title:' ',width:85, resizable:true, align:'right',
						formatter:function(value,rowData,rowIndex){ return $.getFormat(value,'#,##0');}},
					{field:'F7', title:' ',width:85, resizable:true, align:'right',
						formatter:function(value,rowData,rowIndex){ return $.getFormat(value,'#,##0.0');}},
					{field:'F8', title:' ',width:85, resizable:true, align:'right',
						formatter:function(value,rowData,rowIndex){ return $.getFormat(value,'#,##0');}},
					{field:'F9', title:' ',width:85, resizable:true, align:'right',
						formatter:function(value,rowData,rowIndex){ return $.getFormat(value,'#,##0');}},
					{field:'F10', title:' ',width:85, resizable:true, align:'right',
						formatter:function(value,rowData,rowIndex){ return $.getFormat(value,'#,##0');}},
					{field:'F11', title:' ',width:85, resizable:true, align:'right',
						formatter:function(value,rowData,rowIndex){ return $.getFormat(value,'#,##0');}}
				]],
				onSortColumn:function(sort, order){
					if (that.jsonString.length===0) return false;
					// カラム並び替え
					that.success(that.name, true);
				},
				onBeforeLoad:function(param){
					var options = $($.id.gridholder).datagrid('options');
					if (options.sortName !== null){
						// 並び替え設定時、ロード処理をキャンセルする
						if (that.sortName !== options.sortName || that.sortOrder !== options.sortOrder)	return false;
					}
					param.report = that.name;
				},
				onLoadSuccess:function(data){
					// ログ出力
					$.log(that.timeData, 'onLoadSuccess setGrid:');
				},
				toolbar:$.id.buttons,
				pagination:true,
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
		getRecord: function(){					// （必須）レコード件数を戻す
			var data = $($.id.gridholder).datagrid('getData');
			if (data == null) {
				return 0;
			} else {
				return data.total;
			}
		},
		setResize: function(){					// （必須）リサイズ
			var changeHeight = $(window).height();
			if (0 < changeHeight) {
				// window 幅取得
				var changeWidth  = $(window).width();

				// toolbar の調整
				$($.id.toolbar).panel('resize',{width:changeWidth});

				// DataGridの高さ
				var gridholderHeight = 0;
				var placeholderHeight = 0;

				if ($($.id.gridholder).datagrid('options') != 'undefined') {
					// tb
					placeholderHeight = $($.id.gridholder).datagrid('getPanel').offset().top;	// Gridの表示位置(TOP)

					// datagrid の格納された panel の高さ
					gridholderHeight = $(window).height() - placeholderHeight;
				}
				$($.id.gridholder).datagrid('resize', {
					width:	changeWidth,
					height:	gridholderHeight
				});
			}
		},
		isFromTo : function (from,to){
			// 第1パラメータが第2パラメータより大きい場合のみ true を返す
			return from > to;
		},
		isBlank : function(val){
			if(val === null){
				return true;
			}
			for(var i = 0; i < val.length; i++) {
				if ((val.charAt(i)!=' ')&&(val.charAt(i)!="\t")&&(val.charAt(i)!="\n")&&(val.charAt(i)!="\r")){
					return false;
				}
			}
			return true;
		},
		getJSONString : function(){				// （必須）JSON形式の文字列
		 	return this.jsonString;
		},
		tryLoadMethods: function(id){			// （オプション）combo.onChange Event
			try {
				var grid = $(id).combogrid('grid');
				grid.datagrid('load');
			} catch (e) {
				// combgrid 未更新時のERROR回避
			}
		},
		excel: function(reportno){						// (必須)Excel出力
			// グリッドの情報取得
			var options = $($.id.gridholder).datagrid('options');
			// タイトル部
			var title = [];
			title = $.outputExcelTitle(title, options.frozenColumns);
			title = $.outputExcelTitle(title, options.columns);

			var datas = {'header': JSON.stringify(title)};
			var szRad			=	$.getJSONObject(this.jsonString, $.id.radio_t07).value;			// 表示項目-1:即時 2:継続

			// 転送
			$.ajax({
				async: false,
				type: "POST",
				url : $.reg.excel,
				data: datas,
				timeout: 1000,
				error: function(request, status, errorThrown){
					// Excel出力エラー
					$.outputExcelError();
				},
				success: function(data,status){
					$.outputExcel(reportno, szRad);			// EXCEL出力時に区分値を利用する場合、null変更。
				}
			});
		}
	} });
})(jQuery);