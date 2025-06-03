/**
 * jquery report container
 * セグメント別実績
 */

;(function($) {

	$.extend({
		reportOption: {
		name:		'ReportB_02a',				// （必須）レポートオプションの確認
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

			this.setCheck(reportno, $.id.lbl_check);				// レポート更新チェック
			this.setRadBtn(reportno);								// ラジオボタン：照会データ指定
			this.setYm(reportno, $.id.select_ym,$.id.select_ym2);	// 基準年月From-To
			this.setDd(reportno, $.id.select_dd, $.id.select_dd2);	// 基準年月日From-To
			this.setTenpoG(reportno, $.id.select_tenpo_g);			// 店舗
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
			this.message = "";
			/* 期間指定の入力チェック */
			if($("input[name="+ $.id.radio_b1 + "]:checked")[0].id == $.id.radio_b1_2){				// 照会データ指定-2:月別
				var f = $("#"+$.id.select_ym).val();
				var t = $("#"+$.id.select_ym2).val();
				if(this.isFromTo(f, t)){															// 日付大小
					this.message += $.message.ID_MESSAGE_INVARID_FROM_TO + "\n";
					rt = false;
				}
			}else if($("input[name="+ $.id.radio_b1 + "]:checked")[0].id == $.id.radio_b1_3){	// 照会データ指定-3:日別
				var f = $("#"+$.id.select_ym).val()+ $("#"+$.id.select_dd).val();
				var t = $("#"+$.id.select_ym2).val()+ $("#"+$.id.select_dd2).val();
				if(this.isFromTo(f, t)){															// 日付大小
					this.message += $.message.ID_MESSAGE_INVARID_FROM_TO + "\n";
					rt = false;
				}else if($.exDate(f,'yyyymmdd').toChar('yyyymmdd')!=f){								// 開始日付妥当性
					this.message += $.message.ID_MESSAGE_INVARID_DATE + "\n";
					rt = false;
				}else if($.exDate(t,'yyyymmdd').toChar('yyyymmdd')!=t){								// 終了日付妥当性
					this.message += $.message.ID_MESSAGE_INVARID_DATE + "\n";
					rt = false;
				}
			}
			/* 店舗の入力チェック */
			if(!$('#'+ $.id.chk_tenpo_all).is(":checked")){											// 各店舗利用
				var tenposb = $.getJSONObject(this.jsonString, $.id.select_tenpo_g).value.split(",");
				// 店舗数確認
				if(tenposb == ""){
					this.message += $.message.ID_MESSAGE_SEL_TENPO2 + "\n";
					rt = false;
				} else if (tenposb.length > 25) {
					this.message = $.message.ID_MESSAGE_SEL_TENPO;
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
			var szRadB1		=	$.getJSONObject(this.jsonString, $.id.radio_b1).value;			// 照会データ指定-1:日別 2:月別
			var szSelYm		=	$.getJSONObject(this.jsonString, $.id.select_ym).value;			// 基準年月
			var szSelYm2	=	$.getJSONObject(this.jsonString, $.id.select_ym2).value;		// 基準年月
			var szSelDd		=	$.getJSONObject(this.jsonString, $.id.select_dd).value;			// 基準日
			var szSelDd2	=	$.getJSONObject(this.jsonString, $.id.select_dd2).value;		// 基準日
			var szSelTenpoG	=	$.getJSONObject(this.jsonString, $.id.select_tenpo_g).value;	// 店舗
			var szSelType	=	$.getJSONObject(this.jsonString, $.id.select_htype1).value;		// 表示項目-1:客数 2:純売上 2:出荷売上 4:調整前純売上

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
					RAD_B1			:	szRadB1,				// 照会データ指定-1:日別 2:月別
					YM_KIJUN		:	szSelYm,				// 基準年
					YM_KIJUN2		:	szSelYm2,				// 基準年
					DD_KIJUN		:	szSelDd,				// 開始日
					DD_KIJUN2		:	szSelDd2,				// 終了日
					TENPOG			:	szSelTenpoG,			// 店舗
					H_TYPE			:	szSelType,				// 表示項目-1:客数 2:純売上 2:出荷売上 4:調整前純売上
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
							// 選択した店舗名称を取得
							var tenpos = $.convSelectVal($.getJSONObject(that.jsonString, $.id.select_tenpo_g).text,true);
							var items = new Array('店舗新規','通販新規','店舗リピート','通販リピート','カード無し','未登録','顧客計');

							var columnTop=[];		// 店舗名称
							var columnBottom=[];	// 項目名称（店舗×表示項目）

							// 店舗ヘッダー列作成
							if(tenpos[0]!="全店舗"){
								columnTop.push({title:'店舗計',colspan:items.length});
							}
							for (var index=0; index<tenpos.length; index++){
								columnTop.push({title:tenpos[index],colspan:items.length});
							}
							// 項目ヘッダー列作成
							var filed=4;								// 項目開始位置
							var num = json.rows.length - filed + 1;		// 項目列数
							for (var i=0; i<num; i++){
								var tit = items[i%items.length];
								if($.inArray(tit, ['店舗リピート','通販リピート','顧客計'])!=-1){
									columnBottom.push({field:'F'+filed, title:tit, width:100, resizable:true, align:'right',
										formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0');}});
								}else{
									columnBottom.push({field:'F'+filed, title:tit, width: 80, resizable:true, align:'right',
										formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0');}});
								}
								filed++;
							}

							// datagrid 形式にタイトル情報の加工
							var columns = [];
							columns.push(columnTop);
							columns.push(columnBottom);
							// datagrid のタイトル再設定
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
			// ラジオボタン-----------------------------------------------------
			this.jsonString.push({
				id:		$.id.radio_b1,
				value:	$("input[name="+ $.id.radio_b1 + "]:checked").val(),
				text:	$.trim($("label[for='"+$("input:radio[name="+ $.id.radio_b1 + "]:checked").attr("id")+"']").text())
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
			this.jsonString.push({
				id:		$.id.select_dd,
				value:	$('#'+$.id.select_dd).val(),
				text:	$('#'+$.id.select_dd+' option:selected').text()
			});
			this.jsonString.push({
				id:		$.id.select_dd2,
				value:	$('#'+$.id.select_dd2).val(),
				text:	$('#'+$.id.select_dd2+' option:selected').text()
			});
			// 店舗情報
			if($('#'+$.id.chk_tenpo_all).is(":checked")){
				this.jsonString.push({
					id:		$.id.select_tenpo_g,
					value:	$('#'+$.id.chk_tenpo_all).val(),
					text:	$.trim($("label[for='"+$.id.chk_tenpo_all+"']").text())
				});
			}else{
				this.jsonString.push({
					id:		$.id.select_tenpo_g,
					value:	$("input[name="+ $.id.select_tenpo_g + "]:checked").map(function(){return $(this).val();}).get().join(","),
					text:	$("input[name="+ $.id.select_tenpo_g + "]:checked").map(function(){return $(this).parent('label').text();}).get().join(",")
				});
			}
			this.jsonString.push({
				id:		$.id.select_htype1,
				value:	$('#'+$.id.select_htype1).val(),
				text:	$('#'+$.id.select_htype1+' option:selected').text()
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
			$.log(that.timeData, 'triggerInitEvent:');
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
		setRadBtn: function(reportno){		// ラジオボタン：照会データ指定/対比表示/表示対象
			// イベント設定
			// 可不可制御処理追加
			$('input[name="' + $.id.radio_b1 + '"]').change(function(){
				var val =$(this).attr("id");
				$("#"+$.id.select_dd ).attr('disabled',val!=$.id.radio_b1_3);
				$("#"+$.id.select_dd2).attr('disabled',val!=$.id.radio_b1_3);
			});
		},
		setYm: function(reportno, id, id2){	// 基準年月
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
		},
		setDd: function(reportno, id, id2){	// 基準年月
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
							item.push('<option value="'+ data[i]['VALUE'] + '">' + data[i]['TEXT'] + '日</option>');
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
		setTenpoG: function(reportno, id){	//
			// 可不可制御処理追加
			$('#'+$.id.chk_tenpo_all).change(function(){
				$("#"+id).attr('disabled',$(this).is(":checked"));
			}).change();
			$.setChkBoxWindowL(reportno, id, "tenpo", "店舗", this, null);
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
				autoRowHeight:false,
				frozenColumns:[[
					{field:'F1',title:'SORT',hidden:true},
					{field:'F2',title:'受注年月', width:105,align:'left',
						styler:function(){return 'background:#b0c4de;font-weight: bold;width:105px;';}},
					{field:'F3',title:'項目', width: 90,align:'left',
						styler:function(){return 'background:#b0c4de;font-weight: bold;width: 90px';}
					}
				]],
				columns:[[{title:'全店舗',colspan:7}],[
					{field:'F4',	title:'店舗新規',	width:80, align:'right' ,
						formatter:function(value,rowData,rowIndex){ return $.getFormat(value,'#,##0');}
					},
					{field:'F5',	title:'通販新規',	width:80, align:'right'	,
						formatter:function(value,rowData,rowIndex){ return $.getFormat(value,'#,##0');}
					},
					{field:'F6',	title:'店舗リピート',	width:100, align:'right',
						formatter:function(value,rowData,rowIndex){ return $.getFormat(value,'#,##0');}
					},
					{field:'F7',	title:'通販リピート',	width:100, align:'right',
						formatter:function(value,rowData,rowIndex){ return $.getFormat(value,'#,##0');}
					},
					{field:'F8',	title:'カード無し',	width:80, align:'right'	,
						formatter:function(value,rowData,rowIndex){ return $.getFormat(value,'#,##0');}
					},
					{field:'F9',	title:'未登録',	width:80, align:'right',
						formatter:function(value,rowData,rowIndex){ return $.getFormat(value,'#,##0');}
					},
					{field:'F10',	title:'顧客計',	width:100, align:'right',
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