/**
 * jquery report container
 * RFM顧客数
 */

;(function($) {

	$.extend({
		reportOption: {
		name:		'ReportT_01',				// （必須）レポートオプションの確認
		jsonString:	[],							// （必須）検索条件情報
		jsonHidden: [],							// （必須）親画面からの引き継ぎ情報
		caption: function(){				// （必須）タイトル
			return $('#reportname').val();
		},
		sortName: '',	// ソート項目名
		sortOrder: '',	// ソート順
		timeData : (new Date()).getTime(),
		lastValues: [],
		defaultValues: [],
		initedObject: [],
		initedObjCount: 5,
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
			this.setMm(reportno, $.id.select_mm);

			// 年齢
			this.setAge(reportno,$.id.select_age);
			// 地域
			this.setArea(reportno,$.id.select_area);
			// チャネル
			this.setChanel(reportno,$.id.select_chanel,$.id.select_tenpo);
			// データ表示エリア初期化
			that.setGrid($.id.gridholder, reportno);
			// ステージ/会員区分の初期値
			this.lastValues[$.id.select_stage] = $.id.select_stage;
			// 初期化終了
			this.initializes=!this.initializes;
			// ログ出力
			$.log(that.timeData, 'init initialize:');
		},
		validation: function (){				// （必須）批准
			// 入力チェック
			var rt = true;
			//var rt = !this.isFromTo($("#"+$.id.select_promo_from).val(), $("#"+$.id.select_promo_to).val());
			//if (!rt) {
				// エラーメッセージ設定
			//	this.message = $.message.ID_MESSAGE_INVARID_FROM_TO_PROMO;
			//	alert(this.message);
			//}
			return rt;
		},
		success: function(reportno,sortable){			// （必須）正処理
			var that = this;
			// 検索実行
			var szSelYy 		=	$.getJSONObject(this.jsonString, $.id.select_yy).value;				// （必須）基準年月
			var szSelMm 		=	$.getJSONObject(this.jsonString, $.id.select_mm).value;				// （必須）基準年月
			var szSelType1	=	$.getJSONObject(this.jsonString, $.id.select_type1_2).value;	// （必須）
			var szSelType2	=	$.getJSONObject(this.jsonString, $.id.select_type2).value;		// （必須）
			var szSelKaiin	=	$.getJSONObject(this.jsonString, this.lastValues[$.id.select_stage]).value;	// ステージ/会員区分
			var szSelAge		=	$.getJSONObject(this.jsonString, $.id.select_age).value;			// 年齢
			var szSelArea		=	$.getJSONObject(this.jsonString, $.id.select_area).value;			// 都道府県
			var szSelChanel	=	$.getJSONObject(this.jsonString, $.id.select_chanel).value;		// （必須）販売チャネル
			var szSelTenpo	=	$.getJSONObject(this.jsonString, $.id.select_tenpo).value;		// 店舗選択

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
					report		:	that.name,		// レポート名
					YYYY_MM		:	szSelYy+szSelMm,// 基準年月
					TYPE1			:	szSelType1,		//
					TYPE2			:	szSelType2,		//
					KAIIN			:	szSelKaiin,		//会員区分
					AGE				:	szSelAge,			//
					AREA			:	szSelArea,		//
					HAN_CHAN	:	szSelChanel,	// 販売チャネル
					TENPO			:	szSelTenpo,		// 店舗選択
					t				:	(new Date()).getTime(),
					sortable	:	sortable,
					sortName	:	that.sortName,
					sortOrder	:	that.sortOrder,
					records		:	0						// 総レコード数（初期化）
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
						// 列表示切替
						if(json!=undefined && json.rows != undefined && json.opts != undefined){
							var column=[];
							var filed = 3;
							// 数値型列作成
							var num = json.rows.length - filed + 1;
							for (var index=0; index<num; index++){
								var tit = json.rows[filed-1];
									column.push({field:'F'+filed, title:tit, width:80, resizable:true, align:'right',
										formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0');}});
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
				id:		$.id.select_type1_2,
				value:	$('#'+$.id.select_type1_2).val(),
				text:	$('#'+$.id.select_type1_2+' option:selected').text()
			});
			this.jsonString.push({
				id:		$.id.select_type2,
				value:	$('#'+$.id.select_type2).val(),
				text:	$('#'+$.id.select_type2+' option:selected').text()
			});
			// 会員区分(ステージ/会員区分の最新値)
			this.jsonString.push({
				id:		this.lastValues[$.id.select_stage],
				value:	$('#'+this.lastValues[$.id.select_stage]).val(),
				text:	$('#'+this.lastValues[$.id.select_stage]+' option:selected').text()
			});
			this.jsonString.push({
				id:		$.id.select_age,
				value:	$('#'+$.id.select_age).val(),
				text:	$('#'+$.id.select_age+' option:selected').text()
			});
			this.jsonString.push({
				id:		$.id.select_area,
				value:$("input[name="+ $.id.select_area + "]").length===$("input[name="+ $.id.select_area + "]:checked").length?"00"  :$("input[name="+ $.id.select_area + "]:checked").map(function(){return "'"+$(this).val()+"'";}).get().join(","),
				text:	$("input[name="+ $.id.select_area + "]").length===$("input[name="+ $.id.select_area + "]:checked").length?"全国":$("input[name="+ $.id.select_area + "]:checked").map(function(){return $(this).parent('label').text();}).get().join(",")
			});
			this.jsonString.push({
				id:		$.id.select_chanel,
				value:	$('#'+$.id.select_chanel).val(),
				text:	$('#'+$.id.select_chanel+' option:selected').text()
			});
			this.jsonString.push({
				id:		$.id.select_tenpo,
				value:	$('#'+$.id.select_tenpo).val(),
				text:	$('#'+$.id.select_tenpo+' option:selected').text()
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
			obj1.change(function(){
				if(obj1.val()===""){return;}
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
						// IE6 selected プロパティ エラー回避
						obj1.width();
						// 初期値設定
						obj1.val($.getFirstValue(that,id,null));
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
		setAge: function(reportno, id){	// 年齢
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
			$.setChkBoxWindow(reportno, id, "area", "都道府県", 210, 500, 150, 8, this, 'CD_AREA_DETAIL',true);
			return;
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
		setChanel: function(reportno, id, id2){	//
			var that = this;
			var obj1 = $('#'+id );
			var obj2 = $('#'+id2);
			$('#'+id).change(function(){
				if(obj1.val()=="2"){
					$('#'+$.id.select_tenpo).attr("disabled",false);
					// 初期化
					obj2.empty();
					$.post(
						$.reg.easy ,
						{
							"page"		:	reportno,
							"obj"		:	id2,
							"sel"		:	1,
							"target"	:	id2,
							"action"	:	$.id.action_init,
							"rowsOnly"	:	true
						},
						function(data){
							var item = new Array();
							for(var i=0; i < data.length; i++){
								item.push('<option value="'+ data[i]['VALUE'] + '">' + data[i]['TEXT'] + '</option>');
							}
							obj2.append(item.join(''));
							// IE6 selected プロパティ エラー回避
							obj2.width();
							// 初期値設定
							obj2.val($.getFirstValue(that,id2,null)).change();
							// 初期表示終了時にイベント実行
							if (that.initedObject.length == that.initedObjCount){
								that.triggerInitEvent();
							}
						},
						"json"
					);
				} else {
					obj2.empty();
					$('#'+$.id.select_tenpo).attr("disabled",true);
					// 初期値設定
					obj2.val($.getFirstValue(that,id2,null)).change();
					// 初期表示終了時にイベント実行
					if (that.initedObject.length == that.initedObjCount){
						that.triggerInitEvent();
					}
				}
			}
			).change();
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
				pageSize:200,
				fitColumns:false,	// 指定カラム幅を適用する場合、false 指定。
				frozenColumns:[[
							{field:'F1', title:'SORT',width:70,sortable:true,hidden:true},
							{field:'F2', title:'',		width:110,sortable:false
								,styler:function(value,rowData,rowIndex){	return 'background:#F1F1F1;';}
							}
						]],
				columns:[[
					{field:'F3', title:' ',width:80,sortable:true, resizable:true, align:'right',
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
				pagination:false,
				rowStyler:function(rowIndex,rowData){
					if ($.trim(rowData.F1).indexOf('計')!=-1){
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