package common;

import java.io.UnsupportedEncodingException;

import org.apache.commons.lang.StringUtils;

/**
 * 半角カタカナ、全角カタカナ変換クラス
 *
 * @author EATONE
 *
 */
public class StringConverter {

	// 半角←→全角変換テーブル
	private static String eisukigoHanZenTbl[][] = { { "!", "！" }, { "\"", "”" }, { "", "＃" }, { "$", "＄" },
			{ "%", "％" }, { "&", "＆" }, { "'", "’" }, { "(", "（" }, { ")", "）" }, { "*", "＊" }, { "+", "＋" },
			{ ".", "．" }, { "-", "－" }, { ".", "．" }, { "/", "／" }, { ":", "：" }, { ";", "；" }, { ">", "＞" },
			{ "=", "＝" }, { "<", "＜" }, { "?", "？" }, { "@", "＠" }, { "[", "［" }, { "\\", "￥" }, { "]", "］" },
			{ "^", "＾" }, { "_", "＿" }, { "`", "‘" }, { "{", "｛" }, { "|", "｜" }, { "}", "｝" }, { "~", "～" },
			{ "1", "１" }, { "2", "２" }, { "3", "３" }, { "4", "４" }, { "5", "５" }, { "6", "６" }, { "7", "７" },
			{ "8", "８" }, { "9", "９" }, { "0", "０" }, { "A", "Ａ" }, { "B", "Ｂ" }, { "C", "Ｃ" }, { "D", "Ｄ" },
			{ "E", "Ｅ" }, { "F", "Ｆ" }, { "G", "Ｇ" }, { "H", "Ｈ" }, { "I", "Ｉ" }, { "J", "Ｊ" }, { "K", "Ｋ" },
			{ "L", "Ｌ" }, { "M", "Ｍ" }, { "N", "Ｎ" }, { "O", "Ｏ" }, { "P", "Ｐ" }, { "Q", "Ｑ" }, { "R", "Ｒ" },
			{ "S", "Ｓ" }, { "T", "Ｔ" }, { "U", "Ｕ" }, { "V", "Ｖ" }, { "W", "Ｗ" }, { "X", "Ｘ" }, { "Y", "Ｙ" },
			{ "Z", "Ｚ" }, { "a", "ａ" }, { "b", "ｂ" }, { "c", "ｃ" }, { "d", "ｄ" }, { "e", "ｅ" }, { "f", "ｆ" },
			{ "g", "ｇ" }, { "h", "ｈ" }, { "i", "ｉ" }, { "j", "ｊ" }, { "k", "ｋ" }, { "l", "ｌ" }, { "m", "ｍ" },
			{ "n", "ｎ" }, { "o", "ｏ" }, { "p", "ｐ" }, { "q", "ｑ" }, { "r", "ｒ" }, { "s", "ｓ" }, { "t", "ｔ" },
			{ "u", "ｕ" }, { "v", "ｖ" }, { "w", "ｗ" }, { "x", "ｘ" }, { "y", "ｙ" }, { "z", "ｚ" } };
	private static final String[]	ZENKAKU_KATAKANA		= { "｡", "｢", "｣", "､", "･", "ァ", "ア", "ィ", "イ", "ゥ", "ウ", "ェ", "エ", "ォ", "オ", "カ", "ガ",
			"キ", "ギ", "ク", "グ", "ケ", "ゲ", "コ", "ゴ", "サ", "ザ", "シ", "ジ", "ス", "ズ", "セ", "ゼ", "ソ", "ゾ", "タ", "ダ", "チ", "ヂ", "ッ", "ツ", "ヅ", "テ", "デ",
			"ト", "ド", "ナ", "ニ", "ヌ", "ネ", "ノ", "ハ", "バ", "パ", "ヒ", "ビ", "ピ", "フ", "ブ", "プ", "ヘ", "ベ", "ペ", "ホ", "ボ", "ポ", "マ", "ミ", "ム", "メ", "モ",
			"ャ", "ヤ", "ュ", "ユ", "ョ", "ヨ", "ラ", "リ", "ル", "レ", "ロ", "ヮ", "ワ", "ヰ", "ヱ", "ヲ", "ン", "ヴ", "ヵ", "ヶ" };

	private static final String[]	HANKAKU_KATAKANA		= { "。", "「", "」", "、", "・", "ｧ", "ｱ", "ｨ", "ｲ", "ｩ", "ｳ", "ｪ", "ｴ", "ｫ", "ｵ", "ｶ", "ｶﾞ",
			"ｷ", "ｷﾞ", "ｸ", "ｸﾞ", "ｹ", "ｹﾞ", "ｺ", "ｺﾞ", "ｻ", "ｻﾞ", "ｼ", "ｼﾞ", "ｽ", "ｽﾞ", "ｾ", "ｾﾞ", "ｿ", "ｿﾞ", "ﾀ", "ﾀﾞ", "ﾁ", "ﾁﾞ", "ｯ", "ﾂ", "ﾂﾞ",
			"ﾃ", "ﾃﾞ", "ﾄ", "ﾄﾞ", "ﾅ", "ﾆ", "ﾇ", "ﾈ", "ﾉ", "ﾊ", "ﾊﾞ", "ﾊﾟ", "ﾋ", "ﾋﾞ", "ﾋﾟ", "ﾌ", "ﾌﾞ", "ﾌﾟ", "ﾍ", "ﾍﾞ", "ﾍﾟ", "ﾎ", "ﾎﾞ", "ﾎﾟ", "ﾏ",
			"ﾐ", "ﾑ", "ﾒ", "ﾓ", "ｬ", "ﾔ", "ｭ", "ﾕ", "ｮ", "ﾖ", "ﾗ", "ﾘ", "ﾙ", "ﾚ", "ﾛ", "ﾜ", "ﾜ", "ｲ", "ｴ", "ｦ", "ﾝ", "ｳﾞ", "ｶ", "ｹ" };

	private static final String[]	ZENKAKU_HIRAGANA		= { "。", "「", "」", "、", "・", "ぁ", "あ", "ぃ", "い", "ぅ", "う", "ぇ", "え", "ぉ", "お", "か", "が",
			"き", "ぎ", "く", "ぐ", "け", "げ", "こ", "ご", "さ", "ざ", "し", "じ", "す", "ず", "せ", "ぜ", "そ", "ぞ", "た", "だ", "ち", "ぢ", "っ", "つ", "づ", "て", "で",
			"と", "ど", "な", "に", "ぬ", "ね", "の", "は", "ば", "ぱ", "ひ", "び", "ぴ", "ふ", "ぶ", "ぷ", "へ", "べ", "ぺ", "ほ", "ぼ", "ぽ", "ま", "み", "む", "め", "も",
			"ゃ", "や", "ゅ", "ゆ", "ょ", "よ", "ら", "り", "る", "れ", "ろ", "ゎ", "わ", "ゐ", "ゑ", "を", "ん", "う゛", "か", "け" };

	/**
	 * 文字列中の半角カタカナを全角カタカナに変換します。<br />
	 *
	 * @param s 変換前文字列
	 * @return 変換後文字列
	 */
	public static String hankakuKatakanaToZenkakuKatakana(String s) {
		return StringUtils.replaceEach(s, HANKAKU_KATAKANA, ZENKAKU_KATAKANA);
	}

	/**
	 * 文字列中の全角カタカナを半角カタカナに変換します。<br />
	 *
	 * @param s 変換前文字列
	 * @return 変換後文字列
	 */
	public static String zenkakuKatakanaToHankakuKatakana(String s) {
		return StringUtils.replaceEach(s, ZENKAKU_KATAKANA, HANKAKU_KATAKANA);
	}


	/**
	 * 文字列中の全角ひらがなを半角カタカナに変換します。<br />
	 *
	 * @param s 変換前文字列
	 * @return 変換後文字列
	 */
	public static String zenkakuHiraganaToHankakuKatakana(String s) {
		return StringUtils.replaceEach(s, ZENKAKU_HIRAGANA, HANKAKU_KATAKANA);
	}

	/**
	 *
	 * 半角英数記号を全角英数記号に変換する
	 *
	 * @param str 文字列
	 * @return String 変換後の文字列
	 */
	public static String eisukigoHankakuToZenkaku(String str) {
		String zenstr = "";
		String chkstr = "";
		// strを1文字づつ eisukigouHanZenTblと照らし合わせて変換する
		for (int i = 0; i < str.length(); i++) {
			chkstr = str.substring(i, i + 1);
			for (String[] element : eisukigoHanZenTbl) {
				if (chkstr.equals(element[0])) {
					chkstr = element[1];
					break;
				}
			}
			zenstr = zenstr + chkstr;
		}
		return zenstr;
	}

	/**
	 *
	 * 全角英数記号を半角英数記号に変換する
	 *
	 * @param str 文字列
	 * @return String 変換後の文字列
	 */
	public static String eisukigoZenkakuToHankaku(String str) {
		String hanstr = "";
		String chkstr = "";
		// strを1文字づつ eisukigouHanZenTblと照らし合わせて変換する
		for (int i = 0; i < str.length(); i++) {
			chkstr = str.substring(i, i + 1);
			for (String[] element : eisukigoHanZenTbl) {
				if (chkstr.equals(element[1])) {
					chkstr = element[0];
					break;
				}
			}
			hanstr = hanstr + chkstr;
		}
		return hanstr;
	}

	/**
	 * 
	 * 文字列を指定のByte数以下になるまで半角に変換する。<br/>
	 * 半角変換でも指定Byte数にならない場合は切り出し。
	 * 
	 * @param str 文字列
	 * @param len バイト数
	 * @return String 変換後の文字列
	 */
	public static String convertToLengthB(String str, int len) {
		if (checkLengthB(str, len)) {
			return str;
		}
		// 全角英数記号→半角
		String hanstr = eisukigoZenkakuToHankaku(str);
		if (checkLengthB(hanstr, len)) {
			return hanstr;
		}
		return leftB(hanstr, len);
	}

	/**
	 * 文字列長さチェック（Byte単位）<br />
	 *
	 * @param str String 対象文字列
	 * @param len int バイト数
	 *
	 * @return String 切り出し後の文字列
	 */
	public static boolean checkLengthB(String str, int len) {
		int lenB = 0;
		try {
			lenB = str.getBytes("utf-8").length;
		} catch (UnsupportedEncodingException e) {
			return false;
		}
		return lenB <= len;
	}

	/**
	 * 文字列切り出し（Byte単位）<br />
	 * <br />
	 * 先頭から指定バイト数分文字列を切り出す。<br />
	 * 切り出し終了部分が日本語の途中にかかる場合は直前の文字までを切り出す<br />
	 * 
	 * @param str String 切り出し対象文字列
	 * @param len Integer 切り出しバイト数
	 * 
	 * @return String 切り出し後の文字列
	 */
	public static String leftB(String str, Integer len) {
		StringBuffer sb = new StringBuffer();
		int cnt = 0;
		try {
			for (int i = 0; i < str.length(); i++) {
				String tmpStr = str.substring(i, i + 1);
				byte[] b = tmpStr.getBytes("utf-8");
				if (cnt + b.length > len) {
					return sb.toString();
				} else {
					sb.append(tmpStr);
					cnt += b.length;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

}
