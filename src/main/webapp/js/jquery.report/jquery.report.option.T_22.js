/**
 * jquery report container
 * セグメント別実績
 */

;(function($) {

	$.extend({
		reportOption: {
		name:		'ReportT_22',				// （必須）レポートオプションの確認
		jsonString:	[],							// （必須）検索条件情報
		jsonHidden: [],							// （必須）親画面からの引き継ぎ情報
		caption: function(){					// （必須）タイトル
			return $('#reportname').val();
		},
		sortName: '',	// ソート項目名
		sortOrder: '',	// ソート順
		timeData : (new Date()).getTime(),
		lastValues: [],
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
				$("#"+$.id.select_yy).closest("div").toggle(val==1);
				$("#"+$.id.select_ym).closest("div").toggle(val==2);
			});
			// 基準年月
			this.setYy(reportno, $.id.select_yy, $.id.select_yy2);
			this.setYm(reportno, $.id.select_ym, $.id.select_ym2);
			// 年月区分
			this.setYmkbn(reportno, $.id.radio_b1);
			// データ表示エリア初期化
			that.setGrid($.id.gridholder, reportno);
			// ステージ/会員区分の初期値
			this.lastValues[$.id.radio_t10] = "0";	// ステージ
			// 対比表示の最新値
			that.lastValues[$.id.radio_b2] = "3";	// なし
			// 初期化終了
			this.initializes=!this.initializes;
			// ログ出力
			$.log(that.timeData, 'init initialize:');
		},
		validation: function (){				// （必須）批准
			var rt = true;
			this.message = "";
			/* 期間指定の入力チェック */
			if($("input[name="+ $.id.radio_b1 + "]:checked")[0].id == $.id.radio_b1_1){
				if(this.isFromTo($("#"+$.id.select_yy).val(), $("#"+$.id.select_yy2).val())){
					this.message += $.message.ID_MESSAGE_INVARID_FROM_TO + "\n";
					rt = false;
				}
			}else if($("input[name="+ $.id.radio_b1 + "]:checked")[0].id == $.id.radio_b1_2){
				if(this.isFromTo($("#"+$.id.select_ym).val(), $("#"+$.id.select_ym2).val())){
					this.message += $.message.ID_MESSAGE_INVARID_FROM_TO + "\n";
					rt = false;
				}
			}
			if(!rt){
				alert(this.message);
			}
			return rt;
		},
		success: function(reportno,sortable){			// （必須）正処理
			var that = this;
			// 検索実行
			var szSelYy			=	$.getJSONObject(this.jsonString, $.id.select_yy).value;		// 開始年
			var szSelYy2		=	$.getJSONObject(this.jsonString, $.id.select_yy2).value;	// 終了年
			var szSelYm			=	$.getJSONObject(this.jsonString, $.id.select_ym).value;		// 開始年月
			var szSelYm2		=	$.getJSONObject(this.jsonString, $.id.select_ym2).value;	// 終了年月
			var szSelChanel		=	$.getJSONObject(this.jsonString, $.id.select_chanel).value;	// 販売チャネル
			var szRadSt			=	$.getJSONObject(this.jsonString, $.id.radio_t10).value;	// ステージ／会員区分 0:ステージ 1:会員区分
			var szRadB1			=	$.getJSONObject(this.jsonString, $.id.radio_b1).value;		// 照会データ指定-1:年別 2:月別
			var szRadB2			=	$.getJSONObject(this.jsonString, $.id.radio_b2).value;		// 対比表示-1:前年比 2:前月比 3:なし
			var szRadT1			=	$.getJSONObject(this.jsonString, $.id.radio_t01).value;		// Ｒ値指定-1:Ｒ３以内 2:Ｒ６以内 3:Ｒ９以内 4:Ｒ１２以内

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
					YYYY			:	szSelYy,				// 開始年
					YYYY2			:	szSelYy2,				// 終了年
					YYYY_MM			:	szSelYm,				// 開始年月
					YYYY_MM2		:	szSelYm2,				// 終了年月
					HAN_CHAN		:	szSelChanel,			// 販売チャネル
					RADST			:	szRadSt,				// ステージ／会員区分
					RAD_B1			:	szRadB1,				// 照会データ指定-1:年別 2:月別
					RAD_B2			:	szRadB2,				// 対比表示-1:前年比 2:前月比 3:なし
					RAD_T1			:	szRadT1,				// Ｒ値指定-1:Ｒ３以内 2:Ｒ６以内 3:Ｒ９以内 4:Ｒ１２以内
					t				:	(new Date()).getTime(),
					sortable		:	sortable,
					sortName		:	that.sortName,
					sortOrder		:	that.sortOrder,
					records			:	0						// 総レコード数（初期化）
				},
				function(json){
					// ログ出力
					$.log(that.timeData, 'query:');
					// 列表示切替
					if(json===undefined || json.rows === undefined){
						return;
					}

					// 初期検索時に並び替え情報のリセット
					if (sortable===0){
						// Load処理回避
						$.tryChangeURL(null);

						var options = $($.id.gridholder).datagrid('options');
						options.sortName = null;
						options.sortOrder = null;

						/** Colomns設定(不要の場合は除去) ※DataGrid用 */
						var r_type	=	$.getJSONObject(that.jsonString, $.id.radio_b2).value;	//1,2対比あり 3なし

						if (that.lastValues[$.id.radio_t10] !== szRadSt || that.lastValues[$.id.radio_b2] !== szRadB2) {
							// ステージ/会員区分の最新値
							that.lastValues[$.id.radio_t10] = szRadSt;
							// 対比表示の最新値
							that.lastValues[$.id.radio_b2] = szRadB2;

							// タイトル切替
							var columns = [];
							if (szRadSt==="0") {	// ステージ
								if (r_type === "3") {	// 対比表示-なし
									columns = [[
												{title:'レギュラー',colspan:3},
												{title:'シルバー',colspan:3},
												{title:'ゴールド',colspan:3},
												{title:'プラチナ',colspan:3},
												{title:'プレミアム',colspan:3},
												{title:'全体',colspan:3}
											],[
												{field:'F3',title:'総顧客数',width:75,align:'right',
													formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0');}
												},
												{field:'F4',title:'R値別数',width:75,align:'right',
													formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0');}
												},
												{field:'F5',title:'比率',width:50,align:'right',
													formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0.0');}
												},

												{field:'F6',title:'総顧客数',width:75,align:'right',
													styler   :function(value,rowData,rowIndex){return 'background:#ccffcc';},
													formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0');}
												},
												{field:'F7',title:'R値別数',width:75,align:'right',
													styler   :function(value,rowData,rowIndex){return 'background:#ccffcc';},
													formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0');}
												},
												{field:'F8',title:'比率',width:50,align:'right',
													styler   :function(value,rowData,rowIndex){return 'background:#ccffcc';},
													formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0.0');}
												},

												{field:'F9',title:'総顧客数',width:75,align:'right',
													formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0');}
												},
												{field:'F10',title:'R値別数',width:75,align:'right',
													formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0');}
												},
												{field:'F11',title:'比率',width:50,align:'right',
													formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0.0');}
												},

												{field:'F12',title:'総顧客数',width:75,align:'right',
													styler   :function(value,rowData,rowIndex){return 'background:#ccffcc';},
													formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0');}
												},
												{field:'F13',title:'R値別数',width:75,align:'right',
													styler   :function(value,rowData,rowIndex){return 'background:#ccffcc';},
													formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0');}
												},
												{field:'F14',title:'比率',width:50,align:'right',
													styler   :function(value,rowData,rowIndex){return 'background:#ccffcc';},
													formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0.0');}
												},

												{field:'F15',title:'総顧客数',width:75,align:'right',
													formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0');}
												},
												{field:'F16',title:'R値別数',width:75,align:'right',
													formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0');}
												},
												{field:'F17',title:'比率',width:50,align:'right',
													formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0.0');}
												},

												{field:'F18',title:'総顧客数',width:75,align:'right',
													styler   :function(value,rowData,rowIndex){return 'background:#ccffcc';},
													formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0');}
												},
												{field:'F19',title:'R値別数',width:75,align:'right',
													styler   :function(value,rowData,rowIndex){return 'background:#ccffcc';},
													formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0');}
												},
												{field:'F20',title:'比率',width:50,align:'right',
													styler   :function(value,rowData,rowIndex){return 'background:#ccffcc';},
													formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0.0');}
												}
											]];
								}else {
									var zen = "前年比";
									var zen2 = "前年差";
									if (r_type === "2"){zen = "前月比";}
									if (r_type === "2"){zen2 = "前月差";}
									columns = [[
												{title:'レギュラー',colspan:6},
												{title:'シルバー',colspan:6},
												{title:'ゴールド',colspan:6},
												{title:'プラチナ',colspan:6},
												{title:'プレミアム',colspan:6},
												{title:'全体',colspan:6}
											],[
												{field:'F3',title:'総顧客数',width:75,align:'right',
													formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0');}
												},
												{field:'F4',title:zen,width:50,align:'right',
													styler   :function(value,rowData,rowIndex){return 'background:#ccffff';},
													formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0.0');}
												},
												{field:'F5',title:'R値別数',width:75,align:'right',
													formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0');}
												},
												{field:'F6',title:zen,width:50,align:'right',
													styler   :function(value,rowData,rowIndex){return 'background:#ccffff';},
													formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0.0');}
												},
												{field:'F7',title:'比率',width:50,align:'right',
													formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0.0');}
												},
												{field:'F8',title:zen2,width:50,align:'right',
													styler   :function(value,rowData,rowIndex){return 'background:#ccffff';},
													formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0.0');}
												},

												{field:'F9',title:'総顧客数',width:75,align:'right',
													styler   :function(value,rowData,rowIndex){return 'background:#ccffcc';},
													formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0');}
												},
												{field:'F10',title:zen,width:50,align:'right',
													styler   :function(value,rowData,rowIndex){return 'background:#ccffff';},
													formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0.0');}
												},
												{field:'F11',title:'R値別数',width:75,align:'right',
													styler   :function(value,rowData,rowIndex){return 'background:#ccffcc';},
													formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0');}
												},
												{field:'F12',title:zen,width:50,align:'right',
													styler   :function(value,rowData,rowIndex){return 'background:#ccffff';},
													formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0.0');}
												},
												{field:'F13',title:'比率',width:50,align:'right',
													styler   :function(value,rowData,rowIndex){return 'background:#ccffcc';},
													formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0.0');}
												},
												{field:'F14',title:zen2,width:50,align:'right',
													styler   :function(value,rowData,rowIndex){return 'background:#ccffff';},
													formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0.0');}
												},

												{field:'F15',title:'総顧客数',width:75,align:'right',
													formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0');}
												},
												{field:'F16',title:zen,width:50,align:'right',
													styler   :function(value,rowData,rowIndex){return 'background:#ccffff';},
													formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0.0');}
												},
												{field:'F17',title:'R値別数',width:75,align:'right',
													formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0');}
												},
												{field:'F18',title:zen,width:50,align:'right',
													styler   :function(value,rowData,rowIndex){return 'background:#ccffff';},
													formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0.0');}
												},
												{field:'F19',title:'比率',width:50,align:'right',
													formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0.0');}
												},
												{field:'F20',title:zen2,width:50,align:'right',
													styler   :function(value,rowData,rowIndex){return 'background:#ccffff';},
													formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0.0');}
												},

												{field:'F21',title:'総顧客数',width:75,align:'right',
													styler   :function(value,rowData,rowIndex){return 'background:#ccffcc';},
													formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0');}
												},
												{field:'F22',title:zen,width:50,align:'right',
													styler   :function(value,rowData,rowIndex){return 'background:#ccffff';},
													formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0.0');}
												},
												{field:'F23',title:'R値別数',width:75,align:'right',
													styler   :function(value,rowData,rowIndex){return 'background:#ccffcc';},
													formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0');}
												},
												{field:'F24',title:zen,width:50,align:'right',
													styler   :function(value,rowData,rowIndex){return 'background:#ccffff';},
													formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0.0');}
												},
												{field:'F25',title:'比率',width:50,align:'right',
													styler   :function(value,rowData,rowIndex){return 'background:#ccffcc';},
													formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0.0');}
												},
												{field:'F26',title:zen2,width:50,align:'right',
													styler   :function(value,rowData,rowIndex){return 'background:#ccffff';},
													formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0.0');}
												},

												{field:'F27',title:'総顧客数',width:75,align:'right',
													formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0');}
												},
												{field:'F28',title:zen,width:50,align:'right',
													styler   :function(value,rowData,rowIndex){return 'background:#ccffff';},
													formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0.0');}
												},
												{field:'F29',title:'R値別数',width:75,align:'right',
													formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0');}
												},
												{field:'F30',title:zen,width:50,align:'right',
													styler   :function(value,rowData,rowIndex){return 'background:#ccffff';},
													formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0.0');}
												},
												{field:'F31',title:'比率',width:50,align:'right',
													formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0.0');}
												},
												{field:'F32',title:zen2,width:50,align:'right',
													styler   :function(value,rowData,rowIndex){return 'background:#ccffff';},
													formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0.0');}
												},

												{field:'F33',title:'総顧客数',width:75,align:'right',
													styler   :function(value,rowData,rowIndex){return 'background:#ccffcc';},
													formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0');}
												},
												{field:'F34',title:zen,width:50,align:'right',
													styler   :function(value,rowData,rowIndex){return 'background:#ccffff';},
													formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0.0');}
												},
												{field:'F35',title:'R値別数',width:75,align:'right',
													styler   :function(value,rowData,rowIndex){return 'background:#ccffcc';},
													formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0');}
												},
												{field:'F36',title:zen,width:50,align:'right',
													styler   :function(value,rowData,rowIndex){return 'background:#ccffff';},
													formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0.0');}
												},
												{field:'F37',title:'比率',width:50,align:'right',
													styler   :function(value,rowData,rowIndex){return 'background:#ccffcc';},
													formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0.0');}
												},
												{field:'F38',title:zen2,width:50,align:'right',
													styler   :function(value,rowData,rowIndex){return 'background:#ccffff';},
													formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0.0');}
												},
											]];
								}
							} else {				// 会員区分
								if (r_type === "3") {	// 対比表示-なし
									// 列表示切替
									if(json!=undefined && json.rows != undefined){
										var columnTop=[];		// 名称
										var columnBottom=[];	//
										var filed = 3;
										columnTop.push({title:'一般会員',colspan:3});
										columnBottom.push({
											field:'F'+filed,title:'総顧客数',width:75,align:'right',
											formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0');}
										});
										filed++;
										columnBottom.push({
											field:'F'+filed,title:'R値別数',width:75,align:'right',
											formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0');}
										});
										filed++;
										columnBottom.push({
											field:'F'+filed,title:'比率',width:50,align:'right',
											formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0.0');}
										});
										filed++;
										columnTop.push({title:'特別会員',colspan:3});
										columnBottom.push({
											field:'F'+filed,title:'総顧客数',width:75,align:'right',
											styler   :function(value,rowData,rowIndex){return 'background:#ccffcc';},
											formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0');}
										});
										filed++;
										columnBottom.push({
											field:'F'+filed,title:'R値別数',width:75,align:'right',
											styler   :function(value,rowData,rowIndex){return 'background:#ccffcc';},
											formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0');}
										});
										filed++;
										columnBottom.push({
											field:'F'+filed,title:'比率',width:50,align:'right',
											styler   :function(value,rowData,rowIndex){return 'background:#ccffcc';},
											formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0.0');}
										});
										filed++;
										columnTop.push({title:'全会員',colspan:3});
										columnBottom.push({
											field:'F'+filed,title:'総顧客数',width:75,align:'right',
											formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0');}
										});
										filed++;
										columnBottom.push({
											field:'F'+filed,title:'R値別数',width:75,align:'right',
											formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0');}
										});
										filed++;
										columnBottom.push({
											field:'F'+filed,title:'比率',width:50,align:'right',
											formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0.0');}
										});
										filed++;
										columns.push(columnTop);
										columns.push(columnBottom);
									}
								}else {
									var zen = "前年比";
									var zen2 = "前年差";
									if (r_type === "2"){zen = "前月比";}
									if (r_type === "2"){zen2 = "前月差";}
									if(json!=undefined && json.rows != undefined){
										var columnTop=[];		// 名称
										var columnBottom=[];	//
										var filed = 3;
										columnTop.push({title:'一般会員',colspan:6});
										columnBottom.push({
											field:'F'+filed,title:'総顧客数',width:75,align:'right',
											formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0');}
										});
										filed++;
										columnBottom.push({
											field:'F'+filed,title:zen,width:50,align:'right',
											styler   :function(value,rowData,rowIndex){return 'background:#ccffff';},
											formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0.0');}
										});
										filed++;
										columnBottom.push({
											field:'F'+filed,title:'R値別数',width:75,align:'right',
											formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0');}
										});
										filed++;
										columnBottom.push({
											field:'F'+filed,title:zen,width:50,align:'right',
											styler   :function(value,rowData,rowIndex){return 'background:#ccffff';},
											formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0.0');}
										});
										filed++;
										columnBottom.push({
											field:'F'+filed,title:'比率',width:50,align:'right',
											formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0.0');}
										});
										filed++;
										columnBottom.push({
											field:'F'+filed,title:zen2,width:50,align:'right',
											styler   :function(value,rowData,rowIndex){return 'background:#ccffff';},
											formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0.0');}
										});
										filed++;
										columnTop.push({title:'特別会員',colspan:6});
										columnBottom.push({
											field:'F'+filed,title:'総顧客数',width:75,align:'right',
											styler   :function(value,rowData,rowIndex){return 'background:#ccffcc';},
											formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0');}
										});
										filed++;
										columnBottom.push({
											field:'F'+filed,title:zen,width:50,align:'right',
											styler   :function(value,rowData,rowIndex){return 'background:#ccffff';},
											formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0.0');}
										});
										filed++;
										columnBottom.push({
											field:'F'+filed,title:'R値別数',width:75,align:'right',
											styler   :function(value,rowData,rowIndex){return 'background:#ccffcc';},
											formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0');}
										});
										filed++;
										columnBottom.push({
											field:'F'+filed,title:zen,width:50,align:'right',
											styler   :function(value,rowData,rowIndex){return 'background:#ccffff';},
											formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0.0');}
										});
										filed++;
										columnBottom.push({
											field:'F'+filed,title:'比率',width:50,align:'right',
											styler   :function(value,rowData,rowIndex){return 'background:#ccffcc';},
											formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0.0');}
										});
										filed++;
										columnBottom.push({
											field:'F'+filed,title:zen2,width:50,align:'right',
											styler   :function(value,rowData,rowIndex){return 'background:#ccffff';},
											formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0.0');}
										});
										filed++;
										columnTop.push({title:'全会員',colspan:6});
										columnBottom.push({
											field:'F'+filed,title:'総顧客数',width:75,align:'right',
											formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0');}
										});
										filed++;
										columnBottom.push({
											field:'F'+filed,title:zen,width:50,align:'right',
											styler   :function(value,rowData,rowIndex){return 'background:#ccffff';},
											formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0.0');}
										});
										filed++;
										columnBottom.push({
											field:'F'+filed,title:'R値別数',width:75,align:'right',
											formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0');}
										});
										filed++;
										columnBottom.push({
											field:'F'+filed,title:zen,width:50,align:'right',
											styler   :function(value,rowData,rowIndex){return 'background:#ccffff';},
											formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0.0');}
										});
										filed++;
										columnBottom.push({
											field:'F'+filed,title:'比率',width:50,align:'right',
											formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0.0');}
										});
										filed++;
										columnBottom.push({
											field:'F'+filed,title:zen2,width:50,align:'right',
											styler   :function(value,rowData,rowIndex){return 'background:#ccffff';},
											formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0.0');}
										});
										filed++;
										columns.push(columnTop);
										columns.push(columnBottom);
									}
								}
							}
							// datagrid のタイトル再設定
							$($.id.gridholder).datagrid({ columns:columns });
							// レコードクリア
							$($.id.gridholder).datagrid('loadData', []);

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
			this.jsonString.push({
				id:		$.id.select_yy2,
				value:	$('#'+$.id.select_yy2).val(),
				text:	$('#'+$.id.select_yy2+' option:selected').text()
			});
			// 基準年月
			this.jsonString.push({
				id:		$.id.select_ym,
				value:	$('#'+$.id.select_ym).val(),
				text:	$('#'+$.id.select_ym+' option:selected').text()
			});
			this.jsonString.push({
				id:		$.id.select_ym2,
				value:	$('#'+$.id.select_ym2).val(),
				text:	$('#'+$.id.select_ym2+' option:selected').text()
			});
			// 販売チャネル
			this.jsonString.push({
				id:		$.id.select_chanel,
				value:	$('#'+$.id.select_chanel).val(),
				text:	$('#'+$.id.select_chanel+' option:selected').text()
			});
			// 年月選択-----------------------------------------------------
			this.jsonString.push({
				id:		$.id.radio_b1,
				value:	$("input[name="+ $.id.radio_b1 + "]:checked").val(),
				text:	$.trim($("label[for='"+$("input:radio[name="+ $.id.radio_b1 + "]:checked").attr("id")+"']").text())
			});
			// ステージ／会員区分
			this.jsonString.push({
				id:		$.id.radio_t10,
				value:	$("input[name="+ $.id.radio_t10 + "]:checked").val(),
				text:	$.trim($("label[for='"+$("input:radio[name="+ $.id.radio_t10 + "]:checked").attr("id")+"']").text())
			});
			// Ｒ値指定
			this.jsonString.push({
				id:		$.id.radio_t01,
				value:	$("input[name="+ $.id.radio_t01 + "]:checked").val(),
				text:	$.trim($("label[for='"+$("input:radio[name="+ $.id.radio_t01 + "]:checked").attr("id")+"']").text())
			});
			// 対比表示
			this.jsonString.push({
				id:		$.id.radio_b2,
				value:	$("input[name="+ $.id.radio_b2 + "]:checked").val(),
				text:	$.trim($("label[for='"+$("input:radio[name="+ $.id.radio_b2 + "]:checked").attr("id")+"']").text())
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
			// ステージ／会員区分
			that.setStage();
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
		setYy: function(reportno, id, id2){	// 基準年
			var that = this;
			var obj1 = $('#'+id );
			var obj2 = $('#'+id2);
			obj1.change(function(){
				if(obj1.val()===""){return;}
				// ステージ／会員区分
				that.setStage();
			});
			obj2.change(function(){
				if(obj2.val()===""){return;}
				// ステージ／会員区分
				that.setStage();
			});
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
		},
		setYm: function(reportno, id, id2){	// 基準年月
			var that = this;
			var obj1 = $('#'+id );
			var obj2 = $('#'+id2);
			obj1.change(function(){
				if(obj1.val()===""){return;}
				// ステージ／会員区分
				that.setStage();
			});
			obj2.change(function(){
				if(obj2.val()===""){return;}
				// ステージ／会員区分
				that.setStage();
			});
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
						if(data.length < 12){
							$('#'+id +' option:eq(' + data.length +')').attr('selected',true);
						}else{
							$('#'+id +' option:eq(11)').attr('selected',true);
						}
						if ($.inArray(id, that.initedObject) < 0){
							that.initedObject.push(id );
							that.initedObject.push(id2);
						}
						// 初期表示終了時にイベント実行
						if (that.initedObject.length == that.initedObjCount){
							that.triggerInitEvent();
						}
					},
					"json"
			);
		},
		setYmkbn : function(reportno, id) {	// 年月区分
			var that = this;
			$( 'input[name="' + id + '"]:radio' ).change( function() {
				// ステージ／会員区分
				that.setStage();
			});
		},
		setStage: function(){	// ステージ／会員区分
			var obj1 = $('#'+$.id.div_stage);
			var obj2 = $('input[name="'+$.id.radio_t10+'"]');
			var obj3 = $($("input[name="+ $.id.radio_b1 + "]:checked"));
			var val1="",val2="";

			// 年 | 年月取得
			if(obj3[0].id == $.id.radio_b1_1){
				val1 = $('#'+$.id.select_yy).val();
				val2 = $('#'+$.id.select_yy2).val();
				start = $.key.stage_Y_from;
				lasts = $.key.stage_Y_to;
			} else {
				val1 = $('#'+$.id.select_ym).val();
				val2 = $('#'+$.id.select_ym2).val();
				start = $.key.stage_from;
				lasts = $.key.stage_to;
			}

			if (val1<start && val2<start){
				// ステージ切替（会員区分のみ）前
				obj1.hide();
				obj2.val(["1"]);
			} else if (val1>lasts && val2>lasts) {
				// 会員区分参照終了後
				obj1.hide();
				obj2.val(["0"]);
			} else {
				// ステージ／会員区分 参照可能期間
				obj1.show();
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
								{field:'F1', title:'SORT', width:10,hidden:true},
								{field:'F2', title:'年月', width:70,align:'center',
									styler:function(){return 'background:#b0c4de;width:70px;';}
								}
							]],
				columns : [[
								{title:'レギュラー',colspan:3},
								{title:'シルバー',colspan:3},
								{title:'ゴールド',colspan:3},
								{title:'プラチナ',colspan:3},
								{title:'プレミアム',colspan:3},
								{title:'全体',colspan:3}
							],[
								{field:'F3',title:'総顧客数',width:75,align:'right',
									formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0');}
								},
								{field:'F4',title:'R値別数',width:75,align:'right',
									formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0');}
								},
								{field:'F5',title:'比率',width:50,align:'right',
									formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0.0');}
								},

								{field:'F6',title:'総顧客数',width:75,align:'right',
									styler   :function(value,rowData,rowIndex){return 'background:#ccffcc';},
									formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0');}
								},
								{field:'F7',title:'R値別数',width:75,align:'right',
									styler   :function(value,rowData,rowIndex){return 'background:#ccffcc';},
									formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0');}
								},
								{field:'F8',title:'比率',width:50,align:'right',
									styler   :function(value,rowData,rowIndex){return 'background:#ccffcc';},
									formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0.0');}
								},

								{field:'F9',title:'総顧客数',width:75,align:'right',
									formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0');}
								},
								{field:'F10',title:'R値別数',width:75,align:'right',
									formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0');}
								},
								{field:'F11',title:'比率',width:50,align:'right',
									formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0.0');}
								},

								{field:'F12',title:'総顧客数',width:75,align:'right',
									styler   :function(value,rowData,rowIndex){return 'background:#ccffcc';},
									formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0');}
								},
								{field:'F13',title:'R値別数',width:75,align:'right',
									styler   :function(value,rowData,rowIndex){return 'background:#ccffcc';},
									formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0');}
								},
								{field:'F14',title:'比率',width:50,align:'right',
									styler   :function(value,rowData,rowIndex){return 'background:#ccffcc';},
									formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0.0');}
								},

								{field:'F15',title:'総顧客数',width:75,align:'right',
									formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0');}
								},
								{field:'F16',title:'R値別数',width:75,align:'right',
									formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0');}
								},
								{field:'F17',title:'比率',width:50,align:'right',
									formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0.0');}
								},

								{field:'F18',title:'総顧客数',width:75,align:'right',
									styler   :function(value,rowData,rowIndex){return 'background:#ccffcc';},
									formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0');}
								},
								{field:'F19',title:'R値別数',width:75,align:'right',
									styler   :function(value,rowData,rowIndex){return 'background:#ccffcc';},
									formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0');}
								},
								{field:'F20',title:'比率',width:50,align:'right',
									styler   :function(value,rowData,rowIndex){return 'background:#ccffcc';},
									formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0.0');}
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

			var data = { 'header': JSON.stringify(title) };
			// ステージ／会員
			var szRadSt = $.getJSONObject(this.jsonString, $.id.radio_t10).value;	// ステージ／会員区分 0:ステージ 1:会員区分
			// 対比表示
			var szRadB2 = $.getJSONObject(this.jsonString, $.id.radio_b2).value;	// 対比表示-1:前年比 2:前月比 3:なし

			// 転送
			$.ajax({
				url: $.reg.excel,
				type: 'POST',
				data: data,
				async: false,
				timeout: 1000,
				success: function(){
					// Excel出力
					$.outputExcel(reportno, szRadSt+szRadB2);
				},
				error: function(){
					// Excel出力エラー
					$.outputExcelError();
				}
			});
		}
	} });
})(jQuery);