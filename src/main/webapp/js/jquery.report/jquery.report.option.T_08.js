/**
 * jquery report container
 * セグメント別実績
 */

;(function($) {

	$.extend({
		reportOption: {
		name:		'ReportT_08',				// （必須）レポートオプションの確認
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
		initedObjCount: 3,
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
			// 基準年月
			this.setYm(reportno, $.id.select_ym);
			// プロモーション
			this.setPromo(reportno,$.id.select_promo);
			// 条件番号
			this.setSegno(reportno,$.id.select_segno);
			// データ表示エリア初期化
			that.setGrid($.id.gridholder, reportno);

			// 初期化終了
			this.initializes=!this.initializes;
			// ログ出力
			$.log(that.timeData, 'init initialize:');
		},
		validation: function (){				// （必須）批准
			// 入力チェック
			var rt = true;
			return rt;
		},
		success: function(reportno,sortable){			// （必須）正処理
			var that = this;
			// 検索実行
			var szSelYm 	=	$.getJSONObject(this.jsonString, $.id.select_ym).value;		// （必須）基準年月
			var szSelPromo	=	$.getJSONObject(this.jsonString, $.id.select_promo).value;	// （任意）プロモーション
			var szSelSegno	=	$.getJSONObject(this.jsonString, $.id.select_segno).value;	// （任意）条件番号

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
					report			:	that.name,				// レポート名
					YYYY_MM			:	szSelYm,				// 基準年月
					PROMO_CD		:	szSelPromo,				// プロモーション
					SEGNO			:	szSelSegno,				// 条件番号
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

			// 基準年月
			this.jsonString.push({
				id:		$.id.select_ym,
				value:	$('#'+$.id.select_ym).val(),
				text:	$('#'+$.id.select_ym+' option:selected').text()
			});
			//プロモーションFROM
			this.jsonString.push({
				id:		$.id.select_promo,
				value:	$('#'+$.id.select_promo).val(),
				text:	$('#'+$.id.select_promo+' option:selected').text()
			});
			//条件番号FROM
			this.jsonString.push({
				id:		$.id.select_segno,
				value:	$('#'+$.id.select_segno).val(),
				text:	$('#'+$.id.select_segno+' option:selected').text()
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
		setPromo: function(reportno,id){
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
		setSegno: function(reportno, id){	// 条件番号
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
		setGrid: function (id, reportNumber){	// グリッドの構築
		 	var that = this;
			$(id).datagrid({
				nowrap: true,
				border: false,
				striped: true,
				collapsible:false,
				remoteSort: true,
				idField:'F1',
				pageSize:200,
				frozenColumns:[[
							{field:'F1', title:'SORT', width:10,hidden:true},
							{field:'F2', title:'受注年月日',width:75,height:100,resizable:true,align:'center',
								styler:function(){return 'background:#b0c4de;font-weight: bold;';}
							}
						]],
				columns:[[
							{title:'電話',colspan:3},
							{title:'ハガキ',colspan:3},
							{title:'封書',colspan:3},
							{title:'FAX',colspan:3},
							{title:'IVR',colspan:3},
							{title:'WEB(PC)',colspan:3},
							{title:'WEB(モバイル)',colspan:3},
							{title:'WEB(スマホ)',colspan:3},
							{title:'その他',colspan:3},
							{title:'全受注形態',colspan:3}
						],[
							{field:'F3', title:'件数',width:50, resizable:true, align:'right',
								formatter:function(value,rowData,rowIndex){ return $.getFormat(value,'#,##0');}
							},
							{field:'F4', title:'累計',	width:50, resizable:true, align:'right',
								formatter:function(value,rowData,rowIndex){ return $.getFormat(value,'#,##0');}
							},
							{field:'F5', title:'累計比率',	width:60, resizable:true, align:'right',
								formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0.0');}
							},
							{field:'F6', title:'件数',width:50, resizable:true, align:'right',
								styler   :function(value,rowData,rowIndex){return 'background:#ccffcc';},
								formatter:function(value,rowData,rowIndex){ return $.getFormat(value,'#,##0');}
							},
							{field:'F7', title:'累計',	width:50, resizable:true, align:'right',
								styler   :function(value,rowData,rowIndex){return 'background:#ccffcc';},
								formatter:function(value,rowData,rowIndex){ return $.getFormat(value,'#,##0');}
							},
							{field:'F8', title:'累計比率',	width:60, resizable:true, align:'right',
								styler   :function(value,rowData,rowIndex){return 'background:#ccffcc';},
								formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0.0');}
							},
							{field:'F9', title:'件数',width:50, resizable:true, align:'right',
								formatter:function(value,rowData,rowIndex){ return $.getFormat(value,'#,##0');}
							},
							{field:'F10', title:'累計',	width:50, resizable:true, align:'right',
								formatter:function(value,rowData,rowIndex){ return $.getFormat(value,'#,##0');}
							},
							{field:'F11', title:'累計比率',	width:60, resizable:true, align:'right',
								formatter:function(value,rowData,rowIndex){ return $.getFormat(value,'#,##0.0');}
							},
							{field:'F12', title:'件数',width:50, resizable:true, align:'right',
								styler   :function(value,rowData,rowIndex){return 'background:#ccffcc';},
								formatter:function(value,rowData,rowIndex){ return $.getFormat(value,'#,##0');}
							},
							{field:'F13', title:'累計',	width:50, resizable:true, align:'right',
								styler   :function(value,rowData,rowIndex){return 'background:#ccffcc';},
								formatter:function(value,rowData,rowIndex){ return $.getFormat(value,'#,##0');}
							},
							{field:'F14', title:'累計比率',	width:60, resizable:true, align:'right',
								styler   :function(value,rowData,rowIndex){return 'background:#ccffcc';},
								formatter:function(value,rowData,rowIndex){ return $.getFormat(value,'#,##0.0');}
							},
							{field:'F15', title:'件数',width:50, resizable:true, align:'right',
								formatter:function(value,rowData,rowIndex){ return $.getFormat(value,'#,##0');}
							},
							{field:'F16', title:'累計',	width:50, resizable:true, align:'right',
								formatter:function(value,rowData,rowIndex){ return $.getFormat(value,'#,##0');}
							},
							{field:'F17', title:'累計比率',	width:60, resizable:true, align:'right',
								formatter:function(value,rowData,rowIndex){ return $.getFormat(value,'#,##0.0');}
							},
							{field:'F18', title:'件数',width:50, resizable:true, align:'right',
								styler   :function(value,rowData,rowIndex){return 'background:#ccffcc';},
								formatter:function(value,rowData,rowIndex){ return $.getFormat(value,'#,##0');}
							},
							{field:'F19', title:'累計',	width:50, resizable:true, align:'right',
								styler   :function(value,rowData,rowIndex){return 'background:#ccffcc';},
								formatter:function(value,rowData,rowIndex){ return $.getFormat(value,'#,##0');}
							},
							{field:'F20', title:'累計比率',	width:60, resizable:true, align:'right',
								styler   :function(value,rowData,rowIndex){return 'background:#ccffcc';},
								formatter:function(value,rowData,rowIndex){ return $.getFormat(value,'#,##0.0');}
							},
							{field:'F21', title:'件数',width:50, resizable:true, align:'right',
								formatter:function(value,rowData,rowIndex){ return $.getFormat(value,'#,##0');}
							},
							{field:'F22', title:'累計',	width:50, resizable:true, align:'right',
								formatter:function(value,rowData,rowIndex){ return $.getFormat(value,'#,##0');}
							},
							{field:'F23', title:'累計比率',	width:60, resizable:true, align:'right',
								formatter:function(value,rowData,rowIndex){ return $.getFormat(value,'#,##0.0');}
							},
							{field:'F24', title:'件数',width:50, resizable:true, align:'right',
								styler   :function(value,rowData,rowIndex){return 'background:#ccffcc';},
								formatter:function(value,rowData,rowIndex){ return $.getFormat(value,'#,##0');}
							},
							{field:'F25', title:'累計',	width:50, resizable:true, align:'right',
								styler   :function(value,rowData,rowIndex){return 'background:#ccffcc';},
								formatter:function(value,rowData,rowIndex){ return $.getFormat(value,'#,##0');}
							},
							{field:'F26', title:'累計比率',	width:60, resizable:true, align:'right',
								styler   :function(value,rowData,rowIndex){return 'background:#ccffcc';},
								formatter:function(value,rowData,rowIndex){ return $.getFormat(value,'#,##0.0');}
							},
							{field:'F27', title:'件数',width:50, resizable:true, align:'right',
								formatter:function(value,rowData,rowIndex){ return $.getFormat(value,'#,##0');}
							},
							{field:'F28', title:'累計',	width:50, resizable:true, align:'right',
								formatter:function(value,rowData,rowIndex){ return $.getFormat(value,'#,##0');}
							},
							{field:'F29', title:'累計比率',	width:60, resizable:true, align:'right',
								formatter:function(value,rowData,rowIndex){ return $.getFormat(value,'#,##0.0');}
							},
							{field:'F30', title:'件数',width:50, resizable:true, align:'right',
								styler   :function(value,rowData,rowIndex){return 'background:#ccffcc';},
								formatter:function(value,rowData,rowIndex){ return $.getFormat(value,'#,##0');}
							},
							{field:'F31', title:'累計',	width:60, resizable:true, align:'right',
								styler   :function(value,rowData,rowIndex){return 'background:#ccffcc';},
								formatter:function(value,rowData,rowIndex){ return $.getFormat(value,'#,##0');}
							},
							{field:'F32', title:'累計比率',	width:60, resizable:true, align:'right',
								styler   :function(value,rowData,rowIndex){return 'background:#ccffcc';},
								formatter:function(value,rowData,rowIndex){ return $.getFormat(value,'#,##0.0');}
							}
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
					$.outputExcel(reportno, null);			// EXCEL出力時に区分値を利用する場合、null変更。
				}
			});
		}
	} });
})(jQuery);