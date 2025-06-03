/**
 * 作成日: 2007/10/26
 */
package authentication.defines;


/**
 * メッセージクラス 各画面の上部、又はエラー画面にて出力される文字を定義
 */
public class Message {

	/** ログイン画面:ID、又はパスワードの何れかが一致しない場合に表示されるエラーメッセージ */
	public static final String ERR_AUTH_FAIL = "&nbsp;&nbsp;・ユーザーコード または パスワード が違います";

	/** 共通:セッションがタイムアウトになった際に表示されるエラーメッセージ */
	public static final String ERR_SES_TIMEOUT = "セッションがタイムアウトしました。再度ログインして下さい";

	/** 共通:画面を閲覧する権限がない際にひょうじされるエラーメッセージ */
	public static final String ERR_AUTH_INVALID = "閲覧権限がない為、表示できませんでした";

	/** 共通:送信処理を実行し、送信が成功した際に表示されるエラーメッセージ */
	public static final String ERR_PAGE_NOT_FOUND = "指定されたページが存在しません。";

	/** 共通:保存処理を実行し、保存が成功した際に表示される連絡メッセージ */
	public static final String SUP_SAVE_END = "が正常に完了しました";

	/** 共通：保存処理を実行し、保存に失敗した際に表示される連絡メッセージ */
	public static final String ERR_SAVE_END = "に失敗しました";

	/** アップロード画面：1行目の項目が正しくない際に表示されるﾒｯｾｰｼﾞ */
	public static final String UP_EER = "指定されたアップロードファイルの項目名に誤りがあります";

	/** アップロード画面：？行目が失敗した際に表示されるエラーメッセージ */
	public static final String ERR_SAVE_NO = "行目：";

	/** アップロード画面：ファイルに情報が存在しない場合 */
	public static final String UP_FILE_ERR = "アップロードする情報がありません。ファイルをお確かめ下さい";

	public static final String ERR_DOWN_FILE = "ファイルダウンロードが失敗しました";

	public static final String ERR_UP_FILE = "アップロードが失敗しました";

	/** アップロード画面：ファイルに情報が存在しない場合 */
	public static final String FILE_MSG = "　入力ファイル名：";

	/** 共通:保存処理を実行し、保存が成功した際に表示される連絡メッセージ */
	public static final String SUP_SAVE_END_UP = "のアップロードが正常に終了しました";
	public static final String ERR_SAVE_END_UP = "のアップロードに失敗しました";

	/** アップロード画面：ファイルの存在チェック */
	public static final String ERR_FILE_EXISTANCE = "指定したアップロードファイル名又はディレクトリが存在しません";

	/** アップロード画面：ファイルの存在チェック */
	public static final String ERR_MASTER_TB = "マスタ管理テーブルにデータが存在しません";

	/** アップロード画面：ファイルの存在チェック */
	public static final String ERR_MASTER_ER_FLG = "マスタ管理テーブルのエラーフラグには空白を0以上を入力ください";

	/** アップロード画面:送信処理を実行し、送信が成功した際に表示される連絡メッセージ */
	public static final String SUP_UPLOAD_END = "送信が正常に完了しました。";

	/** LDAP関連 エラー */
	public enum LDAPErr {
		INVALID_CREDENTIALS(1, new String[] { "data 52e" }, "アカウントまたはパスワードが違っています<br>確認のうえ再入力してください"),
		INVALID_PASSWORD(2, new String[] { "data 532", "data 773" }, "パスワードの有効期限が切れているため、ログインできません。"),
		INVALID_USER(3, new String[] { "data 533", "data 701", "data 775" }, "アカウントが無効になっているため、ログインできません。"),
		NOT_FOUND_USER(4, new String[] { "data 525" }, "存在しないアカウントです。アカウントを確認し、再入力してください。"),
		OTHER(5, new String[] {}, "認証エラーのため、ログインできません。");

		private final int idx;
		private final String[] vals;
		private final String txt;

		/**
		 * 初期化
		 *
		 * @param idx インデックス
		 * @param vals 値
		 * @param txt 表示名称
		 */
		private LDAPErr(int idx, String[] vals, String txt) {
			this.idx = idx;
			this.vals = vals;
			this.txt = txt;
		}

		/** @return idx インデックス */
		public int getIdx() {
			return idx;
		}

		/** @return vals 値 */
		public String[] getVals() {
			return vals;
		}

		/** @return txt 表示名称 */
		public String getTxt() {
			return txt;
		}
	}
}
