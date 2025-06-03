/**
 * jquery report container
 * セグメント別実績
 */

;(function($) {

	$.extend({
		reportOption: {
		name:		'ReportT_19',				// （必須）レポートオプションの確認
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
		initedObjCount: 2,
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
			this.setYy(reportno, $.id.select_yy);
			// 都道府県
			this.setArea(reportno,$.id.select_area);
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
			var szSelYy		=	$.getJSONObject(this.jsonString, $.id.select_yy).value;		// 基準年
			var szSelChanel	=	$.getJSONObject(this.jsonString, $.id.select_chanel).value;	// 販売チャネル
			var szSelArea	=	$.getJSONObject(this.jsonString, $.id.select_area).value;	// 都道府県
			var szRadB2		=	$.getJSONObject(this.jsonString, $.id.radio_b2).value;		// 対比表示-1:前年比 2:前月比 3:なし

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
					YYYY			:	szSelYy,					// 基準年
					HAN_CHAN		:	szSelChanel,				// 販売チャネル
					AREA			:	szSelArea,					// 都道府県
					RAD_B2			:	szRadB2,					// 対比表示-1:前年比 2:前月比 3:なし
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

						/** Colomns設定(不要の場合は除去) ※DataGrid用 */
						if(json!=undefined && json.rows != undefined && json.opts != undefined){
							var fColumn=[];				// 固定列
							var column=[];				// 可変列
							var szRadB2 = $.getJSONObject(that.jsonString, $.id.radio_b2).value;		// 対比表示-1:前年比 2:前月比 3:なし

							var endRow = [];
							endRow.push(options.pageSize - 1);
							endRow.push(json.total - 1);

							// 共通列追加
							fColumn.push({field:'F1',title:'SORT',hidden:true});
							fColumn.push({field:'F2',title:json.rows[1],width:110,
								styler:function(value,rowData,rowIndex){
									var idx = $.trim(rowData.F1).substr(0,1);
									var color = ':#FFF;';
									if(idx=='1'){
										color = ':#ccffcc;';
									} else if(idx=='3'){
										color = ':#66cdaa;';
									} else if(idx=='A'){
										color = ':#e0ffff;';
									}
									if($.trim(rowData.F1).replace(idx,'')!='Z'
										&& $.trim(rowData.F1).replace(idx,'')!='Z1'
										&& $.inArray(rowIndex, endRow)==-1){
										return 'font-weight:bold; width:110px; background' + color + ' border-bottom-color' + color;
									} else {
										return 'font-weight:bold; width:110px; background' + color;
									}
								}
							});
							fColumn.push({field:'F3', title:json.rows[2], width:150,
								styler:function(value,rowData,rowIndex){
									if($.trim(rowData.F1).substr(0,1)=="1"){
										return 'font-weight:bold; width:150px; background:#ccffcc;';
									} else if($.trim(rowData.F1).substr(0,1)=="3"){
										return 'font-weight:bold; width:150px; background:#66cdaa;';
									} else if($.trim(rowData.F1).substr(0,1)=="A"){
										return 'font-weight:bold; width:150px; background:#e0ffff;';
									} else {
										return 'font-weight:bold; width:150px; background:#FFF;';
									}
								}
							});
							if(szRadB2!=3){
								fColumn.push({field:'F4', title:json.rows[3], width:60,
									styler:function(value,rowData,rowIndex){
										if($.trim(rowData.F1).substr(0,1)=="1"){
											return 'font-weight:bold; width:60px; background:#ccffcc;';
										} else if($.trim(rowData.F1).substr(0,1)=="3"){
											return 'font-weight:bold; width:60px; background:#66cdaa;';
										} else if($.trim(rowData.F1).substr(0,1)=="A"){
											return 'font-weight:bold; width:150px; background:#e0ffff;';
										} else {
											return 'font-weight:bold; width:60px; background:#FFF;';
										}
									}
								});
							}

							// 数値列追加
							var num = szRadB2==3?4:5;
							for (var filed=num; filed<=json.rows.length; filed++){
								var tit = json.rows[filed-1];
								var wid  = 80;
								if($.inArray(tit, ['合計',])!=-1){
									wid = 100;
								}
								column.push({field:'F'+filed, title:tit, width: wid, resizable:true, align:'right'
									,formatter:function(value,rowData,rowIndex){
									if($.inArray(rowData.F4, ['前月比','前年比'])!=-1){return $.getFormat(value,'#,##0.0');}
									return $.getFormat(value,'#,##0');}
								});
							}

							var fColumns = [];
							var columns = [];
							fColumns.push(fColumn);
							columns.push(column);
							$($.id.gridholder).datagrid({ frozenColumns:fColumns, columns:columns });
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

			// 基準年
			this.jsonString.push({
				id:		$.id.select_yy,
				value:	$('#'+$.id.select_yy).val(),
				text:	$('#'+$.id.select_yy+' option:selected').text()
			});
			// 販売チャネル
			this.jsonString.push({
				id:		$.id.select_chanel,
				value:	$('#'+$.id.select_chanel).val(),
				text:	$('#'+$.id.select_chanel+' option:selected').text()
			});
			// 都道府県
			this.jsonString.push({
				id:		$.id.select_area,
				value:	$('#'+$.id.select_area).val(),
				text:	$('#'+$.id.select_area+' option:selected').text()
			});
			// 対比表示
			this.jsonString.push({
				id:		$.id.radio_b2,
				value:	$("input[name="+ $.id.radio_b2 + "]:checked").val(),
				text:	$("label[for='"+$("input:radio[name="+ $.id.radio_b2 + "]:checked").attr("id")+"']").text()
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
		setYy: function(reportno, id){	// 基準年月
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
		setArea: function(reportno, id){	// 地域
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
		setGrid: function (id, reportNumber){	// グリッドの構築
		 	var that = this;
			$(id).datagrid({
				nowrap: true,
				border: false,
				striped: true,
				collapsible:false,
				remoteSort: true,
				idField:'F1',
				pageSize:35,
				fitColumns:false,	// 指定カラム幅を適用する場合、false 指定。
				frozenColumns:[[
								{field:'F1', title:'SORT', width:70,hidden:true},
								{field:'F2', title:'当月状態', width:110,
									styler:function(){return 'background:#b0c4de;font-weight: bold;width:110px;';}
								},
								{field:'F3', title:'前当月状態', width:150,
									styler:function(){return 'background:#b0c4de;font-weight: bold;width:150px;';}
								}
							]],
				columns:[[
					{field:'F4', title:' ',	width:85,  border:10, resizable:true, align:'right',
						formatter:function(value,rowData,rowIndex){ return $.getFormat(value,'#,##0');}
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
			// 対比表示
			var szRadB2 = $.getJSONObject(this.jsonString, $.id.radio_b2).value;		// 対比表示-1:前年比 2:前月比 3:なし

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
					$.outputExcel(reportno, szRadB2);			// EXCEL出力時に区分値を利用する場合、null変更。
				}
			});
		}
	} });
})(jQuery);