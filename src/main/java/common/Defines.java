/*
 * 作成日: 2006/08/04
 *
 */
package common;

/**
 * 定数クラス <br />
 * システムで使用する定数をまとめて定義。 <br />
 */
public final class Defines {
	/** JNDI名 */
	public final static String STR_JNDI_DS = "java:comp/env/jdbc/atlas";

	/** JSPファイル名:ログイン画面 */
	public final static String STR_JSP_LOGIN = "/jsp/Error.jsp";
	/** JSPファイル名:メニュー画面 */
	public final static String STR_JSP_MENU = "/jsp/Menu.jsp";
	/** JSPファイル名:レポート画面 */
	public final static String STR_JSP_RP = "/jsp/Report.jsp";
	/** JSPファイル名:エラー画面 */
	public final static String STR_JSP_ERROR = "/jsp/Error.jsp";
	/** JSPファイル名:エラー画面 */
	public final static String	STR_EXCEL_ERROR = "/jsp/ExcelOutputError.jsp";

	/**
	 * クロスコンテキストに利用するリクエスト情報
	 *
	 */
	// ユーザーID
	public final static String ID_REQUEST_USER_ID			=	"uid";
	// ユーザーカスタムプロパティ
	public final static String ID_REQUEST_CUSTOM_USER		=	"cu";
	// グループカスタムプロパティ
	public final static String ID_REQUEST_CUSTOM_GROUP	=	"cg";
	// ロールカスタムプロパティ
	public final static String ID_REQUEST_CUSTOM_POS		=	"cp";
	// 分類カスタムプロパティ
	public final static String ID_REQUEST_CUSTOM_SIDE		=	"cs";
	// レポートカスタムプロパティ
	public final static String ID_REQUEST_CUSTOM_REPORT	=	"cr";
	// レポートJSP
	public final static String ID_REQUEST_JSP_REPORT		=	"jsp";
	// レポートタイトル
	public final static String ID_REQUEST_TITLE_REPORT	=	"title";
	// レポート番号
	public final static String ID_REQUEST_REPORT_NO		=	"DisplayReportNo";

	// 親要素からの引き継ぎ情報
	public final static String ID_REQUEST_SEND_PARAM		=	"sendParam";

	/** JSPファイル名:時間外画面 */
	public final static String STR_JSP_CLOSE = "/jsp/Close.jsp";

	/** システム開始時間 HHMMSS */
	public final static String STR_SYS_START = "075000";
	/** システム終了時間 HHMMSS */
	public final static String STR_SYS_END = "211000";

	/** JSPファイル名:メンテナンス画面 */
	public final static String STR_JSP_WORK = "/jsp/Work.jsp";

	/** フォーム名:画面メッセージ */
	public final static String STR_FRM_MSG = "formMsg";
	/** フォーム名:画面メッセージタイプ */
	public final static String STR_FRM_MSGTYPE = "msg";

	/** フォームの値:画面メッセージ表示タイプ エラー */
	public final static String STR_VAL_MSGTYPE_ERR = "errmsg";


	/** セッション名:店舗情報保持 */
	public final static String STR_SES_TENPO = "tenpo_sel";
	/** セッション名:店舗情報保持 */
	public final static String STR_SES_TENPOTXT = "tenpotxt_sel";

	/** セッション名:輸出情報保持 */
	public final static String STR_SES_YUSHUTU = "yushutu_sel";
	/** セッション名:輸出情報保持 */
	public final static String STR_SES_YUSHUTUTXT = "yushututxt_sel";

	/** セッション名:ブランド情報保持 */
	public final static String STR_SES_BRAND = "brand_sel";
	/** セッション名:ブランド情報保持 */
	public final static String STR_SES_BRANDTXT = "brandtxt_sel";

	/** セッション名:関連会社情報保持 */
	public final static String STR_SES_COMPANY = "company_sel";
	/** セッション名:関連会社情報保持 */
	public final static String STR_SES_COMPANYTXT = "companytxt_sel";

	/** セッション名:表示項目情報保持 */
	public final static String STR_SES_VIEW_ITEM_JUN = "viewitemjun";
	/** セッション名:表示項目情報保持 */
	public final static String STR_SES_VIEW_ITEM_SHU = "viewitemshu";
	/** セッション名:表示項目情報保持 */
	public final static String STR_SES_VIEW_ITEM_TENPO_JUN = "viewitemtenpojun";
	/** セッション名:表示項目情報保持 */
	public final static String STR_SES_VIEW_ITEM_TENPO_SHU = "viewitemtenposhu";
	/** セッション名:表示項目情報保持 */
	public final static String STR_SES_VIEW_ITEM_TENPO_CHO = "viewitemtenpocho";



}
