if ($.fn.pagination){
	$.fn.pagination.defaults.beforePageText = '頁';
	$.fn.pagination.defaults.afterPageText = '/ {pages}';
	$.fn.pagination.defaults.displayMsg = '{total} 行中 {from} ～ {to} 表示 ';
}
if ($.fn.datagrid){
	$.fn.datagrid.defaults.loadMsg = 'しばらくお待ちください ...';
}
if ($.fn.treegrid && $.fn.datagrid){
	$.fn.treegrid.defaults.loadMsg = $.fn.datagrid.defaults.loadMsg;
}
if ($.messager){
	$.messager.defaults.ok = 'Ok';
	$.messager.defaults.cancel = 'Cancel';
}
if ($.fn.validatebox){
	$.fn.validatebox.defaults.missingMessage = 'この項目は、必須指定です。';
	$.fn.validatebox.defaults.rules.email.message = 'Please enter a valid email address.';
	$.fn.validatebox.defaults.rules.url.message = 'Please enter a valid URL.';
	$.fn.validatebox.defaults.rules.length.message = 'Please enter a value between {0} and {1}.';
	$.fn.validatebox.defaults.rules.remote.message = 'Please fix this field.';
}
if ($.fn.numberbox){
	$.fn.numberbox.defaults.missingMessage = 'この項目は、必須指定です。';
}
if ($.fn.combobox){
	$.fn.combobox.defaults.missingMessage = 'この項目は、必須指定です。';
}
if ($.fn.combotree){
	$.fn.combotree.defaults.missingMessage = 'この項目は、必須指定です。';
}
if ($.fn.combogrid){
	$.fn.combogrid.defaults.missingMessage = 'この項目は、必須指定です。';
}
if ($.fn.calendar){
	$.fn.calendar.defaults.weeks = ['日','月','火','水','木','金','土'];
	$.fn.calendar.defaults.months = ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月'];
}
if ($.fn.datebox){
	$.fn.datebox.defaults.currentText = '今日';
	$.fn.datebox.defaults.closeText = '閉じる';
	$.fn.datebox.defaults.okText = '適用';
	$.fn.datebox.defaults.missingMessage = 'この項目は、必須指定です。';
}
if ($.fn.datetimebox && $.fn.datebox){
	$.extend($.fn.datetimebox.defaults,{
		currentText: $.fn.datebox.defaults.currentText,
		closeText: $.fn.datebox.defaults.closeText,
		okText: $.fn.datebox.defaults.okText,
		missingMessage: $.fn.datebox.defaults.missingMessage
	});
}
