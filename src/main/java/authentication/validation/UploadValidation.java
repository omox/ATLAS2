/*
 * 作成日: 2006/09/05
 */
package authentication.validation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import authentication.defines.Defines;
import authentication.util.Tips;


/**
 * エラー制御を行う必要があるイベントのエラーチェック・判定を行い、 <br />
 * エラーが存在する際は、各イベントに適切なエラーメッセージを設定する <br />
 */
public class UploadValidation {

	/** エラーメッセージを格納するArrayList */
	private ArrayList<String> msgs = new ArrayList();

	private long errrow = 0L;		//エラー行格納用

	/** エラーメッセージ生成用文字:必須項目チェック */
	private static final String MSG_REQ = "NULL";
	/** エラーメッセージ生成用文字:桁数チェック */
	private static final String MSG_LEN = "桁";
	/** エラーメッセージ生成用文字:半角英数字チェック */
	private static final String MSG_HALFNUMENG = "半角英数字以外";
	/** エラーメッセージ生成用文字:半角数字チェック */
	private static final String MSG_HALFNUM = "整数以外";
	/** エラーメッセージ生成用文字:半角数字チェック */
	private static final String MSG_HALFNUMD = "数値以外";
	/** エラーメッセージ生成用文字:日付チェック */
	private static final String MSG_DATE = "日付形式以外";
	/** エラーメッセージ生成用文字:エラーメッセージ */
	private static final String MSG_END = "を設定することは出来ません。";
	/** エラーメッセージ生成用文字:エラーメッセージ */
	private static final String MSG_KIGO = "記号も認められません。";
	/** エラーメッセージ生成用文字:エラーメッセージ */
	private static final String MSG_COL = "列数が不正です。";

	/**
	 * エラーメッセージ出力 <br />
	 * 各エラーチェックにて生成されたエラーメッセージを <br />
	 * HTMLで出力する為に加筆・編集し、戻す。 <br />
	 *
	 * @return エラー内容
	 */
	public String OutMsg() {
		StringBuffer msg = new StringBuffer();
		int size = msgs.size();
		msg.append("行" + errrow + "に以下の " + size + "つ のエラーがあります。 内容を確認して下さい。<br />");
		for (int i = 0; i < size; i++) {
			msg.append("&nbsp;&nbsp;・" + (String) msgs.get(i) + "<br />");
		}
		return msg.toString();
	}
	/**
	 * エラーメッセージ出力 <br />
	 * 各エラーチェックにて生成されたエラーメッセージのみ <br />
	 * HTMLで出力する為に編集し、戻す。 <br />
	 *
	 * @return エラー内容
	 */
	public String OutMsgShort() {
		StringBuffer msg = new StringBuffer();
		int size = msgs.size();
		for (int i = 0; i < size; i++) {
			msg.append("&nbsp;&nbsp;・" + (String) msgs.get(i) + "<br />");
		}
		return msg.toString();
	}
	/**
	 * 半角数値のみで構成されているかを判定し、結果を戻す。 <br />
	 *
	 * @param str 対象文字列
	 * @return 数値のみ:true
	 */
	private boolean isNumeric(String str) {
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if ((c < '0') || (c > '9')) {
				return false;
			}
		}
		return true;

	}
	/**
	 * 整数かを判定し、結果を戻す。(小数可) <br />
	 *
	 * @param str 対象文字列
	 * @return 数値のみ:true
	 */
	private boolean isNumericL(String str) {
		try {
			//Long型に変換可能かチェック
			Long.parseLong(str);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 数値かを判定し、結果を戻す。(小数可) <br />
	 *
	 * @param str 対象文字列
	 * @return 数値のみ:true
	 */
	private boolean isNumericD(String str) {
		try {
			//Double型に変換可能かチェック
			Double.parseDouble(str);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 半角英字のみで構成されているかを判定し、結果を戻す。 <br />
	 * 記号は対象外(ｴﾗｰ)とする
	 *
	 * @param c 対象文字列
	 * @return 半角英字のみ:true
	 */
	private boolean isHalfchar(String str) {
		for (int i = 0; i < str.length(); i++) {

			char c = str.charAt(i);
			if (((c < 'A') || (c > 'Z')) && ((c < 'a') || (c > 'z'))) {
				return false;
			}
		}
		return true;

	}

	/**
	 * 半角英数字のみで構成されているかを判定し、結果を戻す。 <br />
	 * 記号は対象外(ｴﾗｰ)とする
	 *
	 * @param c 対象文字列
	 * @return 半角英数字のみ:true
	 */
	private boolean isHalfcharAndNumeric(String str) {
		for (int i = 0; i < str.length(); i++) {

			char c = str.charAt(i);
			if (((c < 'A') || (c > 'Z')) && ((c < 'a') || (c > 'z')) && ((c < '0') || (c > '9'))) {
				return false;
			}
		}
		return true;

	}
	/**
	 * 全角文字のバイト桁数を取得し、その桁数を返す。
	 * @param src 対象文字
	 * @return バイト桁数
	 */
	private int isByteLenOver(String src){
	  int dstlen = 0;
	  for(int i = 0;i < src.length() ;i++){
	    dstlen += (src.charAt(i) <= 0xff ? 1 : 2);
	  }
	  return dstlen;
	}
	/**
	 * 日付か判定し、結果を戻す。 <br />
	 * 記号は対象外(ｴﾗｰ)とする
	 *
	 * @param strDate 対象日付
	 * @param format  日付パターン
	 *
	 * @return 日付
	 */
	private boolean isDate(String strDate, String format) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			Date dt = sdf.parse(strDate);

			if(sdf.format(dt).toString().equals(strDate)){
				return true;
			}else{
				return false;
			}
		} catch (ParseException e) {
			if (format.equals(format.replaceAll("/", ""))==false){
				if (isDate(strDate, format.replaceAll("/", "")) == true) {
					return true;
				} else {
					return false;
				}
			}else{
				return false;
			}
		}
	}

	/**
	 * 日付か判定し、結果を戻す。 <br />
	 * 記号は対象外(ｴﾗｰ)とする
	 *
	 * @param strDate 対象日付
	 * @param format  日付パターン
	 *
	 * @return 日付
	 */
	private boolean isDateYM(String strDate) {

		if (strDate.trim().length() != 6) {
			return false;
		}
		if (isNumeric(strDate) == false){
			return false;
		}
		int month = 0;
		month = Integer.parseInt(strDate.substring(4,6));
		if (month < 1 || month > 12){
			return false;
		}
		return true;
	}

	/**
	 * 必須,桁数,日付のチェックを行い、結果を戻す。 <br />
	 *
	 * @param obj チェック対象
	 * @param len 対象桁数
	 * @param objMSG 対象の表示文字
	 * @return 正常:true エラー:false
	 */
	private boolean chkRequiredLHANDate(String obj, int len, String objMSG, String format) {
		boolean errflg = true;

		/* 必須,桁数,半角数字 */
		if (obj.trim().length() == 0) {
			String msg = objMSG + "に" + MSG_REQ + MSG_END;
			msgs.add(msg);
			errflg = false;
		}else if (obj.trim().length() != len) {
			String msg = objMSG + "に (" + Defines.DT_FORMAT_YM + ") の" + MSG_DATE + MSG_END;
			msgs.add(msg);
			errflg = false;
		}else if (obj.trim().length() != 0) {
			if (this.isDate(obj,format) == false) {
				String msg = objMSG + "に (" + Defines.DT_FORMAT_YM + ") の" + MSG_DATE + MSG_END;
				msgs.add(msg);
				errflg = false;
			}
		}
		return errflg;
	}

	/**
	 * 必須,桁数,半角英数字のチェックを行い、結果を戻す。 <br />
	 *
	 * @param obj チェック対象
	 * @param len 対象桁数
	 * @param objMSG 対象の表示文字
	 * @return 正常:true エラー:false
	 */
	private boolean chkRequiredLHAN(String obj, int len, String objMSG) {
		boolean errflg = true;

		/* 必須,桁数,半角数字 */
		if (obj.trim().length() == 0) {
			String msg = objMSG + "に" + MSG_REQ + MSG_END;
			msgs.add(msg);
			errflg = false;
		} else if (obj.trim().length() != len) {
			String msg = objMSG + "に" + len + MSG_LEN + "の" + MSG_HALFNUM + MSG_END + MSG_KIGO;
			msgs.add(msg);
			errflg = false;
		} else if (!this.isNumeric(obj)) {
			String msg = objMSG + "に" + len + MSG_LEN + "の" + MSG_HALFNUM + MSG_END + MSG_KIGO;
			msgs.add(msg);
			errflg = false;
		}
		return errflg;
	}

	/**
	 * 必須,桁数,半角英数字のチェックを行い、結果を戻す。 <br />
	 *
	 * @param obj チェック対象
	 * @param len 対象桁数
	 * @param objMSG 対象の表示文字
	 * @return 正常:true エラー:false
	 */
	private boolean chkRequiredOHAN(String obj, int len, String objMSG) {
		boolean errflg = true;

		/* 必須,桁数,半角数字 */
		if (obj.trim().length() == 0) {
			String msg = objMSG + "に" + MSG_REQ + MSG_END;
			msgs.add(msg);
			errflg = false;
		} else if (obj.trim().length() > len) {
			String msg = objMSG + "に" + len + MSG_LEN + "の" + MSG_HALFNUM + MSG_END + MSG_KIGO + "A";
			msgs.add(msg);
			errflg = false;
		} else if (!this.isNumeric(obj)) {
			String msg = objMSG + "に" + len + MSG_LEN + "の" + MSG_HALFNUM + MSG_END + MSG_KIGO + "B";
			msgs.add(msg);
			errflg = false;
		}
		return errflg;
	}

	/**
	 * 必須,桁数のチェックを行い、結果を戻す。 <br />
	 *
	 * @param obj チェック対象
	 * @param len 対象桁数
	 * @param objMSG 対象の表示文字
	 * @return 正常:true エラー:false
	 */
	private boolean chkRequired(String obj, int len, String objMSG) {
		boolean errflg = true;

		/* 必須,桁数 */
		if (obj.trim().length() == 0) {
			String msg = objMSG + "に" + MSG_REQ + MSG_END;
			msgs.add(msg);
			errflg = false;
		} else if (obj.trim().length() > len) {
			String msg = objMSG + "に" + len + MSG_LEN + "以上" + MSG_END;
			msgs.add(msg);
			errflg = false;
		}
		return errflg;
	}

	/**
	 * 桁数のチェックを行い、結果を戻す。 <br />
	 *
	 * @param obj チェック対象
	 * @param len 対象桁数
	 * @param objMSG 対象の表示文字
	 * @return 正常:true エラー:false
	 */
	private boolean chkNonRequired(String obj, int len, String objMSG) {
		boolean errflg = true;

		/* 桁数 */
		if (obj.trim().length() > len) {
			String msg = objMSG + "に" + len + MSG_LEN + "以上" + MSG_END;
			msgs.add(msg);
			errflg = false;
		}
		return errflg;
	}

	/**
	 * 桁数,整数のチェックを行い、結果を戻す。 <br />
	 *
	 * @param obj チェック対象
	 * @param len 対象桁数
	 * @param objMSG 対象の表示文字
	 * @return 正常:true エラー:false
	 */
	private boolean chkNonreqLNL(String obj, int len, String objMSG) {
		boolean errflg = true;

		/* 桁数,半角数字 */
		if (obj.trim().length() != 0) {
			if (obj.trim().length() > len) {
				String msg = objMSG + "に" + len + MSG_LEN + "の" + MSG_HALFNUM + MSG_END;
				msgs.add(msg);
				errflg = false;
			} else if (!this.isNumericL(obj)) {
				String msg = objMSG + "に" + len + MSG_LEN + "の" + MSG_HALFNUM + MSG_END;
				msgs.add(msg);
				errflg = false;
			}
		}
		return errflg;
	}

	/**
	 * 桁数,数値のチェックを行い、結果を戻す。 <br />
	 *
	 * @param obj チェック対象
	 * @param len 対象桁数
	 * @param objMSG 対象の表示文字
	 * @return 正常:true エラー:false
	 */
	private boolean chkNonreqLND(String obj, int len, String objMSG){
		boolean errflg = true;

		/* 桁数,半角数字 */
		if (obj.trim().length() != 0) {
			if (obj.trim().replaceAll("\\.", "").length() > len){
				String msg = objMSG + "に" + len + MSG_LEN + "の" + MSG_HALFNUMD + MSG_END;
				msgs.add(msg);
				errflg = false;
			} else if (!this.isNumericD(obj)) {
				String msg = objMSG + "に" + len + MSG_LEN + "の" + MSG_HALFNUMD + MSG_END;
				msgs.add(msg);
				errflg = false;
			}
		}
		return errflg;
	}


	/**
	 * 桁数,数値のチェックを行い、結果を戻す。 <br />
	 *
	 * @param obj チェック対象
	 * @param ilen 整数部桁数
	 * @param dlen 小数部桁数
	 * @param objMSG 対象の表示文字
	 * @return 正常:true エラー:false
	 */
	private boolean chkNonreqLND(String obj, int ilen, int dlen, String objMSG) {
		boolean errflg = true;
		/* 桁数,半角数字 */
		if (obj.trim().length() != 0) {
			if (!NumberUtils.isNumber(obj.trim())) {
				String msg = objMSG + "に" + MSG_HALFNUMD + MSG_END;
				msgs.add(msg);
				errflg = false;
			} else {
				String ival = StringUtils.substringBefore(obj.trim(), ".").trim();	// 整数部
				String dval = StringUtils.substringAfter(obj.trim(), ".").trim();	// 小数部
				if (StringUtils.isNotEmpty(ival) && ival.length() > ilen) {
					String msg = objMSG + "の整数部に" + ilen + MSG_LEN + "の" + MSG_HALFNUMD + MSG_END;
					msgs.add(msg);
					errflg = false;
				}
				if (StringUtils.isNotEmpty(dval) && dval.length() > dlen) {
					String msg = objMSG + "の小数部に" + dlen + MSG_LEN + "の" + MSG_HALFNUMD + MSG_END;
					msgs.add(msg);
					errflg = false;
				}
			}
		}
		return errflg;
	}

	/**
	 * 桁数,日付のチェックを行い、結果を戻す。 <br />
	 *
	 * @param obj チェック対象
	 * @param len 対象桁数
	 * @param objMSG 対象の表示文字
	 * @return 正常:true エラー:false
	 */
	private boolean chkNonreqLDate(String obj, int len, String objMSG) {
		boolean errflg = true;

		/* 必須,桁数,半角数字 */
		if (obj.trim().length() != 0) {
			if (obj.trim().length() != len) {
				String msg = objMSG + "に (" + Defines.DT_PW_FORMAT_SAVE + ") の" + MSG_DATE + MSG_END;
				msgs.add(msg);
				errflg = false;
			} else if (!this.isDate(obj, Defines.DT_PW_FORMAT_SAVE)) {
				String msg = objMSG + "に (" + Defines.DT_PW_FORMAT_SAVE + ") の" + MSG_DATE + MSG_END;
				msgs.add(msg);
				errflg = false;
			}
		}
		return errflg;
	}


	/**
	 * アップロード・ダウンロード画面：ダウンロードボタンのチェック <br />
	 *
	 * @param strdata インポート対象データ
	 * @param filetype インポートファイルタイプ
	 * @return 正常:true エラー:false
	 */
	public boolean UpDown_Download(String[] strdata,String filetype,long targetrow) {
		boolean errflg = true;

		//Tips
		Tips tip = new Tips();

		if (filetype.equals("0")) {
			//関連会社売上目標
			if (strdata.length == 9) {
				/* 売上目標年月 */
				if (!this.chkRequiredLHANDate(strdata[0], 6, "列1", Defines.DT_UP_FORMAT_YM)) {
					errflg = false;
				}
				/* チャネル1 */
				if (!this.chkRequiredLHAN(strdata[1], 1, "列2")) {
					errflg = false;
				}
				/* チャネル2 */
				if (!this.chkRequiredLHAN(tip.setZero(strdata[2], 2), 2, "列3")) {
					errflg = false;
				}
				tip.setZero(strdata[2], 2);
				/* チャネル3 */
				if (!this.chkRequiredLHAN(tip.setZero(strdata[3], 3), 3, "列4")) {
					errflg = false;
				}
				/* チャネル4 */
				if (!this.chkRequiredLHAN(tip.setZero(strdata[4], 2), 2, "列5")) {
					errflg = false;
				}
				/* 目標件数 NUMBER(10,0) */
				if (!this.chkNonreqLNL(strdata[5], 10, "列6")) {
					errflg = false;
				}
				/* 目標出荷売上 NUMBER(10,0) */
				if (!this.chkNonreqLNL(strdata[6], 10, "列7")) {
					errflg = false;
				}
				/* 目標純売上 NUMBER(10,0) */
				if (!this.chkNonreqLNL(strdata[7], 10, "列8")) {
					errflg = false;
				}
				/* 最終更新日 */
				if (!this.chkNonreqLDate(strdata[8], 8, "列9")) {
					errflg = false;
				}
			} else {
				msgs.add(MSG_COL);
				errflg = false;
			}
		} else if (filetype.equals("1")) {
			//海外為替
			if (strdata.length == 7) {
				/* 年月 */
				if (!this.chkRequiredLHANDate(strdata[0], 6, "列1", Defines.DT_UP_FORMAT_YM)) {
					errflg = false;
				}
				/* チャネル1 */
				if (!this.chkRequiredLHAN(strdata[1], 1, "列2")) {
					errflg = false;
				}
				/* チャネル2 */
				if (!this.chkRequiredLHAN(tip.setZero(strdata[2], 2), 2, "列3")) {
					errflg = false;
				}
				/* チャネル3 */
				if (!this.chkRequiredLHAN(tip.setZero(strdata[3], 3), 3, "列4")) {
					errflg = false;
				}
				/* チャネル4 */
				if (!this.chkRequiredLHAN(tip.setZero(strdata[4], 2), 2, "列5")) {
					errflg = false;
				}
				/* 為替 NUMBER(7,4) */
				if (!this.chkNonreqLND(strdata[5], 3, 4, "列6")) {
					errflg = false;
				}
				/* 率 NUMBER(4,1) */
				if (!this.chkNonreqLND(strdata[6], 3, 1, "列7")) {
					errflg = false;
				}
			} else {
				msgs.add(MSG_COL);
				errflg = false;
			}

		} else if (filetype.equals("2")) {
			// 新規獲得_計画数
			if (strdata.length == 15) {
				/* 分類 */
				if (!this.chkRequired(strdata[0], 20, "分類")) {
					errflg = false;
				}
				/* 項目名 */
				if (!this.chkRequired(strdata[1], 100, "項目名")) {
					errflg = false;
				}
				/* 年 */
				if (!this.chkRequiredLHAN(strdata[2], 4, "年")) {
					errflg = false;
				}
				/* 1月～12月 */
				for (int i=1;i<=12;i++){
					if (!this.chkRequiredOHAN(strdata[2+i], 11, String.valueOf(i)+"月")) {
						//errflg = false;
					}
				}
			} else {
				msgs.add(MSG_COL);
				errflg = false;
			}

		} else if (filetype.equals("3")) {
			// 新規獲得_媒体別費用（新規獲得CPO）
			if (strdata.length == 16) {
			} else {
				msgs.add(MSG_COL);
				errflg = false;
			}

		} else if (filetype.equals("4")) {
			// プロモ商品計画数（通店プロモ商品速報）
			if (strdata.length == 12) {
				/* 年月 */
				errflg = this.chkNonreqLDate(strdata[7], 8, "プロモーション開始日");
				/* 年月 */
				if (errflg) {
					errflg = this.chkNonreqLDate(strdata[8], 8, "プロモーション終了日");
				}
			} else {
				msgs.add(MSG_COL);
				errflg = false;
			}

		} else if (filetype.equals("5")) {
			// 商品実績_カテゴリ
			if (strdata.length == 12) {
				/* 年月 */
				errflg = this.chkRequiredLHANDate(strdata[0], 8, "適用開始日", Defines.DT_UP_FORMAT_YMD);
				/* 分類 */
				if (errflg) {
					errflg = this.chkRequiredLHAN(strdata[1], 1, "分類");
					System.out.println(strdata[1]);
					if (!"1".equals(strdata[1])){
						String msg = "分類に1以外" + MSG_END;
						msgs.add(msg);
						errflg = false;
						System.out.println(errflg);
					}
				}
				/* 項目名 */
				if (errflg) {
					errflg = this.chkRequired(strdata[2], 100, "項目名");
				}
				/* 基本 */
				if (errflg) {
					errflg = this.chkRequiredLHAN(tip.setZero(strdata[3], 2), 2, "基本");
				}
				/* 大分類 */
				if (errflg) {
					errflg = this.chkRequiredLHAN(tip.setZero(strdata[4], 2), 2, "大分類");
				}
				/* 中分類 */
				if (errflg) {
					errflg = this.chkRequiredLHAN(tip.setZero(strdata[5], 2), 2, "中分類");
				}
				/* 小分類 */
				if (errflg) {
					errflg = this.chkRequiredLHAN(tip.setZero(strdata[6], 2), 2, "小分類");
				}
				/* シリーズA */
				if (errflg) {
					errflg = this.chkRequiredLHAN(tip.setZero(strdata[7], 2), 2, "シリーズA");
				}
				/* シリーズB */
				if (errflg) {
					errflg = this.chkRequiredLHAN(tip.setZero(strdata[8], 2), 2, "シリーズB");
				}
				/* 商品コード */
				if (errflg) {
					errflg = this.chkRequiredLHAN(tip.setZero(strdata[9], 2), 2, "商品コード");
				}
				/* 商品名 */
				if (errflg) {
					errflg = this.chkNonRequired(strdata[10], 150, "商品名");
				}
				/* 並び順 */
				if (errflg) {
					errflg = this.chkRequiredOHAN(strdata[11], 3, "並び順");
				}
			} else {
				msgs.add(MSG_COL);
				errflg = false;
			}

		} else if (filetype.equals("6")) {
			// 商品実績_重点商材
			if (strdata.length == 12) {
				/* 年月 */
				errflg = this.chkRequiredLHANDate(strdata[0], 8, "適用開始日", Defines.DT_UP_FORMAT_YMD);
				/* 分類 */
				if (errflg) {
					errflg = this.chkRequiredLHAN(strdata[1], 1, "分類");
					if (!"2".equals(strdata[1])){
						String msg = "分類に2以外" + MSG_END;
						msgs.add(msg);
						errflg = false;
					}
				}
				/* 項目名 */
				if (errflg) {
					errflg = this.chkRequired(strdata[2], 100, "項目名");
				}
				/* 基本 */
				if (errflg) {
					errflg = this.chkRequiredLHAN(tip.setZero(strdata[3], 2), 2, "基本");
				}
				/* 大分類 */
				if (errflg) {
					errflg = this.chkRequiredLHAN(tip.setZero(strdata[4], 2), 2, "大分類");
				}
				/* 中分類 */
				if (errflg) {
					errflg = this.chkRequiredLHAN(tip.setZero(strdata[5], 2), 2, "中分類");
				}
				/* 小分類 */
				if (errflg) {
					errflg = this.chkRequiredLHAN(tip.setZero(strdata[6], 2), 2, "小分類");
				}
				/* シリーズA */
				if (errflg) {
					errflg = this.chkRequiredLHAN(tip.setZero(strdata[7], 2), 2, "シリーズA");
				}
				/* シリーズB */
				if (errflg) {
					errflg = this.chkRequiredLHAN(tip.setZero(strdata[8], 2), 2, "シリーズB");
				}
				/* 商品コード */
				if (errflg) {
					errflg = this.chkRequiredLHAN(tip.setZero(strdata[9], 2), 2, "商品コード");
				}
				/* 商品名 */
				if (errflg) {
					errflg = this.chkNonRequired(strdata[10], 150, "商品名");
				}
				/* 並び順 */
				if (errflg) {
					errflg = this.chkRequiredOHAN(strdata[11], 3, "並び順");
				}
			} else {
				msgs.add(MSG_COL);
				errflg = false;
			}

		} else if (filetype.equals("7")) {
			// 新規獲得_商材指定
			if (strdata.length == 12) {
				/* 年月 */
				errflg = this.chkRequiredLHANDate(strdata[0], 8, "適用開始日", Defines.DT_UP_FORMAT_YMD);
				/* 分類 */
				if (errflg) {
					errflg = this.chkRequiredLHAN(strdata[1], 1, "分類");
					if (!"3".equals(strdata[1])){
						String msg = "分類に3以外" + MSG_END;
						msgs.add(msg);
						errflg = false;
					}
				}
				/* 項目名 */
				if (errflg) {
					errflg = this.chkRequired(strdata[2], 100, "項目名");
				}
				/* 基本 */
				if (errflg) {
					errflg = this.chkRequiredLHAN(tip.setZero(strdata[3], 2), 2, "基本");
				}
				/* 大分類 */
				if (errflg) {
					errflg = this.chkRequiredLHAN(tip.setZero(strdata[4], 2), 2, "大分類");
				}
				/* 中分類 */
				if (errflg) {
					errflg = this.chkRequiredLHAN(tip.setZero(strdata[5], 2), 2, "中分類");
				}
				/* 小分類 */
				if (errflg) {
					errflg = this.chkRequiredLHAN(tip.setZero(strdata[6], 2), 2, "小分類");
				}
				/* シリーズA */
				if (errflg) {
					errflg = this.chkRequiredLHAN(tip.setZero(strdata[7], 2), 2, "シリーズA");
				}
				/* シリーズB */
				if (errflg) {
					errflg = this.chkRequiredLHAN(tip.setZero(strdata[8], 2), 2, "シリーズB");
				}
				/* 商品コード */
				if (errflg) {
					errflg = this.chkRequiredLHAN(tip.setZero(strdata[9], 2), 2, "商品コード");
				}
				/* 商品名 */
				if (errflg) {
					errflg = this.chkNonRequired(strdata[10], 150, "商品名");
				}
				/* 並び順 */
				if (errflg) {
					errflg = this.chkRequiredOHAN(strdata[11], 3, "並び順");
				}
			} else {
				msgs.add(MSG_COL);
				errflg = false;
			}

		}
		if (!errflg) {
			this.errrow = targetrow;
		}
		return errflg;
	}

}
