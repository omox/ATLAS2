/**
 * jquery report container
 * 業績レポート
 */

;(function($) {

	$.extend({
		reportOption: {
		name:		'Gyoseki_Report',				// （必須）レポートオプションの確認
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
			that.jsonHidden = $.getTargetValue();
			that.defaultValues = that.getDefaultValue(reportno);

			that.setCheck(reportno, $.id.lbl_check);	// レポート更新チェック
			that.setYy(reportno, $.id.select_yy);		// 基準年
			that.setMm(reportno, $.id.select_mm);		// 基準月

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
			var szJiseki 		=	this.button;			//純売上・出荷売上の区別
			var szSelYY 		=	$.getJSONObject(this.jsonString, $.id.select_yy).value;			// （必須）基準年月
			var szSelMM 		=	$.getJSONObject(this.jsonString, $.id.select_mm).value;			// （必須）基準年月

			// 処理時間計測用
			that.timeData = (new Date()).getTime();

			// grid.options 取得
			var options = $($.id.gridholder).datagrid('options');
			if (sortable) sortable=1; else sortable=0;
			that.sortName	= options.sortName;
			that.sortOrder	= options.sortOrder;

			$.getJSON(
				$.reg.jqgrid ,
				{
					report			:	that.name,				// レポート名
					YYYY_YY			:	szSelYY,				// 基準年月
					YYYY_MM			:	szSelMM,			// 販売チャネル
					JISEKI			:	szJiseki,			//純売上・出荷売上の区別
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

					// 通常検索時に並び替え情報のリセット
					if (sortable===0){
						var options = $($.id.gridholder).datagrid('options');
						options.sortName = null;
						options.sortOrder = null;

						// Colomns再設定(不要の場合は除去) ※DataGrid用
						if(json.rows != undefined){
							for (var i=0; i< json.rows.length; i++){
								$("TH[field=F" + (i+1)+"]").innerText = json.rows[i];
								$(".datagrid-header-inner td span:first-child")[i].innerText = json.rows[i];
							}
						}
					}
					// Load処理回避
					$.tryChangeURL($.reg.jqeasy);

					// グリッド再描画
					$($.id.gridholder).datagrid('load', {} );
					$($.id.gridholder).datagrid('loaded');
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
			// 検索実行
			$('#'+$.id.select_yy).change();
			// リサイズ
			that.setResize();
		},
		getDefaultValue: function (reportno){
			var dt = null;
			$.ajax({
				async: false,
				type: "POST",
				url : $.reg.easy,
				data: {
					"page"		:	reportno,
					"obj"		:	$.id.select_ym,
					"sel"		:	(new Date()).getTime(),
					"target"	:	$.id.target_default,
					"action"	:	$.id.action_init,
					"rowsOnly"	:	true
				},
				dataType : "json",
				success: function(data){
					if(data.length>0){
						dt = data[0]['MAX'];
					}
				}
			});
			// 初期化
			var json = [];
			$.setJSONObject(json,$.id.select_yy,dt.substr(0, 4));		// 開始年
			$.setJSONObject(json,$.id.select_mm,dt.substr(4));			// 開始月
			return json;
		},
		setCheck: function(reportno, id){
			// 情報設定
			$.post(
					$.reg.easy ,
					{
						"page"		:	"ReportB_01a",
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
								$('#'+$.id.select_yy).attr("disabled",true);
								$('#'+$.id.select_mm).attr("disabled",true);
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
			obj1.on("change", $.pushSearch);
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
		setMm: function(reportno, id){	// 基準年月
			var that = this;
			var obj1 = $('#'+id );
			obj1.on("change", $.pushSearch);
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
				border: true,
				striped: true,
				collapsible:false,
				remoteSort: true,
				idField:'F1',
				pageSize:20,
				fitColumns:false,	// 指定カラム幅を適用する場合、false 指定。
				frozenColumns:[[
								{field:'F1', title:'SORT',width:10,hidden:true}
							]],
				columns:[[
					{field:'F2', title:'項目',width:120,resizable:true },
					{field:'F3', title:'予算',width:100,resizable:true, align:'right',
						formatter:function(value,rowData,rowIndex){ return $.getFormat(value,'#,##0');}
					},
					{field:'F4', title:'純売上',width:100,resizable:true, align:'right',
						formatter:function(value,rowData,rowIndex){ return $.getFormat(value,'#,##0');}
					},
					{field:'F5', title:'前年比',width:85,resizable:true, align:'right',
						formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0.0');}
					},
					{field:'F6', title:'達成率',width:85,resizable:true, align:'right',
						formatter:function(value,rowData,rowIndex){ return $.getFormat(value,'#,##0.0');}
					},
					{field:'F7', title:'年間累計予算',width:120,resizable:true, align:'right',
						styler   :function(value,rowData,rowIndex){return 'background:#ccffcc';},
						formatter:function(value,rowData,rowIndex){ return $.getFormat(value,'#,##0');}
					},
					{field:'F8', title:'年間累計純売上',width:120,resizable:true, align:'right',
						styler   :function(value,rowData,rowIndex){return 'background:#ccffcc';},
						formatter:function(value,rowData,rowIndex){ return $.getFormat(value,'#,##0');}
					},
					{field:'F9', title:'累計前年比',width:95,resizable:true, align:'right',
						styler   :function(value,rowData,rowIndex){return 'background:#ccffcc';},
						formatter:function(value,rowData,rowIndex){ return $.getFormat(value,'#,##0.0');}
					},
					{field:'F10', title:'累計達成率',width:95,resizable:true, align:'right',
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