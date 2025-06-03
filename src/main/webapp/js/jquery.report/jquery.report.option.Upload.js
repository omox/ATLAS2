/**
 * jquery report container
 * セグメント別実績
 */

;(function($) {

	$.extend({
		reportOption: {
		name:		'Report_Upload',				// （必須）レポートオプションの確認
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

			// ボタン伊ベンツ追加
			$($.id.button_upload).attr("disabled",true).on("click", that.pushUpload);
			$($.id.button_download).attr("disabled",true).on("click", that.pushDown);

			// 年月（売上目標）
			this.setYy(reportno, $.id.select_yy);
			this.setMm(reportno, $.id.select_mm);

			// 年月（プロモ計画情報）
			this.setYy(reportno, $.id.select_yy2);
			this.setMm(reportno, $.id.select_mm2);

			// ラジオボタン
			this.setRadBtn(reportno, $.id.radio_uptype);
			// メッセージ
			this.setMsg(reportno,$.id.lbl_up_down);

			// 初期化終了
			this.initializes=!this.initializes;
			// ログ出力
			$.log(that.timeData, 'init initialize:');
		},
		validation: function (){				// （必須）批准
			// 入力チェック
			var rt = true;
			if(this.button==1){
				var path = $('#'+$.id.fle_upload).val();
				if(this.isBlank(path)){
					this.message = $.message.ID_MESSAGE_SEL_FILE;
					rt = false;
				}else if(!path.match(/\.(txt|csv)$/i)){
					this.message = "テキスト,CSV" + $.message.ID_MESSAGE_SEL_FILE;
					rt = false;
				}
			}
			if(!rt){
				alert(this.message);
			}
			return rt;
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
			// 集計期間（プロモ計画情報）
			this.jsonString.push({
				id:		$.id.select_yy2,
				value:$('#'+$.id.select_yy2).val(),
				text:	$('#'+$.id.select_yy2+' option:selected').text()
			});
			this.jsonString.push({
				id:		$.id.select_mm2,
				value:$('#'+$.id.select_mm2).val(),
				text:	$('#'+$.id.select_mm2+' option:selected').text()
			});
			// 対象項目
			this.jsonString.push({
				id:		$.id.radio_uptype,
				value:	$("input[name="+$.id.radio_uptype + "]:checked").val(),
				text:	$("label[for='"+$("input:radio[name="+ $.id.radio_uptype + "]:checked").attr("id")+"']").text()
			});
			// ファイルパス
			this.jsonString.push({
				id:		$.id.fle_upload,
				value:	$('#'+$.id.fle_upload).val(),
				text:	$('#'+$.id.fle_upload).val()
			});
		},
		triggerInitEvent:function(){		// 初期表示時イベント実施
			var that = this;
			$($.id.button_upload).attr("disabled",false);
			$($.id.button_download).attr("disabled",false);
			// 初期表示処理完了
			that.initedObject.push("triggerInitEvent");
			// ログ出力
			$.log(that.timeData, 'triggerInitEvent:');
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
						obj1.val($.getFirstValue(that,id ,'START_YY_SEL'));
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
						obj1.val($.getFirstValue(that,id ,'START_MM_SEL'));
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
						// 初期表示終了時にイベント実行
						that.triggerInitEvent();
					},
					"json"
			);
		},
		setRadBtn: function(reportno, id){	// ラジオボタン
			var that = this;
			// 初期値設定
			var json = $.getJSONObject(that.jsonHidden, id);
			that.initedObject.push(id);
			if(json){
				$("input[name="+$.id.radio_uptype + "]").val([json.value]);
			}
			// 初期表示終了時にイベント実行
			if (that.initedObject.length == that.initedObjCount){
				that.triggerInitEvent();
			}
		},
		setMsg: function(reportno, id){	// アップロードメッセージ
			var that = this;
			var obj1 = $('#'+id );
			// 初期値設定
			var json = $.getJSONObject(that.jsonHidden, id);
			that.initedObject.push(id);
			if(json){
				obj1.addClass(json.value);
				obj1.append(json.text);
			}
			// 初期表示終了時にイベント実行
			if (that.initedObject.length == that.initedObjCount){
				that.triggerInitEvent();
			}
		},
		/**
		 * アップロードイベント
		 * @param {Object} e
		 */
		pushUpload:function(e){
			// レポート番号取得
			var reportno=$($.id.hidden_reportno).val();

			// レポート定義位置
			var reportNumber = $.getReportNumber(reportno);
			if (typeof(reportNumber) !== 'number') { alert("レポート定義が見つかりません。"); return false;}

			// メッセージクリア
			$('#'+$.id.lbl_up_down).empty();

			// フォーム情報取得
			$.report[reportNumber].getEasyUI();
			// ボタン判定追加
			$.report[reportNumber].button = 1;
			var id = e.target.id;
			if ($.report[reportNumber].validation()) {
				// 時間外または、メンテナンスの確認
				var nonreload = true;
				$.post(
					$.reg.easy ,
					{
						"page"   : reportno ,
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
						"obj": id,
						"sel": "json",
						"userid": $('#'+$.id.userid).val(),
						"user": $('#hiddenUser').val(),
						"report": $('#hiddenReport').val(),
						"json": JSON.stringify($.report[reportNumber].getJSONString())
					}, function(json){
						var url = "../Servlet/Upload.do";
						var frm = document.forms["form_upload"];
						frm.action=url;
						frm.submit();
					});
				}
				return true;
			} else {
				return false;
			}
		},
		/**
		 * ダウンロードイベント
		 * @param {Object} e
		 */
		pushDown:function(e){
			// レポート番号取得
			var reportno=$($.id.hidden_reportno).val();

			// レポート定義位置
			var reportNumber = $.getReportNumber(reportno);
			if (typeof(reportNumber) !== 'number') { alert("レポート定義が見つかりません。"); return false;}

			// メッセージクリア
			$('#'+$.id.lbl_up_down).empty();

			// フォーム情報取得
			$.report[reportNumber].getEasyUI();
			// ボタン判定追加	ボタン判定-1:純売上その他 2:出荷売上 3:調整前純売上
			var id = e.target.id;
			$.report[reportNumber].button = 2;
			var that = $.report[reportNumber];
			// ログ情報の格納
			$.post(
				$.reg.easy ,
				{
					"page" : reportno ,
					"obj"  : id ,
					"sel": "json",
					"userid": $('#'+$.id.userid).val(),
					"user": $('#hiddenUser').val(),
					"report": $('#hiddenReport').val(),
					"json": JSON.stringify($.report[reportNumber].getJSONString())
				},
				function(json){}
			);

			var szSelYy		=	$.getJSONObject(that.jsonString, $.id.select_yy).value;		// 開始年
			var szSelMm		=	$.getJSONObject(that.jsonString, $.id.select_mm).value;		// 開始月

			var szSelYy2	=	$.getJSONObject(that.jsonString, $.id.select_yy2).value;	// 開始年（プロモ計画情報）
			var szSelMm2	=	$.getJSONObject(that.jsonString, $.id.select_mm2).value;	// 開始月（プロモ計画情報）

			// パラメータ取得
			var parm = '&report='+reportno
				+'&obj='+id
				+'&userid='+$('#'+$.id.userid).val()
				+'&TYPE='+$.getJSONObject(that.jsonString, $.id.radio_uptype).value
				+'&YYYYMM='+szSelYy+szSelMm
				+'&YYYYMM2='+szSelYy2+szSelMm2
			;
			var url = "../Servlet/Upload.do";
			window.parent.frames['blank'].open(url+'?ts='+(new Date()).getTime()+parm,'_self','width=400, height=300, menubar=no, toolbar=no, scrollbars=yes');
		},
		setResize: function(){					// （必須）リサイズ
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