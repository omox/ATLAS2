/**
 * jquery report container
 * 商品出荷実績
 */

;(function($) {

	$.extend({
		reportOption: {
		name:		'ReportB_04',				// （必須）レポートオプションの確認
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
		initedObjCount: 57,
		initializes : true,
		initialize: function (reportno){		// （必須）初期化
			var that = this;
			// ツールバー初期化
			$($.id.toolbar).panel({
				//title: "",
				border: false,
				onBeforeCollapse:function(){
					// Load処理回避
					$.tryChangeURL(null);
				},
				onCollapse:function(){
					$("#collapse").hide();
					$("label[for='collapse']").hide();
					$("#expand").show();
					$("label[for='expand']").show();
					$("#"+$.id.area_report).show();
					that.setResize();
					// Load処理回避
					$.tryChangeURL($.reg.jqeasy);
				},
				onBeforeExpand:function(){
					// Load処理回避
					$.tryChangeURL(null);
				},
				onExpand:function(){
					$("#collapse").show();
					$("label[for='collapse']").show();
					$("#expand").hide();
					$("label[for='expand']").hide();
					$("#"+$.id.area_report).hide();
					that.setResize();
					// Load処理回避
					$.tryChangeURL($.reg.jqeasy);
				},
				tools:'#tools',
				collapsible:false
			});
			$("#collapse").click(function(){ $($.id.toolbar).panel("collapse");});
			$("#expand").click(function(){ $($.id.toolbar).panel("expand"); });

			// 引き継ぎ情報
			this.jsonHidden = $.getTargetValue();
			this.defaultValues = this.getDefaultValue(reportno);

			// レポート更新チェック
			this.setCheck(reportno, $.id.lbl_check);
			this.setRadBtn(reportno);
			this.setCmbBox(reportno);
			// 年月
			this.setYy(reportno, $.id.select_yy, $.id.select_yy2);
			this.setMm(reportno, $.id.select_mm, $.id.select_mm2);
			this.setDd(reportno, $.id.select_dd, $.id.select_dd2);
			// 新製品年月
			this.setYmN(reportno, $.id.select_ym_nr, $.id.select_ym_nr2);
			// チャネル選択
			this.setCha4(reportno, $.id.select_cha_4);
			this.setCha3(reportno, $.id.select_cha_3);
			this.setCha2(reportno, $.id.select_cha_2);
			this.setCha1(reportno, $.id.select_cha_1);
			// 商品分類選択
			this.setSho6(reportno, $.id.select_shos_6);
			this.setSho5(reportno, $.id.select_shos_5);
			this.setSho4(reportno, $.id.select_shos_4);
			this.setSho3(reportno, $.id.select_shos_3);
			this.setSho2(reportno, $.id.select_shos_2);
			this.setSho1(reportno, $.id.select_shos_1);
			// 選択
			this.setBun(reportno, $.id.select_bun);		// 一括
			this.setBun2(reportno, $.id.select_bs);		// 一括

			//this.initBunBlank(reportno);

			// データ表示エリア初期化
			that.setGrid($.id.gridholder, reportno);
			// Load処理回避
			$.tryChangeURL(null);
			// ツールバー表示
			$.tryShowToolbar($.id.toolbar, that);
			// 初期化終了
			this.initializes=!this.initializes;
			// ログ出力
			$.log(that.timeData, 'init initialize:');
		},
		validation: function (){				// （必須）批准
			var rt = true;
			this.message = "";
			/* 期間指定の入力チェック */
			if($("input[name="+ $.id.radio_b1 + "]:checked")[0].id == $.id.radio_b1_1){			// 照会データ指定-1:年別
				if(this.isFromTo($("#"+$.id.select_yy).val(), $("#"+$.id.select_yy2).val())){	// 日付大小
					this.message += $.message.ID_MESSAGE_INVARID_FROM_TO + "\n";
					rt = false;
				}
			}else if($("input[name="+ $.id.radio_b1 + "]:checked")[0].id == $.id.radio_b1_2){	// 照会データ指定-2:月別
				var f = $("#"+$.id.select_yy).val() + $("#"+$.id.select_mm).val();
				var t = $("#"+$.id.select_yy2).val() + $("#"+$.id.select_mm2).val();
				if(this.isFromTo(f, t)){															// 日付大小
					this.message += $.message.ID_MESSAGE_INVARID_FROM_TO + "\n";
					rt = false;
				}else if(this.isFromTo(t, $.exDate(f,'yyyymm').addMonths(36).toChar("yyyymm"))){	// 期間妥当性(36ヶ月以内)
					this.message += $.message.ID_MESSAGE_LONG_MONTH + "\n";
					rt = false;
				}
			}else if($("input[name="+ $.id.radio_b1 + "]:checked")[0].id == $.id.radio_b1_3){	// 照会データ指定-3:日別
				var f = $("#"+$.id.select_yy).val() + $("#"+$.id.select_mm).val()+ $("#"+$.id.select_dd).val();
				var t = $("#"+$.id.select_yy2).val() + $("#"+$.id.select_mm2).val()+ $("#"+$.id.select_dd2).val();
				if(this.isFromTo(f, t)){															// 日付大小
					this.message += $.message.ID_MESSAGE_INVARID_FROM_TO + "\n";
					rt = false;
				}else if($.exDate(f,'yyyymmdd').toChar('yyyymmdd')!=f){								// 開始日付妥当性
					this.message += $.message.ID_MESSAGE_INVARID_DATE + "\n";
					rt = false;
				}else if($.exDate(t,'yyyymmdd').toChar('yyyymmdd')!=t){								// 終了日付妥当性
					this.message += $.message.ID_MESSAGE_INVARID_DATE + "\n";
					rt = false;
				}else if(this.isFromTo(t, this.computeDate(f,99))){								// 期間妥当性(100日以内)
					this.message += "開始終了日付は100日の期間内で指定してください。" + "\n";
					rt = false;
				}
			}
			/* 全表示の入力チェック2013/4追加 */
			if($("input[name="+ $.id.radio_sho1 + "]:checked")[0].id == $.id.radio_sho1_1){			// 全表示の入力
				if($("input[name="+ $.id.radio_b7a + "]:checked")[0].id == $.id.radio_b7a_5){			// 店舗別
					var szChk_s7	=	$.getJSONObject(this.jsonString, $.id.chk_sl7).value;		// 商品集計-商品
					var szChk_c2	=	$.getJSONObject(this.jsonString, $.id.chk_cl2).value;		// チャネル集計-大分類
					if(szChk_s7=="on" & szChk_c2=="on" & $("#"+$.id.select_cha_1).val() =="20"& $("#"+$.id.select_cha_2).val() =="00"){															// 日付大小
						this.message += "表示件数が多くなります、条件を変更するか、実績のみを選択してください。\n";
						rt = false;
					}
				}
			}

			if(!rt){
				alert(this.message);
			}
			return rt;
		},
		success: function(reportno,sortable){			// （必須）正処理
			var that = this;
			// ツールバー非表示
			$($.id.toolbar).panel('collapse');
			// 検索実行
			var szSelYy		=	$.getJSONObject(this.jsonString, $.id.select_yy).value;		// 開始年
			var szSelMm		=	$.getJSONObject(this.jsonString, $.id.select_mm).value;		// 開始月
			var szSelDd		=	$.getJSONObject(this.jsonString, $.id.select_dd).value;		// 開始日
			var szSelYy2	=	$.getJSONObject(this.jsonString, $.id.select_yy2).value;	// 終了年
			var szSelMm2	=	$.getJSONObject(this.jsonString, $.id.select_mm2).value;	// 終了月
			var szSelDd2	=	$.getJSONObject(this.jsonString, $.id.select_dd2).value;	// 終了日
			var szSelYmN1	=	$.getJSONObject(this.jsonString, $.id.select_ym_nr).value;	// 新製品-開始年月
			var szSelYmN2	=	$.getJSONObject(this.jsonString, $.id.select_ym_nr2).value;	// 新製品-終了年月
			var szSu		=	$.getJSONObject(this.jsonString, $.id.select_htype2).value;	// 数/額指定
			var szZen		=	$.getJSONObject(this.jsonString, $.id.select_htype3).value;	// 対比表示
			var szNR		=	$.getJSONObject(this.jsonString, $.id.select_nr_typeS).value;	// リピート別集計-しない/する

			var szCha1		=	$.getJSONObject(this.jsonString, $.id.select_cha_1).value;	// チャネル分類-基本分類
			var szCha2		=	$.getJSONObject(this.jsonString, $.id.select_cha_2).value;	// チャネル分類-大分類
			var szCha3		=	$.getJSONObject(this.jsonString, $.id.select_cha_3).value;	// チャネル分類-中分類
			var szCha4		=	$.getJSONObject(this.jsonString, $.id.select_cha_4).value;	// チャネル分類-小分類
			var szSho1		=	$.getJSONObject(this.jsonString, $.id.select_shos_1).value;	// 商品分類-基本分類
			var szSho2		=	$.getJSONObject(this.jsonString, $.id.select_shos_2).value;	// 商品分類-大分類
			var szSho3		=	$.getJSONObject(this.jsonString, $.id.select_shos_3).value;	// 商品分類-中分類
			var szSho4		=	$.getJSONObject(this.jsonString, $.id.select_shos_4).value;	// 商品分類-小分類
			var szSho5		=	$.getJSONObject(this.jsonString, $.id.select_shos_5).value;	// 商品分類-シリーズＡ
			var szSho6		=	$.getJSONObject(this.jsonString, $.id.select_shos_6).value;	// 商品分類-シリーズＢ

			var szBun1		=	$.getJSONObject(this.jsonString, $.id.select_bun_1).value;	// 商品分類-分類2
			var szBun2		=	$.getJSONObject(this.jsonString, $.id.select_bun_2).value;	// 商品分類-分類2
			var szBun3		=	$.getJSONObject(this.jsonString, $.id.select_bun_3).value;	// 商品分類-分類2
			var szBun4		=	$.getJSONObject(this.jsonString, $.id.select_bun_4).value;	// 商品分類-分類2
			var szBun5		=	$.getJSONObject(this.jsonString, $.id.select_bun_5).value;	// 商品分類-分類2
			var szBun6		=	$.getJSONObject(this.jsonString, $.id.select_bun_6).value;	// 商品分類-分類2
			var szBun7		=	$.getJSONObject(this.jsonString, $.id.select_bun_7).value;	// 商品分類-分類2
			var szBun8		=	$.getJSONObject(this.jsonString, $.id.select_bun_8).value;	// 商品分類-分類2
			var szBun9		=	$.getJSONObject(this.jsonString, $.id.select_bun_9).value;	// 商品分類-分類2
			var szBun10		=	$.getJSONObject(this.jsonString, $.id.select_bun_10).value;	// 商品分類-分類2
			var szBS1		=	$.getJSONObject(this.jsonString, $.id.select_bs_1).value;	// 商品分類-分類2-1
			var szBS2		=	$.getJSONObject(this.jsonString, $.id.select_bs_2).value;	// 商品分類-分類2-1
			var szBS3		=	$.getJSONObject(this.jsonString, $.id.select_bs_3).value;	// 商品分類-分類2-1
			var szBS4		=	$.getJSONObject(this.jsonString, $.id.select_bs_4).value;	// 商品分類-分類2-1
			var szBS5		=	$.getJSONObject(this.jsonString, $.id.select_bs_5).value;	// 商品分類-分類2-1
			var szBS6		=	$.getJSONObject(this.jsonString, $.id.select_bs_6).value;	// 商品分類-分類2-1
			var szBS7		=	$.getJSONObject(this.jsonString, $.id.select_bs_7).value;	// 商品分類-分類2-1
			var szBS8		=	$.getJSONObject(this.jsonString, $.id.select_bs_8).value;	// 商品分類-分類2-1
			var szBS9		=	$.getJSONObject(this.jsonString, $.id.select_bs_9).value;	// 商品分類-分類2-1
			var szBS10		=	$.getJSONObject(this.jsonString, $.id.select_bs_10).value;	// 商品分類-分類2-1

			var szRadNen	=	$.getJSONObject(this.jsonString, $.id.radio_b1).value;		// 照会データ指定
			var szRad12		=	$.getJSONObject(this.jsonString, $.id.radio_b4).value;		// 月/12指定
			var szRadNewS	=	$.getJSONObject(this.jsonString, $.id.radio_b5).value;		// 新製品指定
			var szRadNr		=	$.getJSONObject(this.jsonString, $.id.radio_b6).value;		// 通販・リピート別集計-しない/する
			var szRadTenDai	=	$.getJSONObject(this.jsonString, $.id.radio_b7a).value;		// チャネル-店販大分類
			var szRadDsp	=	$.getJSONObject(this.jsonString, $.id.radio_sho1).value;	// 表示形式-全表示/実績のみ

			var szChkCha1	=	$.getJSONObject(this.jsonString, $.id.chk_cl1).value;		// チャネル集計-基本分類
			var szChkCha2	=	$.getJSONObject(this.jsonString, $.id.chk_cl2).value;		// チャネル集計-大分類
			var szChkCha3	=	$.getJSONObject(this.jsonString, $.id.chk_cl3).value;		// チャネル集計-中分類
			var szChkCha4	=	$.getJSONObject(this.jsonString, $.id.chk_cl4).value;		// チャネル集計-小分類
			var szChkSho1	=	$.getJSONObject(this.jsonString, $.id.chk_sl1).value;		// 商品集計-基本分類
			var szChkSho2	=	$.getJSONObject(this.jsonString, $.id.chk_sl2).value;		// 商品集計-大分類
			var szChkSho3	=	$.getJSONObject(this.jsonString, $.id.chk_sl3).value;		// 商品集計-中分類
			var szChkSho4	=	$.getJSONObject(this.jsonString, $.id.chk_sl4).value;		// 商品集計-小分類
			var szChkSho5	=	$.getJSONObject(this.jsonString, $.id.chk_sl5).value;		// 商品集計-シリーズＡ
			var szChkSho6	=	$.getJSONObject(this.jsonString, $.id.chk_sl6).value;		// 商品集計-シリーズＢ
			var szChkSho7	=	$.getJSONObject(this.jsonString, $.id.chk_sl7).value;		// 商品集計-商品

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
					USER_ID			:	$('#'+$.id.userid).val(),	// ユーザーID
					YYYY_YY			:	szSelYy,				// 開始年
					YYYY_MM			:	szSelMm,				// 開始月
					YYYY_DD			:	szSelDd,				// 開始日
					YYYY_YY2		:	szSelYy2,				// 終了年
					YYYY_MM2		:	szSelMm2,				// 終了月
					YYYY_DD2		:	szSelDd2,				// 終了日
					START_YM		:	szSelYmN1,				// 新製品-開始年月
					START_YM2		:	szSelYmN2,				// 新製品-終了年月
					SU_GA				:	szSu,					// 数/額指定
					TAIHI				:	szZen,					// 対比表示
					NR_TYPE			:	szNR,					// リピート別集計-しない/する

					SEL_CHA1		:	szCha1,					// チャネル分類-基本分類
					SEL_CHA2		:	szCha2,					// チャネル分類-大分類
					SEL_CHA3		:	szCha3,					// チャネル分類-中分類
					SEL_CHA4		:	szCha4,					// チャネル分類-小分類
					SEL_SHO1		:	szSho1,					// 商品分類-基本分類
					SEL_SHO2		:	szSho2,					// 商品分類-大分類
					SEL_SHO3		:	szSho3,					// 商品分類-中分類
					SEL_SHO4		:	szSho4,					// 商品分類-小分類
					SEL_SHO5		:	szSho5,					// 商品分類-シリーズＡ
					SEL_SHO6		:	szSho6,					// 商品分類-シリーズＢ
					SEL_BUN1		:	szBun1,					// 商品分類
					SEL_BUN2		:	szBun2,					// 商品分類
					SEL_BUN3		:	szBun3,					// 商品分類
					SEL_BUN4		:	szBun4,					// 商品分類
					SEL_BUN5		:	szBun5,					// 商品分類
					SEL_BUN6		:	szBun6,					// 商品分類
					SEL_BUN7		:	szBun7,					// 商品分類
					SEL_BUN8		:	szBun8,					// 商品分類
					SEL_BUN9		:	szBun9,					// 商品分類
					SEL_BUN10		:	szBun10,				// 商品分類
					SEL_BS1			:	szBS1,					// 商品分類
					SEL_BS2			:	szBS2,					// 商品分類
					SEL_BS3			:	szBS3,					// 商品分類
					SEL_BS4			:	szBS4,					// 商品分類
					SEL_BS5			:	szBS5,					// 商品分類
					SEL_BS6			:	szBS6,					// 商品分類
					SEL_BS7			:	szBS7,					// 商品分類
					SEL_BS8			:	szBS8,					// 商品分類
					SEL_BS9			:	szBS9,					// 商品分類
					SEL_BS10		:	szBS10,					// 商品分類
					RAD_NEN			:	szRadNen,				// 照会データ指定
					RAD_12			:	szRad12,				// 月/12指定
					RAD_NEW			:	szRadNewS,				// 新製品指定
					RAD_NR			:	szRadNr,				// 通販・リピート別集計-しない/する
					RAD_TEN_DAI		:	szRadTenDai,			// チャネル-店販大分類
					RAD_DSP_TYP		:	szRadDsp,				// 表示形式
					CHK_C1			:	szChkCha1,				// チャネル集計-基本分類
					CHK_C2			:	szChkCha2,				// チャネル集計-大分類
					CHK_C3			:	szChkCha3,				// チャネル集計-中分類
					CHK_C4			:	szChkCha4,				// チャネル集計-小分類
					CHK_S1			:	szChkSho1,				// 商品集計-基本分類
					CHK_S2			:	szChkSho2,				// 商品集計-大分類
					CHK_S3			:	szChkSho3,				// 商品集計-中分類
					CHK_S4			:	szChkSho4,				// 商品集計-小分類
					CHK_S5			:	szChkSho5,				// 商品集計-シリーズＡ
					CHK_S6			:	szChkSho6,				// 商品集計-シリーズＢ
					CHK_S7			:	szChkSho7,				// 商品集計-商品
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
							var column=[];
							var filed = 2;
							// 数値型列作成
							var num = json.rows.length - filed + 1;
							for (var index=0; index<num; index++){
								var tit = $.trim(json.rows[filed-1]);
								if(tit.indexOf("年計:数前年比")!=-1||tit.indexOf("年計:額前年比")!=-1){
									column.push({field:'F'+filed, title:tit, width: 100, resizable:true, align:'right',
										formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0.0');}});
								}else if(tit.indexOf("前年比")!=-1||tit.indexOf("前月比")!=-1){
									column.push({field:'F'+filed, title:tit, width: 60, resizable:true, align:'right',
										formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0.0');}});
								}else if(tit.indexOf("数")!=-1){
									column.push({field:'F'+filed, title:tit, width: 80, resizable:true, align:'right',
										formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0');}});
								}else if(tit.indexOf("額")!=-1){
									column.push({field:'F'+filed, title:tit, width: 100, resizable:true, align:'right',
										formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0');}});
								}else if(tit.indexOf("商品:シリーズB")!=-1){
									column.push({field:'F'+filed, title:tit, width: 120, resizable:true, align:'left'});
								}else if(tit.indexOf("番号")!=-1){
									column.push({field:'F'+filed, title:tit, width: 60, resizable:true, align:'left'});
								}else{
									var w =90;
									if(tit.indexOf("商品名称")!=-1){ w = 230; }
									column.push({field:'F'+filed, title:tit, width: w, resizable:true, align:'left'});
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

			// 照会条件 -----------------------------------------------------
			this.jsonString.push({
				id:		$.id.radio_b1,
				value:	$("input[name="+ $.id.radio_b1 + "]:checked").val(),
				text:	$.trim($("label[for='"+$("input:radio[name="+ $.id.radio_b1 + "]:checked").attr("id")+"']").text())
			});
			this.jsonString.push({
				id:		$.id.radio_sho2,
				value:	$("input[name="+ $.id.radio_sho2 + "]:checked").val(),
				text:	$.trim($("label[for='"+$("input:radio[name="+ $.id.radio_sho2 + "]:checked").attr("id")+"']").text())
			});
			this.jsonString.push({
				id:		$.id.radio_b4,
				value:	$("input[name="+ $.id.radio_b4 + "]:checked").val(),
				text:	$.trim($("label[for='"+$("input:radio[name="+ $.id.radio_b4 + "]:checked").attr("id")+"']").text())
			});
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
			this.jsonString.push({
				id:		$.id.select_yy2,
				value:	$('#'+$.id.select_yy2).val(),
				text:	$('#'+$.id.select_yy2+' option:selected').text()
			});
			this.jsonString.push({
				id:		$.id.select_mm2,
				value:	$('#'+$.id.select_mm2).val(),
				text:	$('#'+$.id.select_mm2+' option:selected').text()
			});
			this.jsonString.push({
				id:		$.id.select_dd2,
				value:	$('#'+$.id.select_dd2).val(),
				text:	$('#'+$.id.select_dd2+' option:selected').text()
			});
			this.jsonString.push({
				id:		$.id.select_htype2,
				value:	$('#'+$.id.select_htype2).val(),
				text:	$('#'+$.id.select_htype2+' option:selected').text()
			});
			this.jsonString.push({
				id:		$.id.select_htype3,
				value:	$('#'+$.id.select_htype3).val(),
				text:	$('#'+$.id.select_htype3+' option:selected').text()
			});
			this.jsonString.push({
				id:		$.id.select_nr_typeS,
				value:	"1",
				text:		"1"
			});
			// 新製品指定あり・なし------------------------------
			this.jsonString.push({
				id:		$.id.radio_b5,
				value:	$("input[name="+ $.id.radio_b5 + "]:checked").val(),
				text:	$.trim($("label[for='"+$("input:radio[name="+ $.id.radio_b5 + "]:checked").attr("id")+"']").text())
			});
			this.jsonString.push({
				id:		$.id.select_ym_nr,
				value:	$('#'+$.id.select_ym_nr).val(),
				text:	$('#'+$.id.select_ym_nr+' option:selected').text()
			});
			this.jsonString.push({
				id:		$.id.select_ym_nr2,
				value:	$('#'+$.id.select_ym_nr2).val(),
				text:	$('#'+$.id.select_ym_nr2+' option:selected').text()
			});
			// チャネル -----------------------------------------------------
			this.jsonString.push({
				id:		$.id.radio_b7a,
				value:	$("input[name="+ $.id.radio_b7a + "]:checked").val(),
				text:	$.trim($("label[for='"+$("input:radio[name="+ $.id.radio_b7a + "]:checked").attr("id")+"']").text())
			});
			this.jsonString.push({
				id:		$.id.select_cha_1,
				value:	$('#'+$.id.select_cha_1).val(),
				text:	$('#'+$.id.select_cha_1+' option:selected').text()
			});
			this.jsonString.push({
				id:		$.id.select_cha_2,
				value:	$('#'+$.id.select_cha_2).val(),
				text:	$('#'+$.id.select_cha_2+' option:selected').text()
			});
			this.jsonString.push({
				id:		$.id.select_cha_3,
				value:	$('#'+$.id.select_cha_3).val(),
				text:	$('#'+$.id.select_cha_3+' option:selected').text()
			});
			this.jsonString.push({
				id:		$.id.select_cha_4,
				value:	$('#'+$.id.select_cha_4).val(),
				text:	$('#'+$.id.select_cha_4+' option:selected').text()
			});
			this.jsonString.push({
				id:		$.id.chk_cl1,
				value:	$.getCheckBoxVal($.id.chk_cl1),
				text:	$.trim($('#'+$.id.chk_cl1).parent('label').text())
			});
			this.jsonString.push({
				id:		$.id.chk_cl2,
				value:	$.getCheckBoxVal($.id.chk_cl2),
				text:	$.trim($('#'+$.id.chk_cl2).parent('label').text())
			});
			this.jsonString.push({
				id:		$.id.chk_cl3,
				value:	$.getCheckBoxVal($.id.chk_cl3),
				text:	$.trim($('#'+$.id.chk_cl3).parent('label').text())
			});
			this.jsonString.push({
				id:		$.id.chk_cl4,
				value:	$.getCheckBoxVal($.id.chk_cl4),
				text:	$.trim($('#'+$.id.chk_cl4).parent('label').text())
			});
			// 新規・リピートするしない-------------------------------------
			this.jsonString.push({
				id:		$.id.radio_b6,
				value:	$("input[name="+ $.id.radio_b6 + "]:checked").val(),
				text:	$.trim($("label[for='"+$("input:radio[name="+ $.id.radio_b6 + "]:checked").attr("id")+"']").text())
			});
			// 商品 -----------------------------------------------------
			this.jsonString.push({
				id:		$.id.select_shos_1,
				value:	$('#'+$.id.select_shos_1).val(),
				text:	$('#'+$.id.select_shos_1+' option:selected').text()
			});
			this.jsonString.push({
				id:		$.id.select_shos_2,
				value:	$('#'+$.id.select_shos_2).val(),
				text:	$('#'+$.id.select_shos_2+' option:selected').text()
			});
			this.jsonString.push({
				id:		$.id.select_shos_3,
				value:	$('#'+$.id.select_shos_3).val(),
				text:	$('#'+$.id.select_shos_3+' option:selected').text()
			});
			this.jsonString.push({
				id:		$.id.select_shos_4,
				value:	$('#'+$.id.select_shos_4).val(),
				text:	$('#'+$.id.select_shos_4+' option:selected').text()
			});
			this.jsonString.push({
				id:		$.id.select_shos_5,
				value:	$('#'+$.id.select_shos_5).val(),
				text:	$('#'+$.id.select_shos_5+' option:selected').text()
			});
			this.jsonString.push({
				id:		$.id.select_shos_6,
				value:	$('#'+$.id.select_shos_6).val(),
				text:	$('#'+$.id.select_shos_6+' option:selected').text()
			});
			this.jsonString.push({
				id:		$.id.chk_sl1,
				value:	$.getCheckBoxVal($.id.chk_sl1),
				text:	$.trim($('#'+$.id.chk_sl1).parent('label').text())
			});
			this.jsonString.push({
				id:		$.id.chk_sl2,
				value:	$.getCheckBoxVal($.id.chk_sl2),
				text:	$.trim($('#'+$.id.chk_sl2).parent('label').text())
			});
			this.jsonString.push({
				id:		$.id.chk_sl3,
				value:	$.getCheckBoxVal($.id.chk_sl3),
				text:	$.trim($('#'+$.id.chk_sl3).parent('label').text())
			});
			this.jsonString.push({
				id:		$.id.chk_sl4,
				value:	$.getCheckBoxVal($.id.chk_sl4),
				text:	$.trim($('#'+$.id.chk_sl4).parent('label').text())
			});
			this.jsonString.push({
				id:		$.id.chk_sl5,
				value:	$.getCheckBoxVal($.id.chk_sl5),
				text:	$.trim($('#'+$.id.chk_sl5).parent('label').text())
			});
			this.jsonString.push({
				id:		$.id.chk_sl6,
				value:	$.getCheckBoxVal($.id.chk_sl6),
				text:	$.trim($('#'+$.id.chk_sl6).parent('label').text())
			});
			this.jsonString.push({
				id:		$.id.chk_sl7,
				value:	$.getCheckBoxVal($.id.chk_sl7),
				text:	$.trim($('#'+$.id.chk_sl7).parent('label').text())
			});
			// 商品 下　分類２-----------------------------------------------
			//
			for (var i=1; i<=10; i++){
				var objId = $.id.select_bun + "_" + i;
				this.jsonString.push({
					id:		objId,
					value:	$('#' + objId).val(),
					text:	$('#' + objId+' option:selected').text()
				});
			}
			for (var i=1; i<=10; i++){
				var objId = $.id.select_bs + "_" + i;
				this.jsonString.push({
					id:		objId,
					value:	$('#' + objId).val(),
					text:	$('#' + objId+' option:selected').text()
				});
			}

			// 表示形式-全表示/実績のみ
			this.jsonString.push({
				id:		$.id.radio_sho1,
				value:	$("input[name="+ $.id.radio_sho1 + "]:checked").val(),
				text:	$.trim($("label[for='"+$("input:radio[name="+ $.id.radio_sho1 + "]:checked").attr("id")+"']").text())
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
			// 初期イベント
			$("input[name='"+ $.id.radio_b1 +"']:checked").change();
			$("input[name='"+ $.id.radio_b5 +"']:checked").change();
			$("input[name='"+ $.id.radio_b6 +"']:checked").change();
			$("#"+$.id.chk_cl+"1").change();
			if ( ($("#"+$.id.select_bs_1).val()!="00")||($("#"+$.id.select_bs_2).val()!="00")||($("#"+$.id.select_bs_3).val()!="00")||($("#"+$.id.select_bs_4).val()!="00")||($("#"+$.id.select_bs_5).val()!="00")||($("#"+$.id.select_bs_6).val()!="00")||($("#"+$.id.select_bs_7).val()!="00")||($("#"+$.id.select_bs_8).val()!="00")||($("#"+$.id.select_bs_9).val()!="00")||($("#"+$.id.select_bs_10).val()!="00")) {
				var obj = $("select[name='select_bs'] option:selected[value!='00']").parent().prev();	// X-2選択中X-1
				var minVal = Math.max.apply(null,obj.find("option:selected").map( function() { return $(this).val()*1; }).get());
				if(minVal <= 2 ){
					$("#"+$.id.chk_sl1).attr("disabled",true);
					$("#"+$.id.chk_sl2).attr("disabled",true);
				} else if(minVal <= 3 ){
					$("#"+$.id.chk_sl1).attr("disabled",true);
					$("#"+$.id.chk_sl2).attr("disabled",true);
					$("#"+$.id.chk_sl3).attr("disabled",true);
				} else if(minVal <= 4 ){
					$("#"+$.id.chk_sl1).attr("disabled",true);
					$("#"+$.id.chk_sl2).attr("disabled",true);
					$("#"+$.id.chk_sl3).attr("disabled",true);
					$("#"+$.id.chk_sl4).attr("disabled",true);
				} else if(minVal <= 5 ){
					$("#"+$.id.chk_sl1).attr("disabled",true);
					$("#"+$.id.chk_sl2).attr("disabled",true);
					$("#"+$.id.chk_sl3).attr("disabled",true);
					$("#"+$.id.chk_sl4).attr("disabled",true);
					$("#"+$.id.chk_sl5).attr("disabled",true);
				} else if(minVal <= 6 ){
					$("#"+$.id.chk_sl1).attr("disabled",true);
					$("#"+$.id.chk_sl2).attr("disabled",true);
					$("#"+$.id.chk_sl3).attr("disabled",true);
					$("#"+$.id.chk_sl4).attr("disabled",true);
					$("#"+$.id.chk_sl5).attr("disabled",true);
					$("#"+$.id.chk_sl6).attr("disabled",true);
				}
			}
			$($.id.button_search).attr("disabled",false);
			$($.id.button_excel).attr("disabled",false);

			$('#'+$.id.select_cha_1).change();	// 照会条件	分類指定の初期化
			$('#'+$.id.select_shos_1).change();	// 商品			分類指定の初期化

			that.initedObject.push("triggerInitEvent");
			// ログ出力
			$.log(that.timeData, 'triggerInitEvent:');
			// リサイズ
			that.setResize();
		},
		getDefaultValue: function (reportno){
			// 初期化
			var json = [];
			$.setJSONObject(json,$.id.radio_b1, 4);		// 照会データ指定
			$.setJSONObject(json,$.id.radio_b4, 1);		// 月/12指定
			$.setJSONObject(json,$.id.radio_b5, 1);		// 新製品指定
			$.setJSONObject(json,$.id.radio_b6, 1);		// 新規・リピート別集計-しない/する
			$.setJSONObject(json,$.id.radio_b7a, 1);	// チャネル-店販大分類
			$.setJSONObject(json,$.id.radio_sho1, 2);	// 表示形式-全表示/実績のみ
			$.setJSONObject(json,$.id.chk_sl1, 1);		// 商品集計-基本分類
			return json;
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
		setRadBtn: function(reportno){		// ラジオボタン：照会データ指定/月/12指定/新製品指定/ 新規・リピート別集計/チャネル
			// 初期値設定
			$("input[name='"+$.id.radio_b1+"']").val([$.getFirstValue(this,$.id.radio_b1,'REF_DATA_RAD')]);			// 照会データ指定
			$("input[name='"+$.id.radio_b4+"']").val([$.getFirstValue(this,$.id.radio_b4,'MONTH_12_RAD')]);			// 月/12指定
			$("input[name='"+$.id.radio_b5+"']").val([$.getFirstValue(this,$.id.radio_b5,'NEW_PRODUCT_RAD')]);		// 新製品指定
			$("input[name='"+$.id.radio_b6+"']").val([$.getFirstValue(this,$.id.radio_b6,'TSU_REP_RAD')]);			// 新規・リピート別集計-しない/する
			$("input[name='"+$.id.radio_b7a+"']").val([$.getFirstValue(this,$.id.radio_b7a,'TENPO_DAI_RAD')]);		// チャネル-店販大分類
			$("input[name='"+$.id.radio_sho1+"']").val([$.getFirstValue(this,$.id.radio_sho1,'DISP_TYPE_RAD')]);	// 表示形式-全表示/実績のみ
			// 初期表示終了時にイベント実行
			if (this.initedObject.length == this.initedObjCount){
				this.triggerInitEvent();
			}
		},
		setCmbBox: function(reportno){		// 固定SELECT
			$("#"+$.id.select_htype2).val($.getFirstValue(this,$.id.select_htype2 ,'SU_GA_SEL'));			//  数/額指定
			$("#"+$.id.select_htype3).val($.getFirstValue(this,$.id.select_htype3 ,'TAIHI_SEL'));			// 対比表示
			//$("#"+$.id.select_nr_typeS).val($.getFirstValue(this,$.id.select_nr_typeS ,'NR_TYPE_SEL'));		// リピート別集計-しない/する
			// 初期表示終了時にイベント実行
			if (this.initedObject.length == this.initedObjCount){
				this.triggerInitEvent();
			}
		},
		setYy: function(reportno, id, id2){	// 基準年月
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
						obj1.val($.getFirstValue(that,id ,'START_YY_SEL'));
						obj2.val($.getFirstValue(that,id2,'END_YY_SEL'));
						// 初期表示終了時にイベント実行
						if (that.initedObject.length == that.initedObjCount){
							that.triggerInitEvent();
						}
					},
					"json"
			);
		},
		setMm: function(reportno, id, id2){	// 基準年月
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
						obj1.val($.getFirstValue(that,id ,'START_MM_SEL'));
						obj2.val($.getFirstValue(that,id2,'END_MM_SEL'));
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
							item.push('<option value="'+ data[i]['VALUE'] + '">' + data[i]['TEXT'] + '</option>');
						}
						obj1.append(item.join(''));
						obj2.append(item.join(''));
						// IE6 selected プロパティ エラー回避
						obj1.width();
						obj2.width();
						// 初期値設定
						obj1.val($.getFirstValue(that,id ,'START_DD_SEL'));
						obj2.val($.getFirstValue(that,id2,'END_DD_SEL'));
						// 初期表示終了時にイベント実行
						if (that.initedObject.length == that.initedObjCount){
							that.triggerInitEvent();
						}
					},
					"json"
			);
		},
		setYmN: function(reportno, id, id2){	// 新製品
			var that = this;
			var obj1 = $('#'+id );
			var obj2 = $('#'+id2);
			obj1.change(function(){
				obj2.val($(this).val());
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
						obj1.val($.getFirstValue(that,id ,'START_SYM_SEL'));
						obj2.val($.getFirstValue(that,id2,'END_SYM_SEL'));
						// 初期表示終了時にイベント実行
						if (that.initedObject.length == that.initedObjCount){
							that.triggerInitEvent();
						}
					},
					"json"
			);
		},
		setCha1: function(reportno, id){	// チャネル選択
			var that = this;
			var id2 = $.id.select_cha_2;
			var obj1 = $('#'+id );
			obj1.change(function(){
				if(obj1.val()==""){return;}
				// チャネル集計レベル
				$("#"+$.id.chk_cl+"1").attr('checked',obj1.val()!="00");
				// 初期化
				$('#'+id2).empty();
				$.post(
					$.reg.easy ,
					{
						"page"		:	reportno,
						"obj"		:	id2,
						"sel"		:	obj1.val(),
						"target"	:	$("input[name="+ $.id.radio_b7a + "]:checked").val(),
						"action"	:	$.id.action_init,
						"rowsOnly"	:	true
					},
					function(data){
						// オプション追加
						var item = new Array();
						for(var i=0; i < data.length; i++){
							item.push('<option value="'+ data[i]['VALUE'] + '">' + data[i]['TEXT'] + '</option>');
						}
						var obj2 = $('#'+id2);
						obj2.append(item.join(''));
						// IE6 selectedプロパティエラー,幅調整不具合回避
						if ($._ua.ltIE6){
							obj2.width();
						}
						// 初期値設定
						obj2.val($.getFirstValue(that,id2,'CHANEL_DAI_SEL')).change();
					},
					"json"
				);
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
						// オプション追加
						var item = new Array();
						for(var i=0; i < data.length; i++){
							item.push('<option value="'+ data[i]['VALUE'] + '">' + data[i]['TEXT'] + '</option>');
						}
						obj1.append(item.join(''));
						// IE6 selectedプロパティエラー,幅調整不具合回避
						obj1.width();
						// 初期値設定
						obj1.val($.getFirstValue(that,id ,'CHANEL_KIHON_SEL')).change();
					},
					"json"
			);
		},
		setCha2: function(reportno, id){	// チャネル選択
			var that = this;
			var id2 = $.id.select_cha_3;
			var obj1 = $('#'+id );
			obj1.change(function(){
				if(obj1.val()==""){return;}
				// チャネル集計レベル
				$("#"+$.id.chk_cl+"2").attr('checked',obj1.val()!="00");
				// 初期化
				$('#'+id2).empty();
				$.post(
					$.reg.easy ,
					{
						"page"		:	reportno,
						"obj"		:	id2,
						"sel"		:	$("#"+$.id.select_cha_1).val()+$(this).val(),
						"target"	:	$("input[name="+ $.id.radio_b7a + "]:checked").val(),
						"action"	:	$.id.action_init,
						"rowsOnly"	:	true
					},
					function(data){
						// オプション追加
						var item = new Array();
						for(var i=0; i < data.length; i++){
							item.push('<option value="'+ data[i]['VALUE'] + '">' + data[i]['TEXT'] + '</option>');
						}
						var obj2 = $('#'+id2);
						obj2.append(item.join(''));
						// IE6 selectedプロパティエラー,幅調整不具合回避
						if ($._ua.ltIE6){
							obj2.width();
						}
						// 初期値設定
						obj2.val($.getFirstValue(that,id2,'CHANEL_TYU_SEL')).change();
					},
					"json"
				);
			}
			);
		},
		setCha3: function(reportno, id){	// チャネル選択
			var that = this;
			var id2 = $.id.select_cha_4;
			var obj1 = $('#'+id );
			obj1.change(function(){
				if(obj1.val()==""){return;}
				// チャネル集計レベル
				$("#"+$.id.chk_cl+"3").attr('checked',obj1.val()!="00");
				// 初期化
				$('#'+id2).empty();
				$.post(
					$.reg.easy ,
					{
						"page"		:	reportno,
						"obj"		:	id2,
						"sel"		:	$("#"+$.id.select_cha_1).val()+$("#"+$.id.select_cha_2).val()+$(this).val(),
						"target"	:	$("input[name="+ $.id.radio_b7a + "]:checked").val(),
						"action"	:	$.id.action_init,
						"rowsOnly"	:	true
					},
					function(data){
						// オプション追加
						var item = new Array();
						for(var i=0; i < data.length; i++){
							item.push('<option value="'+ data[i]['VALUE'] + '">' + data[i]['TEXT'] + '</option>');
						}
						var obj2 = $('#'+id2);
						obj2.append(item.join(''));
						// IE6 selectedプロパティエラー,幅調整不具合回避
						if ($._ua.ltIE6){
							obj2.width();
						}
						// 初期値設定
						obj2.val($.getFirstValue(that,id2,'CHANEL_SHO_SEL')).change();
					},
					"json"
				);
			}
			);
		},
		setCha4: function(reportno, id){	// チャネル選択
			var that = this;
			var obj = $('#'+id );
			obj.change(function(){
					if(obj.val()==""){return;}
					// チャネル集計レベル
					$("#"+$.id.chk_cl+"4").attr('checked',obj.val()!="00");
					// チャネル集計初期値設定
					if ($.inArray($.id.chk_cl1, that.initedObject) < 0){
						that.setChaChk(reportno);
					}else{
						$("#"+$.id.chk_cl+"1").change();
					}
				}
			);
		},
		setChaChk:function(reportno){	// チャネル集計
			var that = this;
			// 初期値設定(連動対応)
			$("#"+$.id.chk_cl1).attr('checked',$.getFirstValue(that,$.id.chk_cl1,'CHANEL_KIHON_CHK')==1);	// チャネル集計-基本分類
			$("#"+$.id.chk_cl2).attr('checked',$.getFirstValue(that,$.id.chk_cl2,'CHANEL_DAI_CHK')==1);		// チャネル集計-大分類
			$("#"+$.id.chk_cl3).attr('checked',$.getFirstValue(that,$.id.chk_cl3,'CHANEL_TYU_CHK')==1);		// チャネル集計-中分類
			$("#"+$.id.chk_cl4).attr('checked',$.getFirstValue(that,$.id.chk_cl4,'CHANEL_SHO_CHK')==1);		// チャネル集計-小分類
			// 初期表示終了時にイベント実行
			if (that.initedObject.length == that.initedObjCount){
				that.triggerInitEvent();
			}
		},
		setSho1: function(reportno, id){		// 基本分類
			var that = this;
			var id2 = $.id.select_shos_2;
			var obj1 = $('#'+id );
			obj1.change(function(){
					if($(this).val()==""){return;}
					// 商品集計レベル‐基本分類
					$("#"+$.id.chk_sl+"1").attr('checked',true);
					$("#"+$.id.chk_sl+"1").attr('disabled',true);
					// 商品集計レベル‐商品
					$("#"+$.id.chk_sl+"7").attr('checked',false);
					$("#"+$.id.chk_sl+"7").attr('disabled',$(this).val()=="00");
					// 初期化
					$('#'+id2).empty();
					$.post(
							$.reg.easy ,
							{
								"page"		:	reportno,
								"obj"		:	id2,
								"sel"		:	$(this).val(),
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
								var obj2 = $('#'+id2);
								obj2.append(item.join(''));
								// IE6 selectedプロパティエラー,幅調整不具合回避
								if ($._ua.ltIE6){
									obj2.width();
								}
								// 初期値設定
								obj2.val($.getFirstValue(that,id2,'SHOHIN_DAI_SEL'));
								obj2.change();
							},
							"json"
					);
					// 商品分類-条件X-1 初期化
					if($.inArray($.id.select_bun_1, that.initedObject) >= 0){	// 初回はSho6で実行
						if ($('#'+$.id.select_shos_1).val()=="00"){
							that.initBunBlank(reportno);
						}
					}
				}
			);
			$.post(
					$.reg.easy ,
					{
						"page"		:	reportno,
						"obj"		:	id,
						"sel"		:	"",
						"target"	:	id,
						"action"	:	$.id.action_init,
						"rowsOnly"	:	true
					},
					function(data){
						// オプション追加
						var item = new Array();
						for(var i=0; i < data.length; i++){
							item.push('<option value="'+ data[i]['VALUE'] + '">' + data[i]['TEXT'] + '</option>');
						}
						obj1.append(item.join(''));
						// IE6 selectedプロパティエラー,幅調整不具合回避
						obj1.width();
						// 初期値設定
						obj1.val($.getFirstValue(that,id ,'SHOHIN_KIHON_SEL'));
						obj1.change();
					},
					"json"
			);
		},
		setSho2: function(reportno, id){		//  大分類
			var that = this;
			var id2 = $.id.select_shos_3;
			var obj1 = $('#'+id );
			obj1.change(function(){
					if(obj1.val()==""){return;}
					// 商品集計レベル - 大分類
					$("#"+$.id.chk_sl+"2").attr('checked',obj1.val()!="00");
					$("#"+$.id.chk_sl+"2").attr('disabled',obj1.val()!="00");
					// 初期化
					$('#'+id2).empty();
					$.post(
							$.reg.easy ,
							{
								"page"		:	reportno,
								"obj"		:	id2,
								"sel"		:	$(this).val(),
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
								var obj2 = $('#'+id2);
								obj2.append(item.join(''));
								// IE6 selectedプロパティエラー,幅調整不具合回避
								if ($._ua.ltIE6){
									obj2.width();
								}
								// 初期値設定
								obj2.val($.getFirstValue(that,id2,'SHOHIN_TYU_SEL'));
								obj2.change();
							},
							"json"
					);
					// 商品分類-条件X-1 初期化
					if($.inArray($.id.select_bun_1, that.initedObject) >= 0){	// 初回はSho6で実行
						that.initBun(reportno, id);
					}
				}
			);
		},
		setSho3: function(reportno, id){		// 中分類
			var that = this;
			var obj1 = $('#'+id );
			var id2 = $.id.select_shos_4;
			obj1.change(function(){
					if(obj1.val()==""){return;}
					// 商品集計レベル - 中分類
					$("#"+$.id.chk_sl+"3").attr('checked',obj1.val()!="00");
					$("#"+$.id.chk_sl+"3").attr('disabled',obj1.val()!="00");
					// 初期化
					$('#'+id2).empty();
					$.post(
							$.reg.easy ,
							{
								"page"		:	reportno,
								"obj"		:	id2,
								"sel"		:	$(this).val(),
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
								var obj2 = $('#'+id2);
								obj2.append(item.join(''));
								// IE6 selectedプロパティエラー,幅調整不具合回避
								if ($._ua.ltIE6){
									obj2.width();
								}
								// 初期値設定
								obj2.val($.getFirstValue(that,id2,'SHOHIN_SHO_SEL'));
								obj2.change();
							},
							"json"
					);
					// 商品分類-条件X-1 初期化
					if ($.inArray($.id.select_bun_1, that.initedObject) >= 0){	// 初回はSho6で実行
						that.initBun(reportno, id);
					}
				}
			);
		},
		setSho4: function(reportno, id){		// 小分類
			var that = this;
			var id2 = $.id.select_shos_5;
			$('#'+id ).change(function(){
					var obj1 = $('#'+id );
					if(obj1.val()==""){return;}
					// 商品集計レベル - 小分類
					$("#"+$.id.chk_sl+"4").attr('checked',obj1.val()!="00");
					$("#"+$.id.chk_sl+"4").attr('disabled',obj1.val()!="00");
					// 初期化
					$('#'+id2).empty();
					$.post(
							$.reg.easy ,
							{
								"page"		:	reportno,
								"obj"		:	id2,
								"sel"		:	$(this).val(),
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
								var obj2 = $('#'+id2);
								obj2.append(item.join(''));
								// IE6 selectedプロパティエラー,幅調整不具合回避
								if ($._ua.ltIE6){
									obj2.width();
								}
								// 初期値設定
								obj2.val($.getFirstValue(that,id2,'SHOHIN_A_SEL'));
								obj2.change();
							},
							"json"
					);
					// 商品分類-条件X-1 初期化
					if ($.inArray($.id.select_bun_1, that.initedObject) >= 0){	// 初回はSho6で実行
						that.initBun(reportno, id);
					}
				}
			);
		},
		setSho5: function(reportno, id){		// シリーズA
			var that = this;
			var id2 = $.id.select_shos_6;
			$('#'+id ).change(function(){
					var obj1 = $('#'+id );
					if(obj1.val()==""){return;}
					// 商品集計レベル - シリーズA
					$("#"+$.id.chk_sl+"5").attr('checked',obj1.val()!="00");
					$("#"+$.id.chk_sl+"5").attr('disabled',obj1.val()!="00");
					// 初期化
					$('#'+id2).empty();
					$.post(
							$.reg.easy ,
							{
								"page"		:	reportno,
								"obj"		:	id2,
								"sel"		:	$(this).val(),
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
								var obj2 = $('#'+id2);
								obj2.append(item.join(''));
								// IE6 selectedプロパティエラー,幅調整不具合回避
								if ($._ua.ltIE6){
									obj2.width();
								}
								// 初期値設定
								obj2.val($.getFirstValue(that,id2,'SHOHIN_B_SEL'));
								obj2.change();
							},
							"json"
					);
					// 商品分類-条件X-1 初期化
					if ($.inArray($.id.select_bun_1, that.initedObject) >= 0){	// 初回はSho6で実行
						that.initBun(reportno, id);
					}
				}
			);
		},
		setSho6: function(reportno, id){		// シリーズB
			var that = this;
			$('#'+id ).change(function(e){
					var obj = $('#'+id );
					if(obj.val()==""){return;}
					// 商品集計レベル - シリーズB
					$("#"+$.id.chk_sl+"6").attr('checked',obj.val()!="00");
					$("#"+$.id.chk_sl+"6").attr('disabled',obj.val()!="00");

					// 商品集計初期値設定
					if ($.inArray($.id.chk_sl1, that.initedObject) < 0){
						that.setShoChk(reportno, $.id.chk_cl);
					}

					// 商品分類-条件X-1 初期化
					if ($.inArray($.id.select_bun_1, that.initedObject) < 0){	// 初回処理
						if($('#'+$.id.select_shos_1).val()!="00" ){
							that.initBun(reportno, null);
						} else {
							that.initBunBlank(reportno);
						}
					} else if (obj.val()=='00'){
						that.initBun(reportno, id);
					} else {
						that.initBunBlank(reportno);
					}
				}
			);
		},
		setShoChk:function(reportno, id){	// 商品集計
			var that = this;
			// 初期値設定
			$("#"+$.id.chk_sl1).attr('checked',$.getFirstValue(that,$.id.chk_sl1,'SHOHIN_KIHON_CHK')==1);	// 商品集計-基本分類
			$("#"+$.id.chk_sl2).attr('checked',$.getFirstValue(that,$.id.chk_sl2,'SHOHIN_DAI_CHK')==1);		// 商品集計-大分類
			$("#"+$.id.chk_sl3).attr('checked',$.getFirstValue(that,$.id.chk_sl3,'SHOHIN_TYU_CHK')==1);		// 商品集計-中分類
			$("#"+$.id.chk_sl4).attr('checked',$.getFirstValue(that,$.id.chk_sl4,'SHOHIN_SHO_CHK')==1);		// 商品集計-小分類
			$("#"+$.id.chk_sl5).attr('checked',$.getFirstValue(that,$.id.chk_sl5,'SHOHIN_A_CHK')==1);		// 商品集計-シリーズＡ
			$("#"+$.id.chk_sl6).attr('checked',$.getFirstValue(that,$.id.chk_sl6,'SHOHIN_B_CHK')==1);		// 商品集計-シリーズＢ
			$("#"+$.id.chk_sl7).attr('checked',$.getFirstValue(that,$.id.chk_sl7,'SHOHIN_SHOHIN_CHK')==1);	// 商品集計-商品
			// 初期表示終了時にイベント実行
			if (that.initedObject.length == that.initedObjCount){
				that.triggerInitEvent();
			}
		},
		initBun: function(reportno,id){		// 商品分類-初期化イベント設定
			var that = this;
			var obj1 = $("select[name='"+$.id.select_bun+"']");			// 商品分類-条件X-1
			// 初期検索以外で再作成しないパターン
			if(id!=null &&($('#'+$.id.select_shos_1).val()=="00" || $('#'+id).val()!="00" || $('#'+id).children().length<=1)){
				return;
			}
			// 商品分類の選択オプション要素('すべて'選択以外)取得
			var sel = $("select[name='"+$.id.select_shos+"'] :selected:not([value='00'])").length;
			$.post(
					$.reg.easy ,
					{
						"page"		:	reportno,
						"obj"		:	$.id.select_bun_1,
						"sel"		:	sel,
						"target"	:	$.id.select_bun_1,
						"action"	:	$.id.action_init,
						"rowsOnly"	:	true
					},
					function(data){
						var item = new Array();
						for(var i=0; i < data.length; i++){
							item.push('<option value="'+ data[i]['VALUE'] + '">' + data[i]['TEXT'] + '</option>');
						}
						obj1.empty().append(item.join(''));
						// IE6 selectedプロパティエラー,幅調整不具合回避
						obj1.width();
						// 初期値設定
						if($.inArray($.id.select_bun_1, that.initedObject) < 0){
							for(var i=0; i < obj1.length; i++){
								var idx = obj1[i].id.replace($.id.select_bun + "_", "");
								$(obj1[i]).val($.trim($.getFirstValue(that,obj1[i].id,'BUN' + idx + "_SEL")));
							}
						}
						obj1.change();
					},
					"json"
			);
		},
		initBunBlank: function(reportno){		// 商品分類-初期化イベント設定
			var that = this;
			var obj1 = $("select[name='"+$.id.select_bun+"']");			// 商品分類-条件X-1
			var obj2 = $("select[name='"+$.id.select_bs+"']");		// 商品分類-条件X-2

			if(obj1.eq(0).val()=='0'){ return; }

			obj1.empty().append('<option value="0" selected></option>');
			obj2.empty().append('<option value="00" selected></option>');
			// IE6 selectedプロパティエラー,幅調整不具合回避
			obj1.width();
			obj2.width();
			// 初期値設定
			if($.inArray($.id.select_bun_1, that.initedObject) < 0){
				for(var i=0; i < obj1.length; i++){
					var idx = obj1[i].id.replace($.id.select_bun + "_", "");
					$(obj1[i]).val($.getFirstValue(that,obj1[i].id,'BUN' + idx + "_SEL"));
					$(obj2[i]).val($.getFirstValue(that,obj2[i].id,'BUNS' + idx + "_SEL"));
				}
				// 初期表示終了時にイベント実行
				if (that.initedObject.length == that.initedObjCount){
					that.triggerInitEvent();
				}
			}
			// obj2.change();
		},
		setBun: function(reportno, nm){		// 商品分類-条件X-1 イベント設定
			var that = this;
			$("select[name='"+nm+"']").change(function(e){
					var id = e.target.id;
					// 商品分類-条件X-2 初期化
					var idx = id.replace(nm,"").replace("_","");
					var id2 = $.id.select_bs + "_" + idx;
					$('#'+id2).empty();
					// 商品分類の選択オプション要素('すべて'選択以外)の末尾取得
					var sho = $("select[name='"+$.id.select_shos+"'] :selected:not([value='00']):last");
					$.post(
							$.reg.easy ,
							{
								"page"		:	reportno,
								"obj"		:	$.id.select_bs_1,
								"sel"		:	sho.val(),
								"target"	:	$('#'+id).val(),
								"action"	:	$.id.action_init,
								"rowsOnly"	:	true
							},
							function(data){
								var item = new Array();
								for(var i=0; i < data.length; i++){
									item.push('<option value="'+ data[i]['VALUE'] + '">' + data[i]['TEXT'] + '</option>');
								}
								var obj = $('#'+id2);
								obj.append(item.join(''));
								//obj.width();	// IE6 selectedプロパティエラー,幅調整不具合回避
								if ($._ua.ltIE6){
									obj.width();
								}
								// 初期値設定
								// obj.val($.trim($.getFirstValue(that,id2,'BUNS' + idx + '_SEL'))).change();
								obj.val($.trim($.getFirstValue(that,id2,'BUNS' + idx + '_SEL')));
								// 初期表示終了時にイベント実行
								if (that.initedObject.length == that.initedObjCount){
									that.triggerInitEvent();
								}
							},
							"json"
					);
				}
			);
		},
		setBun2: function(reportno, nm){		// 商品分類-条件X-2 イベント設定
			var that = this;
			$("select[name='"+nm+"']").change(function(e){
					var obj = $("select[name='" + nm + "'] option:selected[value!='00']").parent().prev();	// X-2選択中X-1
					var sel = $("select[name='" + $.id.select_shos + "']");
					if(obj.length==0){
						var minVal = Math.max.apply(null,obj.find("option:selected").map( function() { return $(this).val()*1; }).get());
						sel.not("[id='" + $.id.select_shos_1 +"']").each( function() {
							var idx = $(this).attr("id").replace($.id.select_shos+"_","");
							if(idx*1 <= minVal){
								$(this).attr("disabled",false);
								$("#"+ $.id.chk_sl + idx).attr("disabled",false);
							}else{
								//$(that).attr("disabled",false);
								$("#"+ $.id.chk_sl + idx).attr("disabled",$(this).val()!="00");
							}
						});
					}else{
						var minVal = Math.max.apply(null,obj.find("option:selected").map( function() { return $(this).val()*1; }).get());
						sel.not("[id='" + $.id.select_shos_1 +"']").each( function() {
							var idx = $(this).attr("id").replace($.id.select_shos+"_","");
							if(idx*1 <= minVal){
								$(this).attr("disabled",false);
								$("#"+ $.id.chk_sl + idx).attr("disabled",true);
							}else{
								//$(that).attr("disabled",false);
								$("#"+ $.id.chk_sl + idx).attr("disabled",$(this).val()!="00");
							}
						});
					}
					// 初期表示終了時にイベント実行
					if (that.initedObject.length == that.initedObjCount){
						that.triggerInitEvent();
					}
				}
			);
		},
		setGrid: function (id, reportNumber){	// グリッドの構築
		 	var that = this;
			$(id).datagrid({
				nowrap: true,
				border: false,
				striped: true,
				collapsible:true,
				remoteSort: true,
				idField:'F1',
				view:scrollview,
				pageSize:40,
				fitColumns:false,	// 指定カラム幅を適用する場合、false 指定。
				frozenColumns:[[
					{field:'F1',title:'SORT',width:10,hidden:true}
				]],
				columns:[[
					{field:'F2',  title:' ', width:85,resizable:true, align:'right'}
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
					if ($.trim(rowData.F1).indexOf('合計')!=-1){
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
		computeDate:function(ymd, addDays){
			var dt = $.exDate(ymd,'yyyymmdd');
			var addSec = addDays * 86400000;//日数 * 1日のミリ秒数
			var targetSec = dt.getTime() + addSec;
			dt.setTime(targetSec);
			return dt.toChar('yyyymmdd');
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