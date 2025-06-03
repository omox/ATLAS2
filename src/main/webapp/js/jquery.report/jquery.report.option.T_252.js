/**
 * jquery report container
 * セグメント別実績
 */

;(function($) {

	$.extend({
		reportOption: {
		name:		'ReportT_252',				// （必須）レポートオプションの確認
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
		initedObjCount: 8,
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
				$("#"+$.id.select_mm).prop('disabled', (val==1));
			});

			// 基準年月
			this.setYy(reportno, $.id.select_yy);
			this.setMm(reportno, $.id.select_mm);

			// 商品
			this.setSho_1(reportno,$.id.select_sho1);
			this.setSho_2(reportno,$.id.select_sho2);
			this.setSho_3(reportno,$.id.select_sho3);
			this.setSho_4(reportno,$.id.select_sho4);
			this.setSho_5(reportno,$.id.select_sho5);
			this.setSho_6(reportno,$.id.select_sho6);
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
			var szRadB1		=	$.getJSONObject(this.jsonString, $.id.radio_b1).value;		// 照会データ指定-1:年別 2:月別
			var szRadT3		=	$.getJSONObject(this.jsonString, $.id.radio_t03).value;		// 表示対象-1:年齢５歳集計 2:１歳毎集計
			var szSelYy		=	$.getJSONObject(this.jsonString, $.id.select_yy).value;		// 開始年
			var szSelYm		=	$.getJSONObject(this.jsonString, $.id.select_mm).value;		// 開始年月
			var szSelChanel	=	$.getJSONObject(this.jsonString, $.id.select_chanel).value;	// 販売チャネル
			var szSho1		=	$.getJSONObject(this.jsonString, $.id.select_sho1).value;	// 商品分類-基本分類
			var szSho2		=	$.getJSONObject(this.jsonString, $.id.select_sho2).value;	// 商品分類-大分類
			var szSho3		=	$.getJSONObject(this.jsonString, $.id.select_sho3).value;	// 商品分類-中分類
			var szSho4		=	$.getJSONObject(this.jsonString, $.id.select_sho4).value;	// 商品分類-小分類
			var szSho5		=	$.getJSONObject(this.jsonString, $.id.select_sho5).value;	// 商品分類-シリーズＡ
			var szSho6		=	$.getJSONObject(this.jsonString, $.id.select_sho6).value;	// 商品分類-シリーズＢ
			var szChkSho1	=	$.getJSONObject(this.jsonString, $.id.check_sho1).value;	// 商品集計-基本分類
			var szChkSho2	=	$.getJSONObject(this.jsonString, $.id.check_sho2).value;	// 商品集計-大分類
			var szChkSho3	=	$.getJSONObject(this.jsonString, $.id.check_sho3).value;	// 商品集計-中分類
			var szChkSho4	=	$.getJSONObject(this.jsonString, $.id.check_sho4).value;	// 商品集計-小分類
			var szChkSho5	=	$.getJSONObject(this.jsonString, $.id.check_sho5).value;	// 商品集計-シリーズＡ
			var szChkSho6	=	$.getJSONObject(this.jsonString, $.id.check_sho6).value;	// 商品集計-シリーズＢ
			var szChkSho7	=	$.getJSONObject(this.jsonString, $.id.check_item).value;	// 商品集計-商品

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
					RAD_B1			:	szRadB1,					// 照会データ指定-1:年別 2:月別
					RAD_T3			:	szRadT3,					// 表示対象-1:年齢５歳集計 2:１歳毎集計
					YYYY				:	szSelYy,					// 開始年
					YYYY_MM			:	szSelYy+szSelYm,	// 開始年月
					HAN_CHAN		:	szSelChanel,			// 販売チャネル
					SEL_SHO1		:	szSho1,						// 商品分類-基本分類
					SEL_SHO2		:	szSho2,						// 商品分類-大分類
					SEL_SHO3		:	szSho3,						// 商品分類-中分類
					SEL_SHO4		:	szSho4,						// 商品分類-小分類
					SEL_SHO5		:	szSho5,						// 商品分類-シリーズＡ
					SEL_SHO6		:	szSho6,						// 商品分類-シリーズＢ
					CHK_S1			:	szChkSho1,				// 商品集計-基本分類
					CHK_S2			:	szChkSho2,				// 商品集計-大分類
					CHK_S3			:	szChkSho3,				// 商品集計-中分類
					CHK_S4			:	szChkSho4,				// 商品集計-小分類
					CHK_S5			:	szChkSho5,				// 商品集計-シリーズＡ
					CHK_S6			:	szChkSho6,				// 商品集計-シリーズＢ
					CHK_S7			:	szChkSho7,				// 商品集計-商品
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
						// 列表示切替
						if(json!=undefined && json.rows != undefined){
							var column=[];
							var filed = 2;
							// 数値型列作成
							var num = json.rows.length - filed + 1;
							for (var index=0; index<num; index++){
								var tit = $.trim(json.rows[filed-1]);
								if(tit.indexOf("分類")!=-1||tit.indexOf("シリーズ")!=-1||tit.indexOf("商品")!=-1){
									var w = 90;
									if($.inArray(tit, ['商品番号'])!=-1){ w =  60; }
									if(tit.indexOf("シリーズ")!=-1){ w = 120; }
									if(tit.indexOf("商品名称")!=-1){ w = 200; }
									column.push({field:'F'+filed, title:tit, width: w, resizable:true, align:'left'
										,styler:function(value,rowData,rowIndex){	return 'background:#FAFAFA;';}
									});
								}else{
									var w = 65;
									if(tit=="合計顧客数"){ w = 75;}
									column.push({field:'F'+filed, title:tit, width: w, resizable:true, align:'right',
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
			// 年別区分-1:年別 2:月別
			this.jsonString.push({
				id:		$.id.radio_b1,
				value:	$("input[name="+ $.id.radio_b1 + "]:checked").val(),
				text:	$("label[for='"+$("input:radio[name="+ $.id.radio_b1 + "]:checked").attr("id")+"']").text()
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
			// 販売チャネル
			this.jsonString.push({
				id:		$.id.select_chanel,
				value:	$('#'+$.id.select_chanel).val(),
				text:	$('#'+$.id.select_chanel+' option:selected').text()
			});
			// 年齢-1:年齢５歳集計 2:１歳毎集計
			this.jsonString.push({
				id:		$.id.radio_t03,
				value:	$("input[name="+ $.id.radio_t03 + "]:checked").val(),
				text:	$("label[for='"+$("input:radio[name="+ $.id.radio_t03 + "]:checked").attr("id")+"']").text()
			});
			// 商品分類
			this.jsonString.push({
				id:		$.id.select_sho1,
				value:	$('#'+$.id.select_sho1).val(),
				text:	$('#'+$.id.select_sho1+' option:selected').text()
			});
			// 商品分類
			this.jsonString.push({
				id:		$.id.select_sho2,
				value:	$('#'+$.id.select_sho2).val(),
				text:	$('#'+$.id.select_sho2+' option:selected').text()
			});
			// 商品分類
			this.jsonString.push({
				id:		$.id.select_sho3,
				value:	$('#'+$.id.select_sho3).val(),
				text:	$('#'+$.id.select_sho3+' option:selected').text()
			});
			// 商品分類
			this.jsonString.push({
				id:		$.id.select_sho4,
				value:	$('#'+$.id.select_sho4).val(),
				text:	$('#'+$.id.select_sho4+' option:selected').text()
			});
			// 商品分類
			this.jsonString.push({
				id:		$.id.select_sho5,
				value:	$('#'+$.id.select_sho5).val(),
				text:	$('#'+$.id.select_sho5+' option:selected').text()
			});
			// 商品分類
			this.jsonString.push({
				id:		$.id.select_sho6,
				value:	$('#'+$.id.select_sho6).val(),
				text:	$('#'+$.id.select_sho6+' option:selected').text()
			});
			// 商品チェックBOX
			this.jsonString.push({
				id:		$.id.check_sho1,
				value:	$.getCheckBoxVal($.id.check_sho1),
				text:	$.trim($('#'+$.id.check_sho1).parent('label').text())
			});
			this.jsonString.push({
				id:		$.id.check_sho2,
				value:	$.getCheckBoxVal($.id.check_sho2),
				text:	$.trim($('#'+$.id.check_sho2).parent('label').text())
			});
			this.jsonString.push({
				id:		$.id.check_sho3,
				value:	$.getCheckBoxVal($.id.check_sho3),
				text:	$.trim($('#'+$.id.check_sho3).parent('label').text())
			});
			this.jsonString.push({
				id:		$.id.check_sho4,
				value:	$.getCheckBoxVal($.id.check_sho4),
				text:	$.trim($('#'+$.id.check_sho4).parent('label').text())
			});
			this.jsonString.push({
				id:		$.id.check_sho5,
				value:	$.getCheckBoxVal($.id.check_sho5),
				text:	$.trim($('#'+$.id.check_sho5).parent('label').text())
			});
			this.jsonString.push({
				id:		$.id.check_sho6,
				value:	$.getCheckBoxVal($.id.check_sho6),
				text:	$.trim($('#'+$.id.check_sho6).parent('label').text())
			});
			this.jsonString.push({
				id:		$.id.check_item,
				value:	$.getCheckBoxVal($.id.check_item),
				text:	$.trim($('#'+$.id.check_item).parent('label').text())
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
		setYy: function(reportno, id){	// 基準年
			var that = this;
			var obj1 = $('#'+id );
			$.post(
					$.reg.easy ,
					{
						"page"		:	reportno,
						"obj"			:	id,
						"sel"			:	(new Date()).getTime(),
						"target"	:	id,
						"action"	:	$.id.action_init,
						"rowsOnly":	true
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
		setMm: function(reportno, id){	// 基準年月
			var that = this;
			var obj1 = $('#'+id );
			obj1.change(function(){
				if(obj1.val()===""){return;}
			});
			$.post(
					$.reg.easy ,
					{
						"page"		:	reportno,
						"obj"			:	id,
						"sel"			:	(new Date()).getTime(),
						"target"	:	id,
						"action"	:	$.id.action_init,
						"rowsOnly":	true
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
						obj1.val($.getFirstValue(that,id,null));

						$.post(
							$.reg.easy ,
							{
								"page"		:	reportno,
								"obj"			:	id,
								"sel"			:	(new Date()).getTime(),
								"target"	:	id,
								"action"	:	$.id.action_defaults,
								"rowsOnly"	:	true
							},
							function(data){
								var item = new Array();
								for(var i=0; i < data.length; i++){
									$('#'+id).val(data[i]['VALUE']);
								}
							}
							,"json"
						);

						// 初期表示終了時にイベント実行
						if (that.initedObject.length == that.initedObjCount){
							that.triggerInitEvent();
						}
					},
					"json"
			);
		},
		setSho_1: function(reportno, id){		// 基本分類
			var id2 = $.id.select_sho2;
			var that = this;
			var obj1 = $('#'+id );
			obj1.change(function(){
					if(obj1.val()==""){return;}
					if(obj1.val()!="00"){
						$('#'+$.id.check_sho2).attr('checked',false);
						$('#'+$.id.check_sho3).attr('checked',false);
						$('#'+$.id.check_sho4).attr('checked',false);
						$('#'+$.id.check_sho5).attr('checked',false);
						$('#'+$.id.check_sho6).attr('checked',false);
					}
					// 初期化
					$('#'+id2).empty();
					$.post(
							$.reg.easy ,
							{
								"page"		:	reportno,
								"obj"		:	id2,
								"sel"		:	$(this).val(),
								"target"	:	id2,
								"action"	:	$.id.action_init,
								"rowsOnly"	:	true
							},
							function(data){
								var item = new Array();
								for(var i=0; i < data.length; i++){
									item.push('<option value="'+ data[i]['VALUE'] + '">' + data[i]['TEXT'] + '</option>');
								}
								var obj2 = $('#'+id2);
								obj2.append(item.join(''));
								// IE6 selectedプロパティエラー,幅調整不具合回避
								obj2.width();
								// 初期値設定
								obj2.val($.getFirstValue(that,id2,null)).change();
							},
							"json"
					);
				}
			);
			$.post(
					$.reg.easy ,
					{
						"page"		:	reportno,
						"obj"		:	id,
						"sel"		:	"",
						"target"	:	id,
						"action"	:	$.id.action_init,
						"rowsOnly"	:	true
					},
					function(data){
						// オプション追加
						var item = new Array();
						for(var i=0; i < data.length; i++){
							item.push('<option value="'+ data[i]['VALUE'] + '">' + data[i]['TEXT'] + '</option>');
						}
						obj1.append(item.join(''));
						// IE6 selectedプロパティエラー,幅調整不具合回避
						obj1.width();
						// 初期値設定
						obj1.val($.getFirstValue(that,id ,null)).change();
					},
					"json"
			);
		},
		setSho_2: function(reportno, id){		//  大分類
			var id2 = $.id.select_sho3;
			var that = this;
			var obj1 = $('#'+id );
			obj1.change(function(){
					if(obj1.val()==""){return;}
					$('#'+$.id.check_sho2).attr('checked',obj1.val()!="00");
					// 初期化
					$('#'+id2).empty();
					$.post(
							$.reg.easy ,
							{
								"page"		:	reportno,
								"obj"		:	id2,
								"sel"		:	$(this).val(),
								"target"	:	id2,
								"action"	:	$.id.action_init,
								"rowsOnly"	:	true
							},
							function(data){
								var item = new Array();
								for(var i=0; i < data.length; i++){
									item.push('<option value="'+ data[i]['VALUE'] + '">' + data[i]['TEXT'] + '</option>');
								}
								var obj2 = $('#'+id2);
								obj2.append(item.join(''));
								// IE6 selectedプロパティエラー,幅調整不具合回避
								obj2.width();
								// 初期値設定
								obj2.val($.getFirstValue(that,id2,null)).change();
							},
							"json"
					);
				}
			);
		},
		setSho_3: function(reportno, id){		// 中分類
			var id2 = $.id.select_sho4;
			var that = this;
			var obj1 = $('#'+id );
			obj1.change(function(){
					if(obj1.val()==""){return;}
					$('#'+$.id.check_sho3).attr('checked',obj1.val()!="00");
					// 初期化
					$('#'+id2).empty();
					$.post(
							$.reg.easy ,
							{
								"page"		:	reportno,
								"obj"		:	id2,
								"sel"		:	$('#'+id).val(),
								"target"	:	id2,
								"action"	:	$.id.action_init,
								"rowsOnly"	:	true
							},
							function(data){
								var item = new Array();
								for(var i=0; i < data.length; i++){
									item.push('<option value="'+ data[i]['VALUE'] + '">' + data[i]['TEXT'] + '</option>');
								}
								var obj2 = $('#'+id2);
								obj2.append(item.join(''));
								// IE6 selectedプロパティエラー,幅調整不具合回避
								obj2.width();
								// 初期値設定
								obj2.val($.getFirstValue(that,id2,null)).change();
							},
							"json"
					);
				}
			);
		},
		setSho_4: function(reportno, id){		// 小分類
			var id2 = $.id.select_sho5;
			var that = this;
			var obj1 = $('#'+id );
			obj1.change(function(){
					if(obj1.val()==""){return;}
					$('#'+$.id.check_sho4).attr('checked',obj1.val()!="00");
					// 初期化
					$('#'+id2).empty();
					$.post(
							$.reg.easy ,
							{
								"page"		:	reportno,
								"obj"		:	id2,
								"sel"		:	$('#'+id).val(),
								"target"	:	id2,
								"action"	:	$.id.action_init,
								"rowsOnly"	:	true
							},
							function(data){
								var item = new Array();
								for(var i=0; i < data.length; i++){
									item.push('<option value="'+ data[i]['VALUE'] + '">' + data[i]['TEXT'] + '</option>');
								}
								var obj2 = $('#'+id2);
								obj2.append(item.join(''));
								// IE6 selectedプロパティエラー,幅調整不具合回避
								obj2.width();
								// 初期値設定
								obj2.val($.getFirstValue(that,id2,null)).change();
							},
							"json"
					);
				}
			);
		},
		setSho_5: function(reportno, id){		// シリーズA
			var id2 = $.id.select_sho6;
			var that = this;
			var obj1 = $('#'+id );
			obj1.change(function(){
					if(obj1.val()==""){return;}
					$('#'+$.id.check_sho5).attr('checked',obj1.val()!="00");
					// 初期化
					$('#'+id2).empty();
					$.post(
							$.reg.easy ,
							{
								"page"		:	reportno,
								"obj"		:	id2,
								"sel"		:	$(this).val(),
								"target"	:	id2,
								"action"	:	$.id.action_init,
								"rowsOnly"	:	true
							},
							function(data){
								var item = new Array();
								for(var i=0; i < data.length; i++){
									item.push('<option value="'+ data[i]['VALUE'] + '">' + data[i]['TEXT'] + '</option>');
								}
								var obj2 = $('#'+id2);
								obj2.append(item.join(''));
								// IE6 selectedプロパティエラー,幅調整不具合回避
								obj2.width();
								// 初期値設定
								obj2.val($.getFirstValue(that,id2,null)).change();
							},
							"json"
					);
				}
			);
		},
		setSho_6: function(reportno, id){		// シリーズB
			var that = this;
			var obj1 = $('#'+id );
			obj1.change(function(){
					if(obj1.val()==""){return;}
					$('#'+$.id.check_sho6).attr('checked',obj1.val()!="00");
					// 初期表示終了時にイベント実行
					if (that.initedObject.length == that.initedObjCount){
						// 初期値設定
						obj1.val($.getFirstValue(that,id ,null));
						that.triggerInitEvent();
					}
				}
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
				view:scrollview,
				pageSize:35,
				fitColumns:false,	// 指定カラム幅を適用する場合、false 指定。
				frozenColumns:[[
					{field:'F1', title:'SORT',width:10,hidden:true}
				]],
				columns:[[
					{field:'F2', title:' ',width:100}
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
		}
	} });
})(jQuery);