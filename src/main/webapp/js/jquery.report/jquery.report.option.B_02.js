/**
 * jquery report container
 * セグメント別実績
 */

;(function($) {

	$.extend({
		reportOption: {
		name:		'ReportB_02',				// （必須）レポートオプションの確認
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
		initedObjCount: 7,
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

			// データ表示エリア初期化
			that.setGrid($.id.gridholder, reportno);
			this.setCheck(reportno, $.id.lbl_check);		// レポート更新チェック
			this.setChkBox(reportno);						// チェックボックス：表示項目
			this.setYy(reportno, $.id.select_yy);			// 基準年
			this.setMm(reportno, $.id.select_mm);			// 基準月
			this.setTenpoG(reportno, $.id.select_tenpo_g);	// 店舗
			this.setType2(reportno, $.id.select_htype2);	// 軸項目

			// 初期化終了
			this.initializes=!this.initializes;
			// ログ出力
			$.log(that.timeData, 'init initialize:');
		},
		validation: function (){				// （必須）批准
			// 入力チェック
			var rt = true;
			var tenposb =	$.getJSONObject(this.jsonString, $.id.select_tenpo_g).value.split(",");
			var h_type2	=	$.getJSONObject(this.jsonString, $.id.select_htype2).value;	//
			//alert(tenposb.length+' test '+tenposb);
			// 店舗数確認
			if (tenposb == "" ) {
				// エラーメッセージ設定
				this.message = $.message.ID_MESSAGE_SEL_TENPO2;
				rt = false;
			}
			// 店舗数確認
			if ((tenposb.length > 25 ) && (h_type2 == "1")) {
				// エラーメッセージ設定
				this.message = $.message.ID_MESSAGE_SEL_TENPO;
				rt = false;
			}
			// 項目数確認
			var items = null;
			if(this.button=="2"){		// 出荷売上
				items= $('#' + $.id.item_shu + " input:checked");
			}else if(this.button=="3"){	// 調整売上
				items= $('#' + $.id.item_cho + " input:checked");
			}else {						// 純売上
				items= $('#' + $.id.item_jun + " input:checked");
			}
			if(items.length <= 0){
				// エラーメッセージ設定
				this.message = $.message.ID_MESSAGE_SEL_ITEM;
				rt = false;
			}
			if(!rt){
				alert(this.message);
			}
			return rt;
		},
		success: function(reportno,sortable){			// （必須）正処理
			var that = this;
			// 検索実行
			var szJiseki		=	this.button;													// 純売上・出荷売上・調整売上区分
			var szSelYy			=	$.getJSONObject(this.jsonString, $.id.select_yy).value;			// 年
			var szSelMm			=	$.getJSONObject(this.jsonString, $.id.select_mm).value;			// 月
			var szSelTenpoG		=	$.getJSONObject(this.jsonString, $.id.select_tenpo_g).value;	// 店舗
			var szSelType2		=	$.getJSONObject(this.jsonString, $.id.select_htype2).value;		// 軸選択
			var szSelItemJun	=	$.getJSONObject(this.jsonString, $.id.item_jun).value;			// 表示項目-純売上
			var szSelItemShu	=	$.getJSONObject(this.jsonString, $.id.item_shu).value;			// 表示項目-出荷売上
			var szSelItemCho	=	$.getJSONObject(this.jsonString, $.id.item_cho).value;			// 表示項目-調整売上

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
					USER_ID			:	$('#'+$.id.userid).val(),	// ユーザーID
					JISEKI			:	szJiseki,					// 純売上・出荷売上・調整売上区分
					YY_KIJUN		:	szSelYy,					// 年
					MM_KIJUN		:	szSelMm,					// 月
					TENPOG			:	szSelTenpoG,				// 店舗
					H_TYPE2			:	szSelType2,					// 軸選択
					ITEM_JUN		:	szSelItemJun,				// 表示項目-純売上
					ITEM_SHU		:	szSelItemShu,				// 表示項目-出荷売上
					ITEM_CHO		:	szSelItemCho,				// 表示項目-調整売上
					t				:	(new Date()).getTime(),
					sortable		:	sortable,
					sortName		:	that.sortName,
					sortOrder		:	that.sortOrder,
					records			:	0							// 総レコード数（初期化）
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
						// 列表示切替
						var h_type2	=	$.getJSONObject(that.jsonString, $.id.select_htype2).value;
						if(json!=undefined && json.rows != undefined){
							if (h_type2 === "1") {
								// 選択した店舗名称を取得
								var tenpos = $.getJSONObject(that.jsonString, $.id.select_tenpo_g).text.split(",");
								// 選択した項目を取得
								var itemval = null;
								var itemnm = null;
								if(szJiseki=="2"){			// 出荷売上
									itemval= $.getJSONObject(that.jsonString, $.id.item_shu).value.split(",");
									itemnm = $.getJSONObject(that.jsonString, $.id.item_shu).text.split(",");
								}else if(szJiseki=="3"){	// 調整売上
									itemval= $.getJSONObject(that.jsonString, $.id.item_cho).value.split(",");
									itemnm = $.getJSONObject(that.jsonString, $.id.item_cho).text.split(",");
								}else {						// 純売上
									itemval= $.getJSONObject(that.jsonString, $.id.item_jun).value.split(",");
									itemnm = $.getJSONObject(that.jsonString, $.id.item_jun).text.split(",");
								}
								var items = new Array();
								for (var i=0; i<itemval.length; i++){
									if(itemval[i]==1){
										items.push(itemnm[i]);
									}
								}

								var fColumn=[];
								var columnTop=[];		// 店舗名称
								var columnBottom=[];	// 項目名称（店舗×表示項目）

								// 共通列作成
								fColumn.push({field:'F1',title:'SORT',width:10,hidden:true});
								fColumn.push({field:'F2',title:'年月日',width:100,styler:function(){return 'background:#b0c4de;font-weight: bold;';}});

								// 店舗ヘッダー列作成
								columnTop.push({title:'店舗計',colspan:items.length});
								var byte = 0;									// 店舗名折り返し調整(選択項目が1、もしくは特定の組合せ時調整実施)
								if($.inArray(items.join(""), ['単価','前年比'])!=-1){													// width: 65
									byte = 8;
								} else if($.inArray(items.join(""), ['レジ客数','計画客数','客数達成率','売上達成率'])!=-1){			// width: 75
									byte = 10;
								} else if($.inArray(items.join(""), ['レジ客数累計','純売上','出荷売上','調整前売上','純売上累計'
																	,'出荷売上累計','調整前累計','予算','計画出荷売上'])!=-1){			// width: 85
									byte = 12;
								} else if(items.length == 2){
									if($.inArray(items.join(""), ['単価前年比'])!=-1){													// width:130
										byte = 20;
									} else if($.inArray(items[0], ['レジ客数','計画客数','客数達成率','売上達成率'])!=-1
										&& $.inArray(items[1], ['レジ客数','計画客数','客数達成率','売上達成率'])!=-1){					// width:150
										byte = 24;
									} else if($.inArray(items[0], ['レジ客数','計画客数','客数達成率','単価','売上達成率','前年比'])!=-1
										&& $.inArray(items[1], ['レジ客数','計画客数','客数達成率','単価','売上達成率','前年比'])!=-1){	// width:140
										byte = 22;
									} else if($.inArray(items[0], ['単価','前年比','レジ客数累計','純売上','出荷売上','調整前売上','純売上累計'
																	,'出荷売上累計','調整前累計','予算','計画出荷売上'])!=-1
										&& $.inArray(items[1], ['単価','前年比','レジ客数累計','純売上','出荷売上','調整前売上','純売上累計'
																,'出荷売上累計','調整前累計','予算','計画出荷売上'])!=-1){				// width:150
										byte = 24;
									}
								}

								for (var index=0; index<tenpos.length; index++){
									var tit = tenpos[index];
									if(byte!=0 && byte<tit.length*2){
										tit = $.getNewHeaderText(tit,byte);
									}
									columnTop.push({title:tit,colspan:items.length});
								}

								// 項目ヘッダー列作成
								var filed=3;								// 項目開始位置
								var num = json.rows.length - filed + 1;		// 項目列数
								for (var i=0; i<num; i++){
									var tit = items[i%items.length];

									if($.inArray(tit, ['レジ客数','計画客数'])!=-1){
										columnBottom.push({field:'F'+filed, title:tit, width: 75, resizable:true, align:'right',
											formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0');}});
									}else if($.inArray(tit, ['客数達成率','売上達成率'])!=-1){
										columnBottom.push({field:'F'+filed, title:tit, width: 75, resizable:true, align:'right',
											formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0.0');}});
									}else if($.inArray(tit, ['前年比'])!=-1){
										columnBottom.push({field:'F'+filed, title:tit, width: 65, resizable:true, align:'right',
											formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0.0');}});
									}else if($.inArray(tit, ['単価'])!=-1){
										columnBottom.push({field:'F'+filed, title:tit, width: 65, resizable:true, align:'right',
											formatter:function(value,rowData,rowIndex){return '￥'+$.getFormat(value,'#,##0');}});
									}else{
										columnBottom.push({field:'F'+filed, title:tit, width: 85, resizable:true, align:'right',
											formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0');}});
									}
									filed++;
								}

								// datagrid 形式にタイトル情報の加工
								var fColumns = [];
								var columns = [];
								fColumns.push(fColumn);
								columns.push(columnTop);
								columns.push(columnBottom);
								// datagrid のタイトル再設定
								//$($.id.gridholder).datagrid({ columns:columns });
								$($.id.gridholder).datagrid({ frozenColumns:fColumns, columns:columns });
							}else {                       //縦:店舗　横:年月日
								var fColumn=[];
								fColumn.push({field:'F1',title:'SORT',hidden:true});
								fColumn.push({field:'F2',title:'店舗', width:120,styler:function(){return 'background:#b0c4de;font-weight: bold;width:120px;';}});
								fColumn.push({field:'F3',title:'項目', width:100,styler:function(){return 'background:#b0c4de;font-weight: bold;width:100px';}});
								var column=[];
								var filed = 4;
								// 数値型列作成
								var num = json.rows.length - filed + 1;
								for (var index=0; index<num; index++){
									column.push({field:'F'+filed, title:json.rows[filed-1], width: 90, resizable:true, align:'right',
										formatter:function(value,rowData,rowIndex){
											if($.inArray(rowData.F3,['客数達成率','売上達成率','前年比'])!=-1){return $.getFormat(value,'#,##0.0');}
											if($.inArray(rowData.F3,['単価'])!=-1){return '￥' + $.getFormat(value,'#,##0');}
											return $.getFormat(value,'#,##0');
										}});
									filed++;
								}
								var fColumns = [];
								var columns = [];
								fColumns.push(fColumn);
								columns.push(column);
								$($.id.gridholder).datagrid({ frozenColumns:fColumns, columns:columns });
							}
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

			// 基準年月
			this.jsonString.push({
				id:		$.id.select_yy,
				value:	$('#'+$.id.select_yy).val(),
				text:	$('#'+$.id.select_yy+' option:selected').text()
			});
			this.jsonString.push({
				id:		$.id.select_mm,
				value:	$('#'+$.id.select_mm).val(),
				text:	$('#'+$.id.select_mm+' option:selected').text()
			});
			this.jsonString.push({
				id:		$.id.select_htype2,
				value:	$('#'+$.id.select_htype2).val(),
				text:	$('#'+$.id.select_htype2+' option:selected').text()
			});
			this.jsonString.push({
				id:		$.id.select_tenpo_g,
				value:	$("input[name="+ $.id.select_tenpo_g + "]:checked").map(function(){return $(this).val();}).get().join(","),
				text:	$("input[name="+ $.id.select_tenpo_g + "]:checked").map(function(){return $(this).parent('label').text();}).get().join(",")
			});
			// 表示項目
			this.jsonString.push({
				id:		$.id.item_jun,
				value:	$('#' + $.id.item_jun).find("input").map(function(){return $(this).is(":checked")?1:0;}).get().join(","),
				text:	$('#' + $.id.item_jun).find("input").map(function(){return $(this).parent('label').text();}).get().join(",")
			});
			this.jsonString.push({
				id:		$.id.item_shu,
				value:	$('#' + $.id.item_shu).find("input").map(function(){return $(this).is(":checked")?1:0;}).get().join(","),
				text:	$('#' + $.id.item_shu).find("input").map(function(){return $(this).parent('label').text();}).get().join(",")
			});
			this.jsonString.push({
				id:		$.id.item_cho,
				value:	$('#' + $.id.item_cho).find("input").map(function(){return $(this).is(":checked")?1:0;}).get().join(","),
				text:	$('#' + $.id.item_cho).find("input").map(function(){return $(this).parent('label').text();}).get().join(",")
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
			$($.id.button_search2).attr("disabled",false);
			$($.id.button_search3).attr("disabled",false);
			$($.id.button_excel).attr("disabled",false);
			$($.id.button_item).attr("disabled",false);
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
								$($.id.button_search2).attr("style","visibility: hidden;");
								$($.id.button_search3).attr("style","visibility: hidden;");
								$($.id.button_excel).attr("style","visibility: hidden;");
								$('#'+id).attr("style","color: #ff0000;");
							}
						}
					},
					"json"
			);
		},
		setChkBox: function(reportno){		// チェックボックス：チャネル区分/表示項目
			// 初期値設定
			var chkVals1 = $.convSelectVal($.getFirstValue(this,$.id.item_jun,'VIEW_ITEM_JUN'),true);				// 表示項目-純売上
			var chkVals2 = $.convSelectVal($.getFirstValue(this,$.id.item_shu,'VIEW_ITEM_SHU'),true);				// 表示項目-出荷売上
			var chkVals3 = $.convSelectVal($.getFirstValue(this,$.id.item_cho,'VIEW_ITEM_CHO'),true);				// 表示項目-調整売上
			$.Columns.init(chkVals1, chkVals2, chkVals3);
			// 初期表示終了時にイベント実行
			if (this.initedObject.length == this.initedObjCount){
				this.triggerInitEvent();
			}
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
						obj1.val($.getFirstValue(that,id ,'YY_SEL'));
						// 初期表示終了時にイベント実行
						if (that.initedObject.length == that.initedObjCount){
							that.triggerInitEvent();
						}
					},
					"json"
			);
		},
		setMm: function(reportno, id){	// 基準年月
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
						obj1.val($.getFirstValue(that,id ,'MM_SEL'));
						// 初期表示終了時にイベント実行
						if (that.initedObject.length == that.initedObjCount){
							that.triggerInitEvent();
						}
					},
					"json"
			);
		},
		setTenpoG: function(reportno, id){	//
			$.setChkBoxWindowL(reportno, id, "tenpo", "店舗", this, 'CD_TENPO_DETAIL');
		},
		setType2: function(reportno, id){	// 軸項目
			// 初期値設定
			 $('#'+id ).val($.getFirstValue(this,id ,'TYPE'));
			// 初期表示終了時にイベント実行
			if (this.initedObject.length == this.initedObjCount){
				this.triggerInitEvent();
			}
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
					{field:'F1',title:'SORT',hidden:true},
					{field:'F2',title:'店舗', width:120,styler:function(){return 'background:#b0c4de;font-weight: bold;';}},
					{field:'F3',title:'項目', width:100 ,styler:function(){return 'background:#b0c4de;font-weight: bold; height:20px;';}}
				]],
				columns:[[
					{field:'F4',title:' ', width:80, align:'right'}
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
		onClickButton: function (id){
			$.Columns.Open();
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
			var szSelType2	=	$.getJSONObject(this.jsonString, $.id.select_htype2).value;		// 表示項目

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
					$.outputExcel(reportno, szSelType2);			// EXCEL出力時に区分値を利用する場合、null変更。
				}
			});
		}

	} });
})(jQuery);