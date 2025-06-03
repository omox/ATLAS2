/*******************************************************************************
 * Copyright (c) 2001, 2007 IBM Corporation and others. All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 * 作成日: 2007/10/26
 */
package authentication.validation;

import java.text.SimpleDateFormat;
import java.util.Date;

import authentication.defines.Defines;
import authentication.parser.LoginUser;
import authentication.util.CmnDate;

/**
 * ログイン画面でのチェック処理 <br />
 */
public class LoginValidation extends Validation {

	/**
	 * ログイン画面：ログインボタンのチェック
	 * @param usr    入力ユーザー
	 * @param usrs   リスト
	 * @return エラーなし：true、あり：false
	 */
	public boolean entry(String id, String pass) {

		boolean errflg = true;
		/**
		 * 入力チェック
		 */

		/* ID:必須,桁数,半角英数字 */
		if (!super.chkRequiredLHAN(id, LENGTH_USER_ID, MSG_USER_ID)) {
			errflg = false;
		}

		/* PASS:必須,桁数,半角英数字 */
		if (!super.chkRequiredLHAN(pass, LENGTH_PASSWORD, MSG_PASSWORD)) {
			errflg = false;
		}

		return errflg;
	}

	/**
	 * ログイン画面：ログインボタンのチェック
	 * 
	 * @param id 入力ユーザー
	 * @param pass 入力パスワード
	 * @return エラーなし：true、あり：false
	 */
	public boolean entryLdap(String id, String pass) {

		boolean errflg = true;
		/**
		 * 入力チェック
		 */

		/* ID:必須 */
		if (!super.chkRequired_notinput(id, MSG_USER_ID)) {
			errflg = false;
		}

		/* PASS:必須 */
		if (!super.chkRequired_notinput(pass, MSG_PASSWORD)) {
			errflg = false;
		}

		return errflg;
	}

	/**
	 * ログイン画面にアクセスした際の時間判定 closeTime
	 * 
	 * @param fromData
	 * @param toData
	 * @return
	 */
	public String closeTime(String fromData, String toData) {

		// 現在のシステム時間取得
		CmnDate hhmmss = new CmnDate();

		String jsppage = "CloseTime";
		try {

			if (super.chkTime(fromData) || super.chkTime(toData)) {
				jsppage = "Page_Error";
			}

			int strhhmmss = Integer.parseInt(hhmmss.getNowTimeHHMM().toString());
			int intfromData = Integer.parseInt(fromData);
			int inttoData = Integer.parseInt(toData);

			// from <= systime <= to はOK
			if (intfromData <= strhhmmss && strhhmmss <= inttoData) {
				jsppage = "Loginform";
			}

		} catch (Exception e) {
			e.printStackTrace();
			jsppage = "Page_Error";
			System.out.println("closeTime Exception");
		}
		return jsppage;
	}

	/**
	 * パスワードの有効期限確認
	 * @param usr
	 * @return
	 */
	public boolean IsPasswordTrim(LoginUser usr) {
		boolean errflg = true;

		// 管理者確認
		if (!usr.hasAdmin()) {

			// 日付確認
			if (this.isDate(usr.getPwdate(), Defines.DT_PW_FORMAT_SAVE)) {

				Date now = new Date();
				String snow = new SimpleDateFormat(Defines.DT_PW_FORMAT_SAVE).format(now);

				// 有効期限確認
				if (usr.getPwdate(Defines.DT_PW_FORMAT_SAVE).toString().compareTo(snow) < 0) {
					errflg = false;
				}
			}
		}
		return errflg;
	}

}