/*
 * 作成日: 2006/12/05
 *
 */
package common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.time.DateUtils;

/**
 * Dateを扱うクラス
 */
public class CmnDate extends Date {

	/**
	 *
	 */
	private static final long serialVersionUID = -6434746783286089750L;

	/**
	 *
	 */
	public CmnDate() {
		super();
	}

	/**
	 * 日付フォーマット種類
	 */
	public static enum DATE_FORMAT {
		/** デフォルト日付フォーマット 年月日＆時分秒 */
		DEFAULT_DATE_TIME("yyyy/MM/dd HH:mm:ss"),
		/** デフォルト日付フォーマット 年月日 */
		DEFAULT_DATE("yyyy/MM/dd"),
		/** デフォルト日付フォーマット 年月 */
		DEFAULT_MONTH("yyyy/MM"),
		/** デフォルト日付フォーマット 時分秒 */
		DEFAULT_TIME("HH:mm:ss"),
		/** 8桁フォーマット 年月日 */
		DATE8("yyyyMMdd"),
		/** 8桁フォーマット 時分秒 */
		TIME8("HHmmss"),
		/** 6桁フォーマット 年月 */
		MONTH6("yyyyMM"),
		/** 日本日付フォーマット 年月日 */
		KANJI_DATE("yyyy'年'MM'月'dd'日'"),
		/** デフォルト日付フォーマット 年月 */
		KANJI_MONTH("yyyy'年'MM'月'");

		/** 日付フォーマットパターン */
		private String pattern;

		/**
		 * 日付フォーマットパターンを拡張した列挙型を生成
		 *
		 * @param pattern
		 *            日付フォーマットパターン
		 */
		private DATE_FORMAT(final String pattern) {
			this.pattern = pattern;
		}

		/**
		 * 日付フォーマットパターンを返します。
		 *
		 * @return 日付フォーマットパターン
		 */
		public String formatPattern() {
			return this.pattern;
		}
	}

	/**
	 * 指定されたパターンで日付/時刻をフォーマットします。
	 *
	 * @param date
	 *            フォーマット対象となる日付
	 * @param pattern
	 *            日付をフォーマットする際に使用されるパターン
	 * @return フォーマットされた日付（対象日付がnullの場合はnull）
	 */
	private static String dateFormat(java.util.Date date, String pattern) {
		if (date == null) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}

	/**
	 * 指定されたパターンで日付/時間をフォーマットします。
	 *
	 * @param date
	 *            フォーマット対象となる日付
	 * @param format
	 *            日付をフォーマットする際に使用されるパターン
	 * @return フォーマットされた日付
	 */
	public static String dateFormat(java.util.Date date, DATE_FORMAT format) {
		return dateFormat(date, format.formatPattern());
	}

	/**
	 * 指定されたパターンで文字列を日付/時刻に変換します。
	 *
	 * @param source 対象となる文字列
	 * @param pattern 日付に変換する際に使用されるパターン
	 * @return 日付/時刻
	 */
	private static java.util.Date convDate(String source, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		try {
			return sdf.parse(source);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 指定されたパターンで文字列を日付/時刻に変換します。
	 * 
	 * @param source 対象となる文字列
	 * @param format 日付に変換する際に使用されるパターン
	 * @return 日付/時刻
	 */
	public static java.util.Date convDate(String source, DATE_FORMAT format) {
		return convDate(source, format.formatPattern());
	}


	/**
	 * 日付文字列を指定されたパターンの文字列に変換します。
	 * 
	 * @param source 対象となる文字列
	 * @param befFormat 日付に変換する際に使用されるパターン
	 * @param aftFormat 日付をフォーマットする際に使用されるパターン
	 * @return 日付/時刻
	 */
	public static String dateFormat(String source, DATE_FORMAT befFormat, DATE_FORMAT aftFormat) {
		return dateFormat(convDate(source, befFormat.formatPattern()), aftFormat.formatPattern());
	}

	/**
	 * 8桁日付文字列を指定されたパターンの文字列に変換します。
	 * 
	 * @param source 対象となる文字列
	 * @param format 日付をフォーマットする際に使用されるパターン
	 * @return 日付/時刻
	 */
	public static String date8Format(String source, DATE_FORMAT format) {
		return dateFormat(convDate(source, DATE_FORMAT.DATE8), format.formatPattern());
	}

	/**
	 * 6桁年月文字列を指定されたパターンの文字列に変換します。
	 * 
	 * @param source 対象となる文字列
	 * @param format 日付をフォーマットする際に使用されるパターン
	 * @return 日付/時刻
	 */
	public static String month6Format(String source, DATE_FORMAT format) {
		return dateFormat(convDate(source, DATE_FORMAT.MONTH6), format.formatPattern());
	}

	/**
	 * システム日付を取得する
	 * 
	 * @return String システム日付 YYYYMMDD
	 */
	public String getToday() {
		return dateFormat(new Date(), DATE_FORMAT.DATE8);
	}

	/**
	 * システム時刻を取得する
	 *
	 * @return String システム時刻 HHMMSS
	 */
	public String getNowTime() {
		return dateFormat(new Date(), DATE_FORMAT.TIME8);
	}

	/**
	 * システム時刻を取得する
	 * @param sept 区切り文字
	 * @return String システム時刻 HHMMSS
	 */
	public String getTime(String sept) {
		return dateFormat(new Date(), "HH" + sept + "mm" + sept + "ss");
	}

	/**
	 * 時刻を設定する
	 *
	 * @param time HHMMSS
	 */
	@SuppressWarnings("deprecation")
	public void setTime(String time) {
		super.setHours(Integer.parseInt(time.substring(0, 2)));
		super.setMinutes(Integer.parseInt(time.substring(2, 4)));
		super.setSeconds(Integer.parseInt(time.substring(4, 6)));
	}


	/**
	 * from月度～to月度の全月度を返します。
	 *
	 * @param fromMon from月度
	 * @param toMon to月度
	 * @return from月度～to月度の全月度
	 */
	public static String[] getMonthFromTo(String fromMon, String toMon) {
		Date from = convDate(fromMon.concat("01"), DATE_FORMAT.DATE8);
		Date to = convDate(toMon.concat("01"), DATE_FORMAT.DATE8);
		if (from.compareTo(to) > 0) {
			return new String[] {};
		}

		Object[] dtArray = new String[] {};
		while (from.compareTo(to) <= 0) {
			dtArray = ArrayUtils.add(dtArray, dateFormat(from, DATE_FORMAT.MONTH6));
			from = DateUtils.addMonths(from, 1);
		}

		return (String[]) dtArray;
	}

	/**
	 * from日付～to日付の全日付を返します。
	 * 
	 * @param fromDt from日付
	 * @param toDt to日付
	 * @return from日付～to日付の全日付
	 */
	public static String[] getDateFromTo(String fromDt, String toDt) {
		Date from = convDate(fromDt, DATE_FORMAT.DATE8);
		Date to = convDate(toDt, DATE_FORMAT.DATE8);
		if (from.compareTo(to) > 0) {
			return new String[] {};
		}

		Object[] dtArray = new String[] {};
		while (from.compareTo(to) <= 0) {
			dtArray = ArrayUtils.add(dtArray, dateFormat(from, DATE_FORMAT.DATE8));
			from = DateUtils.addDays(from, 1);
		}

		return (String[]) dtArray;
	}
}
