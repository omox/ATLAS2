/**
 * jquery report container
 * セグメント別実績
 */

;(function($) {

	$.extend({
		reportOption: {
		name:		'ReportT_23',				// （必須）レポートオプションの確認
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
			this.lastValues[$.id.select_stage] = $.id.select_stage;
			// 対比表示の最新値
			that.lastValues[$.id.radio_b2] = "3";	// なし
			// 初回検索の最新値
			that.lastValues["success"] = false;	// 検索なし
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
			var szRadB1		=	$.getJSONObject(this.jsonString, $.id.radio_b1).value;		// 照会データ指定-1:年別 2:月別
			var szSelYy1	=	$.getJSONObject(this.jsonString, $.id.select_yy).value;		// 開始年
			var szSelYy2	=	$.getJSONObject(this.jsonString, $.id.select_yy2).value;	// 終了年
			var szSelYm1	=	$.getJSONObject(this.jsonString, $.id.select_ym).value;		// 開始年月
			var szSelYm2	=	$.getJSONObject(this.jsonString, $.id.select_ym2).value;	// 終了年月
			var szSelChanel	=	$.getJSONObject(this.jsonString, $.id.select_chanel).value;	// 販売チャネル
			var szSelKai	=	$.getJSONObject(this.jsonString, this.lastValues[$.id.select_stage]).value;	// ステージ/会員区分
			var szRadT4		=	$.getJSONObject(this.jsonString, $.id.radio_t04).value;		// 顧客状態-2:ACT顧客 3:DM-ACT顧客
			var szRadB2		=	$.getJSONObject(this.jsonString, $.id.radio_b2).value;		// 対比表示-1:前年比 2:前月比 3:なし


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
					report			:	that.name,					// レポート名
					RAD_B1			:	szRadB1,					// 照会データ指定-1:年別 2:月別
					YYYY			:	szSelYy1,					// 開始年
					YYYY2			:	szSelYy2,					// 終了年
					YYYY_MM			:	szSelYm1,					// 開始年月
					YYYY_MM2		:	szSelYm2,					// 終了年月
					HAN_CHAN		:	szSelChanel,				// 販売チャネル
					KAIIN_KBN		:	szSelKai,					// 会員区分
					RAD_T4			:	szRadT4,					// 顧客状態-2:ACT顧客 3:DM-ACT顧客
					RAD_B2			:	szRadB2,					// 対比表示-1:前年比 2:前月比 3:なし
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
						if (that.lastValues[$.id.radio_b2] !== szRadB2 || !that.lastValues["success"]) {
							// 対比表示の最新値
							that.lastValues[$.id.radio_b2] = szRadB2;
							// 初回検索の最新値
							if (!that.lastValues["success"]) that.lastValues["success"] = true;

							// タイトル切替
							var column=[];
							var filed = 3;
							// 数値型列作成
							var num = json.rows.length - filed + 1;
							for (var index=0; index<num; index++){
								var tit = json.rows[filed-1];
								if($.inArray(tit, ['前年比','前月比','前年差','前月差'])!=-1){
									column.push({field:'F'+filed, title:tit, width: 70, resizable:true, align:'right',
										formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0.0');}});
								}else if($.inArray(tit, ['ACT率','DM-ACT率'])!=-1){
									column.push({field:'F'+filed, title:tit, width: 70, resizable:true, align:'right',
										styler   :function(value,rowData,rowIndex){return 'background:#ffff99';},
										formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0.0');}});
								}else if($.inArray(tit, ['F1顧客数','F2以上顧客数'])!=-1){
									column.push({field:'F'+filed, title:tit, width:100, resizable:true, align:'right',
										styler   :function(value,rowData,rowIndex){return 'background:#ccffff';},
										formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0');}});
								}else if(tit == '総顧客数'){
									column.push({field:'F'+filed, title:tit, width:100, resizable:true, align:'right',
										styler   :function(value,rowData,rowIndex){return 'background:#ccffcc';},
										formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0');}});
								}else{
									column.push({field:'F'+filed, title:tit, width:100, resizable:true, align:'right',
										formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0');}});
								}
								filed++;
							}
							var columns = [];
							columns.push(column);
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
			// 会員区分(ステージ/会員区分の最新値)
			this.jsonString.push({
				id:		this.lastValues[$.id.select_stage],
				value:	$('#'+this.lastValues[$.id.select_stage]).val(),
				text:	$('#'+this.lastValues[$.id.select_stage]+' option:selected').text()
			});
			// ラジオボタン-----------------------------------------------------
			this.jsonString.push({
				id:		$.id.radio_b1,
				value:	$("input[name="+ $.id.radio_b1 + "]:checked").val(),
				text:	$.trim($("label[for='"+$("input:radio[name="+ $.id.radio_b1 + "]:checked").attr("id")+"']").text())
			});
			// 顧客状態
			this.jsonString.push({
				id:		$.id.radio_t04,
				value:	$("input[name="+ $.id.radio_t04 + "]:checked").val(),
				text:	$("label[for='"+$("input:radio[name="+ $.id.radio_t04 + "]:checked").attr("id")+"']").text()
			});
			// 対比表示
			this.jsonString.push({
				id:		$.id.radio_b2,
				value:	$("input[name="+ $.id.radio_b2 + "]:checked").val(),
				text:	$("label[for='"+$("input:radio[name="+ $.id.radio_b2 + "]:checked").attr("id")+"']").text()
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
			var obj1 = $('#'+$.id.div_kaiin);
			var obj2 = $('#'+$.id.div_stage);
			var obj3 = $('#'+$.id.div_stage_kaiin);

			var obj4 = $($("input[name="+ $.id.radio_b1 + "]:checked"));
			var val1="",val2="";
			// 年 | 年月取得
			if(obj4[0].id == $.id.radio_b1_1){
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

			var setValue = "";
			if (val1<start && val2<start){
				// ステージ切替（会員区分のみ）前
				setValue=$.id.select_kaiin;
				obj2.hide();//ステージ
				obj3.hide();//ステージ/会員区分
				obj1.show();//会員区分
			} else if (val1>lasts && val2>lasts) {
				// 会員区分参照終了後
				setValue=$.id.select_stage;
				obj3.hide();//ステージ/会員区分
				obj1.hide();//会員区分
				obj2.show();//ステージ
			} else {
				// ステージ／会員区分 参照可能期間
				setValue=$.id.select_stage_kaiin;
				obj1.hide();//会員区分
				obj2.hide();//ステージ
				obj3.show();//ステージ/会員区分
			}
			// ステージ/会員区分の最新値
			this.lastValues[$.id.select_stage] = setValue;
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
					{field:'F2', title:'年月', width:65,resizable:true, align:'center',
						styler:function(){return 'background:#b0c4de;width:65px;';}
					}
				]],
				columns:[[
					{field:'F3', title:' ',width:85,resizable:true, align:'right'}
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
			var szRadB2		=	$.getJSONObject(this.jsonString, $.id.radio_b2).value;		// 対比表示-1:前年比 2:前月比 3:なし

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
					$.outputExcel(reportno, szRadB2);			// EXCEL出力時に区分値を利用する場合、null変更。
				}
			});
		}
	} });
})(jQuery);