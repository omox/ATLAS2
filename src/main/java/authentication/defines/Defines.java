/*
 * 作成日: 2006/08/04
 *
 */
package authentication.defines;

/**
 * 定数クラス <br />
 * システムで使用する定数をまとめて定義。 <br />
 */
public final class Defines {

	/** クロスコンテキストに利用するリクエスト情報
	 *
	 */
	// ユーザーID
	public static final String ID_REQUEST_USER_ID			=	"uid";
	// ユーザーカスタムプロパティ
	public static final String ID_REQUEST_CUSTOM_USER		=	"cu";
	// グループカスタムプロパティ
	public static final String ID_REQUEST_CUSTOM_GROUP		=	"cg";
	// ロールカスタムプロパティ
	public static final String ID_REQUEST_CUSTOM_POS		=	"cp";
	// 分類カスタムプロパティ
	public static final String ID_REQUEST_CUSTOM_SIDE		=	"cs";
	// レポートカスタムプロパティ
	public static final String ID_REQUEST_CUSTOM_REPORT		=	"cr";
	// レポートJSP
	public static final String ID_REQUEST_JSP_REPORT		=	"jsp";
	// レポートタイトル
	public static final String ID_REQUEST_TITLE_REPORT		=	"title";

	// 親要素からの引き継ぎ情報
	public static final String ID_REQUEST_SEND_PARAM		=	"sendParam";

	/** JSPファイル名:エラー画面 */
	public static final String STR_JSP_ERROR = "/frame/jsp/Error.jsp";

	/** JSPファイルパス:ダッシュボード画面 */
	public static final String STR_JSP_DB_PATH = "/jsp/dashboard";

	/** JSPファイル名:時間外画面 */
	public static final String STR_JSP_WORK = "/frame/jsp/CloseTime.jsp";

	/** JSPファイル名:ファイルアップロード画面 */
	public static final String STR_JSP_UPLOAD = "/jsp/Upload.jsp";

	/** システム開始時間 HHMMSS */
	public static final String STR_SYS_START = "080000";
	/** システム終了時間 HHMMSS */
	public static final String STR_SYS_END = "235900";

	/** セッション名:ユーザー情報 */
	public static final String STR_SES_LOGINUSER = "User";

	/** セッション名:パスワード有効期限 */
	public static final String STR_SES_PASS_TERM_MSG = "passwordTermMsg";

	/** フォーム名:画面メッセージ */
	public static final String STR_FRM_MSG = "formMsg";
	/** フォーム名:画面メッセージタイプ */
	public static final String STR_FRM_MSGTYPE = "msg";

	/** セッション名:ダッシュボード画面 ReportName */
	public static final String SES_DB_REP_NAME = "Ses_Report_Name";
	/** セッション名:ダッシュボード画面 YM */
	public static final String SES_DB_YM = "Ses_YM";
	/** セッション名:ダッシュボード画面 REPORT_TYPE */
	public static final String SES_DB_REP_TYPE = "Ses_Report_Type";

	/** フォームの値:画面メッセージ表示タイプ エラー */
	public static final String STR_VAL_MSGTYPE_ERR = "errmsg";

	/** システム管理者情報:ID */
	public static final String STR_ADMIN_ID = "sysadmin";
	/** システム管理者情報:ﾊﾟｽﾜｰﾄﾞ */
	public static final String STR_ADMIN_PASS = "atlaspass";
	/** システム管理者情報:ユーザー名 */
	public static final String STR_ADMIN_NAME = "システム管理者";
	/** システム管理者情報:メニュー表示用権限 */
	public static final String STR_ADMIN_AUTH = "99";

	/** TermDateFormat */
	public static final String DT_PW_FORMAT_DISP = "yyyy/MM/dd";
	/** TermDateFormat */
	public static final String DT_PW_FORMAT_SAVE = "yyyyMMdd";
	/** TermDateFormat */
	public static final String DT_FORMAT_YM = "yyyyMM";
	/** TermDateFormat */
	public static final String DT_UP_FORMAT_YMD = "yyyyMMdd";
	/** TermDateFormat */
	public static final String DT_UP_FORMAT_YM = "yyyyMM";

	/** パスワードの有効期限が切れている場合のメッセージ */
	public static final String MSG_LUSER_PASSOUT = "パスワードの有効期限が切れています。";


	/** セッション名:送受信ファイル検索用文字列 */
	public static final String STR_SES_FILE_TYPE = "load_file_type";
	/** Download・Upload ファイルパス Upload*/
	public static final String UPLOAD_FILE_PATH = "/updata";
	/** Download・Upload ファイルパス Download*/
	public static final String DOWNLOAD_FILE_PATH = "/downdata";
	/** Download・Upload ファイル名称 */
	public static final String UPLOAD_FILE_COMPANY = "/company.csv";
	/** Download・Upload ファイル名称 */
	public static final String UPLOAD_FILE_KAWASE = "/kawase.csv";
	/** Download・Upload ファイル名称 */
	public static final String UPLOAD_FILE_KAWASE2 = "/kawase2.csv";
	/** Download・Upload ファイル名称 */
	public static final String UPLOAD_FILE_TEL = "/tel.csv";
	/** Download・Upload ファイル名称 */
	public static final String UPLOAD_FILE_KESHOU = "/keshou.csv";
	/** Download・Upload ファイル名称 */
	public static final String	UPLOAD_FILE_URIMOK			= "/uri_moku.csv";

	/** Download・Upload ファイル名称 年間計画情報 */
	public static final String UPLOAD_FILE_PLAN_Y = "/nenkankeikaku.csv";
	/** Download・Upload ファイル名称 媒体別費用 */
	public static final String UPLOAD_FILE_BAITAI_HIYOU = "/baitaihiyou.csv";
	/** Download・Upload ファイル名称 プロモ計画情報 */
	public static final String UPLOAD_FILE_PLAN_PROMO = "/promokeikaku.csv";
	/** Download・Upload ファイル名称 カテゴリ */
	public static final String UPLOAD_FILE_CATEGORY = "/category.csv";
	/** Download・Upload ファイル名称 重点商材 */
	public static final String UPLOAD_FILE_JUTEN_SHOZAI = "/jutensyozai.csv";
	/** Download・Upload ファイル名称 初回購入分類 */
	public static final String UPLOAD_FILE_KOUNYU_1ST = "/kounyubunrui.csv";
}
