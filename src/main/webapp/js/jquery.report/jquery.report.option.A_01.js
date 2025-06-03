/**
 * jquery report container
 * セグメント別実績
 */

;(function($) {

	$.extend({
		reportOption: {
		name:		'ReportA_01',				// （必須）レポートオプションの確認
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
		initedObjCount: 9,
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
			this.setYm(reportno, $.id.select_ym);
			// 表示形式指定
			this.setType(reportno, $.id.select_type1_2,$.id.select_juchu_from, $.id.select_juchu_to);
			// 初回受注月
			this.setJucYm(reportno,$.id.select_juchu_from, $.id.select_juchu_to);
			// 回数
			this.setKaisu(reportno,$.id.select_kaisu);
			// 受注形態
			this.setJuckbn(reportno,$.id.select_juc_kbn);
			// 年齢
			this.setAge(reportno,$.id.select_age);
			// 地域
			this.setArea(reportno,$.id.select_area);
			// 媒体
			this.setBytai(reportno,$.id.select_s_bytai);
			// 初回商品
			this.setShog(reportno,$.id.select_s_shog);

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
			//var rt = !this.isFromTo($("#"+$.id.select_promo_from).val(), $("#"+$.id.select_promo_to).val());
			//if (!rt) {
				// エラーメッセージ設定
			//	this.message = $.message.ID_MESSAGE_INVARID_FROM_TO_PROMO;
			//	alert(this.message);
			//}
			if($("#"+$.id.select_ym).val()<='200800'){
				alert("2005年-2007年は仕様が違っています。詳しくはデータの説明を参照してください。");
			}
			return rt;
		},
		success: function(reportno,sortable){			// （必須）正処理
			var that = this;
			// 検索実行
			var szSelYm 		=	$.getJSONObject(this.jsonString, $.id.select_ym).value;			// （必須）基準年月
			var szSelType1		=	$.getJSONObject(this.jsonString, $.id.select_type1_2).value;		// （必須）
			var szSelType2		=	$.getJSONObject(this.jsonString, $.id.select_type2).value;		// （必須）
			var szSelhType3		=	$.getJSONObject(this.jsonString, $.id.select_htype3).value;		// （必須）

			var szSelJucYm1	=	$.getJSONObject(this.jsonString, $.id.select_juchu_from).value;	//
			var szSelJucYm2	=	$.getJSONObject(this.jsonString, $.id.select_juchu_to).value;	//
			var szSelKaisu	=	$.getJSONObject(this.jsonString, $.id.select_kaisu).value;	//
			var szSelJuckbn	=	$.getJSONObject(this.jsonString, $.id.select_juc_kbn).value;	//
			var szSelAge	=	$.getJSONObject(this.jsonString, $.id.select_age).value;	//
			var szSelArea	=	$.getJSONObject(this.jsonString, $.id.select_area).value;	//
			var szSelBytei	=	$.getJSONObject(this.jsonString, $.id.select_s_bytai).value;	//
			var szSelShoG	=	$.getJSONObject(this.jsonString, $.id.select_s_shog).value;	//

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
					report			:	that.name,		// レポート名
					YYYY_MM			:	szSelYm,		// 基準年月
					TYPE1			:	szSelType1,	//
					TYPE2			:	szSelType2,	//
					TYPE3			:	szSelhType3,	//
					JUCYM1			:	szSelJucYm1,	//
					JUCYM2			:	szSelJucYm2,	//
					KAISU			:	szSelKaisu,		//
					JUCKBN			:	szSelJuckbn,	//
					AGE				:	szSelAge,		//
					AREA			:	szSelArea,		//
					BYTAI			:	szSelBytei,		//
					SHOG			:	szSelShoG,		//
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
						var w = $("#"+$.id.select_htype3).val()*1>=2?100:80;
						if(json!=undefined && json.rows != undefined && json.opts != undefined){
							var column=[];
							var filed = 3;
							// 数値型列作成
							var num = json.rows.length - filed + 1;
							for (var index=0; index<num; index++){
								var tit = json.rows[filed-1];
									column.push({field:'F'+filed, title:tit, width:w, resizable:true, align:'right',
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
				id:		$.id.select_ym,
				value:	$('#'+$.id.select_ym).val(),
				text:	$('#'+$.id.select_ym+' option:selected').text()
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
			this.jsonString.push({
				id:		$.id.select_htype3,
				value:	$('#'+$.id.select_htype3).val(),
				text:	$('#'+$.id.select_htype3+' option:selected').text()
			});
			// 初回受注月TO
			this.jsonString.push({
				id:		$.id.select_juchu_to,
				value:	$('#'+$.id.select_juchu_to).val(),
				text:	$('#'+$.id.select_juchu_to+' option:selected').text()
			});
			//初回受注月FROM
			this.jsonString.push({
				id:		$.id.select_juchu_from,
				value:	$('#'+$.id.select_juchu_from).val(),
				text:	$('#'+$.id.select_juchu_from+' option:selected').text()
			});
			// 回数
			this.jsonString.push({
				id:		$.id.select_kaisu,
				value:	$('#'+$.id.select_kaisu).val(),
				text:	$('#'+$.id.select_kaisu+' option:selected').text()
			});
			this.jsonString.push({
				id:		$.id.select_juc_kbn,
				value:	$('#'+$.id.select_juc_kbn).val(),
				text:	$('#'+$.id.select_juc_kbn+' option:selected').text()
			});
			this.jsonString.push({
				id:		$.id.select_age,
				value:	$('#'+$.id.select_age).val(),
				text:	$('#'+$.id.select_age+' option:selected').text()
			});
			this.jsonString.push({
				id:		$.id.select_area,
				value:	$('#'+$.id.select_area).val(),
				text:	$('#'+$.id.select_area+' option:selected').text()
			});
			this.jsonString.push({
				id:		$.id.select_s_bytai,
				value:	$('#'+$.id.select_s_bytai).val(),
				text:	$('#'+$.id.select_s_bytai+' option:selected').text()
			});
			this.jsonString.push({
				id:		$.id.select_s_shog,
				value:	$('#'+$.id.select_s_shog).val(),
				text:	$('#'+$.id.select_s_shog+' option:selected').text()
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
		setYm: function(reportno, id){	// 基準年月
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
		setType: function(reportno, id, id2, id3){	//
			var that = this;
			var obj1 = $('#'+id );
			var obj2 = $('#'+id2);
			var obj3 = $('#'+id3);
			var selval = 0;
			$('#'+id).change(function(){
				if($.inArray(obj1.val(), ['4','5','6','7','8','9','10'])!=-1){
					selval = 1;
				}else{
					selval = 0;
				}
				// 初期化
				obj2.empty();
				obj3.empty();
				$.post(
					$.reg.easy ,
					{
						"page"		:	reportno,
						"obj"		:	id2,
						"sel"		:	selval,
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
						obj2.append(item.join(''));
						obj3.append(item.join(''));
						// IE6 selectedプロパティエラー,幅調整不具合回避
						obj2.width();
						obj3.width();
					},
					"json"
				);
			}
			);
		},
		setJucYm: function(reportno,id,id2){ //初回受注月
			var that = this;
			var obj1 = $('#'+id );
			var obj2 = $('#'+id2);
			$.post(
					$.reg.easy ,
					{
						"page"		:	reportno,
						"obj"		:	id,
						"sel"		:	0,
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
		setKaisu: function(reportno, id){	// 回数
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
		setJuckbn: function(reportno, id){	// 受注形態
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
		setBytai: function(reportno, id){	// 媒体
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
		setShog: function(reportno, id){	// 初回商品
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
		setGrid: function (id, reportNumber){	// グリッドの構築
		 	var that = this;
			$(id).datagrid({
				nowrap: true,
				border: false,
				striped: true,
				collapsible:false,
				remoteSort: true,
				idField:'F1',
				pageSize:200,
				fitColumns:false,	// 指定カラム幅を適用する場合、false 指定。
				frozenColumns:[[
							{field:'F1', title:'SORT',width:10,sortable:false,hidden:true},
							{field:'F2', title:'',width:150,sortable:false,
								styler:function(){return 'background:#b0c4de;font-weight: bold;width:150px;';}
							}
						]],
				columns:[[
					{field:'F3', title:' ',width:80,resizable:true, align:'right',formatter:function(value,rowData,rowIndex){ return $.getFormat(value,'#,##0');}},
					{field:'F4', title:' ',width:80,resizable:true, align:'right',formatter:function(value,rowData,rowIndex){ return $.getFormat(value,'#,##0');}}
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
		}
	} });
})(jQuery);