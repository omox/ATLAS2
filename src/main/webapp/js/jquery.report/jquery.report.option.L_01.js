/**
 * jquery report container
 * アクセスログ表示
 */

;(function($) {

	$.extend({
		reportOption: {
		name:		'ReportL_01',				// （必須）レポートオプションの確認
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
		initedObjCount: 3,
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
			$('input[name="' + $.id.radio_category_1 + '"]').change(function(){
				var val =$(this).val();
				$("#"+$.id.select_dd).prop('disabled', (val<=2));
			});

			// 基準年月
			this.setYy(reportno, $.id.select_yy);
			this.setMm(reportno, $.id.select_mm);
			this.setDd(reportno, $.id.select_dd);

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
			var szSelYm 		=	$.getJSONObject(this.jsonString, $.id.select_yy).value
											+ $.getJSONObject(this.jsonString, $.id.select_mm).value;			// （必須）基準年月
			var szSelYd 		=	$.getJSONObject(this.jsonString, $.id.select_yy).value
											+ $.getJSONObject(this.jsonString, $.id.select_mm).value
											+ $.getJSONObject(this.jsonString, $.id.select_dd).value;			// （必須）基準年月日
			var szRadCategory1	=	$("input[name="+ $.id.radio_category_1 + "]:checked").val();		//
			var szRadCategory2	=	$("input[name="+ $.id.radio_category_2 + "]:checked").val();		//
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
					YYYY_MM			:	szSelYm,				// 基準年月
					YYYY_DD			:	szSelYd,				// 基準年月
					CATEGORY1		:	szRadCategory1,			// 区分
					CATEGORY2		:	szRadCategory2,			// 区分
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

						// Colomns再設定(不要の場合は除去) ※DataGrid用
						if(json!=undefined && json.rows != undefined && json.opts != undefined){
							var column=[];
							var filed = 2;
							var szRadCategory1	=	$("input[name="+ $.id.radio_category_1 + "]:checked").val();		//
							// 数値型列作成
							var num = json.rows.length - filed + 1;
							for (var index=0; index<num; index++){
								var tit = json.rows[filed-1];
								if($.inArray(tit, ['回数'])!=-1){
									column.push({field:'F'+filed, title:tit, width: 70, resizable:true, align:'right',
										formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0');}});
								}else if(tit == '年月日'){
									if(szRadCategory1==1){
										column.push({field:'F'+filed, title:tit, width:150, resizable:true, align:'left'});
									} else {
										column.push({field:'F'+filed, title:tit, width:80, resizable:true, align:'left'});
									}
								}else if(tit == '名称'){
									column.push({field:'F'+filed, title:tit, width:290, resizable:true, align:'left'});
								}else if(tit == 'ID'){
									column.push({field:'F'+filed, title:tit, width:80, resizable:true, align:'left'});
								}else{
									column.push({field:'F'+filed, title:tit, width:200, resizable:true, align:'left'});
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
				id:		$.id.select_dd,
				value:	$('#'+$.id.select_dd).val(),
				text:	$('#'+$.id.select_dd+' option:selected').text()
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
		setDd: function(reportno, id){	// 基準年月日
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
						obj1.val($.getFirstValue(that,id ,'START_DD_SEL'));

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

		setGrid: function (id, reportNumber){	// グリッドの構築
		 	var that = this;
			$(id).datagrid({
				nowrap: false,
				border: false,
				striped: true,
				collapsible:false,
				remoteSort: true,
				idField:'F1',
				view:scrollview,
				pageSize:200,
				fitColumns:false,	// 指定カラム幅を適用する場合、false 指定。
				frozenColumns:[[
								{field:'F1', title:'SORT', width:70,sortable:true,hidden:true}
							]],
				columns:[[
					{field:'F2', title:'年月日', width:80,sortable:true,
							styler:function(){return 'background:#b0c4de;font-weight: bold;width:80px;';}},
					{field:'F3', title:'', width:100,sortable:true, resizable:true, align:'left'},
					{field:'F4', title:'', width:120,sortable:true, resizable:true, align:'left'},
					{field:'F5', title:'', width:200,sortable:true, resizable:true, align:'left'},
					{field:'F6', title:'', width: 55,sortable:true, resizable:true, align:'right',
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
		}
	} });
})(jQuery);