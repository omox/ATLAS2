/**
 * jquery report container
 * 通販受注実績_時間別
 */

;(function($) {

	$.extend({
		reportOption: {
		name:		'ReportB_031',				// （必須）レポートオプションの確認
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
			this.defaultValues = this.getDefaultValue(reportno);
			// データ表示エリア初期化
			that.setGrid($.id.gridholder, reportno);
			this.setCheck(reportno, $.id.lbl_check);			// レポート更新チェック
			// 基準年月
			this.setYm(reportno, $.id.select_ym);
			this.setDd(reportno, $.id.select_dd);

			// 初期化終了
			this.initializes=!this.initializes;
			// ログ出力
			$.log(that.timeData, 'init initialize:');
		},
		validation: function (){				// （必須）批准
			// 入力チェック
			var rt = true;
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
			var szSelYm 	=	$.getJSONObject(this.jsonString, $.id.select_ym).value;	// （必須）基準年月
			var szSelDd 	=	$.getJSONObject(this.jsonString, $.id.select_dd).value;	// （必須）基準年月
			// 受注経路
			var szCheck1	=	$.getJSONObject(this.jsonString, $.id.check_kei1).value;// 電話
			var szCheck2	=	$.getJSONObject(this.jsonString, $.id.check_kei2).value;// IVR
			var szCheck3	=	$.getJSONObject(this.jsonString, $.id.check_kei3).value;// FAX･封書･ハガキ
			var szCheck4	=	$.getJSONObject(this.jsonString, $.id.check_kei4).value;// スマホ
			var szCheck5	=	$.getJSONObject(this.jsonString, $.id.check_kei5).value;// アプリ
			var szCheck6	=	$.getJSONObject(this.jsonString, $.id.check_kei6).value;// PC
			var szCheck7	=	$.getJSONObject(this.jsonString, $.id.check_kei7).value;// モバイル
			var szCheck8	=	$.getJSONObject(this.jsonString, $.id.check_kei8).value;// 定期販売

			var szLabel1	=	$.getJSONObject(this.jsonString, $.id.check_kei1).text;	// 電話
			var szLabel2	=	$.getJSONObject(this.jsonString, $.id.check_kei2).text;	// IVR
			var szLabel3	=	$.getJSONObject(this.jsonString, $.id.check_kei3).text;	// FAX･封書･ハガキ
			var szLabel4	=	$.getJSONObject(this.jsonString, $.id.check_kei4).text;	// スマホ
			var szLabel5	=	$.getJSONObject(this.jsonString, $.id.check_kei5).text;	// アプリ
			var szLabel6	=	$.getJSONObject(this.jsonString, $.id.check_kei6).text;	// PC
			var szLabel7	=	$.getJSONObject(this.jsonString, $.id.check_kei7).text;	// モバイル
			var szLabel8	=	$.getJSONObject(this.jsonString, $.id.check_kei8).text;	// 定期販売

			var szKeiro 		=	this.button;			//経路別照会の区別

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
					report			:	that.name,			// レポート名
					YYYY_YM			:	szSelYm,			// 基準年月
					YYYY_DD			:	szSelDd,			// 基準年月
					CHK_1 			:	szCheck1,			// 入力経路
					CHK_2 			:	szCheck2,			// 入力経路
					CHK_3 			:	szCheck3,			// 入力経路
					CHK_4 			:	szCheck4,			// 入力経路
					CHK_5 			:	szCheck5,			// 入力経路
					CHK_6 			:	szCheck6,			// 入力経路
					CHK_7 			:	szCheck7,			// 入力経路
					CHK_8 			:	szCheck8,			// 入力経路
					LBL_1 			:	szLabel1,			// 入力経路
					LBL_2 			:	szLabel2,			// 入力経路
					LBL_3 			:	szLabel3,			// 入力経路
					LBL_4 			:	szLabel4,			// 入力経路
					LBL_5 			:	szLabel5,			// 入力経路
					LBL_6 			:	szLabel6,			// 入力経路
					LBL_7 			:	szLabel7,			// 入力経路
					LBL_8 			:	szLabel8,			// 入力経路
					KEIRO				:	szKeiro,			//経路別照会の区別
					t						:	(new Date()).getTime(),
					sortable		:	sortable,
					sortName		:	that.sortName,
					sortOrder		:	that.sortOrder,
					records			:	0						// 総レコード数（初期化）
				},
				function(json){
					// ログ出力
					$.log(that.timeData, 'query:');
					// Load処理回避
					$.tryChangeURL(null);

					// 初期検索時に並び替え情報のリセット
					if (sortable===0){
						var options = $($.id.gridholder).datagrid('options');
						options.sortName = null;
						options.sortOrder = null;
					}

					// Load処理回避
					$.tryChangeURL($.reg.jqeasy);
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
			this.jsonString.push({
				id:		$.id.select_dd,
				value:	$('#'+$.id.select_dd).val(),
				text:	$('#'+$.id.select_dd+' option:selected').text()
			});
			// 区分
			this.jsonString.push({
				id:		$.id.radio_category,
				value:	$("input[name="+ $.id.radio_category + "]:checked").val(),
				text:	$("label[for='"+$("input:radio[name="+ $.id.radio_category + "]:checked").attr("id")+"']").text()
			});
			// 入力経路
			this.jsonString.push({
				id:		$.id.check_kei1,
				value:	$.getCheckBoxVal($.id.check_kei1),
				text:	$.trim($('#'+$.id.check_kei1).parent('label').text())
			});
			this.jsonString.push({
				id:		$.id.check_kei2,
				value:	$.getCheckBoxVal($.id.check_kei2),
				text:	$.trim($('#'+$.id.check_kei2).parent('label').text())
			});
			this.jsonString.push({
				id:		$.id.check_kei3,
				value:	$.getCheckBoxVal($.id.check_kei3),
				text:	$.trim($('#'+$.id.check_kei3).parent('label').text())
			});
			this.jsonString.push({
				id:		$.id.check_kei4,
				value:	$.getCheckBoxVal($.id.check_kei4),
				text:	$.trim($('#'+$.id.check_kei4).parent('label').text())
			});
			this.jsonString.push({
				id:		$.id.check_kei5,
				value:	$.getCheckBoxVal($.id.check_kei5),
				text:	$.trim($('#'+$.id.check_kei5).parent('label').text())
			});
			this.jsonString.push({
				id:		$.id.check_kei6,
				value:	$.getCheckBoxVal($.id.check_kei6),
				text:	$.trim($('#'+$.id.check_kei6).parent('label').text())
			});
			this.jsonString.push({
				id:		$.id.check_kei7,
				value:	$.getCheckBoxVal($.id.check_kei7),
				text:	$.trim($('#'+$.id.check_kei7).parent('label').text())
			});
			this.jsonString.push({
				id:		$.id.check_kei8,
				value:$.getCheckBoxVal($.id.check_kei8),
				text:	$.trim($('#'+$.id.check_kei8).parent('label').text())
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
			if(that.jsonHidden==undefined||that.jsonHidden.length <= 2){	// MAX/MIN
				that.setDefaultValue(reportno);
			}
			$($.id.button_search).attr("disabled",false);
			$($.id.button_search2).attr("disabled",false);
			$($.id.button_excel).attr("disabled",false);

			// リサイズ
			that.setResize();
			// 初期表示処理完了
			that.initedObject.push("triggerInitEvent");
		},
		getDefaultValue: function (reportno){
			var dt = $.getJSONValue(this.jsonHidden, "MAX");
			if( typeof dt === 'undefined')	return ;

			// 初期化
			var json = [];
			$.setJSONObject(json,$.id.select_ym,dt.substr(0, 6));		// 開始年
			$.setJSONObject(json,$.id.select_dd,dt.substr(6, 2));		// 開始月
			return json;
		},
		setDefaultValue: function (reportno){
			// コンボボックス
			$("#"+$.id.select_ym).val($.getJSONValue(this.defaultValues, $.id.select_ym));						// 開始年
			$("#"+$.id.select_dd).val($.getJSONValue(this.defaultValues, $.id.select_dd));						// 開始月
			// チェックボックス
			$("input[name='"+ $.id.check_kei +"']").attr('checked',true);		// 入力経路
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
		setDd: function(reportno, id){	// 基準年月
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
					{field:'F1', title:'SORT', width:10,hidden:true},
					{field:'F2', title:'受注日　時間 ', width:100,resizable:true, align:'right',
						styler:function(value,rowData,rowIndex){
							if(value.indexOf("電話")!=-1)								{return 'background:white;color: black;';}
							if(value.indexOf("IVR")!=-1)								{return 'background:white;color: black;';}
							if(value.indexOf("FAX･封書･ハガキ")!=-1)		{return 'background:white;color: black;';}
							if(value.indexOf("スマホ")!=-1)							{return 'background:white;color: black;';}
							if(value.indexOf("アプリ")!=-1)							{return 'background:white;color: black;';}
							if(value.indexOf("PC")!=-1)									{return 'background:white;color: black;';}
							if(value.indexOf("モバイル")!=-1)						{return 'background:white;color: black;';}
							if(value.indexOf("定期販売")!=-1)						{return 'background:white;color: black;';}
							if(value.indexOf("店舗")!=-1)								{return 'background:white;color: black;';}
							if(value.indexOf("日")!=-1)									{return 'background:#F3F3F3;color: red';}
							return 'background:#F3F3F3;';
						}
					}
				]],
				columns:[[
						{title:'合計',colspan:4},
						{title:'オルビス新規',colspan:4},
						{title:'その他',colspan:4}
					],[
					{field:'F3', title:'受注数', width:55,resizable:true, align:'right',
						formatter:function(value,rowData,rowIndex){ return $.getFormat(value,'#,##0');}
					},
					{field:'F4', title:'累計', width:75,resizable:true, align:'right',
						formatter:function(value,rowData,rowIndex){ return $.getFormat(value,'#,##0');}
					},
					{field:'F5', title:'受注額', width:90,resizable:true, align:'right',
						formatter:function(value,rowData,rowIndex){ return $.getFormat(value,'#,##0');}
					},
					{field:'F6', title:'受注累計額', width:100,resizable:true, align:'right',
						formatter:function(value,rowData,rowIndex){ return $.getFormat(value,'#,##0');}
					},
					{field:'F7', title:'受注数', width:55,resizable:true, align:'right',
						formatter:function(value,rowData,rowIndex){ return $.getFormat(value,'#,##0');}
					},
					{field:'F8', title:'累計', width:75,resizable:true, align:'right',
						formatter:function(value,rowData,rowIndex){ return $.getFormat(value,'#,##0');}
					},
					{field:'F9', title:'受注額', width:85,resizable:true, align:'right',
						formatter:function(value,rowData,rowIndex){ return $.getFormat(value,'#,##0');}
					},
					{field:'F10', title:'受注累計額', width:90,resizable:true, align:'right',
						formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0');}
					},
					{field:'F11', title:'受注数', width:55,resizable:true, align:'right',
						formatter:function(value,rowData,rowIndex){ return $.getFormat(value,'#,##0');}
					},
					{field:'F12', title:'累計', width:75,resizable:true, align:'right',
						formatter:function(value,rowData,rowIndex){ return $.getFormat(value,'#,##0');}
					},
					{field:'F13',  title:'受注額', width:85,resizable:true, align:'right',
						formatter:function(value,rowData,rowIndex){ return $.getFormat(value,'#,##0');}
					},
					{field:'F14', title:'受注累計額', width:90,resizable:true, align:'right',
						formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0');}
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