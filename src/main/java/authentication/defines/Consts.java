/*
 * 作成日: 2007/11/08
 */
package authentication.defines;

/**
 * 定数クラス システムで使用する定数をまとめて定義
 */
public final class Consts {

	/** セッション名:ユーザー情報 */
	public static final String	STR_SES_LOGINUSER			= "LoginUser";
	/** セッション名:ユーザー情報 */
	public static final String	STR_SES_LOGINPOS			= "LoginUserPosition";
	/** セッション名:ユーザー情報 */
	public static final String	STR_SES_LOGINATH			= "LoginUserAuth";

	/** 開発用ユーザー */
	public static final String	DEVELOPMENT_USER			= "development_user";
	public static final String	DEVELOPMENT_PASS			= "pass";

	/** システム管理者用番号 */
	public static final int		SYSTEM_MASTER				= -1;
	/** 一般用番号 */
	public static final int		USER_NO 					= -11;

	public static final int 	SYSTEM_MENTENANCE			= -1;

	/** セッション名:メニュー分類 */
	public static final String	STR_SES_REPSIDE				= "Menu";
	/** セッション名:メニュー分類(マスタ) */
	public static final String	STR_SES_MSTSIDE				= "Master";

	/** セッション名:メニュー分類(マスタ) */
	public static final String	STR_SES_REPORT_SIDE			= "DisplayReportSide";
	/** セッション名:メニュー分類(マスタ) */
	public static final String	STR_SES_REPORT_NO			= "DisplayReportNo";
	/** セッション名:メニュー分類(マスタ) */
	public static final String	STR_SES_REPORT_SIDE_ARRAY	= "ReportSide_array";
	/** セッション名:メニュー分類(マスタ) */
	public static final String	STR_SES_REPORT_NO_ARRAY		= "ReportNo_array";

	/** リクエスト名:選択レコード */
	public static final String	STR_REQ_REC					= "SelectedRecord";
	/** セッション名:グリッドリスト */
	public static final String	STR_SES_GRD					= "GridData";
	public static final String	STR_SES_GRD2				= "GridData2";
	public static final String	STR_SES_GRD3				= "GridData3";
	public static final String	STR_SES_GRD4				= "GridData4";

	public static final String	STR_SES_GRD5				= "GridData5";
	public static final String	STR_SES_GRD6				= "GridData6";

	/** アップデート */
	public static final String	UPD_DETA_LIST				= "UPD_LIST";

	/** レポートメンテナンスのタイトル */
	public static final String	STR_SES_TITLE				= "Title";

	/** ヘッダー表示タイプ(メニュー、ログオフのリンクボタン表示非表示) */
	public static final String	STR_HEAD_TYPE				= "headtype";
	/** メニュー、ログオフのリンクボタン表示 */
	public static final String	STR_HEAD_TYPE_ON			= "on";

	/** URLエンコード・URLデコードする際の文字エンコード */
	public static final String	ENCODE						= "UTF-8";

	/***/
	public static final String	SQL_MESSAGE_IN				= "登録";
	/** パスワード変更画面 */
	public static final String	SQL_MESSAGE_CHENGE			= "変更";

	public static final String	SQL_MESSAGE_DELETE			= "削除";

	/** タイムオフ画面 */
	public static final String	FROM_DATA					= "FROM_DATA";
	public static final String	TO_DATA						= "TO_DATA";
	public static final String	CLOSE_TIME					= "CloseTime";
	public static final String	LOGIN_FORM					= "Loginform";

	/** お知らせ */
	public static final int		INFO_FREE_CD				= -99;
	public static final String	INFO_FREE_NM				= "お知らせ_ログイン画面";

	public static final String	INFO_ALWAYS					= "1";

}
