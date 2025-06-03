/**
 * jquery report container
 * セグメント別実績
 */

;(function($) {

	$.extend({
		reportOption: {
		name:		'ReportB_03',				// （必須）レポートオプションの確認
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
			// 通販受注実績予想数
			this.setTsu(reportno, $.id.lbl_tsu);
			// 基準年月
			this.setYy(reportno, $.id.select_yy);
			this.setMm(reportno, $.id.select_mm);

			//this.setTsuD(reportno, $.id.lbl_tsu2);

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
			var szSelYy 	=	$.getJSONObject(this.jsonString, $.id.select_yy).value;		// （必須）基準年月
			var szSelMm 	=	$.getJSONObject(this.jsonString, $.id.select_mm).value;		// （必須）基準年月
			var szCheck1	=	$.getJSONObject(this.jsonString, $.id.check_kei1).value;	// 入力経路-電話
			var szCheck2	=	$.getJSONObject(this.jsonString, $.id.check_kei2).value;	// 入力経路-バッチ
			var szCheck3	=	$.getJSONObject(this.jsonString, $.id.check_kei3).value;	// 入力経路-OCR
			var szCheck4	=	$.getJSONObject(this.jsonString, $.id.check_kei4).value;	// 入力経路-IVR
			var szCheck5	=	$.getJSONObject(this.jsonString, $.id.check_kei5).value;	// 入力経路-WEB
			var szCheck6	=	$.getJSONObject(this.jsonString, $.id.check_kei6).value;	// 入力経路-モバイル
			var szCheck7	=	$.getJSONObject(this.jsonString, $.id.check_kei7).value;	// 入力経路-定期
			var szKeiro 		=	this.button;			//経路別照会の区別

			// 通販ラベル再設定
			that.setTsu(reportno,  $.id.lbl_tsu);
			// 処理時間計測用
			that.timeData = (new Date()).getTime();			$($.id.gridholder).datagrid('loading');

			// grid.options 取得
			var options = $($.id.gridholder).datagrid('options');
			if (sortable) sortable=1; else sortable=0;
			that.sortName	= options.sortName;
			that.sortOrder	= options.sortOrder;
			$.getJSON(
				$.reg.jqgrid ,
				{
					report			:	that.name,			// レポート名
					YYYY_YY			:	szSelYy,			// 基準年月
					YYYY_MM			:	szSelMm,			// 基準年月
					CHK_1 			:	szCheck1,			// 入力経路
					CHK_2 			:	szCheck2,			// 入力経路
					CHK_3 			:	szCheck3,			// 入力経路
					CHK_4 			:	szCheck4,			// 入力経路
					CHK_5 			:	szCheck5,			// 入力経路
					CHK_6 			:	szCheck6,			// 入力経路
					CHK_7 			:	szCheck7,			// 入力経路
					KEIRO			:	szKeiro,			//経路別照会の区別
					t				:	(new Date()).getTime(),
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

						// Colomns再設定(不要の場合は除去) ※DataGrid用
						//if(json.rows != undefined){
						//	for (var i=0; i< json.rows.length; i++){
						//		$("TH[field=F" + (i+1)+"]").innerText = json.rows[i];
						//		$(".datagrid-header-inner td span:first-child")[i].innerText = json.rows[i];
						//	}
						//}
						// 更新日設定
						if(json.opts[0]["MSG2"]!=undefined){
							$("#"+$.id.lbl_tsu2).val(json.opts[0]["MSG2"]);
						}
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
				id:		$.id.select_yy,
				value:	$('#'+$.id.select_yy).val(),
				text:	$('#'+$.id.select_yy+' option:selected').text()
			});
			this.jsonString.push({
				id:		$.id.select_mm,
				value:	$('#'+$.id.select_mm).val(),
				text:	$('#'+$.id.select_mm+' option:selected').text()
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
			// 通販受注実績予想数
			this.jsonString.push({
				id:		$.id.lbl_tsu,
				value:	$('#'+$.id.lbl_tsu).val(),
				text:	$('#'+$.id.lbl_tsu).text()
			});
			this.jsonString.push({
				id:		$.id.lbl_tsu2,
				value:	$('#'+$.id.lbl_tsu2).val(),
				text:	$('#'+$.id.lbl_tsu2).text()
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
			$('#'+$.id.object_reset).attr("disabled",false);
			// リサイズ
			that.setResize();
			// 初期表示処理完了
			that.initedObject.push("triggerInitEvent");
		},
		getDefaultValue: function (reportno){
			var dt = $.getJSONValue(this.jsonHidden, "MAX");
			// 初期化
			var json = [];
			$.setJSONObject(json,$.id.select_yy,dt.substr(0, 4));		// 開始年
			$.setJSONObject(json,$.id.select_mm,dt.substr(4));			// 開始月
			return json;
		},
		setDefaultValue: function (reportno){
			// コンボボックス
			$("#"+$.id.select_yy).val($.getJSONValue(this.defaultValues, $.id.select_yy));						// 開始年
			$("#"+$.id.select_mm).val($.getJSONValue(this.defaultValues, $.id.select_mm));						// 開始月
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
								$($.id.button_search1).attr("style","visibility: hidden;");
								$($.id.button_search2).attr("style","visibility: hidden;");
								$($.id.button_excel).attr("style","visibility: hidden;");
								$('#'+id).attr("style","color: #ff0000;");
							}
						}
					},
					"json"
			);
		},
		setTsu: function(reportno, id){
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
							$('#'+id).attr("style","background-color: lightcyan;");
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
				pageSize:35,
				fitColumns:false,	// 指定カラム幅を適用する場合、false 指定。
				frozenColumns:[[
					{field:'F1', title:'SORT', width:10,hidden:true},
					{field:'F2', title:'受注日',width:50,align:'center',
						styler:function(value,rowData,rowIndex){
							if(value.indexOf("電話")!=-1){return 'background:white;font-weight: bold;color: black;';}
							if(value.indexOf("バッチ")!=-1){return 'background:white;font-weight: bold;color: black;';}
							if(value.indexOf("OCR")!=-1){return 'background:white;font-weight: bold;color: black;';}
							if(value.indexOf("IVR")!=-1){return 'background:white;font-weight: bold;color: black;';}
							if(value.indexOf("WEB")!=-1){return 'background:white;font-weight: bold;color: black;';}
							if(value.indexOf("モバイル")!=-1){return 'background:white;font-weight: bold;color: black;';}
							if(value.indexOf("定期")!=-1){return 'background:white;font-weight: bold;color: black;';}
							if(value.indexOf("日")!=-1){return 'background:#ffcccc;font-weight: bold;color: red;';}
							return 'background:#b0c4de;font-weight: bold;';}}
							]],
				columns:[[
						{title:'合計',colspan:6},
						{title:'新規',colspan:6},
						{title:'リピート',colspan:6}
					],[
					{field:'F3', title:'受注数', width:65,resizable:true, align:'right',
						formatter:function(value,rowData,rowIndex){ return $.getFormat(value,'#,##0');}
					},
					{field:'F4', title:'累計', width:75,resizable:true, align:'right',
						formatter:function(value,rowData,rowIndex){ return $.getFormat(value,'#,##0');}
					},
					{field:'F5', title:'単価', width:50,resizable:true, align:'right',
						formatter:function(value,rowData,rowIndex){
							if(value ){return '￥'+$.getFormat(value,'#,##0');}
							return $.getFormat(value,'#,##0');}
					},
					{field:'F6', title:'受注額', width:90,resizable:true, align:'right',
						formatter:function(value,rowData,rowIndex){ return $.getFormat(value,'#,##0');}
					},
					{field:'F7', title:'受注累計額', width:100,resizable:true, align:'right',
						formatter:function(value,rowData,rowIndex){ return $.getFormat(value,'#,##0');}
					},
					{field:'F8', title:'前年比', width:55,resizable:true, align:'right',
						formatter:function(value,rowData,rowIndex){ return $.getFormat(value,'#,##0.0');}
					},
					{field:'F9', title:'受注数', width:65,resizable:true, align:'right',
						styler   :function(value,rowData,rowIndex){return 'background:#ccffcc';},
						formatter:function(value,rowData,rowIndex){ return $.getFormat(value,'#,##0');}
					},
					{field:'F10', title:'累計', width:75,resizable:true, align:'right',
						styler   :function(value,rowData,rowIndex){return 'background:#ccffcc';},
						formatter:function(value,rowData,rowIndex){ return $.getFormat(value,'#,##0');}
					},
					{field:'F11', title:'単価', width:50,resizable:true, align:'right',
						styler   :function(value,rowData,rowIndex){return 'background:#ccffcc';},
						formatter:function(value,rowData,rowIndex){
							if(value ){return '￥'+$.getFormat(value,'#,##0');}
							return $.getFormat(value,'#,##0');}
					},
					{field:'F12', title:'受注額', width:85,resizable:true, align:'right',
						styler   :function(value,rowData,rowIndex){return 'background:#ccffcc';},
						formatter:function(value,rowData,rowIndex){ return $.getFormat(value,'#,##0');}
					},
					{field:'F13', title:'受注累計額', width:90,resizable:true, align:'right',
						styler   :function(value,rowData,rowIndex){return 'background:#ccffcc';},
						formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0');}
					},
					{field:'F14', title:'前年比', width:55,resizable:true, align:'right',
						styler   :function(value,rowData,rowIndex){return 'background:#ccffcc';},
						formatter:function(value,rowData,rowIndex){ return $.getFormat(value,'#,##0.0');}
					},
					{field:'F15', title:'受注数', width:65,resizable:true, align:'right',
						formatter:function(value,rowData,rowIndex){ return $.getFormat(value,'#,##0');}
					},
					{field:'F16', title:'累計', width:75,resizable:true, align:'right',
						formatter:function(value,rowData,rowIndex){ return $.getFormat(value,'#,##0');}
					},
					{field:'F17', title:'単価', width:50,resizable:true, align:'right',
						formatter:function(value,rowData,rowIndex){
							if(value ){return '￥'+$.getFormat(value,'#,##0');}
							return $.getFormat(value,'#,##0');}
					},
					{field:'F18',  title:'受注額', width:85,resizable:true, align:'right',
						formatter:function(value,rowData,rowIndex){ return $.getFormat(value,'#,##0');}
					},
					{field:'F19', title:'受注累計額', width:90,resizable:true, align:'right',
						formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0');}
					},
					{field:'F20', title:'前年比', width:55,resizable:true, align:'right',
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