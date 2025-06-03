/*
 * atlas - MenuValidation.java.
 *
 * @since 2012/04/25
 * @author eatone
 */
package authentication.validation;


/**
 * メニュー画面でのチェック処理<br />
 * ATLASでは、メニュー画面にてお知らせ情報を更新する。<br/>
 *
 * @author eatone
 *
 */
public class MenuValidation  extends Validation {


	/**
	 * お知らせマスタ：登録ボタンのチェック
	 *
	 * @param value お知らせ情報
	 * @param list
	 *
	 * @return エラーなし：true、あり：false
	 */
	public boolean entry(String value) {
		boolean errflg = true;
		/* お知らせ:桁数 */
		if (!super.chkNonreqL(value, LENGTH_ZEN_SYS_INFO, LENGTH_HALF_SYS_INFO, MSG_INFO)) {
			errflg = false;
		}
		return errflg;
	}

}
