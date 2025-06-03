/**
 * 表示年変更
 */
$("#selectyear").on("change",function(){

	// ダッシュボード初期化
	initDashboard($("#selectyear").val(), $("#selectmonth").val());

});

/**
 * 表示月変更
 */
$("#selectmonth").on("change",function(){

	// ダッシュボード初期化
	initDashboard($("#selectyear").val(), $("#selectmonth").val());

});

/**
 * タブクリック
 */
$(".cp_tab > input[type='radio']").on("click",function(){
	var $ip = $(this);
	onChangeTab($ip);
});

/**
 * 数字の3桁カンマ区切り
 * @param num
 * @param fix 少数点
 * @returns
 */
function separate(num,fix){
	if (num){
	  return (num*1).toFixed(fix).replace( /(\d)(?=(\d\d\d)+(?!\d))/g, '$1,');
	}else{
		return '';
	}
}
/**
 * ダブ切替初期化
 */
function initTab(){
	// 初期化リセット
	onInitTab1 = true;
	onInitTab2 = true;
	onInitTab3 = true;

	// タブ切替処理
	var $ip = $(".cp_tab > input[type='radio']:checked");
	onChangeTab($ip);
}
/**
 * タブ切替処理
 * @param $ip
 */
function onChangeTab($ip){

	switch(Number($ip.val())){
	case 1:	// 国内売上
		onShowTab1();
		break;
	case 2:	// 商品実績
		onShowTab2();
		break;
	case 3:	// KPI
		onShowTab3();
		break;
	}
	// 「メニューはこちら」フォーカス移動
	$('#callmenu').focus();

};


/**
 * タブ更新：国内売上
 */
function onShowTab1(){

	$('#tab1').hide();
	$('#tab2').hide();
	$('#tab3').hide();

	if (db.length===0){
		return;
	}

	if (onInitTab1){

		// 初期値設定
		for(i=0;i<db.length;i++){
			// 101:国内純売上,104:国内購入者,107:国内顧客単価
			if ((db[i]["KB"]==="101")||(db[i]["KB"]==="104")||(db[i]["KB"]==="107")){
				if (
						(db[i]["KB"]==="101" && db[i]["AXIS"]==="0" && db[i]["SEQ"]==="1")
					||(db[i]["KB"]==="101" && db[i]["AXIS"]==="0" && db[i]["SEQ"]==="2")
					||(db[i]["KB"]==="101" && db[i]["AXIS"]==="1" && db[i]["SEQ"]==="1")
					||(db[i]["KB"]==="101" && db[i]["AXIS"]==="1" && db[i]["SEQ"]==="2")
					||(db[i]["KB"]==="101" && db[i]["AXIS"]==="2" && db[i]["SEQ"]==="1")
					||(db[i]["KB"]==="101" && db[i]["AXIS"]==="2" && db[i]["SEQ"]==="2")
				){
					$("#_"+db[i]["KB"]+"_"+db[i]["AXIS"]+"_"+db[i]["SEQ"]).text(separate(db[i]["VAL"],2));//少数点2桁表示
				} else if (
						(db[i]["KB"]==="107" && db[i]["AXIS"]==="0" && db[i]["SEQ"]==="1")
					||(db[i]["KB"]==="107" && db[i]["AXIS"]==="1" && db[i]["SEQ"]==="1")
					||(db[i]["KB"]==="107" && db[i]["AXIS"]==="2" && db[i]["SEQ"]==="1")
				){
					$("#_"+db[i]["KB"]+"_"+db[i]["AXIS"]+"_"+db[i]["SEQ"]).text(separate(db[i]["VAL"],0));//少数点表示なし
				} else {
					$("#_"+db[i]["KB"]+"_"+db[i]["AXIS"]+"_"+db[i]["SEQ"]).text(separate(db[i]["VAL"],1));//少数点1桁表示
				}
			}
		}

		$('#tab1').show();
		// 国内純売上　日別売上
		chart_uri_day();
		// 国内純売上　月別売上
		chart_uri_mon();

		// 国内購入者　通販　月別購入者
		chart_tsu_kyk_mon();
		// 国内購入者　店舗　月別購入者
		chart_ten_kyk_mon();

		// 国内顧客単価　通販　月別顧客単価
		chart_tsu_tan_mon();
		// 国内顧客単価　店舗　月別顧客単価
		chart_ten_tan_mon();

		// 国内受注実績　通販　日別受注件数
		chart_tsu_kyk_day();
		// 国内受注実績　通販　日別受注単価
		chart_tsu_tan_day();

		// 国内受注実績　店舗　日別レジ件数
		chart_ten_kyk_day();
		// 国内受注実績　店舗　日別レジ単価
		chart_ten_tan_day();

		// 国内受注実績　チャネル別受注件数
		chart_chanel_ken();
		// 国内受注実績　チャネル別受注単価
		chart_chanel_tan();

		// 初期化完了
		onInitTab1 = false;
	}else{
		$('#tab1').show();
	}
}

/**
 * タブ更新：商品実績
 */
function onShowTab2(){

	$('#tab1').hide();
	$('#tab2').hide();
	$('#tab3').hide();

	if (db.length===0){
		return;
	}

	if (onInitTab2){

		$('#tab2').show();

		//分野別購入額
		chart_cls_gak();
		// 分野別購入者
		chart_cls_kyk();
		// 分野別顧客単価
		chart_cls_tan();

		// カテゴリ別購入額
		chart_cat_gak();
		// カテゴリ別購入者
		chart_cat_kyk();
		// カテゴリ別客単価
		chart_cat_tan();

		// 重点商材別購入額
		chart_cho_gak();
		// 重点商材別購入者
		chart_cho_kyk();
		// 重点商材別客単価
		chart_cho_tan();

		// 初期化完了
		onInitTab2 = false;
	}else{
		$('#tab2').show();
	}
}

/**
 * タブ更新：KPI
 */
function onShowTab3(){

	$('#tab1').hide();
	$('#tab2').hide();
	$('#tab3').hide();

	if (db.length===0){
		return;
	}

	if (onInitTab3){

		// 初期値設定
		for(i=0;i<db.length;i++){
			// 301:当月　初回受注者,305:リピート活性,307:ポイント発行・使用状況
			if (db[i]["KB"]==="301" || db[i]["KB"]==="305" || db[i]["KB"]==="307"){
				if (typeof db[i]["VAL"] === "undefined") {
					$("#_"+db[i]["KB"]+"_"+db[i]["AXIS"]+"_"+db[i]["SEQ"]).text(db[i]["DISPLAY"]);
				} else if (
						(db[i]["KB"]==="301" && db[i]["AXIS"]==="0" && db[i]["SEQ"]==="1")
					||(db[i]["KB"]==="301" && db[i]["AXIS"]==="0" && db[i]["SEQ"]==="2")
					||(db[i]["KB"]==="301" && db[i]["AXIS"]==="1" && db[i]["SEQ"]==="1")
					||(db[i]["KB"]==="301" && db[i]["AXIS"]==="1" && db[i]["SEQ"]==="2")
					||(db[i]["KB"]==="301" && db[i]["AXIS"]==="2" && db[i]["SEQ"]==="1")
					||(db[i]["KB"]==="301" && db[i]["AXIS"]==="2" && db[i]["SEQ"]==="2")
					||(db[i]["KB"]==="305" && db[i]["AXIS"]==="0" && db[i]["SEQ"]==="1")
					||(db[i]["KB"]==="305" && db[i]["AXIS"]==="1" && db[i]["SEQ"]==="1")
				){
					$("#_"+db[i]["KB"]+"_"+db[i]["AXIS"]+"_"+db[i]["SEQ"]).text(separate(db[i]["VAL"],0));//少数点表示なし
				} else {
					$("#_"+db[i]["KB"]+"_"+db[i]["AXIS"]+"_"+db[i]["SEQ"]).text(separate(db[i]["VAL"],1));//少数点1桁表示
				}
			}
		}

		$('#tab3').show();

		// 新規獲得　日別　初回購入者数
		chart_news_day();
		// 新規獲得　月別　初回購入者数
		chart_news_mon();
		// 新規獲得　月別　初回購入単価
		chart_news_tan();

		// リピート活性　Ｒ６以内反応率
		chart_r6_mon();

		// ポイント発行・使用状況　月別発行ポイント
		chart_point_hak();
		// ポイント発行・使用状況　月別使用ポイント
		chart_point_gak();

		// 初期化完了
		onInitTab3 = false;
	}else{
		$('#tab3').show();
	}
}

/**
 * 日付調整
 * 最終日を追加
 */
function getTicksDays() {
	var YY = $("#selectyear").val();
	var MM = $("#selectmonth").val();
	var dt = new Date(parseInt(YY), parseInt(MM), 0);
	var dtEnd = dt.getDate();
	var ticks = [1, , , , 5, , , , , 10, , , , , 15, , , , , 20, , , , , 25, , ];
	for(i=27;i<=dtEnd;i++){
		// 日付
		if (i===dtEnd){
			ticks.push(''+i);
		}else{
			ticks.push('');
		}
	}
	return ticks;
}

/**
 * 前年の曜日取得
 * @param DD 日
 * @returns {String} 加工済み日付
 */
function getTooltipDay(DD){
	// 日付（土曜、日曜色付け）
	var YY = $("#selectyear").val();
	var MM = $("#selectmonth").val();
	// 前年同月日
	var dt = new Date(parseInt(YY)-1, parseInt(MM)-1, parseInt(DD));
	if (dt.getDay()===0){// 日曜日
		return ('<span class="sun">'+DD+'</span>');
	}else if (dt.getDay()===6){// 土曜日
		return ('<span class="std">'+DD+'</span>');
	}else{
		return (''+DD);
	}
}

// 国内純売上　日別売上
function chart_uri_day(){

	// グラフ破棄
	if(obj_chart_uri_day){
		obj_chart_uri_day.destroy();
	}

	// グラフ用情報
	var s1 = [];// 当年 通販
	var s2 = [];// 当年 店舗
	var s3 = [];// 前年 通販
	var s4 = [];// 前年 店舗
	for(i=0;i<db.length;i++){
		// KB=102:国内純売上　日別　売上
		if (db[i]["KB"]==="102"){
			if (db[i]["AXIS"]==="1"){s1.push(parseFloat(db[i]["VAL"]));}	// 当年　通販
			if (db[i]["AXIS"]==="2"){s2.push(parseFloat(db[i]["VAL"]));}	// 当年　店舗
			if (db[i]["AXIS"]==="3"){s3.push(parseFloat(db[i]["VAL"]));}	// 前年　通販
			if (db[i]["AXIS"]==="4"){s4.push(parseFloat(db[i]["VAL"]));}	// 前年　店舗
		}
	}
	if (s1.length===0 || s2.length===0 || s3.length===0 || s4.length===0)	return;

	// 積み上げの最大値
	var maxVal = 0;
	for(i=0;i<s1.length;i++){
		if (maxVal<s1[i]+s2[i]){maxVal=s1[i]+s2[i];}
		if (maxVal<s3[i]+s4[i]){maxVal=s3[i]+s4[i];}
	}
	if (maxVal>=10){
		maxVal = Math.ceil(maxVal/10)*10;
	}else{
		maxVal = Math.ceil(maxVal*10)/10;
	}

	// 日付（土曜、日曜色付け）
	var YY = $("#selectyear").val();
	var MM = $("#selectmonth").val();
	var dt = new Date(parseInt(YY), parseInt(MM), 0);
	var dtEnd = dt.getDate();
	var basevals = [];
	var ticks = [];
	for(i=1;i<=dtEnd;i++){
		// 日付
		dt = new Date(parseInt(YY), parseInt(MM)-1, i);
		if (dt.getDay()===0){// 日曜日
			ticks.push('<span class="sun">'+i+'</span>');
		}else if (dt.getDay()===6){// 土曜日
			ticks.push('<span class="std">'+i+'</span>');
		}else{
			ticks.push(''+i);
		}
		ticks.push('');

		// ベース
		basevals.push(0);
		basevals.push(0);
	}

	var labels = ['通販', '店舗', '前年通販', '前年店舗'];

	obj_chart_uri_day = $.jqplot('chart_uri_day', [s1, s2, s3, s4], {
		stackSeries: true,
		captureRightClick: true,
		grid: {
			background: '#ffffff',
			borderWidth: 2,
			shadow: false
		},
		seriesColors: ["#F5A347", "#759FD4", "#DCDDDD", "BFC0C0"],
		seriesDefaults:{
			renderer:$.jqplot.BarRenderer,
			rendererOptions: {barMargin: 0, highlightMouseOver: false, shadow: false },
			pointLabels: {show: false}
		},
		series: [
			{
				renderer:$.jqplot.BarRenderer,
				color: '#F5A347'
			},
			{
				renderer:$.jqplot.BarRenderer,
				color: '#759FD4'
			},
			{
				disableStack : true,
				renderer:$.jqplot.BarRenderer,
				color: '#DCDDDD',
				rendererOptions: {
					barMargin: 0,
					barPadding: -11,
					barWidth: 11
				}
			},
			{
				disableStack : false,
				renderer:$.jqplot.BarRenderer,
				color: '#BFC0C0'
			}
		],
		axes: {
			xaxis: {
				renderer: $.jqplot.CategoryAxisRenderer,
				ticks: ticks,
				tickOptions: {
					fontSize: '8pt',
					showGridline: false,
					formatString:'%d'
				}
			},
			yaxis: {
//				padMin: 0,
				min: 0,
//				max: maxVal,
				tickInterval: 1,
				tickOptions: {
					formatString:'%.2f'
				}
			}
		},
		highlighter: {
			show: true,
			tooltipContentEditor: function (str, seriesIndex, pointIndex, plot) {
				var prefix = ticks[pointIndex]+"日<br>";
				var value = plot.data[seriesIndex][pointIndex];
				var suffix = "";

				switch(seriesIndex) {
				case 0:
					prefix = labels[seriesIndex] + ' ' + prefix;
					suffix = " 円";
					break;
				case 1:
					prefix = labels[seriesIndex] + ' ' + prefix;
					suffix = " 円";
					break;
				case 2:
					prefix = labels[seriesIndex] + ' ' + getTooltipDay((pointIndex+1)/2)+"日<br>";
					suffix = " 円";
					break;
				case 3:
					prefix = labels[seriesIndex] + ' ' + getTooltipDay((pointIndex+1)/2)+"日<br>";
					suffix = " 円";
					break;
				}

				var html = "<div class='highlighter'>";
				html += prefix;
				html += String(Math.round((value+0)*100000000)).replace( /(\d)(?=(\d\d\d)+(?!\d))/g, '$1,');
				html += suffix;
				html += "  </div>";

				return html;
			},
			showMarker: false,
			tooltipLocation: 'n',
			tooltipAxes: 'y',
			formatString: '%s'
		},
		legend: {
			show: true,
			renderer: $.jqplot.EnhancedLegendRenderer,
			rendererOptions: {
				seriesToggle: false,
				numberRows: 1
			},
			fontSize: '0.6em',
			location: 's',
			labels: labels,
			marginTop : "40px",
			placement: 'outside'
		}
	});

	// ハイライト非表示
	$('#chart_uri_day').mouseout(function(e){
		$('.jqplot-highlighter-tooltip').css("position","absolute").css("display", "none");
	});
}

// 国内純売上　月別売上
function chart_uri_mon(){

	// グラフ破棄
	if (obj_chart_uri_mon){
		obj_chart_uri_mon.destroy();
	}

	// グラフ用情報
	var s1 = [];// 当年 通販
	var s2 = [];// 当年 店舗
	var s3 = [];// 計画
	var s4 = [];// 前年 通販
	var s5 = [];// 前年 店舗
	for(i=0;i<db.length;i++){
		// KB=103:国内純売上　月別　売上
		if (db[i]["KB"]==="103"){
			if (db[i]["AXIS"]==="1"){s1.push(parseFloat(db[i]["VAL"]));}	// 当年　通販
			if (db[i]["AXIS"]==="2"){s2.push(parseFloat(db[i]["VAL"]));}	// 当年　店舗
			if (db[i]["AXIS"]==="3"){s3.push(parseFloat(db[i]["VAL"]));}	// 計画
			if (db[i]["AXIS"]==="4"){s4.push(parseFloat(db[i]["VAL"]));}	// 前年　通販
			if (db[i]["AXIS"]==="5"){s5.push(parseFloat(db[i]["VAL"]));}	// 前年　店舗
		}
	}
	if (s1.length===0 || s2.length===0 || s3.length===0 || s4.length===0 || s5.length===0)	return;

	// 積み上げの最大値
	var maxVal = 0;
	for(i=0;i<s1.length;i++){
		if (maxVal<s3[i]){maxVal=s3[i];}
		if (maxVal<s1[i]+s2[i]){maxVal=s1[i]+s2[i];}
		if (maxVal<s4[i]+s5[i]){maxVal=s4[i]+s5[i];}
	}
	if (maxVal>=10){
		maxVal = Math.ceil(maxVal/10)*10;
	}else{
		maxVal = Math.ceil(maxVal*10)/10;
	}

	/* graph ticks */
	var ticks = ['1', '', '2', '', '3', '', '4', '', '5', '', '6', '', '7', '', '8', '', '9', '', '10', '', '11', '', '12', ''];

	var labels = ['通販', '店舗', '前年通販', '前年店舗', '計画'];

	obj_chart_uri_mon = $.jqplot('chart_uri_mon', [s1, s2, s4, s5, s3], {
		stackSeries: true,
		captureRightClick: true,
		grid: {
			background: '#ffffff',
			borderWidth: 2,
			shadow: false
		},
		seriesColors: ["#F5A347", "#759FD4", "#DCDDDD", "#BFC0C0", "#757474"],
		seriesDefaults:{
			renderer:$.jqplot.BarRenderer,
			rendererOptions: {
				barMargin: 0,
				barPadding: 0,
				highlightMouseOver: false,
				shadow: false
			},
			pointLabels: {show: false}
		},
		series: [
			{
				renderer:$.jqplot.BarRenderer,
				color: '#F5A347'
			},
			{
				renderer:$.jqplot.BarRenderer,
				color: '#759FD4'
			},
			{
				disableStack : true,
				renderer:$.jqplot.BarRenderer,
				color: '#DCDDDD',
				rendererOptions: {
					barMargin: 0,
					barPadding: -10,
					barWidth: 10
				}
			},
			{
				disableStack : false,
				renderer:$.jqplot.BarRenderer,
				color: '#BFC0C0'
			},
			{
				disableStack : true,
				renderer: $.jqplot.LineRenderer,
				rendererOptions: {
				shadow: false
				},
				lineWidth:1.5,
				markerOptions: {
					size: 3,
					style: 'filledSquare'
				},
				color: '#757474'
			}
		],
		axes: {
			xaxis: {
				renderer: $.jqplot.CategoryAxisRenderer,
				ticks: ticks,
				tickOptions: {
					fontSize: '8pt',
					showGridline: false,
					formatString:'%.2f'
				}
			},
			yaxis: {
//				padMin: 0,
				min: 0,
//				max: maxVal,
				tickOptions: {
					formatString:'%.2f'
				}
			}
		},
		highlighter: {
			show: true,
			tooltipContentEditor: function (str, seriesIndex, pointIndex, plot) {

				var prefix = ticks[pointIndex]+"月<br>";
				var value = plot.data[seriesIndex][pointIndex];
				var suffix = "";

				switch(seriesIndex) {
				case 0:
					prefix = labels[seriesIndex] + ' ' + prefix;
					suffix = " 円"
						break;
				case 1:
					prefix = labels[seriesIndex] + ' ' + prefix;
					suffix = " 円"
					break;
				case 2:
					prefix = labels[seriesIndex] + ' ' + ticks[pointIndex-1]+"月<br>";
					suffix = " 円"
					break;
				case 3:
					prefix = labels[seriesIndex] + ' ' + ticks[pointIndex-1]+"月<br>";
					suffix = " 円"
					break;
				case 4:
					prefix = '計画' + ' ' + prefix;
					value = plot.data[seriesIndex][pointIndex];
					suffix = " 円"
					break;
				}

				var html = "<div class='highlighter'>";
				html += prefix;
				html += String(Math.round((value+0)*100000000)).replace( /(\d)(?=(\d\d\d)+(?!\d))/g, '$1,');
				html += suffix;
				html += "  </div>";

				return html;
			},
			showMarker: false,
			tooltipLocation: 'n',
			tooltipAxes: 'y',
			formatString: '%s'
		},
		legend: {
			show: true,
			renderer: $.jqplot.EnhancedLegendRenderer,
			rendererOptions: {
				seriesToggle: false,
				numberRows: 1
			},
			fontSize: '0.6em',
			location: 's',
			labels: labels,
			marginTop : "40px",
			placement: 'outside'
		}
	});
	// ハイライト非表示
	$('#chart_uri_mon').mouseout(function(e){
		$('.jqplot-highlighter-tooltip').css("position","absolute").css("display", "none");
	});
}

//国内購入者　通販　月別購入者
function chart_tsu_kyk_mon(){

	// グラフ破棄
	if (obj_chart_tsu_kyk_mon) {
		obj_chart_tsu_kyk_mon.destroy();
	}

	// グラフ用情報
	var s1 = [];// 当年購入者
	var s2 = [];// 前年購入者
	for(i=0;i<db.length;i++){
		// KB=105:国内購入者　通販 月別　購入者
		if (db[i]["KB"]==="105"){
			if (db[i]["AXIS"]==="1"){s1.push(parseFloat(db[i]["VAL"]));}	// 当年購入者
			if (db[i]["AXIS"]==="2"){s2.push(parseFloat(db[i]["VAL"]));}	// 前年購入者
		}
	}
	if (s1.length===0 || s2.length===0)	return;

	var ticks = ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12'];
	var labels = ['当年', '前年'];

	obj_chart_tsu_kyk_mon = $.jqplot('chart_tsu_kyk_mon', [s1, s2], {
		stackSeries: false,
		captureRightClick: true,
		grid: {
			background: '#ffffff',
			borderWidth: 1,
			shadow: false
		},
		seriesDefaults:{
			renderer:$.jqplot.BarRenderer,
			rendererOptions: {
				barMargin: 0,
				barWidth:6,
				barPadding: 0,
				shadow: false,
				highlightMouseDown: true
			},
			pointLabels: {show: false}
		},
		series: [
			{
				renderer:$.jqplot.BarRenderer,
				color: '#F9A45E'
			},
			{
				renderer:$.jqplot.BarRenderer,
				rendererOptions: {
					barWidth: 4
				},
				color: '#DFE0E0'
			}
		],
		axes: {
			xaxis: {
				renderer: $.jqplot.CategoryAxisRenderer,
				ticks: ticks,
				tickOptions: {
					fontSize: '8pt',
					showGridline: false,
					formatString:'%d'
				}
			},
			yaxis: {
				padMin: 0,
				min: 0,
				tickOptions: {
					formatString:'%d'
				},
				autoscale: true
			}
		},
		highlighter: {
			show: true,
			tooltipContentEditor: function (str, seriesIndex, pointIndex, plot) {

				var prefix = ticks[pointIndex]+"月<br>";
				var value = plot.data[seriesIndex][pointIndex];
				var suffix = "";

				switch(seriesIndex) {
				case 0:
					prefix = labels[seriesIndex] + ' ' + prefix;
					suffix = " 人"
					break;
				case 1:
					prefix = labels[seriesIndex] + ' ' + prefix;
					suffix = " 人"
					break;
				}

				var html = "<div class='highlighter'>";
				html += prefix;
				html += String(Math.round((value+0)*10000)).replace( /(\d)(?=(\d\d\d)+(?!\d))/g, '$1,');
				html += suffix;
				html += "  </div>";

				return html;
			},
			showMarker: false,
			tooltipLocation: 'n',
			tooltipAxes: 'y',
			formatString: '%s'
		},
		legend: {
			show: true,
			renderer: $.jqplot.EnhancedLegendRenderer,
			rendererOptions: {
				seriesToggle: false,
				numberRows: 1
			},
			fontSize: '0.6em',
			location: 's',
			labels: labels,
			marginTop : "40px",
			placement: 'outside'
		}
	});
	// ハイライト非表示
	$('#chart_tsu_kyk_mon').mouseout(function(e){
		$('.jqplot-highlighter-tooltip').css("position","absolute").css("display", "none");
	});
}

//国内購入者　店舗　月別購入者
function chart_ten_kyk_mon(){

	// グラフ用情報
	var s1 = [];// 当年購入者
	var s2 = [];// 前年購入者
	for(i=0;i<db.length;i++){
		// KB=106:国内購入者　店舗 月別　購入者
		if (db[i]["KB"]==="106"){
			if (db[i]["AXIS"]==="1"){s1.push(parseFloat(db[i]["VAL"]));}	// 当年購入者
			if (db[i]["AXIS"]==="2"){s2.push(parseFloat(db[i]["VAL"]));}	// 前年購入者
		}
	}
	if (s1.length===0 || s2.length===0)	return;

	var ticks = ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12'];
	var labels = ['当年', '前年'];

	if (obj_chart_ten_kyk_mon) {
		obj_chart_ten_kyk_mon.destroy();
	}

	obj_chart_ten_kyk_mon = $.jqplot('chart_ten_kyk_mon', [s1, s2], {
		stackSeries: false,
		captureRightClick: true,
		grid: {
			background: '#ffffff',
			borderWidth: 1,
			shadow: false
		},
		seriesDefaults:{
			renderer:$.jqplot.BarRenderer,
			rendererOptions: {
				barMargin: 0,
				barWidth:6,
				barPadding: 0,
				shadow: false,
				highlightMouseDown: true
			},
			pointLabels: {show: false}
		},
		series: [
			{
				renderer:$.jqplot.BarRenderer,
				color: '#81A3D4'
			},
			{
				renderer:$.jqplot.BarRenderer,
				rendererOptions: {
					barWidth: 4
				},
				color: '#DCDDDD'
			}
		],
		axes: {
			xaxis: {
				renderer: $.jqplot.CategoryAxisRenderer,
				ticks: ticks,
				tickOptions: {
					fontSize: '8pt',
					showGridline: false,
					formatString:'%d'
				}
			},
			yaxis: {
				padMin: 0,
				min: 0,
				tickOptions: {
					formatString:'%d'
				},
				autoscale: true
			}
		},
		highlighter: {
			show: true,
			tooltipContentEditor: function (str, seriesIndex, pointIndex, plot) {

				var prefix = ticks[pointIndex]+"月<br>";
				var value = plot.data[seriesIndex][pointIndex];
				var suffix = "";

				switch(seriesIndex) {
				case 0:
					prefix = labels[seriesIndex] + ' ' + prefix;
					suffix = " 人"
					break;
				case 1:
					prefix = labels[seriesIndex] + ' ' + prefix;
					suffix = " 人"
					break;
				}

				var html = "<div class='highlighter'>";
				html += prefix;
				html += String(Math.round((value+0)*10000)).replace( /(\d)(?=(\d\d\d)+(?!\d))/g, '$1,');
				html += suffix;
				html += "  </div>";

				return html;
			},
			showMarker: false,
			tooltipLocation: 'n',
			tooltipAxes: 'y',
			formatString: '%s'
		},
		legend: {
			show: true,
			renderer: $.jqplot.EnhancedLegendRenderer,
			rendererOptions: {
				seriesToggle: false,
				numberRows: 1
			},
			fontSize: '0.6em',
			location: 's',
			labels: labels,
			marginTop : "40px",
			placement: 'outside'
		}
	});
	// ハイライト非表示
	$('#chart_ten_kyk_mon').mouseout(function(e){
		$('.jqplot-highlighter-tooltip').css("position","absolute").css("display", "none");
	});
}

//国内顧客単価　通販　月別顧客単価
function chart_tsu_tan_mon(){

	// グラフ用情報
	var s1 = [];// 当年顧客単価
	var s2 = [];// 前年顧客単価
	for(i=0;i<db.length;i++){
		// KB=108:国内顧客単価　通販　月別　顧客単価
		if (db[i]["KB"]==="108"){
			if (db[i]["AXIS"]==="1"){s1.push(parseFloat(db[i]["VAL"]));}	// 当年顧客単価
			if (db[i]["AXIS"]==="2"){s2.push(parseFloat(db[i]["VAL"]));}	// 前年顧客単価
		}
	}
	if (s1.length===0 || s2.length===0)	return;

	var ticks = ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12'];
	var labels = ['当年', '前年'];

	// グラフ廃棄
	if (obj_chart_tsu_tan_mon) {
		obj_chart_tsu_tan_mon.destroy();
	}

	obj_chart_tsu_tan_mon = $.jqplot('chart_tsu_tan_mon', [s1, s2], {
		stackSeries: false,
		captureRightClick: true,
		grid: {
			background: '#ffffff',
			borderWidth: 1,
			shadow: false
		},
		seriesDefaults:{
			shadow: false
		},
		series: [
			{
				renderer: $.jqplot.LineRenderer,
				yaxis:'yaxis',
				lineWidth:1.5,
				markerOptions: {
					size: 3,
					style: 'filledSquare'
				},
				color: '#C58842'
			},
			{
				renderer: $.jqplot.LineRenderer,
				yaxis:'yaxis',
				lineWidth:1.5,
				markerOptions: {
					size: 2,
					style: 'filledCircle'
				},
				color: '#9FA0A0'
			}
		],
		axes: {
			xaxis: {
				renderer: $.jqplot.CategoryAxisRenderer,
				ticks: ticks,
				tickOptions: {
					fontSize: '8pt',
					showGridline: false,
					formatString:'%d'
				}
			},
			yaxis: {
				padMin: 0,
				min: 0,
				tickOptions: {
					formatString:"%'d"
				},
				autoscale: true
			}
		},
		highlighter: {
			show: true,
			tooltipContentEditor: function (str, seriesIndex, pointIndex, plot) {

				var prefix = ticks[pointIndex]+"月<br>";
				var value = plot.data[seriesIndex][pointIndex];
				var suffix = "";

				switch(seriesIndex) {
				case 0:
					prefix = labels[seriesIndex] + ' ' + prefix;
					suffix = " 円"
					break;
				case 1:
					prefix = labels[seriesIndex] + ' ' + prefix;
					suffix = " 円"
					break;
				}

				var html = "<div class='highlighter'>";
				html += prefix;
				html += String(Math.round((value+0)*1)).replace( /(\d)(?=(\d\d\d)+(?!\d))/g, '$1,');
				html += suffix;
				html += "  </div>";

				return html;
			},
			showMarker: false,
			tooltipLocation: 'n',
			tooltipAxes: 'y',
			formatString: '%s'
		},
		legend: {
			show: true,
			renderer: $.jqplot.EnhancedLegendRenderer,
			rendererOptions: {
				seriesToggle: false,
				numberRows: 1
			},
			fontSize: '0.6em',
			location: 's',
			labels: labels,
			marginTop : "40px",
			placement: 'outside'
		}
	});
	// ハイライト非表示
	$('#chart_tsu_tan_mon').mouseout(function(e){
		$('.jqplot-highlighter-tooltip').css("position","absolute").css("display", "none");
	});
}

//国内顧客単価　店舗　月別顧客単価
function chart_ten_tan_mon(){

	// グラフ用情報
	var s1 = [];// 当年顧客単価
	var s2 = [];// 前年顧客単価
	for(i=0;i<db.length;i++){
		// KB=109:国内顧客単価　店舗　月別　顧客単価
		if (db[i]["KB"]==="109"){
			if (db[i]["AXIS"]==="1"){s1.push(parseFloat(db[i]["VAL"]));}	// 当年顧客単価
			if (db[i]["AXIS"]==="2"){s2.push(parseFloat(db[i]["VAL"]));}	// 前年顧客単価
		}
	}
	if (s1.length===0 || s2.length===0)	return;

	var ticks = ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12'];
	var labels = ['当年', '前年'];

	// グラフ廃棄
	if (obj_chart_ten_tan_mon) {
		obj_chart_ten_tan_mon.destroy();
	}

	obj_chart_ten_tan_mon = $.jqplot('chart_ten_tan_mon', [s1, s2], {
		stackSeries: false,
		captureRightClick: true,
		grid: {
			background: '#ffffff',
			borderWidth: 1,
			shadow: false
		},
		seriesDefaults:{
			shadow: false
		},
		series: [
			{
				renderer: $.jqplot.LineRenderer,
				yaxis:'yaxis',
				lineWidth:1.5,
				markerOptions: {
					size: 3,
					style: 'filledSquare'
				},
				color: '#3C82C4'
			},
			{
				renderer: $.jqplot.LineRenderer,
				lineWidth:1.5,
				yaxis:'yaxis',
				markerOptions: {
					size: 2,
					style: 'filledCircle'
				},
				color: '#9FA0A0'
			}
		],
		axes: {
			xaxis: {
				renderer: $.jqplot.CategoryAxisRenderer,
				ticks: ticks,
				tickOptions: {
					fontSize: '8pt',
					showGridline: false,
					formatString:'%d'
				}
			},
			yaxis: {
				padMin: 0,
				min: 0,
				tickOptions: {
					formatString: "%'d"
				},
				autoscale: true
			}
		},
		highlighter: {
			show: true,
			tooltipContentEditor: function (str, seriesIndex, pointIndex, plot) {

				var prefix = pointIndex+1+"月<br>";
				var value = plot.data[seriesIndex][pointIndex];
				var suffix = "";

				switch(seriesIndex) {
				case 0:
					prefix = labels[seriesIndex] + ' ' + prefix;
					suffix = " 円"
					break;
				case 1:
					prefix = labels[seriesIndex] + ' ' + prefix;
					suffix = " 円"
					break;
				}

				var html = "<div class='highlighter'>";
				html += prefix;
				html += String(Math.round((value+0)*1)).replace( /(\d)(?=(\d\d\d)+(?!\d))/g, '$1,');
				html += suffix;
				html += "  </div>";

				return html;
			},
			showMarker: false,
			tooltipLocation: 'n',
			tooltipAxes: 'y',
			formatString: '%s'
		},
		legend: {
			show: true,
			renderer: $.jqplot.EnhancedLegendRenderer,
			rendererOptions: {
				seriesToggle: false,
				numberRows: 1
			},
			fontSize: '0.6em',
			location: 's',
			labels: labels,
			marginTop : "40px",
			placement: 'outside'
		}
	});
	// ハイライト非表示
	$('#chart_ten_tan_mon').mouseout(function(e){
		$('.jqplot-highlighter-tooltip').css("position","absolute").css("display", "none");
	});
}


// 国内受注実績　通販　日別受注件数
function chart_tsu_kyk_day(){

	// グラフ破棄
	if (obj_chart_tsu_kyk_day) {
		obj_chart_tsu_kyk_day.destroy();
	}

	// グラフ用情報
	var s1 = [];// 当年受注件数
	var s2 = [];// 前年受注件数
	for(i=0;i<db.length;i++){
		// KB=110:国内受注実績　通販　日別　受注件数
		if (db[i]["KB"]==="110"){
			if (db[i]["AXIS"]==="1"){s1.push(parseFloat(db[i]["VAL"]));}	// 当年受注件数
			if (db[i]["AXIS"]==="2"){s2.push(parseFloat(db[i]["VAL"]));}	// 前年受注件数
		}
	}
	if (s1.length===0 || s2.length===0)	return;

  // 日付調整
  var ticks = getTicksDays();
	var labels = ['当月', '前年同月'];

	obj_chart_tsu_kyk_day = $.jqplot('chart_tsu_kyk_day', [s1, s2], {
		stackSeries: false,
		captureRightClick: true,
		grid: {
			background: '#ffffff',
			borderWidth: 1,
			shadow: false
		},
		seriesDefaults:{
			renderer:$.jqplot.BarRenderer,
			rendererOptions: {
				barMargin: 0,
				barWidth:4,
				barPadding: 0,
				shadow: false,
				highlightMouseDown: true
			},
			pointLabels: {show: false}
		},
		series: [
			{
				renderer:$.jqplot.BarRenderer,
				color: '#F9A45E'
			},
			{
				renderer:$.jqplot.BarRenderer,
				rendererOptions: {
					barWidth: 3
				},
				color: '#DFE0E0'
			}
		],
		axes: {
			xaxis: {
				renderer: $.jqplot.CategoryAxisRenderer,
				ticks: ticks,
				tickOptions: {
					fontSize: '8pt',
					showGridline: false,
					formatString:'%d'
				}
			},
			yaxis: {
				padMin: 0,
				min: 0,
				tickOptions: {
					formatString:'%.1f'
				},
				autoscale: true
			},
			y2axis: {
				padMin: 0,
				tickOptions: {
					formatString:"%'d"
				},
				autoscale: true
			}
		},
		highlighter: {
			show: true,
			tooltipContentEditor: function (str, seriesIndex, pointIndex, plot) {

				var prefix = pointIndex+1+"日<br>";
				var value = plot.data[seriesIndex][pointIndex];
				var suffix = "";

				switch(seriesIndex) {
				case 0:
					prefix = labels[seriesIndex] + ' ' + prefix;
					suffix = " 人"
					break;
				case 1:
					prefix = labels[seriesIndex] + ' ' + prefix;
					suffix = " 人"
					break;
				}

				var html = "<div class='highlighter'>";
				html += prefix;
				html += String(Math.round((value+0)*10000)).replace( /(\d)(?=(\d\d\d)+(?!\d))/g, '$1,');
				html += suffix;
				html += "  </div>";

				return html;
			},
			showMarker: false,
			tooltipLocation: 'n',
			tooltipAxes: 'y',
			formatString: '%s'
		},
		legend: {
			show: true,
			renderer: $.jqplot.EnhancedLegendRenderer,
			rendererOptions: {
				seriesToggle: false,
				numberRows: 1
			},
			fontSize: '0.6em',
			location: 's',
			labels: labels,
			marginTop : "40px",
			placement: 'outside'
		}
	});
	// ハイライト非表示
	$('#chart_tsu_kyk_day').mouseout(function(e){
		$('.jqplot-highlighter-tooltip').css("position","absolute").css("display", "none");
	});
}

//国内受注実績　通販　日別受注単価
function chart_tsu_tan_day(){

	// グラフ破棄
	if (obj_chart_tsu_tan_day) {
		obj_chart_tsu_tan_day.destroy();
	}

	// グラフ用情報
	var s1 = [];// 当年受注単価
	var s2 = [];// 前年受注単価
	for(i=0;i<db.length;i++){
		// KB=111:国内受注実績　通販　日別　受注単価
		if (db[i]["KB"]==="111"){
			if (db[i]["AXIS"]==="1"){s1.push(parseFloat(db[i]["VAL"]));}	// 当年受注単価
			if (db[i]["AXIS"]==="2"){s2.push(parseFloat(db[i]["VAL"]));}	// 前年受注単価
		}
	}
	if (s1.length===0 || s2.length===0)	return;

	// 日付調整
	var ticks = getTicksDays();
	var labels = ['当月', '前年同月'];

	obj_chart_tsu_tan_day = $.jqplot('chart_tsu_tan_day', [s1, s2], {
		stackSeries: false,
		captureRightClick: true,
		grid: {
			background: '#ffffff',
			borderWidth: 1,
			shadow: false
		},
		seriesDefaults:{
			shadow: false
		},
		series: [
			{
				renderer: $.jqplot.LineRenderer,
				yaxis:'yaxis',
				lineWidth:1.5,
				markerOptions: {
					size: 3,
					style: 'filledSquare'
				},
				color: '#C58842'
			},
			{
				renderer: $.jqplot.LineRenderer,
				yaxis:'yaxis',
				lineWidth:1.5,
				markerOptions: {
					size: 2,
					style: 'filledCircle'
				},
				color: '#9FA0A0'
			}
		],
		axes: {
			xaxis: {
				renderer: $.jqplot.CategoryAxisRenderer,
				ticks: ticks,
				tickOptions: {
					fontSize: '8pt',
					showGridline: false,
					formatString:'%d'
				}
			},
			yaxis: {
				padMin: 0,
				min: 0,
				tickOptions: {
					formatString:"%'d"
				},
				autoscale: true
			}
		},
		highlighter: {
			show: true,
			tooltipContentEditor: function (str, seriesIndex, pointIndex, plot) {

				var prefix = pointIndex+1+"日<br>";
				var value = plot.data[seriesIndex][pointIndex];
				var suffix = "";

				switch(seriesIndex) {
				case 0:
					prefix = labels[seriesIndex] + ' ' + prefix;
					suffix = " 円"
					break;
				case 1:
					prefix = labels[seriesIndex] + ' ' + prefix;
					suffix = " 円"
					break;
				}

				var html = "<div class='highlighter'>";
				html += prefix;
				html += String(Math.round((value+0)*1)).replace( /(\d)(?=(\d\d\d)+(?!\d))/g, '$1,');
				html += suffix;
				html += "  </div>";

				return html;
			},
			showMarker: false,
			tooltipLocation: 'n',
			tooltipAxes: 'y',
			formatString: '%s'
		},
		legend: {
			show: true,
			renderer: $.jqplot.EnhancedLegendRenderer,
			rendererOptions: {
				seriesToggle: false,
				numberRows: 1
			},
			fontSize: '0.6em',
			location: 's',
			labels: labels,
			marginTop : "40px",
			placement: 'outside'
		}
	});
	// ハイライト非表示
	$('#chart_tsu_tan_day').mouseout(function(e){
		$('.jqplot-highlighter-tooltip').css("position","absolute").css("display", "none");
	});
}

// 国内受注実績　店舗　日別レジ件数
function chart_ten_kyk_day(){

	// グラフ破棄
	if (obj_chart_ten_kyk_day) {
		obj_chart_ten_kyk_day.destroy();
	}

	// グラフ用情報
	var s1 = [];// 当年レジ件数
	var s2 = [];// 前年レジ件数
	for(i=0;i<db.length;i++){
		// KB=112:国内受注実績　店舗　日別　レジ件数
		if (db[i]["KB"]==="112"){
			if (db[i]["AXIS"]==="1"){s1.push(parseFloat(db[i]["VAL"]));}	// 当年レジ件数
			if (db[i]["AXIS"]==="2"){s2.push(parseFloat(db[i]["VAL"]));}	// 前年レジ件数
		}
	}
	if (s1.length===0 || s2.length===0)	return;

	// 日付調整
	var ticks = getTicksDays();
	var labels = ['当月', '前年同月'];

	obj_chart_ten_kyk_day = $.jqplot('chart_ten_kyk_day', [s1, s2], {
		stackSeries: false,
		captureRightClick: true,
		grid: {
			background: '#ffffff',
			borderWidth: 1,
			shadow: false
		},
		seriesDefaults:{
			renderer:$.jqplot.BarRenderer,
			rendererOptions: {
				barMargin: 0,
				barWidth:4,
				barPadding: 0,
				shadow: false,
				highlightMouseDown: true
			},
			pointLabels: {show: false}
		},
		series: [
			{
				renderer:$.jqplot.BarRenderer,
				color: '#81A3D4'
			},
			{
				renderer:$.jqplot.BarRenderer,
				rendererOptions: {
					barWidth: 3
				},
				color: '#DCDDDD'
			}
		],
		axes: {
			xaxis: {
				renderer: $.jqplot.CategoryAxisRenderer,
				ticks: ticks,
				tickOptions: {
					fontSize: '8pt',
					showGridline: false,
					formatString:'%d'
				}
			},
			yaxis: {
				padMin: 0,
				min: 0,
				tickOptions: {
					formatString:'%.1f'
				},
				autoscale: true
			}
		},
		highlighter: {
			show: true,
			tooltipContentEditor: function (str, seriesIndex, pointIndex, plot) {

				var prefix = pointIndex+1+"日<br>";
				var value = plot.data[seriesIndex][pointIndex];
				var suffix = "";

				switch(seriesIndex) {
				case 0:
					prefix = labels[seriesIndex] + ' ' + prefix;
					suffix = " 人"
					break;
				case 1:
					prefix = labels[seriesIndex] + ' ' + prefix;
					suffix = " 人"
					break;
				}

				var html = "<div class='highlighter'>";
				html += prefix;
				html += String(Math.round((value+0)*10000)).replace( /(\d)(?=(\d\d\d)+(?!\d))/g, '$1,');
				html += suffix;
				html += "  </div>";

				return html;
			},
			showMarker: false,
			tooltipLocation: 'n',
			tooltipAxes: 'y',
			formatString: '%s'
		},
		legend: {
			show: true,
			renderer: $.jqplot.EnhancedLegendRenderer,
			rendererOptions: {
				seriesToggle: false,
				numberRows: 1
			},
			fontSize: '0.6em',
			location: 's',
			labels: labels,
			marginTop : "40px",
			placement: 'outside'
		}
	});
	// ハイライト非表示
	$('#chart_ten_kyk_day').mouseout(function(e){
		$('.jqplot-highlighter-tooltip').css("position","absolute").css("display", "none");
	});
}

// 国内受注実績　店舗　日別レジ単価
function chart_ten_tan_day(){

	if (obj_chart_ten_tan_day) {
		obj_chart_ten_tan_day.destroy();
	}

	// グラフ用情報
	var s1 = [];// 当年レジ単価
	var s2 = [];// 前年レジ単価
	for(i=0;i<db.length;i++){
		// KB=113:国内受注実績　店舗　日別　レジ単価
		if (db[i]["KB"]==="113"){
			if (db[i]["AXIS"]==="1"){s1.push(parseFloat(db[i]["VAL"]));}	// 当年レジ単価
			if (db[i]["AXIS"]==="2"){s2.push(parseFloat(db[i]["VAL"]));}	// 前年レジ単価
		}
	}
	if (s1.length===0 || s2.length===0)	return;

	// 日付調整
	var ticks = getTicksDays();
	var labels = ['当月', '前年同月'];

	obj_chart_ten_tan_day = $.jqplot('chart_ten_tan_day', [s1, s2], {
		stackSeries: false,
		captureRightClick: true,
		grid: {
			background: '#ffffff',
			borderWidth: 1,
			shadow: false
		},
		seriesDefaults:{
			shadow: false
		},
		series: [
			{
				renderer: $.jqplot.LineRenderer,
				yaxis:'yaxis',
				lineWidth:1.5,
				markerOptions: {
					size: 3,
					style: 'filledSquare'
				},
				color: '#3C82C4'
			},
			{
				renderer: $.jqplot.LineRenderer,
				yaxis:'yaxis',
				lineWidth:1.5,
				markerOptions: {
					size: 2,
					style: 'filledCircle'
				},
				color: '#9FA0A0'
			}
		],
		axes: {
			xaxis: {
				renderer: $.jqplot.CategoryAxisRenderer,
				ticks: ticks,
				tickOptions: {
					fontSize: '8pt',
					showGridline: false,
					formatString:'%d'
				}
			},
			yaxis: {
				padMin: 0,
				min: 0,
				tickOptions: {
					formatString:"%'d"
				},
				autoscale: true
			}
		},
		highlighter: {
			show: true,
			tooltipContentEditor: function (str, seriesIndex, pointIndex, plot) {

				var prefix = pointIndex+1+"日<br>";
				var value = plot.data[seriesIndex][pointIndex];
				var suffix = "";

				switch(seriesIndex) {
				case 0:
					prefix = labels[seriesIndex] + ' ' + prefix;
					suffix = " 円"
					break;
				case 1:
					prefix = labels[seriesIndex] + ' ' + prefix;
					suffix = " 円"
					break;
				}

				var html = "<div class='highlighter'>";
				html += prefix;
				html += String(Math.round((value+0))).replace( /(\d)(?=(\d\d\d)+(?!\d))/g, '$1,');
				html += suffix;
				html += "  </div>";

				return html;
			},
			showMarker: false,
			tooltipLocation: 'n',
			tooltipAxes: 'y',
			formatString: '%s'
		},
		legend: {
			show: true,
			renderer: $.jqplot.EnhancedLegendRenderer,
			rendererOptions: {
				seriesToggle: false,
				numberRows: 1
			},
			fontSize: '0.6em',
			location: 's',
			labels: labels,
			marginTop : "40px",
			placement: 'outside'
		}
	});
	// ハイライト非表示
	$('#chart_ten_tan_day').mouseout(function(e){
		$('.jqplot-highlighter-tooltip').css("position","absolute").css("display", "none");
	});
}


// 国内受注実績　チャネル別受注件数
function chart_chanel_ken(){

	// グラフ破棄
	if (obj_chart_chanel_ken) {
		obj_chart_chanel_ken.destroy();
	}

	// グラフ用情報
	var s1 = [];// 当年 通販
	var s2 = [];// 前年 通販
	var s3 = [];// 当年 店舗
	var s4 = [];// 前年 店舗
	for(i=0;i<db.length;i++){
		// KB=114:国内受注実績　チャネル別　受注件数
		if (db[i]["KB"]==="114"){
			if (db[i]["AXIS"]==="1"){s1.push(parseFloat(db[i]["VAL"]));}	// 当年　通販
			if (db[i]["AXIS"]==="2"){s2.push(parseFloat(db[i]["VAL"]));}	// 前年　通販
			if (db[i]["AXIS"]==="3"){s3.push(parseFloat(db[i]["VAL"]));}	// 当年　店舗
			if (db[i]["AXIS"]==="4"){s4.push(parseFloat(db[i]["VAL"]));}	// 前年　店舗
		}
	}
	if (s1.length===0 || s2.length===0 || s3.length===0 || s4.length===0)	return;

	var ticks = ['スマホ　','アプリ　','PC　　','電話　　','通販その他','　店舗'];
	var labels = ['通販',' ','店舗','前年同月'];

	// 最大値
	var maxVal = 0;
	for(i=0;i<s1.length;i++){
		if (maxVal<s1[i]){maxVal=s1[i];}
		if (maxVal<s2[i]){maxVal=s2[i];}
		if (maxVal<s3[i]){maxVal=s3[i];}
		if (maxVal<s4[i]){maxVal=s4[i];}
	}
	if (maxVal>=10){
		maxVal = Math.ceil(maxVal/10)*10;
	}else{
		maxVal = Math.ceil(maxVal*10)/10;
	}


	obj_chart_chanel_ken = $.jqplot('chart_chanel_ken', [s1, s2, s3, s4], {
		stackSeries: false,
		captureRightClick: true,
		grid: {
			background: '#ffffff',
			borderWidth: 1,
			shadow: false
		},
		seriesDefaults:{
			renderer:$.jqplot.BarRenderer,
			rendererOptions: {
				barMargin: 20,
				barWidth: 10,
				barPadding: 0,
				shadow: false,
				highlightMouseDown: true
			},
			pointLabels: {show: false}
		},
		series: [
			{
				renderer:$.jqplot.BarRenderer,
				rendererOptions:{
					barPadding:-5
				},
				color: '#F9A45E'
			},
			{
				renderer:$.jqplot.BarRenderer,
				rendererOptions:{
					barWidth: 5,
					barPadding:-4
				},
				color: '#DCDDDD'
			},
			{
				renderer:$.jqplot.BarRenderer,
				rendererOptions:{
					barPadding:-10
				},
				color: '#759FD4'
			},
			{
				renderer:$.jqplot.BarRenderer,
				rendererOptions:{
					barWidth: 5,
					barPadding: 0
				},
				color: '#DCDDDD'
			}
		],
		axes: {
			xaxis: {
				renderer: $.jqplot.CategoryAxisRenderer,
				ticks: ticks,
				tickOptions: {
					fontSize: '6pt',
					showGridline: false,
					formatString:'%d'
				}
			},
			yaxis: {
//				padMin: 0,
				min: 0,
				max: maxVal,
				tickOptions: {
					formatString: "%'d"
				},
				autoscale: true
			}
		},
		highlighter: {
			show: true,
			tooltipContentEditor: function (str, seriesIndex, pointIndex, plot) {

				var prefix = ticks[pointIndex]+"<br>";
				var value = plot.data[seriesIndex][pointIndex];
				var suffix = " 件";

				switch(seriesIndex) {
				case 0:
					prefix = "当月 " + prefix;
					suffix = " 件";
					break;
				case 1:
					prefix = "前年同月 " + prefix;
					suffix = " 件";
					break;
				case 2:
					prefix = "当月 " + prefix;
					suffix = " 件";
					break;
				case 3:
					prefix = "前年同月 " + prefix;
					suffix = " 件";
					break;
				}

				var html = "<div class='highlighter'>";
				html += prefix;
				html += String(Math.round((value+0)*10000)).replace( /(\d)(?=(\d\d\d)+(?!\d))/g, '$1,');
				html += suffix;
				html += "  </div>";

				return html;
			},
			showMarker: false,
			tooltipLocation: 'n',
			tooltipAxes: 'y',
			formatString: '%s'
		},
		legend: {
			show: true,
			renderer: $.jqplot.EnhancedLegendRenderer,
			rendererOptions: {
				seriesToggle: false,
				numberRows: 1
			},
			fontSize: '0.6em',
			location: 's',
			labels: labels,
			marginTop : "40px",
			placement: 'outside'
		}
	});

	// 凡例の2番目のマーカーを非表示
	$("#chart_chanel_ken .jqplot-table-legend-swatch-outline:eq(1)").css("display", "none");
	// 凡例の2番目のラベルを非表示
	$("#chart_chanel_ken .jqplot-table-legend-label:eq(1)").css("display", "none");
	// 凡例の位置調整
	$("#chart_chanel_ken .jqplot-table-legend").css("left", "47px");

	// ハイライト非表示
	$('#chart_chanel_ken').mouseout(function(e){
		$('.jqplot-highlighter-tooltip').css("position","absolute").css("display", "none");
	});
}

// 国内受注実績　チャネル別受注単価
function chart_chanel_tan(){

	// グラフ破棄
	if (obj_chart_chanel_tan) {
		obj_chart_chanel_tan.destroy();
	}

	// グラフ用情報
	var s1 = [];// 当年 通販
	var s2 = [];// 前年 通販
	var s3 = [];// 当年 店舗
	var s4 = [];// 前年 店舗
	for(i=0;i<db.length;i++){
		// KB=115:国内受注実績　チャネル別　受注単価
		if (db[i]["KB"]==="115"){
			if (db[i]["AXIS"]==="1"){s1.push(parseFloat(db[i]["VAL"]));}	// 当年　通販
			if (db[i]["AXIS"]==="2"){s2.push(parseFloat(db[i]["VAL"]));}	// 前年　通販
			if (db[i]["AXIS"]==="3"){s3.push(parseFloat(db[i]["VAL"]));}	// 当年　店舗
			if (db[i]["AXIS"]==="4"){s4.push(parseFloat(db[i]["VAL"]));}	// 前年　店舗
		}
	}
	if (s1.length===0 || s2.length===0 || s3.length===0 || s4.length===0)	return;

	var ticks = ['スマホ　','アプリ　','PC　　','電話　　','通販その他','　店舗'];
	var labels = ['通販',' ','店舗','前年同月'];

	obj_chart_chanel_tan = $.jqplot('chart_chanel_tan', [s1, s2, s3, s4], {
		stackSeries: false,
		captureRightClick: true,
		grid: {
			background: '#ffffff',
			borderWidth: 1,
			shadow: false
		},
		seriesDefaults:{
			renderer:$.jqplot.BarRenderer,
			rendererOptions: {
				barMargin: 20,
				barWidth:10,
				barPadding: 0,
				shadow: false,
				highlightMouseDown: true
			},
			pointLabels: {show: false}
		},
		series: [
			{
				renderer:$.jqplot.BarRenderer,
				rendererOptions:{
					barPadding:-5
				},
				color: '#F9A45E'
			},
			{
				renderer:$.jqplot.BarRenderer,
				rendererOptions:{
					barWidth: 5,
					barPadding:-4
				},
				color: '#DCDDDD'
			},
			{
				renderer:$.jqplot.BarRenderer,
				rendererOptions:{
					barPadding:-10
				},
				color: '#759FD4'
			},
			{
				renderer:$.jqplot.BarRenderer,
				rendererOptions:{
					barWidth: 5,
					barPadding: 0
				},
				color: '#DCDDDD'
			}
		],
		axes: {
			xaxis: {
				renderer: $.jqplot.CategoryAxisRenderer,
				ticks: ticks,
				tickOptions: {
					fontSize: '6pt',
					showGridline: false,
					formatString:'%d'
				}
			},
			yaxis: {
				padMin: 0,
				min: 0,
				tickOptions: {
					formatString: "%'d"
				},
				autoscale: true
			}
		},
		highlighter: {
			show: true,
			tooltipContentEditor: function (str, seriesIndex, pointIndex, plot) {

				var prefix = ticks[pointIndex]+"<br>";
				var value = plot.data[seriesIndex][pointIndex];
				var suffix = " 円";

				switch(seriesIndex) {
				case 0:
					prefix = "当月 " + prefix;
					suffix = " 円"
					break;
				case 1:
					prefix = "前年同月 " + prefix;
					suffix = " 円"
					break;
				case 2:
					prefix = "当月 " + prefix;
					suffix = " 円"
					break;
				case 3:
					prefix = "前年同月 " + prefix;
					suffix = " 円"
					break;
				}

				var html = "<div class='highlighter'>";
				html += prefix;
				html += String(Math.round((value+0)*1)).replace( /(\d)(?=(\d\d\d)+(?!\d))/g, '$1,');
				html += suffix;
				html += "  </div>";

				return html;
			},
			showMarker: false,
			tooltipLocation: 'n',
			tooltipAxes: 'y',
			formatString: '%s'
		},
		legend: {
			show: true,
			renderer: $.jqplot.EnhancedLegendRenderer,
			rendererOptions: {
				seriesToggle: false,
				numberRows: 1
			},
			fontSize: '0.6em',
			location: 's',
			labels: labels,
			marginTop : "40px",
			placement: 'outside'
		}
	});

	// 凡例の2番目のマーカーを非表示
	$("#chart_chanel_tan .jqplot-table-legend-swatch-outline:eq(1)").css("display", "none");
	// 凡例の2番目のラベルを非表示
	$("#chart_chanel_tan .jqplot-table-legend-label:eq(1)").css("display", "none");
	// 凡例の位置調整
	$("#chart_chanel_tan .jqplot-table-legend").css("left", "47px");

	// ハイライト非表示
	$('#chart_chanel_tan').mouseout(function(e){
		$('.jqplot-highlighter-tooltip').css("position","absolute").css("display", "none");
	});
}

//******************************************************************************************************************

//分野別購入額
function chart_cls_gak(){

	// グラフ破棄
	if (obj_chart_cls_gak){
		obj_chart_cls_gak.destroy();
	}

	// グラフ用情報
	var ticks = [];// ラベル
	var s1 = [];// 当年
	var s2 = [];// 前年
	for(i=0;i<db.length;i++){
		// KB=201:分野別　購入額
		if (db[i]["KB"]==="201"){
			if (db[i]["AXIS"]==="0"){ticks.push(db[i]["DISPLAY"]);}				// ラベル
			if (db[i]["AXIS"]==="1"){s1.push(parseFloat(db[i]["VAL"]));}	// 当年
			if (db[i]["AXIS"]==="2"){s2.push(parseFloat(db[i]["VAL"]));}	// 前年
		}
	}
	if (ticks.length===0 || s1.length===0 || s2.length===0)	return;

	var labels = ['当月','前年同月'];

	obj_chart_cls_gak = $.jqplot('chart_cls_gak', [s1, s2], {
		stackSeries: false,
		captureRightClick: true,
		grid: {
			background: '#ffffff',
			borderWidth: 1,
			shadow: false
		},
		seriesDefaults:{
			renderer:$.jqplot.BarRenderer,
			rendererOptions: {
				barMargin: 30,
				barWidth: 20,
				barPadding: 0,
				shadow: false,
				highlightMouseDown: true
			},
			pointLabels: {show: false}
		},
		series: [
			{
				renderer:$.jqplot.BarRenderer,
				color: '#F27292'
			},
			{
				renderer:$.jqplot.BarRenderer,
				rendererOptions: {
					barWidth: 10
				},
				color: '#DCDDDD'
			}
		],
		axes: {
			xaxis: {
				renderer: $.jqplot.CategoryAxisRenderer,
				ticks: ticks,
//				tickRenderer: $.jqplot.CanvasAxisTickRenderer,
				tickOptions: {
					fontSize: '8pt',
					showGridline: false,
					formatString:'%d'
				}
			},
			yaxis: {
				padMin: 0,
				min: 0,
				tickOptions: {
					formatString:"%'d"
				},
				autoscale: true
			}
		},
		highlighter: {
			show: true,
			tooltipContentEditor: function (str, seriesIndex, pointIndex, plot) {

				var prefix = ticks[pointIndex]+"<br>";
				var value = plot.data[seriesIndex][pointIndex];
				var suffix = "";

				switch(seriesIndex) {
				case 0:
					prefix = labels[seriesIndex] + ' ' + prefix;
					suffix = " 円"
					break;
				case 1:
					prefix = labels[seriesIndex] + ' ' + prefix;
					suffix = " 円"
					break;
				}

				var html = "<div class='highlighter'>";
				html += prefix;
				html += String(Math.round((value+0)*10000)).replace( /(\d)(?=(\d\d\d)+(?!\d))/g, '$1,');
				html += suffix;
				html += "  </div>";

				return html;
			},
			showMarker: false,
			tooltipLocation: 'n',
			tooltipAxes: 'y',
			formatString: '%s'
		},
		legend: {
			show: true,
			renderer: $.jqplot.EnhancedLegendRenderer,
			rendererOptions: {
				seriesToggle: false,
				numberRows: 1
			},
			fontSize: '0.6em',
			location: 's',
			labels: labels,
			marginTop : "40px",
			placement: 'outside'
		}
	});
	// ハイライト非表示
	$('#chart_cls_gak').mouseout(function(e){
		$('.jqplot-highlighter-tooltip').css("position","absolute").css("display", "none");
	});
}

// 分野別購入者
function chart_cls_kyk(){

	// グラフ破棄
	if (obj_chart_cls_kyk) {
		obj_chart_cls_kyk.destroy();
	}

	// グラフ用情報
	var ticks = [];// ラベル
	var s1 = [];// 当年
	var s2 = [];// 前年
	for(i=0;i<db.length;i++){
		// KB=202:分野別　購入者
		if (db[i]["KB"]==="202"){
			if (db[i]["AXIS"]==="0"){ticks.push(db[i]["DISPLAY"]);}				// ラベル
			if (db[i]["AXIS"]==="1"){s1.push(parseFloat(db[i]["VAL"]));}	// 当年
			if (db[i]["AXIS"]==="2"){s2.push(parseFloat(db[i]["VAL"]));}	// 前年
		}
	}
	if (ticks.length===0 || s1.length===0 || s2.length===0)	return;

	var labels = ['当月','前年同月'];

	obj_chart_cls_kyk = $.jqplot('chart_cls_kyk', [s1, s2], {
		stackSeries: false,
		captureRightClick: true,
		grid: {
			background: '#ffffff',
			borderWidth: 1,
			shadow: false
		},
		seriesDefaults:{
			renderer:$.jqplot.BarRenderer,
			rendererOptions: {
				barMargin: 30,
				barWidth: 20,
				barPadding: 0,
				shadow: false,
				highlightMouseDown: true
			},
			pointLabels: {show: false}
		},
		series: [
			{
				renderer:$.jqplot.BarRenderer,
				color: '#F27292'
			},
			{
				renderer:$.jqplot.BarRenderer,
				rendererOptions: {
					barWidth: 10
				},
				color: '#DCDDDD'
			}
		],
		axes: {
			xaxis: {
				renderer: $.jqplot.CategoryAxisRenderer,
				ticks: ticks,
//				tickRenderer: $.jqplot.CanvasAxisTickRenderer,
				tickOptions: {
					fontSize: '8pt',
					showGridline: false,
					formatString:'%d'
				}
			},
			yaxis: {
				padMin: 0,
				min: 0,
				tickOptions: {
					formatString:"%'d"
				},
				autoscale: true
			}
		},
		highlighter: {
			show: true,
			tooltipContentEditor: function (str, seriesIndex, pointIndex, plot) {

				var prefix = ticks[pointIndex]+"<br>";
				var value = plot.data[seriesIndex][pointIndex];
				var suffix = "";

				switch(seriesIndex) {
				case 0:
					prefix = labels[seriesIndex] + ' ' + prefix;
					suffix = " 人"
					break;
				case 1:
					prefix = labels[seriesIndex] + ' ' + prefix;
					suffix = " 人"
					break;
				}

				var html = "<div class='highlighter'>";
				html += prefix;
				html += String(Math.round((value+0)*10000)).replace( /(\d)(?=(\d\d\d)+(?!\d))/g, '$1,');
				html += suffix;
				html += "  </div>";

				return html;
			},
			showMarker: false,
			tooltipLocation: 'n',
			tooltipAxes: 'y',
			formatString: '%s'
		},
		legend: {
			show: true,
			renderer: $.jqplot.EnhancedLegendRenderer,
			rendererOptions: {
				seriesToggle: false,
				numberRows: 1
			},
			fontSize: '0.6em',
			location: 's',
			labels: labels,
			marginTop : "40px",
			placement: 'outside'
		}
	});
	// ハイライト非表示
	$('#chart_cls_kyk').mouseout(function(e){
		$('.jqplot-highlighter-tooltip').css("position","absolute").css("display", "none");
	});
}

// 分野別顧客単価
function chart_cls_tan(){

	// グラフ破棄
	if (obj_chart_cls_tan) {
		obj_chart_cls_tan.destroy();
	}

	// グラフ用情報
	var ticks = [];// ラベル
	var s1 = [];// 当年
	var s2 = [];// 前年
	for(i=0;i<db.length;i++){
		// KB=203:分野別　顧客単価
		if (db[i]["KB"]==="203"){
			if (db[i]["AXIS"]==="0"){ticks.push(db[i]["DISPLAY"]);}				// ラベル
			if (db[i]["AXIS"]==="1"){s1.push(parseFloat(db[i]["VAL"]));}	// 当年
			if (db[i]["AXIS"]==="2"){s2.push(parseFloat(db[i]["VAL"]));}	// 前年
		}
	}
	if (ticks.length===0 || s1.length===0 || s2.length===0)	return;

	var labels = ['当月','前年同月'];

	obj_chart_cls_tan = $.jqplot('chart_cls_tan', [s1, s2], {
		stackSeries: false,
		captureRightClick: true,
		grid: {
			background: '#ffffff',
			borderWidth: 1,
			shadow: false
		},
		seriesDefaults:{
			renderer:$.jqplot.BarRenderer,
			rendererOptions: {
				barMargin: 30,
				barWidth: 20,
				barPadding: 0,
				shadow: false,
				highlightMouseDown: true
			},
			pointLabels: {show: false}
		},
		series: [
			{
				renderer:$.jqplot.BarRenderer,
				color: '#F27292'
			},
			{
				renderer:$.jqplot.BarRenderer,
				rendererOptions: {
					barWidth: 10
				},
				color: '#DCDDDD'
			}
		],
		axes: {
			xaxis: {
				renderer: $.jqplot.CategoryAxisRenderer,
				ticks: ticks,
//				tickRenderer: $.jqplot.CanvasAxisTickRenderer,
				tickOptions: {
					fontSize: '8pt',
					showGridline: false,
					formatString:'%d'
				}
			},
			yaxis: {
				padMin: 0,
				min: 0,
				tickOptions: {
					formatString:"%'d"
				},
				autoscale: true
			}
		},
		highlighter: {
			show: true,
			tooltipContentEditor: function (str, seriesIndex, pointIndex, plot) {

				var prefix = ticks[pointIndex]+"<br>";
				var value = plot.data[seriesIndex][pointIndex];
				var suffix = "";

				switch(seriesIndex) {
				case 0:
					prefix = labels[seriesIndex] + ' ' + prefix;
					suffix = " 円"
					break;
				case 1:
					prefix = labels[seriesIndex] + ' ' + prefix;
					suffix = " 円"
					break;
				}

				var html = "<div class='highlighter'>";
				html += prefix;
				html += String(Math.round((value+0)*1)).replace( /(\d)(?=(\d\d\d)+(?!\d))/g, '$1,');
				html += suffix;
				html += "  </div>";

				return html;
			},
			showMarker: false,
			tooltipLocation: 'n',
			tooltipAxes: 'y',
			formatString: '%s'
		},
		legend: {
			show: true,
			renderer: $.jqplot.EnhancedLegendRenderer,
			rendererOptions: {
				seriesToggle: false,
				numberRows: 1
			},
			location: 's',
			fontSize: '0.6em',
			labels: labels,
			marginTop : "40px",
			placement: 'outside'
		}
	});
	// ハイライト非表示
	$('#chart_cls_tan').mouseout(function(e){
		$('.jqplot-highlighter-tooltip').css("position","absolute").css("display", "none");
	});
}

// カテゴリ別購入額
function chart_cat_gak(){

	// グラフ破棄
	if (obj_chart_cat_gak) {
		obj_chart_cat_gak.destroy();
	}

	// グラフ用情報
	var ticks = [];// ラベル
	var s1 = [];// 当年
	var s2 = [];// 前年
	for(i=0;i<db.length;i++){
		// KB=204:カテゴリ別　購入額
		if (db[i]["KB"]==="204"){
			if (db[i]["AXIS"]==="0"){ticks.push(db[i]["DISPLAY"]);}				// ラベル
			if (db[i]["AXIS"]==="1"){s1.push(parseFloat(db[i]["VAL"]));}	// 当年
			if (db[i]["AXIS"]==="2"){s2.push(parseFloat(db[i]["VAL"]));}	// 前年
		}
	}
	if (ticks.length===0 || s1.length===0 || s2.length===0)	return;

	var labels = ['当月','前年同月'];

	obj_chart_cat_gak = $.jqplot('chart_cat_gak', [s1, s2], {
		stackSeries: false,
		captureRightClick: true,
		grid: {
			background: '#ffffff',
			borderWidth: 1,
			shadow: false
		},
		seriesDefaults:{
			renderer:$.jqplot.BarRenderer,
			rendererOptions: {
				barMargin: 10,
				barWidth: 15,
				barPadding: 0,
				shadow: false,
				highlightMouseDown: true
			},
			pointLabels: {show: false}
		},
		series: [
			{
				renderer:$.jqplot.BarRenderer,
				color: '#B371AF'
			},
			{
				renderer:$.jqplot.BarRenderer,
				rendererOptions: {
					barWidth: 10
				},
				color: '#DCDDDD'
			}
		],
		axes: {
			xaxis: {
				renderer: $.jqplot.CategoryAxisRenderer,
				ticks: ticks,
				tickRenderer: $.jqplot.CanvasAxisTickRenderer,
				tickOptions: {
					angle: -20,
					fontSize: '8pt',
					showGridline: false,
					formatString:'%d'
				}
			},
			yaxis: {
				padMin: 0,
				min: 0,
				tickOptions: {
					formatString:"%'d"
				},
				autoscale: true
			}
		},
		highlighter: {
			show: true,
			tooltipContentEditor: function (str, seriesIndex, pointIndex, plot) {

				var prefix = ticks[pointIndex]+"<br>";
				var value = plot.data[seriesIndex][pointIndex];
				var suffix = "";

				switch(seriesIndex) {
				case 0:
					prefix = labels[seriesIndex] + ' ' + prefix;
					suffix = " 円"
					break;
				case 1:
					prefix = labels[seriesIndex] + ' ' + prefix;
					suffix = " 円"
					break;
				}

				var html = "<div class='highlighter'>";
				html += prefix;
				html += String(Math.round((value+0)*10000)).replace( /(\d)(?=(\d\d\d)+(?!\d))/g, '$1,');
				html += suffix;
				html += "  </div>";

				return html;
			},
			showMarker: false,
			tooltipLocation: 'n',
			tooltipAxes: 'y',
			formatString: '%s'
		},
		legend: {
			show: true,
			renderer: $.jqplot.EnhancedLegendRenderer,
			rendererOptions: {
				seriesToggle: false,
				numberRows: 1
			},
			location: 's',
			fontSize: '0.6em',
			labels: labels,
			marginTop : "60px",
			placement: 'outside'
		}
	});
	// ハイライト非表示
	$('#chart_cat_gak').mouseout(function(e){
		$('.jqplot-highlighter-tooltip').css("position","absolute").css("display", "none");
	});
}

// カテゴリ別購入者
function chart_cat_kyk(){

	// グラフ破棄
	if (obj_chart_cat_kyk) {
		obj_chart_cat_kyk.destroy();
	}

	// グラフ用情報
	var ticks = [];// ラベル
	var s1 = [];// 当年
	var s2 = [];// 前年
	for(i=0;i<db.length;i++){
		// KB=205:カテゴリ別　購入者
		if (db[i]["KB"]==="205"){
			if (db[i]["AXIS"]==="0"){ticks.push(db[i]["DISPLAY"]);}				// ラベル
			if (db[i]["AXIS"]==="1"){s1.push(parseFloat(db[i]["VAL"]));}	// 当年
			if (db[i]["AXIS"]==="2"){s2.push(parseFloat(db[i]["VAL"]));}	// 前年
		}
	}
	if (ticks.length===0 || s1.length===0 || s2.length===0)	return;

	var labels = ['当月','前年同月'];

	obj_chart_cat_kyk = $.jqplot('chart_cat_kyk', [s1, s2], {
		stackSeries: false,
		captureRightClick: true,
		grid: {
			background: '#ffffff',
			borderWidth: 1,
			shadow: false
		},
		seriesDefaults:{
			renderer:$.jqplot.BarRenderer,
			rendererOptions: {
				barMargin: 10,
				barWidth: 15,
				barPadding: 0,
				shadow: false,
				highlightMouseDown: true
			},
			pointLabels: {show: false}
		},
		series: [
			{
				renderer:$.jqplot.BarRenderer,
				color: '#B371AF'
			},
			{
				renderer:$.jqplot.BarRenderer,
				rendererOptions: {
					barWidth: 10
				},
				color: '#DCDDDD'
			}
		],
		axes: {
			xaxis: {
				renderer: $.jqplot.CategoryAxisRenderer,
				ticks: ticks,
				tickRenderer: $.jqplot.CanvasAxisTickRenderer,
				tickOptions: {
					angle: -20,
					fontSize: '8pt',
					showGridline: false,
					formatString:'%d'
				}
			},
			yaxis: {
				padMin: 0,
				min: 0,
				tickOptions: {
					formatString:"%'d"
				},
				autoscale: true
			}
		},
		highlighter: {
			show: true,
			tooltipContentEditor: function (str, seriesIndex, pointIndex, plot) {

				var prefix = ticks[pointIndex]+"<br>";
				var value = plot.data[seriesIndex][pointIndex];
				var suffix = "";

				switch(seriesIndex) {
				case 0:
					prefix = labels[seriesIndex] + ' ' + prefix;
					suffix = " 人"
					break;
				case 1:
					prefix = labels[seriesIndex] + ' ' + prefix;
					suffix = " 人"
					break;
				}

				var html = "<div class='highlighter'>";
				html += prefix;
				html += String(Math.round((value+0)*10000)).replace( /(\d)(?=(\d\d\d)+(?!\d))/g, '$1,');
				html += suffix;
				html += "  </div>";

				return html;
			},
			showMarker: false,
			tooltipLocation: 'n',
			tooltipAxes: 'y',
			formatString: '%s'
		},
		legend: {
			show: true,
			renderer: $.jqplot.EnhancedLegendRenderer,
			rendererOptions: {
				seriesToggle: false,
				numberRows: 1
			},
			location: 's',
			fontSize: '0.6em',
			labels: labels,
			marginTop : "60px",
			placement: 'outside'
		}
	});
	// ハイライト非表示
	$('#chart_cat_kyk').mouseout(function(e){
		$('.jqplot-highlighter-tooltip').css("position","absolute").css("display", "none");
	});
}

// カテゴリ別　顧客単価
function chart_cat_tan(){

	// グラフ破棄
	if (obj_chart_cat_tan) {
		obj_chart_cat_tan.destroy();
	}

	// グラフ用情報
	var ticks = [];// ラベル
	var s1 = [];// 当年
	var s2 = [];// 前年
	for(i=0;i<db.length;i++){
		// KB=206:カテゴリ別　顧客単価
		if (db[i]["KB"]==="206"){
			if (db[i]["AXIS"]==="0"){ticks.push(db[i]["DISPLAY"]);}				// ラベル
			if (db[i]["AXIS"]==="1"){s1.push(parseFloat(db[i]["VAL"]));}	// 当年
			if (db[i]["AXIS"]==="2"){s2.push(parseFloat(db[i]["VAL"]));}	// 前年
		}
	}
	if (ticks.length===0 || s1.length===0 || s2.length===0)	return;

	var labels = ['当月','前年同月'];

	obj_chart_cat_tan = $.jqplot('chart_cat_tan', [s1, s2], {
		stackSeries: false,
		captureRightClick: true,
		grid: {
			background: '#ffffff',
			borderWidth: 1,
			shadow: false
		},
		seriesDefaults:{
			renderer:$.jqplot.BarRenderer,
			rendererOptions: {
				barMargin: 10,
				barWidth: 15,
				barPadding: 0,
				shadow: false,
				highlightMouseDown: true
			},
			pointLabels: {show: false}
		},
		series: [
			{
				renderer:$.jqplot.BarRenderer,
				color: '#B371AF'
			},
			{
				renderer:$.jqplot.BarRenderer,
				rendererOptions: {
					barWidth: 10
				},
				color: '#DCDDDD'
			}
		],
		axes: {
			xaxis: {
				renderer: $.jqplot.CategoryAxisRenderer,
				ticks: ticks,
				tickRenderer: $.jqplot.CanvasAxisTickRenderer,
				tickOptions: {
					angle: -20,
					fontSize: '8pt',
					showGridline: false,
					formatString:'%d'
				}
			},
			yaxis: {
				padMin: 0,
				min: 0,
				tickOptions: {
					formatString:"%'d"
				},
				autoscale: true
			}
		},
		highlighter: {
			show: true,
			tooltipContentEditor: function (str, seriesIndex, pointIndex, plot) {

				var prefix = ticks[pointIndex]+"<br>";
				var value = plot.data[seriesIndex][pointIndex];
				var suffix = "";

				switch(seriesIndex) {
				case 0:
					prefix = labels[seriesIndex] + ' ' + prefix;
					suffix = " 円"
					break;
				case 1:
					prefix = labels[seriesIndex] + ' ' + prefix;
					suffix = " 円"
					break;
				}

				var html = "<div class='highlighter'>";
				html += prefix;
				html += String(Math.round((value+0)*1)).replace( /(\d)(?=(\d\d\d)+(?!\d))/g, '$1,');
				html += suffix;
				html += "  </div>";

				return html;
			},
			showMarker: false,
			tooltipLocation: 'n',
			tooltipAxes: 'y',
			formatString: '%s'
		},
		legend: {
			show: true,
			renderer: $.jqplot.EnhancedLegendRenderer,
			rendererOptions: {
				seriesToggle: false,
				numberRows: 1
			},
			location: 's',
			fontSize: '0.6em',
			labels: labels,
			marginTop : "60px",
			placement: 'outside'
		}
	});
	// ハイライト非表示
	$('#chart_cat_tan').mouseout(function(e){
		$('.jqplot-highlighter-tooltip').css("position","absolute").css("display", "none");
	});
}

// 重点商材別　購入額
function chart_cho_gak(){

	// グラフ破棄
	if (obj_chart_cho_gak) {
		obj_chart_cho_gak.destroy();
	}

	// グラフ用情報
	var ticks = [];// ラベル
	var s1 = [];// 当年
	var s2 = [];// 前年
	for(i=0;i<db.length;i++){
		// KB=207:重点商材別　購入額
		if (db[i]["KB"]==="207"){
			if (db[i]["AXIS"]==="0"){ticks.push(db[i]["DISPLAY"]);}				// ラベル
			if (db[i]["AXIS"]==="1"){s1.push(parseFloat(db[i]["VAL"]));}	// 当年
			if (db[i]["AXIS"]==="2"){s2.push(parseFloat(db[i]["VAL"]));}	// 前年
		}
	}
	if (ticks.length===0 || s1.length===0 || s2.length===0)	return;

	var labels = ['当月','前年同月'];

	obj_chart_cho_gak = $.jqplot('chart_cho_gak', [s1, s2], {
		stackSeries: false,
		captureRightClick: true,
		grid: {
			background: '#ffffff',
			borderWidth: 1,
			shadow: false
		},
		seriesDefaults:{
			renderer:$.jqplot.BarRenderer,
			rendererOptions: {
				barMargin: 10,
				barWidth: 15,
				barPadding: 0,
				shadow: false,
				highlightMouseDown: true
			},
			pointLabels: {show: false}
		},
		series: [
			{
				renderer:$.jqplot.BarRenderer,
				color: '#33C3E0'
			},
			{
				renderer:$.jqplot.BarRenderer,
				rendererOptions: {
					barWidth: 10
				},
				color: '#DCDDDD'
			}
		],
		axes: {
			xaxis: {
				renderer: $.jqplot.CategoryAxisRenderer,
				ticks: ticks,
//				tickRenderer: $.jqplot.CanvasAxisTickRenderer,
				tickOptions: {
					fontSize: '8pt',
					showGridline: false,
					formatString:'%d'
				}
			},
			yaxis: {
				padMin: 0,
				min: 0,
				tickOptions: {
					formatString:"%'d"
				},
				autoscale: true
			}
		},
		highlighter: {
			show: true,
			tooltipContentEditor: function (str, seriesIndex, pointIndex, plot) {

				var prefix = ticks[pointIndex]+"<br>";
				var value = plot.data[seriesIndex][pointIndex];
				var suffix = "";

				switch(seriesIndex) {
				case 0:
					prefix = labels[seriesIndex] + ' ' + prefix;
					suffix = " 円"
					break;
				case 1:
					prefix = labels[seriesIndex] + ' ' + prefix;
					suffix = " 円"
					break;
				}

				var html = "<div class='highlighter'>";
				html += prefix;
				html += String(Math.round((value+0)*10000)).replace( /(\d)(?=(\d\d\d)+(?!\d))/g, '$1,');
				html += suffix;
				html += "  </div>";

				return html;
			},
			showMarker: false,
			tooltipLocation: 'n',
			tooltipAxes: 'y',
			formatString: '%s'
		},
		legend: {
			show: true,
			renderer: $.jqplot.EnhancedLegendRenderer,
			rendererOptions: {
				seriesToggle: false,
				numberRows: 1
			},
			location: 's',
			fontSize: '0.6em',
			labels: labels,
			marginTop : "40px",
			placement: 'outside'
		}
	});
	// ハイライト非表示
	$('#chart_cho_gak').mouseout(function(e){
		$('.jqplot-highlighter-tooltip').css("position","absolute").css("display", "none");
	});
}

// 重点商材別　購入者
function chart_cho_kyk(){

	// グラフ破棄
	if (obj_chart_cho_kyk) {
		obj_chart_cho_kyk.destroy();
	}

	// グラフ用情報
	var ticks = [];// ラベル
	var s1 = [];// 当年
	var s2 = [];// 前年
	for(i=0;i<db.length;i++){
		// KB=208:重点商材別　購入者
		if (db[i]["KB"]==="208"){
			if (db[i]["AXIS"]==="0"){ticks.push(db[i]["DISPLAY"]);}				// ラベル
			if (db[i]["AXIS"]==="1"){s1.push(parseFloat(db[i]["VAL"]));}	// 当年
			if (db[i]["AXIS"]==="2"){s2.push(parseFloat(db[i]["VAL"]));}	// 前年
		}
	}
	if (ticks.length===0 || s1.length===0 || s2.length===0)	return;

	var labels = ['当月','前年同月'];

	obj_chart_cho_kyk = $.jqplot('chart_cho_kyk', [s1, s2], {
		stackSeries: false,
		captureRightClick: true,
		grid: {
			background: '#ffffff',
			borderWidth: 1,
			shadow: false
		},
		seriesDefaults:{
			renderer:$.jqplot.BarRenderer,
			rendererOptions: {
				barMargin: 10,
				barWidth: 15,
				barPadding: 0,
				shadow: false,
				highlightMouseDown: true
			},
			pointLabels: {show: false}
		},
		series: [
			{
				renderer:$.jqplot.BarRenderer,
				color: '#33C3E0'
			},
			{
				renderer:$.jqplot.BarRenderer,
				rendererOptions: {
					barWidth: 10
				},
				color: '#DCDDDD'
			}
		],
		axes: {
			xaxis: {
				renderer: $.jqplot.CategoryAxisRenderer,
				ticks: ticks,
//				tickRenderer: $.jqplot.CanvasAxisTickRenderer,
				tickOptions: {
					fontSize: '8pt',
					showGridline: false,
					formatString:'%d'
				}
			},
			yaxis: {
				padMin: 0,
				min: 0,
				tickOptions: {
					formatString:"%'d"
				},
				autoscale: true
			}
		},
		highlighter: {
			show: true,
			tooltipContentEditor: function (str, seriesIndex, pointIndex, plot) {

				var prefix = ticks[pointIndex]+"<br>";
				var value = plot.data[seriesIndex][pointIndex];
				var suffix = "";

				switch(seriesIndex) {
				case 0:
					prefix = labels[seriesIndex] + ' ' + prefix;
					suffix = " 人"
					break;
				case 1:
					prefix = labels[seriesIndex] + ' ' + prefix;
					suffix = " 人"
					break;
				}

				var html = "<div class='highlighter'>";
				html += prefix;
				html += String(Math.round((value+0)*10000)).replace( /(\d)(?=(\d\d\d)+(?!\d))/g, '$1,');
				html += suffix;
				html += "  </div>";

				return html;
			},
			showMarker: false,
			tooltipLocation: 'n',
			tooltipAxes: 'y',
			formatString: '%s'
		},
		legend: {
			show: true,
			renderer: $.jqplot.EnhancedLegendRenderer,
			rendererOptions: {
				seriesToggle: false,
				numberRows: 1
			},
			location: 's',
			fontSize: '0.6em',
			labels: labels,
			marginTop : "40px",
			placement: 'outside'
		}
	});
	// ハイライト非表示
	$('#chart_cho_kyk').mouseout(function(e){
		$('.jqplot-highlighter-tooltip').css("position","absolute").css("display", "none");
	});
}

// 重点商材別　顧客単価
function chart_cho_tan(){

	// グラフ破棄
	if (obj_chart_cho_tan) {
		obj_chart_cho_tan.destroy();
	}

	// グラフ用情報
	var ticks = [];// ラベル
	var s1 = [];// 当年
	var s2 = [];// 前年
	for(i=0;i<db.length;i++){
		// KB=209:重点商材別　顧客単価
		if (db[i]["KB"]==="209"){
			if (db[i]["AXIS"]==="0"){ticks.push(db[i]["DISPLAY"]);}				// ラベル
			if (db[i]["AXIS"]==="1"){s1.push(parseFloat(db[i]["VAL"]));}	// 当年
			if (db[i]["AXIS"]==="2"){s2.push(parseFloat(db[i]["VAL"]));}	// 前年
		}
	}
	if (ticks.length===0 || s1.length===0 || s2.length===0)	return;

	var labels = ['当月','前年同月'];

	obj_chart_cho_tan = $.jqplot('chart_cho_tan', [s1, s2], {
		stackSeries: false,
		captureRightClick: true,
		grid: {
			background: '#ffffff',
			borderWidth: 1,
			shadow: false
		},
		seriesDefaults:{
			renderer:$.jqplot.BarRenderer,
			rendererOptions: {
				barMargin: 10,
				barWidth: 15,
				barPadding: 0,
				shadow: false,
				highlightMouseDown: true
			},
			pointLabels: {show: false}
		},
		series: [
			{
				renderer:$.jqplot.BarRenderer,
				color: '#33C3E0'
			},
			{
				renderer:$.jqplot.BarRenderer,
				rendererOptions: {
					barWidth: 10
				},
				color: '#DCDDDD'
			}
		],
		axes: {
			xaxis: {
				renderer: $.jqplot.CategoryAxisRenderer,
				ticks: ticks,
//				tickRenderer: $.jqplot.CanvasAxisTickRenderer,
				tickOptions: {
					fontSize: '8pt',
					showGridline: false,
					formatString:'%d'
				}
			},
			yaxis: {
				padMin: 0,
				min: 0,
				tickOptions: {
					formatString:"%'d"
				},
				autoscale: true
			}
		},
		highlighter: {
			show: true,
			tooltipContentEditor: function (str, seriesIndex, pointIndex, plot) {

				var prefix = ticks[pointIndex]+"<br>";
				var value = plot.data[seriesIndex][pointIndex];
				var suffix = "";

				switch(seriesIndex) {
				case 0:
					prefix = labels[seriesIndex] + ' ' + prefix;
					suffix = " 円"
					break;
				case 1:
					prefix = labels[seriesIndex] + ' ' + prefix;
					suffix = " 円"
					break;
				}

				var html = "<div class='highlighter'>";
				html += prefix;
				html += String(Math.round((value+0)*1)).replace( /(\d)(?=(\d\d\d)+(?!\d))/g, '$1,');
				html += suffix;
				html += "  </div>";

				return html;
			},
			showMarker: false,
			tooltipLocation: 'n',
			tooltipAxes: 'y',
			formatString: '%s'
		},
		legend: {
			show: true,
			renderer: $.jqplot.EnhancedLegendRenderer,
			rendererOptions: {
				seriesToggle: false,
				numberRows: 1
			},
			location: 's',
			fontSize: '0.6em',
			labels: labels,
			marginTop : "40px",
			placement: 'outside'
		}
	});
	// ハイライト非表示
	$('#chart_cho_tan').mouseout(function(e){
		$('.jqplot-highlighter-tooltip').css("position","absolute").css("display", "none");
	});
}

//******************************************************************************************************************
// 新規獲得　日別　初回購入者数
function chart_news_day(){

	// グラフ破棄
	if (obj_chart_news_day) {
		obj_chart_news_day.destroy();
	}

	// グラフ用情報
	var s1 = [];// 当年
	var s2 = [];// 前年
	for(i=0;i<db.length;i++){
		// KB=302:新規獲得　日別　初回受注者数
		if (db[i]["KB"]==="302"){
			if (db[i]["AXIS"]==="1"){s1.push(parseFloat(db[i]["VAL"]));}	// 当年
			if (db[i]["AXIS"]==="2"){s2.push(parseFloat(db[i]["VAL"]));}	// 前年
		}
	}
	if (s1.length===0 || s2.length===0)	return;

	// 日付（土曜、日曜色付け）
	var YY = $("#selectyear").val();
	var MM = $("#selectmonth").val();
	var dt = new Date(parseInt(YY), parseInt(MM), 0);
	var dtEnd = dt.getDate();
	var ticks = [];
	for(i=1;i<=dtEnd;i++){
		// 日付
		dt = new Date(parseInt(YY), parseInt(MM)-1, i);
		if (dt.getDay()===0){// 日曜日
			ticks.push('<span class="sun">'+i+'</span>');
		}else if (dt.getDay()===6){// 土曜日
			ticks.push('<span class="std">'+i+'</span>');
		}else{
			ticks.push(''+i);
		}
	}

	var labels = ['当月', '前年同月'];

	obj_chart_news_day = $.jqplot('chart_news_day', [s1, s2], {
		stackSeries: false,
		captureRightClick: true,
		grid: {
			background: '#ffffff',
			borderWidth: 1,
			shadow: false
		},
		seriesDefaults:{
			renderer:$.jqplot.BarRenderer,
			rendererOptions: {
				barMargin: 0,
				barWidth:6,
				barPadding: 0,
				shadow: false,
				highlightMouseDown: true
			},
			pointLabels: {show: false}
		},
		series: [
			{
				renderer:$.jqplot.BarRenderer,
				rendererOptions:{
					barPadding:-3
				},
				color: '#A5CF4C'
			},
			{
				renderer:$.jqplot.BarRenderer,
				rendererOptions:{
					barPadding:3
				},
				color: '#DCDDDD'
			}
		],
		axes: {
			xaxis: {
				renderer: $.jqplot.CategoryAxisRenderer,
				ticks: ticks,
				tickOptions: {
					fontSize: '8pt',
					showGridline: false,
					formatString:'%d'
				}
			},
			yaxis: {
				padMin: 0,
				min:0,
				tickOptions: {
					formatString: "%'d"
				},
				autoscale: true
			}
		},
		highlighter: {
			show: true,
			tooltipContentEditor: function (str, seriesIndex, pointIndex, plot) {

				var prefix = ticks[pointIndex]+"日<br>";
				var value = plot.data[seriesIndex][pointIndex];
				var suffix = " 人";

				switch(seriesIndex) {
				case 0:
					prefix = labels[seriesIndex] + ' ' + prefix;
					suffix = " 人";
					break;
				case 1:
					prefix = labels[seriesIndex] + ' ' + getTooltipDay(pointIndex+1)+"日<br>";
					suffix = " 人";
					break;
				}

				var html = "<div class='highlighter'>";
				html += prefix;
				html += String(Math.round((value+0)*1)).replace( /(\d)(?=(\d\d\d)+(?!\d))/g, '$1,');
				html += suffix;
				html += "  </div>";

				return html;
			},
			showMarker: false,
			tooltipLocation: 'n',
			tooltipAxes: 'y',
			formatString: '%s'
		},
		legend: {
			show: true,
			renderer: $.jqplot.EnhancedLegendRenderer,
			rendererOptions: {
				seriesToggle: false,
				numberRows: 1
			},
			fontSize: '0.6em',
			location: 's',
			labels: labels,
			marginTop : "40px",
			placement: 'outside'
		}
	});
	// ハイライト非表示
	$('#chart_news_day').mouseout(function(e){
		$('.jqplot-highlighter-tooltip').css("position","absolute").css("display", "none");
	});
}

//新規獲得　月別　初回購入者数
function chart_news_mon(){

	// グラフ破棄
	if (obj_chart_news_mon) {
		obj_chart_news_mon.destroy();
	}

	// グラフ用情報
	var s1 = [];// 当年
	var s2 = [];// 前年
	var s3 = [];// 計画
	for(i=0;i<db.length;i++){
		// KB=303:新規獲得　月別　初回購入者数
		if (db[i]["KB"]==="303"){
			if (db[i]["AXIS"]==="1"){s1.push(parseFloat(db[i]["VAL"]));}	// 当年
			if (db[i]["AXIS"]==="2"){s2.push(parseFloat(db[i]["VAL"]));}	// 前年
			if (db[i]["AXIS"]==="3"){s3.push(parseFloat(db[i]["VAL"]));}	// 計画
		}
	}
	if (s1.length===0 || s2.length===0 || s3.length===0)	return;

	var s4 = [0,,,,,,,,,,,];
	var ticks = ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '　10月', '　11月', '　12月'];
	var labels = [' ','当年', '前年', '計画'];

	obj_chart_news_mon = $.jqplot('chart_news_mon', [s4, s1, s2, s3], {
		stackSeries: false,
		captureRightClick: true,
		grid: {
			background: '#ffffff',
			borderWidth: 1,
			shadow: false
		},
		seriesDefaults:{
			shadow: false,
			renderer:$.jqplot.BarRenderer,
			rendererOptions: {
				barWidth:15,
				barMargin: 0,  // 隣接するカテゴリとの距離。
				barPadding: 0, // 同カテゴリ異系列の、隣接する棒と棒の間の距離。
				shadow: false,
				highlightMouseDown: true
			},
			pointLabels: {show: false}
		},
		series: [
			{
				renderer:$.jqplot.BarRenderer,
				rendererOptions:{
					barWidth: 0,
				},
				color: '#FFFFFF'
			},
			{
				renderer:$.jqplot.BarRenderer,
				color: '#A5CF4C'
			},
			{
				renderer:$.jqplot.BarRenderer,
				rendererOptions:{
					barWidth: 8,
					barPadding: 3
				},
				color: '#DCDDDD'
			},
			{
				renderer: $.jqplot.LineRenderer,
				lineWidth:1.5,
				markerOptions: {
					size: 3,
					style: 'filledSquare'
				},
				color: '#728D1F'
			}
		],
		axes: {
			xaxis: {
				renderer: $.jqplot.CategoryAxisRenderer,
				ticks: ticks,
				tickOptions: {
					fontSize: '8pt',
					showGridline: false,
					formatString:'%d'
				}
			},
			yaxis: {
				padMin: 0,
				min: 0,
				tickOptions: {
					formatString: "%'d"
				},
				autoscale: true
			}
		},
		highlighter: {
			show: true,
			tooltipContentEditor: function (str, seriesIndex, pointIndex, plot) {

				var prefix = ticks[pointIndex]+"<br>";
				var value = plot.data[seriesIndex][pointIndex];
				var suffix = " 人";

				switch(seriesIndex) {
				case 1:
					prefix = labels[seriesIndex] + ' ' + prefix;
					suffix = " 人";
					break;
				case 2:
					prefix = labels[seriesIndex] + ' ' + prefix;
					suffix = " 人";
					break;
				case 3:
					prefix = labels[seriesIndex] + ' ' + prefix;
					suffix = " 人";
					break;
				}

				var html = "<div class='highlighter'>";
				html += prefix;
				html += String(Math.round((value+0)*10000)).replace( /(\d)(?=(\d\d\d)+(?!\d))/g, '$1,');
				html += suffix;
				html += "  </div>";

				return html;
			},
			showMarker: false,
			tooltipLocation: 'n',
			tooltipAxes: 'y',
			formatString: '%s'
		},
		legend: {
			show: true,
			renderer: $.jqplot.EnhancedLegendRenderer,
			rendererOptions: {
				seriesToggle: false,
				numberRows: 1
			},
			fontSize: '0.6em',
			location: 's',
			labels: labels,
			marginTop : "40px",
			placement: 'outside'
			}
	});
	// ハイライト非表示
	$('#chart_news_mon').mouseout(function(e){
		$('.jqplot-highlighter-tooltip').css("position","absolute").css("display", "none");
	});
}

// 新規獲得　月別　初回購入単価
function chart_news_tan(){

	// グラフ破棄
	if (obj_chart_news_tan) {
		obj_chart_news_tan.destroy();
	}

	// グラフ用情報
	var s1 = [];// 当年
	var s2 = [];// 前年
	for(i=0;i<db.length;i++){
		// KB=304:新規獲得　月別　初回購入単価
		if (db[i]["KB"]==="304"){
			if (db[i]["AXIS"]==="1"){s1.push(parseFloat(db[i]["VAL"]));}	// 当年
			if (db[i]["AXIS"]==="2"){s2.push(parseFloat(db[i]["VAL"]));}	// 前年
		}
	}
	if (s1.length===0 || s2.length===0)	return;

	var ticks = ['1月　　', '2月　　', '3月　　', '4月　　', '5月　　', '6月　　', '7月　　', '8月　　', '9月　　', '10月　', '11月　', '12月　'];
	var labels = ['当年', '前年'];

	obj_chart_news_tan = $.jqplot('chart_news_tan', [s1, s2], {
		stackSeries: false,
		captureRightClick: true,
		grid: {
			background: '#ffffff',
			borderWidth: 1,
			shadow: false
		},
		seriesDefaults:{
			renderer:$.jqplot.BarRenderer,
			rendererOptions: {
				barMargin: 0,
				barPadding:0,
				barWidth:15,
				shadow: false,
				highlightMouseDown: true
			},
			pointLabels: {show: false}
		},
		series: [
			{
				renderer:$.jqplot.BarRenderer,
				color: '#A5CF4C'
			},
			{
				renderer:$.jqplot.BarRenderer,
				rendererOptions:{
					barWidth: 8
				},
				color: '#DCDDDD'
			}
		],
		axes: {
			xaxis: {
				renderer: $.jqplot.CategoryAxisRenderer,
				ticks: ticks,
				tickOptions: {
					fontSize: '8pt',
					showGridline: false,
					formatString:'%d'
				}
			},
			yaxis: {
				padMin: 0,
				min: 0,
				tickOptions: {
					formatString:"%'d"
				},
				autoscale: true
			}
		},
		highlighter: {
			show: true,
			tooltipContentEditor: function (str, seriesIndex, pointIndex, plot) {

				var prefix = ticks[pointIndex]+"<br>";
				var value = plot.data[seriesIndex][pointIndex];
				var suffix = "";

				switch(seriesIndex) {
				case 0:
					prefix = labels[seriesIndex] + ' ' + prefix;
					suffix = " 円"
					break;
				case 1:
					prefix = labels[seriesIndex] + ' ' + prefix;
					suffix = " 円"
					break;
				}

				var html = "<div class='highlighter'>";
				html += prefix;
				html += String(Math.round((value+0)*1)).replace( /(\d)(?=(\d\d\d)+(?!\d))/g, '$1,');
				html += suffix;
				html += "  </div>";

				return html;
			},
			showMarker: false,
			tooltipLocation: 'n',
			tooltipAxes: 'y',
			formatString: '%s'
		},
		legend: {
			show: true,
			renderer: $.jqplot.EnhancedLegendRenderer,
			rendererOptions: {
				seriesToggle: false,
				numberRows: 1
			},
			fontSize: '0.6em',
			location: 's',
			labels: labels,
			marginTop : "40px",
			placement: 'outside'
		}
	});
	// ハイライト非表示
	$('#chart_news_tan').mouseout(function(e){
		$('.jqplot-highlighter-tooltip').css("position","absolute").css("display", "none");
	});
}

// リピート活性　月別　Ｒ６以内反応率
function chart_r6_mon(){

	// グラフ破棄
	if (obj_chart_r6_mon) {
		obj_chart_r6_mon.destroy();
	}

	// グラフ用情報
	var s1 = [];// 当年対象者
	var s2 = [];// 前年対象者
	var s3 = [];// 当年反応率
	var s4 = [];// 前年反応率
	for(i=0;i<db.length;i++){
		// KB=306:リピート活性　月別　Ｒ６以内反応率
		if (db[i]["KB"]==="306"){
			if (db[i]["AXIS"]==="1"){s1.push(parseFloat(db[i]["VAL"]));}	// 当年対象者
			if (db[i]["AXIS"]==="2"){s2.push(parseFloat(db[i]["VAL"]));}	// 前年対象者
			if (db[i]["AXIS"]==="3"){s3.push(parseFloat(db[i]["VAL"]));}	// 当年反応率
			if (db[i]["AXIS"]==="4"){s4.push(parseFloat(db[i]["VAL"]));}	// 前年反応率
		}
	}
	if (s1.length===0 || s2.length===0 || s3.length===0 || s4.length===0)	return;

	var ticks = ['1月　　', '2月　　', '3月　　', '4月　　', '5月　　', '6月　　', '7月　　', '8月　　', '9月　　', '10月　', '11月　', '12月　'];
	var labels = ['当年対象者', '前年対象者', '当年反応率', '前年反応率'];

	obj_chart_r6_mon = $.jqplot('chart_r6_mon', [s1, s2, s3, s4], {
		stackSeries: false,
		captureRightClick: true,
		grid: {
			background: '#ffffff',
			borderWidth: 1,
			shadow: false
		},
		seriesDefaults:{
			shadow: false,
			renderer:$.jqplot.BarRenderer,
			rendererOptions: {
				barMargin: 0,
				barWidth:15,
				barPadding: 0,
				highlightMouseDown: true
			},
			pointLabels: {show: false}
		},
		series: [
			{
				renderer:$.jqplot.BarRenderer,
				color: '#A372A4'
			},
			{
				renderer:$.jqplot.BarRenderer,
				rendererOptions:{
					barWidth: 10,
				},
				color: '#DCDDDD'
			},
			{
				disableStack : true,
				renderer: $.jqplot.LineRenderer,
				yaxis:'y2axis',
				lineWidth:1.5,
				markerOptions: {
					size: 3,
					style: 'filledSquare'
				},
				color: '#59245D'
			},
			{
				renderer: $.jqplot.LineRenderer,
				yaxis:'y2axis',
				lineWidth:1.5,
				markerOptions: {
					size: 2,
					style: 'filledCircle'
				},
				rendererOptions: {
					barPadding: 0,
					barMargin: 10
				},
				color: '#9E9BA8'
			}
		],
		axes: {
			xaxis: {
				renderer: $.jqplot.CategoryAxisRenderer,
				ticks: ticks,
				tickOptions: {
					fontSize: '8pt',
					showGridline: false,
					formatString:'%d'
				}
			},
			yaxis: {
				padMin: 0,
				min: 0,
				tickOptions: {
					formatString:"%'d"
				},
				autoscale: true
			},
			y2axis: {
				padMin: 0,
				min: 0,
				tickOptions: {
					showLabel: true,
					showGridline: false,
					formatString:"%.1f%%"
				},
				autoscale: true
			}
		},
		highlighter: {
			show: true,
			tooltipContentEditor: function (str, seriesIndex, pointIndex, plot) {

				var prefix = ticks[pointIndex]+"<br>";
				var value = plot.data[seriesIndex][pointIndex];
				var suffix = "";

				switch(seriesIndex) {
				case 0:
					prefix = labels[seriesIndex] + ' ' + prefix;
					suffix = " 人"
					value = String(Math.round((value+0)*10000)).replace( /(\d)(?=(\d\d\d)+(?!\d))/g, '$1,');
					break;
				case 1:
					prefix = labels[seriesIndex] + ' ' + prefix;
					suffix = " 人"
					value = String(Math.round((value+0)*10000)).replace( /(\d)(?=(\d\d\d)+(?!\d))/g, '$1,');
					break;
				case 2:
					prefix = labels[seriesIndex] + ' ' + prefix;
					suffix = " ％"
					value = String((value+0)*1).replace( /(\d)(?=(\d\d\d)+(?!\d))/g, '$1,');
					break;
				case 3:
					prefix = labels[seriesIndex] + ' ' + prefix;
					suffix = " ％"
					value = String((value+0)*1).replace( /(\d)(?=(\d\d\d)+(?!\d))/g, '$1,');
					break;
				}

				var html = "<div class='highlighter'>";
				html += prefix;
				html += value;
				html += suffix;
				html += "  </div>";

				return html;
			},
			showMarker: false,
			tooltipLocation: 'n',
			tooltipAxes: 'y',
			formatString: '%s'
		},
		legend: {
			show: true,
			renderer: $.jqplot.EnhancedLegendRenderer,
			rendererOptions: {
				seriesToggle: false,
				numberRows: 1
			},
			fontSize: '0.6em',
			location: 's',
			labels: labels,
			marginTop : "40px",
			placement: 'outside'
		}
	});
	// ハイライト非表示
	$('#chart_r6_mon').mouseout(function(e){
		$('.jqplot-highlighter-tooltip').css("position","absolute").css("display", "none");
	});
}

// ポイント発行・使用状況　月別　発行ポイント
function chart_point_hak(){

	// グラフ破棄
	if (obj_chart_point_hak) {
		obj_chart_point_hak.destroy();
	}

	// グラフ用情報
	var s1 = [];// 当年発行
	var s2 = [];// 前年発行
	var s3 = [];// 当年発行率
	var s4 = [];// 前年発行率
	for(i=0;i<db.length;i++){
		// KB=308:月別　発行ポイント
		if (db[i]["KB"]==="308"){
			if (db[i]["AXIS"]==="1"){s1.push(parseFloat(db[i]["VAL"]));}	// 当年発行
			if (db[i]["AXIS"]==="2"){s2.push(parseFloat(db[i]["VAL"]));}	// 前年発行
			if (db[i]["AXIS"]==="3"){s3.push(parseFloat(db[i]["VAL"]));}	// 当年発行率
			if (db[i]["AXIS"]==="4"){s4.push(parseFloat(db[i]["VAL"]));}	// 前年発行率
		}
	}
	if (s1.length===0 || s2.length===0 || s3.length===0 || s4.length===0)	return;

	var ticks = ['1月　　　　', '2月　　　　', '3月　　　　', '4月　　　　', '5月　　　　', '6月　　　　', '7月　　　　', '8月　　　　', '9月　　　　', '10月　　　', '11月　　　', '12月　　　'];
	var labels = ['当年発行PT', '前年発行PT', '当年発行率', '前年発行率'];

	obj_chart_point_hak = $.jqplot('chart_point_hak', [s1, s2, s3, s4], {
		stackSeries: false,
		captureRightClick: true,
		grid: {
			background: '#ffffff',
			borderWidth: 1,
			shadow: false
		},
		seriesDefaults:{
			shadow: false,
			renderer:$.jqplot.BarRenderer,
			rendererOptions: {
				barMargin: 0,
				barWidth:20,
				barPadding: 0,
				highlightMouseDown: true
			},
			pointLabels: {show: false}
		},
		series: [
			{
				renderer:$.jqplot.BarRenderer,
				color: '#9492C7'
			},
			{
				renderer:$.jqplot.BarRenderer,
				rendererOptions:{
					barWidth: 10,
				},
				color: '#DCDDDD'
			},
			{
				renderer: $.jqplot.LineRenderer,
				yaxis:'y2axis',
				lineWidth:1.5,
				markerOptions: {
					size: 3,
					style: 'filledSquare'
				},
				color: '#534975'
			},
			{
				renderer: $.jqplot.LineRenderer,
				yaxis:'y2axis',
				lineWidth:1.5,
				markerOptions: {
					size: 2,
					style: 'filledCircle'
				},
				color: '#9E9BA8'
			}
		],
		axes: {
			xaxis: {
				renderer: $.jqplot.CategoryAxisRenderer,
				ticks: ticks,
				tickOptions: {
					fontSize: '8pt',
					showGridline: false,
					formatString:'%d'
				}
			},
			yaxis: {
				padMin: 0,
				min: 0,
				tickOptions: {
					formatString:"%'d"
				},
				autoscale: true
			},
			y2axis: {
				padMin: 0,
				min: 0,
				tickOptions: {
					showGridline: false,
					formatString:"%.1f%%"
				},
				autoscale: true
			}
		},
		highlighter: {
			show: true,
			tooltipContentEditor: function (str, seriesIndex, pointIndex, plot) {

				var prefix = ticks[pointIndex]+"<br>";
				var value = plot.data[seriesIndex][pointIndex];
				var suffix = " 件";

				switch(seriesIndex) {
				case 0:
					prefix = labels[seriesIndex] + ' ' + prefix;
					suffix = " PT"
					value = String(Math.round((value+0)*100000000)).replace( /(\d)(?=(\d\d\d)+(?!\d))/g, '$1,');
					break;
				case 1:
					prefix = labels[seriesIndex] + ' '  + prefix;
					suffix = " PT"
					value = String(Math.round((value+0)*100000000)).replace( /(\d)(?=(\d\d\d)+(?!\d))/g, '$1,');
					break;
				case 2:
					prefix = labels[seriesIndex] + ' '  + prefix;
					suffix = " ％"
					value = String((value+0)*1).replace( /(\d)(?=(\d\d\d)+(?!\d))/g, '$1,');
					break;
				case 3:
					prefix = labels[seriesIndex] + ' '  + prefix;
					suffix = " ％"
					value = String((value+0)*1).replace( /(\d)(?=(\d\d\d)+(?!\d))/g, '$1,');
					break;
				}

				var html = "<div class='highlighter'>";
				html += prefix;
				html += value;
				html += suffix;
				html += "  </div>";

				return html;
			},
			showMarker: false,
			tooltipLocation: 'n',
			tooltipAxes: 'y',
			formatString: '%s'
		},
		legend: {
			show: true,
			renderer: $.jqplot.EnhancedLegendRenderer,
			rendererOptions: {
				seriesToggle: false,
				numberRows: 1
			},
			fontSize: '0.6em',
			location: 's',
			labels: labels,
			marginTop : "40px",
			placement: 'outside'
		}
	});
	// ハイライト非表示
	$('#chart_point_hak').mouseout(function(e){
		$('.jqplot-highlighter-tooltip').css("position","absolute").css("display", "none");
	});
}

// ポイント発行・使用状況　月別　使用ポイント
function chart_point_gak(){

	// グラフ破棄
	if (obj_chart_point_gak) {
		obj_chart_point_gak.destroy();
	}

	// グラフ用情報
	var s1 = [];// 当年使用
	var s2 = [];// 前年使用
	var s3 = [];// 当年使用率
	var s4 = [];// 前年使用率
	for(i=0;i<db.length;i++){
		// KB=309:月別使用ポイント
		if (db[i]["KB"]==="309"){
			if (db[i]["AXIS"]==="1"){s1.push(parseFloat(db[i]["VAL"]));}	// 当年使用
			if (db[i]["AXIS"]==="2"){s2.push(parseFloat(db[i]["VAL"]));}	// 前年使用
			if (db[i]["AXIS"]==="3"){s3.push(parseFloat(db[i]["VAL"]));}	// 当年使用率
			if (db[i]["AXIS"]==="4"){s4.push(parseFloat(db[i]["VAL"]));}	// 前年使用率
		}
	}
	if (s1.length===0 || s2.length===0 || s3.length===0 || s4.length===0)	return;

	var ticks = ['1月　　　　', '2月　　　　', '3月　　　　', '4月　　　　', '5月　　　　', '6月　　　　', '7月　　　　', '8月　　　　', '9月　　　　', '10月　　　', '11月　　　', '12月　　　'];
	var labels = ['当年使用PT', '前年使用PT', '当年使用率', '前年使用率'];

	obj_chart_point_gak = $.jqplot('chart_point_gak', [s1, s2, s3, s4], {
		stackSeries: false,
		captureRightClick: true,
		grid: {
			background: '#ffffff',
			borderWidth: 1,
			shadow: false
		},
		seriesDefaults:{
			shadow: false,
			renderer:$.jqplot.BarRenderer,
			rendererOptions: {
				barMargin: 0,
				barWidth:20,
				barPadding: 0,
				highlightMouseDown: true
			},
			pointLabels: {show: false}
		},
		series: [
			{
				renderer:$.jqplot.BarRenderer,
				color: '#9492C7'
			},
			{
				renderer:$.jqplot.BarRenderer,
				rendererOptions:{
					barWidth: 10,
				},
				color: '#DCDDDD'
			},
			{
				renderer: $.jqplot.LineRenderer,
				yaxis:'y2axis',
				lineWidth:1.5,
				markerOptions: {
					size: 3,
					style: 'filledSquare'
				},
				color: '#534975'
			},
			{
				renderer: $.jqplot.LineRenderer,
				yaxis:'y2axis',
				lineWidth:1.5,
				markerOptions: {
					size: 2,
					style: 'filledCircle'
				},
				color: '#9E9BA8'
			}
		],
		axes: {
			xaxis: {
				renderer: $.jqplot.CategoryAxisRenderer,
				ticks: ticks,
				tickOptions: {
					fontSize: '8pt',
					showGridline: false,
					formatString:'%d'
				}
			},
			yaxis: {
				padMin: 0,
				min: 0,
				tickOptions: {
					formatString:"%'d"
				},
				autoscale: true
			},
			y2axis: {
				padMin: 0,
				min: 0,
				tickOptions: {
					showGridline: false,
					formatString:"%.1f%%"
				},
				autoscale: true
			}
		},
		highlighter: {
			show: true,
			tooltipContentEditor: function (str, seriesIndex, pointIndex, plot) {

				var prefix = ticks[pointIndex]+"<br>";
				var value = plot.data[seriesIndex][pointIndex];
				var suffix = " 件";

				switch(seriesIndex) {
				case 0:
					prefix = labels[seriesIndex] + ' ' + prefix;
					suffix = " PT"
					value = String(Math.round((value+0)*100000000)).replace( /(\d)(?=(\d\d\d)+(?!\d))/g, '$1,');
					break;
				case 1:
					prefix = labels[seriesIndex] + ' ' + prefix;
					suffix = " PT"
					value = String(Math.round((value+0)*100000000)).replace( /(\d)(?=(\d\d\d)+(?!\d))/g, '$1,');
					break;
				case 2:
					prefix = labels[seriesIndex] + ' ' + prefix;
					suffix = " ％"
					value = String((value+0)*1).replace( /(\d)(?=(\d\d\d)+(?!\d))/g, '$1,');
					break;
				case 3:
					prefix = labels[seriesIndex] + ' ' + prefix;
					suffix = " ％"
					value = String((value+0)*1).replace( /(\d)(?=(\d\d\d)+(?!\d))/g, '$1,');
					break;
				}

				var html = "<div class='highlighter'>";
				html += prefix;
				html += value;
				html += suffix;
				html += "  </div>";

				return html;
			},
			showMarker: false,
			tooltipLocation: 'n',
			tooltipAxes: 'y',
			formatString: '%s'
		},
		legend: {
			show: true,
			renderer: $.jqplot.EnhancedLegendRenderer,
			rendererOptions: {
				seriesToggle: false,
				numberRows: 1
			},
			fontSize: '0.6em',
			location: 's',
			labels: labels,
			marginTop : "40px",
			placement: 'outside'
		}
	});
	// ハイライト非表示
	$('#chart_point_gak').mouseout(function(e){
		$('.jqplot-highlighter-tooltip').css("position","absolute").css("display", "none");
	});
}

/**
 * ダッシュボード初期化
 * @param YY
 * @param MM
 */
function initDashboard(YY,MM){
	// ダッシュボード用データ取得
	$.post(
		"../EasyToJSON" ,
		{
			"page"		:	"DSB",
			"obj"		:	"dashboard",
			"sel"		:	(new Date()).getTime(),
			"action"	:	"init",
			"YY"		:	YY,
			"MM"		:	MM,
			"rowsOnly"	:	true
		},
		function(data){
			// ダッシュボード用データ保持
			db = data;
			// タブ初期化
			initTab();
		},
		"json"
	);
}

/**
 * 初期情報取得
 */
function initData(){
	// 表示年の更新
	$.post(
		"../EasyToJSON" ,
		{
			"page"		:	"DSB",
			"obj"		:	"selectyear",
			"sel"		:	(new Date()).getTime(),
			"target"	:	"",
			"action"	:	"init",
			"rowsOnly"	:	true
		},
		function(data){
			var item = new Array();
			for(var i=0; i < data.length; i++){
				item.push('<option value="'+ data[i]['VALUE'] + '">' + data[i]['TEXT'] + '</option>');
			}
			var obj1 = $("#selectyear");
			obj1.append(item.join(''));
			// IE6 selected プロパティ エラー回避
			obj1.width();
			// 初期値設定
			obj1.val(obj1.children().first().val());

			// 表示年月の初期値
			$.post(
				"../EasyToJSON" ,
				{
					"page"		:	"DSB",
					"obj"		:	"selectmonth",
					"sel"		:	(new Date()).getTime(),
					"YY"		:	obj1.val(),
					"action"	:	"init",
					"rowsOnly"	:	true
				},
				function(data){
					// 初期値設定
					var obj1 = $("#selectmonth");
					if (data.length>0){
						obj1.val(data[0]['VALUE']);
					}

					// ダッシュボード初期化
					initDashboard($("#selectyear").val(), $("#selectmonth").val());
				},
				"json"
			);

		},
		"json"
	);
};

var db = null;	// ダッシュボード用データ
var onInitTab1 = true;
var onInitTab2 = true;
var onInitTab3 = true;

var obj_chart_uri_day = null;

var obj_chart_uri_mon = null;

var obj_chart_tsu_kyk_day = null;
var obj_chart_tsu_kyk_mon = null;
var obj_chart_ten_kyk_day = null;
var obj_chart_ten_kyk_mon = null;
var obj_chart_tsu_tan_day = null;
var obj_chart_tsu_tan_mon = null;
var obj_chart_ten_tan_day = null;
var obj_chart_ten_tan_mon = null;
var obj_chart_chanel_ken = null;
var obj_chart_chanel_tan = null;

var obj_chart_cls_gak = null;
var obj_chart_cls_kyk = null;
var obj_chart_cls_tan = null;
var obj_chart_cat_gak = null;
var obj_chart_cat_kyk = null;
var obj_chart_cat_tan = null;
var obj_chart_cho_gak = null;
var obj_chart_cho_kyk = null;
var obj_chart_cho_tan = null;

var obj_chart_news_day = null;
var obj_chart_news_tan = null;
var obj_chart_news_mon = null;
var obj_chart_r6_mon = null;
var obj_chart_point_hak = null;
var obj_chart_point_gak = null;

$(document).ready(function(){
	// 初期情報取得
	initData();
});
