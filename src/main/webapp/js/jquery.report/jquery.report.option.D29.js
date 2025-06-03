/**
 * jquery report container
 * 通店単価分布
 */

;(function($) {

	$.extend({
		reportOption: {
		name: 'ReportD29',	// （必須）レポートオプションの確認
		jsonString:	[],			// （必須）検索条件情報
		jsonHidden: [],			// （必須）親画面からの引き継ぎ情報
		caption: function(){// （必須）タイトル
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

			// 年月
			this.setYy(reportno, $.id.select_yy, $.id.select_yy2);
			this.setMm(reportno, $.id.select_mm, $.id.select_mm2);
			// プロモーション(FROM/TO)
			this.setPromo(reportno,$.id.select_promo_from, $.id.select_promo_to);
			// 可不可制御処理追加
			$('input[name="' + $.id.chk_dm + '"]').change(function(){
				var val =$.getCheckBoxVal($.id.chk_dm);
				$("#"+$.id.select_promo_from ).attr('disabled',val=='on');
				$("#"+$.id.select_promo_to ).attr('disabled',val=='on');
			});

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
			if(!rt){
				console.log(this.message);
			}
			return rt;
		},

		success: function(reportno,sortable){			// （必須）正処理
			var that = this;

			// 検索実行
			var szChanel	= $.getJSONObject(this.jsonString, $.id.radio_d01).value;		// 購入チャネル
			var szSelYy		=	$.getJSONObject(this.jsonString, $.id.select_yy).value;		// 開始年
			var szSelMm		=	$.getJSONObject(this.jsonString, $.id.select_mm).value;		// 開始月
			var szCheDM		=	$.getJSONObject(this.jsonString, $.id.chk_dm).value;			// DM対象
			var szSelPromoFrom=	$.getJSONObject(this.jsonString, $.id.select_promo_from).value;	// プロモーションFROM
			var szSelPromoTo	=	$.getJSONObject(this.jsonString, $.id.select_promo_to).value;		// プロモーションTO
			var szRadSu		=	$.getJSONObject(this.jsonString, $.id.radio_t08).value;		// 表示項目-1:顧客数 2:受注件数

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
					YYYY_YY			:	szSelYy,		// 開始年
					YYYY_MM			:	szSelMm,		// 開始月
					CHECKDM			:	szCheDM,		// DM対象
					PROMO_CD_FROM	:	szSelPromoFrom,	// プロモーションFROM
					PROMO_CD_TO		:	szSelPromoTo,		// プロモーションTO
					RADSU					:	szRadSu,				// 表示項目-1:顧客数 2:受注件数

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
							fColumn.push({field:'F1',	title:titles[0],	width:110,  halign:'center', align:'right',
								styler: function(value,rowData,rowIndex){return 'background:#F3F3F3;';}
							});
							var num = 1;
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
											var coltitle = rowData.F1+rowData.F2+rowData.F3;
											if ( coltitle.indexOf('比') != -1 || coltitle.indexOf('率') != -1) {
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
								columnTop.push({title:titles[filed+0].split("_")[0], colspan:3});
								columnTop.push({title:titles[filed+3].split("_")[0], colspan:3});
								columnTop.push({title:titles[filed+6].split("_")[0], colspan:3});
								columns.push(columnTop);

								// 多段ヘッダー（２段目）
								for (var i=startField; i<titles.length; i++){
									var tit = titles[i].split("_")[1];
									var hed = ""

									columnBottom.push({field:'F'+(i+1), title:tit, width:100, align:'right', halign:'center',
										styler: function(value,rowData,rowIndex){
											var rtnVal="";
											if ( this.title.indexOf('比') != -1 && (rowData.F1.indexOf('率') != -1||rowData.F1.indexOf('単価') != -1)) {
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
			// DM対象
			this.jsonString.push({
				id:		$.id.chk_dm,
				value:	$.getCheckBoxVal($.id.chk_dm),
				text:	$.trim($('#'+$.id.chk_dm).parent('label').text())
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
			// 表示項目-1:顧客数 2:受注件数
			this.jsonString.push({
				id:		$.id.radio_t08,
				value:	$("input[name="+ $.id.radio_t08 + "]:checked").val(),
				text:	$.trim($("label[for='"+$("input:radio[name="+ $.id.radio_t08 + "]:checked").attr("id")+"']").text())
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

		initObj: function (id){
			var that=this;
			// 初期化終了
			if ($.inArray(id, that.initedObject) < 0) that.initedObject.push(id);
			console.log(that.initedObject.length);
			// 初期表示終了時にイベント実行
			if (that.initedObject.length == that.initedObjCount) that.triggerInitEvent();
		},

		setYy: function(reportno, id, id2){	// 基準年
			var that = this;
			var obj1 = $('#'+id );
			var obj2 = $('#'+id2);
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
										$('#'+id).val(data[i]['VALUE']);
									}
								}
								,"json"
							);

						that.initObj(id);
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