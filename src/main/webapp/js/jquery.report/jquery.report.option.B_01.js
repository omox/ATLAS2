/**
 * jquery report container
 * 全社売上_詳細
 */

;(function($) {
	$.extend({
		reportOption: {
		name:		'ReportB_01',				// （必須）レポートオプションの確認
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
		initedObjCount: 21,
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

			this.setCheck(reportno, $.id.lbl_check);				// レポート更新チェック
			this.setRadBtn(reportno);								// ラジオボタン：照会データ指定/対比表示/表示対象
			this.setChkBox(reportno);								// チェックボックス：チャネル区分/表示項目
			this.setKanren(reportno, $.id.select_kaigai);			// 関連会社
			this.setYushutu(reportno, $.id.select_yushu);			// 輸出先
			this.setItaku(reportno, $.id.select_itaku);				// 他社売上
			this.setBrand(reportno, $.id.select_brand);				// 他ブランド
			this.setYy(reportno, $.id.select_yy, $.id.select_yy2);	// 開始年-終了年
			this.setMm(reportno, $.id.select_mm, $.id.select_mm2);	// 開始月-終了月
			this.setTenpo(reportno, $.id.select_tenpo);				// 店舗
			this.setJuhan(reportno, $.id.select_juhan);				// 従販

			// データ表示エリア初期化
			that.setGrid($.id.gridholder, reportno);

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
				if(this.isFromTo($("#"+$.id.select_yy).val() + $("#"+$.id.select_mm).val(), $("#"+$.id.select_yy2).val() + $("#"+$.id.select_mm2).val())){
					this.message += $.message.ID_MESSAGE_INVARID_FROM_TO + "\n";
					rt = false;
				}
			}
			/* チャネルの入力チェック */
			if ($("input[name="+ $.id.radio_b3 + "]:checked")[0].id == $.id.radio_b3_1){
				if ($("input[name=" + $.id.chk_b1 + "]:checked").length == 0){
					this.message += $.message.ID_MESSAGE_WARNING_BLANK_CHANEL + "\n";
					rt = false;
				}
			}
			/* 輸出の入力チェック */
			//if ($("input[name="+ $.id.radio_b3 + "]:checked")[0].id == $.id.radio_b3_1){
			//alert($("#"+$.id.chk_b1_3).checked);
			//if ( $.id.chk_b1_3.checked == true){
			//	alert("checked");
			//		this.message += $.message.ID_MESSAGE_WARNING_BLANK_CHANEL + "\n";
			//		rt = false;
			//}
			//}
			/* 純売上照会時の期間チェック */
			if (this.button == 1){
				if(this.isFromTo("200601",$("#"+$.id.select_yy).val() + $("#"+$.id.select_mm).val())){
					this.message += $.message.ID_MESSAGE_WARNING_FROM_JUNURI + "\n";
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
			var szJiseki		=	this.button;												// 純売上・出荷売上区分
			var szSelYy			=	$.getJSONObject(this.jsonString, $.id.select_yy).value;		// 開始年
			var szSelMm			=	$.getJSONObject(this.jsonString, $.id.select_mm).value;		// 開始月
			var szSelYy2		=	$.getJSONObject(this.jsonString, $.id.select_yy2).value;	// 終了年
			var szSelMm2		=	$.getJSONObject(this.jsonString, $.id.select_mm2).value;	// 終了月
			var szSelTenpo		=	$.getJSONObject(this.jsonString, $.id.select_tenpo).value;	// 店舗
			var szSelYushu		=	$.getJSONObject(this.jsonString, $.id.select_yushu).value;	// 輸出先コード
			var szSelYushuNm	=	$.getJSONObject(this.jsonString, $.id.select_yushu).text;	// 輸出先名称
			var szSelBrand		=	$.getJSONObject(this.jsonString, $.id.select_brand).value;	// 他ブランドコード
			var szSelBrandNm	=	$.getJSONObject(this.jsonString, $.id.select_brand).text;	// 他ブランド名称
			var szSelItaku		=	$.getJSONObject(this.jsonString, $.id.select_itaku).value;	// 他社売上コード
			var szSelItakuNm	=	$.getJSONObject(this.jsonString, $.id.select_itaku).text;	// 他社売上名称
			var szSelKaigai		=	$.getJSONObject(this.jsonString, $.id.select_kaigai).value;	// 関連会社コード
			var szSelKaigaiNm	=	$.getJSONObject(this.jsonString, $.id.select_kaigai).text;	// 関連会社名称
			var szSelJuhan		=	$.getJSONObject(this.jsonString, $.id.select_juhan).value;	// 従販コード
			var szSelJuhanNm	=	$.getJSONObject(this.jsonString, $.id.select_juhan).text;	// 従販名称
			var szRadB1			=	$.getJSONObject(this.jsonString, $.id.radio_b1).value;		// 照会データ指定-1:年別 2:月別 3:日別
			var szRadB2			=	$.getJSONObject(this.jsonString, $.id.radio_b2).value;		// 対比表示-1:前年比 2:前月比
			var szRadB3			=	$.getJSONObject(this.jsonString, $.id.radio_b3).value;		// 表示対象-1:オルビス 2:関連会社 3:連結
			var szChkB1_1		=	$.getJSONObject(this.jsonString, $.id.chk_b1_1).value;		// チャネル区分-通販
			var szChkB1_2		=	$.getJSONObject(this.jsonString, $.id.chk_b1_2).value;		// チャネル区分-店舗
			var szChkB1_3		=	$.getJSONObject(this.jsonString, $.id.chk_b1_3).value;		// チャネル区分-輸出
			var szChkB1_4		=	$.getJSONObject(this.jsonString, $.id.chk_b1_4).value;		// チャネル区分-従販
			var szChkB1_5		=	$.getJSONObject(this.jsonString, $.id.chk_b1_5).value;		// チャネル区分-他社売上
			var szChkB1_6		=	$.getJSONObject(this.jsonString, $.id.chk_b1_6).value;		// チャネル区分-他ブランド
			var szSelItemJun	=	$.getJSONObject(this.jsonString, $.id.item_jun).value;		// 表示項目-純売上
			var szSelItemShu	=	$.getJSONObject(this.jsonString, $.id.item_shu).value;		// 表示項目-出荷売上

			// 処理時間計測用
			that.timeData = (new Date()).getTime();
			$($.id.gridholder).datagrid('loading');

			// 各擬似ウインドウ選択値ラベル表示
			$("#"+$.id.lbl_yushu + " span:last").text(szSelYushuNm);
			$("#"+$.id.lbl_brand + " span:last").text(szSelBrandNm);
			$("#"+$.id.lbl_brand + " span:last").text(szSelBrandNm);
			$("#"+$.id.lbl_itaku + " span:last").text(szSelItakuNm);
			$("#"+$.id.lbl_kaigai+ " span:last").text(szSelKaigaiNm);
			$('#'+$.id.lbl_yushu).toggle(!$('#'+$.id.select_yushu).attr('disabled')&&(szSelYushu != ""));
			$('#'+$.id.lbl_brand).toggle(!$('#'+$.id.select_brand).attr('disabled')&&(szSelBrandNm != ""));
			$('#'+$.id.lbl_itaku).toggle(!$('#'+$.id.select_itaku).attr('disabled')&&(szSelItakuNm != ""));
			$('#'+$.id.lbl_kaigai).toggle(szRadB3==2&&(szSelKaigaiNm != ""));

			// grid.options 取得
			var options = $($.id.gridholder).datagrid('options');
			if (sortable) sortable=1; else sortable=0;
			that.sortName	= options.sortName;
			that.sortOrder	= options.sortOrder;
			$.getJSON(
				$.reg.jqgrid ,
				{
					report			:	that.name,		// レポート名
					USER_ID			:	$('#'+$.id.userid).val(),	// ユーザーID
					YY_KIJUN		:	szSelYy,					// 開始年
					YY_KIJUN_TO		:	szSelYy2,					// 終了年
					MM_KIJUN		:	szSelMm,					// 開始月
					MM_KIJUN_TO		:	szSelMm2,					// 終了月
					TENPO			:	szSelTenpo,					// 店舗
					YUSHUTU			:	szSelYushu,					// 輸出コード
					YUSHUTU_NM		:	szSelYushuNm,				// 輸出名称
					BRAND			:	szSelBrand,					// 他ブランドコード
					BRAND_NM		:	szSelBrandNm,				// 他ブランド名称
					ITAKU			:	szSelItaku,					// 他社売上コード
					ITAKU_NM		:	szSelItakuNm,				// 他社売上名称
					KAIGAI			:	szSelKaigai,				// 関連会社コード
					KAIGAI_NM		:	szSelKaigaiNm,				// 関連会社名称
					JUHAN			:	szSelJuhan,					// 従販コード
					JUHAN_NM		:	szSelJuhanNm,				// 従販名称
					JISEKI			:	szJiseki,					// 純売上・出荷売上区分-純売上
					RAD_B1			:	szRadB1,					// 照会データ指定-1:年別 2:月別 3:日別
					RAD_B2			:	szRadB2,					// 対比表示-1:前年比 2:前月比
					RAD_B3			:	szRadB3,					// 表示対象-1:オルビス 2:関連会社 3:連結
					CHK_B1			:	szChkB1_1,					// チャネル区分-通販
					CHK_B2			:	szChkB1_2,					// チャネル区分-店舗
					CHK_B3			:	szChkB1_3,					// チャネル区分-輸出
					CHK_B4			:	szChkB1_4,					// チャネル区分-従販
					CHK_B5			:	szChkB1_5,					// チャネル区分-他社売上
					CHK_B6			:	szChkB1_6,					// チャネル区分-他ブランド
					ITEM_JUN		:	szSelItemJun,				// 表示項目-純売上
					ITEM_SHU		:	szSelItemShu,				// 表示項目-出荷売上
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
						if(json!=undefined && json.rows != undefined && json.opts != undefined){
							var column=[];
							var filed = 3;
							// 数値型列作成
							var num = json.rows.length - filed + 1;
							for (var index=0; index<num; index++){
								var tit = json.rows[filed-1];
								if($.inArray(tit, ['曜日'])!=-1){
									column.push({field:'F'+filed, title:tit, width: 40, resizable:true, align:'center',
										styler:function(value,rowData,rowIndex){
											if(value.indexOf("日")!=-1){return 'background:#F3F3F3;color: red';}
											if(value.indexOf("補")!=-1){return 'background:#F3F3F3;color: blue;';}
											return 'background:#F3F3F3;';}});
								}else if($.inArray(tit, ['前年','前月'])!=-1){
									column.push({field:'F'+filed, title:tit, width: 50, resizable:true, align:'center',
										styler:function(value,rowData,rowIndex){return 'background:#F3F3F3;';}});
								}else if($.inArray(tit, ['前年比','前月比','達成率'])!=-1){
									column.push({field:'F'+filed, title:tit, width: 50, resizable:true, align:'right',
										formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0.0');}});
								}else if($.inArray(tit, ['単価','出荷単価','前年単価','前月単価'])!=-1){
									column.push({field:'F'+filed, title:tit, width: 60, resizable:true, align:'right',
										formatter:function(value,rowData,rowIndex){
											if(value===undefined){
												return '';
											}else{
												return '￥'+$.getFormat(value,'#,##0');
											}
										}
									});

								}else if($.inArray(tit, ['出荷件数','純売上'])!=-1){
									column.push({field:'F'+filed, title:tit, width: 80, resizable:true, align:'right',
										formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0');}});
								}else if($.inArray(tit, ['件数','出荷件数','前年件数','前月件数'])!=-1){
									column.push({field:'F'+filed, title:tit, width: 80, resizable:true, align:'right',
										formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0');}});
								}else if($.inArray(tit, ['送料','返品送料'])!=-1){
									column.push({field:'F'+filed, title:tit, width: 95, resizable:true, align:'right',
										formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0');}});
								}else if($.inArray(tit, ['割引'])!=-1){
									column.push({field:'F'+filed, title:tit, width: 95, resizable:true, align:'right',
										formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0');}});
								}else if($.inArray(tit, ['割引合計'])!=-1){
									column.push({field:'F'+filed, title:tit, width: 95, resizable:true, align:'right',
										formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0');}});
								}else if($.inArray(tit, ['純売上','前年純売上','前月純売上'])!=-1){
									column.push({field:'F'+filed, title:tit, width: 100, resizable:true, align:'right',
										formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0');}});
								}else if($.inArray(tit, ['出荷売上','上代売上','上代－割引','上代総売上','上代売上'])!=-1){
									column.push({field:'F'+filed, title:tit, width: 100, resizable:true, align:'right',
										formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0');}});
								}else if($.inArray(tit, ['純売上累計','前年純売上累計','前月純売上累計','前年出荷売上','前月出荷売上','出荷売上累計','前年出荷売上累計','前月出荷売上累計','上代売上合計'])!=-1){
									column.push({field:'F'+filed, title:tit, width: 110, resizable:true, align:'right',
										formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0');}});
								}else{
									column.push({field:'F'+filed, title:tit, width:90, resizable:true, align:'right',
										formatter:function(value,rowData,rowIndex){return $.getFormat(value,'#,##0');}});
								}
								filed++;
							}
							var columns = [];
							columns.push(column);
							$($.id.gridholder).datagrid({ columns:columns });

							// 売上目標合計値設定
							if(json.opts[0]["売上目標"]!=undefined){
								$("#"+$.id.lbl_yosan).val($.getFormat(json.opts[0]["売上目標"],"#,##0"));
							}
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

			// 開始/終了年月指定 --------------------------------------------
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
				id:		$.id.select_yy2,
				value:	$('#'+$.id.select_yy2).val(),
				text:	$('#'+$.id.select_yy2+' option:selected').text()
			});
			this.jsonString.push({
				id:		$.id.select_mm2,
				value:	$('#'+$.id.select_mm2).val(),
				text:	$('#'+$.id.select_mm2+' option:selected').text()
			});
			// 表示対象-----------------------------------------------------
			this.jsonString.push({
				id:		$.id.select_tenpo,
				value:	$('#'+$.id.select_tenpo).val(),
				text:	$('#'+$.id.select_tenpo+' option:selected').text()
			});
			this.jsonString.push({
				id:		$.id.select_juhan,
				value:	$('#'+$.id.select_juhan).val(),
				text:	$('#'+$.id.select_juhan+' option:selected').text()
			});
			this.jsonString.push({
				id:		$.id.select_kaigai,
				value:	$("input[name="+ $.id.select_kaigai + "]:checked").map(function(){return $(this).val();}).get().join(","),
				text:	$("input[name="+ $.id.select_kaigai + "]:checked").map(function(){return $(this).parent('label').text();}).get().join(",")
			});
			this.jsonString.push({
				id:		$.id.select_yushu,
				value:	$("input[name="+ $.id.select_yushu + "]:checked").map(function(){return $(this).val();}).get().join(","),
				text:	$("input[name="+ $.id.select_yushu + "]:checked").map(function(){return $(this).parent('label').text();}).get().join(",")
			});
			this.jsonString.push({
				id:		$.id.select_brand,
				value:	$("input[name="+ $.id.select_brand + "]:checked").map(function(){return $(this).val();}).get().join(","),
				text:	$("input[name="+ $.id.select_brand + "]:checked").map(function(){return $(this).parent('label').text();}).get().join(",")
			});
			this.jsonString.push({
				id:		$.id.select_itaku,
				value:	$("input[name="+ $.id.select_itaku + "]:checked").map(function(){return $(this).val();}).get().join(","),
				text:	$("input[name="+ $.id.select_itaku + "]:checked").map(function(){return $(this).parent('label').text();}).get().join(",")
			});
			// ラジオボタン-----------------------------------------------------
			this.jsonString.push({
				id:		$.id.radio_b1,
				value:	$("input[name="+ $.id.radio_b1 + "]:checked").val(),
				text:	$.trim($("label[for='"+$("input:radio[name="+ $.id.radio_b1 + "]:checked").attr("id")+"']").text())
			});
			this.jsonString.push({
				id:		$.id.radio_b2,
				value:	$("input[name="+ $.id.radio_b2 + "]:checked").val(),
				text:	$.trim($("label[for='"+$("input:radio[name="+ $.id.radio_b2 + "]:checked").attr("id")+"']").text())
			});
			this.jsonString.push({
				id:		$.id.radio_b3,
				value:	$("input[name="+ $.id.radio_b3 + "]:checked").val(),
				text:	$.trim($("label[for='"+$("input:radio[name="+ $.id.radio_b3 + "]:checked").attr("id")+"']").text())
			});
			// チェックボックス-----------------------------------------------------
			this.jsonString.push({
				id:		$.id.chk_b1_1,
				value:	$.getCheckBoxVal($.id.chk_b1_1),
				text:	$.trim($('#'+$.id.chk_b1_1).parent('label').text())
			});
			this.jsonString.push({
				id:		$.id.chk_b1_2,
				value:	$.getCheckBoxVal($.id.chk_b1_2),
				text:	$.trim($('#'+$.id.chk_b1_2).parent('label').text())
			});
			this.jsonString.push({
				id:		$.id.chk_b1_3,
				value:	$.getCheckBoxVal($.id.chk_b1_3),
				text:	$.trim($('#'+$.id.chk_b1_3).parent('label').text())
			});
			this.jsonString.push({
				id:		$.id.chk_b1_4,
				value:	$.getCheckBoxVal($.id.chk_b1_4),
				text:	$.trim($('#'+$.id.chk_b1_4).parent('label').text())
			});
			this.jsonString.push({
				id:		$.id.chk_b1_5,
				value:	$.getCheckBoxVal($.id.chk_b1_5),
				text:	$.trim($('#'+$.id.chk_b1_5).parent('label').text())
			});
			this.jsonString.push({
				id:		$.id.chk_b1_6,
				value:	$.getCheckBoxVal($.id.chk_b1_6),
				text:	$.trim($('#'+$.id.chk_b1_6).parent('label').text())
			});
			// 表示項目
			this.jsonString.push({
				id:		$.id.item_jun,
				value:	$('#' + $.id.item_jun).find("input").map(function(){return $(this).is(":checked")?1:0;}).get().join(","),
				text:	$('#' + $.id.item_jun).find("input").map(function(){return $(this).parent('label').text();}).get().join(",")
			});
			this.jsonString.push({
				id:		$.id.item_shu,
				value:	$('#' + $.id.item_shu).find("input").map(function(){return $(this).is(":checked")?1:0;}).get().join(","),
				text:	$('#' + $.id.item_shu).find("input").map(function(){return $(this).parent('label').text();}).get().join(",")
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
			} else {
				// 各種イベント実行
				$("input[name='"+ $.id.radio_b1 +"']:checked").change();
				$("input[name='"+ $.id.radio_b2 +"']:checked").change();
				$("input[name='"+ $.id.radio_b3 +"']:checked").change();
			}
			$($.id.button_search).attr("disabled",false);
			$($.id.button_search2).attr("disabled",false);
			$($.id.button_excel).attr("disabled",false);
			$($.id.button_item).attr("disabled",false);
			$("#" + $.id.object_reset).attr("disabled",false);
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
			$.setJSONObject(json,$.id.select_yy2,dt.substr(0, 4));		// 開始月
			$.setJSONObject(json,$.id.select_mm2,dt.substr(4));			// 終了月
			$.setJSONObject(json,$.id.select_tenpo,'00');				// 店舗
			$.setJSONObject(json,$.id.select_juhan,'00');				// 従販
			$.setJSONObject(json,$.id.radio_b1,3);						// 照会データ指定-1:年別 2:月別 3:日別
			$.setJSONObject(json,$.id.radio_b2,1);						// 対比表示-1:前年比 2:前月比
			$.setJSONObject(json,$.id.radio_b3,1);						// 表示対象-1:オルビス 2:関連会社 3:連結
			return json;
		},
		setDefaultValue: function (reportno){
			// コンボボックス
			$("#"+$.id.select_yy).val($.getJSONValue(this.defaultValues, $.id.select_yy));						// 開始年
			$("#"+$.id.select_mm).val($.getJSONValue(this.defaultValues, $.id.select_mm));						// 開始月
			$("#"+$.id.select_yy2).val($.getJSONValue(this.defaultValues, $.id.select_yy2));					// 終了年
			$("#"+$.id.select_mm2).val($.getJSONValue(this.defaultValues, $.id.select_mm2));					// 終了月
			$("#"+$.id.select_tenpo).val($.getJSONValue(this.defaultValues, $.id.select_tenpo));				// 店舗
			$("#"+$.id.select_juhan).val($.getJSONValue(this.defaultValues, $.id.select_juhan));				// 従販
			// ラジオボタン
			$("input[name='"+ $.id.radio_b1 +"']").val([$.getJSONValue(this.defaultValues, $.id.radio_b1)]);	// 照会データ指定-1:年別 2:月別 3:日別
			$("input[name='"+ $.id.radio_b2 +"']").val([$.getJSONValue(this.defaultValues, $.id.radio_b2)]);	// 対比表示-1:前年比 2:前月比
			$("input[name='"+ $.id.radio_b3 +"']").val([$.getJSONValue(this.defaultValues, $.id.radio_b3)]);	// 表示対象-1:オルビス 2:関連会社 3:連結
			// チェックボックス
			$("input[name='"+ $.id.chk_b1 +"']").attr('checked',true);					// チャネル区分
			$("input[name='"+ $.id.select_yushu +"']").attr('checked',true);			// 輸出先
			$("input[name='"+ $.id.select_brand +"']").attr('checked',true);			// 他ブランド
			$("input[name='"+ $.id.select_itaku +"']").attr('checked',true);			// 他社売上
			$("input[name='"+ $.id.select_kaigai +"']").attr('checked',true);			// 関連会社
			$('#' + $.id.item_jun).find("input").attr('checked',true);					// 表示項目-純売上
			$('#' + $.id.item_shu).find("input").attr('checked',true);					// 表示項目-出荷-売上
			// 各種イベント実行
			$("input[name='"+ $.id.radio_b1 +"']:checked").change();
			$("input[name='"+ $.id.radio_b2 +"']:checked").change();
			$("input[name='"+ $.id.radio_b3 +"']:checked").change();
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
								$($.id.button_search2).attr("style","visibility: hidden;");
								$($.id.button_excel).attr("style","visibility: hidden;");
								$('#'+id).attr("style","color: #ff0000;");
							}
						}
					},
					"json"
			);
		},
		setRadBtn: function(reportno){		// ラジオボタン：照会データ指定/対比表示/表示対象
			// 初期値設定
			$("input[name='"+ $.id.radio_b1 +"']").val([$.getFirstValue(this,$.id.radio_b1 ,'REF_DATA_RAD')]);		// 照会データ指定-1:年別 2:月別 3:日別
			$("input[name='"+ $.id.radio_b2 +"']").val([$.getFirstValue(this,$.id.radio_b2 ,'VIEW_CMP_RAD')]);		// 対比表示-1:前年比 2:前月比
			$("input[name='"+ $.id.radio_b3 +"']").val([$.getFirstValue(this,$.id.radio_b3 ,'VIEW_OBJ_RAD')]);		// 表示対象-1:オルビス 2:関連会社 3:連結

			// イベント設定
			$('input[name="' + $.id.radio_b1 + '"]').change(function(){
				var val =$(this).val();
				$("#"+$.id.select_mm).attr('disabled',val==1);
				$("#"+$.id.select_yy2).attr('disabled',val==3);
				$("#"+$.id.select_mm2).attr('disabled',val!=2);
				if(val==1){$("#"+$.id.radio_b2_1).click();}
				$("#"+$.id.radio_b2_2).attr('disabled',val==1);
			});
			$('input[name="' + $.id.radio_b3 + '"]').change(function(){
				var val =$(this).val();
				$('#' + $.id.radio_b3 + 'Area' + ' input[name="' + $.id.chk_b1 + '"]').attr('disabled',val!=1);
				$('#'+$.id.chk_b1_2).change();
				$('#'+$.id.chk_b1_3).change();
				$('#'+$.id.chk_b1_4).change();
				$('#'+$.id.chk_b1_5).change();
				$('#'+$.id.chk_b1_6).change();
				$('#' + $.id.select_kaigai).attr('disabled',val!=2);
			});
			// 初期表示終了時にイベント実行
			if (this.initedObject.length == this.initedObjCount){
				this.triggerInitEvent();
			}
		},
		setChkBox: function(reportno){		// チェックボックス：チャネル区分/表示項目
			// 初期値設定
			$("#"+$.id.chk_b1_1).attr('checked',$.getFirstValue(this,$.id.chk_b1_1 ,'CHANEL_TSUHAN_CHK')!=0);		// チャネル区分-通販
			$("#"+$.id.chk_b1_2).attr('checked',$.getFirstValue(this,$.id.chk_b1_2 ,'CHANEL_TENPO_CHK')!=0);		// チャネル区分-店舗
			$("#"+$.id.chk_b1_3).attr('checked',$.getFirstValue(this,$.id.chk_b1_3 ,'CHANEL_YUSHUTU_CHK')!=0);		// チャネル区分-輸出
			$("#"+$.id.chk_b1_4).attr('checked',$.getFirstValue(this,$.id.chk_b1_4 ,'CHANEL_JUHAN_CHK')!=0);		// チャネル区分-従販
			$("#"+$.id.chk_b1_5).attr('checked',$.getFirstValue(this,$.id.chk_b1_5 ,'CHANEL_TASYAURI_CHK')!=0);		// チャネル区分-他社売上
			$("#"+$.id.chk_b1_6).attr('checked',$.getFirstValue(this,$.id.chk_b1_6 ,'CHANEL_TABRAND_CHK')!=0);		// チャネル区分-他ブランド
			var chkVals1 = $.convSelectVal($.getFirstValue(this,$.id.item_jun,'VIEW_ITEM_JUN'),true);				// 表示項目-純売上
			var chkVals2 = $.convSelectVal($.getFirstValue(this,$.id.item_shu,'VIEW_ITEM_SHU'),true);				// 表示項目-出荷-売上
			$.Columns.init(chkVals1, chkVals2);

			// チェンジイベント設定
			$('#'+$.id.chk_b1_2).change(function(){		// チャネル区分-店舗
				$('#'+$.id.select_tenpo).attr('disabled',!$(this).is(':checked')||!$('#'+$.id.radio_b3_1).is(':checked'));
			});
			$('#'+$.id.chk_b1_3).change(function(){		// チャネル区分-輸出
				$('#'+$.id.select_yushu).attr('disabled',!$(this).is(':checked')||!$('#'+$.id.radio_b3_1).is(':checked'));
			});
			$('#'+$.id.chk_b1_4).change(function(){		// チャネル区分-従販
				$('#'+$.id.select_juhan).attr('disabled',!$(this).is(':checked')||!$('#'+$.id.radio_b3_1).is(':checked'));
			});
			$('#'+$.id.chk_b1_5).change(function(){		// チャネル区分-他社売上
				$('#'+$.id.select_itaku).attr('disabled',!$(this).is(':checked')||!$('#'+$.id.radio_b3_1).is(':checked'));
			});
			$('#'+$.id.chk_b1_6).change(function(){		// チャネル区分-他ブランド
				$('#'+$.id.select_brand).attr('disabled',!$(this).is(':checked')||!$('#'+$.id.radio_b3_1).is(':checked'));
			});
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
		setTenpo: function(reportno, id){	//
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
						if ($._ua.ltIE6){
							obj1.width();
						}
						$.getFirstValue(that,id ,'TENPO_SEL');	// selected エラー回避のため遅延処理用に追加
						// 初期値設定
						obj1.val($.getFirstValue(that,id ,'TENPO_SEL'));
						// 初期表示終了時にイベント実行
						if (that.initedObject.length == that.initedObjCount){
							that.triggerInitEvent();
						}
					},
					"json"
			);
		},
		setJuhan: function(reportno, id){	//
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
						obj1.val($.getFirstValue(that,id,'CD_JUHAN_DETAIL'));
						// 初期表示終了時にイベント実行
						if (that.initedObject.length == that.initedObjCount){
							that.triggerInitEvent();
						}
					},
					"json"
			);
		},
		setKanren: function(reportno, id){
			$.setChkBoxWindow(reportno, id, "kaigai", "関連会社", 210, 400, 150, 8, this, 'CD_KANREN_DETAIL');
			//$.setChkBoxWindowS(reportno, id, "kaigai", "関連会社", this, 'CD_KANREN_DETAIL');
		},
		setYushutu: function(reportno, id){
			$.setChkBoxWindow(reportno, id, "yushutu", "輸出先", 210, 400, 150, 8, this, 'CD_YUSHUTU_DETAIL');
		},
		setItaku: function(reportno, id){
			$.setChkBoxWindow(reportno, id, "itaku", "他社売上", 210, 400, 150, 8, this, 'CD_ITAKU_DETAIL');
		},
		setBrand: function(reportno, id){
			$.setChkBoxWindowS(reportno, id, "brand", "他ブランド", this, 'CD_TABRAND_DETAIL');
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
					{field:'F1', title:'SORT', width:10,sortable:false,hidden:true},
					{field:'F2', title:'日付', width:50,sortable:false,align:'center',
						styler:function(value,rowData,rowIndex){
							if(value.indexOf("日")!=-1){return 'background:#F3F3F3;color: red;width:50px;';}
							if(value.indexOf("補")!=-1){return 'background:#F3F3F3;color: blue;width:50px;';}
							return 'background:#F3F3F3;width:50px;';}}
				]],
				columns:[[
					{field:'F3', title:' ',width:70,sortable:false, resizable:true, align:'right',
						formatter:function(value,rowData,rowIndex){ return $.getFormat(value,'#,##0');}}
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
		onClickButton: function (id){
			$.Columns.Open();
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