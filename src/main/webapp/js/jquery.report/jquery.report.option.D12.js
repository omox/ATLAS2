/**
 * jquery report container
 * 通店商品原価率
 */

;(function($) {

	$.extend({
		reportOption: {
		name: 'ReportD12',	// （必須）レポートオプションの確認
		jsonString:	[],			// （必須）検索条件情報
		jsonHidden: [],			// （必須）親画面からの引き継ぎ情報
		caption: function(){// （必須）タイトル
			return $('#reportname').val();
		},
		sortName: '',	// ソート項目名
		sortOrder: '',	// ソート順
		timeData : (new Date()).getTime(),
		defaultValues: [],
		titles: [],
		initedObject: [],
		initedObjCount: 12,
		initializes : true,
		initialize: function (reportno){		// （必須）初期化
			var that = this;
			// ツールバー初期化
			$($.id.toolbar).panel({
				border: false,
				onBeforeCollapse:function(){
					// Load処理回避
					$.tryChangeURL(null);
				},
				onCollapse:function(){
					$("#"+$.id.area_report).show();
					that.setResize();
					// Load処理回避
					$.tryChangeURL($.reg.jqeasy);
				},
				onBeforeExpand:function(){
					// Load処理回避
					$.tryChangeURL(null);
				},
				onExpand:function(){
					$("#"+$.id.area_report).hide();
					that.setResize();
					// Load処理回避
					$.tryChangeURL($.reg.jqeasy);
				},
				collapsible:false
			});

			// 引き継ぎ情報
			this.jsonHidden = $.getTargetValue();

			// レポート更新チェック
			this.setCheck(reportno, $.id.lbl_check);

			// 商品分類選択
			this.setSho6(reportno, $.id.select_shos_6);
			this.setSho5(reportno, $.id.select_shos_5);
			this.setSho4(reportno, $.id.select_shos_4);
			this.setSho3(reportno, $.id.select_shos_3);
			this.setSho2(reportno, $.id.select_shos_2);
			this.setSho1(reportno, $.id.select_shos_1);

			// 集計単位
			this.setKikan(reportno, $.id.radio_d02);
			// 購入商品
			this.setItem(reportno, $.id.radio_d09);

			// 年月
			this.setYy(reportno, $.id.select_yy, $.id.select_yy2);
			this.setMm(reportno, $.id.select_mm, $.id.select_mm2);

			// データ表示エリア初期化
			that.setGrid($.id.gridholder, reportno);
			// Load処理回避
			$.tryChangeURL(null);
			// ツールバー表示
			$.tryShowToolbar($.id.toolbar, that);
			// 初期化終了
			this.initializes=!this.initializes;
			// ログ出力
			$.log(that.timeData, 'init initialize:');
		},

		validation: function (){				// （必須）批准
			var rt = true;
			this.message = "";
			var szUnit		= $.getJSONObject(this.jsonString, $.id.radio_d02).value;		// 集計単位
			var szSelYy		=	$.getJSONObject(this.jsonString, $.id.select_yy).value;		// 開始年
			var szSelYy2	=	$.getJSONObject(this.jsonString, $.id.select_yy2).value;	// 終了年
			if ((szUnit==='1'||szUnit==='4') && this.isFromTo(szSelYy,szSelYy2)){
				rt = false;
				this.message = "集計期間の指定に誤りがあります。";
			}

			if(!rt){
				alert(this.message);
			}
			return rt;
		},

		success: function(reportno,sortable){			// （必須）正処理
			var that = this;

			// 検索実行
			var szChanel	= $.getJSONObject(this.jsonString, $.id.radio_d01).value;		// 購入チャネル
			var szUnit		= $.getJSONObject(this.jsonString, $.id.radio_d02).value;		// 集計単位
			var szSelYy		=	$.getJSONObject(this.jsonString, $.id.select_yy).value;		// 開始年
			var szSelMm		=	$.getJSONObject(this.jsonString, $.id.select_mm).value;		// 開始月
			var szSelYy2	=	$.getJSONObject(this.jsonString, $.id.select_yy2).value;	// 終了年
			var szSelMm2	=	$.getJSONObject(this.jsonString, $.id.select_mm2).value;	// 終了月

			var szItem		= $.getJSONObject(this.jsonString, $.id.radio_d09).value;		// 購入商品

			var szSho1		=	$.getJSONObject(this.jsonString, $.id.select_shos_1).value;	// 商品分類-基本分類
			var szSho2		=	$.getJSONObject(this.jsonString, $.id.select_shos_2).value;	// 商品分類-大分類
			var szSho3		=	$.getJSONObject(this.jsonString, $.id.select_shos_3).value;	// 商品分類-中分類
			var szSho4		=	$.getJSONObject(this.jsonString, $.id.select_shos_4).value;	// 商品分類-小分類
			var szSho5		=	$.getJSONObject(this.jsonString, $.id.select_shos_5).value;	// 商品分類-シリーズＡ
			var szSho6		=	$.getJSONObject(this.jsonString, $.id.select_shos_6).value;	// 商品分類-シリーズＢ

			var szChkSho1	=	$.getJSONObject(this.jsonString, $.id.check_d10_1).value;		// 商品集計-基本分類
			var szChkSho2	=	$.getJSONObject(this.jsonString, $.id.check_d10_2).value;		// 商品集計-大分類
			var szChkSho3	=	$.getJSONObject(this.jsonString, $.id.check_d10_3).value;		// 商品集計-中分類
			var szChkSho4	=	$.getJSONObject(this.jsonString, $.id.check_d10_4).value;		// 商品集計-小分類
			var szChkSho5	=	$.getJSONObject(this.jsonString, $.id.check_d10_5).value;		// 商品集計-シリーズＡ
			var szChkSho6	=	$.getJSONObject(this.jsonString, $.id.check_d10_6).value;		// 商品集計-シリーズＢ

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
					report			:	that.name,	// レポート名
					USER_ID			:	$('#'+$.id.userid).val(),	// ユーザーID

					CHANEL			: szChanel,		// 購入チャネル
					UNIT				: szUnit,			// 集計単位
					YYYY_YY			:	szSelYy,		// 開始年
					YYYY_MM			:	szSelMm,		// 開始月
					YYYY_YY2		:	szSelYy2,		// 終了年
					YYYY_MM2		:	szSelMm2,		// 終了月

					ITEM				: szItem,			// 購入商品

					SEL_SHO1		:	szSho1,			// 商品分類-基本分類
					SEL_SHO2		:	szSho2,			// 商品分類-大分類
					SEL_SHO3		:	szSho3,			// 商品分類-中分類
					SEL_SHO4		:	szSho4,			// 商品分類-小分類
					SEL_SHO5		:	szSho5,			// 商品分類-シリーズＡ
					SEL_SHO6		:	szSho6,			// 商品分類-シリーズＢ

					CHK_S1			:	szChkSho1,	// 商品集計-基本分類
					CHK_S2			:	szChkSho2,	// 商品集計-大分類
					CHK_S3			:	szChkSho3,	// 商品集計-中分類
					CHK_S4			:	szChkSho4,	// 商品集計-小分類
					CHK_S5			:	szChkSho5,	// 商品集計-シリーズＡ
					CHK_S6			:	szChkSho6,	// 商品集計-シリーズＢ

					t						:	(new Date()).getTime(),
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

							var titles = json.rows;
							var fColumn = [];
							// 可変列作成
							var columnTop=[];		// 項目グループ
							var columnBottom=[];// 項目名称
							var startField=2;		// 開始位置
							var filed=3;				// 可変列開始位置
							// 固定列作成
							var fColumns = [];

							// datagrid のタイトル再設定
							var columns = [];

							// 固定列作成
							fColumn.push({field:'F1',	title:titles[0],		width:70});
							var num = 0;
							if (szItem==="1"||szItem==="2"||szItem==="3"){num=2;}
							if (szItem==="4"){
								num=1;
								if (szChkSho1=="on"){num++;}
								if (szChkSho2=="on"){num++;}
								if (szChkSho3=="on"){num++;}
								if (szChkSho4=="on"){num++;}
								if (szChkSho5=="on"){num++;}
								if (szChkSho6=="on"){num++;}
							}
							for (var index=1; index<num; index++){
								fColumn.push({field:'F'+(index+1),	title:titles[index],	width:70, halign:'center'});
							}
							startField = num;
							filed=num;
							that.maxMergeCell = num;
							fColumns.push(fColumn);

							if (titles[filed].split("_").length === 1) {

								for (var i=startField; i<titles.length; i++){
									var tit = titles[i].split("_")[0];
									var hed = ""

									columnBottom.push({field:'F'+(i+1), title:tit, width:100, align:'right', halign:'center',
										styler: function(value,row,index){if (value < 0){ return 'color:red;';}},
										formatter:function(value,rowData,rowIndex){
											if( typeof value === 'undefined'){
												return;
											}
											if ( this.title.indexOf('比') != -1 || rowData.F1.indexOf('率') != -1 || rowData.F1.indexOf('比') != -1) {
												return $.getFormat(value,'#,##0.00');
											}else{
												return $.getFormat(value,'#,##0');
											}
										}
									});
								}

							} else {

								// 多段ヘッダー（１段目）
								columnTop = [];
								columnTop.push({title:titles[filed+0].split("_")[0], colspan:2});
								columnTop.push({title:titles[filed+2].split("_")[0], colspan:2});

								columnTop.push({title:titles[filed+4].split("_")[0], colspan:1,rowspan:2,
									field:'F'+(titles.length-1), width:100, align:'right', halign:'center',
									styler: function(value,rowData,rowIndex){if (value < 0){ return 'color:red;';}},
									formatter:function(value,rowData,rowIndex){
										if ( rowData.F1.indexOf('率') != -1) {
											return $.getFormat(value,'#,##0.00');
										}else{
											return $.getFormat(value,'#,##0');
										}
									}
								});
								columnTop.push({title:titles[filed+5].split("_")[0], colspan:1,rowspan:2,
									field:'F'+(titles.length), width:100, align:'right', halign:'center',
									styler: function(value,rowData,rowIndex){if (value < 0){ return 'color:red;';}},
									formatter:function(value,rowData,rowIndex){
										return $.getFormat(value,'#,##0.00');
									}
									});
								columns.push(columnTop);

								// 多段ヘッダー（２段目）
								for (var i=startField; i<titles.length-2; i++){
									var tit = titles[i].split("_")[1];
									var hed = ""

									columnBottom.push({field:'F'+(i+1), title:tit, width:100, align:'right', halign:'center',
										styler: function(value,rowData,rowIndex){
											var rtnVal="";
											if ( this.title.indexOf('比') != -1 && rowData.F1.indexOf('率') != -1) {
												rtnVal="background-color:#808080;";
											}
											if (value < 0){
												rtnVal="color:red;";
											}
											return rtnVal;
										},
										formatter:function(value,rowData,rowIndex){
											if ( this.title.indexOf('比') != -1 || rowData.F1.indexOf('率') != -1) {
												return $.getFormat(value,'#,##0.00');
											}else{
												return $.getFormat(value,'#,##0');
											}
										}
									});
								}
							}

							columns.push(columnBottom);
							// datagrid のタイトル再設定
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

			// 照会条件 -----------------------------------------------------
			// 購入チャネル
			this.jsonString.push({
				id:		$.id.radio_d01,
				value:$("input[name="+ $.id.radio_d01 + "]:checked").val(),
				text:	$.trim($("label[for='"+$("input:radio[name="+ $.id.radio_d01 + "]:checked").attr("id")+"']").text())
			});
			// 集計単位
			this.jsonString.push({
				id:		$.id.radio_d02,
				value:$("input[name="+ $.id.radio_d02 + "]:checked").val(),
				text:	$.trim($("label[for='"+$("input:radio[name="+ $.id.radio_d02 + "]:checked").attr("id")+"']").text())
			});
			// 集計期間
			this.jsonString.push({
				id:		$.id.select_yy,
				value:$('#'+$.id.select_yy).val(),
				text:	$('#'+$.id.select_yy+' option:selected').text()
			});
			this.jsonString.push({
				id:		$.id.select_mm,
				value:$('#'+$.id.select_mm).val(),
				text:	$('#'+$.id.select_mm+' option:selected').text()
			});
			this.jsonString.push({
				id:		$.id.select_yy2,
				value:$('#'+$.id.select_yy2).val(),
				text:	$('#'+$.id.select_yy2+' option:selected').text()
			});
			this.jsonString.push({
				id:		$.id.select_mm2,
				value:$('#'+$.id.select_mm2).val(),
				text:	$('#'+$.id.select_mm2+' option:selected').text()
			});
			// 購入商品
			this.jsonString.push({
				id:		$.id.radio_d09,
				value:$("input[name="+ $.id.radio_d09 + "]:checked").val(),
				text:	$.trim($("label[for='"+$("input:radio[name="+ $.id.radio_d09 + "]:checked").attr("id")+"']").text())
			});

			// 分類選択
			this.jsonString.push({
				id:		$.id.select_shos_1,
				value:$('#'+$.id.select_shos_1).val(),
				text:	$('#'+$.id.select_shos_1+' option:selected').text()
			});
			this.jsonString.push({
				id:		$.id.select_shos_2,
				value:$('#'+$.id.select_shos_2).val(),
				text:	$('#'+$.id.select_shos_2+' option:selected').text()
			});
			this.jsonString.push({
				id:		$.id.select_shos_3,
				value:$('#'+$.id.select_shos_3).val(),
				text:	$('#'+$.id.select_shos_3+' option:selected').text()
			});
			this.jsonString.push({
				id:		$.id.select_shos_4,
				value:$('#'+$.id.select_shos_4).val(),
				text:	$('#'+$.id.select_shos_4+' option:selected').text()
			});
			this.jsonString.push({
				id:		$.id.select_shos_5,
				value:$('#'+$.id.select_shos_5).val(),
				text:	$('#'+$.id.select_shos_5+' option:selected').text()
			});
			this.jsonString.push({
				id:		$.id.select_shos_6,
				value:$('#'+$.id.select_shos_6).val(),
				text:	$('#'+$.id.select_shos_6+' option:selected').text()
			});

			// 分類表示
			this.jsonString.push({
				id:		$.id.check_d10_1,
				value:$.getCheckBoxVal($.id.check_d10_1),
				text:	$.trim($('#'+$.id.check_d10_1).parent('label').text())
			});
			this.jsonString.push({
				id:		$.id.check_d10_2,
				value:$.getCheckBoxVal($.id.check_d10_2),
				text:	$.trim($('#'+$.id.check_d10_2).parent('label').text())
			});
			this.jsonString.push({
				id:		$.id.check_d10_3,
				value:$.getCheckBoxVal($.id.check_d10_3),
				text:	$.trim($('#'+$.id.check_d10_3).parent('label').text())
			});
			this.jsonString.push({
				id:		$.id.check_d10_4,
				value:$.getCheckBoxVal($.id.check_d10_4),
				text:	$.trim($('#'+$.id.check_d10_4).parent('label').text())
			});
			this.jsonString.push({
				id:		$.id.check_d10_5,
				value:$.getCheckBoxVal($.id.check_d10_5),
				text:	$.trim($('#'+$.id.check_d10_5).parent('label').text())
			});
			this.jsonString.push({
				id:		$.id.check_d10_6,
				value:$.getCheckBoxVal($.id.check_d10_6),
				text:	$.trim($('#'+$.id.check_d10_6).parent('label').text())
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

			// 初期イベント
			$('#'+$.id.select_shos_1).trigger('change');								// 分類指定
			$("input[name='"+ $.id.radio_d02 +"']").trigger('change');	// 集計単位
			$("input[name='"+ $.id.radio_d09 +"']").trigger('change');	// 購入商品

			$($.id.button_search).prop("disabled",false);
			$($.id.button_excel).prop("disabled",false);

			that.initedObject.push("triggerInitEvent");
			// ログ出力
			$.log(that.timeData, 'triggerInitEvent:');
			// リサイズ
			that.setResize();
		},

		setCheck: function(reportno, id){
			// 情報設定
			$.post(
					$.reg.easy ,
					{
						"page"		:	reportno,
						"obj"			:	id,
						"sel"			:	(new Date()).getTime(),
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

		setKikan:function(reportno, id){	// 集計単位
			var that = this;
			$('input[name="' + id + '"]').on('change',function(){
				var val =$('input[name="' + id + '"]:checked').val();
				switch (val+""){
				case '1':	// 年
					$('#'+$.id.select_yy ).prop("disabled",false);
					$('#'+$.id.select_mm ).prop("disabled",true);
					$('#'+$.id.select_yy2).prop("disabled",false);
					$('#'+$.id.select_mm2).prop("disabled",true);
					break;
				case '2':	// 月
					$('#'+$.id.select_yy ).prop("disabled",false);
					$('#'+$.id.select_mm ).prop("disabled",true);
					$('#'+$.id.select_yy2).prop("disabled",true);
					$('#'+$.id.select_mm2).prop("disabled",true);
					break;
				case '4':	// 累計
					$('#'+$.id.select_yy ).prop("disabled",false);
					$('#'+$.id.select_mm ).prop("disabled",true).val($.getFirstValue(that, $.id.select_mm ,'START_YY_SEL'));
					$('#'+$.id.select_yy2).prop("disabled",true);
					$('#'+$.id.select_mm2).prop("disabled",false);
					break;
				}
				$('#'+$.id.select_yy).trigger('change');
				$('#'+$.id.select_mm).trigger('change');
			});
			that.initObj(id);
		},

		initObj: function (id){
			var that=this;
			// 初期化終了
			if ($.inArray(id, that.initedObject) < 0) that.initedObject.push(id);
			console.log(that.initedObject.length);
			// 初期表示終了時にイベント実行
			if (that.initedObject.length == that.initedObjCount) that.triggerInitEvent();
		},


		setItem:function(reportno, id){	// 購入商品
			var that = this;
			$('input[name="' + id + '"]').on('change',function(){
				var val =$('input[name="' + id + '"]:checked').val();
				var flag = (val!=='4');

				$("#"+$.id.check_d10_1).prop('disabled',true);
				$("#"+$.id.check_d10_2).prop('disabled',flag||$("#"+$.id.select_shos_2).val()!=='00');
				$("#"+$.id.check_d10_3).prop('disabled',flag||$("#"+$.id.select_shos_3).val()!=='00');
				$("#"+$.id.check_d10_4).prop('disabled',flag||$("#"+$.id.select_shos_4).val()!=='00');
				$("#"+$.id.check_d10_5).prop('disabled',flag||$("#"+$.id.select_shos_5).val()!=='00');
				$("#"+$.id.check_d10_6).prop('disabled',flag||$("#"+$.id.select_shos_6).val()!=='00');

				$("#"+$.id.select_shos_1).prop('disabled',flag);
				$("#"+$.id.select_shos_2).prop('disabled',flag);
				$("#"+$.id.select_shos_3).prop('disabled',flag);
				$("#"+$.id.select_shos_4).prop('disabled',flag);
				$("#"+$.id.select_shos_5).prop('disabled',flag);
				$("#"+$.id.select_shos_6).prop('disabled',flag);

			});
			that.initObj(id);
		},

		setYy: function(reportno, id, id2){	// 基準年
			var that = this;
			var obj1 = $('#'+id );
			var obj2 = $('#'+id2);
			// イベント
			obj1.change(function(){
				var ut = $("input[name="+ $.id.radio_d02 + "]:checked").val();
				if (ut!=="1"){	// 年以外：年同期
					$('#'+$.id.select_yy2).val($('#'+$.id.select_yy).val());
				}
			});
			$.post(
					$.reg.easy ,
					{
						"page"		:	reportno,
						"obj"			:	id,
						"sel"			:	(new Date()).getTime(),
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
						obj1.val($.getFirstValue(that,id ,'START_YY_SEL'));
						obj2.val($.getFirstValue(that,id2,'END_YY_SEL'));
						that.initObj(id);
					},
					"json"
			);
		},

		setMm: function(reportno, id, id2){	// 基準年月
			var that = this;
			var obj1 = $('#'+id );
			var obj2 = $('#'+id2);
			// イベント
			obj1.change(function(){
				var ut = $("input[name="+ $.id.radio_d02 + "]:checked").val();
				if (ut==="2" || ut==="3"){	// 月、日：月同期
					if ($('#'+$.id.select_mm).val()>$('#'+$.id.select_mm2).val()){
						$('#'+$.id.select_mm2).val($('#'+$.id.select_mm).val());
					}
				}
			});
			$.post(
					$.reg.easy ,
					{
						"page"		:	reportno,
						"obj"			:	id,
						"sel"			:	(new Date()).getTime(),
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
						obj1.val($.getFirstValue(that,id ,'START_MM_SEL'));
						obj2.val($.getFirstValue(that,id2,'END_MM_SEL'));

						// 初期値
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
										$('#'+id2).val(data[i]['VALUE']);
									}
									that.initObj(id);
								}
								,"json"
							);
					},
					"json"
			);
		},

		setSho1: function(reportno, id){		// 基本分類
			var that = this;
			var id2 = $.id.select_shos_2;
			var obj1 = $('#'+id );
			obj1.change(function(){
					if($(this).val()==""){return;}
					// 商品集計レベル‐基本分類
					$("#"+$.id.check_d10_1).prop('checked',true);
					$("#"+$.id.check_d10_1).prop('disabled',true);
					$(this).prop('disabled',!($("input[name='"+ $.id.radio_d09 +"']:checked").val()==='4'));
					// 初期化
					$('#'+id2).empty();
					$.post(
							$.reg.easy ,
							{
								"page"		:	reportno,
								"obj"			:	id2,
								"sel"			:	$(this).val(),
								"target"	:	id2,
								"action"	:	$.id.action_init,
								"rowsOnly":	true
							},
							function(data){
								// オプション追加
								var item = new Array();
								for(var i=0; i < data.length; i++){
									item.push('<option value="'+ data[i]['VALUE'] + '">' + data[i]['TEXT'] + '</option>');
								}
								var obj2 = $('#'+id2);
								obj2.append(item.join(''));
								// IE6 selectedプロパティエラー,幅調整不具合回避
								if ($._ua.ltIE6){
									obj2.width();
								}
								// 初期値設定
								obj2.val($.getFirstValue(that,id2,'SHOHIN_DAI_SEL'));
								obj2.change();
							},
							"json"
					);
				}
			);
			$.post(
					$.reg.easy ,
					{
						"page"		:	reportno,
						"obj"			:	id,
						"sel"			:	"",
						"target"	:	id,
						"action"	:	$.id.action_init,
						"rowsOnly":	true
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
						obj1.val($.getFirstValue(that,id ,'SHOHIN_KIHON_SEL'));
						that.initObj(id);
						obj1.change();
					},
					"json"
			);
		},
		setSho2: function(reportno, id){		//  大分類
			var that = this;
			var id2 = $.id.select_shos_3;
			var obj1 = $('#'+id );
			obj1.change(function(){
					if(obj1.val()==""){return;}
					// 商品集計レベル - 大分類
					$("#"+$.id.check_d10_2).prop('checked', obj1.val()!="00");
					var flag=true;
					if ($("input[name='"+ $.id.radio_d09 +"']:checked").val()==='4'){
						flag=(obj1.val()!="00");
					}
					$("#"+$.id.check_d10_2).prop('disabled',flag);
					$(this).prop('disabled',!($("input[name='"+ $.id.radio_d09 +"']:checked").val()==='4'));
					// 初期化
					$('#'+id2).empty();
					$.post(
							$.reg.easy ,
							{
								"page"		:	reportno,
								"obj"			:	id2,
								"sel"			:	$(this).val(),
								"target"	:	id2,
								"action"	:	$.id.action_init,
								"rowsOnly"	:	true
							},
							function(data){
								// オプション追加
								var item = new Array();
								for(var i=0; i < data.length; i++){
									item.push('<option value="'+ data[i]['VALUE'] + '">' + data[i]['TEXT'] + '</option>');
								}
								var obj2 = $('#'+id2);
								obj2.append(item.join(''));
								// IE6 selectedプロパティエラー,幅調整不具合回避
								if ($._ua.ltIE6){
									obj2.width();
								}
								// 初期値設定
								obj2.val($.getFirstValue(that,id2,'SHOHIN_TYU_SEL'));
								that.initObj(id);
								obj2.change();
							},
							"json"
					);
				}
			);
		},
		setSho3: function(reportno, id){		// 中分類
			var that = this;
			var obj1 = $('#'+id );
			var id2 = $.id.select_shos_4;
			obj1.change(function(){
					if(obj1.val()==""){return;}
					// 商品集計レベル - 中分類
					$("#"+$.id.check_d10_3).prop('checked',obj1.val()!="00");
					var flag=true;
					if ($("input[name='"+ $.id.radio_d09 +"']:checked").val()==='4'){
						flag=(obj1.val()!="00");
					}
					$("#"+$.id.check_d10_3).prop('disabled',flag);
					$(this).prop('disabled',!($("input[name='"+ $.id.radio_d09 +"']:checked").val()==='4'));
					// 初期化
					$('#'+id2).empty();
					$.post(
							$.reg.easy ,
							{
								"page"		:	reportno,
								"obj"			:	id2,
								"sel"			:	$(this).val(),
								"target"	:	id2,
								"action"	:	$.id.action_init,
								"rowsOnly"	:	true
							},
							function(data){
								// オプション追加
								var item = new Array();
								for(var i=0; i < data.length; i++){
									item.push('<option value="'+ data[i]['VALUE'] + '">' + data[i]['TEXT'] + '</option>');
								}
								var obj2 = $('#'+id2);
								obj2.append(item.join(''));
								// IE6 selectedプロパティエラー,幅調整不具合回避
								if ($._ua.ltIE6){
									obj2.width();
								}
								// 初期値設定
								obj2.val($.getFirstValue(that,id2,'SHOHIN_SHO_SEL'));
								that.initObj(id);
								obj2.change();
							},
							"json"
					);
				}
			);
		},
		setSho4: function(reportno, id){		// 小分類
			var that = this;
			var id2 = $.id.select_shos_5;
			$('#'+id ).change(function(){
					var obj1 = $('#'+id );
					if(obj1.val()==""){return;}
					// 商品集計レベル - 小分類
					$("#"+$.id.check_d10_4).prop('checked',obj1.val()!="00");
					var flag=true;
					if ($("input[name='"+ $.id.radio_d09 +"']:checked").val()==='4'){
						flag=(obj1.val()!="00");
					}
					$("#"+$.id.check_d10_4).prop('disabled',flag);
					$(this).prop('disabled',!($("input[name='"+ $.id.radio_d09 +"']:checked").val()==='4'));
					// 初期化
					$('#'+id2).empty();
					$.post(
							$.reg.easy ,
							{
								"page"		:	reportno,
								"obj"			:	id2,
								"sel"			:	$(this).val(),
								"target"	:	id2,
								"action"	:	$.id.action_init,
								"rowsOnly":	true
							},
							function(data){
								// オプション追加
								var item = new Array();
								for(var i=0; i < data.length; i++){
									item.push('<option value="'+ data[i]['VALUE'] + '">' + data[i]['TEXT'] + '</option>');
								}
								var obj2 = $('#'+id2);
								obj2.append(item.join(''));
								// IE6 selectedプロパティエラー,幅調整不具合回避
								if ($._ua.ltIE6){
									obj2.width();
								}
								// 初期値設定
								obj2.val($.getFirstValue(that,id2,'SHOHIN_A_SEL'));
								that.initObj(id);
								obj2.change();
							},
							"json"
					);
				}
			);
		},
		setSho5: function(reportno, id){		// シリーズA
			var that = this;
			var id2 = $.id.select_shos_6;
			$('#'+id ).change(function(){
					var obj1 = $('#'+id );
					if(obj1.val()==""){return;}
					// 商品集計レベル - シリーズA
					$("#"+$.id.check_d10_5).prop('checked',obj1.val()!="00");
					var flag=true;
					if ($("input[name='"+ $.id.radio_d09 +"']:checked").val()==='4'){
						flag=(obj1.val()!="00");
					}
					$("#"+$.id.check_d10_5).prop('disabled',flag);
					$(this).prop('disabled',!($("input[name='"+ $.id.radio_d09 +"']:checked").val()==='4'));
					// 初期化
					$('#'+id2).empty();
					$.post(
							$.reg.easy ,
							{
								"page"		:	reportno,
								"obj"			:	id2,
								"sel"			:	$(this).val(),
								"target"	:	id2,
								"action"	:	$.id.action_init,
								"rowsOnly":	true
							},
							function(data){
								// オプション追加
								var item = new Array();
								for(var i=0; i < data.length; i++){
									item.push('<option value="'+ data[i]['VALUE'] + '">' + data[i]['TEXT'] + '</option>');
								}
								var obj2 = $('#'+id2);
								obj2.append(item.join(''));
								// IE6 selectedプロパティエラー,幅調整不具合回避
								if ($._ua.ltIE6){
									obj2.width();
								}
								// 初期値設定
								obj2.val($.getFirstValue(that,id2,'SHOHIN_B_SEL'));
								that.initObj(id);
								obj2.change();
							},
							"json"
					);
				}
			);
		},
		setSho6: function(reportno, id){		// シリーズB
			var that = this;
			$('#'+id ).change(function(e){
					var obj = $('#'+id );
					if(obj.val()==""){return;}
					// 商品集計レベル - シリーズB
					$("#"+$.id.check_d10_6).prop('checked',obj.val()!="00");
					var flag=true;
					if ($("input[name='"+ $.id.radio_d09 +"']:checked").val()==='4'){
						flag=(obj.val()!="00");
					}
					$("#"+$.id.check_d10_6).prop('disabled',flag);

					$(this).prop('disabled',!($("input[name='"+ $.id.radio_d09 +"']:checked").val()==='4'));
					that.initObj(id);
				}
			);
		},

		setGrid: function (id, reportNumber){	// グリッドの構築
		 	var that = this;
			$(id).datagrid({
				nowrap: true,
				border: false,
				striped: true,
				collapsible:true,
				remoteSort: true,
				idField:'F1',
				view:scrollview,
				pageSize:40,
				fitColumns:false,	// 指定カラム幅を適用する場合、false 指定。
				frozenColumns:[[
					{field:'F1',title:'SORT',width:10,hidden:true}
				]],
				columns:[[
					{field:'F2',  title:' ', width:85,resizable:true, align:'right'}
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
				rowStyler:function(rowIndex,rowData){
					if ($.trim(rowData.F1).indexOf('合計')!=-1){
						return 'background:#DCE6F1;';
					}
					if ($.trim(rowData.F2+rowData.F3+rowData.F4+rowData.F5+rowData.F6+rowData.F7+rowData.F8).indexOf('計')!=-1){
						return 'background:#DCE6F1;';
					}
				},
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
		computeDate:function(ymd, addDays){
			var dt = $.exDate(ymd,'yyyymmdd');
			var addSec = addDays * 86400000;//日数 * 1日のミリ秒数
			var targetSec = dt.getTime() + addSec;
			dt.setTime(targetSec);
			return dt.toChar('yyyymmdd');
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