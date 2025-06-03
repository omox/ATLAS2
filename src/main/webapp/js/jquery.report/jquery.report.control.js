/**
 * レポート情報の共通部分と固有定義の初期化
 * $.exDate は、exdate.js の読み込みが必須。
 */

/**
 * デバッグ用
 **/
if (!('console' in window)) {
	window.console = {};
	window.console.log = function(str){
		return str;
	};
}

;(function($) {

   $.report = function(options) {

      var defaults = {
		reg : {
			excel	:	"../ExcelGenerate",	// Excel出力用
			jqgrid	:	"../JQGridJSON",	// jqGrid 用
			jqflot	:	"../JQFlotJSON",	// jquery.flot 用
			jqeasy	:	"../JQEasyJSON",	// jquery.easyUI 用
			easy	:	"../EasyToJSON",	// jquery.easyUI 用
			debug 	:	true,				// デバッグ（有効:true, 無効:false)
			search	:	false				// 初期表示時、検索実行（有効:true, 無効:false)
		},
		_ua : (function(){
			// version support
			return {
				ltIE6:typeof window.addEventListener == "undefined" && typeof document.documentElement.style.maxHeight == "undefined",
				ltIE7:typeof window.addEventListener == "undefined" && typeof document.querySelectorAll == "undefined",
				ltIE8:typeof window.addEventListener == "undefined" && typeof document.getElementsByClassName == "undefined",
				ie:document.uniqueID,
				firefox:window.globalStorage,
				opera:window.opera,
				webkit:!document.uniqueID && !window.opera && !window.globalStorage && window.localStorage,
				mobile:/android|iphone|ipad|ipod/i.test(navigator.userAgent.toLowerCase())
			};
		})(),

		log : function(baseTime, comment){
			if ($.reg.debug) {
				var message = comment + ((new Date()).getTime() - baseTime) + ' ms';
				status = message;
				console.log(message);
			}
		},

		/**
		 * 帳票の状態保持（ページサイズ、パネル表示on/off、検索条件）
		 */
		saveState:function(reportno, jsonString, datagrid){
			if (jsonString == null) return false;

			// 検索条件保持
			$.ajax({
				url: $.reg.easy,
				type: 'POST',
				async: false,
				data: {
				"page": reportno,
				"obj": $.id.object_search,
				"target":"state",
				"sel": "json",
				"userid": $('#'+$.id.userid).val(),
				"user": $('#hiddenUser').val(),
				"report": $('#hiddenReport').val(),
				"json": JSON.stringify(jsonString)
				},
				success: function(){
					// マスク削除
					var _1ad=$($.id.toolbar).panel("panel");
					_1ad.children("div.datagrid-mask").remove();
				},
				error: function(){
					// マスク削除
					var _1ad=$($.id.toolbar).panel("panel");
					_1ad.children("div.datagrid-mask").remove();
				}
			});
		},

		getReportNumber : function (reportName) {
			// レポート名からレポート定義配列の位置取得
			for (var i=0; i < this.report.length; i++) {
				if ($.report[i].name === reportName) {
					return i;
				}
			}
		},
		getFormat : function(value,format){
			// 数値->文字フォーマット変換
			if (undefined==value) return '';
			// 数値以外の場合
			if (isNaN(value)||value=='') return value;
			// 形式未指定時の初期値
			if (undefined==format) format='#,###';
			return $.formatNumber(value, {format:format, locale:"jp"});
		},

		getCheckBoxVal: function(id){
			if ($("#"+ id).is(':checked')) {
				return 'on';
			}
			return 'off';
		},
		convSelectVal: function(val,isArray){
			if(val==null || val == undefined){
				return "";
			}
			if(val.indexOf(",")==-1){
				if(isArray==null||isArray== undefined||!isArray){
					return $.trim(val);
				}else{
					return [val];
				}
			}
			return val.split(",");
		},

		getJSONObject	: function (jsonArray, idValue) {
			// JSON 配列から指定されたIDのオブジェクトを戻す
			// 該当がない場合は、該当なし。
			// jsonArray 配列判定
			if(jsonArray) {
				for (var i = 0; i < jsonArray.length; i++) {
					if (jsonArray[i].id === idValue) {
						return json = jsonArray[i];
					}
				}
			}

		},
		setJSONObject	: function (jsonArray, idValue, value, text) {
			// JSON 配列から指定されたIDにデータをセット
			// 該当がない場合は、追加する。
			// jsonArray 配列判定
			var flag = true;
			if(jsonArray) {
				for (var i = 0; i < jsonArray.length; i++) {
					if (jsonArray[i].id === idValue) {
						jsonArray[i].value = value;
						jsonArray[i].text = text;
						flag = false;
						break;
					}
				}
				if (flag) {
					// 存在しない場合に作成
					var json = {
						"id"	:	idValue,
						"value"	:	value,
						"text"	:	text
					};
					jsonArray.push(json);
				}

			}

		},
		getJSONText:function(jsonString, id){
			var json = $.getJSONObject(jsonString, id);
			if(json){
				return json.text;
			}
			return "";
		},
		getJSONValue:function(jsonString, id){
			var json = $.getJSONObject(jsonString, id);
			if(json){
				return json.value;
			}
			return "";
		},

		setRadioInit : function(jsonHidden, id) {
			// Radio 要素の初期化
			// 初期化情報取得
			var json = $.getJSONObject(jsonHidden, id);
			if (json){
				// 初期化
				$('input[name="'+id+'"]').val([json.value]);
			}
		},

		setCheckboxInit : function(jsonHidden, id) {
			// Checkbox 要素の初期化
			// 初期化情報取得
			var json = $.getJSONObject(jsonHidden, id);
			if (json && json.value.length > 0){
				// 初期化
				$('#'+id).attr('checked','checked');
			}
		},

		getTargetValue : function() {
			// 指定要素の初期化
			try{
				var sendParam = $($.id.hiddenSendParam).val();
				if (typeof sendParam === 'string') {
					return JSON.parse(sendParam);
				}
			} catch(e){

			}
		},
		getFirstValue:function(that, id, prefix){
			var json = null;
			var val = null;
			if ($.inArray(id, that.initedObject) < 0){
				that.initedObject.push(id);
				json = $.getJSONObject(that.jsonHidden, prefix);
				if (json == null && that.defaultValues){
					json = $.getJSONObject(that.defaultValues, id);
				}
				if (json){
					val  = json.value;
				}
				// ログ出力
				// $.log(that.timeData, 'getFirstValue:' + id + ' = ' + val + " ");
			}
			if (val === null) {	// IE9の場合、nullでは先頭が選択されない現象の回避
				try {
					val = $('#'+id ).children().first().val();
				} catch(e){
				}
			}
			return val;
		},

		changeColumns: function(method,start,end){
			for (var idx=start; idx<= end; idx++){
				$($.id.gridholder).datagrid(method,'F'+idx);
			}
		},

		tryClickSearch: function(){			// 初期表示時に検索実行
			if ($.reg.search) {
				// 検索ボタン押下
				$($.id.button_search).trigger('click');
			}
			// 検索ボタンにフォーカス
			$($.id.button_search).focus();
		},

		tryShowToolbar: function(id,that){			// （オプション）ツールバーの表示 Event
			// ツールバーが非表示の場合、1度だけ表示＆リサイズ
			// combbox,combgrid 構築時の要素変動を非表示に設定
			var toolbar = $(id);
			if ((toolbar.is(':hidden'))) {
				toolbar.show();
				that.setResize();
			}
		},
		tryChangeURL:function(url){
			// datagrid.url 定義
			// Load処理回避
			var options = $($.id.gridholder).datagrid('options');
			if(options) options.url = url;
		},
		/**
		 * ヘッダーテキストを指定バイト数の位置で改行させる
		 * @param str
		 * @param byte
		 * @returns
		 */
		getNewHeaderText:function(str, byte) {
			var rtn = '';
			var num = 0;
			for (var i = 0; i < str.length; i++) {
				if (num >= byte){
					num = 0;
					rtn += '<br>';
				}
				var c = str.charCodeAt(i);
				// Unicode : 0x0 ～ 0x80, 0xf8f0, 0xff61 ～ 0xff9f, 0xf8f1 ～ 0xf8f3
				if ( (c >= 0x0 && c < 0x81) || (c == 0xf8f0) || (c >= 0xff61 && c < 0xffa0) || (c >= 0xf8f1 && c < 0xf8f4)){
					num += 1;
				} else {
					num += 2;
				}
				rtn += str.substring(i, i+1);
			}
			return rtn;
		},

		/**
		 * Excel出力用タイトル作成
		 * @param {Object} title
		 * @param {Object} columns
		 * @return {Object} rtn
		 */
		outputExcelTitle:function(title, columns){
			var rtn = [];

			// カラム情報の読込
			var colIdx = -2;	// 列番号
			var flag = true;	// 次の列の処理続行フラグ
			var esc = [];		// 行単位で、作業済みのcolIdx+1を保持
			var org = [];		// 行単位で、columns配列indexを保持

			while (flag){
				colIdx++;
				flag = false;

				for (var rowIdx=0; rowIdx<columns.length; rowIdx++){
					if (colIdx == -1){
						// 初期設定
						flag = true;
						esc[rowIdx] = 0;
						org[rowIdx] = 0;
						rtn[rowIdx] = [];

						// title配列が空でない場合、先にrtn配列に入れておく
						if (title.length){
							if (rowIdx < title.length){
								rtn[rowIdx] = title[rowIdx].slice(0);
							} else {
								rtn[rowIdx] = title[title.length-1].slice(0);
							}
						}

					} else if (org[rowIdx] < columns[rowIdx].length && esc[rowIdx] <= colIdx){
						flag = true;

						if (columns[rowIdx][org[rowIdx]].title != null){
							var str = columns[rowIdx][org[rowIdx]].title.replace(/<br>/g, '');
							var colspan = columns[rowIdx][org[rowIdx]].colspan;
							var rowspan = columns[rowIdx][org[rowIdx]].rowspan;
							rtn[rowIdx].push(str);
							esc[rowIdx] += 1;
							org[rowIdx] += 1;

							if (colspan != null && rowspan != null){
								for (var col=1; col<colspan; col++){
									rtn[rowIdx].push(str);
									esc[rowIdx] += 1;
								}
								for (var row=1; row<rowspan && rowIdx+row<columns.length; row++){
									for (var col=0; col<colspan; col++){
										rtn[rowIdx+row].push(str);
										esc[rowIdx+row] += 1;
									}
								}
							} else if (colspan != null){
								for (var col=1; col<colspan; col++){
									rtn[rowIdx].push(str);
									esc[rowIdx] += 1;
								}
							} else if (rowspan != null){
								for (var row=1; row<rowspan && rowIdx+row<columns.length; row++){
									rtn[rowIdx+row].push(str);
									esc[rowIdx+row] += 1;
								}
							}
						}
					}
				}
			}
			return rtn;
		},
		/**
		 * Excel出力用レコード作成
		 * @param {Object} loadData
		 * @param {Object} options
		 * @param {Object} level
		 * @param {Object} frow
		 * @param {Object} crow
		 * @return {Object} data
		 */
		outputExcelRows : function(loadData, options, level, frow, crow){
			var data = [];
			var levelSpace=new Array(level).join('　');
			for ( var row in loadData ) {
				if (row.match(/^[0-9]+$/)) {
					var rowData = [];
					// 固定カラム情報
					for (var column in options.frozenColumns[frow]) {
						if (column.match(/^[0-9]+$/)) {
							if (options.treeField===options.frozenColumns[frow][column].field){
								rowData.push(levelSpace+loadData[row][options.frozenColumns[frow][column].field]);
							} else {
								rowData.push(loadData[row][options.frozenColumns[frow][column].field]);
							}
						}
					}
					// カラム情報
					for (var column in options.columns[crow]) {
						if (column.match(/^[0-9]+$/)) {
							rowData.push(loadData[row][options.columns[crow][column].field]);
						}
					}
					data.push(rowData);
				}
				// 下層情報
				if (loadData[row]['children']!=undefined){
					var rows = $.outputExcelRows(loadData[row]['children'],options, (level+1), frow, crow);
					for (var i=0, l=rows.length; i<l; i++) {
					    data.push(rows[i]);
					}
				}
			}
			return data;
		},

		/**
		 * 検索ボタンイベント
		 * @param {Object} e
		 */
		pushSearch:function(e){
			// レポート番号取得
			var reportno=$($.id.hidden_reportno).val();

			// レポート定義位置
			var reportNumber = $.getReportNumber(reportno);
			if (typeof(reportNumber) !== 'number') { alert("レポート定義が見つかりません。"); return false;}

			// フォーム情報取得
			$.report[reportNumber].getEasyUI();
			// ボタン判定追加	ボタン判定-1:純売上その他 2:出荷売上 3:調整前純売上
			if(e.target.id == $.id.object_search2){
				$.report[reportNumber].button = 2;
			}else if(e.target.id == $.id.object_search3){
				$.report[reportNumber].button = 3;
			}else{
				$.report[reportNumber].button = 1;
			}

			if ($.report[reportNumber].validation()) {
				// 時間外または、メンテナンスの確認
				var nonreload = true;
				$.post(
					$.reg.easy ,
					{
						"page" : reportno ,
						"action" : "get" ,
						"sel"    : (new Date()).getTime() ,
						"json"	 : "" ,
						"obj"    : $.id.object_search,
						"userid" : $('#'+$.id.userid).val()
					},
					function(json){
						var data = JSON.parse(json);
						if (data.rows[0][1]==="1") {
							// 頁のリフレッシュ
							window.location.reload();
							nonreload = false;
						}
					}
				);
				// 頁リフレッシュなしの場合
				if (nonreload) {
					// 検索条件保持
					$.post($.reg.easy, {
						"page": reportno,
						"obj": $.id.object_search,
						"sel": "json",
						"userid": $('#'+$.id.userid).val(),
						"user": $('#hiddenUser').val(),
						"report": $('#hiddenReport').val(),
						"json": JSON.stringify($.report[reportNumber].getJSONString())
					}, function(json){
						// 検索実行
						$.report[reportNumber].success(reportno);
					});
				}
				return true;
			} else {
				return false;
			}
		},
		/**
		 * Excelイベントボタン
		 * @param {Object} e
		 */
		pushExcel:function(e){
			// レポート番号取得
			var reportno=$($.id.hidden_reportno).val();

			// レポート定義位置
			var reportNumber = $.getReportNumber(reportno);
			if (typeof(reportNumber) !== 'number') { alert("レポート定義が見つかりません。"); return false;}

			// 検索結果の再確認
			if ($.report[reportNumber].getRecord() <= 0){
				alert($.message.ID_MESSAGE_WARNING_EXCEL_OUTPUT);
				return false;
			}

			// ログ情報の格納
			$.post(
				$.reg.easy ,
				{
					"page" : reportno ,
					"obj"  : $.id.object_excel ,
					"sel"   : new Date().getTime(),
					"userid": $('#'+$.id.userid).val(),
					"user": $('#hiddenUser').val(),
					"report": $('#hiddenReport').val(),
					"json"  : ""
				},
				function(json){}
			);

			// Excel 出力
			if ($.report[reportNumber].excel != undefined) {
				$.report[reportNumber].excel(reportno);
			}else{
				$.outputExcel(reportno);
			}
			return false;
		},
		/**
		 * Excel出力実行
		 * @param {Object} reportno
		 * @param {Object} kbn
		 */
		outputExcel:function(reportno, kbn){
			if(kbn == null) kbn = 0;
			window.parent.frames['blank'].open($.reg.excel+'?report='+reportno+'&kbn='+kbn+'&ts='+(new Date()).getTime(),'_self','width=400, height=300, menubar=no, toolbar=no, scrollbars=yes');
			return false;
		},

		/**
		 * Excel出力エラー
		 */
		outputExcelError:function(){
			alert($.message.ID_MESSAGE_ERROR_EXCEL_OUTPUT);
		},

		ItemSets:function(e){
			// レポート番号取得
			var reportno=$($.id.hidden_reportno).val();

			// レポート定義位置
			var reportNumber = $.getReportNumber(reportno);
			if (typeof(reportNumber) !== 'number') { alert("レポート定義が見つかりません。"); return false;}
			window.open("../jsp/reports/Report_Columnn_N.jsp","win_column", 'width=400,height=600,scrollbars=0,resizable=1,status=0 ');


			// Excel 出力
			//window.open($.reg.excel+'?report='+reportno,'_blank','width=400, height=300, menubar=no, toolbar=no, scrollbars=yes');
			return false;
		},
		/**
		 * 移動イベントボタン
		 * @param {Object} e
		 */
		pushReturn:function(e){

			// レポート番号取得
			var reportno=$($.id.hidden_reportno).val();

			// レポート定義位置
			var reportNumber = $.getReportNumber(reportno);
			if (typeof(reportNumber) !== 'number') { alert("レポート定義が見つかりません。"); return false;}

			// 検索結果の再確認
			if ($.report[reportNumber].getRecord() <= 0){
				alert($.message.ID_MESSAGE_WARNING_REPORT_OUTPUT);
				return false;
			}

			// ボタン押下処理
			$.report[reportNumber].onClickButton(e.id);
		},

		/**
		 * Combobox、Combogridにフォーカスを移動時、全選択状態にする
		 * @param {Object} id
		 */
		setFocusEvent: function(id){
			var combotext = $('#'+id).combo('textbox');
			combotext.focus(function(){
				$(this).select();
			});
			combotext.mouseup(function(e){
				e.preventDefault();
			});
		},

		/**
		 * ComboBox共通作成用
		 * @param {Object} jsonHidden
		 * @param {Object} id
		 */
		setCombobox: function(jsonHidden, id){
			$('#'+id).combobox({
				required:false,
				editable:false,
				panelHeight:'auto',
				onLoadSuccess:function(data){
					// 初期化
					var json = $.getJSONObject(jsonHidden, id);
					if (json){
						$('#'+id).combobox('setValue',json.value);
					}
				}
			});
		},

		/**
		 * NumberSpinner共通作成用
		 * @param {Object} jsonHidden
		 * @param {Object} id
		 * @param {Object} min
		 * @param {Object} max
		 */
		setNumberspinner: function(jsonHidden, id, min, max){
			$('#'+id).numberspinner({
				editable:true,
				min: min,
				max: max
			});
			// 初期化
			var json = $.getJSONObject(jsonHidden, id);
			if (json){
				$('#'+id).numberspinner('setValue',json.value);
			}
		},

		/**
		 * チェックボックス擬似ウインドウ(小)作成
		 * @param {Object} jsonHidden
		 * @param {Object} id
		 * @param {Object} prefix
		 * @param {Object} name
		 * @param {Object} obj - option.xx.js
		 * @param {Object} param - パラメーター名
		 */
		setChkBoxWindowS: function(reportno, id, prefix, name, obj, param){	//
			$.setChkBoxWindow(reportno, id, prefix, name, 200, 200, 150, 8, obj, param);
		},
		/**
		 * チェックボックス擬似ウインドウ(大)作成
		 * @param {Object} jsonHidden
		 * @param {Object} id
		 * @param {Object} prefix
		 * @param {Object} name
		 * @param {Object} obj - option.xx.js
		 * @param {Object} param - パラメーター名
		 */
		setChkBoxWindowL: function(reportno, id, prefix, name, obj, param){	//
			$.setChkBoxWindow(reportno, id, prefix, name, 430, 800, 75, 38, obj, param);
		},
		/**
		 * チェックボックス擬似ウインドウ作成
		 * @param {Object} reportno
		 * @param {Object} id
		 * @param {Object} prefix
		 * @param {Object} name
		 * @param {Object} h - height
		 * @param {Object} w - width
		 * @param {Object} t - top
		 * @param {Object} n - divisor
		 * @param {Object} obj - option.xx.js
		 * @param {Object} param - パラメーター名
		 * @param boolean checked - 初期設定
		 */
		setChkBoxWindow: function(reportno, id, prefix, name, h, w, t, n, obj, param, initchecked){	//
			var listId = prefix+ "List";
			var winId = prefix+ "Window";
			var btnDivId = prefix+ "Btnform";
			var chkBtnId = "btn_" + prefix+ "Check";
			var cclBtnId = "btn_" + prefix+ "Cancel";
			var item = '<div id="'+ winId + '" ><div id="' + listId + '" style="padding:3px 0px;font-size:105%;"></div><div id="' + btnDivId +'" class="line" style="height: 2em;display: none;">'
						+ '<input type="button" id="' + chkBtnId + '" value="全選択" class="btn01"><input type="button" id="' + cclBtnId + '" value="全解除" class="btn01"></div></div>';
			$('#'+id ).after(item);
			$('#'+chkBtnId).click(function () {$("input[name='" + id + "']").prop('checked',true);});
			$('#'+cclBtnId).click(function () {$("input[name='" + id + "']").prop('checked',false);});
			if(h > 300 && w > 300){
				var clsBtnId = "btn_" + prefix+ "Close";
				$('#'+cclBtnId).after('<input type="button" id="' + clsBtnId + '" value="確定" class="btn01">');
				$('#'+clsBtnId).click(function () {$("#"+winId).window('close');});
			}
			$.post(
					$.reg.easy ,
					{
						"page"		:	reportno,
						"obj"			:	id,
						"sel"			:	(new Date()).getTime(),
						"target"	:	id,
						"action"	:	$.id.action_init,
						"json"		:	JSON.stringify([{}]),
						"rowsOnly"	:	true
					},
					function(data){
						// 初期値取得
						var values = $.convSelectVal($.getFirstValue(obj,id,param),true);
						// チェックリスト作成
						for(var i=0; i < data.length; i++){
							if(i%n==0){
								$("#"+listId).append('<ul style="list-style-type: none; list-style-position: outside; float: left; padding:0px 5px;margin:0;"></ul>');
							}
							$("#"+listId + " ul:last").append('<li><label for="' + id + data[i]['VALUE'] + '"></label></li>');
							var checked = "";
							if(values&&$.inArray(data[i]['VALUE'], values)!=-1){
								checked = " checked";
							}
							if (initchecked){
								checked = " checked";
							}
							$("#"+listId + " label:last").append('<input type="checkbox" name="' + id  + '" id="' + id  + data[i]['VALUE'] + '" value="' + data[i]['VALUE'] + '"' + checked + ' />' +  data[i]['TEXT']);
						}
						$("#"+winId).window({
							title		:name+"選択",
							width		:w,
							height	:h,
							top			:t,
							collapsible	:false,
							minimizable	:false,
							maximizable	:false,
							resizable	:false,
							doSize		:false,
							closed		:true
						}).window("close");
						$("#"+listId).panel({ width:w-14, height:h-62});
						$('#'+id).click(function () { $("#"+winId).window('open');});
						$('#'+btnDivId).show();
						// 初期設定終了の場合、イベント実行
						if (obj.initedObject.length == obj.initedObjCount){
							obj.triggerInitEvent();
						}
					},
					"json"
			);
		},
		id : {
			 setHeight				:	8					// オフセット
			,action_default		:	"run"			// action パラメータの初期値
			,action_get				:	"get"			// action パラメータ データ取得用
			,action_init			:	"init"		// action パラメータ 初期化
			,action_defaults	:	"defaults"// action パラメータ 初期値
			,action_items			:	"items"		// action パラメータ 商品取得

			,area_header			:	"header"		// ヘッダ
			,area_tab				:	"report_tab"	// タブ
			,area_factor			:	"AREA_FACTOR"	// 分析条件
			,area_report			:	"MAIN_REPORT"	// 結果表示

			,hidden_reportno		:	"#reportno"		// レポート番号
			,userid					:	"userid"		// ユーザーID
			,searchmode				:	"searchmode"	// 検索実行ボタンの判定（0:通常検索、1:商品入力）
			,hiddenSyohinCode		:	"hiddenSyohinCode"	// 商品コード
			,hiddenSendParam		:	"#hiddenParam"	// パラメータ

			,hdn_max_dt				:	"#hiddenMaxDate"	// 最大日付

			,target_default			:	"default"		// target パラメータオプション デフォルト値取得

			,object_search			:	"btn_Search"	// オブジェクト番号
			,object_search1			:	"btn_Search1"	// オブジェクト番号
			,object_search2			:	"btn_Search2"	// オブジェクト番号
			,object_search3			:	"btn_Search3"	// オブジェクト番号
			,object_search_promo	:	"btn_Search_Promo"	// オブジェクト番号
			,object_excel			:	"btn_Excel"		// オブジェクト番号
			,object_excel2			:	"btn_Excel2"	// オブジェクト番号
			,object_item			:	"btn_Item"		// オブジェクト番号
			,object_reset			:	"btn_Reset"		// オブジェクト番号
			,object_upload			:	"up_load_btn"	// オブジェクト番号
			,object_download		:	"dow_load_btn"	// オブジェクト番号


			,div_input201310		:	"div_input201310"	// DIV（入力経路）2013年10月まで
			,div_input201311		:	"div_input201311"	// DIV（入力経路）2013年11月以降
			,div_kaiin				:	"div_kaiin"			// DIV（会員区分）
			,div_stage_kaiin		:	"div_stage_kaiin"	// DIV（ステージ／会員区分）
			,div_stage				:	"div_stage"			// DIV（ステージ）

			,lbl_check				:	"label_check"		// ラベル（レポート更新チェック）
			,lbl_tsu				:	"label_tsu"			// ラベル（通販受注実績予想数）
			,lbl_tsu2				:	"label_tsu2"		// ラベル（不正データ削除日）
			,lbl_yosan				:	"label_yosan"		// ラベル（売上目標）
			,lbl_kaigai				:	"label_kaigai"		// ラベル（海外関係会社）
			,lbl_yushu				:	"label_yushu"		// ラベル（輸出先）
			,lbl_brand				:	"label_brand"		// ラベル（他ブランド）
			,lbl_itaku				:	"label_itaku"		// ラベル（他社売上）

			,lbl_up_down			:	"up_down_msg"		// ラベル（アップロード画面メッセージ）

			,select_ym				:	"select_ym"			// 選択リスト（基準年月）
			,select_ymb				:	"select_ymb"			// 選択リスト（基準年月）
			,select_ymb2			:	"select_ymb2"			// 選択リスト（基準年月）
			,select_ym_kor			:	"select_ym_kor"		// 選択リスト（基準年月） 韓国
			,select_yy				:	"select_yy"			// 選択リスト（基準年）
			,select_mm				:	"select_mm"			// 選択リスト（基準月）
			,select_dd				:	"select_dd"			// 選択リスト（基準日）
			,select_ymd				:	"select_ymd"			// 選択リスト（基準日）
			,select_ymd2			:	"select_ymd2"			// 選択リスト（基準日）
			,select_ym2				:	"select_ym2"			// 選択リスト（基準年月）
			,select_yy2				:	"select_yy2"			// 選択リスト（基準年）
			,select_mm2				:	"select_mm2"			// 選択リスト（基準月）
			,select_dd2				:	"select_dd2"			// 選択リスト（基準日）
			,select_ym_nr			:	"select_ym_nr"			// 選択リスト（新製品準日）
			,select_ym_nr2			:	"select_ym_nr2"			// 選択リスト（新製品準日）

			,select_ym_up			:	"cmb_ym"			// 選択リスト(アップロード画面年月)

			,select_chanel			:	"select_chanel"		// 選択リスト（販売チャネル）
			,select_chanel_kor		:	"select_chanel_kor"	// 選択リスト（販売チャネル） 韓国
			,select_type1			:	"select_type1"		// 選択リスト（形式表示）
			,select_type1_2			:	"select_type1_2"		// 選択リスト（展開表示）
			,select_type2			:	"select_type2"		// 選択リスト（展開表示）
			,select_nr_type			:	"select_nr_type"		// 選択リスト（形式表示）
			,select_nr_typeS		:	"select_nr_typeS"		// 選択リスト（商品出荷実績新規）

			,select_juchu_from		:	"select_juchu_from"	// 選択リスト（初回受注月FROM）
			,select_juchu_to		:	"select_juchu_to"	// 選択リスト（初回受注月TO）
			,select_juchu_ym1		:	"select_juchu_ym1"	// 選択リスト（初回受注月FROM）転換率
			,select_juchu_ym2		:	"select_juchu_ym2"	// 選択リスト（初回受注月TO）転換率
			,select_toroku_from		:	"select_toroku_from" // 選択リスト（登録年月FROM）
			,select_toroku_to		:	"select_toroku_to"	 // 選択リスト（登録年月TO）
			,select_promo_from		:	"select_promo_from"	// 選択リスト（プロモーションFROM）
			,select_promo_to		:	"select_promo_to"	// 選択リスト（プロモーションTO）
			,select_promo			:	"select_promo"		// 選択リスト（プロモーション）
			,select_keisaibi		:	"select_keisaibi"		// 選択リスト（掲載日）
			,select_segno_from		:	"select_segno_from"	// 選択リスト（条件番号FROM）
			,select_segno_to		:	"select_segno_to"	// 選択リスト（条件番号TO）
			,select_segno			:	"select_segno"		// 選択リスト（条件番号
			,select_segno_t			:	"select_segno_t"	// 選択リスト（条件番号）
			,select_sho1			:	"select_sho1"		// 選択リスト（商品分類1）
			,select_sho2			:	"select_sho2"		// 選択リスト（商品分類2）
			,select_sho3			:	"select_sho3"		// 選択リスト（商品分類3）
			,select_sho4			:	"select_sho4"		// 選択リスト（商品分類4）
			,select_sho5			:	"select_sho5"		// 選択リスト（商品分類5）
			,select_sho6			:	"select_sho6"		// 選択リスト（商品分類6）
			,select_item			:	"select_item"		// 選択リスト（商品）

			,select_kaiin			:	"select_kaiin"		// 選択リスト（会員区分）
			,select_stage_kaiin		:	"select_stage_kaiin"// 選択リスト（ステージ/会員区分）
			,select_stage			:	"select_stage"		// 選択リスト（ステージ時）
			,select_kaiin_kor		:	"select_kaiin_kor"	// 選択リスト（会員区分） 韓国

			,select_tenpo			:	"select_tenpo"		// 選択リスト（）
			,select_juhan			:	"select_juhan"		// 選択リスト（）
			,select_kaigai			:	"select_kaigai"		// 選択リスト（海外関係会社）
			,select_yushu			:	"select_yushu"		// 選択リスト（輸出先）
			,select_itaku			:	"select_itaku"		// 選択リスト（委託販売）
			,select_brand			:	"select_brand"		// 選択リスト（他ブランド）
			,select_tenpo_g			:	"select_tenpo_g"	// 選択リスト（）
			,select_htype1			:	"select_htype1"		// 選択リスト（）
			,select_htype2			:	"select_htype2"		// 選択リスト（商品出荷実績数額）
			,select_htype3			:	"select_htype3"		// 選択リスト（）

			,select_kaisu			:	"select_kaisu"		// 回数
			,select_kaisu2			:	"select_kaisu2"		// 回数
			,select_juc_kbn			:	"select_juc_kbn"	// 受注形態
			,select_age				:	"select_age"		// 年齢
			,select_age_kor			:	"select_age_kor"	// 年齢 韓国
			,select_area			:	"select_area"		// 地域
			,select_area_kor		:	"select_area_kor"	// 地域 韓国
			,select_s_bytai			:	"select_s_bytai"	// 媒体
			,select_s_bytai2		:	"select_s_bytai2"	// 媒体種類
			,select_s_shog			:	"select_s_shog"		// 初回商品
			,select_s_shog2			:	"select_s_shog2"	// 初回商品
			,select_F2jiki			:	"select_F2jiki"		// F2転換時期
			,select_janru			:	"select_janru"		// 購入ジャンル

			,select_cha_1			:	"select_cha_1"		// 選択リスト（商品出荷実績chanel分類）
			,select_cha_2			:	"select_cha_2"		// 選択リスト（商品出荷実績chanel分類）
			,select_cha_3			:	"select_cha_3"		// 選択リスト（商品出荷実績chanel分類）
			,select_cha_4			:	"select_cha_4"		// 選択リスト（商品出荷実績chanel分類）

			,select_bun				:	"select_bun"		// 選択リスト（商品出荷実績項目）
			,select_bun_1			:	"select_bun_1"		// 選択リスト（商品出荷実績項目）
			,select_bun_2			:	"select_bun_2"		// 選択リスト（商品出荷実績項目）
			,select_bun_3			:	"select_bun_3"		// 選択リスト（商品出荷実績項目）
			,select_bun_4			:	"select_bun_4"		// 選択リスト（商品出荷実績項目）
			,select_bun_5			:	"select_bun_5"		// 選択リスト（商品出荷実績項目）
			,select_bun_6			:	"select_bun_6"		// 選択リスト（商品出荷実績項目）
			,select_bun_7			:	"select_bun_7"		// 選択リスト（商品出荷実績項目）
			,select_bun_8			:	"select_bun_8"		// 選択リスト（商品出荷実績項目）
			,select_bun_9			:	"select_bun_9"		// 選択リスト（商品出荷実績項目）
			,select_bun_10			:	"select_bun_10"		// 選択リスト（商品出荷実績項目）

			,select_shos			:	"select_shos"		// 選択リスト（商品出荷実績商品分類）
			,select_shos_1			:	"select_shos_1"		// 選択リスト（商品出荷実績商品分類）
			,select_shos_2			:	"select_shos_2"		// 選択リスト（商品出荷実績商品分類）
			,select_shos_3			:	"select_shos_3"		// 選択リスト（商品出荷実績商品分類）
			,select_shos_4			:	"select_shos_4"		// 選択リスト（商品出荷実績商品分類）
			,select_shos_5			:	"select_shos_5"		// 選択リスト（商品出荷実績商品分類）
			,select_shos_6			:	"select_shos_6"		// 選択リスト（商品出荷実績商品分類）
			,select_shos_7			:	"select_shos_7"		// 選択リスト（商品出荷実績商品分類）

			,select_bs				:	"select_bs"			// 選択リスト（商品出荷実績商品分類）
			,select_bs_1			:	"select_bs_1"		// 選択リスト（商品出荷実績商品分類）
			,select_bs_2			:	"select_bs_2"		// 選択リスト（商品出荷実績商品分類）
			,select_bs_3			:	"select_bs_3"		// 選択リスト（商品出荷実績商品分類）
			,select_bs_4			:	"select_bs_4"		// 選択リスト（商品出荷実績商品分類）
			,select_bs_5			:	"select_bs_5"		// 選択リスト（商品出荷実績商品分類）
			,select_bs_6			:	"select_bs_6"		// 選択リスト（商品出荷実績商品分類）
			,select_bs_7			:	"select_bs_7"		// 選択リスト（商品出荷実績商品分類）
			,select_bs_8			:	"select_bs_8"		// 選択リスト（商品出荷実績商品分類）
			,select_bs_9			:	"select_bs_9"		// 選択リスト（商品出荷実績商品分類）
			,select_bs_10			:	"select_bs_10"		// 選択リスト（商品出荷実績商品分類）

			,radio_category			:	"radio_category"	// ラジオ（カテゴリー）
			,radio_category_1		:	"radio_category_1"	// ラジオ（カテゴリー1）
			,radio_category_2		:	"radio_category_2"	// ラジオ（カテゴリー2）
			,radio_sho1				:	"radio_sho1"		// ラジオ（商品表示）
			,radio_sho1_1			:	"radio_sho1_1"	// ラジオ（商品表示1）
			,radio_sho1_2			:	"radio_sho1_2"	// ラジオ（商品表示2）
			,radio_sho2				:	"radio_sho2"		// ラジオ（商品金額選択）
			,radio_sho2_1			:	"radio_sho2_1"	// ラジオ（商品金額選択1）
			,radio_sho2_2			:	"radio_sho2_2"	// ラジオ（商品金額選択2）
			,radio_sho2_3			:	"radio_sho2_3"	// ラジオ（商品金額選択2）
			,radio_sho2_4			:	"radio_sho2_4"	// ラジオ（商品金額選択2）

			,radio_b1			:	"radio_b1"		// ラジオ（基本形。年・月・日）
			,radio_b1_1			:	"radio_b1_1"	// ラジオ（年）
			,radio_b1_2			:	"radio_b1_2"	// ラジオ（月）
			,radio_b1_3			:	"radio_b1_3"	// ラジオ（日）
			,radio_b1_4			:	"radio_b1_4"	// ラジオ（基本形）
			,radio_b2			:	"radio_b2"		// ラジオ（対比表示）
			,radio_b2_1			:	"radio_b2_1"	// ラジオ（前年比）
			,radio_b2_2			:	"radio_b2_2"	// ラジオ（前月比）
			,radio_b2_3			:	"radio_b2_3"	// ラジオ（なし）
			,radio_b3			:	"radio_b3"		// ラジオ（表示対象）
			,radio_b3_1			:	"radio_b3_1"	// ラジオ（オルビス）
			,radio_b3_2			:	"radio_b3_2"	// ラジオ（関連会社）
			,radio_b3_3			:	"radio_b3_3"	// ラジオ（連結）

			,radio_b4			:	"radio_b4"		// ラジオ（商品出荷実績単12指定）
			,radio_b4_1			:	"radio_b4_1"	// ラジオ（商品出荷実績単12指定）
			,radio_b4_2			:	"radio_b4_2"	// ラジオ（商品出荷実績単12指定）
			,radio_b4_3			:	"radio_b4_3"	// ラジオ（商品出荷実績単12指定）
			,radio_b5			:	"radio_b5"		// ラジオ（商品出荷実績新製品指定）
			,radio_b5_1			:	"radio_b5_1"	// ラジオ（商品出荷実績新製品指定）
			,radio_b5_2			:	"radio_b5_2"	// ラジオ（商品出荷実績新製品指定）
			,radio_b5_3			:	"radio_b5_3"	// ラジオ（商品出荷実績新製品指定）
			,radio_b6			:	"radio_b6"		// ラジオ（商品出荷実績新リピート別）
			,radio_b6_1			:	"radio_b6_1"	// ラジオ（商品出荷実績新リピート別）
			,radio_b6_2			:	"radio_b6_2"	// ラジオ（商品出荷実績新リピート別）
			,radio_b6_3			:	"radio_b6_3"	// ラジオ（商品出荷実績新リピート別）
			,radio_b7a			:	"radio_b7a"		// ラジオ（商品出荷実績店販大分類）
			,radio_b7a_1		:	"radio_b7a_1"	// ラジオ（商品出荷実績店販大分類）
			,radio_b7a_2		:	"radio_b7a_2"	// ラジオ（商品出荷実績店販大分類）
			,radio_b7a_3		:	"radio_b7a_3"	// ラジオ（商品出荷実績店販大分類）
			,radio_b7a_4		:	"radio_b7a_4"	// ラジオ（商品出荷実績店販大分類）
			,radio_b7a_5		:	"radio_b7a_5"	// ラジオ（商品出荷実績店販大分類）
			,radio_b7b			:	"radio_b7b"		// ラジオ（商品出荷実績店販中分類）
			,radio_b7b_1		:	"radio_b7b_1"	// ラジオ（商品出荷実績店販中分類）
			,radio_b7b_2		:	"radio_b7b_2"	// ラジオ（商品出荷実績店販中分類）
			,radio_b8			:	"radio_b8"		// ラジオ（商品出荷実績分類AND OR選択）
			,radio_b8_1			:	"radio_b8_1"	// ラジオ（商品出荷実績分類AND OR選択）
			,radio_b8_2			:	"radio_b8_2"	// ラジオ（商品出荷実績分類AND OR選択）

			,radio_b9			:	"radio_b9"		// ラジオ（外貨表示選択）
			,radio_b9_1			:	"radio_b9_1"	// ラジオ（外貨表示選択）
			,radio_b9_2			:	"radio_b9_2"	// ラジオ（外貨表示選択）

			,radio_b10			:	"radio_b10"		// ラジオ（選択）
			,radio_b10_1		:	"radio_b10_1"	// ラジオ（選択）chk_jun_1
			,radio_b10_2		:	"radio_b10_2"	// ラジオ（選択）

			,radio_t01			:	"radio_t01"		// ラジオ（選択）
			,radio_t01_1		:	"radio_t01_1"	// ラジオ（選択）
			,radio_t01_2		:	"radio_t01_2"	// ラジオ（選択）
			,radio_t01_3		:	"radio_t01_3"	// ラジオ（選択）
			,radio_t01_4		:	"radio_t01_4"	// ラジオ（選択）
			,radio_t02			:	"radio_t02"		// ラジオ（選択）
			,radio_t02_1		:	"radio_t02_1"	// ラジオ（選択）
			,radio_t02_2		:	"radio_t02_2"	// ラジオ（選択）
			,radio_t02_3		:	"radio_t02_3"	// ラジオ（選択）
			,radio_t03			:	"radio_t03"		// ラジオ（選択）
			,radio_t03_1		:	"radio_t03_1"	// ラジオ（選択）
			,radio_t03_2		:	"radio_t03_2"	// ラジオ（選択）
			,radio_t04			:	"radio_t04"		// ラジオ（選択）
			,radio_t04_1		:	"radio_t04_1"	// ラジオ（選択）
			,radio_t04_2		:	"radio_t04_2"	// ラジオ（選択）
			,radio_t04_3		:	"radio_t04_3"	// ラジオ（選択）
			,radio_t05			:	"radio_t05"		// ラジオ（選択）
			,radio_t05_1		:	"radio_t05_1"	// ラジオ（選択）
			,radio_t05_2		:	"radio_t05_2"	// ラジオ（選択）
			,radio_t05_3		:	"radio_t05_3"	// ラジオ（選択）
			,radio_t05_4		:	"radio_t05_4"	// ラジオ（選択）
			,radio_t06			:	"radio_t06"		// ラジオ（選択）
			,radio_t06_1		:	"radio_t06_1"	// ラジオ（選択）
			,radio_t06_2		:	"radio_t06_2"	// ラジオ（選択）
			,radio_t07			:	"radio_t07"		// ラジオ（選択）
			,radio_t07_1		:	"radio_t07_1"	// ラジオ（選択）
			,radio_t07_2		:	"radio_t07_2"	// ラジオ（選択）
			,radio_t08			:	"radio_t08"		// ラジオ（選択）
			,radio_t08_1		:	"radio_t08_1"	// ラジオ（選択）
			,radio_t08_2		:	"radio_t08_2"	// ラジオ（選択）
			,radio_t09			:	"radio_t09"		// ラジオ（選択）
			,radio_t09_1		:	"radio_t09_1"	// ラジオ（選択）
			,radio_t09_2		:	"radio_t09_2"	// ラジオ（選択）
			,radio_t09_3		:	"radio_t09_3"	// ラジオ（選択）
			,radio_t10			:	"radio_t10"		// ラジオ（選択）
			,radio_t10_1		:	"radio_t10_1"	// ラジオ（選択）
			,radio_t10_2		:	"radio_t10_2"	// ラジオ（選択）

			,radio_d01	:"radio_d01"// ラジオ（購入チャネル）
			,radio_d01_1:"radio_d01_1"
			,radio_d01_2:"radio_d01_2"
			,radio_d01_3:"radio_d01_3"
			,radio_d02	:"radio_d02"// ラジオ（集計単位）
			,radio_d02_1:"radio_d02_1"
			,radio_d02_2:"radio_d02_2"
			,radio_d02_3:"radio_d02_3"
			,radio_d02_4:"radio_d02_4"
			,radio_d03	:"radio_d03"// ラジオ（店舗）
			,radio_d03_1:"radio_d03_1"
			,radio_d03_2:"radio_d03_2"
			,radio_d04	:"radio_d04"// ラジオ（都道府県）
			,radio_d04_1:"radio_d04_1"
			,radio_d04_2:"radio_d04_2"
			,radio_d05	:"radio_d05"// ラジオ（年齢）
			,radio_d05_1:"radio_d05_1"
			,radio_d05_2:"radio_d05_2"
			,radio_d05_3:"radio_d05_3"
			,radio_d06	:"radio_d06"// ラジオ（初回購入年）
			,radio_d06_1:"radio_d06_1"
			,radio_d06_2:"radio_d06_2"
			,radio_d06_3:"radio_d06_3"
			,radio_d07	:"radio_d07"// ラジオ（前年購入額）
			,radio_d07_1:"radio_d07_1"
			,radio_d08	:"radio_d08"// ラジオ（R値）
			,radio_d08_1:"radio_d08_1"
			,radio_d08_2:"radio_d08_2"
			,radio_d09	:"radio_d09"// ラジオ（購入商品）
			,radio_d09_1:"radio_d09_1"
			,radio_d09_2:"radio_d09_2"
			,radio_d09_3:"radio_d09_3"
			,radio_d09_4:"radio_d09_4"

			,radio_d11	:"radio_d11"// ラジオ（集計軸）
			,radio_d11_1:"radio_d11_1"
			,radio_d11_2:"radio_d11_2"
			,radio_d11_3:"radio_d11_3"
			,radio_d11_4:"radio_d11_4"

			,radio_d12	:"radio_d12"// ラジオ（照会）
			,radio_d12_1:"radio_d12_1"
			,radio_d12_2:"radio_d12_2"
			,radio_d12_3:"radio_d12_3"

			,radio_d13	:"radio_d13"// ラジオ（表示項目）
			,radio_d13_1:"radio_d13_1"
			,radio_d13_2:"radio_d13_2"
			,radio_d13_3:"radio_d13_3"
			,radio_d13_4:"radio_d13_1"
			,radio_d13_5:"radio_d13_2"
			,radio_d13_6:"radio_d13_3"
			,radio_d13_7:"radio_d13_1"
			,radio_d13_8:"radio_d13_2"

			,radio_d14	:"radio_d14"// ラジオ（商品分野）
			,radio_d14_1:"radio_d14_1"
			,radio_d14_2:"radio_d14_2"
			,radio_d14_3:"radio_d14_3"
			,radio_d14_3:"radio_d14_4"
			,radio_d14_3:"radio_d14_5"

			,radio_uptype		:	"rad_uptype"	// ラジオ（アップロード/ダウンロード項目選択）

			,check_d10_n:	"check_d10_"		//
			,check_d10_1:	"check_d10_1"		// チェック（基本分類）
			,check_d10_2:	"check_d10_2"		// チェック（大分類）
			,check_d10_3:	"check_d10_3"		// チェック（中分類）
			,check_d10_4:	"check_d10_4"		// チェック（小分類）
			,check_d10_5:	"check_d10_5"		// チェック（シリーズA）
			,check_d10_6:	"check_d10_6"		// チェック（シリーズB）
			,check_d10_7:	"check_d10_7"		// チェック（商品）

			,chk_jun_1			:	"chk_jun_1"		// チェックＢＯＸ（）

			,check_sho1			:	"check_sho1"		// チェックＢＯＸ（商品分類1）
			,check_sho2			:	"check_sho2"		// チェックＢＯＸ（商品分類1）
			,check_sho3			:	"check_sho3"		// チェックＢＯＸ（商品分類1）
			,check_sho4			:	"check_sho4"		// チェックＢＯＸ（商品分類1）
			,check_sho5			:	"check_sho5"		// チェックＢＯＸ（商品分類1）
			,check_sho6			:	"check_sho6"		// チェックＢＯＸ（商品分類1）
			,check_item			:	"check_item"		// チェックＢＯＸ（商品分類1）

			,check_kei			:	"check_kei"			// チェックＢＯＸ（入力経路）
			,check_kei1			:	"check_kei1"		// チェックＢＯＸ（入力経路）
			,check_kei2			:	"check_kei2"		// チェックＢＯＸ（入力経路1）
			,check_kei3			:	"check_kei3"		// チェックＢＯＸ（入力経路1）
			,check_kei4			:	"check_kei4"		// チェックＢＯＸ（入力経路1）
			,check_kei5			:	"check_kei5"		// チェックＢＯＸ（入力経路1）
			,check_kei6			:	"check_kei6"		// チェックＢＯＸ（入力経路1）
			,check_kei7			:	"check_kei7"		// チェックＢＯＸ（入力経路1）

			,check_kei8			:	"check_kei8"		// チェックＢＯＸ（入力経路8）
			,check_kei9			:	"check_kei9"		// チェックＢＯＸ（入力経路9）
			,check_kei5K		:	"check_kei5K"		// チェックＢＯＸ（入力経路5改）

			,chk_b1				:	"chk_b1"		//
			,chk_b1_1			:	"chk_b1_1"		// チェックＢＯＸ（）
			,chk_b1_2			:	"chk_b1_2"		// チェックＢＯＸ（）
			,chk_b1_3			:	"chk_b1_3"		// チェックＢＯＸ（）
			,chk_b1_4			:	"chk_b1_4"		// チェックＢＯＸ（）
			,chk_b1_5			:	"chk_b1_5"		// チェックＢＯＸ（）
			,chk_b1_6			:	"chk_b1_6"		// チェックＢＯＸ（）

			,chk_cl				:	"chk_cl"		//
			,chk_cl1			:	"chk_cl1"		// チェック（商品出荷実績チャネル）
			,chk_cl2			:	"chk_cl2"		// チェック（商品出荷実績チャネル）
			,chk_cl3			:	"chk_cl3"		// チェック（商品出荷実績チャネル）
			,chk_cl4			:	"chk_cl4"		// チェック（商品出荷実績チャネル）

			,chk_sl				:	"chk_sl"		//
			,chk_sl1			:	"chk_sl1"		// チェック（商品出荷実績商品集計レベル）
			,chk_sl2			:	"chk_sl2"		// チェック（商品出荷実績商品集計レベル）
			,chk_sl3			:	"chk_sl3"		// チェック（商品出荷実績商品集計レベル）
			,chk_sl4			:	"chk_sl4"		// チェック（商品出荷実績商品集計レベル）
			,chk_sl5			:	"chk_sl5"		// チェック（商品出荷実績商品集計レベル）
			,chk_sl6			:	"chk_sl6"		// チェック（商品出荷実績商品集計レベル）
			,chk_sl7			:	"chk_sl7"		// チェック（商品出荷実績商品集計レベル）
			,chk_sl8			:	"chk_sl8"		// チェック（商品出荷実績商品集計レベル）

			,chk_nr1			:	"chk_nr1"		// チェック（商品出荷実績新規リピート）
			,chk_nr2			:	"chk_nr2"		// チェック（商品出荷実績新規リピート）
			,chk_dm				:	"chk_dm"		// チェック（）

			,item_jun			:	"select_item_jun"	// 表示項目-純売上
			,item_shu			:	"select_item_shu"	// 表示項目-出荷売上
			,item_cho			:	"select_item_cho"	// 表示項目-調整前純売上

			,chk_tenpo_all		:	"tenpo_all"			// チェック（全店舗）

			,fle_upload			:	"fle_upload"		// ファイル入力(アップロード)

			,btn_tenpo:"btn_tenpo" // 店舗選択
			,btn_area :"btn_area"  // 都道府県選択

			,button_search			:	"#btn_Search"	// 照会ボタン
			,button_search1			:	"#btn_Search1"	// 照会ボタン
			,button_search2			:	"#btn_Search2"	// 照会ボタン
			,button_search3			:	"#btn_Search3"	// 照会ボタン
			,button_excel			:	"#btn_Excel"	// EXCELボタン
			,button_excel2			:	"#btn_Excel2"	// EXCELボタン
			,button_item			:	"#btn_Item"		// 表示項目ボタン
			,button_promo			:	"#btn_Promo"	// 対象ﾌﾟﾛﾓｰｼｮﾝ一覧ボタン
			,button_search_promo	:	"#btn_Search_Promo"	// 対象ﾌﾟﾛﾓｰｼｮﾝ一覧 照会ボタン
			,button_upload			:	"#up_load_btn"	// アップロードボタン
			,button_download		:	"#dow_load_btn"	// ダウンロードボタン

			,listtable				:	"#list"			// jqGrid 用 table id
			,toolbarform			:	"#ff"			// toolbarform
			,toolbar				:	"#tb"			// toolbar
			,placeholder			:	"#placeholder"	// placeholder
			,gridholder				:	"#gridholder"	// gridholder
			,compholder				:	"#compholder"	// gridholder
			,buttons				:	"#buttons"		// buttons

		},

		key:{
			 stage_from			:	"201409"	// 会員区分・ステージの切替基準
			,stage_to			:	"201412"	// 会員区分・ステージの切替基準
			,stage_Y_from		:	"2014"		// 会員区分・ステージの切替基準
			,stage_Y_to			:	"2014"		// 会員区分・ステージの切替基準

			,input_kei5			:	"201310"	// 入力経路5「WEB」表示最終年月。以降「PC」表記
			,input_kei3			:	"200906"	// 入力経路3「OCR」表示最終年月。以降「手入力（委託）」表記

			,input_time_kei5	:	"20150220"	// 入力経路5「WEB」時間帯表示最終日。以降「PC」表記
		},

		input:{
			 kei3_200906			:	"ORC"			// 入力経路 ※2009年06月まで
			,kei3_200907			:	"手入力（委託）"// 入力経路 ※2009年07月から
			,kei5_201310			:	"WEB"			// 入力経路「WEB」※2013年10月まで
			,kei5_201311			:	"PC"			// 入力経路「PC」 ※2013年11月から
		},

		message : {
			 ID_MESSAGE_WARNING_EXCEL_OUTPUT		:	"照会後のみExcelの表示は可能です。"
			,ID_MESSAGE_WARNING_REPORT_OUTPUT		:	"照会後に実行してください。"
			,ID_MESSAGE_INVARID_FROM_TO				:	"年月の範囲の順番に誤りがあります。"
			,ID_MESSAGE_WARNING_NONE_PROMO_CODE		:	"プロモーションコードに誤りがあります。"
			,ID_MESSAGE_WARNING_BLANK_PROMO_CODE	:	"プロモーションコードは必須入力です。"
			,ID_MESSAGE_INVARID_FROM_TO_PROMO		:	"プロモーションコードの範囲の順番に誤りがあります。"
			,ID_MESSAGE_INVARID_FROM_TO_SEG			:	"条件番号の範囲の順番に誤りがあります。"
			,ID_MESSAGE_WARNING_BLANK_CHANEL		:	"チャネルは1つ以上選択してください。"
			,ID_MESSAGE_WARNING_FROM_JUNURI			:	"純売上は2006年1月以降を選択してください。"
			,ID_MESSAGE_INVARID_DATE 				:	"指定日付は存在していません。"
			,ID_MESSAGE_LONG_DATE 					:	"開始終了日付は62日の期間内で指定してください。"
			,ID_MESSAGE_LONG_MONTH 					:	"開始終了日付は36ヶ月の期間内で指定してください。"
			,ID_MESSAGE_SEL_TENPO					:	"選択は25店舗までにしてください。"
			,ID_MESSAGE_SEL_TENPO2					:	"店舗を選択してください。"
			,ID_MESSAGE_ERROR_EXCEL_OUTPUT			:	"Excelの出力に失敗しました。"
			,ID_MESSAGE_WARNING_SEG_FORMAT			:	"条件番号は1から100の半角数字をカンマ区切りで入力してください。\n範囲指定の場合はハイフン区切りで順番に入力してください。"
			,ID_MESSAGE_SEL_ITEM					:	"表示項目を選択してください。"
			,ID_MESSAGE_SEL_FILE					:	"ファイルを指定してください。"
		},
		report: []

      };

      var plugin = this;

      plugin.settings = {};

      var init = function() {
		// push Array report Option
		defaults.report.push(options);
		// extend
		$.extend(defaults);
      };

      plugin.PublicMethod = function() {
         // code goes here
      };

      var PrivateMethod = function() {
         // code goes here
      };

      init();

   };

})(jQuery);