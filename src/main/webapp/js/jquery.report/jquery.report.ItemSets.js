/**
 * jquery report ItemSets
 * 商品入力
 */

;(function($) {

	$.extend({
	  ItemSets: {

		initializes: false,	// 初期化フラグ
		lastIndex: -1,			// 編集位置（行）

        init: function() {
			var that = this;
        },
        Open: function() {
            // 商品入力 window 表示
			$('#win').window('open');
        }

	  }
	});

})(jQuery);