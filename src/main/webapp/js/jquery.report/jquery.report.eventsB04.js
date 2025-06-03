/**
 * jQuery : 1.6.2
 * レポート情報の初期化と共通イベント定義
 * 個別レポートに対応したイベントを作成する場合は、別ファイルに定義してください。
 */
$(function(){

	/**
	 * 初期化
	 * 事前読込 jquery.report.nn.js , jquery.report.control.js
	 * @param {Object} $.reportOption
	 */
	(function() {

		// レポートオプションの設定
		$.report($.reportOption);

		// レポート番号取得
		var reportno = $($.id.hidden_reportno).val();

		// レポート定義位置
		var reportNumber = $.getReportNumber(reportno);
		if (typeof(reportNumber) !== 'number') { alert("レポート定義が見つかりません。"); return false;}

		// initialize
		if(typeof $.report[reportNumber].initialize != "function") return true;
		$.report[reportNumber].initialize(reportno);

		// 	コールバック関数の紐付け
		// 検索  クリックイベント
		$($.id.button_search).attr("disabled",true).on("click", $.pushSearch);
	 	// Excel クリックイベント
		$($.id.button_excel).attr("disabled",true).on("click", $.pushExcel);
		// リセットボタン
		$("#" + $.id.object_reset).click(function () {
			$("#"+$.id.select_shos_1).val("00").change();
		});

		// 照会データ指定-1:年別 2:月別 3:日別 4:基本形 イベント
		$('input[name="' + $.id.radio_b1 + '"]').change(function(){
			var val =$(this).val();
			$("#"+$.id.select_mm ).attr('disabled',val==1);
			$("#"+$.id.select_dd ).attr('disabled',val!=3);
			$("#"+$.id.select_yy2).attr('disabled',val==4);
			$("#"+$.id.select_mm2).attr('disabled',(val==1||val==4));
			$("#"+$.id.select_dd2).attr('disabled',val!=3);

			$('#'+$.id.radio_b4_1).attr("disabled",val!=2);
			$('#'+$.id.radio_b4_2).attr("disabled",val!=2);

			// 数/額指定SELECTのoption表示
			var typ2 = $('#'+$.id.select_htype2 + " :selected").val();
			var itm2 = '<option value="1">数量</option><option value="2">金額（税抜）</option>';
			if(val==4){ itm2 = '<option value="2">金額（税抜）</option>';}
			var obj2 = $('#'+$.id.select_htype2);
			obj2.empty();
			obj2.append(itm2);
			//obj2.width();	// IE6 selectedプロパティエラー,幅調整不具合回避
			if ($._ua.ltIE6){
				obj2.width();
			}
			obj2.val(typ2);

			// 対比SELECTのoption表示
			var typ3 = $('#'+$.id.select_htype3 + " :selected").val();
			var itm3 = '<option value="1">なし</option><option value="2">前年比</option><option value="3">前月比</option>';
			if(val==1){
				itm3 = '<option value="1">なし</option><option value="2">前年比</option>';
			}else if(val==2){
				$('input[name="' + $.id.radio_b4 + '"]:checked').change();
				return;
			}else if(val==3){
				itm3 = '<option value="1">なし</option>';
			}else if(val==4){
				itm3 = '<option value="2">前年比</option><option value="3">前月比</option>';
			}
			var obj3 = $('#'+$.id.select_htype3);
			obj3.empty();
			obj3.append(itm3);
			//obj3.width();	// IE6 selectedプロパティエラー,幅調整不具合回避
			if ($._ua.ltIE6){
				obj3.width();
			}
			obj3.val(typ3);
		});

		// 月12指定-1:単月 2:12点移動和り イベント
		$('input[name="' + $.id.radio_b4 + '"]').change(function(){
			var val =$(this).val();
			var typ3 = $('#'+$.id.select_htype3 + " :selected").val();
			var itm3 = '<option value="1">なし</option><option value="2">前年比</option><option value="3">前月比</option>';
			if(val==2){
				itm3 = '<option value="1">なし</option>';
			}
			var obj = $('#'+$.id.select_htype3);
			obj.empty();
			obj.append(itm3);
			//obj.width();	// IE6 selectedプロパティエラー,幅調整不具合回避
			if ($._ua.ltIE6){
				obj.width();
			}
			obj.val(typ3);
		});
		// 新製品指定-1:なし 2:あり イベント
		$('input[name="' + $.id.radio_b5 + '"]').change(function(){
			var val =$(this).val();
			$('#'+$.id.select_ym_nr ).attr("disabled",val==1);
			$('#'+$.id.select_ym_nr2).attr("disabled",val==1);
		});

		// 新リピート別-1:しない 2:する イベント
		$('input[name="' + $.id.radio_b6 + '"]').change(function(){
			var val =$(this).val();
			$('#'+$.id.select_nr_typeS ).attr("disabled",val==1);
		});

		//店舗分類区分のイベント
		$('input[name="' + $.id.radio_b7a + '"]').change(function(){
			$("#"+$.id.select_cha_1).val("00");			// チャネル分類-基本分類
			$("#"+$.id.chk_cl2).attr("checked",false);
			$("#"+$.id.chk_cl3).attr("checked",false);
			$("#"+$.id.chk_cl4).attr("checked",false);
			$("#"+$.id.select_cha_1).change();
		});

		// チャネル集計チェック
		$("#"+$.id.chk_cl1).change(function(){
			if($('#'+$.id.select_cha_1).val()!="00"){
				$(this).attr("disabled", true).attr("checked", true);
			} else {
				$(this).attr("disabled", false).attr("checked", $(this).is(":checked"));
			}
			$('#'+$.id.chk_cl2).change();
		});
		$("#"+$.id.chk_cl2).change(function(){
			if($('#'+$.id.select_cha_2).val()!="00"){
				$(this).attr("disabled", true).attr("checked", true);
			}else if($("#"+$.id.chk_cl1).is(":checked")){
				$(this).attr("disabled", false).attr("checked", $(this).is(":checked"));
			}else{
				$(this).attr("disabled", true).attr("checked", false);
			}
			$('#'+$.id.chk_cl3).change();
		});
		$("#"+$.id.chk_cl3).change(function(){
			if($('#'+$.id.select_cha_3).val()!="00"){
				$(this).attr("disabled", true).attr("checked", true);
			}else if($("#"+$.id.chk_cl2).is(":checked")){
				$(this).attr("disabled", false).attr("checked", $(this).is(":checked"));
			}else{
				$(this).attr("disabled", true).attr("checked", false);
			}
			$('#'+$.id.chk_cl4).change();
		});
		$("#"+$.id.chk_cl4).change(function(){
			if($('#'+$.id.select_cha_4).val()!="00"){
				$(this).attr("disabled", true).attr("checked", true);
			}else if($("#"+$.id.chk_cl3).is(":checked")){
				$(this).attr("disabled", false).attr("checked", $(this).is(":checked"));
			}else{
				$(this).attr("disabled", true).attr("checked", false);
			}
		});
	})();

	/**
	 * resize Event
	 */
	var ResizeWindows=function(){
		// レポート番号取得
		var reportno=$($.id.hidden_reportno).val();

		// レポート定義位置
		var reportNumber = $.getReportNumber(reportno);
		if (typeof(reportNumber) !== 'number') { alert("レポート定義が見つかりません。"); return false;}

		// リサイズ処理
		$.report[reportNumber].setResize();
	};
	var resizeTimer = null;
	$(window).on('resize', function() {
		if (resizeTimer) clearTimeout(resizeTimer);
		resizeTimer = setTimeout(ResizeWindows, 200);
	});
});