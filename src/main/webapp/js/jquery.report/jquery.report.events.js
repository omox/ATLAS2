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
		if($($.id.button_search1).length==1){
			$($.id.button_search1).attr("disabled",true).on("click", $.pushSearch);
		}
		if($($.id.button_search2).length==1){
			$($.id.button_search2).attr("disabled",true).on("click", $.pushSearch);
		}
		if($($.id.button_search3).length==1){
			$($.id.button_search3).attr("disabled",true).on("click", $.pushSearch);
		}
		// リセットボタン
		if($("#" + $.id.object_reset).length==1){
			$("#" + $.id.object_reset).attr("disabled",true).click(function () { $.report[reportNumber].setDefaultValue(reportno);});
		}
		// 表示項目ボタン
		$($.id.button_item).attr("disabled",true);
	 	// Excel クリックイベント
		$($.id.button_excel).attr("disabled",true).on("click", $.pushExcel);

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